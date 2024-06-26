## HTTP 상태코드 소개 (2xx, 3xx)

100대, 200대, 300대, 400대, 500대 에러가 있는 것이다.

- 이중에서 100대 에러는 잘 사용하지 않는다.

<br/><br/>

## 상태 코드

클라이언트가 보낸 요청의 처리 상태를 응답에서 알려주는 기능이다

- `1xx` : 요청이 수신되어 처리중

- `2xx` : 요청 정상 처리

- `3xx` : 요청을 완료하려면 추가 행동이 필요

- `4xx` : 클라이언트 오류, 잘못된 문법등으로 서버가 요청을 수행할 수 없다

- `5xx` : 서버 오류, 서버가 정상 요청을 처리하지 못한다.

```java
"만약 299, 455, 588 이런식으로 온다면?"
-> 다 이해 하지 않아도 되고, 그냥 "2백대구나", "4백대구나" 라고 생각하면 된다.
```

<br/><br/>

## 2xx : 요청 정상 처리

![이미지](/programming/img/입문613.PNG)

<br/>

## 202 Accepted

요청이 접수되었으나 처리가 완료되지 않았다.

- 배치 처리 같은 곳에서 사용한다.

- e.g.) 요청 접수 후 1시간 뒤에 배치 프로세스가 요청을 처리함.

<br/>

## 204 No Content

서버가 요청을 성공적으로 수행했지만, 응답 페이로드 본문에 보낼 데이터가 없다.

- e.g.) 웹 문서 편집기에서 save 버튼

- save 버튼의 결과로 아무 내용이 없어도 된다.

- save 버튼을 눌러도 같은 화면을 유지해야 한다.

- 결과 내용이 없어도 204 메시지 (2xx)만으로 성공을 인식할 수 있다.

<br/><br/>

## 3xx - 리다이렉션

- 클라이언트가 서버 요청을 했다.

    - 그러면 서버가 `“이, 요청을 완료 할려면 추가적인 작업이 필요해”` 말하고, 다시 클라이언트에게 보내는 것이다.
        

```java
"301 ~ 308까지가 중요하다"
```

<br/>

### 3xx : 요청을 완료하기 위해 유저 에이전트의 추가 조치 필요

- `300` : Muliiple Choices

- `301` : Moved Permanently

- `302` : Found

- `303` : See Other

- `304` : Not Modified

- `307` : Temporary Redirect


<br/><br/>

## 리다이렉션이 무엇인가요?

웹 브라우저는 `3xx` 응답 결과에 Location 헤더가 있으면, 

`Location` 위치로 자동 이동하는 것을 `리다이렉트` 라고도 부른다.

![이미지](/programming/img/입문614.PNG)

- 위 그림을 보면 1번 순서를 보면 `/event` 요청을 보낸다.

    - 그런데 서버 쪽에서 `“이제 /event url은 없어 졌는데? 변경된 곳으로 알려줄게”` 하고
        
        `301 시리즈와 동시`에 `새로운 /new-event 를 보내주는 것이다.` 
        
- 그리고 3번 순서를 보면 `자동으로 url이 변경되어 있다.`

- 변경된 url로 요청을 다시 서버로 보내고, 마지막 5번 `“ok”`를 해야 끝나게 되는 것이다.

<br/><br/>

## 리다이렉션 이해

- 영구 리다이렉션 - 특정 리소스의 URI가 영구적으로 이동한다.

    - e.g. `/members → /users`

    - e.g. `/event → /new-event`

- 일시 리다이렉션 - 일시적인 변경

    - 주문 완료 후 주문 내역 화면으로 이동

    - PRG: `post / Redirect / Get`

- 특수 리다이렉션

    - 결과 대신 캐시를 사용


<br/><br/>

## 301, 308은 기능이 똑같다. (영구 리다이렉션)

- 리소스의 URI가 영구적으로 이동한다.

- 원래의 URL를 사용하지 않고, 검색 엔진 등에서도 변경을 인지한다.

- 301, 308 : 리다이렉트시 요청 메서드가 GET으로 변하고, 본문이 제거될 수 있다.

<br/>

## 302, 307, 303 (일시적인 리다이렉션)

```java
"302는 요청 메서드가 GET 으로 변할 수도 있고, 아닐수도 있다"
 -> 이걸 보완 한것이 303 인것이다. 
 -> 303은 무조건 GET으로 보낸다. (=302와 기능은 같다)
```

- 리소스의 URI가 일시적으로 변경
    - 따라서 검색 엔진 등에서 URL을 변경하면 안됨
- 리다이렉트시 요청 메서드가 GET으로 변하고, 본문이 제거될 수 있음.

<br/>

### 그렇다면 일시적인 리다이렉트 언제 사용할까?

```java
"PRG 순간에 사용한다"
```

<br/>

### .e.g 설명

1. POST로 상품 주문하기 `버튼을 누른다`

2. POST로 내 데이터가 넘어간다.

3. 그 다음, 웹 브라우저를 새로고침 한다면?

4. 그 POST의 데이터가 한번 더 들어가게 될 것이다.

    - 그렇게 되면 `중복주문`이 들어가게 된다.

<br/><br/>

## PRG 사용하기 전 문제점.

![이미지](/programming/img/입문615.PNG)

<br/>

### 그리하여 PRG를 사용하면 문제 해결이 된다. (밑에 그림 참고)

![이미지](/programming/img/입문616.PNG)

- 새로 고침 해도 GET으로 결과 화면만 조회

- 그래서 뭘 사용해야 되나요? → 이미 많은 애플리케이션 라이브러리들이 302를 기본값으로 사용

<br/><br/>

![이미지](/programming/img/입문617.PNG)

<br/><br/>

![이미지](/programming/img/입문618.PNG)

<br/><br/>

마지막 get 부분이다.

![이미지](/programming/img/입문619.PNG)


