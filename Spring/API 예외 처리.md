## API 예외 처리

<br/>

## ExceptionResolver 란?

핸들러에서 발생한 예외를 잡아서 정상적으로 처리 할 수 있도록 도와주는 것으로 이해하자.

(=해결사) `(postHandle()는 호출되지 않는다)`

![이미지](/programming/img/입문150.PNG)

<br/><br/>

## ExceptionResolver

ExceptionResolver 를 사용하면 컨트롤러에서 예외가 발생해도 ExceptionResolver 에서 예외를 처리해 버린다. 

따라서 예외가 발생해도 서블릿 컨테이너까지 예외가 전달되지 않고, 

스프링 mvc에서 예외 처리는 끝이 난다. 

<br/>

결과적으로 WAS 입장에서는 정상 처리가 된 것이다.

```
이렇게 예외를 이곳에서 모두 처리할 수 있다는 것이 핵심이다.
```

<br/><br/>

## @ExceptionHandler

스프링은 API 예외 처리 문제를 해결하기 위해 `@ExceptionHandler` 라는 

애노테이션을 사용하는 매우 편리한 예외 처리 기능을 제공하는데, 

이것이 바로 `ExceptionHandlerExceptionResolver` 이다.

<br/><br/>

## 사용 방법

`@ExceptionHandler(IllegalArgumentException.class)` : 무엇인가?

- 현재 컨트롤러에서 `IllegalArgumentException` 예외가 발생하면
이 메서드가 잡아주는 것이다.
    
- 추가로 현재 컨트롤러가 `@RestController` 이므로 반환은 그대로 제이슨으로 반환해준다.
    

```java
@RestController
public class ApiExceptionController {

    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {
        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
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

<br/><br/>

### 출력 시켜보기.

![이미지](/programming/img/입문151.PNG)

결국 정상적으로 반환 해주게 되는 것이다.

<br/><br/>

### 웹에서 출력.

![이미지](/programming/img/입문152.PNG)


<br/><br/>

## 실행 흐름

- 컨트롤러를 호출한 결과 `IllegalArgumentException` 예외가 컨트롤러 밖으로 던져진다.
    
- 예외가 발생했음으로 `ExceptionResolver` 가 작동한다.
    
    가장 우선순위가 높은 `ExceptionHandlerExceptionResolver` 가 실행된다.
    
- `ExceptionHandlerExceptionResolver` 는 해당 컨트롤러 `IllegalArgumentException` 을 

    처리할 수 있는 `@ExceptionHandler` 가 있는지 확인한다.
- `illegalExHandle()` 를 실행한다. `@RestController` 이므로 `illegalExHandle()` 에도 

    `@ResponseBody` 가 적용된다. 따라서 HTTP 컨버터가 사용되고, 
응답이 다음과 같은 `JSON`으로 반환된다.
- `@ResponseStatus(HttpStatus.BAD_REQUEST)` 를 지정했으므로 HTTP 상태 코드 `400`으로 응답한다.


<br/><br/>

## 만약, 200 시리즈로 말고 변경하고 싶다면?

`@ResponseStatus(HttpStatus.BAD_REQUEST)` : 추가 해주면 되는 것이다.

`BAD_REQUEST` : 안으로 들어가 보면 400, 401, 402, 403, .. 등등 많이 있다. 

```java
@RestController
public class ApiExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        // ..
    }

        // ..
}
```

<br/><br/>

### 출력 시켜보기

![이미지](/programming/img/입문153.PNG)

<br/><br/>

## 추가로 알고 있어야 될 것.

이 로직은, 가장 넓은 범위의 `(Exception e)` 으로 처리하는 것이다.

즉, 위에서 `illegalExHandler()` 등등 메서드들이 있다고 생각해보자. 

<br/>

여러가지 메서드들이 있음에도, 생각지도 못한 예외가 발생한다면 문제가 된다.

그리하여. 그걸 잡아주는 것이 `exHandle()` 메서드 인것이다.

```java
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@ExceptionHandler
public ErrorResult exHandle(Exception e) {
    log.error("[exceptionHandle] ex", e);
    return new ErrorResult("EX", "내부 오류");
}
```

<br/>

만약, `IllegalArgumentException` 예외가 터지고 `exHandle`과 `illegalExHandle` 둘다

있는 상황에서 우선 순위는? → 똑같이 자세한 것부터 이다. `illegalExHandle` 가 호출 되는 것이다.

<br/><br/>

## 이런 방식으로도 가능하다.

`@ExceptionHandler` 에 예외를 지정하지 않으면 해당 메서드 파라미터 예외를 사용한다.

```java
@Slf4j
@RestController
public class ApiExceptionController {

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandle(UserException e) {
        log.error("[exceptionHandle] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {
        
        // .. 생략

        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
        }
        return new MemberDto(id, "hello " + id);
    }
}

// .. ErrorResult 클래스
@Data
@AllArgsConstructor
public class ErrorResult {
    private String code;
    private String message;
}
```

<br/><br/>

## @ControllerAdvice

`@ExceptionHandler` 를 사용해서 예외를 깔끔하게 처리할 수 있게 되었지만, 

정상 코드와 예외 처리 코드가 하나의 컨트롤러에 섞여 있다. 

```
@ControllerAdvice 또는 @RestControllerAdvice 를 사용하면 둘을 분리할 수 있다.
```

<br/>


이렇게 한다면 여러 컨트롤러에서 발생하는 인셉션들을 여기에서 처리해 주는 것이다.

그리하여 기존에 컨트롤러에 있던 코드를 지우고, 여기로 옮긴 것이다.

```java
@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandle(IllegalArgumentException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandle(UserException e) {
        log.error("[exceptionHandle] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandle(Exception e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }
}
```

- `@ControllerAdvice` 는 대상으로 지정한 여러 컨트롤러에 `@ExceptionHandler` , `@InitBinder`
    
    기능을 부여해주는 역할을 한다. (대상을 지정하지 않으면 모든 컨트롤러에 적용된다)
    
- `@RestControllerAdvice` 는 `@ControllerAdvice` 와 같고, `@ResponseBody` 가 추가되어 있다.
    
    `@Controller` , `@RestController` 의 차이와 같다


<br/><br/>

## 대상 컨트롤러 지정 방법

패키지 지정의 경우 해당 패키지와 그 하위에 있는 컨트롤러가 대상이 된다

```java
@ControllerAdvice("org.example.controllers")
public class ExampleAdvice2 {...}
```

<br/>

## 궁금증!

```java
@ExceptionHandler 는 어떻게 우리 메서드를 찾아서 부르는 걸까?
```

앞의 디스패처 서블릿 글에서 본 예외 갈래에 그 답이 있다.

```java
doDispatch()
  try {
      핸들러 실행
  } catch (Exception e) {
      dispatchException = e;              // 던지지 않고 잡아둔다
  }
  processDispatchResult(..., dispatchException)
      -> HandlerExceptionResolver 목록에게 순서대로 물어본다
```

<br/>

컨트롤러에서 예외가 나도 그 자리에서 위로 던지지 않는다.

`DispatcherServlet` 이 일단 잡아두고, 처리할 수 있는 리졸버를 찾는다.

<br/>

기본으로 등록된 리졸버가 셋이다.

```java
ExceptionHandlerExceptionResolver   -> @ExceptionHandler 를 찾아서 실행    (우선순위 1)
ResponseStatusExceptionResolver     -> @ResponseStatus 를 보고 상태 코드 결정 (2)
DefaultHandlerExceptionResolver     -> 스프링 내부 예외를 상태 코드로 변환    (3)
```

<br/>

`@ExceptionHandler` 가 제일 먼저 물어보는 대상이다.

그래서 우리가 만든 핸들러가 있으면 나머지는 아예 안 불린다.

<br/>

## 어떤 핸들러를 고르는가

여러 개가 있으면 `가장 구체적인 것` 을 고른다.

```java
@ExceptionHandler(IllegalArgumentException.class)
public ErrorResult a(IllegalArgumentException e) { ... }

@ExceptionHandler(RuntimeException.class)
public ErrorResult b(RuntimeException e) { ... }

@ExceptionHandler(Exception.class)
public ErrorResult c(Exception e) { ... }
```

<br/>

`IllegalArgumentException` 이 터지면 셋 다 처리할 수 있다.

`IllegalArgumentException` 은 `RuntimeException` 이고 `Exception` 이기도 하기 때문이다.

<br/>

이때 스프링은 상속 계층에서 가장 가까운 `a` 를 고른다.

원문의 `핵심은 구체적인 것에서 덜 구체적인 것으로` 와 같은 규칙이다.

<br/>

## 그래서 Exception.class 핸들러는 맨 마지막 보루다

```java
@ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResult> unexpected(Exception e) {
    log.error("예상 못 한 예외", e);         // 반드시 로그를 남긴다
    return ResponseEntity.status(500)
            .body(new ErrorResult("SERVER_ERROR", "잠시 후 다시 시도해주세요"));
}
```

<br/>

이걸 안 두면 처리 못 한 예외가 톰캣까지 올라가서 기본 오류 페이지가 나간다.

API인데 HTML이 응답으로 나가는 셈이라, 클라이언트가 파싱에 실패한다.

<br/>

주의할 점이 하나 있다. 여기서 예외 메시지를 그대로 내려주면 안 된다.

```java
return new ErrorResult("ERROR", e.getMessage());   // 위험하다
```

<br/>

`SQLException` 의 메시지에는 테이블 이름과 컬럼 이름이 그대로 들어 있다.

`NullPointerException` 은 내부 클래스 이름을 노출한다.

<br/>

밖으로는 안전한 문구만 내보내고, 진짜 원인은 로그에만 남기는 것이 맞다.

앞의 예외 글에서 본 `원인을 담아 던져야 한다` 는 로그를 위한 것이고, 응답은 별개다.

```java
로그에는  -> 원인 예외까지 전부 (개발자가 본다)
응답에는  -> 사용자가 이해할 문구만 (누구나 본다)
```

<br/>

## @ControllerAdvice 가 필요한 이유

`@ExceptionHandler` 를 컨트롤러 안에 두면 그 컨트롤러에서만 동작한다.

컨트롤러가 스무 개면 같은 핸들러를 스무 번 적어야 한다.

<br/>

`@ControllerAdvice` 는 그것을 한 곳으로 모은 것이다.

```java
@RestControllerAdvice
public class ApiExceptionAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResult> badRequest(IllegalArgumentException e) { ... }
}
```

<br/>

`@RestControllerAdvice` 는 `@ControllerAdvice + @ResponseBody` 다.

앞의 `@RestController` 와 같은 방식으로 묶어놓은 것이다.

<br/>

원문의 `대상 컨트롤러 지정 방법` 을 쓰면 범위를 좁힐 수도 있다.

```java
@RestControllerAdvice(basePackages = "com.example.api")     // 이 패키지만
@RestControllerAdvice(annotations = RestController.class)   // @RestController 만
```

<br/>

화면용 컨트롤러와 API 컨트롤러가 섞여 있을 때 유용하다.

화면 쪽은 오류 페이지로 보내고, API 쪽은 JSON을 내려줘야 하니 처리가 다르기 때문이다.

<br/>

## @ResponseStatus 로 될 일도 있다

간단한 경우는 예외 클래스에 붙이는 것만으로 끝난다.

```java
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "회원을 찾을 수 없습니다")
public class MemberNotFoundException extends RuntimeException { }
```

<br/>

`ResponseStatusExceptionResolver` 가 이걸 읽어서 `404` 로 응답한다.

핸들러를 따로 안 만들어도 된다.

<br/>

다만 응답 본문을 마음대로 못 만든다는 한계가 있다.

에러 코드나 필드별 오류 같은 것을 담으려면 결국 `@ExceptionHandler` 를 써야 한다.

```java
상태 코드만 바꾸면 됨       -> @ResponseStatus
응답 본문까지 만들어야 함   -> @ExceptionHandler
여러 컨트롤러에서 공통으로   -> @ControllerAdvice
```
