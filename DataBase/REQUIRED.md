## REQUIRED

스프링은 다양한 트랜잭션 전파 옵션을 제공한다. 

전파 옵션에 별도의 설정을 하지 않으면 `REQUIRED` 가 기본으로 사용된다.

```
참고로 실무에서는 대부분 REQUIRED 옵션을 사용한다. 
```

<br/>

그리고 아주 가끔 `REQUIRES_NEW` 을 사용하고, 나머지는 거의 사용하지 않는다. 

그래서 나머지 옵션은 이런 것이 있다는 정도로만 알아두고 필요할 때 찾아보자.

<br/><br/>

## REQUIRED

가장 많이 사용하는 기본 설정이다. 기존 트랜잭션이 없으면 생성하고, 있으면 참여한다.

트랜잭션이 필수라는 의미로 이해하면 된다. (필수이기 때문에 없으면 만들고, 있으면 참여한다.)

- 기존 트랜잭션 없음: 새로운 트랜잭션을 생성한다.

- 기존 트랜잭션 있음: 기존 트랜잭션에 참여한다.

<br/><br/>

## REQUIRES_NEW

항상 새로운 트랜잭션을 생성한다.

- 기존 트랜잭션 없음: 새로운 트랜잭션을 생성한다.

- 기존 트랜잭션 있음: 새로운 트랜잭션을 생성한다.

<br/><br/>

## 트랜잭션 전파와 옵션

isolation , timeout , readOnly 는 트랜잭션이 처음 시작될 때만 적용된다. 

트랜잭션에 참여하는 경우에는 적용되지 않는다.

(’참여’ 한다는 뜻은 해당 트랜잭션을 그대로 따른다는 뜻이고, 

<br/>

동시에 같은 동기화 커넥션을 사용한다는 뜻이다.)

예를 들어서 REQUIRED 를 통한 트랜잭션 시작, REQUIRES_NEW 를 통한 트랜잭션 시작 시점에만 적용된다.


<br/><br/>

>**Reference** <br/>[스프링 DB 2편 - 데이터 접근 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-db-2/dashboard)


<br/>

## 궁금증!

```java
전파 옵션이 REQUIRED 와 REQUIRES_NEW 말고도 있을까?
```

일곱 개가 있다. 다만 실무에서 쓰는 것은 사실상 둘뿐이다.

```java
REQUIRED      - 있으면 참여, 없으면 새로 만든다            (기본값)
REQUIRES_NEW  - 항상 새로 만든다. 기존 것은 잠시 미뤄둔다
SUPPORTS      - 있으면 참여, 없으면 트랜잭션 없이 실행
NOT_SUPPORTED - 트랜잭션 없이 실행. 있으면 잠시 미뤄둔다
MANDATORY     - 반드시 있어야 한다. 없으면 예외
NEVER         - 있으면 안 된다. 있으면 예외
NESTED        - 중첩 트랜잭션. 부분 롤백이 가능하다
```

<br/>

`MANDATORY` 와 `NEVER` 는 규칙을 강제할 때 쓴다.

```java
@Transactional(propagation = Propagation.MANDATORY)
public void updateBalance(...) { ... }
```

<br/>

`이 메서드는 반드시 다른 트랜잭션 안에서 불려야 한다` 는 뜻이다.

혼자 부르면 예외가 나니, 실수로 단독 호출하는 것을 막을 수 있다.

<br/>

## NESTED 는 왜 잘 안 쓰이는가

이름만 보면 제일 유용해 보인다. 부분 롤백이 된다는 뜻이기 때문이다.

```java
outer 시작
  inner 시작 (세이브포인트 생성)
  inner 롤백 -> 세이브포인트까지만 되돌린다
outer 커밋   -> inner 이전 작업은 살아남는다
```

<br/>

그런데 두 가지 제약이 있다.

- JDBC의 세이브포인트 기능이 있어야 한다. 지원 안 하는 DB가 있다

- JPA에서는 쓸 수 없다. 하이버네이트가 세이브포인트 롤백을 지원하지 않는다

<br/>

JPA를 쓰는 프로젝트가 대부분이라 실질적으로 선택지에서 빠진다.

<br/>

## REQUIRES_NEW 는 커넥션을 두 개 쓴다

앞의 트랜잭션 전파 글에서 본 그 문제다.

```java
outer 트랜잭션 -> 커넥션 1 을 잡고 있는다
inner 트랜잭션 -> 커넥션 2 를 새로 잡는다
inner 가 끝날 때까지 두 개를 동시에 점유한다
```

<br/>

앞의 커넥션 풀 글에서 본 대로 커넥션은 개수가 정해져 있다.

풀 크기가 `10` 인데 `REQUIRES_NEW` 를 쓰는 요청이 `10` 개 동시에 들어오면,

바깥 트랜잭션이 커넥션을 다 차지한 상태에서 안쪽이 커넥션을 못 얻어 서로 기다리게 된다.

<br/>

이걸 `커넥션 풀 데드락` 이라고 한다. 락 때문이 아니라 자원 때문에 생기는 교착이다.

<br/>

## 그래서 실무 선택은 대개 이렇다

```java
기본적으로는 REQUIRED (아무것도 안 적는다)

트랜잭션을 나눠야 한다면
  -> 먼저 "정말 한 트랜잭션이 아니어야 하는가" 를 다시 본다
  -> 대개는 이벤트로 분리하는 편이 낫다

@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
```

<br/>

앞의 `@EventListener` 글에서 본 방식이다.

커밋된 뒤에 실행되니 애초에 같은 트랜잭션에 안 묶이고, 커넥션도 하나만 쓴다.

<br/>

## 옵션이 어디에 적혀 있는가

전파 옵션은 `TransactionDefinition` 인터페이스에 상수로 들어 있다.

```java
public interface TransactionDefinition {
    int PROPAGATION_REQUIRED = 0;
    int PROPAGATION_SUPPORTS = 1;
    int PROPAGATION_MANDATORY = 2;
    int PROPAGATION_REQUIRES_NEW = 3;
    int PROPAGATION_NOT_SUPPORTED = 4;
    int PROPAGATION_NEVER = 5;
    int PROPAGATION_NESTED = 6;
}
```

<br/>

앞의 PSA 글에서 본 `PlatformTransactionManager.getTransaction(TransactionDefinition)` 의

그 파라미터가 이것이다.

<br/>

`@Transactional` 에 적은 옵션들이 `TransactionDefinition` 하나로 묶여서 넘어가고,

각 DB 기술의 트랜잭션 매니저가 그걸 보고 자기 방식대로 처리하는 구조다.

```java
@Transactional(propagation = REQUIRES_NEW, isolation = READ_COMMITTED, timeout = 3)
  -> TransactionDefinition 하나로 묶인다
  -> JpaTransactionManager 가 받아서 JPA 방식으로 처리한다
```
