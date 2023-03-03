## @Autowired , DI , @Component , @ComponentScan

<br/>

## `@Autowired` 순서.

1. 스프링이 `@Autowired` 어너테이션이 달려 있는 메서드의 매개변수 타입을 본다.

2. 그리고 `타입`을 확인 후, `컨테이너`에서 같은 타입 꺼내 연결 시켜주는 것이다.

![이미지](/programming/img/입문8.PNG)

(자동으로 의존관계 주입을 해준다)

<br/><br/>

## 그리하여 정리하면,

`MemberController` 클래스랑 `memberService` 클래스를 연결 시켜줘야 될 때 

`@Autowired` 를 사용하게 되는 것이다. (이것을 `디펜던시 인젝션`이라고 부른다)

![이미지](/programming/img/입문9.PNG)

<br/><br/>

## DI (=Dependency Injection =의존관계 주입)

스프링의 DI를 사용하면 ‘기존 코드를 전혀 손대지 않고, 설정만으로 구현 클래스를 변경’ 할 수 있다.

- 뭔가 밖에서 넣어주는 기분이 들것이다.  `“누가?”` 스프링이 넣어 주는 것.)

<br/><br/>

## `@Autowired` 를 사용하게 되면 `@Component` 뭔지 알아야 한다.

`@Controller`, `@Service`, `@Repository` 어너테이션들이 `컴포넌트 스캔` 방식이다.

사실, `@Controller`, `@Service`, `@Repository` 하지 않고, `@Component` 만 사용 하면 된다.

<br/>

그렇지만 `@Controller`, `@Service`, `@Repository` 의 내부를 보면 `@Component` 가 들어 있다!

![이미지](/programming/img/입문10.PNG)

그리하여, 상관이 없는 것이다. (우린 명시적으로 더 확실히 사용할 수 있게 되는 것이다)

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


>**Reference** <br/>[스프링 핵심 원리 - 기본편](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8?utm_source=google&utm_medium=cpc&utm_campaign=04.general_backend&utm_content=spring&utm_term=%EC%8A%A4%ED%94%84%EB%A7%81%20%EC%9E%85%EB%AC%B8&gclid=CjwKCAiAjPyfBhBMEiwAB2CCImohok2YrQ2tRdhqfr3cZvKqkIJOHUJ36u6s1-7C9X1gzZIapTvOtxoCangQAvD_BwE)