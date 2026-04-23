package project;

public class MyDynamicQueue {
	private MyLinkedList list;

	public MyDynamicQueue() {
		list = new MyLinkedList();
	}

	public void enqueue(Party p) {
		list.add(p);
	}

	public boolean isEmpty() {
		return list.size() == 0;
	}

	public Party dequeue() {
		if (isEmpty()) {
			return null;
		}

		Party front = (Party) list.get(0);
		list.remove(front);
		return front;
	}

	public Party peek() {
		if (isEmpty()) {
			return null;
		}
		return (Party) list.get(0);
	}

	public int size() {
		return list.size();
	}

	public Party get(int index) {
		return (Party) list.get(index);
	}

	public boolean contains(Party p) {
		return list.contains(p);
	}

	public void remove(Party p) {
		list.remove(p);
	}
}
