## Java에서의 ‘역할’과 ‘구현’

<br/>

### 객체 지향 프로그래밍

프로그램을 유연하고 변경이 용이하게 사용할 수 있도록 해준다.

<br/>

### 이렇게 정의 할 수 있다.

- `레고` 블럭 조립하듯이

- `키보드`, `마우스` 갈아 끼우듯이
- 컴퓨터 `부품` 갈아 끼우듯이

<br/><br/>

## 다형성

```
클라이언트의 영향을 주지 않고, 새로운 기능을 제공할 수 있는것.
이게 가능한 이유는 '역할'과 '구현'으로 세상을 구분 하였기 때문이다.
```

핵심 포인트는 구현체를 여러개 구현할 수 있다는게 아니고,

새로운 자동차가 나와도 클라이언트는 새로운 것을 배우지 않아도 된다는 것이다.



<br/>

### 다형성의 실세계 비유

- 운전자 - 자동차
  
- 공연 무대
- 키보드, 마우스
- 정렬 알고리즘 (기능만 같다면, 성능이 더 좋은 알고리즘으로 교체)
- 할인 정책 로직(할인률은 자주 변경 되기 때문)

<br/><br/>

## ‘역할’과 ‘구현’으로 분리하자.

- `역할`과 `구현`으로 구분하면 세상이 단순해지고, 유연해지며 변경도 편리해진다.

<br/>

### 장점

- `클라이언트`는 대상의 `역할`(인터페이스)만 알면 된다. (로미오와 줄리엣 연극 생각)

- `클라이언트`는 구현 대상의 내부 구조를 몰라도 된다. (자동차 생각)
- `클라이언트`는 구현 대상의 내부 구조가 변경되어도 영향을 받지 않는다. (기름차 → 전기차)
- `클라이언트`는 구현 대상 자체를 변경해도 영향을 받지 않는다. (아반떼 → 제네시스)

<br/><br/>

## 자바 언어에서의 ‘역할’과 ‘구현’을 분리하면?

- 역할 == 인터페이스

- 구현 == 인터페이스로 구현한 클래스, 구현 객체

그리하여 객체를 설계할 때 `‘역할’`과 `‘구현’`을 명확히 분리해서 설계해야 된다.

<br/>

핵심은 구현보다 역할이 먼저라는 것이다. `(역할이 더 중요하다)`

```
설계할때 인터페이스를 안정적으로 가장 변화가 없게 설계하는 것이 가장 중요하다.
```

역할(인터페이스) 자체가 변경 된다면 -> 클라이언트, 서버 모두 변경이 발생한다.

- 자동차를 비행기로 변경해야 한다면?
  
- 대본 자체가 변경된다면?
- USB 인터페이스가 변경된다면?

<br/><br/>

## 객체의 ‘협력’이라는 관계부터 생각하기

- 클라이언트 : 요청하는 사람
- 서버 : 클라이언트의 요청에 응답하는 사람

이렇게 수많은 `클라이언트 객체`와 `서버 객체`가 서로 `협력 관계`를 가진다.

<br/><br/>

## 다형성의 본질

- 인터페이스를 구현한 객체 인스턴스를 `실행 시점`에 `유연`하게 변경할 수 있다.
  
- (핵심) 클라이언트를 변경하지 않고, 서버의 구현 기능을 유연하게 변경할 수 있다.

<br/><br/>

## 객체지향의 꽃은 다형성이다.

결국, 제어의 역전(IoC), 의존관계 주입(DI) 들은 다형성을 활용해서 

역할과 구현을 편리하게 다룰 수 있도록 지원할 기능일 뿐이다. (그 이상, 그 이하는 없다.)


<br/><br/>


>**Reference** <br/>[스프링 핵심 원리 - 기본편](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8?utm_source=google&utm_medium=cpc&utm_campaign=04.general_backend&utm_content=spring&utm_term=%EC%8A%A4%ED%94%84%EB%A7%81%20%EC%9E%85%EB%AC%B8&gclid=CjwKCAiAjPyfBhBMEiwAB2CCImohok2YrQ2tRdhqfr3cZvKqkIJOHUJ36u6s1-7C9X1gzZIapTvOtxoCangQAvD_BwE)

<br/>

## 궁금증!

```java
역할과 구현을 나누면 무엇이 실제로 편해지는가
```

말로는 알겠는데 체감이 안 돼서, 나눴을 때와 안 나눴을 때를 나란히 써봤다.

<br/>

```java
// 안 나눈 경우
public class OrderService {
    private final FixDiscountPolicy discountPolicy = new FixDiscountPolicy();
}
```

<br/>

할인 정책을 정률로 바꾸려면 이 줄을 고쳐야 한다.

`OrderService` 는 할인 계산이 어떻게 되는지에 관심이 없는데도 고치게 되는 것이다.

<br/>

```java
// 나눈 경우
public class OrderService {
    private final DiscountPolicy discountPolicy;

    public OrderService(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }
}
```

<br/>

이제 `OrderService` 는 안 고친다.

<br/>

앞의 SOLID 글에서 본 개방-폐쇄 원칙이 이걸 말한 것이다.

<br/>

## 그런데 누군가는 결정해야 한다

`new FixDiscountPolicy()` 가 사라진 게 아니라 다른 곳으로 옮겨간 것뿐이다.

```java
@Configuration
public class AppConfig {

    @Bean
    public OrderService orderService() {
        return new OrderService(discountPolicy());
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        return new FixDiscountPolicy();       // 여기로 왔다
    }
}
```

<br/>

앞의 IoC (=제어의 역전) 글에서 본 그 구성 영역이다.

<br/>

이렇게 모아두면 좋은 점이 있다.

```java
"이 시스템이 무엇으로 조립되어 있는가" 를 한 파일에서 볼 수 있다
```

<br/>

여기저기 흩어진 `new` 를 찾아다닐 필요가 없어지는 것이다.

<br/>

## 인터페이스가 항상 정답은 아니다

구현이 하나뿐인데 인터페이스를 만드는 경우가 있다.

```java
MemberService  (인터페이스)
MemberServiceImpl  (구현체 하나)
```

<br/>

`Impl` 하나만 있는 인터페이스는 이득이 별로 없다.

기능을 추가할 때마다 두 파일을 고쳐야 하고, 코드를 따라갈 때 한 번 더 건너뛰어야 한다.

<br/>

앞의 인터페이스를 왜 사용하는가 글에서 본 판단 기준을 다시 적으면 이렇다.

```java
구현이 바뀔 여지가 있나
테스트에서 가짜로 바꿔 끼울 일이 있나
외부 시스템과 붙는 자리인가
```

<br/>

셋 다 아니면 그냥 클래스 하나로 두는 게 낫다.

<br/>

## 실무에서 인터페이스가 확실히 필요한 자리

외부와 붙는 곳이다.

```java
PaymentGateway   - 토스, 아임포트, 나이스페이
FileStorage      - S3, 로컬 디스크
MessageSender    - 메일, 문자, 알림톡
```

<br/>

이런 것은 바꿀 일이 실제로 생긴다.

그리고 테스트에서 진짜로 결제를 하거나 메일을 보낼 수는 없으니 가짜가 필요하다.

<br/>

```java
class FakePaymentGateway implements PaymentGateway {
    @Override
    public PayResult pay(long amount) {
        return PayResult.success("test-key");     // 실제로 안 보낸다
    }
}
```

<br/>

앞의 스프링이 뭔가 글에서 본 대로

`운영에서는 진짜, 테스트에서는 가짜` 를 갈아 끼울 수 있는 것이 DI의 실질적인 이득이다.

<br/>

## 역할이 먼저 있고 구현이 나중이다

설계할 때 순서가 이렇게 된다.

```java
1. 무엇이 필요한가       -> 역할 (인터페이스)
2. 그것을 어떻게 하는가  -> 구현 (클래스)
```

<br/>

반대로 하면, 이미 만든 클래스에서 메서드를 뽑아 인터페이스를 만들게 된다.

그러면 그 클래스의 사정이 인터페이스에 그대로 새어나온다.

```java
public interface MemberRepository {
    Member findByIdWithJdbcTemplate(Long id);     // 구현 기술이 이름에 드러난다
}
```

<br/>

앞의 SOLID 글에서 본 인터페이스 분리 원칙과도 이어지는 얘기다.

역할을 먼저 정의하면 `무엇이 필요한지` 만 남고, 방법은 안 새어나온다.
