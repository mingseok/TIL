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