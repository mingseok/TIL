## JSP, 서블릿과 JSP의 한계

JSP 라이브러리 추가

JSP를 사용하려면 먼저 다음 라이브러리를 추가해야 한다.

<br/>

`build.gradle` 에 추가

```java
//JSP 추가 시작
implementation 'org.apache.tomcat.embed:tomcat-embed-jasper'
implementation 'javax.servlet:jstl'
//JSP 추가 끝
```

<br/>

`new-form.jsp` 를 생성한다.

```html
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/jsp/members/save.jsp" method="post">
    username: <input type="text" name="username"/>
    age: <input type="text" name="age"/>
    <button type="submit">전송</button>
</form>
</body>
</html>
```

<br/>

`save.jsp` 를 생성한다.

```jsp
<%@ page import="hello.servlet.domain.member.Member" %>
<%@ page import="hello.servlet.domain.MemberRepository" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    MemberRepository memberRepository = MemberRepository.getInstance();

    //request, response 사용 가능
    System.out.println("MemberSaveServlet.service");
    String username = request.getParameter("username");
    int age = Integer.parseInt(request.getParameter("age"));

    Member member = new Member(username, age);
    memberRepository.save(member);
%>

<html>
<head>
    <title>Title</title>
</head>
<body>
성공
<ul>
    <li>id=<%=member.getId()%></li>
    <li>username=<%=member.getUsername()%></li>
    <li>age=<%=member.getAge()%></li>
</ul>

<a href="/index.html">메인</a>

</body>
</html>
```

<br/>

실행 시켜보면,

localhost:8080/ 검색하고 들어가서

‘JSP 회원가입’ 을 클릭하여 이름과 나이를 입력 후에 전송 보내면 

<br/>이렇게 출력 되는 걸 알 수 있다.

![이미지](/programming/img/서28.PNG)

<br/>

`members.jsp` 를 생성해준다.

```jsp
<%@ page import="hello.servlet.domain.member.Member" %>
<%@ page import="java.util.List" %>
<%@ page import="hello.servlet.domain.MemberRepository" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    MemberRepository memberRepository = MemberRepository.getInstance();
    List<Member> members = memberRepository.findAll();
%>

<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<a href="/index.html">메인</a>
<table>
    <thead>
    <th>id</th>
    <th>username</th>
    <th>age</th>
    </thead>
    <tbody>
    <%
        for (Member member : members) {
            out.write(" <tr>");
            out.write(" <td>" + member.getId() + "</td>");
            out.write(" <td>" + member.getUsername() + "</td>");
            out.write(" <td>" + member.getAge() + "</td>");
            out.write(" </tr>");
        }
    %>
    </tbody>
</table>
</body>
</html>
```

<br/>

실행시켜서 값을 입력하고

‘회원목록’ 창을 본다면 이렇게 리스트로 출력 되어 있는 것을 알 수 있다.

![이미지](/programming/img/서29.PNG)

<br/>

## 정리

`<%@ page contentType="text/html;charset=UTF-8" language="java" %>`

첫 줄은 JSP문서라는 뜻이다. JSP 문서는 이렇게 시작해야 한다.

<br/>실행시 `.jsp` 까지 함께 적어주어야 한다.

localhost:8080/jsp/members/new-form.jsp

<br/>JSP는 자바 코드를 그대로 다 사용할 수 있다.

`<%@ page import="hello.servlet.domain.member.MemberRepository" %>`

<br/>

## 자바의 `import` 문과 같다.

<br/>

`<% ~~ %>`

이 부분에는 자바 코드를 입력할 수 있다.

<br/>

`<%= ~~ %>`

이 부분에는 자바 코드를 출력할 수 있다.

<br/>회원 저장 JSP를 보면, 회원 저장 서블릿 코드와 같다. 

다른 점이 있다면, HTML을 중심으로 하고, 

<br/>자바코드를 부분 부분 입력해주었다. 

`<% ~ %>` 를 사용해서 HTML 중간에 자바 코드를 출력하고 있다.

<br/><br/>

## 서블릿과 JSP의 한계



### 서블릿

서블릿으로 개발할 때는 뷰(View)화면을 위한 HTML을 만드는 작업이 자바 코드에 섞여서 지저분하고
복잡했다.


<br/>


JSP를 사용한 덕분에 뷰를 생성하는 HTML 작업을 깔끔하게 가져가고, 

중간중간 동적으로 변경이 필요한
부분에만 자바 코드를 적용했다. 

<br/>

### 그런데 이렇게 해도 해결되지 않는 몇가지 고민이 남는다.


회원 저장 JSP를 보자. 코드의 상위 절반은 회원을 저장하기 위한 비즈니스 로직이고,

나머지 하위 절반만
결과를 HTML로 보여주기 위한 뷰 영역이다. 

<br/>

### 회원 목록의 경우에도 마찬가지다.


코드를 잘 보면, JAVA 코드, 데이터를 조회하는 리포지토리 등등 다양한 코드가 모두 JSP에 노출되어 있다.

<br/>


### JSP가 너무 많은 역할을 한다. 

수백 수천줄이 넘어가는
JSP를 떠올려보면 정말 지옥과 같을 것이다.




<br/><br/>

>**Reference** <br/>[스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1)

<br/>

## 궁금증!

```java
JSP 는 결국 무엇이 되는가
```

서블릿이 된다. 톰캣이 JSP를 자바 파일로 바꾼 다음 컴파일한다.

```java
members.jsp
  -> members_jsp.java     (톰캣이 만든 서블릿 소스)
  -> members_jsp.class
```

<br/>

`work/Catalina/localhost/` 아래에 이 파일들이 생긴다.

<br/>

변환된 코드를 열어보면 이런 식이다.

```java
public final class members_jsp extends HttpJspBase {
    public void _jspService(HttpServletRequest request, HttpServletResponse response) {
        out.write("<html>\n<body>\n");
        out.print(member.getName());
        out.write("</body>\n</html>");
    }
}
```

<br/>

HTML은 `out.write()` 로, `<%= %>` 는 `out.print()` 로 바뀐다.

<br/>

앞의 서블릿 설명 내부 동작 글에서 본 그 서블릿이다.

`HTML 안에 자바를 쓴다` 는 게 실제로는 `자바 안에 HTML 문자열을 쓴다` 로 바뀌는 것이다.

<br/>

## 그래서 첫 요청이 느리다

변환하고 컴파일하는 시간이 걸린다.

<br/>

한 번 하고 나면 클래스가 남아서 그 다음부터는 빠르다.

파일이 바뀌면 다시 한다.

<br/>

미리 해두는 옵션도 있다.

```java
JSP precompile
```

<br/>

배포할 때 미리 컴파일해두면 첫 요청도 빨라진다.

<br/>

## 한계라는 게 정확히 무엇인가

세 가지다.

```java
1. 뷰와 로직이 섞인다
2. 자바 코드에 오류가 나도 컴파일할 때 안 잡힌다
3. 테스트할 방법이 마땅치 않다
```

<br/>

`2번` 이 특히 곤란하다.

```java
<%= member.getNmae() %>       <- 오타
```

<br/>

IDE가 잡아주지 못하는 경우가 많고, 실행해봐야 알 수 있다.

앞의 컴파일 시점에 오류를 잡는 것의 이점 글에서 본 그 차이다.

<br/>

`3번` 도 크다.

JSP를 테스트하려면 서버를 띄우고 요청을 보내야 한다.

자바 클래스면 그냥 `new` 해서 부르면 되는데 그게 안 된다.

<br/>

## 타임리프가 무엇을 해결했나

HTML 파일 그대로다.

```java
<span th:text="${member.name}">이름</span>
```

<br/>

브라우저로 이 파일을 그냥 열면 `이름` 이라고 나온다.

서버를 안 띄워도 디자인을 확인할 수 있는 것이다.

<br/>

앞의 타임리프 특징 글에서 본 `내추럴 템플릿` 이 이것이다.

```java
JSP    - 브라우저로 열면 <% %> 가 그대로 보인다. 깨진다
타임리프 - 브라우저로 열면 정상적인 HTML 로 보인다
```

<br/>

그리고 자바 코드를 못 쓴다. 표현식만 쓸 수 있다.

로직을 넣고 싶어도 못 넣게 막아둔 것이다.

<br/>

## 스크립틀릿이 왜 생겼나

처음부터 나쁜 설계였던 게 아니다.

<br/>

서블릿만 있던 시절에는 HTML을 이렇게 만들었다.

```java
out.write("<html>");
out.write("<body>");
out.write("<h1>" + member.getName() + "</h1>");
out.write("</body>");
out.write("</html>");
```

<br/>

앞의 서블릿 글에서 본 그 방식이다.

<br/>

HTML 하나 고치려면 자바 코드를 고치고 다시 컴파일해야 했다.

디자이너는 손도 못 댄다.

<br/>

JSP는 이걸 뒤집었다.

```java
서블릿 - 자바 안에 HTML
JSP    - HTML 안에 자바
```

<br/>

당시로는 큰 개선이었다. HTML이 HTML답게 보이게 된 것이다.

<br/>

## 그런데 다시 섞였다

로직을 넣을 수 있으니 사람들이 넣었다.

```java
<%
    MemberRepository repository = MemberRepository.getInstance();
    List<Member> members = repository.findAll();
%>
```

<br/>

여기서 MVC가 나온 것이다.

앞의 MVC(=Model View Controller) 글에서 본 그 흐름이다.

```java
Model 1  -> JSP 가 다 한다
Model 2  -> 서블릿이 처리하고 JSP 는 화면만
```

<br/>

`할 수 있게 열어두면 하게 된다` 는 게 교훈인 셈이다.

타임리프가 아예 자바를 못 쓰게 막은 것도 그래서다.

<br/>

## 지금 JSP 를 새로 쓸 일은 없다

스프링 부트에서는 오히려 설정이 번거롭다.

```java
내장 톰캣 + jar 로 배포하면 JSP 가 안 된다
war 로 말아야 하거나 별도 설정이 필요하다
```

<br/>

앞의 WAS 서버와 WEB 서버의 이해 글에서 본 대로

`jar` 하나로 배포하는 흐름과 JSP가 잘 안 맞는 것이다.

<br/>

그래도 알아둘 이유는 있다.

오래된 프로젝트에는 아직 많이 남아 있고,

`왜 지금 방식이 이렇게 됐는지` 를 알려면 그 앞을 봐야 하기 때문이다.
