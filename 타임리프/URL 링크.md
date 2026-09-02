## URL 링크

## URL을 생성할 때는 `@{...}` 문법을 사용하면 된다.


결국 경로를 만드는 부분(= '{ }' )과 데이터가 있는 부분(= '( )' )이 분리가 되어 있는 것이다.

- 파라미터를 사용해서 사용하고 싶을 경우는?


```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<h1>URL 링크</h1>
<ul>
    <!-- /hello -->
    <li><a th:href="@{/hello}">basic url</a></li>

    <!-- /hello?param1=data1&param2=data2 -->
    <li><a th:href="@{/hello(param1=${param1}, param2=${param2})}">hello query param</a></li>

    <!-- /hello/data1/data2 -->
    <li><a th:href="@{/hello/{param1}/{param2}(param1=${param1}, param2=${param2})}">path variable</a></li>
    
    <!-- /hello/data1?param2=data2 -->
    <li><a th:href="@{/hello/{param1}(param1=${param1}, param2=${param2})}">path variable + query parameter</a></li>
</ul>
</body>
</html>
```



<br/><br/>


>**Reference** <br/>[스프링 MVC 2편 - 백엔드 웹 개발 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2)

<br/>

## 궁금증!

```java
@{...} 를 안 쓰고 그냥 적으면 안 되나
```

컨텍스트 경로 때문에 안 된다.

```html
<a href="/members">회원 목록</a>
<a th:href="@{/members}">회원 목록</a>
```

<br/>

앱이 루트에 배포되면 둘 다 같다.

하위 경로에 배포되면 갈린다.

```java
컨텍스트가 /myapp 일 때

href="/members"        ->  /members         (404)
th:href="@{/members}"  ->  /myapp/members
```

<br/>

앞의 절대 경로, 상대 경로 글에서 본 그 문제다.

<br/>

스프링 부트는 기본 컨텍스트가 `/` 라 차이가 안 보인다.

그래서 나중에 배포 경로가 바뀔 때 한꺼번에 깨진다.

<br/>

## 경로 변수와 쿼리 파라미터

```html
<a th:href="@{/members/{id}(id=${member.id})}">상세</a>
<a th:href="@{/members(page=${page}, size=10)}">목록</a>
```

<br/>

```java
/members/1
/members?page=2&size=10
```

<br/>

괄호 안의 이름이 경로에 있으면 경로 변수가 되고, 없으면 쿼리 파라미터가 된다.

<br/>

둘을 섞을 수도 있다.

```html
<a th:href="@{/members/{id}/orders(id=${member.id}, status='PAID')}">
```

```java
/members/1/orders?status=PAID
```

<br/>

## 인코딩을 알아서 해준다

```html
<a th:href="@{/search(keyword=${keyword})}">
```

<br/>

`keyword` 가 `안녕 하세요` 면 이렇게 나간다.

```java
/search?keyword=%EC%95%88%EB%85%95+%ED%95%98%EC%84%B8%EC%9A%94
```

<br/>

앞의 @PostMapping(), form태그 글에서 확인한 그 퍼센트 인코딩이다.

<br/>

직접 문자열을 이어 붙이면 이 처리가 안 된다.

```html
<a th:href="'/search?keyword=' + ${keyword}">      <!-- 공백이 그대로 들어간다 -->
```

<br/>

공백이나 `&` 가 들어가면 URL이 깨진다.

<br/>

## 상대 경로도 쓸 수 있다

```html
th:href="@{/members}"        절대 경로. 컨텍스트가 붙는다
th:href="@{members}"         상대 경로. 지금 위치 기준
th:href="@{~/members}"       서버 루트. 컨텍스트를 안 붙인다
th:href="@{//cdn.com/a.js}"  프로토콜 상대 경로
```

<br/>

거의 항상 첫 번째를 쓴다.

<br/>

세 번째는 같은 서버의 다른 앱을 가리킬 때 쓴다.

<br/>

## 프래그먼트도 붙일 수 있다

```html
<a th:href="@{/members#section2}">
<a th:href="@{/members(page=2)#section2}">   <!-- /members?page=2#section2 -->
```

<br/>

앞의 URI 와 웹 브라우저 요청 흐름 글에서 본 대로,

`#` 뒤는 서버로 안 간다. 브라우저 안에서만 쓰인다.

<br/>

## 자바스크립트에서 URL 을 쓸 때

```html
<script th:inline="javascript">
    const url = [[@{/api/members}]];
</script>
```

<br/>

앞의 자바스크립트 인라인 each 글에서 본 그 방식이다.

<br/>

`th:inline="javascript"` 를 안 붙이면 따옴표가 안 붙어서 문법 오류가 난다.

<br/>

정적 `.js` 파일에서는 이게 안 된다.

타임리프가 처리하지 않는 파일이기 때문이다.

<br/>

그럴 때는 HTML에서 값을 넘겨준다.

```html
<body th:data-base-url="@{/}">
```

```javascript
const baseUrl = document.body.dataset.baseUrl;
```

<br/>

`data-` 속성으로 넘기면 정적 파일에서도 읽을 수 있다.

<br/>

앞의 제어할 태그 선택하기 글에서 본 `dataset` 이 이럴 때 쓰인다.
