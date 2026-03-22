package project;

public class TableManager {
	private MyLinkedList openTbls;
	private MyLinkedList closeTbls;
	
	public TableManager() {
		openTbls = new MyLinkedList();
		closeTbls = new MyLinkedList();
	}
	
	public void addTable(Table t) {
		openTbls.add(t);
	}
	
	public Table seatParty(int size) {
		for(int i = 0; i < openTbls.size(); i++) {
			Table t = (Table) openTbls.get(i);
			
			if(size <= t.getSize()) {
				openTbls.remove(t);
				closeTbls.add(t);
				return t;
			}
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
	
	public MyLinkedList getOpenTables() {
		return openTbls;
	}
	
	public MyLinkedList getCloseTables() {
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
