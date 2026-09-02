## API (json)방식 / @ResponseBody 내부 동작

데이터를 쉽게 `'교환'` 하고 `'저장'` 하기 위한 텍스트 기반의 데이터 교환 표준을 말한다.


데이터를 표시하는 표현 방법으로 어떤 식으로 데이터를 보여주는지 파악하면 된다

<br/>

(JSON은 ‘한 덩러리’ 라고 생각하기)

```java
"json은 ‘키’, ‘벨류’ 형태로 이루어진 구조이다." -> {"username":"hello", "age":20}
```

<br/><br/>

## 객체를 `@ResponseBody` 로 `return` 한다면 어떻게 될까?

- 코드에서의 `hello` 객체를 출력 시키면 어떻게 될까?

```java
@Controller
public class HelloController {

    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name);
        return hello; // 어떻게 되는가요?
    }

    // static을 사용하면 클래스 내부에서 클래스 생성가능
    static class Hello {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
```

<br/>

‘키’는 `Hello 클래스`의 `name` 이 되는 것이고, 벨류는 내가 입력한 `스프링!!!!`이 되는 것이다.

```
그리하여 json 방식이라고 하면 이런 방식이다.
```



![이미지](/programming/img/입문532.PNG)

<br/><br/>

## API JSON을 무슨 방법을 쓰든 객체로 바뀌어야 한다.

간단하게, `JSON` 형식으로 넘어 오는 걸 `HelloData` 클래스에 있는 

이름에 맞는 `set프로퍼티`에 `저장`된다고 생각하면 된다.

```java
@ResponseBody
@PostMapping("/request-body-json")
public String requestBodyJson(@RequestBody HelloData helloData) {
    log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
    return "ok";
}
```

<br/><br/>

## return값이 객체이면?

```java
@ResponseBody
@PostMapping("/request-body-json")
public HelloData requestBodyJson(@RequestBody HelloData data) {
    log.info("username={}, age={}", data.getUsername(), data.getAge());
    return data; // ?? 뭐야 이건
}
```

설명하자면, JSON이 객체가 되었다가 → 객체가 나갈때 다시 JSON이 되어서 클라이언트(응답)에게 보이는 것이다.

<br/><br/>

## 정리

- `@RequestBody` 요청

    - `JSON` 요청 → HTTP 메시지 컨버터 → 객체

- `@ResponseBody` 응답(= `return data` 말함)

    - `객체` → HTTP 메시지 컨버터 → JSON 응답

<br/><br/>

## @ResponseBody 내부 동작

![이미지](/programming/img/입문533.PNG)

<br/>

### @ResponseBody를 사용하면?

1. http의 body에 있는 내부 문자 내용을 직접 반환한다.

2. `viewResolver` 대신에 `HttpMessageConverter`가 동작한다.

3. 기본 문자처리는 `StringHttpMessageConverter`가 처리한다.

4. 기본 객체처리는 `MappingJackson2HttpMessageConverter`가 처리한다.

5. byte 처리 등등 기타 여러 `HttpMessageConverter`가 기본으로 등록되어 있다.
<br/>

## 궁금증!

```java
@ResponseBody 를 붙이면 JSON 이 되는데, 그 변환은 정확히 어디서 일어날까?
```

앞의 핸들러 어댑터 글에서 본 `ReturnValueHandler` 와 `HttpMessageConverter` 가 순서대로 일한다.

```java
컨트롤러가 Hello 객체를 반환
  -> RequestMappingHandlerAdapter 가 받는다
  -> "이 반환값을 처리할 ReturnValueHandler 가 누구지?"
     -> @ResponseBody 가 붙어 있으니 RequestResponseBodyMethodProcessor
  -> 그 핸들러가 HttpMessageConverter 에게 넘긴다
     -> "Hello 객체를 application/json 으로 바꿀 수 있는 컨버터가 누구지?"
     -> Jackson 컨버터가 손을 든다
  -> Jackson 이 {"name":"스프링!!!!"} 문자열을 만들어 응답 본문에 직접 쓴다
```

<br/>

여기서 뷰 리졸버는 아예 안 불린다.

원문의 `hello` 라는 문자열이 뷰 이름이 아니라 데이터로 취급되는 이유가 이것이다.

```java
@ResponseBody 없음 -> 반환값을 뷰 이름으로 본다 -> ViewResolver 로 간다
@ResponseBody 있음 -> 반환값을 데이터로 본다   -> HttpMessageConverter 로 간다
```

<br/>

## Jackson 은 getter 를 보고 JSON 을 만든다

원문 코드에서 `getName()` 이 있기 때문에 `name` 이라는 키가 나온 것이다.

필드 이름이 아니라 게터 이름에서 `get` 을 떼고 첫 글자를 소문자로 바꾼 것이 키가 된다.

```java
public String getName()      -> "name"
public String getUserName()  -> "userName"
public boolean isActive()    -> "active"        (boolean 은 is 를 뗀다)
```

<br/>

그래서 이런 일이 생긴다.

```java
static class Hello {
    private String name;

    public String getFullName() {     // 게터 이름을 바꿨다
        return name;
    }
}
```

```java
{"fullName":"스프링!!!!"}       // 키가 fullName 이 된다
```

<br/>

필드는 그대로인데 게터 이름만 바꿔도 API 응답의 키가 바뀐다.

앞의 DTO 글에서 `엔티티를 그대로 반환하지 말라` 고 한 이유 중 하나가 이것이다.

<br/>

키 이름을 고정하고 싶으면 어노테이션으로 못 박는다.

```java
@JsonProperty("name")
public String getFullName() { ... }
```

<br/>

## 게터가 없으면 어떻게 되는가

```java
static class Hello {
    private String name;      // 게터가 없다
}
```

```java
InvalidDefinitionException: No serializer found for class Hello
and no properties discovered to create BeanSerializer
```

<br/>

Jackson 이 꺼낼 방법이 없어서 예외가 난다.

`private` 필드는 밖에서 읽을 수 없고, 게터도 없으니 아무것도 못 만드는 것이다.

<br/>

반대로 요청을 받을 때는 기본 생성자와 세터(또는 필드 접근)가 필요하다.

앞의 생성자 글에서 본 `JPA 엔티티에 기본 생성자가 필요한 이유` 와 같은 맥락이다.

```java
응답할 때 -> 게터가 필요하다
요청받을 때 -> 기본 생성자가 필요하다 (또는 @JsonCreator 로 생성자를 지정)
```

<br/>

## @RestController 는 이것을 묶은 것이다

메서드마다 `@ResponseBody` 를 적는 것이 번거로워서 만들어진 어노테이션이다.

```java
@Controller
@ResponseBody
public @interface RestController { }
```

<br/>

`@RestController` 를 열어보면 실제로 이 둘이 붙어 있다.

클래스에 붙이면 그 안의 모든 메서드가 `@ResponseBody` 인 것처럼 동작한다.

```java
@RestController
public class HelloApi {

    @GetMapping("/hello-api")
    public Hello helloApi(@RequestParam String name) {   // @ResponseBody 를 안 적어도 된다
        ...
    }
}
```

<br/>

## JSON 이 표준이 된 이유

원문의 `데이터 교환 표준` 이라는 말에 이유를 붙이면 이렇다.

<br/>

예전에는 XML을 썼다. 같은 데이터가 이만큼 차이 난다.

```java
<user><name>hello</name><age>20</age></user>     // 45자
{"name":"hello","age":20}                        // 25자
```

<br/>

그리고 JSON은 자바스크립트 객체 표기법에서 나와서, 브라우저에서 파싱하는 비용이 거의 없다.

XML은 파서를 따로 돌려야 한다.

<br/>

다만 JSON에도 약점이 있다. 타입 정보가 거의 없다.

```java
{"age":20}      // 숫자인지 문자열인지는 알 수 있지만
{"date":"2026-01-01"}   // 이건 그냥 문자열이다. 날짜라는 정보가 없다
```

<br/>

그래서 날짜를 주고받을 때 형식을 맞추는 문제가 자주 생긴다.

`ObjectMapper` 에 `JavaTimeModule` 을 등록하는 설정이 이걸 처리하기 위한 것이다.
