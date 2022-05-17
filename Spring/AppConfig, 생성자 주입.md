## 관심사의 분리 1/3

- 배우는 본인의 역할인 배역을 수행하는 것에만 집중해야 한다.
- 디카프리오는 어떤 여자 주인공이 선택 되더라도 똑같이 공연을 할 수 있어야 한다.
- 공연을 구성하고, 담당 배우를 섭외하고, 역할에 맞는 배우를 지정하는 책임을 담당하는 별도의 공연 기획자가 나올 시점이다.
- 공연 기획자를 만들고, 배우와 공연 기획자의 책임을 확실히 분리하자.

<br/>

## AppConfig 등장

애플리케이션의 전체 동작 방식을 구성(config)하기 위해, 구현 객체를 생성하고,

연결하는 책임을 가지는 별도의 설정 클래스를 만들자.

<br/>

### `AppConfig` 클래스

설명하자면, (`MemberServiceImpl` 만들고) <br/>“내가 만든 `MemberServiceImpl` 은 `MemoryMemberRepository` 를 사용할거야” 하고, 딱 주입 시켜주는 것이다.

```java
package hello.core;

// 나의 애플리케이션 전체를 설정하고 구성한다는 뜻이다.
public class AppConfig {

    public MemberService memberService() {
        return new MemberServiceImpl(new MemoryMemberRepository());
    }
}
```

<br/>

### `MemberServiceImpl` 클래스

생성자를 통해서 들어가게 하는 것이다.

이렇게 하면 철저하게 **DIP 를 지키고 있는 것이다. 인터페이스에만 의존 하는 것이다.**

추가 설명 : final 로 되어 있으면 무조건 생성자를 통해서 할당 되어야 한다.

![이미지](/programming/img/스프링1.PNG)

이렇게 하면 현재 `MemberServiceImpl` 클래스에는 `MemoryMemberRepository` 클래스가 없다. 

즉, `MemberRepository` 인터페이스에만 의존하는 것이 된다.

<br/>

### :pushpin: 이 부분을 ‘생성자 주입’ 이라고 부른다.


<br/><br/>

## AppConfig 리팩터링 2/3


`AppConfig` 클래스를 보면 역할과 구현 클래스가 한눈에 들어온다. 

<br/>

애플리케이션 전체 구성이 어떻게 되어 있는지 빠르게 파악할 수 있다.

<br/>

### 흐름 설명.

“현재 `MemberService`에 대한 구현은 `MemberServiceImpl`을 쓸거야.” 

그리고 “내 애플리케이션에 `memberRepository` 에 대한 것은 

`MemoryMemberRepository`를 사용할거야“ 한 눈에 알 수 있다

<br/>

또 다른것은 `OrderService`에 대한 구현은 `OrderServiceImpl` 쓸거고,

현재 내 애플리케이션에 쓰는 `memberRepository`를 가져 온다는 건 

즉, `MemoryMemberRepository` 사용하겠단 말이다. 

그리고 내 애플리케이션에 `discountPolicy` 사용한다는 건 `FixDiscountPolicy` 를 

사용한다는 말이다.


<br/>

## 예시

역할이란 걸 한눈에 알 수 있고

```java
public DiscountPolicy discountPolicy() { // 할인부분 인터페이스이다.
```

구현이란 걸 한눈에 알 수 있다.

```java
return new FixDiscountPolicy(); // 할일부분 인터페이스 오버라이딩한 클래스이다.
```

이렇게 보면 `discountPolicy` 는 `FixDiscountPolicy` 쓰는구나 한눈에 알 수 있다.

역할을 만들고 구현이 안에 들어가도록.

![이미지](/programming/img/스프링2.PNG)


<br/>

![이미지](/programming/img/스프링3.PNG)


<br/><br/>

```java
package hello.core;


// 나의 애플리케이션 전체를 설정하고 구성한다는 뜻이다.
public class AppConfig {

    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    public MemoryMemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    public DiscountPolicy discountPolicy() {
        return new FixDiscountPolicy();
    }

}
```

<br/><br/>

## 스프링으로 전환하기 3/3

### `AppConfig` 클래스

```java
package hello.core;

// 나의 애플리케이션 전체를 설정하고 구성한다는 뜻이다.
// 설정 정보
@Configuration
public class AppConfig {

    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public DiscountPolicy discountPolicy() {

     // return new FixDiscountPolicy();
        return new RateDiscountPolicy();

    }

}
```

<br/>

### `MemberApp` 클래스

주석 잘보기.

```java
package hello.core;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MemberApp {

    public static void main(String[] args) {
        
        ApplicationContext applicationContext
                = new AnnotationConfigApplicationContext(AppConfig.class);

		// 기본적으로 빈에 등록이 될때 AppConfig 클래스에 있는 메서드 이름으로 등록이 된다.
        // 즉, 앞에 "memberService" 부분은 메서드 이름을 말하는 것이다. 

		// MemberService.class 는 그냥 타입이다. 
		// 무슨타입? "memberService" 꺼내기 위한 타입 설정이라고 생각하면 된다.

		// 결록적으로는 '키' 는 "memberService" 가 되는 것이고,
		// '벨류' 는 memberService() 메서드 안에 있는
		// new MemberServiceImpl(memberRepository()); 가 되어 스프링 컨테이너에 등록 된다.
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);
        
        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);

        Member findMember = memberService.findMember(1L); // '키' 를 넘겨준것이다.

        System.out.println("new member = " + member.getName());
        System.out.println("find Member = " + findMember.getName());

    }

}
```

출력시켜보면 이렇게 스프링 컨테이너에 등록 되는 걸 알 수 있다.


### ApplicationContext 를 스프링 컨테이너라고 한다.

- 스프링 컨테이너는 @Configuration 이 붙은 AppConfig 를 설정(구성) 정보로 사용한다.
    
    여기서 @Bean이라 적힌 메서드를 모두 호출해서 반환된 객체를 스프링 컨테이너에 등록한다. 
    
    이렇게 스프링 컨테이너에 등록된 객체를 스프링 빈이라 한다
    
- 스프링 빈은 @Bean 이 붙은 메서드의 명을 스프링 빈의 이름으로 사용한다.
    
    ( memberService , orderService )
    
- 이전에는 개발자가 필요한 객체를 AppConfig 를 사용해서 직접 조회했지만,
    
    이제부터는 스프링 컨테이너를 통해서 필요한 스프링 빈(객체)를 찾아야 한다. 
    
    스프링 빈은 applicationContext.getBean() 메서드를 사용해서 찾을 수 있다.
    
- 기존에는 개발자가 직접 자바코드로 모든 것을 했다면
    
     이제부터는 스프링 컨테이너에 객체를 스프링 빈으로 등록하고, 스프링 컨테이너에서
    
     스프링 빈을 찾아서 사용하도록 변경되었다.


<br/>


>**Reference** <br/>스프링 핵심 원리 - 기본편 : https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8
