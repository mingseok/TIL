## 교착 상태(Deadlock)는 왜 생기고 어떻게 찾나요?

<br/>

## 교착 상태란

두 개 이상의 스레드가 서로 상대가 쥔 락을 기다리면서 아무도 진행하지 못하는 상태다.

```java
스레드-1 : A 를 잡고 -> B 를 기다린다
스레드-2 : B 를 잡고 -> A 를 기다린다
```

<br/>

둘 다 자기가 쥔 것을 놓지 않으니 영원히 풀리지 않는다.

<br/><br/>

## 교착이 생기는 네 가지 조건

네 가지가 전부 성립해야 교착이 난다. 하나만 깨면 안 난다.

```java
1. 상호 배제   - 자원을 한 번에 하나만 쓸 수 있다
2. 점유와 대기 - 하나를 쥔 채로 다른 것을 기다린다
3. 비선점      - 남이 쥔 것을 강제로 빼앗을 수 없다
4. 순환 대기   - 기다리는 관계가 원을 그린다
```

<br/>

`1번` 과 `3번` 은 락의 본질이라 깰 수 없다. 깨면 락이 아니다.

그래서 실제로 손댈 수 있는 것은 `2번` 과 `4번` 이다.

<br/><br/>

## 궁금증!

```java
교착 상태를 일부러 만들어서 그 순간을 봤다
```

```java
static final Object A = new Object(), B = new Object();

Thread t1 = new Thread(() -> { synchronized (A) { sleep(100); synchronized (B) { } } });
Thread t2 = new Thread(() -> { synchronized (B) { sleep(100); synchronized (A) { } } });
```

<br/>

`sleep(100)` 은 둘이 각자 첫 락을 잡을 시간을 주려고 넣은 것이다.

<br/>

### 결과

```java
스레드-1 상태 = BLOCKED, 스레드-2 상태 = BLOCKED
```

<br/>

둘 다 `BLOCKED` 로 멈춰 있다.

앞의 스레드의 상태 글에서 본 대로, `BLOCKED` 는 `synchronized` 에 들어가려고 기다리는 상태다.

둘이 서로를 기다리니 이 상태에서 영원히 안 바뀐다.

<br/><br/>

## JVM 이 교착을 찾아준다

`ThreadMXBean` 에 교착 탐지 메서드가 있다.

```java
ThreadMXBean mx = ManagementFactory.getThreadMXBean();
long[] ids = mx.findDeadlockedThreads();
```

<br/>

### 결과

```java
교착 상태 스레드 수 = 2
스레드-1 가 java.lang.Object 를 기다림. 그 락을 쥔 것은 스레드-2
스레드-2 가 java.lang.Object 를 기다림. 그 락을 쥔 것은 스레드-1
```

<br/>

누가 무엇을 기다리고, 그것을 누가 쥐고 있는지까지 나온다.

원이 그려지면 교착인 것이다. 위의 `4번 순환 대기` 를 JVM이 그래프로 찾아낸 셈이다.

<br/>

운영 서버에서는 `jstack` 이 같은 일을 한다.

```java
$ jstack <pid>

Found one Java-level deadlock:
=============================
"스레드-1":
  waiting to lock monitor 0x... (object 0x..., a java.lang.Object),
  which is held by "스레드-2"
"스레드-2":
  waiting to lock monitor 0x... (object 0x..., a java.lang.Object),
  which is held by "스레드-1"
```

<br/>

앞의 자바 가상머신(JVM) 글에서 본 대로, 서버가 멈췄을 때 제일 먼저 찍어보는 것이 이것이다.

`Found one Java-level deadlock` 이라는 문장이 보이면 원인은 찾은 것이다.

<br/><br/>

## 교착이 한 번 나면 그 락은 영원히 잠긴다

교착과 아무 상관없는 세 번째 스레드를 만들어 그 락을 잡게 해봤다.

```java
Thread third = new Thread(() -> { synchronized (C) { } });     // C 는 교착 중인 락
```

<br/>

### 결과

```java
교착과 무관한 세 번째 스레드가 C 를 잡으려 하면: 영원히 BLOCKED
```

<br/>

이게 교착이 무서운 진짜 이유다.

두 스레드가 멈추는 것으로 끝나지 않고, 그 락을 필요로 하는 모든 요청이 뒤에 쌓인다.

<br/>

앞의 멀티 쓰레드 글에서 본 톰캣 스레드 200개가 전부 이 락 앞에 줄을 서면, 서버가 요청을 하나도 못 받는 상태가 된다.

CPU는 놀고 있는데 응답이 없는 것이다. 지표만 보면 원인을 못 찾는다.

<br/><br/>

## 예방 1 : 락을 항상 같은 순서로 잡는다

`4번 순환 대기` 를 깨는 방법이다.

```java
Thread u1 = new Thread(() -> { synchronized (A) { sleep(100); synchronized (B) { } } });
Thread u2 = new Thread(() -> { synchronized (A) { sleep(100); synchronized (B) { } } });   // 순서를 A -> B 로 통일
```

<br/>

### 결과

```java
둘 다 끝났나: true
```

<br/>

모든 스레드가 `A -> B` 순서로만 잡으면 원이 생길 수 없다.

`B 를 쥔 채로 A 를 기다리는` 스레드가 존재하지 않기 때문이다.

<br/>

실무에서는 이렇게 규칙을 정한다.

```java
계좌 이체 : 항상 계좌 번호가 작은 쪽부터 잠근다
엔티티    : 항상 부모 -> 자식 순서로 잠근다
```

<br/>

앞의 DB 락 글에서 본 데드락도 같은 원리다.

두 트랜잭션이 같은 두 행을 반대 순서로 `UPDATE` 하면 DB 안에서 교착이 나고, MySQL은 그것을 감지해서 한쪽을 강제 롤백한다.

<br/><br/>

## 예방 2 : 못 잡으면 물러난다

`2번 점유와 대기` 를 깨는 방법이다.

```java
first.lock();
try {
    if (second.tryLock(50, TimeUnit.MILLISECONDS)) {
        // 둘 다 잡았다. 일한다
    } else {
        backoff++;          // 못 잡았으면 first 도 놓고 잠깐 뒤 다시
    }
} finally {
    first.unlock();
}
```

<br/>

### 결과

```java
둘 다 끝났나: true, 물러난 횟수 = 2
```

<br/>

두 번 물러났다가 결국 둘 다 끝났다.

앞의 Reentrant Lock과 synchronized의 차이 글에서 본 `tryLock` 이 이래서 필요한 것이다.

`synchronized` 로는 이 방법을 쓸 수 없다. 못 잡으면 기다리는 것 말고 선택지가 없기 때문이다.

<br/><br/>

## 교착이 아닌데 교착처럼 보이는 것

```java
라이브락 - 서로 물러나기만 반복해서 아무도 진행 못 한다
기아     - 계속 다른 스레드에 밀려서 한 스레드만 영원히 못 잡는다
```

<br/>

위의 `tryLock` 물러나기도 운이 나쁘면 라이브락이 된다. 둘이 동시에 물러나고 동시에 다시 시도하면 또 부딪힌다.

그래서 물러날 때 무작위로 조금씩 다르게 기다리는 것이 관례다.

<br/>

기아는 앞의 Reentrant Lock과 synchronized의 차이 글에서 본 비공정 락 순서가 극단으로 간 경우다.

```java
비공정 락 순서: [0, 0, 0, 0, 0, 1, 1, 1, 1, 1, ...]
```

<br/>

셋 다 `진행이 안 된다` 는 점은 같은데 원인이 다르니 해법도 다르다.

`jstack` 을 찍었을 때 `Found one Java-level deadlock` 이 안 나오면 이 둘을 의심하면 된다.

<br/><br/>

## 정리

```java
교착 = 네 조건이 전부 성립
찾기 = ThreadMXBean.findDeadlockedThreads(), jstack
예방 = 락 순서 통일 (순환 대기 깨기), tryLock (점유와 대기 깨기)
```

<br/>

제일 좋은 것은 락을 두 개 이상 동시에 잡는 코드를 안 만드는 것이다.

앞의 동시성 문제 해결 방법 글에서 본 대로, 공유 상태를 줄이면 락이 줄고, 락이 하나뿐이면 교착이 날 수가 없다.
