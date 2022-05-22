## HTTP 요청 데이터 - API 메세지 바디 - JSON

### JSON 형식 전송

POST (http://localhost:8080/request-body-json)

`content-type: application/json`

`message body: {"username": "hello", "age": 20}`

<br/>

결과: messageBody = {"username": "hello", "age": 20}

json은 바로 쓰지 않고, 보통 객체로 바꿔서 사용한다.

<br/>

### `basic 폴더` 선택하고, `HelloData` 클래스를 생성해준다.

```java
package hello.servlet.basic;

@Getter @Setter
public class HelloData {

    private String username;
    private int age;

}
```

<br/>


```java
package hello.servlet.basic.request;

@WebServlet(name = "requestBodyJsonServlet", urlPatterns = "/request-body-json")
public class RequestBodyJsonServlet extends HttpServlet {

    // 잭슨 주입 받기 
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest request,
                                HttpServletResponse response) throws ServletException, IOException {

        // 데이터 읽는 방법
        // getInputStream()는. message body 부분을 bytecode로 받아냄.
        ServletInputStream inputStream = request.getInputStream();

        // StreamUtils.copyToString()는. bytecode를 문자열로 변환. 
				// 단, 인코딩 형태를 항상 명시.
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        System.out.println("messageBody = " + messageBody);

        // 객체로 뽑아낸 것처럼 동일하게 ObjectMapper 를 통해 객체를 담아서 전송한다. 
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);

				// 객체로 변환 한것이다.
        System.out.println("helloData.username = " + helloData.getUsername());
        System.out.println("helloData.age = " + helloData.getAge());

        response.getWriter().write("ok");
    }
}
```

<br/>

실행 시킨 다음에

이렇게 작성하고 선택하기, 이걸 사용하는 이유는 테스트 하기 위해 

계속 html을 만들 수 없기 때문이다.


<br/>콘솔창에 이렇게 출력 되는 걸 알 수 있다.

밑에 두개는 `HelloData 클래스`의 변수로 변환 시켜보는 것이다.

<br/>그러기 위해선 라이브러리가 필요하다. 그리하여 objectMapper 를 사용 하는 것이다.

![이미지](/programming/img/서24.PNG)

<br/>

### `getInputStream()` 설명 


• `StreamUtils.copyToString()` : bytecode를 문자열로 변환. 단, 인코딩 형태를 항상 명시.



<br/> 정리

전송할 때도 messagebody에서 `ObjectMapper`를 이용해 

객체로 뽑아낸 것처럼 동일하게 `ObjectMapper` 를 통해 객체를 담아서 전송한다. 

마찬가지로 content-type을 `application/json`으로 지정해주어야 JSON으로 반환할 수 있다.

<br/>

>**Reference** <br/>스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1