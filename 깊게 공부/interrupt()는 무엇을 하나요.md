## interrupt()는 무엇을 하나요?

<br/>

## 이름이 오해를 부른다

```java
thread.interrupt();
```

<br/>

`중단` 이라는 뜻이라 스레드를 멈추는 것 같은데, 그렇지 않다.

스레드를 멈추는 메서드는 자바에 없다. `stop()` 이 있었는데 위험해서 쓰지 말라고 되어 있다.

<br/>

`interrupt()` 가 하는 일은 두 가지뿐이다.

```java
1. 그 스레드의 인터럽트 플래그를 true 로 세운다
2. 그 스레드가 sleep / wait / join 으로 잠들어 있으면 깨운다
```

<br/><br/>

## 궁금증!

```java
세 종류의 스레드에 interrupt() 를 걸어봤다
```

<br/>

### 1. 계산만 하는 스레드. 플래그를 확인하는 코드가 있다

```java
while (true) {
    c++;
    if (c % 100_000_000 == 0 && Thread.currentThread().isInterrupted()) break;
}
```

```java
[spinner] 플래그를 직접 확인해서 멈췄다
계산만 하는 스레드에 interrupt(): 3초 안에 멈췄나 = true
```

<br/>

멈췄다. 코드가 플래그를 보고 스스로 나갔기 때문이다.

<br/>

### 2. 잠들어 있는 스레드

```java
try {
    Thread.sleep(10_000);
} catch (InterruptedException e) {
    System.out.println("플래그 = " + Thread.currentThread().isInterrupted());
}
```

```java
[sleeper] sleep 중 InterruptedException. 플래그 = false
sleep 중인 스레드에 interrupt(): 즉시 깨어났나 = true
```

<br/>

10초 자려던 것이 즉시 깨어났다. 예외로 깨어난다.

<br/>

그리고 깨어난 뒤 플래그가 `false` 다. 예외를 던지면서 플래그를 지운 것이다.

이게 함정이다. 뒤에서 본다.

<br/>

### 3. 계산만 하고 플래그를 안 보는 스레드

```java
while (System.currentTimeMillis() < end) c++;       // 플래그를 안 본다
```

```java
[ignorer] 아무것도 확인 안 하고 끝까지 돌았다. 플래그 = true
```

<br/>

`interrupt()` 를 걸었는데 끝까지 돌았다. 플래그만 `true` 로 남아 있다.

<br/>

셋을 합치면 이렇다.

```java
interrupt() 는 스레드를 죽이지 않는다
플래그를 세우고, 잠든 스레드를 깨울 뿐이다
멈추는 것은 스레드 자신이 플래그를 보고 결정한다
```

<br/><br/>

## 왜 강제로 멈추게 안 만들었나

`stop()` 이 그렇게 동작했고, 문제가 생겼다.

```java
synchronized (lock) {
    balance -= 100;
    // <- 여기서 stop() 당하면
    other += 100;
}
```

<br/>

락은 풀리는데 데이터는 반쯤 바뀐 상태로 남는다.

앞의 동시성  동기화 글에서 본 임계 영역이 중간에 끊어지는 것이다.

<br/>

그래서 `언제 멈출지는 그 스레드가 안전한 지점에서 스스로 정하게` 바꿨다.

`interrupt()` 는 `멈춰라` 가 아니라 `멈춰달라는 요청이 왔다` 를 알리는 것이다.

<br/><br/>

## 플래그가 지워지는 함정

위 2번 실험에서 `InterruptedException` 이 나면서 플래그가 `false` 로 바뀌었다.

<br/>

이 코드가 문제다.

```java
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    // 아무것도 안 함
}
// 여기서 isInterrupted() 는 false. 인터럽트가 있었다는 사실이 사라졌다
```

<br/>

예외를 잡고 아무것도 안 하면, 이 스레드를 멈추려던 바깥의 요청이 증발한다.

스레드 풀이 `shutdownNow()` 로 전부 인터럽트했는데 이 코드가 삼켜서 계속 도는 것이다.

<br/>

그래서 규칙이 있다.

```java
catch (InterruptedException e) {
    Thread.currentThread().interrupt();      // 플래그를 다시 세운다
    return;                                  // 또는 예외를 다시 던진다
}
```

<br/>

앞의 예외 포함과 스택 트레이스 글에서 본 `InterruptedException 은 잡고 나서 반드시 다시 표시해줘야 한다` 가 이것이다.

<br/><br/>

## 어디에서 InterruptedException 이 나오나

```java
Thread.sleep()
Object.wait()
Thread.join()
BlockingQueue.put() / take()
Lock.lockInterruptibly()
Future.get()
```

<br/>

전부 `기다리는` 메서드다. 기다리는 중에 깨울 수 있게 만들어둔 것이다.

<br/>

앞의 Reentrant Lock과 synchronized의 차이 글에서 본 대로 `synchronized` 는 여기 없다.

`synchronized` 를 기다리는 스레드는 `interrupt()` 로 못 깨운다. `BLOCKED` 상태는 인터럽트에 반응하지 않는다.

<br/>

`lockInterruptibly()` 가 따로 있는 이유가 그것이다.

<br/><br/>

## 소켓 I/O 는 안 깨어난다

```java
socket.getInputStream().read();      // 여기서 멈춘 스레드에 interrupt() -> 안 깨어난다
```

<br/>

앞의 IO 모델 글에서 본 Blocking I/O는 운영체제가 잠재우고 있어서 JVM이 못 깨운다.

<br/>

깨우려면 소켓을 닫아야 한다.

```java
socket.close();      // 다른 스레드에서. read() 가 SocketException 으로 깨어난다
```

<br/>

NIO 채널은 다르다. `InterruptibleChannel` 이라 `interrupt()` 에 반응해서 `ClosedByInterruptException` 을 던진다.

<br/><br/>

## 실무에서 이 요청이 오는 곳

```java
ExecutorService.shutdownNow()     - 풀의 모든 스레드에 interrupt()
Future.cancel(true)               - 그 작업의 스레드에 interrupt()
서버 종료 (SIGTERM)                - 스프링이 풀을 shutdown 하면서 interrupt()
@Transactional 타임아웃            - 초과하면 그 스레드에 interrupt()
```

<br/>

서버를 내릴 때 `interrupt()` 를 받고도 안 멈추는 스레드가 있으면 서버가 안 내려간다.

앞의 자바 가상머신(JVM) 글에서 본 `jstack` 을 찍으면 그 스레드가 `RUNNABLE` 로 계속 돌고 있는 것이 보인다.

<br/>

오래 도는 반복문에는 플래그 확인을 넣어야 하는 것이다.

```java
while (!Thread.currentThread().isInterrupted()) {
    처리();
}
```

<br/>

`interrupt()` 는 협력을 전제로 한 설계다.

요청하는 쪽과 받는 쪽이 둘 다 규칙을 지켜야 동작한다.
