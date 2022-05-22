## HTTP 요청 메세지 - JSON

`RequestBodyJsonController` 클래스 생성하기.

```java
package hello.springmvc.basic.request;

/**
 * {"username":"hello", "age":20}
 * content-type: application/json
 */

@Slf4j
@Controller
public class RequestBodyJsonController {

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        response.getWriter().write("ok");
    }
}
```

<br/>포스트맨으로 실행 시키기.

![이미지](/programming/img/서56.PNG)

<br/>위에 있는 코드를 줄여서 이렇게도 가능하다.

```java
@ResponseBody
@PostMapping("/request-body-json-v2")
public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {

     log.info("messageBody={}", messageBody);
     HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
     log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

     return "ok";
}
```

<br/>

### 코드를 더 줄여보면 이렇다.

```java
@ResponseBody
@PostMapping("/request-body-json-v3")
public String requestBodyJsonV3(@RequestBody HelloData helloData) {
        
     log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
     return "ok";
}
```

<br/>

### @RequestBody 객체 파라미터

- `@RequestBody HelloData data`
- `@RequestBody` 에 직접 만든 객체를 지정할 수 있다.

<br/>

`HttpEntity` , `@RequestBody` 를 사용하면 HTTP 메시지 컨버터가 HTTP 메시지 바디의 <br/>내용을 우리가 원하는 문자나 객체 등으로 변환해준다.

<br/>

HTTP 메시지 컨버터는 문자 뿐만 아니라 JSON도 객체로 변환해주는데, <br/>우리가 방금 V2에서 했던 작업을 대신 처리해준다.

<br/>자세한 내용은 뒤에 HTTP 메시지 컨버터에서 다룬다

<br/>

### @RequestBody는 생략 불가능

스프링은 `@ModelAttribute` , `@RequestParam` 해당 생략시 다음과 같은 규칙을 적용한다.

`String` , `int` , `Integer` 같은 단순 타입 = `@RequestParam`

<br/>나머지 = `@ModelAttribute` (argument resolver 로 지정해둔 타입 외)

따라서 이 경우 `HelloData`에 `@RequestBody` 를 생략하면 `@ModelAttribute` 가 적용되어버린다.

<br/>

`‘HelloData data’` → `‘@ModelAttribute HelloData data’`따라서 생략하면 

HTTP 메시지 바디가 아니라 요청 파라미터를 처리하게 된다

<br/>

### `HttpEntity` 쓸경우에는

```java
@ResponseBody
@PostMapping("/request-body-json-v3")
public String requestBodyJsonV3(HttpEntity<HelloData> httpEntity) {
     HelloData helloData = httpEntity.getBody();
     log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

     return "ok";
}
```

<br/>

## 정리

`@RequestBody` 요청

- JSON 요청 → HTTP 메시지 컨버터 → 객체

`@ResponseBody` 응답

- 객체 HTTP → 메시지 컨버터 → JSON 응답


<br/>

>**Reference** <br/>스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1