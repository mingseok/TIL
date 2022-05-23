## @Autowired

### @Autowired 는 기본적으로 타입으로 조회를 한다.

`memberService를 스프링이 스프링 컨테이너에 있는 memberService을 가져다 연결시켜주는 것이다.`

<br/>‘오토와이어드’ 라는 어너테이션을 생성자에 붙여주면 스프링이 어떻게 하냐면,

`MemberRepository` 타입에 맞는 애를 찾아와서 자동으로 의존관계 주입을 해주게 되는 것이다.

![이미지](/programming/img/가.PNG)

<br/>그리하여 `@Autowired` 를 사용하게 되면 자동적으로 `@Component` 어너테이션이 필요하게 되는 것이다.

### 비유 하자면, 이런식으로 동작한다고 생각하면 되는 것이다.

```java
@Autowired 가 == ac.getBean(MemberRepository.class) 랑 같은 것이다.
```

<br/>

## 정리를 해보자면,

1. `AutoAppConfig` 클래스를 간다.

2. 안에 “`@ComponentScan` 어너테이션이 있다” 한다면
3. 애가 자동으로 클래스 패스를 다 뒤적뒤적 해서 
4. 다 스프링 빈에 등록을 해준다. 뭐를??
5. 이름 그대로 `@Component` 붙은 애들을 뒤적뒤적 하여
6. 스프링 컨테이너에 자동으로 등록 시켜 주는 것이다.
7. 그러면 그때부터 찾아 사용 할 수 있게 된 것이다.

<br/>

### 이렇게 까지 하니깐 의존관계를 자동으로 주입 할 수 있는 
방법이 없어진 것이다 !!

<br/>이걸 해결 하기 위해.

의존 관계 자동 주입인 @Autowired 를 사용하여 주입을 하면 되는 것이다.

그러면 @Autowired 붙은 생성자의 매개변수인 타입을 찾아서 등록을 해준다


<br/>


>**Reference** <br/>스프링 핵심 원리 - 기본편 : https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8
