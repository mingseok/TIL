## @Autowired , DI , @Component , @ComponentScan, 옵션 처리

<br/>

## `@Autowired` 순서.

1. 스프링이 `@Autowired` 어너테이션이 달려 있는 생성자의 매개변수 타입을 본다.

2. 그리고 `타입`을 확인 후, `컨테이너`에서 같은 타입 꺼내 연결 시켜주는 것이다.

![이미지](/programming/img/입문8.PNG)

(자동으로 의존관계 주입을 해준다)

<br/><br/>

## 그리하여 정리하면,

`MemberController` 클래스랑 `memberService` 클래스를 연결 시켜줘야 될 때 

`@Autowired` 를 사용하게 되는 것이다. (이것을 `디펜던시 인젝션`이라고 부른다)

![이미지](/programming/img/입문9.PNG)



<br/><br/>

## `@Autowired` 를 사용하게 되면 `@Component` 뭔지 알아야 한다.

`@Controller`, `@Service`, `@Repository` 어너테이션들이 `컴포넌트 스캔` 방식이다.

사실, `@Controller`, `@Service`, `@Repository` 하지 않고, `@Component` 만 사용 하면 된다.

<br/>

그렇지만 `@Controller`, `@Service`, `@Repository` 의 내부를 보면 `@Component` 가 들어 있다!

![이미지](/programming/img/입문10.PNG)

<br/><br/>

그리하여, 상관이 없는 것이다. (우린 명시적으로 더 확실히 사용할 수 있게 되는 것이다)

![이미지](/programming/img/입문21.PNG)

<br/><br/>

## 정리 하자면,

스프링이 실행 될때 `@Component` 어너테이션이 있으면 

개들을 전부 객체로 생성해서 컨테이너에 등록하는 것이다.

<br/>

### `@Autowired`는 연관 관계. 위 (녹색)사진 처럼 선을 연결 해주는 것이다.

그리하여 `MemberController`가 `MemberService`를 쓸 수 있게 해주고, 

`MemberService`가 `MemberRepository`를 사용할 수 있게 되는 것이다.

<br/><br/>

## `@ComponentScan`

스프링 3.1부터 도입된 Annotation이며 **스캔 위치를 설정**하고,

어떤 Annotation을 스캔할지 또는 하지 않을지 결정하는 **Filter 기능**을 가지고 있다.

<br/>

### **Filter 기능은** 무엇인가?

컨포넌트 스캔으로 다 찾아서 자동 등록 해주는데, 그중에 뺄걸 지정해주는 것이다.

```java
@ComponentScan(
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION,
        classes = Configuration.class)
)
```

<br/><br/>

## 옵션 처리

주입할 스프링 빈이 없어도 동작해야 할 때가 있다.

그런데 `@Autowired` 만 사용하면 `required` 옵션의 기본값이 `true` 로 되어 있어서 

자동 주입 대상이 없으면 오류가 발생한다

자동 주입 대상을 옵션으로 처리하는 방법은 다음과 같다

- `@Autowired(required=false)` : 자동 주입할 대상이 없으면 수정자 메서드 자체가 호출 안됨
  
- `org.springframework.lang.@Nullable` : 자동 주입할 대상이 없으면 null이 입력된다.
  
- `Optional<>` : 자동 주입할 대상이 없으면 Optional.empty 가 입력된다.

```java
//호출 안됨
@Autowired(required = false)
public void setNoBean1(Member member) {
   // setNoBean1() 은 @Autowired(required=false) 이므로 호출 자체가 안된다.
   System.out.println("setNoBean1 = " + member); 
}

//null 호출
@Autowired
public void setNoBean2(@Nullable Member member) {
   System.out.println("setNoBean2 = " + member); // setNoBean2 = null
}

//Optional.empty 호출
@Autowired(required = false)
public void setNoBean3(Optional<Member> member) {
   System.out.println("setNoBean3 = " + member); // setNoBean3 = Optional.empty
}
```





<br/><br/>


>**Reference** <br/>[스프링 핵심 원리 - 기본편](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8?utm_source=google&utm_medium=cpc&utm_campaign=04.general_backend&utm_content=spring&utm_term=%EC%8A%A4%ED%94%84%EB%A7%81%20%EC%9E%85%EB%AC%B8&gclid=CjwKCAiAjPyfBhBMEiwAB2CCImohok2YrQ2tRdhqfr3cZvKqkIJOHUJ36u6s1-7C9X1gzZIapTvOtxoCangQAvD_BwE)

<br/>

## 궁금증!

```java
컴포넌트 스캔은 무엇을 어떻게 찾나
```

패키지 아래의 클래스 파일을 전부 읽어서 어노테이션을 확인한다.

```java
@ComponentScan(basePackages = "com.example")
```

<br/>

여기서 `.class` 파일을 직접 읽는다는 것이 중요하다.

클래스를 로딩하지 않고 바이트코드만 훑는 것이다.

<br/>

앞의 클래스 로딩 순서 글에서 본 대로,

클래스를 로딩하면 `static` 초기화까지 실행되어버린다.

수천 개 클래스를 그렇게 하면 기동이 느려지고 부작용도 생긴다.

<br/>

그래서 `ASM` 이라는 바이트코드 라이브러리로 어노테이션만 읽는다.

`@Component` 가 붙은 것만 골라서 그때 로딩하는 것이다.

<br/>

## basePackages 를 안 적으면

`@ComponentScan` 이 붙은 클래스가 있는 패키지가 기준이 된다.

```java
com.example.Application     <- @SpringBootApplication
com.example.member.*        <- 스캔됨
com.example.order.*         <- 스캔됨
com.other.*                 <- 스캔 안 됨
```

<br/>

`@SpringBootApplication` 안에 `@ComponentScan` 이 들어 있다.

그래서 메인 클래스를 최상위 패키지에 두는 것이 관례가 된 것이다.

<br/>

기본 패키지에 두면 문제가 생긴다.

```java
// 패키지 선언이 없는 클래스에 @ComponentScan 을 붙이면
// 클래스패스 전체를 스캔한다. 스프링 자신의 클래스까지 훑는다
```

<br/>

기동이 아주 느려지고, 엉뚱한 것이 빈으로 등록되기도 한다.

<br/>

## 스캔 대상을 좁히거나 넓힐 수 있다

```java
@ComponentScan(
    includeFilters = @Filter(type = FilterType.ANNOTATION, classes = MyComponent.class),
    excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
```

<br/>

`FilterType` 이 다섯 가지다.

```java
ANNOTATION      - 어노테이션으로
ASSIGNABLE_TYPE - 특정 타입의 하위인지로
ASPECTJ         - AspectJ 표현식으로
REGEX           - 정규식으로
CUSTOM          - 직접 만든 필터로
```

<br/>

실무에서는 거의 안 쓴다. 기본 설정으로 충분하기 때문이다.

<br/>

## 같은 이름의 빈이 두 개면

수동 등록과 자동 등록이 겹치면 수동이 이긴다.

```java
@Component
public class MemoryMemberRepository implements MemberRepository {}   // 자동

@Bean
public MemberRepository memoryMemberRepository() { ... }             // 수동
```

<br/>

`수동이 이긴다` 는 게 편해 보이지만 실제로는 위험하다.

조용히 덮어써지니 어느 쪽이 등록됐는지 모르고 넘어가게 된다.

<br/>

그래서 스프링 부트는 이걸 오류로 만들었다.

```java
The bean 'memoryMemberRepository' could not be registered.
A bean with that name has already been defined and overriding is disabled.
```

<br/>

풀려면 설정을 켜야 하는데, 안 켜는 게 낫다.

```java
spring.main.allow-bean-definition-overriding=true
```

<br/>

앞의 생성자 주입을 선택 글에서 본 것과 같은 태도다.

```java
애매한 것은 조용히 넘어가지 말고 뜰 때 터뜨린다
```

<br/>

## 주입할 빈이 없을 때 세 가지 방법

```java
@Autowired(required = false)
public void setStat(StatCollector stat) { ... }      // 아예 호출을 안 한다

@Autowired
public void setStat(@Nullable StatCollector stat) { ... }   // null 이 들어온다

@Autowired
public void setStat(Optional<StatCollector> stat) { ... }   // Optional.empty 가 들어온다
```

<br/>

세 개가 미묘하게 다르다.

<br/>

`required = false` 는 메서드 자체를 안 부른다.

그래서 그 안에 다른 코드가 있으면 그것도 실행이 안 된다.

<br/>

앞의 Optional 글에서 본 대로, `Optional` 을 쓰면 값이 없을 수 있다는 게 타입에 드러난다.

`@Nullable` 은 어노테이션이라 컴파일러가 강제하지 않는다.

<br/>

## 같은 타입의 빈을 전부 받을 수도 있다

```java
@Autowired
public DiscountService(Map<String, DiscountPolicy> policyMap) {
    this.policyMap = policyMap;
}
```

<br/>

`DiscountPolicy` 타입의 빈이 전부 들어온다.

키는 빈 이름이다.

```java
{fixDiscountPolicy=..., rateDiscountPolicy=...}
```

<br/>

이러면 `if` 로 분기하는 코드를 없앨 수 있다.

```java
public int discount(Member member, int price, String code) {
    DiscountPolicy policy = policyMap.get(code);      // 이름으로 골라 쓴다
    return policy.discount(member, price);
}
```

<br/>

앞의 스트래티지 패턴 글에서 본 그 구조를 스프링이 대신 조립해주는 셈이다.

새 정책을 추가하면 `@Component` 만 붙이면 되고, 이 코드는 안 고친다.

<br/>

`List<DiscountPolicy>` 로 받을 수도 있다.

순서가 필요하면 `@Order` 를 붙이면 된다.
