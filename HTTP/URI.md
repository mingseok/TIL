## URI

리소스를 식별하는 통일된 방법.

<br/>

## 리소스란?

`URL`로 식별할 수 있는 모든 것을 자원(리소스)이라고 한다. 



웹 브라우저에 있는 HTML 파일 같은 것만 자원(리소스)을 뜻하는 게 아니고, 

실시간 교통정보 라던가, 우리가 구분할 수 있는 모든 것을 리소스라고 하는 것이다.

<br/><br/>

## 각각 어떤 것인가??

![이미지](/programming/img/입문567.PNG)


<br/><br/>

## URI라는 큰 개념이 있다.

- 그 안에 `URL` 과 `URN` 이 있는 것이다.

    - `URL` : 리소스가 이 위치에 있다는 뜻이다

    - `URN` : 리소스의 이름이다.

        - URN은 “이런게 있구나..” 만 생각하자

![이미지](/programming/img/입문568.PNG)


<br/><br/>

## 차이는 이렇다.

그래서 거진 URL만 사용하는 것이다. 

![이미지](/programming/img/입문569.PNG)

<br/><br/>

## 실험

```java
https://www.naver.com/
```

위 링크를 검색 하면 `URL`을 쳤기 때문에, 저 `URL`에 대한 `리소스 결과를 돌려준 것이다.`

<br/><br/>

## URL 전체 문법

```java
https://www.google.com:443/search?q=hello&hl=ko
```

- `프로토콜(https)`

- `호스트명(www.google.com)`

    - 도메인명 또는 IP 주소를 직접 사용가능

- `포트 번호(443)`

    - 접속 포트, 일반적으로 생략, 생략시 http는 80, http는 443

- `패스(/search)`

    - 리소스 경로(path), 계층적 구조

    - 예) /home/file1.jpg

    - /members

- `쿼리 파라미터(q=hello&hl=ko)`

    - key=value 형태

    - ?로 시작, &로 추가 기능 ?keyA=valueA&keyB=valueB

    - ‘쿼리 파라미터’, ‘쿼리 스트링’ 으로 불린다.

<br/><br/>

## 주로 프로토콜 사용

- `프로토콜:` 어떤 방식으로 자원에 접근할 것인가 하는 약속 규칙

    - 예) `http, https`등

- http는 80 포트, https는 443 포트를 주로 사용, 포트는 생략 가능

- https는 http에 보안 추가 (HTTP Secure)
<br/>

## 궁금증!

```java
URI, URL, URN 을 실제 주소로 구분해보면
```

```java
URI (Uniform Resource Identifier) - 자원을 식별하는 모든 방식
  URL (Locator) - 어디에 있는지로 식별한다
  URN (Name)    - 이름으로 식별한다
```

<br/>

```java
https://www.example.com:443/members/100?q=hello#section2     <- URL
urn:isbn:9788966262281                                       <- URN
```

<br/>

`URN` 은 위치를 안 말한다. `이 책` 이라고만 하고 어디서 구하는지는 안 알려준다.

그래서 이름만으로 실제 자원을 찾아주는 시스템이 따로 있어야 하는데, 널리 쓰이지 않는다.

<br/>

실무에서 만나는 것은 사실상 전부 `URL` 이라, `URI` 와 `URL` 을 섞어 써도 큰 문제는 없다.

<br/>

## URL 을 쪼개보면

```java
https://www.example.com:443/members/100?q=hello#section2
```

```java
https              - 스킴 (프로토콜)
www.example.com    - 호스트
443                - 포트
/members/100       - 경로
q=hello            - 쿼리
section2           - 프래그먼트
```

<br/>

## 프래그먼트는 서버에 안 간다

이게 잘 안 알려진 사실이다.

```java
GET /members/100?q=hello HTTP/1.1
Host: www.example.com
```

<br/>

앞의 HTTP 메시지 글에서 본 요청 줄에 `#section2` 가 없다.

<br/>

`#` 뒤는 브라우저가 쓰는 것이다. 그 위치로 스크롤하라는 표시일 뿐이다.

서버 로그에도 안 남고 서버는 알 방법이 없다.

<br/>

그래서 `#` 뒤에 뭔가를 담아 서버로 보내려 하면 안 된다.

<br/>

## 포트를 생략하면

```java
https://example.com     -> 443 으로 간다
http://example.com      -> 80 으로 간다
```

<br/>

스킴마다 정해진 기본 포트가 있어서 생략할 수 있는 것이다.

앞의 PORT, DNS 글에서 본 그 잘 알려진 포트다.

<br/>

기본 포트가 아니면 반드시 적어야 한다.

```java
http://localhost:8080     -> 생략하면 80 으로 가서 연결이 안 된다
```

<br/>

## 한글이 들어가면

앞의 form 태그 글에서 본 그 문제다.

```java
$ curl "http://localhost:18080/members?name=민석"
<h1>400 Bad Request</h1>URISyntaxException thrown
```

<br/>

URL에는 정해진 문자만 쓸 수 있다. 영문, 숫자, 일부 기호가 전부다.

<br/>

그 밖의 문자는 UTF-8 바이트로 쪼갠 뒤 `%` 를 붙인다.

```java
민석 -> %EB%AF%BC%EC%84%9D
```

<br/>

브라우저 주소창에 한글이 보이는 것은 브라우저가 예쁘게 보여주는 것이고,

실제로 나가는 요청에는 인코딩된 형태로 나간다.

<br/>

## 예약 문자를 조심해야 한다

`&`, `=`, `?`, `/`, `#` 은 구분자로 쓰이니 값에 들어가면 인코딩해야 한다.

```java
// 검색어가 "커피 & 차" 라면
?q=커피 & 차        -> & 가 파라미터 구분자로 해석된다
?q=%EC%BB%A4%ED%94%BC%20%26%20%EC%B0%A8    -> 하나의 값으로 들어간다
```

<br/>

자바에서는 이렇게 처리한다.

```java
URLEncoder.encode("커피 & 차", StandardCharsets.UTF_8);
```

<br/>

`+` 도 조심해야 한다. 쿼리 스트링에서는 공백을 뜻해서, 진짜 `+` 를 보내려면 `%2B` 로 써야 한다.

이메일 주소에 `+` 를 쓰는 경우가 있어서 실제로 종종 문제가 된다.

<br/>

## 대소문자 규칙도 부분마다 다르다

```java
스킴과 호스트 - 대소문자를 안 가린다  (HTTP://EXAMPLE.COM 도 된다)
경로 이후    - 대소문자를 가린다     (/Members 와 /members 는 다르다)
```

<br/>

호스트는 앞의 PORT, DNS 글에서 본 DNS가 대소문자를 안 가려서 그렇다.

경로는 서버가 해석하는 것이라 서버 마음이다. 대개 가린다.

<br/>

앞의 리소스 식별 글에서 소문자와 하이픈을 권하는 이유 중 하나가 이것이다.

대소문자를 섞어두면 `/Members` 와 `/members` 가 다른 주소가 되어 캐시도 따로 잡힌다.
