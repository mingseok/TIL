## Iterator, ListIterator, Enumeration

### 컬렉션에 저장된 데이터를 접근하는데 사용되는 인터페이스

<br/>


## asIterator() 메서드
이 열거에 포함 된 요소들을 순회 하는 Iterator를 리턴합니다.


### forEachRemaining() 을 이용한 Iterator to ArrayList

Java8 에서 추가된 forEachRemaining() 메소드를 사용해 Iterator 를 ArrayList 로 변환 할 수 있다

<br/>


## Iterator 메서드

| 메서드 | 설명 |
| --- | --- |
| boolean hasNext() | 읽어 올 요소가 남아있는지 확인한다. 있으면 true, 없으면 false를 반환한다. |
| Object next() | 다음 요소를 읽어 온다. next()를 호출하기 전에 hasNext()를 호출해서 읽어 올 요소가 있는지 확인하는 것이 안전하다.  |

<br/>hasNext() → 확인, next() → 읽기 를 반복 할 수 있다.

즉, 컬렉션에 저장되어 있는 요소를 다 꺼내 올때까지 계속 될게 되는 것이다.

더 이상 읽어 올게 없으면 hasNext() 에서 false 를 반환 하여 끝나는 것이다.

**<br/>Iterator 이랑 Enumeration 은 거의 같다.**

Iterator 이 최신 버전이고, Enumeration 구버전이라고 생각하면 된다.

<br/>

## Enumeration 메서드

잘 쓰지 않는다.

| 메서드 | 설명 |
| --- | --- |
| boolean hasMoreElements() | Iterator 메서드의 boolean hasNext() 와 같다. |
| Object nextElement() | Iterator 메서드의 Object next() 와 같다. |

<br/>

## ListIterator 메서드

양방향이다.

<br/>

## Iterator 가 필요한 이유.

- 컬렉션에 저장된 요소들을 읽어오는 방법을 표준화한 것.
    
    컬렉션에는 List, Set, Map 종류가 있다. 이걸 표준화 한것.
    

<br/>즉, List에서 Set로 바꿨을때 (=컬렉션 클래스를 다른걸로 바꿨을때), 

<br/>저장된 요소를 읽어 오는 코드가 바꿔야 되는 것이다. 

<br/>하지만, 그걸 iterator 로 읽어오면 컬렉션이 바꿔도 읽어오는 코드를 변경하지 않아도 된다.


<br/><br/>

## Iterator 쓰는 방법.

- 컬렉션에 iterator()를 호출해서 iterator 를 구현한 객체를 얻어서 사용한다.

```java
List list = new ArrayList(); // 다른 컬렉션으로 변경할 때는 이 부분만 고치면 된다.
Iterator it = list.iterator(); // Iterator 객체를 반환.

while(it.hasNext()) { // 읽어올 요소가 있는지 확인
		System.out.println(it.next()); // 다음 요소를 읽어옴
}
```

<br/>

### 예제1)

```java
import java.util.*;

class aaa {
	public static void main(String[] args) {
		ArrayList list = new ArrayList();
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");

		Iterator it = list.iterator();
        
		while(it.hasNext()) {
			Object obj = it.next();
			System.out.println(obj);
		}
	} // main
}

출력값.
1
2
3
4
5
```

<br/><br/>

## 예제 1번에서 추가적인 설명

while() 문 코드를 한번 더 작성했다. 하더라도 실행 시키면 똑같이 1, 2, 3, 4, 5 만 나온다. 

<br/>

### 이유는?

두번째 while문에서 while(it.hasNext()) 가 while(false) 로 되기 때문이다.

iterator 는 하나씩 읽어 오면서 더이상 읽어 올게 없다면 hasNext로 호출에도 값이 없는 것이다. 

즉, 읽어온 것들은 그 상태로 저장된다는 말이다.

<br/>

이걸 해결 하려면 다시 iterator 를 얻어 와야 하는 것이다.

```java
import java.util.*;

class aaa {
	public static void main(String[] args) {
		ArrayList list = new ArrayList();
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");

		Iterator it = list.iterator();
        
		while(it.hasNext()) {
			Object obj = it.next();
			System.out.println(obj);
		}

		while(it.hasNext()) {
			Object obj = it.next();
			System.out.println(obj);
		}
	} // main
}

출력값.
1
2
3
4
5
```

<br/>

### 어떻게?

다시 말해, iterator는 일회용 이다.

```java
it = list.iterator(); // 새로운 iterator 객체를 얻는다.
```

<br/>

## Iterator 를 사용하게 된 이유 코드

에러 나는 코드이다. (보여주기 용)

<br/>

### 이유는? 

new HashSet(); 으로 생성 했는데, HashSet 안에는 get() 이라는 메서드가 없기 때문이다.

그러므로 밑에 for문 코드는 동작하지 않는 것이다.

```java
import java.util.*;

class aaa {
	public static void main(String[] args) {
		HashSet list = new HashSet();
		// Collection list = new HashSet(); 이렇게도 가능하다. 다형성에 의해. 
		// 이방법을 추천한다.

		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");

		Iterator it = list.iterator();
        
		while(it.hasNext()) {
			Object obj = it.next();
			System.out.println(obj);
		}

		for(int i = 0; i < list.size(); i++) {
					Object obj = list.get(i);
					System.out.println(obj);
		}
	} // main
}

출력값.
1
2
3
4
5
```

<br/><br/>

## Map에는 iterator() 가 없다.

- keySet(), entrySet(), values()를 호출해야 된다.

![이미지](/programming/img/맵.PNG)

1. map 에서 entrySet() 호출한다.
2. 거기에 iterator() 를 호출하는 것이다.
3. 그 다음 iterator() 를 얻어 온 걸로 map에 있는 요소들을 하나 씩 읽어 올 수 있게 되는 것이다.

```java
Map map = new HashMap();

Iterator it = map.entrySet().iterator();
```

<br/>위에 코드를 두줄로 한다면 이렇게 되는 것이다.
```java
Set eSet = map.entrySet();
Iterator it = eSet.iterator();
```

<br/><br/>

>**Reference**
><br/>남궁 성 지음, 『자바의 정석』, 도우출판.
