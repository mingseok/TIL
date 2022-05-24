## HTTP 요청 데이터 - POST HTML Form

이번에는 HTML의 Form을 사용해서 클라이언트에서 서버로 데이터를 전송해보자.

주로 회원 가입, 상품 주문 등에서 사용하는 방식이다.


<br/>

### 특징

`content-type: application/x-www-form-urlencoded`

메시지 바디에 쿼리 파리미터 형식으로 데이터를 전달한다. `username=hello&age=20`

<br/>

`hello-form.html` 생성하기

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<form action="/request-param" method="post">
    username: <input type="text" name="username"/>
    age: <input type="text" name="age"/>
    <button type="submit">전송</button>
</form>
</body>
</html>
```

<br/>

실행 시켜서 개발자 모드 ‘F12’ 를 누른 다음에, 값을 작성하고 ‘전송’ 을 보내보자.

![이미지](/programming/img/서21.PNG)

<br/>

그러면 친절하게 다 보여준다.

![이미지](/programming/img/서22.PNG)

<br/>

어떤 방식인지도 잘 알 수 있다.

![이미지](/programming/img/서23.PNG)

<br/>

## 정리해보면

`application/x-www-form-urlencoded` 형식은 앞서 `GET`에서 살펴본 쿼리 파라미터 형식과 같다.

따라서 쿼리 파라미터 조회 메서드를 그대로 사용하면 된다.

<br/>클라이언트(웹 브라우저) 입장에서는 두 방식에 차이가 있지만, 

서버 입장에서는 둘의 형식이 동일하므로,

<br/>

`request.getParameter()` 로 편리하게 구분없이 조회할 수 있다

정리하면 `request.getParameter()` 는 GET URL 쿼리 파라미터 형식도 지원하고, 
POST HTML Form 형식도 둘 다 지원한다. 

(자바 코드를 안만들어도 되니깐 편한 것이다)

### form<> 태그에서 온 것을 request.getParameter() 를 이용해서 꺼내는 것이다.

<br/>

>**Reference** <br/>스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1
