## 집계함수 (SUM, AVG, MIN, MAX)

SUM으로 합계 구하기

| no | quantity |
| --- | --- |
| 1 | 1 |
| 2 | 2 |
| 3 | 10 |
| 4 | 3 |
| 5 | NULL |

출력값 → 16


SUM 집계함수의 인수로 이 집합을 지정하면 1+2+3으로 계산하여 6이라는 값을 반환한다.

<br/>

## SUM으로 quantity 열의 합계 구하기.

```sql
SELECT SUM(quantity) FROM sample51;
```


| SUM(quantity) |
| --- |
| 16 |

한편 SUM 집계함수도 COUNT와 마찬가지로 NULL 값을 무시한다.

NULL 값을 제거한 뒤에 합계를 낸다.

<br/>

## AVG로 평균내기

```sql
SELECT AVG(quantity), SUM(quantity)/COUNT(quantity) FROM sample51;
```



AVG 집계함수도 NULL 값을 무시한다.


<br/>

### 만약 NULL을 0로 간주해서 평균을 내고 싶다면

CASE를 사용해 NULL을 0로 변환한 뒤에 AVG 함수로 계산하면 된다.

```sql
SELECT AVG(CASE WHEN quantity IS NULL THEN 0 ELSE quantity END) 
AS avgnull0 FROM sample51;
```


| avgnull0 |
| --- |
| 3.2000 |

<br/>

## MIN / MAX 로 최솟값, 최댓값 구하기

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

```sql
SELECT MIN(quantity), MAX(quantity), MIN(name), MAX(name)
FROM sample51;
```



| MIN(quantity) | MAX(quantity) | MIN(name) | MAX(name) |
| --- | --- | --- | --- |
| 1 | 10 | A | C |

<br/><br/>

>**Reference** <br/> SQL 첫걸음 : 아사이 아츠시 지음, 박준용 옮김, 한빛미디어
