## Comparator와 Comparable

객체 정렬에 필요한 메서드(정렬기준 제공)를 정의한 인터페이스

```java
Comparable // 기본 정렬기준으로 구현하는데 사용. (default)
Comparator // 기본 정렬기준 외에 다른 기준으로 정렬하고자할 때 사용 (역순)
```

<br/>


## Comparator 인터페이스의 내부

```java
public interface Comparator {

		// 어느 쪽이 더 큰지를 정수값을 반환을 한다.
		// 결과가 '0' 이면 같은것이고, 
		// '양수'면 왼쪽이 더 큰것이고, 
		// '음수'면 오른쪽이 더 큰것이다.
		int compare(Object o1, object o2); // 이건 매개변수 o1, o2의 두 객체를 비교 하는 것.

		boolean equals(Object obj); // equals를 오버라이딩하라는 뜻
}

public interface Comparable {

		// this와 비교한다.
		int compareTo(Object o); // 주어진 객체(o)와 this 인 자신과 비교 하는 것.
}
```

<br/><br/>


## '기본 정렬 기준' 이란?

사전 순서이다. 대문자 먼저 출력하고 소문자는 뒤에 출력.

하지만 "나는 대소문자 구분 하기 싫다." 한다면 

String.CASE_INSENSITIVE_ORDER 메서드 사용하면 된다.

<br/>그리고 "나는 역순으로 정렬 하고 싶다" 한다면 밑에 Descending 클래스 만들어 주기.

Comparator를 구현하고 있다. compareTo()메서드를 내가 코드 수정하면 된다. 

어떻게? String이 가지고 있던 기본 정렬 기준'을 이용해서 거기다 -1 를 했다.

그러면? 거꾸로 바뀌는 것이다. 음수값으로 변하면서 역순이 되는 것이다. (하지만 - 하면 된다. 이부분은 뒤에 설명)

<br/><br/>

## 예제 1)

정렬 할때는 정렬 기준과 대상이 필요하다. 

그리하여 매개변수로 두개가 온 것이다. (두번째 출력값 메서드부터)

<br/>2번째 출력 메서드를 보면,

strArr 는 정렬 대상이고, String.CASE_INSENSITIVE_ORDER는 정렬 기준 메서드이다.

하지만 첫번째 출력값 코드줄에서는 정렬 '기준'이 없다. 이유는?

comparable 인터페이스란 '기본 정렬 기준' 이다.

<br/>String 클래스 자체가 comparable를 구현 하고 있으므로,

String클래스에서 compareTo메서드인 '기본 정렬 기준' 메서드를 가지고 있게 된 것이다.

그리하여, 정렬 기준이 필요 없게 된 것이다

```java
import java.util.*;

class aaa {
	public static void main(String[] args) {
		String[] strArr = { "cat", "Dog", "lion", "tiger" };

		Arrays.sort(strArr); // String의 Comparable구현에 의한 정렬
		
		// 출력값. strArr=[Dog, cat, lion, tiger] => Dog가 먼저 나온 이유는 대문자라서 
		System.out.println("strArr=" + Arrays.toString(strArr));


		Arrays.sort(strArr, String.CASE_INSENSITIVE_ORDER); // 대소문자 구분안함
		
		// 출력값. strArr=[cat, Dog, lion, tiger]
		System.out.println("strArr=" + Arrays.toString(strArr));


		Arrays.sort(strArr, new Descending()); // 역순 정렬
		
		// 출력값. strArr=[tiger, lion, cat, Dog]
		System.out.println("strArr=" + Arrays.toString(strArr));
	}
}

class Descending implements Comparator {
	public int compare(Object o1, Object o2) {
		if (o1 instanceof Comparable && o2 instanceof Comparable) {
			Comparable c1 = (Comparable) o1;
			Comparable c2 = (Comparable) o2;
			return c1.compareTo(c2) * -1; // -1을 곱해서 기본 정렬방식의 역으로 변경한다.
						      // 또는 c2.compareTo(c1)와 같이 순서를 바꿔도 된다.

		}
		return -1;
	}
}
```

<br/><br/>

## Integer와 Comparable

### Comparable이 뭐다?

‘기본 정렬 기준’ 을 제공해주는 인터페이스 이다. 

그리고 ‘default이다.’ 무슨말이냐면, 정렬 기준이 없을 때 사용하는 것.

그리고 ‘기본 정렬 기준’ 이 뭐다?

public int compareTo() 메서드 이다

<br/>

### compareTo() 메서드 설명

return 값을 보면 두 변수를 ‘-’ 을 한다.

즉, 7 - 5 = 2 양수이며, 5 - 5 = 0 이므로 같고, 5 - 7 = -2 이므로 음수이다. 

즉, 왼쪽 값이 크면 양수를, 두 값이 같으면 0, 오른쪽 값이 크면 음수를 반환한다.

이렇게 ‘내림 차순’의 경우 반대로 뺄셈을 하면 된다.

<br/><br/>

>**Reference**
><br/>남궁 성 지음, 『자바의 정석』, 도우출판.
