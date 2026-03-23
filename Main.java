package project;
import java.util.Random;

public class Main{
    public static MyLinkedList<Table> randTbls(int num){
        MyLinkedList<Table> tbls = new MyLinkedList<>();
        Random rand = new Random();
        
        for(int i = 0; i < num; i++){
        	
        	int size = rand.nextInt(8) + 1;
            Table tbl = new Table(i + 1, size);
            
            tbls.add(tbl);
        }
        
        return tbls;
    }
    
    public static void main(String args[]){
    	
    	Random rand = new Random();
    	
        //create open and closed table lists
        MyLinkedList<Table> openTables = randTbls(40);
        MyLinkedList<Table> closedTables = new MyLinkedList<>();
        
        System.out.println("We're open for business!");
        System.out.println("\nOpen Tables: ");
        openTables.printList();

        //TableManager Creation
        TableManager manager = new TableManager(openTables, closedTables);
        
        //random parties from size 1 - 15
        for (int i = 0; i < 20; i++) {

            int partySize = rand.nextInt(15) + 1;
            manager.seatParty(partySize);
        }
        
        //end of simulation
        System.out.println("\n---- Results ----");
        System.out.println("Final Open Tables: ");
        openTables.printList();
        
        System.out.println("Final Closed Tables: ");
        closedTables.printList();

    }
}
