## Front Controller

서블릿 컨테이너의 제일 앞에서 서버로 들어오는 클라이언트의 모든 요청을 받아서 

처리해주는 컨트롤러로써, MVC 구조에서 함께 사용되는 패턴입니다.

<br/>

## FrontController 패턴 특징

프론트 컨트롤러도 결국 서블릿이다. -> (클라이언트의 요청을 받음)

프론트 컨트롤러가 요청에 맞는 컨트롤러를 찾아서 호출 한다. 

- 입구를 하나로! (공통 처리 가능)

```java
"스프링 웹 MVC의 DispatcherServlet이 바로 FrontController 패턴으로 구현되어 있다"
```
<br/>

## 궁금증!

```java
"입구를 하나로" 라는 게 왜 좋은 걸까? 서블릿을 여러 개 두면 안 되나?
```

프론트 컨트롤러가 없던 시절 코드를 생각해보면 답이 나온다.

```java
@WebServlet("/members")
class MemberListServlet extends HttpServlet {
    protected void service(HttpServletRequest req, HttpServletResponse res) {
        if (로그인_안됨) { res.sendRedirect("/login"); return; }   // 매번 적는다
        // ... 본 로직
        req.getRequestDispatcher("/WEB-INF/views/members.jsp").forward(req, res);  // 매번 적는다
    }
}

@WebServlet("/orders")
class OrderListServlet extends HttpServlet {
    protected void service(HttpServletRequest req, HttpServletResponse res) {
        if (로그인_안됨) { res.sendRedirect("/login"); return; }   // 또 적는다
        // ...
        req.getRequestDispatcher("/WEB-INF/views/orders.jsp").forward(req, res);   // 또 적는다
    }
}
```

<br/>

로그인 확인과 화면으로 넘기는 코드가 서블릿마다 복사된다.

`/WEB-INF/views/` 경로를 바꾸려면 서블릿을 전부 찾아다녀야 한다.

<br/>

입구가 하나면 그 공통 부분을 입구에서 한 번만 하면 된다.

```java
프론트 컨트롤러가 하는 일
  1. 요청 URL 을 보고 어느 컨트롤러가 처리할지 찾는다
  2. 공통 처리 (로그인 확인, 로그 남기기, 인코딩)
  3. 컨트롤러를 호출한다
  4. 컨트롤러가 돌려준 결과로 화면을 만든다

컨트롤러가 하는 일
  자기 로직만
```

<br/>

컨트롤러는 이제 서블릿을 상속할 필요도 없고, `HttpServletRequest` 를 몰라도 된다.

`@Controller` 를 붙인 평범한 클래스가 될 수 있는 이유가 이것이다.

<br/>

## DispatcherServlet 안을 열어보면

`javap` 로 실제 메서드를 꺼내봤다.

```java
$ javap -p org.springframework.web.servlet.DispatcherServlet

  protected void initStrategies(ApplicationContext);
  protected void doDispatch(HttpServletRequest, HttpServletResponse);
  protected HandlerExecutionChain getHandler(HttpServletRequest);
  protected HandlerAdapter getHandlerAdapter(Object);
  private void processDispatchResult(...);
  protected void render(ModelAndView, HttpServletRequest, HttpServletResponse);
```

<br/>

메서드 이름이 곧 흐름이다.

```java
doDispatch()             <- 요청이 들어오면 여기서 시작한다
  -> getHandler()        <- 이 URL 을 처리할 컨트롤러를 찾는다
  -> getHandlerAdapter() <- 그 컨트롤러를 부를 방법을 찾는다
  -> 핸들러 실행         <- 컨트롤러 호출
  -> processDispatchResult()
       -> render()       <- 뷰를 찾아 화면을 만든다
```

<br/>

그리고 상속 구조를 보면 이렇게 나온다.

```java
public class DispatcherServlet extends FrameworkServlet
```

`FrameworkServlet` 을 타고 올라가면 결국 `HttpServlet` 이다.

원문의 `프론트 컨트롤러도 결국 서블릿이다` 가 문자 그대로 맞는 말인 것이다.

<br/>

## 왜 굳이 어댑터를 한 번 더 거치는가

`getHandler()` 로 컨트롤러를 찾았으면 바로 부르면 될 것 같은데, `getHandlerAdapter()` 가 한 단계 더 있다.

<br/>

컨트롤러의 모양이 한 가지가 아니기 때문이다.

```java
@Controller + @RequestMapping   -> 요즘 방식
Controller 인터페이스 구현       -> 옛날 방식
HttpRequestHandler 구현          -> 정적 리소스 처리 등
```

<br/>

모양이 다르면 부르는 법도 다르다. 메서드 이름도 다르고 파라미터도 다르다.

`DispatcherServlet` 이 이걸 전부 알고 `if` 로 갈라야 한다면, 새로운 모양이 생길 때마다 고쳐야 한다.

<br/>

어댑터를 두면 `DispatcherServlet` 은 `HandlerAdapter.handle()` 하나만 알면 된다.

새 모양이 생기면 어댑터를 하나 더 만들어 등록하면 끝이다.

```java
DispatcherServlet -> "누가 처리할지는 모르지만 handle() 을 부르면 된다"
```

<br/>

`ViewResolver` 도 같은 이유로 끼어 있다.

JSP를 쓸지 타임리프를 쓸지 JSON으로 내려줄지를 `DispatcherServlet` 이 몰라도 되게 만든 것이다.

<br/>

## 갈아끼울 수 있는 자리들

`initStrategies()` 라는 이름이 그래서 붙었다. `전략` 을 초기화한다는 뜻이다.

```java
HandlerMapping           - URL 로 컨트롤러 찾기
HandlerAdapter           - 찾은 컨트롤러 호출하기
HandlerExceptionResolver - 예외 처리하기
ViewResolver             - 뷰 이름으로 실제 뷰 찾기
```

<br/>

넷 다 인터페이스다. 그래서 필요하면 직접 만들어 끼울 수 있다.

`@ExceptionHandler` 로 예외를 처리하는 것도 결국 `HandlerExceptionResolver` 구현체 하나가 하는 일이다.

<br/>

프론트 컨트롤러 패턴이 `입구를 하나로 모은다` 로 시작해서

`그 입구 안을 전부 갈아끼울 수 있게 만든다` 까지 간 셈이다.
