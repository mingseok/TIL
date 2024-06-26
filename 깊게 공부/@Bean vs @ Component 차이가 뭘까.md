## @Bean vs @ Component 차이가 뭘까?



스프링 공부하다 `Bean`과 `@Component` 를 마주쳤다. 

```java
"똑같은거 아냐?"
```

<br>

결국, 두개는 빈을 등록할 때 사용하는 어노테이션인데, 

이 둘에 대해서, 자세히 어떻게 다른지에 대해 궁금하여 정리한다.

<br><br>

## @Bean

우리가 직접 제어할 수 없는 클래스들이 있다.

- 외부 라이브러리를 통해서 가져다가 사용하는 클래스에 대해서는
    
    `Bean`을 등록하기 위해 `@Bean`을 사용한다. (`Config` 클래스의 `@Config` 어노테이션도 같이)
    
- `@Bean`의 경우에는 메소드 레벨에서만 선언 가능하다는 점이다.

<br><br>

## @Component

개발자가 컨트롤 가능한 클래스에 대해서 `Bean`을 등록할 때 사용한다. 

- 우리가 직접 만든 자바 클래스에 대해서는 `@Component`를 사용한다.
- `@Component`는 클래스 레벨에서 선언되어야 한다.
- 스프링의 컴포넌트 스캔 기능이 `@Component` 어노테이션이 있는
    
    클래스를 자동으로 찾아서 빈으로 등록함
    
- `@Component` 하위 어노테이션으로 `@Controller`, `@Service`, `@Repository` 등이 있다.