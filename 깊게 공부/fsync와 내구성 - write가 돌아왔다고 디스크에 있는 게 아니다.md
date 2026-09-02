## fsync와 내구성 - write가 돌아왔다고 디스크에 있는 게 아니다

<br/>

## write() 는 어디까지 쓰나

```java
channel.write(buffer);       // 돌아왔다
```

<br/>

돌아왔으니 디스크에 있을 것 같다. 없다.

앞의 페이지 캐시 글에서 본 대로 커널 메모리에 적어놓고 돌아온 것이다.

<br/>

디스크에 실제로 내려가는 것은 나중이다. 커널이 몇 초마다 모아서 내린다.

<br/>

그 사이 전원이 나가면 사라진다. `write()` 가 성공했는데 데이터가 없는 것이다.

<br/><br/>

## fsync

`지금 당장 디스크에 내려라` 는 시스템 콜이다.

```java
channel.force(false);        // 자바에서는 이것. fsync 를 부른다
```

<br/>

돌아오면 디스크에 있다. 전원이 나가도 남는다.

<br/><br/>

## 궁금증!

```java
얼마나 비싼가
```

로그 2000줄을 세 방식으로 썼다.

<br/>

### 결과

```java
write() 만                   =    8 ms    <- 페이지 캐시에 넣고 끝
write() + force() 매번        = 8536 ms    <- 매번 디스크까지. 1000 배
write() 2000번 + force() 1번  =   11 ms    <- 모아서 한 번
```

<br/>

매번 `fsync` 하면 1000배 느리다. 한 줄에 4ms다.

<br/>

SSD인데도 그렇다. 디스크 컨트롤러가 `정말 썼다` 고 확인해주기까지의 왕복이 이만큼이다.

HDD면 회전을 기다려야 해서 더 든다.

<br/>

모아서 한 번 하면 11ms다. `write()` 만 한 것과 거의 같다.

<br/><br/>

## 이게 DB 의 COMMIT 이다

```java
INSERT, UPDATE  -> 로그(WAL, redo log)에 적는다. 페이지 캐시
COMMIT          -> 그 로그를 fsync 한다
```

<br/>

`COMMIT` 이 돌아왔으면 디스크에 있다. 전원이 나가도 복구된다. 이게 ACID의 D(내구성)다.

<br/>

앞의 transaction 설명 글에서 본 그 D가 구현되는 방식이 `fsync` 인 것이다.

<br/>

그래서 `COMMIT` 이 느리다. 한 번에 4ms면 초당 250 트랜잭션이 상한이다.

<br/>

DB는 이걸 `group commit` 으로 푼다.

```java
같은 순간에 COMMIT 을 기다리는 트랜잭션 여러 개를 모아서 fsync 한 번
```

<br/>

위 실험의 세 번째 방식이다. 동시 트랜잭션이 많을수록 한 번의 `fsync` 로 더 많이 처리한다.

<br/><br/>

## 속도와 내구성을 설정으로 고른다

```java
MySQL innodb_flush_log_at_trx_commit
  1 - COMMIT 마다 fsync. 기본값. 안전
  2 - 1초마다 fsync. 전원 나가면 최대 1초 유실
  0 - 더 느슨

Redis appendfsync
  always   - 매 명령마다
  everysec - 1초마다. 기본값
  no       - 커널에 맡김

Kafka
  acks=all + min.insync.replicas 로 복제본 수로 내구성을 확보. fsync 는 주기적으로
```

<br/>

전부 같은 거래다. `얼마나 잃어도 되는가` 를 정하면 `얼마나 빠를 수 있는가` 가 정해진다.

<br/>

Kafka가 매번 `fsync` 를 안 하는 이유가 있다.

서버 한 대의 디스크가 아니라 여러 대의 메모리에 복제되어 있으면, 한 대가 죽어도 다른 대에 있다.

`디스크 내구성` 대신 `복제 내구성` 을 택한 것이다.

<br/><br/>

## 자바에서 마주치는 자리

```java
FileChannel.force(boolean metaData)      - true 면 파일 크기 같은 메타데이터도
FileOutputStream.getFD().sync()          - 같은 일
RandomAccessFile("rws")                  - 매 write 마다 동기
```

<br/>

로그 라이브러리의 `immediateFlush` 는 `fsync` 가 아니다.

자바 버퍼를 커널로 넘기는 `flush()` 다. 커널에서 디스크로 가는 `fsync` 는 또 다른 층이다.

```java
자바 BufferedWriter 버퍼  --flush()-->  커널 페이지 캐시  --fsync()-->  디스크
```

<br/>

앞의 시스템 콜과 유저 모드, 커널 모드 글에서 본 `BufferedOutputStream` 이 첫 화살표를 줄이는 것이고,

`fsync` 는 둘째 화살표를 강제하는 것이다.

<br/><br/>

## 정리

```java
write() 성공 != 디스크에 있음. 페이지 캐시에 있음
fsync 가 디스크를 보장한다. 비용은 한 번에 수 ms
매번 fsync 하면 1000 배 느리다. 모아서 한 번이 답 (group commit)
DB, 큐, 캐시가 전부 이 거래를 설정으로 노출한다
```

<br/>

`데이터를 잃으면 안 된다` 는 요구가 있으면 `fsync 를 누가 언제 하는가` 를 확인해야 한다.

`저장했습니다` 라는 응답이 어느 층까지 내려간 뒤에 나온 것인지가 그 답이다.
