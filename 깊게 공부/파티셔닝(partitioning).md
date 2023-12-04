## 파티셔닝(partitioning)

database table을 더 작은 table들로 나누는 개념 입니다.

<br/>

## partitioning 2가지 종류

table을 목적에 따라 작은 table들로 나누는 방식이다.

- `vertical partitioning`

    - column을 기준으로 table을 나누는 방식

- `horizontal partitioning`

    - row를 기준으로 table을 나누는 방식

<br/><br/>

## vertical partitioning 이란?

![이미지](/programming/img/입문520.PNG)

테이블 정규화 하는 작업도 여기에 포함 될 수 있다.

- 이미 정규화가 되어 있는 테이블이라도 vertical partitioning 진행 할 수 있다는 것이다.

- 민감한 정보에는 제한을 더 걸어서 함부러 접근을 하지 못하게 하기 위함도 있다.

- 자주 사용되는 attribute 를 모아서 하나의 파티션으로 만들수도 있다.

    - 즉, 여러 목적으로 vertical partitioning 사용 될 수 있다.

<br/><br/>

## horizontal partitioning을 사용하기 전에는 이랬다.

- 모든 partition들은 같은 DB 서버에 저장된다.

- 테이블의 크기가 커질수록 인덱스의 크기도 커진다.

- 테이블에 `읽기 / 쓰기`가 있을 때마다 인덱스에서 처리되는 `시간도 조금씩 늘어난다.`

```jsx
"이런 순간들이 생긴다면 horizontal partitioning 사용하면 되는 것이다."
```

<br/><br/>

## hash-based (=해시 기반) horizontal partitioning

![이미지](/programming/img/입문521.PNG)

위 그림 처럼, `user_id`를 기준으로 partitioning 했다.

- 그리고 해시 `function에 넣으면 user_id를 넣으면 0 또는 1이` 나오는 것이다.

- 만약, 0이 나왔다면 오른쪽에 있는 0 테이블로 가게 되는 것이다.

    - 여기서 주목할 점은, `오른쪽 테이블들은 왼쪽에 있는 테이블의 컬럼들과 동일하다는 것이다.`

<br/>

가장 많이 사용될 패턴에 따라 partition key를 정하는 것이 중요하다.

- partition key 란? → 컬럼을 말한다.

```jsx
"데이터가 균등하게 분배 될 수 있도록 hash function을 잘 정의 하는 것도 중요하다."
```

또한, hash-based horizontal partitioning 같은 경우는 

한번 파티션이 나눠져서 사용되면 이후에 partitioning을 추가하기 까다롭다.

<br/><br/>

### 이렇게 테이블을 나누게 된다면,

`user_id` 기준을 해시 펑션에 적용을 하여 구분을 하였기 때문에, 결국 같은 유저는 같은 테이블에 저장 되게 되는 것이다.

![이미지](/programming/img/입문522.PNG)

<br/><br/>

## sharding 이란?

![이미지](/programming/img/입문523.PNG)

- horizontal partitioning으로 나누어진 table들을 각각의 DB 서버에 저장하는 방식이다.

- horizontal partitioning 처럼 동작한다.

- 각 partition이 독립된 DB 서버에 저장된다.

```jsx
"이렇게 나눈 이유는?" -> DB부하를 분산 시키는 목적이 있다.
```

<br/><br/>

## replication 이란?

DB를 복제해서 여러 대의 DB 서버에 저장하는 방식이다.

밑에 그림 처럼 애플리케이션이 서버1 에게 데이터를 읽고, 쓰고 하는 도중에 

서버1이 문제가 발생한다면 서버2 에다가 데이터를 읽고, 쓰도록 처리 할 수 있다.

![이미지](/programming/img/입문524.PNG)

<br/>

### 또한.

서버1에서 read 요청 많아 진다면, 서버2에게 요청을 부탁 할 수도 있다 

- 즉, 서버1 입장에서 내가 할 일을 덜어 줄 수 있다는 것이다.

    - 이렇게 한다면 (DB 부하를 줄일 수 있다)

![이미지](/programming/img/입문525.PNG)