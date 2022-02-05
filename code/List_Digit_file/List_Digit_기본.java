package List_Digit_file;

import test.LinkedList.Node;


// 기본 편.


/* 문제.
 * 어떤 숫자를 자리수 별로 한개씩 Listed list에 담았다.
 * 그런데, 1의 자리가 헤더에 오도록 거꾸로 담았다.
 * 이런식의 Liked list 두개를 받아서 합산하고 같은 식으로 
 * 링크드 리스트에 담아서 변환 하라.
 * 
 * 예를 들면,
 * 9 -> 1 -> 4  => 419
 * 6 -> 4 -> 3  => 346
 * 
 * 합하면 419 + 346 = 765 가 된다.
 * 765 를 다시
 * 5 -> 6 -> 7 로 변환 하는 것이다.
 * 
 * 
 * l1 | 9 -> 1 -> 4
 * l2 | 6 -> 4 -> 3
 * C  | 0    1    0  --> c는 케라값 올림수를 말한다
 * R  | 5    6    7  --> 해당 값 밑에 설명
 * 						 함수를 호출 했던 스택 순서대로 돌아간다.
 * 						 즉, 7을 반환 받으면 6에 링크를 걸고
 * 						 6을 반환 받으면 5에 링크를 건다.
 * 
 */



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

	public Node get(int i) {
		Node n = header;
		for (int j = 0; j < i; j++) {
			n = n.next;
		}
		return n;
	}

}

public class Test {

	public static void main(String[] args) {
		LinkedList l1 = new LinkedList();
		l1.append(9);
		l1.append(1);
		l1.append(4);
		l1.retrieve();

		LinkedList l2 = new LinkedList();
		l2.append(6);
		l2.append(4);
		l2.append(3);
		l2.retrieve();

		Node n = sumLists(l1.get(1), l2.get(1), 0);
		while (n.next != null) {
			System.out.print(n.data + " -> ");
			n = n.next;
		}
		System.out.println(n.data);
	}

	private static Node sumLists(Node l1, Node l2, int carry) {
		if (l1 == null && l2 == null && carry == 0) {
			return null;
		}

		Node result = new Node();
		int value = carry;

		if (l1 != null) {
			value += l1.data;
		}
		if (l2 != null) {
			value += l2.data;
		}
		result.data = value % 10;

		if (l1 != null || l2 != null) {
			Node next = sumLists(l1 == null ? null : l1.next, l2 == null ? null : l2.next, value >= 10 ? 1 : 0);
			result.next = next;

		}
		return result;

	}

}
