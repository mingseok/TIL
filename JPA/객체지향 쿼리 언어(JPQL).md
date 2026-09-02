## JPQL(Java Persistence Query Language)

- JPQL은 객체지향 쿼리 언어다.

    - 따라서 테이블을 대상으로 쿼리하는 것이 아니라, 
    
        엔티티 객체를 대상으로 쿼리한다.

- JPQL은 SQL을 추상화해서 특정 데이터베이스 SQL에 의존하지 않는다.
- JPQL은 결국 SQL로 변환된다. (중요)

<br/><br/>

## JPQL

- `JPA`를 사용하면 엔티티 객체를 중심으로 개발

- 문제는 `검색 쿼리`
- 검색을 할 때도 `테이블이 아닌 엔티티 객체를 대상으로 검색`
- 모든 `DB` 데이터를 객체로 변환해서 검색하는 것은 불가능
- 애플리케이션이 필요한 데이터만 `DB`에서 불러오려면 결국 `검색 조건`이 포함된 `SQL`이 필요
- `JPA`는 `SQL`을 추상화한 `JPQL`이라는 객체 지향 쿼리 언어 제공
- `SQL`과 문법 유사, `SELECT`, `FROM`, `WHERE`, `GROUP BY`, `HAVING`, `JOIN` 지원
- `JPQL`은 엔티티 객체를 대상으로 쿼리
    - `SQL`은 데이터베이스 테이블을 대상으로 쿼리





<br/><br/>

## JPQL 문법

```
select_문 :: =
   select_절
   from_절
   [where_절]
   [groupby_절]
   [having_절]
   [orderby_절]
```


<br/>

### 예시
```
select m from `Member` as m where m.`age` > 18
```

- 위의 `Member`가 엔티티 라는 것이다.

- 엔티티는 대소문자 구분한다.

- JPQL 키워드는 대소문자 구분하지 않는다. `(=SELECT, FROM, where)`

- 엔티티 이름 사용, 테이블 이름이 아니다. (@Entity의 이름을 말함)

- 별칭은 필수`(=m)` , `(=as는 생략가능하다)`

<br/><br/>

## 집합과 정렬

```sql
select
    COUNT(m),   // 회원수
    SUM(m.age), // 나이 합
    AVG(m.age), // 평균 나이
    MAX(m.age), // 최대 나이
    MIN(m.age)  // 최소 나이
from Member m
```

- `GROUP BY`, `HAVING` : 똑같이 사용가능하다.

- `ORDER BY` : 똑같이 사용가능하다.



<br/><br/>

## 코드 확인

```java
//검색
String jpql = "select m From Member m where m.name like ‘%hello%'";

List<Member> result = em.createQuery(jpql, Member.class).getResultList();
```


- 테이블이 아닌 객체를 대상으로 검색하는 객체 지향 쿼리

- SQL을 추상화해서 특정 데이터베이스 SQL에 의존X
- JPQL을 한마디로 정의하면 객체 지향 SQL이다.

<br/><br/>

## JPQL과 실행된 SQL 확인

```
나이가 18살 이상인 회원을 모두 검색하고 싶다면?
```

![이미지](/programming/img/입문379.PNG)




<br/><br/>

>**Reference** <br/>[자바 ORM 표준 JPA 프로그래밍 - 기본편](https://www.inflearn.com/course/ORM-JPA-Basic)


<br/>

## 궁금증!

```java
JPQL 이 SQL 과 정확히 무엇이 다른가
```

`무엇을 대상으로 쓰는가` 가 다르다.

```java
SQL  - 테이블과 컬럼을 대상으로 한다
JPQL - 엔티티와 필드를 대상으로 한다
```

<br/>

```java
SELECT m FROM Member m WHERE m.name = :name
```

<br/>

여기서 `Member` 는 테이블 이름이 아니라 엔티티 이름이다.

앞의 엔티티 매핑 글에서 본 `@Entity(name = "...")` 가 정하는 그 이름이다.

<br/>

`m.name` 도 컬럼 이름이 아니라 필드 이름이다.

컬럼이 `member_name` 이어도 JPQL에는 `name` 이라고 쓴다.

<br/>

## 그래서 DB 를 바꿔도 안 바뀐다

앞의 JPA 설정 - persistence.xml 글에서 본 방언이 이걸 처리한다.

```java
-- JPQL (항상 같다)
SELECT m FROM Member m ORDER BY m.age

-- MySQL
SELECT * FROM member ORDER BY age LIMIT 10 OFFSET 20

-- Oracle
SELECT * FROM (SELECT ROWNUM rn, ... ) WHERE rn BETWEEN 21 AND 30
```

<br/>

앞의 PSA 글에서 본 `기술이 바뀌어도 내 코드는 그대로` 가 여기에도 적용된다.

<br/>

## 그런데 완전히 독립적이지는 않다

JPQL에도 DB 문법이 새어 들어온다.

```java
SELECT FUNCTION('group_concat', m.name) FROM Member m
```

<br/>

`group_concat` 은 MySQL 함수다. 이걸 쓰는 순간 다른 DB로 못 옮긴다.

<br/>

앞의 MyBatis 설명, 설정 방법 글에서 본 것과 같은 상황이다.

편의를 위해 이식성을 포기하는 선택인 것이다.

<br/>

## SELECT m 이 엔티티를 돌려준다는 것

이게 SQL과 제일 크게 다른 점이다.

```java
List<Member> members = em.createQuery("SELECT m FROM Member m", Member.class)
        .getResultList();

members.get(0).changeName("바뀐이름");     // UPDATE 가 나간다
```

<br/>

앞의 영속성 컨텍스트 글에서 본 대로, 조회한 엔티티가 영속 상태가 된다.

그래서 변경 감지가 동작한다.

<br/>

앞의 @Query, 값, DTO 조회하기 글에서 본 DTO 조회는 이게 안 된다.

```java
SELECT new com.example.MemberDto(m.id, m.name) FROM Member m
```

<br/>

DTO는 엔티티가 아니라 영속성 컨텍스트가 관리하지 않는다.

```java
엔티티로 조회 - 변경 감지가 된다. 대신 필요 없는 컬럼까지 읽는다
DTO 로 조회   - 필요한 것만 읽는다. 대신 수정은 못 한다
```

<br/>

## 대상이 엔티티라서 생기는 제약

```java
SELECT * FROM member                  -- SQL 은 된다
SELECT * FROM Member m                -- JPQL 은 안 된다
```

<br/>

`*` 라는 개념이 없다. 엔티티 전체를 원하면 별칭을 쓴다.

```java
SELECT m FROM Member m
```

<br/>

그리고 별칭이 필수다. `FROM Member` 만 쓰면 안 된다.

엔티티를 가리킬 방법이 없기 때문이다.

<br/>

## 조인도 다르다

```sql
-- SQL : 조인 조건을 직접 적는다
SELECT * FROM member m JOIN team t ON m.team_id = t.id
```

```java
-- JPQL : 연관관계를 따라간다
SELECT m FROM Member m JOIN m.team t
```

<br/>

`ON` 절이 없다. `m.team` 이라고 적으면 매핑 정보에서 조인 조건을 알아낸다.

<br/>

앞의 연관관계 매핑 글에서 본 `@JoinColumn` 이 그 정보다.

객체 그래프를 따라가는 방식이라 조인 조건을 틀릴 일이 없다.

<br/>

## 그래서 나가는 SQL 을 봐야 한다

JPQL이 편한 만큼 실제 SQL이 안 보인다.

앞의 DB 접근(JPA) 글에서 본 `N+1` 이 그 대표적인 결과다.

```java
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=debug
```

<br/>

이 설정을 켜두는 것이 JPQL을 쓰는 전제 조건이라고 봐도 된다.

내가 쓴 JPQL 한 줄이 SQL 몇 줄이 되는지 모르면 성능을 다룰 수가 없다.
