## IoC (=제어의 역전, =DI 컨테이너) → 스프링 전환

## 제어의 역전 개념은

```
“내가 뭔가를 호출하는 것이 아닌, 프레임워크 같은것이 대신 호출해 주는 것이다”
```

프로그램의 제어 흐름을 직접 제어하는 것이 아닌, 외부에서 관리하는 것.

<br/><br/>

## 기준은 뭘까?

- 프레임워크가 내가 작성한 코드를 제어하고, 대신 실행하면 그것은 프레임워크가 맞다.

    - 설명으로는 JUnit이 있다 → 실행하고 제어권은 JUnit이 가져간다.

- 반면 내가 작성한 코드가 직접 제어의 흐름을 담당한다면 그것은 라이브러리다.

<br/><br/>

## DI의 장점.

의존관계 주입을 사용하면 정적인 클래스 의존관계를 변경하지 않고, 

동적인 객체 인스턴스 의존관계를 쉽게 변경할 수 있다.

- 애플리케이션 코드를 손대지 않는다는 것이다.

<br/><br/>

## IoC, 제어의 역전 용어.


OrderServiceImpl 자체를 생성해주는 것도 AppConfig가 하고,

OrderServiceImpl에 어떤게 주입이 되서 전체의 애플리케이션 흐름을 잡는것 또한 

AppConfig가 전부 다 하는 것이다. (이렇게 의존관계 역전을 일으킨 것이다.)

<br/>

그리하여 이걸 IoC 컨테이너라고도 부르고 최근에는 `DI 컨테이너` 라고 많이 부른다.

<br/><br/>

## 지금까지를, 스프링으로 전환

지금까지 순수한 자바 코드만으로 DI를 적용했다. 이제 스프링을 사용해보자.

`AppConfig.class` 설정을 구성한다는 뜻의 `@Configuration` 을 붙여준다.

<br/>

그리고 각 메서드에 `@Bean` 을 붙여준다.

이렇게 하면 스프링 컨테이너에 스프링 빈으로 등록한다.

```java

@Configuration
public class AppConfig {
    
    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    // ... 생략
}
```


<br/><br/>

## 스프링 컨테이너

`ApplicationContext` 를 스프링 컨테이너라 한다.

- 기존에는 개발자가 `AppConfig` 를 사용해서 직접 객체를 생성하고 `DI`를 했지만, 

    이제부터는 스프링 컨테이너에 객체를 스프링 빈으로 등록하고,

    스프링 컨테이너에서 스프링 빈을 찾아서 사용하도록 변경되었다.

- 스프링 컨테이너는 `@Configuration` 이 붙은 `AppConfig` 를 `설정`(구성) 정보로 사용한다. 

    여기서 `@Bean`이라 적힌 메서드를 모두 호출해서 반환된 객체를 스프링 컨테이너에 등록한다. 
    
    이렇게 스프링 컨테이너에 등록된 객체를 스프링 빈이라 한다.
- 스프링 빈은 `applicationContext.getBean()` 메서드를 사용해서 찾을 수 있다.


<br/><br/>

## @Configuration

### 내가 ‘직접 스프링 빈을 등록 할거다’ 라고 생각하면 된다.

<br/>

이렇게 하면 스프링이 실행 될때 `@Configuration` 어너테이션을 보고 

`@Bean` 달려 있는 것들을 모두 컨테이너에 등록하게 된다.

![이미지](/programming/img/입문15.PNG)

<br/>

등록된 스프링 빈들은 해당 클래스의 생성자 주입 되는 것이다.






<br/><br/>


>**Reference** <br/>[스프링 핵심 원리 - 기본편](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8?utm_source=google&utm_medium=cpc&utm_campaign=04.general_backend&utm_content=spring&utm_term=%EC%8A%A4%ED%94%84%EB%A7%81%20%EC%9E%85%EB%AC%B8&gclid=CjwKCAiAjPyfBhBMEiwAB2CCImohok2YrQ2tRdhqfr3cZvKqkIJOHUJ36u6s1-7C9X1gzZIapTvOtxoCangQAvD_BwE)

<br/>

## 궁금증!

```java
AppConfig 를 @Configuration 으로 바꾸면 무엇이 달라지나
```

같은 빈을 두 번 호출해봤다.

```java
@Configuration
class Config {
    @Bean A a() { return new A(b()); }
    @Bean B b() { return new B(); }        // a() 안에서도 b() 를 부른다
}
```

<br/>

자바 코드만 보면 `b()` 가 두 번 실행되어야 한다.

`Config` 를 빈으로 등록할 때 한 번, `a()` 안에서 한 번이다.

<br/>

실제로 찍어보니 아니었다. 앞의 @Configuration과 싱글톤 글에서 확인한 그것이다.

```java
Config 클래스 = class demo.Config$$SpringCGLIB$$0
b() 호출 횟수 = 1
```

<br/>

클래스 이름에 `SpringCGLIB` 가 붙어 있다.

내가 만든 `Config` 가 아니라 스프링이 상속해서 만든 자식 클래스가 등록된 것이다.

<br/>

## 그 자식 클래스가 하는 일

`@Bean` 메서드를 전부 오버라이딩해서 이렇게 바꾼다.

```java
@Bean
public B b() {
    if (컨테이너에 이미 있으면) {
        return 그것;
    }
    return super.b();       // 없으면 원래 메서드를 부르고 컨테이너에 넣는다
}
```

<br/>

앞의 상속, 오버라이딩 글에서 본 그 오버라이딩을 런타임에 하는 것이다.

<br/>

그래서 `@Bean` 메서드를 몇 번 부르든 같은 객체가 온다.

<br/>

## @Configuration 을 빼면

`@Bean` 만 남기고 `@Configuration` 을 지우면 CGLIB 처리를 안 한다.

```java
b() 호출 횟수 = 2
```

<br/>

`a()` 안의 `b()` 가 그냥 자바 메서드 호출이 되어버린다.

<br/>

싱글톤이 깨지는 것이라, 상태를 가진 빈이면 문제가 된다.

앞의 싱글톤 패턴, 싱글톤 방식의 주의점 글에서 본 그 상황이 생긴다.

<br/>

## 그럼 @Configuration 없이 쓰는 경우가 있나

의도적으로 그렇게 쓰기도 한다. `Lite 모드` 라고 부른다.

```java
@Component
public class Config {
    @Bean
    public B b() { return new B(); }
}
```

<br/>

CGLIB 프록시를 안 만드니 기동이 조금 빠르다.

대신 `@Bean` 메서드끼리 서로 부르면 안 된다.

<br/>

명시적으로 끌 수도 있다.

```java
@Configuration(proxyBeanMethods = false)
```

<br/>

스프링 부트 내부의 자동 설정 클래스들이 이 옵션을 많이 쓴다.

기동 시간을 줄이려는 것이다.

<br/>

## CGLIB 를 쓰니 제약이 생긴다

앞의 AOP 프록시 글에서 본 것과 같은 제약이다.

```java
@Configuration 클래스는 final 이면 안 된다   (상속을 못 한다)
@Bean 메서드도 final 이면 안 된다             (오버라이딩을 못 한다)
private 메서드에 @Bean 을 붙이면 안 된다
```

<br/>

`final` 을 붙이면 이런 오류가 난다.

```java
@Configuration class 'Config' may not be final. Remove the final marker
```

<br/>

앞의 final 키워드 글에서 본 대로 `final` 클래스는 상속이 막힌다.

프레임워크가 상속으로 동작하는 곳에서는 `final` 이 걸림돌이 되는 것이다.

<br/>

## 제어의 역전이라는 말

무엇이 뒤집힌 것인지 다시 정리하면 이렇다.

```java
원래  - 내 코드가 필요한 것을 만들고 부른다
IoC   - 프레임워크가 내 코드를 만들고 부른다
```

<br/>

`main` 에서 시작해서 아래로 내려가는 흐름이 아니라,

프레임워크가 위에서 내 코드를 꺼내 쓰는 흐름이 된다.

<br/>

앞의 스프링 MVC 구조 글에서 본 것도 같은 모양이다.

```java
내가 DispatcherServlet 을 부르는 게 아니라
DispatcherServlet 이 내 컨트롤러를 부른다
```

<br/>

라이브러리와 프레임워크의 차이가 여기서 갈린다.

```java
라이브러리   - 내가 부른다
프레임워크   - 내가 불린다
```
