## 스프링 컨테이너, 스프링 빈, BeanDefinition

<br/>

## 스프링 컨테이너 생성

![이미지](/programming/img/입문30.PNG)

<br/>


```java
//스프링 컨테이너 생성
ApplicationContext applicationContext = new 
                              AnnotationConfigApplicationContext(AppConfig.class);
```

- `ApplicationContext`는 인터페이스이다.

- 스프링 컨테이너를 생성할 때는 구성 정보를 지정해주어야 한다.

    - 여기서는 `AppConfig.class` 를 구성 정보로 지정했다.

<br/><br/>

## 스프링 빈 등록

![이미지](/programming/img/입문31.PNG)

- 스프링 컨테이너는 파라미터로 넘어온 설정 클래스 정보를 사용해서 스프링 빈을 등록한다

- 주의: 빈 이름은 항상 다른 이름을 부여해야 한다

<br/><br/>

![이미지](/programming/img/입문32.PNG)

- 스프링 컨테이너는 설정 정보를 참고해서 의존관계를 주입(DI)한다.

<br/><br/>

## 스프링 빈 조회 - 상속 관계

부모 타입으로 조회하면, 자식 타입도 함께 조회한다. (본인 포함)

그래서 모든 자바 객체의 최고 부모인 Object 타입으로 조회하면, 모든 스프링 빈을 조회한다.

![이미지](/programming/img/입문33.PNG)

<br/><br/>

## BeanFactory와 ApplicationContext

![이미지](/programming/img/입문34.PNG)

- `BeanFactory` 스프링 컨테이너의 최상위 인터페이스다.

- 스프링 빈을 관리하고 조회하는 역할을 담당한다.
- `getBean()` 을 제공한다.

지금까지 우리가 사용했던 대부분의 기능은 `BeanFactory`가 제공하는 기능이다.

<br/><br/>

## 내가 사용하는 ApplicationContext 뭔가?

- `BeanFactory` 기능을 모두 상속 받아서 제공한다.

```
빈을 관리하고 검색하는 기능을 BeanFactory가 제공해주는데, 그러면 둘의 차이가 뭘까?
```

<br/>

개발할 때는 빈을 관리하고 조회하는 기능은 물론이고, 수 많은 `부가기능`이 필요하다.

![이미지](/programming/img/입문35.PNG)

### 메시지소스를 활용한 국제화 기능

- 예를 들어서 한국에서 들어오면 한국어로, 영어권에서 들어오면 영어로 출력

### 환경변수

- 로컬, 개발, 운영등을 구분해서 처리

### 애플리케이션 이벤트

- 이벤트를 발행하고 구독하는 모델을 편리하게 지원

### 편리한 리소스 조회

- 파일, 클래스패스, 외부 등에서 리소스를 편리하게 조회

<br/><br/>

## 정리

- `BeanFactory`를 직접 사용할 일은 거의 없다.

    - 부가기능이 포함된 `ApplicationContext`를 사용한다.

- `BeanFactory`나 `ApplicationContext`를 `스프링 컨테이너`라 한다.

<br/><br/>

## BeanDefinition 설명

스프링은 어떻게 이런 다양한 설정 형식을 지원하는 것일까? 

그 중심에는 BeanDefinition 이라는 추상화가 있다

<br/>

스프링 컨테이너 자체는 `BeanDefinition`에만 의존한다.

즉, 스프링 컨테이너는 파일이 들어왔다면 `class` 인지, `xml`로 인지 신경 쓰지 않고, 

오로지 `BeanDefinition`에만 의존한다는 것이다.

<br/>

핵심은 설계 자체를 추상화에만 의존하도록 설계를 한것이다 → BeanDefinition는 인터페이스

![이미지](/programming/img/입문36.PNG)


<br/><br/>


>**Reference** <br/>[스프링 핵심 원리 - 기본편](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8?utm_source=google&utm_medium=cpc&utm_campaign=04.general_backend&utm_content=spring&utm_term=%EC%8A%A4%ED%94%84%EB%A7%81%20%EC%9E%85%EB%AC%B8&gclid=CjwKCAiAjPyfBhBMEiwAB2CCImohok2YrQ2tRdhqfr3cZvKqkIJOHUJ36u6s1-7C9X1gzZIapTvOtxoCangQAvD_BwE)

<br/>

## 궁금증!

```java
BeanDefinition 안에 무엇이 들어 있나
```

컨테이너에서 직접 꺼내봤다.

```java
AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(DefCfg.class);
BeanDefinition bd = ac.getBeanDefinition("myService");
```

<br/>

`@Bean` 으로 등록한 것과 컴포넌트 스캔으로 등록한 것을 나란히 찍어봤다.

<br/>

### 결과

```java
--- @Bean 으로 등록한 것 ---
  beanClassName     = null
  factoryBeanName   = defCfg
  factoryMethodName = myService
  scope             = "" (= singleton)
  lazyInit          = false

--- 컴포넌트 스캔으로 등록한 것 ---
  beanClassName     = demo.scan.Scanned
  factoryBeanName   = null
  factoryMethodName = null
  scope             = singleton
  lazyInit          = false
```

<br/>

두 줄이 정확히 반대로 채워져 있다.

<br/>

`@Bean` 쪽은 `beanClassName` 이 `null` 이다.

`defCfg` 라는 빈의 `myService()` 메서드를 부르라고만 적혀 있다.

만들 클래스가 아니라 만드는 방법이 적혀 있는 것이다.

<br/>

컴포넌트 스캔 쪽은 `이 클래스를 new 해라` 라고 적혀 있다.

<br/>

`scope` 가 한쪽만 비어 있는 것도 눈에 띈다.

빈 문자열이면 기본값인 싱글톤으로 본다.

명시적으로 지정된 적이 없다는 뜻이라, 값이 없는 것과 기본값이 구분되는 것이다.

<br/>

## 그래서 설정 방식이 여러 가지일 수 있다

```java
자바 설정 (@Bean)
컴포넌트 스캔 (@Component)
XML 설정
```

<br/>

전부 결국 `BeanDefinition` 으로 바뀐다.

```java
XML 파일        -> XmlBeanDefinitionReader   -> BeanDefinition
자바 설정 클래스 -> AnnotatedBeanDefinitionReader -> BeanDefinition
```

<br/>

컨테이너는 `BeanDefinition` 만 보고 객체를 만든다.

무엇으로 설정했는지는 모른다.

<br/>

앞의 인터페이스를 왜 사용하는가 글에서 본 그 구조다.

```java
설정 방식이 바뀌어도 컨테이너는 안 바뀐다
```

<br/>

읽는 방법을 하나 더 만들면 새 설정 방식이 생기는 것이다.

<br/>

## 정의를 고칠 수도 있다

`BeanFactoryPostProcessor` 라는 것이 있다.

```java
객체를 만들기 전에 BeanDefinition 을 손보는 자리
```

<br/>

앞의 스프링 프로퍼티 글에서 본 `${...}` 치환이 이걸로 동작한다.

```java
@Value("${db.url}")
```

<br/>

`PropertySourcesPlaceholderConfigurer` 가 `BeanDefinition` 안의 `${db.url}` 를

실제 값으로 바꿔놓고 나서, 그 다음에 객체가 만들어진다.

<br/>

객체를 만든 뒤에 손보는 것은 `BeanPostProcessor` 다.

```java
BeanFactoryPostProcessor - 만들기 전. 설계도를 고친다
BeanPostProcessor        - 만든 후. 객체를 바꿔치기할 수 있다
```

<br/>

앞의 AOP 프록시 글에서 본 프록시가 `BeanPostProcessor` 로 끼워진다.

```java
원래 객체를 만든다 -> BeanPostProcessor 가 프록시로 감싼 것을 대신 돌려준다
-> 컨테이너에는 프록시가 등록된다
```

<br/>

그래서 `@Transactional` 이 붙은 빈을 꺼내보면 원래 클래스가 아닌 것이다.

<br/>

## 컨테이너가 두 종류다

```java
BeanFactory        - 빈을 만들고 관리하는 최소 기능
ApplicationContext - BeanFactory + 부가 기능
```

<br/>

`ApplicationContext` 가 상속하는 인터페이스를 보면 무엇이 더 있는지 나온다.

```java
MessageSource            - 국제화
EnvironmentCapable       - 프로파일, 프로퍼티
ApplicationEventPublisher- 이벤트
ResourceLoader           - 파일, 클래스패스 리소스 조회
```

<br/>

앞의 스프링 메시지, 국제화 글에서 본 `MessageSource` 가 여기 들어 있다.

<br/>

실무에서 `BeanFactory` 를 직접 쓸 일은 없다.

`ApplicationContext` 를 쓴다고 보면 된다.

<br/>

## 빈 이름은 어떻게 정해지나

```java
@Bean
public MemberService memberService() { ... }        // 메서드 이름 = "memberService"

@Component
public class MemberServiceImpl { }                  // 클래스명 첫 글자 소문자 = "memberServiceImpl"
```

<br/>

여기서도 자바빈 규약의 함정이 나온다.

```java
URLService  ->  "URLService"       (첫 두 글자가 대문자면 그대로)
UrlService  ->  "urlService"
```

<br/>

앞의 HTTP 요청 파라미터 - @ModelAttribute 글에서 본 그 규칙과 같다.

`Introspector.decapitalize()` 라는 같은 메서드를 쓰기 때문이다.

<br/>

이름을 직접 정하고 싶으면 적어주면 된다.

```java
@Component("memberService")
@Bean(name = "memberService")
```
