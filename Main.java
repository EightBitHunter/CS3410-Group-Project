public class Main{
    public static void main(String args[]){
        MyLinkedList list = new MyLinkedList();

        //create open and closed table lists
        MyLinkedList<Table> openTables = new MyLinkedList<>();
        MyLinkedList<Table> closedTables = new MyLinkedList<>();

        Random rand = new Random();
        //fill openTables with random seats
        for (int i = 0; i < 40; i++){
            int seats = rand.nextInt(8) + 1;
            Table table = new Table(seats);
            openTables.add(table);
        }

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
