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

