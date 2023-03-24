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