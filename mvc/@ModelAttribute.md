## HTTP 요청 파라미터 - @ModelAttribute

### 실제로 개발하면

1. 요청 파라미터를 받는다.
2. 객체를 만든다. 
3. 객체에 값을 넣어준다.

```java
// 요청 파라미터를 받는다.
@RequestParam String username;
@RequestParam int age;

// 객체를 만든다. 
HelloData data = new HelloData();

// 객체에 값을 넣어준다.
data.setUsername(username);
data.setAge(age);
```

<br/>

### 스프링은 이 과정을 완전히 자동화해주는 @ModelAttribute 기능을 제공한다

`HelloData` 클래스 생성하기.

```java
package hello.springmvc.basic;

import lombok.Data;

@Data
public class HelloData {
  
    private String username;
    private int age;
    
}
```

<br/>

### 롬복 

@Data
@Getter , @Setter , @ToString , @EqualsAndHashCode , @RequiredArgsConstructor 를
자동으로 적용해준다.


이 코드를 

```java
@ResponseBody
@RequestMapping("/model-attribute-v1")
public String modelAttributeV1(@RequestParam String username, @RequestParam int age) {
    HelloData helloData = new HelloData();
    helloData.setUsername(username);
    helloData.setAge(age);

    log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

    return "ok";
}
```

<br/>이렇게 줄일 수가 있다.

```java
@ResponseBody
@RequestMapping("/model-attribute-v1")
public String modelAttributeV1(@ModelAttribute HelloData helloData) {
     log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
       
     return "ok";
}
```

<br/>

### 그리고 `@ModelAttribute` 생략이 가능하다.

```java
@ResponseBody
@RequestMapping("/model-attribute-v1")
public String modelAttributeV1(HelloData helloData) {
     log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
     return "ok";
}
```

### 마치 마법처럼 HelloData 객체가 생성되고, 요청 파라미터의 값도 모두 들어가 있다.

스프링MVC는 @ModelAttribute 가 있으면 다음을 실행한다.

1. HelloData 객체를 생성한다.

2. 요청 파라미터의 이름으로 HelloData 객체의 프로퍼티를 찾는다. 
3. 그리고 해당 프로퍼티의 setter를 호출해서 파라미터의 값을 입력(바인딩) 한다.

예) 파라미터 이름이 `username` 이면 `setUsername()` 메서드를 찾아서 호출하면서 값을 입력한다.

<br/>

### 프로퍼티

즉, 무슨말이냐면 

getXxx 첫 글자가 대문자 에서 → get을 빼고 첫 글자를 소문자로 바꾸고 xxx 가 된다.

setXxx 첫 글자가 대문자 에서 → set을 빼고 첫 글자를 소문자로 바꾸고 xxx 가 된다.

객체에 getUsername() , setUsername() 메서드가 있으면, 이 객체는 username 이라는 프로퍼티를 가지고 있다.

username 프로퍼티의 값을 변경하면 setUsername() 이 호출되고, 조회하면 getUsername() 이 호출된다.

<br/>


## `@ModelAttribute` 는 중요한 한가지 기능이 더 있는데, 

바로 `모델(Model)`에 `@ModelAttribute` 로 지정한 객체를 자동으로 넣어준다. 

지금 코드를 보면 `model.addAttribute("item", item)` 가 주석처리 되어 있어도 잘 동작하는 것을 확인할 수 있다.

<br/>


### 모델에 데이터를 담을 때는 이름이 필요하다.

이름은 `@ModelAttribute` 에 지정한 `name(value)` 속성을 사용한다. 

만약 다음과 같이 `@ModelAttribute` 의 이름을 다르게 지정하면 다른 이름으로 모델에 포함된다.

<br/>

`@ModelAttribute("hello") Item item` 이름을 hello 로 지정

`model.addAttribute("hello", item);` 모델에 hello 이름으로 저장

<br/>이름도 생략이 가능하다.

### addItemV3 - 상품 등록 처리 - ModelAttribute 이름 생략

- `@ModelAttribute name` 생략 가능
- `model.addAttribute(item);` 자동 추가, 생략 가능
- 생략시 model에 저장되는 name은 클래스명 첫글자만 소문자로 등록 Item -> item
    
    ```java
    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
    
    	itemRepository.save(item);
    
    	return "basic/item";
    }
    ```
 <br/>   
    
### 주의
    
`@ModelAttribute` 의 이름을 생략하면 모델에 저장될 때 클래스명을 사용한다. 
    
이때 클래스의 첫글자만 소문자로 변경해서 등록한다.

<br/>
    
예) `@ModelAttribute` 클래스명 모델에 자동 추가되는 이름 <br/>
Item → item <br/>
HelloWorld → helloWorld


<br/>

>**Reference** <br/>스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1
