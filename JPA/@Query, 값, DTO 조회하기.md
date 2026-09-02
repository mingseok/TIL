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


<br/>

## 궁금증!

```java
DTO 로 조회할 때 new 를 왜 적어야 할까
```

JPQL은 엔티티를 다루는 언어라서, 엔티티가 아닌 것을 만들려면 알려줘야 하기 때문이다.

```java
@Query("SELECT new com.example.MemberDto(m.id, m.name, t.name) " +
       "FROM Member m JOIN m.team t")
List<MemberDto> findMemberDto();
```

<br/>

패키지 이름까지 전부 적어야 한다. 짧게 쓸 방법이 없다.

<br/>

그리고 생성자가 순서와 타입이 정확히 맞아야 한다.

```java
public MemberDto(Long id, String name, String teamName) { ... }
```

<br/>

하나라도 안 맞으면 애플리케이션이 뜰 때 걸린다.

앞의 쿼리 메소드 글에서 본 것처럼, 시작 시점에 검사해주는 것이 이득이다.

<br/>

## 인터페이스로 받는 방법도 있다

```java
public interface MemberProjection {
    Long getId();
    String getName();
}

List<MemberProjection> findAllProjectedBy();
```

<br/>

구현체를 안 만들어도 된다. 스프링이 프록시를 만들어서 채워준다.

앞의 스프링 데이터 JPA (사용 이유) 글에서 본 그 방식과 같다.

<br/>

이름이 맞으면 알아서 매핑된다.

```java
getName()  ->  엔티티의 name 필드
```

<br/>

이걸 `클로즈드 프로젝션` 이라고 하는데, 이점이 하나 있다.

필요한 컬럼만 `SELECT` 한다.

```sql
SELECT m.id, m.name FROM member m       -- 다른 컬럼은 안 읽는다
```

<br/>

앞의 DB 인덱스 글에서 본 대로 읽는 양이 줄어드니 그만큼 빠르다.

<br/>

## 그런데 계산이 들어가면 전부 읽는다

```java
public interface MemberProjection {
    @Value("#{target.name + ' (' + target.age + ')'}")
    String getDisplayName();
}
```

<br/>

이건 `오픈 프로젝션` 이다. 엔티티 전체를 읽어야 계산할 수 있다.

```sql
SELECT * FROM member       -- 전부 읽는다
```

<br/>

이름은 비슷한데 성능이 완전히 다르니 구분해서 써야 한다.

<br/>

## @Query 에 값 하나만 꺼낼 수도 있다

```java
@Query("SELECT m.name FROM Member m")
List<String> findAllNames();
```

<br/>

컬럼 하나만 필요하면 이게 제일 가볍다.

<br/>

여러 개면 `Object[]` 로 나오는데 이건 쓰기 불편하다.

```java
@Query("SELECT m.id, m.name FROM Member m")
List<Object[]> findIdAndName();      // 형변환을 직접 해야 한다
```

<br/>

앞의 제네릭이 필요한 이유는 글에서 본 `타입 안전성` 이 사라지는 자리다.

`Object[]` 를 쓸 바에는 DTO나 인터페이스로 받는 편이 낫다.

<br/>

## 파라미터 바인딩

```java
@Query("SELECT m FROM Member m WHERE m.name = :name")
Member findByName(@Param("name") String name);
```

<br/>

`:name` 처럼 이름으로 쓰는 것과 `?1` 처럼 순서로 쓰는 것 두 가지가 있다.

<br/>

이름 방식을 써야 한다. 앞의 JDBC 반복 문제 해결 글에서 본 이유와 같다.

파라미터 순서를 바꿨을 때 순서 방식은 조용히 잘못 들어간다.

<br/>

## 컬렉션도 넘길 수 있다

```java
@Query("SELECT m FROM Member m WHERE m.name IN :names")
List<Member> findByNames(@Param("names") List<String> names);
```

<br/>

`IN` 절에 목록을 통째로 넘긴다.

앞의 엔티티 조회 DTO 조회 글에서 본 `batch size` 가 만들어내는 SQL이 이 형태다.

<br/>

주의할 점이 두 가지 있다.

```java
빈 리스트를 넘기면 -> IN () 이 되어 문법 오류가 난다
너무 크면        -> DB 마다 IN 절 개수 제한이 있다 (Oracle 은 1000개)
```

<br/>

그래서 넘기기 전에 확인하는 편이 안전하다.

```java
if (names.isEmpty()) {
    return List.of();
}
```
