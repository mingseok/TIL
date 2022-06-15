## @ControllerAdvice

`@ExceptionHandler` 를 사용해서 예외를 깔끔하게 처리할 수 있게 되었지만, 

정상 코드와 예외 처리 코드가 하나의 컨트롤러에 섞여 있다. 



`@ControllerAdvice` 또는 `@RestControllerAdvice` 를 사용하면 둘을 분리할 수 있다.

<br/>

ExControllerAdvice

이렇게 하면 여러 컨트롤러에서 발생 하는 오류들을 모아 여기에서 다 처리해 주는 것이다.

```java
package hello.exception.exhandler.advice;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

    // 이렇게 하면 여러 컨트롤러에서 발생 하는 오류들을 모아 여기에서 다 처리해 주는 것이다.

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.error("[exceptionHandler] ex, e");
        return new ErrorResult("BAD", e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptionHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }
}
```



### Postman 실행 시켜 보면 똑같이 동작하는 걸 알 수 있다.

<br/>

## @ControllerAdvice

`@ControllerAdvice` 는 대상으로 지정한 여러 컨트롤러에 `@ExceptionHandler` , `@InitBinder` 기능을

부여해주는 역할을 한다.

<br/>

`@ControllerAdvice` 에 대상을 지정하지 않으면 모든 컨트롤러에 적용된다. (글로벌 적용)

`@RestControllerAdvice` 는 `@ControllerAdvice` 와 같고, `@ResponseBody` 가 추가되어 있다.



`@Controller` , `@RestController` 의 차이와 같다.

<br/>

## 대상 컨트롤러 지정 방법

```java
1번. 어너테이션에 따라 지정하는 방법
// Target all Controllers annotated with @RestController
@ControllerAdvice(annotations = RestController.class)
public class ExampleAdvice1 {}


2번.패키지 방법이다 글로벌 하게 사용 많이 사용한다.
// Target all Controllers within specific packages 
@ControllerAdvice("org.example.controllers")
public class ExampleAdvice2 {}


3번. 직접 지정하는 방법이다. 직접 컨트롤러를 지정하여 사용한다.
// Target all Controllers assignable to specific classes
@ControllerAdvice(assignableTypes = {ControllerInterface.class,
AbstractController.class})
public class ExampleAdvice3 {}
```

<br/>

### 글로벌 패키지를 사용하는 방법은 이렇다.

### ApiExceptionV2Controller

```java
package hello.exception.api;

@Slf4j
@RestController
public class ApiExceptionV2Controller {

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
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
}
```

<br/>

### ApiExceptionV3Controller

```java
package hello.exception.api;

@Slf4j
@RestController
public class ApiExceptionV3Controller {

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
				private String name;
		}

    @GetMapping("/api3/members/{id}")
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

        return new MemberDto(id,"hello " + id);
		}
}

```

<br/>

### ExControllerAdvice

`@RestControllerAdvice(basePackages = "hello.exception.api")` 이렇게 하면 위에 작성한 

 `ApiExceptionV2Controller`, `ApiExceptionV3Controller` 두 가지 다 적용되는 것을 알 수 있다.

똑같이 적용 되는 것이다.

```java
package hello.exception.exhandler.advice;

@Slf4j
@RestControllerAdvice(basePackages = "hello.exception.api")
public class ExControllerAdvice {

    // 이렇게 하면 여러 컨트롤러에서 발생 하는 오류들을 모아 여기에서 다 처리해 주는 것이다.

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.error("[exceptionHandler] ex, e");
        return new ErrorResult("BAD", e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptionHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }

}
```

<br/>

>**Reference** <br/>스프링 MVC 2편 - 백엔드 웹 개발 활용 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2