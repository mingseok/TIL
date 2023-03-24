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