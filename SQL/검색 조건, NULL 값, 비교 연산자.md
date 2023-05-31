## 검색 조건, NULL 값, 비교 연산자



<br/>

## 세미콜론

```sql
mysql> select * from sample706
    -> ...
```

mysql은 명령의 마지막에는 세미콜론(;)을 붙여줘야 한다. (그렇지 않으면 계속 다음줄로 넘어감)

<br/><br/>


## 명령의 종류

```
SELECT * FROM sample21
```


<br/>

### SELECT 구

SELECT는 SQL 명령의 한 종류로 'SELECT 명령을 실행하세요' 라는 의미이다.

<br/>

### * (=열을 의미한다)

애스터리스크(*) '모든 열'을 의미하는 메타문자이다.

sample21 테이블의 모든 데이터를 읽어 온다.


<br/>

### FROM 구

처리 대상 테이블을 지정하는 키워드이다.

FROM 뒤에 테이블명을 지정한다.


<br/><br/>

## 테이블 구조

![이미지](/programming/img/입문160.PNG)

'no'라는 열은 숫자만으로 구성된 데이터를 ‘수치형’ 데이터라고 한다.

'name' 이라는 열은 ‘문자열형’ 데이터라고 부른다. 

'birthday' 라는 열은 ‘날짜시간형’ 데이터라고 부른다.


<br/>


### 값이 없는 데이터 = NULL

NULL이라는 데이터가 저장되어 있는 것이 아닌, '아무 것도 저장되어 있지 않은 상태'라는 뜻이다.

<br/><br/>

## 테이블 구조 참조하기

```sql
DESC 테이블명;
```

![이미지](/programming/img/입문161.PNG)


<br/><br/>


## 검색 조건 지정하기 (where)

조건을 지정하여 데이터를 검색하는 방법

```sql
select 열1, 열2 from 테이블명 where 조건식
```

(중요) `행`을 선택할 때는 `where` 구를 사용하며, `열`을 선택할 때는 `select` 구를 사용한다.




<br/><br/>

## select 구에서 열 지정하기

```sql
select 열1, 열2 from 테이블명
```

열은 콤마(,)를 이용하여 구분 짓는다.


![이미지](/programming/img/입문336.PNG)


<br/>

```sql
select address, name from sample21
```

결과는 지정한 열의 순서대로 표시된다.

그리고 동일한 열을 중복해서 지정해도 무관하다.


<br/><br/>

## 예제1)

```sql
SELECT * FROM student;
```

전체 읽어 오는것.

```sql
SELECT name, birthday FROM student;
```

name, birthday 컬럼만 가져 오는 것이다.

![이미지](/programming/img/입문176.PNG)





<br/><br/>


## where 구에서 행 지정하기

where 구는 from 구의 뒤에 표기한다.

```sql
select 열 from 테이블명 where 조건식
```

<br/>

### where 구처럼 생략 가능한 것도 있다.

만약, where 구를 생략한 경우는 테이블 내의 모든 행이 검색 대상이 되는 것이다.

<br/>

### 'no = 1' 조건식

이 조건식에 일치하는 행만 select의 결과를 반환한다.


![이미지](/programming/img/입문337.PNG)

포인트는 : where 구의 조건에 일치하는 행만 결과로 반환 한다는 것이다.


<br/><br/>

## 조건식

조건식 'no = 2'는 no열 값이 2일 경우에 참이 되는 조건이다.

'=' 연산자를 기준으로 서로 같으면 참을, 같지 않으면 거짓을 반환한다.

<br/>

no = 2의 조건식에서 no열 값이 2인 행은 참이 되며 1이나 3인 행은 거짓이 된다.



<br/><br/>

## 예제2)

```sql
SELECT * FROM student WHERE id=3;
```

3번 id인 사람의 정보만 가져 오는 것이다.

![이미지](/programming/img/입문177.PNG)








<br/><br/>

## 값이 서로 다른 경우 '<>'

서로 다른 값인지를 비교하는 연산자이다.

no열 값이 2가 아닐 경우 참이 되므로, 값이 1과 3인 행이 결과로 표시된다.


![이미지](/programming/img/입문162.PNG)

<br/><br/>


## where 예제)

### 나이가 30세 이상

```sql
select name, age
from Address
where age >= 30;
```

| name | age |
| --- | --- |
| 인성 | 30 |
| 준 | 45 |
| 민 | 32 |
| 하린 | 55 |


<br/>

### 주소가 서울시 이외

```sql
select name, address
from Address
where address <> '서울시';
```

| name | address |
| --- | --- |
| 인성 | 부산시 |
| 준 | 인천시 |
| 민 | 속초시 |
| 하린 | 서귀포시 |


<br/>

### 주소가 서울시에 있다 그리고 나이가 30세 이상이다.

```sql
select name, address, age
from Address
where address = '서울시'
AND age >= 30;
```

| name | address | age |
| --- | --- | --- |
| 인성 | 서울시 | 30 |
| 준 | 서울시 | 45 |





<br/><br/>

## 문자열형의 상수

문자열형을 비교할 경우는 ‘박준용’ 처럼 싱글쿼트`(’ ’)`로 둘러싸 표기 해야 한다.

위 사진에서 '박준용' 만 가지고 오고 싶다면 밑에 쿼리처럼 작성해야 한다.

```sql
SELECT * FROM sample21 WHERE name = '박준용'
```

<br/><br/>

## NULL 값 검색

= 연산자로 NULL 을 검색할 수 없다.


<br/>


### 틀린 코드

```sql
select name, address
from Address
where phone_nbr = NULL;
```

실제로 제대로 작동하는 select구문이 아니다. (물론 오류가 발생하지도 않는다)


`null을 검색하고 싶을때는 'IS NULL' 사용하기`

<br/><br/>

## IS NULL

NULL 값을 검색할 때는 = 연산자가 아닌 `‘IS NULL’` 을 사용 해야 한다.

`IS NULL` 을 사용해 birthday가 NULL인 행만 추출

```sql
SELECT * FROM sample21 WHERE birthday IS NULL;
```

<br/>

### 출력값

| no | name | birthday | address |
| --- | --- | --- | --- |
| 2 | 김재진 | NULL | 대구광역시 동구 |
| 3 | 홍길동 | NULL | 서울특별시 마포구 |

반대로 NULL 값이 아닌 행을 검색하고 싶다면 `‘IS NOT NULL’` 을 사용하면 된다.

<br/><br/>

## 비교 연산자

대표적인 비교 연산자들은 이렇게 있다.

```
`=` , `<>` , `>` , `>=` , `<` , `<=`
```

<br/><br/>

>**Reference** <br/> SQL 첫걸음 : 아사이 아츠시 지음, 박준용 옮김, 한빛미디어
