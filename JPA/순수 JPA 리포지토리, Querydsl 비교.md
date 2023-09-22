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

