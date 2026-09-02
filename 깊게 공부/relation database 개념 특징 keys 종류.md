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
        


<br/>

## 궁금증!

```java
super key, candidate key, primary key 를 실제 테이블로 구분해보면
```

회원 테이블을 예로 들면 이렇다.

```sql
CREATE TABLE member (
    id         BIGINT,        -- 자동 증가
    email      VARCHAR(100),  -- 중복 불가
    phone      VARCHAR(20),   -- 중복 불가
    name       VARCHAR(50),
    birth_date DATE
);
```

<br/>

```java
super key     - 행을 유일하게 구분할 수 있는 모든 조합
                {id}, {email}, {phone}, {id, name}, {email, phone, name}, ...

candidate key - super key 중에서 더 줄일 수 없는 것들
                {id}, {email}, {phone}
                ({id, name} 은 name 을 빼도 되니 candidate 가 아니다)

primary key   - candidate key 중에 하나를 고른 것
                {id}

alternate key - 고르지 않은 나머지 candidate key
                {email}, {phone}
```

<br/>

`super key` 가 제일 넓고, `candidate` 가 그 부분집합, `primary` 는 그중 하나다.

<br/>

## 왜 id 를 고르는가

`email` 도 유일하니 기본 키로 쓸 수 있는데 잘 안 쓴다. 이유가 몇 가지 있다.

```java
바뀔 수 있다   - 이메일은 사용자가 바꾼다. 기본 키가 바뀌면 참조하던 모든 곳을 고쳐야 한다
길다          - VARCHAR(100) 을 외래 키로 들고 다니면 인덱스가 커진다
개인정보다     - 다른 테이블에 외래 키로 흩어지면 지우기 어려워진다
```

<br/>

앞의 B tree 글에서 본 것과 이어진다.

기본 키는 모든 보조 인덱스에 같이 들어가기 때문에, 짧을수록 인덱스가 작아진다.

```java
id BIGINT      -> 8 바이트
email VARCHAR  -> 최대 100 바이트
```

<br/>

인덱스가 다섯 개면 그 차이가 다섯 배로 불어난다.

<br/>

그래서 `의미 없는 값을 기본 키로 쓰는` 방식이 자리 잡았다.

이것을 `대리 키(surrogate key)` 라고 하고, 반대로 `email` 처럼 의미가 있는 것을 `자연 키` 라고 한다.

<br/>

## 그래도 유일성은 지켜야 한다

`email` 을 기본 키로 안 쓴다고 중복을 허용해도 된다는 뜻은 아니다.

```sql
CREATE UNIQUE INDEX uk_member_email ON member(email);
```

<br/>

`alternate key` 에는 `UNIQUE` 제약을 걸어둔다.

이렇게 해야 `candidate key` 라는 사실이 DB에 실제로 표현된다.

<br/>

애플리케이션에서만 검사하면 뚫린다.

```java
if (memberRepository.existsByEmail(email)) {    // 여기서 없다고 나왔는데
    throw new DuplicateException();
}
memberRepository.save(member);                   // 그 사이에 남이 먼저 넣을 수 있다
```

<br/>

앞의 Isolation 글에서 본 `Lost Update` 와 같은 구조의 문제다.

확인과 실행 사이에 틈이 있으면 동시에 들어온 요청이 둘 다 통과한다.

<br/>

`UNIQUE` 제약이 있으면 DB가 두 번째를 거부한다.

```java
DuplicateKeyException
```

<br/>

앞의 PSA 글에서 본 대로 스프링이 이걸 기술 무관 예외로 바꿔 던져준다.

이걸 잡아서 처리하는 것이 확실한 방법이다.

<br/>

## 무결성 제약이 지켜주는 것

원문 마지막의 세 가지를 정리하면 이렇다.

```java
도메인 무결성  - 값의 타입과 범위. NOT NULL, CHECK
개체 무결성    - 기본 키는 NULL 일 수 없고 중복될 수 없다
참조 무결성    - 외래 키는 참조하는 곳에 실제로 있는 값이어야 한다
```

<br/>

`참조 무결성` 이 실무에서 논쟁거리다. 외래 키를 걸 것인가 말 것인가.

```java
외래 키를 건다   - DB 가 정합성을 보장한다. 대신 쓰기마다 확인 비용이 들고 락이 늘어난다
외래 키를 안 건다 - 빠르고 유연하다. 대신 참조가 깨진 데이터가 생길 수 있다
```

<br/>

대규모 서비스에서 외래 키를 안 거는 경우가 꽤 있다.

앞의 파티셔닝이나 샤딩을 하면 외래 키를 쓸 수 없기도 하다.

<br/>

다만 안 걸면 그 검증을 애플리케이션이 대신 해야 한다.

앞의 NoSQL 글에서 본 `스키마가 없어진 게 아니라 코드로 옮겨간 것` 과 같은 얘기다.
