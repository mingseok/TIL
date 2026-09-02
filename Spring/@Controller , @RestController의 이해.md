## `@Controller` 설명

스프링이 처음에 실행 될 때 (스프링 컨테이너라고 부른다.)

스프링 컨테이너라는 `‘통’` 이 생긴다.

<br/>

거기서 `@Controller` 어너테이션이 달려 있는 `HelloController.class` 등등 .. 

객체를 생성해서 스프링 컨테이너란 ‘통’에 넣어 둔다.

그리고 스프링이 관리 하는 것이다. 



```
`이것을 스프링 컨테이너에서 '스프링 빈'을 관리 한다고 부른다`
```

<br/>


### 스프링 빈을 등록할 때, 유일하게 하나만 등록한다. (싱글톤)

따라서 같은 스프링 빈이면 모두 같은 인스턴스인 것이다.

<br/><br/>

## @Controller 정리

- 스프링이 자동으로 스프링 빈으로 등록한다.

    - 내부에 @Component 애노테이션이 있어서 컴포넌트 스캔의 대상이 됨

- 스프링 MVC에서 애노테이션 기반 컨트롤러로 인식한다.

<br/><br/>

## `@RestController` 설명

쉽게 생각하면, `@Controller` 랑 `@ResponseBody` 합친 것이 바로 `@RestController` 컨트롤러인 것이다.

```java
@RestController
public class LogTestController {

    @RequestMapping("/log-test")
    public String logTest() {
        return "ok";
    }
}

```

- 스프링은 기본적으로 @Controller 라고 하면, 반환 되는 것은 뷰 이름이 반환 된다.

<br/>

즉, 위 코드 처럼 `@RestController` 사용하면 리턴 타입인 String이므로 문자가 반환 된다.

```html
따라서 실행 화면에서 바로 'ok'를 확인 할 수 있다.
```


<br/><br/>


>**Reference** <br/>[스프링 핵심 원리 - 기본편](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8?utm_source=google&utm_medium=cpc&utm_campaign=04.general_backend&utm_content=spring&utm_term=%EC%8A%A4%ED%94%84%EB%A7%81%20%EC%9E%85%EB%AC%B8&gclid=CjwKCAiAjPyfBhBMEiwAB2CCImohok2YrQ2tRdhqfr3cZvKqkIJOHUJ36u6s1-7C9X1gzZIapTvOtxoCangQAvD_BwE)

<br/>

## 궁금증!

```java
@RestController 안에 무엇이 들어 있나
```

어노테이션 정의를 열어보면 답이 나온다.

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Controller
@ResponseBody
public @interface RestController {
    String value() default "";
}
```

<br/>

`@Controller` 와 `@ResponseBody` 두 개를 합쳐놓은 것뿐이다.

<br/>

클래스 파일을 뜯어보면 정말 그렇다.

```java
$ javap -v org.springframework.web.bind.annotation.RestController

  #14 = Utf8   Lorg/springframework/stereotype/Controller;
  #24 = Utf8   Lorg/springframework/web/bind/annotation/ResponseBody;
```

<br/>

상수 풀에 두 어노테이션의 이름이 그대로 박혀 있다.

앞의 자바 파일 컴파일 과정 글에서 본 그 상수 풀이다.

<br/>

앞의 어노테이션(Annotation) 동작 원리 글에서 본 대로,

어노테이션 위에 어노테이션을 붙이는 것을 메타 어노테이션이라고 한다.

<br/>

스프링은 이걸 재귀적으로 읽는다.

```java
@RestController 를 발견
  -> 그 위에 @Controller 가 있나? 있다 -> 컨트롤러로 등록
  -> 그 위에 @ResponseBody 가 있나? 있다 -> 반환값을 본문으로
```

<br/>

## @Controller 안에도 뭔가 있다

```java
@Component
public @interface Controller {
    @AliasFor(annotation = Component.class)
    String value() default "";
}
```

<br/>

`@Component` 다. 그래서 컴포넌트 스캔에 걸린다.

```java
$ javap -v org.springframework.stereotype.Controller

  #14 = Utf8   Lorg/springframework/stereotype/Component;
```

<br/>

앞의 @ComponentScan 내부 동작 글에서 본 대로 스캔 대상이 `@Component` 인데,

`@Controller` 를 붙여도 스캔되는 이유가 이것이다.

```java
@Component
  <- @Controller
  <- @Service
  <- @Repository
       <- @RestController (@Controller 를 통해)
```

<br/>

전부 `@Component` 를 달고 있는 별칭인 셈이다.

<br/>

## 그럼 이름을 왜 나눠놨나

역할을 드러내기 위해서다. 그리고 부가 기능이 붙은 것도 있다.

```java
@Repository - 예외를 스프링 예외로 바꿔준다 (PersistenceExceptionTranslationPostProcessor)
@Controller - DispatcherServlet 이 핸들러로 인식한다
@Service    - 부가 기능 없음. 표시용
```

<br/>

`@Repository` 가 하는 예외 변환이 실제로 의미가 있다.

<br/>

앞의 PSA(Portable Service Abstraction) 글에서 본 그 내용이다.

```java
SQLException (JDBC)             -> DataAccessException
PersistenceException (JPA)      -> DataAccessException
```

<br/>

기술마다 다른 예외를 하나로 맞춰주니 서비스 층이 JDBC인지 JPA인지 몰라도 된다.

<br/>

## 반환값이 어디로 가는지가 갈린다

```java
@Controller
public String home() {
    return "home";          // 뷰 이름. ViewResolver 로 간다
}

@RestController
public String home() {
    return "home";          // 본문 그 자체. "home" 이라는 글자가 응답에 실린다
}
```

<br/>

같은 코드인데 결과가 완전히 다르다.

<br/>

앞의 API (json)방식 @ResponseBody 내부 동작 글에서 본 대로,

`@ResponseBody` 가 붙으면 `ViewResolver` 대신 `HttpMessageConverter` 가 처리한다.

```java
String 반환   -> StringHttpMessageConverter    -> text/plain
객체 반환     -> MappingJackson2HttpMessageConverter -> application/json
```

<br/>

## 섞어 쓸 수도 있다

```java
@Controller
public class MixedController {

    @GetMapping("/page")
    public String page() {
        return "page";                 // 화면
    }

    @GetMapping("/api/data")
    @ResponseBody
    public List<Item> data() {
        return items;                  // JSON
    }
}
```

<br/>

`@Controller` 에 메서드마다 `@ResponseBody` 를 붙이면 된다.

<br/>

반대는 안 된다. `@RestController` 는 클래스 전체가 `@ResponseBody` 라

특정 메서드만 뷰로 보낼 수가 없다.

<br/>

화면과 API를 한 클래스에 섞을 일이 있으면 `@Controller` 를 쓰면 되고,

API만 만들면 `@RestController` 를 쓰면 되는 것이다.

<br/>

요즘은 프론트를 따로 만드는 경우가 많아 `@RestController` 를 훨씬 많이 쓴다.
