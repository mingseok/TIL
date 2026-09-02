## 스프링 MVC 구조

<br/>

## 스프링 MVC의 강점

- DispatcherServlet 코드의 변경 없이, 원하는 기능을 변경하거나 확장할 수 있다는 점이다.

    - 지금까지 설명한 대부분을 확장 가능할 수 있게 인터페이스로 제공한다.

<br/>

이 인터페이스들만 구현해서 DispatcherServlet 에 등록하면 나만의 컨트롤러를 만들 수도 있다.

![이미지](/programming/img/입문79.PNG)

<br/><br/>

## 동작 순서

### 1. `핸들러 조회:` 핸들러 매핑을 통해 요청 URL에 매핑된 핸들러(컨트롤러)를 조회한다.

![이미지](/programming/img/입문80.PNG)

<br/><br/>

### 1-1 핸들러(컨트롤러) 찾는 과정.

![이미지](/programming/img/입문81.PNG)

<br/><br/>


### 2. `핸들러 어댑터 조회:` 핸들러를 실행할 수 있는 핸들러 어댑터를 조회한다.

![이미지](/programming/img/입문82.PNG)

<br/><br/>


### 2-1. 핸들러 어댑터 찾는 과정

![이미지](/programming/img/입문83.PNG)

<br/><br/>

### 3. `핸들러 어댑터 실행:` 핸들러 어댑터를 실행한다.

![이미지](/programming/img/입문84.PNG)


<br/><br/>

### 4. `핸들러 실행:` 핸들러 어댑터가 실제 핸들러를 실행한다.

![이미지](/programming/img/입문85.PNG)

<br/><br/>

### 4-1. 실제 핸들러를 실행하여 `논리 이름` 을 넣은 ModelView 객체를 생성하여 반환 한다.

![이미지](/programming/img/입문86.PNG)


<br/><br/>


### 참고) ModelView 클래스

![이미지](/programming/img/입문87.PNG)

<br/><br/>


### 참고) createParamMap() 메서드

![이미지](/programming/img/입문88.PNG)


<br/><br/>

### 5. `ModelView 반환:` 

핸들러 어댑터는 `논리 이름` 을 넣은 `ModelView` 객체를 프론트 컨트롤러에 리턴 한다. (4번 사진 동일)


<br/><br/>

### 6. `viewResolver 호출:` `ModelView` 객체에서 논리 이름을 가져 온다. 


<br/>

### 6-1. 그리고 뷰 리졸버 메서드를 실행한다.

![이미지](/programming/img/입문89.PNG)


<br/><br/>

### 7. `View 반환:` 

뷰 리졸버는 뷰(html)의 논리 이름을 물리 이름으로 바꾸고, 
MyView객체를 생성하여 MyView에 물리 이름을 저장시킨다.

![이미지](/programming/img/입문90.PNG)


<br/><br/>


### 8. `뷰 렌더링:` MyView객체를 통해서 render() 메서드를 실행시킨다.

![이미지](/programming/img/입문91.PNG)


<br/><br/>


### 9. 물리 이름인 HTML로 전송(이동) 하게 되는 것이다.

![이미지](/programming/img/입문92.PNG)


<br/><br/>

>**Reference** <br/>[스프링 MVC 2편 - 백엔드 웹 개발 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2/dashboard)
<br/>

## 궁금증!

```java
DispatcherServlet 하나로 모든 요청을 받는다면 그 매핑은 누가 하나
```

`web.xml` 도 없는데 어떻게 `/` 로 들어오는 걸 다 받는지가 궁금했다.

<br/>

스프링 부트가 자동으로 등록해준다.

```java
DispatcherServletAutoConfiguration
  -> DispatcherServlet 을 빈으로 등록
  -> ServletRegistrationBean 으로 "/" 에 매핑
```

<br/>

앞의 서블릿 설명 내부 동작 글에서 본 `서블릿 등록` 을 자동으로 해주는 것이다.

```java
spring.mvc.servlet.path=/api      # 이 설정으로 바꿀 수도 있다
```

<br/>

## / 와 /* 는 다르다

여기가 헷갈리는 부분이다.

```java
"/"   - 다른 서블릿이 처리 못 하는 것만 받는다. JSP 는 JspServlet 이 먼저 가져간다
"/*"  - 정말 전부 다 받는다. JSP 요청까지 가로챈다
```

<br/>

`DispatcherServlet` 은 `/` 로 등록된다.

그래서 `.jsp` 요청은 톰캣의 `JspServlet` 이 처리하고,

`DispatcherServlet` 은 나머지를 받는 구조가 되는 것이다.

<br/>

앞의 뷰 리졸버 글에서 본 대로 `InternalResourceViewResolver` 가

`/WEB-INF/views/members.jsp` 로 포워드를 하면 그때 `JspServlet` 이 받아서 처리한다.

<br/>

## doDispatch 안에서 무엇이 일어나는가

`javap` 로 꺼내본 메서드 순서가 그대로 흐름이다.

```java
$ javap -p org.springframework.web.servlet.DispatcherServlet

  protected void doDispatch(HttpServletRequest, HttpServletResponse);
  protected HandlerExecutionChain getHandler(HttpServletRequest);
  protected HandlerAdapter getHandlerAdapter(Object);
  private void processDispatchResult(...);
  protected void render(ModelAndView, ...);
  protected View resolveViewName(String, Map, Locale, HttpServletRequest);
```

<br/>

```java
1. getHandler()          어느 컨트롤러인가
2. getHandlerAdapter()   그것을 어떻게 부르나
3. handle()              부른다 -> ModelAndView 를 받는다
4. render()              모델을 뷰에 넘겨 화면을 만든다
     -> resolveViewName()  뷰 이름으로 실제 뷰를 찾는다
```

<br/>

## 확장 지점이 전부 인터페이스다

`initStrategies()` 가 채우는 것들이다.

```java
HandlerMapping
HandlerAdapter
HandlerExceptionResolver
ViewResolver
LocaleResolver
MultipartResolver
```

<br/>

전부 인터페이스라 구현을 갈아 끼울 수 있다.

<br/>

앞의 인터페이스는 왜 사용하는가 글에서 본 그 이득이 프레임워크 뼈대에 그대로 쓰인 것이다.

```java
DispatcherServlet 은 인터페이스만 안다
-> 새 방식이 생겨도 DispatcherServlet 은 안 고친다
```

<br/>

## 예외가 나면 어디로 가는가

`processDispatchResult()` 안에서 처리한다.

```java
try {
    핸들러 실행
} catch (Exception ex) {
    dispatchException = ex;
}
processDispatchResult(..., dispatchException);
   -> HandlerExceptionResolver 목록에게 차례로 물어본다
```

<br/>

앞의 API 예외 처리 - @ExceptionHandler 글에서 본 그 어노테이션도 여기로 연결된다.

```java
ExceptionHandlerExceptionResolver   -> @ExceptionHandler 를 찾아 부른다
ResponseStatusExceptionResolver     -> @ResponseStatus 를 보고 상태 코드를 정한다
DefaultHandlerExceptionResolver     -> 스프링 내부 예외를 상태 코드로 바꾼다
```

<br/>

셋 다 처리 못 하면 예외가 톰캣까지 올라가고,

앞의 서블릿 예외 처리 - 오류 화면 글에서 본 오류 페이지 흐름을 타게 된다.

<br/>

## 스프링 MVC 를 안 쓰면

`DispatcherServlet` 대신 다른 것이 앞에 설 수 있다.

```java
Spring WebFlux  -> DispatcherHandler
```

<br/>

이름이 비슷한 이유가 있다. 하는 일이 같기 때문이다.

`요청을 받아 -> 처리할 것을 찾아 -> 부르고 -> 응답을 만든다` 는 뼈대는 같고,

스레드를 쓰는 방식만 다른 것이다.

<br/>

앞의 멀티 쓰레드 이해 글에서 본 `요청마다 스레드 하나` 대신,

적은 수의 스레드가 이벤트를 돌리는 방식이라고 보면 된다.
