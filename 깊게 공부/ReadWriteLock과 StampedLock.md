## ReadWriteLock과 StampedLock

<br/>

## 읽기는 동시에 해도 된다

`synchronized` 는 읽기든 쓰기든 한 번에 하나만 들여보낸다.

```java
synchronized (lock) { return data[i]; }     // 읽기만 하는데도 다른 읽기를 막는다
```

<br/>

읽기끼리는 서로 방해하지 않는다. 값을 안 바꾸니까.

쓰기만 다른 것을 막으면 된다.

```java
읽기 - 읽기  : 동시에 OK
읽기 - 쓰기  : 안 됨
쓰기 - 쓰기  : 안 됨
```

<br/>

이걸 구현한 것이 `ReentrantReadWriteLock` 이다.

```java
rw.readLock().lock();     // 여러 스레드가 동시에 잡을 수 있다
rw.writeLock().lock();    // 혼자만. 읽기도 전부 기다린다
```

<br/><br/>

## 궁금증!

```java
읽기가 대부분이면 정말 빨라지나
```

스레드 8개, 읽기 95% 쓰기 5%로 재봤다.

<br/>

### 결과

```java
synchronized            = 51 ms
ReentrantReadWriteLock  = 63 ms     <- 더 느리다
StampedLock (낙관적 읽기) = 14 ms     <- 3.6 배 빠르다
```

<br/>

`ReentrantReadWriteLock` 이 `synchronized` 보다 느렸다. 예상과 반대다.

<br/>

읽기가 95%인데 왜 느린가.

<br/>

임계 영역이 너무 짧기 때문이다. 배열 한 칸 읽는 것이다.

읽기 락을 잡는 비용 자체가 배열 읽기보다 크다. 락 안에서 `읽는 스레드 수` 를 세고 관리해야 하니까.

<br/>

앞의 동시성 문제 해결 방법 글에서 본 대로 `synchronized` 는 경쟁이 잠깐이면 CAS 한 번으로 끝나는데,

읽기 락은 카운터를 올리고 내리는 두 번의 원자 연산이 매번 든다.

<br/><br/>

## 읽기/쓰기 락이 이기는 조건

```java
읽기 임계 영역이 길다   - 락 비용이 묻힌다
읽기가 정말 압도적이다  - 95% 로도 부족했다. 99% 이상, 또는 읽기가 길어야
쓰기가 드물다
```

<br/>

캐시 조회처럼 `맵을 훑어서 여러 값을 조합하는` 읽기라면 이득이 난다.

배열 한 칸 읽는 데는 안 쓰는 게 낫다.

<br/>

50/50으로 바꾸면 격차가 더 벌어진다.

```java
synchronized            = 41 ms
ReentrantReadWriteLock  = 54 ms
```

<br/>

쓰기가 많아지면 읽기 락의 장점이 사라지고 비용만 남는다.

<br/><br/>

## StampedLock 의 낙관적 읽기

14ms로 압도적이었다. 방식이 다르다.

```java
long stamp = lock.tryOptimisticRead();     // 락을 안 잡는다. 도장(stamp)만 받는다
int v = data[i];                            // 그냥 읽는다
if (!lock.validate(stamp)) {                // 읽는 동안 쓰기가 있었나 확인
    stamp = lock.readLock();                // 있었으면 그때 진짜 락 잡고 다시
    try { v = data[i]; } finally { lock.unlockRead(stamp); }
}
```

<br/>

락을 잡지 않고 읽는다. 읽고 나서 `그 사이 누가 썼나` 만 확인한다.

<br/>

앞의 CAS 알고리즘 설명 글에서 본 것과 같은 발상이다.

```java
비관적 - 먼저 막고 한다
낙관적 - 먼저 하고 충돌했으면 다시 한다
```

<br/>

쓰기가 드물면 거의 매번 첫 시도에 성공하니, 락 비용이 도장 하나 받는 것으로 준다.

<br/>

앞의 트랜잭션 글에서 본 낙관적 락의 `version` 컬럼과 정확히 같은 구조다.

`stamp` 가 `version` 이고, `validate()` 가 `WHERE version = ?` 이다.

<br/><br/>

## StampedLock 의 함정

```java
1. 재진입이 안 된다   - 같은 스레드가 두 번 잡으면 교착
2. Condition 이 없다  - wait/notify 같은 것을 못 쓴다
3. 낙관적 읽기 중에 읽은 값은 깨져 있을 수 있다
```

<br/>

`3번` 이 중요하다.

```java
long stamp = lock.tryOptimisticRead();
int a = data[0];
int b = data[1];          // 이 사이에 쓰기가 끼면 a 와 b 가 서로 다른 시점의 값
if (!lock.validate(stamp)) { ... }
```

<br/>

`validate()` 전까지는 `a` 와 `b` 를 믿고 계산하면 안 된다.

읽기만 하고, 검증 통과한 뒤에 쓰는 것이 규칙이다.

<br/>

앞의 Reentrant Lock과 synchronized의 차이 글에서 본 대로 재진입이 안 되는 것도 조심해야 한다.

`ReentrantReadWriteLock` 은 이름대로 재진입이 된다.

<br/><br/>

## 정리

```java
기본                       -> synchronized. 짧은 임계 영역에서는 이게 제일 빠르다
읽기가 길고 압도적           -> ReentrantReadWriteLock
읽기가 압도적이고 아주 짧다   -> StampedLock 낙관적 읽기. 재진입 불필요할 때만
```

<br/>

`읽기가 많으니 읽기/쓰기 락` 이라고 바로 가면 위 실험처럼 오히려 느려진다.

재봐야 안다. 락의 종류를 바꾸는 것은 최적화이고, 최적화는 측정이 먼저다.
