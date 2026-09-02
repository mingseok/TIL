## Reentrant Lock과 synchronized의 차이



둘다 `JVM`환경에서 쓰레드에 `Lock`걸어야 할때의 방법이다.

<br/><br/>

## Synchronized

```java
public synchronized void method(){
}
```

현재 데이터를 사용하고 있는 해당 스레드를 제외하고, 

나머지 스레드들은 메서드에 접근 시 블록 전체에 `lock`을 걸어 막는 개념이다.

- 자바에서 스레드 동기화 시 사용하는 대표적인 기법(?) → “이거 맞나?”

- `Synchronized` 키워드를 너무 남발하면 오히려 프로그램 성능저하를 일으킬 수 있다.

<br/>

### `Syncronized`를 사용할 경우

나의 스레드가 

- 현재 멈추어 있는지?

- 기아 상태(=무한히 진행하지 못하는 상태)에 빠진 건지?
- `Locking`을 들고 있는 건지?

등등의 정보를 얻을 수 없다.

<br/><br/>

## 하지만, **ReentrantLock 가능하다.**

```java
private final ReentrantLock lock = new ReentrantLock();
```

`Syncronized`는 `Lock`이 걸리면 `Release`까지 무한하게 기다릴 수 밖에 없으나, 

`ReentrantLock`을 사용하면 현재 상태를 확인할 수 있다.

<br/><br/>


## 임계영역 범위는?

```java
"임계영역" 뜻은?
-> 동시에 여러 스레드가 공유 데이터에 접근하려는 상황을 관리하고 제어하는 영역이다.
```

### **synchronized**

- 메서드 안에 임계 영역의 시작과 끝이 존재해야 한다.

### **ReentrantLock**

- `lock()`, `unlock()`으로 시작과 끝을 명시하기 때문에 여러 메서드에 나눠서 작성할 수 있다.

<br/><br/>

## 경쟁 상태는?

```java
"경쟁상태" 뜻은?
-> '경쟁 상태' 또는 '경쟁 조건'은 다중 스레드나 다중 프로세스 환경에서 
    여러 실행 단위가 데이터에 동시 접근하고 수정하려고
    시도할 때 발생하는 문제를 가리킵니다.
```

### **synchronized**

- 스레드 진입권 획득 순서 보장 하지 않는다.

### **ReentrantLock**

- 메서드를 호출함으로써 어떤 스레드가 먼저 락을 획득하게 될지 순서를 지정할 수 있다.


<br/>
<br/>


## Synchronized와 ReentrantLock의 차이점

- `Synchronized`는 어느 스레드가 먼저 `lock`을 획득할지
    
    보장하지 못하나, `ReentrantLock`락은 가능하다
    
- `Synchronized`는 임계 영역의 시작과 끝이 있어야 하나,
    
    `ReentrantLock`는 `lock`, `unlock`을 사용해 여러 메서드에 나눠 작성이 가능하다
    
    - 현실적으로 `rock`, `unlock`을 여러 메서드에서 쓰는 경우는 거의 없다고 한다.
    
- 공정성
    - `lock()` 사용 시 경쟁이 발생하면 가장 오래 기다린 스레드에게 `lock`을 제공하는 것

    - `Synchronized`는 공정성 지원하지 않지만 `ReentrantLock`는 지원함
<br/><br/>

## 궁금증!

```java
원문에 적힌 차이들이 실제로 어떻게 보이는지 하나씩 돌려봤다
```

<br/>

## 1. tryLock : 못 잡으면 바로 돌아온다

```java
Thread holder = new Thread(() -> { lock.lock(); sleep(1000); lock.unlock(); });
holder.start();

lock.tryLock();                                // 다른 스레드가 잡고 있는 상태에서
lock.tryLock(200, TimeUnit.MILLISECONDS);
```

<br/>

### 결과

```java
다른 스레드가 잡고 있을 때 tryLock() = false
tryLock(200ms) = false                        <- 200ms 만 기다리고 돌아온다
lock.isLocked() = true, getQueueLength() = 0
```

<br/>

`synchronized` 는 이게 불가능하다. 들어가려면 잡힐 때까지 기다리는 것 말고 방법이 없다.

<br/>

`isLocked()`, `getQueueLength()` 로 지금 상태를 볼 수 있다는 것도 확인됐다.

원문의 `synchronized 는 정보를 얻을 수 없다` 가 이 얘기다.

<br/>

실무에서 `tryLock` 이 쓰이는 자리는 이런 곳이다.

```java
if (!lock.tryLock(3, TimeUnit.SECONDS)) {
    throw new ServiceException(ErrorCode.TOO_BUSY);     // 3초 기다려보고 안 되면 포기
}
```

<br/>

영원히 기다리는 대신 `지금은 바빠요` 를 돌려줄 수 있는 것이다.

<br/><br/>

## 2. 기다리는 중에 취소할 수 있나

```java
// synchronized 를 기다리는 스레드에게 interrupt()
waiter.interrupt();
```

```java
synchronized 대기 중 interrupt() -> 500ms 뒤에도 살아 있나: true
```

<br/>

인터럽트가 안 먹는다. 락이 풀릴 때까지 그냥 기다린다.

<br/>

```java
// lockInterruptibly() 로 기다리는 스레드에게 interrupt()
```

```java
lockInterruptibly 대기 중 interrupt() -> InterruptedException 으로 즉시 빠져나옴
```

<br/>

`ReentrantLock` 은 기다리다가 빠져나올 수 있다.

<br/>

서버를 내릴 때 이 차이가 드러난다.

`synchronized` 에서 기다리는 스레드는 종료 신호를 못 받아서 서버가 안 내려간다.

앞의 자바 가상머신(JVM) 글에서 본 `jstack` 을 찍어보면 `BLOCKED` 상태로 박혀 있는 것이 보이는 상황이다.

<br/><br/>

## 3. 공정성 : 누가 먼저 받나

10개 스레드가 각자 5번씩 락을 잡게 하고, 잡은 순서를 기록했다.

```java
비공정 ReentrantLock 순서: [0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, ...]
공정   ReentrantLock 순서: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, ...]
```

<br/>

비공정 락은 스레드 0이 다섯 번을 연달아 잡고 나서야 1에게 넘겼다.

락을 놓은 스레드가 바로 다시 잡으면, 줄 서 있던 스레드를 깨우는 것보다 빠르기 때문이다.

<br/>

공정 락은 정확히 돌아가면서 잡는다.

<br/>

`synchronized` 는 비공정이다. 그리고 `new ReentrantLock()` 의 기본값도 비공정이다.

```java
new ReentrantLock()        // 비공정. synchronized 와 같다
new ReentrantLock(true)    // 공정
```

<br/>

공정 락은 앞의 context switch vs Context 글에서 본 스위치를 매번 일으킨다.

그래서 처리량이 눈에 띄게 떨어진다. 순서가 정말 중요할 때만 쓰는 것이다.

<br/><br/>

## 4. 재진입 : 같은 스레드가 두 번 잡을 수 있나

```java
lock.lock();
lock.lock();                  // 같은 스레드가 다시
lock.getHoldCount()           // = 2
```

<br/>

`Reentrant` 라는 이름이 이 뜻이다. 다시 들어갈 수 있다.

<br/>

`synchronized` 도 재진입이 된다.

```java
public synchronized void a() { b(); }
public synchronized void b() { }       // a() 안에서 b() 를 불러도 막히지 않는다
```

<br/>

이게 안 되면 자기가 잡은 락을 자기가 기다리는 교착에 빠진다.

앞의 뮤텍스와 세마포어의 차이 글에서 본 대로, 세마포어는 재진입이 안 되어서 실제로 이 일이 생긴다.

<br/><br/>

## 5. 조건 변수를 여러 개 둘 수 있다

```java
Condition notEmpty = lock.newCondition();
Condition notFull  = lock.newCondition();
```

<br/>

`synchronized` 는 `wait()` / `notify()` 가 락 하나에 하나뿐이다.

큐가 비었을 때 기다리는 소비자와, 큐가 찼을 때 기다리는 생산자를 한 대기실에 넣어야 한다.

`notifyAll()` 로 전부 깨우고 각자 조건을 다시 확인하는 식이다.

<br/>

`ReentrantLock` 은 대기실을 나눌 수 있어서 필요한 쪽만 깨운다.

`ArrayBlockingQueue` 가 실제로 이렇게 구현되어 있다.

<br/><br/>

## 그래서 무엇을 쓰나

```java
기본은 synchronized
```

<br/>

이유가 있다.

```java
unlock() 을 빼먹을 수가 없다      - 블록이 끝나면 자동으로 풀린다
코드가 짧다
JVM 이 최적화한다                - 편향 락, 경량 락
```

<br/>

`ReentrantLock` 은 `finally` 에서 `unlock()` 을 반드시 불러야 한다.

```java
lock.lock();
try {
    ...
} finally {
    lock.unlock();          // 빼먹으면 영원히 잠긴다
}
```

<br/>

예외가 나도 풀리게 하려면 이 모양이 강제된다.

<br/>

그러니 `ReentrantLock` 은 위의 네 가지가 실제로 필요할 때만 꺼내면 된다.

```java
기다리다 포기해야 한다      -> tryLock
기다리다 취소해야 한다      -> lockInterruptibly
순서를 보장해야 한다        -> new ReentrantLock(true)
조건별로 따로 깨워야 한다   -> Condition
```

<br/>

원문의 `현실적으로 lock, unlock 을 여러 메서드에서 쓰는 경우는 거의 없다` 는 말이 맞다.

락을 잡는 곳과 푸는 곳이 떨어져 있으면 읽는 사람이 짝을 찾아다녀야 하고, 하나만 빠져도 서버가 멈춘다.
