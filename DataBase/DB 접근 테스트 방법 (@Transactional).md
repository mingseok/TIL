## DB 접근 테스트 방법 (Transactional)

<br/>

테스트를 진행 하면서 데이터를 이미 저장했다. 

그런데, 중간에 테스트가 실패해서 롤백을 호출하지 못해도 괜찮다. → `“이유는?”`

트랜잭션을 커밋하지 않았기 때문에 데이터베이스에 해당 데이터가 반영되지 않는다.

```
트랜잭션을 활용하면 테스트가 끝나고 나서 데이터를 깔끔하게 원래 상태로 되돌릴 수 있다.
```

<br/><br/>

## 동작 흐름

이렇게 해야, 다음 테스트에서 데이터로 인한 영향을 주지 않는다.

```
1. 트랜잭션 시작
2. 테스트 A 실행
3. 트랜잭션 롤백

4. 트랜잭션 시작
5. 테스트 B 실행
6. 트랜잭션 롤백
```

<br/><br/>

## @Transactional

스프링은 테스트 데이터 초기화를 위해 트랜잭션을 적용하고,

롤백 하는 방식을 `@Transactional` 애노테이션 하나로 깔끔하게 해결해준다.

<br/><br/>

## 사용 방법

클래스단에 `@Transactional` 애노테이션만 붙여 주면 끝이다.

- 만약, 해당 메서드만 적용하고 싶다면 클래스단 말고 해당 메서드에 붙여주면 된다.

```java
@Transactional
@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;
    // .. 생략
}
```

<br/><br/>

## @Transactional 원리

스프링이 제공하는 `@Transactional` 애노테이션은 원래 성공적으로 로직이 동작하면 커밋 하도록 동작한다.

그런데 `@Transactional` 애노테이션을 테스트 단에서 사용하면 아주 특별하게 동작한다.

```
`@Transactional` 이 테스트 단에 있으면 스프링은 테스트를 
트랜잭션 안에서 실행하고, 테스트가 끝나면 트랜잭션을 자동으로 롤백시켜 버린다.
```

<br/><br/>

## findItems() 메서드 동작 흐름

![이미지](/programming/img/입문249.PNG)

1. 테스트에 `@Transactional` 애노테이션이 테스트 메서드나 클래스 단에 있으면 먼저 트랜잭션을 시작한다.
    
2. 테스트 로직을 실행한다. → 테스트가 끝날 때 까지 모든 로직은 트랜잭션 안에서 수행된다.
3. 테스트 실행 중에 `INSERT SQL`을 사용해서 item1 , item2 , item3 를 데이터베이스에 저장한다.
    
    테스트가 리포지토리를 호출하고, 리포지토리는 `JdbcTemplate`을 사용해서 데이터를 저장한다.
    
4. 검증을 위해서 `SELECT SQL`로 데이터를 조회한다. 
    
    (커밋을 하지 않았는데 `SELECT SQL`가 되는 이유는?, 
    
    `임시 데이터` 라고 해도, 나의 트랜잭션에 넣은 것이기 때문에 조회가 가능한 것이다.)
    
    `SELECT SQL`도 같은 트랜잭션을 사용하기 때문에 저장한 `데이터를 조회`할 수 있는 것이다. 
    
    다른 트랜잭션에서는 `해당 데이터를 확인할 수 없다.`
    
5. `@Transactional` 이 테스트에 있으면 테스트가 끝날때 트랜잭션을 `강제로 롤백`한다.
6. 롤백에 의해 앞서 데이터베이스에 저장한 item1 , item2 , item3 의 데이터가 `제거`된다.

<br/><br/>

## 위 내용을 정리하면

`@BeforeEach` 실행, SQL 실행, `@AfterEach` 실행 과정을 자동으로 해준다고 생각하면 된다.

- `데이터소스`랑, `트랜잭션 매니저`는 `스프링 부트`가 자동으로 등록을 해준다.

```
`@BeforeEach` : 각각의 테스트 케이스를 실행하기 직전에 호출된다.
`@AfterEach` : 각각의 테스트 케이스가 완료된 직후에 호출된다.
```

<br/><br/>

## 처음엔 다 지우기.

![이미지](/programming/img/입문250.PNG)

<br/><br/>

### 롤백이 되면서, 정상 동작이 되는걸 알 수 있다.

![이미지](/programming/img/입문251.PNG)

<br/><br/>

### DB에도 없는 것을 알 수 있다.

![이미지](/programming/img/입문252.PNG)

<br/>

### 이렇게 하여,

이제 DB 테스트도 반복해서 돌릴 수 있게 되는 것이다.

평소 테스트 코드 작성하고 검사하는 것처럼 말이다.

테스트를 실행하기 전에 먼저 테스트에 영향을 주지 않도록 testcase 데이터베이스에 접근해서 기존 데이터를 깔끔하게 삭제하자.

<br/><br/>

## `@Commit` 애노테이션

@Transactional 을 테스트에서 사용하면 테스트가 끝나면 바로 롤백되기 때문에 테스트 과정에서 저장한 모든 데이터가 사라진다.



당연히 이렇게 되어야 하지만, 정말 가끔은 데이터베이스에 데이터가 잘 보관되었는지 최종 결과를 눈으로 확인하고 싶을 때도 있다.

```
이럴 때는 @Commit 을 클래스 또는 메서드에 붙이면 테스트 종료 후 롤백 대신 커밋이 호출된다.
```

<br/>

### 사용 방법

```java
@Commit
@Test
void save() {
   // .. 생략
}
```

<br/>

### 출력

![이미지](/programming/img/입문253.PNG)

<br/><br/>

## 정리

- 테스트가 끝난 후 개발자가 직접 데이터를 삭제하지 않아도 되는 편리함을 제공한다.
- 테스트 실행 중에 데이터를 등록하고 중간에 테스트가 강제로 종료되어도 걱정이 없다.
    
    이 경우 트랜잭션을 커밋하지 않기 때문에, 데이터는 자동으로 롤백된다. 
    
- 트랜잭션 범위 안에서 테스트를 진행하기 때문에 동시에 다른 테스트가 진행되어도 서로 영향을 주지 않는 장점이 있다.
    
- `@Transactional` 덕분에 아주 편리하게 다음 원칙을 지킬 수 있게 되었다.
    - 테스트는 다른 테스트와 격리해야 한다.

    - 테스트는 반복해서 실행할 수 있어야 한다.


<br/><br/>

>**Reference** <br/>[스프링 DB 2편 - 데이터 접근 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-db-2/dashboard)


<br/>

## 궁금증!

```java
테스트에 @Transactional 을 붙이면 롤백된다는데, 운영 코드와 다르게 동작하는 걸까?
```

다르다. 테스트에서만 자동 롤백된다.

```java
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Test
    void join() {
        memberService.join("민석");
        // 테스트가 끝나면 자동으로 롤백된다
    }
}
```

<br/>

일반 코드에서는 예외가 없으면 커밋되는데, 테스트에서는 성공해도 롤백된다.

`TestTransaction` 이 기본값을 `롤백` 으로 뒤집어놨기 때문이다.

<br/>

이렇게 하면 테스트를 몇 번 돌려도 DB가 깨끗하게 유지된다.

앞의 테스트 방법 글에서 본 `테스트 간 격리` 문제가 이걸로 해결된다.

<br/>

## 그런데 이 편리함에 함정이 있다

### 첫번째) 테스트가 통과했는데 운영에서 터진다

```java
@Test
@Transactional
void join() {
    memberService.join("민석");
    memberService.join("민석");     // 중복인데 통과할 수도 있다
}
```

<br/>

`UNIQUE` 제약 위반은 대개 `INSERT` 가 실제로 나갈 때 걸린다.

그런데 JPA는 커밋 직전까지 쓰기를 미룬다. 롤백되면 `INSERT` 가 아예 안 나갈 수 있다.

<br/>

`flush()` 를 명시적으로 부르거나, 롤백을 끄면 드러난다.

```java
@Test
@Transactional
void join() {
    memberService.join("민석");
    entityManager.flush();          // 여기서 DB 로 나간다
}
```

<br/>

### 두번째) 지연 로딩이 테스트에서만 된다

```java
@Test
@Transactional
void find() {
    Order order = orderService.find(1L);
    order.getMember().getName();     // 테스트에서는 된다
}
```

<br/>

테스트 메서드 전체가 하나의 트랜잭션이라, 서비스가 끝나도 세션이 살아 있다.

<br/>

그런데 운영에서는 서비스 메서드가 끝나면 트랜잭션도 끝난다.

앞의 DB 접근(JPA) 글에서 본 `LazyInitializationException` 이 거기서 터진다.

```java
테스트 - 트랜잭션이 테스트 메서드 전체를 감싼다 -> 지연 로딩이 된다
운영   - 트랜잭션이 서비스 메서드에서 끝난다   -> 지연 로딩이 안 된다
```

<br/>

이 차이 때문에 `테스트는 통과하는데 운영에서 터지는` 상황이 생긴다.

<br/>

### 세번째) 실제로 커밋됐는지 확인이 안 된다

```java
@Test
@Transactional
void order() {
    orderService.order(...);
    assertThat(orderRepository.count()).isEqualTo(1);   // 통과한다
}
```

<br/>

같은 트랜잭션 안에서 조회하니 내가 넣은 것이 보인다.

앞의 transaction 글에서 본 `트랜잭션 안에서는 내가 바꾼 값이 보인다` 가 그대로 적용된다.

<br/>

정말 커밋됐는지 보려면 롤백을 꺼야 한다.

```java
@Test
@Commit                 // 또는 @Rollback(false)
void order() { ... }
```

<br/>

대신 데이터가 남으니 다음 테스트에 영향을 준다.

<br/>

## 그래서 나눠서 쓴다

```java
단위 테스트         - 스프링 없이. 가짜 리포지토리로 (제일 빠르다)
@DataJpaTest       - JPA 관련 빈만 띄운다. 기본으로 롤백된다
@SpringBootTest    - 전체를 띄운다. 통합 테스트
```

<br/>

앞의 서비스 계층 글에서 본 대로, 비즈니스 로직은 단위 테스트로 검증하는 것이 좋다.

DB가 필요한 것만 `@DataJpaTest` 로 확인하면 전체 테스트가 훨씬 빨라진다.

<br/>

## 롤백 대신 지우는 방법도 있다

`@Transactional` 을 안 쓰고 매번 테이블을 비우는 방식이다.

```java
@AfterEach
void clear() {
    jdbcTemplate.execute("TRUNCATE TABLE orders");
    jdbcTemplate.execute("TRUNCATE TABLE member");
}
```

<br/>

느리지만 운영과 똑같이 동작한다. 위의 세 가지 함정이 전부 사라진다.

<br/>

앞의 DML/DDL 글에서 본 대로 `TRUNCATE` 는 `DELETE` 보다 훨씬 빠르다.

다만 외래 키가 걸려 있으면 순서를 맞춰야 하고, 제약을 잠깐 꺼야 할 수도 있다.

```sql
SET REFERENTIAL_INTEGRITY FALSE;    -- H2
```

<br/>

정확성이 중요한 핵심 흐름만 이렇게 테스트하고, 나머지는 롤백 방식을 쓰는 것이 절충안이다.
