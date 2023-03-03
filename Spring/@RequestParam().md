## @RequestParam() 설명

<br/>

## 밑 코드의 `@RequestParam("aaa")` 이 뭘까?

```java
@GetMapping("hello-mvc")
public String helloMvc(@RequestParam("aaa") String name, Model model) {
    model.addAttribute("name", name); // 키, 벨류
    return "hello-template";
}
```

<br/>

## 결론부터 보자면 `?aaa=` 를 잘보자

![이미지](/programming/img/입문2.PNG)

사진 처럼 `url`로 부터 `?aaa=` 하여 ‘=’ 뒤에 값을 입력 해주면 (`’민석!!!!!!'`)

그 값이 `String name` 매개변수에 저장되어 `model`에 담기게 되는 것이다.

(바인딩 한다고 부른다)

<br/>

![이미지](/programming/img/입문3.PNG)

<br/>

## 그리하여 hello-template 파일인 html에 출력 된다.

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Hello</title>
</head>
<body>
    <p th:text="${name}">hello! empty</p>
</body>
</html>
```

<br/><br/>

## 한번 더 해보기.

`“name”` 으로 수정 되었고, `url`에 `‘스프링’` 이라고 작성하여 `‘스프링’`이 출력 되었다.

```java
@GetMapping("hello-mvc")
public String helloMvc(@RequestParam("name") String name, Model model) {
    model.addAttribute("name", name); // 키, 벨류
    return "hello-template";
}
```

<br/>

![이미지](/programming/img/입문4.PNG)