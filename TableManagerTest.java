package project;

public class TableManagerTest {
    public static void main(String[] args) {
        TableManager tm = new TableManager();

        // Add tables
        tm.addTable(new Table(1, 2));
        tm.addTable(new Table(2, 4));
        tm.addTable(new Table(3, 6));

        // Test getTable
        System.out.println("Get Table 2: " + tm.getTable(2));

        // Test seating
        Table seated = tm.seatParty(3);
        System.out.println("Seated party at: " + seated);

        // Test free tables list
        System.out.println("Free tables: ");
        tm.getOpenTables().printList();

        // Test occupied tables list
        System.out.println("Occupied tables: ");
        tm.getCloseTables().printList();

        // Free a table
        tm.freeTable(seated.getTblNum());
        System.out.println("After freeing table:");
        System.out.println("Free tables: ");
        System.out.println("Free tables: ");
        tm.getOpenTables().printList();
        System.out.println("Occupied tables: ");
        tm.getCloseTables().printList();
    }
}
