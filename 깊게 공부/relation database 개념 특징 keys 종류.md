## relation database 개념 / 특징 / keys 종류

<br/>

## relational database란?

- `relational data model`에 기반하여 구조화된 `database`이다.

- `relational database`는 여러 개의 `relations`으로 구성된다.

<br/><br/>

## relational data model이란?

테이블로 표시가 된다.

![이미지](/programming/img/입문421.PNG)

- `domain` : set of atomic values

    - 즉, 값들의 집합 → 포인트는 atomic한 값이다. 더 이상 나누어질 수 없는 값

- `domain name` : `domain` 이름

- `attribute` : `domain`이 `relation`에서 맡은 역할 이름
- `tuple` : 각 `attribute`의 값으로 이루어진 리스트. 일부 값은 `null`일 수도 있다.
- `relation` : set of tuples
    - 즉, 튜플들의 집합인 것이다.
- `relation name` : relation의 이름

<br/><br/>

## relation의 특징들은?

### 1. `relation`은 중복된 `tuple`을 가질 수 없다.

![이미지](/programming/img/입문422.PNG)

<br/>

### 2. relation의 tuple을 식별하기 위해 attribute의 부분 집합을 `key`로 설정한다.

![이미지](/programming/img/입문423.PNG)

3. relation에서 tuple의 순서는 중요하지 않다.

4. 하나의 relation에서 attribute의 이름은 중복되면 안된다.

5. 하나의 tuple에서 attribute의 순서는 중요하지 않다.

6. attribute는 atomic 해야 한다.

```sql
6. 추가 설명
ex1) "서울특별시 강남구 청담동" 이런 데이터가 있다면, 3개로 나눠야 한다는 것이다.
ex2) "컴공, 디자인" 되어 있다면 → 2개로 나눠야 한다는 것이다.
```



<br/><br/>

## 제약조건 keys

- `primary key`
    - 유일하게 식별 할 수 있는 키이다.

    - Null 값을 가질 수 없다.
    - 변경될 수 있는 경우가 없어야 한다.
- `unique key`
    - `primary key`가 아닌 `대체 키`이다.

    - 유니크 키로 지정 되었다면, `중복`된 값을 가질 수 없다
    - 단, `NULL`은 `중복`을 허용할 수도 있다.
    
    ```sql
    // id는 primary key로 설정 되어 있다고 가정.
    PLAYER(id, name, team_id, back_number, birth_date)의 unique key는? 
    -> team_id, back_number
    ```
    
- `foreign key`

    - 다른 `relation`의 `PK`를 참조하는 `attributes set`이다.

    - 즉, `attribute`가 다른 `table`의 `primary key`나 `unique key`를 참조할 때 사용한다.
    - 그리고, 다른 table의 프라이머리키가 반드시 존재하는 값이여 한다.
        
        ![이미지](/programming/img/입문424.PNG)
        
    
    ```sql
    PLAYER(id, name, team_id, back_number, birth_date)와
    TEAM(id, name, manager)가 있을 때
    foreign key는 PLAYER의 team_id 가 된다.
    
    그리하여, "PLAYER의 {team_id}"는 "TEAM의 {id}"를 참조하게 되는 것이다.
    ```
    
- `NOT NULL`

    - `attribute`가 `not null`로 지정되면 해당 `attribute`는 `null`을 값으로 가질 수 없다.
- `DEFAULT '1000'`

    - 뒤에 ‘1000’은 그냥 예시이다.

    - `attribute`의 `default`값 즉, 기본값을 정의할 때 사용한다.

    - 새로운 `tuple`을 저장할 때 해당 `attribute`에 대한 값이 없다면, `default` 값으로 저장된다.
        - 즉, `id`, `name`, `birth_date` 에 대한 값들은 지정했지만, `salary`에 대한 값만 지정하지 않고, 
        
            `default`로 설정했다면, `default`값이 들어가게 되는 것이다.
            
    - `ex)` `menu varchar(15) DEFAULT ‘짜장면’`
- `check`
    - `attribute`의 값을 제한하고 싶을 때 사용한다.
    - `salary`라는 `attribute`에 `CHECK (salary ≥ 5000)` 라고 되어 있으면
        
        `최소` 연봉이 5000이라고 잡은 것이다. 즉, 5000 밑으로는 `저장되지 않는다.`
        
    - `ex)` `CHECK (salary ≥ 5000)`

<br/><br/>

## `foreign key` reference_option 추가 설명

![이미지](/programming/img/입문425.PNG)

<br/>

### 빨간색 부분을 설명하자면

- `FORELGN KEY (dept_id)`

    - 현재 employee의 포링키를 `dept_id`로 지정 했다

- `references DEPARTMENT(id)`
    - `dept_id`가 참조하는 것은 `DEPARTMENT(id)`의 id를 참조한다.
- `on delete SET NULL`
    - 참조하는 값이 삭제가 될 경우에는 `dept_id` 를 NULL로 변경해준다.
- `on update CASCADE`

    - 만약, 위 과정이 아니라, 참조하는 값이 업데이트가 된다면,
        
        업데이트 되는 값으로 변경해 준다고 선언한 것이다.
        
    - `CASCADE`에 관련 추가 내용들은 밑에 참고하기!

<br/>

![이미지](/programming/img/입문426.PNG)



<br/><br/>

## null의 의미

- 값이 존재하지 않는다

- 값이 존재하나 아직 그 값이 무엇인지 알지 못한다
- 해당 사항과 관련이 없다



<br/><br/>

## constraints 뜻?

relational database의 relations들이 언제나 항상 지켜줘야 하는 제약 사항을 말한다.

- `attribute`가 `NOT NULL`로 명시됐다면 `NULL`을 값으로 가질 수 없다.

- `primary key`는 `value`에 `NULL`을 가질 수 없다.
- FK와 PK는 도메인이 같아야 하고, PK에 없는 values를 FK가 값으로 가질 수 없다.
    - 즉, `TEAM`의 `{id = 111}` 이면, `PLAYER` FK값은 `{team_id = 111}` 로
        
        되어 있어야 한다는 것이다. `PLAYER` FK값을 의미로 `{team_id = 222}`하면 안된다.
        

```