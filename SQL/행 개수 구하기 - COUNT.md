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
