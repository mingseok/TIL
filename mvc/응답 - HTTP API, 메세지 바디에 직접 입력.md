## HTTP 응답 - HTTP API, 메세지 바디에 직접 입력

`ResponseBodyController` 클래스 생성

```java
package hello.springmvc.basic.response;

@Slf4j
@Controller
//@RestController
public class ResponseBodyController {
    @GetMapping("/response-body-string-v1")
    public void responseBodyV1(HttpServletResponse response) throws IOException {
        response.getWriter().write("ok");
    }

    @GetMapping("/response-body-string-v2")
    public ResponseEntity<String> responseBodyV2() {
        return new ResponseEntity<>("ok", HttpStatus.NOT_EXTENDED);
    }

    @ResponseBody
    @GetMapping("/response-body-string-v3")
    public String responseBodyV3() {
        return "ok";
    }

    @GetMapping("/response-body-json-v1")
    public ResponseEntity<HelloData> responseBodyJsonV1() {
        HelloData helloData = new HelloData();
        helloData.setUsername("userA");
        helloData.setAge(20);
        return new ResponseEntity<>(helloData, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @GetMapping("/response-body-json-v2")
    public HelloData responseBodyJsonV2() {
        HelloData helloData = new HelloData();
        helloData.setUsername("userA");
        helloData.setAge(20);
        return helloData;
    }
}
```

### responseBodyV1

서블릿을 직접 다룰 때 처럼

`HttpServletResponse` 객체를 통해서 HTTP 메시지 바디에 직접 ok 응답 메시지를 전달한다.

`response.getWriter().write("ok")`

<br/>

### responseBodyV2

`ResponseEntity` 엔티티는 `HttpEntity` 를 상속 받았는데, `HttpEntity`는 `HTTP` 메시지의 헤더, 

바디 정보를 가지고 있다. `ResponseEntity` 는 여기에 더해서 HTTP 응답 코드를 설정할 수 있다.

<br/>

`HttpStatus.CREATED` 로 변경하면 201 응답이 나가는 것을 확인할 수 있다.

이런식으로 설정이 가능하다.

![이미지](/programming/img/서58.PNG)

<br/>

### responseBodyV3

`@ResponseBody` 를 사용하면 `view`를 사용하지 않고, HTTP 메시지 컨버터를 통해서 HTTP 메시지를 

직접 입력할 수 있다. `ResponseEntity` 도 동일한 방식으로 동작한다.

<br/>

### responseBodyJsonV1

`ResponseEntity` 를 반환한다. HTTP 메시지 컨버터를 통해서 `JSON` 형식으로 변환되어서 반환된다.

<br/>

### responseBodyJsonV2

`ResponseEntity` 는 HTTP 응답 코드를 설정할 수 있는데, `@ResponseBody` 를 사용하면 이런 것을

설정하기 까다롭다. 

<br/>

그리하여 `@ResponseStatus(HttpStatus.OK)` 애노테이션을 사용하면 응답 코드도 설정할 수 있다.

물론 애노테이션이기 때문에 응답 코드를 동적으로 변경할 수는 없다. 프로그램 조건에 따라서 동적

으로 변경하려면 ResponseEntity 를 사용하면 된다.

<br/>

@RestController는 @Controller에 @ResponseBody가 추가된 것입니다. 

당연하게도 RestController의 주용도는 Json 형태로 객체 데이터를 반환하는 것입니다.


<br/>

### 만약 `@ResponseBody` 어너테이션을 메서드 하나하나 붙이기 싫으면 클래스에 붙이면 되는 것이다. <br/>대신 메서드 전부다 적용이 되는 것이다.

<br/>

### 그리고 만약 `@Controller` 랑 `@ResponseBody` 합치고 싶을때는? 그게 바로 `@RestController` 컨트롤러 인것이다.

<br/>

>**Reference** <br/>스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1
