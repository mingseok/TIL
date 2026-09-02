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


<br/>

## 궁금증!

```java
@RequestMapping 은 어디까지 좁힐 수 있나
```

URL만 보는 게 아니다. 조건을 여러 개 걸 수 있다.

```java
@RequestMapping(
    value = "/members",
    method = RequestMethod.POST,
    params = "mode=save",
    headers = "X-Api-Version=2",
    consumes = "application/json",
    produces = "application/json"
)
```

<br/>

```java
value    - 경로
method   - HTTP 메서드
params   - 특정 쿼리 파라미터가 있어야
headers  - 특정 헤더가 있어야
consumes - 요청 본문의 Content-Type
produces - 클라이언트가 받고 싶어하는 Accept
```

<br/>

`RequestMappingHandlerMapping` 이 이 조건을 전부 보고 매칭한다.

<br/>

## consumes 와 produces 를 헷갈리기 쉽다

방향이 반대다.

```java
consumes - 내가 "먹는" 것. 요청의 Content-Type 을 본다
produces - 내가 "내놓는" 것. 요청의 Accept 헤더를 본다
```

<br/>

앞의 HTTP 헤더 - 표현 글에서 본 그 헤더들이다.

```java
POST /api/members
Content-Type: application/json      <- consumes 가 이걸 본다
Accept: application/json            <- produces 가 이걸 본다
```

<br/>

안 맞으면 각각 다른 상태 코드가 나간다.

```java
consumes 불일치 -> 415 Unsupported Media Type
produces 불일치 -> 406 Not Acceptable
```

<br/>

## 매칭이 안 되면 어떻게 되는가

```java
경로가 없다        -> 404 Not Found
경로는 있는데 메서드가 다르다 -> 405 Method Not Allowed
```

<br/>

`405` 가 나온다는 건 그 URL은 존재한다는 뜻이다.

`GET /members` 는 있는데 `DELETE /members` 는 없다는 것이다.

<br/>

앞의 HTTP 상태 코드 글에서 본 대로, `405` 응답에는 `Allow` 헤더가 같이 온다.

```java
HTTP/1.1 405 Method Not Allowed
Allow: GET, POST
```

<br/>

`이 주소로는 이 메서드들만 된다` 를 알려주는 것이다.

<br/>

## 축약형이 따로 있다

```java
@RequestMapping(value = "/members", method = RequestMethod.GET)
@GetMapping("/members")        // 같다
```

<br/>

`@GetMapping` 정의를 열면 이렇게 되어 있다.

```java
@RequestMapping(method = RequestMethod.GET)
public @interface GetMapping { ... }
```

<br/>

앞의 @Controller, @RestController 글에서 본 메타 어노테이션이다.

`@RequestMapping` 을 미리 채워둔 별칭인 것이다.

<br/>

## 클래스와 메서드의 경로가 합쳐진다

```java
@RequestMapping("/members")
@RestController
public class MemberController {

    @GetMapping("/{id}")        // 결과는 /members/{id}
    public Member find(@PathVariable Long id) { ... }
}
```

<br/>

클래스에 붙은 것이 공통 경로가 된다.

<br/>

## HttpServletRequest 를 직접 받을 수도 있다

```java
@GetMapping("/members")
public String list(HttpServletRequest request) {
    String param = request.getParameter("keyword");
    String header = request.getHeader("User-Agent");
    HttpSession session = request.getSession();
}
```

<br/>

앞의 요청 매핑 헨들러 어뎁터 글에서 본 `ServletRequestMethodArgumentResolver` 가 이걸 채워준다.

<br/>

다만 실무에서는 잘 안 쓴다. 필요한 것만 따로 받는 게 낫기 때문이다.

```java
public String list(@RequestParam String keyword,
                   @RequestHeader("User-Agent") String agent,
                   HttpSession session) { ... }
```

<br/>

이러면 메서드 시그니처만 보고 무엇을 쓰는지 알 수 있다.

`HttpServletRequest` 를 통째로 받으면 안을 들여다봐야 알 수 있다.

<br/>

테스트할 때도 차이가 난다.

`HttpServletRequest` 를 받으면 가짜 객체를 만들어야 하지만,

문자열만 받으면 그냥 값을 넣어서 부르면 된다.

<br/>

앞의 PSA(Portable Service Abstraction) 글에서 본 `POJO` 의 이득이 여기서 나오는 것이다.
