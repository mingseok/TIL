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
<br/>

## 궁금증!

```java
"컨버터를 몇 가지 등록한다" 고 했는데, 실제로 몇 개나 있을까?
```

`spring-web` jar 안을 열어보면 이렇게 들어 있다.

```java
ByteArrayHttpMessageConverter            - byte[]
StringHttpMessageConverter               - String
FormHttpMessageConverter                 - form 데이터
BufferedImageHttpMessageConverter        - 이미지
AbstractJacksonHttpMessageConverter      - JSON (Jackson 계열)
AbstractKotlinSerializationHttpMessageConverter - 코틀린 직렬화
```

<br/>

원문에서 든 세 개 외에도 여러 개가 있고, 클래스패스에 어떤 라이브러리가 있느냐에 따라 등록되는 것이 달라진다.

`Jackson` 이 없으면 JSON 컨버터도 등록되지 않는다.

<br/>

## "안되는 케이스" 를 더 정확히 보면

원문에서 든 그 흐름이 실제 코드에서는 이렇게 생겼다.

```java
for (HttpMessageConverter converter : 컨버터목록) {
    if (converter.canRead(대상클래스, 요청의 Content-Type)) {
        return converter.read(대상클래스, 요청);
    }
}
throw new HttpMediaTypeNotSupportedException();
```

<br/>

`canRead()` 가 두 가지를 같이 본다는 것이 핵심이다.

```java
canRead(클래스, 미디어타입)
  -> 이 클래스로 바꿀 수 있나?      (StringHttpMessageConverter 는 String 만)
  -> 이 미디어 타입을 다룰 수 있나?  (Jackson 은 application/json 계열만)
```

<br/>

둘 다 맞아야 통과한다. 원문의 예에서 실패한 이유가 두 번째 조건이다.

```java
Content-Type: text/html + HelloData 객체
  -> ByteArray  : 클래스가 byte[] 가 아니다        (탈락)
  -> String     : 클래스가 String 이 아니다         (탈락)
  -> Jackson    : 클래스는 되는데 text/html 이다    (탈락)
  -> 남은 컨버터 없음 -> 415 Unsupported Media Type
```

<br/>

응답할 때는 `canWrite()` 로 같은 일을 하는데, 미디어 타입을 요청의 `Accept` 헤더에서 가져온다.

```java
요청을 읽을 때 -> Content-Type 헤더를 본다 (내가 보낸 것이 무엇인지)
응답을 쓸 때   -> Accept 헤더를 본다       (상대가 무엇을 받고 싶은지)
```

<br/>

## 순서가 중요하다

컨버터 목록은 순서가 있고, 앞에서부터 물어본다.

앞쪽이 더 구체적인 것, 뒤쪽이 더 포괄적인 것으로 정렬되어 있다.

<br/>

`StringHttpMessageConverter` 가 `MappingJackson2HttpMessageConverter` 보다 앞에 있는 이유가 있다.

```java
@PostMapping("/hello")
void hello(@RequestBody String data) { }
```

<br/>

`Content-Type: application/json` 인데 파라미터가 `String` 이면,

Jackson도 처리할 수 있고 String 컨버터도 처리할 수 있다.

<br/>

이때 앞에 있는 String 컨버터가 먼저 잡아서, JSON 문자열을 그대로 `String` 에 넣어준다.

원문에 적힌 그 예가 이 순서 덕분에 동작하는 것이다.

<br/>

## 직접 컨버터를 끼울 수도 있다

앞의 핸들러 어댑터 글에서 본 것처럼 전부 인터페이스라 갈아끼울 수 있다.

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0, new MyCsvHttpMessageConverter());   // 맨 앞에 끼운다
    }
}
```

<br/>

`configureMessageConverters` 와 `extendMessageConverters` 두 개가 있는데 차이가 있다.

```java
configureMessageConverters -> 기본 컨버터를 전부 없애고 내가 준 것만 쓴다
extendMessageConverters    -> 기본 컨버터는 그대로 두고 추가하거나 순서만 바꾼다
```

<br/>

보통은 `extend` 쪽을 쓴다. `configure` 를 쓰면 기본으로 되던 것들이 갑자기 안 된다.

<br/>

## Jackson 설정을 바꾸고 싶을 때

날짜 형식이나 `null` 필드 처리를 바꾸려면 컨버터를 새로 만들 필요가 없다.

컨버터가 쓰는 `ObjectMapper` 를 빈으로 등록하면 된다.

```java
@Bean
public ObjectMapper objectMapper() {
    return new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
}
```

<br/>

앞의 스프링 부트 글에서 본 `@ConditionalOnMissingBean` 이 여기서 작동한다.

내가 `ObjectMapper` 를 등록하면 부트가 만들던 기본 것이 사라지고 내 것이 쓰인다.

<br/>

## 뷰 리졸버와 갈라지는 지점

원문 첫머리의 `뷰 템플릿 대신` 이라는 말이 정확한 갈림길이다.

```java
@Controller + String 반환         -> ViewResolver 로 간다 (화면을 만든다)
@Controller + @ResponseBody       -> HttpMessageConverter 로 간다 (본문에 바로 쓴다)
@RestController                   -> 전부 @ResponseBody 라 항상 컨버터로 간다
```

<br/>

`@RestController` 는 `@Controller + @ResponseBody` 를 합쳐놓은 것이다.

API 서버를 만들 때 메서드마다 `@ResponseBody` 를 안 적어도 되게 만든 편의 어노테이션이다.
