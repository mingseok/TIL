## 제어의 역전 IoC(Inversion of Control)

- 프로그램에 대한 제어 흐름에 대한 권한은 모두 `AppConfig`가 가지고 있다. 
심지어 `OrderServiceImpl`도 `AppConfig`가 생성한다.

- 그리고 `AppConfig`는 `OrderServiceImpl` 이 아닌 `OrderService`인터페이스의 
다른 구현 객체를 생성하고 실행할 수 도 있다.
- 그런 사실도 모른체 `OrderServiceImpl` 은 묵묵히 자신의 로직을 실행할 뿐이다.

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