## 요청 매핑, @PathVariable

http://localhost:8080/hello-basic 를 실행 시켜보면 OK가 출력되는 걸 알 수 있다.

“이, `"/hello-basic"` url이 오면 이 `helloBasic()` 메서드가 호출된다” 생각하면 된다.

<br/>

### 매핑은 배열식으로도 할 수 있다

 `@RequestMapping({"/hello-basic", “/hello-go”})`  이렇게 여러개도 가능하다.

```java
package hello.springmvc.basic.requestmapping;

@RestController
public class MappingController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/hello-basic")
    public String helloBasic() {
        log.info("helloBasic");
        return "OK";
    }

    // 이렇게도 가능하다. 어차피 적지 않아도 @RequestMapping 에 들어가 보면
    // method = RequestMethod.GET 선언 되어 있다.
    @RequestMapping(value = "/hello-get-v1", method = RequestMethod.GET)
    public String mappingGetV1() {
        log.info("mappingGetV1");
        return "OK";
    }

}
```

<br/>

## 콘솔에도 log 방식으로 잘된다.

![이미지](/programming/img/서44.PNG)

<br/>log.info("helloBasic"); 를 `error` 로 바꾸면 이렇게 출력 되는 것이다.

![이미지](/programming/img/서45.PNG)

<br/>

### 요즘 선언하는 방식. (많이 사용한다.)

패스베리어블 쓰는 방식, 경로변수 방식 둘다 부른다.

1. 이렇게 작성하고 실행 시켜보면
    
    ![이미지](/programming/img/서46.PNG)
    

1. 이렇게 출력 되는 걸 알 수 있다.

![이미지](/programming/img/서47.PNG)

<br/>
<br/>


## @PathVariable 이란?

<br/>

![이미지](/programming/img/겨10.PNG)

<br/>

```java
@GetMapping("/test/{cat}")
public String methodName(@PathVariable int cat){
  

  return "test";
}
```

위와 같이 코드를 작성하면,

"localhost:8080/test/3"과 같이 호출했을 때 cat 값이 3이 된다.


하지만, 만약에 "localhost:8080/test"와 같이 변수를 넣어주지 않으면 에러가 발생한다.


<br/>


### 밑에 코드에서의 1-1 과 1-2 의 차이점

- `@RequestMapping` 은 URL 경로를 템플릿화 할 수 있는데, `@PathVariable` 을 사용하면
    
    매칭 되는 부분을 편리하게 조회할 수 있다.
    
- `@PathVariable` 의 이름과 `파라미터` 이름이 같으면 생략할 수 있다.
- 생략은 가능하지만,

```java
package hello.springmvc.basic.requestmapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class MappingController {

    private Logger log = LoggerFactory.getLogger(getClass());
    
    // 요즘 유행하는 스타일 1-1
    @GetMapping("/mapping/{userId}")
    public String mappingPath1(@PathVariable("userId") String data) {
        log.info("mappingPath userId={}", data);
        return "ok";
    }

    // @PathVariable 의 이름과 파라미터 이름이 같으면 생략할 수 있다.
    // 요즘 유행하는 스타일 1-2
    @GetMapping("/mapping/{userId}")
    public String mappingPath2(@PathVariable String userId) {
        log.info("mappingPath userId={}", userId);
        return "ok";
    }

}
```

<br/>

### PathVariable 사용 - 다중

localhost:8080/mapping/users/A/orders/1234 이렇게 해야 된다.

<br/>

위에 이걸 사용 할때는 방식이 있다.

localhost:8080/mapping/users/{A}/orders/{1234} 이렇게 하면 에러 발생.

즉, `/mapping/users/{userId}/orders/{orderId}` 이걸 그대로 복사하면 안된다는 말이다.

```java
package hello.springmvc.basic.requestmapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class MappingController {

    private Logger log = LoggerFactory.getLogger(getClass());

    // PathVariable 사용 - 다중
    // http://localhost:8080/mapping/users/A/orders/1234 이런식으로 해야됨 
    @GetMapping("/mapping/users/{userId}/orders/{orderId}") // 이걸 그대로 복사하면 에러가 발생한다.
    public String mappingPath(@PathVariable String userId, @PathVariable Long orderId) {
        log.info("mappingPath userId={}, orderId={}", userId, orderId);
        return "ok";
    }

}
```

<br/>실행 시켜보면 이렇다.

localhost:8080/mapping/users/userA/orders/100

![이미지](/programming/img/서48.PNG)

<br/>

>**Reference** <br/>스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1
<br/>
매트의 개발 블로그 - https://kkangdda.tistory.com/36