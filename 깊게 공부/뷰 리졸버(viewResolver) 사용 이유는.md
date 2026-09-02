## 뷰 리졸버(viewResolver) 사용 이유는?


ViewResolver는 DispatcherServlet에서 `“이런 이름을 가진 View를 줘”` 라고 

요청하면 `DispatcherServlet`에서 전달한 View 이름을 해석한 뒤 View 객체를 리턴 해주는 역할을 합니다.

<br>

스프링에서 데이터를 처리하거나 가지고 왔다면, 이 데이터를 `View의 영역으로 전달`을 해야 한다. 

View를 어떤 것을 사용할지 `자유롭게 설정을` 가능하다.

```
그때, `설정 역할`을 하는 것이 View Resolver라고 생각하면 된다.
```


<br><br>


## 왜 개념을 분리 했는가?

- 논리 뷰 이름 : ‘members’

- 물리 뷰 경로 : ‘/WEB-INF/views/members.jsp’

```java
private static MyView viewResolver(String viewName) {
    return new MyView("/WEB-INF/views/" + viewName + ".jsp");
}
```

만약, 나중에 폴더 이름이 `‘views’` 되어 있는 것이, `‘jsp’`로 바뀌는 것이다.

그러면 어떻게 해야 되는가? → `컨트롤러를 건들 필요가 없다!`

<br>

나는 위 코드인 `viewResolver()` 메서드만 변경하면 되는 것이다.

그리하여 논리적인 이름과 물리적인 이름은 나눠 놓으면, 

변경 사항이 발생 했을때, 컨트롤러 코드는 변경하지 않아도 되는 장점이 있는 것이다.



<br/><br/>


## 입력 화면, 출력 화면

<br/>

클라이언트는 컨트롤 클래스를 호출 시킨다는 것을 명심하자.

그리고 그것을 우리 알맞게 처리해주는 것이다.

![이미지](/programming/img/입문544.PNG)

<br/><br/>

## 흐름 

생성자에 들어가 있는 `‘키’` 값들은 `html`이나 `class` 주소들이 아니다.. (착각하지 말자.)

단지, `‘벨류’` 인 `객체`를 찾기 위해서 임의로 정해 놓은 문자열 일뿐이다.

1. 클라이언트의 모든 요청은 전체 컨트롤러가 요청을 받게 된다.

2. 전체 컨트롤러에서 해당 컨트롤러를 찾아 연결 시켜준다.
3. 해당 컨트롤러가 해당 뷰 페이지로 연결 시켜준다.
4. 클라이언트는 입력값을 입력한다. 그리고 서브밋 한다.
5. 다시 전체 컨트롤러로 와서 해당 URL의 컨트롤러를 찾는다.
6. 찾은 해당 컨트롤러로 가서 저장 시킬 데이터들은 저장 시키고,
7. 해당 뷰 페이지로 이동 된다.




<br/><br/>

>**Reference** <br/>[스프링 MVC 2편 - 백엔드 웹 개발 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2/dashboard)
<br/>

## 궁금증!

```java
"View 를 자유롭게 설정 가능하다" 고 했는데, 실제로 몇 가지나 있을까?
```

`spring-webmvc` jar 안의 구현체를 세어봤다.

```java
InternalResourceViewResolver     - JSP
AbstractTemplateViewResolver     - 템플릿 엔진 공통
FreeMarkerViewResolver           - FreeMarker
GroovyMarkupViewResolver         - Groovy
ScriptTemplateViewResolver       - 자바스크립트 템플릿
XsltViewResolver                 - XML 변환
BeanNameViewResolver             - 뷰 이름과 같은 이름의 빈을 찾는다
ContentNegotiatingViewResolver   - 요청 헤더를 보고 뷰를 고른다
```

<br/>

타임리프는 별도 라이브러리라 이 목록에 없지만 `ThymeleafViewResolver` 로 같은 자리에 끼어든다.

<br/>

## 여러 개를 동시에 등록할 수도 있다

`DispatcherServlet` 은 `ViewResolver` 를 하나가 아니라 목록으로 들고 있다.

```java
resolveViewName("members") 를 첫 번째 리졸버에게 물어본다
  -> null 을 돌려주면 다음 리졸버에게 물어본다
  -> View 를 돌려주면 거기서 멈춘다
```

<br/>

그래서 JSP와 타임리프를 같이 쓰는 것도 가능하다.

앞의 프론트 컨트롤러 글에서 본 `HandlerAdapter` 를 고르는 방식과 똑같다.

<br/>

## ContentNegotiatingViewResolver 가 재밌다

이건 뷰를 직접 만들지 않고, 다른 리졸버들에게 물어본 뒤 `요청에 맞는 것을 고르는` 역할만 한다.

```java
GET /members  (Accept: text/html)         -> HTML 뷰를 고른다
GET /members  (Accept: application/json)  -> JSON 뷰를 고른다
GET /members.json                         -> 확장자를 보고 JSON 을 고른다
```

<br/>

같은 URL인데 요청 헤더에 따라 다른 형식으로 응답하는 것이다.

`논리 뷰 이름` 하나로 여러 형식을 낼 수 있는 이유가 여기에 있다.

<br/>

## 그런데 요즘은 뷰 리졸버를 안 타는 경우가 많다

`@ResponseBody` 나 `@RestController` 를 쓰면 뷰 리졸버가 아예 안 불린다.

```java
@RestController
public class MemberApi {

    @GetMapping("/api/members")
    public List<MemberResponse> list() {
        return memberService.findAll();     // 뷰 이름이 아니라 데이터 그 자체
    }
}
```

<br/>

반환값이 뷰 이름이 아니라 데이터이기 때문에, `HTTP 메시지 컨버터` 가 대신 처리한다.

객체를 JSON 문자열로 바꿔서 응답 본문에 바로 써버리는 것이다.

```java
컨트롤러가 String 을 반환         -> ViewResolver 가 그 이름으로 화면을 찾는다
컨트롤러가 @ResponseBody 로 반환  -> HttpMessageConverter 가 JSON 으로 바꿔 바로 내려준다
```

<br/>

프론트를 React나 Vue로 따로 만들면 서버는 데이터만 내려주면 되니,

이 경우 뷰 리졸버는 정적 파일을 내려줄 때 외에는 거의 쓰이지 않는다.

<br/>

## 그래도 개념은 남는다

논리 이름과 물리 경로를 나눈다는 발상 자체는 뷰에만 있는 게 아니다.

```java
논리 뷰 이름 "members"  ->  물리 경로 /WEB-INF/views/members.jsp
논리 빈 이름 "payClient" ->  실제 구현체 TossPayClient
메시지 키 "member.name"  ->  실제 문구 "회원 이름" 또는 "Member Name"
```

<br/>

전부 `부르는 이름` 과 `실제 대상` 을 분리해서, 실제 대상이 바뀌어도 부르는 쪽은 안 바뀌게 하는 구조다.

원문의 `컨트롤러를 건들 필요가 없다` 가 이 구조가 주는 이득이다.
