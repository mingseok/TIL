## `@Controller` 설명

스프링이 처음에 실행 될 때 (스프링 컨테이너라고 부른다.)

스프링 컨테이너라는 `‘통’` 이 생긴다.

<br/>

거기서 `@Controller` 어너테이션이 달려 있는 `HelloController.class` 등등 .. 

객체를 생성해서 스프링 컨테이너란 ‘통’에 넣어 둔다.

그리고 스프링이 관리 하는 것이다. 



```
`이것을 스프링 컨테이너에서 '스프링 빈'을 관리 한다고 부른다`
```

<br/>


### 스프링 빈을 등록할 때, 유일하게 하나만 등록한다. (싱글톤)

따라서 같은 스프링 빈이면 모두 같은 인스턴스인 것이다.

<br/><br/>

## @Controller 정리

- 스프링이 자동으로 스프링 빈으로 등록한다.

    - 내부에 @Component 애노테이션이 있어서 컴포넌트 스캔의 대상이 됨

- 스프링 MVC에서 애노테이션 기반 컨트롤러로 인식한다.

<br/><br/>

## `@RestController` 설명

쉽게 생각하면, `@Controller` 랑 `@ResponseBody` 합친 것이 바로 `@RestController` 컨트롤러인 것이다.

```java
@RestController
public class LogTestController {

    @RequestMapping("/log-test")
    public String logTest() {
        return "ok";
    }
}

```

- 스프링은 기본적으로 @Controller 라고 하면, 반환 되는 것은 뷰 이름이 반환 된다.

<br/>

즉, 위 코드 처럼 `@RestController` 사용하면 리턴 타입인 String이므로 문자가 반환 된다.

```html
따라서 실행 화면에서 바로 'ok'를 확인 할 수 있다.
```


<br/><br/>


>**Reference** <br/>[스프링 핵심 원리 - 기본편](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8?utm_source=google&utm_medium=cpc&utm_campaign=04.general_backend&utm_content=spring&utm_term=%EC%8A%A4%ED%94%84%EB%A7%81%20%EC%9E%85%EB%AC%B8&gclid=CjwKCAiAjPyfBhBMEiwAB2CCImohok2YrQ2tRdhqfr3cZvKqkIJOHUJ36u6s1-7C9X1gzZIapTvOtxoCangQAvD_BwE)
