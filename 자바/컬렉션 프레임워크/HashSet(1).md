## HashSet

### Set - **순서를 유지 하지 않고, 중복도 허용하지 않는다.**

<br/>

## 핵심

HashSet, TreeSet

### HashSet

- Set인터페이스를 구현한 대표적인 컬렉션 클래스
- 먄약에 Set이 필요하다 !! 한다면 HashSet을 사용하면 된다. (일반적)
- 만약에 HashSet 쓰긴 쓰는데 순서를 유지 하고 싶다 !!
    
    그렇다면 LinkedHashSet 을 이용하면 된다.
    

	<br/>

### TreeSet

- 범위 검색과 정렬에 유리한 컬렉션 클래스
- HashSet보다 데이터 추가, 삭제에 시간이 더 걸림

<br/><br/>


## HashSet - 주요 메서드

```java
boolean add(Object o) // 추가
boolean addAll(Collection o) // 합집합

boolean remove(Object o) // 삭제
boolean removeAll(Collection o) // 교집합

boolean retainAll(Collection o) // 조건부 삭제, 차집합

boolean clear() // 모두 삭제

boolean contains(Object o) // 매개변수인 객체가 포함 되어 있는지?

// 컬렉션에 담긴 여러 객체가 모두 포함 되어 있는지 물어보는것.
boolean containsAll(Collection o) // 컬렉션으로 받는다.

Iterator iterator() // 컬렉션에 요소를 읽어 올수 있다.

boolean isEmpty() // 비었는지?

int size() // 저장된 객체수

Object[] toArray() // set에 저장된 객체를. 객체 배열로 반환 하는 것이다. 
```

<br/><br/>


### 예제1)

순서가 나열된 것처럼 보이지만, set의 특징은 순서가 없다.

```java
import java.util.*;

class aaa {
	public static void main(String[] args) {
		Object[] objArr = { "1", new Integer(1), "2", "2", "3", "3", "4", "4", "4" };
		Set set = new HashSet();

		for (int i = 0; i < objArr.length; i++) {
			set.add(objArr[i]); // HashSet에 objArr의 요소들을 저장한다.
		}

		// HashSet에 저장된 요소들을 출력한다.
		System.out.println(set);

		// HashSet에 저장된 요소들을 출력한다.(Iterator이용)
		Iterator it = set.iterator();

		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}
}

출력값.
[1, 1, 2, 3, 4]
1
1
2
3
4
```

<br/><br/>


### 예제2)

set은 정렬이 안된다.

그래서 List로 옮기는 것이다.
```java
import java.util.*;

class aaa {
	public static void main(String[] args) {

		Set set = new HashSet();

		// set의 크기가 6보다 작은 동안 1~45사이의 난수를 저장
		for (int i = 0; set.size() < 6; i++) {
			int num = (int) (Math.random() * 45) + 1;
			set.add(new Integer(num));
			set.add(num);
		}
		
		// 1. set의 모든요소를 List에 저장.
		// 그리고 대부분 컬렉션 생성자에는 이렇게 변환이 쉽게 되어 있으니
		// 잘 제공하기 때문에 편하게 사용하자.
		List list = new LinkedList(set); 
		
		// 2. list를 정렬.
		Collections.sort(list);

		// 3. list를 출력.
		System.out.println(list);

	}
}

출력값.
[8, 21, 27, 32, 35, 45]
```

<br/><br/>

>**Reference**
><br/>남궁 성 지음, 『자바의 정석』, 도우출판.
