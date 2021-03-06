## 웹 서버, 웹 애플리케이션 서버

웹이라는 것은 HTTP 기반으로 통신을 한다.

HTTP 메세지에 모든 것을 전송한다.

우리가 흔히 부르는 서버라는 것은 WAS라는 것이다.

HTTP 요청을 보내고 받는 서버가 WAS 이다.

<br/>

### 웹 서버(Web Server)

- HTTP 기반으로 동작하는 서버
- 정적(파일) HTML, CSS, JS, 이미지, 영상 == 정적인 파일만 가능하다.
- 대표적인 아파치 서버

<br/>

### 웹 애플리케이션 서버(WAS ‘와스’라고 부른다 == Web Application Server)

(애플리케이션 로직을 수행할 수 있는 놈)

- HTTP 기반으로 동작
- 웹 서버 기능 포함 + (정적 리소스 제공 기능)
- 프로그램 코드를 실행해서 애플리케이션 로직 수행 == 동적
    - 사용자에 따라서 그 사람의 이름도 보여줄 수 있고, 다른 화면들도 보여줄 수 있는 것이다.
    - 동적 HTML, HTTP API(JSON)
    - 서블릿, JSP, 스프링 MVC
- 예) 톰캣

<br/>

### 웹 서버, 웹 애플리케이션 서버(WAS) 차이

- 웹 서버는 정적 리소스(파일), WAS는 애플리케이션 로직
- 사실은 둘의 용어도 경계도 모호함
    - 웹 서버도 프로그램을 실행하는 기능을 포함하기도 함
    - 웹 애플리케이션 서버도 웹 서버의 기능을 제공함
- 자바는 서블릿 컨테이너 기능을 제공하면 WAS.
    - 서블릿 없이 자바코드를 실행하는 서버 프레임워크도 있음
- WAS는 애플리케이션 코드를 실행하는데 더 특화

<br/>

### 웹 시스템 구성 - WEB, WAS, DB

- 정적 리소스는 웹 서버가 처리
- 웹 서버는 애플리케이션 로직같은 동적인 처리가 필요하면 WAS에 요청을 위임
- WAS는 중요한 애플리케이션 로직 처리 전담
- 장점은 WAS는 엄청 중요한 애플리케이션 로직에 전담 할 수 있다는 장점이 있다.
- 그리고 웹 서버는 단순한 정적인 리소스들만 처리하면 됨으로써, 업무를 분담 할 수 있다.

![이미지](/programming/img/서1.PNG)

<br/>

- 정적 리소스만 제공하는 웹 서버는 잘 죽지 않음
- 애플리케이션 로직이 동작하는 WAS 서버는 잘 죽음 (이유 개발자들이 만들기 때문)
- WAS, DB 장애 시 WEB 서버가 오류 화면 제공 가능

![이미지](/programming/img/서2.PNG)

<br/>

>**Reference** <br/>스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1
