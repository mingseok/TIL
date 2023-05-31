## 그룹화 - GROUP BY

테이블에서 단순하게 데이터를 선택하는 것뿐만 아니라 

합계 또는 평균 등의 집계 연산을 SQL 구문으로 할 수 있다.

<br/>

## group by 구의 기능을 쉽게 설명


```
테이블을 홀 케이크 처럼 다룬다
```

동근 형태의 큰 케이크가 있다면, 칼이라는 도구를 사용해 케이크를 자르고 많은 사람이 나눠 먹는 것이 일반적이다.

이때, group by 구는 케이크를 자르는 `칼`과 같은 역할을 한다.

<br/>

### 문법

```sql
select * from 테이블명 group by 열1, 열2 ...
```

<br/><br/>

## 설명을 위한 예제)

### 테이블 확인

| name | address | age |
| --- | --- | --- |
| 인성 | 서울시 | 30 |
| 하진 | 서울시 | 21 |
| 준 | 서울시 | 45 |
| 민 | 부산시 | 32 |
| 하린 | 부산시 | 55 |
| 빛나래 | 인천시 | 19 |
| 인아 | 인천시 | 20 |
| 아린 | 속초시 | 25 |
| 기주 | 서귀포시 | 32 |


<br/>

```sql
select address, COUNT(*)
from Address
group by address;
```

| address | count |
| --- | --- |
| 서울시 | 3 |
| 인천시 | 2 |
| 부산시 | 2 |
| 속초시 | 1 |
| 서귀포시 | 1 |





<br/><br/>


## 테이블 출력

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

<br/><br/>

## `name` 열로 그룹하기

'A', 'B', 'C' 로 그룹화 하는 것이다.

```sql
SELECT name FROM sample51 GROUP BY name;
```

| name |
| --- |
| A |
| B |
| C |
| NULL |

GROUP BY 구에 열을 지정하여 그룹화하면 지정된 열의 값이 같은 행이 하나의 그룹으로 묶인다.

따라서 GROUP BY를 지정해 그룹화하면 `DISTINCT와 같이 중복을 제거하는 효과`가 있다.

```
GROUP BY 구는 집계함수와 함께 사용하지 않으면 의미가 없다.
```


<br/><br/>

## name 열을 그룹화해 계산하기


### 테이블 확인



| no | name | quantity |
| --- | --- | --- |
| 1 | A | 1 |
| 2 | A | 2 |
| 3 | B | 10 |
| 4 | C | 3 |
| 5 | NULL | NULL |


<br/>


### GROUP BY 구와 집계함수를 조합

```sql
SELECT name, COUNT(name), SUM(quantity)
FROM sample51 GROUP BY name;
```



| name | COUNT(name) | SUM(quantity) |
| --- | --- | --- |
| NULL | 0 | NULL |
| A | 2 | 3 |
| B | 1 | 10 |
| C | 1 | 3 |



GROUP BY name에 의해 name 열 값인 A, B, C, NULL 4개의 그룹으로 나뉜다.


A 그룹에는 두개의 행이 있는데, `COUNT`는 행의 개수를 반환 하므로 2가 된다.

<br/>

2개의 행의 `quantity` 열 값은 각각 1과 2이다. -> 1 + 2 되어 A에 3이 된것이다.

`SUM`은 합계를 구하는 집계함수 이므로 3을 반환한다.

<br/><br/>

## GROUP BY를 사용하는 경우

예를 들어, 각 점포의 일별 매출 데이터가 중앙 판매 관리시스템에 전송되어 

점포별 매출실적을 집계해 어떤 점포가 매출이 올라가는지, 어떤 상품이 인기가 있는지 등을 분석할 때 사용된다.

```
점포별, 상품별, 월별, 일별 등 특정 단위로 집계할때 GROUP BY를 사용한다
```



<br/><br/>

## 내부처리 순서

현재 밑에 쿼리를 실행하면 에러가 발생한다.

```sql
select name, COUNT(name) 
from sample51
where COUNT(name) = 1 
group by name;
```

<br/>

### 이유는 뭘까?

where 구로 행을 검색하는 처리가 group by로 그룹화하는 처리보다 순서상 앞서 있기 때문이다.

select 구에서 지정한 별명을 where 구에서 사용할 수 없었던 것과 같은 이유로

그룹화가 필요한 집계함수는 where 구에서 지정할 수 없다.

```
WHERE 구 -> GROUP BY 구 -> SELECT 구 -> ORDER BY 구
```

<br/>

where 구에서는 집계함수를 사용할 수 없다.

그렇기에, SELECT 명령에는 HAVING 구가 있다.


<br/><br/>

## HAVING 구로 조건 지정

HAVING 구는 GROUP BY 구의 뒤에 기술하며 WHERE 구와 동일하게 조건을 지정할 수 있다.


(집계함수는 WHERE 구의 조건식에서는 사용할 수 없기 때문이다)

```
쉽게 설명하면, 조건식에는 그룹별로 집계된 열의 값이나 집계함수의 계산결과가 전달된다고 생각하자
```

<br/>


### 결과적으로

where 구와 having 구에 지정된 조건으로 검색하는 2단 구조가 된다.

![이미지](/programming/img/입문352.PNG)



<br/><br/>


## 예제) HAVING 사용하여 검색

처음엔 GROUP BY 검색한다.

```sql
SELECT name, COUNT(name) FROM sample51 GROUP BY name;
```

| name | COUNT(name) |
| --- | --- |
| NULL | 0 |
| A | 2 |
| B | 1 |
| C | 1 |

<br/>

### HAVING구로 걸러내기

```sql
SELECT name, COUNT(name) FROM sample51
GROUP BY name HAVING COUNT(name) = 1;
```

| name | COUNT(name) |
| --- | --- |
| B | 1 |
| C | 1 |

집계함수를 사용할 경우 HAVING 구로 검색조건을 지정한다.

다만 SELECT 구보다도 먼저 처리되므로 별명을 사용할 수 없다.


<br/><br/>

## HAVING을 추가한 내부처리 순서

```
WHERE 구 -> GROUP BY 구 -> HAVING 구 -> SELECT 구 -> ORDER BY 구
```




<br/><br/>

## 복수열의 그룹화

GROUP BY를 사용할 때 주의할 점이 하나 더 있다.

GROUP BY에 지정한 열 이외의 열은 집계함수를 사용하지 않은 채 SELECT 구에 기술해서는 안된다.

<br/>

예를 들어, 밑에 쿼리는 에러가 발생하는 SQL이다.

```sql
SELECT no, name, quantity FROM sample51 GROUP BY name;
```

| no | name | quantity |
| --- | --- | --- |
| 1 | A | 1 |
| 2 | A | 2 |
| 3 | B | 10 |
| 4 | C | 3 |
| 5 | NULL | NULL |

name을 A는 중복이다. quantity 여기서 어느 값을 출력해야 하는지 모르는 것이다.

이때 집계함수를 사용하면 집합은 하나의 값으로 계산되고, 그룹마다 하나의 행을 출력할 수 있다.


![이미지](/programming/img/입문353.PNG)


<br/>

### 즉, 이렇게 쿼리를 작성하면 문제 없이 실행이 된다.

```sql
SELECT MIN(no), name, SUM(quantity) FROM sample51 GROUP BY name;
```

GROUP BY에서 지정한 열 이외의 열은 집계함수를 사용하지 않은 채 SELECT 구에 지정할 수 없다.

### 결과값을 순서대로 정렬해야 한다면 ORDER BY 구를 지정해줘야 한다.

<br/><br/>


## 결과값 정렬 쿼리 방법

```sql
select name, COUNT(name), SUM(quantity)
from sample51
group by name
order by SUM(quantity) DESC;
```



| name | COUNT(name) | SUM(quantity) |
| --- | --- | --- |
| B | 1 | 10 |
| C | 1 | 3 |
| A | 2 | 3 |
| NULL | 0 | NULL |


<br/><br/>


>**Reference** <br/> SQL 첫걸음 : 아사이 아츠시 지음, 박준용 옮김, 한빛미디어
