# 세션

서버 세션을 사용하면 클라이언트의 상태를 저장할 수 있다.

쿠키와의 차이점은 세션은 웹 브라우저가 아니라 서버에 저장한다는 점이다. 

서버는 세션을 사용해서 클라이언트 상태를 유지할 수 있기 때문에, 로그인한 사용자 정보를 유지하기 위한 목적으로 세션을 사용한다.

웹 브라우저에 정보를 보관할 때 쿠키를 사용한다면, 세션은 웹 컨테이너에 정보를 보관할 때 사용한다. 세션은 오직 서버에만 생성된다. 
<br/><br/><br/>
![이미지](/programming/img/컨테이너.PNG)

웹 컨테이너는 기본적으로 한 웹 브라우저마다 한 세션을 생성한다. 예를 들어 사진 처럼 JSP1 과 JSP2가 세션을 사용한다고 해보자. 이 경우 웹 브라우저1이 JSP1 과 JSP2를 실행 하면, 웹 브라우저1과 관련된 세션1을 사용한다.  웹 브라우저2가 JSP1과 JSP2를 실행하면 웹 브라우저2와 관련된 세션2를 사용한다. 즉, 같은 JSP 페이지라도 웹 브라우저에 따라 서로 다른 세션을 사용한다.<br/><br/>

웹 브라우저마다 세션이 따로 존재 하기 때문에, 세션은 웹 브라우저와 관련된 정보를 저장하기에 알맞은 장소이다. 즉, 쿠키가 클라이언트 측의 데이터 보관소라면 세션은 서버측의 데이터 보관소인 것이다. <br/><br/><br/>

JSP에서 세션을 생성하려면 page 디렉티브의 session 속성을 “true”로 지정하면 된다.

```java
<%@ page session = "true" %>
```

그런데 page 디렉티브의 session 속성은 기본값이 true 이므로 session 속성값을 false로 지정하지만 

않으면 세션이 생성된다. 세션이 생성되면 session 기본 객체를 통해서 세션을 사용할 수 있다.

세션을 사용하는 서버 프로그램에 웹 브라우저가 처음 접속할 때 세션을 생성하고, 이후로는 이미 생성된 세션을 사용한다. <br/><br/><br/>

| 메서드 | 리턴 타입 | 설명 |
| --- | --- | --- |
| getId( ) | String | 세션의 고유 ID를 구한다 (세션 ID라고 한다. |

웹 브라우저마다 별도의 세션을 갖는다고 했다. 이때 각 세션을 구분하기 위해 세션마다 고유 ID를 할당하는데 그 아이디를 세션  ID라고 한다. 웹 서버는 웹 브라우저에 세션 ID를 전송한다. 웹 브라우저는 웹 서버에 연결할 때마다 매번 세션 ID를 보내서 웹 서버가 어떤 세션을 사용할지 판단 할 수 있게 한다.<br/><br/>

웹 서버는 세션 ID를 이용해서 웹 브라우저를 위한 세션을 찾기 때문에, 웹 서버와 웹 브라우저는 세션 ID를 공유할 수 있는 방법이 필요하다. 세션 ID를 공유하기 위해 사용하는 것이 쿠키이다.
<br/><br/>
쿠키 목록 중에 이름이 JSESSIONID인 쿠키가 있는 것을 알 수 있다. 이 JSESSIONID 쿠키가 세션 ID를 공유할 때 사용하는 쿠키이다.<br/><br/>

세션에 값을 저장할 때는 속성을 사용한다. 속성에 값을 저장하려면 setAttribute( ) 메서드를 사용하고, 속성값을 읽으려면 getAttribute( ) 메서드를 사용한다.

```java
session.setAttribute("MEMBERID", "madvirus");
session.setAttribute("NAME", "최범균");
```

<br/><br/>sesstion 기본 객체에 저장한 두 속성을 사용할 수 있게 된다.

일단은 session 기본 객체에 속성을 설정하면 세션이 종료되기 전까지는 다음과 같이 속성값을 사용할 수 있다.

```java
<% 
		String name = (String)session.getAttribute(NAME"); // 이렇게 사용한다.
%>

회원명: <%= name %>
```

## 쿠키 vs 세션

쿠키 대신에 세션을 사용하는 가장 큰 이유는 세션이 쿠키보다 보안에서 앞선다는 점이다.

쿠키의 이름이나 데이터는 네트워크를 통해 전달되기 때문에 http 프로토콜을 사용하는 경우 중간에 누군가 쿠키의 값을 읽어올 수 있다. 하지만, 세션의 값은 오직 서버에만 저장 되기 때문에 중요한 데이터를 저장하기에 알맞은 장소이다.<br/><br/><br/>

## 세션 종료

세션을 유지할 필요가 없다면 session.invalidate( ) 메서드를 사용해서 세션을 종료한다.

session 기본 객체에 저장했던 속성 목록도 함께 삭제한다. 

```java
<%
		session.invalidate();
%>
```

<br/><br/>HttpSession을 생성하는 또 다른 방법은 request 기본 객체의 getSession( ) 메서드를 사용 하는 것이다. request.getSession( ) 메서드는 현재 요청과 관련된 session 객체를 리턴한다.  request.getSession( ) 을 이요해서 세션을 구하므로, page 디렉티브의 session 속성값은 false로 지정한다. 

```java
<% page session="false" %>
<% 
		HttpSession httpSession = request.getSession();
		List list = (List)httpSession.getAttribute("list");
		list.add(productId);
%>
```

request.getSession( ) 메서드는 session이 존재하면 해당 session을 리턴하고 존재하지 않으면 새롭게 session을 생성해서 리턴한다.

session이 생성된 경우에만 session 객체를 구하고 싶다면, getSession( ) 메서드에 false를 인자로 전달하면 된다. request.getSession(false)를 실행하면 세션이 존재하는 경우에만 session 객체를 리턴하고 세션이 존재하지 않으면 null을 리턴한다.
<br/><br/><br/>

## 세션을 사용한 로그인 상태 유지

1. 로그인에 성공하면 session 기본 객체의 특정 속성에 데이터를 기록한다.
2. 이후로 session 기본 객체의 특정 속성이 존재하면 로그인한 것으로 간주한다.
3. 로그아웃할 경우 session.invalidate( ) 메서드를 호출하여 세션을 종료한다.
<br/><br/>
## 인증된 사용자 정보  session 기본 객체에 저장하기

세션을 사용해서 로그인 상태를 유지하려면 session 기본 객체의 속성에 로그인 성공 정보를 저장하면 된다. session 기본 객체의 특정 속성을 로그인 상태 정보로 사용하면 된다.

```java
session.setAttribute("MEMBERID", id);
```
session 기본 객체에 로그인 상태를 위한 속성의 존재 여부에 따라 로그인 상태를 판단할 수가 있다.

예를 들어, 로그인에 성공하면 MEMBERID 속성에 로그인 상태 정보를 보관하므로, session 기본 객체의 “MEMBERID” 속성을 사용해서 로그인 여부를 판단 한다.
<br/><br/>
```java
String memberId = (String) session.getAttribute("MEMBERID");
boolean login = memberId == null ? false : true;
```

세션에 여러 속성을 사용해서 연관 정보들을 저장할 때 발생할 수 있는 문제점을 줄일 수 있는 방법은 클래스를 사용하는 것이다. 예를 들어 회원과 관련된 정보를 다음과 같은 클래스에 묶어서 저장한다고 해보자.<br/><br/>

```java
public class MemberInfo {

	private String id;
	private String name;
	private String email;
	private boolean male;
	private int age;

	// get 메서드
}
```

연관된 정보를 클래스로 묶어서 저장하면 각 정보를 개별 속성으로 저장하지 않고 다음과 같이 한개의 속성을 이용해서 저장할 수 있게 된다.<br/><br/>

```java
<%
		MemberInfo memberInfo = new MemberInfo(id, name);
		session.setAttribute("memberInfo", memberInfo);
%>
```

연관된 정보를 한 객체에 담아 저장하기 때문에, 세션에 저장한 객체를 사용할 때에도 다음과 같이 객체를 가져온 뒤 객체로 부터 필요한 값을 읽어올 수 있다.