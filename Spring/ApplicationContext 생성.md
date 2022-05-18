## 스프링 컨테이너 생성 과정.

```java
ApplicationContext applicationContext
                = new AnnotationConfigApplicationContext(AppConfig.class);
```

- ApplicationContext 를 스프링 컨테이너라 한다.
- ApplicationContext 는 인터페이스이다.
- 자바 설정 클래스를 기반으로 스프링 컨테이너( ApplicationContext )를 만들어보자.<br/>
new AnnotationConfigApplicationContext(AppConfig.class); 생성자에 파라미터로 클래스를 매개변수로 넣어준 것이다. 이 클래스는 ApplicationContext 인터페이스의 구현체이다

<br/>

추가로 위에 코드처럼 이렇게 AppConfig.class 를 넘기면 AppConfig클래스도 빈에 등록이 된다.

```java
new AnnotationConfigApplicationContext(AppConfig.class);
```

<br/>

## 스프링 컨테이너의 생성 과정

### 스프링 컨테이너 생성

![이미지](/programming/img/스프링7.PNG)

- new AnnotationConfigApplicationContext(AppConfig.class)
- 스프링 컨테이너를 생성할 때는 구성 정보를 지정해주어야 한다.
- 여기서는 AppConfig.class 를 구성 정보로 지정했다.

<br/>

### 2. 스프링 빈 등록

![이미지](/programming/img/스프링8.PNG)

- 스프링 컨테이너는 파라미터로 넘어온 설정 클래스 정보를 사용해서 스프링 빈을 등록한다.
- ‘키’ 가 메서드 이름이다.
- 저장 되어 있는 ‘빈 이름’ 과 ‘빈 객체’ 를 ‘스프링 빈’ 이라고 한다.

<br/>

### 빈 이름

- 빈 이름은 메서드 이름을 사용한다.
- 빈 이름을 직접 부여할 수 도 있다.

```java
@Bean(name="memberService2")
```

<br/>

### 주의: 빈 이름은 항상 다른 이름을 부여해야 한다.

같은 이름을 부여하면, 다른 빈이 무시되거나, 기존 빈을 덮어버리거나 설정에 따라 오류가 발생한다

<br/>

### 3. 스프링 빈 의존관계 설정 - 준비

![이미지](/programming/img/스프링9.PNG)

<br/>

### 4. 스프링 빈 의존관계 설정 - 완료

화살표 방향을 잘보자. 한곳에 여러개가 의존 될 수 있다.

![이미지](/programming/img/스프링10.PNG)

스프링 컨테이너는 설정 정보를 참고해서 의존관계를 주입(DI)한다.

단순히 자바 코드를 호출하는 것 같지만, 차이가 있다. 

<br/>

이 차이는 뒤에 싱글톤 컨테이너에서 설명한다.

<br/>


>**Reference** <br/>스프링 핵심 원리 - 기본편 : https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8
