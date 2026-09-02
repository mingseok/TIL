## 스레드의 상태(Thread State)는 어떻게 바뀌나요?

<br/>

## 자바 스레드의 여섯 가지 상태

```java
Thread.State.values()
= [NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED]
```

<br/>

`Thread.getState()` 로 언제든 볼 수 있다.

<br/>

앞의 pcb는 무엇인가요 글에서 본 운영체제의 `준비 / 실행 / 봉쇄` 와 대응은 되는데 완전히 같지는 않다.

<br/><br/>

## 궁금증!

```java
스레드 하나를 만들어서 살아가는 동안 상태를 하나씩 찍어봤다
```

```java
Thread t = new Thread(() -> {
    synchronized (mon) { mon.wait(); }          // 누가 깨워줄 때까지
    Thread.sleep(300);                          // 300ms
    long x = 0; for (...) x += i;               // 계산
});
```

<br/>

### 결과

```java
new Thread() 직후            = NEW
wait() 안에서 기다릴 때        = WAITING
sleep(300) 중                = TIMED_WAITING
계산 중                      = RUNNABLE
run() 이 끝난 뒤              = TERMINATED
```

<br/>

그리고 다른 스레드가 쥔 `synchronized` 에 들어가려 할 때는 따로 있다.

```java
synchronized 진입 대기 중       = BLOCKED
```

<br/><br/>

## 기다리는 상태가 셋으로 나뉜다

이게 헷갈리는 부분이다. 운영체제는 전부 `봉쇄` 하나로 보는데 자바는 셋으로 나눈다.

```java
BLOCKED       - synchronized 락을 기다린다. 락이 풀리면 JVM 이 깨운다
WAITING       - wait(), join(), park() 로 무한히 기다린다. 누가 notify 해야 깨어난다
TIMED_WAITING - sleep(ms), wait(ms), join(ms). 시간이 지나면 저절로 깨어난다
```

<br/>

셋 다 CPU를 안 쓴다. 차이는 `무엇이 깨우느냐` 다.

<br/>

`jstack` 을 읽을 때 이 구분이 바로 진단이 된다.

```java
BLOCKED 가 수십 개      -> 락 경합. 누가 그 락을 오래 쥐고 있나
WAITING 이 수십 개      -> 풀에서 일감을 기다리는 스레드들. 정상일 수 있다
TIMED_WAITING 이 많다   -> sleep 이나 타임아웃 대기. 외부 응답이 느린가
```

<br/>

앞의 교착 상태(Deadlock) 글에서 본 교착이 `BLOCKED` 두 개로 나타난 것이 이것이다.

<br/><br/>

## RUNNABLE 은 '실행 중' 이 아니다

열거값에 `RUNNING` 이 없다.

```java
[NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED]
```

<br/>

자바는 `CPU 를 실제로 쓰고 있는 것` 과 `쓸 준비가 되어 줄 서 있는 것` 을 구분하지 않는다.

둘 다 `RUNNABLE` 이다.

<br/>

앞의 pcb는 무엇인가요 글에서 본 운영체제의 `준비` 와 `실행` 이 자바에서는 하나로 합쳐진 것이다.

<br/>

이유가 있다. 어느 스레드가 지금 코어에 올라가 있는지는 운영체제만 알고, 그것도 마이크로초 단위로 바뀐다.

JVM이 그걸 물어보는 순간 이미 바뀌어 있을 수 있어서 의미가 없는 것이다.

<br/>

그래서 `RUNNABLE` 인데 진행이 안 되면 두 가지를 의심한다.

```java
코어가 부족하다          -> 다른 RUNNABLE 스레드에 밀려 줄만 서 있다
I/O 를 하고 있다        -> 소켓 read 는 운영체제가 막고 있어도 자바에서는 RUNNABLE 로 보인다
```

<br/>

두 번째가 함정이다. 네트워크 응답을 기다리는 스레드가 `jstack` 에 `RUNNABLE` 로 찍힌다.

`java.net.SocketInputStream.socketRead0` 같은 줄이 스택 맨 위에 있으면 실제로는 기다리고 있는 것이다.

<br/><br/>

## 상태 전이를 그려보면

```java
NEW --start()--> RUNNABLE
RUNNABLE --synchronized 대기--> BLOCKED --락 획득--> RUNNABLE
RUNNABLE --wait()/join()--> WAITING --notify()/종료--> RUNNABLE
RUNNABLE --sleep(ms)--> TIMED_WAITING --시간 경과--> RUNNABLE
RUNNABLE --run() 끝--> TERMINATED
```

<br/>

`TERMINATED` 에서는 못 돌아온다.

```java
t.start();
t.join();
t.start();      // IllegalThreadStateException
```

<br/>

끝난 스레드를 다시 시작할 수 없다. 새로 만들어야 한다.

앞의 스레드 풀 글에서 본 대로, 그래서 풀은 스레드를 끝내지 않고 일감을 기다리는 `WAITING` 상태로 되돌려 재사용하는 것이다.

<br/><br/>

## WAITING 에서 깨우는 두 방법

```java
notify()     - 대기 중인 것 중 하나를 깨운다. 누구인지는 정해져 있지 않다
notifyAll()  - 전부 깨운다. 각자 조건을 다시 확인하고 아니면 다시 wait()
```

<br/>

`wait()` 는 항상 반복문 안에서 써야 한다.

```java
synchronized (mon) {
    while (!조건) {          // if 가 아니라 while
        mon.wait();
    }
}
```

<br/>

깨어났다고 조건이 만족된 게 아닐 수 있기 때문이다.

`notifyAll()` 로 전부 깨어났는데 실제로 진행할 수 있는 것은 하나뿐인 경우가 그렇다.

<br/>

깨어났는데 아무 일 없이 다시 자는 것을 거짓 깨어남이라고 부른다.

운영체제 수준에서 이유 없이 깨어나는 경우도 있어서, 자바 문서에 `항상 반복문으로 확인하라` 고 적혀 있다.

<br/><br/>

## 데몬 스레드는 상태가 아니라 속성이다

```java
t.setDaemon(true);
```

<br/>

상태 목록에는 없다. `TERMINATED` 가 되는 조건이 다를 뿐이다.

```java
일반 스레드 - run() 이 끝나야 TERMINATED
데몬 스레드 - run() 이 끝나거나, 일반 스레드가 전부 끝나면 강제 TERMINATED
```

<br/>

앞의 async 코드 구현 글에서 본 `runAsync 콜백이 안 찍힌다` 가 이것이다.

`main` 이 끝나면 데몬인 풀 스레드가 하던 일을 마치지 못하고 사라진다.

<br/>

앞의 동시성 문제 해결 방법 글의 `plain 플래그 실험` 에서 JVM이 안 끝났던 것도 반대 경우다.

일반 스레드 하나가 `RUNNABLE` 로 계속 도니, `main` 이 끝나도 JVM이 안 내려간 것이다.
