## @GetMapping, Model 설명

컨트롤러가 필요하다 → `HelloController`

컨트롤러는 어너테이션(`@Controller`)을 무조건 달아줘야 한다

<br/><br/>

## `@GetMapping` 이란?

![이미지](/programming/img/입문1.PNG)

웹 어플리케이션에서 `'/hello'` 라고 들어오면 이 메서드를 호출해준다.

어떻게? → @GetMapping(`"hello"`) 으로 되어 있기 때문이다.

다시 말해, 클라이언트가 `url` 등으로 요청을 보내는 이름이다.




<br/><br/>

## `return` 뭘까?

return의 `hello`는 `html` 파일의 이름이다. (컨트롤 + v 하면 해당 링크 이동)

즉, html 파일로 모델을 넘기는 것이다. (이 처리를 해주는 애가 `뷰 리졸버`)

예시) `member/register` 되어 있으면 member/register 페이지로 넘어가게 된다.

```java
@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("data", "hello 민석!!");
        return "hello";
    }
}
```

<br/><br/>

## 위 코드에서의 `Model` 이란?

스프링이 모델이란걸 만들어서 여기 안으로 넣어준다는 개념이다.

그리하여 저장할땐 `addAttribute()` 메서드를 사용하여 모델에 저장한다.

`("data", "hello 민석!!")` → 키와 벨류 라고 생각하면 된다.


<br/><br/>


### 자세히 설명하자면,

Model은 `HttpServletRequest` 객체를 사용한다. 

`request`는 내부에 `데이터 저장소`를 가지고 있는데, 

`request.setAttribute()` 을 사용하여 데이터를 저장 할 수 있고, 

`request.getAttribute()` 를 사용하여 데이터를 조회할 수 있다.

<br/><br/>

### 밑 html 코드랑 같이 보기

```java
@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("data", "hello 민석!!");
        return "hello";
    }
}
```

<br/>

### hello.html

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Hello</title>
</head>
<body>
<p th:text="'안녕하세요. '+ ${data}">안녕하세요. 손님</p>
</body>
</html>
```

<br/><br/>

## `Model` 정리

컨트롤러를 보면 앞쪽 `"data"` 에 `"hello 민석!!"` 를 저장하는 것이다.

그리하여 html 코드의 `${data}` 부분에 `"data"` 가 들어가게 되어, `"hello 민석!!"` 로 치환되는 것이다. 

(밑에 코드 처럼 변경 된다.)

```html
<p th:text="'안녕하세요. '+ hello 민석!!">안녕하세요. 손님</p>
```

<br/><br/>


![이미지](/programming/img/입문.PNG)

<br/><br/>

## 추가로

```markdown
### http 바디에 데이터를 보내지 않는다. 
    
URL라는 주소에 쿼리 파라미터인 데이터를 포함해서 전달 하는 것이다.

예) 검색, 필터, 페이징 등에서 많이 사용하는 방식.
```


<br/><br/>


>**Reference** <br/>[스프링 핵심 원리 - 기본편](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8?utm_source=google&utm_medium=cpc&utm_campaign=04.general_backend&utm_content=spring&utm_term=%EC%8A%A4%ED%94%84%EB%A7%81%20%EC%9E%85%EB%AC%B8&gclid=CjwKCAiAjPyfBhBMEiwAB2CCImohok2YrQ2tRdhqfr3cZvKqkIJOHUJ36u6s1-7C9X1gzZIapTvOtxoCangQAvD_BwE)
