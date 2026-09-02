## DI 생성자 주입을 권장 하는 이유는?

<br/>

## 필드 주입의 단점

필드 주입으로 객체를 주입하면 외부에서 수정이 불가능하고, 

이러한 이유로 테스트 코드를 작성할 때 객체를 수정할 수 없게 됩니다.

<br/><br/>

## final 키워드의 사용

- 생성자 주입 사용 시 `final` 키워드 사용 가능하다

- `최고의 장점은` → 생성자에서 혹시라도 값이 설정되지 않는 오류를 컴파일 시점에 막아준다.

<br/><br/>

## 불변

- 대부분의 의존 관계는 어플리케이션 종료까지 변경될 일이 없다 → `불변`

- 수정자 주입을 사용하면, `setXxx` 메서드를 `public`으로 열어두어야 한다.

    - 실수로 변경 가능 → `좋은 방법이 아님`

- 생성자 주입은 객체를 생성할 때 딱 1번만 호출되므로 이후에 호출되는 일이 없다.
    - 따라서 `불변`하게 설계할 수 있다.

<br/><br/>

## 누락

- 프레임워크 없이 순수한 자바코드를 통해 단위 테스트하는 일이 많다.

- 수정자 주입을 사용하면 임의의 관련 객체를 만들어야 한다.

    - 누군가 실수로 변경할 수 도 있다.

    - 변경하면 안되는 메서드를 열어두는 것은 좋은 설계 방법이 아니다.

- 생성자 주입을 사용하면 누락을 막을 수 있다.
<br/>

## 궁금증!

```java
"테스트 코드를 작성할 때 객체를 수정할 수 없다" 는 게 실제로 어떤 모습일까?
```

같은 서비스를 두 가지 방식으로 만들어놓고, 컨테이너 없이 순수 자바로 만들어봤다.

```java
@Component
class CtorService {
    private final Repo repo;

    CtorService(Repo repo) {      // 생성자 주입
        this.repo = repo;
    }

    String run() { return repo.find(); }
}

@Component
class FieldService {
    @Autowired
    private Repo repo;            // 필드 주입

    String run() { return repo.find(); }
}
```

<br/>

```java
CtorService ctor = new CtorService(new Repo());   // 컴파일러가 강제한다
System.out.println(ctor.run());

FieldService field = new FieldService();          // 컴파일은 된다
System.out.println(field.run());
```

<br/>

### 결과

```java
생성자 주입 = 데이터
필드 주입   = NullPointerException (repo 가 null)
```

<br/>

생성자 주입은 `new CtorService()` 라고만 쓰면 컴파일이 안 된다.

무엇이 필요한지 컴파일러가 알려주니, 빠뜨릴 수가 없다.

<br/>

필드 주입은 컴파일이 그냥 된다. 실행해야 터진다.

`@Autowired` 는 컨테이너가 읽는 표시일 뿐이라, 컨테이너 밖에서는 아무 일도 안 일어난다.

```java
컨테이너 안에서는 둘 다 잘 된다
컨테이너 밖(단위 테스트)에서는 생성자 주입만 안전하다
```

<br/>

원문의 `누락을 막을 수 있다` 가 이 얘기다.

의존성이 하나 늘었을 때 생성자 주입이면 테스트 코드가 컴파일 에러로 알려주고,

필드 주입이면 테스트를 돌려봐야 `NullPointerException` 으로 알게 된다.

<br/>

## 더 큰 차이는 순환 참조에서 나온다

서로를 참조하는 빈 두 개를 두 방식으로 만들어봤다.

```java
// 생성자 주입
@Bean ACtor aCtor(BCtor b) { return new ACtor(b); }
@Bean BCtor bCtor(ACtor a) { return new BCtor(a); }

// 필드 주입
@Component class AField { @Autowired BField b; }
@Component class BField { @Autowired AField a; }
```

<br/>

### 결과

```java
--- 생성자 주입 + 순환참조 ---
UnsatisfiedDependencyException
Error creating bean with name 'aCtor': ...
Requested bean is currently in creation:
Is there an unresolvable circular reference?

--- 필드 주입 + 순환참조 ---
컨테이너가 그냥 떴다. A -> AField
```

<br/>

생성자 주입은 애플리케이션이 아예 안 뜬다.

`A` 를 만들려면 `B` 가 있어야 하고 `B` 를 만들려면 `A` 가 있어야 하니, 시작할 수가 없다.

<br/>

필드 주입은 그냥 뜬다.

빈을 먼저 만들어놓고 나중에 필드를 채우는 방식이라, 순서를 맞출 수 있기 때문이다.

<br/>

편해 보이지만 오히려 위험하다.

순환 참조는 대개 `두 클래스의 책임이 잘못 나뉘어 있다` 는 신호인데, 그걸 덮어버리기 때문이다.

<br/>

당장은 돌아가다가, 나중에 초기화 순서가 얽히거나 프록시가 끼면서 이상하게 터진다.

```java
생성자 주입 -> 설계가 잘못됐으면 뜨지도 않는다 (뜨는 순간 정상이라는 보장)
필드 주입   -> 잘못된 설계도 일단 뜬다 (문제를 나중으로 미룬다)
```

참고로 스프링 부트 `2.6` 부터는 순환 참조를 기본적으로 막아놨다.

필드 주입이어도 에러가 나고, 굳이 허용하려면 설정을 켜야 한다.

<br/>

## final을 붙일 수 있다는 것의 의미

원문의 `final 키워드 사용 가능` 이 그냥 문법 얘기가 아니다.

```java
class CtorService {
    private final Repo repo;    // 생성자 주입이면 가능

    CtorService(Repo repo) {
        this.repo = repo;
    }
}
```

<br/>

`final` 이 붙으면 컴파일러가 두 가지를 보장해준다.

- 생성자에서 반드시 값이 채워진다. 안 채우면 컴파일 에러

- 만들어진 뒤에는 절대 안 바뀐다

<br/>

앞의 불변 객체 글에서 본 것처럼, 안 바뀌는 것이 보장되면 동시성 문제도 사라진다.

스프링 빈은 싱글톤이라 여러 스레드가 동시에 쓰는데, 의존성이 `final` 이면 그 부분은 안심할 수 있다.

<br/>

필드 주입이나 수정자 주입은 `final` 을 붙일 수 없다.

객체를 먼저 만들고 나중에 값을 넣는 방식이라 `final` 과 애초에 맞지 않는다.

<br/>

## 그래서 요즘은 이렇게 쓴다

생성자가 하나뿐이면 `@Autowired` 도 생략할 수 있다.

```java
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final PayClient payClient;

    public OrderService(OrderRepository orderRepository, PayClient payClient) {
        this.orderRepository = orderRepository;
        this.payClient = payClient;
    }
}
```

<br/>

롬복을 쓰면 생성자도 안 적는다.

```java
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final PayClient payClient;
}
```

<br/>

`@RequiredArgsConstructor` 가 `final` 필드를 받는 생성자를 만들어준다.

앞의 롬복 글에서 `javap` 로 확인한 그 생성자다.

필드에 `final` 만 붙이면 생성자가 자동으로 따라오니, 생성자 주입이 오히려 제일 짧아진다.
