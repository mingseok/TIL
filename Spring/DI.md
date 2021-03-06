## DI 

### 스프링의 DI를 사용하면 ‘기존 코드를 전혀 손대지 않고, 
설정만으로 구현 클래스를 변경’ 할 수 있다.

연결 시켜줄때 @Autowired 를 사용 하는 것이다. 

<br/>스프링 빈은 싱글톤으로 등록하기 때문에 같은 스프링 빈이면 모두 같은 인스턴스 이다.

![이미지](/programming/img/의2.PNG)

<br/>

## 전체적인 흐름

`MemberController`가 스프링이 생성이 할때 스프링 빈에 

등록 되어 있는 `MemberService`객체를 가져다가 딱! 넣어주는 것이다. 

그리고 `MemberService` 는 `MemoryMemberRepository` 가 필요하다

<br/>그러면 연결 시켜주는 것이다. 

`MemberService` 를 스프링이 생성을 할때, ‘`@Service`’ 어너테이션을 확인하고

스프링 컨테이너에 등록을 하면서 ‘생성자’를 호출한다. 

<br/>그때 `@Autowired` 어너테이션이 있으면 스프링 입장에서 

“아, 너는 `MemoryMemberRepository` 가 필요하구나” 생각해서 

스프링 컨테이너에 있는 `MemoryMemberRepository` 를 

`MemberService` 의 생성자에 넣어주는(주입) 것이다.

<br/>

### 그리하여 `MemberController` 가 `MemberService` 사용할 수 있게 되는 것이고,

### `MemberService` 가 `MemoryMemberRepository` 사용할 수 있게 되는 것이다.

<br/>

## 이것이 바로 ‘DI’ == ‘의존성 주입’ 인 것이다.

(뭔가 밖에서 넣어주는 기분이 들것이다.  누가? 스프링이 넣어 주는것이다.)

<br/>이것이 의존 관계를 주입 해주는 것이다. 

`MemberService`입장에서 보자면 내가 직접 new하지 않는다.

외부에서 `memberRepository`를 넣어준 것이다.

<br/>

### 이것을 Dependency Injection(디펜던시 인젝션) 이라 하고 "DI" 라고 부른다.

애플리케이션 “실행 시점” 에 외부에서 실제 구현 객체를 생성하고 

클라이언트에 전달해서 클라이언트와 서버의 실제 의존관계가 

연결 되는 것을 “의존관계 주입” == 디펜던시 인젝션 이라고 한다.

<br/>

## DI (Dependency Injection) - 의존관계 주입

의존하는 객체를 직접 생성하는 것이 아니라, 외부에서 생성한 후 주입하는 것.

인젝션(=주입이라는 뜻)

<br/>

### 3가지 조건이 필요

- 클래스 모델이나 코드에는 런타임(실행) 시점의 의존관계가 드러나지 않는다.
    
    (= 정적인 클래스 의존관계가 아니다)(= 동적인 객체 인스턴스 의존관계이다) => 
    
    인터페이스에만 의존하고 있어야 한다

    
- 런타임 시점의 의존관계는 외부에서 결정한다

- 외부에서 실제 구현 객체를 생성하고 클라이언트(사용할 오브젝트)에
    
    전달(주입)함으로써 의존관계가 연결되는 것이다
    

    <br/>

예를 들어

```java
private Car myCar = new 벤츠();
```

Car가 인터페이스고 벤츠가 구현 객체라면, 런타임 이전에, 

즉 코드상으로 벤츠 클래스가 Car인터페이스를 의존하는 것을 알 수 있다

```java
private Car myCar;
```

이러면 Car에 대해 무슨 차가 들어올지 알 수 없다.

(런타임 시점의 의존관계가 드러나지 않으므로) 

즉 이렇게 인터페이스에만 의존하고 의존관계 주입이 발생할 수 있다.

<br/>예시.

![이미지](/programming/img/의3.PNG)

<br/>여기로

memberService 클래스에 memberRepository에는 MemoryMemberRepository 가 저장 되는 것이다.

![이미지](/programming/img/의4.PNG)


<br/>

>**Reference** <br/>스프링 핵심 원리 - 기본편 : https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8
