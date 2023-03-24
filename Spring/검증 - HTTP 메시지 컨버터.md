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