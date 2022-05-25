## URL 링크

타임리프에서 URL을 생성할 때는 `@{...}` 문법을 사용하면 된다.

<br/>

어렵게 생각하지 말자.

결국 경로를 만드는 부분(= '{ }' )과 데이터가 있는 부분(= '( )' )이 분리가 되어 있는 것이다.



<br/>

### 단순한 URL

`@{/hello} /hello`

<br/>

### 쿼리 파라미터

`@{/hello(param1=${param1}, param2=${param2})}`

→ /hello?param1=data1&param2=data2 이렇게 된다.

`()` 에 있는 부분은 쿼리 파라미터로 처리된다.

<br/>

### 경로 변수

`@{/hello/{param1}/{param2}(param1=${param1}, param2=${param2})}`

→ /hello/data1/data2

URL 경로상에 변수가 있으면 () 부분은 경로 변수로 처리된다.

<br/>

### 경로 변수 + 쿼리 파라미터

`@{/hello/{param1}(param1=${param1}, param2=${param2})}`

→ /hello/data1?param2=data2

경로 변수와 쿼리 파라미터를 함께 사용할 수 있다.

<br/>

### 상대경로, 절대경로, 프로토콜 기준을 표현할 수 도 있다.

/hello : 절대 경로

hello : 상대 경로

<br/>

java 클래스

```java
package hello.thymeleaf.basic;

@Controller
@RequestMapping("/basic")
public class BasicController {

    @GetMapping("link")
    public String link(Model model) {
        model.addAttribute("param1", "data1");
        model.addAttribute("param2", "data3");
        return "basic/link";
    }
}
```

<br/>

/resources/templates/basic/link.html
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
    <li><a th:href="@{/hello}">basic url</a></li>
    <li><a th:href="@{/hello(param1=${param1}, param2=${param2})}">hello query param</a></li>
    <li><a th:href="@{/hello/{param1}/{param2}(param1=${param1}, param2=${param2})}">path variable</a></li>
    <li><a th:href="@{/hello/{param1}(param1=${param1}, param2=${param2})}">path variable + query parameter</a></li>
</ul>
</body>
</html>
```

<br/>

>**Reference** <br/>스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1