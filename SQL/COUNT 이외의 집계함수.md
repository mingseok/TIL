## 집계함수 (SUM, AVG, MIN, MAX)

<br/>

## SUM으로 합계 구하기

### 테이블 확인

| no | quantity |
| --- | --- |
| 1 | 1 |
| 2 | 2 |
| 3 | 10 |
| 4 | 3 |
| 5 | NULL |

<br/>

### SUM으로 quantity 열의 합계 쿼리

```sql
SELECT SUM(quantity) FROM sample51;
```

<br/>

### 검색 결과 확인

| SUM(quantity) |
| --- |
| 16 |

한편 SUM 집계함수도 COUNT와 마찬가지로 NULL 값을 무시한다.

NULL 값을 제거한 뒤에 합계를 낸다.

<br/><br/>

## AVG로 평균내기

밑에 쿼리 둘다 같은 값을 출력한다.

```sql
SELECT AVG(quantity), SUM(quantity)/COUNT(quantity) FROM sample51;
```


AVG 집계함수도 NULL 값을 무시한다.


하지만, 굳이 `sum`과 `count`를 이용하지 않아도, `AVG`라는 집계함수를 통해 평균값을 간단하게 구할 수 있다.


<br/>

### 검색 결과 출력

| AVG(quantity) | SUM(quantity)/COUNT(quantity) |
| --- | --- |
| 4.0000 | 4.0000 |




<br/><br/>

## 만약 NULL을 0로 간주해서 평균을 내고 싶다면

CASE를 사용해 NULL을 0로 변환한 뒤에 AVG 함수로 계산하면 된다.

```sql
SELECT AVG(CASE WHEN quantity IS NULL THEN 0 ELSE quantity END) AS avgnull0 FROM sample51;
```


<br/><br/>

## MIN / MAX 로 최솟값, 최댓값 구하기

최솟값과 최댓값을 구할 수 있다.

NULL 값을 무시하는 기본규칙은 다른 집계함수와 같다.

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


### 검색 결과 확인


```sql
SELECT MIN(quantity), MAX(quantity), MIN(name), MAX(name)
FROM sample51;
```


| MIN(quantity) | MAX(quantity) | MIN(name) | MAX(name) |
| --- | --- | --- | --- |
| 1 | 10 | A | C |

<br/><br/>

>**Reference** <br/> SQL 첫걸음 : 아사이 아츠시 지음, 박준용 옮김, 한빛미디어
