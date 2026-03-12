public class Main{
    public static void main(String args[]){
        MyLinkedList list = new MyLinkedList();

        list.add(10);
        list.add(20);
        list.add(30);

        list.addFirst(5);
        list.printList();

        list.remove(20);

        list.printList();

        System.out.println("Contains 30?" + list.contains(30));
    }
}