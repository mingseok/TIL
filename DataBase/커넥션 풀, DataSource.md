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

