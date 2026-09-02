
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


<br/>

## 궁금증!

```java
CASE 식을 자바 if 문 대신 쓰면 무엇이 나은가
```

`거를 데이터를 DB 에서 처리하느냐` 가 갈린다.

```java
// 자바에서 처리
List<Member> members = em.createQuery("SELECT m FROM Member m", Member.class)
        .getResultList();
for (Member m : members) {
    String grade = m.getAge() >= 60 ? "경로" : m.getAge() <= 10 ? "학생" : "일반";
}

// CASE 식으로 처리
SELECT
    CASE WHEN m.age >= 60 THEN '경로'
         WHEN m.age <= 10 THEN '학생'
         ELSE '일반'
    END
FROM Member m
```

<br/>

자바에서 처리하면 `10만` 건을 전부 가져와야 한다.

앞의 DBCP 글에서 본 대로 네트워크로 오가는 양이 그대로 비용이다.

<br/>

## 그런데 항상 DB 가 나은 것은 아니다

앞의 프로시저(procedure)란 글에서 본 판단이 여기에도 적용된다.

```java
애플리케이션 서버는 늘리기 쉽다
DB 는 늘리기 어렵다
```

<br/>

계산을 DB로 몰면 늘리기 어려운 쪽이 먼저 한계에 부딪힌다.

<br/>

그래서 기준을 이렇게 잡는 편이 낫다.

```java
CASE 를 쓸 만한 경우
  - 결과를 줄인다 (GROUP BY 와 함께 집계)
  - 정렬 기준으로 쓴다 (ORDER BY CASE ...)

자바에서 하는 편이 나은 경우
  - 비즈니스 규칙이다 (등급 판정 기준이 바뀔 수 있다)
  - 테스트하고 싶다
```

<br/>

앞의 객체는 객체스럽게 사용한다 글에서 본 대로,

`등급을 어떻게 매기는가` 는 도메인 규칙이라 객체 안에 있는 편이 맞다.

```java
public Grade grade() {
    if (age >= 60) return Grade.SENIOR;
    if (age <= 10) return Grade.STUDENT;
    return Grade.NORMAL;
}
```

<br/>

이러면 앞의 테스트 코드 작성 규칙 글에서 본 대로 DB 없이 테스트할 수 있다.

<br/>

## 집계와 같이 쓸 때가 진짜 쓸모다

```java
SELECT
    SUM(CASE WHEN o.status = 'PAID' THEN o.amount ELSE 0 END),
    SUM(CASE WHEN o.status = 'CANCELED' THEN o.amount ELSE 0 END)
FROM Order o
```

<br/>

`결제 합계` 와 `취소 합계` 를 쿼리 한 번에 구한다.

<br/>

자바로 하려면 주문을 전부 가져와서 세어야 한다.

`100만` 건이면 `100만` 건을 다 읽고 두 개의 숫자만 쓰는 셈이다.

<br/>

이건 `결과를 줄이는` 경우라 DB에서 하는 것이 명백히 낫다.

<br/>

## ORDER BY 에 쓰면 정렬 순서를 정할 수 있다

```java
SELECT o FROM Order o
ORDER BY CASE o.status
    WHEN 'URGENT' THEN 1
    WHEN 'NORMAL' THEN 2
    ELSE 3
END
```

<br/>

`URGENT`, `NORMAL`, 나머지 순으로 정렬한다.

문자열 순서로는 이 순서가 안 나오니 이렇게 매핑한다.

<br/>

다만 앞의 DB 인덱스 글에서 본 문제가 그대로 온다.

컬럼에 계산을 씌운 것이라 인덱스를 못 쓴다.

```sql
|--SCAN member
`--USE TEMP B-TREE FOR ORDER BY
```

<br/>

자주 쓰는 정렬이라면 앞의 enum 사용하는 이유는 글에서 본 것처럼

정렬용 숫자 컬럼을 따로 두는 편이 낫다.

```java
enum OrderStatus {
    URGENT(1), NORMAL(2), DONE(3);
    private final int sortOrder;      // 앞의 ordinal 대신 직접 들고 있는다
}
```

<br/>

앞의 enum 사용하는 이유는 글에서 본 `ordinal 을 쓰지 말자` 가 여기서도 적용된다.

상수를 중간에 끼워넣으면 순서가 전부 밀리기 때문이다.

<br/>

## COALESCE 도 CASE 의 축약형이다

```java
COALESCE(m.nickname, m.name, '익명')
```

```java
CASE WHEN m.nickname IS NOT NULL THEN m.nickname
     WHEN m.name IS NOT NULL THEN m.name
     ELSE '익명'
END
```

<br/>

같은 뜻인데 훨씬 짧다.

앞의 조건식, JPQL 기본 함수 글에서 본 대로 `null` 을 다루는 자리에서 자주 쓰인다.
