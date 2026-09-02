## blocking / non-blocking, sync / async 코드 구현

<br/>

## blocking 코드 구현

```java
public class test {
    public static void main(String[] args) {
        System.out.println("시작");

        // 블로킹 코드: 3초 동안 현재 스레드를 정지시킴
        try {
            System.out.println("b()함수의 작업이 끝날때까지 기다리는 중...");
            Thread.sleep(3000);
            System.out.println("b()함수 작업 종료");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 블로킹 코드 후의 실행
        System.out.println("a()함수로 제어권이 넘어와 a()함수 코드 처리");
    }
}

-- "출력값" --
시작
b()함수의 작업이 끝날때까지 기다리는 중...
// 1...
// 2...
// 3...
b()함수 작업 종료
a()함수로 제어권이 넘어와 a()함수 코드 처리
```

`Thread.sleep(3000)`: 실행되는 동안 아무것도 못하고 기다리고 있다.

<br/><br/>

## Non-blocking 코드 구현

```java
public class test {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("시작");

        // Non-blocking 코드: CompletableFuture를 사용하여 비동기적으로 작업을 수행
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            // 비동기적으로 수행될 작업
            System.out.println("b()함수는 작업을 수행");
        });

        // 다른 작업을 수행할 수 있음
        System.out.println("a()함수는 작업을 수행");

        // 메인 스레드를 블록하지 않고 계속 진행
        while (!future.isDone()) {
            System.out.println("a()함수는 계속 작업 중...");
            Thread.sleep(500); // a함수는 기다리는 동안 b함수는 일하고 있다.
        }
    }
}

-- "출력값" --
시작
a()함수는 작업을 수행
b()함수는 작업을 수행
a()함수는 계속 작업 중...
```

- `CompletableFuture.runAsync()`:  새로운 스레드에서 실행하며, 반환 값이 없는 비동기 작업을 수행한다.
    
- `future.isDone()`: CompletableFuture 객체가 완료 되었는지 여부를 확인하는 메서드이다.

<br/><br/>

## sync 코드 구현

`A()` 함수가 `B()` 함수를 호출 할 때, `B()` 함수의 결과를 `A()` 함수가 처리하는 것입니다.

```java
public class test {
    public static void main(String[] args) {
        System.out.println("시작");

        // 동기화된 코드: 메서드를 호출하여 동기적으로 작업을 수행
        synchronousMethod();

        System.out.println("a()함수 종료");
    }

    private static void synchronousMethod() {
        // 동기화된 메서드 내에서 작업 수행
        System.out.println("a()함수가 b()함수의 결과를 처리");
    }
}

-- "출력값" --
시작
a()함수가 b()함수의 결과를 처리
a()함수 종료
```

<br/><br/>

## async 코드 구현

`A()` 함수가 `B()` 함수를 호출 할 때, `B()` 함수의 결과를 `B()` 함수가 처리하는 것이다.

- `async`는 결과를 바로 처리하는 것이 아님

```java
public class test {
    public static void main(String[] args) {
        System.out.println("시작");

        // CompletableFuture를 사용하여 비동기 작업을 수행
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            // 비동기 작업 수행
            System.out.println("b()함수 비동기 작업을 실행");
        });

        // 다른 작업을 수행할 수 있음
        System.out.println("a()함수 다른 작업을 수행");

        // 비동기 작업이 완료될 때까지 기다리지 않음
        future.thenRun(() -> System.out.println("b()함수 비동기 작업 완료"));

        System.out.println("a()함수 작업 종료");
    }
}

-- "출력값" --
시작
a()함수 다른 작업을 수행
b()함수 비동기 작업을 실행
a()함수 작업 종료
b()함수 비동기 작업 완료

-- "또는" -- 
시작
a()함수 다른 작업을 수행
b()함수 비동기 작업을 실행
b()함수 비동기 작업 완료
a()함수 작업 종료
```

마지막 `"a()함수 작업 종료”` 와 `“b()함수 비동기 작업 완료”` 문구는 실행 시킬때마다 순서가 다르다. 

→ 이유는 async 이기 때문에 `B()` 함수 또한 

결과를 `B()` 함수가 처리하는 것이기에, 각자 처리하고 먼저 끝내는 쪽이 먼저 출력되는 느낌이다.

<br/>

`CompletableFuture.runAsync()`를 사용하여 비동기 작업을 수행합니다. 

- `thenRun()`: 사용하여 비동기 작업이 완료될 때 실행할 후속 작업을 등록할 수 있다

- `thenRun()`: 콜백(callback) 형태로 비동기 작업의 완료를 처리한다.

비동기 작업이 완료될 때까지 메인 스레드가 기다리지 않고, 다른 작업을 계속 수행할 수 있다.
<br/><br/>

## 궁금증!

```java
마지막 두 줄의 순서가 왜 실행할 때마다 바뀌나
```

원문의 `async 코드` 에서 `"a()함수 작업 종료"` 와 `"b()함수 비동기 작업 완료"` 의 순서가 매번 다르다고 적혀 있다.

<br/>

두 줄을 찍는 스레드가 다르기 때문이다.

```java
System.out.println("a()함수 작업 종료");                        // main 스레드
future.thenRun(() -> System.out.println("b()함수 비동기 작업 완료"));   // ForkJoinPool 스레드
```

<br/>

서로 다른 스레드가 각자 달리는데, 둘 중 누가 먼저 `println` 에 도달할지는 운영체제 스케줄러가 정한다.

앞의 pcb는 무엇인가요 글에서 본 `준비 -> 실행` 전이를 누가 먼저 받느냐다.

<br/>

어느 쪽이 먼저인지 알 수 없다는 것이 비동기의 정의이지, 버그가 아니다.

순서가 필요하면 그 순서를 코드로 적어야 한다.

```java
future.thenRun(() -> System.out.println("b 완료"))
      .thenRun(() -> System.out.println("그 다음에 a 마무리"));      // 이제 순서가 정해진다
```

<br/><br/>

## 원문 코드에 숨은 함정: 콜백이 안 찍힐 수 있다

`runAsync` 를 실행 풀을 안 정하고 부르면 기본 풀을 쓴다.

```java
CompletableFuture.runAsync(() -> { ... });      // ForkJoinPool.commonPool()
```

<br/>

이 풀의 스레드가 데몬 스레드인지 확인해봤다.

```java
ForkJoinPool.commonPool().getFactory().newThread(...).isDaemon()   // = true
```

<br/>

데몬 스레드는 `main` 이 끝나면 하던 일을 마치지 못하고 같이 죽는다.

<br/>

그래서 이런 코드는 마지막 줄이 안 찍힌다.

```java
CompletableFuture.runAsync(() -> {
    sleep(200);
    System.out.println("이 줄은 main 이 먼저 끝나면 안 찍힌다");
});
System.out.println("main 종료");
```

<br/>

### 결과

```java
[main] main 종료. 기본 ForkJoinPool 스레드 데몬 여부 = true
```

<br/>

`이 줄은 ... 안 찍힌다` 가 실제로 안 찍혔다.

<br/>

원문의 `async 코드` 가 두 가지 출력을 보인 이유 중 하나가 이것일 수 있다.

콜백이 늦으면 아예 안 나오는 경우도 있는 것이다.

<br/>

테스트 코드에서 이 문제가 자주 난다.

```java
@Test
void test() {
    service.sendAsync();          // 비동기로 메일 발송
    // 테스트가 여기서 끝난다 -> 메일 발송 스레드가 죽는다 -> 검증할 것이 없다
}
```

<br/>

앞의 옵저버 패턴 글에서 본 `@Async` 이벤트 리스너를 테스트할 때도 같은 함정이 있다.

`join()` 이나 `CountDownLatch` 로 끝나기를 기다려야 한다.

<br/><br/>

## Non-blocking 코드의 isDone 반복이 하는 일

원문의 `while (!future.isDone())` 은 앞의 sync(동기) vs async(비동기) 글에서 본 `Non-Blocking + Sync` 다.

```java
[main] isDone() 을 6 번 물어본 뒤 결과 = 결과
```

<br/>

안 멈추긴 하는데, 50ms마다 깨어나서 물어본다.

`sleep(500)` 을 빼면 CPU 하나를 100% 태우면서 물어보게 된다.

<br/>

이 방식을 폴링이라고 부르고, 실무에서는 잘 안 쓴다.

물어보는 간격이 길면 결과가 나와도 늦게 알고, 짧으면 CPU를 태운다.

<br/>

콜백이 나온 이유가 이것이다.

`언제 끝났는지 내가 물어보는` 대신 `끝나면 저쪽이 알려주는` 것으로 바꾼 것이다.

<br/><br/>

## sync 코드 구현이 조금 어긋난다

원문의 `sync 코드` 는 `synchronousMethod()` 를 그냥 부른 것이다.

```java
synchronousMethod();          // 이건 Blocking + Sync 다
```

<br/>

`Sync` 이기는 하다. 결과를 부른 쪽이 처리한다.

그런데 동시에 `Blocking` 이기도 하다. 그 메서드가 끝날 때까지 다음 줄로 못 간다.

<br/>

그래서 원문의 `blocking 코드` 와 `sync 코드` 는 사실 같은 조합이다.

`Thread.sleep` 이 있느냐 없느냐만 다르다.

<br/>

`Sync` 만 따로 보이는 예를 만들려면 `Non-Blocking + Sync` 여야 한다.

```java
Future<String> f = pool.submit(work);
while (!f.isDone()) { 다른일(); }      // Non-Blocking
String r = f.get();                    // 결과는 내가 처리 = Sync
```

<br/>

원문의 `Non-blocking 코드` 가 사실 이것이다. 두 예제가 같은 조합을 두 번 보여준 셈이다.

<br/><br/>

## 어느 스레드에서 콜백이 도는가

```java
.thenRun(...)        - 앞 작업을 끝낸 그 스레드에서 이어서 돈다 (보통)
.thenRunAsync(...)   - 풀에서 새 스레드를 받아 돈다
```

<br/>

`thenRun` 이 `main` 에서 돌 수도 있다. 앞 작업이 이미 끝나 있으면 `thenRun` 을 부른 스레드가 바로 실행하기 때문이다.

<br/>

그래서 콜백 안에서 스레드에 의존하는 코드를 쓰면 안 된다.

```java
.thenRun(() -> {
    ThreadLocal 값 읽기          // 어느 스레드인지 모르니 없을 수 있다
    @Transactional 기대          // 트랜잭션은 스레드에 묶여 있다. 다른 스레드면 없다
});
```

<br/>

앞의 멀티 쓰레드 글에서 본 `ThreadLocal 을 안 지우면 남의 값이 보인다` 와,

앞의 옵저버 패턴 글에서 본 `@Async 에서 지연 로딩하면 터진다` 가 전부 이 문제다.

비동기 콜백은 `다른 스레드일 수 있다` 를 전제로 써야 하는 것이다.

<br/><br/>

## 실무에서는 풀을 직접 정한다

```java
CompletableFuture.runAsync(task)                  // 기본 풀. 데몬. 크기는 코어-1
CompletableFuture.runAsync(task, myExecutor)      // 내 풀
```

<br/>

기본 풀을 쓰면 세 가지가 걸린다.

```java
1. 데몬이라 위에서 본 대로 조용히 사라진다
2. 크기가 코어 수 - 1 이라 I/O 작업을 넣으면 금방 막힌다
3. 앞의 스트림 글에서 본 병렬 스트림과 같은 풀이라 서로 방해한다
```

<br/>

스프링에서는 `@Async` 에 `Executor` 빈을 지정하는 것이 같은 이유다.

```java
@Bean
public Executor taskExecutor() {
    ThreadPoolTaskExecutor e = new ThreadPoolTaskExecutor();
    e.setCorePoolSize(10);
    e.setThreadNamePrefix("async-");        // 로그에서 어느 풀인지 보인다
    return e;
}
```

<br/>

이름을 붙여두면 위 실험처럼 `[일꾼]` 으로 찍혀서, 어느 스레드가 무엇을 했는지 로그로 따라갈 수 있다.
