import java.rmi.Remote;
import java.util.NoSuchElementException;

class Queue<T> {

	class Node<T> {
		private T data;
		private Node<T> next;

		public Node(T data) {
			this.data = data;
		}
	}

	private Node<T> first;
	private Node<T> last;

	public void add(T item) {
		Node<T> t = new Node<T>(item);

		if (last != null) { // 처음이 아니라 두번째부터 라서 진행
		     last.next = t; // 그 다음 노드를 end로 설정 해주기.
		}

		last = t; // 맨 처음에 만들어 진다면 첫노드로 잡는 것이다.

		// 필요없는 과정을 두번 하는게 아닌가 생각이 든다.
		// 예를 들어, 세번째 노드까지 만들거라고 생각하자.
		// 첫번째 노드는 '그렇다' 하고 넘기고, 
		// 두번째 부터는 if문을 통해 

		// last노드 2번째를 .next를 통해 노드값에 end 주소를 가르키도록 한다.
		// 그리고 핵심인 last = end; 여기서 last를 최신 등록(last 3번째) 하는 것이다.

		// 즉, if문의 last는 2 이였으며, last = end; 코드줄을 하고 난 뒤 last는 3으로 변경 되어 있는 것이다.
		// if문의 퍼포먼스는 최신화 되기전 last 2번째를 새로운 노드 3의 주소로 연결하기 위해 이용한 것이다.
		if (first == null) {
		   first = last;
		}
	}

	public T remove() {
		if (first == null) {
			throw new NoSuchElementException();
		}

		T data = first.data;
		first = first.next;

		if (first == null) {
			last = null;
		}
		return data;
	}

	public T peek() {
		if (first == null) {
			throw new NoSuchElementException();
		}

		return first.data;
	}

	public boolean isEmpty() {
		return first == null;
	}
}

public class Test4 {

	public static void main(String[] args) {
		Queue<Integer> q = new Queue<Integer>();
		q.add(1);
		q.add(2);
		q.add(3);
		q.add(4);
		System.out.println(q.remove());
		System.out.println(q.remove());
		System.out.println(q.peek());
		System.out.println(q.remove());
		System.out.println(q.isEmpty());
		System.out.println(q.remove());
		System.out.println(q.isEmpty());

	}

}
