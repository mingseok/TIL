## 트랜잭션 AOP 주의 사항 - 초기화 시점

<br/>

## 이런 경우를 말하는 것이다.

```java
@PostConstruct
@Transactional
public void initV1() {
    // ... 생략
}
```

`@PostConstruct`와 `@Transactional` 을 함께 사용하면 트랜잭션이 적용되지 않는다.

왜냐하면 초기화 코드가 먼저 호출되고, 그 다음에 트랜잭션 AOP가 적용되기 때문이다. 

따라서, 초기화 시점에는 해당 메서드에서 트랜잭션을 획득할 수 없다.

<br/><br/>

## 대안은 무엇인가?

초기화 보다도 그 이후인 → 스프링이 컨테이너 다 만들고, AOP 다 만든 다음에 호출하게 하는 것이다. 

그것이 바로 `@EventListener`

`ApplicationReadyEvent` 이벤트를 사용하는 것이다.

```java
@EventListener(value = ApplicationReadyEvent.class)
@Transactional
public void init2() {
    boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
    log.info("Hello init ApplicationReadyEvent tx active={}", isActive);
}
```

<br/><br/>

>**Reference** <br/>[스프링 DB 2편 - 데이터 접근 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-db-2/dashboard)


<br/>

## 궁금증!

```java
왜 하필 초기화 시점에는 트랜잭션이 안 걸릴까?
```

프록시가 아직 안 씌워졌기 때문이다.

앞의 BeanDefinition 글에서 본 빈 생성 순서를 다시 보면 답이 나온다.

```java
1. BeanDefinition 등록
2. 객체 생성
3. 의존관계 주입
4. BeanPostProcessor 실행           <- AOP 프록시가 여기서 씌워진다
5. 초기화 콜백 (@PostConstruct)
```

<br/>

여기서 문제가 되는 것은 `4번과 5번의 관계` 다.

<br/>

`@PostConstruct` 는 `내가 만들어진 직후` 에 불린다.

그 시점에는 나 자신에 대한 프록시가 아직 컨테이너에 등록되기 전일 수 있다.

<br/>

그리고 더 중요한 것은 `this` 다.

```java
@PostConstruct
public void init() {
    save();          // this.save() 다
}
```

<br/>

앞의 트랜잭션 AOP 주의 사항 글에서 본 그대로다.

`this` 는 프록시가 아니라 진짜 객체이므로 프록시를 안 거친다.

<br/>

## 실제로 확인해보면

스프링 AOP로 같은 상황을 만들어 돌려봤다.

```java
class ConcreteCalc {
    public int sum(int n) { ... }

    public int outer(int n) {
        return sum(n);            // this.sum() 이다
    }
}
```

```java
  [AOP] outer 시작
  outer() 실행, 이제 내부에서 sum() 을 부른다
  [AOP] outer 끝
```

<br/>

`sum` 에는 부가 기능이 안 붙었다.

`@PostConstruct` 안에서 자기 메서드를 부르는 것도 정확히 같은 상황이다.

<br/>

## ApplicationReadyEvent 로 옮기면 되는 이유

```java
@EventListener(ApplicationReadyEvent.class)
public void init() {
    save();
}
```

<br/>

이건 두 가지가 다르다.

<br/>

첫째, 실행 시점이 훨씬 뒤다.

모든 빈이 만들어지고 프록시까지 다 씌워진 뒤에 발행되는 이벤트다.

<br/>

둘째, 부르는 주체가 다르다.

`@PostConstruct` 는 컨테이너가 `진짜 객체` 에게 직접 부른다.

`@EventListener` 는 이벤트 발행기가 `컨테이너에 등록된 빈` 을 찾아서 부른다.

그 등록된 빈이 곧 프록시다.

```java
@PostConstruct        -> 컨테이너가 진짜 객체의 메서드를 직접 부른다
@EventListener        -> 컨테이너에 등록된 프록시의 메서드를 부른다
```

<br/>

그래서 `@EventListener` 로 옮기면 메서드에 붙은 `@Transactional` 이 정상 동작한다.

앞의 `@EventListener` 글에서 본 `AOP를 포함해 완전히 초기화된 이후` 라는 말이 이 뜻이다.

<br/>

## 초기화 시점에 트랜잭션이 필요한 경우

실무에서 자주 있는 두 가지다.

```java
1. 테스트용 초기 데이터 넣기
2. 애플리케이션이 뜰 때 캐시를 미리 채우기
```

<br/>

둘 다 `ApplicationReadyEvent` 로 옮기면 해결된다.

<br/>

굳이 `@PostConstruct` 를 쓰고 싶다면, 트랜잭션이 필요한 부분을 다른 빈으로 빼면 된다.

```java
@Component
@RequiredArgsConstructor
public class DataInit {
    private final DataInitService service;    // 다른 빈

    @PostConstruct
    public void init() {
        service.save();                        // 프록시를 거친다
    }
}
```

<br/>

앞의 트랜잭션 AOP 주의 사항 글에서 본 `클래스를 분리한다` 와 같은 해법이다.

문제의 원인이 같으니 해법도 같다.

<br/>

## 조용히 실패한다는 것이 제일 무섭다

예외가 나지 않는다는 점이 이 문제의 성질을 말해준다.

```java
@PostConstruct
@Transactional
public void init() {
    memberRepository.save(new Member("a"));
    throw new RuntimeException();              // 롤백될까?
}
```

<br/>

롤백되지 않는다. 애초에 트랜잭션이 시작되지 않았기 때문이다.

<br/>

앞의 트랜잭션 AOP 주의 사항 글에서 본 로그 설정으로 확인할 수 있다.

```java
logging.level.org.springframework.transaction.interceptor=TRACE
```

<br/>

`Getting transaction for ...` 줄이 안 찍히면 트랜잭션 없이 돌고 있는 것이다.

`@Transactional` 을 붙였는데 롤백이 안 되면 이것부터 확인하면 된다.
