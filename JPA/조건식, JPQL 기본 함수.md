
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

>**Reference** <br/>[자바 ORM 표준 JPA 프로그래밍 - 기본편](https://www.inflearn.com/course/ORM-JPA-Basic)


<br/>

## 궁금증!

```java
JPQL 함수와 DB 함수 중 무엇을 쓸 수 있을까
```

JPQL이 표준으로 정한 것만 이식성이 보장된다.

```java
CONCAT, SUBSTRING, TRIM, LOWER, UPPER, LENGTH, LOCATE
ABS, SQRT, MOD
SIZE, INDEX
COALESCE, NULLIF
CURRENT_DATE, CURRENT_TIME, CURRENT_TIMESTAMP
```

<br/>

이 목록에 없는 것을 쓰려면 `FUNCTION` 으로 감싼다.

```java
SELECT FUNCTION('group_concat', m.name) FROM Member m
```

<br/>

앞의 객체지향 쿼리 언어(JPQL) 글에서 본 대로, 이 순간 특정 DB에 묶인다.

`group_concat` 은 MySQL 함수이고 PostgreSQL에는 `string_agg` 다.

<br/>

## COALESCE 와 NULLIF 가 유용하다

앞의 attribute data type 글에서 본 `NULL 이 SQL 을 까다롭게 만든다` 는 문제를 다루는 함수다.

```java
SELECT COALESCE(m.name, '이름없음') FROM Member m
```

<br/>

`name` 이 `null` 이면 `'이름없음'` 을 준다.

여러 개를 넘기면 `null` 이 아닌 첫 번째를 고른다.

```java
COALESCE(m.nickname, m.name, '익명')
```

<br/>

`NULLIF` 는 반대다. 두 값이 같으면 `null` 로 만든다.

```java
SELECT NULLIF(m.name, '관리자') FROM Member m
```

<br/>

집계에서 특정 값을 빼고 싶을 때 쓴다.

앞의 attribute data type 글에서 본 대로 `COUNT` 는 `null` 을 안 세기 때문이다.

```java
COUNT(NULLIF(m.status, 'DELETED'))     -- 삭제된 것 빼고 센다
```

<br/>

## SIZE 는 서브쿼리가 된다

```java
SELECT SIZE(t.members) FROM Team t
```

```sql
SELECT (SELECT COUNT(*) FROM member WHERE team_id = t.id) FROM team t
```

<br/>

앞의 JPQL - 경로 표현식 글에서 본 그 문제다.

팀이 `100` 개면 서브쿼리가 `100` 번 실행된다.

<br/>

목록 화면에서 개수를 보여줘야 한다면 `GROUP BY` 로 한 번에 세는 편이 낫다.

```java
SELECT t.name, COUNT(m) FROM Team t LEFT JOIN t.members m GROUP BY t.id
```

<br/>

## 문자열 함수의 인덱스 주의

앞의 DB 인덱스 글에서 확인한 그 문제가 여기서도 적용된다.

```java
SELECT m FROM Member m WHERE UPPER(m.email) = :email
```

```sql
`--SCAN member          -- 인덱스를 못 쓴다
```

<br/>

인덱스에는 원본 값이 정렬되어 있지 대문자로 바꾼 값이 정렬되어 있지 않기 때문이다.

<br/>

대소문자 구분 없이 찾아야 한다면 두 가지 방법이 있다.

```java
1. 저장할 때 소문자로 통일한다 (애플리케이션에서)
2. 함수 기반 인덱스를 만든다 (DB 가 지원하면)
```

<br/>

`1번` 이 대개 간단하다. 앞의 임베디드 타입 글에서 본 것처럼

`Email` 이라는 값 타입을 만들어서 생성자에서 소문자로 바꾸면 규칙이 한 곳에 모인다.

<br/>

## LIKE 도 마찬가지다

```java
WHERE m.name LIKE '%민%'      -- 인덱스를 못 쓴다
WHERE m.name LIKE '민%'       -- 인덱스를 쓴다
```

<br/>

앞의 DB 인덱스 글에서 확인한 결과다.

```sql
LIKE '%@test.com'  ->  `--SCAN member
LIKE 'user1%'      ->  `--SEARCH member USING INDEX (email>? AND email<?)
```

<br/>

`앞이 고정` 이면 범위 조건으로 바뀌어서 인덱스를 탄다.

<br/>

가운데나 뒤를 검색해야 한다면 인덱스로는 답이 없다.

전문 검색 엔진을 붙이거나, DB의 전문 검색 인덱스를 쓰는 쪽으로 간다.
