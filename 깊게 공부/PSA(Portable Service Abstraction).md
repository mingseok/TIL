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
<br/>

## 궁금증!

```java
"잘 만든 인터페이스" 라는 게 얼마나 단순한 것일까?
```

`@Transactional` 뒤에 있는 인터페이스를 꺼내봤다.

```java
$ javap org.springframework.transaction.PlatformTransactionManager

public interface PlatformTransactionManager extends TransactionManager {
  TransactionStatus getTransaction(TransactionDefinition definition);
  void commit(TransactionStatus status);
  void rollback(TransactionStatus status);
}
```

<br/>

메서드가 세 개뿐이다. `시작한다`, `커밋한다`, `롤백한다`.

트랜잭션이라는 개념에서 정말 빠질 수 없는 것만 남긴 것이다.

<br/>

## 그리고 구현체가 기술마다 하나씩 있다

```java
DataSourceTransactionManager   - JDBC 를 쓸 때
JdbcTransactionManager         - JdbcTemplate 계열
JpaTransactionManager          - JPA 를 쓸 때
HibernateTransactionManager    - 하이버네이트를 직접 쓸 때
JtaTransactionManager          - 여러 DB 를 묶는 분산 트랜잭션
```

<br/>

기술마다 트랜잭션을 다루는 방법이 전부 다르다.

```java
JDBC       -> connection.setAutoCommit(false) / connection.commit()
JPA        -> entityManager.getTransaction().begin() / commit()
```

<br/>

그런데 우리가 쓰는 코드는 어느 쪽이든 똑같다.

```java
@Transactional
public void order() { ... }
```

<br/>

JDBC에서 JPA로 바꿔도 이 코드는 한 글자도 안 바뀐다.

스프링이 등록된 `PlatformTransactionManager` 구현체를 바꿔 끼울 뿐이다.

```java
원문의 "기술이 바뀌어도 유연하게 대처할 수 있다" 가 이 모습이다
```

<br/>

## 세 글자가 각각 무엇을 뜻하는가

```java
Portable    - 옮길 수 있다. JDBC 코드를 JPA 로 바꿔도 내 코드는 그대로
Service     - 트랜잭션, 캐시, 메시징 같은 기술 서비스
Abstraction - 그 서비스들의 공통점만 뽑아 인터페이스로 만든 것
```

<br/>

`Portable` 이 핵심이다. 그냥 추상화가 아니라 `갈아끼워도 안 깨지는` 추상화라는 뜻이다.

<br/>

## 트랜잭션 말고 어떤 것들이 있는가

스프링에는 이런 추상화가 여러 겹 깔려 있다.

```java
PlatformTransactionManager  - 트랜잭션    (JDBC / JPA / JTA)
CacheManager                - 캐시        (Redis / Caffeine / EhCache)
MessageSource               - 메시지      (properties / DB)
Resource                    - 자원 읽기   (classpath / 파일 / URL)
DataAccessException         - 예외        (SQLException 을 기술 무관 예외로 변환)
```

<br/>

`CacheManager` 를 예로 들면, `@Cacheable` 을 붙인 코드는 그대로 두고

의존성만 바꿔서 로컬 캐시에서 Redis로 옮길 수 있다.

<br/>

`DataAccessException` 도 PSA의 좋은 예다.

앞의 예외 글에서 본 것처럼, `SQLException` 을 그대로 위로 올리면

서비스 계층이 JDBC를 알게 된다. 스프링이 이걸 잡아서 기술과 무관한 예외로 바꿔 던진다.

```java
DB가 다르면 에러 코드가 다르다
MySQL 중복키 = 1062, PostgreSQL 중복키 = 23505
-> 둘 다 DuplicateKeyException 으로 바뀌어 올라온다
```

<br/>

## 그래서 PSA와 AOP는 붙어 있다

`@Transactional` 을 다시 보면, 두 가지가 겹쳐 있다.

```java
PSA - "트랜잭션을 어떻게 다룰지" 를 인터페이스 뒤로 숨긴다
AOP - "언제 시작하고 언제 커밋할지" 를 프록시가 대신 해준다
```

<br/>

앞의 AOP 글에서 본 프록시가 메서드 앞뒤를 감싸면서

`getTransaction()` 과 `commit()` 을 대신 불러주는 것이다.

<br/>

그래서 우리 코드에는 어노테이션 한 줄만 남는다.

기술을 몰라도 되고(PSA), 부르는 코드도 안 적어도 되는(AOP) 것이다.

```java
PSA 없이 AOP만 있으면 -> 프록시가 JDBC 코드를 직접 부르게 되어 기술에 묶인다
AOP 없이 PSA만 있으면 -> 인터페이스는 깔끔한데 여전히 직접 불러야 한다
둘이 같이 있어야 @Transactional 한 줄이 된다
```

<br/>

## POJO 라는 말도 여기서 나온다

원문의 `POJO 기반으로 한번씩 더 추상화한 Layer` 가 이 얘기다.

<br/>

서블릿을 쓰려면 `HttpServlet` 을 상속해야 했다. 스프링 프레임워크에 묶이는 것이다.

`@Controller` 는 그냥 어노테이션이라, 그 클래스는 여전히 평범한 자바 클래스다.

<br/>

평범한 클래스면 테스트할 때도 그냥 `new` 해서 쓸 수 있다.

앞의 DI 글에서 생성자 주입으로 만든 객체를 순수 자바 테스트에서 바로 썼던 것이 그 결과다.

```java
POJO = Plain Old Java Object = 특정 기술에 묶이지 않은 평범한 자바 객체
```
