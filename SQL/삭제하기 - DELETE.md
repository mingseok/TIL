## 삭제하기 - DELETE

행단위로 데이터를 삭제한다.

<br/>

## DELETE로 행 삭제하기 문법

```sql
DELETE FROM 테이블명 WHERE 조건식
```

<br/><br/>

### 이런 표가 있다고 생각하자.

![이미지](/programming/img/입문172.PNG)

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

<br/>

### 문법

```sql
TRUNCATE 테이블명
```

<br/>

### 예제

```sql
TRUNCATE student;
```

<br/>

다 지워 지는 걸 알 수 있다.

![이미지](/programming/img/입문174.PNG)


<br/><br/>

## DROP TABLE - 테이블을 삭제한다.

<br/>

### 문법

```sql
DROP TABLE 테이블명;
```

<br/>

### 예제

컬럼이 아닌 테이블을 삭제 하는 것이다.

```sql
DROP TABLE student;
```


<br/><br/>


## John이 퇴사를 하게 되었다. employee 테이블에서 John 정보를 삭제해야 한다.



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

## Jane이 휴직을 떠나게 되면서, 현재 진행 중인 프로젝트에서 중도하차하게 됐다.

- jane의 id는 2다

```sql
delete from works_on where impl_id = 2;
```

<br/><br/>

### Dingyo가 두 개의 프로젝트에 참여하고 있었는데, 프로젝트 2001에 선택과 집중을 하기로 하고 프로젝트 2002에서는 빠지기로 했다.

- Dingyo의 id는 5다

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