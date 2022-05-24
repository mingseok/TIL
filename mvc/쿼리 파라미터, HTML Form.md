## HTTP 요청 파라미터 - 쿼리 파라미터, HTML Form


클라이언트에서 서버로 요청 데이터를 전달할 때는 주로 다음 3가지 방법을 사용한다.

<br/>

### GET - 쿼리 파라미터

`/url?username=hello&age=20`

메시지 바디 없이, URL의 쿼리 파라미터에 데이터를 포함해서 전달

예) 검색, 필터, 페이징 등에서 많이 사용하는 방식

<br/>

### POST - HTML Form

`content-type: application/x-www-form-urlencoded`

메시지 바디에 쿼리 파리미터 형식으로 전달 username=hello&age=20

예) 회원 가입, 상품 주문, HTML Form 사용

<br/>HTTP message body에 데이터를 직접 담아서 요청

HTTP API에서 주로 사용, `JSON, XML, TEXT`

데이터 형식은 주로 JSON 사용

POST, PUT, PATCH

<br/>

### 요청 파라미터 - 쿼리 파라미터, HTML Form

`HttpServletRequest` 의 `request.getParameter()` 를 사용하면 다음 두가지 요청 파라미터를 조회 할 수 있다

<br/>

### GET, 쿼리 파라미터 전송

예시 : localhost:8080/request-param?username=hello&age=20

<br/>

### POST, HTML Form 전송

예시

```java
POST /request-param ...
content-type: application/x-www-form-urlencoded

username=hello&age=20
```

<br/>GET 쿼리 파리미터 전송 방식이든, POST HTML Form 전송 방식이든 둘다 형식이 

같으므로 구분 없이 조회할 수 있다.

<br/>이것을 간단히 요청 파라미터(`request parameter`) 조회라 한다.

지금부터 스프링으로 요청 파라미터를 조회하는 방법을 단계적으로 알아보자.

<br/>

`RequestParamController` 클래스 생성

### 이건 그냥 서블릿이 제공하는 `HttpServletRequest` 가 제공하는 `getParameter()` 를 사용한 것이다.

```java
package hello.springmvc.basic.request;

@Slf4j
@Controller
public class RequestParamController {

    /**
     * 반환 타입이 없으면서 이렇게 응답에 값을 직접 집어넣으면, view 조회X
     * http://localhost:8080/request-param-v1?username=hello&age=20 실행 시켜보자.
     */
    @RequestMapping("/request-param-v1") // 이거 실행시키는게 아니고 위에꺼
    public void requestParamV1(HttpServletRequest request,
                               HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));
        log.info("username={}, age={}", username, age);

        response.getWriter().write("ok");
    }
}
```

<br/>실행하기.

![이미지](/programming/img/서50.PNG)

<br/>

`hello-form.html` 생성 해주기.

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<form action="/request-param-v1" method="post">
    username: <input type="text" name="username" />
    age: <input type="text" name="age" />
    <button type="submit">전송</button>
</form>
</body>
</html>
```

<br/>

실행 시켜보면 이렇게 나온다.

값을 입력하고 ‘전송’ 눌러보면 

![이미지](/programming/img/서51.PNG)

<br/>

이렇게 출력 되는 걸 알 수 있다.
![이미지](/programming/img/서52.PNG)


<br/>

>**Reference** <br/>스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1