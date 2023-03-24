## HTTP body에 데이터를 직접 담아서 요청 - @RequestBody

<br/>

`요청 파라미터`란? → `GET`에 쿼리스트링 오는 것, or `POST`방식으로 

HTML `Form 태그` 데이터 전송 하는 방식을 말하는 것이다. 

<br/>

둘 경우에만 `@RequestParam` 이랑 `@ModelAttribute` 를 사용하는 것이다.

그 외 경우들은 전부 `@RequestBody`를 사용하거나, 데이터를 직접 꺼내야 되는 것이다.

```
요청 파라미터 vs HTTP 메시지 바디
요청 파라미터를 조회 : @RequestParam , @ModelAttribute
HTTP 메시지 바디를 직접 조회 : @RequestBody
```

<br/>

```java
--머리에 삽입--
@ModelAttribute 는 HTTP 요청 파라미터(URL 쿼리 스트링, POST Form)를 다룰 때 사용한다.

@RequestBody 는 HTTP Body의 데이터를 객체로 변환할 때 사용한다. 
- 주로 API JSON 요청을 다룰 때 사용
```






HTTP API에서 주로 사용하며, JSON을 담아서 반환 할 수도 있고, XML, TEXT 등 가능하다.

- 주로 JSON 형식으로 사용한다.






<br/><br/>

## 예전 방식 (이런 과정을 해준다고 생각하기)

```java
@Slf4j
@Controller
public class RequestBodyStringController {
    
    @PostMapping("/request-body-string")
    public void requestBodyString(HttpServletRequest request, 
                                  HttpServletResponse response) throws IOException {
        
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        
        log.info("messageBody={}", messageBody);
        response.getWriter().write("ok");
    }
}
```

즉, 바꿔주는 것이라고 생각하자. 

(inputStream으로 문자를 읽어서, 문자로 바꾸고 하는 작업들을 자동으로 해주는 것이다.)

<br/><br/>

## 변경 방식. `@RequestBody`가 중요

```java
@ResponseBody
@PostMapping("/request-body-string")
public String requestBodyString(@RequestBody String messageBody) {
	  log.info("messageBody={}", messageBody);
	  return "ok";
}
```

```
요청 오는 것은 @RequestBody로, 응답 나가는 것은 @ResponseBody로 생각하면 된다.
```

- `@RequestBody`에 `String`으로 되어 있다면,

    - HTTP 메시지 바디 부분을 읽어서 바로 문자로 변환 후 매개변수에 넣어주는 것이다.

    - `@RequestBody` 를 사용하면 HTTP 메시지 바디 정보를 편리하게 조회할 수 있다.

- `@ResponseBody` 응답 같은 경우는 메서드 반환타입인 `String`이랑 짝이 된다고 생각하면 된다.

<br/><br/>

## JSON 형식을 보냈을 경우.

![이미지](/programming/img/입문66.PNG)

```java
콘솔창 출력 : messageBody={"username": "hello", "age": 20}
```


<br/><br/>

## Text 형식을 보냈을 경우.

![이미지](/programming/img/입문67.PNG)

```java
콘솔창 출력 : messageBody=hello
```

<br/><br/>

### 무엇이든 문자로 변환 되는 것을 알 수 있다.

반환 타입을 보면 String 타입으로 된다는 것을 알 수 있다.


```java
@ResponseBody
@PostMapping("/request-body-string")
public String requestBodyString(@RequestBody String messageBody) {

    String str = messageBody; // 반환 타입이 String이다.
    
    log.info("messageBody={}", messageBody);
    return "ok";
}
```

### HttpEntity: HTTP header, body 정보를 편리하게 조회

- 메시지 바디 정보를 직접 조회

- 요청 파라미터를 조회하는 기능과 관계 없음 `@RequestParam`, `@ModelAttribute`

<br/>

### HttpEntity는 응답에도 사용 가능

- 메시지 바디 정보 직접 반환

- 헤더 정보 포함 가능
- view 조회X




<br/><br/>

>**Reference** <br/>[스프링 MVC 2편 - 백엔드 웹 개발 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2/dashboard)