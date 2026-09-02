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

밑에 결과 잘보기.

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

`@PostConstruct` , `@PreDestroy` 이 두 애노테이션을 사용하면 가장 편리하게 초기화와 종료를 실행할 수 있다.

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
<br/>

## 궁금증!

```java
초기화를 생성자에서 하면 안 되나
```

안 되는 이유가 있다.

```java
@Component
public class NetworkClient {

    private String url;

    public NetworkClient() {
        connect(url);        // 이 시점에 url 은 아직 null 이다
    }

    @Value("${my.url}")
    public void setUrl(String url) { this.url = url; }
}
```

<br/>

객체를 먼저 만들고 값을 나중에 넣는 순서라, 생성자에서는 값이 없다.

<br/>

앞의 클래스 멤버 변수 초기화 순서 글에서 본 대로

자바 자체의 초기화 순서와, 스프링이 값을 채우는 순서는 별개인 것이다.

```java
1. 생성자 실행           <- 여기서는 주입이 아직 안 끝났다
2. 의존관계 주입 (setter, 필드)
3. @PostConstruct
```

<br/>

그래서 `주입이 끝난 뒤에 할 일` 을 위한 자리가 따로 필요한 것이다.

<br/>

## 생성자 주입이면 어떻게 되나

앞의 생성자 주입을 선택 글에서 본 대로,

생성자 주입은 만들 때 값이 다 들어온다.

```java
public NetworkClient(String url) {
    this.url = url;
    connect(url);        // 이건 된다
}
```

<br/>

그래도 생성자에서 무거운 일을 하는 것은 피하는 게 낫다.

```java
객체를 만드는 것과, 그 객체가 일을 시작하는 것은 다른 얘기다
```

<br/>

생성자에서 네트워크에 붙으면 테스트에서 `new` 하는 것만으로 접속이 일어난다.

<br/>

## 실제 호출 순서를 찍어봤다

앞의 빈 스코프 글에서 만든 것과 같은 방식으로 확인했다.

```java
  [singleton] init 호출              <- 컨테이너가 뜰 때
--- 컨테이너 종료 ---
  [singleton] destroy 호출
```

<br/>

`@PostConstruct` 는 컨테이너가 뜰 때, `@PreDestroy` 는 닫을 때 불린다.

<br/>

프로토타입 빈은 `destroy` 가 안 불린다는 것도 그 글에서 확인했다.

<br/>

## 콜백을 등록하는 방법이 세 가지다

```java
1. @PostConstruct / @PreDestroy
2. InitializingBean / DisposableBean 인터페이스
3. @Bean(initMethod = "init", destroyMethod = "close")
```

<br/>

`2번` 은 스프링 인터페이스를 구현해야 해서 안 쓴다.

<br/>

앞의 PSA(Portable Service Abstraction) 글에서 본 `POJO` 가 아니게 되는 것이다.

<br/>

`3번` 은 외부 라이브러리 클래스에 쓴다. 내가 못 고치는 코드라 어노테이션을 못 붙이기 때문이다.

```java
@Bean(destroyMethod = "close")
public DataSource dataSource() { ... }
```

<br/>

사실 `destroyMethod` 는 기본값이 `(inferred)` 라,

`close` 나 `shutdown` 이라는 이름의 메서드가 있으면 알아서 부른다.

<br/>

## @Primary 와 @Qualifier

같은 타입의 빈이 둘이면 주입할 때 막힌다.

```java
NoUniqueBeanDefinitionException: expected single matching bean but found 2
```

<br/>

해결하는 방법이 세 가지다.

```java
1. @Primary        - 기본으로 쓸 것을 정한다
2. @Qualifier      - 이름을 지정해서 고른다
3. 필드 이름 맞추기 - 파라미터 이름과 빈 이름을 같게 한다
```

<br/>

우선순위는 좁은 것이 이긴다.

```java
@Qualifier > @Primary
```

<br/>

앞의 @Autowired, DI, @Component 글에서 본 그 순서다.

<br/>

실무에서는 이렇게 나눠 쓴다.

```java
@Primary   - 주로 쓰는 것에 붙인다
@Qualifier - 가끔 필요한 특수한 것에만 붙인다
```

<br/>

`@Qualifier` 를 여기저기 붙이면 문자열이 흩어져서 오타가 나기 쉽다.

<br/>

## 어노테이션을 직접 만들면 낫다

```java
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier("mainDiscountPolicy")
public @interface MainDiscountPolicy {
}
```

```java
public OrderService(@MainDiscountPolicy DiscountPolicy policy) { ... }
```

<br/>

앞의 @Controller, @RestController 글에서 본 메타 어노테이션 방식이다.

<br/>

문자열이 한 곳에만 있으니 오타가 나도 컴파일할 때 잡히고,

`이 어노테이션을 어디서 쓰나` 를 IDE로 찾을 수 있게 된다.
