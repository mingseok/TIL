## Route 53 - DNS, A 레코드, CNAME, Alias

<br/>

## DNS 가 하는 일

이름을 IP로 바꿔준다.

```java
api.example.com  ->  13.125.x.x
```

<br/>

앞의 PORT, DNS 글에서 본 그 DNS다. Route 53은 AWS의 DNS 서비스다.

도메인을 사고(등록), 그 도메인의 레코드를 관리한다(호스팅 영역).

<br/><br/>

## 레코드 종류

## 궁금증!

```java
실제 조회 결과가 어떻게 생겼나
```

공개 도메인을 조회해봤다.

```java
$ dig +short A github.com
20.200.245.247                    <- A 레코드. 이름 -> IPv4

$ dig +short www.github.com
github.com.                       <- CNAME. 이름 -> 다른 이름
20.200.245.247                    <- 그 이름을 다시 따라가서 얻은 IP

TTL 30 초                          <- 30 초 뒤에 캐시가 만료된다
```

<br/>

```java
A       - 이름 -> IPv4
AAAA    - 이름 -> IPv6
CNAME   - 이름 -> 다른 이름. 두 단계로 조회된다
MX      - 메일 서버
TXT     - 텍스트. 도메인 소유 증명, SPF 같은 것
NS      - 이 도메인을 담당하는 DNS 서버
```

<br/><br/>

## CNAME 의 제약

```java
example.com        CNAME   xxx.elb.amazonaws.com      <- 안 된다
www.example.com    CNAME   xxx.elb.amazonaws.com      <- 된다
```

<br/>

루트 도메인(`example.com` 자체)에는 CNAME을 못 건다. DNS 규격이 그렇다.

루트에는 `NS`, `SOA` 레코드가 있어야 하는데 CNAME은 다른 레코드와 공존할 수 없다.

<br/>

그런데 ALB는 IP가 고정이 아니다. DNS 이름만 준다.

앞의 ALB 글에서 본 대로 IP를 감추는 것이 목적이니 당연하다.

루트 도메인을 ALB에 연결하려면 A 레코드에 IP를 적어야 하는데 IP가 없고, CNAME은 못 건다.

<br/><br/>

## Alias : Route 53 의 해법

```java
example.com        A (Alias)   xxx.elb.amazonaws.com
```

<br/>

겉으로는 A 레코드인데, 값이 IP가 아니라 AWS 자원의 이름이다.

조회가 오면 Route 53이 그 순간 ALB의 IP를 찾아서 A 레코드처럼 돌려준다.

<br/>

```java
CNAME  - 클라이언트가 두 번 조회한다. 루트 도메인 불가. 조회 요금 부과
Alias  - Route 53 이 안에서 해석. 루트 도메인 가능. AWS 자원 대상이면 조회 무료
```

<br/>

ALB, CloudFront, S3 정적 사이트, API Gateway가 Alias 대상이다.

AWS 자원에 도메인을 붙일 때는 항상 Alias다.

<br/><br/>

## TTL

```java
TTL 300   - 5 분. 바꾸면 최대 5 분 뒤 전파
TTL 86400 - 하루. 바꾸면 최대 하루
```

<br/>

앞의 캐시 기본 동작 글에서 본 캐시가 DNS에도 있다.

브라우저, 운영체제, ISP의 DNS 서버가 각자 TTL만큼 들고 있다.

<br/>

서버를 옮길 계획이 있으면 며칠 전에 TTL을 60초로 줄여둔다.

옮기는 날 레코드를 바꾸면 1분 안에 전파된다. 그 뒤 TTL을 다시 올린다.

<br/>

TTL이 하루인 상태에서 바꾸면, 하루 동안 사용자 절반은 옛 서버로 간다.

<br/><br/>

## JVM 의 DNS 캐시

자바는 운영체제 캐시 위에 자기 캐시를 또 갖고 있다.

```java
networkaddress.cache.ttl        - 성공한 조회를 얼마나 캐시하나. 기본 30 초 (보안 관리자가 있으면 무한)
networkaddress.cache.negative.ttl - 실패한 조회. 기본 10 초
```

<br/>

앞의 RDS 글에서 본 Multi-AZ 전환에서 이게 문제가 된다.

RDS 엔드포인트가 새 서버를 가리키게 바뀌었는데 JVM이 옛 IP를 캐시하고 있으면 계속 죽은 서버를 찌른다.

<br/>

앞의 커넥션 풀 글에서 본 HikariCP가 새 커넥션을 만들 때 DNS를 다시 조회하니, JVM 캐시 TTL이 짧아야 새 IP를 받는다.

<br/><br/>

## 라우팅 정책

Route 53은 단순 매핑 이상을 한다.

```java
단순           - 하나의 값
가중치         - 90% 는 A, 10% 는 B. 카나리 배포
지연 시간      - 사용자에게 가장 빠른 리전으로
장애 조치      - 주 서버가 헬스체크 실패하면 대기 서버로
지리적 위치     - 한국 사용자는 서울, 미국은 버지니아
```

<br/>

앞의 무중단 배포 글에서 본 블루그린을 DNS 수준에서 하는 것이 가중치 라우팅이다.

다만 TTL 때문에 즉시 전환이 안 된다. ALB 수준에서 하는 것이 더 정밀하다.

<br/><br/>

## 정리

```java
A / CNAME  - IP 로 / 다른 이름으로. 루트 도메인에 CNAME 불가
Alias      - AWS 자원용. 루트 도메인 가능. 무료. ALB, CloudFront 에는 항상 이것
TTL        - 바꿀 계획이 있으면 미리 줄인다. JVM 도 자기 캐시가 있다
라우팅 정책  - 가중치, 지연 시간, 장애 조치로 DNS 수준의 트래픽 제어
```
