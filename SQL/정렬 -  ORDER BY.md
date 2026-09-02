## group by, aggregate function order by

<br/>

## order by

조회 결과를 특정 attribute 기준으로 정렬하여 가져오고 싶을때 사용한다.

- default 정렬 방식은 오름차순이다.

    - 키워드를 사용하지 않으면 default이다.

- 오름차순 정렬은 ASC로 표기 된다.

- 내림차순 정렬은 DESC로 표기 된다.

<br/>

### order by 문제)

직원들의 정보를 순서대로 정렬해서 알고 싶다면,

```sql
select *
from employee
order by salary;
```

<br/>

### 추가로 정렬은 하나만 되는 것이 아니다.

이런식으로 여러개도 가능하다.

- 추가로 알아야 될 것은, `null`이 있다면 `null`끼리 묶어서 출력시킨다는 것이다.

```sql
select *
from employee
order by dept_id asc, salary desc;
```

<br/><br/>

## aggregate function

여러 tuple들의 정보를 요약해서 하나의 값으로 추출하는 함수

- 대표적으로 COUNT, SUM, MAX, MIN, AVG 함수가 있다.

- (주로) 관심있는 attribute에 사용된다.

    - e.g.) AVG(salary), MAX(birth_date)

- NULL 값들은 제외하고 요약 값을 추출한다.

<br/>

### aggregate function 문제1)

임직원 수를 알고 싶다면?

```sql
select COUNT(*)
from employee;
```

COUNT 같은 경우는 중복도 허용한다.

- 만약, dept_id로 쿼리를 작성하였다면?

    - `COUNT(dept_id)` / `COUNT(*)` 했을때랑 값이 다르게 나온다.

    - 이유는 `null`은 포함 시키지 않기 때문이다.

<br/>

### aggregate function 문제2)

프로젝트 2002에 참여한 직원 수와 최대 연봉과 최소 연봉과 최소 연봉과 평균 연봉을 알고 싶다면?

```sql
select COUNT(*), MAX(salary), MIN(salary), AVG(salary)
from works_on W join employee E on W.empl_id = E.id
where W.proj_id = 2002;
```

<br/>

### 출력값 확인

![이미지](/programming/img/입문439.PNG)

<br/><br/>

# group by

관심있는 attribute 기준으로 그룹을 나눠서 

그룹별로 aggregate function을 적용하고 싶을때 사용합니다.

- 그룹을 나누는 기준이 되는 attribute를 → `grouping attribute`라고 부른다.

<br/>

### group by 문제)

각 프로젝트에 참여한 임직원 수와 최대 연봉과 최소 연봉과 평균 연봉을 알고 싶다.

```sql
select W.proj_id, COUNT(*), MAX(salary), MIN(salary), AVG(salary)
from works_on W join employee E on W.empl_id = E.id
group by W.proj_id;
```

proj_id 가지고 그룹핑을 하는 것이다.



그렇게, 그룹핑을 한 `그룹 별로` 각각의 통계를 출력시키는 것이다.

<br/>

### 출력값 확인

select절 맨 앞에 `W.proj_id` 적어주는 이유는?

- 출력된 통계들이 어떤 프로젝트에 대한 것인지 알아 볼 수 있게 하기 위해서이다.

- `W.proj_id`를 `grouping attribute`라고 부른다.

![이미지](/programming/img/입문440.PNG)

<br/><br/>

# HAVING

having은 group by와 함께 사용된다.

aggregate function의 `결과값을 바탕`으로 → 그룹을 필터링하고 싶을때 사용한다.

having절에 명시된 조건을 만족하는 그룹만 결과에 포함된다.

<br/>

### HAVING 문제)

프로젝트 참여 인원이 7명 이상인 프로젝트들에 대해서 

각 프로젝트에 참여한 직원 수와 최대 연봉과 최소 연봉과 평균 연봉을 알고 싶다.

```sql
select W.proj_id, COUNT(*), MAX(salary), MIN(salary), AVG(salary)
from works_on W join employee E on W.empl_id = E.id
group by W.proj_id;
having count(*) >= 7;
```

어떠한 결과가 나왔는데, 추가적으로 조건 or 필터링을 걸고 싶을때 사용하는 것이 having절이다.

<br/>

### 출력값 확

![이미지](/programming/img/입문441.PNG)

<br/><br/>

## 여러가지 예제)

### 문제 1)

각 부서별 인원수를 인원 수가 많은 순서대로 정렬해서 알고 싶다면

```sql
select dept_id, COUNT(*) as empl_count
from employee
group by dept_id
order by empl_count desc;
```

<br/>

### 출력값 확인

![이미지](/programming/img/입문442.PNG)

<br/>

### 문제 2)

각 부서별 - 성별 인원수를 인원 수가 많은 순서대로 정렬해서 알고 싶다

```sql
select dept_id, sex, COUNT(*) as empl_count
from employee
group by dept_id, sex
order by empl_count desc;
```

<br/>

### 출력값 확인

![이미지](/programming/img/입문443.PNG)

<br/>

### 문제 3)

회사 전체 평균 연봉보다 평균 연봉이 적은 부서들의 평균 연봉을 알고 싶다

```sql
select dept_id, AVG(salary)
from employee
group by dept_id
having AVG(salary) < (
                       select AVG(salary) 
                       from employee
                     );
```
<br/>

## 궁금증!

```java
NULL 은 정렬에서 어디로 가나
```

찍어봤다.

```sql
SELECT name, age FROM member ORDER BY age;
```

<br/>

### 결과 (SQLite)

```java
박지성          <- age 가 NULL. 맨 앞으로 갔다
이영한  25
한지민  25
김민석  30
최민식  40
```

<br/>

`NULL` 이 제일 작은 값처럼 취급됐다.

<br/>

## DB 마다 다르다

```java
MySQL, SQLite, SQL Server   ->  ASC 에서 NULL 이 앞
PostgreSQL, 오라클           ->  ASC 에서 NULL 이 뒤
```

<br/>

표준에는 `구현에 맡긴다` 로 되어 있다.

<br/>

그래서 DB를 옮기면 정렬 결과가 달라진다.

`값이 있는 것부터 보여주려고 했는데 빈 것이 먼저 나온다` 는 문제가 생기는 것이다.

<br/>

## 명시하려면

```sql
ORDER BY age NULLS LAST      -- PostgreSQL, 오라클
```

<br/>

MySQL은 이 문법이 없다. 이렇게 흉내낸다.

```sql
ORDER BY age IS NULL, age
```

<br/>

`age IS NULL` 이 `0` 또는 `1` 이라, `NULL` 인 행이 뒤로 간다.

<br/>

앞의 정렬, 페이징, 집합 글에서 본 QueryDSL의 `nullsLast` 가

이 차이를 감춰주는 것이다.

<br/>

## 여러 컬럼으로 정렬할 때

```sql
ORDER BY grade DESC, age ASC
```

<br/>

`DESC` 는 각 컬럼에 따로 붙는다.

```sql
ORDER BY grade, age DESC
```

<br/>

이건 `grade` 는 오름차순, `age` 만 내림차순이다.

`DESC` 가 앞의 것까지 적용되는 게 아니다.

<br/>

앞의 Comparable과 Comparator 글에서 본

자바의 `reversed()` 는 반대로 앞의 체인 전체를 뒤집는다.

둘이 반대로 동작하니 헷갈리기 쉽다.

<br/>

## 정렬이 비싼 이유

```sql
EXPLAIN QUERY PLAN SELECT * FROM member ORDER BY age LIMIT 2 OFFSET 3;
```

```java
SCAN member
USE TEMP B-TREE FOR ORDER BY
```

<br/>

정렬하려고 임시 구조를 만들었다.

<br/>

MySQL에서는 `Using filesort` 로 나온다.

파일이라는 이름이 붙어 있지만 메모리에서 하는 경우가 대부분이다.

<br/>

데이터가 `sort_buffer_size` 를 넘으면 진짜 디스크를 쓴다. 그때부터 아주 느려진다.

<br/>

## 인덱스가 있으면 정렬을 안 해도 된다

인덱스는 이미 정렬되어 있기 때문이다.

```sql
CREATE INDEX idx_age ON member(age);
SELECT * FROM member ORDER BY age;      -- 인덱스를 순서대로 읽으면 끝
```

<br/>

앞의 인덱스 구조 글에서 본 B+트리의 리프 노드가 정렬된 연결 리스트라 그렇다.

<br/>

방향이 반대여도 된다. 거꾸로 읽으면 되기 때문이다.

<br/>

다만 여러 컬럼을 섞으면 안 된다.

```sql
CREATE INDEX idx ON member(grade, age);

ORDER BY grade, age            -- 인덱스를 쓴다
ORDER BY grade DESC, age DESC  -- 쓴다 (전부 거꾸로 읽으면 된다)
ORDER BY grade, age DESC       -- 못 쓴다
```

<br/>

세 번째는 인덱스 순서와 다르다.

한 방향으로 읽어서는 이 순서를 만들 수 없는 것이다.

<br/>

MySQL 8.0부터는 인덱스에 방향을 지정할 수 있다.

```sql
CREATE INDEX idx ON member(grade ASC, age DESC);
```

<br/>

## ORDER BY 에 표현식을 쓰면

```sql
ORDER BY CASE grade
             WHEN 'GOLD' THEN 1
             WHEN 'SILVER' THEN 2
             ELSE 3
         END;
```

<br/>

등급을 사전순이 아니라 내가 정한 순서로 정렬하는 방법이다.

<br/>

MySQL에는 더 짧게 쓰는 함수가 있다.

```sql
ORDER BY FIELD(grade, 'GOLD', 'SILVER', 'BRONZE')
```

<br/>

다만 이러면 인덱스를 못 쓴다.

앞의 문자열 연산 글에서 본 대로, 컬럼을 계산으로 감싸면 정렬된 순서가 깨지기 때문이다.

<br/>

정렬 순서가 고정이면 컬럼을 하나 두는 게 낫다.

```sql
grade_order INT      -- GOLD = 1, SILVER = 2
```

<br/>

이러면 인덱스를 걸 수 있다.

`enum` 에 순서 값을 같이 두는 방식과도 이어진다.
