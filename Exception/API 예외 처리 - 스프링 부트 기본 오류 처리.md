## API 예외 처리 - 스프링 부트 기본 오류 처리

API 예외 처리도 스프링 부트가 제공하는 기본 오류 방식을 사용할 수 있다.

스프링 부트가 제공하는 BasicErrorController 코드를 보자.

<br/>

`BasicErrorController` 코드

```java
@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {}

@RequestMapping
public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {}
```

`/error` 동일한 경로를 처리하는 `errorHtml()` , `error()` 두 메서드를 확인할 수 있다.

- `errorHtml() : produces = MediaType.TEXT_HTML_VALUE` : 클라이언트 요청의 Accept 해더 값이
    
    text/html 인 경우에는 `errorHtml()` 을 호출해서 view를 제공한다.
    
- `error()` : 그외 경우에 호출되고 `ResponseEntity` 로 HTTP Body에 JSON 데이터를 반환한다.

<br/>

## 스프링 부트의 예외 처리

앞서 학습 했듯이 스프링 부트의 기본 설정은 오류 발생시 /error 를 오류 페이지로 요청한다.

BasicErrorController 는 이 경로를 기본으로 받는다.

( server.error.path 로 수정 가능, 기본 경로 /error )

<br/>

## Html 페이지 vs API 오류

BasicErrorController 를 확장하면 JSON 메시지도 변경할 수 있다. 

그런데 API 오류는 조금 뒤에 설명할 @ExceptionHandler 가 제공하는 기능을 사용하는 것이 

더 나은 방법이므로 지금은 BasicErrorController 를 확장해서 JSON 오류 메시지를 

변경할 수 있다 정도로만 이해해두자.

<br/>

스프링 부트가 제공하는 BasicErrorController 는 HTML 페이지를 제공하는 경우에는 매우 편리하다.

4xx, 5xx 등등 모두 잘 처리해준다. 

그런데 API 오류 처리는 다른 차원의 이야기이다. 

API 마다, 각각의 컨트롤러나 예외마다 서로 다른 응답 결과를 출력해야 할 수도 있다. 

<br/>

예를 들어서 회원과 관련된 API에서 예외가 발생할 때 응답과, 상품과 관련된 API에서 발생하는 

예외에 따라 그 결과가 달라질 수 있다. 

결과적으로 매우 세밀하고 복잡하다. 

따라서 이 방법은 HTML 화면을 처리할 때 사용하고, API는 오류 처리는 뒤에서 설명할 `@ExceptionHandler` 를 사용하자.

<br/>

### 그렇다면 복잡한 API는 오류는 어떻게 처리해야 하는지 지금부터 하나씩 알아보자.


<br/>

## API 예외 처리 - HandlerExceptionResolver 시작

목표

예외가 발생해서 서블릿을 넘어 WAS까지 예외가 전달되면 HTTP 상태코드가 500으로 처리된다.

발생하는 예외에 따라서 400, 404 등등 다른 상태코드도 처리하고 싶다.

오류 메시지, 형식등을 API마다 다르게 처리하고 싶다.

<br/>

상태코드 변환

예를 들어서 IllegalArgumentException 을 처리하지 못해서 컨트롤러 밖으로 넘어가는 

일이 발생하면 HTTP 상태코드를 400으로 처리하고 싶다. 어떻게 해야할까?

ApiExceptionController 클래스

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
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
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

localhost:8080/api/members/bad 라고 호출하면 `IllegalArgumentException`이   발생하도록 했다

<br/>

### 실행해보면 상태 코드가 500인 것을 확인할 수 있다.

이제 이걸 수정 해보자.

```
{
 "status": 500,
 "error": "Internal Server Error",
 "exception": "java.lang.IllegalArgumentException",
 "path": "/api/members/bad"
}
```

<br/>

## HandlerExceptionResolver

스프링 MVC는 컨트롤러(핸들러) 밖으로 예외가 던져진 경우 예외를 해결하고, 

동작을 새로 정의할 수 있는 방법을 제공한다. 

컨트롤러 밖으로 던져진 예외를 해결하고, 동작 방식을 변경하고 싶으면 HandlerExceptionResolver 를 사용하면 된다. 

줄여서 ExceptionResolver 라 한다.

<br/>

### ExceptionResolver 적용 전

![이미지](/programming/img/나53.PNG)

<br/>

### ExceptionResolver 적용 후

![이미지](/programming/img/나54.PNG)

참고: ExceptionResolver 로 예외를 해결해도 postHandle() 은 호출되지 않는다.

ExceptionResolver가 예외를 해결 하도록 시도를 한다.

<br/>

만약 해결이 된다면 정상 동작을 하고 DispatcherServlet 에서 다시 정상 응답으로 나간다.

즉, 핸들러에서 발생한 예외를 잡아서 정상 동작하게 만들어 주는 ‘해결사’ 라고 보면 되는 것이다.

<br/>

`DispatcherServlet` 에서 `WAS` 한테 갔을때 WAS는 “엇, `sendError()` 호출이 됬네?” 하면서

오류 페이지를 뒤적뒤적 하는 것이다.

<br/>

MyHandlerExceptionResolver

주석 잘보기.

```java
package hello.exception.resolver;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Object handler,
                                         Exception ex) {

        try {

            if (ex instanceof IllegalArgumentException) {

                log.info("IllegalArgumentException resolver to 400");

				// HttpServletResponse.SC_BAD_REQUEST 가 400으로 변경 되는 것이다.
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());

				// ModelAndView 리턴 하는데 아무것도 없는 새로운 ModelAndView 반환 하는 것이고,
				// 6번인 View는 하지 않는 것이다. 그리고 바로 DispatcherServlet 로 간다.
                return new ModelAndView();
            }

        } catch (IOException e) {

            log.error("resolver ex", e);
        }

        return null;
    }
}
```

ExceptionResolver 가 ModelAndView 를 반환하는 이유는 마치 try, catch를 하듯이, 

Exception 을 처리해서 정상 흐름 처럼 변경하는 것이 목적이다. 

<br/>

이름 그대로 Exception 을 Resolver(해결)하는 것이 목적이다.

여기서는 `IllegalArgumentException` 이 발생하면 `response.sendError(400)` 를 호출해서 


HTTP 상태 코드를 400으로 지정하고, 빈 ModelAndView 를 반환한다.

<br/>

WebConfig 클래스에 등록 해줘야 동작한다.

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver>resolvers) {
     resolvers.add(new MyHandlerExceptionResolver());
}
```

<br/>

### 실행 시켜보면,

![이미지](/programming/img/나55.PNG)

<br/>

### 반환 값에 따른 동작 방식

HandlerExceptionResolver 의 반환 값에 따른 DispatcherServlet 의 동작 방식은 다음과 같다

- 빈 `ModelAndView`: new ModelAndView() 처럼 빈 ModelAndView 를 반환하면 뷰를 렌더링
    
    하지 않고, 정상 흐름으로 서블릿이 리턴된다.
    
- `ModelAndView 지정`: ModelAndView 에 View , Model 등의 정보를 지정해서 반환하면
    
    뷰를 렌더링 한다.
    
- `null`: null 을 반환하면, 다음 ExceptionResolver 를 찾아서 실행한다.
    
    만약 처리할 수 있는 ExceptionResolver 가 없으면 예외 처리가 안되고, 기존에 발생한 예외를 
    
    서블릿 밖으로 던진다.


<br/>
    

### ExceptionResolver 활용

- 예외 상태 코드 변환
    - 예외를 `response.sendError(xxx)` 호출로 변경해서 서블릿에서 상태 코드에 따른 오류를 처리하도록 위임
        
- 이후 WAS는 서블릿 오류 페이지를 찾아서 내부 호출, 예를 들어서 스프링 부트가 기본으로 설정한 `/error` 가 호출됨
    
- 뷰 템플릿 처리
    - `ModelAndView` 에 값을 채워서 예외에 따른 새로운 오류 화면 뷰 렌더링 해서 고객에게 제공
- API 응답 처리
    - `response.getWriter().println("hello");` 처럼 HTTP 응답 바디에 직접 데이터를 넣어주는
        
        것도 가능하다. 여기에 JSON 으로 응답하면 API 응답 처리를 할 수 있다.


<br/>

>**Reference** <br/>스프링 MVC 2편 - 백엔드 웹 개발 활용 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2