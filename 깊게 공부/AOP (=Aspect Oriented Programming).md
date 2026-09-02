## AOP (=Aspect Oriented Programming)

<br/>

## AOP(=관점 지향 프로그래밍)

`1000`개의 메서드에 시간 측정 로직을 붙여야 된다면..?

- 고민 끝에, 나오게 된 것이 `AOP`이다.

```java
"AOP란?"
-> "공통 관심 사항"이랑 "핵심 관심 사항"을 분리한 것이다.
    
    여러 객체에 공통으로 적용할 수 있는 기능을 분리해서
    개발자는 반복 작업을 줄이고 핵심 기능 개발에만 집중할 수 있다.
```

<br/><br/>

## 어떤, 코드를 보아하니..

- 기존 코드의 수정이 필요해 보이고,

- 코드 중복이 많다

이를 해결하기 위해서는? → `“프록시”`를 사용해야 한다.

<br/><br/>

## 프록시란?

자신이 클라이언트가 사용하려고 하는 실제 대상인 것처럼, 

위장해서 클라이언트의 `요청`을 받아주는 것이다. (대리인, 대리자 느낌)

![이미지](/programming/img/입문431.PNG)

참고 자료 : https://www.youtube.com/watch?v=hjDSKhyYK14

<br/><br/>

## 프록시 사용 목적은 뭐야?

- 클라이언트가 타겟에 `“접근하는 방법을 제어"`하기 위해서 → `프록시 패턴`

- 타겟에 `“부가적인 기능을 부여”`해주기 위해서 → `데코레이터 패턴`

<br/><br/>

## 상황 예시)

```java
"BasicCalculator 클래스"     : 반복문을 사용해서 계산한다.
"RecursiveCalculator 클래스" : 재귀를 사용해서 계산한다.
"XXXXCalculator 클래스"      : XXX 사용해서 계산한다.
```

`3가지`의 객체들은 자신만의 `핵심` 기능을 가진다.

여기서, `“실행시간 측정”` 이라는 코드가 도입되면 `“중복코드”`가 발생하게 되는 것이다.

이때, `“핵심 기능”`과 `“부가 기능”`이라는 관점을 분리하는 것이다.

- “부가 기능” : `“실행시간 측정”` -> `“횡단 관심사”`라고도 불린다.

- “핵심 기능” : 각각의 ‘반복문’, ‘재귀’, ‘XXX’

<br/><br/>

## AOP 좋은 점은?

위 처럼, `횡단 관심사`의 분리를 함으로써,

- 개발자는 `반복 작업`을 줄이고 `핵심 기능` 개발에만 집중할 수 있게 된다.

- `핵심 기능`의 코드를 수정하지 않으면서, `공통 기능`의 구현을 추가하는 것이다

<br/><br/>

## AOP를 구현하는 방법은?

- `컴파일 시점`에 코드에 공통 기능 삽입

- `클래스 로딩 시점`에 바이트 코드에 공통 기능 삽입
- `런타임 시점`에 프록시 객체를 생성하여 공통 기능 삽입

    - 스프링 같은 경우는? → `런타임 시점`이다.
<br/>

## 궁금증!

```java
"런타임 시점에 프록시 객체를 생성한다" 는 게 실제로 어떻게 생긴 객체일까?
```

스프링의 `ProxyFactory` 로 직접 만들어서 클래스 이름을 찍어봤다.

```java
ProxyFactory f1 = new ProxyFactory(new CalcImpl());   // 인터페이스가 있는 클래스
f1.addAdvice(new TimeAdvice());
Calc calc = (Calc) f1.getProxy();
System.out.println("프록시 클래스 = " + calc.getClass().getSimpleName());

ProxyFactory f2 = new ProxyFactory(new ConcreteCalc());  // 인터페이스가 없는 클래스
f2.addAdvice(new TimeAdvice());
System.out.println("프록시 클래스 = " + f2.getProxy().getClass().getSimpleName());
```

<br/>

### 결과

```java
--- 인터페이스가 있으면 ---
  프록시 클래스 = $Proxy0
  [AOP] sum 시작
  [AOP] sum 끝

--- 인터페이스가 없으면 ---
  프록시 클래스 = ConcreteCalc$$SpringCGLIB$$0
  [AOP] sum 시작
  [AOP] sum 끝
```

<br/>

프록시를 만드는 방식이 두 가지다.

```java
$Proxy0                      -> JDK 동적 프록시. 인터페이스를 구현해서 만든다
XXX$$SpringCGLIB$$0          -> CGLIB. 대상 클래스를 상속해서 만든다
```

<br/>

`JDK 동적 프록시` 는 자바가 기본으로 제공하는 기능이라 인터페이스가 있어야만 쓸 수 있다.

인터페이스가 없으면 흉내낼 모양이 없기 때문이다.

<br/>

`CGLIB` 은 대상 클래스를 상속받은 자식 클래스를 만들어서 메서드를 오버라이딩한다.

인터페이스가 없어도 되지만, 상속을 쓰기 때문에 `final` 클래스나 `final` 메서드에는 적용할 수 없다.

<br/>

스프링 부트는 기본적으로 `CGLIB` 을 쓴다.

인터페이스가 있어도 CGLIB으로 만드는데, 프록시 타입이 예측 가능해지기 때문이다.

```java
ProxyFactory f3 = new ProxyFactory(new CalcImpl());
f3.setProxyTargetClass(true);            // 강제로 CGLIB
// -> CalcImpl$$SpringCGLIB$$0
```

<br/>

앞의 `@Configuration` 글에서 본 `AppConfig$$SpringCGLIB$$0` 이 같은 원리로 만들어진 것이다.

<br/>

## 프록시라서 생기는 함정

이게 실무에서 제일 많이 걸리는 지점이다.

```java
class ConcreteCalc {
    public int sum(int n) { ... }

    public int outer(int n) {
        System.out.println("outer() 실행, 이제 내부에서 sum() 을 부른다");
        return sum(n);            // this.sum() 이다
    }
}
```

<br/>

`outer()` 를 프록시를 통해 부르면 어떻게 될까.

<br/>

### 결과

```java
  [AOP] outer 시작
  outer() 실행, 이제 내부에서 sum() 을 부른다
  [AOP] outer 끝
```

<br/>

`sum` 에 대한 `[AOP]` 줄이 없다.

`outer` 에는 AOP가 걸렸는데 그 안에서 부른 `sum` 에는 안 걸린 것이다.

<br/>

## 왜 안 걸리는가

프록시는 대상 객체를 감싸고 있는 별개의 객체다.

```java
클라이언트 -> 프록시 -> 진짜 객체
```

<br/>

`outer()` 호출은 프록시를 거치니까 AOP가 붙는다.

그런데 `outer()` 안에서 `sum()` 을 부르는 순간, 그 `this` 는 프록시가 아니라 진짜 객체다.

프록시를 거치지 않고 자기 자신의 메서드를 직접 부르는 것이다.

```java
프록시.outer()  -> [AOP] -> 진짜객체.outer()
                              -> this.sum()   <- 여기서 프록시를 지나치지 않는다
```

<br/>

이게 `@Transactional` 이 안 먹는 대표적인 이유다.

```java
@Service
public class OrderService {

    public void order() {
        save();              // 트랜잭션이 안 걸린다
    }

    @Transactional
    public void save() { }
}
```

<br/>

`order()` 를 부르면 `save()` 의 `@Transactional` 은 아무 일도 안 한다.

내부 호출이라 프록시를 안 거치기 때문이다.

<br/>

해결 방법은 클래스를 나누는 것이다.

```java
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderSaver orderSaver;    // 다른 빈으로 분리

    public void order() {
        orderSaver.save();                   // 프록시를 거친다
    }
}
```

<br/>

`private` 메서드에 `@Transactional` 을 붙여도 마찬가지로 안 먹는다.

CGLIB은 오버라이딩으로 프록시를 만드는데 `private` 메서드는 오버라이딩할 수 없기 때문이다.

```java
AOP 가 안 먹는 경우
  - 같은 클래스 안에서 자기 메서드를 부를 때
  - private 메서드
  - final 메서드 (CGLIB 이 오버라이딩 못 함)
```

<br/>

## 세 가지 구현 시점의 차이

원문에서 세 가지를 들었는데, 왜 스프링이 런타임을 택했는지가 여기서 나온다.

```java
컴파일 시점    - AspectJ. 컴파일할 때 코드를 직접 끼워 넣는다. 제일 강력하지만 별도 컴파일러가 필요하다
클래스 로딩 시점 - 클래스를 읽을 때 바이트코드를 고친다. JVM 실행 옵션을 따로 줘야 한다
런타임 시점     - 프록시 객체를 만들어 감싼다. 별도 도구가 필요 없다
```

<br/>

스프링은 이미 컨테이너가 객체를 만들어서 주입해주고 있다.

객체를 만드는 그 자리에서 프록시로 바꿔치기하면 되니, 추가 도구가 하나도 필요 없다.

<br/>

대신 위에서 본 제약이 생긴다.

메서드 호출을 가로채는 방식이라 `프록시를 거치는 호출` 에만 적용되는 것이다.

`AspectJ` 는 코드 자체를 고치기 때문에 내부 호출에도 걸리지만, 그만큼 준비가 번거롭다.
