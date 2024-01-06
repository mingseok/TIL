## HTTP 메시지

요청 메시지와 응답 메시지는 다르게 생겼다.



![이미지](/programming/img/입문589.PNG)

<br/><br/>

![이미지](/programming/img/입문590.PNG)

<br/><br/>

## 요청 메시지 (요청은 request-lin)

![이미지](/programming/img/입문591.PNG)

- `HTTP 메서드` : (GET: 조회)

    - 종류 GET, POST, PUT, DELETE 등

    - GET: 리소스 조회

    - POST: 요청 내역 처리

- `요청 대상` : (/search?q=hello&hl=ko)

    - 요청 대상

    - 절대경로 = “/”로 시작하는 경로

- `HTTP/1.1` : http 버전

<br/><br/>

## 응답 메시지 (응답은 status-line)

SP : 스페이스 한칸 띄우기 라고 생각하면 된다.

![이미지](/programming/img/입문592.PNG)

- http 상태 코드: 요청 성공, 실패를 나타냄

    - 200: 성공

    - 400: 클라이언트 요청 오류

    - 500: 서버 내부 오류

- 이유 문구: 사람이 이해할 수 있는 짧은 상태 코드 설명 글

<br/><br/>

## 응답 메시지 - HTTP 헤더

```
Content-Type: text/html;charset=UTF-8
Content-Length: 3423
```

- HTTP 전송에 필요한 모든 부가 정보

- 예) 메시지 바디의 내용, 메시지 바디의 크기, 압축, 인증,
    
    요청 클라이언트(브라우저) 정보, 서버 애플리케이션 정보, 캐시 관리 정보 등
    
<br/><br/>

## 응답 메시지 - HTTP 메시지 바디

```
<html>
   <body>...</body>
</html>
```

- 실제 전송할 데이터

- HTML 문서, 이미지, 영상,  JSON 등등 byte로 표현할 수 있는 모든 데이터 전송 가능.