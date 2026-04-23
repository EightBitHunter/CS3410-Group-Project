package project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TableManager {
	private MyLinkedList openTbls;
	private MyLinkedList closeTbls;
	private MyDynamicQueue waitList;

	public TableManager() {
		openTbls = new MyLinkedList();
		closeTbls = new MyLinkedList();
		waitList = new MyDynamicQueue();
	}

	public void addTable(Table t) {
		openTbls.add(t);
	}

	public String initializeParty(Party p) {
		List<Table> bestTables = findBestTableCombination(p.getSize());

		if (bestTables.isEmpty()) {
			waitList.enqueue(p);
			return p + " has been added to waitlist.";
		}

		occupyTables(p, bestTables);
		return buildSeatMessage(p, bestTables, false);
	}

	public String seatPartyAtTables(Party p, List<Integer> tableNumbers) {
		List<Table> selectedTables = getOpenTablesFromNumbers(tableNumbers);

		if (selectedTables.isEmpty()) {
			return "Select one or more open tables first.";
		}

		int totalSeats = getTotalSeats(selectedTables);
		if (totalSeats < p.getSize()) {
			return "Selected tables only have " + totalSeats + " seats, but " + p + " needs " + p.getSize() + ".";
		}

		occupyTables(p, selectedTables);
		return buildSeatMessage(p, selectedTables, false);
	}

	public String seatWaitlistedPartyAtTables(Party party, List<Integer> tableNumbers) {
		if (!waitList.contains(party)) {
			return "That party is no longer on the waitlist.";
		}

		List<Table> selectedTables = getOpenTablesFromNumbers(tableNumbers);

		if (selectedTables.isEmpty()) {
			return "Select one or more open tables first.";
		}

		int totalSeats = getTotalSeats(selectedTables);
		if (totalSeats < party.getSize()) {
			return "Selected tables only have " + totalSeats + " seats, but " + party + " needs " + party.getSize() + ".";
		}

		waitList.remove(party);
		occupyTables(party, selectedTables);
		return buildSeatMessage(party, selectedTables, true);
	}

	public String removeFromWaitList(Party party) {
		if (!waitList.contains(party)) {
			return "That party is no longer on the waitlist.";
		}

		waitList.remove(party);
		return party + " has been removed from the waitlist.";
	}

	public String freeTable(int num) {
		Table selected = getTable(num);

		if (selected == null) {
			return "Invalid table number.";
		}

		if (isTableOpen(num)) {
			return "Table " + num + " is already open.";
		}

		int partyId = selected.getCurrentPartyId();
		List<Table> tablesToFree = new ArrayList<>();

		for (int i = 0; i < closeTbls.size(); i++) {
			Table t = (Table) closeTbls.get(i);
			if (t.getCurrentPartyId() == partyId) {
				tablesToFree.add(t);
			}
		}

		for (Table t : tablesToFree) {
			closeTbls.remove(t);
			t.setCurrentPartyId(-1);
			openTbls.add(t);
		}

		String queueMessage = seatingFromQueue();

		if (queueMessage.isEmpty()) {
			return "Freed tables for Party " + partyId + ".";
		}

		return "Freed tables for Party " + partyId + ". " + queueMessage;
	}

	public String seatingFromQueue() {
		List<String> seatedMessages = new ArrayList<>();

		while (!waitList.isEmpty()) {
			Party p = waitList.peek();
			List<Table> bestTables = findBestTableCombination(p.getSize());

			if (bestTables.isEmpty()) {
				break;
			}

			waitList.dequeue();
			occupyTables(p, bestTables);
			seatedMessages.add(buildSeatMessage(p, bestTables, true));
		}

		if (seatedMessages.isEmpty()) {
			return "";
		}

		return String.join(" ", seatedMessages);
	}

	private void occupyTables(Party p, List<Table> tables) {
		for (Table table : tables) {
			openTbls.remove(table);
			table.setCurrentPartyId(p.getId());
			table.setWeight(0);
			closeTbls.add(table);
		}

		for (int i = 0; i < openTbls.size(); i++) {
			Table openTable = (Table) openTbls.get(i);
			openTable.setWeight(openTable.getWeight() + 1);
		}
	}

	private String buildSeatMessage(Party p, List<Table> tables, boolean fromWaitList) {
		StringBuilder msg = new StringBuilder();

		if (fromWaitList) {
			msg.append(p).append(" seated from waitlist at ");
		} else {
			msg.append(p).append(" has been seated at ");
		}

		for (int i = 0; i < tables.size(); i++) {
			if (i > 0) {
				msg.append(", ");
			}
			msg.append(tables.get(i));
		}

		msg.append(".");
		return msg.toString();
	}

	private List<Table> getOpenTablesFromNumbers(List<Integer> tableNumbers) {
		List<Table> selectedTables = new ArrayList<>();

		for (int tableNum : tableNumbers) {
			Table t = getTable(tableNum);
			if (t != null && isTableOpen(tableNum)) {
				selectedTables.add(t);
			}
		}

		selectedTables.sort(Comparator.comparingInt(Table::getTblNum));
		return selectedTables;
	}

	private int getTotalSeats(List<Table> tables) {
		int total = 0;
		for (Table table : tables) {
			total += table.getSize();
		}
		return total;
	}

	private List<Table> findBestTableCombination(int partySize) {
		List<Table> openTables = getOpenTableList();

		if (openTables.isEmpty()) {
			return Collections.emptyList();
		}

		int maxSeats = 0;
		for (Table table : openTables) {
			maxSeats += table.getSize();
		}

		if (maxSeats < partySize) {
			return Collections.emptyList();
		}

		ComboState[] dp = new ComboState[maxSeats + 1];
		dp[0] = new ComboState(0, 0, -1, -1);

		for (int i = 0; i < openTables.size(); i++) {
			Table table = openTables.get(i);

			for (int seats = maxSeats - table.getSize(); seats >= 0; seats--) {
				if (dp[seats] == null) {
					continue;
				}

				int newSeats = seats + table.getSize();
				ComboState candidate = new ComboState(
						dp[seats].tableCount + 1,
						dp[seats].totalWeight + table.getWeight(),
						seats,
						i
				);

				if (dp[newSeats] == null || isBetterForSameSeatTotal(candidate, dp[newSeats])) {
					dp[newSeats] = candidate;
				}
			}
		}

		int bestSeatTotal = -1;
		ComboState bestState = null;

		for (int seats = partySize; seats <= maxSeats; seats++) {
			if (dp[seats] == null) {
				continue;
			}

			if (bestState == null) {
				bestSeatTotal = seats;
				bestState = dp[seats];
				continue;
			}

			int currentOverflow = seats - partySize;
			int bestOverflow = bestSeatTotal - partySize;

			if (currentOverflow < bestOverflow) {
				bestSeatTotal = seats;
				bestState = dp[seats];
			} else if (currentOverflow == bestOverflow) {
				if (dp[seats].tableCount < bestState.tableCount) {
					bestSeatTotal = seats;
					bestState = dp[seats];
				} else if (dp[seats].tableCount == bestState.tableCount &&
						dp[seats].totalWeight < bestState.totalWeight) {
					bestSeatTotal = seats;
					bestState = dp[seats];
				}
			}
		}

		if (bestState == null) {
			return Collections.emptyList();
		}

		List<Table> result = new ArrayList<>();
		int seats = bestSeatTotal;

		while (seats > 0) {
			ComboState state = dp[seats];
			result.add(openTables.get(state.tableIndexUsed));
			seats = state.previousSeatTotal;
		}

		result.sort(Comparator.comparingInt(Table::getTblNum));
		return result;
	}

	private boolean isBetterForSameSeatTotal(ComboState candidate, ComboState current) {
		if (candidate.tableCount != current.tableCount) {
			return candidate.tableCount < current.tableCount;
		}
		return candidate.totalWeight < current.totalWeight;
	}

	public Table getTable(int num) {
		for (int i = 0; i < openTbls.size(); i++) {
			Table t = (Table) openTbls.get(i);
			if (num == t.getTblNum()) {
				return t;
			}
		}

		for (int i = 0; i < closeTbls.size(); i++) {
			Table t = (Table) closeTbls.get(i);
			if (num == t.getTblNum()) {
				return t;
			}
		}

		return null;
	}

	public boolean isTableOpen(int num) {
		for (int i = 0; i < openTbls.size(); i++) {
			Table t = (Table) openTbls.get(i);
			if (t.getTblNum() == num) {
				return true;
			}
		}
		return false;
	}

	public List<Party> getWaitListParties() {
		List<Party> list = new ArrayList<>();

		for (int i = 0; i < waitList.size(); i++) {
			list.add(waitList.get(i));
		}

		return list;
	}

	public List<Table> getOpenTableList() {
		List<Table> list = new ArrayList<>();

		for (int i = 0; i < openTbls.size(); i++) {
			list.add((Table) openTbls.get(i));
		}

		list.sort(Comparator.comparingInt(Table::getTblNum));
		return list;
	}

	public List<List<Table>> getOccupiedTableGroups() {
		Map<Integer, List<Table>> grouped = new LinkedHashMap<>();

		List<Table> closed = new ArrayList<>();
		for (int i = 0; i < closeTbls.size(); i++) {
			closed.add((Table) closeTbls.get(i));
		}
		closed.sort(Comparator.comparingInt(Table::getCurrentPartyId).thenComparingInt(Table::getTblNum));

		for (Table table : closed) {
			grouped.computeIfAbsent(table.getCurrentPartyId(), k -> new ArrayList<>()).add(table);
		}

		return new ArrayList<>(grouped.values());
	}

	public int getOpenTableCount() {
		return openTbls.size();
	}

	public int getOccupiedTableCount() {
		return closeTbls.size();
	}

	public int getWaitListCount() {
		return waitList.size();
	}

	public int getTotalTableCount() {
		return openTbls.size() + closeTbls.size();
	}

	private static class ComboState {
		int tableCount;
		int totalWeight;
		int previousSeatTotal;
		int tableIndexUsed;

		ComboState(int tableCount, int totalWeight, int previousSeatTotal, int tableIndexUsed) {
			this.tableCount = tableCount;
			this.totalWeight = totalWeight;
			this.previousSeatTotal = previousSeatTotal;
			this.tableIndexUsed = tableIndexUsed;
		}
	}
}
