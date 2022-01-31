
class Node {
	int data;
	Node next = null;

	Node(int d) {
		this.data = d;
	}

	void append(int d) {
		Node end = new Node(d);
		Node n = this;  // this는 나 자신을 말하는 거니깐. 매번 여기서 n에 대입 되는 것은
						// (노드가 연결 되어 있다는 과정해서 생각 한다면) 맨 처음인 1|2 가져오는 것이다.
						// (기차의 머리 부분을 가져온다고 생각하기)

						// 즉, 클래스 변수 data 랑 next 를 말하는 것이다.
						// end 는 아닌 이유는, 지역 변수 이기 때문.
		
		while (n.next != null) { // n.next 는 n 이라는 노드에
								 // 다음 노드를 가르키는 주소를 말하는 것이다.
								 // 있다면 1|2 처럼 되는 것이기 때문에.
								 // 조건문으로 들어간다.

			n = n.next; // n.next인것은 다음주소 2를 가르키고 있으니
						// 2 이므로 n에 2|null 이 되는 것이다.( 2 다음으로 노드가 없는 상황이라고 생각한다면.)
		}
		n.next = end; // 현재 노드의 값이 null 이라면 여기로 와서
					  // 현재 노드에서 end 에 들어가 있는 Node를 다음 노드로 가르키게
					  // 현재 노드 n.next 에 넣는 것이다.
					  // ex) 2|null -> 2|3 으로 변경
		
	}

	void delete(int d) {
		Node n = this;
		while (n.next != null) {
			if (n.next.data == d) { // 노드 -> 다음노드링크 ->다음노드데이터 를 말하는 것이다.
				
				n.next = n.next.next; // 만약 해당 노드 2를 지우고 싶다면
									  // n.next.next 를 해석 해보면
									  // 1|2 -> 2|3 인 '3' 을 가르키게 하는 것이다.
			}
		}
	}

	void retrieve() {
		Node n = this;
		while (n.next != null) {
			System.out.print(n.data + " -> ");
			n = n.next;
		}
		System.out.println(n.data);
	}

}

public class Test8 {

	public static void main(String[] args) {
		Node head = new Node(1);
		head.append(2);
		head.append(3);
		head.append(4);
		head.retrieve();
		head.delete(2);
		head.delete(3);
		head.retrieve();
	}

}
