

## TypeQuery, Query

- `TypeQuery` : 반환 타입이 명확할 때 사용

- `Query` : 반환 타입이 명확하지 않을 때 사용

<br/>

### 반환 타입이 명확할때 코드 

```
TypedQuery<Member> query = em.createQuery("SELECT m FROM Member m", Member.class);
```

위 코드를 보면 제네릭이 생기는걸 알 수 있다

<br/>

### 반환 타입이 명확하지 않을때 코드
```
Query query = em.createQuery("SELECT m.username, m.age from Member m");
```

즉, 이건 `username`은 String타입이고, `age`는 int타입이므로 명확하지 않은 것이다.

<br/><br/>

## 프로젝션

`SELECT` 절에 조회할 대상을 지정하는 것을 말하는 것이다.

- 프로젝션 대상: 엔티티, 임베디드 타입, 스칼라 타입(숫자, 문자등 기본 데이터 타입)

### 프로젝션 문법

- `SELECT m FROM Member m` -> "엔티티 프로젝션” 이라고 부름.
  
    - Member 엔티티를 조회한다는 얘기이다.
      
- `SELECT m.team FROM Member m` -> "엔티티 프로젝션”
    - Member에 연관된 team을 가져오는 것이다.
      
- `SELECT m.address FROM Member m` -> “임베디드 타입 프로젝션”
  
    - Member에 있는 값 타입인 address를 가져온다
- SELECT m.username, m.age FROM Member m -> “스칼라 타입 프로젝션”
- DISTINCT로 중복 제거

<br/><br/>

## 프로젝션 - 여러 값 조회

```
new 명령어로 조회하기
```

- 단순 값을 DTO로 바로 조회

    - 즉, 엔티티가 아니라 DTO 같은 다른 무언가로 가게 된다면 `new` 키워드를 사용해야 된다는 것이다


### 문법 

```
SELECT new jpabook.jpql.UserDTO(m.username, m.age) FROM Member m
```


- 패키지 명을 포함한 전체 클래스 명 입력

- 순서와 타입이 일치하는 생성자 필요



<br/><br/>

>**Reference** <br/>[자바 ORM 표준 JPA 프로그래밍 - 기본편](https://www.inflearn.com/course/ORM-JPA-Basic)


<br/>

## 궁금증!

```java
TypedQuery 와 Query 를 왜 나눠 놓았을까
```

반환 타입을 컴파일 시점에 알 수 있느냐로 갈린다.

```java
TypedQuery<Member> query = em.createQuery("SELECT m FROM Member m", Member.class);
Query query = em.createQuery("SELECT m.name, m.age FROM Member m");
```

<br/>

앞의 제네릭이 필요한 이유는 글에서 본 그 차이다.

```java
List<Member> members = typedQuery.getResultList();     // 형변환이 필요 없다
List result = query.getResultList();                   // Object[] 를 꺼내야 한다
```

<br/>

`Query` 를 쓰면 이렇게 된다.

```java
List<Object[]> result = query.getResultList();
for (Object[] row : result) {
    String name = (String) row[0];      // 손으로 형변환
    Integer age = (Integer) row[1];     // 순서를 틀리면 실행할 때 터진다
}
```

<br/>

앞의 제네릭이 필요한 이유는 글에서 본 `런타임에 터지던 것을 컴파일 타임으로 당겨왔다` 가

여기서는 반대로 후퇴하는 셈이다.

<br/>

## 그래서 여러 값이 필요하면 DTO 를 쓴다

```java
TypedQuery<MemberDto> query = em.createQuery(
        "SELECT new com.example.MemberDto(m.name, m.age) FROM Member m",
        MemberDto.class);
```

<br/>

앞의 @Query, 값, DTO 조회하기 글에서 본 그 방식이다.

타입이 살아 있으니 `row[0]` 같은 것을 안 써도 된다.

<br/>

## 프로젝션의 세 가지 대상

```java
엔티티   SELECT m FROM Member m           -- 영속성 컨텍스트가 관리한다
임베디드 SELECT m.address FROM Member m    -- 관리하지 않는다
스칼라   SELECT m.name FROM Member m       -- 그냥 값
```

<br/>

`엔티티 프로젝션` 만 영속 상태가 된다는 것이 핵심이다.

앞의 객체지향 쿼리 언어(JPQL) 글에서 본 그 차이다.

<br/>

## 연관 엔티티를 프로젝션하면 조인이 나간다

```java
SELECT m.team FROM Member m
```

<br/>

앞의 JPQL - 경로 표현식 글에서 본 묵시적 조인이다.

```sql
SELECT t.* FROM member m INNER JOIN team t ON m.team_id = t.id
```

<br/>

이것도 명시적으로 적는 편이 낫다.

```java
SELECT t FROM Member m JOIN m.team t
```

<br/>

## getSingleResult 는 조심해야 한다

```java
Member member = query.getSingleResult();
```

<br/>

결과가 없으면 예외, 둘 이상이어도 예외다.

```java
NoResultException              // 없을 때
NonUniqueResultException       // 둘 이상일 때
```

<br/>

`없다` 는 것이 예외 상황이 아닌 경우가 많은데도 예외가 난다.

앞의 exception VS error 글에서 본 대로, 정상적인 상황에 예외를 쓰면 코드가 지저분해진다.

```java
try {
    Member member = query.getSingleResult();
} catch (NoResultException e) {
    return null;                       // 이런 코드를 매번 써야 한다
}
```

<br/>

그래서 스프링 데이터 JPA는 이걸 감싸서 `Optional` 로 돌려준다.

```java
Optional<Member> findByEmail(String email);
```

<br/>

앞의 쿼리 메소드 글에서 본 반환 타입 규칙이 이 문제를 해결한 결과다.

`getResultList()` 로 받아서 첫 번째를 꺼내는 방법도 있다.

```java
List<Member> result = query.getResultList();
return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
```

<br/>

앞의 쿼리 메소드 글에서 본 대로 `getResultList` 는 절대 `null` 이 아니라 이 방식이 안전하다.
