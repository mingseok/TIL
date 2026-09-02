## Querydsl vs JPQL, Q-Type 관례

<br/>

## 문법

- `EntityManager` 로 `JPAQueryFactory` 생성

- `Querydsl`은 `JPQL` 빌더

- `JPQL`은 문자(실행 시점 오류) 하지만 `Querydsl`은 컴파일 시점 오류를 알려준다는게 핵심

- `JPQL` : 파라미터 바인딩 직접, `Querydsl` : 파라미터 바인딩 자동 처리



```java
@Test
@DisplayName("JPQL 사용")
public void startJPQL() {
    String qlString =
            "select m " +
            "from Member m " +
            "where m.username = :username";

    Member findMember = em.createQuery(qlString, Member.class)
            .setParameter("username", "member1").getSingleResult();

    assertThat(findMember.getUsername()).isEqualTo("member1");
}


@Test
@DisplayName("Querydsl 사용")
public void startQuerydsl() {
    JPAQueryFactory queryFactory = new JPAQueryFactory(em); // 필드로 빼는식으로 사용
    QMember m = new QMember("m");

    Member findMember = queryFactory
            .select(m)
            .from(m)
            .where(m.username.eq("member1")) // //파라미터 바인딩 처리
            .fetchOne();

    assertThat(findMember.getUsername()).isEqualTo("member1");
}
```

<br/><br/>

## 위 코드에서 Q-Type 줄여쓰기

Q타입을 이렇게 많이 사용한다고 한다.

```java
@SpringBootTest
@Transactional
class MemberTest {

    JPAQueryFactory queryFactory;
    
    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);

        // ...생략
    }


@Test
@DisplayName("Querydsl 사용")
public void startQuerydsl() {
    Member findMember = queryFactory
            .select(member) // QMember.member 처음에 이렇게 했다가 `QMember`부분만 static 임포트 시키면 된다
            .from(member)
            .where(member.username.eq("member1"))
            .fetchOne();

    assertThat(findMember.getUsername()).isEqualTo("member1");
}
```





<br/><br/>

>**Reference** <br/>[실전! Querydsl](https://www.inflearn.com/course/querydsl-%EC%8B%A4%EC%A0%84?_gl=1*lhve3a*_ga*OTY2ODU2MjYxLjE2NzkwNjYzNDU.*_ga_85V6SRKGJV*MTY5MjcwODMyNi40Mi4xLjE2OTI3MDgzMzMuNTMuMC4w)


<br/>

## 궁금증!

```java
같은 쿼리를 둘로 써보면 차이가 어디서 나는가
```

정상 동작할 때는 차이가 별로 없다. 틀렸을 때 갈린다.

```java
// JPQL
em.createQuery("SELECT m FROM Member m WHERE m.usernaem = :name", Member.class)
        .setParameter("name", name)
        .getResultList();

// QueryDSL
queryFactory.selectFrom(member)
        .where(member.usernaem.eq(name))
        .fetch();
```

<br/>

`usernaem` 이라는 오타가 둘 다 있다.

```java
JPQL     - 실행할 때 QuerySyntaxException 이 난다
QueryDSL - 컴파일이 안 된다
```

<br/>

앞의 Querydsl 설정 글에서 본 `Q` 클래스가 그 차이를 만든다.

`QMember` 에 `usernaem` 이라는 필드가 없으니 컴파일러가 막는다.

<br/>

## 타입도 컴파일러가 검사한다

```java
// JPQL - 실행해야 안다
"SELECT m FROM Member m WHERE m.age LIKE '%30%'"

// QueryDSL - 컴파일 에러
member.age.like("%30%")      // NumberPath 에는 like 가 없다
```

<br/>

앞의 Querydsl 설정 글에서 본 대로 필드마다 타입에 맞는 객체가 생성된다.

```java
StringPath name          - like, contains, startsWith 가 있다
NumberPath<Integer> age  - goe, loe, between 이 있다
```

<br/>

## 동적 쿼리에서 차이가 제일 크다

앞의 MyBatis 설명, 설정 방법 글에서 본 그 문제다.

```java
// JPQL 로 동적 쿼리
String jpql = "SELECT m FROM Member m WHERE 1=1 ";
if (name != null) {
    jpql += " AND m.name = :name ";
}
if (age != null) {
    jpql += " AND m.age = :age ";
}
```

<br/>

`WHERE 1=1` 이라는 꼼수가 필요하고, 공백을 빠뜨리면 문법 오류가 난다.

파라미터 바인딩도 조건에 맞춰 따로 해야 한다.

<br/>

```java
// QueryDSL
queryFactory.selectFrom(member)
        .where(
                nameEq(name),        // null 이면 조건에서 빠진다
                ageEq(age)
        )
        .fetch();
```

<br/>

`where()` 에 `null` 을 넘기면 그 조건이 무시된다.

앞의 동적 쿼리 글에서 다루는 것이 이 방식이다.

<br/>

## 조건을 재사용할 수 있다

```java
private BooleanExpression nameEq(String name) {
    return name != null ? member.name.eq(name) : null;
}
```

<br/>

이 메서드를 다른 쿼리에서도 쓸 수 있다.

XML 조각을 재사용하려면 `<sql>` 과 `<include>` 를 써야 하는데,

자바 메서드는 그냥 부르면 된다.

<br/>

그리고 조합할 수도 있다.

```java
private BooleanExpression isActive() {
    return member.deleted.isFalse().and(member.age.goe(19));
}
```

<br/>

앞의 클래스와 메소드의 사용 이유는 글에서 본 `의미 있는 이름으로 묶는다` 가 여기에도 적용된다.

`deleted = false AND age >= 19` 보다 `isActive()` 가 읽기 낫다.

<br/>

## 그럼 JPQL 은 언제 쓰는가

조건이 고정이고 단순하면 JPQL이 짧다.

```java
@Query("SELECT m FROM Member m WHERE m.email = :email")
Optional<Member> findByEmail(@Param("email") String email);
```

<br/>

앞의 쿼리 메소드 글에서 본 순서를 이어보면 이렇게 된다.

```java
조건이 하나둘이고 고정   -> 메서드 이름으로 (findByEmail)
조건이 여럿이고 고정     -> @Query 로 JPQL
조건이 동적으로 바뀐다   -> QueryDSL
```

<br/>

셋을 한 리포지토리에서 섞어 쓰는 것이 실무의 보통 모습이다.

앞의 Spring Data JPA 분석 글에서 본 `Custom` 이나 별도 조회 클래스가 QueryDSL 자리다.

<br/>

## QueryDSL 도 결국 JPQL 을 만든다

```java
queryFactory.selectFrom(member).where(member.name.eq("민석")).fetch();
```

```java
SELECT m FROM Member m WHERE m.name = ?1
```

<br/>

앞의 Querydsl 설정 글에서 본 대로 `JPAQueryFactory` 가 `EntityManager` 를 받는 이유가 이것이다.

<br/>

그래서 JPQL의 제약이 그대로 따라온다.

앞의 JPQL - 페치 조인 글에서 본 `컬렉션 페치 조인 + 페이징` 문제도 QueryDSL에서 똑같이 생긴다.

`문법이 자바일 뿐 하는 일은 JPQL` 이라고 보면 정확하다.
