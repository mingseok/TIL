## API 예외 처리 - 스프링이 제공하는 ExceptionResolver1

스프링 부트가 기본으로 제공하는 `ExceptionResolver` 는 다음과 같다.

`HandlerExceptionResolverComposite` 에 다음 순서로 등록

1. ExceptionHandlerExceptionResolver
2. ResponseStatusExceptionResolver
3. DefaultHandlerExceptionResolver 우선 순위가 가장 낮다.

<br/>

### ExceptionHandlerExceptionResolver

`@ExceptionHandler` 을 처리한다. API 예외 처리는 대부분 이 기능으로 해결한다. 

<br/>

### ResponseStatusExceptionResolver

HTTP 상태 코드를 지정해준다.

예) `@ResponseStatus(value = HttpStatus.NOT_FOUND)`

<br/>

## ResponseStatusExceptionResolver

`ResponseStatusExceptionResolver` 는 예외에 따라서 HTTP 상태 코드를 지정해주는 역할을 한다.

다음 두 가지 경우를 처리한다.

- `@ResponseStatus` 가 달려있는 예외
- `ResponseStatusException` 예외

<br/>

### 예외에 다음과 같이 @ResponseStatus 애노테이션을 적용하면 HTTP 상태 코드를 변경해준다.

```java
package hello.exception.exception;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "잘못된 요청 오류")
public class BadRequestException extends RuntimeException {
}
```

`BadRequestException` 예외가 컨트롤러 밖으로 넘어가면 `ResponseStatusExceptionResolver` 예외가

해당 애노테이션을 확인해서 오류 코드를 `HttpStatus.BAD_REQUEST (400)`으로 변경하고, 메시지도 담는다

<br/>

`ResponseStatusExceptionResolver` 코드를 확인해보면 결국 

`response.sendError(statusCode, resolvedReason)` 를 호출하는 것을 확인할 수 있다.

`sendError(400)` 를 호출했기 때문에 WAS에서 다시 오류 페이지`( /error )`를 내부 요청한다.

<br/>

### 실행 시켜 보자.

![이미지](/programming/img/나58.PNG)

<br/>

### 실행 시켜 중요한 것은 에러가 400으로 출력 되는 것이다.

1. 여기서 BadRequestException() 에러가 터지면 

```java
@GetMapping("/api/response-status-ex1")
public String responseStatusEx1() {
      throw new BadRequestException();
}
```

3. 여기로 오게 된다.

4. 그러고 Exception리졸버가 한번 본다. 
    
    아까 봤던 ResponseStatusExceptionResolver 에 걸리는 것이다.
    
    ResponseStatusExceptionResolver 의 부모는 HandlerExceptionResolver 이다.

<br/>    
    
5. 결국 정리하면 Exception 에 @ResponseStatus 어너테이션이 있네? 하면 
    
    `code = HttpStatus.BAD_REQUEST` 이랑 `reason = "잘못된 요청 오류"` 를 꺼낸 다음에
    
    (BAD_REQUEST 는 400이다)  `response.sendError()` 쪽으로 가는 것이다.
    
    즉, Exception을 먹어버리고 `response.sendError()`랑 바꿔치기 하는 것이다.
    
    그 다음 `new ModelAndView()` 로 반환 하는 것이다. → 이렇게 하면 아무것도 안넣고 반환 하면 
    
    뒤에서 부턴 정상 동작 하는 것이다.
    

```java
package hello.exception.exception;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "잘못된 요청 오류")
public class BadRequestException extends RuntimeException {
}
```

<br/>

### 결국 HttpStatus.BAD_REQUEST 에 설정 해주는 것에 따라 400, 500, 404 등등 으로 지정하는 방법이 있는 것이다.

![이미지](/programming/img/나59.PNG)

<br/>

### 메시지 기능

reason 을 MessageSource 에서 찾는 기능도 제공한다. `reason = "error.bad"`

```java
package hello.exception.exception;

//@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "잘못된 요청 오류")
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad")
public class BadRequestException extends RuntimeException {
}
```

<br/>

`messages.properties`

```java
error.bad=잘못된 요청 오류입니다. 메시지 사용
```

### 실행 결과
```
{
	"status": 400,
	 "error": "Bad Request",
	 "exception": "hello.exception.exception.BadRequestException",
	 "message": "잘못된 요청 오류입니다. 메시지 사용",
	 "path": "/api/response-status-ex1"
}
```

<br/>

>**Reference** <br/>스프링 MVC 2편 - 백엔드 웹 개발 활용 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2