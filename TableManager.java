package project;

public class TableManager {
	private MyLinkedList<Table> openTbls;
	private MyLinkedList<Table> closeTbls;
	
	public TableManager() {
		openTbls = new MyLinkedList<Table>();
		closeTbls = new MyLinkedList<Table>();
	}
	 public TableManager(MyLinkedList<Table> open, MyLinkedList<Table> closed) {
	        this.openTbls = open;
	        this.closeTbls = closed;
	    }
	
	public void addTable(Table t) {
		openTbls.add(t);
	}
	
	public MyLinkedList<Table> seatParty(int size) {
		
		MyLinkedList<Table> usedTables = new MyLinkedList<>();
		
		for(int i = 0; i < openTbls.size(); i++) {
			Table t = (Table) openTbls.get(i);
			
			if(size <= t.getSize()) {
				openTbls.remove(t);
				closeTbls.add(t);
				usedTables.add(t);
				return usedTables;
			}
		}
		
		//combining tables
		int seatsaddedforParty = 0;
		
		for (int i = 0; i < openTbls.size() && seatsaddedforParty < size;i++) {
			
			Table t = (Table) openTbls.get(i);
			seatsaddedforParty += t.getSize();
			usedTables.add(t);
			closeTbls.add(t);
			openTbls.remove(t);
		}
		
		//if enough seats are found, return
		if(seatsaddedforParty >= size) {
			return usedTables;
		}
		
		//final resort if enough seats can't be found for party
		for (int i = 0; i < usedTables.size(); i++) {
			Table t = (Table) usedTables.get(i);
			
			closeTbls.remove(t);
			openTbls.add(t);
		}
		return null;
	}
	
	public void freeTable(int num) {
		for(int i = 0; i < closeTbls.size(); i++) {
			Table t = (Table) closeTbls.get(i);
			if(t != null) {
				openTbls.add(t);
				closeTbls.remove(t);
			}
		}
	}
	
	public MyLinkedList<Table> getOpenTables() {
		return openTbls;
	}
	
	public MyLinkedList<Table> getCloseTables() {
		return closeTbls;
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
	
	
	
}
