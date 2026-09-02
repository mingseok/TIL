## CountDownLatch, CyclicBarrier, Phaser

<br/>

## 셋 다 "기다리는" 도구다

앞의 뮤텍스와 세마포어의 차이 글에서 본 것들은 `자원을 지키는` 도구였다.

이 셋은 자원과 상관없다. `타이밍을 맞추는` 도구다.

```java
CountDownLatch - N 개가 끝나길 한 곳에서 기다린다
CyclicBarrier  - N 개가 서로를 기다렸다가 동시에 출발한다
Phaser         - Barrier 인데 인원이 바뀌어도 된다
```

<br/><br/>

## 궁금증!

```java
셋이 실제로 어떻게 다르게 동작하나
```

<br/>

### CountDownLatch

```java
CountDownLatch latch = new CountDownLatch(3);

// 작업 스레드 3개가 각자 끝나면
latch.countDown();

// main 은
latch.await();               // 카운트가 0 이 될 때까지
```

<br/>

```java
작업 1 끝. 남은 카운트 -> 2
작업 2 끝. 남은 카운트 -> 1
작업 3 끝. 남은 카운트 -> 0
전부 끝났다. 다시 쓸 수 있나? getCount()=0 -> await() 가 바로 통과. 재사용 불가
```

<br/>

한 번 0이 되면 끝이다. 다시 3으로 못 올린다.

<br/>

기다리는 쪽과 세는 쪽이 다르다는 것이 특징이다.

작업 스레드는 `countDown()` 만 하고 기다리지 않는다. `main` 만 기다린다.

<br/>

### CyclicBarrier

```java
CyclicBarrier barrier = new CyclicBarrier(3, () -> System.out.println("3 명 다 모임"));

// 스레드 3개가 각자
barrier.await();             // 3 명이 다 도착할 때까지 여기서 멈춘다
```

<br/>

```java
스레드 1 단계 1 도착
스레드 2 단계 1 도착
스레드 3 단계 1 도착
  <- 3 명 다 모임. 다음 단계로>
스레드 1 단계 2 도착
스레드 2 단계 2 도착
스레드 3 단계 2 도착
  <- 3 명 다 모임. 다음 단계로>
```

<br/>

같은 `barrier` 로 두 단계를 돌았다. 다 모이면 자동으로 초기화된다.

<br/>

기다리는 쪽과 도착하는 쪽이 같다. 셋이 서로를 기다린다.

`Latch` 와 정확히 이 점이 다르다.

<br/>

### Phaser

```java
Phaser ph = new Phaser(1);           // main 이 참가자 1
ph.register();                        // 참가자를 늘린다
ph.arriveAndDeregister();             // 도착하고 빠진다
ph.arriveAndAwaitAdvance();           // 도착하고 다음 단계를 기다린다
```

<br/>

```java
등록 4 -> 3 명이 arriveAndDeregister 로 빠짐. 남은 참가자 = 1
```

<br/>

`Barrier` 는 인원이 생성 시점에 고정이다.

`Phaser` 는 중간에 늘고 줄 수 있다.

<br/><br/>

## 어디에 쓰나

```java
CountDownLatch
  - 서버 기동 시 초기화 작업 N 개가 끝나야 요청을 받는다
  - 테스트에서 비동기 작업이 끝나길 기다린다
  - 앞의 async 코드 구현 글에서 본 "콜백이 끝나기 전에 테스트가 끝나는" 문제의 해법

CyclicBarrier
  - 병렬 계산을 단계로 나눠서 각 단계가 끝나야 다음으로 (행렬 계산, 시뮬레이션)
  - 부하 테스트에서 클라이언트 N 개가 정확히 동시에 출발

Phaser
  - 참가자가 동적으로 붙었다 떨어지는 다단계 작업. 실무에서 드물다
```

<br/>

셋 중 `CountDownLatch` 가 압도적으로 자주 쓰인다.

<br/><br/>

## 테스트에서의 Latch

```java
@Test
void 비동기_이벤트_테스트() throws Exception {
    CountDownLatch latch = new CountDownLatch(1);
    listener.onComplete(() -> latch.countDown());

    service.doAsync();

    assertTrue(latch.await(3, TimeUnit.SECONDS));     // 3초 안에 안 오면 실패
    // 여기서 검증
}
```

<br/>

`Thread.sleep(1000)` 으로 기다리는 것보다 낫다.

빨리 끝나면 빨리 끝나고, 안 끝나면 3초 뒤에 확실히 실패한다.

<br/>

`sleep` 은 짧으면 불안정하고 길면 테스트가 느려진다. 둘 다 못 잡는다.

<br/><br/>

## CompletableFuture 와의 관계

```java
CompletableFuture.allOf(f1, f2, f3).join();
```

<br/>

이것도 `N 개가 끝나길 기다린다` 다. `Latch` 와 같은 일을 한다.

<br/>

차이는 결과를 들고 있느냐다.

```java
CountDownLatch      - 끝났다는 신호만. 결과는 다른 데서 받아야
CompletableFuture   - 결과와 예외까지 들고 있다
```

<br/>

새로 짜는 코드면 `CompletableFuture` 쪽이 편하다.

`Latch` 는 기존 콜백 코드나 스레드 직접 다루는 코드에 끼워 넣을 때 쓴다.

<br/><br/>

## 셋의 공통 함정

한 명이 안 오면 전부 영원히 기다린다.

```java
작업 스레드 하나가 예외로 죽어서 countDown() 을 못 했다
-> main 의 await() 는 영원히 안 풀린다
```

<br/>

그래서 `countDown()` 은 `finally` 에 두고, `await()` 는 시간 제한을 걸어야 한다.

```java
try { 작업(); } finally { latch.countDown(); }
latch.await(10, TimeUnit.SECONDS);
```

<br/>

앞의 교착 상태(Deadlock) 글에서 본 것과 같은 성질이다.

`기다리는 도구` 는 항상 `영원히 기다리는` 경우를 먼저 생각해야 한다.

`CyclicBarrier` 는 한 명이 깨지면 나머지에게 `BrokenBarrierException` 을 던져준다. 그건 좀 낫다.
