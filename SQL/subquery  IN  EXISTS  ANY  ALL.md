## subquery / IN / EXISTS / ANY / ALL

<br/>


## 테이블 구조


![이미지](/programming/img/입문427.PNG)



<br/><br/>

## 문제 1)

id가 14인 직원보다 생일이 빠른 직원의 id, 이름, 생일을 알고 싶다.

<br/>

### 쿼리 작성

```sql
select birth_date
from employee
where id = 14;
```

<br/>

### 출력값 확인

| birth_date |
| --- |
| 1992-08-04 |

<br/>

### 추가적인 쿼리 작성

앞에 출력값으로 나온 1992-08-04 보다 빨라야 하기에 where에 조건으로 넣었다.

```sql
select id, name, birth_date
from employee
where birth_date < '1992-08-04';
```

<br/>

### 궁금증

1번 문제를 풀면서 쿼리를 두번 작성했다.

그렇다면 한번에 할 수는 없을까? → 가능하다. 그것이 `subquery`

![이미지](/programming/img/입문428.PNG)

이렇게 합칠 수가 있는 것이다.

<br/>

### 합쳐진 쿼리

`subquery`는 내부 쿼리부터 시작한다고 생각하면 된다.

```sql
select id, name, birth_date
from employee
where birth_date < (
                     select birth_date
                     from employee 
                     where id = 14
                   );
```

1. id가 14인 직원테이블의 생일보다 

2. “<” → 빠른 생일을 가지는 그 직원테이블들의

3. id, name, birth_date 를 출력하라고 하는 것이다.

<br/><br/>

## subquery란?

- `nested query`, `inner query` 라고도 불린다.

- `select`, `insert`, `update`, `delete`에 포함된 쿼리이다.
- `outer query`란?

    - 서브쿼리 밖에 있는 쿼리를 `outer query`라고 부른다.

- subquery를 기술할때는 ( ) 안에 꼭 기술 해야 한다.

<br/><br/>

## IN 이란?

만약, 이런 쿼리가 있다고 생각해보자.

```sql
proj_id = 10 OR proj_id = 20;
```

위에 쿼리는 둘 중 하나만 조건에 맞아도 된다는 조건문이다.

<br/>

이것을, 같은 동작을 하고 더 간단한 방법이 바로 → IN 키워드 이다.

```sql
proj_id IN (10, 20);
```

설명하자면, `proj_id`가 `IN` 괄호 안에 있는 것 중에서 하나라도 있다면 true가 되는 것이다.

- 즉, OR로 묶인것과 같은 의미가 된다는 것이다.

<br/>

### IN의 특징

- `v IN (v1, v2, v3, ...)`

    - v가 (v1, v2, v3 … ) 중에서 하나와 같이 같다면 `true`를 return 해준다.
- `v NOT IN (v1, v2, v3, ...)`

    - 이건 위에 설명의 반대라고 생각하면 된다.

<br/><br/>

## 문제 2)

ID가 1인 직원과 같은 부서, 같은 성별인 직원들의 ID와 이름과 직군을 알고 싶다.

<br/>

### 쿼리 작성

여기서 포인트는, 하나 이상의 attribute를 리턴 할 수 있다는 것이다.

```sql
select id, name, position
from employee
where (dept_id, sex) = (
                         select dept_id, sex
                         from employee
                         where id = 1
                       );
```

<br/><br/>

## 문제 3)

id가 5인 직원과 같은 프로젝트에 참여한 다른 직원들의 id를 알고 싶다

<br/>

### 쿼리 작성

```sql
select distinct empl_id
from works_on
where empl_id != 5 and proj_id in (
                                select proj_id       // 출력된건 2001, 2002
                                from works_on
                                where empl_id = 5
                                );
```

<br/>

### 서브쿼리 안에서의 출력값은?

id가 5인 직원은 프로젝트가 두개였단 뜻이다.

| proj_id |
| --- |
| 2001 |
| 2002 |

<br/>

### 전체 출력값 확인

- distinct 가 있던 이유는?

    - 어떤 직원은 두개의 프로젝트에 참여 했을수도 있기 때문에.

| empl_id |
| --- |
| 1 |
| 2 |
| 3 |
| … |
| 14 |

<br/><br/>

## 위 3번 문제에 조건 추가하기.

id가 5인 직원과 같은 프로젝트에 참여한 다른 직원들의 id와 `이름`를 알고 싶다

<br/>

### 쿼리 작성

```sql
select id, name
from employee
where id in (
         select distinct empl_id
         from works_on
         where empl_id != 5 and proj_id in (
                                         select proj_id       // 출력된건 2001, 2002
                                         from works_on
                                         where empl_id = 5
                                         )
         );
```

<br/><br/>

## 문제 4) / EXISTS 사용 설명

id가 7 혹은 12인 직원이 참여한 프로젝트의 id와 이름을 알고 싶다

<br/>

### 쿼리 작성

- EXISTS는 subquery의 결과가 최소 하나의 row라도 있다면 `true`를 반환한다.

- NOT EXISTS는 위 EXISTS 설명의 반대이다.

```sql
select P.id, P.name
from project P
where exists (
               select *
               from works_on W
               where W.proj_id = P.id AND W.empl_id in (7, 12)
             );
```

<br/><br/>

## 문제 5) → ANY 사용 설명

리더보다 높은 연봉을 받는 부서원을 가진 리더의 id와 이름과 연봉을 알고 싶다.

<br/>

### 쿼리 작성

`v 비교연산자 any (subquery)` 

- `subquery`가 반환한 결과들 중에 단 하나라도
    
    v 와의 비교 연산이 `true`라면 `true` 리턴한다.
    
- `some`도 `any`와 같은 역할을 한다.

```sql
select E.id E.name, E.salary
from department D, employee E
where D.leader_id = E.id and E.salary < any (
                                         select salary
                                         from employee
                                         where id != D.leader_id and dept_id = E.dept_id
                                         );
```

<br/><br/>

## 문제 6) → ALL

id가 13인 직원과 한번도 같은 프로젝트에 참여하지 못한 직원들의 id, 이름, 직군을 알고 싶다.

<br/>

### 쿼리 작성

- `v 비교연산자 all (subquery)`

    - subquery가 반환한 결과들과 v와의 비교 연산이 모두 true라면 true를 반환한다.

```sql
select distinct E.id, E.name, E.position
from employee E, works_on W
where E.id = W.empl_id and W.proj_id != all (
                                              select proj_id
                                              from works_on
                                              where empl_id = 13
                                             );
```