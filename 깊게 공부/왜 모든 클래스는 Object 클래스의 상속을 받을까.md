## 왜 모든 클래스는 Object 클래스의 상속을 받을까?

가장 큰 이유는, Object 클래스에 있는 메소드들을 통해서 클래스의 기본적인 행동을 정의할 수 있기 때문이다.

`정의`한다고 할때는, Object 클래스가 제공하는 행동을 사용하거나 오버라이딩하여 원하는 방식으로 행동을 변경하는 것을 의미한다.

```java
객체지향 프로그래밍의 기본 원칙 중 하나인 상속과 다형성에 기인한다. 
```

<br/>

## 이러한 설계를 함으로써 이점은 뭘까?

### 표준화된 메서드

`Object` 클래스에는 모든 객체에서 공통적으로 사용되는 메서드가 정의되어 있다.

예를 들어, `toString()`, `equals()`, `hashCode()`와 같은 메서드가 포함되어 있다.

<br/>

이러한 메서드를 모든 클래스에서 오버라이딩하고 사용자 정의 클래스에 맞게

구현함으로써 객체의 동작을 커스터마이즈 할 수 있다.

<br/>

### 상속의 유연성

모든 클래스가 `Object` 클래스를 상속 받도록 함으로써, 모든 객체를 동일한 유형으로 처리하고 다룰 수 있다.

이것은 다형성을 가능하게 하며, 예를 들어 `Object` 클래스로 참조하는 변수를 사용하여 어떤 객체든 다룰 수 있다.

<br/>

### 자바의 최상위 클래스

`Object` 클래스는 자바의 모든 클래스 계층 구조의 최상위에 위치하며,

이것은 객체 지향 프로그래밍에서 모든 클래스를 연결하는 공통된 부분입니다.

<br/>

## 궁금증!

```java
extends를 적은 적이 없는데 어떻게 상속을 받는 것일까?
```

컴파일러가 대신 적어준다. 아무것도 없는 빈 클래스를 하나 만들어서 열어보면 바로 보인다.

```java
public class Plain {
}
```

<br/>

이것을 컴파일해서 `javap -c` 로 열어보면 이렇게 나온다.

```java
public class Plain {
  public Plain();
    Code:
       0: aload_0
       1: invokespecial  // Method java/lang/Object."<init>":()V
       4: return
}
```

<br/>

두 가지가 저절로 생겨 있다.

- 적지도 않은 기본 생성자가 만들어져 있다

- 그 생성자 첫 줄에서 `Object` 의 생성자를 부르고 있다

`extends` 를 안 적으면 컴파일러가 `extends Object` 를 넣고, 생성자 첫 줄에 `super()` 를 넣는다.

<br/>

## Object가 가진 것은 정확히 이만큼이다

```java
public class java.lang.Object {
  public java.lang.Object();
  public final native java.lang.Class<?> getClass();
  public native int hashCode();
  public boolean equals(java.lang.Object);
  protected native java.lang.Object clone();
  public java.lang.String toString();
  public final native void notify();
  public final native void notifyAll();
  public final void wait();
  public final void wait(long);
  public final void wait(long, int);
  protected void finalize();
}
```

`toString`, `equals`, `hashCode` 세 개만 아는 경우가 많은데 실제로는 이만큼이 딸려온다.

<br/>

## 이게 없으면 무엇이 안 되는가

거꾸로 생각해보면 이유가 더 잘 보인다.

<br/>

### 첫번째) 컬렉션에 아무것도 못 담는다

`ArrayList` 내부는 그냥 `Object[]` 배열이다.

모든 객체가 `Object` 라는 보장이 있으니까 무엇이든 담을 수 있는 것이다.

만약 공통 부모가 없다면 타입마다 컬렉션을 따로 만들어야 한다.

<br/>

### 두번째) equals와 hashCode의 약속이 깨진다

`HashMap` 은 키가 무슨 타입이든 `hashCode()` 를 불러서 자리를 정한다.

모든 객체가 그 메서드를 가지고 있다는 보장이 있으니까 가능한 것이다.

```java
Map<Object, String> map = new HashMap<>();   // 무엇을 넣든 hashCode()를 부를 수 있다
```

<br/>

### 세번째) synchronized를 아무 객체에나 못 건다

```java
synchronized (아무객체) {
    // ...
}
```

자바는 모든 객체가 락을 하나씩 달고 있다는 전제로 동작한다.

`wait()`, `notify()` 가 `Object` 에 들어 있는 이유가 이것이다.

특정 클래스만 락을 걸 수 있게 되어 있었다면 동기화 문법 자체가 지금 모양이 아니었을 것이다.

<br/>

## 인터페이스는 어떤가?

인터페이스는 `Object` 를 상속하지 않는다. 그런데도 이렇게 쓸 수 있다.

```java
List<String> list = new ArrayList<>();
list.toString();      // 인터페이스에는 toString이 없는데?
```

<br/>

인터페이스 타입으로 받았어도, 실제로 들어 있는 것은 결국 어떤 클래스의 인스턴스다.

그리고 그 클래스는 반드시 `Object` 를 상속받는다.

그래서 컴파일러가 인터페이스 타입에 대해서도 `Object` 의 메서드 호출을 허용해주는 것이다.

<br/>

## 참고로 배열도 Object다

```java
int[] numbers = new int[3];
Object object = numbers;      // 된다
System.out.println(numbers.getClass().getSimpleName());
```

`int[]` 안의 `int` 는 객체가 아니지만, `int[]` 라는 배열 자체는 객체다.

그래서 `getClass()` 도 되고 `Object` 타입 변수에 담기는 것이다.

```java
자바에서 객체라고 부를 수 있는 것은 전부 Object의 자손이다
```
