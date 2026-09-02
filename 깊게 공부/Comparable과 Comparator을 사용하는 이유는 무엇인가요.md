## Comparable과 Comparator을 사용하는 이유는 무엇인가요

<br/>

## 왜 사용하는가?

`int`형을 받아서 객체로 만드는 순간, 
우리 알지만 컴퓨터는 객체 상태로 포장되어서 모른다! 

그렇기 때문에 `equals()`, `hashCode()` 처럼 다시 재정의 해줘야 하는 것이다.

<br/>


```java
Comparable과 Comparator는 모두 인터페이스이다
```

- Comparable 인터페이스

    - 우리가 실질적으로 구현해야 하는 것은 단 하나다 → `compareTo(T o)`

- Comparator 인터페이스

    - 우리가 실질적으로 구현해야 하는 것은 단 하나다 → `compare(T o1, T o2)`

<br/>

## Comparable과 Comparator의 무슨 차이인 것일까?

- `Comparable`의 `compareTo(T o)` 메소드는 파라미터가 한 개이고,

- `Comparator`의 `compare(T o1, T o2)` 메소드는 파라미터가 두 개인 것일까?

```java
Comparable은 "자기 자신과 파라미터로 들어오는 객체를 비교"하는 것이고,
Comparator는 "자기 자신의 상태가 어떻던 상관없이," +
             "파라미터로 들어오는 두 객체를 비교" 하는 것이다.
```

<br/>

## 왜 객체를 비교할 수 있도록 한다는 것일까?

`primitive` 타입 변수의 경우, `‘==’` 가지고 쉽게 두 변수를 비교할 수 있었다.

`primitive` 타입은은 자바에서 제공되기에 별다른 처리 없이 비교가 가능하다. 

<br/>

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
      Student 민석 = new Student(17, 2);  // 17살 2반
      Student 다연 = new Student(18, 1);  // 18살 1반

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

<br/>

## Comparable 인터페이스

자기 자신은 `ClassName`으로 생성한 객체 자신이 되고, 매개변수 객체는 `ClassName.compareTo(o);` 를 

통해 들어온 파라미터 `o`가 비교 할 객체가 되는 것이다.

<br/>

## 예로 들어보자.

일단, `Student` 클래스에 `Comparable` 을 `implements` 해야한다. 그리고 `<>` 사이에 들어갈 타입은 무엇일까? 

`Student` 객체와 또 다른 `Student` 객체를 비교하고 싶다면, `<>` 사이에 들어갈 타입 또한 `Student`가 되어야 힌다.

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

<br/>

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

<br/>

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
   int age;  // 나이
   int classNumber;  // 학급

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

<br/>

## Comparator

"두 매개변수 객체를 비교" 한다고 했다.
자기 자신이 아니라 파라미터(매개 변수)로 들어오는 두 객체를 비교하는 것이다. 

```java
여기서 바로 Comparable과 차이가 발생하는 것이다.
```

<br/>

Comparator는 매개변수로 들어오는 두 객체를 비교하는 것이기 때문에 당연히 매개변수가 두 개가 되는 것이다.

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

<br/>

## 궁금증!

```java
"this.age - o.age" 이렇게 빼는 방식은 항상 안전할까?
```

안전하지 않다. 값이 크면 뒤집힌다.

```java
Student a = new Student(Integer.MAX_VALUE, 1);
Student b = new Student(-10, 2);

System.out.println("a 가 b 보다 커야 하는데 = " + a.compareTo(b));
System.out.println("Integer.compare 로 하면 = " + Integer.compare(a.age, b.age));
```

<br/>

### 결과

```java
a 가 b 보다 커야 하는데 = -2147483639
Integer.compare 로 하면 = 1
```

<br/>

`a` 가 훨씬 큰데 음수가 나왔다. `a` 가 더 작다는 뜻이 되어버린 것이다.

<br/>

`Integer.MAX_VALUE - (-10)` 은 `int` 가 담을 수 있는 범위를 넘어간다.

넘치면 반대쪽 끝으로 돌아가서 음수가 되는데, 그 값이 그대로 비교 결과로 쓰인 것이다.

```java
21억 + 10 -> int 범위를 넘는다 -> 음수로 뒤집힌다
```

<br/>

나이나 학급 번호처럼 작은 숫자면 티가 안 나지만, 타임스탬프나 금액을 비교할 때는 실제로 터진다.

그래서 뺄셈 대신 이렇게 쓰는 것이 안전하다.

```java
@Override
public int compareTo(Student o) {
    return Integer.compare(this.age, o.age);
}
```

`Integer.compare` 는 뺄셈을 하지 않고 크기만 비교해서 `-1`, `0`, `1` 을 돌려준다.

<br/>

## Comparator는 이제 이렇게 쓴다

원문 예제처럼 `Student` 가 직접 `Comparator` 를 구현하는 것은 좀 어색하다.

`나 자신` 과 `비교 기준` 이 한 클래스에 섞이기 때문이다.

<br/>

`Java 8` 부터는 만들어 쓸 필요 없이 조합해서 쓴다.

```java
List<Student> students = new ArrayList<>(List.of(
        new Student(18, 2), new Student(17, 1), new Student(18, 1)));

students.sort(null);                       // Comparable 순서 (나이)

students.sort(Comparator.comparingInt((Student s) -> s.classNumber)
        .thenComparingInt(s -> s.age));    // 반 -> 나이

students.sort(Comparator.comparingInt((Student s) -> s.age).reversed());
```

<br/>

### 결과

```java
나이순         = [1반 17살, 2반 18살, 1반 18살]
반 -> 나이순   = [1반 17살, 1반 18살, 2반 18살]
나이 역순      = [1반 18살, 2반 18살, 1반 17살]
```

<br/>

- `comparingInt` : 무엇을 기준으로 볼지만 알려준다

- `thenComparing` : 앞의 기준이 같을 때 다음 기준을 본다
- `reversed` : 순서를 뒤집는다

`sort(null)` 은 `Comparator 를 안 줄 테니 Comparable 순서로 해라` 라는 뜻이다.

<br/>

`반 -> 나이순` 결과를 보면 `1반 17살` 과 `1반 18살` 이 붙어 있다.

같은 반끼리 모은 다음, 그 안에서 나이로 다시 정렬한 것이다.

<br/>

## 그래서 둘을 이렇게 나눠 쓴다

```java
Comparable -> 그 클래스의 "기본 순서" 를 정한다. 클래스 안에 하나만 둘 수 있다
Comparator -> "이번에는 이렇게 정렬하고 싶다" 를 정한다. 필요한 만큼 만들 수 있다
```

<br/>

`String` 이 `Comparable` 을 구현해서 사전순이 기본 순서인 것처럼,

`가장 자연스러운 순서` 가 하나 있으면 `Comparable` 로 박아두면 된다.

<br/>

그런데 화면에서는 최신순으로 보여주고, 관리자 페이지에서는 이름순으로 보여줘야 한다면

그 두 개는 `Comparator` 로 따로 만드는 것이다.

<br/>

## compareTo와 equals는 결과가 같아야 한다

이게 잘 안 알려진 함정이다.

```java
class Student implements Comparable<Student> {
    @Override
    public int compareTo(Student o) {
        return Integer.compare(this.age, o.age);   // 나이만 본다
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Student s && age == s.age && classNumber == s.classNumber;
    }
}
```

<br/>

`equals` 는 나이와 반을 다 보는데 `compareTo` 는 나이만 본다.

그러면 `1반 18살` 과 `2반 18살` 이 `equals` 로는 다르고 `compareTo` 로는 같다.

<br/>

`HashSet` 은 `equals` 를 쓰니 둘 다 들어가고,

`TreeSet` 은 `compareTo` 를 쓰니 하나만 들어간다.

<br/>

같은 데이터를 담았는데 컬렉션 종류에 따라 개수가 달라지는 것이다.

```java
compareTo 가 0 을 돌려주는 두 객체는 equals 도 true 여야 한다
```

<br/>

기본 순서를 정할 때 `이 두 개를 같은 것으로 볼 것인가` 까지 생각해야 하는 이유다.

기준이 다를 수밖에 없다면 `Comparable` 을 구현하지 말고 `Comparator` 로만 두는 것이 낫다.
