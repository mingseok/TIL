## @Configuration 안에서 @Bean이 빈으로 등록되는 과정

다음과 같은 클래스가 있고, 이를 스프링 컨테이너에 등록하고자 한다고 하자.

```java
public class MinSeok {

}
```

위 클래스를 빈으로 등록하기 위해서는 명시적으로 설정 클래스에서 

`@Bean` 어노테이션을 사용해 수동으로 스프링 컨테이너에 빈을 등록하는 방법이 있다. 

<br/>

설정 클래스는 `@Configuration` 어노테이션을 클래스에 붙여주면 되는데, 

`@Bean`을 사용해 `수동`으로 빈을 등록해줄 때에는 `메소드 이름으로 빈 이름이 결정`된다. 

<br/>

그러므로 `중복`된 빈 이름이 존재하지 않도록 주의해야 한다.

```java
@Configuration
public class Config {

    @Bean
    public MinSeok minSeok() {
        return new MinSeok();
    }
}
```

<br/><br/>

## `@Configuration` 안에서 `@Bean`이 빈으로 등록되는 과정

스프링 컨테이너는 `@Configuration`이 붙어있는 클래스를 자동으로 빈으로 등록해두고, 

해당 클래스를 `파싱`해서 `@Bean`이 있는 메소드를 찾아서 빈을 `생성`해준다.

```java
"아무 클래스나 만들어서 @Bean 어노테이션 붙인다고 되는게 아니다."
```



`@Bean`을 사용하는 클래스에는 반드시 `@Configuration` 어노테이션을 활용하여 

해당 클래스에서 `Bean`을 등록하고자 함을 명시해주어야 한다.

<br/>

### 하지만

`@Configuration`안에서 `@Bean`을 사용해야 싱글톤을 보장받을 수 있으므로 

`@Bean` 어노테이션은 반드시 `@Configuration`과 함께 사용해주어야 한다.

<br/>

### 이유는? 링크 참고

```java
@Bean과 @Configuration 함께 사용해야 되는 이유는?
-> 링크 : [https://www.notion.so/Bean-Configuration-6a7fb68f5a134e0f8dbc1ce47e0d2047](https://www.notion.so/Bean-Configuration-6a7fb68f5a134e0f8dbc1ce47e0d2047?pvs=21)
```

<br/><br/>

## @Component 어노테이션

수동으로 직접 빈을 등록하는 작업은 빈으로 등록하는 클래스가 

많아질수록 상당히 많은 시간을 차지할 것이고, 생산력 저하를 야기할 것이다. 

<br/>

그래서 스프링에서는 특정 어노테이션이 있는 클래스를 찾아서 

빈으로 등록해주는 컴포넌트 스캔 기능을 제공한다.

<br/>

스프링은 `컴포넌트 스캔(Component Scan)`을 사용해 `@Component` 어노테이션이 있는 

클래스들을 찾아서 자동으로 빈 등록을 해준다. 

<br/>

그래서 우리가 직접 개발한 클래스를 빈으로 편리하게 등록하고자 

하는 경우에는 `@Component` 어노테이션을 활용하면 된다.

```java
public class MinSeok {

}
```

<br/><br/>

## 포인트는

`@Configuration` 안에 있는 `@Component`에 의해 설정 클래스 역시 
자동으로 빈으로 등록이 되고, 

그래서 `@Bean`이 있는 메소드를 통해 
빈을 등록해줄 수 있었던 것이다.

```java
"스프링은 기본적으로 컴포넌트 스캔을 이용한 자동 빈 등록 방식을 권장한다."
```
<br/>

## 궁금증!

```java
"해당 클래스를 파싱해서 @Bean 이 있는 메소드를 찾는다" 는 게 언제 일어날까?
```

객체를 만들기 훨씬 전, `설계도만 모으는 단계` 에서 일어난다.

앞의 BeanDefinition 글에서 본 그 순서에 끼워보면 이렇다.

```java
1. @Configuration 클래스를 BeanDefinition 으로 등록한다
2. ConfigurationClassPostProcessor 가 그 클래스를 읽는다     <- 여기서 파싱한다
     - @Bean 메서드를 찾아서 BeanDefinition 을 추가로 등록한다
     - @ComponentScan 이 있으면 스캔해서 또 추가로 등록한다
     - @Import 가 있으면 그 클래스도 읽는다
3. 모아둔 BeanDefinition 을 보고 실제 객체를 만든다
```

<br/>

`2번` 을 하는 것이 `ConfigurationClassPostProcessor` 다.

이름 끝의 `PostProcessor` 가 `BeanFactoryPostProcessor` 를 뜻한다.

`BeanDefinition 을 다 모은 뒤, 객체를 만들기 전에` 끼어드는 자리다.

<br/>

## 그래서 순서가 이렇게 된다

```java
@Configuration
public class Config {

    @Bean
    public MinSeok minSeok() {
        return new MinSeok();
    }
}
```

<br/>

```java
1. Config 라는 BeanDefinition 이 등록된다 (아직 Config 객체도 없다)
2. Config 클래스를 읽어서 "minSeok 이라는 BeanDefinition" 을 추가 등록한다
     - 클래스는 null, 팩토리메서드는 "minSeok", 팩토리빈은 "config"
3. 객체를 만들 차례가 되면
     - 먼저 Config 를 만든다 (CGLIB 프록시로)
     - 그 프록시의 minSeok() 을 불러서 MinSeok 을 만든다
```

<br/>

`@Bean` 메서드가 곧바로 실행되는 것이 아니라, `나중에 부를 메서드로 등록만 되는` 것이다.

앞의 빈 등록 글에서 `payService` 의 `getBeanClassName()` 이 `null` 이고

`getFactoryMethodName()` 이 채워져 있던 것이 이 결과물이다.

<br/>

## 왜 반드시 @Configuration 이어야 하는가

원문의 결론이 맞지만, 정확히는 `@Bean 만으로도 등록은 된다`.

<br/>

`@Component` 가 붙은 클래스 안에 `@Bean` 을 써도 빈은 만들어진다.

이걸 `Lite 모드` 라고 부르는데, 프록시를 안 만든다는 것이 다르다.

```java
@Configuration 있음 (Full 모드) -> CGLIB 프록시. @Bean 끼리 불러도 싱글톤 유지
@Configuration 없음 (Lite 모드) -> 프록시 없음. @Bean 끼리 부르면 매번 새로 만들어진다
```

<br/>

앞의 글에서 실제로 세어본 결과가 `1회` 대 `3회` 였다.

<br/>

## 빈 이름이 겹치면

원문의 `중복된 빈 이름이 존재하지 않도록 주의해야 한다` 는 부분도 확인해볼 만하다.

```java
@Configuration
public class Config {

    @Bean
    public MinSeok minSeok() { return new MinSeok(); }
}

@Configuration
public class AnotherConfig {

    @Bean
    public MinSeok minSeok() { return new MinSeok(); }   // 이름이 같다
}
```

<br/>

스프링 부트는 기본적으로 이런 상황을 막는다.

```java
BeanDefinitionOverrideException:
Invalid bean definition with name 'minSeok' ... There is already ... bound
```

<br/>

옛날에는 나중에 등록된 것이 조용히 덮어썼다.

의도한 덮어쓰기면 괜찮지만, 실수로 이름이 겹친 경우에는 찾기가 아주 어려웠다.

그래서 부트 `2.1` 부터 기본적으로 막고, 필요하면 설정으로 열게 바뀌었다.

```java
spring.main.allow-bean-definition-overriding=true
```

<br/>

이름을 굳이 다르게 두고 싶으면 이렇게 준다.

```java
@Bean(name = "adminMinSeok")
```

<br/>

## 같은 타입 빈이 여러 개일 때

이름이 다르면 등록은 되는데, 주입할 때 문제가 생긴다.

```java
@Bean MinSeok minSeokA() { ... }
@Bean MinSeok minSeokB() { ... }

@Service
class MyService {
    MyService(MinSeok minSeok) { ... }     // 둘 중 어느 것?
}
```

<br/>

```java
NoUniqueBeanDefinitionException: expected single matching bean but found 2
```

<br/>

해결 방법이 세 가지 있다.

```java
@Primary                      - 여러 개 중 기본으로 쓸 것 하나를 정한다
@Qualifier("minSeokA")        - 주입받는 쪽에서 이름을 지정한다
파라미터 이름을 빈 이름과 맞춘다 - MinSeok minSeokA 라고 적으면 이름으로 찾아준다
```

<br/>

세 번째는 컴파일 옵션에 따라 파라미터 이름이 안 남을 수도 있어서, 앞의 두 개가 더 확실하다.
