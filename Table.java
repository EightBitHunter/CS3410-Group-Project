package project;

public class Table {
	private int tblNum;
	private int size;
	private int weight;
	private int currentPartyId;

	public Table(int num, int size) {
		this.tblNum = num;
		this.size = size;
		this.weight = 0;
		this.currentPartyId = -1;
	}

	public int getTblNum() {
		return tblNum;
	}

	public void setTblNum(int tblNum) {
		this.tblNum = tblNum;
	}

	public int getSize() {
		return size;
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

	public int getCurrentPartyId() {
		return currentPartyId;
	}

	public void setCurrentPartyId(int currentPartyId) {
		this.currentPartyId = currentPartyId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Table)) return false;

		Table other = (Table) o;
		return tblNum == other.tblNum;
	}

	@Override
	public String toString() {
		return "Table " + tblNum;
	}
}
