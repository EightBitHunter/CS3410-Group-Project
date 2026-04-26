package project;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

	private TableManager tableManager;
	private int nextPartyId = 1;
	private int nextTableNumber = 11;

	private final FlowPane tableFloor = new FlowPane();
	private final ListView<Party> waitListView = new ListView<>();

	private final Label statusLabel = new Label("Host stand ready.");
	private final Label selectedTableLabel = new Label("No tables selected");
	private final Label selectedTableInfoLabel = new Label("Select open tables to seat a party, or select an occupied table group to free it.");

	private final Label openCountLabel = new Label();
	private final Label occupiedCountLabel = new Label();
	private final Label waitCountLabel = new Label();

	private final Set<Integer> selectedTableNumbers = new HashSet<>();

	@Override
	public void start(Stage stage) {
		tableManager = new TableManager();

		for (int i = 0; i < 10; i++) {
			tableManager.addTable(new Table(i + 1, 4));
		}

		Label titleLabel = new Label("Restaurant Host Stand");
		titleLabel.getStyleClass().add("app-title");

		Label subtitleLabel = new Label("Manage tables, parties, waitlist, and seating assignments");
		subtitleLabel.getStyleClass().add("app-subtitle");

		HBox summaryBar = new HBox(20, openCountLabel, occupiedCountLabel, waitCountLabel);
		openCountLabel.getStyleClass().add("summary-card");
		occupiedCountLabel.getStyleClass().add("summary-card");
		waitCountLabel.getStyleClass().add("summary-card");
		summaryBar.setAlignment(Pos.CENTER_LEFT);

		VBox topSection = new VBox(6, titleLabel, subtitleLabel, summaryBar, statusLabel);		topSection.setPadding(new Insets(0, 0, 10, 0));

		tableFloor.setHgap(12);
		tableFloor.setVgap(12);
		tableFloor.setPadding(new Insets(10));
		tableFloor.setPrefWrapLength(760);

		VBox floorContent = new VBox(10, createLegend(), tableFloor);
		floorContent.setPadding(new Insets(10));
		floorContent.getStyleClass().add("panel");

		ScrollPane floorScrollPane = new ScrollPane(floorContent);
		floorScrollPane.setFitToWidth(false);
		floorScrollPane.setFitToHeight(false);
		floorScrollPane.setPannable(true);
		floorScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		floorScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		floorScrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

		TextField partySizeField = new TextField();
		partySizeField.setPromptText("Party size");

		Button addPartyButton = new Button("Add Party");
		addPartyButton.setMaxWidth(Double.MAX_VALUE);
		addPartyButton.setOnAction(e -> {
			String text = partySizeField.getText().trim();

			if (text.isEmpty()) {
				statusLabel.setText("Enter a party size.");
				return;
			}

			try {
				int size = Integer.parseInt(text);

				if (size <= 0) {
					statusLabel.setText("Party size must be greater than 0.");
					return;
				}

				Party party = new Party(nextPartyId++, size);
				String result;

				List<Integer> openSelections = getSelectedOpenTableNumbers();
				if (!openSelections.isEmpty()) {
					result = tableManager.seatPartyAtTables(party, openSelections);
				} else {
					result = tableManager.initializeParty(party);
				}

				statusLabel.setText(result);
				partySizeField.clear();
				clearSelection();
				refreshUI();
			} catch (NumberFormatException ex) {
				statusLabel.setText("Party size must be a number.");
			}
		});

		Button seatWaitlistPartyButton = new Button("Seat Selected Waitlist Party");
		seatWaitlistPartyButton.setMaxWidth(Double.MAX_VALUE);
		seatWaitlistPartyButton.setOnAction(e -> {
			Party selectedParty = waitListView.getSelectionModel().getSelectedItem();
			List<Integer> openSelections = getSelectedOpenTableNumbers();

			if (selectedParty == null) {
				statusLabel.setText("Select a waitlist party first.");
				return;
			}

			if (openSelections.isEmpty()) {
				statusLabel.setText("Select one or more open tables first.");
				return;
			}

			String result = tableManager.seatWaitlistedPartyAtTables(selectedParty, openSelections);
			statusLabel.setText(result);
			clearSelection();
			refreshUI();
		});

		Button removeWaitlistPartyButton = new Button("Remove Selected Waitlist Party");
		removeWaitlistPartyButton.setMaxWidth(Double.MAX_VALUE);
		removeWaitlistPartyButton.setOnAction(e -> {
			Party selectedParty = waitListView.getSelectionModel().getSelectedItem();

			if (selectedParty == null) {
				statusLabel.setText("Select a waitlist party first.");
				return;
			}

			String result = tableManager.removeFromWaitList(selectedParty);
			statusLabel.setText(result);
			refreshUI();
		});

		TextField tableSeatsField = new TextField();
		tableSeatsField.setPromptText("Seats for new table");

		Button addTableButton = new Button("Add Table");
		addTableButton.setMaxWidth(Double.MAX_VALUE);
		addTableButton.setOnAction(e -> {
			String text = tableSeatsField.getText().trim();

			if (text.isEmpty()) {
				statusLabel.setText("Enter the number of seats for the new table.");
				return;
			}

			try {
				int seats = Integer.parseInt(text);

				if (seats <= 0) {
					statusLabel.setText("Table seats must be greater than 0.");
					return;
				}

				Table newTable = new Table(nextTableNumber++, seats);
				tableManager.addTable(newTable);

				tableSeatsField.clear();
				statusLabel.setText("Added Table " + newTable.getTblNum() + " with " + seats + " seats.");
				refreshUI();

			} catch (NumberFormatException ex) {
				statusLabel.setText("Table seats must be a number.");
			}
		});

		Button deleteTableButton = new Button("Delete Selected Open Table");
		deleteTableButton.setMaxWidth(Double.MAX_VALUE);
		deleteTableButton.setOnAction(e -> {
			List<Integer> openSelections = getSelectedOpenTableNumbers();

			if (openSelections.isEmpty()) {
				statusLabel.setText("Select an open table to delete.");
				return;
			}

			if (openSelections.size() > 1) {
				statusLabel.setText("Please select only one open table to delete.");
				return;
			}

			int tableNumber = openSelections.get(0);
			String result = tableManager.deleteOpenTable(tableNumber);

			statusLabel.setText(result);
			clearSelection();
			refreshUI();
		});

		Button freeSelectedButton = new Button("Free Selected Table Group");
		freeSelectedButton.setMaxWidth(Double.MAX_VALUE);
		freeSelectedButton.setOnAction(e -> {
			List<Integer> occupiedSelections = getSelectedOccupiedTableNumbers();

			if (occupiedSelections.isEmpty()) {
				statusLabel.setText("Select an occupied table first.");
				return;
			}

			String result = tableManager.freeTable(occupiedSelections.get(0));
			statusLabel.setText(result);
			clearSelection();
			refreshUI();
		});

		Button clearSelectionButton = new Button("Clear Selection");
		clearSelectionButton.setMaxWidth(Double.MAX_VALUE);
		clearSelectionButton.setOnAction(e -> {
			clearSelection();
			refreshUI();
			statusLabel.setText("Selection cleared.");
		});

		Label controlsHeader = new Label("Host Controls");
		controlsHeader.getStyleClass().add("section-header");

		Label detailsHeader = new Label("Selected Tables");
		detailsHeader.getStyleClass().add("section-header");

		VBox selectedTableBox = new VBox(6, selectedTableLabel, selectedTableInfoLabel);
		selectedTableBox.setPadding(new Insets(10));
		selectedTableBox.setStyle(
				"-fx-background-color: white;" +
						"-fx-border-color: #d0d0d0;" +
						"-fx-border-radius: 8;" +
						"-fx-background-radius: 8;"
		);

		Label waitHeader = new Label("Waitlist");
		waitHeader.getStyleClass().add("section-header");

		waitListView.setPrefHeight(250);
		waitListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		VBox rightPanel = new VBox(
				12,
				controlsHeader,
				new Label("Party Size"),
				partySizeField,
				addPartyButton,
				seatWaitlistPartyButton,
				removeWaitlistPartyButton,

				new Label("Table Setup"),
				tableSeatsField,
				addTableButton,
				deleteTableButton,

				detailsHeader,
				selectedTableBox,
				freeSelectedButton,
				clearSelectionButton,
				waitHeader,
				waitListView
		);
		rightPanel.setPadding(new Insets(10));
		rightPanel.setPrefWidth(320);
		rightPanel.setMinWidth(320);
		rightPanel.setMaxWidth(320);
		rightPanel.getStyleClass().add("panel");

		BorderPane root = new BorderPane();
		root.setTop(topSection);
		root.setCenter(floorScrollPane);
		root.setRight(rightPanel);
		root.setPadding(new Insets(15));

		BorderPane.setMargin(rightPanel, new Insets(0, 0, 0, 15));
		VBox.setVgrow(floorScrollPane, Priority.ALWAYS);

		refreshUI();

		Scene scene = new Scene(root, 1280, 760);
		scene.getStylesheets().add(Main.class.getResource("/project/style.css").toExternalForm());
		stage.setTitle("Restaurant Host Stand");
		stage.setScene(scene);
		stage.setMinWidth(1000);
		stage.setMinHeight(650);
		stage.show();
	}

	private HBox createLegend() {
		VBox openLegendBox = new VBox();
		openLegendBox.setPrefSize(20, 20);
		openLegendBox.setStyle(
				"-fx-background-color: white;" +
						"-fx-border-color: #1f9d55;" +
						"-fx-border-width: 3;" +
						"-fx-border-radius: 6;" +
						"-fx-background-radius: 6;"
		);

		VBox occupiedLegendBox = new VBox();
		occupiedLegendBox.setPrefSize(20, 20);
		occupiedLegendBox.setStyle(
				"-fx-background-color: white;" +
						"-fx-border-color: #d64545;" +
						"-fx-border-width: 3;" +
						"-fx-border-radius: 6;" +
						"-fx-background-radius: 6;"
		);

		VBox selectedLegendBox = new VBox();
		selectedLegendBox.setPrefSize(20, 20);
		selectedLegendBox.setStyle(
				"-fx-background-color: #eaf2ff;" +
						"-fx-border-color: #2563eb;" +
						"-fx-border-width: 3;" +
						"-fx-border-radius: 6;" +
						"-fx-background-radius: 6;"
		);

		VBox linkedLegendBox = new VBox();
		linkedLegendBox.setPrefSize(20, 20);
		linkedLegendBox.setStyle(
				"-fx-background-color: #fff4e5;" +
						"-fx-border-color: #f59e0b;" +
						"-fx-border-width: 3;" +
						"-fx-border-radius: 6;" +
						"-fx-background-radius: 6;"
		);

		HBox openLegend = new HBox(6, openLegendBox, new Label("Open"));
		HBox occupiedLegend = new HBox(6, occupiedLegendBox, new Label("Occupied"));
		HBox selectedLegend = new HBox(6, selectedLegendBox, new Label("Selected"));
		HBox linkedLegend = new HBox(6, linkedLegendBox, new Label("Linked Party Group"));

		HBox legend = new HBox(20, openLegend, occupiedLegend, selectedLegend, linkedLegend);
		legend.setAlignment(Pos.CENTER_LEFT);
		return legend;
	}

	private VBox createTableCard(Table table) {
		boolean isOpen = tableManager.isTableOpen(table.getTblNum());
		boolean isSelected = selectedTableNumbers.contains(table.getTblNum());

		Label tableNumberLabel = new Label("Table " + table.getTblNum());
		tableNumberLabel.setStyle(
				"-fx-font-size: 16px;" +
						"-fx-font-weight: bold;" +
						"-fx-text-fill: black;"
		);

		Label seatsLabel = new Label(table.getSize() + " seats");
		seatsLabel.setStyle("-fx-text-fill: black;");

		Label statusTextLabel = new Label(isOpen ? "Available" : "Occupied");
		statusTextLabel.setStyle("-fx-text-fill: black;");

		String partyText = table.getCurrentPartyId() == -1
				? ""
				: "Party " + table.getCurrentPartyId();

		Label partyLabel = new Label(partyText);
		partyLabel.setStyle("-fx-text-fill: black;");

		VBox card = new VBox(8, tableNumberLabel, seatsLabel, statusTextLabel, partyLabel);
		card.getStyleClass().add("table-card");

		if (isSelected) {
			card.getStyleClass().add("selected-table");
		} else if (isOpen) {
			card.getStyleClass().add("open-table");
		} else {
			card.getStyleClass().add("occupied-table");
		}
		card.setAlignment(Pos.CENTER);
		card.setPadding(new Insets(12));
		card.setPrefSize(135, 115);

		String borderColor = isOpen ? "#1f9d55" : "#d64545";
		String backgroundColor = isSelected ? "#eaf2ff" : "white";
		String borderWidth = isSelected ? "4" : "3";

		card.setOnMouseClicked(e -> {
			if (selectedTableNumbers.contains(table.getTblNum())) {
				selectedTableNumbers.remove(table.getTblNum());
			} else {
				selectedTableNumbers.add(table.getTblNum());
			}

			refreshUI();
			statusLabel.setText("Updated table selection.");
		});

		return card;
	}

	private VBox createPartyGroupNode(List<Table> groupTables) {
		int partyId = groupTables.get(0).getCurrentPartyId();
		boolean isSelected = false;
		int totalSeats = 0;

		HBox tablesRow = new HBox(8);
		tablesRow.setAlignment(Pos.CENTER);

		for (Table table : groupTables) {
			totalSeats += table.getSize();
			if (selectedTableNumbers.contains(table.getTblNum())) {
				isSelected = true;
			}
			tablesRow.getChildren().add(createTableCard(table));
		}

		Label header = new Label("Linked Party " + partyId + " • " + totalSeats + " seats");
		header.setStyle(
				"-fx-font-size: 14px;" +
						"-fx-font-weight: bold;" +
						"-fx-text-fill: black;"
		);

		VBox groupBox = new VBox(8, header, tablesRow);
		groupBox.setPadding(new Insets(10));
		groupBox.setAlignment(Pos.CENTER);
		groupBox.setStyle(
				"-fx-background-color: " + (isSelected ? "#eef5ff" : "#fff9ef") + ";" +
						"-fx-border-color: " + (isSelected ? "#2563eb" : "#f59e0b") + ";" +
						"-fx-border-width: 3;" +
						"-fx-border-radius: 14;" +
						"-fx-background-radius: 14;"
		);

		groupBox.setOnMouseClicked(e -> {
			boolean allSelected = true;
			for (Table table : groupTables) {
				if (!selectedTableNumbers.contains(table.getTblNum())) {
					allSelected = false;
					break;
				}
			}

			if (allSelected) {
				for (Table table : groupTables) {
					selectedTableNumbers.remove(table.getTblNum());
				}
			} else {
				for (Table table : groupTables) {
					selectedTableNumbers.add(table.getTblNum());
				}
			}

			refreshUI();
			statusLabel.setText("Updated linked party selection.");
			e.consume();
		});

		return groupBox;
	}

	private List<Integer> getSelectedOpenTableNumbers() {
		List<Integer> result = new ArrayList<>();

		for (int tableNum : selectedTableNumbers) {
			if (tableManager.isTableOpen(tableNum)) {
				result.add(tableNum);
			}
		}

		return result;
	}

	private List<Integer> getSelectedOccupiedTableNumbers() {
		List<Integer> result = new ArrayList<>();

		for (int tableNum : selectedTableNumbers) {
			if (!tableManager.isTableOpen(tableNum)) {
				result.add(tableNum);
			}
		}

		return result;
	}

	private void clearSelection() {
		selectedTableNumbers.clear();
	}

	private void refreshUI() {
		tableFloor.getChildren().clear();

		for (Table table : tableManager.getOpenTableList()) {
			tableFloor.getChildren().add(createTableCard(table));
		}

		for (List<Table> group : tableManager.getOccupiedTableGroups()) {
			if (group.size() == 1) {
				tableFloor.getChildren().add(createTableCard(group.get(0)));
			} else {
				tableFloor.getChildren().add(createPartyGroupNode(group));
			}
		}

		waitListView.getItems().setAll(tableManager.getWaitListParties());

		int openCount = tableManager.getOpenTableCount();
		int occupiedCount = tableManager.getOccupiedTableCount();
		int waitCount = tableManager.getWaitListCount();

		openCountLabel.setText("Open Tables: " + openCount);
		occupiedCountLabel.setText("Occupied Tables: " + occupiedCount);
		waitCountLabel.setText("Waiting Parties: " + waitCount);

		if (selectedTableNumbers.isEmpty()) {
			selectedTableLabel.setText("No tables selected");
			selectedTableInfoLabel.setText("Select open tables to seat a party, or select an occupied table group to free it.");
			return;
		}

		int totalSeats = 0;
		int openSelected = 0;
		int occupiedSelected = 0;
		Set<Integer> linkedPartyIds = new HashSet<>();

		for (int tableNum : selectedTableNumbers) {
			Table table = tableManager.getTable(tableNum);
			if (table != null) {
				totalSeats += table.getSize();
				if (tableManager.isTableOpen(tableNum)) {
					openSelected++;
				} else {
					occupiedSelected++;
					if (table.getCurrentPartyId() != -1) {
						linkedPartyIds.add(table.getCurrentPartyId());
					}
				}
			}
		}

		selectedTableLabel.setText(selectedTableNumbers.size() + " table(s) selected");
		selectedTableInfoLabel.setText(
				"Total seats: " + totalSeats +
						"\nOpen selected: " + openSelected +
						"\nOccupied selected: " + occupiedSelected +
						"\nParty groups selected: " + linkedPartyIds.size()
		);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
