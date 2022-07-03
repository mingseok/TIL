## API 예외 처리 - 시작

목표
API 예외 처리는 어떻게 해야할까?

HTML 페이지의 경우 지금까지 설명했던 것 처럼 4xx, 5xx와 같은 오류 페이지만 있으면 

대부분의 문제를 해결할 수 있다.

<br/>

그런데 API의 경우에는 생각할 내용이 더 많다. 오류 페이지는 단순히 고객에게 

오류 화면을 보여주고 끝이지만, API는 각 오류 상황에 맞는 오류 응답 스펙을 정하고, 

JSON으로 데이터를 내려주어야 한다. 

<br/>

즉, 정확한 데이터를 뿌려줘야 한다.

지금부터 API의 경우 어떻게 예외 처리를 하면 좋은지 알아보자.

API도 오류 페이지에서 설명했던 것 처럼 처음으로 돌아가서 서블릿 오류 페이지로 

방식을 사용해보자.

<br/>

WebServerCustomizer 클래스가 다시 사용되도록 하기 위해 `@Component` 애노테이션에 

있는 주석을 풀자. 이제 WAS에 예외가 전달되거나, `response.sendError()` 가 호출되면 위에 

등록한 예외 페이지 경로가 호출된다.

<br/>

### ApiExceptionController 작성

```java
package hello.exception.api;

@Slf4j
@RestController
public class ApiExceptionController {

    @GetMapping("/api/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {
        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }
        return new MemberDto(id, "hello " + id);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }
}
```

단순히 회원을 조회하는 기능을 하나 만들었다. 

예외 테스트를 위해 URL에 전달된 id 의 값이 ex 이면 예외가 발생하도록 코드를 심어두었다.

<br/>

## Postman으로 테스트

HTTP Header에 Accept 가 application/json 인 것을 꼭 확인하자.

![이미지](/programming/img/나45.PNG)

<br/>

![이미지](/programming/img/나46.PNG)

실행 결과를 보면 Dto 형식으로 나오는 걸 알 수 있다.

왜그런가? if문 return 쪽을 보면 `return new MemberDto(id, "hello " + id);` 

new MemberDto 라고 되어 있기 때문이다.

```java
static class MemberDto {
   private String memberId;
   private String name;
}
```

<br/>

### 중요한 것은 여기서 예외를 발생 시켜 보겠다.

이렇게 html 형식으로 출력 되는 걸 알 수 있다.

즉, 예외가 발생해도 “json으로 발생 했습니다” 라고 알려줘야 되는 것이다.

![이미지](/programming/img/나47.PNG)

<br/>

API를 요청했는데, 정상의 경우 API로 JSON 형식으로 데이터가 정상 반환된다. 

그런데 오류가 발생하면 우리가 미리 만들어둔 오류 페이지 HTML이 반환된다. 

이것은 기대하는 바가 아니다. 클라이언트는 정상 요청이든, 오류 요청이든 JSON이 반환되기를 기대한다.

<br/>

웹 브라우저가 아닌 이상 HTML을 직접 받아서 할 수 있는 것은 별로 없다.

### 문제를 해결하려면 오류 페이지 컨트롤러도 JSON 응답을 할 수 있도록 수정해야 한다

```java
@RequestMapping(value = "/error-page/500", produces 
													= MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<Map<String, Object>> errorPage500Api(HttpServletRequest request,
                                                           HttpServletResponse response) {

        log.info("API errorPage 500");

        // 이제 result에 값만 넣으면 json으로 반환이 될 것이다.
        Map<String, Object> result = new HashMap<>();
        Exception ex = (Exception) request.getAttribute(ERROR_EXCEPTION);
        result.put("status", request.getAttribute(ERROR_STATUS_CODE));
        result.put("message", ex.getMessage());

        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        return new ResponseEntity<>(result, HttpStatus.valueOf(statusCode));
}
```

`produces = MediaType.APPLICATION_JSON_VALUE` 의 뜻은 클라이언트가 요청하는 HTTP Header의

Accept 의 값이 application/json 일 때 해당 메서드가 호출된다는 것이다. 

<br/>결국 클라어인트가 받고 싶은 미디어타입이 json이면 이 컨트롤러의 메서드가 호출된다.

응답 데이터를 위해서 Map 을 만들고 status , message 키에 값을 할당했다. 

<br/>Jackson 라이브러리는 Map 을 JSON 구조로 변환할 수 있다.

ResponseEntity 를 사용해서 응답하기 때문에 메시지 컨버터가 동작하면서 

클라이언트에 JSON이 반환된다.

<br/>

### 포스트맨을 통해서 다시 테스트 해보자.

HTTP Header에 `Accept` 가 `application/json` 인 것을 꼭 확인하자.

<br/>

### 실행 시켜보면 json 으로 출력 되는 걸 알 수 있다.

HTTP Header에 Accept 가 application/json 이 아니면, 기존 오류 응답인 HTML 응답이 출력되는 것을 확인할 수 있다.

![이미지](/programming/img/나48.PNG)

<br/>

## 흐름 설명.

1. http://localhost:8080/api/members/ex 웹 브라우저로 실행 시키면 
2. 여기서 에러 throw new RuntimeException("잘못된 사용자"); 가 터진 것이다. 그리고
    
    ```java
    public class ApiExceptionController {
    
        @GetMapping("/api/members/{id}")
        public MemberDto getMember(@PathVariable("id") String id) {
            if (id.equals("ex")) {
                throw new RuntimeException("잘못된 사용자");
            }
            return new MemberDto(id, "hello " + id);
        }
    ```

<br/> 


3. 여기서 RuntimeException 으로 담겨져 있는 것이다. 뭐가??
    
    "잘못된 사용자" 라는 메시지를 가지고 있는 것이다.
    

```java
@Component
public class WebServerCustomizer implements
WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

		``
	
    ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500");
    
}
```

<br/>

4. 그리고 둘중 하나를 선택 해야 되는 것이다. 
    
    이유는 똑같은 "/error-page/500" 이기 때문이다.
    
    produces = MediaType.APPLICATION_JSON_VALUE 라고 되어 있다면?
    
    밑에 코드 처럼 똑같은 @RequestMappring(”/error-page/500) 있다고 생각해보자.
    
    그렇다면 둘 중 뭘 사용해야 될지 모를 것이다. 하지만 우선 순위를 정할 수 있다.
    
    ![이미지](/programming/img/나49.PNG)
    
    Accept 가 뭔가?? 클라이언트 에서 내가 받을 수 있는 타입이 뭐야? 하는 것이다.
    
    <br/>이렇게 Accept 가 application/json 으로 되어 있다면 application/json 으로 되어 
    있는 것을 실행 시키는 것이다.
    
    “클라이언트에서 내가 받아 드릴 수 있는게 application/json 이야!” 라고 말하는 것이다
    
    그러면 produces 가 붙은 애가 우선 순위를 가지게 되는 것이다.
    
    
    
    ```java
    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
    
            log.info("errorPage 500");
    
            printErrorInfo(request);
            return "error-page/500";
    }
    
    @RequestMapping(value = "/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> errorPage500Api(HttpServletRequest request,
                                                               HttpServletResponse response) {
    
            log.info("API errorPage 500");
    
            // 이제 result에 값만 넣으면 json으로 반환이 될 것이다.
            Map<String, Object> result = new HashMap<>();
            Exception ex = (Exception) request.getAttribute(ERROR_EXCEPTION);
            result.put("status", request.getAttribute(ERROR_STATUS_CODE));
            result.put("message", ex.getMessage());
    
            Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
            return new ResponseEntity<>(result, HttpStatus.valueOf(statusCode));
    }
    ```
    

5. 그렇게 `errorPage500Api()` 메서드가 호출이 되고, log를 찍는다.

![이미지](/programming/img/나50.PNG)

<br/>

6. 그 다음 result 라는 맵에다가.
    
    저장 시키는 거다 뭘?
    

```java
result.put("status", request.getAttribute(ERROR_STATUS_CODE));
result.put("message", ex.getMessage());
```

<br/>

7. "status" 라는 이름과 “message” 이름은 여기에 있는 것이다.

![이미지](/programming/img/나51.PNG)



8. 이건 `return new ResponseEntity<>(result**,** HttpStatus.*valueOf*(statusCode))**;**`
    
    작성 해주었기 때문에. 상태 코드 들어가는 것이기 때문에 이렇게 출력 되는 것이다.

    ![이미지](/programming/img/나52.PNG)

<br/>

>**Reference** <br/>스프링 MVC 2편 - 백엔드 웹 개발 활용 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2    