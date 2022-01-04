# request 기본 객체

request 기본 객체는 웹 브라우저, 즉 클라이언트가 전송한 정보와 서버 정보를 구할 수 있는 메서드를 제공하고 있다.

| 메서드 | 리턴 타입 | 설명 |
| --- | --- | --- |
| getParameter(String name) | String | 이름이 name인 파라미터의 값을 구한다. 존재 하지 
않을 경우 null 리턴한다. |
| getParameterValues(String name) | String[ ] | 이름이 name인 모든 파라미터의 값을 배열로 구한다. 존재하지 않을 경우 null을 리턴 한다. |
| getParameterNames( ) | java.util.Enumeration | 웹 브라우저가 전송한 파라미터의 이름 목록을 구한다. |
| getParameterMap( ) | java.util.Map | 웹 브라우저가 전송한 파라미터의 맵을 구한다. 맵은 
<파라미터 이름, 값> 쌍으로 구성된다. |
<br/>
<br/>
### getParameter(String name) 사용 예제

```java
-----<html>-----
<input type="text" name="name" size="10"> 
<input type="text" name="address" size="30"> 

----- 자바 -----
name 파라미터 = <%= request.getParameter("name"); %>
address 파라미터 = <%= request.getParameter("address"); %>

결과 값.
name 파라미터 = 홍길동
name 파라미터 = 아차곡
```

request 기본 객체의 메서드를 사용해서 요청 파라미터를 읽어올 수 있다.
<br/>
<br/>
### getParameterValues(String name), getParameterName( ) 사용 예제

```java
-----<html>-----
<input type="checkbox" name="pet" value="dog">강아지 
<input type="checkbox" name="pet" value="cat">고양이
<input type="checkbox" name="pet" value="pig">돼지

----- 자바 -----
<%
String[ ] value = request.getParameterValues("pet");
if (value != null) {
		for(int i = 0; i < value.length; i++) {
%>
				<%= value[i] %>
<%
		}
}
%>		

동물 부분에 강아지와 고양이를 선택했다고 해보자.
이 경우 두개의 pet 파라미터가 전송된다.

결과 값.
dog cat

-------------------------------------------------------

<%
Enumeration paramEnum = request.getParameterName();
while(paramEnum.hasElements()) {
			String name = (String) paramEnum.nextElement();
%>
			<%= name %>
<%
}
%>

결과 값.
name address pet
```

 request.getParameterName( ); 추가 설명

결과 값이 name address pet 나오지만 체크박스를 아무것도 선택하지 않으면 웹 브라우저는 해당 이름의 파라미터를 전송하지 않는다. 즉, 폼에서 ‘강아지’,  ‘고양지’,  ‘돼지’ 중에서 아무것도 선택하지 않고 [전송] 버튼을 클릭하면 “pet” 파라미터가 전송되지 않는 것이다.
<br/>
<br/>
# response 기본 객체

response 기본 객체는 request 기본 객체와 반대의 기능을 수행한다.

request 기본 객체가 웹 브라우저가 전송한 요청 정보를 담고 있다면 response 기본 객체는 웹 브라우저에 보내는 응답 정보를 담는다.
<br/>
<br/>
### 리다이렉트를 이용해서 페이지 이동하기.

리다이렉트는 웹 서버가 웹 브라우저에게 다른 페이지로 이동하라고 응답하는 기능이다.

예를 들어, 사용자가 로그인에 성공한 후 메인 페이지로 자동으로 이동하는 사이트가 많은데 이렇게 특정 페이지를 실행한 후 지정한 페이지로 이동하길 원할 때 리다이렉트 기능을 사용 한다.

![이미지](/programming/img/사진3.)

리다이렉트를 지시한 jsp 페이지가 있을 경우 웹 브라우저는 실질적으로 요청을 두 번 하게 된다.
