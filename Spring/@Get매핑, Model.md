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

```
주의해야 될 점은 "data"라는 이름과 html에서의 "data"라는 변수명이 일치해야 된다는 것이다.
```


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

<br/>

## 궁금증!

```java
Model 은 어디에 담기는가
```

결국 `HttpServletRequest` 의 attribute 로 들어간다.

```java
model.addAttribute("members", members);
```

<br/>

이것이 나중에 이렇게 바뀐다.

```java
request.setAttribute("members", members);
```

<br/>

앞의 리다이렉트, 디스패처 글에서 본 대로,

포워드를 하면 같은 `request` 를 넘기니 JSP에서 꺼내 쓸 수 있는 것이다.

```java
<c:forEach var="member" items="${members}">
```

<br/>

`${members}` 가 `request.getAttribute("members")` 를 찾는 표현식이다.

<br/>

## 그래서 리다이렉트하면 Model 이 사라진다

```java
@PostMapping("/members/new")
public String save(Model model) {
    model.addAttribute("message", "등록 완료");
    return "redirect:/members";        // message 는 사라진다
}
```

<br/>

리다이렉트는 새 요청이라 앞의 `request` 가 버려지기 때문이다.

<br/>

그래서 `RedirectAttributes` 가 따로 있는 것이다.

```java
redirectAttributes.addFlashAttribute("message", "등록 완료");
```

<br/>

이건 세션에 잠깐 넣었다가 다음 요청에서 꺼내고 지운다.

<br/>

## Model 대신 쓸 수 있는 것들

세 가지가 있는데 결국 같은 데로 간다.

```java
public String list(Model model) {
    model.addAttribute("members", members);
}

public String list(ModelMap map) {
    map.addAttribute("members", members);
}

public ModelAndView list() {
    ModelAndView mv = new ModelAndView("members/list");
    mv.addObject("members", members);
    return mv;
}
```

<br/>

`ModelAndView` 는 옛날 방식이다. 모델과 뷰 이름을 한 객체에 담아 돌려준다.

<br/>

앞의 스프링 MVC 구조 글에서 본 대로 `HandlerAdapter` 의 반환 타입이 `ModelAndView` 라,

`Model` 을 쓰고 `String` 을 반환해도 내부에서 `ModelAndView` 로 조립된다.

```java
String 반환 + Model 파라미터  ->  내부에서 ModelAndView 로 합쳐진다
```

<br/>

## 반환값을 아예 안 쓸 수도 있다

```java
@GetMapping("/members")
public void list(Model model) {
    model.addAttribute("members", members);
}
```

<br/>

`void` 를 반환하면 스프링이 요청 경로를 뷰 이름으로 쓴다.

`/members` 로 들어왔으니 `members` 라는 뷰를 찾는 것이다.

<br/>

되긴 하는데 실무에서는 안 쓴다.

URL을 바꾸면 뷰까지 같이 바뀌어버려서 예상하기 어렵기 때문이다.

<br/>

## @ModelAttribute 도 모델에 담긴다

```java
@GetMapping("/members/new")
public String form(@ModelAttribute("memberForm") MemberForm form) {
    return "members/newForm";
}
```

<br/>

앞의 특별한 @ModelAttribute 사용법 글에서 본 대로,

`@ModelAttribute` 는 객체를 만들어 값을 채운 다음 모델에도 자동으로 넣어준다.

<br/>

그래서 `model.addAttribute()` 를 따로 안 써도 뷰에서 쓸 수 있다.

```java
<input type="text" th:field="*{name}">
```

<br/>

이름을 생략하면 클래스 이름의 첫 글자를 소문자로 바꾼 것이 이름이 된다.

```java
MemberForm  -> "memberForm"
Item        -> "item"
```

<br/>

## Model 을 안 쓰는 경우

앞의 API 방식(json), @ResponseBody 글에서 본 그 경우다.

```java
@GetMapping("/api/members")
@ResponseBody
public List<Member> list() {
    return members;              // Model 이 필요 없다
}
```

<br/>

뷰를 안 그리니 모델도 필요 없다. 객체를 그대로 돌려주면 JSON이 된다.

<br/>

`Model` 은 화면을 서버에서 그릴 때만 쓰는 것이라고 보면 된다.
