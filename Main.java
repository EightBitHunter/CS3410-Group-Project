public class Main{
import java.util.Random;
    public static MyLinkedList randTbls(int num, boolean open){
        MyLinkedList tbls = new MyLinkedList();
        Random rand = new Random();
        for(int i = 0; i < num; i++){
            Table tbl = new Table(i + 1, rand.nextInt(8) + 1);
            if(!open){
                tbl.occupy();
            }
            tbls.add(tbl);
        }
    }
    
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

        MyLinkedList openTbls = randTbls(40, true);
        MyLinkedList closeTbls = randTbls(40, false);

    }
}
