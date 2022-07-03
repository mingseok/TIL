## API 예외 처리 - @ExceptionHandler 제일 중요

### HTML 화면 오류 vs API 오류

웹 브라우저에 HTML 화면을 제공할 때는 오류가 발생하면 BasicErrorController 를 사용하는게 편하다.

이때는 단순히 5xx, 4xx 관련된 오류 화면을 보여주면 된다. 

<br/>

`BasicErrorController` 는 이런 메커니즘을 모두 구현해두었다.

그런데 API는 각 시스템 마다 응답의 모양도 다르고, 스펙도 모두 다르다. 

예외 상황에 단순히 오류 화면을 보여주는 것이 아니라, 예외에 따라서 각각 다른 데이터를 출력해야 할 수도 있다. 

<br/>

그리고 같은 예외라고 해도 어떤 컨트롤러에서 발생했는가에 따라서 다른 예외 응답을 

내려주어야 할 수 있다. 한마디로 매우 세밀한 제어가 필요하다.

앞서 이야기했지만, 예를 들어서 상품 API와 주문 API는 오류가 발생했을 때 응답의 모양이 완전히 다를 수 있다.

<br/>

결국 지금까지 살펴본 BasicErrorController 를 사용하거나 HandlerExceptionResolver 를 

직접 구현하는 방식으로 API 예외를 다루기는 쉽지 않다

<br/>

### API 예외처리의 어려운 점

HandlerExceptionResolver 를 떠올려 보면 ModelAndView 를 반환해야 했다. 

이것은 API 응답에는 필요하지 않다.

API 응답을 위해서 HttpServletResponse 에 직접 응답 데이터를 넣어주었다. 

이것은 매우 불편하다. 

<br/>

스프링 컨트롤러에 비유하면 마치 과거 서블릿을 사용하던 시절로 돌아간 것 같다.

특정 컨트롤러에서만 발생하는 예외를 별도로 처리하기 어렵다. 

<br/>

예를 들어서 회원을 처리하는 컨트롤러에서 발생하는 RuntimeException 예외와 상품을 

관리하는 컨트롤러에서 발생하는 동일한 RuntimeException 예외를 서로 다른 방식으로 처리하고 

싶다면 어떻게 해야할까?

<br/>

## @ExceptionHandler

스프링은 API 예외 처리 문제를 해결하기 위해 @ExceptionHandler 라는 애노테이션을 

사용하는 매우 편리한 예외 처리 기능을 제공하는데, 이것이 바로 

ExceptionHandlerExceptionResolver 이다.

<br/>

스프링은 ExceptionHandlerExceptionResolver 를 기본으로 제공하고, 기본으로 제공하는

ExceptionResolver 중에 우선순위도 가장 높다. 

실무에서 API 예외 처리는 대부분 이 기능을 사용한다.

<br/>

ErrorResult 클래스

예외가 발생했을 때 API 응답으로 사용하는 객체를 정의했다.

```java
package hello.exception.exhandler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResult {

    private String code;
    private String message;

}
```

<br/>

ApiExceptionV2Controller 컨트롤러

```java
// ExceptionHandler 란??
// 이, 컨트롤러 안에서 IllegalArgumentException 예외가 터지면
// 이 메서드가 잡게 되고, new ErrorResult("BAD", e.getMessage()); 로직이
// 호출이 되는 것이다. 그리고 json으로 반환 된다.
// 그리고 IllegalArgumentException 의 자식 예외까지 처리해준다.
@ExceptionHandler(IllegalArgumentException.class)
public ErrorResult illegalExHandler(IllegalArgumentException e) {
    log.error("[exceptionHandler] ex, e");
    return new ErrorResult("BAD", e.getMessage());
}
```

### `@ExceptionHandler` 예외 처리 방법

@ExceptionHandler 애노테이션을 선언하고, 

해당 컨트롤러에서 처리하고 싶은 예외를 지정해주면 된다.

<br/>해당 컨트롤러에서 예외가 발생하면 이 메서드가 호출된다. 

참고로 지정한 예외 또는 그 예외의 자식 클래스는 모두 잡을 수 있다.

<br/>

### 실행 시켜 보면

![이미지](/programming/img/나62.PNG)

<br/>

### 그런데 순서를 잘 알아야 된다.

![이미지](/programming/img/나63.PNG)

1. 핸들러(=컨트롤러)에서 예외가 터지면 
2. ExceptionResolver 한테 예외 해결 시도를 한다. 즉, 물어보는 것이다. 뭐라고??
    
    “너가 이거 처리해 줄 수 있어?” 하고 말이다,
    
3. 그리하여 가장 먼저 요청을 받는 애가 `ExceptionHandlerExceptionResolver` 실행이 된다.
4. `ExceptionHandlerExceptionResolver` 라는 애는 컨트롤러에 `@ExceptionHandler` 라는
    
    어너테이션이 있는지 찾아 본다.
    
5. 그렇게 컨트롤러에 `@ExceptionHandler` 어너테이션이 있으면 그 어너테이션이 붙은 메서드를
    
    대신 호출 시켜준다.
    
6. 즉, `@ExceptionHandler` 붙은 메서드를 정상 흐름으로 바꿔서 리턴하게 도와주는 것이다.

이걸 의도 할때 HTTP 상태 코드도 바꾸고, 객체도 바꿔 버린 것이다. 

그리하여 정상 흐름으로 된것이다.

<br/>

### 그리고 강점이 더 있다! `IllegalArgumentException` 해놨다면 그 하위 자식 클래스를 모두 처리할 수 있다.

### 하지만 내가 원하는 건 추가로 예외 상태 코드도 바꾸고 싶은 것이다.

<br/>

코드 추가

`@ResponseStatus(HttpStatus.BAD_REQUEST)` 어너테이션을 추가 해주자.

```java
@ResponseStatus(HttpStatus.BAD_REQUEST) // 400
@ExceptionHandler(IllegalArgumentException.class)
public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.error("[exceptionHandler] ex, e");
        return new ErrorResult("BAD", e.getMessage());
}
```

<br/>

### 실행 시켜 보면

![이미지](/programming/img/나64.PNG)

<br/>

### 여기까지 정리

이렇게 하면 그림에서의 ExceptionReslover 에서 끝이 난 것이다.

그러면 서블릿 컨테이너로 올라가지 않는다 !! 지저분하게 서블릿 컨테이너로 가서 다시 또 내려오고

그러는 것이 아닌 것이다 !! 여기서 흐름이 다 끝난 것이다 !! 

<br/>즉, 여기서 정상적으로 리턴을 한것이고, json 만들어서 반환 해주고, Http 상태 코드는 400을 

적게 되는 것이다. 다시 말해 정상 흐름으로 끝이 난 것이다.

<br/>

## 다른 예제

```java
@ExceptionHandler
public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptionHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity(errorResult, HttpStatus.BAD_REQUEST);
}
```

<br/>

실행 시켜 보면 이렇다.

![이미지](/programming/img/나65.PNG)

<br/>

예외 터질 때 로직이다.

```java
if (id.equals("user-ex")) {
    throw new UserException("사용자 오류");
}
```

<br/>

## 다른 예제

```java
// @ExceptionHandler 는 이 컨트롤러 안에서만 적용 된다는 것을 까먹지 말자.
// exHandler(Exception e) 에서 Exception 이 온것이다. (최상위 예외)
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
@ExceptionHandler
public ErrorResult exHandler(Exception e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("EX", "내부 오류");
}
```

### 설명하자면 `(Exception e)` 은 개발자가 낼 수 있는 최상위 예외 이다

즉, 위에서 했던 `IllegalArgumentException` 예외 와 `UserException`  예외 말고 다른 

<br/>

예외들은 `exHandler(Exception e)` 메서드다 전부 처리하는 것이다. 어떻게??

`Exception`의 자식들 까지 전부 `exHandler(Exception e)` 메서드로 오기 때문이다.

```java
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
```

<br/>

### 실행 시켜 보면

왜냐? RuntimeException 예외는 위에 있는 두 메서드가 해결 못하고 

Exception은 해결 할 수 있기 때문이다.

![이미지](/programming/img/나66.PNG)

해당 예외 터지는 곳

```java
if (id.equals("ex")) {
     throw new RuntimeException("잘못된 사용자");
}
```

<br/>

### 우선순위

스프링의 우선순위는 항상 자세한 것이 우선권을 가진다. 

예를 들어서 부모, 자식 클래스가 있고 다음과 같이 예외가 처리된다.

```java
@ExceptionHandler(부모예외.class)
public String 부모예외처리()(부모예외 e) {}

@ExceptionHandler(자식예외.class)
public String 자식예외처리()(자식예외 e) {}
```

`@ExceptionHandler` 에 지정한 부모 클래스는 자식 클래스까지 처리할 수 있다.

따라서 자식 예외가 발생하면 부모예외처리() , 자식예외처리() 둘다 호출 대상이 된다. 

<br/>그런데 둘 중 더 자세한 것이 우선권을 가지므로 자식예외처리() 가 호출된다. 

물론 부모예외 가 호출되면 부모예외처리() 만 호출 대상이 되므로 부모예외처리() 가 호출된다.

<br/>

## IllegalArgumentException 처리

```java
@ResponseStatus(HttpStatus.BAD_REQUEST)
@ExceptionHandler(IllegalArgumentException.class)
public ErrorResult illegalExHandle(IllegalArgumentException e) {
	 log.error("[exceptionHandle] ex", e); return new ErrorResult("BAD", e.getMessage());
}
```

### 실행 흐름

- 컨트롤러를 호출한 결과 `IllegalArgumentException` 예외가 컨트롤러 밖으로 던져진다.
- 예외가 발생했으로 `ExceptionResolver` 가 작동한다. 가장 우선순위가 높은
    
    `ExceptionHandlerExceptionResolver` 가 실행된다.
    
- `ExceptionHandlerExceptionResolver` 는 해당 컨트롤러에 `IllegalArgumentException` 을 처리할
    
    수 있는 `@ExceptionHandler` 가 있는지 확인한다.
    
- `illegalExHandle()` 를 실행한다. `@RestController` 이므로 `illegalExHandle()` 에도
    
    `@ResponseBody` 가 적용된다. 따라서 HTTP 컨버터가 사용되고, 응답이 다음과 같은 JSON으로 
    
    반환 된다.
    
- @ResponseStatus(HttpStatus.BAD_REQUEST) 를 지정했으므로 HTTP 상태 코드 400으로
    
    응답한다.

<br/>    
    

### 결과

```
{
 "code": "BAD",
 "message": "잘못된 입력 값"
}
```

<br/>

## UserException 처리

```java
@ExceptionHandler
public ResponseEntity<ErrorResult> userExHandle(UserException e) {
		log.error("[exceptionHandle] ex", e);
		ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
		return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
}
```

- `@ExceptionHandler` 에 예외를 지정하지 않으면 해당 메서드 파라미터 예외를 사용한다.
    
    여기서는 `UserException` 을 사용한다.
    
- `ResponseEntity` 를 사용해서 HTTP 메시지 바디에 직접 응답한다. 물론 HTTP 컨버터가 사용된다. 

    `ResponseEntity` 를 사용하면 HTTP 응답 코드를 프로그래밍해서 동적으로 변경할 수 있다. 

    앞서 살펴본 `@ResponseStatus` 는 애노테이션이므로 HTTP 응답 코드를 동적으로 변경할 수 없다.
    
<br/>

## Exception 처리

```java
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@ExceptionHandler
public ErrorResult exHandle(Exception e) {
		 log.error("[exceptionHandle] ex", e);
		 return new ErrorResult("EX", "내부 오류");
}
```

- throw new RuntimeException("잘못된 사용자") 이 코드가 실행되면서, 컨트롤러 밖으로 RuntimeException 이 던져진다.
    
- RuntimeException 은 Exception 의 자식 클래스이다. 따라서 이 메서드가 호출된다.
- @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) 로 HTTP 상태 코드를 500으로 응답한다.

<br/>

>**Reference** <br/>스프링 MVC 2편 - 백엔드 웹 개발 활용 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2