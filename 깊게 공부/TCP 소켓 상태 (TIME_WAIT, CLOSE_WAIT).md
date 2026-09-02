## TCP 소켓 상태 (TIME_WAIT, CLOSE_WAIT)

<br/>

## 소켓은 상태를 가진다

앞의 TCP 3 way handshake 글에서 연결이 맺어지는 과정을 봤다.

연결이 끊어지는 과정도 있다. 4-way handshake라고 부른다.

```java
먼저 닫는 쪽 : FIN 보냄  -> FIN_WAIT_1 -> (ACK 받음) -> FIN_WAIT_2 -> (FIN 받음) -> TIME_WAIT -> 소멸
나중 닫는 쪽 : FIN 받음  -> CLOSE_WAIT -> (자기도 close) -> LAST_ACK -> (ACK 받음) -> 소멸
```

<br/>

이 중 실무에서 마주치는 것이 `TIME_WAIT` 와 `CLOSE_WAIT` 다.

<br/><br/>

## 궁금증!

```java
누가 먼저 닫느냐에 따라 어디에 무엇이 남나
```

같은 JVM 안에 서버와 클라이언트를 두고 접속 300개를 맺은 뒤, 한쪽씩 닫으면서 `netstat` 을 찍었다.

<br/>

### 접속 유지 중

```java
ESTABLISHED = 600        <- 300 접속 × 양쪽 끝
```

<br/>

### 1. 클라이언트가 먼저 close()

```java
CLOSE_WAIT = 300         <- 서버 쪽 소켓. 상대 FIN 은 받았는데 나는 아직 close 안 함
TIME_WAIT  = 0
```

<br/>

서버 쪽에 `CLOSE_WAIT` 300개가 생겼다.

클라이언트 쪽은 `FIN_WAIT_2` 상태다. 상대의 FIN을 기다리고 있다.

<br/>

### 2. 서버도 자기 소켓을 close()

```java
CLOSE_WAIT = 0           <- 사라졌다
TIME_WAIT  = 300         <- 클라이언트 쪽. 먼저 닫은 쪽에 남는다
```

<br/>

서버가 닫자 `CLOSE_WAIT` 는 사라지고 `TIME_WAIT` 300개가 생겼다.

<br/><br/>

## CLOSE_WAIT 는 내 잘못이다

`CLOSE_WAIT` 는 `상대는 닫았는데 내가 안 닫은` 상태다.

<br/>

커널이 알아서 정리하지 않는다. 내 프로그램이 `close()` 를 불러야 사라진다.

<br/>

그래서 서버에 `CLOSE_WAIT` 가 쌓이면 코드 버그다.

```java
응답을 보낸 뒤 소켓을 안 닫았다
예외가 나서 close() 를 건너뛰었다
HTTP 클라이언트의 응답 body 를 안 읽고 버렸다 (커넥션이 풀로 안 돌아감)
```

<br/>

앞의 파일 디스크립터 한계 글에서 본 대로 소켓 하나가 FD 하나라, `CLOSE_WAIT` 가 쌓이면 `Too many open files` 로 간다.

<br/>

```java
$ netstat -an | grep CLOSE_WAIT | wc -l      # 이게 계속 늘면 어딘가 close 를 빠뜨린 것
```

<br/><br/>

## TIME_WAIT 는 정상이다

`TIME_WAIT` 는 먼저 닫은 쪽이 `2 × MSL` 동안 소켓을 붙잡아두는 상태다. 보통 30~60초다.

<br/>

이유가 있다.

```java
1. 마지막 ACK 가 유실되면 상대가 FIN 을 다시 보낸다. 그걸 받아서 다시 ACK 하려고
2. 같은 포트로 새 연결이 바로 맺어지면, 늦게 도착한 옛 패킷이 새 연결에 섞일 수 있다. 그걸 막으려고
```

<br/>

커널이 정해진 시간 뒤에 알아서 지운다. 프로그램이 할 일은 없다.

<br/><br/>

## 그런데 TIME_WAIT 가 문제가 되는 경우

앞의 파일 디스크립터 한계 글의 실험에서 겪었다.

```java
소켓 1만6천 개를 열고 닫은 직후
$ netstat -an | grep -c TIME_WAIT
32699

다음 실험 : Can't assign requested address
```

<br/>

클라이언트가 접속할 때마다 임시 포트를 하나 쓴다. 범위가 약 1만6천~2만8천 개다.

닫은 포트가 `TIME_WAIT` 로 30초 묶여 있으니, 30초 안에 그 이상 접속하면 포트가 없다.

<br/>

서버가 다른 서버를 부르는 쪽(클라이언트 역할)에서 이 문제가 생긴다.

```java
API 서버 -> 외부 API 를 초당 1000번 부르고 매번 새 연결
-> 30초에 3만 개 TIME_WAIT -> 포트 고갈
```

<br/>

해법은 연결을 재사용하는 것이다.

```java
HTTP Keep-Alive / 커넥션 풀   - 연결을 안 끊으니 TIME_WAIT 가 안 생긴다
```

<br/>

앞의 커넥션 풀 글에서 본 DB 커넥션 풀과 같은 이유로 HTTP 클라이언트도 풀을 쓰는 것이다.

<br/><br/>

## 서버가 먼저 닫으면 서버에 TIME_WAIT 가 쌓인다

HTTP/1.0 시절 서버가 응답 후 연결을 끊었다. 그러면 서버 쪽에 `TIME_WAIT` 가 쌓인다.

<br/>

서버는 포트가 하나(80)라 포트 고갈은 없지만, 소켓 구조체가 커널 메모리를 먹는다.

초당 수만 접속이면 수십만 개가 30초씩 남는다.

<br/>

그래서 `누가 먼저 닫을 것인가` 도 설계다.

```java
클라이언트가 닫게 한다   - TIME_WAIT 가 클라이언트에 분산된다
Keep-Alive 로 안 닫는다  - 제일 좋다
```

<br/>

앞의 HTTP 메시지 글에서 본 `Connection: keep-alive` 헤더가 이 문제를 풀려고 나온 것이다.

<br/><br/>

## 진단 명령

```java
$ netstat -an -p tcp | awk '{print $6}' | sort | uniq -c      # 상태별 개수
$ ss -s                                                        # 리눅스. 요약
$ lsof -i :8080                                                # 특정 포트를 쥔 프로세스
```

<br/>

```java
CLOSE_WAIT 많다  -> 내 코드가 close 를 안 한다. 버그
TIME_WAIT 많다   -> 연결을 너무 자주 맺고 끊는다. 풀을 쓴다
SYN_RECV 많다    -> 접속 요청이 몰리거나 SYN flood 공격
```

<br/>

앞의 pcb는 무엇인가요 글에서 프로세스 상태 분포를 봤듯이,

소켓 상태 분포도 서버가 무엇을 하고 있는지 말해준다.
