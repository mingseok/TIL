import test.LinkedList.Node;

// 방법 2.
// 포인터 방법으로 풀기.

// 단방향 LinkedList의 끝에서 부터 n번째 노드를 찾는 알고리즘을 구현 하시오.

// 뒤에서 부터 숫자를 카운트 하는게 중요하다.

/* 노드 5개가 있다고 가정하자.
	
   1  2  3  4  5  인 노드가 있다.
   
   이렇게 만들었다고 생각하기
   첫번째 노드는 인덱스 처럼 0이 아니고 
   밑에서 처럼 1이다.
   		ll.append(1);
		ll.append(2);
		ll.append(3);
		ll.append(4);
		ll.append(5);
		ll.retrieve();
   
   규칙. 
   1. k가 3이라면 3칸 이동한다.(현재 자리에 3을 포함하지 않는다)
   2. null 까지가 무슨 뜻이냐면 마지막 노드 next의 값 null을 말하는게 아니고,
      마지막 노드 다음인 null을 얘기 한다.
   3. k가 3이라 p1이 먼저 3칸 갔다고 p1과 p2가 다시 3칸만 움직이는게 아니라, null을 만날때 까지 이동한다.
   4. 모든 작업이 끝나고 p2가 있는 그 해당 노드가 바로 정답이다. (뒤에서 부터 앞으로 카운트 하는게 아니다.)   
   
   
   k = 3 값이 있다.
  
   첫번째. 포인터 p1을 k값 만큼 보낸다.
   그리고 p2는 그대로 둔다.
   
             p1
   1.   2.   3.   4.   5.   null.
   p2	

   두번째. 동시에 p1과 p2를 p1이 null을 만날때 까지 전진 시킨다.
   p1이 null을 만나면 p2의 자리가 바로 정답인 것이다.  


*/ 

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

	private static Node KthToLast(Node first, int k) {

		Node p1 = first;
		Node p2 = first;

		for (int i = 0; i < k; i++) {
			if (p1 == null) {
				return null;
			}
			p1 = p1.next;
		}

		while (p1 != null) {
			p1 = p1.next;
			p2 = p2.next;
		}
		return p2;

	}

	public static void main(String[] args) {
		LinkedList ll = new LinkedList();
		ll.append(1);
		ll.append(2);
		ll.append(3);
		ll.append(4);
		ll.append(5);
		ll.retrieve();

		int k = 4;

		Node found = KthToLast(ll.getFirst(), k);
		System.out.println(found.data);
	}

}


// 엔지니어대한민국 - https://www.youtube.com/watch?v=Vb24scNDAVg