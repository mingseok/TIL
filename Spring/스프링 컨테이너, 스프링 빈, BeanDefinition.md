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
