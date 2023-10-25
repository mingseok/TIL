## 행 추가 - (INSERT)

`신규등록`이나 `추가`와 같은 버튼을 클릭 했을때 처리되는 데이터 추가 기능이라 생각하면 된다.

<br/>

## 테이블 설명

| Field | Type | Null | Key | Default | Extra |
| --- | --- | --- | --- | --- | --- |
| no | int(11) | NO |  | NULL |  |
| a | varchar(30) | YES |  | NULL |  |
| b | date | YES |  |  |  |


- no 열은 int(11) 이므로 수치형 데이터를 저장할 수 있다.

- a 열은 varchar(30)이므로 최대 길이가 30인 문자열을 저장할 수 있다.

- b 열은 날짜시간형 데이터를 저장할 수 있다.


<br/>

### 이런식으로 추가하면 되는 것이다.

```sql
insert into sample41 values(1, 'ABC', '2014-01-25')
```



<br/><br/>

## 문법

또 다른 문법으로는 컬럼들이 들어가서 values에 들어가는 것이다. 



```sql
INSERT INTO table_name VALUES (value1, value2, value3,...)
```





</br>

### 이렇게 앞부분을 생략하고 많이 사용한다.

```sql
INSERT INTO `student` VALUES ('2', '김스', '여자', '서울', '2000-10-26');
```

</br>

insert 해보면 이렇게 저장 되는 걸 알 수 있다.

![이미지](/programming/img/입문163.PNG)


</br></br>

## 추가로 내가 넣고 싶은 데이터만 삽입 할 수 있다.


### 기존 테이블 스키마

| id | name | birth_date | sex | position(직군) | salary(연봉) | dept_id(부서id) |
| --- | --- | --- | --- | --- | --- | --- |

</br>

### 넣고 싶은 데이터만 넣기

id인 프라이머리키가 뒤로 빠진걸 알 수 있고,

salary(연봉), dept_id(부서id) 두개의 컬럼이 빠진걸 알 수 있다.

```sql
insert into employee (name, birth_date, sex, position, id) 
values ('JENNT', '2000-10-12', 'F', 'DEV_BACK', 3);
```

### 만약, 넣지 않은 경우는 무슨 경우 일까?

아마도, 해당 attribute를 보면 default값으로 설정되어 있기 때문일 것이다. (=아닐수도 있다.)




</br></br>

## 값을 저장할 열 지정하기

no 열과 a 열만 지정해 행 추가하기

```sql
insert into sample41(a, no) values('XYZ', 2);
```

<br/>

### 출력값 -> 이렇게 추가 되는 것이다.


| no | a | b |
| --- | --- | --- |
| 2 | XYZ | NULL |



</br></br>


## NOT NULL 제약

`NOT NULL` 제약이 걸려 있는 곳은 NULL을 추가 할 수 없다.