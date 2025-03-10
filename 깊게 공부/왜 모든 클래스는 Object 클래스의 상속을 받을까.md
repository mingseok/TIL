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
