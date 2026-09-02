## IO 스트림 - InputStream, Reader, Buffered

<br/>

## 바이트 스트림과 문자 스트림

```java
InputStream / OutputStream   - 바이트. 파일, 소켓, 이미지 전부 이것
Reader / Writer              - 문자. 바이트를 글자로 바꿔준다
```

<br/>

파일에는 바이트만 있다. `글자` 는 바이트를 인코딩 규칙으로 해석한 결과다.

앞의 문자 인코딩과 한글 깨짐 글에서 본 그 변환을 `Reader` 가 한다.

<br/><br/>

## 궁금증!

```java
한 바이트씩 읽는 것이 얼마나 느린가
```

한글 100만 글자(UTF-8 3MB) 파일을 네 방식으로 끝까지 읽었다.

<br/>

### 결과

```java
FileInputStream.read() 1바이트씩   : 3,000,000 번 읽음, 1475 ms
BufferedInputStream.read()        : 3,000,000 번 읽음,   28 ms    <- 52 배
InputStreamReader.read()          : 1,000,000 번 읽음,   41 ms
BufferedReader.read()             : 1,000,000 번 읽음,   17 ms
```

<br/>

## 1475ms 의 이유

`FileInputStream.read()` 는 부를 때마다 시스템 콜이다.

앞의 시스템 콜과 유저 모드, 커널 모드 글에서 잰 대로 한 번에 600ns다. 300만 번이면 1.8초다.

<br/>

`BufferedInputStream` 은 8KB씩 미리 읽어둔다. 시스템 콜이 300만 번에서 375번으로 준다.

그래서 52배다.

<br/>

## 3,000,000 vs 1,000,000

바이트 스트림은 300만 번, 문자 스트림은 100만 번 읽었다.

한글 한 글자가 UTF-8로 3바이트라, `Reader` 가 3바이트를 받아 1글자로 돌려준 것이다.

<br/>

`InputStreamReader` 는 안에 자기 버퍼가 있어서 `BufferedInputStream` 없이도 41ms다.

그 위에 `BufferedReader` 를 얹으면 17ms로 더 준다. 층이 하나 줄어서다.

<br/><br/>

## 감싸는 순서

```java
new BufferedReader(new InputStreamReader(new FileInputStream(f), UTF_8))
     문자 버퍼          바이트 -> 문자 변환        파일에서 바이트 읽기
```

<br/>

안쪽부터 바깥으로 읽으면 된다. 파일에서 바이트를 읽고, 문자로 바꾸고, 버퍼에 모은다.

<br/>

앞의 프록시 패턴 글에서 본 데코레이터 패턴이다.

각 겹이 `InputStream` 이나 `Reader` 인터페이스를 그대로 구현해서, 어떤 순서로 끼워도 타입이 맞는다.

<br/>

```java
FileInputStream    -> GZIPInputStream    -> InputStreamReader -> BufferedReader
파일                   압축 해제             문자 변환             버퍼
```

<br/>

압축 파일을 읽고 싶으면 한 겹만 더 끼우면 된다. 나머지 코드는 안 바뀐다.

<br/><br/>

## 인코딩을 명시하는 이유

```java
new FileReader(f)                                    // 기본 인코딩. 자바 17 이하면 OS 따라 다르다
new InputStreamReader(new FileInputStream(f), UTF_8)  // 명시
```

<br/>

`FileReader` 는 인코딩을 못 정한다(자바 11부터는 정할 수 있는 생성자가 생겼다).

앞의 문자 인코딩과 한글 깨짐 글에서 본 대로 자바 18 이전에는 이게 윈도우와 리눅스에서 다르게 동작했다.

<br/>

바이트를 다루는 코드와 문자를 다루는 코드가 나뉘어 있는 것이 이 때문이다.

인코딩을 정하는 지점이 딱 한 곳(`InputStreamReader`)이라 여기서만 신경 쓰면 된다.

<br/><br/>

## 닫아야 한다

```java
FileInputStream 하나 = 파일 디스크립터 1개
```

<br/>

앞의 파일 디스크립터 한계 글에서 본 그 FD다. 안 닫으면 쌓인다.

<br/>

```java
try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), UTF_8))) {
    ...
}       // 여기서 close() 가 보장된다. 예외가 나도
```

<br/>

바깥 스트림을 닫으면 안쪽까지 연쇄로 닫힌다. 데코레이터가 `close()` 를 안쪽으로 전달하기 때문이다.

<br/>

앞의 try  catch  finally 글에서 본 `try-with-resources` 가 이걸 위해 자바 7에 들어왔다.

그 전에는 `finally` 에 `close()` 를 쓰고, `close()` 자체가 예외를 던질 수 있어서 `try` 를 한 번 더 감싸야 했다.

<br/><br/>

## flush 와 fsync 는 다르다

```java
writer.flush()          // 자바 버퍼 -> 커널 페이지 캐시
channel.force(true)     // 커널 페이지 캐시 -> 디스크
```

<br/>

앞의 fsync와 내구성 글에서 본 구분이다.

`BufferedWriter` 에 쓰고 `flush()` 를 안 하면 자바 버퍼에 남아서 파일에 아무것도 없다.

`close()` 가 `flush()` 를 부르니 닫으면 나가긴 한다.

<br/>

`flush()` 를 했어도 디스크에는 아직 없다. 그건 `fsync` 의 일이다.

<br/><br/>

## NIO 와의 관계

```java
java.io   - 스트림. 한 방향. Blocking
java.nio  - 채널 + 버퍼. 양방향. Non-blocking 가능
```

<br/>

앞의 IO 모델 글에서 본 NIO가 다른 축이다.

파일 하나 읽는 데는 `java.io` 가 편하고, 소켓 수천 개를 다루는 데는 `java.nio` 가 필요하다.

<br/>

`Files.readString(path)`, `Files.lines(path)` 같은 편의 메서드가 `java.nio.file` 에 있다.

파일을 통째로 읽을 때는 스트림을 조립하는 대신 이걸 쓰면 한 줄이다.

<br/><br/>

## 정리

```java
바이트 스트림    - 파일, 소켓의 원래 모습
문자 스트림      - 인코딩을 적용한 해석. InputStreamReader 가 그 지점
Buffered        - 시스템 콜을 줄인다. 52 배 차이
데코레이터       - 겹을 끼워서 기능을 더한다. 바깥을 닫으면 전부 닫힌다
try-with-resources - FD 누수를 막는다
```
