## 로그인 처리하기 - 서블릿 HTTP 세션1

세션이라는 개념은 대부분의 웹 애플리케이션에 필요한 것이다. 

어쩌면 웹이 등장하면서 부터 나온 문제이다.

<br/>서블릿은 세션을 위해 HttpSession 이라는 기능을 제공하는데, 지금까지 나온 문제들을 해결해준다.

우리가 직접 구현한 세션의 개념이 이미 구현되어 있고, 더 잘 구현되어 있다.

<br/>

### HttpSession 소개

서블릿이 제공하는 HttpSession 도 결국 우리가 직접 만든 `SessionManager` 와 같은 방식으로 동작한다. 

서블릿을 통해 `HttpSession` 을 생성하면 다음과 같은 쿠키를 생성한다. 

<br/>

쿠키 이름이 `JSESSIONID` 이고, 값은 추정 불가능한 랜덤 값이다.

`Cookie: JSESSIONID=5B78E23B513F50164D6FDD8C97B0AD05`

HttpSession 사용 서블릿이 제공하는 HttpSession 을 사용하도록 개발해보자.

<br/>

### SessionConst

HttpSession 에 데이터를 보관하고 조회할 때, 같은 이름이 중복 되어 사용되므로, 

상수를 하나 정의했다

```java
package [hello.login.web](http://hello.login.web/);

public class SessionConst {
public static final String LOGIN_MEMBER = "loginMember";
}
```

<br/>

LoginController

```java
@PostMapping("/login")
public String login3(@Valid @ModelAttribute LoginForm form,
                     BindingResult bindingResult,
                     HttpServletRequest request) {

		``

        // 로그인 성공 처리

        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 반환한다.
        HttpSession session = request.getSession();

        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:/";

    }
```

<br/>

### 세션 생성과 조회

세션을 생성하려면 `request.getSession(true)` 를 사용하면 된다.

`public HttpSession getSession(boolean create);`

<br/>

### 세션의 create 옵션에 대해 알아보자.

- `request.getSession(true)`
    - 세션이 있으면 기존 세션을 반환한다.

    - 세션이 없으면 새로운 세션을 생성해서 반환한다.
- `request.getSession(false)`
    - 세션이 있으면 기존 세션을 반환한다.

    - 세션이 없으면 새로운 세션을 생성하지 않는다. null 을 반환한다.
- `request.getSession()` : 신규 세션을 생성하는 `request.getSession(true)` 와 동일하다.

    - 추가 설명.
        - `request.getSession()`을 하면
        - `request` 정보에서 얻어온! `UUID`값으로 이뤄진 쿠키의 `value` 값을 보고
        - `Session`들을 모아둔 `Session`저장소에서 동일한 sessionId(=UUID) 값이 있는지 찾는다.
        - Session저장소에서  sessionId는  key값으로 쓰인다.
        - 동일한 sessionId(=UUID)가 있으면 해당 Session을 가져오고, sessionId(=UUID)가
            
            없으면 해당 Session을 새로 만들어 반환한다.
            

<br/>

### 세션에 로그인 회원 정보 보관

`session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);`

세션에 데이터를 보관하는 방법은 `request.setAttribute(..)` 와 비슷하다. 

<br/>

하나의 세션에 여러 값을 저장할 수 있다.

LoginController

```java
@PostMapping("/logout")
public String logout3(HttpServletRequest request) {

		 // 세션을 없애는게 목적이기 때문에 false
     HttpSession session = request.getSession(false); 

        if (session != null) {
            session.invalidate(); // invalidate()란? 
																	// 세션이랑 그안에 데이터까지 싹다 지운다.
        }

        return "redirect:/";
    }
```

<br/>

session.invalidate() : 세션을 제거한다.

```java
@GetMapping("/")
public String homeLogin3(HttpServletRequest request, Model model) {

      // false인 이유는
      // 메인페이지에 처음 들어오는 사용자도 세션이 만들어 지는 걸 방지하기 위해서 이다.
      HttpSession session = request.getSession(false);

      if (session == null) {
          return "home";
      }

      Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);

			// 세션에 회원 데이터가 없으면 home
      if (loginMember == null) {
           return "home";
      }

      // 세션이 유지되면 로그인으로 이동
      model.addAttribute("member", loginMember);
      return "loginHome";
}
```

- `request.getSession(false) : request.getSession()` 를 사용하면 기본 값이
    
    `create: true`이므로, 로그인 하지 않을 사용자도 의미 없는 세션이 만들어진다. 
    
    따라서 세션을 찾아서 사용하는 시점에는 `create: false` 옵션을 사용해서 세션을 
    
    생성하지 않아야 한다.
    
- `session.getAttribute(SessionConst.LOGIN_MEMBER)` : 로그인 시점에 세션에 보관한
    
    회원 객체를 찾는다.
    
    - `session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember)`를 하면
    - `request.getSession`(=create 하는 부분)으로 가져온 특정 `Session` 내에
        
        `key(=SESSIONCONST.LOGIN_MEMbER)와 value(=loginMember)`를 저장시켜
        
        나중에, `sessionId(=UUID)`를 통해 특정 Session을 가져올 때
        
        가져온 Session 내에서 `key(loginMember)`를 가지고 loginMember 값을 가져올 수 있다.
        
<br/>        

실행 시켜보면 이렇다.

로그아웃 해도 잘 넘어오는걸 알 수 있다.

![이미지](/programming/img/나19.PNG)

<br/>

## 정리하면

1. 세션 uuid 생성 (1234 라고가정)

2. 세션store(Map)에 저장(1234, 유저A)
3. 쿠키생성("mySessionId", 1234)
4. 쿠키를 response에 저장 후 응답
5. 웹 브라우저에서 서버로 다시 요청을 하면
6. 서버는 클라이언트가 넘겨준 여러 개의 쿠키 중("mySessionId", 1234) 쿠키를 찾고
7. 서버는 이 쿠키(쿠키의 벨류인 UUID)를 가지고 세션store에서 ‘유저A’ 정보를 찾아낸다.

<br/>

### 코드로 정리.

1. 쿠키(쿠키_name , uuid) 키 벨류로 저장된다. 밑에 코드가 해당 부분 그 다음 → 

```java
Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
```

<br/>

2. 세션 저정소(uuid , 세션) 으로 간다. 

여기서 벨류인 ‘세션’은 밑에 코드인 첫번째 줄을 의미한다. 그 다음 →

```java
HttpSession session = request.getSession();
session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
```

<br/>

3. ‘해당 세션’(세션_name , 객체) 로 들어가게 되는 것이다. 

밑에 코드가 ‘해당 세션’ 부분이다.

```java
Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
```

sessionid(=uuid)를 key로 갖고 session(저장공간)을 value로 갖는 세션저장소(Map) 가 있는 것이다.

<br/>

sessionid(=uuid)는 tomcat에서 생성합니다.

![이미지](/programming/img/나20.PNG)

<br/>

>**Reference** <br/>스프링 MVC 2편 - 백엔드 웹 개발 활용 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2