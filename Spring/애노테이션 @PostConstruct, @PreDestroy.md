## 애노테이션 @PostConstruct, @PreDestroy

<br/>
결론부터 말하자면 이 방법을 사용하면 되는것이다.

위치, 코드 수정

![이미지](/programming/img/스프링35.PNG)

<br/>

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
