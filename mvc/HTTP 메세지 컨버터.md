## HTTP 메세지 컨버터

### @ResponseBody 사용 원리

![이미지](/programming/img/서59.PNG)

<br/>

### @ResponseBody 를 사용

- `HTTP`의 `BODY`에 문자 내용을 직접 반환

- `viewResolver` 대신에 `HttpMessageConverter` 가 동작
- 기본 문자처리: `StringHttpMessageConverter`
- 기본 객체처리: `MappingJackson2HttpMessageConverter`
- byte 처리 등등 기타 여러 `HttpMessageConverter`가 기본으로 등록되어 있음

<br/>

### HTTP Accept 란?

클라이언트가 “나는 이런 메세지를 해석할 수 있어요” , “그러니 나한텐 JSON만 달라, html만 달라,  text로 달라” 말하는 것이다.


<br/>

### 스프링 MVC는 다음의 경우에 HTTP 메시지 컨버터를 적용한다.

`HTTP` 요청: `@RequestBody` , `HttpEntity(RequestEntity)`

`HTTP` 응답: `@ResponseBody` , `HttpEntity(ResponseEntity)`

<br/>

### HTTP 메시지 컨버터는 HttpMessageConverter 인터페이스 이다.

기본 문자처리 할때는, `StringHttpMessageConverter` 구현 클래스가 있고,

기본 객체처리 할때는, `MappingJackson2HttpMessageConverter` 구현 클래스가 있다.

<br/>

### HTTP 메시지 컨버터는 HTTP 요청, HTTP 응답 둘 다 사용된다.

`canRead()` , `canWrite()` : 메시지 컨버터가 해당 클래스, 미디어타입을 지원하는지 체크

`read()` , `write()` : 메시지 컨버터를 통해서 메시지를 읽고 쓰는 기능

<br/>

## 미디어타입이란?

content-type을 말하는 것이다.

### 몇가지 주요한 메시지 컨버터를 알아보자.

- `ByteArrayHttpMessageConverter` : `byte[]` 데이터를 처리한다.
    - 클래스 타입: `byte[]` , 미디어타입: */* (아무 미디어 타입이나 받을수 있단 뜻이다)
    - 요청 예) `@RequestBody byte[] data` → 이렇게 요청이 오면 컨버터가 `byte[]` 로 바꿔서 `data`에 넣어주는 것이다.
    - 응답 예) `@ResponseBody return byte[]` 쓰기 미디어타입 `application/octet-stream` 이렇게 반환이 되는 것이다.

<br/>

- `StringHttpMessageConverter` : `String` 문자로 데이터를 처리한다.
    - 클래스 타입: `String` , 미디어타입: */*
    - 요청 예) `@RequestBody String data`
    - 응답 예) `@ResponseBody return "ok"` 쓰기 미디어타입 `text/plain`

<br/>

- `MappingJackson2HttpMessageConverter` : `application/json` 을 주로 처리한다.
    - 클래스 타입: 객체 또는 `HashMap` , 미디어타입 `application/json` 관련
    - 요청 예) `@RequestBody HelloData data`
    - 응답 예) `@ResponseBody return helloData` 쓰기 미디어타입 `application/json` 관련
    
    <br/>

    
### HTTP 요청 데이터 읽기
    
- HTTP 요청이 왔고, 컨트롤러에서 `@RequestBody` , `HttpEntity` 파라미터를 사용한다.<br/>
    (만약, 이 두개를 사용한다면 이렇게 동작한다는  뜻이다.)
- 메시지 컨버터가 메시지를 읽을 수 있는지 확인하기 위해. 컨버터가 확인한다는 것은 <br/>이렇게 0번부터 순서대로 `canRead()` 할 수 있는지 
    확인 하는 것이다.
    
![이미지](/programming/img/서60.PNG)
    

- 대상 클래스 타입을 지원하는가.
        - 예) `@RequestBody` 의 대상 클래스 ( `byte[] , String , HelloData` )
    - `HTTP` 요청의 `Content-Type` 미디어 타입을 지원하는가.
        - 예) `text/plain , application/json , */*`
    - `canRead()` 조건을 만족하면 `read()` 를 호출해서 객체 생성하고, 반환한다.

<br/>
    
### HTTP 응답 데이터 생성
    
 - 컨트롤러에서 `@ResponseBody` , `HttpEntity` 로 값이 반환된다.
    - 메시지 컨버터가 메시지를 쓸 수 있는지 확인하기 위해 `canWrite()` 를 호출한다.

        - 대상 클래스 타입을 지원하는가.

            - 예) return의 대상 클래스 ( `byte[]` , `String` , `HelloData` )
        - HTTP 요청의 Accept 미디어 타입을 지원하는가.(더 정확히는 `@RequestMapping` 의 `produces` )
            - 예) `text/plain` , `application/json` , /

    - `canWrite()` 조건을 만족하면 `write()` 를 호출해서 HTTP 응답 메시지 바디에 데이터를 생성한다.
    
<br/>

![이미지](/programming/img/서61.PNG)
    
`StringHttpMessageConverter`
    
```java
content-type: application/json
    
@RequestMapping
void hello(@RequetsBody String data) { }
```

<br/>
    
`MappingJackson2HttpMessageConverter`
    
```java
content-type: application/json
    
@RequestMapping
void hello(@RequetsBody HelloData data) {}
```
    
안되는 경우.
```java
content-type: text/html

@RequestMapping
void hello(@RequetsBody HelloData data) {}
```

<br/>

>**Reference** <br/>스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1