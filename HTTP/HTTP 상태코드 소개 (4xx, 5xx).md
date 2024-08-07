## HTTP 상태코드 소개 (4xx, 5xx)

4백대는 클라이언트가 잘못한 것.

```java
"4백대 오류는 복구 불가능하다"
```



즉, 클라이언트가 보낸 자체에, 문제가 있기 때문에 그 요청을 수정해서 보내야 되는 것이다.

<br/><br/>

## 4xx (클라이언트 오류)

- 클라이언트의 요청에 잘못된 문법등으로 서버가 요청을 수행할 수 없다.

- 오류의 원인이 클라이언트에 있다.

- 중요) 클라이언트가 이미 잘못된 요청, 데이터를 보내고 있기 때문에, 똑같은 재시도가 실패함

<br/><br/>

## 400 Bad Request

클라이언트가 잘못된 요청을 해서 서버가 요청을 처리할 수 없다.

- 요청 구문, 메시지 등 오류

- 클라이언트는 요청 내용을 다시 검토하고, 보내야 한다.

    - e.g.) 요청 파라미터가 잘못되거나, API 스펙이 맞지 않을때.

<br/><br/>

## 401 Unauthorized

클라이언트가 해당 리소스에 대한 인증이 필요하다.

- 인증 되지 않음

    - 인증: 본인이 누구인지 확인, (로그인)

    - 인가: 권한 부여

- 401 오류 발생시 응답에 www-authenticate 헤더와 함께 인증 방법을 설명

<br/><br/>

## 403 Forbidden

서버가 요청을 이해했지만 승인을 거부한다

- 주로 인증 자격 증명은 있지만, 접근 권한이 불충분한 경우이다.

    - e.g.) 어드민 등급이 아닌 사용자가 로그인은 했지만, 어드민 등급의 리소스에 접근한 경우

<br/><br/>

## 404 Not Found

요청 리소스를 찾을 수 없다.

- 요청 리소스가 서버에 없다

- 또는 클라이언트가 권한이 부족한 리소스에 접근할 시, 해당 리소스를 숨기고 싶을때


<br/><br/>

## 5xx (Sercer Error)

- 서버 문제로 오류 발생

- 서버에 문제가 있기 때문에 재시도 하면 성공할 수도 있다.

    - 복구가 되거나 등등.

<br/><br/>

## 500 Internal Server Error

서버 문제로 오류 발생, 애매하면 500 오류

- 서버 내부 문제로 오류 발생

- 애매하면 500 오류

<br/><br/>

## 503 Service Unavailable

서비스 이용 불가

- 서버가 일시적인 과부하 또는 예정된 작업으로 잠시 요청을 처리할 수 없다