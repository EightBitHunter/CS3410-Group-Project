package project;

public class Table {
	private int tblNmbr;
	private int size;
	private boolean occupied;
	
	public Table(int number, int size) {
		this.tblNmbr = number;
		this.size = size;
		this.occupied = false;
	}
	
	public int getTblNmbr(){
		return this.tblNmbr;
	}
	public void setTblNmbr(int tblNmbr) {
		this.tblNmbr = tblNmbr;
	}
	
	public int getSize() {
		return this.size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	public boolean isOccupied() {
		return this.occupied;
	}
	public void setOccupied(boolean status) {
		this.occupied = status; 
	}
}
