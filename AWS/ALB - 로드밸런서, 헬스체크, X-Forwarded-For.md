## ALB - 로드밸런서, 헬스체크, X-Forwarded-For

<br/>

## 로드밸런서가 하는 일

요청을 받아 뒤의 서버 여러 대에 나눈다.

```java
사용자 -> ALB -> EC2 A
             -> EC2 B
```

<br/>

사용자는 ALB의 주소만 안다. 뒤에 서버가 몇 대인지, IP가 뭔지 모른다.

앞의 탄력적 IP 글에서 본 `IP 를 아무도 모르는 구조` 가 이것이다.

<br/>

```java
ALB   - HTTP/HTTPS. 경로와 호스트를 보고 나눈다. 7 계층
NLB   - TCP/UDP. 빠르다. 고정 IP. 4 계층
```

<br/>

앞의 프로토콜 OSI 7 layer 글에서 본 계층이다. ALB는 HTTP 헤더를 읽고, NLB는 패킷만 넘긴다.

웹 서비스는 거의 ALB다.

<br/><br/>

## 타겟 그룹과 헬스체크

ALB는 타겟 그룹에 요청을 보낸다. 타겟 그룹이 서버 목록이다.

```java
헬스체크 경로    /actuator/health
간격           30 초
정상 판정       연속 2 번 200
비정상 판정     연속 2 번 실패
```

<br/>

## 궁금증!

```java
헬스체크가 실제로 무엇을 보나
```

로컬에 `/health` 를 두고 상태를 바꿔봤다.

```java
정상일 때        : 200   <- 200 이면 트래픽을 보낸다
앱이 DOWN 표시 후 : 503   <- 503 이면 대상에서 뺀다. 프로세스는 살아 있어도
```

<br/>

프로세스가 살아 있는지가 아니라 `이 경로가 200 을 주는지` 만 본다.

<br/>

그래서 헬스체크 응답에 무엇을 넣을지가 설계다.

```java
항상 200                     - 프로세스가 살아 있으면 정상. DB 가 죽어도 정상으로 본다
DB 연결까지 확인해서 200      - DB 가 죽으면 서버 전체가 빠진다. 이게 맞나?
```

<br/>

DB가 죽었을 때 모든 서버가 헬스체크에 실패하면 ALB가 보낼 곳이 없어서 전부 503이 된다.

DB 없이도 줄 수 있는 응답(정적 페이지, 캐시된 것)까지 못 준다.

<br/>

그래서 헬스체크는 `이 서버 자체가 요청을 받을 수 있는가` 만 보는 것이 관례다.

DB 상태는 별도 모니터링으로 본다. 앞의 CloudWatch 글에서 본다.

<br/>

앞의 시그널과 graceful shutdown 글에서 본 종료 순서에서 `헬스체크를 먼저 실패시킨다` 가 여기 들어간다.

새 요청이 안 오게 한 뒤에 진행 중인 것을 끝내는 것이다.

<br/><br/>

## 서버가 보는 클라이언트 IP

```java
[직접 접속]
  서버가 보는 접속 IP  = 127.0.0.1
  X-Forwarded-For    = null

[로드밸런서를 거친 것처럼]
  서버가 보는 접속 IP  = 127.0.0.1         <- 로드밸런서 IP
  X-Forwarded-For    = 203.0.113.7       <- 진짜 사용자
  X-Forwarded-Proto  = https
```

<br/>

앞의 WAS 서버와 WEB 서버의 이해 글에서 본 그 문제다.

`request.getRemoteAddr()` 는 ALB의 IP다. 진짜 사용자는 `X-Forwarded-For` 에 있다.

<br/>

```java
X-Forwarded-For: 203.0.113.7, 10.0.0.15      <- 여러 단계를 거치면 쉼표로 이어진다. 맨 앞이 원래 사용자
X-Forwarded-Proto: https                      <- 사용자는 HTTPS 로 왔는데 ALB -> 서버는 HTTP
X-Forwarded-Port: 443
```

<br/>

스프링 부트는 설정 한 줄로 이 헤더를 읽어서 `getRemoteAddr()`, `getScheme()` 을 바꿔준다.

```java
server.forward-headers-strategy=native
```

<br/>

이걸 안 하면 리다이렉트 URL이 `http://` 로 만들어져서 브라우저가 경고를 띄운다.

<br/>

`X-Forwarded-For` 는 클라이언트가 위조할 수 있다. 위 실험에서도 내가 헤더를 직접 붙였다.

ALB는 자기가 받은 IP를 맨 뒤에 추가하니, 신뢰할 것은 `ALB 가 붙인 마지막 값` 이다. IP 기반 차단이나 rate limit을 할 때 이걸 헷갈리면 우회된다.

<br/><br/>

## HTTPS 종단

```java
사용자 --HTTPS--> ALB --HTTP--> EC2
```

<br/>

인증서는 ALB에만 둔다. ACM(Certificate Manager)에서 무료로 발급되고 자동 갱신된다.

EC2는 HTTP만 받는다. 앞의 WAS 글에서 본 대로 TLS 계산을 앞에서 한 번만 한다.

<br/>

내부 구간이 HTTP라도 VPC 안이라 외부에서 볼 수 없다.

규제가 엄격한 곳은 내부도 HTTPS로 하는데, 그건 앞의 VPC 글에서 본 네트워크 격리를 믿지 못하는 경우다.

<br/><br/>

## 라우팅 규칙

ALB는 경로와 호스트를 보고 다른 타겟 그룹으로 보낼 수 있다.

```java
/api/*          -> api 타겟 그룹
/admin/*        -> admin 타겟 그룹
api.example.com -> api 타겟 그룹
```

<br/>

앞의 CloudFront 글에서 본 `/static 은 S3, /api 는 ALB` 처럼, ALB 안에서 또 나눌 수 있다.

<br/><br/>

## 고정 세션(sticky session)

같은 사용자를 계속 같은 서버로 보내는 기능이다.

앞의 쿠키, 세션 글에서 본 서버 세션을 쓸 때 필요하다. 다른 서버로 가면 세션이 없으니까.

<br/>

켜면 부하가 고르게 안 나뉘고, 그 서버가 죽으면 그 사용자들은 로그아웃된다.

세션을 Redis 같은 밖에 두고 고정 세션을 안 쓰는 것이 맞다.

앞의 HTML, HTTP API, CSR, SSR 글에서 본 토큰 방식이면 이 문제가 처음부터 없다.

<br/><br/>

## 정리

```java
ALB       - HTTP 를 이해하는 로드밸런서. 뒤의 서버를 감춘다
헬스체크   - 경로가 200 을 주는지만. DB 상태는 넣지 않는다
X-Forwarded-For - 진짜 사용자 IP. 위조 가능하니 ALB 가 붙인 값만 믿는다
HTTPS 종단 - 인증서는 ALB 에. 내부는 HTTP
고정 세션  - 피한다. 세션을 밖으로
```
