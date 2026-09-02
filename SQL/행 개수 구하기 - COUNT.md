## 행 개수 구하기 - COUNT

`count` 함수는 인수로 주어진 집합의 `개수`를 구해 반환한다.

![이미지](/programming/img/디비2.JPG)

<br/><br/>


### 테이블 확인

```sql
SELECT * FROM sample51;
```

| no | name | quantity |
| --- | --- | --- |
| 1 | A | 1 |
| 2 | A | 2 |
| 3 | B | 10 |
| 4 | C | 3 |
| 5 | NULL | NULL |

<br/>

### COUNT 로 행 개수 계산 쿼리

```sql
SELECT COUNT(*) FROM sample51;
```

<br/>

### 검색 조건 출력

| COUNT(*) |
| --- |
| 5 |

인수로 * 가 지정되어 있는데 이는 SELECT 구에서 ‘모든 열’ 을 나타날 때 사용하는 메타 문자와 같다.

이렇게 집합으로부터 하나의 값을 계산하는 것을 ‘집계’ 라 부른다.

<br/><br/>

## WHERE 구 지정하기.

sample51의 행 개수를 WHERE 구를 지정하여 구하기.

```sql
SELECT * FROM sample51 WHERE name = 'A';
```

| no | name | quantity |
| --- | --- | --- |
| 1 | A | 1 |
| 2 | A | 2 |

<br/>

```sql
SELECT COUNT(*) FROM sample51 WHERE name = 'A';
```

| COUNT(*) |
| --- |
| 2 |

결과 행은 오직 하나뿐

<br/><br/>

## 집계함수와 NULL값

집계함수는 집합 안에 NULL 값이 있을 경우 이를 제외하고 처리한다.

<br/>

### 행 개수를 구할 때 NULL 값 다루기

```sql
SELECT * FROM sample51;
```

| no | name | quantity |
| --- | --- | --- |
| 1 | A | 1 |
| 2 | A | 2 |
| 3 | B | 10 |
| 4 | C | 3 |
| 5 | NULL | NULL |

<br/>

### 결과 확인

```sql
SELECT COUNT(no), COUNT(name) FROM sample51;
```

| COUNT(no) | COUNT(name) |
| --- | --- |
| 5 | 4 |

name 열에는 NULL 값을 가지는 행이 하나 존재하므로 이를 제외한 개수는 4가 된다.

<br/><br/>

>**Reference** <br/> SQL 첫걸음 : 아사이 아츠시 지음, 박준용 옮김, 한빛미디어

<br/>

## 궁금증!

```java
COUNT(*) 와 COUNT(컬럼) 이 다른가
```

5행짜리 표에서 찍어봤다. 나이가 `NULL` 인 행이 하나, 도시가 `NULL` 인 행이 하나 있다.

```sql
SELECT count(*), count(age), count(city) FROM member;
```

<br/>

### 결과

```java
count(*)  count(age)  count(city)
5         4           4
```

<br/>

`COUNT(*)` 는 행을 세고, `COUNT(컬럼)` 은 `NULL` 이 아닌 값을 센다.

<br/>

## 그래서 평균이 예상과 다르게 나온다

```sql
SELECT avg(age), sum(age), count(age) FROM member;
```

<br/>

### 결과

```java
avg(age)  sum(age)  행수
30.0      120       4
```

<br/>

`120 / 5 = 24` 가 아니라 `120 / 4 = 30` 이다.

<br/>

`NULL` 인 행은 분모에서 빠진 것이다.

<br/>

전체 행으로 나누고 싶으면 직접 계산해야 한다.

```sql
SELECT sum(age) * 1.0 / count(*) FROM member;    -- 24.0
```

<br/>

`평균 나이가 30살` 인지 `24살` 인지가 이 차이로 갈린다.

리포트에서 숫자가 안 맞는 원인이 대개 여기다.

<br/>

## COUNT(1) 은 COUNT(*) 와 같다

```sql
SELECT count(1) FROM member;      -- 5
SELECT count(*) FROM member;      -- 5
```

<br/>

`COUNT(1)` 이 더 빠르다는 얘기가 돌았는데 지금은 아니다.

옵티마이저가 둘을 같게 처리한다.

<br/>

`COUNT(*)` 를 쓰는 게 의도가 분명해서 낫다.

<br/>

## COUNT(DISTINCT) 는 무겁다

```sql
SELECT count(DISTINCT city) FROM member;
```

<br/>

중복을 없애려면 전부 모아서 정렬하거나 해시를 만들어야 한다.

<br/>

앞의 인덱스 글에서 본 대로, 인덱스가 있으면 훨씬 낫다.

인덱스는 이미 정렬되어 있어서 옆 값과 비교만 하면 되기 때문이다.

<br/>

## 전체 개수를 세는 게 느릴 때

```sql
SELECT count(*) FROM orders;      -- 천만 건
```

<br/>

MySQL의 InnoDB는 이걸 세려고 실제로 훑는다.

행 개수를 따로 저장해두지 않기 때문이다.

<br/>

MVCC 때문이다. 앞의 MVCC 글에서 본 대로,

트랜잭션마다 보이는 행이 다르니 `정답인 개수` 라는 게 하나가 아니다.

<br/>

그래서 페이징에서 총 개수를 구하는 쿼리가 병목이 되곤 한다.

```sql
SELECT * FROM orders LIMIT 20 OFFSET 0;    -- 빠르다
SELECT count(*) FROM orders;                -- 느리다
```

<br/>

앞의 페이징 API 글에서 본 그 문제다.

<br/>

## 그래서 총 개수를 안 구하는 방법을 쓴다

```java
1. 개수 대신 "다음 페이지가 있는지" 만 본다  (limit + 1 개를 가져온다)
2. 근사치를 쓴다 (통계 테이블의 추정 행 수)
3. 카운터를 따로 관리한다
```

<br/>

`1번` 이 제일 간단하다.

20개를 보여줄 거면 21개를 가져와서, 21개가 오면 다음 페이지가 있는 것이다.

<br/>

앞의 결과 조회 글에서 본 QueryDSL의 `fetchResults` 가

`deprecated` 된 것도 이 카운트 쿼리 때문이었다.

<br/>

## 조건별 개수를 한 번에 세기

```sql
SELECT
    count(*) AS 전체,
    count(CASE WHEN grade = 'GOLD' THEN 1 END) AS 골드,
    sum(CASE WHEN grade = 'GOLD' THEN 1 ELSE 0 END) AS 골드2
FROM member;
```

<br/>

`CASE` 가 `NULL` 을 돌려주면 `COUNT` 가 안 센다는 성질을 이용한 것이다.

<br/>

앞의 조건식 - CASE 식 글에서 본 그 표현식이다.

`ELSE` 를 안 쓰면 기본이 `NULL` 이라 `count` 와 잘 맞는다.

<br/>

쿼리를 세 번 날리는 대신 한 번에 끝나니 훨씬 빠르다.
