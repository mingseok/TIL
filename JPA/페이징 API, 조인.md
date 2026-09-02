
## 페이징 API

- JPA는 페이징을 다음 두 API로 추상화

- `setFirstResult(int startPosition)` : 조회 시작 위치`(=0부터 시작)`
- `setMaxResults(int maxResult)` : 조회할 데이터 수

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

엔티티 중심! 헷갈리지 말자

- 내부 조인: `SELECT m FROM Member m [INNER] JOIN m.team t`

    - 이렇게 함으로써 쿼리 맨끝에 있는 `‘t’`를 사용할 수 있게 되는 것이다.
 
    - 즉, Member -> 'm'하고 team이 조인 된것이 't'라고 하는 것이다
       
- 외부 조인: `SELECT m FROM Member m LEFT [OUTER] JOIN m.team t`
    - `OUTER` 같은 경우는 생략이 가능하다.

    - `LEFT OUTER` 조인은 -> `LEFT` 조인이라고 많이 부른다. (`OUTER` 생략)
    
        - 테이블 상에 null을 표시하는 것

- 세타 조인: `select count(m) from Member m, Team t where m.username = t.name`
    - “막조인” 이라고 보통 부른다.

<br/><br/>

## 조인 - ON절

조인에 `ON`절이란 조인 할때~의 조건을 말한다

<br/>

예시) 조인 대상 필터링

```
회원과 팀을 조인하면서, 팀 이름이 `A`인 팀만 조인하고 싶을때
```


- JPQL : `SELECT m, t FROM Member m LEFT JOIN m.team t on t.name = 'A'`
    
    - 회원이랑 팀을 조인하는 것인데, `on`(ON절은 조인할때 조건을 말한다.)을 넣어

        `t.name = ‘A’` 즉, 팀 이름이 `‘A’`인 애만 `LEFT` 조인 한다는 뜻이 된다.

    <br/>

    실제로, SQL 동작은 밑에와 같다.
    
    ```sql
    SELECT m.*, t.* 
    FROM Member m 
    LEFT JOIN Team t 
    ON m.TEAM_ID = t.id and t.name='A'
    
    // m.TEAM_ID = t.id 코드는 
    // PK랑 FK랑 조인이 되어야 하기 때문이다
    ```
        
<br/>

### 연관관계 없는 엔티티 외부 조인 가능하다.

ex) 회원의 이름과 팀의 이름이 같은 대상 외부 조인이 가능하다

```sql
SELECT m, t FROM Member m LEFT JOIN Team t on m.username = t.name
```


<br/><br/>

>**Reference** <br/>[자바 ORM 표준 JPA 프로그래밍 - 기본편](https://www.inflearn.com/course/ORM-JPA-Basic)


<br/>

## 궁금증!

```java
setFirstResult 와 setMaxResults 가 DB 마다 어떤 SQL 이 되는가
```

앞의 JPA 설정 - persistence.xml 글에서 본 방언이 이걸 처리한다.

```java
em.createQuery("SELECT m FROM Member m ORDER BY m.age DESC", Member.class)
        .setFirstResult(20)
        .setMaxResults(10)
        .getResultList();
```

<br/>

```sql
-- MySQL
SELECT * FROM member ORDER BY age DESC LIMIT 10 OFFSET 20

-- Oracle (ROWNUM 을 두 번 감싸야 한다)
SELECT * FROM (
    SELECT ROWNUM rn, a.* FROM (
        SELECT * FROM member ORDER BY age DESC
    ) a WHERE ROWNUM <= 30
) WHERE rn > 20
```

<br/>

Oracle 쪽을 손으로 짜라고 하면 실수하기 딱 좋다.

이걸 안 적어도 되는 것이 방언의 이득이다.

<br/>

## OFFSET 이 뒤로 갈수록 느려지는 것

앞의 페이징과 정렬 글에서 본 그 문제가 JPQL에도 그대로 있다.

```sql
LIMIT 10 OFFSET 1000000     -- 100만 개를 읽고 버린 뒤 10개를 준다
```

<br/>

JPA를 쓰든 SQL을 직접 쓰든 DB가 하는 일은 같다.

앞의 페이징과 정렬 글에서 본 커서 방식으로 바꿔야 해결된다.

```java
SELECT m FROM Member m WHERE m.id > :lastId ORDER BY m.id
```

<br/>

앞의 DB 인덱스 글에서 본 대로 범위 조건은 인덱스를 탄다.

```sql
`--SEARCH member USING INDEX (id>?)
```

<br/>

## 컬렉션 페치 조인과 같이 쓰면 안 된다

앞의 JPQL - 페치 조인 글에서 본 그 경고다.

```java
HHH90003004: firstResult/maxResults specified with collection fetch; applying in memory
```

<br/>

`ToOne` 은 괜찮다. 행이 안 늘어나기 때문이다.

```java
SELECT o FROM Order o JOIN FETCH o.member       -- 페이징 가능
SELECT o FROM Order o JOIN FETCH o.orderItems   -- 페이징 불가
```

<br/>

## 조인 세 가지

```java
INNER JOIN  - 양쪽에 다 있는 것만
LEFT JOIN   - 왼쪽 것은 전부, 오른쪽은 있으면
THETA JOIN  - 연관관계 없이 조건으로 붙인다
```

<br/>

`INNER` 는 생략할 수 있다.

```java
SELECT m FROM Member m JOIN m.team t          -- INNER JOIN
SELECT m FROM Member m LEFT JOIN m.team t     -- LEFT JOIN
```

<br/>

앞의 JPQL - 경로 표현식 글에서 본 대로,

묵시적 조인은 항상 `INNER JOIN` 이라 팀이 없는 회원이 빠진다.

`LEFT JOIN` 이 필요하면 반드시 명시적으로 적어야 한다.

<br/>

## 세타 조인은 연관관계가 없어도 된다

```java
SELECT m FROM Member m, Team t WHERE m.name = t.name
```

<br/>

`Member` 와 `Team` 사이에 매핑이 없어도 이름으로 붙일 수 있다.

<br/>

앞의 RDBMS와 DBMS 차이는 글에서 본 대로,

`FROM A, B WHERE 조건` 은 곱집합에서 걸러내는 연산이다.

<br/>

`10만` × `10만` 이면 `100억` 행을 만들어놓고 거르는 것이라 아주 느리다.

실무에서는 거의 안 쓴다.

<br/>

## ON 절로 조인 조건을 추가할 수 있다

```java
SELECT m, t FROM Member m LEFT JOIN m.team t ON t.name = 'A팀'
```

<br/>

`WHERE` 와 다르다는 것이 중요하다.

```java
LEFT JOIN ... ON t.name = 'A팀'   -- 조인할 때 거른다. 회원은 전부 나온다
LEFT JOIN ... WHERE t.name = 'A팀' -- 조인 후 거른다. 팀이 없는 회원이 빠진다
```

<br/>

`LEFT JOIN` 을 써놓고 `WHERE` 로 오른쪽 테이블 조건을 걸면,

사실상 `INNER JOIN` 이 되어버린다. `null` 인 행이 조건에서 탈락하기 때문이다.

<br/>

이건 SQL에서도 똑같이 걸리는 함정이다.

`LEFT JOIN` 을 썼는데 결과가 예상보다 적으면 이걸 먼저 의심해보면 된다.
