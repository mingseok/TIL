## 웰컴 페이지, WEB-INF 디렉토리

스프링을 실행 시키고, URL에 [http://localhost:8080/](http://localhost:8080/) 입력하면 초기화면이 출력된다.

이 화면을 `웰컴 페이지` 라고 부른다. (도메인 왔을때 첫화면)

<br/>

이 화면은 webapp 디렉토리 안에 index.html 이라고 작성하면 

여기가 웰컴 페이지랑 연동이 되는 것이다.

![이미지](/programming/img/입문45.PNG)

<br/><br/>

## WEB-INF 디렉토리

- 항상 컨트롤러를 거쳐서 내부에서 포워드 등 작업을 해야 페이지 출력이 되는 것이다.

- 컨트롤러를 통하지 않는다면 파일이 있음에도, 호출이 되지 않는다.
<br/>

## 궁금증!

```java
WEB-INF 는 누가 막는 건가
```

톰캣 같은 서블릿 컨테이너가 막는다. 서블릿 스펙에 그렇게 적혀 있다.

```java
/WEB-INF 아래의 파일은 클라이언트가 직접 요청할 수 없다
```

<br/>

브라우저가 `/WEB-INF/views/members.jsp` 를 치면 `404` 가 나온다.

파일이 있는데도 없다고 하는 것이다.

<br/>

## 왜 이런 규칙이 필요한가

JSP 안에 로직이 있을 수 있기 때문이다.

```java
<%
    Member member = (Member) request.getAttribute("member");
%>
<html><body><%=member.getName()%></body></html>
```

<br/>

컨트롤러를 안 거치고 이 JSP에 바로 들어오면 `member` 가 `null` 이다.

앞의 JSP, 서블릿과 JSP의 한계 글에서 본 그 구조라 그렇다.

<br/>

그리고 설정 파일이 노출되는 것도 막는다.

```java
/WEB-INF/web.xml          - 서블릿 설정
/WEB-INF/classes/         - 컴파일된 클래스
/WEB-INF/lib/             - 라이브러리 jar
```

<br/>

이게 다운로드되면 소스와 설정이 통째로 새어나가는 것이다.

<br/>

## 포워드는 되고 리다이렉트는 안 된다

```java
request.getRequestDispatcher("/WEB-INF/views/members.jsp").forward(request, response);   // 된다
response.sendRedirect("/WEB-INF/views/members.jsp");                                     // 404
```

<br/>

앞의 리다이렉트, 디스패처 글에서 본 그 차이다.

```java
포워드     - 서버 내부에서 넘긴다. 브라우저가 모른다
리다이렉트 - 브라우저에게 다시 요청하라고 시킨다. 브라우저의 요청이라 막힌다
```

<br/>

`클라이언트가 직접 요청할 수 없다` 는 규칙이라, 서버 내부 이동은 허용되는 것이다.

<br/>

## 스프링 부트에서는 좀 다르다

내장 톰캣을 쓰면 `WEB-INF` 를 잘 안 쓴다.

```java
src/main/resources/templates/    - 타임리프. 여기도 브라우저가 직접 못 본다
src/main/resources/static/       - 정적 파일. 브라우저가 직접 본다
```

<br/>

`templates` 아래는 클래스패스 안이라 URL로 접근할 수 없다.

`WEB-INF` 와 같은 효과가 다른 방식으로 나는 것이다.

<br/>

`static` 은 그대로 서빙된다.

```java
static/css/main.css   ->  /css/main.css 로 접근된다
```

<br/>

그래서 여기에 민감한 파일을 두면 안 된다.

`application.yml` 을 실수로 `static` 에 넣으면 그대로 노출되는 것이다.

<br/>

## 웰컴 페이지 찾는 순서

스프링 부트는 이 순서로 찾는다.

```java
1. static/index.html
2. templates/index.html  (템플릿 엔진이 있으면)
3. @GetMapping("/") 컨트롤러
```

<br/>

`index.html` 이 `static` 에 있으면 컨트롤러보다 먼저 잡힌다.

<br/>

컨트롤러를 만들었는데 안 불린다면 이걸 의심하면 된다.

앞의 스프링 MVC 구조 글에서 본 대로

정적 리소스 핸들러가 `DispatcherServlet` 보다 먼저 잡아가는 경우가 있다.

<br/>

## 옛날 방식의 웰컴 페이지

`web.xml` 에 적었다.

```xml
<welcome-file-list>
    <welcome-file>index.html</welcome-file>
    <welcome-file>index.jsp</welcome-file>
</welcome-file-list>
```

<br/>

디렉토리로 요청이 오면 이 목록을 위에서부터 찾는다.

<br/>

지금은 설정 파일이 사라지고 규칙으로 대체됐다.

앞의 스프링이 뭔가 글에서 본 부트의 방향이다.

```java
설정을 적는 대신, 정해진 자리에 두면 알아서 찾는다
```

<br/>

편한 대신 `왜 이게 되는지` 를 모르고 쓰게 되는 면도 있다.

안 될 때 어디를 봐야 할지 모르게 되는 것이다.
