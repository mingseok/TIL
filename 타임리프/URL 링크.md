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
