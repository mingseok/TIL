## HTTP 메시지

요청 메시지와 응답 메시지는 다르게 생겼다.



![이미지](/programming/img/입문589.PNG)

<br/><br/>

![이미지](/programming/img/입문590.PNG)

<br/><br/>

## 요청 메시지 (요청은 request-lin)

![이미지](/programming/img/입문591.PNG)

- `HTTP 메서드` : (GET: 조회)

    - 종류 GET, POST, PUT, DELETE 등

    - GET: 리소스 조회

    - POST: 요청 내역 처리

- `요청 대상` : (/search?q=hello&hl=ko)

    - 요청 대상

    - 절대경로 = “/”로 시작하는 경로

- `HTTP/1.1` : http 버전

<br/><br/>

## 응답 메시지 (응답은 status-line)

SP : 스페이스 한칸 띄우기 라고 생각하면 된다.

![이미지](/programming/img/입문592.PNG)

- http 상태 코드: 요청 성공, 실패를 나타냄

    - 200: 성공

    - 400: 클라이언트 요청 오류

    - 500: 서버 내부 오류

- 이유 문구: 사람이 이해할 수 있는 짧은 상태 코드 설명 글

<br/><br/>

## 응답 메시지 - HTTP 헤더

```
Content-Type: text/html;charset=UTF-8
Content-Length: 3423
```

- HTTP 전송에 필요한 모든 부가 정보

- 예) 메시지 바디의 내용, 메시지 바디의 크기, 압축, 인증,
    
    요청 클라이언트(브라우저) 정보, 서버 애플리케이션 정보, 캐시 관리 정보 등
    
<br/><br/>

## 응답 메시지 - HTTP 메시지 바디

```
<html>
   <body>...</body>
</html>
```

- 실제 전송할 데이터

- HTML 문서, 이미지, 영상,  JSON 등등 byte로 표현할 수 있는 모든 데이터 전송 가능.
<br/>

## 궁금증!

```java
실제 요청 메시지가 어떻게 생겼는지 그대로 보자
```

요청을 받아서 그대로 되돌려주는 서버를 띄워놓고 보냈다.

<br/>

### GET 요청

```java
$ curl --get --data-urlencode "name=민석" http://localhost:18080/members

GET /members?name=%eb%af%bc%ec%84%9d HTTP/1.1
Accept: */*
Host: localhost:18080
User-agent: curl/8.7.1

[본문] (없음)
```

<br/>

### POST 요청

```java
$ curl -X POST http://localhost:18080/members/new --data-urlencode "name=민석"

POST /members/new HTTP/1.1
Accept: */*
Host: localhost:18080
User-agent: curl/8.7.1
Content-type: application/x-www-form-urlencoded
Content-length: 20

[본문] name=%EB%AF%BC%EC%84%9D
```

<br/>

원문의 구조가 그대로 보인다.

```java
1. 시작 줄  : GET /members?... HTTP/1.1
2. 헤더     : Host, User-Agent, Content-Type ...
3. 빈 줄    : 헤더가 끝났다는 표시
4. 본문     : 있을 수도 없을 수도
```

<br/>

## 빈 줄이 왜 필요한가

헤더가 몇 개인지 미리 안 알려주기 때문이다.

<br/>

서버는 한 줄씩 읽다가 `빈 줄` 을 만나면 `헤더 끝` 이라고 판단한다.

그 다음부터가 본문이다.

<br/>

그리고 본문이 어디서 끝나는지는 `Content-Length` 로 안다.

```java
Content-length: 20    -> 빈 줄 다음부터 20 바이트가 본문이다
```

<br/>

이 값이 틀리면 문제가 생긴다.

실제보다 크면 서버가 오지 않는 데이터를 계속 기다리고, 작으면 뒷부분이 잘린다.

<br/>

## Host 헤더가 필수인 이유

`HTTP/1.1` 부터 `Host` 는 없으면 안 되는 헤더다.

<br/>

이유는 한 IP에 여러 사이트가 있을 수 있기 때문이다.

```java
a.com 과 b.com 이 같은 서버(같은 IP)에 있다
-> 요청이 도착했을 때 어느 사이트를 원하는지 알 수가 없다
-> Host 헤더를 보고 판단한다
```

<br/>

`HTTP/1.0` 시절에는 IP 하나에 사이트 하나였다.

지금은 서버 하나에 수백 개 사이트를 올리는 것이 흔해서, 이 헤더 없이는 동작할 수 없다.

<br/>

## 응답 메시지도 구조가 같다

```java
$ curl -i http://localhost:18083/etag

HTTP/1.1 200 OK
Date: Tue, 01 Sep 2026 12:59:36 GMT
Etag: "v1-abc123"
Content-length: 41
Cache-control: no-cache

안녕하세요 이것은 본문입니다
```

<br/>

첫 줄만 다르다.

```java
요청 : 메서드 + 경로 + 버전       (GET /members HTTP/1.1)
응답 : 버전 + 상태코드 + 설명     (HTTP/1.1 200 OK)
```

<br/>

나머지는 똑같이 `헤더 -> 빈 줄 -> 본문` 이다.

<br/>

## 본문이 없는 응답도 있다

앞의 검증 헤더 글에서 본 `304` 가 그렇다.

```java
HTTP/1.1 304 Not Modified
Etag: "v1-abc123"
```

<br/>

`Content-length` 조차 없다. 빈 줄 다음에 아무것도 안 온다.

<br/>

앞의 리다이렉트 글에서 본 `302` 도 마찬가지다.

```java
HTTP/1.1 302 Temporary Redirect
Content-length: 0
Location: /order/done
```

<br/>

`Content-length: 0` 이라고 명시했다.

`0` 이라고 적는 것과 아예 안 적는 것 중에는 적어주는 편이 친절하다.

<br/>

## 헤더 이름은 대소문자를 안 가린다

위 출력에서 `Content-type` 과 `Content-Type` 이 섞여 보이는데 둘 다 맞다.

```java
Content-Type: application/json
content-type: application/json
CONTENT-TYPE: application/json
```

<br/>

전부 같은 헤더로 취급된다.

<br/>

다만 값은 대소문자를 가리는 경우가 있다.

```java
Content-Type: application/json    (값은 소문자로 쓴다)
```

<br/>

`HTTP/2` 부터는 헤더 이름을 소문자로만 쓰도록 규격이 바뀌었다.

그래서 요즘 코드에서는 소문자로 쓰는 편이 안전하다.
