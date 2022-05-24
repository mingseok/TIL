## HTTP 요청 데이터 & GET 쿼리 파라미터

HTTP 요청 메시지를 통해 클라이언트에서 서버로 데이터를 전달하는 방법을 알아보자.

<br/>


## 주로 다음 3가지 방법을 사용한다.

- GET - 쿼리 파라미터
    - /url`?username=hello&age=20`
    - 메시지 바디 없이, URL의 쿼리 파라미터에 데이터를 포함해서 전달.

    - 예) 검색, 필터, 페이징 등에서 많이 사용하는 방식.
    
- POST - HTML Form
    - content-type: application/x-www-form-urlencoded

    - 메시지 바디에 쿼리 파리미터 형식으로 전달 username=hello&age=20
    - 예) 회원 가입, 상품 주문, HTML Form 사용
    
![이미지](/programming/img/서19.PNG)
    

- HTTP message body에 데이터를 직접 담아서 요청

    - HTTP API에서 주로 사용, JSON, XML, TEXT

    - 데이터 형식은 주로 JSON 사용
    - POST, PUT, PATCH


<br/><br/>

## GET 쿼리 파라미터

데이터를 클라이언트에서 서버로 전송해보자.

form<> 태그에서 온 것을 request.getParameter() 를 이용해서 꺼내는 것이다.

<br/>즉, 사용자가 id 와 pwd 입력한 걸 읽어 와야 하기 때문이다.

![이미지](/programming/img/서20.PNG)

전달 데이터

- username=hello
- age=20

<br/>메시지 바디 없이, URL의 쿼리 파라미터를 사용해서 데이터를 전달하자.

예) 검색, 필터, 페이징등에서 많이 사용하는 방식

<br/>쿼리 파라미터는 URL에 다음과 같이 `?` 를 시작으로 보낼 수 있다. 

추가 파라미터는 `&` 로 구분하면 된다.

localhost:8080/request-param?username=hello&age=20

<br/>서버에서는 HttpServletRequest 가 제공하는 다음 메서드를 통해 쿼리 파라미터를 편리하게 조회할 수 있다.

### 쿼리 파라미터 조회 메서드

```java
// 단일 파라미터 조회
String username = request.getParameter("username"); 

// 파라미터 이름들 모두 조회
Enumeration<String> parameterNames = request.getParameterNames();

// 파라미터를 Map으로 조회
Map<String, String[]> parameterMap = request.getParameterMap();

// 복수 파라미터 조회
String[] usernames = request.getParameterValues("username");
```

<br/>

### 복수 파라미터에서 단일 파라미터 조회

`username=hello&username=kim` 과 같이 파라미터 이름은 하나인데, 값이 중복이면 어떻게 될까?

`request.getParameter()` 는 하나의 파라미터 이름에 대해서 단 하나의 값만 있을 때 사용해야 한다.

<br/>지금처럼 중복일 때는 `request.getParameterValues()` 를 사용해야 한다.

참고로 이렇게 중복일 때 `request.getParameter()` 를 사용하면 

<br/>

`request.getParameterValues()` 의 첫 번째 값을 반환한다.

### 핵심은 `request.getParameter()` 를 가장 많이 사용한다는 것이다.



<br/>

>**Reference** <br/>스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1