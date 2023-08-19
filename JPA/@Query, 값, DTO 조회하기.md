## @Query, 값, DTO 조회하기

<br/>

## @Query, 리포지토리 메소드에 쿼리 정의하기

```java
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where m.username= :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);
}
```

메소드 이름으로 쿼리 생성 기능은 파라미터가 증가하면 메서드 이름이 매우 지저분해진다.

따라서 `@Query` 기능을 자주 사용한다.


- 메서드 이름으로 생성하는 방법은 간단한곳에서 사용하고, 복잡해진다면 `@Query`를 사용하자


<br/><br/>

## DTO 조회하기

```java
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select new study.datajap.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();
}
```

- `new` 키워드를 사용해야 되는 것이다

- 패키지명들도 전부 작성해야 되는 것이다

    - `study.datajap.dto.MemberDto(m.id, m.username, t.name)`


<br/><br/>


## 컬렉션 파라미터 바인딩

Collection 타입으로 in절 지원

```java
@Query("select m from Member m where m.username in :names")
List<Member> findByNames(@Param("names") List<String> names);
```



<br/><br/>

>**Reference** <br/>[실전! 스프링 데이터 JPA](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%EB%8D%B0%EC%9D%B4%ED%84%B0-JPA-%EC%8B%A4%EC%A0%84?_gl=1*1x5vsec*_ga*OTY2ODU2MjYxLjE2NzkwNjYzNDU.*_ga_85V6SRKGJV*MTY5MjMyMTczNi40MC4xLjE2OTIzNDAwNDguNTIuMC4w)

