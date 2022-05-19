## HTTP 메시지

요청 메시지와 응답 메시지는 다르게 생겼다.

![이미지](/programming/img/HTTP23.PNG)

<br/>

![이미지](/programming/img/HTTP24.PNG)

<br/><br/>

![이미지](/programming/img/HTTP25.PNG)

## 시작 라인 - 요청 메시지

```
GET /search?q=hello&hl=ko HTTP/1.1
Host: www.google.com
```

### GET 부분은??

- HTTP 메서드

- 종류 GET, POST, PUT, DELETE…
    - GET: 리소스 조회
    - POST: 요청 내역 처리

<br/>

### /search?q=hello&hl=ko 부분은??

- 요청 대상

- 절대경로 = “/”로 시작하는 경로

<br/>

### HTTP/1.1 부분은??

- HTTP 버전

- HTTP Version

<br/><br/>

## 요청은 request-lin, 응답은 status-line

SP : 스페이스 한칸 띄우기 라고 생각하면 된다.


![이미지](/programming/img/HTTP26.PNG)

<br/>

‘:’ 사용 했다면 : 이건 붙이기 Host: 이런식으로.

![이미지](/programming/img/HTTP27.PNG)


<br/>

## 응답 메시지

### HTTP 헤더 - 용도 (위에 그림 참조)

```
Content-Type: text/html;charset=UTF-8
Content-Length: 3423
```

- HTTP 전송에 필요한 모든 부가 정보

- 예) 메시지 바디의 내용, 메시지 바디의 크기, 압축, 인증, 요청 클라이언트(브라우저) 정보,
서버 애플리케이션 정보, 캐시 관리 정보…등등

<br/>

### HTTP 메시지 바디 - 용도 (위에 그림 참조)

```
<html>
   <body>...</body>
</html>
```

- 실제 전송할 데이터
- HTML 문서, 이미지, 영상,  JSON 등등 byte로 표현할 수 있는 모든 데이터 전송 가능.


<br/>


>**Reference** <br/>모든 개발자를 위한 HTTP 웹 기본 지식 : https://www.inflearn.com/course/http-%EC%9B%B9-%EB%84%A4%ED%8A%B8%EC%9B%8C%ED%81%AC