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

<br/><br/>

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

<br/>

## 궁금증!

```java
@RequestBody 는 왜 한 번밖에 못 읽나
```

본문이 스트림이기 때문이다.

```java
InputStream body = request.getInputStream();
```

<br/>

스트림은 흘러가는 물 같은 것이라, 한 번 읽으면 끝이다.

되감을 수가 없다.

<br/>

그래서 이런 코드는 두 번째에서 빈 값이 나온다.

```java
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
    String body = readBody(request);        // 필터에서 로그를 찍으려고 읽었다
    log.info("body = {}", body);
    chain.doFilter(request, response);      // 컨트롤러에서는 빈 본문을 받는다
}
```

<br/>

앞의 필터, 스프링 인터셉터 글에서 본 필터에서 본문 로그를 남기려다 자주 겪는 일이다.

<br/>

## 해결하려면 감싸야 한다

```java
ContentCachingRequestWrapper wrapper = new ContentCachingRequestWrapper(request);
chain.doFilter(wrapper, response);
byte[] cached = wrapper.getContentAsByteArray();     // 읽은 것을 보관해둔다
```

<br/>

읽으면서 복사본을 만들어두는 방식이다.

<br/>

`HttpServletRequestWrapper` 를 상속한 것인데,

앞의 데코레이터 패턴 관점에서 보면 원본을 감싸서 기능을 덧붙인 것이다.

<br/>

## @RequestParam 은 왜 여러 번 읽히나

이건 스트림이 아니기 때문이다.

```java
GET /members?name=민석      -> URL 에 붙어 있다. 파싱해서 Map 에 담아둔다
POST + form                 -> 본문이지만 서블릿이 미리 읽어서 Map 에 담는다
```

<br/>

`request.getParameter()` 는 이미 파싱된 `Map` 을 조회하는 것이라 몇 번이든 된다.

<br/>

앞의 클라이언트에서 서버로 데이터 전송 글에서 본 대로

`application/x-www-form-urlencoded` 는 `name=민석&age=30` 형태라 파싱이 쉽다.

JSON은 구조가 자유로워서 서블릿이 미리 파싱해두지 않는 것이다.

<br/>

## HttpEntity 로 받으면 헤더까지 같이 온다

```java
@PostMapping("/api/members")
public String create(HttpEntity<MemberRequest> entity) {
    MemberRequest body = entity.getBody();
    HttpHeaders headers = entity.getHeaders();
}
```

<br/>

`RequestEntity` 를 쓰면 URL과 메서드까지 볼 수 있다.

<br/>

응답 쪽의 짝이 `ResponseEntity` 다.

```java
HttpEntity     - 본문 + 헤더
RequestEntity  - + URL, 메서드
ResponseEntity - + 상태 코드
```

<br/>

## String 으로 받으면 원문이 온다

```java
@PostMapping("/api/raw")
public String raw(@RequestBody String body) {
    // {"name":"민석"} 이라는 글자 그대로
}
```

<br/>

앞의 API 방식(json), @ResponseBody 글에서 본 `StringHttpMessageConverter` 가 처리한다.

<br/>

객체로 바꾸지 않고 원문이 필요할 때 쓴다.

앞의 결제 웹훅처럼 서명을 검증해야 하는 경우가 그렇다.

```java
서명은 원문 바이트로 계산한다
-> 객체로 바꿨다가 다시 JSON 으로 만들면 공백이나 순서가 달라져 서명이 안 맞는다
```

<br/>

## @RequestBody 는 생략할 수 없다

```java
@PostMapping("/api/members")
public String create(MemberRequest request) {      // @RequestBody 를 안 붙였다
```

<br/>

이러면 `@ModelAttribute` 로 동작한다.

<br/>

앞의 HTTP 요청 파라미터 - @ModelAttribute 글에서 본 규칙이다.

```java
단순 타입 (String, int 등)  -> @RequestParam 으로 본다
그 외 객체                  -> @ModelAttribute 로 본다
```

<br/>

JSON을 보냈는데 필드가 전부 `null` 로 들어오면 이걸 의심하면 된다.

`@ModelAttribute` 는 쿼리 파라미터를 찾는데 본문에 JSON이 들어 있으니 채울 게 없는 것이다.

<br/>

`@RequestParam` 은 생략해도 되고 `@RequestBody` 는 생략하면 안 된다는 것을

한 번 겪어보면 안 잊는다.
