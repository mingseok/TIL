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
