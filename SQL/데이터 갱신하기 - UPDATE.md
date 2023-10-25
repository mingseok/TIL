## 데이터 갱신 (UPDATE)

<br/>

## 문법

```sql
UPDATE 테이블명 SET 열명 = 값 where 조건식
```

`where` 구를 생략한 경우에는 `DELETE`의 경우와 마찬가지로 테이블의 모든 행이 갱신된다.

<br/>

`UPDATE` 명령에서는 `SET` 구를 사용하여 갱신할 열과 값을 지정한다.

문법은 `'SET 열명 = 값'` 이다.

이때, `=`은 비교 연산자가 아닌, 값을 대입하는 대입 연산자이다.

<br/><br/>

## 셀 값을 갱신하기

![이미지](/programming/img/입문349.PNG)

<br/><br/>

## UPDATE 명령으로 증가 연산하기


![이미지](/programming/img/입문350.PNG)


<br/><br/>



## 복수열 갱신

밑에 코드 처럼 하나로 묶어서 update 명령을 실행 할 수 있다

```sql
UPDATE sample41 SET a = 'xxx', b = '2014-01-01' WHERE no = 2;
```

<br/><br/>

## 예제1) 복수열 mySQL 같은 경우

<br/>

### 테이블 확인

| no | a | b |
| --- | --- | --- |
| 2 | ABC | 2014-01-25 |
| 3 | XYZ | 2014-01-25 |

<br/>

### 검색 결과 쿼리

```sql
update sample41 set no = no + 1, a = no;

select * from sample41;
```

<br/>


### 검색 결과 확인

| no | a | b |
| --- | --- | --- |
| 3 | 3 | 2014-01-25 |
| 4 | 4 | 2014-01-25 |

no 열과 a 열의 값이 서로 같아진다.

no 열의 값에 1을 더하여 no 열에 저장한 뒤, 그 값이 다시 a열에 대입되기 때문이다.


<br/><br/>


## 예제2) 복수열 mySQL 같은 경우

```sql
update sample41 set a = no, no = no + 1;

select * from sample41;
```


<br/>

### 검색 결과 확인


| no | a | b |
| --- | --- | --- |
| 4 | 3 | 2014-01-25 |
| 5 | 4 | 2014-01-25 |










<br/><br/>

## 예제 테이블 출력

![이미지](/programming/img/입문168.PNG)

<br/><br/>

## 예제1)

```sql
UPDATE `student` SET address='서울';
```

즉, `address` 라는 컬럼에 `‘서울’` 을 저장하겠다. 하는 것이다.

<br/>

그러면, 모두 서울로 변하는 것이다.

![이미지](/programming/img/입문169.PNG)

<br/><br/>


## 예제2)

```sql
UPDATE `student` SET name='이진경' WHERE id=1;
```

### where 문은 “어디에 적용 될 것인가?” , ”어떤 행에 적용 될 것인가?” 지정하는 키워드이다.

<br/>

그렇다면 WHERE id=1; 이라고 했다면, `id` 컬럼의 값이 1인 행에 대해서 실행 한다는 것이다.

![이미지](/programming/img/입문170.PNG)

즉, where 부터 본다는 것이다.

<br/><br/>

## 예제3)


```sql
UPDATE `student` SET name='이고잉', birthday='2001-4-1' WHERE id=3;
```

<br/>

이렇게 변경 되는 것이다.

![이미지](/programming/img/입문171.PNG)


<br/><br/>

## 만약, 개발팀 연봉을 두 배로 인상하고 싶다면?


### EMPLOYEE : 직원

| id | name | birth_date | sex | position(직군) | salary(연봉) | dept_id(부서id) |
| --- | --- | --- | --- | --- | --- | --- |

<br/>

### PROJECT : 이, 회사가 진행하는 프로젝트 정보

| id | name | leader_id | start_date | end_date |
| --- | --- | --- | --- | --- |


<br/>

### WORKS_ON : 직원들이 어떤 프로젝트에 일하고 있는지에 대한 정보

| empl_id | proj_id |
| --- | --- |



<br/><br/>

- 개발팀 ID는 1003이다.

```sql
update employee
set salary = salary * 2
where dept_id = 1003;
```

<br/><br/>

## 위 테이블 참고) 만약, 프로젝트에 참여한 직원의 연봉을 두 배로 인상하고 싶다면?

- 프로젝트팀의 ID는 2003이다.

- 일단, 직원들의 정보도 알아야하기 때문에 `employee` 테이블도 연관이 있을 것이다.


<br/>

### 추가로 알고 있어야 할 테이블

`WORKS_ON`: 직원들이 어떤 프로젝트에 일하고 있는지에 대한 정보인 테이블이다.


| empl_id | proj_id |
| --- | --- |


```sql
update employee, works_on
set salary = salary * 2
where employee.id = works_on.empl_id and works_on.proj_id = 2003;
```

<br/>

### where employee.id = works_on.empl_id and works_on.proj_id = 2003; -> 설명

- `where employee.id`: employee 테이블에 있는 id이다.

- `= works_on.empl_id`: works_on 테이블에 있는 empl_id 이다


```
여기까지 무슨 뜻이냐?
-> `employee.id = works_on.empl_id` 가 동일하다는 걸 말하는 것이다. 
-> 즉, "연결고리 역할을 하는 것이다."


그리고 `and works_on.proj_id = 2003` 
직원 id가 2003일때 연봉을 올려주는 것이다.
```

<br/><br/>

## 회사의 모든 구성원의 연봉을 두 배로 올리자.

```sql
update employee
set salary = salary * 2;
```



