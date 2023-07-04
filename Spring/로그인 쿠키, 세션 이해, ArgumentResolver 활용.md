## 로그인 쿠키, 세션 이해, ArgumentResolver 활용


<br/>

## 쿠키 보안 문제

쿠키 값은 임의로 변경할 수 있다.

- 클라이언트가 쿠키를 강제로 변경하면 다른 사용자가 된다.

- 실제 웹브라우저 개발자모드 Application Cookie 변경으로 확인
- Cookie: memberId=1 Cookie: memberId=2 (다른 사용자의 이름이 보임)

<br/><br/>

## 세션 동작 방식

![이미지](/programming/img/입문134.PNG)

### 여기서 중요한 포인트

- 회원과 관련된 정보는 전혀 클라이언트에 전달하지 않는다는 것이다.

    - 중요한 정보들은 서버의 세션 저장소에 관리 된다.

    - 그리하여 임의로 세션id를 지정해줘서, (키, 벨류) 형태로 보관한다.

- 오직 추정 불가능한 세션 ID만 쿠키를 통해 클라이언트에 전달한다.

    - 이유는, 세션id 인 ‘키’ 값으로 서버에 접근하기에 내 컴퓨터가 해킹 당해도, 
    내 정보들을 지킬 수 있게 되는 것이다.

- 해커가 쿠키를 털어가도 시간이 지나면 사용할 수 없도록 서버에서 세션의 만료시간을 
짧게(ex: 30분) 유지한다.

<br/><br/>

## 세션이란?

세션이라는 것이 특별한 것이 아니라 단지 쿠키를 사용하는데, 

서버에서 데이터를 유지하는 방법일 뿐이라는 것이다.

<br/><br/>

## @SessionAttribute

스프링은 세션을 더 편리하게 사용할 수 있도록 `@SessionAttribute` 을 지원한다.

<br/>

### 그리하여

이미 로그인 된 사용자를 찾을 때 다음과 같이 사용하면 된다. 

참고로 이 기능은 세션을 생성하지 않는다.

```java
@SessionAttribute(name = "loginMember", required = false) Member loginMember
```

<br/>

### `HomeController`

```java
@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String homeLoginV3Spring(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {

        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}
```

<br/>

필수 값 여부: required 대상이 없어도 동작하게 하려면 (required = false) 로 지정하면 된다

```java
required = false는 쿠키값이 있는지 없는지 여부를 나타내는 것입니다.

false로 설정되어 있으면 쿠키값이 없어도 메소드가 정상적으로 호출됩니다. 

그러나 true로 설정되어 있으면 해당 쿠키값이 없으면 에러가 발생합니다.
```

<br/><br/>

## 세션 타임아웃 설정

세션은 사용자가 `로그아웃`을 직접 호출 되는 경우에 삭제된다.

그런데, 대부분의 사용자는 로그아웃을 선택하지 않고, 그냥 웹 브라우저를 종료한다. 

<br/>

문제는 `HTTP가 비연결성`이므로 서버 입장에서는 해당 사용자가

웹 브라우저를 종료한 것인지 아닌지를 인식할 수 없다. 

<br/>

따라서 서버에서 세션 데이터를 언제 삭제해야 하는지 판단하지 못한다.

이 경우 남아있는 세션을 무한정 보관하면 다음과 같은 문제가 발생할 수 있다.

- 메모리의 크기가 무한하지 않기 때문에 꼭 필요한 경우만 생성해서 사용해야 한다

<br/><br/>

## 세션의 종료 시점

세션 생성 시점이 아니라 사용자가 서버에 최근에 요청한 시간을 기준으로 30분 정도를 유지.

이렇게 하면 사용자가 서비스를 사용하고 있으면, 세션의 생존 시간이 30분으로 계속 늘어나게 된다. 

따라서 30분 마다 로그인해야 하는 번거로움이 사라진다.

<br/><br/>

## 전체 세션 적용 방법

`application.properties` 설정하면 된다.

```java
server.servlet.session.timeout=1800 // 60(1분), 120(2분), ...)
```

<br/>

### 특정 세션 단위로 시간 설정 방법

```java
session.setMaxInactiveInterval(1800); //1800초
```

<br/><br/>

## ArgumentResolver 활용 (이 방법으로 사용하기)

- 여기서는, 로그인 된 사용자에 대한 정보를 얻는 것을 간단하게 하기 위한 방법

- 로그인 회원을 편리하게 찾는 방법.
- 아규먼트에 대해서 내가 직접 해결한다는 것이다.

<br/><br/>

## 이걸 사용한 목표는?

`@Login` 애노테이션을 만들어서, `@Login Member loginMember` 부분에 

로그인 된 맴버를 바로 들어오게 해주는 것이다.

### `HomeController.class`

![이미지](/programming/img/입문135.PNG)

<br/>

`@Login` 직접 만든 애노테이션을 `Member loginMember` 로 로그인 된 맴버를 주입 시키는것

```java
@Controller
public class HomeController {

    @GetMapping("/")
    public String homeLoginArgumentResolver(@Login Member loginMember, Model model) {

        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}
```

<br/><br/>

### `Login @interface`

![이미지](/programming/img/입문136.PNG)

```java
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {
}
```

`@Target(ElementType.PARAMETER) :` 파라미터에만 사용한다는 뜻이다.

`@Retention(RetentionPolicy.RUNTIME) :` 리플렉션 등을 활용할 수 있도록 런타임까지 
애노테이션 정보가 남아있음

<br/><br/>

### `LoginMemberArgumentResolver.class`

```java
@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) { // 지원하는 것인지 확인
        log.info("supportsParameter 실행");

        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && hasMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        log.info("resolveArgument 실행");

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }

        // 리턴 되는 것은 Member 객체이다.
        // 그리하여 HomeController Member 매개변수에 들어가게 되는 것이다.
        // 만약 없으면 null이 넘어간다.
        // 밑에 추가 설명
        return session.getAttribute(SessionConst.LOGIN_MEMBER);
    }

```

- `hasParameterAnnotation(…) :` 이, 파라미터에 애노테이션 정보가 있는지 확인 하는 것이다.

    - 즉, `HomeController`에 `@Login` 애노테이션이 붙어 있는지 확인 하는 것이다.

<br/>

### 코드 설명

```java
boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());
```

- 여기서 `getParameterType()` 은? → `HomeController`에 `@Login Member loginMember` 인 `Member`타입을 말한다.
    
- 반환 되는 것은 같다면 `true`를 아니면 `false` 를 반환 한다.

<br/>

그리하여 `return hasLoginAnnotation && hasMemberType;` 둘 다 만족하면 

true가 되어 코드 기준, 밑에 있는 메서드인 `resolveArgument()` 가 실행이 되는 것이다.

- 만약 false면 `resolveArgument()` 실행 되지 않음

<br/><br/>

## 코드 설명.

### `resolveArgument()` 메서드에서 정보를 가지고 작업을 한다.

```java
HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
```

- `webRequest.getNativeRequest()` 사용하면 `HttpServletRequest` 를 뽑을 수 있다.

    - 타입 캐스팅까지 해줘야 한다.

<br/>

### 코드 설명.

`request.getSession(false)` 은 현재 요청에 대한 세션이 이미 존재하는 경우 해당 세션을 반환하고, 

세션이 존재하지 않는 경우 `null`을 반환 한다.

```java
HttpSession session = request.getSession(false);

if (session == null) {
    return null;
}
```

<br/>

### 코드 설명.

세션에서 로그인 회원 정보를 꺼내는 것이다.

그렇다면, 어디서 로그인 회원 정보를 저장 했는지 궁금할 것이다.

그 부분은 `LoginController.class` 에서 이다. (밑에 있음)

```java
return session.getAttribute(SessionConst.LOGIN_MEMBER);
```

<br/>

### 코드 설명.

`LoginController.class`

```java
@Controller
public class LoginController {

    @PostMapping("/login")
    public String loginV4(@Valid @ModelAttribute LoginForm form,
                          BindingResult bindingResult,
                          @RequestParam(defaultValue = "/") String redirectURL,
                          HttpServletRequest request) {

        //... 생략

        //로그인 성공 처리
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();

        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:" + redirectURL;

    }
}
```

<br/>

### `WebConfig.class`

아까 만든 `LoginMemberArgumentResolver` 클래스를 생성해주면 끝인 것이다.

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }
}
```

<br/><br/>

>**Reference** <br/>[스프링 MVC 2편 - 백엔드 웹 개발 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2/dashboard)
