## HandlerExceptionResolver 활용

예외를 여기서 마무리하기

예외가 발생하면 WAS까지 예외가 던져지고, WAS에서 오류 페이지 정보를 찾아서 

다시 /error 를 호출하는 과정은 생각해보면 너무 복잡하다. 

<br/>

ExceptionResolver 를 활용하면 예외가 발생했을 때 이런 복잡한 과정 없이 여기에서 

문제를 깔끔하게 해결할 수 있다.

<br/>

UserException 예외 처리나는 클래스 하나 만들기

```java
package hello.exception.exception;

public class UserException extends RuntimeException {

    public UserException() {
        super();
    }

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserException(Throwable cause) {
        super(cause);
    }

    protected UserException(String message, Throwable cause,
                            boolean enableSuppression,
                            boolean writableStackTrace) {

        super(message, cause, enableSuppression, writableStackTrace);
    }
}
```

<br/>

localhost:8080/api/members/user-ex 호출시 UserException 이 발생하도록 해두었다.

```java
package hello.exception.api;

@Slf4j
@RestController
public class ApiExceptionController {

    @GetMapping("/api/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {

				```	
			
        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
        }

        ```
}
```

UserHandlerExceptionResolver 클래스

<br/>

HTTP 요청 해더의 ACCEPT 값이 application/json 이면 JSON으로 오류를 내려주고, 

그 외 경우에는 error/500에 있는 HTML 오류 페이지를 보여준다

- `String acceptHeader = request.getHeader("accept");` : http 메시지 타입을 비교하는 것이다
- `response.setStatus(HttpServletResponse.SC_BAD_REQUEST);` : 응답으로 나갈땐 400으로 나간다.
- `String result = objectMapper.writeValueAsString(errorResult);` : objectMapper에 있는
    
    메서드로 errorResult를 String으로 변환했다.
    


```java
package hello.exception.resolver;

@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Object handler,
                                         Exception ex) {

        try {

            if (ex instanceof UserException) {
                log.info("UserException resolver to 400");

                String acceptHeader = request.getHeader("accept");//http 메시지 타입을 비교하는 것이다
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 응답으로 나갈땐 400으로 나간다.

                if ("application/json".equals(acceptHeader)) {
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("ex", ex.getClass());
                    errorResult.put("message", ex.getMessage());

                    String result = objectMapper.writeValueAsString(errorResult); //objectMapper에 있는 메서드로
                                                                                  //errorResult를 String으로 변환했다.
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().write(result);
                    return new ModelAndView();// 렌더링 하지 않는 케이스

                } else {
                    return new ModelAndView("error/500"); // 렌더링 하는 케이스
                }

            }
            

        } catch(IOException e) {
            log.error("resolver ex", e);
        }

        return null;
    }
}
```

<br/>

## 실행

POSTMAN 에서 실행 시키기

![이미지](/programming/img/나56.PNG)

<br/>

## 정리

`ExceptionResolver` 를 사용하면 컨트롤러에서 예외가 발생해도 `ExceptionResolver` 에서 예외를 처리해버린다.

따라서 예외가 발생해도 서블릿 컨테이너까지 예외가 전달되지 않고, 

스프링 MVC에서 예외 처리는 끝이 난다.

결과적으로 WAS 입장에서는 정상 처리가 된 것이다. 

<br/>

### 이렇게 예외를 이곳에서 모두 처리할 수 있다는 것이 핵심이다.

서블릿 컨테이너까지 예외가 올라가면 복잡하고 지저분하게 추가 프로세스가 실행된다. 

반면에 ExceptionResolver 를 사용하면 예외처리가 상당히 깔끔해진다.

<br/>

그런데 직접 ExceptionResolver 를 구현하려고 하니 상당히 복잡하다. 

지금부터 스프링이 제공하는 ExceptionResolver 들을 알아보자.

![이미지](/programming/img/나57.PNG)

<br/>

>**Reference** <br/>스프링 MVC 2편 - 백엔드 웹 개발 활용 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2