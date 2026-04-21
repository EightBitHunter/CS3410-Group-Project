package project;

public class Table{
	private int tblNum;
	private int size;
	private int weight = 0;
	
	public Table(int num, int size) {
		this.tblNum = num;
		this.size = size;
	}
	
	public int getTblNum(){
		return this.tblNum;
	}
	public void setTblNum(int tblNmbr) {
		this.tblNum = tblNmbr;
	}
	
	public int getSize() {
		return this.size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) {
	    	return true;
	    }
	    if (!(o instanceof Table)) {
	    	return false;
	    }
	    Table other = (Table) o;
	    return this.tblNum == other.tblNum;
	}
	
	public String toString() {
		String msg = "Table " + getTblNum();
		return msg;
	}
}
