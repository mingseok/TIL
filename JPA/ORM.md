## ORM

- `Object-relational mapping`(객체 관계 매핑)

- 객체는 객체대로 설계
- 관계형 데이터베이스는 관계형 데이터베이스대로 설계
- ORM 프레임워크가 중간에서 매핑
    - `‘R’`? → 관계형 DB 할때 `‘R’`이다.
    
    - `매핑`? → 중간에서 무언가를 해준다는 것이다.
        - 객체는 객체대로 설계를 따로하고, 관계형 DB는 관계형 DB대로 따로 설계한다.

```
그렇게 중간에서 다른부분들을 'ORM 프레임워크'가 매핑하여 해결해 준다는 것이다.
```

<br/><br/>

>**Reference** <br/>[자바 ORM 표준 JPA 프로그래밍 - 기본편](https://www.inflearn.com/course/ORM-JPA-Basic)




<br/>

## 궁금증!

```java
"중간에서 매핑한다" 는 게 무엇을 맞춰준다는 뜻일까?
```

객체와 테이블이 서로 다르게 생겼기 때문이다. 이 차이를 `패러다임 불일치` 라고 한다.

<br/>

## 첫번째) 상속

객체에는 상속이 있는데 테이블에는 없다.

```java
class Item { }
class Book extends Item { String author; }
class Movie extends Item { String director; }
```

<br/>

이걸 테이블로 만들려면 방법을 정해야 한다.

```sql
-- 방법 1 : 한 테이블에 다 넣고 구분 컬럼을 둔다
CREATE TABLE item (id, name, dtype, author, director);

-- 방법 2 : 테이블을 나누고 조인한다
CREATE TABLE item (id, name);
CREATE TABLE book (item_id, author);
```

<br/>

JPA는 이걸 `@Inheritance` 로 고를 수 있게 해준다.

앞의 상속관계 매핑 글에서 다루는 그 선택이다.

<br/>

## 두번째) 연관관계의 방향

객체는 참조로 한 방향만 갈 수 있는데, 테이블은 조인으로 양쪽 다 갈 수 있다.

```java
member.getTeam()      // 회원에서 팀으로는 간다
team.getMember()      // 팀에서 회원으로는 못 간다. 참조가 없으면
```

```sql
SELECT * FROM member m JOIN team t ON m.team_id = t.id     -- 어느 쪽에서든 된다
```

<br/>

그래서 JPA에는 `mappedBy` 라는 것이 생겼다.

앞의 연관관계 매핑 글에서 다루는 `주인` 개념이 이 차이 때문에 나온 것이다.

<br/>

## 세번째) 동일성

같은 행을 두 번 조회하면 객체는 두 개가 된다.

```java
Member a = repository.findById(1L);
Member b = repository.findById(1L);
a == b;                            // SQL 로 직접 짜면 false
```

<br/>

앞의 JPA 설정, 적용, 핵심 글에서 본 대로, JPA는 영속성 컨텍스트로 이걸 `true` 로 만든다.

같은 트랜잭션 안에서는 같은 행이 항상 같은 객체가 된다.

<br/>

## 네번째) 연관 객체를 어디까지 가져올 것인가

```java
Member member = repository.findById(1L);
member.getTeam().getName();          // 팀도 가져와야 하나?
member.getTeam().getCompany().getName();   // 회사까지?
```

<br/>

SQL로 직접 짜면 `어디까지 조인할지` 를 쿼리마다 정해야 한다.

<br/>

JPA는 `프록시` 로 이걸 미룬다. 앞의 프록시 기초 글과 즉시 로딩과 지연 로딩 글에서 다루는 얘기다.

<br/>

그 대신 앞의 DB 접근(JPA) 글에서 본 `N+1` 문제가 생긴다.

편의를 얻은 대가로 쿼리가 언제 나가는지 눈에 안 보이게 되는 것이다.

<br/>

## 그래서 ORM 이 만능은 아니다

```java
ORM 이 잘하는 것 - 한 건 조회, 저장, 수정. 객체 그래프를 따라가는 탐색
ORM 이 약한 것   - 통계, 대량 갱신, 복잡한 조인
```

<br/>

앞의 Querydsl 설정 글에서 다루는 것도 그 약한 부분을 메우려는 도구다.

<br/>

그리고 앞의 MyBatis 설명, 설정 방법 글에서 본 대로, 복잡한 SQL은 직접 쓰는 편이 나을 때가 있다.

실무에서는 JPA로 대부분을 처리하고, 통계 쿼리만 따로 두는 구성을 많이 쓴다.
