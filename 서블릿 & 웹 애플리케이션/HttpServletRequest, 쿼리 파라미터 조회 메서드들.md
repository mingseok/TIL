## HttpServletRequest, 쿼리 파라미터 조회 메서드들

<br/>

## HttpServletRequest 역할.

서블릿은 개발자가 HTTP 요청 메시지를 편리하게 사용할 수 있도록 개발자 대신에 HTTP 요청 메시지를 파싱한다. 


![이미지](/programming/img/서18.PNG)


- 첫번째줄, 두번째줄 설명 : START LINE 이라고 한다.

    - HTTP 메소드

    - URL
    - 쿼리 스트링
    - 스키마, 프로토콜 이 있다.

- 세번째 줄 설명 : 헤더

    - 헤더 조회

- 네번째 줄 설명 : 바디
    - form 파라미터 형식 조회

    - message body 데이터 직접 조회


<br/><br/>

## 핵심 기능

### 해당 HTTP 요청이 시작부터 끝날 때 까지 유지되는 임시 저장소 기능을 한다.

```
- 저장: request.setAttribute(name, value)

- 조회: request.getAttribute(name)
```

<br/>

### 세션 관리 기능

```
- request.getSession(create: true)
``` 

<br/>

`HttpServletRequest`, `HttpServletResponse`를 사용할 때 가장 중요한 점은 

이 객체들이 HTTP 요청 메시지, HTTP 응답 메시지를 편리하게 사용하도록 도와주는 객체라는 점이다. 

<br/><br/>

## 쿼리 파라미터란?

```
http://localhost:8080/hello?username=kim
```

- `?` 뒤부분 부터가 `쿼리 파라미터` 라고 부른다.

- 형태는 `키` , `벨류` 형식으로 보내는 것이다.

<br/><br/>

## 하나씩 예제로 사용해보자.

```java
요청 URL : localhost:8080/request-param?username=hello&age=20

// 단일 파라미터 조회
String username = request.getParameter("username"); 

// 파라미터 이름들 모두 조회
Enumeration<String> parameterNames = request.getParameterNames();

// 복수 파라미터 조회
String[] usernames = request.getParameterValues("username");
```

<br/><br/>

## 단일 파라미터 조회 : `getParameter()`

- 키인 “`username`” 과 “`age`” 를 넣어서

- value인 ‘`hello`’ 와 ‘`20`’ 을 출력 하였다.

```java
요청 URL : localhost:8080/request-param?username=hello&age=20

@Override
protected void service(HttpServletRequest request, HttpServletResponse response) ... {

        String username = request.getParameter("username");
        String age = request.getParameter("age");

        System.out.println(username);
        System.out.println(age);

   // ...생략
```

<br/>

출력 :

![이미지](/programming/img/입문41.PNG)

<br/><br/>

## 전체 파라미터 조회 : `getParameterNames()`

- `paramName`은 'username', 'age'가 출력 된다.

- `request.getParameter(paramName)`은 'hello', '20'가 출력된다.

```java
요청 URL : localhost:8080/request-param?username=hello&age=20

@Override
protected void service(HttpServletRequest request, HttpServletResponse response) ... {

request.getParameterNames().asIterator()
        .forEachRemaining(paramName ->
                 System.out.println(paramName + "=" + request.getParameter(paramName)));

   // ...생략
```

<br/>

출력 :

![이미지](/programming/img/입문42.PNG)

<br/><br/>

## 이름이 같은 복수 파라미터 조회 : `getParameterValues()`

- URL이 추가된 것을 알 수 있다.

```java
요청 URL : localhost:8080/request-param?username=hello&age=20&username=hello2

@Override
protected void service(HttpServletRequest request, HttpServletResponse response) ... {

        String[] usernames = request.getParameterValues("username");
        for (String name : usernames) {
            System.out.println("usernames = " + name);
        }

   // ...생략
```

<br/>

출력 : 

![이미지](/programming/img/입문43.PNG)

<br/>

### 만약, 이렇게 URL을 요청한다면?

```java
http://localhost:8080/request-param?username=hello&age=20&kim=hello2&age=20
```

<br/>

### 하나만 출력 되는 것이다.

![이미지](/programming/img/입문44.PNG)





<br/><br/>

<br/><br/>

>**Reference** <br/>[스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1)
