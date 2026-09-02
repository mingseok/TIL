## ThreadLocal 내부 구조와 메모리 누수

<br/>

## ThreadLocal 이 하는 일

스레드마다 다른 값을 들고 있게 해준다.

```java
static final ThreadLocal<User> current = new ThreadLocal<>();

current.set(user);       // 이 스레드에만
current.get();           // 이 스레드가 넣은 것만
```

<br/>

같은 변수 이름인데 스레드 A가 넣은 것을 스레드 B는 못 본다.

앞의 싱글톤 패턴, 싱글톤 방식의 주의점 글에서 본 대로, 공유 필드 대신 스레드별 저장소를 쓰는 방법이다.

<br/><br/>

## 궁금증!

```java
값이 어디에 저장되나
```

`ThreadLocal` 객체 안에 `Map<Thread, T>` 가 있을 것 같은데, 반대다.

```java
Field f = Thread.class.getDeclaredField("threadLocals");
f.get(Thread.currentThread())
```

<br/>

### 결과

```java
Thread.threadLocals 필드 타입 = java.lang.ThreadLocal$ThreadLocalMap
```

<br/>

`Thread` 객체가 맵을 들고 있다.

```java
Thread
  └ threadLocals : ThreadLocalMap
       ├ [ThreadLocal A] -> 값
       ├ [ThreadLocal B] -> 값
```

<br/>

키가 `ThreadLocal` 객체이고 값이 내가 `set` 한 것이다.

<br/>

이렇게 뒤집어 놓은 이유가 있다.

`ThreadLocal` 안에 스레드별 맵을 두면, 스레드가 죽어도 그 항목이 남는다. 맵이 `ThreadLocal` 에 있으니까.

`Thread` 가 맵을 들고 있으면 스레드가 죽을 때 맵도 같이 사라진다.

<br/><br/>

## 그런데 스레드가 안 죽는 곳이 있다

스레드 풀이다.

앞의 스레드 풀 글에서 본 대로 풀의 스레드는 요청이 끝나도 죽지 않고 다음 요청을 기다린다.

<br/>

스레드 1개짜리 풀에 요청 3개를 보냈다. 각 요청은 50MB를 `set` 하고 `remove` 를 안 한다.

<br/>

### 결과

```java
요청 1: 이전 요청이 남긴 값 = 없음
요청 2: 이전 요청이 남긴 값 = 50MB 배열 (다른 요청의 것)
요청 3: 이전 요청이 남긴 값 = 50MB 배열 (다른 요청의 것)
3 요청 뒤 힙 사용량 = 52 MB   (50MB 하나가 스레드에 붙어 있어서 GC 가 못 지운다)
remove() 뒤 힙 사용량 = 1 MB
```

<br/>

두 가지 문제가 한 번에 보인다.

<br/>

## 문제 1 : 남의 값이 보인다

요청 2가 요청 1의 값을 봤다.

<br/>

여기에 로그인 사용자를 넣어뒀다면, 요청 2의 사용자가 요청 1 사용자의 권한으로 동작한다.

앞의 멀티 쓰레드 글에서 본 그 사고다.

<br/>

## 문제 2 : 메모리가 안 풀린다

요청이 끝났는데 50MB가 힙에 남아 있다.

<br/>

`Thread -> threadLocals -> 값` 으로 참조가 이어져 있고 `Thread` 는 살아 있으니, GC가 도달 가능하다고 본다.

앞의 가비지 컬렉션(G.C) 글에서 본 대로 도달 가능한 것은 안 지운다.

<br/>

톰캣 스레드 200개가 각자 이런 값을 들고 있으면 200배다.

<br/><br/>

## remove() 가 답이다

```java
try {
    current.set(user);
    처리();
} finally {
    current.remove();        // 이게 없으면 위 두 문제가 다 생긴다
}
```

<br/>

앞의 async 코드 구현 글에서 본 대로 `finally` 에 둬야 한다. 예외가 나도 지워져야 하니까.

<br/>

스프링은 이걸 필터나 인터셉터의 `afterCompletion` 에서 한다.

`SecurityContextHolder`, `RequestContextHolder`, `TransactionSynchronizationManager` 가 전부 `ThreadLocal` 이고, 요청이 끝날 때 스프링이 지워준다.

<br/>

내가 만든 `ThreadLocal` 은 내가 지워야 한다.

<br/><br/>

## 키가 약한 참조인 이유

`ThreadLocalMap` 의 키는 `WeakReference<ThreadLocal>` 이다.

```java
ThreadLocal 객체를 아무도 참조하지 않으면 -> 키가 GC 된다 -> 그 항목의 키가 null 이 된다
```

<br/>

그래서 `ThreadLocal` 변수를 버리면 키는 사라진다.

<br/>

그런데 값은 안 사라진다. 값은 강한 참조다.

키가 `null` 인 항목이 값만 들고 남는다. 이걸 `stale entry` 라고 한다.

<br/>

`ThreadLocalMap` 은 `set` 이나 `get` 을 할 때 지나가다 발견한 `stale entry` 를 지운다.

그런데 그 스레드가 `ThreadLocal` 을 다시 안 건드리면 영원히 남는다.

<br/>

결국 `remove()` 말고는 확실한 방법이 없다.

약한 참조는 `실수를 조금 덜 나쁘게` 만들어주는 것이지 해결책이 아니다.

<br/><br/>

## 가상 스레드에서는

앞의 가상 스레드 글에서 본 가상 스레드는 요청마다 새로 만들고 버린다.

스레드가 죽으니 `ThreadLocal` 도 같이 사라진다. 누수 문제가 사실상 없어진다.

<br/>

대신 다른 문제가 생긴다.

가상 스레드 100만 개가 각자 `ThreadLocal` 을 갖고 있으면 그것도 100만 개다.

풀에서는 200개면 됐던 캐시 객체가 100만 개가 되는 것이다.

<br/>

그래서 자바 21에 `ScopedValue` 가 들어왔다.

```java
ScopedValue.where(CURRENT_USER, user).run(() -> {
    처리();                    // 이 안에서만 보인다. 끝나면 자동으로 사라진다
});
```

<br/>

`set` 과 `remove` 를 짝으로 부르는 대신 범위로 묶는다.

`try-with-resources` 가 `close()` 를 자동으로 해주는 것과 같은 발상이다.

<br/><br/>

## 정리

```java
저장 위치   - Thread 가 맵을 들고 있다. 키 = ThreadLocal, 값 = 내 것
풀에서     - 스레드가 안 죽으니 값이 다음 요청까지 남는다. 남의 값 노출 + 메모리 누수
해법       - finally 에서 remove(). 약한 참조 키는 보조 장치일 뿐
가상 스레드 - 누수는 사라지지만 개수가 폭발. ScopedValue 로 간다
```
