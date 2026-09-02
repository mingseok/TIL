## 커넥션 풀, DataSource

<br/>

![이미지](/programming/img/입문241.PNG)



### 데이터베이스 커넥션을 획득할 때는 다음과 같은 복잡한 과정을 거친다.

1. 애플리케이션 로직은 `DB 드라이버`를 통해 `커넥션`을 조회한다.

2. DB 드라이버는 `DB`와 `TCP/IP 커넥션`을 연결한다. 물론 이 과정에서 
    
    - `3 way handshake` 같은 `TCP/IP` 연결을 위한 네트워크 동작이 발생한다.
    
3. DB 드라이버는 TCP/IP 커넥션이 연결되면 `ID`, `PW`와 기타 부가정보를 `DB에 전달`한다.

4. DB는 ID, PW를 통해 `내부 인증을 완료`하고, 내부에 `DB 세션을 생성`한다.

5. DB는 `커넥션 생성`이 완료되었다는 응답을 보낸다.

6. DB 드라이버는 `커넥션 객체를 생성`해서 `클라이언트에 반환`한다.

<br/><br/>

## 문제점.

```
커넥션을 새로 만드는 것은 과정도 복잡하고 시간도 많이 많이 소모되는 일이다.
```

고객이 애플리케이션을 사용할 때, SQL을 실행하는 시간 뿐만 아니라 

커넥션을 새로 만드는 시간이 추가되기 때문에 결과적으로 응답 속도에 영향을 준다. 



이것은 사용자에게 좋지 않은 경험을 줄 수 있다. 

(1초라도 화면에 늦게 뜨면 나감)

<br/><br/>

## 이런 문제를 해결하려면?

커넥션을 미리 생성해두고, 사용하는 `커넥션 풀`이라는 방법이다.

커넥션 풀은 이름 그대로 커넥션을 관리하는 풀(수영장 풀을 상상하면 된다.)이다.

<br/><br/>

## 커넥션 풀 초기화

![이미지](/programming/img/입문242.PNG)

보통 얼마나 보관할 지는 서비스의 특징과 서버 스펙에 따라 다르지만 기본값은 보통 10개이다

<br/><br/>

## 커넥션 풀의 연결 상태

![이미지](/programming/img/입문243.PNG)

커넥션 풀에 들어 있는 커넥션은 TCP/IP로 DB와 커넥션이 연결되어 있는 상태이기 

때문에 언제든지 즉시 SQL을 DB에 전달할 수 있다.

<br/><br/>

## 커넥션 풀 사용) 1단계

![이미지](/programming/img/입문244.PNG)

DB 드라이버를 통해서 새로운 커넥션을 획득하는 것이 아니다.

커넥션 풀을 통해 이미 생성되어 있는 커넥션을 객체 참조로 가져다 쓰기만 하면 된다.

<br/><br/>

## 커넥션 풀 사용) 2단계

![이미지](/programming/img/입문245.PNG)

커넥션을 모두 사용하고 나면 이제는 커넥션을 종료하는 것이 아니라, 

다음에 다시 사용할 수 있도록 해당 커넥션을 그대로 커넥션 풀에 반환하면 된다. 

```
주의할 점은 커넥션을 종료하는 것이 아니라 
커넥션이 살아있는 상태로 커넥션 풀에 반환 한다는 것이다.
```

<br/><br/>

## DataSource

![이미지](/programming/img/입문246.PNG)

자바에서는 `DataSource` 라는 인터페이스를 제공한다.

`DataSource` 는 커넥션을 획득하는 방법을 `추상화` 하는 인터페이스이다.

이 인터페이스의 핵심 기능은 `커넥션 조회 하나`이다.

<br/><br/>

## 정리

대부분의 커넥션 풀은 DataSource 인터페이스를 이미 구현해두었다. 

개발자는 `DBCP2 커넥션 풀`, `HikariCP 커넥션 풀` 의 코드를 직접 의존하는 것이 아니라 

`DataSource 인터페이스에만` 의존하도록 애플리케이션 로직을 작성하면 된다.

```
커넥션 풀 구현 기술을 변경하고 싶으면 해당 구현체로 갈아끼우기만 하면 된다는 뜻이다.
```

<br/><br/>

자바는 `DataSource` 를 통해 커넥션을 획득하는 방법을 추상화 했다. 

이제 애플리케이션 로직은 DataSource 인터페이스에만 의존하면 된다

<br/>

`DriverManagerDataSource`또는, `HikariDataSource` 로 변경해도 `MemberRepositoryV1` 의 

코드는 전혀 변경하지 않아도 된다. 

<br/>

`MemberRepositoryV1` 는 `DataSource` 인터페이스에만 의존하기 때문이다. 

이것이 `DataSource` 를 사용하는 장점이다.`(DI + OCP)`


<br/><br/>

>**Reference** <br/>[스프링 DB 2편 - 데이터 접근 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-db-2/dashboard)


<br/>

## 궁금증!

```java
DataSource 라는 인터페이스를 왜 굳이 하나 더 뒀을까?
```

앞의 PSA 글에서 본 것과 같은 이유다. 구현을 갈아끼우기 위해서다.

```java
public interface DataSource {
    Connection getConnection() throws SQLException;
}
```

<br/>

메서드가 사실상 이것 하나다. `커넥션을 하나 달라` 는 것뿐이다.

<br/>

이 인터페이스 뒤에 무엇이 있는지는 쓰는 쪽이 몰라도 된다.

```java
DriverManagerDataSource - 부를 때마다 새로 연결한다 (풀 없음)
HikariDataSource        - 풀에서 빌려준다
개발용 임베디드 DataSource - 메모리 DB 에 연결한다
```

<br/>

셋 다 `getConnection()` 하나만 제공한다.

그래서 설정만 바꾸면 코드를 안 고치고 갈아끼울 수 있다.

<br/>

## 진짜 커넥션이 아니라는 것이 핵심이다

풀을 쓸 때 `getConnection()` 이 돌려주는 것은 진짜 커넥션이 아니다.

```java
Connection connection = dataSource.getConnection();
System.out.println(connection.getClass().getName());
// -> com.zaxxer.hikari.pool.HikariProxyConnection
```

<br/>

`HikariProxyConnection` 이라는 이름에 `Proxy` 가 들어 있다.

<br/>

이 프록시가 하는 일은 하나다. `close()` 를 가로채는 것이다.

```java
우리 코드 -> connection.close()
프록시    -> 진짜로 안 끊고 풀에 반납한다
```

<br/>

앞의 AOP 글에서 본 프록시와 같은 발상이다.

`쓰는 쪽 코드를 안 바꾸면서 동작만 바꾼다` 는 목적도 같다.

<br/>

그래서 우리는 풀을 쓰든 안 쓰든 항상 같은 코드를 쓴다.

```java
try (Connection connection = dataSource.getConnection()) {
    // ...
}   // 여기서 close() -> 풀이면 반납, 아니면 진짜 종료
```

<br/>

앞의 try-with-resources 글에서 본 그 문법이 여기서도 그대로 쓰인다.

<br/>

## 스프링이 여기에 한 겹 더 얹는다

트랜잭션이 걸려 있으면 `같은 커넥션을 계속 써야` 한다.

메서드 안에서 여러 번 조회하는데 매번 다른 커넥션을 쓰면 같은 트랜잭션이 아니게 된다.

<br/>

그래서 스프링은 `DataSourceUtils` 라는 것을 거친다.

```java
DataSourceUtils.getConnection(dataSource)
  -> 현재 스레드에 트랜잭션이 있나?
       있으면 -> 그 트랜잭션이 쓰던 커넥션을 그대로 준다
       없으면 -> dataSource.getConnection() 을 부른다
```

<br/>

`현재 스레드` 에 보관해두는 것이 포인트다.

앞의 서블릿 글에서 본 대로 요청 하나에 스레드 하나이므로,

스레드에 매달아두면 그 요청 안에서만 공유된다.

<br/>

이걸 담당하는 것이 `TransactionSynchronizationManager` 다.

내부는 `ThreadLocal` 하나다.

```java
private static final ThreadLocal<Map<Object, Object>> resources = new NamedThreadLocal<>(...);
```

<br/>

`ThreadLocal` 은 스레드마다 다른 값을 갖는 저장소다.

앞의 변수 할당 글에서 본 `스택은 스레드마다 따로` 와 같은 효과를 힙에서 내는 것이다.

<br/>

## 그래서 @Transactional 안에서는 커넥션이 하나다

```java
@Transactional
public void order() {
    memberRepository.find(1L);      // 커넥션 A
    itemRepository.find(2L);        // 커넥션 A (같은 것)
    orderRepository.save(order);    // 커넥션 A (같은 것)
}
```

<br/>

세 번 조회했지만 커넥션은 하나다.

트랜잭션이 시작될 때 하나 빌려서 `ThreadLocal` 에 넣어두고, 셋 다 그것을 꺼내 쓴다.

<br/>

그리고 이래서 트랜잭션이 길면 커넥션이 오래 묶인다.

앞의 DBCP 글에서 본 `풀이 마른다` 는 문제의 정확한 원인이 이 구조다.

<br/>

## 비동기에서 주의할 점

`ThreadLocal` 이라서 스레드가 바뀌면 못 찾는다.

```java
@Transactional
public void order() {
    CompletableFuture.runAsync(() -> {
        memberRepository.save(member);    // 다른 스레드다. 이 트랜잭션에 안 묶인다
    });
}
```

<br/>

새 스레드에는 `ThreadLocal` 값이 없으니 커넥션을 새로 빌린다.

바깥 트랜잭션과는 완전히 별개로 돈다. 바깥이 롤백돼도 이건 커밋된다.

<br/>

앞의 `@Async` 이벤트 리스너에서 트랜잭션을 따로 챙겨야 한다고 한 이유가 이것이다.

`ThreadLocal` 은 스레드 경계를 못 넘는다.
