# cookie

### **쿠키는 웹 브라우저가 보관하는 데이터이다.**

(여기서 웹 브라우저는 내 pc에 있다.)

웹 브라우저는 웹 서버에 요청을 보낼때 쿠키를 함께 전송하며, 웹 서버는 웹 브라우저가 전송한 쿠키를 사용해서 필요한 데이터를 읽을 수 있다.

일단 웹 브라우저에 쿠키가 저장되면, 웹 브라우저는 쿠키가 삭제되기 전까지 웹 서버에 쿠키를 전송한다. 따라서 웹 어플리케이션을 사용하는 동안 지속적으로 유지해야 하는 정보는 쿠키를 사용해서 저장하면 된다.
<br/><br/>
### **쿠키의 핵심 요소는 이름과 값이다.**

하나의 웹 브라우저는 여러 개의 쿠키를 가질 수 있는데, 각 쿠키를 구분할 때 이름을 사용한다. 각 쿠키는 값을 가지며 서버는 이 값을 사용해서 원하는 작업을 수행한다. 쿠키 이름은 콤마, 세미콜론, 공백, 등호기호(=)를 제외한 출력 가능한 아스키 문자로 구성된다.
<br/><br/>
### **유효시간을 사용하면 웹 브라우저가 쿠키를 얼마 동안 보관할지를 지정할 수 있다.**

예를 들어, 쿠키 유효 시간을 1시간으로 지정하면 1시간 뒤에 웹 브라우저는 해당 쿠키를 삭제하며, 별도 유효 시간을 지정하지 않으면 웹 브라우저를 종료할 때 쿠키를 함께 삭제 한다.

또한, 지정한 도메인이나 경로로만 쿠키를 전송하도록 제한할 수도 있다.

즉, 서버에서 웹 브라우저로 보낼때 쿠키도 같이 보내지면서 ID 로그인 한 상태가 유지 되고 있는 것이다.<br/> 그리고 웹 브라우저 창을 종료하면 로그인 한 ID는 없어져 있다.
<br/>

```java
// 첫번째 인자는 쿠키의 이름을, 두번째 인자는 쿠키의 값을 지정한다.
Cookie cookie = new Cookie("cookieName", "cookieValue");
response.addCookie(cookie);

출력 값.
name 쿠키의 값 = "%EC%B5%9S%5F%1G%**T2**%TF%F1%3Y%H1"
```
<br/><br/>
쿠키 값을 변경하려면 같은 이름의 쿠키를 새로 생성해서 응답 데이터로 보내면 된다. 예를 들어,

이름이 “name”인 쿠키의 값을 변경하기 위해서는 다음과 같이 새로운 Cookie 객체를 생성해서 응답 데이터에 추가하면 된다.

쿠키의 값을 변경한다는 것은 기존에 존재하는 쿠키의 값을 변경하는 것이다.

위 코드를 실행하면 쿠키가 존재하지 않으면 쿠키가 생성되므로, 쿠키 값을 변경하려면 쿠키가 존재하는지 먼저 확인 해야 한다.
```java
Cookie cookie = new Cookie("name", URLEncoder.encode("새로운값", "euc-kr"));
response.addCookie(cookie);
```

<br/><br/>
### **쿠키의 도메인**

기본적으로 쿠키는 그 쿠키를 생성한 서버에만 전송한다. 예를 들어 “http://java.com” 에 연결해서 생성된 쿠키는 다른 사이트로 연결할 때에는 전송되지 않으며, “http://java.com” 에 연결할 때에만 전송 된다. 하지만 같은 도메인을 사용하는 모든 서버에 쿠키를 보내야 할때가 있다. 

그땐 setDomain( ) 메서드를 사용하기.<br/><br/>

```java
Cookie cookiel = new Cookie("id", "madvirus");
cookiel.setDomain(".somehost.com");
response.addCookie(cookie1);

Cookie cookiel = new Cookie("only", "onlycookie");
response.addCookie(cookie2);

Cookie cookiel = new Cookie("invalid", "invalidcookie");
cookiel.setDomain("java.tistory.com");
response.addCookie(cookie3);
```

### **쿠키를 사용한 로그인 상태 유지**

로그인 상태를 확인할 때 가장 많이 사용하는 방법이 바로 쿠키를 이용하는 것이다.

1. 로그인에 성공하면 특정 이름을 갖는 쿠키를 생성한다.
2. 해당 쿠키가 존재하면 로그인한 상태라고 판단한다.
3. 로그아웃하면 해당 쿠키를 삭제한다.
<br/><br/>
### **로그인 여부 판단**

로그인에 성공 했음을 나타내는 쿠키를 생성한 이후, 웹 브라우저는 요청을 보낼 때마다 쿠키를 전송한다. 그러므로 로그인에 성공할 때 생성되는 쿠키가 존재하는지 확인하면 현재 로그인 했는지 여부를 판단할 수 있다.
<br/><br/>

### **로그아웃 처리**

로그인에 성공하면 쿠키를 생성하므로, 로그아웃을 처리하려면 쿠키를 삭제하면 된다.


<br/><br/>

>**Reference** 
> <br/>
최범균,**『**최범균의 JSP 2.3 웹프로그래밍 기초부터 중급까지**』**, 가메출판사.