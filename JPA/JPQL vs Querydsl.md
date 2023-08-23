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

