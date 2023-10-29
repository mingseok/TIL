## @Bean과 @Configuration 함께 사용해야 되는 이유는?



`@Configuration` 어노테이션 안에는 `@Component` 어노테이션이 붙어있어서 

`@Configuration`이 붙어있는 클래스 역시 스프링의 빈으로 등록이 된다. 

<br/>

그럼에도 불구하고 스프링이 `@Configuration`을 따로 만든 이유는 

`CGLib`으로 프록시 패턴을 적용해 수동으로 등록하는 스프링 빈이 반드시 

싱글톤으로 생성됨을 보장하기 위해서이다.

<br/>

### 예를 들어

스프링 빈으로 등록하고자 하는 클래스가 있다고 하자.

```java
public class MinSeok{

}
```

<br/>

위의 클래스를 `@Component`를 이용해 자동으로 빈 등록을 한다면 
스프링이 해당 클래스의 

객체의 생성을 제어하게 되고 
`1개`의 객체만 생성되도록 컨트롤할 수 있다. 

<br/>

### 하지만

위의 클래스를 `@Bean`으로 사용해 직접 빈으로 등록해준다고 하자. 

그러면 우리는 다음과 같이 해당 빈 등록 메소드를 여러 번 호출할 수 있게 된다.

```java
@Configuration
public class BeanConfiguration { 

    @Bean 
    public MinSeok minSeok() {
        return new MinSeok(); 
    } 
    
    @Bean 
    public MyFirstBean myFirstBean() { 
        return new MyFirstBean(minSeok()); 
    } 
    
    @Bean 
    public SecondBean secondBean() { 
        return new SecondBean(minSeok()); 
    } 

    @Bean 
    public ThirdBean thirdBean() { 
        return new ThirdBean(minSeok()); 
    } 
}
```

실수로 위와 같이 빈을 생성하는 메소드를 여러 번 호출하였다면 불필요하게 여러 개의 빈이 생성이 된다. 

<br/>

스프링은 이러한 문제를 방지하고자 `@Configuration`이 있는 

클래스를 객체로 생성할 때 `CGLib` 라이브러리를 사용해 프록시 패턴을 적용한다. 

<br/>

그래서 `@Bean`이 있는 메소드를 여러 번 호출하여도 항상 

동일한 객체를 반환하여 싱글톤을 보장한다.

```java
@Configuration
public class BeanConfiguration { 

    @Bean 
    public MinSeok minSeok() {
        return new MinSeok(); 
    } 
    
    @Bean 
    public MyFirstBean myFirstBean() { 
        return new MyFirstBean(minSeok()); 
    } 
    
    @Bean 
    public SecondBean secondBean() { 
        return new SecondBean(minSeok()); 
    } 
}

@Configuration
public class BeanConfigurationProxy extends BeanConfiguration { 

    private Object source;

    @Override
    public MinSeok minSeok() {
        if (minSeok == null) {
            this.source = super.minSeok();
        }
        return source; 
    }
    
    @Override
    public FirstBean firstBean() { 
        return super.firstBean();
    } 
    
    @Override
    public SecondBean secondBean() { 
        return super.secondBean();
    } 
}
```

<br/>

`CGLib`은 상속을 사용해서 프록시를 구현하므로 다음과 

같이 프록시가 구현된다고 이해할 수 있다. 

```java
"이해를 돕기 위한 코드로만 생각하면 된다."
```