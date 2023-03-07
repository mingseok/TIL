## @Primary, 빈 생명주기 콜백 @PostConstruct, @PreDestroy

<br/>

## `@Primary` 는 우선순위를 정하는 방법이다. 

`@Autowired` 시에 여러 빈이 매칭되면 `@Primary` 가 우선권을 가진다.

`RateDiscountPolicy.class` 가 우선권을 가진다.

```java
@Component
@Primary
public class RateDiscountPolicy implements DiscountPolicy {}

@Component
public class FixDiscountPolicy implements DiscountPolicy {}
```

<br/><br/>

## 빈 생명주기 콜백 애노테이션 @PostConstruct, @PreDestroy

이름만 봐도 느낌이 온다. `PostConstruct`는 → 생성이 된 이후에,

그리고 `PreDestroy`는 → 소멸 되기 전에!

<br/>

코드부터 보면 이렇다.

```java
public class NetworkClient {
    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //서비스 시작시 호출
    public void connect() {
        System.out.println("connect: " + url);
    }

    public void call(String message) {
        System.out.println("call: " + url + " message = " + message);
    }

    //서비스 종료시 호출
    public void disConnect() {
        System.out.println("close + " + url);
    }

    @PostConstruct
    public void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메시지");
    }

    @PreDestroy
    public void close() {
        System.out.println("NetworkClient.close");
        disConnect();
    }

    @Configuration
    static class LifeCycleConfig {
        
        @Bean
        public NetworkClient networkClient() {
            NetworkClient networkClient = new NetworkClient();
            networkClient.setUrl("http://hello-spring.dev");
            return networkClient;
        }
    }
}
```

<br/>

### 실행 결과

```
생성자 호출, url = null
NetworkClient.init
connect: http://hello-spring.dev
call: http://hello-spring.dev message = 초기화 연결 메시지
19:40:50.269 [main] DEBUG
org.springframework.context.annotation.AnnotationConfigApplicationContext -
Closing NetworkClient.close
close + http://hello-spring.dev
```

`@PostConstruct` , `@PreDestroy` 이 두 애노테이션을 사용하면 가장 편리하게 

초기화와 종료를 실행할 수 있다.

<br/><br/>

## @PostConstruct, @PreDestroy 애노테이션 특징

최신 스프링에서 가장 권장하는 방법이다.

스프링에 종속적인 기술이 아니라 JSR-250 라는 자바 표준이다. 

<br/>

따라서 스프링이 아닌 다른 컨테이너에서도 동작한다. 컴포넌트 스캔과 잘 어울린다.

유일한 단점은 외부 라이브러리에는 적용하지 못한다는 것이다. 

외부 라이브러리를 초기화, 종료 해야 하면 `@Bean`의 기능을 사용하자.

<br/><br/>

## 정리

`@PostConstruct`, `@PreDestroy` 애노테이션을 사용하자

코드를 고칠 수 없는 외부 라이브러리를 초기화, 종료해야 하면 

@Bean 의 initMethod , destroyMethod를 사용하자.

<br/><br/>


>**Reference** <br/>[스프링 핵심 원리 - 기본편](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8?utm_source=google&utm_medium=cpc&utm_campaign=04.general_backend&utm_content=spring&utm_term=%EC%8A%A4%ED%94%84%EB%A7%81%20%EC%9E%85%EB%AC%B8&gclid=CjwKCAiAjPyfBhBMEiwAB2CCImohok2YrQ2tRdhqfr3cZvKqkIJOHUJ36u6s1-7C9X1gzZIapTvOtxoCangQAvD_BwE)