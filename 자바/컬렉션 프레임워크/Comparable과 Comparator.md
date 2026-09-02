## Comparable과 Comparator

## Comparable 인터페이스

Comparable 인터페이스에는 compareTo(T o)메소드 하나가 선언되어있는 것을 볼 수 있다. 

이 말은 우리가 만약 Comparable을 사용하고자 한다면, 

compareTo 메소드를 재정의(Override/구현)을 해주어야 한다는 것이다.

<br/><br/>

## Comparator 인터페이스

Comparator를 보면 선언 된 메소드가 많아서 어질할 수 있겠지만, 

우리가 실질적으로 구현해야 하는 것은 단 하나다. 바로 compare(T o1, T o2) 다.

<br/><br/>

## 자, 그럼 두 인터페이스의 차이점을 알고있다.

Comparable 인터페이스를 쓰려면 compareTo 메소드를 구현해야하고, 

Comparator 인터페이스를 쓰려면 compre 메소드를 구현해야 한다는 점이다.

<br/>

보통 많은 사람들의 경우 객체를 정렬을 하기 위해 쓴다고 한다만, 

정확히 말하자면 그 건 용도에 불과하다. 내가 생각해야 할 것은 딱 하나다.

"객체를 비교할 수 있도록 만든다."

<br/><br/>

## 왜 객체를 비교할 수 있도록 한다는 것일까?

생각해보면 우리는 primitive 타입의 실수 변수(byte, int, double 등등..)의 경우 

부등호를 갖고 쉽게 두 변수를 비교할 수 있었다.

<br/>

이런식으로 primitive type은 자바 자체에서 제공되기에 별다른 처리 없이 비교가 가능하다. 

즉, 기본 자료형이기 때문에 부등호로 쉽게 비교가 가능하다.

```java
public class Test {
    public static void main(String[] args)  {
 
        int a = 1;
        int b = 2;
		
        if(a > b) {
           System.out.println("a가 b보다 큽니다.");
        } else if(a == b) {
           System.out.println("a와 b는 같습니다.");
        } else {
           System.out.println("b가 a보다 큽니다. ");
        }
    }
}
```

하지만, 여러분들이 새로운 클래스 객체를 만들어 비교하고자 한다면 어떻게 될까? 

예로들어 학생의 나이와 학급 정보를 갖고있는 클래스를 만든다고 가정해보자.

<br/>

자, a학생과 b학생 두 객체를 생성했다.

그럼 두 객체(a, b)를 어떻게 비교할 것인가? 부등호로 비교하려 하면, 

나이(age)를 기준으로 비교되는 건가? 아니면 학급(classNumber)을 기준으로 비교되는 건가?

```java
public class Test {
    public static void main(String[] args)  {
 
		Student a = new Student(17, 2);	// 17살 2반
		Student b = new Student(18, 1);	// 18살 1반
		
		/*
		   어떻게 비교..?
		   
		   if(a > b) ..?
		 */
		
	}
}
 
class Student {
 
	int age;			// 나이
	int classNumber;	// 학급
	
	Student(int age, int classNumber) {
		this.age = age;
		this.classNumber = classNumber;
	}
}
```

<br/><br/>

## Comparable과 Comparator의 무슨 차이인 것일까?

왜 Comparable의 compareTo(T o) 메소드는 파라미터(매개변수)가 한 개이고, 

Comparator의 compare(T o1, T o2) 메소드는 파라미터가 왜 두 개인 것일까?

<br/>

일단, 두 인터페이스를 구체적으로 알아보기에 앞서 먼저 정답부터 말하자면, 

Comparable은 "자기 자신과 매개변수 객체를 비교" 하는 것이고, 

Comparator는 "두 매개변수 객체를 비교" 한다는 것이다.

<br/>

쉽게 말하자면, Comparable은 자기 자신과 파라미터로 들어오는 객체를 비교하는 것이고, 

Comparator는 자기 자신의 상태가 어떻던 상관없이 파라미터로 들어오는 두 객체를 비교하는 것이다. 

<br/>

즉, 본질적으로 비교한다는 것 자체는 같지만, 비교 대상이 다르다는 것이다.

<br/><br/>

## Comparable

아까 Comparable은 자기 자신과 매개변수 객체를 비교한다고 했다.

즉, 자기자신은 ClassName으로 생성한 객체 자신이 되고, 매개변수 객체는

ClassName.compareTo(o); 를 통해 들어온 파라미터 o가 비교 할 객체가 되는 것이다.

<br/><br/>

## 예로 들어보자.

아까 Student클래스를 비교하고자 했으니 이를 위 방법에 맞게 적용하려면 어떻게 해야할까?

일단, Student 클래스에 Comparable 을 implements 해야한다. 

<br/>

그리고 <> 사이에 들어갈 타입은 무엇일까? 

Student 객체와 또 다른 Student 객체를 비교하고 싶다면, <> 사이에 들어갈 

타입 또한 Student가 되어야 하지 않겠는가? 즉, Type 은 Student로 바뀌게 된다.

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
		/*
		 * 비교 구현
		 */
	}
}
```

<br/><br/>

## 이제 compareTo 메소드를 구현해야 할 것이다.

만약 나이를 기준으로 비교(대소 관계)를 하고자 한다면 어떻게 하면 될까?

자기 자신의 age(나이)와 매개변수로 들어온 o의 age(나이)의 값을 비교하면 된다.

<br/>

일단, 말로 설명하기 전에 코드로 먼저 보자면 이렇다.

compareTo 메소드를 보면 int값을 반환하도록 되어있다.

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
    
		// 자기자신의 age가 o의 age보다 크다면 양수
		if(this.age > o.age) {
			return 1;
		}
		// 자기 자신의 age와 o의 age가 같다면 0
		else if(this.age == o.age) {
			return 0;
		}
		// 자기 자신의 age가 o의 age보다 작다면 음수
		else {
			return -1;
		}
	}
}
```

<br/>

### 하지만, 양수 / 0 / 음수를 반환 하는 방법이 아닌 다른 방법도 있다

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
 
		/*
		 * 만약 자신의 age가 o의 age보다 크다면 양수가 반환 될 것이고,
		 * 같다면 0을, 작다면 음수를 반환할 것이다.
		 */
		return this.age - o.age;
	}
}
```

<br/>

전체 코드

```java
public class Test {
	public static void main(String[] args)  {
 
		Student a = new Student(17, 2);	// 17살 2반
		Student b = new Student(18, 1);	// 18살 1반
		
		int isBig = a.compareTo(b);	// a자기자신과 b객체를 비교한다.
		
		if(isBig > 0) {
			System.out.println("a객체가 b객체보다 큽니다.");
		}
		else if(isBig == 0) {
			System.out.println("두 객체의 크기가 같습니다.");
		}
		else {
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

<br/><br/>

## Comparable의 정리

1. 자기 자신과 매개변수를 비교한다.

2. compareTo 메소드를 반드시 구현해야한다.

<br/><br/>

## Comparator

"두 매개변수 객체를 비교"한다고 했다.

이 말은 자기 자신이 아니라 파라미터(매개 변수)로 들어오는 두 객체를 비교하는 것이다. 

여기서 바로 Comparable과 차이가 발생하는 것이다.

<br/>

이 때, 필수 구현 부분인 compare() 메소드가 바로 

우리가 객체를 비교할 기준을 정의해주는 부분이 된다. 

<br/>

앞서 말했듯, Comparable과 다르게 Comparator는 매개변수로 들어오는 두 객체를 

비교하는 것이기 때문에 당연히 매개변수가 두 개가 되는 것이다.

```java
import java.util.Comparator;	// import 필요
public class ClassName implements Comparator<Student> { 
 
/*
  ...
  code
  ...
 */
 
	// 필수 구현 부분
	@Override
	public int compare(Student o1, Student o2) {
		/*
		 비교 구현
		 */
	}
}
```

<br/>

구현은 이렇다.

```java
import java.util.Comparator;	// import 필요
class Student implements Comparator<Student> {
 
	int age;			// 나이
	int classNumber;	// 학급
	
	Student(int age, int classNumber) {
		this.age = age;
		this.classNumber = classNumber;
	}
	
	@Override
	public int compare(Student o1, Student o2) {
    
		// o1의 학급이 o2의 학급보다 크다면 양수
		if(o1.classNumber > o2.classNumber) {
			return 1;
		}
		// o1의 학급이 o2의 학급과 같다면 0
		else if(o1.classNumber == o2.classNumber) {
			return 0;
		}
		// o1의 학급이 o2의 학급보다 작다면 음수
		else {
			return -1;
		}
	}
}
```

<br/>

간략하게

```java
import java.util.Comparator;	// import 필요
class Student implements Comparator<Student> {
 
	int age;			// 나이
	int classNumber;	// 학급
	
	Student(int age, int classNumber) {
		this.age = age;
		this.classNumber = classNumber;
	}
	
	@Override
	public int compare(Student o1, Student o2) {
 
		/*
		 * 만약 o1의 classNumber가 o2의 classNumber보다 크다면 양수가 반환 될 것이고,
		 * 같다면 0을, 작다면 음수를 반환할 것이다.
		 */
		return o1.classNumber - o2.classNumber;
	}
}
```
<br/>

## 궁금증!

```java
(a, b) -> a - b 로 쓰면 안 되나
```

짧아서 자주 쓰는데, 오버플로가 난다.

```java
int a = Integer.MAX_VALUE;
int b = -1;
```

<br/>

### 결과

```java
a - b            = -2147483648   <- 음수다. a 가 작다는 뜻
Integer.compare  = 1             <- 양수다. a 가 크다는 뜻
```

<br/>

`2147483647 - (-1)` 은 `2147483648` 이어야 하는데 `int` 범위를 넘는다.

앞의 정수 오버플로 글에서 본 대로 한 바퀴 돌아 음수가 된다.

<br/>

비교기가 **반대로** 답한 것이다.

<br/>

## 그런데 정렬 결과는 맞게 나왔다

```java
뺄셈    = [-2147483648, -100, -1, 0, 5, 2147483000, 2147483647]
compare = [-2147483648, -100, -1, 0, 5, 2147483000, 2147483647]
같은가? true
```

<br/>

비교기가 틀렸는데 결과는 같다.

<br/>

원소 배치에 따라 그 잘못된 비교가 결과를 뒤집는 자리까지 안 갈 수도 있기 때문이다.

앞의 정렬(Sort) 글에서 본 `TimSort` 가 원소를 어떤 순서로 비교하느냐에 달렸다.

<br/>

이게 더 나쁘다.

테스트에서는 통과하고 운영 데이터에서만 틀리는 종류의 버그인 것이다.

<br/>

값이 항상 작으면 뺄셈도 문제가 없다. 그래서 몇 년을 모르고 지나가기도 한다.

<br/>

## 그러니 그냥 표준 메서드를 쓰면 된다

```java
Integer.compare(a, b)
Comparator.comparingInt(Member::getAge)
```

<br/>

`Integer.compare` 안은 뺄셈이 아니다.

```java
return (x < y) ? -1 : ((x == y) ? 0 : 1);
```

<br/>

비교만 하고 계산을 안 하니 오버플로가 날 수가 없다.

<br/>

## Comparable 과 equals 가 안 맞으면

이것도 조용히 깨지는 종류다.

```java
class Money implements Comparable<Money> {
    int amount;
    String where;

    public int compareTo(Money o) { return Integer.compare(amount, o.amount); }   // 금액만
    public boolean equals(Object o) { ... }                                        // 금액 + 장소
}
```

<br/>

```java
TreeSet<Money> set = new TreeSet<>();
set.add(new Money(1000, "지갑"));
set.add(new Money(1000, "통장"));
```

<br/>

### 결과

```java
TreeSet 크기 = 1   <- 하나가 안 들어갔다
HashSet 크기 = 2
```

<br/>

같은 데이터인데 `Set` 종류에 따라 크기가 다르다.

<br/>

앞의 Set 글에서 본 대로 두 `Set` 이 중복을 판단하는 기준이 다르기 때문이다.

```java
HashSet - hashCode() 와 equals() 로 판단
TreeSet - compareTo() 가 0 이면 같다고 본다
```

<br/>

`compareTo` 가 금액만 보니 `1000원` 이면 전부 같은 것으로 취급한 것이다.

<br/>

## 그래서 규약이 있다

```java
(x.compareTo(y) == 0) == x.equals(y)
```

<br/>

`compareTo` 가 0이면 `equals` 도 `true` 여야 한다는 뜻이다.

<br/>

자바 문서에도 적혀 있고, 안 지켜도 컴파일은 된다.

`강력히 권장한다` 는 표현으로 되어 있다. 강제할 방법이 없는 것이다.

<br/>

`BigDecimal` 이 유명한 위반 사례다.

```java
new BigDecimal("1.0").equals(new BigDecimal("1.00"))     -> false
new BigDecimal("1.0").compareTo(new BigDecimal("1.00"))  -> 0
```

<br/>

`equals` 는 소수점 자릿수까지 보고, `compareTo` 는 값만 본다.

그래서 `HashSet` 에는 둘 다 들어가고 `TreeSet` 에는 하나만 들어간다.

<br/>

앞의 BigDecimal 글에서 본 그 특성이 여기까지 영향을 준다.

<br/>

## Comparable 과 Comparator 를 언제 쓰나

```java
Comparable - 그 클래스의 "자연스러운 순서" 가 하나 있을 때. 클래스 안에 넣는다
Comparator - 정렬 기준이 여러 개일 때. 밖에서 만든다
```

<br/>

`String` 은 사전순이 자연스러운 순서라 `Comparable` 이 있다.

그런데 길이순으로 정렬하고 싶으면 `Comparator` 를 따로 만들어야 한다.

```java
list.sort(Comparator.comparingInt(String::length));
```

<br/>

`Member` 같은 도메인 객체는 자연스러운 순서가 없는 경우가 많다.

이름순인지 나이순인지 가입일순인지 정할 수가 없다.

<br/>

그럴 때는 `Comparable` 을 안 만드는 게 낫다.

억지로 하나 정해두면 나중에 다른 기준으로 정렬할 때 헷갈리기 때문이다.

<br/>

## 체이닝이 편하다

```java
list.sort(Comparator.comparing(Member::getGrade)
                    .thenComparing(Member::getName)
                    .reversed());
```

<br/>

등급으로 먼저, 같으면 이름으로, 전체를 뒤집는다.

<br/>

`reversed()` 가 맨 뒤에 붙으면 앞의 체인 전체를 뒤집는다는 게 헷갈리기 쉽다.

이름만 뒤집고 싶으면 그 자리에 붙여야 한다.

```java
.thenComparing(Comparator.comparing(Member::getName).reversed())
```

<br/>

`null` 이 섞여 있으면 터진다.

```java
Comparator.nullsFirst(Comparator.comparing(Member::getName))
```

<br/>

앞의 정렬, 페이징, 집합 글에서 본 QueryDSL의 `nullsLast` 와 같은 고민이다.

`null` 을 어디에 둘지는 결국 사람이 정해줘야 하는 것이다.
