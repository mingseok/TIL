## 애노테이션 @PostConstruct, @PreDestroy

<br/>

결론부터 말하자면 이 방법을 사용하면 되는것이다.

<br/>

@PostConstruct는 의존성 주입이 이루어진 후 초기화를 수행하는 메서드이다. <br/>@PostConstruct가 붙은 메서드는 클래스가 service(로직을 탈 때? 로 생각 됨)를 수행하기 전에 발생한다. <br/>이 메서드는 다른 리소스에서 호출되지 않는다해도 수행된다

<br/>

왜 사용하는가?

1) 생성자(일반)가 호출 되었을 때, 빈(bean)은 아직 초기화 되지 않았다. (예를 들어, 주입된 의존성이 없음)

    하지만, @PostConstruct를 사용하면, 빈(bean)이 초기화 됨과 동시에 의존성을 확인할 수 있다. 


<br/><br/>

위치, 코드 수정

![이미지](/programming/img/스프링35.PNG)


## @PostConstruct, @PreDestroy 애노테이션 특징

- 최신 스프링에서 가장 권장하는 방법이다.

- 애노테이션 하나만 붙이면 되므로 매우 편리하다.
- 패키지를 잘 보면 javax.annotation.PostConstruct 이다. <br/>스프링에 종속적인 기술이 아니라 
JSR-250 라는 자바 표준이다. 따라서 스프링이 아닌 다른 컨테이너에서도 동작한다.
- 컴포넌트 스캔과 잘 어울린다.
- 유일한 단점은 외부 라이브러리에는 적용하지 못한다는 것이다. 외부 라이브러리를 초기화, 
종료 해야 하면 @Bean의 기능을 사용하자.

<br/>

### 정리

- @PostConstruct, @PreDestroy 애노테이션을 사용하자

- 코드를 고칠 수 없는 외부 라이브러리를 초기화, 종료해야 하면 @Bean 의 initMethod , destroyMethod를 사용하자


<br/>

>**Reference** <br/>스프링 핵심 원리 - 기본편 : https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8
