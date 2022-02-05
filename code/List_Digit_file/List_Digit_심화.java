
import test.LinkedList.Node;

// 심화 편.

/* 문제.
* 기본편 문제를 바탕으로 Linked List에 숫자가 거꾸로가 아닌 것이다.
* 
* 예를 들면,
* 4 -> 1 -> 9 = 419
* 
* 
* 그리고 이런 경우도 있다.
* 먼저. 두개의 리스트의 길이를 알아야 된다.
* 그러면 앞에 값을 '0' 으로 채운다.
* 그리고 맨 끝이 '1'의 자리니깐 뒤에서 부터 계산해야 된다.
* 4 -> 1 -> 9 = 419
*      3 -> 4 =  34
*       
* ----------------------------------------------
* 
* 풀이를 설명 하면 이렇다. 
* C는 올림수를 말함, R은 합산 결과를 말함.
* 
* l1 | 4 -> 1 -> 9 -> null 까지 가야 되는것이다.
* l2 | 0 -> 3 -> 4 -> null 까지 가야 되는것이다.
* 
* 첫번째. null에서 C=0, R=null 인 객체를 생성 했다.
* 두번째. null을 호출한 함수로 돌아간다. C=1, R=3 -> null (앞에 추가하는 것이다 '3'.)
* 세번째. C=0, R=5 -> 3 -> null 
* 네번째. C=0, R=4 -> 5 -> 3 -> null
* 끝나는 것이다.
* 
* 만약 맨앞에 케리값이 발생하면 케리를 노드로 만들어서 
* R = 4 -> 5 -> 3 -> null 이 되는 것이다.
*
*
*
*/

class LinkedList {
	Node header;

	static class Node {

		int data;
		Node next = null;

		public Node(int data) {
			this.data = data;
		}

		public Node() {
			this.data = data;
			this.next = next;
		}

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

	public Node get(int i) {
		Node n = header;
		for (int j = 0; j < i; j++) {
			n = n.next;
		}
		return n;
	}

}

class Storage {
	int carry = 0;
	Node result = null;
}

public class Test {

	public static void main(String[] args) {
		LinkedList l1 = new LinkedList();
		l1.append(9);
		l1.append(1);
		l1.retrieve();

		LinkedList l2 = new LinkedList();
		l2.append(1);
		l2.append(1);
		l2.retrieve();

		Node l = sumLists(l1.get(1), l2.get(1));
		while (l.next != null) {
			System.out.print(l.data + " -> ");
			l = l.next;
		}
		System.out.println(l.data);
	}

	private static Node sumLists(Node l1, Node l2) {
		int len1 = getListLength(l1);
		int len2 = getListLength(l2);

		if (len1 < len2) {
			l1 = LPadList(l1, len2 - len1);

		} else {
			l2 = LPadList(l2, len1 - len2);
		}

		Storage storage = addLists(l1, l2);
		if (storage.carry != 0) {
			storage.result = insertBefore(storage.result, storage.carry);
		}

		return storage.result;

	}

	private static Storage addLists(Node l1, Node l2) {
		if (l1 == null && l2 == null) {
			Storage storage = new Storage();
			return storage;
		}

		Storage storag = addLists(l1.next, l2.next);
		int value = storag.carry + l1.data + l2.data;
		int data = value % 10;
		storag.result = insertBefore(storag.result, data);
		storag.carry = value / 10;
		return storag;

	}

	private static int getListLength(Node l) {
		int total = 0;
		while (l != null) {
			total++;
			l = l.next;
		}
		return total;
	}

	private static Node insertBefore(Node node, int data) {
		Node before = new Node(data); // 여기 data 들어가야됨.
		if (node != null) {
			before.next = node;
		}
		return before;
	}

	private static Node LPadList(Node l, int length) {

		Node head = l;
		for (int i = 0; i < length; i++) {
			head = insertBefore(head, 0);
		}
		return head;
	}

}
