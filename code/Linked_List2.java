class LinkedList {
    Node header;

    static class Node {
        int data;
        Node next = null;
    }

    LinkedList() {
        header = new Node(); // 노드의 대표 첫부분 (스타트 부분)
    }

    void append(int d) {
        Node end = new Node(); // 노드 생성
        end.data = d;
        Node n = header; // 노드 머리인 0|null -> n에 대입

        while (n.next != null) { // 만약 4노드 까지 연결되어 있다면 4노드까지 와서 멈춘다.

            n = n.next; // 1, 2, 3 노드들은 다음 노드가 연결 되어 있기 때문에,
                        // 노드 next가 null을 가르키는 노드를 찾을때까지 다음 노드 번지를 n에 대입
        }
        n.next = end; // 마지막 주소번지를 가르키는 null인 곳에 append로 들어와 생성한 Node 를 연결시켜준다.

    }

    void delete(int d) {
        Node n = header; // 머리를 기준으로 잡는다.
        while (n.next != null) { // 마지막 노드까지 찾는다.
            if (n.next.data == d) { // 그 다음 노드의 데이터가 d 인것을 찾는다.

                n.next = n.next.next; // 찾았다면 그 다음 다음 노드를 가르키도록 현재 노드에 넣는다. 두칸 건너 뛴다는 얘기다.

            } else {
                n = n.next; // 그리고 그 값을 n에 넣는다.
            }
        }
    }

    void retrieve() {
        Node n = header.next;
        while (n.next != null) {

            System.out.print(n.data + " -> ");
            n = n.next;
        }
        System.out.println(n.data);
    }
}

public class aaa {

    public static void main(String[] args) {
        LinkedList ll = new LinkedList();
        ll.append(1);
        ll.append(2);
        ll.append(3);
        ll.append(4);
        ll.retrieve();
        ll.delete(1);
        ll.retrieve();
    }

}
