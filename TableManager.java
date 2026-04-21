package project;

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
	
	public MyLinkedList seatParty(int size) {
		MyLinkedList readyTbls = new MyLinkedList();
		
		Table bestTable = null;
		int bestWeight = Integer.MAX_VALUE;
		
		//new: finds lowest weight table that fits requirements; just uses weight instead of distance to find best fitting table
		for(int i = 0; i < openTbls.size(); i++) {
			Table t = (Table) openTbls.get(i);
			
			if(size <= t.getSize()) {
				if(t.getWeight() < bestWeight) {
					bestWeight = t.getWeight();
					bestTable = t;
				}
				
			}
		
		}
		for(int i =0; i< openTbls.size(); i++) {
			Table t = (Table)openTbls.get(i);
			
			if(t != bestTable && (t.getSize() >=size)) {
				t.setWeight(t.getWeight()+1);
			}
		}
		
		if(bestTable == null){
			return readyTbls;
		}
		
		bestTable.setWeight(0);
		openTbls.remove(bestTable);
		closeTbls.add(bestTable);
		readyTbls.add(bestTable);
		
		return readyTbls;
	}
	
	public void freeTable(int num) {
		for(int i = 0; i < closeTbls.size(); i++) {
			Table t = (Table) closeTbls.get(i);
			
			if(t != null && t.getTblNum() == num) {
				closeTbls.remove(t);
				openTbls.add(t);
				break;
			}
		}
		seatingFromQueue();
	}
	
	public MyLinkedList getOpenTables() {
		return openTbls;
	}
	
	public MyLinkedList getCloseTables() {
		return closeTbls;
	}
	public void printOpenTables() {
		System.out.println("Open Tables: ");
		
	    for (int i = 0; i < openTbls.size(); i++) {
	        Table t = (Table) openTbls.get(i);
	        System.out.println(t + " | size=" + t.getSize() + " | weight=" + t.getWeight());
	    }
	}
	public void printClosedTables() {
		System.out.println("Closed Tables: ");
		
		for(int i = 0; i<closeTbls.size(); i++) {
			Table t = (Table) closeTbls.get(i);
	        System.out.println(t + " | size=" + t.getSize() + " | weight=" + t.getWeight());
		}
	}
	public void printWaitList() {
		System.out.println("\nWaitlist: ");
		waitList.printQueue();
	}
	
	public Table getTable(int num) {
		for(int i = 0; i < openTbls.size(); i++) {
			Table t = (Table) openTbls.get(i);
			if(num == t.getTblNum()) {
				return t;
			}
		}
		
		for(int i = 0; i < closeTbls.size(); i++) {
			Table t = (Table) closeTbls.get(i);
			if(num == t.getTblNum()) {
				return t;
			}
		}
		
		return null;
	}
	
	public void initializeParty(Party p) {
		
		MyLinkedList result = seatParty(p.getSize());
		
		if (result.size() == 0) {
			waitList.enqueue(p);
			System.out.println(p + " has been added to waitlist.");
		}
		else {
			System.out.println(p + " has been seated at: ");
			for(int i = 0; i < result.size(); i++) {
				System.out.println(result.get(i));
			}
		}
	}
	
	public void seatingFromQueue() {
		while (!waitList.isEmpty()) {
			Party p = (Party) waitList.peek();
			
			MyLinkedList result = seatParty(p.getSize());
			if(result.size() == 0) {
				break;
			}
			
			waitList.dequeue();
			
			System.out.println(p + " seated from waitlist at: ");
			for(int i = 0; i<result.size(); i++) {
				Table t = (Table) result.get(i);
				System.out.println(t + " | size=" + t.getSize() + " | weight=" + t.getWeight());
			}
		}
	}
	
	
}
