## 행 제한하기 - LIMIT

```
select 명령에서는 결과값으로 반환되는 행을 제한할 수가 있다.
```


게시판을 읽다 보면 수많은 상품과 게시물을 전부 하나의 페이지에 표시하는 대신 몇 건씩 나누어 표시하는 것을 알 수 있다. 

이런 경우에는 LIMIT 구를 사용해 표시할 건(행) 수를 제한 할 수 있다.

<br/><br/>


## 문법

```sql
select 열명 from 테이블명 LIMIT 행수 [OFFSET 시작행]
```



<br/><br/>

## 행수 제한

LIMIT 구는 표준 SQL은 아니다.

MySQL 과 PostgreSQL 에서 사용할 수 있는 문법이다.

`LIMIT` 구는 `WHERE` 구나 `ORDER BY` 구의 뒤에 지정한다.


<br/>


```sql
select 열명 from 테이블명 where 조건식 order by 열명 limit 행수
```

`LIMIT`를 10으로 지정하면 최대 10개의 행이 클라이언트로 반환된다.


<br/><br/>

## 테이블 출력 해보기

```sql
SELECT * FROM sample33;
```

sample33 테이블 구조

| no |
| --- |
| 1 |
| 2 |
| 3 |
| 4 |
| 5 |
| 6 |
| 7 |

<br/>

LIMIT 사용

```sql
SELECT * FROM sample33 LIMIT 3;
```

출력

| no |
| --- |
| 1 |
| 2 |
| 3 |

<br/><br/>


## 예제


```sql
SELECT * FROM student LIMIT 1;
```

LIMIT 은 내가 조회한 결과를 몇개를 가져 올 것인지 하는 것이다.

![이미지](/programming/img/입문180.PNG)


<br/><br/>





```sql
SELECT * FROM student LIMIT 3,1;
```

id 3번 말고 그 다음부터(=4부터) ~ 1개를 가져 오는 것이다.

![이미지](/programming/img/입문181.PNG)

<br/><br/>

```sql
SELECT * FROM student WHERE sex='남자' LIMIT 2;
```

student 테이블 에서 * 전체를 조회 할건데 '남자' 인 데이터 2개 까지 출력 한다는 것이다.

![이미지](/programming/img/입문182.PNG)



<br/><br/>


## 오프셋 지정


커뮤니티 사이트 등에서 게시판 하단 부분에 `1 2 3 4 5 다음` 등으로 표시된 것이라고 생각하면 된다.

<br/>


한 페이지당 5건의 데이터를 표시하도록 한다면 첫 번째 페이지의 경우 LIMIT 5로 결과값을 표시하면 된다. 

그 다음 페이지에서는 6번째 행부터 5건의 데이터를 표시하도록 한다.

<br/>

6번째 행부터 라는 표현은 결과값으로부터 데이터를 취득할 위치를 가리키는 것으로, LIMIT 구에 OFFSET으로 지정할 수 있다.

LIMIT로 첫 번째 페이지에 표시할 데이터 취득하기.


![이미지](/programming/img/입문343.PNG)

<br/><br/>

### sample33에서 LIMIT 3 OFFSET 0으로 첫 번째 페이지 표시

```sql
SELECT * FROM sample33 LIMIT 3 OFFSET 0;
```

출력 

| no |
| --- |
| 1 |
| 2 |
| 3 |

<br/>

```
OFFSET은 생략 가능하며 기본값은 0이다.
```

간단하게 정리하면 ‘시작할 행 - 1’ 로 기억해 두면 된다.

<br/>

예를 들어, 첫번째 행부터 5건을 취득한다면, ‘1 - 1’ 로 위치는 

0이 되어 OFFSET 0으로 지정하면 된다.

<br/><br/>

## sample33에서 LIMIT 3 OFFSET 3으로 두 번째 페이지 표시

위치 지정은 0부터 시작하는 컴퓨터 자료구조의 배열 인덱스를 생각하면 이해하기 쉽다.

```sql
SELECT * FROM sample33 LIMIT 3 OFFSET 3;
```

| no |
| --- |
| 4 |
| 5 |
| 6 |



<br/><br/>

>**Reference** <br/> SQL 첫걸음 : 아사이 아츠시 지음, 박준용 옮김, 한빛미디어

<br/>

## 궁금증!

```java
ORDER BY 없이 LIMIT 을 쓰면 어떤 행이 나오나
```

정해져 있지 않다.

```sql
SELECT id, name FROM member LIMIT 2;
```

<br/>

### 결과

```java
1   김민석
3   박지성
```

<br/>

`1, 2` 가 아니라 `1, 3` 이 나왔다.

앞에서 만들어둔 인덱스를 타면서 순서가 그렇게 된 것이다.

<br/>

`정렬 없이는 순서가 보장되지 않는다` 는 게 이런 뜻이다.

<br/>

인덱스가 생기거나 없어지면, 같은 쿼리가 다른 결과를 낸다.

데이터가 안 바뀌었는데도 그렇다.

<br/>

앞의 정렬, 페이징, 집합 글에서 본 그 규칙이다.

```java
페이징에는 반드시 정렬이 있어야 한다
```

<br/>

## 정렬 기준이 겹쳐도 마찬가지다

```sql
SELECT name, age FROM member ORDER BY age LIMIT 2 OFFSET 0;
```

<br/>

`25살` 이 두 명이면 둘 중 누가 먼저 나올지 정해져 있지 않다.

<br/>

1페이지에서 `이영한`, 2페이지에서 또 `이영한` 이 나올 수 있는 것이다.

<br/>

그래서 유일한 값을 마지막에 하나 붙인다.

```sql
ORDER BY age, id
```

<br/>

## OFFSET 이 커지면 느려진다

```sql
SELECT * FROM member ORDER BY age LIMIT 2 OFFSET 3;
```

<br/>

### 결과 (실행 계획)

```java
SCAN member
USE TEMP B-TREE FOR ORDER BY
```

<br/>

정렬용 임시 구조를 만들어서 처리한다.

<br/>

`OFFSET` 은 앞의 행을 **읽고 나서 버린다.**

건너뛰는 게 아니라 읽는 것이다.

```sql
LIMIT 20 OFFSET 100000
```

<br/>

20개를 보여주려고 100,020개를 읽는다.

뒤 페이지로 갈수록 느려지는 이유다.

<br/>

## 그래서 커서 방식을 쓴다

마지막으로 본 값을 기준으로 이어서 읽는다.

```sql
SELECT * FROM member WHERE id > 100000 ORDER BY id LIMIT 20;
```

<br/>

`OFFSET` 이 없으니 몇 페이지든 같은 속도다.

인덱스로 `id > 100000` 위치를 바로 찾아가기 때문이다.

<br/>

무한 스크롤에 잘 맞는다.

<br/>

대신 제약이 있다.

```java
"5페이지로 바로 가기" 가 안 된다
정렬 기준을 자유롭게 못 바꾼다
```

<br/>

정렬 기준이 여러 개면 조건도 복잡해진다.

```sql
WHERE (age, id) > (25, 100)      -- 나이순 정렬일 때
```

<br/>

앞의 페이징 API, 조인 글에서 본 것과 같은 고민이다.

<br/>

## DB 마다 문법이 다르다

```sql
MySQL, PostgreSQL   LIMIT 20 OFFSET 100
오라클 (11g 이전)     ROWNUM 을 쓴다
SQL Server          TOP 또는 OFFSET FETCH
표준 SQL            OFFSET 100 ROWS FETCH NEXT 20 ROWS ONLY
```

<br/>

앞의 페이징 API 글에서 본 대로,

JPA는 이걸 방언으로 흡수해서 같은 코드로 쓸 수 있게 한다.

```java
setFirstResult(100).setMaxResults(20)
```

<br/>

DB를 바꿔도 이 코드는 안 바뀌는 것이다.

<br/>

## 페이징에서 총 개수를 세는 게 더 비쌀 때가 많다

```sql
SELECT * FROM orders ORDER BY id DESC LIMIT 20;    -- 빠르다
SELECT count(*) FROM orders;                        -- 느리다
```

<br/>

앞의 행 개수 구하기 - COUNT 글에서 본 그 문제다.

<br/>

`21개를 가져와서 20개만 보여주기` 로 총 개수를 안 세는 방식이

실무에서 자주 쓰이는 절충안이다.
