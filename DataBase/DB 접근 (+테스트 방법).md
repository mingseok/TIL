## DB 접근 (+테스트 방법)



데이터 접근 기술은 실제 데이터베이스에 접근해서 데이터를 잘 저장하고 

조회할 수 있는지 확인하는 것이 필요하다.

<br/>

## 테스트를 진행하기 전에 설정을 해야 한다.

`main` 디렉토리에 있는 `application.properties` 파일을 말하는 것이 아니다.

(`src/main/resources/application.properties` → 여기가 아님.)


<br/>


### 핵심은 test 디렉토리에 있는 곳.

`test` 디렉토리 밑에 있는 `application.properties` 에서 설정 해줘야 하는 것이다.

`src/test/resources/application.properties`

```java
spring.profiles.active=test // 맨위는 수정해야 됨.
spring.datasource.url=jdbc:h2:tcp://localhost/~/test
spring.datasource.username=sa

logging.level.org.springframework.jdbc=debug
```

`main` 디렉토리에 있는 `application.properties` 를 복사해서 가져 오면 된다.

- 하지만 `spring.profiles.active=test` 로 변경 해줘야 한다.

<br/><br/>

## 문제점.

로컬에서 사용하는 애플리케이션 서버와 테스트에서 같은 데이터베이스를 사용하고 있으니 테스트에서 문제가 발생한다. 

이런 문제를 해결하려면 테스트를 다른 환경과 철저하게 분리해야 한다.

<br/><br/>

## 해결 방법

H2 데이터베이스를 용도에 따라 2가지로 구분하면 된다.

- `jdbc:h2:tcp://localhost/~/test local`에서 접근하는 서버 전용 데이터베이스.

- `jdbc:h2:tcp://localhost/~/testcase test` 케이스에서 사용하는 전용 데이터베이스.

```java
조심해야 될 점은. DB를 하나 더 만들려고 한다면 현재 진행중인 DB를 껐다가 다시 켜서 만들어야 한다는 것이다. 
(그렇게 하지 않으면 안됨) 그리고 다시 처음 켰을때 하던 방식으로 진행 해야 된다.

나갔다 들어 왔을때 -> jdbc:h2:tcp://localhost/~/test 가 아니고
새로 만든 jdbc:h2:tcp://localhost/~/testcase 가 되어야 한다.

연동이 되었다면 처음부터 테이블 다시 만들고 해야 되는 것이다.
```

<br/><br/>

### 이렇게 DB가 분리 된 것을 알 수 있다.

![이미지](/programming/img/입문240.PNG)

<br/><br/>

## `test` 디렉토리 → `application.properties` 설정 변경

test → testcase 이름을 변경해줘야 한다.

```java
spring.profiles.active=test
spring.datasource.url=jdbc:h2:tcp://localhost/~/testcase
spring.datasource.username=sa

logging.level.org.springframework.jdbc=debug
```

<br/><br/>

## 테스트를 실행!

`findItems()` 테스트만 단독으로 실행해보자

- 처음에는 실행에 성공한다.

- 그런데 같은 `findItems()` 테스트를 다시 실행하면 테스트에 실패한다.

<br/><br/>

## 이유는?

처음 테스트를 실행할 때 저장한 데이터가 계속 남아있기 때문에 두번째 테스트에 영향을 준 것이다.

다른 테스트에서 이미 데이터를 추가했기 때문이다. 결과적으로 테스트 데이터가 오염된 것이다.

<br/><br/>

## 해결 방법

각각의 테스트가 끝날 때 마다 해당 테스트에서 추가한 데이터를 삭제해야 한다.

그래야 다른 테스트에 영향을 주지 않는다

<br/>

## 그리하여 테스트에서 중요한 원칙

- 테스트는 다른 테스트와 격리해야 한다.

- 테스트는 반복해서 실행할 수 있어야 한다

### 해결 방법은 트랜잭션이다