## Comparable과 Comparator은 왜 사용하는가?

<br/>

## 왜 사용하는가?

`int`형을 받아서 객체로 만드는 순간, 

우리 알지만 컴퓨터는 객체 상태로 포장되어서 모른다! 

<br/>

그렇기 때문에 `equals()`, `hashCode()` 처럼 다시 재정의 해줘야 하는 것이다.

```java
Comparable과 Comparator는 모두 인터페이스이다
```

- Comparable 인터페이스

    - 우리가 실질적으로 구현해야 하는 것은 단 하나다 → `compareTo(T o)`

- Comparator 인터페이스

    - 우리가 실질적으로 구현해야 하는 것은 단 하나다 → `compare(T o1, T o2)`

<br/><br/>

## Comparable과 Comparator의 무슨 차이인 것일까?

- `Comparable`의 `compareTo(T o)` 메소드는 파라미터가 한 개이고,

- `Comparator`의 `compare(T o1, T o2)` 메소드는 파라미터가 두 개인 것일까?



```java
Comparable은 "자기 자신과 파라미터로 들어오는 객체를 비교"하는 것이고,
Comparator는 "자기 자신의 상태가 어떻던 상관없이," +
             "파라미터로 들어오는 두 객체를 비교" 하는 것이다.
```

<br/><br/>

## 왜 객체를 비교할 수 있도록 한다는 것일까?

`primitive` 타입 변수의 경우, `‘==’` 가지고 쉽게 두 변수를 비교할 수 있었다.

`primitive` 타입은은 자바에서 제공되기에 별다른 처리 없이 비교가 가능하다. 

<br/><br/>

## 새로운 객체를 만들어 비교하고자 한다면 어떻게 될까?

예를 들어, 학생의 나이와 학급 정보를 갖고 있는 클래스를 만든다고 가정해보자.

`a`학생과 `b`학생 두 객체를 생성했다. → 그럼 두 객체를 어떻게 비교할 것인가? 

- “기준은 뭘까?”

- “나이(age)를 기준으로 비교되는 건가?”
- “학급(classNumber)을 기준으로 비교되는 건가?”

<br/>

### 밑에 코드로 설명하면 이렇다.

```java
public class Test {
	public static void main(String[] args)  {
		Student 민석 = new Student(17, 2);	// 17살 2반
		Student 다연 = new Student(18, 1);	// 18살 1반
		
		/*
		 * 어떻게 비교해..? (기준이 없다)
		 * "if(민석 > 다연) ?? 이렇게 못한다.."
		*/
	}
}
 
class Student {
	int age; // 나이
	int classNumber; // 학급
	
	Student(int age, int classNumber) {
		this.age = age;
		this.classNumber = classNumber;
	}
}
```

<br/><br/>

## Comparable 인터페이스

자기 자신은 `ClassName`으로 생성한 객체 자신이 되고, 

매개변수 객체는 `ClassName.compareTo(o);` 를 통해 들어온 파라미터 `o`가 비교 할 객체가 되는 것이다.

<br/><br/>

## 예로 들어보자.

일단, `Student` 클래스에 `Comparable` 을 `implements` 해야한다. 

그리고 `<>` 사이에 들어갈 타입은 무엇일까? 

<br/>

`Student` 객체와 또 다른 `Student` 객체를 비교하고 싶다면, 

`<>` 사이에 들어갈 타입 또한 `Student`가 되어야 힌다.

```java
class Student implements Comparable<Student> {
	int age;			// 나이
	int classNumber;	// 학급
	
	Student(int age, int classNumber) {
		this.age = age;
		this.classNumber = classNumber;
	}
	
	@Override
	public int compareTo(Student o) {
     // 비교 구현
	}
}
```

<br/><br/>

## 이제 compareTo 메소드를 구현 하면 된다. (=주석 잘보기)

```java
class Student implements Comparable<Student> {
 
	int age;			// 나이
	int classNumber;	// 학급
	
	Student(int age, int classNumber) {
		this.age = age;
		this.classNumber = classNumber;
	}
	
	@Override
	public int compareTo(Student o) {

		// 만약 자신의 age가 o의 age보다 크다면 양수가 반환 될 것이고,
		// 같다면 0을, 작다면 음수를 반환할 것이다.
		return this.age - o.age;
	}
}
```

<br/><br/>

## 윗부분 전체 설명 코드

```java
public class Test {
	public static void main(String[] args)  {
		Student a = new Student(17, 2);	// 17살 2반
		Student b = new Student(18, 1);	// 18살 1반
		
		int isBig = a.compareTo(b);	// a자기자신과 b객체를 비교한다.
		
		if(isBig > 0) {
			System.out.println("a객체가 b객체보다 큽니다.");
		} else if(isBig == 0) {
			System.out.println("두 객체의 크기가 같습니다.");
		} else {
			System.out.println("a객체가 b객체보다 작습니다.");
		}
	}
}
 
class Student implements Comparable<Student> {
	int age;			// 나이
	int classNumber;	// 학급
	
	Student(int age, int classNumber) {
		this.age = age;
		this.classNumber = classNumber;
	}
	
	@Override
	public int compareTo(Student o) {
		return this.age - o.age;
	}
}
```

## Comparator

"두 매개변수 객체를 비교" 한다고 했다.

자기 자신이 아니라 파라미터(매개 변수)로 
들어오는 두 객체를 비교하는 것이다. 

```java
여기서 바로 Comparable과 차이가 발생하는 것이다.
```

<br/>

Comparator는 매개변수로 들어오는 두 객체를 비교하는 

것이기 때문에 당연히 매개변수가 두 개가 되는 것이다.

```java
import java.util.Comparator;	// import 필요

class Student implements Comparator<Student> {
	int age; // 나이
	int classNumber; // 학급
	
	Student(int age, int classNumber) {
		this.age = age;
		this.classNumber = classNumber;
	}
	
	@Override
	public int compare(Student o1, Student o2) {
		// 만약 o1의 classNumber가 o2의 classNumber보다 크다면 양수가 반환 될 것이고,
		// 같다면 0을, 작다면 음수를 반환할 것이다.
		return o1.classNumber - o2.classNumber;
	}
}
```