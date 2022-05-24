## HTTP 응답 데이터 - 단순 텍스트, HTML

### 단순 텍스트 응답

- 앞에서 살펴봄 ( writer.println("ok"); )
- HTML 응답
- HTTP API - MessageBody JSON 응답

<br/>

`ResponseHtmlServlet 클래스` 생성하기.

```java
package hello.servlet.basic.response;

@WebServlet(name = "responseHtmlServlet", urlPatterns = "/response-html")
public class ResponseHtmlServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request,
                HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

        PrintWriter writer = response.getWriter();
        writer.println("<html>");
        writer.println("<body>");
        writer.println(" <div>안녕?</div>");
        writer.println("</body>");
        writer.println("</html>");

    }
}
```

<br/>이렇게 출력 되는 걸 알 수 있다.

![이미지](/programming/img/서25.PNG)

<br/>HTTP 응답으로 HTML을 반환할 때는 content-type을 text/html 로 지정해야 한다.

<br/>

>**Reference** <br/>스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1