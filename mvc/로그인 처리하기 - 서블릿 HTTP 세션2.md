## 로그인 처리하기 - 서블릿 HTTP 세션2

`@SessionAttribute`

스프링은 세션을 더 편리하게 사용할 수 있도록 `@SessionAttribute` 을 지원한다.

이미 로그인 된 사용자를 찾을 때는 다음과 같이 사용하면 된다. 

<br/>

### 참고로 이 기능은 세션을 생성하지 않는다.

그냥 세션을 찾아올때 이 기능을 사용하면 되는 것이다.

`@SessionAttribute(name = "loginMember", required = false) Member loginMember`

<br/>

HomeController

```java
@GetMapping("/")
public String homeLogin3Spring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER,
                                required = false) Member loginMember, Model model) {

    //세션에 회원 데이터가 없으면 home
    if (loginMember == null) {
          return "home";
    }

    //세션이 유지되면 로그인으로 이동
    model.addAttribute("member", loginMember);
      return "loginHome";
    }
```

세션을 찾고, 세션에 들어있는 데이터를 찾는 번거로운 과정을 스프링이 한번에 

편리하게 처리해주는 것을 확인할 수 있다.

<br/>

## TrackingModes

로그인을 처음 시도하면 URL이 다음과 같이 jsessionid 를 포함하고 있는 것을 확인할 수 있다.


localhost:8080/;jsessionid=F59911518B921DF62D09F0DF8F83F872


![이미지](/programming/img/나21.PNG)

이것은 웹 브라우저가 쿠키를 지원하지 않을 때 쿠키 대신 URL을 통해서 

세션을 유지하는 방법이다. 이 방법을 사용하려면 URL에 이 값을 계속 포함해서 전달해야 한다. 

<br/>타임리프 같은 템플릿은 엔진을 통해서 링크를 걸면 jsessionid 를 URL에 자동으로 포함해준다. 

서버 입장에서 웹 브라우저가 쿠키를 지원하는지 하지 않는지 최초에는 판단하지 못하므로, 

쿠키 값도 전달하고, URL에 jsessionid 도 함께 전달한다

<br/>URL 전달 방식을 끄고 항상 쿠키를 통해서만 세션을 유지하고 싶으면 다음 옵션을 

넣어주면 된다. 이렇게 하면 URL에 jsessionid 가 노출되지 않는다.

<br/>

`application.properties`
```java
server.servlet.session.tracking-modes=cookie
```

<br/>

>**Reference** <br/>스프링 MVC 2편 - 백엔드 웹 개발 활용 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2