## API 방식(json) , @ResponseBody

@ResponseBody는 두가지 기능이 있다.

<br/>

## 첫번째. http 바디 부분에 내가 직접 넣는 방법.

view 페이지를 통해서 화면에 출력 시켜주는게 아닌, 

컨트롤러 클래스에서 내가 직접 `return` 값으로 화면에 출력 시키고 싶은 경우 

메서드에 `@ResponseBody`어너테이션을 붙여주면 된다.

<br/>

즉, return 값인 `‘helloworld'` 라는 문자열을 그대로 반환하고 싶다면, 

해당 메소드에 `@ResponseBody` 어너테이션을 추가하면 되는 것이다.

```java
@GetMapping("hello-string")
@ResponseBody
public String heeloString(@RequestParam("name") String name) {
    return "hello " + name; // 출력하면 "hello spring 이런 식으로 출력된다. 
}
```

<br/>

![이미지](/programming/img/입문5.PNG)


<br/><br/>

## 두번째. API (json)방식

일단 json은 ‘키’, ‘벨류’ 형태로 이루어진 구조이다.

<br/>

### 객체를 `@ResponseBody` 로 `return` 한다면 어떻게 될까?

코드에서의 `hello` 를 출력 시키면 어떻게 될까?

```java
@Controller
public class HelloController {

    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name);
        return hello;
    }

    // static을 사용하면 클래스 내부에서 클래스 생성가능
    static class Hello { 
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
```

<br/>

‘키’는 `Hello 클래스`의 `name` 이 되는 것이고, 벨류는 내가 입력한 `스프링!!!!`이 되는 것이다.

그리하여 json 방식이란 이런 것이다.

![이미지](/programming/img/입문6.PNG)

<br/><br/>

![이미지](/programming/img/입문7.PNG)


<br/><br/>


>**Reference** <br/>[스프링 핵심 원리 - 기본편](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8?utm_source=google&utm_medium=cpc&utm_campaign=04.general_backend&utm_content=spring&utm_term=%EC%8A%A4%ED%94%84%EB%A7%81%20%EC%9E%85%EB%AC%B8&gclid=CjwKCAiAjPyfBhBMEiwAB2CCImohok2YrQ2tRdhqfr3cZvKqkIJOHUJ36u6s1-7C9X1gzZIapTvOtxoCangQAvD_BwE)
