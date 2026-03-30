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
	
	public MyLinkedList seatParty(int size) {
		MyLinkedList readyTbls = new MyLinkedList();
		
		for(int i = 0; i < openTbls.size(); i++) {
			Table t = (Table) openTbls.get(i);
			
			if(size <= t.getSize()) {
				openTbls.remove(t);
				closeTbls.add(t);
				readyTbls.add(t);
				return readyTbls;
			}
		}
		
		int remain = size;
		
		Table max = null;
		for(int i = 0; i < openTbls.size(); i++) {
			Table t = (Table) openTbls.get(i);
			if(max == null || t.getSize() > max.getSize()) {
				max = t;
			}
		}
		
		if(max == null) {
			return null;
		}
		
		openTbls.remove(max);
		closeTbls.add(max);
		readyTbls.add(max);
		remain -= max.getSize();
		
		while(remain > 0 && openTbls.size() > 0){
			
			Table best = null;
			int bestDist = Integer.MAX_VALUE;
			
			for(int i = 0; i < openTbls.size(); i++) {
				Table t = (Table) openTbls.get(i);
				
				if(t.getSize() >= remain) {
					int dist = Math.abs(t.getTblNum() - max.getTblNum());
					if(dist < bestDist) {
						bestDist = dist;
						best = t;
					}
				}
			}
			
			if(best == null) {
				Table nxtMax = null;
				for(int i = 0; i < openTbls.size(); i++) {
					Table t = (Table) openTbls.get(i);
					if(nxtMax == null || t.getSize() > nxtMax.getSize()) {
						nxtMax = t;
					}
				}
				if(nxtMax == null) {
					break;
				}
				
				best = nxtMax;
			}
			openTbls.remove(best);
			closeTbls.add(best);
			readyTbls.add(best);
			remain -= best.getSize();
			
			max = best;
		}
		
		return readyTbls;
	}
	
	public void freeTable(int num) {
		for(int i = 0; i < closeTbls.size(); i++) {
			Table t = (Table) closeTbls.get(i);
			if(t != null && t.getTblNum() == num) {
				openTbls.add(t);
				closeTbls.remove(t);
				break;
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
