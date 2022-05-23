## 제어의 역전 IoC(Inversion of Control)

- 프로그램에 대한 제어 흐름에 대한 권한은 모두 `AppConfig`가 가지고 있다. 
심지어 `OrderServiceImpl`도 `AppConfig`가 생성한다.

- 그리고 `AppConfig`는 `OrderServiceImpl` 이 아닌 `OrderService`인터페이스의 
다른 구현 객체를 생성하고 실행할 수 도 있다.
- 그런 사실도 모른체 `OrderServiceImpl` 은 묵묵히 자신의 로직을 실행할 뿐이다.

<br/>


프로그램의 제어 흐름(ex:메소드나 객체의 호출작업)을 개발자가 결정하는 것이 아니라, 외부에서 결정(관리)하는 것.

즉 객체를 개발자가 Member member = new Member(); 이런식으로 만드는 것이 아니라, 

스프링이 스스로 객체를 생성해서, 필요한 곳에 사용할 수 있게 해줌!

```java
AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
```

<br/>

### 위 코드 단 하나만 실행했을 뿐인데, 
우리는 ac.getBean을 통해, 원하는 객체를 사용할 수 있음!!!!

<br/>

## 참고로 DI도 제어의 역전에 포함되는 기술이며, IoC는 좀더 광범위하게 쓰인다!

### 스프링은 IoC, DI와 같은 기술을 사용함으로써

자바만으로는 DIP(의존관계 역전 원칙)와 OCP(계방-폐쇠 원칙)을 지켜가며 

객체지향적으로 설계하는데 어려움이 있었는데, 이를 해결.

<br/>즉 스프링을 사용하면 좋은 객체 지향 애플리케이션을 개발하기 편해지며, 

스프링은 이를 도와주는 프레임워크.

스프링이 좋은 객체 지향 애플리케이션을 개발하는데 도움을 주는 프레임워크인 것 처럼, 

스프링 부트는 스프링의 기술들을 좀 더 편리하게 사용하는데 도움을 주는 프레임워크.
























<br/>

## 정적인 클래스 의존관계

클래스가 사용하는 import 코드만 보고 의존관계를 쉽게 판단할 수 있다. 

정적인 의존관계는애플리케이션을 실행하지 않아도 분석할 수 있다. 

<br/>클래스 다이어그램을 보자.

![이미지](/programming/img/스프링6.PNG)

<br/>`OrderServiceImpl`  클래스를 보면 “상위 클래스에 `OrderService` 있구나” 알 수 있다.

<br/>그리고 `OrderServiceImpl` 클래스는 `MemberRepository`와 `DiscountPolicy` 를 참조하고 있다.

`FixDiscountPolicy` 와 `RateDiscountPolicy` 는 `DiscountPolicy` 를 의존 하고 있는 것이다.

`DiscountPolicy`  인터페이스는 아무곳도 의존하지 않는다.

<br/>

## 동적인 객체 인스턴스 의존 관계

애플리케이션 실행 시점에 실제 생성된 객체 인스턴스의 참조가 연결된 의존 관계다.

`OrderServiceImpl` 은 `MemberRepository`, `DiscountPolicy` 에 의존한다는 것을 알 수 있다.

그런데 이러한 클래스 의존관계 만으로는 실제 어떤 객체가 

`OrderServiceImpl` 에 주입 될지 알 수 없는 것을 동적인 객체 인스턴스 의존 관계라고 한다.

<br/>

## Config를 IoC 컨테이너, DI 컨테이너라고도 부름

- AppConfig 처럼 객체를 생성하고 관리하면서 의존관계를 연결해 주는 것을 IoC 컨테이너 또는 DI 컨테이너라 한다.
    
- 의존관계 주입에 초점을 맞추어 최근에는 주로 DI 컨테이너라 한다. 또는 어샘블러, 오브젝트 팩토리 등으로 불리기도 한다


<br/>


>**Reference** <br/>스프링 핵심 원리 - 기본편 : https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8
