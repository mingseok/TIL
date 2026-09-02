## 검증 - HTTP 메시지 컨버터

<br/>

`@Valid` , `@Validated` 는 `HttpMessageConverter(@RequestBody)`에도 적용할 수 있다.


<br/>

## 머리에 삽입

```java
@ModelAttribute 는 HTTP 요청 파라미터(URL 쿼리 스트링, POST Form)를 다룰 때 사용한다.

@RequestBody 는 HTTP Body의 데이터를 객체로 변환할 때 사용한다. 
- 주로 API JSON 요청을 다룰 때 사용한다.
```

<br/><br/>

## 컨트롤러

```java
@Slf4j
@RestController
@RequestMapping("/validation/api/items")
public class ValidationItemApiController {

    @PostMapping("/add")
    public Object addItem(@RequestBody @Validated ItemSaveForm form, 
                                       BindingResult bindingResult) {

        log.info("API 컨트롤러 호출");

        // JSON API가 온것이다.
        if (bindingResult.hasErrors()) {
            log.info("검증 오류 발생 errors={}", bindingResult);
            return bindingResult.getAllErrors();
        }

        log.info("성공 로직 실행");
        return form;
    }
}
```

<br/><br/>

## 성공 케이스

![이미지](/programming/img/입문130.PNG)

<br/>

### 출력 확인

![이미지](/programming/img/입문131.PNG)

<br/>

### 콘솔창 확인

```
API 컨트롤러 호출
성공 로직 실행
```

<br/><br/>

## 실패 케이스

price에 숫자가 들어가야 되는데, 문자가 입력 되었다.

![이미지](/programming/img/입문132.PNG)

<br/>

### 출력 확인

![이미지](/programming/img/입문133.PNG)

<br/>

### 콘솔창 확인

컨트롤러 조차 호출이 되지 않는 걸 알 수 있다.

```
아무것도 출력 안됨.
```

<br/><br/>

## 알아야 될 것은.

`API JSON`을 무슨 방법을 쓰든 `객체`로 바뀌어야 하는 것이다.

이유는? → 컨트롤러로 넘어오는 `addItem()` 메서드에서 `@RequestBody ItemSaveForm form` 으로 되어 있기 때문이다. 

(객체로 바뀌어야 @Validated 을 할 수 있는데, 객체를 바뀌는 단계 조차 못하고 있는 것이다.)

<br/><br/>

## API의 경우 3가지 경우를 나누어 생각해야 한다.

- 성공 요청: 성공

- 실패 요청: JSON을 객체로 생성하는 것 자체가 실패함

- 검증 오류 요청: JSON을 객체로 생성하는 것은 성공했고, 검증에서 실패함

## @ModelAttribute vs @RequestBody

- HTTP 요청 파리미터를 처리하는 `@ModelAttribute` 는 각각의 필드 단위로 세밀하게 적용된다

- HttpMessageConverter 는 @ModelAttribute 와 다르게 각각의 필드 단위로 
적용되는 것이 아니라, 

    전체 객체 단위로 적용된다.



<br/><br/>

>**Reference** <br/>[스프링 MVC 2편 - 백엔드 웹 개발 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2/dashboard)
<br/>

## 궁금증!

```java
"3가지 경우를 나누어 생각해야 한다" 는데, 왜 API 만 이렇게 복잡할까?
```

`@ModelAttribute` 와 `@RequestBody` 의 실패 지점이 다르기 때문이다.

```java
@ModelAttribute -> 필드 단위로 하나씩 바인딩한다
                   price 가 실패해도 itemName 은 성공한다
                   실패한 것만 BindingResult 에 담고 컨트롤러는 호출된다

@RequestBody    -> JSON 전체를 한 번에 객체로 만든다
                   하나라도 실패하면 객체 자체가 안 만들어진다
                   컨트롤러가 호출되지 않는다
```

<br/>

이 차이가 원문의 세 가지 경우를 만든다.

```java
1. 성공                    -> 컨트롤러 호출됨, BindingResult 비어 있음
2. 검증 오류 (형식은 맞음)   -> 컨트롤러 호출됨, BindingResult 에 오류
3. JSON 파싱 실패           -> 컨트롤러 호출 안 됨, 예외 발생
```

<br/>

## 3번을 컨트롤러에서 못 잡는 이유

```java
{"itemName":"상품", "price":"abc", "quantity":10}
```

<br/>

`price` 에 `"abc"` 가 들어왔다. Jackson이 이걸 `Integer` 로 못 바꾼다.

<br/>

`@ModelAttribute` 라면 그 필드만 포기하고 나머지는 채운 뒤 컨트롤러로 보낸다.

`@RequestBody` 는 객체를 통째로 만드는 방식이라, 중간에 실패하면 넘길 객체가 없다.

<br/>

객체가 없으니 컨트롤러 메서드를 부를 수가 없고, `BindingResult` 에 담을 대상도 없다.

그래서 그 앞에서 예외가 던져진다.

```java
HttpMessageNotReadableException
```

<br/>

## 그래서 3번은 예외 처리로 잡는다

앞의 API 예외 처리 글에서 본 `@ExceptionHandler` 자리다.

```java
@RestControllerAdvice
public class ApiExceptionAdvice {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResult> parseError(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResult("BAD_REQUEST", "요청 형식이 올바르지 않습니다"));
    }
}
```

<br/>

이걸 안 만들어두면 스프링 기본 응답이 나간다.

`400` 은 맞게 나가지만, 응답 형식이 우리 API의 다른 오류들과 달라진다.

```java
// 우리가 만든 오류 응답
{"code":"BAD_REQUEST","message":"..."}

// 스프링 기본 오류 응답
{"timestamp":"...","status":400,"error":"Bad Request","path":"/api/items"}
```

<br/>

클라이언트 입장에서는 같은 API인데 오류 형식이 두 가지가 되어버린다.

<br/>

## 2번은 왜 BindingResult 로 잡히는가

`@RequestBody` 도 객체를 만드는 데 성공했다면 그 뒤에 검증이 돈다.

```java
{"itemName":"", "price":100, "quantity":10}
```

<br/>

형식은 다 맞다. `itemName` 이 빈 문자열이고 `price` 가 `1000` 미만일 뿐이다.

객체는 정상적으로 만들어졌으니, 그 객체를 대상으로 Bean Validation이 돈다.

<br/>

이때는 `BindingResult` 를 받으면 오류가 담긴다.

```java
@PostMapping("/api/items")
public Object addItem(@RequestBody @Validated ItemSaveForm form, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
        return bindingResult.getAllErrors();
    }
    ...
}
```

<br/>

`BindingResult` 를 안 받으면 `MethodArgumentNotValidException` 이 던져진다.

API에서는 이쪽을 쓰는 경우가 많다. 오류 응답을 한 곳에서 통일할 수 있기 때문이다.

```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ErrorResult> validationError(MethodArgumentNotValidException e) {
    List<FieldError> errors = e.getBindingResult().getFieldErrors();
    // 필드별 오류를 모아서 응답 만들기
}
```

<br/>

## 화면과 API 의 처리 방식이 갈리는 지점

```java
화면 -> BindingResult 로 받아서 입력 화면으로 되돌린다
        사용자가 다시 고쳐야 하니, 입력값도 같이 보여줘야 한다

API  -> 예외로 던지고 @ControllerAdvice 에서 한 번에 처리한다
        클라이언트가 오류 응답을 받아서 알아서 처리한다
```

<br/>

앞의 Form 전송 객체 분리 글에서 화면용 폼과 도메인을 나눴던 것처럼,

여기서도 `누가 이 오류를 보는가` 에 따라 처리 방식이 갈린다.

<br/>

원문의 `@ModelAttribute vs @RequestBody` 결론이 이 차이를 정리한 것이다.
