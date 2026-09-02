## HTTP 요청 메시지 - JSON

데이터를 쉽게 `'교환'` 하고 `'저장'` 하기 위한 텍스트 기반의 데이터 교환 표준.

데이터를 표시하는 표현 방법으로 어떤 식으로 데이터를 보여주는지 파악하면 된다




(JSON은 ‘한 덩러리’ 라고 생각하기)


<br/>

### 설명 :

데이터의 이름이 `"name"`이고, 값은 `"식빵"`이라는 `문자열을 갖는 JSON 데이터` 이다. → `{"name": "식빵"}`


<br/><br/>


### 이런 형식의 `JSON`이 온다.


```
{"username":"hello", "age":20}
```

<br/>

## 코드 작성

넘어올때 스프링이 `content-type: application/json` 제이슨인걸 확인한다. 
(개발자 모드(F12) 에서 확인 가능)


그렇다면, `{"username":"hello", "age":20}` 의 "형식은 JSON이겠구나” 라고 읽어서, 객체에 맞는 것으로 반환해주는 것이다.

```
API JSON을 무슨 방법을 쓰든 객체로 바뀌어야 하는 것이다.
```

<br/>

간단하게, `JSON` 형식으로 넘어 오는 걸 `HelloData` 클래스에 있는 이름에 
맞는 
`set프로퍼티`에 `저장`된다고 생각하면 된다.

```java
@ResponseBody
@PostMapping("/request-body-json")
public String requestBodyJson(@RequestBody HelloData helloData) {
    log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
    return "ok";
}
```

<br/><br/>

## 출력 시켜보기.

![이미지](/programming/img/입문68.PNG)

```
콘솔창 출력 : username=hello, age=20
```

<br/><br/>

## 이건 뭘까? (return 값이 다름)

```java
@ResponseBody
@PostMapping("/request-body-json")
public HelloData requestBodyJson(@RequestBody HelloData data) {
    log.info("username={}, age={}", data.getUsername(), data.getAge());
    return data;
}
```

설명하자면, JSON이 객체가 되었다가 → 객체가 나갈때 

다시 JSON이 되어서 클라이언트(응답)에게 보이는 것이다.


<br/>

객체를 @ResponseBody 로 return 한다면? ->
[@ResponseBody 설명 링크](https://github.com/mingseok/TIL/blob/main/Spring/API%20%EB%B0%A9%EC%8B%9D(json)%20%2C%20%40ResponseBody.md)




![이미지](/programming/img/입문69.PNG)






<br/><br/>

## 정리

- `@RequestBody` 요청

    - `JSON` 요청 → HTTP 메시지 컨버터 → 객체

- `@ResponseBody` 응답(= `return data` 말함)

    - `객체` → HTTP 메시지 컨버터 → JSON 응답


<br/><br/>

>**Reference** <br/>[스프링 MVC 2편 - 백엔드 웹 개발 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2/dashboard)
<br/>

## 궁금증!

```java
JSON 이 객체로 바뀔 때 생성자가 필요한가
```

Jackson은 기본 생성자로 객체를 만든 다음 `setter` 나 필드로 값을 채운다.

```java
public class MemberRequest {
    private String name;
    private int age;

    public MemberRequest() {}            // 이게 필요하다
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
}
```

<br/>

`final` 필드만 있는 불변 객체로 만들려면 생성자를 알려줘야 한다.

```java
public class MemberRequest {
    private final String name;

    @JsonCreator
    public MemberRequest(@JsonProperty("name") String name) {
        this.name = name;
    }
}
```

<br/>

`record` 는 이걸 자동으로 해준다. Jackson이 `record` 를 알아서 처리한다.

<br/>

## JSON 에 없는 필드가 오면

```java
{"name":"민석","nickname":"밍석"}     // nickname 이라는 필드가 클래스에 없다
```

<br/>

기본 설정에서는 예외가 난다.

```java
UnrecognizedPropertyException
```

<br/>

스프링 부트는 이걸 꺼둔다.

```java
spring.jackson.deserialization.fail-on-unknown-properties=false      # 기본값
```

<br/>

그래서 모르는 필드가 와도 그냥 무시한다.

앞의 클라이언트에서 서버로 데이터 전송 글에서 본 대로,

클라이언트가 필드를 추가해도 서버가 안 깨지게 하려는 것이다.

<br/>

## 반대로 서버가 필드를 추가하면

```java
{"name":"민석"}                    -> {"name":"민석","grade":"GOLD"}
```

<br/>

클라이언트가 모르는 필드를 받아도 보통 무시한다.

그래서 **필드 추가는 안전하고, 필드 삭제나 이름 변경은 위험하다.**

<br/>

앞의 HTTP API 설계 예시 글에서 본 버전 관리가 이래서 나오는 것이다.

<br/>

## 타입이 안 맞으면

```java
{"age":"서른"}      // int 필드에 문자열이 왔다
```

```java
HttpMessageNotReadableException
-> 400 Bad Request
```

<br/>

앞의 API 예외 처리 - 스프링이 제공하는 ExceptionResolver 글에서 본 대로,

`DefaultHandlerExceptionResolver` 가 이걸 `400` 으로 바꿔준다.

<br/>

여기서 주의할 점이 있다. 이건 `@Valid` 검증보다 먼저 일어난다.

```java
@PostMapping("/api/members")
public String create(@Valid @RequestBody MemberRequest request, BindingResult bindingResult) {
    // 타입이 안 맞으면 여기까지 못 온다
}
```

<br/>

JSON을 객체로 못 만들었으니 검증할 대상 자체가 없기 때문이다.

<br/>

앞의 Bean Validation 글에서 본 `@ModelAttribute` 와 다른 점이다.

```java
@ModelAttribute - 타입 오류가 나도 BindingResult 에 담고 컨트롤러로 온다
@RequestBody    - 아예 못 오고 400 이 나간다
```

<br/>

## 날짜는 따로 신경 써야 한다

```java
{"createdAt":"2026-09-01T10:30:00"}
```

<br/>

`LocalDateTime` 으로 받으려면 `jackson-datatype-jsr310` 이 필요하다.

스프링 부트는 기본으로 넣어준다.

<br/>

형식이 다르면 지정해야 한다.

```java
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
private LocalDateTime createdAt;
```

<br/>

앞의 스프링 타입 컨버터 글에서 본 `@DateTimeFormat` 과 헷갈리기 쉽다.

```java
@DateTimeFormat - @RequestParam, @ModelAttribute 에서 동작 (ConversionService)
@JsonFormat     - @RequestBody 에서 동작 (Jackson)
```

<br/>

담당하는 곳이 아예 다르니, 안 먹으면 어느 쪽을 쓰고 있는지부터 보면 된다.

<br/>

## 빈 문자열과 null 이 다르다

```java
{"name":""}      -> name 은 빈 문자열
{"name":null}    -> name 은 null
{}               -> name 은 null
```

<br/>

`@NotNull` 은 빈 문자열을 통과시킨다.

앞의 Bean Validation 글에서 본 대로 `@NotBlank` 를 써야 막힌다.

```java
@NotNull  - null 만 막는다. "" 는 통과
@NotEmpty - null 과 "" 를 막는다. " " 는 통과
@NotBlank - null, "", " " 전부 막는다
```
