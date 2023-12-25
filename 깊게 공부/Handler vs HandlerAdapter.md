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