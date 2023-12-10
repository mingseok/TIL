## URI(Uniform Resource Identifier) : 리소스를 식별하는 통일된 방법.

<br/>

### 리소스란?

URL로 식별할 수 있는 모든 것을 자원이라고 한다. 

웹 브라우저에 있는 HTML 파일 같은 것만 자원을 뜻하는게 아니고, 

실시간 교통정보 라던가, 우리가 구분할 수 있는 모든 것을 리소스라고 하는 것이다.

<br/>

### URI란

![이미지](/programming/img/HTTP13.PNG)

### **URI라는 큰 개념이 있다. 그 안에 URL 과 URN 이 있는 것이다.**

URL : 리소스가 이 위치에 있다는 뜻이다

URN : 리소스의 이름이다. 예를 들어) ‘김민석’ 이 있다. 그러면 URN은 ‘김민석’ 인 것이다.

<br/>

![이미지](/programming/img/HTTP14.PNG)

이렇게 차이가 있는 것이다.


<br/>

### 그래서 거진 URL만 사용하는 것이다. URN은 “이런게 있구나..” 만 생각하자

![이미지](/programming/img/HTTP15.PNG)

<br/>

## 정리하면

URL - Locator : 리소스가 있는 위치를 지정

URN - Name : 리소스에 이름을 부여

- URN 이름만으로 실제 리소스를 찾을 수 있는 방법이 보편화 되지 않음
- 앞으로 URI를 URL과 같은 의미로 이야기 한다.

<br/>

### 만약 이렇게 검색 하면 이 URL을 쳤기 때문에 저 URL에 대한 리소스 결과를 돌려 준것이 된다.

URL 전체 문법

- scheme://[userinfo@]host[:port][/path][?query][#fragment]
- https://www.google.com:443/search?q=hello&hl=ko
    - 프로토콜(https)
    - 호스트명(www.google.com)
        - 도메인명 또는 IP 주소를 직접 사용가능
    - 포트 번호(443)
        -  이것이 몇동 몇호인 ‘포트’인 것이다.
        - **접속 포트, 일반적으로 생략, 생략시 http는 80, http는 445**
    - 패스(/search)
        - 리소스 경로(path), 계층적 구조
        - 예) /home/file1.jpg
        - /members
    - 쿼리 파라미터(q=hello&hl=ko)
        - key=value 형태
        - ?로 시작, &로 추가 기능 ?keyA=valueA&keyB=valueB
        - ‘쿼리 파라미터’, ‘쿼리 스트링’ 으로 불린다.

<br/>

주로 프로토콜 사용

- 프로토콜: 어떤 방식으로 자원에 접근할 것인가 하는 약속 규칙
    - 예) http, https, ftp 등등
- http는 80 포트, https는 443 포트를 주로 사용, 포트는 생략 가능
- https는 http에 보안 추가 (HTTP Secure)


<br/>


>**Reference** <br/>모든 개발자를 위한 HTTP 웹 기본 지식 : https://www.inflearn.com/course/http-%EC%9B%B9-%EB%84%A4%ED%8A%B8%EC%9B%8C%ED%81%AC