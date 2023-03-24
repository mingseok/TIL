## @RequestMapping(), (HttpServletRequest request)

<br/>

### `@RequestMapping` : 요청 정보를 매핑한다.

해당 URL이 호출되면 이 메서드가 호출된다. 

<br/>

## 조합

컨트롤러 클래스를 통합하는 것을 넘어서 조합도 가능하다.

```java
클래스 레벨 @RequestMapping("/springmvc/v2/members")

메서드 레벨 @RequestMapping("/new-form")     ->     /springmvc/v2/members/new-form
메서드 레벨 @RequestMapping("/save")         ->     /springmvc/v2/members/save
메서드 레벨 @RequestMapping                  ->     /springmvc/v2/members
```

<br/><br/>

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

<br/><br/>

## 변경 코드

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

`members()` 메서드는 `@RequestMapping("/springmvc/v2/members")` 라고 되어 있기 때문에, 전부 생략이 가능한 것이다. 

만약 members() 메서드에 `@RequestMapping("/members")` 추가한다면 `("/springmvc/v2/members/members")` 이렇게 될 것이다.. 

<br/>


하지만, 메서드에 `@RequestMapping()` 사용하는 것은 좋은 개발 방법이 아니다. 

그리하여 `@GetMapping`, `@PostMapping` 애너테이션을 사용해서 개발하자.

<br/>

### 추가로 2개도 가능하다.

```java
@RequestMapping({"/new-form", "/save"})
public ModelAndView newForm() {
    return "new-form";
}
```

<br/><br/>

## `HttpServletRequest` 이란 뭔가?

`HttpServletRequest` 는 자바에서 제공하는 표준 서블릿 규약에 의해 `http` 리퀘스트 정보를 받을 수 있다. 

(`고객 요청` 정보를 받을 수 있는 것이다.)

그리하여, `getRequestURI()` 메서드를 사용하면 고객이 어떤 `URL`로 요청했는지 출력할 수 있다.

```java
@Controller
@RequiredArgsConstructor
public class LogDemoController {

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

