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