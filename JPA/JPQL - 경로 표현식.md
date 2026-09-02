## JPQL - 경로 표현식

<br/>

## .(점)을 찍어 객체 그래프를 탐색하는 것

```sql
select m.username -> 상태 필드라고 한다.
 from Member m
 join m.team t -> 단일 값 연관 필드라고 한다.
 join m.orders o -> 컬렉션 값 연관 필드라고 한다.(=orders가 컬렉션이다)
where t.name = '팀A'
```

- 상태 필드: 단순히 값을 저장하기 위한 필드 `(ex: m.username)`

- 연관 필드: 연관관계를 위한 필드
    - 단일 값 연관 필드: `@ManyToOne`, `@OneToOne`, 대상이 엔티티 `(ex: m.team)`

    - 컬렉션 값 연관 필드: `@OneToMany`, `@ManyToMany`, 대상이 컬렉션 `(ex: m.orders)`

<br/><br/>

## 경로 표현식 특징

- 상태 필드: 경로 탐색의 끝, 탐색이 불가능하다.

- 단일 값 연관 경로: `묵시적 내부 조인 발생`, 탐색이 가능하다
- 컬렉션 값 연관 경로: 묵시적 내부 조인 발생, 탐색X
    - FROM 절에서 명시적 조인을 통해 별칭을 얻으면 별칭을 통해 탐색 가능

```
하지만, 묵시적 조인이 발생하는 것은 현업에서 사용하면 안된다.
```

<br/><br/>

## 경로 표현식 - 예제

```java
- select o.member.team from Order o -> '성공' (묵시적 조인)
- select t.members from Team -> '성공' (컬렉션 조회 가능)
- select t.members.username from Team t -> '실패' (컬렉션에서는 더이상 되지 않는다)
- select m.username from Team t join t.members m -> '성공'
```

<br/><br/>

## 실무 조언

- 가급적 묵시적 조인 대신에 명시적 조인 사용하자
- 조인은 SQL 튜닝에 중요 포인트
- 묵시적 조인은 조인이 일어나는 상황을 한눈에 파악하기 어려움


<br/><br/>

>**Reference** <br/>[자바 ORM 표준 JPA 프로그래밍 - 기본편](https://www.inflearn.com/course/ORM-JPA-Basic)



<br/>

## 궁금증!

```java
경로 표현식에서 "묵시적 조인" 이 왜 위험할까
```

내가 안 적은 조인이 SQL에 생기기 때문이다.

```java
SELECT o.member.team.name FROM Order o
```

<br/>

점 세 개를 찍었을 뿐인데 SQL은 이렇게 나온다.

```sql
SELECT t.name
FROM orders o
INNER JOIN member m ON o.member_id = m.id
INNER JOIN team t ON m.team_id = t.id
```

<br/>

조인이 두 개 붙었다. JPQL만 봐서는 그게 안 보인다.

<br/>

앞의 객체지향 생활 체조 글에서 본 `한 줄에 점을 하나만` 이 여기서도 통한다.

점이 늘어날수록 뒤에서 일어나는 일이 늘어난다.

<br/>

## 그래서 명시적으로 적는 편이 낫다

```java
SELECT t.name FROM Order o
JOIN o.member m
JOIN m.team t
```

<br/>

같은 SQL이 나가는데, 조인이 두 개라는 것이 코드에 드러난다.

<br/>

그리고 조인 종류를 고를 수 있게 된다.

```java
LEFT JOIN o.member m       -- 묵시적 조인은 항상 INNER JOIN 이다
```

<br/>

묵시적 조인은 `INNER JOIN` 으로만 나간다.

회원이 없는 주문이 있으면 그 주문이 결과에서 통째로 빠진다.

<br/>

## 세 가지 경로가 있다

```java
상태 필드      - m.name       더 갈 수 없다
단일 값 연관   - m.team       조인이 일어난다. 계속 갈 수 있다
컬렉션 값 연관 - t.members    조인이 일어난다. 더 못 간다
```

<br/>

컬렉션에서 더 못 가는 이유는 `어느 것` 인지 정할 수 없기 때문이다.

```java
SELECT t.members.name FROM Team t      -- 회원이 여러 명인데 누구의 이름인가
```

<br/>

가려면 별칭을 줘서 하나씩 꺼내야 한다.

```java
SELECT m.name FROM Team t JOIN t.members m
```

<br/>

## 컬렉션에서 쓸 수 있는 것

```java
SELECT SIZE(t.members) FROM Team t          -- 개수는 된다
```

<br/>

```sql
SELECT (SELECT COUNT(*) FROM member WHERE team_id = t.id) FROM team t
```

<br/>

서브쿼리가 붙는다. 팀이 `100` 개면 서브쿼리가 `100` 번 실행된다.

<br/>

앞의 페이징과 정렬 글에서 본 `COUNT 가 비싸다` 는 얘기가 여기서도 적용된다.

목록 화면에서 `SIZE()` 를 쓰면 목록 크기만큼 세는 쿼리가 나간다.

<br/>

## 실무에서는 명시적 조인만 쓴다

정리하면 규칙이 하나로 줄어든다.

```java
점을 두 번 이상 찍지 않는다. 조인은 직접 적는다
```

<br/>

앞의 JPQL - 페치 조인 글에서 본 것과 이어진다.

```java
조건에만 쓸 조인   -> JOIN
데이터도 가져올 조인 -> JOIN FETCH
```

<br/>

둘 다 명시적으로 적으면, 나가는 SQL을 JPQL만 보고도 예상할 수 있게 된다.

앞의 객체지향 쿼리 언어(JPQL) 글에서 본 `나가는 SQL 을 봐야 한다` 가 조금 쉬워지는 것이다.
