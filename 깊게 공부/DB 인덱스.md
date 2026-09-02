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
<br/>

## 궁금증!

```java
인덱스가 있고 없고가 실제로 얼마나 차이 날까?
```

`10만` 행짜리 테이블을 만들어놓고, 같은 조회를 `1000` 번씩 돌려봤다.

```sql
CREATE TABLE member (id INTEGER PRIMARY KEY, email TEXT, name TEXT, age INTEGER);
-- 10만 행 삽입

SELECT * FROM member WHERE email = 'user1000@test.com';
-- ... 1000번 반복
```

<br/>

### 결과

```sql
인덱스 없이 = 3411 ms
인덱스 있이 =   37 ms
```

<br/>

`92` 배 차이가 난다.

<br/>

## 실행 계획을 보면 이유가 보인다

```sql
-- 인덱스 없이
EXPLAIN QUERY PLAN SELECT * FROM member WHERE email = 'user99999@test.com';
`--SCAN member

-- 인덱스 만든 뒤
CREATE INDEX idx_member_email ON member(email);
`--SEARCH member USING INDEX idx_member_email (email=?)
```

<br/>

`SCAN` 은 처음부터 끝까지 다 읽는다는 뜻이다. `10만` 행을 전부 본다.

`SEARCH` 는 인덱스로 바로 찾아간다는 뜻이다.

<br/>

`10만` 행이면 이진 탐색으로 `17` 번만에 찾는다. `10만` 번과 `17` 번의 차이다.

앞의 자료구조 글에서 본 `이진 탐색` 이 실제로 쓰이는 자리가 여기다.

<br/>

## 그런데 인덱스가 안 쓰이는 경우가 있다

만들어놓고도 `SCAN` 이 나오는 경우들을 찍어봤다.

```sql
-- 앞이 와일드카드
EXPLAIN QUERY PLAN SELECT * FROM member WHERE email LIKE '%@test.com';
`--SCAN member

-- 컬럼에 함수를 씌움
EXPLAIN QUERY PLAN SELECT * FROM member WHERE UPPER(email) = 'USER1@TEST.COM';
`--SCAN member

-- 인덱스가 없는 컬럼
EXPLAIN QUERY PLAN SELECT * FROM member WHERE age = 30;
`--SCAN member
```

<br/>

앞의 두 개가 특히 중요하다. 인덱스는 있는데 못 쓰는 경우다.

<br/>

이유는 인덱스가 `정렬된 순서로 저장되어 있다` 는 성질에 있다.

```java
user1@test.com
user10@test.com
user100@test.com
...
```

<br/>

`user1` 로 시작하는 것을 찾으라면 정렬되어 있으니 바로 찾아간다.

그런데 `@test.com 으로 끝나는 것` 을 찾으라면 순서가 아무 도움이 안 된다. 다 봐야 한다.

<br/>

`UPPER(email)` 도 마찬가지다.

인덱스에는 원본 값이 정렬되어 있지 대문자로 바꾼 값이 정렬되어 있는 게 아니다.

```java
인덱스를 쓰려면 -> 컬럼을 그대로 두고 비교해야 한다
WHERE UPPER(email) = ...   (X)
WHERE email = ...          (O)
```

<br/>

## 앞부분 검색은 쓸 수 있다

```sql
PRAGMA case_sensitive_like=ON;
EXPLAIN QUERY PLAN SELECT * FROM member WHERE email LIKE 'user1%';

`--SEARCH member USING INDEX idx_member_email (email>? AND email<?)
```

<br/>

재밌는 것은 `LIKE 'user1%'` 가 `email > ? AND email < ?` 라는 범위 조건으로 바뀐 것이다.

`user1` 로 시작한다는 것은 결국 정렬된 순서에서 어느 구간에 있다는 뜻이니까 그렇다.

<br/>

## 정렬에도 쓰인다

```sql
EXPLAIN QUERY PLAN SELECT * FROM member ORDER BY email LIMIT 10;
`--SCAN member USING INDEX idx_member_email

EXPLAIN QUERY PLAN SELECT * FROM member ORDER BY name LIMIT 10;
|--SCAN member
`--USE TEMP B-TREE FOR ORDER BY
```

<br/>

`email` 로 정렬하면 인덱스가 이미 정렬되어 있으니 그냥 읽으면 된다.

`name` 은 인덱스가 없어서, 임시로 정렬용 구조를 만들어야 한다.

<br/>

`ORDER BY` 가 느리다면 그 컬럼에 인덱스가 없어서인 경우가 많다.

<br/>

## 공짜가 아니다

인덱스가 차지하는 공간을 재봤다.

```sql
member            3824 KB   (테이블)
idx_member_email  2620 KB   (인덱스)
```

<br/>

컬럼 하나짜리 인덱스가 테이블 크기의 `70%` 다.

인덱스를 서너 개 만들면 인덱스가 테이블보다 커진다.

<br/>

쓰기 비용도 확인해봤다. `1만` 건 삽입이다.

```sql
인덱스 없이 = 43 ms
인덱스 있이 = 51 ms
```

<br/>

한 건을 넣을 때마다 인덱스도 같이 갱신해야 하기 때문이다.

인덱스가 다섯 개면 한 번 `INSERT` 할 때 여섯 군데를 쓴다.

```java
읽기 -> 훨씬 빨라진다 (92배)
쓰기 -> 조금 느려진다 (인덱스 개수만큼 누적)
공간 -> 인덱스마다 추가로 든다
```

<br/>

그래서 `일단 다 걸어두자` 는 안 된다.

조회가 많은 컬럼에만 걸고, 안 쓰이는 인덱스는 지우는 것이 맞다.

<br/>

## 어떤 컬럼에 거는가

```java
WHERE 절에 자주 오는 컬럼      -> 걸 만하다
JOIN 조건에 쓰이는 컬럼        -> 걸 만하다
ORDER BY 에 자주 쓰이는 컬럼   -> 걸 만하다

값의 종류가 적은 컬럼          -> 효과가 적다 (성별, 상태값)
자주 바뀌는 컬럼               -> 쓰기 비용이 크다
```

<br/>

값의 종류가 적으면 왜 효과가 없는지는 생각해보면 당연하다.

`성별` 에 인덱스를 걸어봐야 절반이 걸린다. 절반을 읽을 거면 그냥 다 읽는 것과 비슷하다.

이것을 `카디널리티가 낮다` 고 한다.
