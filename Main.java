import java.util.Random;
import java.util.Scanner;
public class Main{
    public static MyLinkedList randTbls(int num){
        MyLinkedList tbls = new MyLinkedList();
        Random rand = new Random();
        
        for(int i = 0; i < num; i++){
        	
        	int size = rand.nextInt(8) + 1;
            Table tbl = new Table(i + 1, size);
            
            tbls.add(tbl);
        }
        
        return tbls;
    }
    
    public static void main(String args[]){
        Scanner scnr = new Scanner(System.in);
        Random rand = new Random();
        //Creates new LinkedLists for open and closed tables
        TableManager tm = new TableManager();
        
        //fill openTables with random seats
        for (int i = 0; i < 40; i++){
            int seats = rand.nextInt(8) + 1;
            Table table = new Table(i + 1, seats);
            tm.addTable(table);
        }

        //Runs the program with an input party size
        boolean run = true;
        while(run) {
	        System.out.println("What is the party size? (Input -1 to close program)");
	        int size = scnr.nextInt();
            //Closes the program when a -1 is inputted
	        if(size == -1) {
	        	System.out.println("Program closed");
	        	run = false;
	        	break;
	        }
            //Finds a table for the party
	        MyLinkedList tbls = tm.seatParty(size);
            
            //If there is not a table available
	        if(tbls.size() == 0) {
	        	System.out.println("There are no tables ready at the moment.");
	        }
            //If a single table will fit the party and is available
	        else if(tbls.size() == 1){
	        	Table t = (Table) tbls.get(0);
	        	System.out.println(t + " is available.");
	        }
            //If two or more tables will fit the party and is available
	        else {
	        	System.out.println("These tables are available:");
	        	for(int i = 0; i < tbls.size(); i++) {
	        		System.out.println(tbls.get(i));
	        	}
	        }
	        System.out.println();
        }
        scnr.close();
	}
        //print open tables
        // System.out.println("Open tables:");
        // openTables.printList();

        // list.add(10);
        // list.add(20);
        // list.add(30);

        // list.addFirst(5);
        // list.printList();

        // list.remove(20);

        // list.printList();
        
        // System.out.println("Contains 30?" + list.contains(30));

        // MyLinkedList openTbls = randTbls(40, true);
        // MyLinkedList closeTbls = randTbls(40, false);

}
