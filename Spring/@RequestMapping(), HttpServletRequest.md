## @RequestMapping(), (HttpServletRequest request)

## `@RequestMapping` : 요청 정보를 매핑한다.

해당 URL이 호출되면 이 메서드가 호출된다. 

## 조합

컨트롤러 클래스를 통합하는 것을 넘어서 조합도 가능하다.

```java
클래스 레벨 @RequestMapping("/springmvc/v2/members")

메서드 레벨 @RequestMapping("/new-form")     ->     /springmvc/v2/members/new-form
메서드 레벨 @RequestMapping("/save")         ->     /springmvc/v2/members/save
메서드 레벨 @RequestMapping                  ->     /springmvc/v2/members
```

## 기존 코드

```java
@Controller
public class SpringMemberControllerV2 {

    @RequestMapping("/springmvc/v2/members/new-form")
    public ModelAndView newForm() {
        return "new-form";
    }

    @RequestMapping("/springmvc/v2/members/save")
    public ModelAndView save(...) {
        
        // ... 

        return mav;
    }

    @RequestMapping("/springmvc/v2/members")
    public ModelAndView members() {
        
        // ... 

        return mav;
    }
}
```

## 변경 코드

`members()` 메서드는 `@RequestMapping("/springmvc/v2/members")` 라고 되어 있기 때문에,

전부 생략이 가능한 것이다. 

만약 `@RequestMapping("/members")` 추가한다면 `("/springmvc/v2/members/members")` 이렇게 

될 것이다.. 

```java
@Controller
@RequestMapping("/springmvc/v2/members")
public class SpringMemberControllerV2 {

    @RequestMapping("/new-form")
    public ModelAndView newForm() {
        return "new-form";
    }

    @RequestMapping("/save")
    public ModelAndView save(...) {
        
        // ... 

        return mav;
    }

    @RequestMapping
    public ModelAndView members() {
        
        // ... 

        return mav;
    }

```

하지만, 메서드에 `@RequestMapping()` 사용하는 것은 좋은 개발 방법이 아니다. 

그리하여 `@GetMapping`, `@PostMapping` 애너테이션을 사용해서 개발하자.

### 추가로 2개도 가능하다.

```java
@RequestMapping({"/new-form", "/save"})
public ModelAndView newForm() {
    return "new-form";
}
```

## `HttpServletRequest` 이란 뭔가?

HttpServletRequest 는 자바에서 제공하는 표준 서블릿 규약에 의해 http 리퀘스트 정보를 

받을 수 있다. (`고객 요청` 정보를 받을 수 있는 것이다.)

그리하여, getRequestURI() 메서드를 사용하면 고객이 어떤 URL로 요청했는지 출력할 수 있다.

```java
@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final MyLogger myLogger;

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) {

        // 고객이 어떤 url로 요청했는지 알 수 있다.
        String requestURL = request.getRequestURL().toString();
        myLogger.setRequestURL(requestURL);
        return "OK";
    }
}
```

출력: `http://localhost:8080/log-demo`

이렇게 받은 `requestURL` 값을 `myLogger`에 저장해둔다. 

`myLogger`는 HTTP 요청 당 각각 구분되므로 다른 HTTP 요청 때문에 

값이 섞이는 걱정은 하지 않아도 된다.

```
참고 : `requestURL`을 `MyLogger`에 저장하는 부분은 컨트롤러 보다는 공통 처리가 
        가능한 스프링 인터셉터나 서블릿 필터 같은 곳을 활용하는 것이 좋다.
```