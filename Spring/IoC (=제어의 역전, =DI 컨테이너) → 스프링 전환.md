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

```
OrderServiceImpl 자체를 생성해주는 것도 AppConfig가 하고,
OrderServiceImpl에 어떤게 주입이 되서 전체의 애플리케이션 흐름을 잡는것 또한 
AppConfig가 전부 다 하는 것이다. (이렇게 의존관계 역전을 일으킨 것이다.)
```

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

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        return new RateDiscountPolicy();
    }
}
```

<br/>

### 기존과 동일한 결과가 출력된다.

```java
public class MemberApp {
    public static void main(String[] args) {

        // AppConfig appConfig = new AppConfig();
        // MemberService memberService = appConfig.memberService();

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService getMemberService = applicationContext.getBean("memberService", MemberService.class);

        Member member = new Member(1L, "memberA", Grade.VIP);
        getMemberService.join(member);
        Member findMember = getMemberService.findMember(1L);

        System.out.println("new member = " + member.getName());
        System.out.println("find Member = " + findMember.getName());
    }
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


>**Reference** <br/>[스프링 핵심 원리 - 기본편](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8?utm_source=google&utm_medium=cpc&utm_campaign=04.general_backend&utm_content=spring&utm_term=%EC%8A%A4%ED%94%84%EB%A7%81%20%EC%9E%85%EB%AC%B8&gclid=CjwKCAiAjPyfBhBMEiwAB2CCImohok2YrQ2tRdhqfr3cZvKqkIJOHUJ36u6s1-7C9X1gzZIapTvOtxoCangQAvD_BwE)
