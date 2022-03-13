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

        while (n.next != null) { // 맨 처음은 연결 된것이 아무것도 없기 때문에 data:0 이며, next: null 이다.
                                 // 만약 4노드 까지 연결되어 있다면 4노드까지 와서 멈춘다.

            n = n.next; // 1, 2, 3 노드들은 다음 노드가 연결 되어 있기 때문에,
                        // 노드 next가 null을 가르키는 노드를 찾을때까지 다음 노드 번지를 n에 대입
        }
        n.next = end; // 마지막 주소번지를 가르키는 null인 곳에 append로 들어와 생성한 Node 를 연결시켜준다.

    }

    void delete(int d) {
        Node n = header; // 머리를 기준으로 잡는다.
        while (n.next != null) { // 마지막 노드까지 찾는다.
            if (n.next.data == d) { // 그 다음 노드의 데이터가 d 인것을 찾는다.
                                    // 맨처음을 생각한다면 n.next.data라고 한다면 데이터는 1이다. 
                                    // 이유는 n은 data:0 이며, next: null 부터 시작하기 때문.
                                    // (main을 보면 현재 d 값은 1이다. 그래서 바로 if문 조건을 들어 올수 있는 것이다.)

                                    
                n.next = n.next.next; // 찾았다면 그 다음 다음 노드를 가르키도록 현재 노드에 넣는다. 즉, data 0에서 data 2인곳으로 연결

            } else {
                n = n.next; // data 2, 3, 4 계속해서 n.next 값이 null인 곳까지 찾는다.
                            // 찾았다면 종료. 이렇게 하는 이유는 while문을 끝까지 돌리기 위해서도 있지만, 
                            // 데이터를 지워야 할 중복값이 있을수도 있기 때문이다.
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
        ll.append(1);
        ll.append(4);
        ll.retrieve();
        ll.delete(1);
        ll.retrieve();
    }

}
