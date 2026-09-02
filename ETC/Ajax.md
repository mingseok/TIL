## Ajax 이해

### $.ajax의 문법은 아래와 같다.

```html
jQuery.ajax( [settings ] )
```

setting는 Ajax 통신을 위한 옵션을 담고 있는 객체가 들어간다. 

<br/>

## 주요한 옵션을 열거해보면 아래와 같다.

- `data`
    
    서버로 데이터를 전송할 때 이 옵션을 사용한다.
    
- `dataType`
    
    서버측에서 전송한 데이터를 어떤 형식의 데이터로 해석할 것인가를 지정한다. 
    
    값으로 올 수 있는 것은 xml, json, script, html이다. 
    
    형식을 지정하지 않으면 jQuery가 알아서 판단한다.
    
- `success`
    
    성공했을 때 호출할 콜백을 지정한다.
    
    Function( PlainObject data, String textStatus, jqXHR jqXHR )
    
- `type`
    
    데이터를 전송하는 방법을 지정한다. get, post를 사용할 수 있다.


<br/>
    

## POST 방식 예제1)

### 흐름

1. id가 #execute인 버튼을 클릭했을때 이 함수가 호출이 되는 것이다.

2. 그렇게 $.ajax 통신을 하는데,
3. 옵션에 오는 url은 '/like/add' 여기로 접속을 하겠다는 뜻이고,
4. 그리고 만약 post 방식으로 보낼때는, 명시적으로 post로 작성해야 되는 것이다.
<br/>즉, get일 경우는 디폴트가 get 이기 때문에 생략이 가능하다.
5. data 는 데이터 값을 서버 쪾으로 전송할 데이터 예를 들면, 현재의 시간대 이런것들을
    
    서버측으로 넘기는 것이다. $('form') 이렇게 할경우는 form태그를 말하는 것이다.
    
    serialize() -> '시리얼라이즈'를 하게 되면 form 태그 안에 들어갈 모든 태그들과 그안에 들어있는
    
    데이터들을 문자열로 만들어 주는 것이다. 즉 이 문자열이 서버로 전송이 되는 것이다.
    
6. success 는 성공 했을때 리턴하는 값을 id가 time인 곳에 test 형식으로 (data) 를 추가해라. 
    
    하는 것이다. (초록색 부분으로 들어가게 되는 것이다)
    

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <title>WEB</title>
    <meta charset="utf-8">
</head>
<body>

<p>time : <span id="time">   </span></p>

<form>
    <select name="timezone">
        <option value="Asia/Seoul">asia/seoul</option>
        <option value="America/New_York">America/New_York</option>
    </select>
    <select name="format">
        <option value="Y-m-d H:i:s">Y-m-d H:i:s</option>
        <option value="Y-m-d">Y-m-d</option>
    </select>
</form>
<input type="button" id="execute" value="execute"/>
<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>

<script>
    $('#execute').click(function () {
        $.ajax({
            url: '/like/add',
            type: 'post',
            data: $('form').serialize(),
            success: function (data) {
                $('#time').text(data);
            }
        })
    })
</script>

</body>
</html>
```

<br/>

실행 결과

time : 2022-05-21 11:22:57

<br/>

## JSON 방식 예제2)

동작 방식은 이렇다.

버튼 execute를 누르면 다른 파일에 접속을 하며, json 데이터가 있는 것이다.

`"Asia/Seoul", "America/New_York"` 이 정보를 받아서 리스트로 만들어 주는 예제이다.

<br/><br/>

## 흐름.

데이터 타입이 있다는 것이 중요하다.

다시 말해 /like/add 가 리턴 해주는 데이터가 `‘json’` 형식이 라는 것을 알 수 있는 것이다.

<br/>

그리고 success 가 콜백 되었을때(=호출 됬을때), 전달 되는 데이터는

‘json’ 파스를 이용해서 자바스크립트의 객체나 배열로 전환된 데이터가 첫번째 ‘data’ 인자로 전달이 되는 것이다. 

<br/>

success 는 자바의 데이터로 변환하는 과정이 들어 있는 것이다.

그리하여 str 이라는 빈 변수에 data를 반복문으로 돌리면서 그 값을 li 태그로 조합 한 다음에

ul 태그를 더해서 id가 timezones 이라는 곳에 들어가게 되는 것이다. 파란색 부분이다.

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <title>WEB</title>
    <meta charset="utf-8">
</head>
<body>

<p>time : <span id="time">   </span></p>

<form>
    <select name="timezone">
        <option value="Asia/Seoul">asia/seoul</option>
        <option value="America/New_York">America/New_York</option>
    </select>
    <select name="format">
        <option value="Y-m-d H:i:s">Y-m-d H:i:s</option>
        <option value="Y-m-d">Y-m-d</option>
    </select>
</form>
<input type="button" id="execute" value="execute"/>
<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>

<script>
    $('#execute').click(function () {
        $.ajax({
            url: '/like/add',
            type: 'post',
            data: $('form').serialize(),
            success: function (data) {
                $('#time').text(data);
            }
        })
    })
</script>

</body>
</html>
```

<br/>
<br/>

## $.ajax의 문법

```html
jQuery.ajax( [settings ] )
```

setting는 Ajax 통신을 위한 옵션을 담고 있는 객체가 들어간다. 

<br/>

주요한 옵션을 열거해보면 아래와 같다.

- data서버로 데이터를 전송할 때 이 옵션을 사용한다.
- dataType서버측에서 전송한 데이터를 어떤 형식의 데이터로 해석할 것인가를 지정한다.
    
    값으로 올 수 있는 것은 xml, json, script, html이다. 
    
    형식을 지정하지 않으면 jQuery가 알아서 판단한다.
    
- success성공했을 때 호출할 콜백을 지정한다.Function
    
    ( PlainObject data, String textStatus, jqXHR jqXHR )
    
- type데이터를 전송하는 방법을 지정한다. get, post를 사용할 수 있다.


<br/><br/>

## 실제 코드에서 사용시

```html
<!DOCTYPE html>
<html lang="en"
      xmlns:th="http://www.thymeleaf.org">
<head th:replace="fragments.html :: head"></head>
<body>
<div th:replace="fragments.html :: main-nav"></div>
<div class="container">
    <p>글번호 : [[${post.id}]]</p>
    <p>제목 : [[${post.title}]]</p>
    <p>작성자 : [[${post.writer}]]</p>
    내용 : <textarea class="form-control" th:text="${post.content}"></textarea>
    <p>조회수 : [[${post.hit}]]</p>

    <button id="update" class="btn btn-outline-primary" type="button">
        <a th:href="@{'/update/' + ${post.id}}">수정</a>
    </button>
    <button id="delete" class="btn btn-danger delete-btn" type="button" th:value="${post.id}">삭제</button>
</div>
<script type="application/javascript" th:inline="javascript">

    $(document).on("click", ".delete-btn", function () {

        // 스프링 시큐리티를 사용하면 Ajax 통신을 할때 이런 과정을 수행 해야 되는 것이다.

        // 스프링 시큐리티로 Ajax 통신 하려면 토큰이 필요한 것이다.
        // header, token 은 무조건 작성해주기.
        let header = [[${_csrf.headerName}]];
        let token = [[${_csrf.token}]];

        // val() 는 delete-btn 버튼에 value() 값을 말하는 것이다.
        let id = $(".delete-btn").val();

        $.ajax({
            url: '/delete/' + id, // url은 컨트롤러의 @DeleteMapping("/delete/{id}") 으로 가는 것이다.
                                  // 그리고 id는 위에 있는 id를 말하는 것이다.

            type: 'delete', // @GetMapping, @PostMapping, @PutMapping, @DeleteMapping 을 말하는 것이다.

            success: function (data) { // (data) 는 컨트롤러의 @DeleteMapping("/delete/{id}") 메서드의 리턴값을 말하는 것이다.
                window.location.replace(data); // 그리고 여기서 replace() 가 리다이렉트라고 생각하면 된다. -> return "/post";
            },

            // beforeSend 통신하기 전에 먼저 실행 되는 것이다.
            // 즉, beforeSend 가 먼저 실행되고 그 다음 위가 실행 되는 것이다.
            // http 헤더에 header, token 실어서 보내면서 통신이 되는 것이다.
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
        });

    })

</script>
</body>

</html>
```


<br/><br/>

>**Reference** <br/>생활코딩 javaScript - Ajax : https://www.youtube.com/watch?v=_FI0U7I8QAg

<br/>

## 궁금증!

```java
fetch 는 왜 404 에서 catch 로 안 가나
```

`fetch` 는 네트워크가 실패했을 때만 거부한다.

```javascript
fetch('/api/none')
    .then(res => console.log('여기로 온다'))     // 404 여도 여기
    .catch(err => console.log('여기는 안 온다'));
```

<br/>

서버가 응답을 주기만 하면 `성공` 으로 본다.

`404` 든 `500` 든 응답은 응답이기 때문이다.

<br/>

`catch` 로 가는 건 이런 경우다.

```java
네트워크가 끊겼다
CORS 에 막혔다
요청이 중단됐다
```

<br/>

## 그래서 직접 확인해야 한다

```javascript
const res = await fetch('/api/members');
if (!res.ok) {
    throw new Error(`HTTP ${res.status}`);
}
const data = await res.json();
```

<br/>

`res.ok` 는 상태 코드가 `200~299` 면 `true` 다.

<br/>

앞의 HTTP 상태 코드 글에서 본 그 분류다.

<br/>

`axios` 는 이걸 알아서 해준다.

`4xx` 와 `5xx` 를 거부로 처리하는 것이다.

<br/>

`fetch` 가 표준인데도 `axios` 를 쓰는 이유 중 하나가 이것이다.

<br/>

## json() 도 비동기다

```javascript
const data = await res.json();
```

<br/>

응답 본문이 스트림이라 다 읽어야 파싱할 수 있기 때문이다.

<br/>

앞의 HTTP body에 데이터를 직접 담아서 요청 - @RequestBody 글에서 본 그 성질이다.

한 번 읽으면 다시 못 읽는다.

```javascript
await res.json();
await res.text();      // TypeError: body stream already read
```

<br/>

두 번 읽으려면 복제해야 한다.

```javascript
const clone = res.clone();
```

<br/>

서버 쪽의 `ContentCachingRequestWrapper` 와 같은 발상이다.

<br/>

## CORS 가 왜 필요한가

브라우저가 다른 출처로 요청을 보내는 것을 막기 때문이다.

```java
출처 = 프로토콜 + 도메인 + 포트
```

<br/>

하나라도 다르면 다른 출처다.

```java
https://a.com      -> http://a.com       (프로토콜이 다르다)
https://a.com      -> https://api.a.com  (도메인이 다르다)
https://a.com:443  -> https://a.com:8080 (포트가 다르다)
```

<br/>

앞의 쿠키(Cookie) 글에서 본 그 문제 때문이다.

<br/>

브라우저는 쿠키를 자동으로 붙인다.

악성 사이트가 은행 API를 부르면 사용자 쿠키가 같이 나갈 것이다.

<br/>

그래서 `서버가 허락한 출처만` 읽을 수 있게 막아둔 것이다.

<br/>

## 요청은 나가고 응답만 막힌다

이게 헷갈리는 부분이다.

<br/>

단순 요청은 서버까지 갔다 온다.

브라우저가 응답을 자바스크립트에 안 넘길 뿐이다.

<br/>

그래서 `GET` 요청은 CORS로 막아도 서버에서는 실행된다.

<br/>

`부수 효과가 있는 요청` 은 미리 물어본다.

```java
OPTIONS /api/members
Access-Control-Request-Method: DELETE
```

<br/>

이게 프리플라이트다.

`DELETE` 를 보내도 되냐고 먼저 묻는 것이다.

<br/>

서버가 허락하면 진짜 요청을 보낸다.

<br/>

## 프리플라이트가 뜨는 조건

```java
메서드가 GET, HEAD, POST 가 아니다
Content-Type 이 application/json 이다
커스텀 헤더가 있다 (Authorization 등)
```

<br/>

JSON을 보내는 API는 거의 전부 프리플라이트가 뜬다.

<br/>

요청이 두 번 나가니 느려진다.

```java
Access-Control-Max-Age: 3600
```

<br/>

이걸 주면 브라우저가 결과를 캐시해서 한동안 안 묻는다.

<br/>

## 쿠키를 같이 보내려면 양쪽 설정이 필요하다

```javascript
fetch(url, { credentials: 'include' })
```

```java
Access-Control-Allow-Credentials: true
Access-Control-Allow-Origin: https://a.com      // * 는 안 된다
```

<br/>

`*` 와 `credentials` 는 같이 못 쓴다.

<br/>

`아무나 와도 된다` 와 `쿠키를 붙여 보내라` 가 같이 있으면

아무 사이트나 사용자 쿠키로 API를 부를 수 있게 되기 때문이다.

<br/>

그래서 출처를 명시해야 한다.

<br/>

## 서버가 서버를 부를 때는 CORS 가 없다

CORS는 브라우저의 규칙이다.

<br/>

앞의 Postman 사용 이유 글에서 본 대로,

`curl` 이나 Postman으로는 그냥 된다.

<br/>

서버끼리 부를 때도 마찬가지다.

<br/>

`CORS 오류` 를 보안 기능으로 오해하기 쉬운데,

브라우저 밖에서는 아무 보호가 안 되는 것이다.

<br/>

진짜 보안은 서버의 인증과 인가다.

앞의 조건부 if 글에서 본 것과 같은 얘기다.

`화면에서 막는 것` 과 `서버에서 막는 것` 은 다른 층의 일이다.
