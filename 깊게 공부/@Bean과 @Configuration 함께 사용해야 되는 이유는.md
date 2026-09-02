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
<br/>

## 궁금증!

```java
"싱글톤이 보장된다" 는 것을 숫자로 확인해보자
```

같은 설정 클래스를 `@Configuration` 이 있는 것과 없는 것으로 두 벌 만들고,

생성자에 카운터를 넣어서 몇 번 만들어지는지 셌다.

```java
@Configuration
class WithConfiguration {
    @Bean Repo repo() { return new Repo(); }
    @Bean ServiceA serviceA() { return new ServiceA(repo()); }
    @Bean ServiceB serviceB() { return new ServiceB(repo()); }
}

class WithoutConfiguration {          // @Configuration 만 뗐다
    @Bean Repo repo() { return new Repo(); }
    @Bean ServiceA serviceA() { return new ServiceA(repo()); }
    @Bean ServiceB serviceB() { return new ServiceB(repo()); }
}
```

<br/>

### 결과

```java
--- @Configuration 있음 ---
  설정 클래스 = WithConfiguration$$SpringCGLIB$$0
  A가 받은 repo == B가 받은 repo : true
  repo() 가 실제로 실행된 횟수 : 1

--- @Configuration 없음 ---
  설정 클래스 = WithoutConfiguration
  A가 받은 repo == B가 받은 repo : false
  repo() 가 실제로 실행된 횟수 : 3
```

<br/>

`@Configuration` 이 없으면 `repo()` 가 세 번 실행되고 객체도 세 개 만들어진다.

자바 코드로만 보면 이게 당연하다. 메서드를 세 번 불렀으니 세 번 도는 것이다.

<br/>

`@Configuration` 이 있으면 한 번만 실행된다.

그리고 설정 클래스의 이름이 `WithConfiguration$$SpringCGLIB$$0` 으로 바뀌어 있다.

<br/>

## 그 프록시가 하는 일

원문에서 말한 `CGLib 으로 프록시 패턴을 적용해` 가 이 클래스다.

내 설정 클래스를 상속받은 자식을 만들어서, `@Bean` 메서드를 전부 오버라이딩한다.

```java
class WithConfiguration$$SpringCGLIB$$0 extends WithConfiguration {

    @Override
    Repo repo() {
        if (컨테이너에 "repo" 라는 빈이 이미 있으면) {
            return 그것을 꺼내서 반환;         // 부모 메서드를 아예 실행하지 않는다
        }
        Repo created = super.repo();          // 없을 때만 진짜로 만든다
        컨테이너에 등록;
        return created;
    }
}
```

<br/>

대략 이런 모양이다. 실제 코드는 더 복잡하지만 핵심은 `이미 있으면 그걸 준다` 이다.

<br/>

`serviceA()` 안에서 `repo()` 를 부르면, 그 호출도 프록시의 `repo()` 로 간다.

`this.repo()` 인데도 프록시를 거치는 이유는, `this` 자체가 프록시 객체이기 때문이다.

<br/>

앞의 AOP 글에서 본 `내부 호출은 프록시를 안 거친다` 와 반대 상황이라 헷갈리기 쉽다.

```java
AOP 프록시  -> 원본 객체를 감싼다. 원본 안의 this 는 원본이라 프록시를 안 거친다
@Configuration 프록시 -> 설정 클래스를 상속한다. 컨테이너에 등록된 것이 프록시 자신이라
                        메서드 안의 this 도 프록시다
```

<br/>

`감싸기(위임)` 와 `상속` 의 차이가 여기서 나온다.

<br/>

## @Configuration 없이도 @Bean 은 동작한다

`@Configuration` 이 없어도 빈은 등록된다. 위 결과에서 `ServiceA`, `ServiceB` 둘 다 만들어졌다.

싱글톤이 깨질 뿐이다. 이 상태를 `Lite 모드` 라고 부른다.

```java
Full 모드 (@Configuration 있음) - CGLIB 프록시를 만든다. @Bean 끼리 호출해도 싱글톤 보장
Lite 모드 (@Configuration 없음) - 프록시를 안 만든다. 메서드 호출이 그냥 자바 호출
```

<br/>

`Lite 모드` 는 프록시를 안 만드니 시작이 살짝 빠르다.

`@Bean` 메서드끼리 서로 부르지 않는다면 문제가 없기도 하다.

<br/>

그래도 `@Configuration` 을 붙이는 편이 안전하다.

지금은 서로 안 부르더라도, 나중에 누가 한 줄 추가하는 순간 조용히 깨지기 때문이다.

<br/>

## 서로 부르는 대신 파라미터로 받는 방법도 있다

`@Bean` 메서드끼리 직접 부르지 않고 파라미터로 받으면 프록시가 필요 없어진다.

```java
@Bean
MyFirstBean myFirstBean(MinSeok minSeok) {    // 부르지 않고 받는다
    return new MyFirstBean(minSeok);
}
```

<br/>

이러면 스프링이 `minSeok` 빈을 찾아서 넣어준다.

컨테이너를 거치니까 당연히 같은 객체가 들어온다.

<br/>

원문의 예제도 이렇게 바꿔 쓸 수 있다.

```java
@Configuration
public class BeanConfiguration {

    @Bean
    public MinSeok minSeok() {
        return new MinSeok();
    }

    @Bean
    public MyFirstBean myFirstBean(MinSeok minSeok) {
        return new MyFirstBean(minSeok);
    }

    @Bean
    public SecondBean secondBean(MinSeok minSeok) {
        return new SecondBean(minSeok);
    }
}
```

<br/>

`minSeok()` 을 직접 부르지 않으니 프록시에 의존하지 않는다.

의존관계가 파라미터에 드러나서 읽기도 더 낫다.

```java
메서드 직접 호출 -> @Configuration 의 프록시에 의존한다
파라미터로 받기  -> 컨테이너가 주입해준다. 프록시가 없어도 안전하다
```
