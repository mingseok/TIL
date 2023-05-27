## 중복값 제외 (DISTINCT)


SQL의 SELECT 명령은 이러한 중복된 값을 제거하는 함수를 제공한다.

<br/>


## DISTINCT로 중복 제거


테이블을 확인하기 위해 `ALL` 명령어를 사용하였다. 

ALL, DISTINCT 둘다 사용하지 않으면 디폴트 값은 ALL이 된다.

밑 테이블 결과를 보면, name 열을 보면 `A`로 값이 중복되어 있는 것을 알수 있다.



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

### DISTINCT를 사용하여 중복을 제거한다.

```sql
SELECT DISTINCT name FROM smaple51;
```

| name |
| --- |
| A |
| B |
| C |
| NULL |


<br/><br/>

## 예제)

중복을 제거한 뒤 개수 구하기

```sql
SELECT COUNT(ALL name), COUNT(DISTINCT name) FROM sample51;
```

| COUNT(ALL name) | COUNT(DISTINCT name) |
| --- | --- |
| 4 | 3 |

NULL 을 제외한 값의 개수

DISTINCT 와 ALL은 인수가 아니므로 콤마는 붙이지 않는다.