## HTTP 요청 파라미터 - @RequestParam

### @RequestParam 이 있으면 명확하게 요청 파리미터에서 데이터를 읽는 다는 것을 알 수 있다.

요청 매개변수에 들어있는 기본 타입 데이터를 메서드 인자로 받아올 수 있다.

여기서 요청 매개변수란 URL 요청의 localhost:8080?username=value&age=value 의 쿼리 매개변수와 HTML 태그의 Form 데이터가 해당한다.

1개의 HTTP 요청 파라미터를 받기 위해서 사용한다



```java
package hello.springmvc.basic.request;

@Slf4j
@Controller
public class RequestParamController {

    /**
     * 반환 타입이 없으면서 이렇게 응답에 값을 직접 집어넣으면, view 조회X
     */
    // http://localhost:8080/request-param-v1?username=hello&age=20
    @RequestMapping("/request-param-v1") // 이거 실행시키는게 아니고 위에꺼
    public void requestParamV1(HttpServletRequest request,
                               HttpServletResponse response) throws IOException {
			       
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));
        log.info("username={}, age={}", username, age);

        response.getWriter().write("ok");
    }

    
    // http://localhost:8080/request-param-v2?username=hello&age=20
    @ResponseBody
    @RequestMapping("/request-param-v2")
    public String requestParamV2(
            @RequestParam("username") String memberName,
            @RequestParam("age") int memberAge) {
        
        log.info("username={}, age={}", memberName, memberAge);
        return "ok";
    }

			
    // 이렇게 생략 할 수도 있다.
    // http://localhost:8080/request-param-v3?username=hello&age=20
    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(
            @RequestParam String username,
            @RequestParam int age) {

        log.info("username={}, age={}", username, age);
        return "ok";
    }


    // 더 줄일수 있다.
    // http://localhost:8080/request-param-v4?username=hello&age=20
    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(String username, int age) {

        log.info("username={}, age={}", username, age);
        return "ok";
    }
		
		
    // 모든 요청을 다 받고 싶다 할때는.
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap) {
    
        log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }

}
```

<br/>

`@RequestParam` : 파라미터 이름으로 바인딩

String username = request.getParameter("username"); -> 이걸


@RequestParam("username") String memberName, 이렇게 이름으로 바인딩 한다는 것이다.


<br/>


`@ResponseBody` : `View` 조회를 무시하고, `HTTP message body`에 직접 해당 내용 입력

<br/>


`@RequestParam("username") String memberName`

"username" 으로 넘어온 값을 memberName(매개변수) 으로 받겠다 한것이다. 


![이미지](/programming/img/갸2.PNG)

<br/>

결국 `request.getParameter("username")` 하는 거랑 똑같은 효과가 있는 것이다.

<br/>

## 정리

`@RequestParam` 은 밑에 코드이다. html에서 값을 받아서 그 값으로 매개변수로 사용하는 것이다.
    
컨트롤러에서 `@RequestParam` 도 생략 가능하다.

```html
<td>
   <a th:href="@{/view(id=${list.id})}">
       [[${list.title}]]
   </a>
</td>
```


```java
// 게시판 상세 보기
@GetMapping("/post-view")
public String viewPost(Model model, @RequestParam Long id) {
      model.addAttribute("post", postService.getPost(id));
      return "posts/post-view";
}

// 이렇게도 가능하다.
@GetMapping("/post-view")
public String viewPost(Model model, Long id) {
      model.addAttribute("post", postService.getPost(id));
      return "posts/post-view";
}
```

<br/>

### 이렇게도 사용가능하다. 에러 사용할때 사용

`@RequestParam(required = false` 되어 있으니, 에러가 있으면 담게 되는 것이고, 

없으면 그냥 null 인 것이다. 즉, `@RequestParam` 은 파라미터 이름으로 바인딩 한다는 것인데
    
여기서는 넘어온 에러를 `value = "exception"` 이름으로 사용하겠다 한 것이다.
    
정리하면 에러가 있으면 담고, 없으면 담지 않는 것이다.

```java
@GetMapping("/login")
public String login(@RequestParam(required = false, value = "error") String error,
                    @RequestParam(required = false, value = "exception") String exception, Model model)        {

    model.addAttribute("error", error);
    model.addAttribute("exception", exception);

    return "/members/login";
}
```

<br/>
<br/>

>**Reference** <br/>스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1
