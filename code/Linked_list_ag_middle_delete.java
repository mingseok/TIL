
import test.LinkedList.Node;

// 단방향 Linked List 중간노드 삭제.
// 단, 당신은 첫번째 노드가 어딨는지 모르고, 오직 삭제할 노드만 갖고 있다. 

class LinkedList {
	Node header;

	static class Node {
		int data;
		Node next = null;
	}

	LinkedList() {
		header = new Node();
	}

	void append(int d) {
		Node end = new Node();
		end.data = d;
		Node n = header;

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

	public Node get(int i) {
		Node n = header;
		for (int j = 0; j < i; j++) {
			n = n.next;
		}
		return n;
	}
}

public class Test2 {

	public static void main(String[] args) {
		LinkedList ll = new LinkedList();
		ll.append(1);
		ll.append(2);
		ll.append(3);
		ll.append(4);
		ll.retrieve();

		deleteNode(ll.get(3));
		ll.retrieve();
	}

	public static boolean deleteNode(Node n) {
		if (n == null || n.next == null) {
			return false;
		}
		Node next = n.next;
		n.data = next.data;
		n.next = next.next;
		return true;

	}

}

// 엔지니어대한민국 - https://www.youtube.com/watch?v=xI4iPEmkHlc