## recoverable, unrecoverable

<br/>

![이미지](/programming/img/입문456.PNG)

이런 상황인 것이다.

H는 마지막에 롤백 하여 다시 200만원으로 돌렸다.

<br/>

하지만 K는 H가 작업한 뒤에 입금을 하여서, 230만원부터 시작하게 된 것이다.

그러면, K도 같이 롤백 되어야 하는데, `durability` 속성은 한번 `commit`된 
 트랜잭션은 `rollback` 될 수 없다 라는 `속성`이 있다

<br/><br/>

## 이런 상황을 `unrecoverable schedule` 이라고 부른다.

스케줄 내에서 `commit` 된 트랜잭션이! `rollback` 되어버린 다른 트랜잭션의 `write`한

부분의 데이터(230만원)를 읽은 경우를 `unrecoverable schedule` 라고 부른다.

```java
rollback을 해도 이전 상태로 회복 불가능할 수 있기 때문에 이런 schedule은 DBMS가 허용하면 안된다.
```

<br/><br/>

## 그러면 `recoverable schedule`은 무엇인가?

![이미지](/programming/img/입문457.PNG)

1. 스케줄 내에서 그 어떠한 트랜잭션도 자신이 읽은! 데이터의 트랜잭션을 계속 기다린다.

2. 읽음 당했던, 즉 write한 트랜잭션(주체)이 먼저 commit / rollback 전까지는 
    
    그 어떠한 읽었던 트랜잭션들은 commit 하지 않는 경우를 말한다.
    

```java
rollback 할 때, 이전 상태로 온전히 돌아갈 수 있기 때문에 DBMS는 이런 schedule만 허용해야 한다는 것입니다.
```

<br/><br/>

## cascading rollback이란?

하나의 트랜잭션이 `rollback`을 하면 의존성 있는 다른 트랜잭션도 연쇄적으로 `rollback` 하는 것을 `cascading rollback`이라고 합니다.

- 처리하는 비용이 많이 든다.

<br/>

### 비용 해결의 방법은 없을까? → `cascadeless schedule`

데이터를 `write`한 트랜잭션이 commit / rollback 한 뒤, 데이터를 읽는 스케줄만 허용하는 것입니다.

![이미지](/programming/img/입문458.PNG)

<br/><br/>

## 가장 엄격한 스케줄인 → strict schedule

스케줄 내에서 어떤 트랜잭션도 `commit 되지 않은` 트랜잭션들에 대해서는

`write`한 데이터가 있더라도, `쓰지도 읽지도 않는 경우`를 말합니다.

![이미지](/programming/img/입문459.PNG)

rollback 할때 recovery가 쉽다

트랜잭션 이전 상태로 돌려 놓기만 하면 되기 때문이다.

<br/><br/>

## 최종 정리

- `unrecoverable schedule`

    - rollback 이 발생 했을때, 회복 불가능 할 수 있기 때문에 DBMS에서 허용을 하면 안된다.

- `recoverable schedule`

    - rollback 시에 이전 상태로 회복이 가능하므로, 이런 스케줄은 DBMS에서 허용 한다.
    - `recoverable schedule` 내에서 `cascading rollback`이 발생하지 않도록 하는 스케줄은?

        - `cascadeless schedule` 이라고 부른다.

            - `cascadeless schedule` 중에서도 좀 더 엄격한 것은?
                
                즉, commit되지 않은 트랜잭션들이 `write`한 데이터는 쓰지도 않고, 읽지도 않는 스케줄은?
                
                - `strict schedule 이라고 부른다.`

<br/>

### 그리고

어떤 스케줄이 있는데, `a 트랜잭션`이 write한 데이터를 다른 `b 트랜잭션`이 read 하였다면?

<br/>

그 write한 `a 트랜잭션`이 commit 할 때까지는, `read`한 `b 트랜잭션`도 `commit` 하지 않은 상황의 스케줄을? → 

`recoverable` 스케줄이라고 부른다.
혹은, `recoverability` 한 속성을 가진다고 표현한다.

<br/><br/>

## 정리

`concurrency control`은 `serializable` 와 `recoverability` 를 제공합니다.

이와 관련된 트랜잭션 속성이 `“Isolation”` 이다.
<br/>

## 궁금증!

```java
회복 가능성이라는 게 실무에서 어떻게 지켜질까?
```

앞의 read-lock 글에서 본 `Strict 2PL` 이 그 답이다.

<br/>

문제 상황을 다시 정리하면 이렇다.

```java
A: x 를 100 -> 200 으로 바꾼다 (커밋 안 함)
B: x 를 읽는다 -> 200
B: 커밋한다
A: 롤백한다                      <- x 는 100 으로 돌아간다
```

<br/>

`B` 는 `200` 을 보고 커밋했는데, 그 `200` 은 세상에 존재한 적 없는 값이 됐다.

`B` 를 되돌리려 해도 이미 커밋됐으니 방법이 없다. 이것이 `unrecoverable` 이다.

<br/>

## 막는 방법은 두 가지다

### 첫번째) 커밋할 때까지 락을 안 푼다

`Strict 2PL` 이다. `A` 가 커밋할 때까지 `x` 에 락이 걸려 있으니 `B` 가 읽을 수가 없다.

<br/>

앞의 DB 락 글에서 실제로 확인한 그 동작이다.

```sql
A: BEGIN IMMEDIATE; UPDATE t SET v = 200;   -- 락 획득
B: UPDATE t SET v = 300;
   -> Error: database is locked
```

<br/>

### 두번째) 커밋된 값만 보여준다

MVCC 방식이다. `B` 를 막는 대신 `옛 값을 보여준다`.

```sql
B: SELECT v FROM t;
   -> 100     (A 가 바꾼 200 이 아니라)
```

<br/>

`B` 는 애초에 `200` 을 본 적이 없으니, `A` 가 롤백해도 아무 문제가 없다.

<br/>

앞의 MVCC 글에서 본 대로 이쪽이 훨씬 빠르다. `B` 가 안 기다려도 되기 때문이다.

```java
Strict 2PL - B 를 기다리게 해서 못 읽게 한다
MVCC       - B 에게 커밋된 옛 값을 보여준다
```

<br/>

## 격리 수준으로 다시 보면

`READ COMMITTED` 이상이면 회복 가능성이 보장된다.

```java
READ UNCOMMITTED - 커밋 안 된 값을 읽는다 -> unrecoverable 이 생길 수 있다
READ COMMITTED   - 커밋된 값만 읽는다     -> recoverable 이 보장된다
```

<br/>

그래서 실무에서 `READ UNCOMMITTED` 를 쓰는 경우가 거의 없다.

앞의 isolation level 글에서 본 표의 맨 윗줄이 사실상 쓰이지 않는 이유가 이것이다.

<br/>

## 연쇄 롤백

`recoverable` 이어도 문제가 하나 남는다.

```java
A: x 를 바꾼다
B: x 를 읽는다 (A 가 아직 커밋 안 했지만 락을 잠깐 풀었다고 하자)
A: 롤백
-> B 도 같이 롤백해야 한다
-> B 를 읽은 C 도 롤백해야 한다
-> 도미노처럼 번진다
```

<br/>

`recoverable` 은 `되돌릴 수는 있다` 는 뜻이지 `되돌릴 일이 없다` 는 뜻이 아니다.

<br/>

`Strict 2PL` 은 이것도 막는다. 커밋 전에는 아무도 못 읽으니 연쇄가 시작될 수 없다.

이 성질을 `cascadeless` 라고 한다.

```java
recoverable  - 되돌릴 수는 있다
cascadeless  - 되돌릴 일이 남에게 번지지 않는다
strict       - 읽기도 쓰기도 커밋 전에는 손댈 수 없다
```

<br/>

아래로 갈수록 조건이 세다. 실무 DB는 대개 `strict` 를 만족한다.

<br/>

## 그런데 이건 DB 안에서만 지켜진다

애플리케이션이 밖으로 나가면 되돌릴 방법이 없다.

```java
@Transactional
public void order() {
    orderRepository.save(order);
    mailSender.send("주문 완료");     // 여기서 메일이 나갔다
    throw new RuntimeException();     // 롤백된다
}
```

<br/>

주문은 롤백되는데 메일은 이미 나갔다. 메일에는 롤백이 없다.

<br/>

앞의 `@EventListener` 글에서 본 `AFTER_COMMIT` 이 이 문제를 다룬다.

```java
@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
void sendMail(OrderCreated event) { ... }
```

<br/>

커밋이 확정된 뒤에만 내보내니, 롤백된 주문의 메일이 나갈 일이 없다.

`회복 가능성` 을 DB 밖까지 확장한 셈이다.
