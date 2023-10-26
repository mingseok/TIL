## 데이터 조회 (SELECT) / AS 사용 / 중복값 제외 (DISTINCT)

SELECT statement 라고도 부른다.

문제들의 상황에 따라 설명하도록 한다.

<br/>


## 문제 1)

id가 9인 직원의 이름과 직군을 알고 싶다.

```sql
select name, position 
from employee
where id = 9;
```

- 앞부분 `name, position`은 projection attributes 라고 부른다.


- 마지막 `id = 9` 조건 부분을 selection condition 이라고 부른다.


<br/><br/>

## 문제 2)

project 2002를 리딩하고 있는 직원의 id와 이름과 직군을 알고 싶다.


![이미지](/programming/img/입문407.PNG)


그림을 설명하자면, `leader_id`를 통해서 -> `employee`테이블로 접근이 가능하게 되는 것이다.

그렇게, 최종적으로 내가 관심있는 attributes들에게 접근하면 되는 것이다.


<br/>

### 쿼리 작성

```sql
select employee.id, employee.name, position
from project, employee
where project.id = 2002 and project.leader_id = employee.id;
```

<br/>

### 그림으로 설명

1. `project` 테이블에 id 컬럼이 `2002`에 대한 튜플(=가로 한줄)이 선택이 되는 것이다.

2. 그리고 `project` 테이블에 `leader_id` 컬럼 데이터와 

    `employee` 테이블의 `id`가 같아야 된다고 조건을 명시 하는 것이다.

    - 즉, 두개의 테이블을 연결 시키는 역할을 하는 것이다.


3. where구의 조건은 `and`로 묶여있기 때문에 `project` `id`가 `2002`인 `leader_id`로 선택 되는 것이다.

4. 추가로) `employee.id` 처럼 id 앞에 테이블 이름을 작성해주는 이유는, `attribute`가 충돌나기 때문이다. 

    - 만약, 충돌이나지 않는다면 테이블명을 작성하지 않아도 된다.

![이미지](/programming/img/입문408.PNG)

<br/>

### 출력값 확인

| id | name | position |
| --- | --- | --- |
| 13 | JISUNG | PO |






<br/><br/>



### 문제 3) 나이가 30세 이상

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

### 문제 4) 주소가 서울시 이외

```sql
select name, address
from Address
where address != '서울시';
```

| name | address |
| --- | --- |
| 인성 | 부산시 |
| 준 | 인천시 |
| 민 | 속초시 |
| 하린 | 서귀포시 |


<br/>

### 문제 5) 주소가 서울시에 있다, 그리고 나이가 30세 이상이다.

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

## AS 사용하기

AS는 테이블이나 attribute에 별칭을 붙일 때 사용한다.

```
- AS는 생략도 가능하다.
```
<br/>

### 문제 2번을 예시로 설명하겠다.

이렇게 수정이 가능하다

- 포인트는 `from`절에서 `project AS P` 이렇게 선언을 해줘야 한다는 것이다.

- `from` 절에도 AS 생략 가능하다.



```sql
select E.id, E.name, position
from project AS P, employee AS E
where P.id = 2002 and P.leader_id = E.id;
```

<br/>


### 그리고 출력값을 확인해보자.

`id`, `name`, `position`으로 출력 시키기는 뭔가 명확하지 않다.

그렇기에 이것도 AS 사용하여 바꿔주는 것이다.

즉, 셀렉트 결과에 대한 이름에 대해서 새로운 이름을 붙여주고 싶은 것이다.

| id | name | position |
| --- | --- | --- |
| 13 | JISUNG | PO |


<br/>


### 쿼리 변경해보기


```sql
select E.id AS leader_id, E.name AS leader_name, position
from project AS P, employee AS E
where P.id = 2002 and P.leader_id = E.id;
```

이렇게 작성이 가능한 것이다.

- AS는 생략 가능하다.


<br/><br/>


## 중복값 제외 (DISTINCT)


SQL의 SELECT 명령은 이러한 중복된 값을 제거하는 함수를 제공한다.

<br/>


## 문제 1)

디자이너들이 참여하고 있는 프로젝트들의 ID와 이름을 알고 싶다

- 디자이너와 프로젝트를 연결 시켜주는 것이 `WORKS_ON` 테이블인것이다.

- 왜냐면, `WORKS_ON` 테이블은 어떤 직원이, 어떤 프로젝트에 일하고 있는지 알 수 있기 때문이다.

![이미지](/programming/img/입문410.PNG)


<br/>

### 쿼리 작성

```sql
select P.id, P.name
FROM employee AS E, works_on AS W, project P
WHERE E.position = 'DSGN' and
      E.id = W.empl_id and W.proj_id = P.id;
```


<br/>

### 출력값 확인

```
중복값이 발생했다! -> 이때 사용하는 것이 DISTINCT 키워드 인것이다.
```

| id | name |
| --- | --- |
| 2002 | 백엔드 리팩토링 |
| 2003 | 홈페이지 UI 개선 |
| 2003 | 홈페이지 UI 개선 |

<br/>


### 쿼리 수정

DISTINCT 키워드를 사용할 경우 P.id, P.name에 대해서들은 

중복된 값을 제외하고 나오게 되는 것이다.

```sql
select DISTINCT P.id, P.name
FROM employee AS E, works_on AS W, project P
WHERE E.position = 'DSGN' and
      E.id = W.empl_id and W.proj_id = P.id;
```