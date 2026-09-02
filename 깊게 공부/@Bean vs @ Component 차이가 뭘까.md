## @Bean vs @ Component 차이가 뭘까?



스프링 공부하다 `Bean`과 `@Component` 를 마주쳤다. 

```java
"똑같은거 아냐?"
```

<br>

결국, 두개는 빈을 등록할 때 사용하는 어노테이션인데, 

이 둘에 대해서, 자세히 어떻게 다른지에 대해 궁금하여 정리한다.

<br><br>

## @Bean

우리가 직접 제어할 수 없는 클래스들이 있다.

- 외부 라이브러리를 통해서 가져다가 사용하는 클래스에 대해서는
    
    `Bean`을 등록하기 위해 `@Bean`을 사용한다. (`Config` 클래스의 `@Config` 어노테이션도 같이)
    
- `@Bean`의 경우에는 메소드 레벨에서만 선언 가능하다는 점이다.

<br><br>

## @Component

개발자가 컨트롤 가능한 클래스에 대해서 `Bean`을 등록할 때 사용한다. 

- 우리가 직접 만든 자바 클래스에 대해서는 `@Component`를 사용한다.
- `@Component`는 클래스 레벨에서 선언되어야 한다.
- 스프링의 컴포넌트 스캔 기능이 `@Component` 어노테이션이 있는
    
    클래스를 자동으로 찾아서 빈으로 등록함
    
- `@Component` 하위 어노테이션으로 `@Controller`, `@Service`, `@Repository` 등이 있다.
<br/>

## 궁금증!

```java
등록되는 결과물은 똑같은 빈인데, 정말 아무 차이도 없을까?
```

컨테이너 안에 저장되는 `설계도` 를 꺼내보면 확실히 다르다.

```java
BeanDefinition def  = context.getBeanDefinition("orderService");  // @Service 로 등록
BeanDefinition def2 = context.getBeanDefinition("payService");    // @Bean 으로 등록

System.out.println("orderService 클래스       = " + def.getBeanClassName());
System.out.println("orderService 팩토리메서드 = " + def.getFactoryMethodName());
System.out.println("payService   클래스       = " + def2.getBeanClassName());
System.out.println("payService   팩토리메서드 = " + def2.getFactoryMethodName());
```

<br/>

### 결과

```java
orderService 클래스       = demo.OrderService
orderService 팩토리메서드 = null

payService   클래스       = null
payService   팩토리메서드 = payService
```

<br/>

정확히 반대다.

- `@Component` : 클래스 이름이 적혀 있다. 스프링이 그 클래스를 직접 `new` 한다

- `@Bean` : 클래스는 비어 있고 메서드 이름만 적혀 있다. 그 메서드를 불러서 반환값을 받는다

<br/>

`@Bean` 쪽 클래스가 `null` 인 이유는, 실행해봐야 무엇이 나올지 알기 때문이다.

메서드 안에서 조건에 따라 다른 구현체를 반환할 수도 있으니 미리 정해둘 수가 없다.

```java
@Bean
public PayClient payClient() {
    if (테스트환경) {
        return new FakePayClient();
    }
    return new TossPayClient();
}
```

`@Component` 로는 이런 분기를 표현할 방법이 없다.

<br/>

## @Bean은 반드시 @Configuration 안에 있어야 한다

이게 실제로 가장 크게 다른 점이다. 확인해봤다.

```java
@Configuration
class WithConfiguration {
    @Bean Repo repo() { return new Repo(); }
    @Bean ServiceA serviceA() { return new ServiceA(repo()); }
    @Bean ServiceB serviceB() { return new ServiceB(repo()); }
}

class WithoutConfiguration {        // @Configuration 을 뗐다
    @Bean Repo repo() { return new Repo(); }
    @Bean ServiceA serviceA() { return new ServiceA(repo()); }
    @Bean ServiceB serviceB() { return new ServiceB(repo()); }
}
```

<br/>

### 결과

```java
--- @Configuration 있음 ---
  A가 받은 repo == B가 받은 repo : true
  repo() 가 실제로 실행된 횟수 : 1

--- @Configuration 없음 ---
  A가 받은 repo == B가 받은 repo : false
  repo() 가 실제로 실행된 횟수 : 3
```

<br/>

`@Configuration` 이 없으면 `repo()` 가 부를 때마다 실행되어 객체가 세 개 만들어진다.

싱글톤이 깨지는 것이다.

<br/>

`@Configuration` 이 붙으면 스프링이 그 클래스를 상속한 CGLIB 자식 클래스를 만들어서

`@Bean` 메서드 호출을 가로챈다. 이미 만든 것이 있으면 그걸 돌려주기 때문에 한 번만 실행된다.

```java
@Component 는 그냥 클래스에 붙이면 끝
@Bean 은 @Configuration 이 붙은 클래스 안에 있어야 제대로 동작한다
```

<br/>

## 선언 위치가 다른 이유

원문의 `@Bean 은 메서드 레벨, @Component 는 클래스 레벨` 도 여기서 설명이 된다.

<br/>

`@Component` 는 `이 클래스를 빈으로 만들어라` 라는 뜻이라 클래스에 붙는다.

`@Bean` 은 `이 메서드가 만들어내는 것을 빈으로 등록해라` 라는 뜻이라 메서드에 붙는다.

<br/>

그래서 `@Bean` 은 반환 타입이 곧 빈의 타입이 되고, 메서드 이름이 곧 빈 이름이 된다.

<br/>

## 정리하면 이렇게 고른다

```java
내가 만든 클래스이고 그냥 new 하면 됨  -> @Component (더 짧다)
외부 라이브러리 클래스                 -> @Bean (어노테이션을 못 붙이니까)
만들면서 설정을 해줘야 함              -> @Bean
조건에 따라 다른 구현체를 넣어야 함     -> @Bean
```

<br/>

한 가지 더 있다. `@Component` 는 컴포넌트 스캔 범위 안에 있어야 찾아진다.

기본 스캔 범위는 `@SpringBootApplication` 이 붙은 클래스의 패키지와 그 하위다.

<br/>

그 밖에 있는 클래스는 `@Component` 를 붙여도 등록되지 않는다.

이럴 때도 `@Bean` 으로 직접 등록해야 한다.
