## 순수 JPA 리포지토리, Querydsl 비교

`memberJpaRepositoty` 클래스

```java
@Repository
public class MemberJpaRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public MemberJpaRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    /**
     * Querydsl로 변경
     */
    public List<Member> findAll_Querydsl() {
        return queryFactory
                .selectFrom(member).fetch();
    }
    
    

    public List<Member> findByUsername(String username) {
        return em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    /**
     * Querydsl로 변경
     */
    public List<Member> findByUsername_Querydsl(String username) {
        return queryFactory
                .selectFrom(member)
                .where(member.username.eq(username))
                .fetch();
    }
}
```


<br/><br/>

>**Reference** <br/>[실전! Querydsl](https://www.inflearn.com/course/querydsl-%EC%8B%A4%EC%A0%84?_gl=1*lhve3a*_ga*OTY2ODU2MjYxLjE2NzkwNjYzNDU.*_ga_85V6SRKGJV*MTY5MjcwODMyNi40Mi4xLjE2OTI3MDgzMzMuNTMuMC4w)


<br/>

## 궁금증!

```java
순수 JPA 로 짠 동적 쿼리와 QueryDSL 을 나란히 놓으면
```

같은 검색 기능을 두 방식으로 짜보면 차이가 확실하다.

```java
// 순수 JPA
public List<Member> search(String name, Integer ageGoe, Integer ageLoe) {
    String jpql = "SELECT m FROM Member m WHERE 1=1";

    if (StringUtils.hasText(name)) {
        jpql += " AND m.name = :name";
    }
    if (ageGoe != null) {
        jpql += " AND m.age >= :ageGoe";
    }
    if (ageLoe != null) {
        jpql += " AND m.age <= :ageLoe";
    }

    TypedQuery<Member> query = em.createQuery(jpql, Member.class);

    if (StringUtils.hasText(name)) {
        query.setParameter("name", name);      // 조건을 두 번 쓴다
    }
    if (ageGoe != null) {
        query.setParameter("ageGoe", ageGoe);
    }
    if (ageLoe != null) {
        query.setParameter("ageLoe", ageLoe);
    }

    return query.getResultList();
}
```

<br/>

같은 `if` 를 두 번 쓴다. 조건을 추가할 때마다 두 군데를 고쳐야 하고,

한 군데만 고치면 `파라미터가 없다` 는 예외가 실행할 때 난다.

<br/>

```java
// QueryDSL
public List<Member> search(String name, Integer ageGoe, Integer ageLoe) {
    return queryFactory.selectFrom(member)
            .where(nameEq(name), ageGoe(ageGoe), ageLoe(ageLoe))
            .fetch();
}
```

<br/>

앞의 동적 쿼리 글에서 본 방식이다. 조건과 바인딩이 한 자리에 있다.

<br/>

## 그래도 순수 JPA 를 알아야 하는 이유

QueryDSL이 만들어내는 것이 결국 JPQL이기 때문이다.

앞의 JPQL vs Querydsl 글에서 본 그 관계다.

```java
QueryDSL 코드 -> JPQL -> SQL
```

<br/>

그래서 JPQL의 제약이 그대로 따라온다.

```java
컬렉션 fetch join 은 하나만       (앞의 JPQL - 페치 조인 글)
컬렉션 fetch join + 페이징 불가    (앞의 페이징 API, 조인 글)
FROM 절 서브쿼리 불가              (앞의 서브 쿼리 글)
```

<br/>

이걸 모르면 QueryDSL에서 안 되는 이유를 못 찾는다.

`자바 문법이니까 될 것 같은데` 라고 생각하게 되기 때문이다.

<br/>

## 세 가지를 어떻게 나눠 쓰는가

앞의 쿼리 메소드 글과 JPQL vs Querydsl 글의 기준을 합치면 이렇게 된다.

```java
findByEmail(String email)                    -> 메서드 이름
@Query("SELECT m FROM Member m WHERE ...")   -> 조건이 고정이고 여럿일 때
QueryDSL                                     -> 조건이 동적일 때
```

<br/>

한 리포지토리에서 셋을 섞어 쓰는 것이 실무의 보통 모습이다.

<br/>

앞의 Spring Data JPA 분석 글에서 본 두 가지 방법으로 QueryDSL을 붙인다.

```java
1. MemberRepositoryCustom + MemberRepositoryImpl
2. 별도 조회 클래스 (MemberQueryRepository)
```

<br/>

`2번` 이 더 자유롭다. `Impl` 이름 규칙에 안 묶이고, 화면별로 나눌 수도 있다.

<br/>

## 순수 JPA 리포지토리를 남겨두는 경우

테스트나 학습 목적 외에도 이유가 있다.

```java
스프링 없이 JPA 만 쓸 때
스프링 데이터 JPA 의 동작을 정확히 통제하고 싶을 때
```

<br/>

앞의 스프링 데이터 JPA 설명 글에서 본 `save` 가 좋은 예다.

```java
if (entityInformation.isNew(entity)) {
    em.persist(entity);
} else {
    return em.merge(entity);
}
```

<br/>

식별자를 직접 넣는 경우에 `merge` 로 가서 `SELECT` 가 하나 더 나간다.

<br/>

순수 JPA면 이걸 직접 정할 수 있다.

```java
public void save(Member member) {
    em.persist(member);      // 항상 persist
}
```

<br/>

편의를 얻는 대신 통제를 내주는 것이 프레임워크의 성질이라,

가끔은 아래층으로 내려가야 할 때가 있다.

앞의 JDBC 표준 인터페이스 글에서 본 `위에 얹힌 것들이 전부 JDBC 를 쓴다` 와 같은 얘기다.
