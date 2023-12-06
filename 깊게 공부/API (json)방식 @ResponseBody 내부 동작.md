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