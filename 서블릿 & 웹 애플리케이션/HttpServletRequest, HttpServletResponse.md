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