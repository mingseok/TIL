## 스프링 빈(Bean)이란? / BeanDefinition이란?



빈(Bean)은 스프링 컨테이너에 의해 관리되는 

재사용 가능한 소프트웨어 컴포넌트이다.

<br/>

즉, 스프링 컨테이너가 관리하는 자바 객체를 뜻하며, 
하나 이상의 빈(Bean)을 관리한다.

빈은 인스턴스화된 `객체`를 의미하며, 스프링 컨테이너에 
등록된 `객체`를 `스프링 빈`이라고 한다.

<br/>

`@Bean` 어노테이션을 통해 메서드로부터 
반환된 객체를 `스프링 컨테이너`에 등록한다.

빈은 클래스의 `등록 정보`, `Getter/Setter` 메서드를 포함하며, 
컨테이너에 사용되는 `설정` 메타데이터로 생성된다.

```java
"메타데이터란?"
메타데이터란 애플리케이션이 처리해야 할 데이터가 아니라, 
컴파일 과정과 실행 과정에서 코드를 어떻게 컴파일하고,
처리할 것인지 알려주는 정보입니다

-> "XML" 또는 "자바 어노테이션", 자바 코드로 표현하며, 
    컨테이너의 명령과 인스턴스화, 설정, 조립할 객체 등
```

<br/><br/>

## 빈(Bean) 접근 방법

먼저, `ApplicationContext`를 사용하여 `bean`을 정의를 `읽고`, `액세스` 할 수 있다.

<br/><br/>

## BeanDefinition

스프링의 다양한 설정 형식은 `BeanDefinition`이라는 추상화 덕분에 할 수 있는 것이다.

<br/>

`Bean`은 `BeanDefinition`으로 정의되고, 
`BeanDefinition`에 

따라 활용하는 방법이 달라진다
`BeanDefinition`은 속성에 따라 컨테이너가 

<br/>

`Bean`을 어떻게 생성하고 관리 할지를 결정한다.

`@Bean` 어노테이션 또는, `<bean>` 태그당 `1개`씩 메타 정보가 생성된다.

<br/>

`Spring`이 설정 메타정보를 `BeanDefinition` 인터페이스를 통해


관리하기 때문에 컨테이너 설정을 `XML`, `Java`로 할 수 있는 것이다.

<br/>

`스프링 컨테이너`는 설정 형식이 `XML`인지, 

`Java`코드인지 모르며, `BeanDefinition`만 알면 된다.
<br/>

## 궁금증!

```java
BeanDefinition 안에는 실제로 무엇이 들어 있을까?
```

두 가지 방식으로 등록한 빈의 정의를 꺼내서 비교해봤다.

```java
BeanDefinition def  = context.getBeanDefinition("orderService");  // @Service 로 등록
BeanDefinition def2 = context.getBeanDefinition("payService");    // @Bean 으로 등록

System.out.println("클래스       = " + def.getBeanClassName());
System.out.println("스코프       = " + def.getScope());
System.out.println("지연생성     = " + def.isLazyInit());
System.out.println("팩토리메서드 = " + def.getFactoryMethodName());
```

<br/>

### 결과

```java
orderService 클래스       = demo.OrderService
orderService 스코프       = singleton
orderService 지연생성     = false
orderService 팩토리메서드 = null

payService   클래스       = null
payService   팩토리메서드 = payService
```

<br/>

원문의 `속성에 따라 컨테이너가 어떻게 생성하고 관리할지 결정한다` 가 이 항목들이다.

- `클래스` : 무엇을 만들지

- `스코프` : 하나만 만들지, 요청마다 만들지
- `지연생성` : 미리 만들지, 꺼낼 때 만들지
- `팩토리메서드` : 직접 `new` 할지, 어떤 메서드를 불러서 받을지

<br/>

`@Component` 로 등록하면 클래스 이름이 채워지고, `@Bean` 으로 등록하면 메서드 이름이 채워진다.

같은 `BeanDefinition` 인데 채워지는 칸이 다른 것이다.

<br/>

## 이래서 설정 방식을 갈아끼울 수 있다

원문의 `스프링 컨테이너는 XML 인지 자바 코드인지 모른다` 가 핵심이다.

```java
XML 파일     ->  XmlBeanDefinitionReader     ->  BeanDefinition
자바 어노테이션 ->  AnnotatedBeanDefinitionReader ->  BeanDefinition
컴포넌트 스캔  ->  ClassPathBeanDefinitionScanner ->  BeanDefinition
                                                        |
                                                        v
                                                   스프링 컨테이너
```

<br/>

읽는 방식이 세 가지지만 결과물은 하나로 통일된다.

컨테이너는 `BeanDefinition` 만 보고 일하니, 앞단이 무엇이든 상관이 없다.

<br/>

앞의 프론트 컨트롤러 글에서 본 `HandlerAdapter` 와 같은 구조다.

`여러 모양을 하나의 형식으로 바꿔놓고, 그 뒤로는 그것만 다룬다` 는 방식이다.

```java
읽는 방법이 늘어나도 -> Reader 를 하나 더 만들면 된다
컨테이너는 안 바뀐다
```

<br/>

## 빈이 만들어지는 순서

`BeanDefinition` 은 설계도이고, 실제 객체는 나중에 만들어진다.

```java
1. 설정을 읽어서 BeanDefinition 을 전부 등록한다   (아직 객체는 하나도 없다)
2. BeanFactoryPostProcessor 가 BeanDefinition 을 수정할 기회를 갖는다
3. 등록된 BeanDefinition 을 보고 객체를 만든다
4. 의존관계를 주입한다
5. BeanPostProcessor 가 만들어진 객체를 손볼 기회를 갖는다   <- AOP 프록시가 여기서 씌워진다
6. 초기화 콜백을 부른다
```

<br/>

`1번` 과 `3번` 이 나뉘어 있다는 것이 중요하다.

객체를 만들기 전에 설계도만 먼저 다 모아두기 때문에, 그 사이에 설계도를 고칠 수 있다.

<br/>

`@Value("${db.url}")` 의 `${...}` 를 실제 값으로 바꾸는 일이 `2번` 에서 일어난다.

`PropertySourcesPlaceholderConfigurer` 라는 `BeanFactoryPostProcessor` 가 하는 일이다.

<br/>

앞의 AOP 글에서 본 프록시는 `5번` 에서 만들어진다.

이미 만들어진 객체를 받아서, 그것을 감싼 프록시를 대신 돌려주는 방식이다.

```java
BeanPostProcessor.postProcessAfterInitialization(빈, 이름)
  -> 원래 빈을 돌려주면 그대로 등록
  -> 프록시를 돌려주면 프록시가 등록된다
```

<br/>

`@PostConstruct` 안에서 `@Transactional` 이 안 먹는 이유가 여기서 나온다.

`6번` 초기화 콜백이 도는 시점에는 `5번` 이 아직 안 끝났을 수 있어서, 프록시가 없을 수 있는 것이다.

<br/>

## 그래서 빈이란

정리하면 이렇게 말할 수 있다.

```java
자바 객체            - new 로 만들면 끝. 만든 사람이 관리한다
스프링 빈            - 컨테이너가 BeanDefinition 을 보고 만들고,
                      의존성을 주입하고, 프록시를 씌우고,
                      생명주기 콜백을 불러주고, 종료까지 책임진다
```

`컨테이너가 관리한다` 는 말에 이만큼이 들어 있는 것이다.
