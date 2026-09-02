## DB 접근 (+테스트 방법)

데이터 접근 기술은 실제 데이터베이스에 접근해서 데이터를 잘 저장하고 

조회할 수 있는지 확인하는 것이 필요하다.

<br/><br/>

## 테스트를 진행하기 전에 설정을 해야 한다.

`src/main/resources/application.properties` → 있는

`application.properties` 파일을 말하는 것이 아님(X)

<br/><br/>

## 핵심은 test 디렉토리에 있는 곳.

`test` 디렉토리 밑에 있는 `application.properties` 에서 설정 해줘야 하는 것이다.

`src/test/resources/application.properties`

```java
spring.profiles.active=test
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

<br/><br/>

## 조심해야 될 점.

```
DB를 하나 더 만들려고 한다면 현재 진행중인 DB를 껐다가 다시 켜서 만들어야 한다는 것이다. 
(그렇게 하지 않으면 안됨) 그리고 다시 처음 켰을때 하던 방식으로 진행 해야 된다.

나갔다 들어 왔을때 -> jdbc:h2:tcp://localhost/~/test 가 아니고
새로 만든 jdbc:h2:tcp://localhost/~/testcase 가 되어야 한다.

연동이 되었다면 처음부터 테이블 다시 만들고 해야 되는 것이다.
```

<br/><br/>

## 이렇게 DB가 분리 된 것을 알 수 있다.

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

<br/><br/>

## 그리하여 테스트에서 중요한 원칙

- 테스트는 다른 테스트와 격리해야 한다.

- 테스트는 반복해서 실행할 수 있어야 한다

### 해결 방법은 트랜잭션이다
<br/>

## 궁금증!

```java
DB 가 있어야 하는 테스트와 없어도 되는 테스트를 어떻게 나눌까?
```

`무엇을 검증하려는가` 로 나뉜다.

```java
비즈니스 규칙을 검증한다     -> DB 가 필요 없다
쿼리가 맞는지 검증한다       -> DB 가 필요하다
매핑이 맞는지 검증한다       -> DB 가 필요하다
```

<br/>

## 비즈니스 규칙은 DB 없이 검증한다

앞의 서비스 계층 글에서 본 구조라면 가능하다.

```java
@Test
void 재고가_모자라면_주문할_수_없다() {
    MemberRepository fake = new FakeMemberRepository();
    OrderService service = new OrderService(fake);

    assertThatThrownBy(() -> service.order(1L, 100))
            .isInstanceOf(NotEnoughStockException.class);
}
```

<br/>

`재고가 모자라면 예외` 라는 규칙에 DB는 아무 상관이 없다.

<br/>

이 테스트는 밀리초 단위로 끝난다.

스프링 컨텍스트도 안 띄우고 DB도 안 뜨기 때문이다.

<br/>

수백 개를 돌려도 몇 초면 끝나니, 코드를 고칠 때마다 부담 없이 돌릴 수 있다.

<br/>

## 쿼리와 매핑은 DB 가 필요하다

```java
@DataJpaTest
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    void 이메일로_찾는다() {
        memberRepository.save(new Member("민석", "a@test.com"));

        Optional<Member> found = memberRepository.findByEmail("a@test.com");

        assertThat(found).isPresent();
    }
}
```

<br/>

앞의 스프링 데이터 JPA 글에서 본 대로, 메서드 이름이 SQL로 바뀌는지 확인하는 것이다.

이건 실제로 실행해봐야 안다.

<br/>

`@DataJpaTest` 는 JPA 관련 빈만 띄운다.

컨트롤러나 서비스는 안 띄우니 `@SpringBootTest` 보다 훨씬 빠르다.

그리고 기본으로 롤백되어서 테스트 사이에 데이터가 안 남는다.

<br/>

## 통합 테스트는 최소한만

```java
@SpringBootTest
class OrderIntegrationTest { ... }
```

<br/>

전체를 다 띄우니 제일 느리다. 컨트롤러부터 DB까지 실제로 흐르는지 확인할 때만 쓴다.

<br/>

그리고 이때 앞의 테스트 방법 글에서 본 함정들이 그대로 적용된다.

```java
@Transactional 을 붙이면 -> 지연 로딩이 되어서 운영과 다르게 통과할 수 있다
안 붙이면              -> 데이터가 남아서 다음 테스트에 영향을 준다
```

<br/>

그래서 통합 테스트는 개수를 줄이고, 대신 정말 중요한 흐름만 담는다.

<br/>

## 테스트 개수의 비율

```java
단위 테스트   - 많다  (빠르고 세밀하다)
통합 테스트   - 적다  (느리지만 실제와 가깝다)
```

<br/>

단위 테스트가 많아지려면 앞의 DI 글과 서비스 계층 글에서 본 구조가 필요하다.

의존성을 인터페이스로 받고 생성자로 주입받아야 가짜를 끼울 수 있다.

<br/>

거꾸로 말하면, `단위 테스트를 쓰기 어렵다` 는 것은 설계에 문제가 있다는 신호다.

```java
테스트하려고 DB 를 띄워야 한다      -> 서비스가 리포지토리 구현에 묶여 있다
테스트하려고 웹을 띄워야 한다       -> 로직이 컨트롤러에 있다
테스트하려고 외부 API 를 불러야 한다 -> 외부 호출이 인터페이스로 분리 안 됐다
```

<br/>

앞의 인터페이스 글에서 본 결제 예제가 그 해법이다.

`FakePayClient` 를 끼워 넣을 수 있으면 실제 결제 없이 테스트가 된다.
