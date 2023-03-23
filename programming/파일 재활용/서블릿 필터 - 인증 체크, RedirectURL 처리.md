## 서블릿 필터 - 인증 체크, RedirectURL 처리(중요)

드디어 인증 체크 필터를 개발해보자. 

로그인 되지 않은 사용자는 상품 관리 뿐만 아니라 미래에 개발될 페이지에도 
접근하지 못하도록 하자

<br/>

LoginCheckFilter 클래스

주석 잘 보기.

```java
package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    // whitelist 에 들어 있는 url들은 모두 비회원도 들어갈 수 있는 url 들이다.
    private static final String[] whitelist = {"/", "/members/add", "/login",
            "/logout", "/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            log.info("인증 체크 필터 시작 {}", requestURI);

            // whitelist 안든건 false가 온다.
            if (isLoginCheckPath(requestURI)) { // whitelist 가 아니면 밑에 로직을 탄다. 예를 들어
                                                // localhost:8080/items 같은 회원 정보가 들어 있는 곳.

                log.info("인증 체크 로직 실행 {}", requestURI); // localhost:8080/items

                HttpSession session = httpRequest.getSession(false); // false면 생성이 아니고, null 반환
                
					if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
					// session.getAttribute(SessionConst.LOGIN_MEMBER) == null 이유는.
					// session 자체가 없거나 session이 있더라도 로그인한 사용자 정보(유저a 객체)가 없으면
					// 로그인을 요청해야 합니다.								

                    log.info("미인증 사용자 요청 {}", requestURI); // localhost:8080/items

                    //로그인으로 redirect
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);

                    return; //여기가 중요, 미인증 사용자는 다음으로 진행하지 않고 끝!
                }
            }

            // false
            // whitelist 에 있는 url 이면 이쪽으로 온다.
            chain.doFilter(request, response);
            
        } catch (Exception e) {
            throw e; //예외 로깅 가능 하지만, 톰캣까지 예외를 보내주어야 함
            
        } finally {
            log.info("인증 체크 필터 종료 {}", requestURI);
        }
    }

    /**
     * 화이트 리스트의 경우 인증 체크X
     */
    private boolean isLoginCheckPath(String requestURI) {

        //whitelist 와 requestURI 가 매칭 되는가?
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }
}
```

- `whitelist = {"/", "/members/add", "/login", "/logout","/css/*"};`
    - whitelist 에 들어 있는 url들은 모두 비회원도 들어갈 수 있는 url 들이다.
    - 인증 필터를 적용해도 홈, 회원가입, 로그인 화면, css 같은 리소스에는 접근할 수 있어야 한다. 
    
    - 이렇게 화이트 리스트 경로는 인증과 무관하게 항상 허용한다. 화이트 리스트를 
        
        제외한 나머지 모든 경로에는 인증 체크 로직을 적용한다.
        
- `if (isLoginCheckPath(requestURI)) {`
    - whitelist 가 아니면 밑에 로직을 탄다.
        
        예를 들어 localhost:8080/items 같은 회원 정보가 들어 있는 곳.
        
- `httpResponse.sendRedirect("/login?redirectURL=" + requestURI);`
    - 미인증 사용자는 로그인 화면으로 리다이렉트 한다.
        
        그런데 로그인 이후에 다시 홈으로 이동해버리면, 원하는 경로를 다시 찾아가야 하는 불편함이 있다. 
        
        예를 들어서 상품 관리 화면을 보려고 들어갔다가 로그인 화면으로 
        
        이동하면, 로그인 이후에 다시 상품 관리 화면으로 들어가는 것이 좋다. 
        
        이런 부분이 개발자 입장에서는 좀 귀찮을 수 있어도 사용자 입장으로 보면 편리한 기능이다.
        
        이러한 기능을 위해 현재 요청한 경로인 requestURI 를 /login 에 쿼리 파라미터로 함께 전달한다. 
        
        물론 /login 컨트롤러에서 로그인 성공시 해당 경로로 이동하는 기능은 추가로 개발해야 한다
        
- `return;` 여기가 중요하다. 필터를 더는 진행하지 않는다.
    
    이후 필터는 물론 서블릿, 컨트롤러가 더는 호출되지 않는다. 
    
    앞서 redirect 를 사용했기 때문에 redirect 가 응답으로 적용되고 요청이 끝난다.
    
- `session.getAttribute(SessionConst.LOGIN_MEMBER) == null`
    - 이유는. `session` 자체가 없거나 session이 있더라도 로그인한 사용자 정보(유저a 객체)가
        
        없으면 로그인을 요청해야 합니다.		

<br/>        

WebConfig - loginCheckFilter() 추가

```java
@Bean
public FilterRegistrationBean loginCheckFilter() {
		 FilterRegistrationBean<Filter> filterRegistrationBean = 
																		new FilterRegistrationBean<>();

 filterRegistrationBean.setFilter(new LoginCheckFilter());
 filterRegistrationBean.setOrder(2);
 filterRegistrationBean.addUrlPatterns("/*");

 return filterRegistrationBean;
}
```

- `setFilter(new LoginCheckFilter())` : 로그인 필터를 등록한다.

- `setOrder(2)` : 순서를 2번으로 잡았다. 로그 필터 다음에 로그인 필터가 적용된다.
- `addUrlPatterns("/*")` : 모든 요청에 로그인 필터를 적용한다.

<br/>

![이미지](/programming/img/나22.PNG)


<br/>

### RedirectURL 처리

로그인에 성공하면 처음 요청한 URL로 이동하는 기능을 개발해보자

즉, 로그인 하지 않고 게시판을 보고 있다가 댓글을 달기 위해 로그인을 했다면 좀전에 보고 있던 게시판으로 와야 된다. 

<br/>

LoginController

```java
@PostMapping("/login")
public String login4(@Valid @ModelAttribute LoginForm form,
            BindingResult bindingResult,

						// 만약 없으면 그냥 "/" 가 되는 것이다.
            @RequestParam(defaultValue = "/") String redirectURL, 
            HttpServletRequest request) {

			``

			return "redirect:" + redirectURL;
}
```

로그인 체크 필터에서, 미인증 사용자는 요청 경로를 포함해서 /login 에 redirectURL 요청 파라미터를 추가해서 요청했다. 

이 값을 사용해서 로그인 성공시 해당 경로로 고객을 redirect 한다.

<br/>

## 정리

서블릿 필터를 잘 사용한 덕분에 로그인 하지 않은 사용자는 나머지 경로에 들어갈 수 없게 되었다. 

공통 관심사를 서블릿 필터를 사용해서 해결한 덕분에 향후 로그인 관련 정책이 변경되어도 이 부분만 변경하면 된다.

<br/>

### 참고

필터에는 다음에 설명할 스프링 인터셉터는 제공하지 않는, 아주 강력한 기능이 있다.

<br/>

>**Reference** <br/>스프링 MVC 2편 - 백엔드 웹 개발 활용 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2