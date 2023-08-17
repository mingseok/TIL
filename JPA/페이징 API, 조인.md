
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

