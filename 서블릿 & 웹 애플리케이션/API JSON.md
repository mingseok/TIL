## HTTP 응답 데이터 - API JSON

HelloData 클래스

```java
package hello.servlet.basic;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HelloData {

    private String username;
    private int age;
}
```

<br/>

`ResponseJsonServlet` 클래스 생성한다.

```java
package hello.servlet.basic.response;

@WebServlet(name = "responseJsonServlet",urlPatterns = "/response-json")
public class ResponseJsonServlet extends HttpServlet {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest request,
                HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        HelloData helloData = new HelloData();
        helloData.setUsername("MMMMMMM");
        helloData.setAge(777);

        // HelloData 클래스에 있는 객체를 
	// {"username":"MMMMMMM", "age":777} 이렇게 스트링(=json) 형태로 
	// 변경하고 값도 변경한 것이다.
        String result = objectMapper.writeValueAsString(helloData);
        response.getWriter().write(result);

    }
}
```

<br/>

객체를 json으로 변환해서 출력 시킨게 끝이다.

어렵지 않다.

<br/>실행 시켜보면, 이렇게 json 데이터를 웹 브라우저에 뿌릴 수 있는 것이다.

![이미지](/programming/img/서26.PNG)


<br/>

## 정리

HTTP 응답으로 JSON을 반환할 때는 `content-type`을 `application/json` 로 지정해야 한다.

Jackson(잭슨) 라이브러리가 제공하는 `objectMapper.writeValueAsString()` 를 

사용하면 객체를 JSON 문자로 변경할 수 있다.



<br/>

>**Reference** <br/>스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1
