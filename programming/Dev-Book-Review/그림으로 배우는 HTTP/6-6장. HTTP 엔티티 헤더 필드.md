## 6-6장. HTTP 엔티티 헤더 필드

## 엔티티 헤더 필드

리퀘스트 메시지와 리스폰스 메시지에 포함된 엔티티에 사용되는 헤더로

콘텐츠의 갱신 시간 같은 엔티티에 관한 정보르 포함 하는 것입니다.

<br/><br/>

## Allow

```
Allow: GET, HEAD
```

Request-URI에 지정된 리소스가 제공하는 메소드의 일람을 전달 합니다.

서버가 받을 수 없는 메소드를 수신한 경우

- 상태 코드 `405 Method Not Allowed 리스폰스` + 수신 가능한 메소드의 일람을 기술한 `Allow 헤더 필드`를 반환

<br/><br/>

## Content-Encoding

```
Content-Encoding: gzip
```

서버가 엔티티 바디에 대해서 실시한 콘텐츠 코딩 형식을 전달한다.

콘텐츠 코딩은 엔티티의 정보가 누락되지 않도록 압축할 것을 지시한다.

<br/>

### 주로 4가지 콘텐츠 코딩 형식이 사용된다.

- Gzip
- Compress
- Deflate
- Identity

<br/><br/>

## Content-Language

```
Content-Language: en
```

엔티티 바디에 사용된 자연어(한국어, 영어 등)를 전달한다.

<br/><br/>

## Content-Length

```
Content-Length
```

엔티티 바디의 크기를 전달한다.

엔티티 바디에 전송 코딩이 실시된 경우, Content-Language 헤더 필드를 사용해서는 안된다.

<br/><br/>

## Content-Location

```
Content-Location: http://www.hackr.jp/index-ja.html
```

메시지 바디에 대응하는 URI를 전달한다.

메시지 바디로 반환된 리소스의 URI를 나타낸다.

<br/><br/>

## Content-MD5

```
Content-MD5: OGFkZDUwNGVhNGT3N2MXMDlwZmQ4NTDFSTY==
```

메시지 바디가 변경되지 않고 도착했는지 확인하기 위해 MD5 알고리즘에 의해서 생성된 값을 전달 한다.

결국, 클라이언트는 수신한 메시지 바디에 MD5 알고리즘을 사용해서

Content-MD5 헤더 필드의 필드값을 비교한다.

<br/><br/>

## Content-Range

```
Content-Range: bytes 5001-10000/10000
```

범위를 지정해서 일부분만을 리퀘스트하는 **Range 리퀘스트에 대해 리스폰스를 할 때 사용**

<br/><br/>

## Content-Type

```
Content-Type: text/html; charset=UTF-8
```

엔티티 바디에 포함되는 오브젝트의 미디어 타입을 전달한다.

<br/><br/>

## Expires

```
Expires: Wed, 04 Jul 2012 08:26:05 GMT
```

리소스의 유효 기한 날짜를 전달

<br/>

오리진 서버가 캐시 서버에 캐시되는 것을 원하는 않은 경우,

Date 헤더 필드의 필드 값과 같은 날짜로 해두면 된다.

<br/><br/>

## Last-Modified

```
Last-Modified: Wed, 23 May 2012 09:59:55 GMT
```

리소스가 마지막으로 갱신되었던 날짜 정보를 전달한다.

<br/><br/>

## 쿠키를 위한 헤더 필드

쿠키는 유저 식별과 상태 관리에 사용되고 있는 기능이다.

웹 사이트가 유저의 상태를 관리하기 위해서 웹 브라우저 경유로 유저의 컴퓨터 상에 일시적으로 

데이터를 기록해 두고, 그 다음 그 유저가 웹 사이트에 액세스 해 왔을 때 

<br/>

지난번에 발행한 쿠키를 송신받을 수 있습니다.

유효 기한과 송신지의 도메인, 경로, 프로토콜 등을 체크하는 것이 가능하기에,

적절하게 발행된 쿠키는 다른 웹 사이트와 공격자의 공격에 의해 데이터가 도난 당하는 일은 없다.

<br/>

| 헤더 필드 명 | 설명 | 헤더 종별 |
| --- | --- | --- |
| Set-Cookie | 상태 관리 개시를 위한 쿠키 정보 | 리스폰스 |
| Cookie | 서버에서 수신한 쿠키 정보 | 리퀘스트 |

<br/><br/>

## Set-Cookie

```java
Set-Cookie: status-enable; expires=Tue, 05 Jul 2011 07:26:31 GMT;
=>path=/;domain=.hack.jp;
```

서버가 클라이언트에 대해서 상태 관리를 시작할 때 다양한 정보를 전달한다.

<br/>

필드 값에는 다음과 같은 정보가 기술된다.

| 속성 | 설명 |
| --- | --- |
| NAME=VALUE | 쿠키에 부여된 이름과 값(필수) |
| Expires=DATE | 쿠키 유효 기한(지정되지 않은 경우는 브라우저를 닫을 때까지) |
| Path=PATH | 쿠키 적용 대상이 되는 서버 상의 디렉토리(지정하지 않은 경우는 도큐먼트와 같은 디렉토리) |
| Domain=도메인 명 | 쿠키 적용 대상이 되는 도메인 명(지정하지 않은 경우는 쿠키를 생성한 서버의 도메인) |
| Secure | HTTPS로 통신하고 있는 경우에만 쿠키를 송신 |
| HttpOnly | 쿠키를 JavaScript에서 액세스하지 못하도록 제한 |

<br/><br/>

## Expires 속성

쿠키의 Expires 속성은 브라우저가 쿠키를 송출할 수 있는 유효 기한을 지정 할 수 있다.

생략한 경우에는 브라우저 세션이 유지되고 있는 동안만 유효하게 된다.

<br/>

즉, 브라우저 애플리케이션을 닫을 때까지 이다.

유효 기한이 지났다면 쿠키를 덮어 쓰는 것으로 실질적인 클라이언트 측의 쿠키를 삭제하는 것이 가능하다.

<br/><br/>

## Path 속성

쿠키의 path 속성은 쿠키를 송출하는 범위를 특정 디렉토리에 한정할 수 있다.

하지만, 보안 효과는 기대할 수 없다.

<br/><br/>

## Domain 속성

쿠키의 domain 속성에 의해서 지정된 도메인 명은 후방 일치가 된다.

예를 들면, “minseok.com” 지정 했다면 → “minseok.com” 이외에 “www.minseok.com” 과

“www2.minseok.com” 등에서도 쿠키가 송출 된다.

<br/>

그렇기에, 쿠키를 송출하는 경우를 제외하고 domain 속성은 지정하지 않는 쪽이 안전하다.

<br/><br/>

## Secure 속성

쿠키의 secure 속성은 웹 페이지가 HTTPS에서 열렸을 때에만 쿠키 송출을 제한하기 위해서 지정한다.

아래와 같이 쿠키를 발행할 때에 secure 속성을 지정해서 실시한다.

```java
Set-Cookie: name=value; secure
```

<br/><br/>

## HttpOnly 속성

자바스크립트를 경유해서 쿠키를 취득하지 못하도록 하는 쿠키의 확장 기능이다.

도청을 막는 것을 목적으로 하고 있다.

<br/><br/>

## Cookie

```java
Cookie: status=enable
```

HTTP의 상태 관리 지원을 원할 때 서버로부터 수신한 쿠키를 이후의 리퀘스트에 포함해서 전달.

쿠키를 여러 개 수신하고 있을 때에는 쿠키를 여러 개 보내는 것도 가능하다.

<br/><br/>

## 그 이외의 헤더 필드

HTTP 헤더 필드는 독자적으로 확장할 수 있다고 한다.

- X-Frame-Opthion
- X-XSS-Protection
- DNT
- P3P


<br/><br/>

>**Reference** 
> <br/> [
그림으로 배우는 HTTP & Network](http://www.yes24.com/Product/Goods/15894097)


