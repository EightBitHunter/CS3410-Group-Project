package project;

public class MyLinkedList<T> {

    // Node class
    private class Node {
        T data;
        int weight;
        Node next;

        public Node(T data) {
            this.data = data;
            this.weight = 0;
            this.next = null;
        }
    }

    private Node head;   // first node in the list

    // Constructor
    public MyLinkedList() {
        head = null;
    }

    // Add to end of list
    public void add(T data) {
        Node newNode = new Node(data);

        if (head == null) {
            head = newNode;
            return;
        }

        Node current = head;

        while (current.next != null) {
            current = current.next;
        }

        current.next = newNode;
    }

    // Add to beginning
    public void addFirst(T data) {
        Node newNode = new Node(data);
        newNode.next = head;
        head = newNode;
    }

    // Remove a value
    public void remove(T data) {

        if (head == null) {
            return;
        }

        if (head.data.equals(data)) {
            head = head.next;
            return;
        }

        Node current = head;

        while (current.next != null) {

            if (current.next.data.equals(data)) {
                current.next = current.next.next;
                return;
            }

            current = current.next;
        }
    }
    
    public T get(int index) {

        if (index < 0) {
            throw new IndexOutOfBoundsException("Index cannot be negative");
        }

        Node current = head;
        int count = 0;

        while (current != null) {
            if (count == index) {
                return current.data;
            }
            current = current.next;
            count++;
        }

        throw new IndexOutOfBoundsException("Index out of range");
    }

    // Check if value exists
    public boolean contains(T data) {

        Node current = head;

        while (current != null) {

            if (current.data.equals(data)) {
                return true;
            }

            current = current.next;
        }

        return false;
    }
    
    public int size() {
        int count = 0;
        Node current = head;

        while (current != null) {
            count++;
            current = current.next;
        }

        return count;
    }

    // Increase the weight
    public void increaseWeight() {

        Node current = head;

        while (current != null) {
            current.weight++;   // increase weight when visited
            current = current.next;
        }
    }
    
    // Print the list
    public void printList() {

        Node current = head;

        while (current != null) {
            System.out.print(current.data + " -> ");
            current = current.next;
        }

        System.out.println("null");
    }

    public Table checkTable(int partysize){
        Node current = head;

        while (current != null){
        	
        	if(current.data instanceof Table) {
        		Table table = (Table)current.data;
        		
        		//find first open table with enough seats
        		if(table.getSize() >= partysize){
                    return table;
                }
        	}
       

            current = current.next;
        }

        return null;
    }
}
