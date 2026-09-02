## transaction isolation level

<br/>

### 트랜잭션의 이상한 현상들은 이렇다.

- `Dirty read` : commit 되지 않은 변화를 읽음

- `Non-repeatable read` : 같은 데이터를 두번 읽었는데, 값이 달라짐
    - `Fuzzy read` 라고 불리기도 한다.
- `Phantom read` : 없던 데이터가 생겼다.

<br/>

이런 현상들은 발생하지 않는 것이 좋은 것이다.

즉, 이상한 결과, 데이터 불일치를 발생시키는 현상이기 때문이다.

```java
하지만, 위 현상들이 없게 할수는 있지만,
동시 처리 가능한 트랜잭션 수가 줄어들어, 결국 DB의 성능이 떨어지게 되는 것이다.
```

<br/><br/>

## 그리하여 나온것이 isolation level 인것이다.

일부 이상한 현상은 허용하는 `level`을 만들어, 사용자가 필요에 따라 적절하게 선택하도록 하는 것이다.

단계가 높아질수록 느려진다고 생각하면 되는 것이다.

| Isolation level | Dirty read | Non-repeatable read | Phantom read |
| --- | --- | --- | --- |
| Read uncommitted | 허용 | 허용 | 허용 |
| Read committed | 불가 | 허용 | 허용 |
| Repeatable read | 불가 | 불가 | 허용 |
| Serializable | 불가 | 불가 | 불가 |

<br/>

- `Read uncommitted` : 모두 허용한다.

    - 좋게 말하면 가장 자유로운 것이고,
    - 나쁘게 말하면 이상한 현상이 가장 취약한 곳

    - 동시성은 높아서 처리량은 가장 높다.

- `Read committed` : 커밋된 데이터만 읽는다.

- `Repeatable read` : `Read committed` 의 레벨에서 허용하지 않는 것은 같이 허용하지 않는다
    - 추가로 `Non-repeatable read`도 허용하지 않는다.

- `Serializable` : 이상한 현상들을 모두 허용하지 않는다.
    
    - 위 세가지 현상 뿐만 아니라, `이상한 현상 자체가 발생하지 않는 level`을 의미한다.

<br/><br/>

## 정리

3가지 이상 현상에 따라, 각각의 isolation level이 구분 지었다.

그렇기에, 개발자는 isolation level을 통해 전체 처리량과 

데이터 일관성 사이에서 어느 정도 트레이드 오프 해야 하는 것이다.

<br/><br/>

## SNAPSHOT ISOLATION

기존의 level들 하고 약간 다르다.

이상한 현상들을 정의한 뒤, 얼마만큼 허용하는지에 대해 level을 부여 했다.

<br/>

### SNAPSHOT ISOLATION은 

isolation 구현 방식에 기반하여 level이 정의 된 것이다.

- 트랜잭션 시작전에 commit된 데이터만 보입니다.

- first-committer win → 즉, 처음 commit한 트랜잭션이 승리자가 된다.

    - 그 뒤에, 다른 트랜잭션이 commit하려고 하면 실패한다.
<br/>

## 궁금증!

```java
격리 수준을 낮추면 정확히 무엇을 포기하는 걸까?
```

수준마다 `허용하는 이상 현상` 이 다르다. 그 표를 먼저 정리하면 이렇다.

```java
                    Dirty Read   Non-repeatable Read   Phantom Read
READ UNCOMMITTED       O                O                  O
READ COMMITTED         X                O                  O
REPEATABLE READ        X                X                  O (*)
SERIALIZABLE           X                X                  X

O = 일어날 수 있다 / X = 막아준다
```

<br/>

`(*)` 표시를 한 이유는, MySQL InnoDB는 `REPEATABLE READ` 에서 팬텀 리드도 대부분 막기 때문이다.

표준 정의와 실제 구현이 다른 대표적인 경우다.

<br/>

## 아래로 내려갈수록 안전하지만 느리다

이유는 간단하다. 안전하게 만들려면 더 오래 잠가야 하기 때문이다.

```java
READ COMMITTED   - 읽을 때 잠깐 잠그고 바로 푼다
REPEATABLE READ  - 읽은 것을 트랜잭션이 끝날 때까지 잠근다
SERIALIZABLE     - 읽을 범위 전체를 잠근다. 새 행이 끼어들지도 못한다
```

<br/>

오래 잠글수록 다른 트랜잭션이 기다리는 시간이 길어진다.

앞의 DB 락 글에서 본 대로, 기다리는 요청이 쌓이면 서버 전체가 느려진다.

<br/>

## READ COMMITTED 를 직접 확인해보면

앞의 DB 락 글에서 돌려본 실험이 정확히 이 수준의 동작이다.

```sql
-- A 세션
BEGIN IMMEDIATE;
UPDATE t SET v = 200 WHERE id = 1;   -- 아직 커밋 안 함

-- B 세션이 조회
SELECT v FROM t;
```

```sql
B: 읽기 결과 -> 100
```

<br/>

`A` 가 바꾼 `200` 이 아니라 커밋된 마지막 값인 `100` 이 보인다.

이것이 `Dirty Read 를 막았다` 는 뜻이다.

<br/>

만약 `READ UNCOMMITTED` 였다면 `200` 이 보였을 것이다.

그리고 `A` 가 롤백하면, `B` 는 세상에 존재한 적 없는 값을 읽은 셈이 된다.

<br/>

## 기본값이 다르다

```java
MySQL (InnoDB)  -> REPEATABLE READ
PostgreSQL      -> READ COMMITTED
Oracle          -> READ COMMITTED
SQL Server      -> READ COMMITTED
```

<br/>

MySQL만 유독 한 단계 높다.

복제(replication)를 안전하게 하려던 역사적인 이유 때문인데, 지금도 그대로 남아 있다.

<br/>

이 차이를 모르면 DB를 옮길 때 사고가 난다.

MySQL에서는 트랜잭션 안에서 두 번 조회하면 같은 값이 나오는데,

PostgreSQL로 옮기면 그 사이에 남이 커밋한 값이 보인다.

<br/>

## 스프링에서 지정하는 법

```java
@Transactional(isolation = Isolation.REPEATABLE_READ)
public void order() { ... }
```

<br/>

기본값은 `Isolation.DEFAULT` 인데, `DB 가 정한 것을 그대로 쓴다` 는 뜻이다.

앞의 PSA 글에서 본 대로 스프링은 DB 기술에 관여하지 않으려 하기 때문이다.

<br/>

실무에서는 이 옵션을 거의 안 건드린다.

격리 수준을 올리는 대신, 필요한 곳에만 락을 명시적으로 거는 편이 낫기 때문이다.

```java
// 격리 수준을 올린다 -> 트랜잭션 전체가 느려진다
@Transactional(isolation = Isolation.SERIALIZABLE)

// 필요한 조회에만 락을 건다 -> 그 한 줄만 느려진다
SELECT * FROM item WHERE id = 1 FOR UPDATE;
```

<br/>

## SNAPSHOT ISOLATION 이 표에 없는 이유

원문 마지막에 나온 그것은 표준 네 단계에 속하지 않는다.

락으로 막는 방식이 아니라, `트랜잭션 시작 시점의 사진을 찍어두고 그것만 보는` 방식이기 때문이다.

<br/>

읽는 쪽이 아무것도 안 잠그니 쓰는 쪽과 부딪히지 않는다.

앞의 실험에서 `B` 의 읽기가 안 막힌 것도 이 원리다.

<br/>

이 방식의 구현이 `MVCC` 다. 다음 글에서 그것을 다룬다.
