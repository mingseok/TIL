## JPQL - 페치 조인

- SQL 조인 종류가 아니다.

- JPQL에서 `성능 최적화`를 위해 제공하는 기능이다.
- 연관된 엔티티나 컬렉션을 SQL 한 번에 함께 조회하는 기능
- join fetch 명령어 사용
- 페치 조인 ::= [ LEFT [OUTER] | INNER ] JOIN FETCH 조인경로

<br/><br/>

## 엔티티 페치 조인

회원을 조회하면서 연관된 팀도 함께 조회한다.`(=SQL 한 번에)`

SQL을 보면 회원 뿐만 아니라 `팀(T.*)`도 함께 SELECT 한다.

- 장점은 페치 조인으로 회원과 팀을 함께 조회해서 지연 로딩X

- 조회를 할때 많이 사용한다.

```sql
-- JPQL 이렇게 사용
select m from Member m join fetch m.team

-- SQL
SELECT M.*, T.* FROM MEMBER M
INNER JOIN TEAM T ON M.TEAM_ID=T.ID
```

<br/><br/>

## 페치 조인 사용 코드

```sql
String jpql = "select m from Member m join fetch m.team";
List<Member> members = em.createQuery(jpql, Member.class).getResultList();

for (Member member : members) {
 //페치 조인으로 회원과 팀을 함께 조회해서 지연 로딩X
 System.out.println("username = " + member.getUsername() + ", " +
                    "teamName = " + member.getTeam().name());
}
```

<br/>

### 결과

```sql
username = 회원1, teamname = 팀A
username = 회원2, teamname = 팀A
username = 회원3, teamname = 팀B
```

<br/><br/>

## 페치 조인과 일반 조인의 차이

- JPQL은 결과를 반환할 때 연관관계 고려X

- 단지 SELECT 절에 지정한 엔티티만 조회할 뿐
- 여기서는 팀 엔티티만 조회하고, 회원 엔티티는 조회X
- 페치 조인을 사용할 때만 연관된 엔티티도 함께 조회`(=즉시 로딩)`
- `페치 조인은 객체 그래프를 SQL 한번에 조회하는 개념`

<br/><br/>

## 페치 조인 실행 예시

페치 조인은 연관된 엔티티를 함께 조회한다.

```sql
-- JPQL
select t
from Team t join fetch t.members
where t.name = '팀A'

-- SQL
SELECT T.*, M.*
FROM TEAM T
INNER JOIN MEMBER M ON T.ID=M.TEAM_ID
WHERE T.NAME = '팀A'
```

<br/><br/>

## 페치 조인의 특징과 한계

- `페치 조인 대상에는 별칭을 줄 수 없다.`

    - 하이버네이트는 가능, 가급적 사용X

- `둘 이상의 컬렉션은 페치 조인 할 수 없다.`
- `컬렉션을 페치 조인하면 페이징 API를 사용할 수 없다.`
    - 일대일, 다대일 같은 단일 값 연관 필드들은 페치 조인해도 페이징 가능하다

    - 하이버네이트는 경고 로그를 남기고 메모리에서 페이징`(=매우 위험)`
- 연관된 엔티티들을 SQL 한 번으로 조회 - 성능 최적화

- 엔티티에 직접 적용하는 글로벌 로딩 전략보다 우선함
    - @OneToMany(fetch = FetchType.LAZY) //글로벌 로딩 전략

- 실무에서 글로벌 로딩 전략은 모두 지연 로딩
- 최적화가 필요한 곳은 페치 조인 적용

<br/><br/>

## 페치 조인 - 정리

- 모든 것을 페치 조인으로 해결할 수 는 없음
- 페치 조인은 객체 그래프를 유지할 때 사용하면 효과적
- 여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 전혀 다른 결과를 내야 하면,
    
    페치 조인 보다는 일반 조인을 사용하고 필요한 데이터들만 조회해서 DTO로 반환하는 것이 효과적이다.


<br/><br/>

>**Reference** <br/>[자바 ORM 표준 JPA 프로그래밍 - 기본편](https://www.inflearn.com/course/ORM-JPA-Basic)



<br/>

## 궁금증!

```java
일반 조인과 페치 조인이 무엇이 다른가
```

`가져오는 것` 이 다르다. 조인 자체는 똑같이 한다.

```java
-- 일반 조인
SELECT m FROM Member m JOIN m.team t

-- 페치 조인
SELECT m FROM Member m JOIN FETCH m.team
```

<br/>

나가는 SQL을 비교하면 차이가 바로 보인다.

```sql
-- 일반 조인
SELECT m.* FROM member m INNER JOIN team t ON m.team_id = t.id

-- 페치 조인
SELECT m.*, t.* FROM member m INNER JOIN team t ON m.team_id = t.id
```

<br/>

`SELECT` 에 `t.*` 가 있느냐 없느냐다.

<br/>

일반 조인은 `조인은 하되 팀 데이터는 안 가져온다`.

그래서 나중에 `member.getTeam().getName()` 을 부르면 또 쿼리가 나간다.

앞의 DB 접근(JPA) 글에서 본 `N+1` 이 그대로 생기는 것이다.

<br/>

## 왜 이런 구분이 있는가

조인을 조건에만 쓰고 싶은 경우가 있기 때문이다.

```java
SELECT m FROM Member m JOIN m.team t WHERE t.name = 'A팀'
```

<br/>

`A팀 소속 회원` 을 찾는 것이 목적이지 팀 정보가 필요한 것은 아니다.

이때 팀까지 가져오면 그냥 낭비다.

<br/>

앞의 DB 인덱스 글에서 본 `안 쓸 데이터를 읽는 것은 낭비다` 가 여기에도 적용된다.

<br/>

## 페치 조인에는 별칭을 주지 않는 것이 원칙이다

```java
SELECT m FROM Member m JOIN FETCH m.team t WHERE t.name = 'A팀'    -- 위험하다
```

<br/>

문법상 되기는 하는데 결과가 이상해진다.

<br/>

`페치 조인` 은 `연관된 것을 전부 가져온다` 는 뜻인데,

`WHERE` 로 걸러내면 일부만 담긴 컬렉션이 나온다.

```java
team.getMembers()      // 실제로는 10명인데 3명만 들어 있다
```

<br/>

그리고 그 상태로 영속성 컨텍스트에 올라간다.

앞의 영속성 컨텍스트 글에서 본 1차 캐시에 `잘못된 데이터` 가 들어가는 것이다.

다른 곳에서 같은 팀을 조회하면 그 3명짜리가 나온다.

<br/>

`ToOne` 관계면 상관없다. 컬렉션이 아니라 걸러낼 것이 없기 때문이다.

<br/>

## 컬렉션 페치 조인은 중복이 생긴다

```java
SELECT t FROM Team t JOIN FETCH t.members
```

```sql
SELECT t.*, m.* FROM team t INNER JOIN member m ON t.id = m.team_id
```

<br/>

팀 하나에 회원이 `3` 명이면 결과가 `3` 행이다.

그러면 `Team` 객체가 `3` 개 나온다. 전부 같은 팀인데도 그렇다.

<br/>

`DISTINCT` 를 붙이면 해결된다.

```java
SELECT DISTINCT t FROM Team t JOIN FETCH t.members
```

<br/>

SQL의 `DISTINCT` 만으로는 안 된다. 회원이 다르니 행이 전부 다르기 때문이다.

JPQL의 `DISTINCT` 는 SQL에 붙이는 것 외에 `같은 식별자의 엔티티를 하나로 합치는` 일도 한다.

<br/>

`Hibernate 6` 부터는 이게 기본 동작이 되어서 `DISTINCT` 를 안 써도 된다.

버전에 따라 다르니 로그로 확인해보는 편이 확실하다.

<br/>

## 컬렉션 페치 조인은 페이징이 안 된다

앞의 페이징과 정렬 글에서 본 그 경고다.

```java
HHH90003004: firstResult/maxResults specified with collection fetch; applying in memory
```

<br/>

행 수와 엔티티 수가 안 맞으니 DB에서 자를 수가 없다.

그래서 전부 읽어와서 메모리에서 자른다. `100만` 건이면 `100만` 건을 다 읽는다.

<br/>

해결은 앞의 엔티티 조회 DTO 조회 글에서 본 순서를 따른다.

```java
ToOne 은 fetch join 한다          -> 행이 안 늘어난다
컬렉션은 batch size 로 푼다        -> N+1 이 1+1 이 된다
```

<br/>

## 페치 조인은 하나만 할 수 있다는 것

컬렉션 기준이다. `ToOne` 은 여러 개 해도 된다.

```java
SELECT o FROM Order o
JOIN FETCH o.member          -- ToOne. 된다
JOIN FETCH o.delivery        -- ToOne. 된다
JOIN FETCH o.orderItems      -- 컬렉션. 여기까지는 된다
JOIN FETCH o.coupons         -- 컬렉션 두 번째. 안 된다
```

```java
MultipleBagFetchException: cannot simultaneously fetch multiple bags
```

<br/>

`3 × 2 = 6` 행이 되어 어느 것이 진짜인지 구분할 수 없기 때문이다.

<br/>

참고로 `List` 대신 `Set` 을 쓰면 이 예외를 피할 수는 있다.

`bag` 이 아니라 `set` 이 되어 중복이 자동으로 제거되기 때문이다.

<br/>

다만 곱집합 자체는 그대로라, 읽어오는 행 수는 여전히 많다.

근본 해결은 아니고 앞의 `batch size` 쪽이 낫다.
