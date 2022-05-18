## @ComponentScan

### `@ComponentScan` 은 `@Component` 가 붙은 모든 클래스를 스프링 빈으로 등록한다.

- 이때 스프링 빈의 기본 이름은 클래스명을 사용하되 맨 앞글자만 소문자를 사용한다.

- 빈 이름 기본 전략: MemberServiceImpl 클래스 memberServiceImpl
- 빈 이름 직접 지정: 만약 스프링 빈의 이름을 직접 지정하고 싶으면
- @Component("memberService2") 이런식으로 이름을 부여하면 된다.

<br/>

![이미지](/programming/img/스프링17.PNG)

<br/>

### ConponentScan : Component 어노테이션이 붙은 클래스들을 검색한다.

@ComponentScan 어너테이션이 붙은 클래스를 찾아가서 자동으로 스프링 빈에 등록 해주는 것이다.

<br/>

@ComponentScan은 스프링 3.1부터 도입된 Annotation이며 **스캔 위치를 설정**하고,

어떤 Annotation을 스캔할지 또는 하지 않을지 결정하는 **Filter 기능**을 가지고 있다.

<br/>

### **Filter 기능은** 무엇인가?

컨포넌트스캔으로 다 찾아서 자동 등록 해주는데, 그중에 뺄걸 지정해주는 것이다.

```java
@ComponentScan(
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION,
        classes = Configuration.class)
)
```

<br/>
<br/>

## **@Component**

### 컴포넌트 ( Component )



구성요소 라는 뜻으로 컴포넌트는 독립적인 단위 모듈이다. 

유저가 사용하는 시스템에 대한 조작장치를 이야기한다.

만약, @Component 라서 MemoryMemberRepository 클래스가 <br/>자동 등록 되면서 앞글자가 ‘m’ 소문자로 등록된다.

<br/>

### @Component 란?

개발자가 직접 작성한 Class 를 Bean 으로 만드는 것이다.

싱글톤 클래스 빈을 생성하는 어노테이션이다. 

물론 @Scope를 통해 싱글톤이 아닌 방식으로도 생성이 가능하다.

이 어노테이션은 선언적인 어노테이션이다.

<br/>

즉, 패키지 스캔 안에 이 어노테이션은 "**이 클래스를 정의했으니 빈으로 등록하라**" 는 뜻이 된다.

ConponentScan : Component 어노테이션이 붙은 클래스들을 검색한다.

`@Component` 를 들고있는 클래스들이 스캐닝되고, Bean으로 등록된다.

<br/>

대표적인 컴포넌트

- @Controller
- @Repository
- @Service
- @Configuration

참고: @Configuration 이 컴포넌트 스캔의 대상이 된 이유도 <br/>@Configuration 소스코드를 열어보면 @Component 애노테이션이 붙어있기 때문이다.

<br/>

## 전체적으로 정리를 해보자면,

1. `AutoAppConfig` 클래스를 간다.

2. 안에 “`@ComponentScan` 어너테이션이 있다” 한다면
3. 애가 자동으로 클래스 패스를 다 뒤적뒤적 해서 
4. 다 스프링 빈에 등록을 해준다. “뭐를??”
5. 이름 그대로 `@Component` 붙은 애들. 그리고 그 애들을 뒤적뒤적 하여
6. 스프링 컨테이너에 자동으로 등록 시켜 주는 것이다.
7. 그러면 그때부터 찾아 사용 할 수 있게 된 것이다.

<br/>

### 이렇게 까지 하니깐 의존관계를 자동으로 주입 할 수 있는 방법이 없어진 것이다 !!


<br/>

이걸 해결 하기 위해.

의존 관계 자동 주입인 @Autowired 를 사용하여 주입을 하면 되는 것이다.

그러면 @Autowired 붙은 생성자의 매개변수인 타입을 찾아서 등록을 해준다


<br/>


>**Reference** <br/>스프링 핵심 원리 - 기본편 : https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8
