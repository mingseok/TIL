package test;

import test.LinkedList.Node;

// 방법 2.
// 재귀호출 방법으로 풀기.

// 단방향 LinkedList의 끝에서 부터 n번째 노드를 찾는 알고리즘을 구현 하시오.
// (단방향 리스트는 항상 맨 앞에서 부터 시작한다.)

// k이가 3이면 뒤에서 부터 노드 3까지 거리를 카운트 한다고 생각하면 된다.
// 거리를 카운트 하니깐 맨뒤도(자기도) 포함 되는 것이다.
//   1  2  3  4
//     정답은 2


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

	Node getFirst() {
		return header.next;
	}

}

class Reference {
	public int count = 0;
}

public class Test2 {

	private static Node KthToLast(Node n, int k, Reference r) {
		if (n == null) {
			return null;
		}

		Node found = KthToLast(n.next, k, r);
		r.count++;
		if (r.count == k) {
			return n;
		}
		return found;

	}

	public static void main(String[] args) {
		LinkedList ll = new LinkedList();
		ll.append(1);
		ll.append(2);
		ll.append(3);
		ll.append(4);
		ll.retrieve();

		int k = 3;
		Reference r = new Reference();
		Node found = KthToLast(ll.getFirst(), k, r);
		System.out.println(found.data);
	}

}

// 엔지니어대한민국 - https://www.youtube.com/watch?v=Vb24scNDAVg