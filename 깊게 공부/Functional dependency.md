## Functional dependency 무엇인가요?



한 테이블에 있는 두개의 attribute 집합 사이의 제약을 말한다.

<br/>

## functional dependency

![이미지](/programming/img/입문475.PNG)



`특징:` 두 tuple의 x값이 같다면 y값도 같다는 것이다.

- 왜? → empl_id는 여러 직원들중 한명을 특정하기 위해서, 각 직원들마다 유니크하게 부여된 ID이다.

- 그렇기에, 그 empl_id의 `이름, 생일, 직군, 연봉이 모두 동일`하다는 것이다.

```java
그렇기에 x값에 따라 y값이 유일하게 결정 될때를 
- "x가 y를 함수적으로 결정한다" 라고 말한다.
- "y가 x에 함수적으로 의존한다" 라고 말한다.

이런 두 집합 사이의 제약 관계를 
"functional dependency" 라고 부르고 줄여서 "FD" 라고도 부른다.

기호로는 "X -> Y" 집합 X가 집합 Y를 결정한다.
```

<br/><br/>

### FD 주의 점!

테이블의 스키마를 보고 의미적으로 FD가 존재하는지 파악해야 된다.

- `FD가 성립을 하려면?`

    - 서로 다른 튜플에서 같은 이름의 `empl_name`이 있다면,
        
        그 생일 또한! 같아야 되는데, 생일이 다른 경우일 수도 있기 때문이다
        
        그렇기에, empl_name따라서 → birth_date 생일이 유일하게 결정 된다고 할 수 없다
        
        ![이미지](/programming/img/입문476.PNG)


<br/>

### e.g.

- 직원은 반드시 한 부서에만 속해야 한다면?

    - `X → Y` 성립

- 직원은 하나 이상의 부서에 속할 수 있다면?

    - `X ≠ Y` 성립하지 않는다.

        - `이유는:` 한 명의 직원이 여러개의 부서에 포함 될 수도 있기에,
            
            같은 X에 대해서도 여러 개의 Y가 존재할 수도 있기 때문이다.


<br/>

### e.g. 1)

학생 테이블 있고, 그 안에는 여러가지 attribute들이 있다고 가정하자.

```java
{stu_id} -> {stu_name, birth_data, address}
```

그렇다면 `학생의 id`가 나머지 `attribute`들을 결정 할 것이다.

- `이유는?` 학생의 id라는 것은 학생을 식별하기 위해서 유니크하게 부여된 것이기 때문이다.

    - `같은 id를 가지면 이름, 생일, 주소를 가지게 될 것이다.`

<br/>

### e.g. 2)

- 수업관련 테이블:

    - {class_id} -> {'수업이름', '수업개설일', '몇학기', '몇학점짜리')

- 이런식도 가능하다: (하나 이상일 경우)

    - {stu_id, class_id} -> {'학생이 그 수업에서  받은 성적 A+} 가능하다

- 은행계좌를 저장:

    - {계좌번호, 은행이름} -> {'해당계좌에 얼마가 있는지', '계좌는 언제 개설되었는지'}

        - `bank_account`만 사용하여 해결 할 수 없는 이유는:

            - `"서로 다른 은행에서도 우연히 계좌번호가 같을 수 있기 때문이다."`


<br/><br/>

## Trivial tunctional dependency

X → Y에 유효하고, y가 x의 부분 집합이라면 `Trivial FD`라고 부른다.

- `{a, b, c}` → `{c}` 경우

- `{a, b, c}` → `{a, c}` 경우

- `{a, b, c}` → `{a, b, c}` 경우

<br/><br/>

## Non-trivial tunctional dependency

X → Y에 유효하고, y가 x의 부분 집합이 아니라면 non-trivial FD라고 부른다.

- `{ a, b, c }` → `{b, c, d}` 경우, 즉 하나라도 다른게 있다면 non-trivial 이다

- `{ a, b, c }` → `{y, x}` 같이 하나도 맞는게 없는 경우, non-trivial 이라고도 부르고, completely non-trivial 이라고도 부른다.
    
    - 핵심은 `{a, b, c}` → `{b, c, d}` 같은 경우는 completely non-trivial 이라고 부르지 않는다.

<br/><br/>

## Partial tunctional dependency

주요 개념 proper subset인 것이다.



### e.g.) proper subset 

x의 부분 집합이지만, x와 동일하지는 않은 집합을 말한다. → `“무슨말인가?”`

- e.g.) `X = { a, b, c }` 일때,

    - `{ a, c }, {a}, {}`는 모두 X의 proper subset `이다.`
    
    - `{ a, b, c }`는 X의 proper subset이 `아니다.`

```java
"최소 하나라도 결정할 수 있는 경우를 말한다"

"x의 부분 집합이지만, x와 완전히 동일하지는 않는 집합"
```

<br/>

### 그렇다면 다시 돌아와 Partial FD란?

`{empl_id, empl_name} → {birth_date}` 가 유효하다고 가정하다.

- 현재 `empl_id, empl_name`가 같이 있을 때, `birth_date` (생일)을 결정 짓게 되는 것이다.

- 즉, 서로 다른 2개의 튜플에서 `empl_id`와 `empl_name` 두개의 값이 같다면
    
    `birth_date` 또한 같다는 말이 된다.
    
    - 그런데, `empl_id` 하나만으로도 `birth_date` 를 찾을 수 있다.

    - 즉, 굳이 `empl_name`은 필요가 없다는 것이다.
        - 즉, 하나라도 같은 것이 있다면, 이런 상황을 `“partial FD"` 라고 부른다.

<br/><br/>

## Full tunctional dependency

`모두 할 결우를 Full FD`라고 부른다.

- 하나라도 attribute를 제거 하면, 더 이상 dependency가 유효하지 않는 것을 말한다.

- `Partial FD` 같은 경우는 어떠한 attribute를 제거 하여도 여전히 디펜던시가 유효한 것을 말함.

<br/><br/>

## e.g.

```java
{str_id, class_id} → {성적을 결정 짓는다}
```

- 즉, 학생의 id와 클래스의 id가 같다면, 성적 또한 똑같다고 표현한 것이다.

- `결과는 전부 만족해야지 유효한 것이다.`

    - `“str_id"집합만 있을 경우는?`

        - 학생은 여러 개의 수업을 들을 수 있다.
        - 같은 학생id라도 여러가지 과목을 듣고 있기에 성적이 전부 다를 것이란 것이다

        - 학생id만으로는 유니크하게 성적을 결정 지을 수 없다
    - `"class_id"집합만 있을 경우는?`

        - 한 클래스에서도 누군가는 A를 받고, 누군가를 B를 받고 할 것이다.
            
            즉, 같은 클래스id에 대해서도 각각의 다른 성적을 가질 수 있기 때문에  
            
            `class_id집합`만으로는 결정 지을 수 없게 되는 것이다.