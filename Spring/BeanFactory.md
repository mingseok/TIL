### 인터페이스 끼리는 ‘상속’ 받는다고 표현한다.



![이미지](/programming/img/스프링12.PNG)

<br/>

## BeanFactory

- 스프링 컨테이너의 최상위 인터페이스 이다.
- 스프링 빈을 관리하고 조회하는 역할을 담당한다.
- `getBean()` 을 제공한다.
- 지금까지 우리가 사용했던 대부분의 기능은 BeanFactory가 제공하는 기능이다.

<br/>

## ApplicationContext

- BeanFactory 기능을 모두 상속 받아서 제공한다.
- 빈을 관리하고 검색하는 기능을 BeanFactory가 제공해주는데,

<br/>

### 그러면 둘의 차이가 뭘까?

애플리케이션을 개발할 때는 빈은 관리하고 조회하는 기능은 물론이고, 

수 많은 부가기능이 필요하다. !!

![이미지](/programming/img/스프링13.PNG)

<br/>

## 정리

- ApplicationContext는 BeanFactory의 기능을 상속받는다.

- ApplicationContext는 빈 관리기능 + 편리한 부가 기능을 제공한다.
- BeanFactory를 직접 사용할 일은 거의 없다. 부가기능이 포함된 ApplicationContext를 사용한다.
- BeanFactory나 ApplicationContext를 스프링 컨테이너라 한다.


<br/>


>**Reference** <br/>스프링 핵심 원리 - 기본편 : https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8
