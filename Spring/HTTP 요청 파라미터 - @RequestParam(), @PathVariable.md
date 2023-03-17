## @RequestParam(), @PathVariable

`요청 파라미터`란? → `GET`에 쿼리스트링 오는 것, or `POST`방식으로 

HTML `폼 태그` 데이터 전송 하는 방식을 말하는 것이다. 

<br/>

둘 경우에만 `@RequestParam` 이랑 `@ModelAttribute` 를 사용하는 것이다.

그 외 경우들은 전부 `@RequestBody`를 사용하거나, 데이터를 직접 꺼내야 되는 것이다.

```
요청 파라미터 vs HTTP 메시지 바디
요청 파라미터를 조회하는 기능: @RequestParam , @ModelAttribute
HTTP 메시지 바디를 직접 조회하는 기능: @RequestBody
```

<br/><br/>

## `@RequestParam()` : 쿼리 파라미터 방식이다. (=요청 파라미터)

밑에 있는 코드의 `@RequestParam("aaa")` 가 뭘까?

```java
@GetMapping("hello-mvc")
public String helloMvc(@RequestParam("aaa") String name, Model model) {
    model.addAttribute("name", name); // 키, 벨류
    return "hello-template";
}
```

<br/>

### 밑에 있는 URL 그림의 `?aaa=` 를 잘 보자



![이미지](/programming/img/입문2.PNG)

<br/>

사진 처럼 `url`로 부터 `?aaa=` 하여 ‘=’ 뒤에 값을 입력 해주면 (`’민석!!!!!!'`) 입력값이 

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

코드를 보면 `“name”` 으로 수정 되었고, `url`에 `‘스프링’` 이라고 입력하며 `‘스프링’`이 출력 되었다.

```java
@GetMapping("hello-mvc")
public String helloMvc(@RequestParam("name") String name, Model model) {
    model.addAttribute("name", name); // 키, 벨류
    return "hello-template";
}
```

<br/>

![이미지](/programming/img/입문4.PNG)

<br/><br/>

## `@RequestParam()` 변수명을 생략할 수도 있다.

@RequestParam("aaa")인 `(“aaa”)` 처럼 이름을 지정하지 않았다면 

html에서 넘어 오는 name인 → `“itemName”` 이 되는 것이다.

![이미지](/programming/img/입문56.PNG)

<br/>

## 사용 예제)

```java
-- 변경 전 코드 --
@ResponseBody
@RequestMapping("/request-param")
public String requestParam(
        @RequestParam("username") String memberName,
        @RequestParam("age") int memberAge) {

    log.info("username={}, age={}", memberName, memberAge);
    return "ok";
}

-- 변경 후 코드--
@ResponseBody
@RequestMapping("/request-param")
public String requestParam(
        @RequestParam String username,
        @RequestParam int age) {

    log.info("username={}, age={}", username, age);
    return "ok";
}
```

```
http://localhost:8080/request-param?username=kim&age=20

출력 : username=kim, age=20
```

<br/><br/>

## 파라미터를 ‘키’ , ‘벨류’ 전부 가져오고 싶은 경우

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
http://localhost:8080/request-param-map?username=kim&age=20

출력 : username=kim, age=20
```

<br/><br/>

## 파라미터 필수 여부

`@RequestParam.required` 파라미터 필수 여부 기본값이 파라미터 필수`(true)`이다.

파라미터에 값이 없는 경우 `defaultValue`를 사용하면 기본 값으로 적용할 수 있다.

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
http://localhost:8080/request-param-default
출력 : username=guest, age=-1

http://localhost:8080/request-param-default?username=&age=
username=guest, age=-1

http://localhost:8080/request-param-default?username=&age=10
출력 : username=guest, age=10

http://localhost:8080/request-param-default?username=kim&age=10
출력 : username=kim, age=10
```

<br/><br/>

## `@PathVariable` 설명

![이미지](/programming/img/겨10.PNG)

`경로 변수` 방식이라고 부르고, `PathVariable` 방식이라고도 부른다.

`@GetMapping` 주소로 넘어온 `{userId}` 는 달라지는 경로(=데이터)이다.

<br/>

그리하여 같은 이름이 있는 @PathVariable(`"userId"`) 로 바인딩 되어 

`String data` 매개변수에 들어가게 되는 것이다.

```java
@GetMapping("/mapping/{userId}") // /mapping/3 이런식으로 되는 것이다.
public String mappingPath(@PathVariable("userId") String data) {
    log.info("userId={}", data);
    return "ok";
}
```

<br/>

위 코드를 포스트맨으로 실행하면 이렇다.

URL을 이렇게 작성하기. → http://localhost:8080/mapping/`minseok` 

<br/>

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
