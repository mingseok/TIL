## inner join, outer join 종류

두 개 이상의 table들에 있는 데이터를 한 번에 조회하는 것

- 여러 종류의 join이 존재한다.

<br/><br/>

## inner join

```sql
select *
from employee E join department D on E.dept_id = D.id;
```

위 코드에서는,  `join` 앞에 `inner`가 생략된 것이다.

즉, 우리는 이미 inner 조인이라는 것을 사용하고 있었던 것이다.

<br/>

![이미지](/programming/img/입문434.PNG)

<br/>

### 출력값 확인하기

이 결과에서 중요한 것은, `employee`의 `SIMON`이랑 `department`의 `1002`랑은 테이블에서 빠졌다는 것이다.

![이미지](/programming/img/입문435.PNG)

<br/><br/>

## 이것이 inner 조인이다.

- inner join : 두 table에서 join condition을 만족하는 tuple들로 result table을 만드는 join

- from table1 E `inner join` table2 D `on` join_condition

    - 위 쿼리에서 from table1 E `join` table2 D `on` join_condition
        
        이렇게 사용 가능하다.
        
- 사용 가능한 연산자 : `=, <, >, ≠` 등등 여러 비교 연산자가 가능하다

- join condition에서 null 값을 가지는 tuple은 result table에 포함되지 못한다

<br/><br/>

## outer join

- outer join : 두 table에서 join condition을 만족하지 않는 tuple들도 result table에 포함하는 join

- outer join은 세가지 종류가 있다 → [ ] 안에 들어가 있는 건 생략이 가능하다는 것

    - from table1 `left [outer] join` table2 `on` join_condition

    - from table1 `right [outer] join` table2 `on` join_condition

- 사용 가능한 연산자: `=, <, >, ≠`

<br/>

### left [outer] join 출력값 확인하기

이제는 SIMON이 포함이 되었다.

![이미지](/programming/img/입문436.PNG)

`left`의 개념이 쿼리상에서 `left outer join` 기준으로 왼쪽에 있는 테이블을 의미한다. → employee 테이블

<br/>

그런 뜻은, `employee` 테이블에서 위에 `ON`(join_condition이라고 부름)에 
의해서 

매칭 되지 않는 tuple들 까지도 함께 출력 되기에 `SIMON`도 출력 되어 나온 것이다.

- 나머지 `department` 테이블에 관련된 정보들은 전부 `null`로 표시한다.

<br/><br/>

## right [outer] join

left [outer] join의 반대라고 생각하면 된다.

![이미지](/programming/img/입문437.PNG)

<br/><br/>

## join을 사용한 예제

### 문제1)

id가 1003인 부서에 속하는 직원 중 리더를 제외한 부서원의 id, 이름, 연봉을 알고 싶다

```sql
select E.id, E.name, E.salary
from employee E join department D on E.dept_id = D.id
where E.dept_id = 1003 and E.id != D.leader_id;
```

<br/>

### 문제2)

id가 2001인 프로젝트에 참여한 직원들의 이름과 직군과 소속 부서 이름을 알고 싶다.

```sql
select E.name AS empl_name,
       E.position AS empl_position,
       D.name AS dept_name
from works_on W join employee E on W.empl_id = E.id
                left join department D on E.dept_id = D.id
where W.proj_id = 2001;
```
<br/>

## 궁금증!

```java
조인하면 행 개수가 어떻게 되나
```

회원 5명, 주문 4건인 표로 확인했다.

주문 중 하나는 `member_id` 가 `NULL` 이고, 김민석은 주문이 두 건이다.

```sql
SELECT count(*) FROM member;
SELECT count(*) FROM member m JOIN orders o ON m.id = o.member_id;
SELECT count(*) FROM member m LEFT JOIN orders o ON m.id = o.member_id;
```

<br/>

### 결과

```java
member 행 수           = 5
INNER JOIN 결과 행 수  = 3
LEFT JOIN 결과 행 수   = 6
```

<br/>

원래보다 줄기도 하고 늘기도 한다.

<br/>

## 줄어드는 이유

`INNER JOIN` 은 양쪽에 짝이 있는 것만 남긴다.

주문이 없는 회원 3명이 사라진 것이다.

<br/>

`짝 없는 것은 버린다` 는 성질을 모르면 데이터가 새는 것처럼 보인다.

<br/>

앞의 조인 글에서 본 대로,

`LEFT JOIN` 은 왼쪽을 다 남기고 짝이 없으면 오른쪽을 `NULL` 로 채운다.

<br/>

## 늘어나는 이유

한 회원에 주문이 여러 건이면 그만큼 복제된다.

```sql
SELECT m.name, o.amount FROM member m JOIN orders o ON m.id = o.member_id;
```

<br/>

### 결과

```java
김민석  10000
김민석  20000        <- 같은 사람이 두 번
이영한  5000
```

<br/>

회원 행이 주문 건수만큼 늘어난 것이다.

<br/>

## 그래서 집계가 틀린다

```sql
SELECT count(DISTINCT m.id), count(*)
FROM member m JOIN orders o ON m.id = o.member_id;
```

<br/>

### 결과

```java
실제 회원 수  조인 후 행 수
2             3
```

<br/>

`주문한 회원 수` 를 구하려고 `count(*)` 를 쓰면 `3` 이 나온다.

정답은 `2` 다.

<br/>

`count(DISTINCT m.id)` 를 써야 한다.

<br/>

금액 합계도 마찬가지다.

회원 정보에 있는 금액을 합치면 중복 계산된다.

<br/>

앞의 페이징 API, 조인 글에서 본 JPA의 문제와 정확히 같은 원인이다.

컬렉션을 페치 조인하면 페이징이 안 되는 이유가 이 행 복제 때문이다.

<br/>

## ON 과 WHERE 의 차이

`INNER JOIN` 에서는 결과가 같다.

```sql
JOIN orders o ON m.id = o.member_id AND o.amount > 5000
JOIN orders o ON m.id = o.member_id WHERE o.amount > 5000
```

<br/>

`LEFT JOIN` 에서는 완전히 다르다.

```sql
LEFT JOIN orders o ON m.id = o.member_id AND o.amount > 5000
   -> 조건에 안 맞는 주문은 NULL 로. 회원은 다 남는다

LEFT JOIN orders o ON m.id = o.member_id WHERE o.amount > 5000
   -> NULL 인 행이 WHERE 에서 걸러진다. INNER JOIN 과 같아진다
```

<br/>

앞의 조인 글에서 본 그 차이다.

`LEFT JOIN 을 썼는데 왜 INNER JOIN 처럼 나오지` 의 원인이 대개 이것이다.

<br/>

## 조인 순서를 내가 정하지 않는다

```sql
FROM member m JOIN orders o
FROM orders o JOIN member m
```

<br/>

적는 순서가 실행 순서가 아니다.

옵티마이저가 통계를 보고 정한다.

<br/>

작은 쪽을 먼저 읽고 큰 쪽을 인덱스로 찾는 것이 보통 유리하다.

<br/>

앞의 실행 계획 글에서 본 대로,

통계가 낡으면 잘못된 순서를 고르기도 한다.

```sql
ANALYZE TABLE orders;      -- 통계를 갱신한다
```

<br/>

## 조인 알고리즘이 몇 가지 있다

```java
Nested Loop Join  - 하나씩 돌면서 상대를 찾는다. 인덱스가 있으면 빠르다
Hash Join         - 한쪽으로 해시 테이블을 만들고 다른 쪽을 훑는다. 큰 테이블에 유리
Sort Merge Join   - 양쪽을 정렬하고 나란히 훑는다
```

<br/>

MySQL은 오래 `Nested Loop` 만 썼다.

8.0.18부터 `Hash Join` 이 들어갔다.

<br/>

`Nested Loop` 는 안쪽 테이블에 인덱스가 없으면 재앙이 된다.

바깥 행마다 안쪽을 전부 훑기 때문이다.

```java
1000행 x 1000행 = 100만 번 비교
```

<br/>

그래서 조인 조건 컬럼에 인덱스가 있는지를 항상 확인해야 한다.

외래 키 컬럼에 인덱스를 거는 게 관례인 이유이기도 하다.
