## 그룹화 - GROUP BY

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

### name 열로 그룹하기

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

따라서 GROUP BY를 지정해 그룹화하면 DISTINCT와 같이 중복을 제거하는 효과가 있다.

<br/><br/>

## name 열을 그룹화해 계산하기

GROUP BY 구와 집계함수를 조합

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

COUNT는 행의 개수를 반환 하므로 2가 된다. (2개라서 합하여 한줄에 표현한다고 생각)

<br/>

2개의 행의 quantity 열 값은 각각 1과 2이다. -> 1 + 2 되어 A에 3이 된것이다.

SUM은 합계를 구하는 집계함수 이므로 3을 반환한다.



| no | name | quantity |
| --- | --- | --- |
| 1 | A | 1 |
| 2 | A | 2 |
| 3 | B | 10 |
| 4 | C | 3 |
| 5 | NULL | NULL |



<br/><br/>

## HAVING 구로 조건 지정

집계한 결과에서 조건에 맞는 값을 따로 걸래 낼 때 내부 처리

포인트는 WHERE 구에서는 집계함수를 사용할 수 없다.

```sql
WHERE 구 -> GROUP BY 구 -> SELECT 구 -> ORDER BY 구
```

<br/>

### 기존 GROUP BY 검색

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

## 복수열의 그룹화

GROUP BY를 사용할 때 주의할 점이 하나 더 있다.

GROUP BY에 지정한 열 이외의 열은 집계함수를 사용하지 않은 채 SELECT 구에 기술해서는 안된다.

예를 들어, 안되는 코드를 설명하겠다. (에러 코드)

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

<br/>

즉, 이렇게 쿼리를 작성하면 문제 없이 실행이 된다.

```sql
SELECT MIN(no), name, SUM(quantity) FROM sample51 GROUP BY name;
```

GROUP BY에서 지정한 열 이외의 열은 집계함수를 사용하지 않은 채 SELECT 구에 지정할 수 없다.

### 결과값을 순서대로 정렬해야 한다면 ORDER BY 구를 지정해줘야 한다.

<br/><br/>

>**Reference** <br/> SQL 첫걸음 : 아사이 아츠시 지음, 박준용 옮김, 한빛미디어
