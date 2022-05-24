## HttpServletRequest

### HttpServletRequest 역할.

HTTP 요청 메시지를 개발자가 직접 파싱해서 사용해도 되지만, 

매우 불편할 것이다. 

<br/>서블릿은 개발자가 HTTP 요청 메시지를 편리하게 사용할 수 있도록 

개발자 대신에 HTTP 요청 메시지를 파싱한다. 

<br/>그리고 그 결과를 HttpServletRequest 객체에 담아서 제공한다

- 첫번째, 두번째 줄 설명 : START LINE 이라고 한다.
    - HTTP 메소드
    - URL
    - 쿼리 스트링
    - 스키마, 프로토콜 이 있다.
- 세번째 줄 설명 : 헤더
    - 헤더 조회
- 네번째 줄 설명 : 바디
    - form 파라미터 형식 조회
    - message body 데이터 직접 조회

![이미지](/programming/img/서18.PNG)

### 핵심

- 임시 저장소 기능
    - 해당 HTTP 요청이 시작부터 끝날 때 까지 유지되는 임시 저장소 기능

    - 저장: `request.setAttribute(name, value)`
    - 조회: `request.getAttribute(name)`
- 세션 관리 기능
    - request.getSession(create: true)
    

<br/>

`HttpServletRequest`, `HttpServletResponse`를 사용할 때 가장 중요한 점은 

이 객체들이 HTTP 요청 메시지, HTTP 응답 메시지를 편리하게 

사용하도록 도와주는 객체라는 점이다. 

<br/>

>**Reference** <br/>스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1
