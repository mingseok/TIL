## @RequestParam(), @PathVariable

`요청 파라미터`란? → `GET`에 쿼리스트링 오는 것, or `POST`방식으로 

HTML `Form 태그` 데이터 전송 하는 방식을 말하는 것이다. 

<br/>

둘 경우에만 `@RequestParam` 이랑 `@ModelAttribute` 를 사용하는 것이다.

그 외 경우들은 전부 `@RequestBody`를 사용하거나, 데이터를 직접 꺼내야 되는 것이다.

```
요청 파라미터 vs HTTP 메시지 바디
요청 파라미터를 조회 : @RequestParam , @ModelAttribute
HTTP 메시지 바디를 직접 조회 : @RequestBody
```

<br/>

```java
--머리에 삽입--
@ModelAttribute 는 HTTP 요청 파라미터(URL 쿼리 스트링, POST Form)를 다룰 때 사용한다.

@RequestBody 는 HTTP Body의 데이터를 객체로 변환할 때 사용한다. 
- 주로 API JSON 요청을 다룰 때 사용
```



<br/><br/>

## `@RequestParam()` : 쿼리 파라미터 방식이다. (=요청 파라미터)

```java
@GetMapping("hello-mvc")
public String helloMvc(@RequestParam("aaa") String name, Model model) {
    model.addAttribute("name", name); // 키, 벨류
    return "hello-template";
}
```

코드의 `@RequestParam("aaa")` 가 뭘까?



<br/>

![이미지](/programming/img/입문2.PNG)

URL 그림의 `?aaa=` 를 잘 보자





<br/>

`url`로 부터 `?aaa=` 하여 ‘=’ 뒤에 값을 입력 해주면 (`민석!!!!!!`) 입력값이 

매개변수인 `@RequestParam("aaa")` 뒤에 있는 `String name` 에 저장되어 `model`에 담기게 되는 것이다. 

(=바인딩 이라고 부른다)



![이미지](/programming/img/입문3.PNG)

<br/>

### 그리하여 `hello-template.html` 에 출력 된다.

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

## 다른 예제)

```java
@GetMapping("hello-mvc")
public String helloMvc(@RequestParam("name") String name, Model model) {
    model.addAttribute("name", name); // 키, 벨류
    return "hello-template";
}
```


코드를 보면 `“name”` 으로 수정 되었고, `url`에 `‘스프링’` 이라고 입력하여 `‘스프링’`이 출력 되었다.


<br/>

![이미지](/programming/img/입문4.PNG)




<br/><br/>

## `@RequestParam()` 변수명을 생략할 수도 있다.

`@RequestParam("aaa")`인 -> `@RequestParam` 처럼 이름을 지정하지 않았다면 

html에서 넘어 오는 `name`인 → `“itemName”` 이 되는 것이다.

![이미지](/programming/img/입문56.PNG)

<br/>

## 사용 예제)

```java
-- 변경 전 코드 --
@ResponseBody
@RequestMapping("/request-param")
public String requestParam(@RequestParam("username") String memberName, @RequestParam("age") int memberAge) {

    log.info("username={}, age={}", memberName, memberAge);
    return "ok";
}


-- 변경 후 코드--
@ResponseBody
@RequestMapping("/request-param")
public String requestParam(@RequestParam String username, @RequestParam int age) {

    log.info("username={}, age={}", username, age);
    return "ok";
}
```

```
요청 URL : localhost:8080/request-param?username=kim&age=20

출력 : username=kim, age=20
```

<br/><br/>

## 파라미터를 전부 가져오고 싶은 경우 (‘키’ , ‘벨류’)

`Map`으로 조회하기.

```java
@ResponseBody
@RequestMapping("/request-param-map")
public String requestParamMap(@RequestParam Map<String, Object> paramMap) {
    
    log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
    return "ok";
}
```



```
요청 URL : localhost:8080/request-param-map?username=kim&age=20

출력 : username=kim, age=20
```

<br/><br/>

## 파라미터 필수 여부

- `@RequestParam.required` 파라미터 필수 여부 기본값이 파라미터 필수`(true)`이다.


- 필수 값 여부: required 대상이 없어도 동작하게 하려면 (required = false) 로 지정하면 된다



<br/>



- 파라미터에 값이 없는 경우 `defaultValue`를 사용하면 기본 값으로 적용할 수 있다.

    `defaultValue` 는 빈 문자의 경우에도 설정한 기본 값으로 적용된다.

<br/>

`defaultValue` 는 빈 문자의 경우에도 설정한 기본 값으로 적용된다.

```java
@ResponseBody
@RequestMapping("/request-param-default")
public String requestParamDefault(
	 @RequestParam(required = true, defaultValue = "guest") String username,
	 @RequestParam(required = false, defaultValue = "-1") int age) {

	 log.info("username={}, age={}", username, age);
	 return "ok";
}
```

```java

(아예, 없는 경우)
http://localhost:8080/request-param-default
출력 : username=guest, age=-1


(없는 경우)
http://localhost:8080/request-param-default?username=&age=
username=guest, age=-1


(age만 제대로 요청된 경우)
http://localhost:8080/request-param-default?username=&age=10
출력 : username=guest, age=10


(둘다 제대로 요청된 경우)
http://localhost:8080/request-param-default?username=kim&age=10
출력 : username=kim, age=10
```

<br/><br/>

## `@PathVariable` 설명

![이미지](/programming/img/겨10.PNG)

`경로 변수` 방식이라고 부르고, `PathVariable` 방식이라고도 부른다.

`@GetMapping` 주소로 넘어온 `{userId}` 는 달라지는 경로(=데이터)이다.

<br/>

그리하여 같은 이름이 있는 @PathVariable(`"userId"`) 로 바인딩 되어 `String data` 매개변수에 들어가게 되는 것이다.

```java
@GetMapping("/mapping/{userId}") // /mapping/3 이런식으로 되는 것이다.
public String mappingPath(@PathVariable("userId") String data) {
    log.info("userId={}", data);
    return "ok";
}
```

<br/>

### 위 코드를 포스트맨으로 실행하면 이렇다.

URL을 이렇게 작성하기. → localhost:8080/mapping/`minseok` 


![이미지](/programming/img/입문57.PNG)

```
콘솔창 출력 : userId=minseok
```

<br/><br/>

## URL을 다르게 입력 한다면?

![이미지](/programming/img/입문58.PNG)

```
콘솔창 출력 : userId=userA
```

<br/><br/>

## 매개변수명이 같으면 생략 가능하다.

```java
-- 변경전 코드 --
@GetMapping("/mapping/{userId}")
public String mappingPath(@PathVariable("userId") String userId) {
    log.info("userId={}", data);
    return "ok";
}


-- 변경후 코드 --
@GetMapping("/mapping/{userId}")
public String mappingPath(@PathVariable String userId) {
    log.info("mappingPath userId={}", userId);
    return "ok";
}
```

이렇게 매개변수명까지 같다면 생략이 가능한 것이다.

<br/><br/>

## PathVariable 다중 사용

```java
@GetMapping("/mapping/users/{userId}/orders/{orderId}")
public String mappingPath(@PathVariable String userId, @PathVariable Long orderId) {
    log.info("mappingPath userId={}, orderId={}", userId, orderId);
    return "ok";
}
```

<br/>

이렇게 실행 시키면 되는 것이다.

![이미지](/programming/img/입문59.PNG)

```
콘솔창 출력 : mappingPath userId=userA, orderId=100
```


<br/><br/>


>**Reference** <br/>[스프링 핵심 원리 - 기본편](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8?utm_source=google&utm_medium=cpc&utm_campaign=04.general_backend&utm_content=spring&utm_term=%EC%8A%A4%ED%94%84%EB%A7%81%20%EC%9E%85%EB%AC%B8&gclid=CjwKCAiAjPyfBhBMEiwAB2CCImohok2YrQ2tRdhqfr3cZvKqkIJOHUJ36u6s1-7C9X1gzZIapTvOtxoCangQAvD_BwE)

<br/>

## 궁금증!

```java
@RequestParam 을 생략하면 어떻게 이름을 아나
```

컴파일된 클래스 파일에 파라미터 이름이 남아 있어야 한다.

```java
public String find(String keyword)      // 이름을 "keyword" 로 알아야 한다
```

<br/>

원래 자바는 컴파일할 때 파라미터 이름을 지운다.

`arg0`, `arg1` 로만 남는다.

<br/>

`-parameters` 옵션을 주면 이름이 보존된다.

```java
$ javac -parameters Foo.java
```

<br/>

스프링 부트는 이 옵션을 기본으로 켜준다.

그래서 이름을 안 적어도 동작하는 것이다.

<br/>

옵션이 꺼져 있으면 이런 오류가 난다.

```java
Name for argument of type [java.lang.String] not specified,
and parameter name information not available via reflection.
```

<br/>

앞의 리플렉션(Reflection) 글에서 본 그 리플렉션으로 이름을 읽는 것이라,

클래스 파일에 이름이 없으면 방법이 없는 것이다.

<br/>

## required 의 기본값이 다르다

```java
@RequestParam String keyword          // required = true. 없으면 400
@RequestParam(required = false) String keyword    // 없으면 null
@PathVariable Long id                 // 항상 필수. 경로에 없으면 매핑 자체가 안 된다
```

<br/>

`@PathVariable` 은 URL의 일부라서 없을 수가 없다.

없으면 다른 URL이 되는 것이고, 그럼 이 메서드로 안 온다.

<br/>

## required = false 에 int 를 쓰면 터진다

```java
@RequestParam(required = false) int age
```

```java
IllegalStateException: Optional int parameter 'age' is present but cannot be
translated into a null value
```

<br/>

값이 없으면 `null` 을 넣어야 하는데 `int` 에는 `null` 을 못 넣기 때문이다.

<br/>

앞의 원시형과 참조형 글에서 본 그 차이다.

```java
int      - null 이 될 수 없다
Integer  - null 이 될 수 있다
```

<br/>

`Integer` 로 바꾸거나 기본값을 주면 된다.

```java
@RequestParam(defaultValue = "0") int age
```

<br/>

`defaultValue` 를 주면 `required` 는 의미가 없어진다. 항상 값이 있기 때문이다.

<br/>

## defaultValue 는 빈 문자열도 잡는다

```java
@RequestParam(defaultValue = "guest") String name
```

```java
/members            -> name = "guest"    (파라미터 자체가 없다)
/members?name=      -> name = "guest"    (빈 문자열도 기본값으로 바뀐다)
```

<br/>

`required = false` 와 다른 점이다.

```java
required = false    -> /members?name= 이면 "" 가 들어온다
defaultValue        -> "" 도 기본값으로 바꾼다
```

<br/>

## Map 으로 전부 받을 수도 있다

```java
@RequestParam Map<String, String> params
```

<br/>

같은 키가 여러 번 오면 첫 번째만 들어온다.

```java
/members?tag=a&tag=b     ->  params.get("tag") = "a"
```

<br/>

전부 받으려면 `MultiValueMap` 을 쓴다.

```java
@RequestParam MultiValueMap<String, String> params
->  params.get("tag") = ["a", "b"]
```

<br/>

리스트로 받는 것도 된다.

```java
@RequestParam List<String> tag       ->  ["a", "b"]
```

<br/>

앞의 스프링 타입 컨버터 글에서 본 대로

`"a,b"` 처럼 콤마로 온 것도 배열로 바꿔준다.

<br/>

## @PathVariable 의 정규식

경로 변수에 조건을 걸 수 있다.

```java
@GetMapping("/members/{id:[0-9]+}")
public Member find(@PathVariable Long id) { ... }
```

<br/>

숫자만 매칭한다. `/members/abc` 는 이 메서드로 안 온다.

<br/>

이걸 쓰면 같은 자리에 다른 의미를 둘 수 있다.

```java
@GetMapping("/members/{id:[0-9]+}")     // /members/1
@GetMapping("/members/new")             // /members/new
```

<br/>

정규식이 없어도 스프링은 더 구체적인 것을 먼저 고른다.

`{id}` 같은 변수보다 `new` 같은 고정 문자열이 우선이다.

<br/>

그래도 정규식을 적어두면 의도가 드러나고, 잘못된 요청이 `404` 로 걸러진다.

컨트롤러 안에서 숫자인지 검사하는 코드를 안 써도 되는 것이다.
