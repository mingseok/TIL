## read-lock, write-lock

<br/>

데이터베이스에 x = 10이 저장되어 있다.

트랜잭션1과 트랜잭션2가 동시에 x의 값을 변경(read도 포함) 하려고 한다면

예상치 못한 동작이 발생하는 것이다. → 이 문제는 어떻게 해결하는가?

```java
"lock을 사용하여 해결한다" -> cs에서 배우는 lock하고 같은 개념이라고 생각하자.
```

<br/><br/>

## lock이란?

데이터마다 락이 있어서 그 데이터를 변경하거나 읽을때는 lock을 취득을 해야하는 것이다.

만약, lock을 취득하지 못하면 그 lock을 취득할때까지 기다린다.

<br/>

### 위 문제점을 풀어보자면 이렇게 된다

![이미지](/programming/img/입문464.PNG)

<br/>

1. 먼저 트랜잭션1이 시작하여 쓰기lock을 획득 해야 되는 것이다.

2. 그런데, 이 타이밍에 트랜잭션2번이 실행 하였다.

    - 트랜잭션2도 쓰기lock을 획득하려고 시도를 하지만, 트랜잭션1이 쓰고 있기 때문에, 실패한다.
        
        그러면 트랜잭션1이 끝날때까지 기다리는 것이다. `(여기서 트랜잭션2는 블락)`
        

<br/><br/>

## write-lock 개념

```java
write-lock(x) : 트랜잭션이 "쓰기"를 하려고 획득해야 되는 락을 의미한다.
```

lock을 `write-lock` 이라고 부른다.

write라고 부르지만, read할때도 사용하는 것이다.

- 다른 트랜잭션이 같은 데이터를 read / write 하는 것을 허용하지 않는다.

<br/><br/>

## read-lock 개념

```java
read_lock(x) : 트랜잭션이 데이터베이스를 "읽기" 하려고 획득해야 되는 락을 의미

*중요 개념*
트랜잭션1이 read-lock를 가지고 있는 상태이고, 트랜잭션2에서 쓰기를 하려고 
write-lock 획득을 하려고 시도를 해도 불가능하다. 


왜? 
read-lock을 먼저 획득한 상태이기 때문이다. 
결론은, write-lock 획득할 수 없다. (블락 상태가 된다)


언제까지?
read-lock이 해제 될때까지
```

read할 때 사용한다. → 즉, write할때는 사용되지 않는다.

다른 트랜잭션들 여러개가 동시에 같은 데이터를 read하는 것을 허용한다.

- 같은 데이터에 동시에 여러 트랜잭션이 읽는다 해도 상관없다는 뜻이다.

<br/>

### 둘은 호환성이 있다.

| 트랜잭션1 \ 트랜잭션2 | read-lock | write-lock |
| --- | --- | --- |
| read-lock | O | X |
| write-lock | X | X |

<br/><br/>

## 정리 lock은 → Nonserializable 하다

즉, serializable 하지 않다는 것이다

- 두개의 트랜잭션이 동시에 곂쳐 실행되면서 이상 현상이 발생한 것이다.

그렇기에, lock을 사용하는 것만으로는 serializable 하지 않을 수 있다는 것이다.

<br/>

### 문제 상황 예시

![이미지](/programming/img/입문465.PNG)

트랜잭션2번은 업데이트 되지 않은 x를 읽었다.

트랜잭션1번은 업데이트 된 y를 읽어야 하는 상황이다.

<br/>

트랜잭션2번은 write 작성을 하기 위해 unlock(x)를 진행 하고 

y의 값을 읽기 위해 write_lock(y)을 취득하려 한다. → `(현재 위 그림의 빨간부분)`

<br/>

그런데, 트랜잭션1번이 y를 읽으려고 read_lock를 취득해 놓은 상황이다.

`즉, 결론은 write_lock(y) 획득하지 못한다.` 

- read_lock이 해제 될때까지 블락 생태이다. (진행을 못한다는 말)

<br/>

그러고 빨간 부분을 지나서 트랜잭션1은 unlock(y) 을 진행하게 된다.

그리고 이제서야 트랜잭션2는 write_lock(y)을 획득한다.

<br/>

이런식으로 쭉쭉 밑으로 진행되는 것이다.

하지만, 이 스케줄은 Nonserializable 이라고 부른다.

```java
"2개의 트랜잭션이 동시에 곂쳐서 실행되면서 이상한 현상이 발생한 것이다."
```

결론은, lock을 사용한 것만으로는 serializable 하지 않다는 것이다.

<br/><br/>

## 원인은?

```java
여기서 트랜잭션들의 목적을 생각하자.

트랜잭션1은 x + y의 합을 x에 저장하는 목적이고,
트랜잭션2의 역할은 x + y의 합을 y에 저장하는 목적이다
```

트랜잭션 2기준으로 본다면(빨간색 부분) x에 대해서 unlock을 하고, y에 대해서 쓰기를 하려고, 

write_lock을 취득하려고 하는 그 사이에 
1번 트랜잭션이 y에 대한 read락을 취득하게 되면서 트랜잭션2번은 

write_lock을 취득할 수 없게 되는 것이고, 
트랜잭션2는 블락 상태가 되어 버린 것이다.

<br/>

그리하여 트랜잭션1번은 (자기의 역할을 수행하기 위해)계속 진행되면서 

업데이트 되지 않은 y를 읽어 버리는 바람에 이렇게 이상한 현상이 발생하는 것이다.

![이미지](/programming/img/입문466.PNG)

<br/><br/>

## 수정해보기

그렇다면 serializable 하게 만들기 위해서는 이렇게 하면 되는 것이다.

![이미지](/programming/img/입문467.PNG)

<br/><br/>

## 2PL protocol 이란?

![이미지](/programming/img/입문468.PNG)

tx에서 모든 locking operation이 최초의 unlock operation 보다 

먼저 수행되도록 하는 것을 말한다.

<br/><br/>

## 2PL protocol은 무엇인가? (two-phase locking)

tx에서 모든 locking operation이 최초의 unlock operation 보다 

먼저 수행되도록 하는 것이 2PL protocol이라고 말한다.

```java
"한번 unlock이 시작되면 그 이후에는 새로운 락을 취득하지 않는다는 것이다"

락을 획득만 하는 phase와, 락을 반환만 하는 phase가 있다.
즉, 두 단계로 나눠져서 locking이 동작하는 것을 2PL protocol 이라고 말하는 것이다.
```

<br/>

### 첫번째 특징. Expanding phase

lock을 취득하기만 하고 반환하지는 않는 phase를 상태를 말한다.

![이미지](/programming/img/입문469.PNG)

<br/>

### 두번째 특징. Shrinking Phase

lock을 반환만 하고 취득하지는 않는 phase를 말한다.

![이미지](/programming/img/입문470.PNG)

<br/><br/>

## 2PL protocol 최종 정리

| 트랜잭션1 \ 트랜잭션2 | read-lock | write-lock |
| --- | --- | --- |
| read-lock | O | X |
| write-lock | X | X |

서로 다른 트랜잭션이 같은 데이터에 read-lock 하는 경우를 제외하고는 

나머지 모든 경우에 대해서는 한쪽이 블락상태가 되어 버리는 문제가 있다. 

```java
"전체 처리량이 좋지가 않다는 것이다."
```

<br/>

그리하여 좀 더 좋은 성능을 높일 수 있는게 뭐가 있을까 고민하다보니

read와 write가 서로를 block 하는 것이라도 해결하자는 것에서 나온것이 mvcc 입니다.