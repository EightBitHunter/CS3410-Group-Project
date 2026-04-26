package project;

public class Party {
	private int id;
	private int size;

	public Party(int id, int size) {
		this.id = id;
		this.size = size;
	}

	public int getId() {
		return id;
	}

	public int getSize() {
		return size;
	}

	@Override
	public String toString() {
		return "Party " + id + " (" + size + " people)";
	}
}
