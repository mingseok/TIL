## @RequestBody, @RequestParam, @ModelAttribute의 차이



### 요청 파라미터란?

`GET`에 쿼리스트링 오는 것, 또는 `POST`방식으로

HTML `Form 태그` 데이터 전송 하는 방식을 말하는 것이다.

```java
그 외 경우들은 전부 @RequestBody를 사용

요청 파라미터 vs HTTP 메시지 바디
요청 파라미터를 조회 : @RequestParam , @ModelAttribute
HTTP 메시지 바디를 직접 조회 : @RequestBody
```

<br/><br/>

## @RequestBody

`HTTP Body`의 데이터를 객체로 변환할 때 사용한다.

```java
"주로 API JSON 요청을 다룰 때 사용"
```

<br/>

클라이언트가 전송하는 JSON 형태의 HTTP body 내용을 

`messageConverter`를 통해 java 객체로 변환 시켜주는 역할을 합니다.

<br/>

`요청 오는 것은 @RequestBody`로, `응답 나가는 것은 @ResponseBody`로 생각하면 된다.

```java
@ResponseBody
@PostMapping("/request-body-string")
public String requestBodyString(@RequestBody String messageBody) { // 요청 오는 것
	  log.info("messageBody={}", messageBody);
	  return "ok";
}
```

- `@RequestBody`에 `String`으로 되어 있다면,

    - HTTP 메시지 바디 부분을 읽어서 바로 문자로 변환 후 매개변수에 넣어주는 것이다.

    - `@RequestBody` 를 사용하면 HTTP 메시지 바디 정보를 편리하게 조회할 수 있다.

- `@ResponseBody` 응답 같은 경우는 메서드 반환타입인 `String`이랑 짝이 된다고 생각하면 된다.

<br/><br/>

## @RequestParam

`1개`의 http 요청 파라미터를 받기 위해 사용한다.

@RequestParam은 필수 여부가 true이기 때문에 기본적으로 반드시 해당 파라미터가 전송되어야 한다.

전송되지 않으면 `400Error`를 유발할 수 있으며, 반드시 필요한 변수가 아니라면 `required의 값을 false로` 설정해줘야 한다.

<br/>

### 코드의 `@RequestParam("aaa")` 이 뭘까?

```java
@GetMapping("hello-mvc")
public String helloMvc(@RequestParam("aaa") String name, Model model) {
    model.addAttribute("name", name); // 키, 벨류
    return "hello-template";
}
```

<br/>

![이미지](/programming/img/입문460.PNG)

`url`로 부터 `?aaa=` 하여 ‘=’ 뒤에 값을 입력 해주면 `(민석!!!!!!)` 입력값이 
매개변수로 오게 되는 것이다. `(=바인딩 이라고 부른다)`

<br/>

![이미지](/programming/img/입문461.PNG)

<br/>

하지만 **`@RequestParam()`** 변수명을 생략한다.

`@RequestParam("aaa")`인 -> @RequestParam 처럼 이름을 지정하지 않았다면

html에서 넘어 오는 `name`인 → `“itemName”` 이 되는 것이다.

![이미지](/programming/img/입문462.PNG)

<br/>

```java
-- 변경 후 코드--
@ResponseBody
@RequestMapping("/request-param")
public String requestParam(@RequestParam String username, @RequestParam int age) {
    log.info("username={}, age={}", username, age);
    return "ok";
}
```

<br/><br/>

## @ModelAttribute

개발을 하면 `요청 파라미터`를 받아서 필요한 `객체`를 만들고, 그 객체에 `값을 넣어`주어야 한다.

HTTP 요청 파라미터(URL 쿼리 스트링, POST Form)를 다룰 때 사용한다.

```java
@Data
public class HelloData {
    private String username;
    private int age;
}
```

<br/>

http Body 내용과 http 파라미터의 값들을 생성자, 게터, 세터를 통해 주입하기 위해 사용합니다.

값 변환이 아닌, 값을 주입 시키므로 변수들의 
`생성자`나 `게터`, `세터`가 없으면 변수들이 `저장되지 않는다.`

<br/>

### 변경 전 코드

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

<br/>


### 변경 된 코드

`HelloData` 객체가 생성되고, 요청 파라미터의 값도 모두 들어가 있다. (스프링이 없애준다)

```java
@ResponseBody
@RequestMapping("/model-attribute")
public String modelAttribute(@ModelAttribute HelloData helloData) {
    log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
    return "ok";
}
```

### 동작 순서

1. `HelloData` 객체를 생성한다.

2. 요청 파라미터의 이름으로 `HelloData` 객체안의 프로퍼티를 찾는다.

    - 그리고 해당 프로퍼티의 `setter`를 호출해서 파라미터의 값을 입력(`바인딩`) 한다.

e.g. 파라미터 이름이 `username` 이면 `setUsername()` 메서드를 찾아서 호출하면서 값을 입력한다.

<br/><br/>

## model.addAttribute() 도 생략이 가능하다.

대신, `@ModelAttribute("item")` 이렇게 이름을 정해주고, 해당 이름으로

`model.addAttribute("item", item);` 코드가 생략이 가능하고, (코드 주석 잘보기)

매개변수 쪽의 Model model 선언도 생략이 가능하다.

```java
@PostMapping("/add")
public String addItem(@ModelAttribute("item") Item item, Model model) { // 매개변수도 생략 가능하다

    itemRepository.save(item);
    // model.addAttribute("item", item); // 생략 가능하다.

    return "basic/item";
}
```

<br/>

`html`에 렌더링 되는 이름과 동일하게 `@ModelAttribute("item")` 이름과 똑같게 해야 된다는 것이다.

![이미지](/programming/img/입문463.PNG)

모델에 데이터를 담을 때는 이름이 필요하다.
이름은 `@ModelAttribute` 에 지정한 이름으로 한다.

<br/><br/>

## 더 개선하여 이렇게 사용하자.

`@ModelAttribute("item")` 이름도 생략 할 수가 있다.

```java
그럼 어떻게 동작하는가? -> "(@ModelAttribute Item item)"인 클래스명으로 되는 것인데, 
"앞 글자만 소문자"로 변환해서 이름을 지정해 주는 것이다.
```

<br/>

만약, `HelloDate` → `helloDate` 가 `매개변수명`으로 되야 한다.

- 그리하여 밑에 코드처럼 `Item` → `item` 으로 변경하여 사용 할 수 있다.

```java
@PostMapping("/add")
public String addItem(@ModelAttribute Item item) { 

    // Model model 에 담기는 것은 item 이다. 결국 똑같다.
    itemRepository.save(item);

    return "basic/item";
}
```
<br/>

## 궁금증!

```java
셋의 차이를 "어디서 값을 꺼내오는가" 로 정리하면 어떻게 될까?
```

앞의 form 태그 글에서 요청을 실제로 찍어봤던 것을 다시 보면 답이 나온다.

```java
GET /members?name=%eb%af%bc%ec%84%9d&age=30 HTTP/1.1

[본문] (없음)
```

```java
POST /members/new HTTP/1.1
Content-type: application/x-www-form-urlencoded

[본문] name=%EB%AF%BC%EC%84%9D&age=30
```

<br/>

`GET` 은 URL 뒤에, form `POST` 는 본문에 값이 있지만 형식이 똑같다.

`키=값&키=값` 이다.

<br/>

서블릿은 이 둘을 구분하지 않고 `request.getParameter("name")` 하나로 꺼낼 수 있게 해준다.

이것을 `요청 파라미터` 라고 부른다. 원문의 정의 그대로다.

```java
요청 파라미터 (getParameter 로 꺼낼 수 있는 것)
  - GET 의 쿼리 스트링
  - POST 의 form 형식 본문

그 외의 본문 (JSON, XML, 순수 텍스트)
  - getParameter 로는 못 꺼낸다. 본문을 직접 읽어야 한다
```

<br/>

이 경계가 곧 세 어노테이션의 경계다.

```java
@RequestParam    -> 요청 파라미터를 하나씩 꺼낸다
@ModelAttribute  -> 요청 파라미터를 객체에 한 번에 담는다
@RequestBody     -> 본문을 통째로 읽어서 컨버터로 객체를 만든다
```

<br/>

## 값을 채우는 방식이 다르다

이게 실무에서 문제가 되는 지점이다.

```java
@ModelAttribute -> 기본 생성자로 만들고 -> 세터로 하나씩 채운다
@RequestBody    -> Jackson 이 만든다. 기본 생성자 + 세터, 또는 생성자
```

<br/>

`@ModelAttribute` 는 세터가 없으면 값이 안 들어간다.

```java
class ItemForm {
    private String name;      // 게터만 있고 세터가 없다면
    public String getName() { return name; }
}
```

<br/>

에러도 안 나고 `name` 만 `null` 로 남는다.

앞의 form 태그 글에서 `키가 안 맞으면 조용히 넘어간다` 고 한 것과 같은 모양이다.

<br/>

그래서 불변 DTO를 만들고 싶으면 `@ModelAttribute` 로는 어렵다.

`final` 필드에는 세터를 만들 수 없기 때문이다.

<br/>

`@RequestBody` 는 사정이 좀 낫다.

Jackson이 생성자로 값을 받게 할 수 있어서, `final` 필드를 가진 불변 객체를 만들 수 있다.

```java
public class ItemRequest {
    private final String name;
    private final int price;

    @JsonCreator
    public ItemRequest(@JsonProperty("name") String name,
                       @JsonProperty("price") int price) {
        this.name = name;
        this.price = price;
    }
}
```

<br/>

## 검증이 실패하는 시점도 다르다

이 차이가 예외 처리 방식을 갈라놓는다.

```java
@ModelAttribute -> 타입이 안 맞으면 BindingResult 에 오류를 담고 계속 진행한다
@RequestBody    -> 변환에 실패하면 컨트롤러가 아예 호출되지 않는다
```

<br/>

`@ModelAttribute` 는 `age` 에 글자가 들어와도 컨트롤러까지는 들어온다.

`BindingResult` 를 파라미터로 받으면 어떤 필드가 잘못됐는지 볼 수 있다.

```java
@PostMapping("/add")
public String add(@ModelAttribute ItemForm form, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
        return "items/addForm";       // 입력 화면으로 되돌린다
    }
    ...
}
```

<br/>

`@RequestBody` 는 JSON 파싱 자체가 실패하면 컨트롤러 앞에서 예외가 난다.

`HttpMessageNotReadableException` 이 나면서 `400` 이 나간다.

<br/>

이유는 목적이 다르기 때문이다.

```java
@ModelAttribute -> 사람이 폼에 입력한 것. 틀릴 수 있으니 화면에 오류를 보여줘야 한다
@RequestBody    -> 프로그램이 보낸 JSON. 형식이 틀렸으면 그건 버그다
```

<br/>

## 셋 다 생략할 수 있다

```java
public String search(String keyword)          // @RequestParam 생략
public String add(ItemForm form)              // @ModelAttribute 생략
```

<br/>

스프링이 타입을 보고 알아서 정한다.

```java
String, int 같은 단순 타입   -> @RequestParam 으로 본다
그 외 객체 타입             -> @ModelAttribute 로 본다
```

<br/>

`@RequestBody` 만은 생략할 수 없다.

객체 타입이면 기본이 `@ModelAttribute` 라서, 본문을 읽으라고 명시해줘야 하기 때문이다.

<br/>

생략하면 짧아지지만 읽는 사람이 헷갈린다.

```java
public String add(ItemForm form)                     // 본문? 파라미터? 헷갈린다
public String add(@ModelAttribute ItemForm form)     // 명확하다
```

적어두는 편이 낫다.
