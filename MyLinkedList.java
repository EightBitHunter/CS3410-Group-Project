public class MyLinkedList {

    // Node class
    private class Node {
        int data;
        int weight;
        Node next;

        public Node(int data) {
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
    public void add(int data) {
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
    public void addFirst(int data) {
        Node newNode = new Node(data);
        newNode.next = head;
        head = newNode;
    }

    // Remove a value
    public void remove(int data) {

        if (head == null) {
            return;
        }

        if (head.data == data) {
            head = head.next;
            return;
        }

        Node current = head;

        while (current.next != null) {

            if (current.next.data == data) {
                current.next = current.next.next;
                return;
            }

            current = current.next;
        }
    }

    // Check if value exists
    public boolean contains(int data) {

        Node current = head;

        while (current != null) {

            if (current.data == data) {
                return true;
            }

            current = current.next;
        }

        return false;
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

    // See if list is empty
    public boolean isEmpty() {
        return head == null;
    }

    //Find seat size of a usable table
    public int check(int partySize){
        Node current = head;
        while (current != null){
            if (current.data >= partySize){
                //if table is found
                return current.data;
            }

        return -1 //if no table is found
}
