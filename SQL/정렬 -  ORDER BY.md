## group by, aggregate function order by

<br/>

## order by

조회 결과를 특정 attribute 기준으로 정렬하여 가져오고 싶을때 사용한다.

- default 정렬 방식은 오름차순이다.

    - 키워드를 사용하지 않으면 default이다.

- 오름차순 정렬은 ASC로 표기 된다.

- 내림차순 정렬은 DESC로 표기 된다.

<br/>

### order by 문제)

직원들의 정보를 순서대로 정렬해서 알고 싶다면,

```sql
select *
from employee
order by salary;
```

<br/>

### 추가로 정렬은 하나만 되는 것이 아니다.

이런식으로 여러개도 가능하다.

- 추가로 알아야 될 것은, `null`이 있다면 `null`끼리 묶어서 출력시킨다는 것이다.

```sql
select *
from employee
order by dept_id asc, salary desc;
```

<br/><br/>

## aggregate function

여러 tuple들의 정보를 요약해서 하나의 값으로 추출하는 함수

- 대표적으로 COUNT, SUM, MAX, MIN, AVG 함수가 있다.

- (주로) 관심있는 attribute에 사용된다.

    - e.g.) AVG(salary), MAX(birth_date)

- NULL 값들은 제외하고 요약 값을 추출한다.

<br/>

### aggregate function 문제1)

임직원 수를 알고 싶다면?

```sql
select COUNT(*)
from employee;
```

COUNT 같은 경우는 중복도 허용한다.

- 만약, dept_id로 쿼리를 작성하였다면?

    - `COUNT(dept_id)` / `COUNT(*)` 했을때랑 값이 다르게 나온다.

    - 이유는 `null`은 포함 시키지 않기 때문이다.

<br/>

### aggregate function 문제2)

프로젝트 2002에 참여한 직원 수와 최대 연봉과 최소 연봉과 최소 연봉과 평균 연봉을 알고 싶다면?

```sql
select COUNT(*), MAX(salary), MIN(salary), AVG(salary)
from works_on W join employee E on W.empl_id = E.id
where W.proj_id = 2002;
```

<br/>

### 출력값 확인

![이미지](/programming/img/입문439.PNG)

<br/><br/>

# group by

관심있는 attribute 기준으로 그룹을 나눠서 

그룹별로 aggregate function을 적용하고 싶을때 사용합니다.

- 그룹을 나누는 기준이 되는 attribute를 → `grouping attribute`라고 부른다.

<br/>

### group by 문제)

각 프로젝트에 참여한 임직원 수와 최대 연봉과 최소 연봉과 평균 연봉을 알고 싶다.

```sql
select W.proj_id, COUNT(*), MAX(salary), MIN(salary), AVG(salary)
from works_on W join employee E on W.empl_id = E.id
group by W.proj_id;
```

proj_id 가지고 그룹핑을 하는 것이다.



그렇게, 그룹핑을 한 `그룹 별로` 각각의 통계를 출력시키는 것이다.

<br/>

### 출력값 확인

select절 맨 앞에 `W.proj_id` 적어주는 이유는?

- 출력된 통계들이 어떤 프로젝트에 대한 것인지 알아 볼 수 있게 하기 위해서이다.

- `W.proj_id`를 `grouping attribute`라고 부른다.

![이미지](/programming/img/입문440.PNG)

<br/><br/>

# HAVING

having은 group by와 함께 사용된다.

aggregate function의 `결과값을 바탕`으로 → 그룹을 필터링하고 싶을때 사용한다.

having절에 명시된 조건을 만족하는 그룹만 결과에 포함된다.

<br/>

### HAVING 문제)

프로젝트 참여 인원이 7명 이상인 프로젝트들에 대해서 

각 프로젝트에 참여한 직원 수와 최대 연봉과 최소 연봉과 평균 연봉을 알고 싶다.

```sql
select W.proj_id, COUNT(*), MAX(salary), MIN(salary), AVG(salary)
from works_on W join employee E on W.empl_id = E.id
group by W.proj_id;
having count(*) >= 7;
```

어떠한 결과가 나왔는데, 추가적으로 조건 or 필터링을 걸고 싶을때 사용하는 것이 having절이다.

<br/>

### 출력값 확

![이미지](/programming/img/입문441.PNG)

<br/><br/>

## 여러가지 예제)

### 문제 1)

각 부서별 인원수를 인원 수가 많은 순서대로 정렬해서 알고 싶다면

```sql
select dept_id, COUNT(*) as empl_count
from employee
group by dept_id
order by empl_count desc;
```

<br/>

### 출력값 확인

![이미지](/programming/img/입문442.PNG)

<br/>

### 문제 2)

각 부서별 - 성별 인원수를 인원 수가 많은 순서대로 정렬해서 알고 싶다

```sql
select dept_id, sex, COUNT(*) as empl_count
from employee
group by dept_id, sex
order by empl_count desc;
```

<br/>

### 출력값 확인

![이미지](/programming/img/입문443.PNG)

<br/>

### 문제 3)

회사 전체 평균 연봉보다 평균 연봉이 적은 부서들의 평균 연봉을 알고 싶다

```sql
select dept_id, AVG(salary)
from employee
group by dept_id
having AVG(salary) < (
                       select AVG(salary) 
                       from employee
                     );
```