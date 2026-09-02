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
        .forEachRemaining(paramName -> System.out.println(paramName + "=" + request.getParameter(paramName)));

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

<br/>

## 궁금증!

```java
getParameter 와 getAttribute 는 무엇이 다른가
```

이름이 비슷한데 완전히 다른 것이다.

```java
getParameter  - 클라이언트가 보낸 값. 항상 String
getAttribute  - 서버가 담아둔 값. 아무 객체나
```

<br/>

```java
request.getParameter("name")              -> "민석" (String)
request.setAttribute("member", member);
request.getAttribute("member")            -> Member 객체
```

<br/>

`getParameter` 는 읽기만 된다. `setParameter` 라는 메서드가 없다.

클라이언트가 보낸 것이라 서버가 바꿀 게 아니기 때문이다.

<br/>

앞의 @Get매핑, Model 글에서 본 `Model` 이 결국 `setAttribute` 로 간다.

<br/>

## getParameter 가 두 곳을 뒤진다

```java
GET  /members?name=민석            -> 쿼리 스트링에서
POST + form-urlencoded             -> 본문에서
```

<br/>

같은 메서드로 둘 다 읽힌다.

<br/>

앞의 @PostMapping(), form태그 글에서 본 대로 두 형식이 똑같이 생겼기 때문이다.

```java
name=민석&age=30
```

<br/>

붙는 자리만 다르고 형식이 같으니 같은 파서로 처리하는 것이다.

<br/>

`getQueryString()` 으로 쿼리 부분만 원문 그대로 볼 수도 있다.

```java
request.getQueryString()      -> "name=%EB%AF%BC%EC%84%9D&age=30"
```

<br/>

인코딩이 안 풀린 상태다. `getParameter` 는 풀어서 준다.

<br/>

## 같은 키가 여러 번 오면

```java
/members?tag=a&tag=b
```

```java
request.getParameter("tag")             -> "a"        (첫 번째만)
request.getParameterValues("tag")       -> ["a", "b"]
```

<br/>

`getParameter` 를 쓰면 조용히 하나만 가져간다.

체크박스처럼 여러 개가 올 수 있는 것은 `getParameterValues` 를 써야 하는 것이다.

<br/>

앞의 HTTP 요청 파라미터 - @RequestParam 글에서 본 대로

스프링에서는 `List` 로 받으면 알아서 전부 들어온다.

<br/>

## 값이 없는 것과 빈 문자열이 다르다

```java
/members                -> getParameter("name") = null
/members?name=          -> getParameter("name") = ""
```

<br/>

`null` 체크만 하면 빈 문자열이 통과한다.

<br/>

그래서 이런 코드가 필요해진다.

```java
if (name != null && !name.isEmpty()) { ... }
```

<br/>

스프링에 이걸 해주는 유틸이 있다.

```java
StringUtils.hasText(name)      // null, "", " " 를 전부 걸러낸다
```

<br/>

앞의 동적 쿼리 글에서 본 그 메서드다.

앞의 프로젝트 컨벤션에도 있듯이, 스프링이 주는 유틸이 있으면 직접 안 짜는 게 낫다.

<br/>

## 헤더를 읽는 메서드

```java
request.getHeader("User-Agent")           -> 하나
request.getHeaders("Accept")              -> Enumeration. 같은 헤더가 여럿일 때
request.getHeaderNames()                  -> 전체 이름
```

<br/>

헤더 이름은 대소문자를 구분하지 않는다.

```java
request.getHeader("content-type")
request.getHeader("Content-Type")         // 둘 다 같은 값
```

<br/>

앞의 HTTP 헤더 글에서 본 대로 스펙에 그렇게 정해져 있다.

<br/>

## 경로를 읽는 메서드가 여러 개다

```java
GET /myapp/members/1?name=민석
```

```java
getRequestURI()      -> /myapp/members/1        (쿼리 제외)
getRequestURL()      -> http://localhost:8080/myapp/members/1
getContextPath()     -> /myapp
getServletPath()     -> /members/1
getQueryString()     -> name=%EB%AF%BC%EC%84%9D
```

<br/>

앞의 절대 경로, 상대 경로 글에서 본 컨텍스트 경로가 여기서 나뉜다.

```java
getRequestURI()  - 컨텍스트 포함
getServletPath() - 컨텍스트 제외
```

<br/>

로그를 남길 때 어느 것을 쓰느냐에 따라 배포 환경이 바뀌면 값이 달라진다.

<br/>

## 이 메서드들을 직접 쓸 일이 줄었다

앞의 @RequestMapping(), HttpServletRequest 글에서 본 그 이유다.

```java
public String find(HttpServletRequest request) {
    String keyword = request.getParameter("keyword");
    int page = Integer.parseInt(request.getParameter("page"));
}
```

```java
public String find(@RequestParam String keyword,
                   @RequestParam(defaultValue = "0") int page) { ... }
```

<br/>

아래쪽이 훨씬 짧고, 타입 변환과 기본값 처리까지 된다.

<br/>

그래도 알아둘 이유는 있다.

필터에서 요청을 다뤄야 할 때는 이 메서드들밖에 없기 때문이다.

앞의 필터, 스프링 인터셉터 글에서 본 대로 필터는 스프링 밖에 있다.
