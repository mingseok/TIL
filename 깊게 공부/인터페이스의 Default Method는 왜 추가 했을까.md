## 인터페이스의 Default Method는 왜 추가 했을까?

인터페이스에서 몸통이 추가 된 메서드를 `Default Method`라고 한다.

즉, 메서드를 구현할 수 있다.

<br/>

또한 이를 구현한 클래스에서는 `Default Method`를 오버라이딩 할 수 있다

```java
pulbic interface MyInterface {
   default void Hello() {
       System.out.println("Hello World");
   }
}
```

<br/>

## 디폴트 메서드를 왜 추가 했을까?

인터페이스가 변경이 되면, 그 인터페이스를 구현하고 있는 모든 클래스들이 해당 메서드를 구현해야 되는 문제가 있다.

### 상황을 생각해보자.

```java
A본사(인터페이스)가 있고, A본사를 구현하고 있는 B지점(클래스), C지점(클래스)이 있다고 생각해보자.

-- 문제점 --
A본사 내부에서만 추가해야 될게 생겨 추가 하려고 하는데, A본사와 모든 지점인 B지점, C지점까지 추가되는 것이다.
```

이런 문제를 해결하기 위해, 인터페이스에서 메서드를 구현해 놓을 수 있도록 자바8에서 추가해 놓은 것이다.

<br/>

이런 문제를 해결 하기 위해 몸통이 { } 있는 메서드인 디폴트 메서드가 생긴 것이다. 

```java
필수!
메서드 앞에 무조건 default 키워드를 붙여 줘야 된다 → 생략 불가능
```

<br/>

## 디폴트 메서드를 사용하면 충돌이라는 문제점 발생.

```java
public class DefaultFoo implements Foo, Bar {

	  
}
```

만약, 인터페이스 2개가 있고, 두개의 인터페이스에서 똑같은게 있다면 어느 기준으로 

사용되게 하는 것이냐? 라고 질문 할 수 있다. → 이럴때는 다시 재정의해서 사용하면 그만인 것이다.

<br/>

충돌이 발생한다면 한가지만 생각하자.

```
직접, 오버라이딩 해주면 되는 것이다.
```

<br/>

## 예제) 주석 잘보기

```java
class MyCalTest {
    public static void main(String[] args) {
        MyCal myCal = new MyCal(); // 내가 만든 계산기

        int plus = myCal.plus(1, 3); // 추상
        int cal = myCal.exec(2, 5); // 디폴트 메서드

        System.out.println(cal); // 출력 : 7
    }
}

class MyCal implements Calculator {
    @Override
    public int plus(int a, int b) {
        return a + b;
    }

    @Override
    public int minus(int a, int b) {
        return a - b;
    }

		// 디폴트 메서드는 없어도 된다!
}

public interface Calculator {
    public int plus(int a, int b);

    public int minus(int a, int b);

    default int exec(int a, int b) {
        return a + b;
    }
}
```

<br/>

## 궁금증!

```java
A본사 이야기는 알겠는데, 자바가 실제로 겪은 그 상황은 무엇이었을까?
```

`Java 8` 에서 스트림과 람다를 넣으려던 것이 그 상황이었다.

<br/>

`list.stream()` 을 쓰려면 `stream()` 메서드가 `Collection` 인터페이스에 있어야 한다.

그런데 `Collection` 을 구현한 클래스가 세상에 몇 개나 될까.

JDK 안에만 수십 개고, 밖의 라이브러리와 회사 코드까지 치면 셀 수도 없다.

<br/>

`stream()` 을 추상 메서드로 추가했다면 그 전부가 컴파일 에러가 났을 것이다.

`Java 8` 로 올리는 순간 세상의 모든 컬렉션 구현체가 깨지는 셈이다.

<br/>

## 실제로 이렇게 들어갔다

```java
$ javap java.util.Collection

  public default boolean removeIf(java.util.function.Predicate<? super E>);
  public default java.util.Spliterator<E> spliterator();
  public default java.util.stream.Stream<E> stream();
  public default java.util.stream.Stream<E> parallelStream();
```

<br/>

```java
$ javap java.util.List

  public default void replaceAll(java.util.function.UnaryOperator<E>);
  public default void sort(java.util.Comparator<? super E>);
```

<br/>

전부 `default` 가 붙어 있다.

기존 구현체는 아무것도 안 고쳤는데 `stream()` 을 쓸 수 있게 된 것이다.

```java
default 메서드 = "이미 배포된 인터페이스에 메서드를 추가하기 위한 장치"
```

<br/>

원문의 `A본사` 비유가 정확히 이 상황이다. 자바가 그 본사였고, 지점이 너무 많았던 것이다.

<br/>

## 충돌은 오버라이딩으로 푼다고 했는데

맞다. 다만 그냥 오버라이딩만 하면 부모 쪽 구현을 못 쓴다.

`A본사의 것을 그대로 쓰고 싶다` 면 이런 문법이 있다.

```java
interface Foo {
    default String hello() { return "Foo"; }
}

interface Bar {
    default String hello() { return "Bar"; }
}

class DefaultFoo implements Foo, Bar {
    @Override
    public String hello() {
        return Foo.super.hello();     // Foo 쪽 구현을 쓰겠다
    }
}
```

<br/>

`Foo.super.hello()` 라는 문법이 `default` 메서드와 함께 생긴 것이다.

<br/>

참고로 오버라이딩을 안 하고 그냥 두면 이런 에러가 난다.

```java
error: types Foo and Bar are incompatible;
  class DefaultFoo inherits unrelated defaults for hello() from types Foo and Bar
```

컴파일러가 못 정하겠다고 손을 들면서, 정해달라고 요구하는 것이다.

<br/>

## Object의 메서드는 default로 못 만든다

```java
interface Bad {
    default String toString() { return "안 될걸"; }
}
```

<br/>

```java
error: default method toString in interface Bad overrides a member of java.lang.Object
```

<br/>

`toString`, `equals`, `hashCode` 는 `default` 로 만들 수 없다.

이유는 이런 상황을 막기 위해서다.

```java
어떤 클래스가 Bad 를 구현하면
-> Object 가 물려준 toString 과 Bad 가 물려준 toString 이 둘 다 있다
-> 어느 쪽을 부를지 정할 수 없다
```

<br/>

모든 클래스는 `Object` 를 상속받으니, 이 충돌은 예외 없이 항상 생긴다.

그래서 아예 문법으로 막아둔 것이다.

<br/>

## 그럼 default를 마음껏 써도 되는가

`default` 는 원래 `이미 배포된 인터페이스를 깨지 않고 고치기 위한` 장치다.

새로 만드는 인터페이스에 처음부터 구현을 잔뜩 넣으라는 뜻이 아니다.

<br/>

넣기 시작하면 인터페이스가 추상 클래스 흉내를 내게 되는데, 상태를 못 가지니 어정쩡해진다.

```java
default 로 넣기 좋은 것 -> 다른 추상 메서드를 조합해서 만들 수 있는 편의 메서드
                          (예: sort() 는 결국 get() 과 set() 으로 만들 수 있다)

넣지 말아야 할 것      -> 구현마다 달라야 하는 진짜 로직
```

<br/>

원문 예제의 `exec()` 도 이 기준으로 보면 애매하다.

```java
default int exec(int a, int b) {
    return a + b;
}
```

<br/>

`plus` 와 하는 일이 같은데 이름만 다르다.

`plus(a, b)` 를 불러주는 형태였다면 `조합해서 만든 편의 메서드` 가 되어 훨씬 자연스러웠을 것이다.

```java
default int exec(int a, int b) {
    return plus(a, b);      // 구현체가 정의한 plus 를 그대로 쓴다
}
```
