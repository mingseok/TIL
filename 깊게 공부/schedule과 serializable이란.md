## schedule과 serializable이란?

<br/>

## Schedule 이란?

여러 트랜잭션들이 동시에 실행될 때, 각 트랜잭션에 속한 작업들의 실행 순서이다.

그리고 각 트랜잭션 내의 작업 순서는 바뀌지 않는다

- `Serial schedule(순차적)`

    - 트랜잭션들이 겹치지 않고, 한번에 하나씩 실행되는 스케줄 말한다.

- `Nonserial schedule`

    - 트랜잭션들이 겹쳐서 실행되는 스케줄을 말한다.

<br/><br/>

## Serial schedule 성능은?

`한번에 하나의 트랜잭션만 실행`하는 것이다.

그리하여, Serial schedule은 결과적으로 이상한 데이터를 만들 가능성은 없다.

- 반면에, 한번에 하나의 트랜잭션만 실행되기 때문에
    
    좋은 성능을 낼 수 없고, `현실적으로 사용할 수 없는 방식이다.`
    

<br/><br/>

## Nonserial schedule 성능은?

`트랜잭션들이 겹쳐서 실행`되기 때문에 `동시성이 높아`져서 같은 시간 동안 더 많은 트랜잭션들을 처리 할 수 있다.

- Serial schedule 보다 더 빨리 끝난다는 것이 포인트이다.

```java
"Nonserial schedule의 단점"
트랜잭션들이 어떤 형태로 겹쳐서 실행되는지에 따라 "이상한 결과"가 나올 수 있다.
```

<br/><br/>

## Conflict operations이란 ?

conflict operation은 `순서가 바뀌면 결과도 바뀌는 것`을 뜻한다.

하지만, `3가지 조건을 모두 만족`해야 conflict 하다고 할 수 있다.

1. 서로 다른 트랜잭션이여야 한다.

2. 같은 데이터에 접근해야 한다

3. 최소 하나는 쓰기(write) 오퍼레이션이야 한다.

<br/><br/>

## Conflict equivalent이란?

아래 두 조건을 `모두 만족`하면 Conflict equivalent하다고 부른다

1. 두 스케줄은 같은 트랜잭션들을 가진다

2. 어떤 Conflicting 오퍼레이션의 순서도 양쪽 스케줄 모두 동일하다

    - 즉, `a 스케줄`에는 Conflicting 오퍼레이션이 `3개`라면
        
        `b 스케줄` Conflicting 오퍼레이션도 `3개`여야 한다는 뜻이 된다.
        

<br/><br/>

## 중요

만약, `a 스케줄`이 Serial 스케줄이고, `b 스케줄`이 Nonserial 스케줄이라면?

a 와 b는 최종적으로 Conflict equivalent 하다고 말할 수 있는 것이다

<br/>

즉, Serial 스케줄과 Conflict equivalent 하는 b는 `Conflict serializable` 하다고 부르는 것이다.

결론은 → Nonserial 스케줄 이였음에도 불구하고 `정상적인 동작을 낼 수 있는 것이다.`

<br/><br/>

## Conflict serializable이란?

serial schedule과 `conflict equivalent`한 상황을 Conflict serializable 이라고 한다

<br/>

### 고민은 뭐였지?

- 성능 때문에 여러 트랜잭션들을 겹쳐서 실행할 수 있으면 좋겠다는 생각

    - 하지만, nonserial schedule은 이상한 결과가 나온다는 것

```java
그리하여, 해결책은 "conflict serializable"한 "nonserial schedule"을 허용하자는 것이다.
```

<br/><br/>

## 해결 방법

여러 트랜잭션을 동시에 실행해도, schedule이 conflict serializable 할 수 있도록 `프로토콜`을 적용하는 방법이다.

- 즉, `실행하고 나서` conflict serializable한지 확인하는 것이 아니라,
    
    `실행 전에` conflict serializable한 `스케줄만 실행 될 수 있도록` 하는 `프로토콜`을 적용한다는 것이다.


<br/><br/>

## 최종 정리

어떤 `a schedule`이 있다. 그리고 `b schedule`하고 동일하다면, 

즉! `equivalent`하다면 a schedule은 `serializable`하다고 말한다

<br/>

그리고 `equivalent`하다는 개념은 두 가지 조건으로 동일하다는 개념을 정의 할 수 있었다.

그렇다면 어떤 스케줄 `a schedule`과 `b schedule`이 `conflict equivalent`하다고 한다면, 

`a schedul`을 `conflict serializable`하다고 말할 수 있다