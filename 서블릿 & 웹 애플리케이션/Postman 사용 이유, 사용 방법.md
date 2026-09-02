## Postman 사용 이유, 사용 방법

포스트맨 사용 방법은 항상 스프링 실행중 일때 사용해야 한다!

<br/>

## form태그 테스트를 포스트맨으로 쉽게 할 수 있다.

![이미지](/programming/img/입문49.PNG)

<br/><br/>

콘솔창에 바로 실행 되는 걸 알 수 있다.

![이미지](/programming/img/입문50.PNG)

<br/><br/>

## 그래서 이걸 왜 쓰는거야?

Post 방법을 사용해서 데이터가 서버단으로 제대로 가는지 테스트 해보고 싶은 것이다.

<br/>

그런데 테스트 하려면 HTML 파일에 form태그를 만들어야 한다는 것이다.



- 이 작업이 너무 귀찮은 것이다.

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<form action="/request-param" method="post">
    username: <input type="text" name="username"/>
    age: <input type="text" name="age"/>
    <button type="submit">전송</button>
</form>
</body>
</html>
```

<br/><br/>

이렇게 포스트맨을 사용하면 HTML 파일을 만들지 않고, 테스트를 쉽게 할 수 있는 것이다.

![이미지](/programming/img/입문51.PNG)


<br/><br/>



## 예제)

```java
@Slf4j
public class MappingController {

    @GetMapping("/hello-basic")
    public String helloBasic() {
        log.info("helloBasic");
        return "ok";
    }
}
```

<br/><br/>

위 코드를 `포스트맨`에서 `GET`으로 실행 시키면 “ok” 가 출력 되며, 잘 실행되는 것을 알 수 있다.

![이미지](/programming/img/입문70.PNG)


<br/><br/>

## 만약 Post로 한다면?

이렇게 `405 에러`가 발생하는 것을 알 수 있다.

추가로 url 주소를 잘못 입력해서 실행하더라도 에러가 발생한다. `(url 에러는 404에러)`

![이미지](/programming/img/입문71.PNG)
<br/>

## 궁금증!

```java
curl 로도 되는데 왜 도구를 쓰나
```

`curl` 로도 다 된다. 앞의 HTTP 요청 데이터 글에서도 `curl` 로 확인했다.

```java
$ curl -X POST http://localhost:8080/api/members \
       -H "Content-Type: application/json" \
       -d '{"name":"민석"}'
```

<br/>

다만 이걸 매번 손으로 치는 게 문제다.

<br/>

```java
저장해두고 다시 쓰기
팀원과 공유하기
토큰을 자동으로 넣기
개발 서버와 운영 서버를 전환하기
```

<br/>

이런 것들 때문에 도구를 쓰는 것이다.

<br/>

## 환경 변수가 제일 유용하다

```java
{{baseUrl}}/api/members
```

<br/>

`baseUrl` 을 환경마다 다르게 두면 요청은 하나만 만들어두면 된다.

```java
local  -> http://localhost:8080
dev    -> https://dev.example.com
```

<br/>

토큰도 마찬가지다.

```java
Authorization: Bearer {{token}}
```

<br/>

로그인 요청의 응답에서 토큰을 꺼내 변수에 넣어두면,

그 다음 요청부터는 자동으로 붙는다.

```java
// 로그인 요청의 Tests 탭에
pm.environment.set("token", pm.response.json().accessToken);
```

<br/>

앞의 JWT 글에서 본 그 토큰을 매번 복사해 붙일 필요가 없어진다.

<br/>

## Content-Type 을 안 맞추면

이게 초보 때 제일 많이 겪는 문제다.

```java
Body 를 raw + JSON 으로 골랐는데
헤더에 Content-Type: application/json 이 안 붙어 있으면
```

```java
415 Unsupported Media Type
```

<br/>

앞의 @RequestMapping(), HttpServletRequest 글에서 본 `consumes` 가 안 맞는 것이다.

<br/>

Postman은 Body 형식을 고르면 헤더를 자동으로 붙여준다.

그런데 헤더 탭에서 직접 다른 값을 넣어두면 그게 이긴다.

<br/>

응답이 이상하면 실제로 나간 요청을 보는 게 빠르다.

```java
Postman 콘솔 (Ctrl+Alt+C)
```

<br/>

## 그런데 요즘은 대안이 많다

```java
IntelliJ 의 .http 파일
VS Code REST Client
Bruno
```

<br/>

`.http` 파일은 소스와 같이 커밋할 수 있는 게 큰 장점이다.

```java
### 회원 등록
POST {{baseUrl}}/api/members
Content-Type: application/json

{
  "name": "민석"
}
```

<br/>

Postman은 클라우드에 저장되고 계정이 필요한데,

이건 그냥 텍스트 파일이라 git으로 관리된다.

<br/>

API가 바뀌면 요청 예시도 같은 커밋에서 바뀌니 어긋날 일이 없다.

<br/>

## 문서와 도구를 하나로 묶는 방향

```java
Swagger (OpenAPI)
```

<br/>

앞의 Swagger 글에서 본 대로, 코드에서 문서를 만들고 그 문서에서 바로 요청을 보낸다.

```java
@Operation(summary = "회원 등록")
@PostMapping("/api/members")
public MemberResponse create(@RequestBody MemberRequest request) { ... }
```

<br/>

컨트롤러가 바뀌면 문서도 자동으로 바뀐다.

Postman 컬렉션은 손으로 고쳐야 하니 금방 낡는다.

<br/>

그래서 요즘 흐름은 이렇게 나뉜다.

```java
API 명세     -> Swagger. 코드에서 자동 생성
탐색적 테스트 -> .http 파일이나 Postman
자동화 테스트 -> 코드로 작성한 통합 테스트
```

<br/>

Postman으로 검증하는 것과 테스트 코드를 쓰는 것은 다른 일이다.

Postman은 내가 눌러야 돌지만, 테스트 코드는 빌드할 때마다 돈다.

<br/>

손으로 확인한 것은 다음 배포에서 깨져도 아무도 모른다는 게 차이다.
