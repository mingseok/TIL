## Block vs Non-Block / sync(동기) vs async(비동기)



### Block과 Non-Block

호출 받은 함수가 호출 했던 함수에게 제어권을 바로 주느냐 안주냐로 나뉠 수 있습니다

<br/>

## Blocking 설명

`Blocking` : A 함수가 B 함수를 호출 할 때, B 함수가 자신의 작업이 

종료되기 전까지 A 함수에게 제어권을 돌려주지 않는 것입니다.

- ex) 예시
    
    ```java
    상사에게 서류를 제출하였는데, 
    상사가 "다 볼때까지 기다리세요" 라고 말한다.
    그렇다면, 나는 다른일을 하지 못하고 기다리고 있어야 하는 것이다
    ```

<br/><br/>

## Non-Blocking 설명

A함수가 B함수를 호출 할때, B함수가 제어권을 바로 A함수에게 넘겨주면서, 

A함수가 다른 일을 할 수 있도록 하는 것입니다.

<br/><br/>

### sync(동기) vs async(비동기)

`Sync / Async` 는 호출한 함수와 호출 받은 함수 중에서 

누가 `return`을 처리하는가 의 차이 입니다!

<br/>

## sync(동기)란?

`A()` 함수가 `B()` 함수를 호출 할 때, `B()` 함수의 결과를 `A()` 함수가 처리하는 것입니다.

`Synchronous`은 `Return`을 기다리는 동안 머물 수도 아닐 수도 있습니다.

```java
Blocking / sync 조합은 자바에서 사용됩니다.
```

<br/><br/>

## async(비동기)란?

`A()` 함수가 `B()` 함수를 호출 할 때, `B()` 함수의 결과를 `B()` 함수가 처리하는 것입니다.

- `async`는 결과를 바로 처리하는 것이 아닙니다.

즉, 작업이 끝난 결과를 바로 처리하지 않고 자신의 일이 

끝나게 되면 그때서야 처리를 하는 느낌입니다.

```java
Non-Blocking / Async 조합은 자바스크립트에서 사용됩니다.
```





<br/><br/>

## Blocking / sync **설명, NonBlocking / Async 설명**

![이미지](/programming/img/입문446.PNG)

출처 : https://homoefficio.github.io/2017/02/19/Blocking-NonBlocking-Synchronous-Asynchronous/

<br/>

### Blocking / sync 설명

`Blocking`의 관점은 제어권에 있고, 다른 작업이 진행되는 동안 자신의 작업을 처리하지 않습니다.

- 대표적인 예시)

    - `Scanner`를 통해 입력을 받는 동안은 제어권이 넘어갔기 때문에 `Blocking`이고,
        
        그 결과를 `리턴`받아서 다음의 작업을 바로 처리하고 있기 때문에 Sync 개념입니다.


<br/>

### NonBlocking / Async 설명

`Non-Blocking`은 다른 작업이 진행되는 동안에도 자신의 작업을 처리합니다. 

그리고 `Async`이기 때문에 다른 작업의 결과 역시도 바로 처리하지 않아도 됩니다.

- 대표적인 예시로 `자바스크립트`에서 API 요청을 하고, 다른 작업을 하다가
    
    콜백을 통해 추가적인 작업을 처리할 때 사용하는 것을 들 수 있습니다.
    

<br/><br/>

## Blocking / a**sync 설명**

![이미지](/programming/img/입문447.PNG)

출처 : https://homoefficio.github.io/2017/02/19/Blocking-NonBlocking-Synchronous-Asynchronous/

<br/>

`Blocking`은 다른 작업이 진행되는 동안 자신의 작업을 처리하지 않습니다. 

그리고 `Async`는 결과를 바로 처리하지 않아도 되는 것입니다.

- 가장 비효율적인 방법입니다.

    - 개발자의 실수나 기타 이유로 이렇게 동작할 수도 있다고 합니다.

<br/><br/>

## Non-Blocking / sync 설명

![이미지](/programming/img/입문448.PNG)

출처 : https://homoefficio.github.io/2017/02/19/Blocking-NonBlocking-Synchronous-Asynchronous/

`Non-Blocking`은 다른 작업이 있어도 자신의 작업을 처리하는 개념입니다.

`Sync`는 그 결과를 리턴 받았을 때 바로 그 결과에 집중하는 개념이었습니다.

<br/>

`B()` 함수가 바로 제어권을 돌려주기에 `A()` 함수는 다른 작업을 수행할 수 있지만,

언제 종료되는지 알 수 없는 `B()` 함수의 종료를 `A()` 함수가 처리해야 합니다.

<br/>

`A()` 함수가 직접 결과를 처리해야 하는 상황이기에 

`B()` 함수의 종료를 반복적으로 물어봐야 하는 것입니다.
<br/><br/>

## 궁금증!

```java
네 가지 조합을 자바로 전부 만들어봤다
```

300ms 걸리는 일을 다른 스레드에게 맡기고, 결과를 받는 방식을 네 가지로 바꿨다.

어느 스레드가 무엇을 하는지 보려고 스레드 이름을 같이 찍었다.

<br/>

### 1. Blocking + Sync

```java
Future<String> f = pool.submit(Async::slowWork);
String r = f.get();                              // 여기서 멈춘다
```

```java
[main] 결과 받음 = 결과, 기다린 시간 = 307 ms
```

<br/>

`get()` 에서 300ms 동안 멈춰 있었다. 제어권이 안 돌아온 것이다.

그리고 결과는 `main` 이 받아서 처리한다. 그래서 `Sync` 다.

<br/>

### 2. Non-Blocking + Sync

```java
Future<String> f = pool.submit(Async::slowWork);
while (!f.isDone()) { 다른일(); sleep(50); }     // 안 멈추고 계속 물어본다
String r = f.get();
```

```java
[main] isDone() 을 6 번 물어본 뒤 결과 = 결과
```

<br/>

`main` 은 멈추지 않았다. 50ms마다 다른 일을 하면서 6번 물어봤다.

결과는 여전히 `main` 이 처리한다.

<br/>

원문의 `B() 함수의 종료를 A() 함수가 반복적으로 물어봐야 한다` 가 이 `isDone()` 반복이다.

<br/>

### 3. Non-Blocking + Async

```java
CompletableFuture.supplyAsync(Async::slowWork, pool)
        .thenAccept(r -> 결과처리(r));          // 결과는 저쪽이 처리
System.out.println("바로 다음 줄");
```

```java
[main] 콜백 등록만 하고 바로 다음 줄로 왔다
  [일꾼] 콜백에서 결과 처리 = 결과     <- main 이 아니다
```

<br/>

콜백을 처리한 스레드가 `일꾼` 이다. `main` 은 결과를 만진 적이 없다.

이게 `Async` 의 정의다. 원문의 `B() 함수의 결과를 B() 함수가 처리한다` 가 정확히 이것이다.

<br/>

### 4. Blocking + Async

```java
CompletableFuture.supplyAsync(Async::slowWork, pool)
        .thenAccept(r -> 결과처리(r))
        .join();                                 // 콜백에 맡겼는데 기다린다
```

```java
  [일꾼] 콜백 처리 = 결과
[main] 기다린 시간 = 307 ms     <- 콜백을 쓴 이득이 없다
```

<br/>

결과는 `일꾼` 이 처리했는데 `main` 은 그동안 멈춰 있었다.

원문의 `가장 비효율적인 방법` 이다. 콜백을 등록해놓고 그 자리에서 기다리면 비동기를 쓴 의미가 없어진다.

<br/>

실무에서 이 실수가 흔하다.

```java
CompletableFuture.runAsync(...).join();          // 비동기처럼 보이지만 동기다
```

<br/><br/>

## 두 축이 정말 독립적인가

네 조합이 다 나왔으니 독립적이다.

```java
Blocking/Non-Blocking - "부른 쪽이 기다리는가"     -> 제어권 얘기
Sync/Async            - "결과를 누가 처리하는가"    -> 결과 처리 주체 얘기
```

<br/>

헷갈리는 이유는 실무에서 대각선 두 조합만 주로 보기 때문이다.

```java
Blocking + Sync       - 평범한 메서드 호출. 자바 코드 대부분
Non-Blocking + Async  - 콜백, 이벤트 루프. 자바스크립트
```

<br/>

나머지 두 조합은 있긴 한데 잘 안 쓴다.

`Non-Blocking + Sync` 는 계속 물어보느라 CPU를 태우고, `Blocking + Async` 는 위에서 본 대로 이득이 없다.

<br/><br/>

## Blocking 이 정확히 무엇을 하는가

앞의 pcb는 무엇인가요 글에서 본 상태 전이가 이것이다.

```java
f.get()  ->  스레드가 "봉쇄" 상태로 들어간다  ->  CPU 를 내놓는다
```

<br/>

멈춰 있는 동안 CPU를 쓰지 않는다. 운영체제가 다른 스레드에게 준다.

<br/>

그래서 `Blocking` 이 항상 나쁜 것은 아니다.

스레드가 많으면, 하나가 멈춰도 다른 것이 돈다.

앞의 멀티 쓰레드 글에서 본 톰캣 방식이 `Blocking + Sync` 를 스레드 200개로 감당하는 것이다.

<br/>

`Non-Blocking` 이 필요해지는 것은 스레드가 적을 때다.

스레드가 하나뿐인 자바스크립트가 `Blocking` 을 쓰면 화면이 멈춘다.

앞의 이벤트 글에서 본 `for 문 10억 번 돌리면 클릭이 안 된다` 가 그것이다.

<br/><br/>

## Sync 가 나쁜 것도 아니다

`Sync` 는 결과를 부른 쪽이 처리한다. 그래서 코드가 위에서 아래로 읽힌다.

```java
String r = f.get();
save(r);
notify(r);
```

<br/>

`Async` 는 결과 처리가 콜백 안으로 들어간다.

```java
f.thenAccept(r -> {
    save(r);
    notify(r);
});
System.out.println("이 줄이 먼저 실행된다");     // 순서가 코드 순서와 다르다
```

<br/>

콜백이 겹치기 시작하면 읽기가 아주 어려워진다.

<br/>

그래서 요즘은 `Non-Blocking` 의 이득은 챙기고 `Sync` 처럼 읽히게 하려는 방향으로 간다.

```java
자바스크립트 - async/await
코틀린      - 코루틴
자바 21     - 가상 스레드
```

<br/>

자바 21의 가상 스레드는 코드를 `Blocking + Sync` 로 그냥 쓰는데, JVM이 안에서 `Non-Blocking` 으로 바꿔준다.

`f.get()` 에서 멈추면 운영체제 스레드를 내놓고 다른 가상 스레드를 돌린다.

<br/>

앞의 multi thread VS multi process 글에서 본 그 얘기다.

네 조합 중 가장 읽기 쉬운 것을 쓰면서 가장 효율적인 것의 성능을 얻으려는 시도인 셈이다.
