## HTTP 요청 파라미터 - @ModelAttribute

`요청 파라미터`란? → `GET`에 쿼리스트링 오는 것, or `POST`방식으로 

HTML `폼 태그` 데이터 전송 하는 방식을 말하는 것이다. 





<br/>

둘 경우에만 `@RequestParam` 이랑 `@ModelAttribute` 를 사용하는 것이다.

그 외 경우들은 전부 `@RequestBody`를 사용하거나, 데이터를 직접 꺼내야 되는 것이다.

```
요청 파라미터 vs HTTP 메시지 바디
요청 파라미터를 조회: @RequestParam, @ModelAttribute
HTTP 메시지 바디를 직접 조회: @RequestBody
```


```
--머리에 삽입--
@ModelAttribute 는 HTTP 요청 파라미터(URL 쿼리 스트링, POST Form)를 다룰 때 사용한다.

@RequestBody 는 HTTP Body의 데이터를 객체로 변환할 때 사용한다. 
- 주로 API JSON 요청을 다룰 때 사용한다.
```



<br/><br/>


### 개발을 하면 `요청 파라미터`를 받아서 필요한 `객체`를 만들고 그 객체에 `값을 넣어`주어야 한다. 

```java
@Data
public class HelloData {
    private String username;
    private int age;
}
```

<br/>

### 보통 이런식으로 할것이다.

```java
@ResponseBody
@RequestMapping("/model-attribute")
public String modelAttribut(@RequestParam String username, @RequestParam int age) {

    HelloData helloData = new HelloData();
    helloData.setUsername(username);
    helloData.setAge(age);
    return "ok";
}
```

스프링은 이 과정을 완전히 자동화해주는 `@ModelAttribute` 기능을 제공한다.

<br/><br/>

## @ModelAttribute 사용

`HelloData` 객체가 생성되고, 요청 파라미터의 값도 모두 들어가 있다.

```java
@ResponseBody
@RequestMapping("/model-attribute")
public String modelAttribute(@ModelAttribute HelloData helloData) {
    log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
    return "ok";
}
```

<br/>

### 출력 시켜보기

```java
- 제대로 입력할 경우 -
http://localhost:8080/model-attribute?username=kim&age=10
username=kim, age=10

- 틀리게 입력할 경우 - 
http://localhost:8080/model-attribute?AAA=kim&BBB=10
username=null, age=0
```

<br/><br/>

## 동작 순서.

1. `HelloData` 객체를 생성한다.

2. 요청 파라미터의 이름으로 `HelloData` 객체안의 프로퍼티를 찾는다. 

    - 그리고 해당 프로퍼티의 `setter`를 호출해서 파라미터의 값을 입력(`바인딩`) 한다.

예) 파라미터 이름이 `username` 이면 `setUsername()` 메서드를 찾아서 호출하면서 값을 입력한다.

<br/><br/>

## 추가로 model.addAttribute() 도 생략이 가능하다.

대신, `@ModelAttribute("item")` 이렇게 이름을 정해주고, 해당 이름으로

`model.addAttribute("item", item);` 코드가 생략이 가능하고, (코드 주석 잘보기)

매개변수 쪽의 Model model 선언도 생략이 가능하다.

```java
@PostMapping("/add")
public String addItem(@ModelAttribute("item") Item item, Model model) { // 매개변수도 생략 가능하다

    itemRepository.save(item);
    // model.addAttribute("item", item); // 생략 가능하다.

    return "basic/item";
}
```

<br/><br/>

## 주의할 점은

`html`에 렌더링 되는 이름과 동일하게 `@ModelAttribute("item")` 이름과 똑같게 해야 된다는 것이다.

![이미지](/programming/img/입문60.PNG)

모델에 데이터를 담을 때는 이름이 필요하다. 

이름은 `@ModelAttribute` 에 지정한 이름으로 한다.

<br/><br/>

## 더 개선하면 (이렇게 사용하자)

`@ModelAttribute("item")` 이름도 생략 할 수가 있다.

그럼 어떻게 동작하는가? → ( … Item item) 인 클래스명으로 되는 것인데, 앞 글자만 

소문자로 변환해서 이름을 지정해 주는 것이다. 

- 만약, `HelloDate` → `helloDate` 가 매개변수명으로 되야 한다.

- 그리하여 밑에 코드처럼 `Item` → `item` 으로 변경하여 사용 할 수 있다.

```java
@PostMapping("/add")
public String addItem(@ModelAttribute Item item) { 

    // Model model 에 담기는 것은 item 이다. 결국 똑같다.
    itemRepository.save(item);

    return "basic/item";
}
```



<br/><br/>


>**Reference** <br/>[스프링 핵심 원리 - 기본편](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8?utm_source=google&utm_medium=cpc&utm_campaign=04.general_backend&utm_content=spring&utm_term=%EC%8A%A4%ED%94%84%EB%A7%81%20%EC%9E%85%EB%AC%B8&gclid=CjwKCAiAjPyfBhBMEiwAB2CCImohok2YrQ2tRdhqfr3cZvKqkIJOHUJ36u6s1-7C9X1gzZIapTvOtxoCangQAvD_BwE)
