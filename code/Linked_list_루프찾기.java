// Linked List 안에 루프가 있는지 확인하고 루프가 시작되는 노드를 찾으시오

class Node {
	int data;
	Node next;

	public Node(int data) {
		this.data = data;
		this.next = null;
	}

	Node addNext(int d) {
		Node end = new Node(d);
		Node n = this;

		while (n.next != null) {
			n = n.next;
		}
		n.next = end;
		return end;
	}

	Node addNext(Node d) {
		Node n = this;

		while (n.next != null) {
			n = n.next;
		}
		n.next = d;
		return d;
	}

	void delete(int d) {
		Node n = this;
		while (n.next != null) {
			if (n.next.data == d) {
				n.next = n.next.next;
			} else {
				n = n.next;
			}
		}
	}

	void print() {
		Node n = this;
		while (n.next != null) {
			System.out.print(n.data + " -> ");
			n = n.next;
		}
		System.out.print(n.data);
	}
}

public class Test {

	public static void main(String[] args) {
		Node n1 = new Node(1);
		Node n2 = n1.addNext(2);
		Node n3 = n2.addNext(3);
		Node n4 = n3.addNext(4);
		Node n5 = n4.addNext(5);
		Node n6 = n5.addNext(6);
		Node n7 = n6.addNext(7);
		Node n8 = n7.addNext(8);
		n8.addNext(n4);

		Node n = findLoop(n1);

		if (n != null) {
			System.out.println("Start of loop: " + n.data);
		} else {
			System.out.println("Not found");
		}

	}

	private static Node findLoop(Node head) {
		Node fast = head;
		Node slow = head;

		while (fast != null && fast.next != null) {
			slow = slow.next;
			fast = fast.next.next;
			if (fast == slow) {
				break;
			}
		}
		if (fast == null || fast.next == null) {
			return null;
		}
		slow = head;
		while (fast != slow) {
			slow = slow.next;
			fast = fast.next;
		}
		return fast;

	}

}

// 엔지니어대한민국 - https://www.youtube.com/watch?v=AWWxMl9-8CY