## keys, reference_option

## relation의 특징들은?

### 1. `relation`은 중복된 `tuple`을 가질 수 없다.

![이미지](/programming/img/입문402.PNG)

### 2. relation의 tuple을 식별하기 위해 attribute의 부분 집합을 `key`로 설정한다.

![이미지](/programming/img/입문403.PNG)

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


## primary key : 중복되지 않는 유일한 키

```
PRIMARY KEY (`id`)
```

테이블 전체를 통틀어서 중복되지 않는 값을 지정해야 한다.



- INSERT 될때 중복이 되어선 안된다 라는 뜻이다.

- where 문을 이용해서 데이터를 조회할 때 가장 고속으로 데이터를 가져올 수 있다.

- 테이블마다 딱 하나의 primary key를 가질 수 있다.

- null 값도 절대로 허용이 안된다.


<br/><br/>

## unique key

- `primary key`가 아닌 `alternate key`(대체 키)

- 유니크 키로 지정 되었다면, `중복`된 값을 가질 수 없다
- 단, `NULL`은 `중복`을 허용할 수도 있다.

```sql
// id는 primary key로 설정 되어 있다고 가정.
PLAYER(id, name, team_id, back_number, birth_date)의 unique key는? 
-> {team_id, back_number}
```

<br/>

### 프라이머리 키와의 차이는 null을 허용한다 vs 안한다 의 차이다.

유니크 키 는 null을 허용 한다.

여러개의 unique key를 지정할 수 있다.


<br/><br/>

## foreign key

- 다른 `relation`의 `PK`를 참조하는 `attributes set`이다.

- 즉, `attribute`가 다른 `table`의 `primary key`나 `unique key`를 참조할 때 사용한다.

- 그리고, 다른 table의 프라이머리키가 반드시 존재하는 값이여 한다.
    
![이미지](/programming/img/입문404.PNG)
    

<br/>

```sql
PLAYER(id, name, team_id, back_number, birth_date)와
TEAM(id, name, manager)가 있을 때
foreign key는 PLAYER의 {team_id} 가 된다.

그리하여, "PLAYER의 {team_id}"는 "TEAM의 {id}"를 참조하게 되는 것이다.
```

<br/><br/>

## foreign key reference_option 추가 설명

![이미지](/programming/img/입문405.PNG)

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

![이미지](/programming/img/입문406.PNG)