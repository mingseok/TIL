## 트랜잭션 AOP, @Transactional

<br/>

## 고민

서비스 계층에 `순수한 비즈니스 로직`만 남기고 싶을땐 어떻게 해야 될까?

이럴 때 `스프링 AOP`를 통해 `프록시`를 도입하면 문제를 깔끔하게 해결할 수 있다.

```
@Transactional 을 사용하면 스프링이 AOP를 사용해서 트랜잭션을 편리하게 처리해준다 
```

<br/><br/>

## 프록시 도입 전

![이미지](/programming/img/입문237.PNG)

<br/>

## 프록시 도입 후

![이미지](/programming/img/입문238.PNG)

서비스 로직을 그대로 두고, 트랜잭션 프록시 라는 것을 앞에 하나 만든다!

<br/><br/>

## 프록시란?

```
대신 무언가를 처리해주는 사람(?) 을 말한다.
```

위 그림으로 설명하자면, 클라이언트가 서비스를 직접 호출하는 것이 아니다. 

1. 클라이언트는 프록시를 호출하고

2. 프록시에서 `트랜잭션 시작`을 하고
3. 실제 서비스 로직을 호출해준다.

그리하여, 트랜잭션 종료까지 담당하게 되는 것이다.

<br/>

### 프록시는 스프링이 다 만들어 준다.

```
프록시를 사용하면 트랜잭션을 처리하는 객체와 
비즈니스 로직을 처리하는 서비스 객체를 명확하게 분리할 수 있다.
```

<br/><br/>

## 트랜잭션 프록시 코드 예시

주석을 보면 하는 일들을 알 수 있다. → 이런 일들을 한다고 생각하기.

```java
public class TransactionProxy {
    private MemberService target;

    public void logic() {
        
        //트랜잭션 시작
        TransactionStatus status = transactionManager.getTransaction(..);
        
        try {
            //실제 대상 호출
            target.logic();
            transactionManager.commit(status); //성공시 커밋
        } catch (Exception e) {
            transactionManager.rollback(status); //실패시 롤백
            throw new IllegalStateException(e);
        }
    }
}
```


<br/><br/>

## 트랜잭션 프록시 적용 후 서비스 코드 예시

```java
public class Service {
	 public void logic() {
			 // 트랜잭션 관련 코드 제거, 순수 비즈니스 로직만 남음
			 bizLogic(fromId, toId, money);
	 }
}
```

`프록시 도입 전:` 서비스에 비즈니스 로직과 트랜잭션 처리 로직이 함께 섞여있다. (위 그림 참고)

`프록시 도입 후:` 트랜잭션 프록시가 트랜잭션 처리 로직을 모두 가져간다.

<br/>

그리고 트랜잭션을 시작한 후에 실제 서비스를 대신 호출한다. 

트랜잭션 프록시 덕분에 서비스 계층에는 순수한 비즈니즈 로직만 남길 수 있다.


<br/><br/>

## 스프링이 제공하는 트랜잭션 AOP

스프링은 트랜잭션 AOP를 처리하기 위한 모든 기능을 제공한다.

스프링 부트를 사용하면 트랜잭션 AOP를 처리하기 위해 필요한 스프링 빈들도 자동으로 등록한다.

<br/>

개발자는 트랜잭션 처리가 필요한 곳에 `@Transactional` 애노테이션만 붙여주면 된다. 

스프링의 `트랜잭션 AOP`는 이 애노테이션을 인식해서 `트랜잭션 프록시`를 적용해준다.

<br/>

### 자세히 뭔지는 모르겠지만,

```
"@Transactional 애노테이션을 사용하면 스프링이 프록시라는 것을 
(‘트랜잭션 프록시’ 맨 앞에 생기는 것) 만들어서 넣어주고 클라이언트를 
대신해 트랜잭션 처리를 한다" 라고 생각하기.
```


<br/><br/>

## @Transactional

스프링이 `@Transactional` 본다면 AOP 적용의 대상이라고 생각한다.

즉, 프록시 대상이라고 판단한다. (`EnhancerBySpringCGLIB..`) 라고 출력 된다.

(`CGLIB` 라이브러리를 통해서 프록시라는 것을 만들어 낸다.)

<br/>

`@Transactional` 애노테이션은 메서드에 붙여도 되고, 클래스에 붙여도 된다. 

클래스에 붙이면 외부에서 호출 가능한 `public` 메서드가 `AOP` 적용 대상이 된다.




<br/><br/>

## 사용 방법

```java
@Transactional
public void accountTransfer(String fromId, String toId, int money) throws SQLException {
		bizLogic(fromId, toId, money);
}
```

`설명:` @Transactional 애노테이션이 붙은 메서드가 실행 될 때, 트랜잭션 걸고 실행 하겠다는 것이다. 

메서드가 종료 될 시 성공하면 `커밋`을 진행하고, 런타임 에러 같은 예외가 발생한다면 → 즉, 실패하면 `롤백` 하는 것이다.


<br/><br/>

## 트랜잭션 AOP 정리

파란색 부분이 스프링이 처리해 주는 것이다. (`@Transactional` 만으로 가능)

![이미지](/programming/img/입문239.PNG)

<br/>

## 흐름 설명

1. 처음 클라이언트에서 `@Transactional` 라고 되어 있으면, 스프링이 

    “너는 트랜잭션을 적용하는 프록시를 만들어야 겠다”고 판단한다.
    
    즉, 1번 AOP 프록시를 호출하는 것이다.
    
2. AOP 프록시 내부 코드에서는 `‘트랜잭션 시작’`을 한다. 
    
    그런데 혼자는 것이 아니고 ‘트랜잭션 매니저'를 통해서 시작한다. 
    
    (프록시가 내부적으로 스프링 빈에 등록 되어 있는 ‘트랜잭션 매니저’를 찾아서 사용한다.)
    
3. 트랜잭션 매니저를 획득 한 다음에서야 `‘트랜잭션 매니저'` 를 시작한다.
4. 트랜잭션 매니저 에서는 `데이터 소스`로 → `커넥션`을 만든다.
5. 그리고 `setAotoCommit(false)` 로 하여 `수동모드`로 만드는 것이다.
6. 해당 커넥션을 동기화 해야 하는 것이다. → 트랜잭션 동기화 매니저에 `보관` 한다.
7. 그렇게 트랜잭션 시작을 하고 → AOP 프록시에서 `실제 서비스 로직`을 `호출`한다. 
    
    (비즈니스 로직을 말함) 그리고 서비스에서 → 리포지토리를 호출한다.
    
8. 그리고 리포지토리에서는 동기화 되어 있는 `트랜잭션 동기화 매니저`에서 `꺼내` 사용한다.
9. 리포지토리에서 작업이 끝난다면 → 서비스 → AOP 프록시로 돌아간다. 
10. 돌아간 다음 성공이면 `‘커밋’` 실패면 `‘롤백’` 을 진행한 다음 클라이언트로 반환이 되는 것이다.

<br/><br/>

>**Reference** <br/>[스프링 DB 2편 - 데이터 접근 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-db-2/dashboard)


<br/>

## 궁금증!

```java
@Transactional 을 붙이면 스프링이 정확히 무엇을 끼워 넣는 걸까?
```

앞의 AOP 글에서 본 프록시가 메서드를 이렇게 감싼다.

```java
프록시.order()
  1. 트랜잭션 매니저에게 트랜잭션을 시작해달라고 한다
       -> 커넥션을 하나 빌리고 setAutoCommit(false)
       -> 그 커넥션을 ThreadLocal 에 넣어둔다
  2. 진짜 order() 를 부른다
  3-a. 예외 없이 끝나면 -> commit()
  3-b. 언체크 예외가 나면 -> rollback()
  4. 커넥션을 반납하고 ThreadLocal 을 비운다
```

<br/>

`1번` 과 `4번` 이 우리가 손으로 적던 것이다.

앞의 스프링으로 트랜잭션 해결 글에서 본 그 `try-catch-finally` 다.

<br/>

## 프록시가 실제로 씌워졌는지 확인하는 법

```java
@Autowired ApplicationContext context;

@Test
void proxyCheck() {
    OrderService service = context.getBean(OrderService.class);
    System.out.println(service.getClass());
    System.out.println(AopUtils.isAopProxy(service));
}
```

<br/>

```java
class com.example.OrderService$$SpringCGLIB$$0
true
```

<br/>

앞의 AOP 글과 `@Configuration` 글에서 본 그 `$$SpringCGLIB$$0` 이다.

이 이름이 안 나오면 `@Transactional` 이 아무 일도 안 하고 있는 것이다.

<br/>

## 트랜잭션이 실제로 걸렸는지 확인하는 법

프록시가 씌워져 있어도 내부 호출이면 안 걸린다.

앞의 트랜잭션 AOP 주의 사항 글에서 본 그 문제다.

<br/>

실행 중에 확인하는 방법이 있다.

```java
TransactionSynchronizationManager.isActualTransactionActive();   // true 여야 한다
TransactionSynchronizationManager.getCurrentTransactionName();   // 어느 메서드의 트랜잭션인지
```

<br/>

로그로 보는 방법도 있다.

```java
logging.level.org.springframework.transaction.interceptor=TRACE
```

```java
Getting transaction for [com.example.OrderService.order]
Completing transaction for [com.example.OrderService.order]
```

<br/>

이 줄이 안 나오면 프록시를 안 거친 것이다.

<br/>

## 어디에 붙이느냐에 따라 범위가 달라진다

```java
@Transactional
public class OrderService { ... }        // 모든 public 메서드에 적용

public class OrderService {
    @Transactional
    public void order() { ... }          // 이 메서드만
}
```

<br/>

클래스와 메서드 둘 다 있으면 메서드 쪽이 이긴다.

```java
@Transactional(readOnly = true)          // 기본은 읽기 전용
public class MemberService {

    @Transactional                        // 이 메서드만 쓰기 가능
    public void join(String name) { ... }

    public Member find(Long id) { ... }   // 읽기 전용
}
```

<br/>

앞의 트랜잭션 옵션 글에서 본 `조회에는 readOnly` 를 이렇게 적용하면 편하다.

기본을 읽기 전용으로 두고 쓰는 메서드만 표시하는 것이다.

<br/>

## 인터페이스에 붙이면 안 되는 이유

```java
public interface OrderService {
    @Transactional
    void order();
}
```

<br/>

동작은 하는데 권장되지 않는다.

<br/>

CGLIB 프록시는 클래스를 상속해서 만들기 때문에, 인터페이스의 어노테이션을 못 볼 수 있다.

스프링 부트는 기본이 CGLIB이라 이 경우에 해당한다.

<br/>

그래서 구현 클래스에 붙이는 것이 확실하다.

```java
@Service
public class OrderServiceImpl implements OrderService {

    @Transactional
    public void order() { ... }      // 여기에 붙인다
}
```

<br/>

## 스프링 어노테이션과 자카르타 어노테이션이 따로 있다

```java
org.springframework.transaction.annotation.Transactional    -- 스프링
jakarta.transaction.Transactional                           -- 자바 표준
```

<br/>

IDE에서 자동 완성할 때 잘못 고르기 쉽다.

<br/>

자바 표준 쪽은 `readOnly` 옵션이 없고, 롤백 규칙 지정 방식도 다르다.

앞의 트랜잭션 옵션 글에서 본 옵션들을 쓰려면 스프링 쪽이어야 한다.

```java
import 가 org.springframework 로 시작하는지 확인하는 습관을 들이면 된다
```
