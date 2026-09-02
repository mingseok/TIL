## Handler vs HandlerAdapter

<br/>

### Handler

자동차의 핸들을 제일 먼저 떠올릴 수 있다.

`자동차의 핸들은` 운전자가 직접 `핸들을 움직이면서,` 직접 `자동차의 주행을 처리하는 역할`을 합니다.

```java
Spring MVC에서는 "자동차의 핸들과 마찬가지"로 
"클라이언트의 요청을 처리하는 처리자를 Handler"라고 합니다.
```

<br/>

### Spring MVC에서 Handler는 누구인가요?

Spring MVC에서의 요청 Handler는 내가 작성하는 Controller 클래스를 의미한다. 

그리고 Controller 클래스에 있는 ‘@GetMapping, @PostMapping’ 같은 

`애너테이션이 붙어 있는 메서드들을 "핸들러 메서드"`라고 합니다.

<br/>

### HandlerMapping이란?

`사용자의 요청과 요청을 처리하는 Handler를 매핑해주는 역할`을 하는 것이다.

<br/>

### 그렇다면, 어떤 기준으로 매핑이 가능하게 되나요?

사용자의 요청과 Handler 메서드의 매핑은 `@GetMapping(”/pizza”)` 처럼 

`HTTP Request Method`과 `Mapping URL`을 기준으로 해당 `Handler와 매핑`이 되는데 

<br/>

Spring 에서는 여러가지 유형의 `HandlerMapping 클래스를 제공`하고 있습니다.

```java
가장 우선순위가 높은 핸들러 매핑과 핸들러 어댑터는 
- RequestMappingHandlerMapping
- RequestMappingHandlerAdapter
"실무에서는 99.9% 이 방식의 컨트롤러를 사용한다."
```

<br/><br/>

## HandlerAdapter란?

일단 Adapter는 → 220V를 5V 전압으로 바꿔주는 충전기를 생각할 수 있다.

이처럼, HandlerAdapter 또한, 무언가를 다른 형식이나 형태로 `바꿔주는 역할`을 한다.

<br/>

`HandlerAdapter`를 한마디로 설명하자면, → `Handler를 실행시켜준다.`

Spring은 객체지향의 설계 원칙을 따르는 유연한 구조인 프레임워크 이다. 

<br/>

그리하여, Spring이 제공하는 `Spring MVC`에서 지원하는 `Handler`를 사용해도 되지만 

`다른 프레임워크의 Handler를 Spring MVC`에 통합할 수 있다.

<br/>

이처럼 `다른 프레임워크의 핸들러를 Spring MVC에 통합`하기 위해서 `HandlerAdapter`를 사용할 수 있다.





<br/><br/>

## HandlerMapping 우선 순위를 잘 알아야 한다.

![이미지](/programming/img/입문93.PNG)

1. 애노테이션 기반의 컨트롤러인 `@RequestMapping`

2. 스프링 빈의 이름으로 핸들러를 찾는다.

<br/><br/>

## HandlerAdapter

![이미지](/programming/img/입문94.PNG)

1. 애노테이션 기반의 컨트롤러인 `@RequestMapping`

2. HttpRequestHandler 처리

3. Controller 인터페이스(애노테이션X, 과거에 사용) 처리

<br/><br/>

## @RequestMapping

가장 우선순위가 높은 핸들러 매핑과 핸들러 어댑터는 

`RequestMappingHandlerMapping` , `RequestMappingHandlerAdapter` 이다.

<br/>

`@RequestMapping` 의 앞글자를 따서 만든 이름인데, 이것이 바로 지금 스프링에서 주로 사용하는 

`애노테이션 기반의 컨트롤러를` `지원하는 매핑과 어댑터이다.` 

실무에서는 99.9% 이 방식의 컨트롤러를 사용한다.


<br/><br/>

>**Reference** <br/>[스프링 MVC 2편 - 백엔드 웹 개발 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2/dashboard)
<br/>

## 궁금증!

```java
"여러가지 유형의 HandlerMapping 을 제공한다" 는데, 왜 여러 개가 필요할까?
```

컨트롤러의 모양이 한 가지가 아니기 때문이다.

`spring-webmvc` 안을 보면 매핑과 어댑터가 짝을 이뤄 들어 있다.

```java
RequestMappingHandlerMapping   <-> RequestMappingHandlerAdapter
   @Controller + @RequestMapping 방식

BeanNameUrlHandlerMapping      <-> SimpleControllerHandlerAdapter
   Controller 인터페이스를 구현한 옛날 방식

SimpleUrlHandlerMapping        <-> HttpRequestHandlerAdapter
   정적 리소스 처리 등
```

<br/>

`DispatcherServlet` 은 이 목록을 순서대로 돌면서 물어본다.

```java
// 1단계 : 이 요청을 처리할 핸들러를 찾는다
for (HandlerMapping mapping : 매핑목록) {
    HandlerExecutionChain handler = mapping.getHandler(request);
    if (handler != null) return handler;
}

// 2단계 : 찾은 핸들러를 실행할 수 있는 어댑터를 찾는다
for (HandlerAdapter adapter : 어댑터목록) {
    if (adapter.supports(handler)) return adapter;
}
```

<br/>

`getHandler()` 와 `getHandlerAdapter()` 라는 메서드가 실제로 이 두 반복문이다.

<br/>

## 왜 두 단계로 나뉘는가

한 번에 하면 안 되나 싶은데, 하는 일이 다르다.

```java
HandlerMapping - "이 URL 은 누가 처리하나" 를 찾는다. URL 이 기준
HandlerAdapter - "그 누구를 어떻게 부르나" 를 찾는다. 핸들러의 타입이 기준
```

<br/>

`@GetMapping("/members")` 가 붙은 메서드를 찾는 것과,

그 메서드를 리플렉션으로 부르면서 파라미터를 채우는 것은 완전히 다른 일이다.

<br/>

그리고 `HandlerExecutionChain` 이라는 이름에 힌트가 있다.

`getHandler()` 가 돌려주는 것은 컨트롤러 하나가 아니라 `컨트롤러 + 인터셉터 목록` 이다.

```java
HandlerExecutionChain
  - handler   : 실행할 컨트롤러
  - interceptors : 그 앞뒤로 실행할 인터셉터들
```

<br/>

그래서 흐름이 이렇게 된다.

```java
인터셉터.preHandle()  -> 어댑터.handle() -> 컨트롤러 -> 인터셉터.postHandle()
```

<br/>

## 어댑터가 필요한 이유를 코드로 보면

핸들러의 모양이 다르다는 것이 이런 뜻이다.

```java
// 요즘 방식 - 메서드 이름도 파라미터도 자유롭다
@Controller
class MemberController {
    @GetMapping("/members")
    public String list(Model model) { ... }
}

// 옛날 방식 - 정해진 메서드를 구현한다
class OldController implements Controller {
    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) { ... }
}
```

<br/>

`DispatcherServlet` 이 이 둘을 직접 부르려면 `if` 로 갈라야 한다.

새로운 모양이 생길 때마다 `DispatcherServlet` 을 고쳐야 하는 것이다.

<br/>

어댑터를 두면 `DispatcherServlet` 은 이것만 안다.

```java
ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler);
```

<br/>

어느 어댑터가 들어오든 이 메서드 하나면 되고, 안에서 어떻게 부르는지는 각 어댑터가 알아서 한다.

앞의 인터페이스 글에서 본 `구체적인 것을 알면 그게 바뀔 때마다 나도 바뀐다` 가 여기서 그대로 적용된다.

<br/>

## 어댑터 패턴이라는 이름

원문의 `220V 를 5V 로 바꿔주는 충전기` 비유가 정확하다.

```java
콘센트(DispatcherServlet)는 220V 하나만 낸다
기기(컨트롤러)마다 필요한 전압이 다르다
-> 어댑터가 사이에서 맞춰준다
```

<br/>

`어댑터 패턴` 은 `내가 원하는 인터페이스` 와 `상대가 가진 인터페이스` 가 다를 때 쓴다.

상대를 고칠 수 없거나 고치기 싫을 때, 사이에 변환기를 하나 끼우는 것이다.

<br/>

자바 표준에도 같은 예가 있다.

```java
Arrays.asList(배열)      // 배열을 List 처럼 쓰게 해준다
InputStreamReader        // 바이트 스트림을 문자 스트림처럼 쓰게 해준다
```

<br/>

## 실무에서는 하나만 쓴다

원문에 적힌 대로 `RequestMappingHandlerMapping` 과 `RequestMappingHandlerAdapter` 만 거의 쓴다.

<br/>

그래도 나머지가 남아 있는 이유는 하위 호환 때문이다.

옛날 방식으로 짠 코드가 아직 돌아가는 곳이 있고, 스프링은 그것을 깨지 않으려고 한다.

<br/>

앞의 default 메서드 글에서 본 것과 같은 태도다.

`이미 배포된 것을 깨지 않으면서 새 방식을 추가한다` 는 방침이 이 목록에도 드러나 있다.
