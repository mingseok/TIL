## @CGLIB 란?
<br/>
이상한게 있다. 이게 뭔가??

![이미지](/programming/img/스프링15.PNG)

<br/>


### 이것은 내가 만든 클래스가 아니라 스프링이 CGLIB라는 바이트코드 조작 라이브러리를 사용한 것이다.

![이미지](/programming/img/스프링16.PNG)

<br/>그 임의의 다른 클래스가 바로 싱글톤이 보장되도록 해준다. 

아마도 다음과 같이 바이트 코드를 조작해서 작성되어 있을 것이다.


<br/>

### AppConfig@CGLIB 예상 코드

```java
@Bean
public MemberRepository memberRepository() {
 
	 if (memoryMemberRepository가 이미 스프링 컨테이너에 등록되어 있으면?) {
				 
			return 스프링 컨테이너에서 찾아서 반환;

	 } else { //스프링 컨테이너에 없으면

			기존 로직을 호출해서 MemoryMemberRepository를 생성하고 스프링 컨테이너에 등록
		
			return 반환
	 }
}
```

- @Bean이 붙은 메서드마다 이미 스프링 빈이 존재하면 존재하는 빈을 반환하고, <br/>스프링 빈이 없으면 생성해서 스프링 빈으로 등록하고 반환하는 코드가 동적으로 만들어진다.

- 덕분에 싱글톤이 보장되는 것이다.

<br/><br/>

### 즉, 이렇게 호출 되야 하는데 


```java
call AppConfig.memberService

call AppConfig.memberRepository

call AppConfig.memberRepository

call AppConfig.orderService

call AppConfig.memberRepository
```

AppConfig@CGLIB 예상 코드에 의해 조건문을 검사하며, 빈이 이미 등록 되어 있는거면 등록되어 있는 빈을 주는 것이다.

<br/>

### 참고 AppConfig@CGLIB는 AppConfig의 자식 타입이므로, AppConfig 타입으로 조회 할 수 있다.

(=부모 타입으로 조회 하면 자식타입도 다 끌려 나온다)

<br/>

## 즉, 실제로 AppConfig 는 스프링 빈으로 등록되어 있는게 아니고, <br/>AppConfig@CGLIB 가 등록되어 있는 것이다.

<br/>대신 부모의 AppConfig 이름을 사용하며, 실제 등록 되는 스프링 빈은

<br/>AppConfig@CGLIB가 되는 것이다. 

<br/>이미 스프링 컨테이너에 등록이 되어 있으면 등록 되어 있는걸 뽑아주고, <br/>아니면 내가 만들었던 로직을 호출해서 뽑아 주는 것이다.

<br/>

## 정리

@Bean만 사용해도 스프링 빈으로 등록되지만 싱글톤을 보장하지 않는다.

- ‘memberRepository()’ 처럼 의존관계 주입이 필요해서 메서드를 직접 호출할 때 싱글톤을 보장하지 않는다.


### 크게 고민할 것이 없다. 스프링 설정 정보는 항상 ‘@Configuration’ 을 사용하자.

<br/>


>**Reference** <br/>스프링 핵심 원리 - 기본편 : https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8
