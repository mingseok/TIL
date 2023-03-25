## 행 개수 구하기 - COUNT

![이미지](/programming/img/디비2.JPG)

<br/>

COUNT 함수는 인수로 주어진 집합의 ‘개수’ 를 구해 반환한다.

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

COUNT 로 행 개수 계산

```sql
SELECT COUNT(*) FROM sample51;
```

| COUNT(*) |
| --- |
| 5 |

인수로 * 가 지정되어 있는데 이는 SELECT 구에서 ‘모든 열’ 을 나타날 때 사용하는 메타 문자와 같다.

이렇게 집합으로부터 하나의 값을 계산하는 것을 ‘집계’ 라 부른다.

<br/>

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

<br/>

## 집계함수와 NULL값

특히 *를 인수로 사용할 수 있는 것은 COUNT 함수뿐이다.

집계함수는 집합 안에 NULL 값이 있을 경우 이를 제외하고 처리한다.

행 개수를 구할 때 NULL 값 다루기

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

```sql
SELECT COUNT(no), COUNT(name) FROM sample51;
```

| COUNT(no) | COUNT(name) |
| --- | --- |
| 5 | 4 |

name 열에는 NULL 값을 가지는 행이 하나 존재하므로 이를 제외한 개수는 4가 된다. 다만,

COUNT(*) 의 경우 모든 열의 행수를 카운트하기 때문에 NULL 값이 있어도 해당 정보가 무시 되지 않는다.

<br/>

## DISTINCT로 중복 제거

SQL의 SELECT 명령은 이러한 중복된 값을 제거하는 함수를 제공한다.

이때 사용하는 키워드가 바로 DISTINCT 이다.

```sql
SELECT ALL name FROM sample51;
```

| name |
| --- |
| A |
| A |
| B |
| C |
| NULL |

<br/>

DISTINCT를 지정, 콤마는 붙이지 않는다.

```sql
SELECT DISTINCT name FROM smaple51;
```

| name |
| --- |
| A |
| B |
| C |
| NULL |

SELECT 구에서 DISTINCT를 지정하면 중복된 데이터를 제외한 결과를 클라이언트로 반환 한다.

이때 ALL 과 DISTINCT 중 어느 것도 지정하지 않은 경우에는 중복된 값은 제거되지 않는다. 

즉, 생략할 경우에는 ALL로 간주 된다.

<br/>

## 집계함수에서 DISTINCT

DISTINCT를 이용해 집합에서 중복을 제거한 뒤 COUNT로 개수를 구할 수 있는 것이다.

중복을 제거한 뒤 개수 구하기

```sql
SELECT COUNT(ALL name), COUNT(DISTINCT name) FROM sample51;
```

| COUNT(ALL name) | COUNT(DISTINCT name) |
| --- | --- |
| 4 | 3 |

NULL 을 제외한 값의 개수

DISTINCT 와 ALL은 인수가 아니므로 콤마는 붙이지 않는다.



<br/><br/>

>**Reference** <br/> SQL 첫걸음 : 아사이 아츠시 지음, 박준용 옮김, 한빛미디어
