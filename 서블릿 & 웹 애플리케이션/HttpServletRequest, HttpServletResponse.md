## HttpServletRequest, HttpServletResponse

<br/>

## `HttpServletRequest` 설명

### 코드는 이렇다.

```java
@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {

        String username = request.getParameter("username");
        System.out.println("username = " + username);
    }
}
```

<br/>

실행 시켜 `url`에 쿼리 파라미터를 입력 한다면

![이미지](/programming/img/입문46.PNG)

<br/>

이렇게 콘솔에 찍히는 걸 알 수 있다.

즉, 클라이언트의 값을 쉽게 가져 올 수 있다는 것이다.

![이미지](/programming/img/입문47.PNG)

<br/><br/>

## `HttpServletResponse` 설명

응답 메시지를 보내는 것이다.

여기다가 값을 넣으면, 웹 브라우저로 응답하는 http 응답 메시지에 데이터가 담겨서 나가게 되는 것이다.

<br/>

### 코드는 이렇다.

```java
@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {

        String username = request.getParameter("username");
        System.out.println("username = " + username);

        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write("hello " + username);
    }
}
```

<br/>

실행 시켜보면 이렇게 페이지에 뿌려 주는 걸 알 수 있다.

![이미지](/programming/img/입문48.PNG)



<br/><br/>

>**Reference** <br/>[스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1)
<br/>

## 궁금증!

```java
Request 와 Response 는 인터페이스인가 클래스인가
```

인터페이스다. 구현체는 서버마다 다르다.

```java
톰캣     -> org.apache.catalina.connector.RequestFacade
제티     -> org.eclipse.jetty.server.Request
언더토우 -> io.undertow.servlet.spec.HttpServletRequestImpl
```

<br/>

그래서 서버를 바꿔도 내 코드는 안 바뀐다.

```java
server:
  jetty:      # 톰캣 대신 제티를 써도
```

<br/>

앞의 PSA(Portable Service Abstraction) 글에서 본 그 구조다.

인터페이스가 표준이고 구현이 갈리는 것이다.

<br/>

## Facade 라는 이름이 붙어 있다

톰캣의 구현체 이름이 `RequestFacade` 다.

<br/>

진짜 `Request` 객체를 감싸서 필요한 것만 노출하는 것이다.

```java
Request (톰캣 내부 객체. 기능이 많다)
  <- RequestFacade (서블릿 스펙에 있는 것만 열어준다)
```

<br/>

내부 객체를 그대로 주면 톰캣 내부 메서드까지 부를 수 있게 된다.

그러면 톰캣에 묶인 코드가 생기니 한 겹 감싸는 것이다.

<br/>

앞의 프록시 패턴 글에서 본 접근 제어와 같은 발상이다.

<br/>

## 응답은 순서가 있다

헤더를 다 쓰고 나서 본문을 써야 한다.

```java
response.setHeader("X-Custom", "value");
response.setStatus(200);
response.getWriter().write("본문");
```

<br/>

본문을 먼저 쓰면 헤더 설정이 무시된다.

```java
response.getWriter().write("본문");
response.setStatus(404);              // 안 먹는다
```

<br/>

앞의 HTTP 메시지 구조 글에서 본 대로,

HTTP 응답은 상태 줄 -> 헤더 -> 빈 줄 -> 본문 순서로 나간다.

<br/>

본문을 쓰기 시작하면 이미 헤더가 나가버린 것이라 되돌릴 수가 없다.

<br/>

## 그래서 예외가 나면 응답이 깨진다

```java
response.getWriter().write("{\"data\":");    // 여기까지 나갔는데
throw new RuntimeException();                 // 예외가 났다
```

<br/>

이미 `200` 과 일부 본문이 나갔으니 `500` 으로 못 바꾼다.

<br/>

버퍼가 있어서 어느 정도는 버틴다.

```java
response.getBufferSize()      // 기본 8192 바이트
response.isCommitted()        // 이미 나갔는지 확인
response.reset()              // 아직 안 나갔으면 되돌릴 수 있다
```

<br/>

버퍼가 넘치기 전이면 `reset()` 으로 지울 수 있는 것이다.

<br/>

앞의 API 예외 처리 글에서 본 `@ExceptionHandler` 가 동작하는 것도

컨트롤러가 응답을 직접 쓰지 않고 객체만 반환하기 때문이다.

<br/>

## 인코딩을 언제 정하느냐가 중요하다

```java
response.setCharacterEncoding("UTF-8");
response.setContentType("text/html; charset=UTF-8");
PrintWriter writer = response.getWriter();       // 이 시점에 인코딩이 확정된다
```

<br/>

`getWriter()` 를 부른 뒤에 인코딩을 바꾸면 안 먹는다.

<br/>

한글이 깨지면 대부분 이 순서 문제다.

앞의 한글이 깨지는 이유 글에서 본 그 상황이다.

<br/>

요청 쪽도 마찬가지다.

```java
request.setCharacterEncoding("UTF-8");     // getParameter() 보다 먼저
```

<br/>

파라미터를 한 번 읽으면 파싱이 끝나서 인코딩을 못 바꾼다.

<br/>

스프링 부트는 `CharacterEncodingFilter` 를 기본으로 등록해준다.

앞의 필터, 스프링 인터셉터 글에서 본 필터가 제일 앞에서 이걸 해주는 것이다.

<br/>

## getWriter 와 getOutputStream 은 같이 못 쓴다

```java
response.getWriter();
response.getOutputStream();       // IllegalStateException
```

<br/>

하나를 고르면 다른 하나는 못 쓴다.

```java
getWriter()        - 문자를 쓴다. 인코딩을 알아서 해준다
getOutputStream()  - 바이트를 쓴다. 이미지나 파일
```

<br/>

앞의 스트림(Stream) 글에서 본 문자 스트림과 바이트 스트림의 구분이 여기에도 있다.

<br/>

파일을 내려줄 때는 `getOutputStream()` 을 쓴다.

이미지 바이트를 문자로 변환하면 깨지기 때문이다.
