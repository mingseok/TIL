## HTTP 메시지 컨버터

<br/>

뷰 템플릿으로 HTML을 생성해서 응답하는 것이 아니라, HTTP API처럼 

JSON 데이터를 HTTP 메시지 바디에서 직접 읽거나 쓰는 경우 HTTP 메시지 컨버터를 사용하면 편리하다.

![이미지](/programming/img/입문100.PNG)

<br/><br/>

## viewResolver 대신에 HttpMessageConverter 가 동작

- `기본 문자처리:` StringHttpMessageConverter

- `기본 객체처리:` MappingJackson2HttpMessageConverter

<br/><br/>

## 스프링 MVC는 다음의 경우에 HTTP 메시지 컨버터를 적용한다.

- `HTTP 요청:` `@RequestBody`

- `HTTP 응답:` `@ResponseBody`

<br/><br/>

## HTTP 메시지 컨버터 인터페이스

HTTP 메시지 컨버터는 `HTTP 요청`, `HTTP 응답` 둘 다 사용된다

- HTTP 응답에 있는 메시지 바디를 읽어서 객체로 바꾸고 컨트롤러에 파라미터로 넘겨주는 역할.
- 또 하나는, 컨트롤러에서 `@ResponseBody` 으로 되어 있으면, 리턴값 가지고 HTTP 응답 메시지에도 넣는 역할을 하는 것이다.
    

```
그리하여 컨버터가 양방향인 것이다.
```

<br/><br/>

## 스프링 부트는 스프링이 실행될 때 메시지 컨버터 몇가지 등록한다.

```
1번째: ByteArrayHttpMessageConverter

2번째: StringHttpMessageConverter

3번째: MappingJackson2HttpMessageConverter
```

<br/><br/>

## 중요한 메시지 컨버터

- ByteArrayHttpMessageConverter : `byte[] 데이터를 처리한다.`

    - 요청 예) `@RequestBody` byte[] data

    - 응답 예) `@ResponseBody` return byte[]

<br/>

- StringHttpMessageConverter : `String 문자로 데이터를 처리한다.`

    - 요청 예) `@RequestBody String data`

    - 응답 예) `@ResponseBody return "ok"` 쓰기 미디어타입 text/plain

```
content-type: application/json // 이런 형식으로 온다는 것

void hello(@RequestBody String data) {...}
```

<br/>

- MappingJackson2HttpMessageConverter : `application/json 주로 처리`

    - 요청 예) `@RequestBody HelloData data`

    - 응답 예) `@ResponseBody return helloData` 쓰기 미디어타입 application/json 관련

```
content-type: application/json // 이런 형식으로 온다는 것

void hello(@RequestBody HelloData data) {...}
```

<br/><br/>

## 안되는 케이스

```
content-type: text/html // 이런 형식으로 온다는 것

void hello(@RequestBody HelloData data) {}
```

안되는 이유는, 맨 처음 부터 `ByteArrayHttpMessageConverter` 비교해보니 패스 → 

그 다음 `StringHttpMessageConverter` 비교해 String 아니라서 패스  → 

그 다음 `MappingJackson2HttpMessageConverter` 보니.. `객체 타입`은 맞는데 

`미디어 타입`이 `application/json` 관련이 아니라서 이것도 또한 실패 하는 것이다.


<br/><br/>

>**Reference** <br/>[스프링 MVC 2편 - 백엔드 웹 개발 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2/dashboard)