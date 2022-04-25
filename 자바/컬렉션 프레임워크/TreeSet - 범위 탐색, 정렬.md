## TreeSet - 범위 탐색, 정렬

- 이진 탐색 트리로 구현, 범위 탐색과 정렬에 유리.
- 이진 트리는 모든 노드가 최대 2개의 하위 노드를 갖음

<br/>

### 이진 탐색 트리

- 부모보다 작은 값은 왼쪽, 큰 값은 오른쪽에 저장
- 데이터가 많아질 수록 추가, 삭제에 시간이 더 걸림(비교 횟수 증가)

<br/>

### 이진 트리 와 이진 탐색 트리 차이점.

자식이 두개 이면 이진트리이고, 

이진 탐색 트리는 부모보다 작은 값이 왼쪽에, 큰 값이 오른쪽에 가는 것이다.

<br/>

### TreeSet - 데이터 저장과정

같은게 있다면 저장 실패이다.

```java
boolean add(Object o) 
```

<br/>

## TreeSet - 주요 생성자와 메서드

add(), size(), remove(), isEmpty(), iterator() 등은 넣지 않았다.

컬렉션 인터페이스가 가지고 있는 메서드들이라 뺐다.

TreeSet만 가지고 있는 특별한 메서드들을 설명한다.

```java
Object first() // 정렬된 순서에서 첫 번째 객체를 반환한다.

Object last() // 정렬된 순서에서 마지막 객체를 반환한다.

Object ceiling(Object o) // 올림.
                        // 지정된 객체와 같은 객체를 반환. 없으면 큰 값을 가진 객체 중
						// 제일 가까운 값의 객체를 반환. 없으면 null
						// 만약, 30, 40, 50 이 있다. 여기서 40을 넣으면 40이 나온다.
						// 그런데 45를 할경우는 50을 반환 되는 것이다. 


Object floor(Object o) // 버림.
					   // 지정된 객체와 같은 객체를 반환. 없으면 작은 값을 가진 객체 중
					   // 제일 가까운 값의 객체를 반환. 없으면 null
					   // 만약, 30, 40, 50 이 있다. 여기서 40을 넣으면 40이 나온다.
					   // 그런데 35를 할경우는 30을 반환 되는 것이다. 


Object higher(Object o) // 지정된 객체보다 큰 값을 가진 객체 중 제일 가까운 값의 객체를 반환.
						// 없으면 null
						// 만약, 30, 40, 50 이 있다. 여기서 40을 넣으면 50이 나온다.


Object lower(Object o)  // 지정된 객체보다 작은 값을 가진 객체 중 제일 가까운 값의 
						// 객체를 반환 없으면 null.
						// 만약, 30, 40, 50 이 있다. 여기서 40을 넣으면 30이 나온다.


SortedSet subSet(Object fromElement, Object toElement) // 범위 검색 
// fromElement 와 toElement 사이의 결과를 반환한다. 
// 끝 범위인 toElement 는 범위에 포함되지 않음.
// 만약 20 ~ 50 까지 지정했다면 결과가 20, 30, 40 이 나오게 되는 것이다.


SortedSet headSet(Object toElement) // 지정된 객체보다 작은 값의 객체들을 반환한다.
									// 만약 어떤 값이 있다면, 그 값보다 작은값들을 가져온다.
									// 포인트는 어떤 값은 포함 안되는 것이다.


SortedSet tailSet(Object fromElement) // 지정된 객체보다 큰 값의 객체들을 반환한다.
									  // 만약 어떤 값이 있다면, 그 값부터 시작하여 큰값들을 가져온다.
									  // 포인트는 어떤 값은 포함이라는 것이다.
```


<br/><br/>


## 예제1)

```java
import java.util.*;

class aaa {
	public static void main(String[] args) {
		TreeSet set = new TreeSet(); // 범위 검색에 유리.(from~to)

		String from = "b";
		String to = "d";

		set.add("abc");
		set.add("alien");
		set.add("bat");
		set.add("car");
		set.add("Car");
		set.add("disc");
		set.add("dance");
		set.add("dZZZZ");
		set.add("dzzzz");
		set.add("elephant");
		set.add("elevator");
		set.add("fan");
		set.add("flower");

		System.out.println(set);
		System.out.println("range search : from " + from + " to " + to);

		// from == b ~ to == d 까지인데 마지막 d는 들어가지 않는다.
		System.out.println("result1 : " + set.subSet(from, to));

		// 할경우 to인 d가 + 'zzz' 까지 하여 dzzz가 된다. 
		// 그러면 dzzz가 끝에 단어니깐 'd'로 시작하는 
		// 단어들도 출력되게 되는 것이다.
		System.out.println("result2 : " + set.subSet(from, to + "zzz"));
	}
}

출력값.
[Car, abc, alien, bat, car, dZZZZ, dance, disc, dzzzz, elephant, elevator, fan, flower]
range search : from b to d
result1 : [bat, car]
result2 : [bat, car, dZZZZ, dance, disc]
```

<br/><br/>

## 예제2)

```java
import java.util.*;

class aaa {
	public static void main(String[] args) {
		TreeSet set = new TreeSet();
		int[] score = { 80, 95, 50, 35, 45, 65, 10, 100 };

		for (int i = 0; i < score.length; i++)
			set.add(new Integer(score[i]));

		System.out.println("50보다 작은 값:" + set.headSet(new Integer(50)));
		System.out.println("50보다 큰 값:" + set.tailSet(new Integer(50)));
		System.out.println("40과 80사이의 값:" + set.subSet(40, 80)); // 80은 들어가지 않는다.
	}
}

출력값.
50보다 작은 값:[10, 35, 45]
50보다 큰 값:[50, 65, 80, 95, 100]
40과 80사이의 값:[45, 50, 65]
```

<br/><br/>

## 트리 순회

이진 트리의 모든 노드를 한번씩 읽는 것을 트리 순회라고 한다.

왼쪽 자식부터 읽고, 부모 읽고, 오른쪽 자식을 읽는걸 ‘인오더’ 라고 말한다.

<br/>중위 순회하면 오름차순으로 정렬 된다.

![이미지](/programming/img/트리순회.PNG)

<br/>이렇게 읽는것을 ‘레벨 순회’라고 말한다.

![이미지](/programming/img/트리순회2.PNG)


<br/><br/>

>**Reference**
><br/>남궁 성 지음, 『자바의 정석』, 도우출판.
