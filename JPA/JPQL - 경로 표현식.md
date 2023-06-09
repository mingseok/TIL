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


