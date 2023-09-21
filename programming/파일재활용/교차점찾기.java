
// 문제.
// 주어진 두개의 단방향 Linked List에서 교차되는 노드를 찾으시오.
// (단, 교차점은 값이 아닌 주소로 찾아야 한다.)

/*

교차점이란?
두개의 리스트가 중간에서 합쳐 지는 것이다.

우리는 그림을 모르고
이렇게만 알고 있는 것이다.
5 -> 7 -> 9 -> 10 -> 7 -> 6 
6 -> 8 -> 10 -> 7 -> 6

푸는 방법.
1. 리스트의 끝을 맞춰 준다.
2. 그리고 앞부분이 튀어 나온 부분은 짤라 준다. (5를 짜르는 것이다.)


*/

class Node {
	int data;
	Node next;

	public Node(int data) {
		this.data = data;
		this.next = null;
	}

	public Node get(int num) {
		Node n = this;
		for (int i = 0; i < num; i++) {
			n = n.next;
		}
		return n;
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
		Node n1 = new Node(5);
		Node n2 = n1.addNext(7);
		Node n3 = n1.addNext(9);
		Node n4 = n1.addNext(10);
		Node n5 = n1.addNext(7);
		Node n6 = n1.addNext(6);

		Node m1 = new Node(6);
		Node m2 = m1.addNext(8);
		Node m3 = m2.addNext(n4);

		// Node m1 = new Node(1);
		// Node m2 = m1.addNext(2);
		// Node m3 = m1.addNext(3);
		// Node m4 = m1.addNext(4);
		// Node m5 = m1.addNext(5);
		// Node m6 = m1.addNext(6);

		n1.print();
		System.out.println();
		m1.print();
		System.out.println();

		Node n = getIntersection(n1, m1);

		if (n != null) {
			System.out.println("Intersection: " + n.data);

		} else {
			System.out.println("Not found");
		}
	}

	private static Node getIntersection(Node l1, Node l2) {
		int len1 = getListLength(l1);
		int len2 = getListLength(l2);

		if (len1 > len2) {
			l1 = l1.get(len1 - len2);
		} else if (len1 < len2) {
			l2 = l2.get(len2 - len1);
		}

		while (l1 != null && l2 != null) {
			if (l1 == l2) {
				return l1;
			}
			l1 = l1.next;
			l2 = l2.next;
		}
		return null;
	}

	private static int getListLength(Node l) {
		int size = 0;
		Node n = l;
		while (n != null) {
			n = n.next;
			size++;
		}
		return size;
	}
}

// 엔지니어대한민국 - https://www.youtube.com/watch?v=dk4oFGJx3ps&t=1s