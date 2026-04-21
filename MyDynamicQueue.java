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
	public Object dequeue() {
        if (isEmpty()) {
        	return null;
        }
        
        Object front = list.get(0);
        list.remove(front);
        return front;
    }
	public Object peek() {
		if(isEmpty()) {
			return null;
		}
		return list.get(0);
	}
	
	public void printQueue() {
		if(list.size() == 0) {
			System.out.println("No parties waiting.");
			return;
		}
		for (int i = 0; i < list.size(); i++) {
	        Party p = (Party) list.get(i);
	        System.out.println((i+1) + ". " + p);
		}
	}

}
