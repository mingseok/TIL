## DB 인덱스

조건을 만족하는 튜플들을 빠르게 조회하기 위해서이다.

- 또한, 빠르게 정렬하거나 그룹핑 하기 위해서 이다.

```java
쉽게 말해, index를 사용하는 이유는 
"where절 같은 특정 조건을 만족하는 데이터들을 빠르게 찾기"위해서 이다.
```

<br/>

### 만약, 이미 테이블이 설계 되어 있는 상황이라면?

즉, 인덱스를 생성해 주려고 한다면?

| id | name | team_id | backnumber |
| --- | --- | --- | --- |


<br/>


### 사용중인 테이블에 대한 e.g.

```sql
select * from player where name = 'Sonny';

# 이름이기에 중복을 허용하는 인덱스를 걸어주자.
create index player_name_id ON player (name); 
             -인덱스의 이름-   -테이블- -어튜리뷰트-
```

<br/>

### 사용중인 테이블에 대한 e.g.

```sql
select * from player where team_id 105 and backnumber = 7;

# where절에 있는 조건 두개를 합쳐서 쿼리를 만들어 줘야 한다.
create unique index team_id_backnumber_idx ON player (team_id, backnumber);
```

<br/>

### 처음 테이블을 생성할때 인덱스를 걸어주는 방식.

```sql
create table player (
  id          int           primary key,
  nama        varchar(20)   not null,
  team_id     int,
  backnumber  int,
  index player_name_idx (name),                             #여기
  unique index team_id_backnumber_idx (team_id, backnumber) #여기
);
```

<br/><br/>

## B-tree 기반의 index 동작 방식

a관련 인덱스를 만들어 주게 된다면, `B-tree 기반의 index(a) 가 하나 생성된 것이다.`

- `특징1:` a관련 데이터들은 정렬되어 있다.

- `특징2:` 포인터(ptr)라는 데이터를 가지고 있다
    - 즉, 실제 members 테이블에 있는 튜플을 가리키는 주소값을 가지고 있는 것이다.
- `찾는 방법:` 바이너리 서치 알고리즘을 사용하여 해당 데이터를 찾게 됩니다.

![이미지](/programming/img/입문485.PNG)


<br/><br/>

## create index(a, b)가 될 경우는?

이런 경우는, index(a, b) 라고 되어 있다면 `먼저 작성된 것을 기준으로` 정렬한다.

- 즉, index(a, b) 라고 되어 있다면,  `“a”`를 기준으로 정렬하고,
    
    `“a"중에서 같은 데이터가 있다면 그제서야 "b"를 기준으로 정렬`하게 되는 것이다.
    

![이미지](/programming/img/입문486.PNG)

<br/><br/>

## 참고

그리고, 이진 탐색 트리 방법(BST)을 사용하면서 밑에 그림 처럼 해당 데이터를 찾았다고 

끝나는 것이 아니라, 또 같은 데이터가 있을 수도 있기에 위, 아래로 확인을 더 해야 하는 것이다.

![이미지](/programming/img/입문487.PNG)

<br/><br/>

## 만약, 위 INDEX(a, b)테이블로 where b = 95; 를 찾는다면?

성능이 나오지 않는다.

- `이유는:` index(a, b) 인덱스의 기준은 a였기 때문에 a만 정렬 되어 있기 때문이다.

    - 즉, b는 members 테이블이랑 별 다를게 없다. (오히려 더 나쁠수도 있다)

```java
정리하면,
"사용되는 query에 맞춰서 적절하게 index를 걸어줘야 query가 빠르게 처리될 수 있는 것이다"

실무에서 개발을 할때 쿼리 성능이 너무 나오지 않는다면, 
쿼리에 인덱스가 적절하게 걸려 있는지 확인해보자.
```

<br/>

### 실제로 쿼리문이 어떤 인덱스를 사용하는지 확인하고 싶다면?

```sql
explain #작성
select * 
from player 
where backnumber = 7;
```

<br/>

### index 선택하는 기준은 내가 아니다.

즉, DBMS에 있는 `“optimizer”`가 알아서 적절하게 index를 선택해주는 것이다.

- 하지만, `“optimizer”`의 설정이 이상할 때가 있다. 그럴때는 직접 해줄 수 있다.
    
    ```sql
    select * 
    from player force index (backnumber_idx) 
    where backnumber = 7;
    ```

<br/><br/>

## 정리해보면, index는 많이 만들수록 좋다?

- `table`에 `write` 할 때마다 `index`도 변경이 발생한다.

- 추가적인 저장 공간을 차지한다.

- 불필요한 index를 만들지 말자.