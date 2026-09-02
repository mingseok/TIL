## LIKE, BETWEEN

<br/>


## LIKE 문제 1)

이름이 N으로 시작하거나 N으로 끝나는 직원들의 이름을 알고 싶다.

```sql
select name
from employee
where name like 'N%' or name like '%N';
```

- N% : n으로 시작하는 경우

- %N : n으로 끝나는 경우


<br/>


### 출력값 확인

| name |
| --- |
| BROWN |
| JOHN |
| NICOLE |


<br/><br/>


## LIKE 문제 2)

이름에 NG가 들어가는 직원들의 이름을 알고 싶다

```sql
select name
from employee
where name like '%NG%';
```

<br/>

### 출력값 확인

| name |
| --- |
| DINGYO |
| JISUNG |




<br/><br/>


## LIKE 문제 3)

이름이 J로 시작하는, 총 네글자의 이름을 가지는 직원들의 이름을 알고 싶다

```sql
select name
from employee
where name 'J___';
```

<br/>

### 출력값 확인

| name |
| --- |
| JANE |
| AOHN |




<br/><br/>


## 만약, 찾고 싶은 대상이 '%' 이거나 '_' 일 경우는?

%로 시작하거나 _로 끝나는 프로젝트 이름을 찾고 싶다면?

```sql
select name
from project
where name LIKE '\%%' or name name LIKE '%\_';
```






<br/><br/>

## BETWEEN , NOT BETWEEN 연산자.

```
“어떤 값이 어떤 특정 값 사이에 있다” 이, 여부를 참과 거짓으로 나타낼 수 있다.
```


<br/>

(중요) 예를 들어 `BETWEEN 1 AND 4` 이렇게 되어 있으면 ‘4’는 포함되지 않는 것이다.



‘4’ 전까지를 말하는 것이다.

![이미지](/programming/img/입문201.PNG)

<br/><br/>

```sql
SELECT 5 BETWEEN 1 AND 10;
```





‘5’ 는 1과 10 사이에 있다. → 참인 것이다.

![이미지](/programming/img/입문202.PNG)

<br/><br/>

```sql
SELECT 'banana' NOT BETWEEN 'Apple' AND 'camera';
```



이건 단어의 앞 글자만 보면 되는 것이다. A와 C 사이에 B 가 있으니 참이지만, NOT 이 붙어 있으니 false 로 나온 것이다.

참고로 mysql 은 소, 대문자 구분을 하지 않는다.

![이미지](/programming/img/입문203.PNG)


<br/><br/>

```sql
SELECT * FROM OrderDetails
WHERE ProductID BETWEEN 1 AND 4;
```



![이미지](/programming/img/입문204.PNG)


<br/><br/>

```sql
SELECT * FROM Customers
WHERE CustomerName BETWEEN 'a' AND 'b';
```



여기서 포인트는 마지막꺼는 포함하지 않는 다는 것이다.

a AND b 를 했지만 a 관한 것만 출력 되는 것이다.

b 가 없어서 출력 안되는 것이 아니다.

![이미지](/programming/img/입문205.PNG)


<br/><br/>


# NULL 값, 비교 연산자

```sql
select id
from employee
where birth_data = null;
```

위 쿼리를 실행 시키면 아무것도 나오지 않을 수도 있다!! 

### 함정이 있다는걸 알고있기!

여기서는 `=` 을 사용하면 안된다

- 왜? -> null과 비교 할때는 같다는 `=` 걸 사용하면 안된다.


<br/><br/>


## 그러면 어떻게 해야돼? -> `IS` 라는 연산자 사용하기!

```sql
select id
from employee
where birth_data = IS NULL;
```

<br/>

```
만약, 생일 정보가 null이 아닌 직원들의 id를 가져 오고 싶다면 `IS NOT`을 사용하기
```

<br/><br/>

## Three-Valued Logic

- SQL에서 NULL과 비교 연산을 하게 되면 그 결과는 `unknown` 이다

- `unknown`은 'true 일수도 있고 false일 수도 있다' 라는 의미이다

```
Three-Valued Logic 이란?
비교/논리 연산의 결과로 true, false, unknown을 가진다
```

<br/><br/>


## unknown에 대해서

- 1 = 1 -> `true`

- 1 != 1 -> `false`

- 1 = null = `unknown`

- 1 != null = `unknown`

- 1 > null = `unknown`

- 1 <= null = `unknown`

- null > null = `unknown`


<br/><br/>

## where절의 condition(S)

where절에 있는 condition의 결과가 true인 tuple만 선택 된다.

- 즉, 결과가 `false`거나 `unknown`이면 tuple은 선택되지 않는다


<br/><br/>

## NOT IN 사용시 주의점!

2000년대생이 없는 부서의 ID와 이름을 알고 싶다면?

<br/>

### 문제점!

2000년생이 회사에 입사를 했는데, 

아직 부서 배치를 받지 않아서 null로 처리되어 있는 상태인 것이다.

<br/>


### 변경 전 코드

```SQL
select D.id, D.name
from department AS D
where D.id NOT IN (
              select E.dept_id
              from employee E
              where E.birth_date >= '2000-01-01'
              );
)
```

<br/>

## 변경 후 코드

```SQL
select D.id, D.name
from department AS D
where D.id NOT IN (
              select E.dept_id
              from employee E
              where E.birth_date >= '2000-01-01' AND E.dept_id IS NOT NULL
              );
)
```


<br/>

## 궁금증!

```java
LIKE '김%' 는 인덱스를 타는데 왜 '%석' 은 못 타나
```

실행 계획을 찍어보면 그 이유가 드러난다.

```sql
CREATE INDEX idx_name ON member(name);

EXPLAIN QUERY PLAN SELECT * FROM member WHERE name LIKE '김%';
EXPLAIN QUERY PLAN SELECT * FROM member WHERE name LIKE '%석';
```

<br/>

### 결과

```java
LIKE '김%'  ->  SEARCH member USING INDEX idx_name (name>? AND name<?)
LIKE '%석'  ->  SCAN member
```

<br/>

앞쪽이 고정된 것은 `SEARCH` 이고, 뒤쪽만 아는 것은 `SCAN` 이다.

<br/>

## 계획에 답이 적혀 있다

```java
(name>? AND name<?)
```

<br/>

`LIKE '김%'` 가 범위 조회로 바뀐 것이다.

<br/>

직접 범위로 써봐도 같은 계획이 나온다.

```sql
SELECT * FROM member WHERE name >= '김' AND name < '깈';
```

```java
SEARCH member USING INDEX idx_name (name>? AND name<?)
```

<br/>

`김` 으로 시작하는 것은 사전순으로 `김` 과 그 다음 글자 사이에 몰려 있으니

범위로 바꿀 수 있는 것이다.

<br/>

## `%석` 은 범위로 못 바꾼다

`석` 으로 끝나는 이름이 사전순 어디에 있는지 알 수가 없다.

```java
김민석, 최민석, 박태석 ...
```

<br/>

인덱스는 앞에서부터 정렬되어 있으니, 뒤를 기준으로는 찾을 방법이 없는 것이다.

<br/>

앞의 인덱스 구조 글에서 본 B+트리의 성질이 그대로다.

전화번호부에서 `~석` 으로 끝나는 이름을 찾으려면 다 넘겨봐야 하는 것과 같다.

<br/>

## 그래서 검색 기능을 만들 때 고민이 생긴다

```sql
WHERE title LIKE '%검색어%'
```

<br/>

앞뒤로 `%` 가 붙으니 인덱스가 무용지물이다.

<br/>

데이터가 적으면 상관없는데, 수백만 건이면 매번 전부 훑는다.

<br/>

해결 방법이 몇 가지 있다.

```java
1. 전문 검색 인덱스 (FULLTEXT, Elasticsearch)
2. 앞부분 검색으로 제한한다 (자동완성 같은 경우)
3. 뒤집어서 저장한 컬럼을 하나 더 둔다 (접미사 검색용)
```

<br/>

`3번` 이 재미있다.

```sql
name_reversed = '석민김'
WHERE name_reversed LIKE '석%'      -- 이건 인덱스를 탄다
```

<br/>

`뒤에서 찾는 문제` 를 `앞에서 찾는 문제` 로 바꿔버리는 것이다.

<br/>

## BETWEEN 은 양쪽 끝을 포함한다

```sql
SELECT name, age FROM member WHERE age BETWEEN 25 AND 30;
```

<br/>

### 결과

```java
이영한  25
한지민  25
김민석  30
```

<br/>

`25` 와 `30` 이 다 들어 있다.

```sql
age >= 25 AND age <= 30
```

<br/>

이것과 같다.

<br/>

## 날짜에서 이 성질이 함정이 된다

```sql
WHERE created_at BETWEEN '2026-09-01' AND '2026-09-30'
```

<br/>

`9월 30일 데이터가 안 나온다` 는 문의가 여기서 나온다.

<br/>

`'2026-09-30'` 은 `'2026-09-30 00:00:00'` 으로 해석되기 때문이다.

그날 낮에 만들어진 것은 이 범위 밖이다.

<br/>

그래서 이렇게 쓴다.

```sql
WHERE created_at >= '2026-09-01' AND created_at < '2026-10-01'
```

<br/>

`끝은 포함하지 않는` 형태가 날짜에서는 더 안전하다.

`23:59:59` 로 적으면 밀리초 단위 데이터를 놓치기 때문이다.

<br/>

## 문자열에도 BETWEEN 을 쓸 수 있다

```sql
SELECT name FROM member WHERE name BETWEEN '김' AND '이';
```

<br/>

### 결과

```java
김민석
박지성
```

<br/>

사전순으로 비교한다.

`이영한` 은 안 나왔는데, `'이영한' > '이'` 라서 범위를 벗어났기 때문이다.

<br/>

`이` 로 시작하는 것까지 포함하려면 끝을 하나 올려야 한다.

```sql
WHERE name >= '김' AND name < '자'
```

<br/>

이 성질이 앞의 `LIKE '김%'` 가 범위로 바뀌는 것과 정확히 같은 얘기다.

<br/>

정렬 기준이 무엇이냐에 따라 달라지니 주의해야 한다.

앞의 문자 인코딩 글에서 본 대로,

한글이 어떤 순서로 정렬되는지는 콜레이션 설정을 따라간다.
