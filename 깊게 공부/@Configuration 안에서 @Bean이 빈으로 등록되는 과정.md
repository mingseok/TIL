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