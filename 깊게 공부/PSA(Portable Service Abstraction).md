## PSA(Portable Service Abstraction)



환경의 변화 없이 접근 환경을 제공하는 추상화 구조를 말합니다.

```java
"잘 만든 인터페이스 하나가 열 클래스 부럽지 않다"
```

PSA = 잘 만든 인터페이스

<br/><br/>

## Spring은

- Spring Web MVC

- Spring Transaction 등의 다양한 PSA를 제공합니다.

<br/><br/>

## Spring Web MVC

`Servlet`을 사용하려면 `HttpServlet`을 상속받고 

`doGet()`, `doPost()`등 오버라이딩하여 사용해야 한다.

<br/>

하지만, `Spring Web Mvc`에서는 일반 클래스에 `@Controller` 어노테이션을 

사용하면 요청을 매핑할 수 있는 컨트롤러 역할을 수행하는 클래스가 된다.

<br/>

그 클래스에서는 `@GetMapping`과 `@PostMapping` 어노테이션을 
사용해서 요청을 매핑할 수 있습니다.

서블릿을 `Low level` 로 개발하지 않고도, `Spring Web MVC`를 사용하면 
이렇게 서블릿을 간편하게 개발할 수 있다. 

<br/>

그 이유는 뒷단에 `spring`이 제공해주는 여러 기능들이 `숨겨져` 있기 때문입니다.

```java
Service Abstraction(서비스 추상화)의 목적 중 하나가 이러한 편의성을 제공하는 것입니다.
```

<br/>

이렇게 `Spring Web MVC`는 `@Controller`, `@RequestMapping` 과 같은 
어노테이션과 

뒷단의 여러가지 복잡한 인터페이스들 그리고 기술들을 기반으로 하여 사용자가 

기존 코드를 거의 변경하지 않고, 
웹 기술 스택을 간편하게 바꿀 수 있도록 해줍니다.

<br/><br/>

## Spring Transaction

### 트랜잭션이란?

트랜잭션 처리를 하려면 명시적으로 `setAutoCommit()`과
`commit()`, `rollback()`을 호출해야 합니다.

<br/>

하지만 `Spring`이 제공하는 `@Transactional`어노테이션을 
사용하면 

단순하게 메소드에 어노테이션을 붙여줌으로써
트랜잭션 처리가 간단하게 이루어집니다.

<br/>

기존 코드는 변경하지 않은 채로, 트랜잭션을 실제로 처리하는 

구현체를 사용 기술에 따라 바꿀 수 있는 것 입니다.

<br/><br/>

## Spring은 이렇게

특정 기술에 직접적 영향을 받지 않게끔 객체를 `POJO` 기반으로 

한번씩 더 추상화한 Layer를 갖고 있으며, 이를 통해 일관성있는 

`Service Abstraction(서비스 추상화)`를 만들어 냅니다.

<br/>

그렇기에, 코드는 더 견고해지고 기술이 바뀌어도 유연하게 대처할 수 있게 됩니다.