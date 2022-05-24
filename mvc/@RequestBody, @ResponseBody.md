## @RequestBody, @ResponseBody

### @RequestBody
@RequestBody 를 사용하면 HTTP 메시지 바디 정보를 편리하게 조회할 수 있다.

<br/>참고로 헤더 정보가 필요하다면 HttpEntity 를 사용하거나 @RequestHeader 를 사용하면 된다.


이렇게 메시지 바디를 직접 조회하는 기능은 요청 파라미터를 조회하는 @RequestParam, @ModelAttribute 와는 전혀 관계가 없다.

<br/>쉽게 생각해서 메세지 바디에 있는걸 다이렉트로 ‘콱’ 잡아서 가져 오는 것이다.

![이미지](/programming/img/서55.PNG)

<br/>

```java

@ResponseBody
@PostMapping("/request-body-string-v4")
public String requestBodyStringV3(@RequestBody String messageBody) {

    log.info("messageBody={}", messageBody);

    return "ok";
}

```

<br/><br/>

## @ResponseBody


`@ResponseBody` : `View` 조회를 무시하고, `HTTP message body`에 직접 해당 내용 입력


만약 @ResponseBody를 사용하지 않고 아래와 같이 코드를 작성하면, helloworld.jsp 라는 찾는다.


하지만, helloworld.jsp를 생성하지 않았기 때문에, view 페이지를 찾지 못해서 아래와 같이 500 Internal Server Error 가 발생한다.


![이미지](/programming/img/갸3.PNG)

<br/>

메소드에 @ResponseBody 로 어노테이션이 되어 있다면 메소드에서 리턴되는 값은 View 를 통해서 출력되지 않고 HTTP Response Body 에 직접 쓰여지게 됩니다.
출처: https://ismydream.tistory.com/140 [창조적고찰:티스토리]


<br/>

### @ResponseBody 작성 해주면

```java

@ResponseBody
@RequestMapping(value = "/helloworld", method = RequestMethod.GET)
public String helloWorld() {
    return "helloworld";
}
```

<br/>

이렇게 출력이 가능한 것이다.

![이미지](/programming/img/갸4.PNG)



## 정리

요청 파라미터를 조회 할때는 이걸 사용: @RequestParam , @ModelAttribute

<br/>

HTTP 메시지 바디를 직접 조회 할때는 이걸 사용: @RequestBody

<br/>

@ResponseBody 를 사용하면 응답 결과를 HTTP 메시지 바디에 직접 담아서 전달할 수 있다.<br/>
물론 이 경우에도 view를 사용하지 않는다.


<br/>

>**Reference** <br/>스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1