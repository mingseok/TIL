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
