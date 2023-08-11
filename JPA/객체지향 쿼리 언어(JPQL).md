## 객체지향 쿼리 언어(JPQL)

## JPQL(Java Persistence Query Language)

- JPQL은 객체지향 쿼리 언어다.

    - 따라서 테이블을 대상으로 쿼리하는 것이 아니라 엔티티 객체를 대상으로 쿼리한다.

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

## 코드 확인

```java
//검색
String jpql = "select m From Member m where m.name like ‘%hello%'";

List<Member> result = em.createQuery(jpql, Member.class).getResultList();
```

여기서 Member는 테이블을 말하는 것이 아니다. → 엔티티 Member를 말하고 있는 것이다.

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

## JPQL 문법

- select m from `Member` as m where m.`age` > 18

    - 엔티티와 속성은 대소문자 구분한다. `(=Member, age)`

    - 클래스명을 생각하면 대소문자로 작성하기 때문이다.
- JPQL 키워드는 대소문자 구분하지 않는다. `(=SELECT, FROM, where)`
- 엔티티 이름 사용, 테이블 이름이 아니다. `(=Member)`
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

## TypeQuery, Query

- TypeQuery: 반환 타입이 명확할 때 사용

- Query: 반환 타입이 명확하지 않을 때 사용

```sql
반환 타입이 명확할때 -
TypedQuery<Member> query = em.createQuery("SELECT m FROM Member m", Member.class);

반환 타입이 명확하지 않을때 - 
즉, 이건 'username'은 String타입이고, 'age'는 int타입이므로 명확하지 않은 것이다.
Query query = em.createQuery("SELECT m.username, m.age from Member m");
```

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

    - `SELECT new jpabook.jpql.UserDTO(m.username, m.age) FROM Member m`

- 패키지 명을 포함한 전체 클래스 명 입력
- 순서와 타입이 일치하는 생성자 필요

<br/><br/>

## 페이징 API

- JPA는 페이징을 다음 두 API로 추상화

- `setFirstResult`(int startPosition) : 조회 시작 위치`(=0부터 시작)`
- `setMaxResults`(int maxResult) : 조회할 데이터 수

<br/>

### 페이징 코드

```java
String jpql = "select m from Member m order by m.name desc";

List<Member> resultList = em.createQuery(jpql, Member.class)
      .setFirstResult(10)
      .setMaxResults(20)
      .getResultList();
```

<br/><br/>

## 조인

- 내부 조인: `SELECT m FROM Member m [INNER] JOIN m.team t`

    - 이렇게 함으로써 쿼리 맨끝에 있는 `‘t’`를 사용할 수 있게 되는 것이다.
 
    - 즉, Member -> 'm'하고 team이 조인 된것이 't'라고 하는 것이다
       
- 외부 조인: `SELECT m FROM Member m LEFT [OUTER] JOIN m.team t`
    - `OUTER` 같은 경우는 생략이 가능하다.
- 세타 조인: `select count(m) from Member m, Team t where m.username = [t.name](http://t.name/)`
    - “막조인” 이라고 보통 부른다.

<br/><br/>

## 조인 - ON절

1. 조인 대상 필터링
    - ex) 회원과 팀을 조인하면서, 팀 이름이 A인 팀만 조인하고 싶을때
    - JPQL : `SELECT m, t FROM Member m LEFT JOIN m.team t on t.name = 'A'`
        - 설명하자면, 두가지를 셀렉트 하는 것이다. → `m, t`

        - 그리고 from에서 Member를 가지고 오는데, LEFT JOIN 하여
        - 회원이랑 조인하는 것인데, on(ON절은 조인할때 조건을 말한다.)을 넣어
        - t.name = ‘A’ 즉, 팀 이름이 ‘A’인 애만 LEFT 조인 한다는 뜻이 된다.
        - 실제로, SQL 동작은 밑에와 같다.
        
        ```sql
        SELECT m.*, t.* FROM
        Member m LEFT JOIN Team t ON m.TEAM_ID = t.id and t.name='A'
        ```
        
<br/>

2. 연관관계 없는 엔티티 외부 조인 가능하다.
    - ex) 회원의 이름과 팀의 이름이 같은 대상 외부 조인이 가능하다
    - JPQL : 밑에 코드.
    
    ```sql
    SELECT m, t FROM
    Member m LEFT JOIN Team t on m.username = t.name
    ```


<br/><br/>

## 서브 쿼리

나이가 평균보다 많은 회원 

```sql
select m from Member m 
where m.age > (select avg(m2.age) from Member m2) -- 이렇게()를 통해 평균값을 만드는 것이다.
```

<br/>

한 건이라도 주문한 고객

```sql
select m from Member m
where (select count(o) from Order o where m = o.member) > 0
```

<br/><br/>

## 서브 쿼리 지원 함수

- `[NOT] EXISTS (subquery)`: 서브쿼리에 결과가 존재하면 참

    - {ALL | ANY | SOME} (subquery)

    - ALL 모두 만족하면 참
    - ANY, SOME: 같은 의미, 조건을 하나라도 만족하면 참
- `[NOT] IN (subquery)`: 서브쿼리의 결과 중 하나라도 같은 것이 있으면 참

<br/><br/>

## 서브 쿼리 예제)

팀A 소속인 회원

```sql
select m from Member m
where exists (select t from m.team t where [t.name](http://t.name/) = ‘팀A')
```

<br/>

전체 상품 각각의 재고보다 주문량이 많은 주문들

```sql
select o from Order o
where o.orderAmount > ALL (select p.stockAmount from Product p)
```

<br/>

어떤 팀이든 팀에 소속된 회원

```sql
select m from Member m
where m.team = ANY (select t from Team t)
```


<br/><br/>

## JPA 서브 쿼리 한계

- JPA는 WHERE, HAVING 절에서만 서브 쿼리 사용 가능

- SELECT 절도 가능(하이버네이트에서 지원)
- `FROM 절의 서브 쿼리는 현재 JPQL에서 불가능`
    - 조인으로 풀 수 있으면 풀어서 해결

<br/><br/>

## JPQL 타입 표현

- `문자`: ‘HELLO’, ‘She’’s’

- `숫자`: 10L(Long), 10D(Double), 10F(Float)
- `Boolean`: TRUE, FALSE
- `ENUM`: jpabook.MemberType.Admin (패키지명 포함)
- `엔티티 타입`: TYPE(m) = Member (상속 관계에서 사용)

<br/><br/>

## JPQL 기타

- SQL과 문법이 같은 식

- EXISTS, IN
- AND, OR, NOT
- =, >, >=, <, <=, <>
- BETWEEN, LIKE, IS NULL

<br/><br/>

## 조건식 - CASE 식

`COALESCE`: 하나씩 조회해서 null이 아니면 반환

`NULLIF`: 두 값이 같으면 null 반환, 다르면 첫번째 값 반환

<br/>



사용자 이름이 없으면 이름 없는 회원을 반환

```sql
select coalesce(m.username,'이름 없는 회원') from Member m
```

<br/>

사용자 이름이 ‘관리자’면 null을 반환하고 나머지는 본인의 이름을 반환

```sql
select NULLIF(m.username, '관리자') from Member m
```

<br/><br/>

## JPQL 기본 함수

- CONCAT

- SUBSTRING
- TRIM
- LOWER, UPPER
- LENGTH
- LOCATE
    - 해당 위치를 반환해준다.

- ABS, SQRT, MOD
- SIZE, INDEX(=JPA 용도, index는 그렇게 추천하지 않는다.)

<br/><br/>

>**Reference** <br/>[자바 ORM 표준 JPA 프로그래밍 - 기본편](https://www.inflearn.com/course/ORM-JPA-Basic)

