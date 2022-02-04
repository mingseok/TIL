
// 정렬 되어 있지 않은 LinkedList 에 중복되는 값을 제거 하시오 (단, 버퍼 X)
// 3 -> 2 -> 1 -> 2 -> 4

/* 풀이 설명.
 * 포인터 사용하여 구현.
 * n 이랑 r 을 선언.
 * n은 하나씩 그리고 r은 계속 왔다갔다 하는애.
 *
 * 여기서 부터 설명 시작.
 * -----------------------
 * r
 * 3 -> 2 -> 1 -> 2 -> 4
 * n
 * -----------------------
 * 
 * n이 시킨다.
 * "r아 3 있으면 지우고 와라" 한다.
 * 그러면 r 은 2 1 2 4 순(3은 현재 위치라서 아님)으로 3이 있는지 없는지 확인한다.
 * 
 * 그러고 3이 있는지 모두 확인하고 돌아오면 n은 2를 가르킨다.
 * 그리고 다시 r에게 시킨다.
 * "r아 2 있으면 지우고 와라" 한다.
 * 
 * 그런데 2가 있다. 그러면 2를 지운다.
 * 3 -> 2 -> 1 -> 4 가 되는 것이다.
 * 
 * 이렇게 n이 마지막 4까지 갈때 까지 r은 계속 돌고 종료가 되는 것이다.
 * 
 * 
 * */

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

		while (n.next != null) {

			n = n.next;
		}
		n.next = end;

	}

	void delete(int d) {
		Node n = header;
		while (n.next != null) {
			if (n.next.data == d) {

				n.next = n.next.next;

			} else {
				n = n.next;
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

	void removeDups() {
		Node n = header;
		while (n != null && n.next != null) {
			Node r = n;
			while (r.next != null) {
				if (n.data == r.next.data) {
					r.next = r.next.next;
				} else {
					r = r.next;
				}
			}
			n = n.next;
		}
	}

}

public class Test {

	public static void main(String[] args) {
		LinkedList ll = new LinkedList();
		ll.append(2);
		ll.append(1);
		ll.append(2);
		ll.append(3);
		ll.append(4);
		ll.append(4);
		ll.append(2);
		ll.retrieve();
		ll.removeDups();
		ll.retrieve();

	}

}


// 엔지니어대한민국 - https://www.youtube.com/watch?v=Ce4baygLMz0