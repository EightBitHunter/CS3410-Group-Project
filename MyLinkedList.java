package project;

public class MyLinkedList {

    private class Node {
        Object data;
        Node next;

        public Node(Object data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;

    public MyLinkedList() {
        head = null;
    }

    public void add(Object data) {
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

    public void addFirst(Object data) {
        Node newNode = new Node(data);
        newNode.next = head;
        head = newNode;
    }

    public void remove(Object data) {
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

    public Object get(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException();
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

        throw new IndexOutOfBoundsException();
    }

    public boolean contains(Object data) {
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

    public void printList() {
        Node current = head;

        while (current != null) {
            System.out.print(current.data + " -> ");
            current = current.next;
        }

        System.out.println("null");
    }

    public Table checkTable(int partySize) {
        Node current = head;

        while (current != null) {
            Table table = (Table) current.data;

            if (table.getSize() >= partySize) {
                return table;
            }

            current = current.next;
        }

        return null;
    }
}

