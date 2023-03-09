## JSP, 서블릿과 JSP의 한계

JSP 라이브러리 추가

JSP를 사용하려면 먼저 다음 라이브러리를 추가해야 한다.

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