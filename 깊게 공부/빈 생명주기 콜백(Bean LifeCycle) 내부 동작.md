## 빈 생명주기 콜백(Bean LifeCycle) 내부 동작

<br/>

## 스프링 빈의 라이프사이클은 어떻게 관리 되는지?

먼저 스프링 Bean의 LifeCycle은 다음과 같습니다.

```java
1. 스프링 IoC 컨테이너 생성
2. 스프링 빈 생성
3. 의존관계 주입
   - 객체의 생성과 의존관계 주입이 동시에 일어난다
4. 초기화 콜백 메소드 호출
5. 사용
6. 소멸 전 콜백 메소드 호출
7. 스프링 종료
```

<br/>

### 스프링은 크게 3가지 방법으로 빈 생명주기 콜백을 관리합니다.

1. 인터페이스( InitializingBean, DisposableBean )

2. 설정 정보에 초기화 메소드, 종료 메소드 지정

3. @PostConstruct, @PreDestroy 어노테이션 지원

<br/>

데이터베이스 커넥션 풀이나, 네트워크 소켓처럼 애플리케이션 시작 시점에 

필요한 연결을 미리 해두고, 애플리케이션 종료 시점에 연결을 모두 종료하는 

작업을 진행하려면, `객체의 초기화와 종료 작업이 필요합니다.`



<br/>

스프링 빈도 위와 같은 원리로 `초기화 작업`과 `종료 작업`이 나눠서 진행됩니다. 

간단하게 말하면 `객체 생성 -> 의존관계 주입`이라는 `라이프사이클`을 가집니다.

```java
즉, 스프링 빈은 "의존관계 주입"이 다 끝난 다음에야 
필요한 "데이터"를 사용할 수 있는 준비가 완료됩니다.
```
<br/>

## 궁금증!

```java
세 가지 방법을 다 쓰면 어떤 순서로 불릴까? 그리고 정말 저 순서가 맞을까?
```

한 클래스에 전부 넣고 돌려봤다.

```java
class Client implements InitializingBean, DisposableBean, BeanNameAware {

    Client() {
        System.out.println("1. 생성자 호출");
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("2. BeanNameAware  (이름 = " + name + ")");
    }

    @PostConstruct
    public void init() {
        System.out.println("3. @PostConstruct");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("4. InitializingBean.afterPropertiesSet");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("5. @PreDestroy");
    }

    @Override
    public void destroy() {
        System.out.println("6. DisposableBean.destroy");
    }
}
```

<br/>

### 결과

```java
1. 생성자 호출
2. BeanNameAware  (이름 = client)
3. @PostConstruct
4. InitializingBean.afterPropertiesSet
--- 컨테이너 준비 끝 ---
5. @PreDestroy
6. DisposableBean.destroy
--- 컨테이너 닫힘 ---
```

<br/>

세 가지를 다 쓰면 `@PostConstruct` 가 먼저, 인터페이스 방식이 나중이다.

`Aware` 계열은 그보다도 먼저 불린다. 이름이나 컨테이너를 초기화 중에 써야 할 수 있기 때문이다.

<br/>

`--- 컨테이너 준비 끝 ---` 이 찍히기 전에 초기화가 다 끝나 있다는 점도 볼 수 있다.

애플리케이션이 요청을 받기 시작할 때는 모든 빈이 준비된 상태인 것이다.

<br/>

## 왜 생성자에서 다 하면 안 되는가

원문의 `의존관계 주입이 다 끝난 다음에야 준비가 완료된다` 는 부분이 핵심이다.

```java
@Component
class Client {
    private final Url url;

    Client(Url url) {
        this.url = url;
        connect();          // 여기서 연결해도 되지 않나?
    }
}
```

<br/>

생성자 주입이라면 사실 이건 동작한다. 값이 이미 다 들어와 있기 때문이다.

문제는 생성자가 해야 할 일이 아니라는 데 있다.

<br/>

생성자는 `객체를 만드는 것` 까지가 책임이다.

네트워크 연결처럼 실패할 수 있고 시간이 오래 걸리는 일을 생성자에서 하면,

객체를 만드는 것만으로 예외가 터지거나 몇 초씩 멈추게 된다.

<br/>

테스트에서 이 객체를 만들려고만 해도 실제 서버에 연결을 시도하게 된다.

```java
생성자        -> 필드를 채운다. 무거운 일은 하지 않는다
초기화 콜백    -> 연결, 캐시 준비 같은 무거운 일을 한다
```

<br/>

## 세 가지 중 무엇을 쓰는가

```java
1. 인터페이스 (InitializingBean, DisposableBean)
   -> 스프링 인터페이스를 코드에 박게 된다. 외부 라이브러리 클래스에는 못 쓴다

2. @Bean(initMethod = "...", destroyMethod = "...")
   -> 코드를 안 건드려도 된다. 외부 라이브러리 클래스에 쓸 수 있다

3. @PostConstruct, @PreDestroy
   -> 자바 표준(jakarta.annotation)이라 스프링에 묶이지 않는다
```

<br/>

`3번` 이 기본이다. 스프링 어노테이션이 아니라 자바 표준이라서, 컨테이너를 바꿔도 그대로 쓸 수 있다.

<br/>

`1번` 은 스프링 초기 방식이라 지금은 거의 안 쓴다.

`2번` 은 내가 고칠 수 없는 외부 클래스에 콜백을 붙여야 할 때 쓴다.

```java
@Bean(destroyMethod = "close")
public DataSource dataSource() {
    return new HikariDataSource(config);   // 남의 클래스라 어노테이션을 못 붙인다
}
```

<br/>

참고로 `@Bean` 은 `destroyMethod` 를 안 적어도 `close` 나 `shutdown` 이라는 이름의 메서드가 있으면

알아서 찾아서 불러준다. 이것을 막으려면 `destroyMethod = ""` 로 비워둬야 한다.

<br/>

## 소멸 콜백이 안 불리는 경우

`5번`, `6번` 은 `context.close()` 를 불렀기 때문에 실행된 것이다.

<br/>

프로토타입 빈은 소멸 콜백이 아예 안 불린다.

컨테이너가 만들어서 넘겨준 뒤로는 추적하지 않기 때문이다.

<br/>

그리고 서버가 강제 종료되면(`kill -9`) 소멸 콜백이 돌 틈이 없다.

정상 종료 신호를 받았을 때만 실행된다.

```java
자원 정리를 소멸 콜백에만 의존하면 안 된다
-> 비정상 종료에도 안전하도록 설계해두는 편이 낫다
```
