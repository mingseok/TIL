## 데이터 갱신하기 - UPDATE

```sql
UPDATE 테이블명 SET 열명 = 값 WHERE 조건식
```

UPDATE 명령에서는 SET 구를 사용하여 갱신할 열과 값을 지정한다.


문법은 ‘SET’ 열명 = ‘값’ 이다. 이때  = 은 비교 연산자가 아닌, 값을 대입하는 대입 연산자 이다.

<br/>

### UPDATE 명령으로 행의 셀 값을 갱신할 수 있다.

그리고 WHERE 구를 생략하면 테이블의 모든 행이 갱신 대상이 된다.

<br/>

## 퀴즈) 다음 쿼리를 실행하면 어떻게 되는가?

```sql
UPDATE sample41 SET no = no + 1;
```

이 명령문에는 WHERE 구가 지정되어 있지 않으므로 갱신 대상은 테이블의 모든 행이 된다.

마치 프로그래밍 예제에 자주 등장하는 증가 연산과 같은 형식을 취한다.

```sql
SELECT * FROM sample41;
```

| no | a | b |
| --- | --- | --- |
| 1 | ABC | 2014-01-25 |
| 2 | XYZ | 2014-09-07 |

<br/>

```sql
UPDATE sample41 SET no = no + 1;
```

| no | a | b |
| --- | --- | --- |
| 2 | ABC | 2014-01-25 |
| 3 | XYZ | 2014-09-07 |

실행 결과, 모든 행위 no 값에 1씩 더해진 것을 알 수 있다.

현재의 no 값에 1을 더한 값으로 no 열을 갱신하라는 의미 이다.

<br/>

## 복수열 갱신

UPDATE 명령의 SET 구에서는 필요에 따라 콤마(,)로 구분하여 갱신할 열을 여러 개 지정 할 수 있다

```sql
UPDATE 테이블명 SET 열명1 = 값1, 열명2 = 값2, ... WHERE 조건식
```

<br/>

UPDATE 명령 실행 1

```sql
UPDATE sample41 SET no = no + 1, a = no;
```

| no | a | b |
| --- | --- | --- |
| 3 | 3 | 2014-01-25 |
| 4 | 4 | 2014-09-07 |

따라서 a 열의 값은 ‘no - 1’ 한 값이 된다.

<br/>

### UPDATE 명령 실행 2

```sql
UPDATE sample41 SET a = no, no = no + 1;
```

| no | a | b |
| --- | --- | --- |
| 4 | 3 | 2014-01-25 |
| 5 | 4 | 2014-09-07 |


<br/>

## NULL로 갱신하기

UPDATE sample41 SET b = NULL과 같이 갱신할 값으로 NULL을 지정하면 된다.

보통 ‘NULL 초기화’ 라 부르기도 한다.

```sql
UPDATE sample41 SET a = NULL;
```

| no | a | b |
| --- | --- | --- |
| 4 | NULL | 2014-01-25 |
| 5 | NULL | 2014-09-07 |

no 열에는 NOT NULL 제약이 설정되어 있으므로 no 열의 셀을 NULL로 갱신할 수 없다.

<br/><br/>

>**Reference** <br/> SQL 첫걸음 : 아사이 아츠시 지음, 박준용 옮김, 한빛미디어
