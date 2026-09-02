# INSERT - 행 추가

`신규등록`이나 `추가`와 같은 버튼을 클릭 했을때 처리되는 데이터 추가 기능이라 생각하면 된다.

<br/>



## 문법

또 다른 문법으로는 컬럼들이 들어가서 values에 들어가는 것이다. 



```sql
insert into sample41 values(1, 'ABC', '2014-01-25')
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

## 추가로 내가 넣고 싶은 데이터만 삽입 할 수도 있다.

</br>

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

</br>

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




# UPDATE - 데이터 갱신



## 예제1)

```sql
UPDATE `student` 
SET address='서울';
```

즉, `address` 라는 컬럼에 `‘서울’` 을 저장하겠다. 하는 것이다.

<br/>

그러면, 모두 서울로 변하는 것이다.

![이미지](/programming/img/입문169.PNG)

<br/><br/>


## 예제2)

```sql
UPDATE `student` 
SET name='이진경' 
WHERE id=1;
```

WHERE id=1; 이라고 했다면, `id` 컬럼의 값이 1인 행에 대해서 실행 한다는 것이다.

![이미지](/programming/img/입문170.PNG)

즉, where 부터 본다는 것이다.

<br/><br/>

## 예제3)


```sql
UPDATE `student` 
SET name='이고잉', birthday='2001-4-1' 
WHERE id=3;
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

<br/><br/>

# 삭제하기 - DELETE

행단위로 데이터를 삭제한다.


<br/>

## 조건을 달아서 삭제하기.

```sql
DELETE FROM student WHERE id = 2;
```

위에 있는 테이블 사진과 비교하면, ‘박재숙’ 이 없어진 걸 알 수 있다.

![이미지](/programming/img/입문173.PNG)


<br/><br/>

## 테이블 내용 전체 삭제.

```sql
DELETE FROM student;
```

<br/><br/>

## TRUNCATE (자르다)

- 테이블의 전체 데이터를 삭제

- 테이블에 외부키(foreign key)가 없다면 DELETE보다 훨씬 빠르게 삭제됨



### 예제

```sql
TRUNCATE student;
```

<br/>

다 지워 지는 걸 알 수 있다.

![이미지](/programming/img/입문174.PNG)


<br/><br/>

## DROP TABLE - 테이블을 삭제한다.



컬럼이 아닌 테이블을 삭제 하는 것이다.

```sql
DROP TABLE student;
```


<br/><br/>

## delete 문제 1)

John이 퇴사를 하게 되었다. employee 테이블에서 John 정보를 삭제해야 한다.



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


<br/>

### 쿼리 작성

- john의 employee ID는 8이다

- 현재 John은 project 2001에 참여하고 있었다.

```sql
delete from employee where id = 8;
```

<br/>

### 그런데, WORKS_ON 테이블에도 해당 사원의 id가 남아 있는데, 삭제해야 되는 것이 아닌가?

처음 WORKS_ON 테이블을 설계 할때를 보면 이렇다.

```sql
create table WORKS_ON (
    empl_id  int,
    proj_id  int,
    primary key (empl_id, proj_id),
    foreign key (empl_id) references EMPLOYEE(id) 
       on delete CASCADE on update CASCADE,
    foreign key (proj_id) references PROJECT(id) 
       on delete CASCADE on update CASCADE,
);
```

그렇기에, `employee` 테이블에서만 `id`를 삭제하면, 

`WORKS_ON` 테이블의 지정한 `empl_id`도 함께 삭제 되는 것이다.

<br/><br/>

## delete 문제 2)

Jane이 휴직을 떠나게 되면서, 현재 진행 중인 프로젝트에서 중도하차하게 됐다.

- jane의 id는 2다

```sql
delete from works_on where impl_id = 2;
```

<br/><br/>

## delete 문제 3)

Dingyo가 두 개의 프로젝트에 참여하고 있었는데, 

프로젝트 2001에 선택과 집중을 하기로 하고 프로젝트 2002에서는 빠지기로 했다.

- Dingyo의 id는 5다

<br/>

### 쿼리 작성



```sql
delete from works_on where impl_id = 5 and proj_id = 2002;
```

Dingyo의 id는 5였기에 `impl_id = 5` 라고 작성한 것이다.

그리고 2001 프로젝트에 집중하기 위해, 2002에는 빠지는 것이기에, 

`and proj_id = 2002` 라고 작성해 주는 것이다.

```
만약, 딩유가 하던 프로젝트가 여러개라면?

delete from works_on where impl_id = 5 and proj_id != 2001;
```
<br/>

## 궁금증!

```java
한 건씩 넣는 것과 한 번에 넣는 것이 얼마나 다른가
```

문법부터 다르다.

```sql
INSERT INTO member (name, age) VALUES ('민석', 30);
INSERT INTO member (name, age) VALUES ('영한', 25);
INSERT INTO member (name, age) VALUES ('지성', 28);
```

```sql
INSERT INTO member (name, age) VALUES
    ('민석', 30), ('영한', 25), ('지성', 28);
```

<br/>

아래쪽이 훨씬 빠르다. 이유가 몇 가지 겹친다.

```java
네트워크 왕복이 3번에서 1번으로 준다
파싱과 실행 계획 수립이 1번만 일어난다
로그 기록이 한 번에 처리된다
```

<br/>

건수가 많아지면 차이가 몇십 배까지 벌어진다.

<br/>

## JPA 에서는 이게 안 되는 경우가 있다

```java
for (Member member : members) {
    em.persist(member);
}
```

<br/>

앞의 쓰기 지연 글에서 본 대로 모아뒀다가 한 번에 보내는데,

`IDENTITY` 전략이면 모을 수가 없다.

```java
@GeneratedValue(strategy = GenerationType.IDENTITY)
```

<br/>

식별자를 DB가 정해주니, `persist` 하는 순간 `INSERT` 를 날려야 값을 알 수 있다.

<br/>

그래서 배치 삽입이 필요하면 전략을 바꾼다.

```java
@GeneratedValue(strategy = GenerationType.SEQUENCE)
```

<br/>

시퀀스는 미리 번호를 받아올 수 있어서 모아둘 수 있다.

MySQL에는 시퀀스가 없어서 `TABLE` 전략을 쓰거나 JDBC로 직접 넣는다.

<br/>

## 중복이면 어떻게 할지 정하는 문법들

```sql
INSERT IGNORE INTO member ...                    -- 중복이면 조용히 무시. MySQL
INSERT ... ON DUPLICATE KEY UPDATE age = 30      -- 중복이면 수정. MySQL
INSERT ... ON CONFLICT DO NOTHING                -- PostgreSQL
INSERT ... ON CONFLICT (email) DO UPDATE SET ... -- PostgreSQL
MERGE INTO ...                                   -- 표준, 오라클
```

<br/>

`INSERT IGNORE` 는 조심해야 한다.

중복뿐 아니라 다른 오류까지 경고로 낮춰버린다.

```sql
INSERT IGNORE INTO member (age) VALUES ('서른');   -- 0 이 들어간다
```

<br/>

타입이 안 맞는데도 오류가 안 나고 들어가는 것이다.

<br/>

## 조회한 것을 그대로 넣기

```sql
INSERT INTO member_archive (id, name)
SELECT id, name FROM member WHERE deleted = true;
```

<br/>

`VALUES` 대신 `SELECT` 를 쓴다.

<br/>

앞의 물리논리삭제 글에서 본 아카이브 이관에 쓰는 방식이다.

애플리케이션으로 데이터를 가져왔다가 다시 넣을 필요가 없다.

<br/>

수백만 건이면 한 번에 하면 안 된다.

트랜잭션이 커져서 락이 오래 잡히고, undo 로그가 쌓인다.

```sql
INSERT INTO member_archive SELECT * FROM member WHERE id BETWEEN 1 AND 10000;
INSERT INTO member_archive SELECT * FROM member WHERE id BETWEEN 10001 AND 20000;
```

<br/>

나눠서 도는 게 낫다.

<br/>

## 넣은 행의 식별자를 받는 방법

```sql
SELECT LAST_INSERT_ID();                       -- MySQL
INSERT ... RETURNING id;                       -- PostgreSQL, 오라클
```

<br/>

`LAST_INSERT_ID()` 는 커넥션마다 따로 관리된다.

그래서 다른 사람이 그 사이에 넣어도 내 값이 나온다.

<br/>

앞의 커넥션 풀 글에서 본 대로 커넥션을 돌려쓰는 환경에서도

같은 커넥션 안이면 안전한 것이다.

<br/>

여러 건을 한 번에 넣으면 첫 번째 값이 나온다는 게 함정이다.

<br/>

## 자동 증가 번호는 롤백해도 안 돌아온다

```sql
BEGIN;
INSERT INTO member (name) VALUES ('민석');    -- id = 10
ROLLBACK;
INSERT INTO member (name) VALUES ('영한');    -- id = 11. 10 은 비었다
```

<br/>

번호에 구멍이 생긴다.

<br/>

번호를 되돌리려면 다른 트랜잭션을 막아야 하는데 그럴 수 없기 때문이다.

<br/>

그래서 자동 증가 번호를 `주문 번호` 로 그대로 쓰면 안 된다.

`몇 번까지 나왔는지` 로 총 건수를 짐작할 수도 있고, 구멍이 있으면 설명하기 곤란해진다.

<br/>

주문 번호는 따로 만드는 게 낫다.

```java
20260901-000123
```

<br/>

## 대량 적재는 전용 명령이 따로 있다

```sql
LOAD DATA INFILE 'members.csv' INTO TABLE member;    -- MySQL
COPY member FROM 'members.csv' CSV;                  -- PostgreSQL
```

<br/>

`INSERT` 를 수만 번 하는 것보다 몇 배에서 몇십 배 빠르다.

파싱과 트랜잭션 처리를 건너뛰고 바로 적재하기 때문이다.

<br/>

앞의 테이블 관리 글에서 본 대로,

인덱스를 잠깐 끄고 적재한 다음 다시 만드는 것과 같이 쓰면 더 빨라진다.
