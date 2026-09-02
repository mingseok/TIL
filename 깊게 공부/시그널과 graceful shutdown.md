## 시그널과 graceful shutdown

<br/>

## 시그널이란

운영체제가 프로세스에게 보내는 짧은 알림이다. 번호로 구분한다.

```java
SIGTERM (15) - 종료해 달라. 프로세스가 받아서 처리할 수 있다
SIGKILL (9)  - 즉시 죽인다. 프로세스에게 전달되지 않고 커널이 바로 죽인다
SIGINT  (2)  - Ctrl+C
SIGHUP  (1)  - 터미널이 끊겼다. 설정 다시 읽기 용도로도 쓴다
```

<br/>

`kill` 명령의 기본이 `SIGTERM` 이다. `kill -9` 가 `SIGKILL` 이다.

<br/><br/>

## 궁금증!

```java
kill -15 와 kill -9 에서 자바의 shutdown hook 이 도나
```

훅을 등록하고 60초 자는 프로그램을 띄운 뒤 시그널을 보냈다.

```java
Runtime.getRuntime().addShutdownHook(new Thread(() ->
    System.out.println("[hook] 정리 작업 실행됨: 커넥션 닫기, 큐 비우기")));
```

<br/>

### 결과

```java
kill -15 (SIGTERM):
  [main] 시작. pid=74460
  [hook] 정리 작업 실행됨: 커넥션 닫기, 큐 비우기

kill -9 (SIGKILL):
  [main] 시작. pid=74464
                                          <- 아무것도 없다
```

<br/>

`SIGTERM` 은 훅이 돌았고 `SIGKILL` 은 안 돌았다.

<br/>

`SIGKILL` 은 프로세스가 받는 것이 아니다. 커널이 프로세스를 그냥 지운다.

훅이 돌 기회 자체가 없다. 앞의 좀비 프로세스와 고아 프로세스 글에서 본 대로 PCB만 남고 끝이다.

<br/><br/>

## graceful shutdown

`SIGTERM` 을 받고 정리한 뒤에 끝나는 것을 말한다.

```java
1. 새 요청을 안 받는다
2. 처리 중인 요청은 끝낸다
3. 커넥션 풀, 스레드 풀을 닫는다
4. 종료한다
```

<br/>

이게 안 되면 배포할 때마다 요청이 잘린다.

사용자가 결제 버튼을 눌렀는데 서버가 그 순간 죽어서 응답을 못 받는 것이다.

<br/>

스프링 부트는 설정 한 줄로 켠다.

```java
server.shutdown=graceful
spring.lifecycle.timeout-per-shutdown-phase=30s
```

<br/>

`SIGTERM` 을 받으면 톰캣이 새 접속을 거부하고, 처리 중인 것을 최대 30초까지 기다린다.

30초 안에 안 끝나면 그때 강제로 끝낸다.

<br/><br/>

## 쿠버네티스가 하는 순서

```java
1. Pod 를 종료 대상으로 표시. 서비스에서 빼서 새 트래픽이 안 온다
2. SIGTERM 을 보낸다
3. terminationGracePeriodSeconds (기본 30초) 기다린다
4. 아직 살아 있으면 SIGKILL
```

<br/>

`3번` 의 30초와 스프링의 `timeout-per-shutdown-phase` 를 맞춰야 한다.

스프링이 60초 기다리게 해놨는데 쿠버네티스가 30초에 `SIGKILL` 을 보내면 소용없다.

<br/>

앞의 WAS 서버와 WEB 서버의 이해 글에서 본 블루그린 배포도 같은 순서다.

로드밸런서에서 먼저 빼고, 그 다음 서버를 내린다.

<br/><br/>

## PID 1 문제

컨테이너에서 `java` 가 PID 1이면 `SIGTERM` 이 이상하게 동작할 수 있다.

<br/>

리눅스는 PID 1에게 기본 시그널 핸들러를 적용하지 않는다.

PID 1이 `SIGTERM` 을 명시적으로 처리하지 않으면 그냥 무시된다.

<br/>

JVM은 `SIGTERM` 핸들러를 등록하니 대개 괜찮은데, 셸 스크립트로 감싸면 문제가 된다.

```java
CMD ["sh", "-c", "java -jar app.jar"]     # sh 가 PID 1. sh 는 SIGTERM 을 java 에게 안 넘긴다
```

<br/>

`sh` 가 시그널을 받고 자식에게 전달하지 않아서, `java` 는 `SIGTERM` 을 못 받고 30초 뒤 `SIGKILL` 로 죽는다.

훅이 안 도는 것이다.

<br/>

```java
CMD ["java", "-jar", "app.jar"]           # exec 형식. java 가 직접 PID 1
ENTRYPOINT ["tini", "--", "java", ...]    # 또는 진짜 init 을 앞에
```

<br/>

앞의 좀비 프로세스와 고아 프로세스 글에서 본 `tini` 가 여기서도 답이다.

<br/><br/>

## 훅 안에서 하면 안 되는 것

```java
addShutdownHook(new Thread(() -> {
    Thread.sleep(60_000);            // 너무 오래. SIGKILL 이 먼저 온다
    System.exit(0);                  // 훅 안에서 exit 을 부르면 교착
    log.info(...);                   // 로거가 이미 닫혔을 수 있다
}));
```

<br/>

훅은 짧아야 한다. 그리고 훅 실행 순서는 보장되지 않는다.

여러 훅이 등록되어 있으면 동시에 돈다.

<br/>

앞의 interrupt()는 무엇을 하나요 글에서 본 대로,

스프링이 종료할 때 스레드 풀에 `shutdownNow()` 를 부르고 그것이 `interrupt()` 를 건다.

작업이 `interrupt()` 에 반응하지 않으면 30초를 다 채우고 `SIGKILL` 로 끝난다.

<br/><br/>

## 정리

```java
SIGTERM  - 정리할 기회를 준다. 훅이 돈다
SIGKILL  - 기회 없음. 훅 안 돈다. 최후의 수단
graceful - 새 요청 거부 -> 진행 중인 것 완료 -> 자원 정리 -> 종료
주의     - 쿠버네티스 유예 시간과 스프링 타임아웃을 맞춘다. PID 1 이 시그널을 받는지 확인한다
```
