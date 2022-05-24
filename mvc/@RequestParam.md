## HTTP 요청 파라미터 - @RequestParam

<br/>

### @RequestParam 이 있으면 명확하게 요청 파리미터에서 데이터를 읽는 다는 것을 알 수 있다.



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

`@ResponseBody` : `View` 조회를 무시하고, `HTTP message body`에 직접 해당 내용 입력

<br/>

`@RequestParam`의 `name(value)` 속성이 파라미터 이름으로 사용되는 것이다.

`@RequestParam("username") String memberName`


![이미지](/programming/img/갸2.PNG)

<br/>

결국 `request.getParameter("username")` 하는 거랑 똑같은 효과가 있는 것이다.


<br/>

>**Reference** <br/>스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1
