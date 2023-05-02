## DB 접근(JPA)

`JdbcTemplate`이나 `MyBatis`같은 `SQL` 매퍼 기술은 SQL을 개발자가 직접 작성해야 하지만, 

`JPA`를 사용하면 `SQL`도 `JPA`가 대신 작성하고 처리해준다.

<br/>

그리고 `JPA`를 더욱 편리하게 사용하기 위해 `스프링 데이터 JPA`와 `Querydsl`이라는 

기술을 함께 사용한다. → `중요한 것은 JPA`

```
간단하게 생각하자면, 'JPA'를 '자바 컬렉션'이라고 이해하자.
-> '자바 컬렉션'이 'DB'를 알아서 해결 해준다고 생각하기.
```

<br/><br/>

## ORM (Object-relational mapping(=객체 관계 매핑))

- 인터페이스이다.

- 객체는 객체대로 설계
- 관계형 데이터베이스는 관계형 데이터베이스대로 설계
- ORM 프레임워크가 중간에서 매핑

<br/><br/>

## JPA를 왜 사용해야 하는가?

- SQL 중심적인 개발에서 객체 중심으로 개발
- 생산성
- 유지보수
- 패러다임의 불일치 해결
- 성능
- 데이터 접근 추상화와 벤더 독립성

<br/><br/>

## JPA와 비교하기.

동일한 트랜잭션에서 조회한 엔티티는 같음을 보장.

```java
String memberId = "100";
Member member1 = jpa.find(Member.class, memberId);
Member member2 = jpa.find(Member.class, memberId);

두개를 비교 해보면,
member1 == member2 // 같다.
```

<br/><br/>

## JPA 성능 최적화 기능

`JPA`는 애플리케이션과 `DB`사이에 하나의 계층이 있는 것이다.

`‘계층’` 있다는 말은 2가지 기능이 있다.

- 캐시와 동일성 보장

```java
Member m1 = jpa.find(Member.class, memberId); // 1번째 SQL 적용
Member m2 = jpa.find(Member.class, memberId); // 2번째부터 캐시 적용 (1차 캐시라고 부름)

그리하여 SQL이 1번만 실행 된다는 것이다.
(m1 == m2) 를 조회하면 true가 나온다.
```

<br/><br/>

## 트랜잭션을 지원하는 쓰기 지연 (INSERT)

1. 트랜잭션을 커밋할 때까지 insert SQL을 모아 놓는다.
2. JDBC BATCH SQL 기능을 사용해서 한번에 SQL을 전송한다



코드로 설명하면 이렇다.

```java
transaction.begin(); // [트랜잭션] 시작

em.persist(memberA);
em.persist(memberB);
em.persist(memberC);
// -- 여기까지 insert sql을 데이터베이스에 보내지 않는다.

// 커밋하는 순간 데이터베이스에 inset SQL을 모아 놓은걸 보낸다.
transaction.commit(); // [트랜잭션] 커밋
```

<br/><br/>

## 지연 로딩과 즉시 로딩

- 지연 로딩 : 객체가 실제 사용될 때 로딩
- 즉시 로딩 : JOIN SQL로 한번에 연관된 객체까지 미리 조회