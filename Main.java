package project;

import java.util.Random;
import java.util.Scanner;
public class Main{
    
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
        
        int partyId = 1;
        
        boolean run = true;
        //Runs the program with an input party size
        while(run) {
	        System.out.println("\nChoose an option:");
	        System.out.println("1. Add party");
	        System.out.println("2. Free table");
	        System.out.println("3. Show open tables");
	        System.out.println("4. Show occupied tables");
	        System.out.println("5. Show waitlist");
	        System.out.println("0. Exit");
	        
	        if(!scnr.hasNextInt()) {
	        	System.out.println("Invalid input. Please input a number.");
	        	scnr.next();
	        	continue;
	        }
	        
	        int num = scnr.nextInt();
	        
	        if (num < 1 || num > 6) {
	            System.out.println("Invalid option. Please choose between 0 and 5.");
	            continue;
	        }
            
	        //1.Add party
	        if(num == 1) {
	        	System.out.println("Enter party size: ");
	        	int size = scnr.nextInt();
	        	
	        	if(size <=0) {
	        		System.out.println("Invalid party size. Please choose a value greater than 0.");
	        		continue;
	        	}
	        	
	        	Party p = new Party(partyId++, size);
		        tm.initializeParty(p);
	        }
            //2.Free table
	        else if(num == 2) {
	        	System.out.println("Enter table number to free: ");
	        	int tblNum = scnr.nextInt();
	        	
	        	Table t = tm.getTable(tblNum);
	        	if(t == null) {
	        		System.out.println("Invalid table number.");
	        		continue;
	        	}
	        	
	        	//check if table is already free
	        	MyLinkedList open = tm.getOpenTables();
	        	boolean isOpen = false;
	        	
	        	for(int i = 0; i<open.size();i++) {
	        		if (((Table) open.get(i)).getTblNum() == num) {
	        			isOpen = true;
	        			break;
	        		}
	        	}
	        	
	        	if(isOpen) {
	        		System.out.println("Table is already open");
	        		continue;
	        	}
	        }
	        
	        else if (num == 3) {
	        	tm.printOpenTables();
	        }
	        else if(num == 4) {
	        	tm.printClosedTables();
	        }
	        else if(num == 5) {
	        	tm.printWaitList();
	        }
	        else if (num == 0) {
	        	System.out.println("Program closed.");
	        	break;
	        }
	        
        }
        scnr.close();
	}

}
