## inner join, outer join 종류

두 개 이상의 table들에 있는 데이터를 한 번에 조회하는 것

- 여러 종류의 join이 존재한다.

<br/><br/>

## inner join

```sql
select *
from employee E join department D on E.dept_id = D.id;
```

위 코드에서는,  `join` 앞에 `inner`가 생략된 것이다.

즉, 우리는 이미 inner 조인이라는 것을 사용하고 있었던 것이다.

<br/>

![이미지](/programming/img/입문434.PNG)

<br/>

### 출력값 확인하기

이 결과에서 중요한 것은, `employee`의 `SIMON`이랑 `department`의 `1002`랑은 테이블에서 빠졌다는 것이다.

![이미지](/programming/img/입문435.PNG)

<br/><br/>

## 이것이 inner 조인이다.

- inner join : 두 table에서 join condition을 만족하는 tuple들로 result table을 만드는 join

- from table1 E `inner join` table2 D `on` join_condition

    - 위 쿼리에서 from table1 E `join` table2 D `on` join_condition
        
        이렇게 사용 가능하다.
        
- 사용 가능한 연산자 : `=, <, >, ≠` 등등 여러 비교 연산자가 가능하다

- join condition에서 null 값을 가지는 tuple은 result table에 포함되지 못한다

<br/><br/>

## outer join

- outer join : 두 table에서 join condition을 만족하지 않는 tuple들도 result table에 포함하는 join

- outer join은 세가지 종류가 있다 → [ ] 안에 들어가 있는 건 생략이 가능하다는 것

    - from table1 `left [outer] join` table2 `on` join_condition

    - from table1 `right [outer] join` table2 `on` join_condition

- 사용 가능한 연산자: `=, <, >, ≠`

<br/>

### left [outer] join 출력값 확인하기

이제는 SIMON이 포함이 되었다.

![이미지](/programming/img/입문436.PNG)

`left`의 개념이 쿼리상에서 `left outer join` 기준으로 왼쪽에 있는 테이블을 의미한다. → employee 테이블

<br/>

그런 뜻은, `employee` 테이블에서 위에 `ON`(join_condition이라고 부름)에 
의해서 

매칭 되지 않는 tuple들 까지도 함께 출력 되기에 `SIMON`도 출력 되어 나온 것이다.

- 나머지 `department` 테이블에 관련된 정보들은 전부 `null`로 표시한다.

<br/><br/>

## right [outer] join

left [outer] join의 반대라고 생각하면 된다.

![이미지](/programming/img/입문437.PNG)

<br/><br/>

## join을 사용한 예제

### 문제1)

id가 1003인 부서에 속하는 직원 중 리더를 제외한 부서원의 id, 이름, 연봉을 알고 싶다

```sql
select E.id, E.name, E.salary
from employee E join department D on E.dept_id = D.id
where E.dept_id = 1003 and E.id != D.leader_id;
```

<br/>

### 문제2)

id가 2001인 프로젝트에 참여한 직원들의 이름과 직군과 소속 부서 이름을 알고 싶다.

```sql
select E.name AS empl_name,
       E.position AS empl_position,
       D.name AS dept_name
from works_on W join employee E on W.empl_id = E.id
                left join department D on E.dept_id = D.id
where W.proj_id = 2001;
```