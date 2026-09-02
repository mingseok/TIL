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
<br/>

## 궁금증!

```java
JPA 를 쓰면 SQL 을 안 짜도 된다는데, 정말 몰라도 될까?
```

오히려 더 알아야 한다. 내가 안 짠 SQL이 나가기 때문이다.

<br/>

가장 흔하게 걸리는 것이 `N+1 문제` 다.

```java
List<Order> orders = orderRepository.findAll();      // SELECT * FROM orders  (1번)

for (Order order : orders) {
    System.out.println(order.getMember().getName()); // SELECT * FROM member WHERE id = ?  (N번)
}
```

<br/>

주문이 `100` 건이면 쿼리가 `101` 번 나간다.

코드에는 반복문 하나뿐인데 눈에 안 보이는 쿼리가 `100` 개 숨어 있는 것이다.

<br/>

앞의 DBCP 글에서 본 대로, 쿼리 하나마다 네트워크 왕복이 있다.

왕복이 `1ms` 면 `101` 번이 `101ms` 다.

<br/>

## 해결은 한 번에 가져오는 것이다

```java
@Query("SELECT o FROM Order o JOIN FETCH o.member")
List<Order> findAllWithMember();
```

<br/>

`JOIN FETCH` 를 쓰면 조인해서 한 번에 가져온다. 쿼리가 `1` 개가 된다.

<br/>

문제는 이걸 알아채기가 어렵다는 것이다.

개발할 때는 데이터가 `10` 건이라 안 느껴지다가, 운영에서 `10만` 건이 되면 터진다.

<br/>

그래서 쿼리를 보이게 해두는 설정이 필수다.

```java
spring.jpa.show-sql=true
logging.level.org.hibernate.SQL=debug
logging.level.org.hibernate.orm.jdbc.bind=trace     # 파라미터 값까지
```

<br/>

## 지연 로딩과 즉시 로딩

`N+1` 이 생기는 근원은 `언제 가져올 것인가` 다.

```java
@ManyToOne(fetch = FetchType.LAZY)    // 필요할 때 가져온다
@ManyToOne(fetch = FetchType.EAGER)   // 조회할 때 같이 가져온다
```

<br/>

`EAGER` 로 바꾸면 `N+1` 이 해결될 것 같은데 아니다.

`findAll()` 같은 경우 하이버네이트가 조인을 못 만들고 그냥 하나씩 조회한다.

<br/>

그리고 안 쓸 연관까지 매번 가져오니 더 느려진다.

```java
@ManyToOne 의 기본값은 EAGER 다      -> LAZY 로 바꿔야 한다
@OneToMany 의 기본값은 LAZY 다       -> 그대로 두면 된다
```

<br/>

`@ManyToOne` 과 `@OneToOne` 은 반드시 `LAZY` 로 명시하는 것이 정석이다.

<br/>

## 지연 로딩은 트랜잭션 밖에서 터진다

```java
@Transactional
public Order find(Long id) {
    return orderRepository.findById(id).orElseThrow();
}

// 컨트롤러에서
Order order = orderService.find(1L);
order.getMember().getName();      // 여기서 예외
```

<br/>

```java
LazyInitializationException: could not initialize proxy - no Session
```

<br/>

지연 로딩은 `필요할 때 DB 에 물어보는` 방식인데,

트랜잭션이 끝나면 커넥션이 반납되어 물어볼 수가 없다.

<br/>

그래서 앞의 DTO 글에서 본 대로 트랜잭션 안에서 DTO로 바꿔서 나가야 한다.

```java
@Transactional(readOnly = true)
public OrderResponse find(Long id) {
    Order order = orderRepository.findById(id).orElseThrow();
    return OrderResponse.from(order);      // 여기서 필요한 것을 다 꺼낸다
}
```

<br/>

엔티티를 컨트롤러까지 들고 나가면 안 되는 이유가 하나 더 늘어난 셈이다.

<br/>

## 변경 감지가 편하면서 무섭다

```java
@Transactional
public void updateName(Long id, String name) {
    Member member = memberRepository.findById(id).orElseThrow();
    member.changeName(name);
    // save() 를 안 불렀는데 UPDATE 가 나간다
}
```

<br/>

앞의 트랜잭션 옵션 글에서 본 스냅샷 비교가 이걸 해준다.

<br/>

편한데, 반대로 `의도하지 않은 UPDATE` 도 나간다.

```java
@Transactional
public OrderResponse find(Long id) {
    Order order = orderRepository.findById(id).orElseThrow();
    order.setViewCount(order.getViewCount() + 1);    // 조회 화면인데 값을 바꿨다
    return OrderResponse.from(order);                 // UPDATE 가 나간다
}
```

<br/>

`readOnly = true` 를 붙여두면 이런 실수를 막을 수 있다.

스냅샷이 없으니 변경 감지가 아예 안 돌기 때문이다.

<br/>

조회 메서드에 `readOnly` 를 붙이라는 것이 성능 때문만은 아닌 것이다.
