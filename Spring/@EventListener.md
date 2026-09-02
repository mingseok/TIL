## @EventListener

<br/>

## 애플리케이션을 실행할 때 초기 데이터를 저장한다

```java
@Slf4j
@RequiredArgsConstructor
public class TestDataInit {

    private final ItemRepository itemRepository;

    /**
     * 확인용 초기 데이터 추가
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        log.info("test data init");
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}
```

- 리스트에서 데이터가 잘 나오는지 편리하게 확인할 용도로 사용한다
- `@EventListener(ApplicationReadyEvent.class)` : 스프링 컨테이너가 완전히 초기화를 다 끝내고,
    
    실행 준비가 되었을 때 발생하는 이벤트이다.
    
- `@EventListener(ApplicationReadyEvent.class)` 는 AOP를 포함한 스프링 컨테이너가 완전히
    
    초기화 된 이후에 호출되기 때문에 이런 문제가 발생하지 않는다

<br/><br/>

## /src/main/resources 하위의 application.properties

```
spring.profiles.active=local
```

<br/><br/>

## ItemServiceApplication

```java
@Import(MemoryConfig.class)
@SpringBootApplication(scanBasePackages = "hello.itemservice.web")
public class ItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

	@Bean
	@Profile("local")
	public TestDataInit testDataInit(ItemRepository itemRepository) {
		return new TestDataInit(itemRepository);
	}
}
```

- `@Import(MemoryConfig.class)` : 앞서 설정한 `MemoryConfig` 를 설정 파일로 사용한다.

- `@Profile("local")` : 특정 프로필의 경우에만 해당 스프링 빈을 등록한다.
- `“local”` 이라는 이름의 프로필이 사용되는 경우에만 testDataInit 이라는 스프링 빈을 등록한다.
<br/>

## 궁금증!

```java
@EventListener 는 초기 데이터 넣는 용도로만 쓰는 걸까?
```

원래 목적은 `빈들끼리 서로 모르게 소통하게 하는 것` 이다.

초기 데이터 넣기는 그 기능을 쓴 한 가지 예일 뿐이다.

<br/>

## 이벤트를 직접 만들어보면

주문이 끝나면 메일도 보내고 포인트도 적립해야 한다고 해보자.

```java
record OrderCreated(String item) { }

@Component
@RequiredArgsConstructor
class OrderSvc {
    private final ApplicationEventPublisher publisher;

    void order(String item) {
        System.out.println("주문 저장 = " + item);
        publisher.publishEvent(new OrderCreated(item));
        System.out.println("주문 메서드 끝");
    }
}

@Component
class MailListener {
    @EventListener
    void on(OrderCreated event) {
        System.out.println("  [메일] " + event.item() + " 주문 확인 메일 발송");
    }
}

@Component
class PointListener {
    @EventListener
    void on(OrderCreated event) {
        System.out.println("  [적립] " + event.item() + " 포인트 적립");
    }
}
```

<br/>

### 결과

```java
주문 저장 = 책
  [메일] 책 주문 확인 메일 발송
  [적립] 책 포인트 적립
주문 메서드 끝
```

<br/>

`OrderSvc` 는 `MailListener` 와 `PointListener` 의 이름조차 모른다.

`publishEvent` 한 줄만 있고, 누가 듣는지는 신경 쓰지 않는다.

<br/>

기능을 하나 더 붙이고 싶으면 리스너 클래스를 하나 더 만들면 끝이다.

`OrderSvc` 는 한 글자도 안 바뀐다.

```java
직접 호출하면 -> 기능이 늘 때마다 OrderSvc 를 고쳐야 한다
이벤트를 쓰면 -> 듣는 쪽만 추가하면 된다
```

<br/>

## 그런데 기본은 동기 방식이다

출력 순서를 보면 `주문 메서드 끝` 이 리스너들보다 나중에 찍혔다.

`publishEvent()` 가 리스너를 전부 실행하고 나서야 다음 줄로 넘어간 것이다.

```java
publishEvent()
  -> MailListener 실행 (끝날 때까지 기다린다)
  -> PointListener 실행 (끝날 때까지 기다린다)
  -> 그 다음 줄로 넘어간다
```

<br/>

같은 스레드에서 그냥 메서드를 부르는 것과 실행 순서가 똑같다.

<br/>

이 말은 두 가지를 뜻한다.

- 리스너가 느리면 주문 메서드도 같이 느려진다

- 리스너에서 예외가 나면 주문까지 같이 롤백된다

<br/>

메일 발송이 실패했다고 주문이 취소되면 안 될 텐데, 그대로 롤백되는 것이다.

<br/>

## 그래서 옵션이 두 개 더 있다

```java
@Async
@EventListener
void on(OrderCreated event) { ... }
```

`@Async` 를 붙이면 다른 스레드에서 돌아서 주문 메서드를 붙잡지 않는다.

다만 다른 스레드라서 트랜잭션과 예외 처리를 따로 챙겨야 한다.

<br/>

```java
@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
void on(OrderCreated event) { ... }
```

<br/>

이쪽이 더 중요하다. `트랜잭션이 커밋된 뒤에` 실행하라는 뜻이다.

<br/>

일반 `@EventListener` 는 트랜잭션이 아직 커밋되기 전에 실행된다.

주문 저장이 나중에 롤백될 수도 있는데 메일이 먼저 나가버리는 것이다.

메일은 한 번 나가면 되돌릴 수 없으니 사고가 된다.

```java
@EventListener                          -> 커밋 전에 실행. 롤백되면 이미 늦었다
@TransactionalEventListener(AFTER_COMMIT) -> 커밋된 뒤에 실행. 확정된 뒤에만 나간다
```

<br/>

## ApplicationReadyEvent가 특별한 이유

원문에서 쓴 그 이벤트도 이 구조 위에 있다.

스프링이 뜨는 동안 여러 이벤트를 순서대로 발행하는데, 그중 마지막 무렵의 것이다.

```java
ContextRefreshedEvent    - 컨테이너 초기화 완료 (빈이 다 만들어짐)
ApplicationStartedEvent  - 부트가 시작을 알림
ApplicationReadyEvent    - 요청을 받을 준비까지 끝남
```

<br/>

`@PostConstruct` 로 초기 데이터를 넣으면 왜 안 되는지가 여기서 나온다.

`@PostConstruct` 는 그 빈 하나가 만들어진 직후에 불린다. 다른 빈은 아직 준비 중일 수 있다.

<br/>

특히 AOP 프록시는 빈이 다 만들어진 뒤에 씌워진다.

`@PostConstruct` 안에서 `@Transactional` 메서드를 부르면 프록시가 아직 없어서 트랜잭션이 안 걸린다.

<br/>

원문의 `AOP를 포함한 스프링 컨테이너가 완전히 초기화된 이후에 호출되기 때문에 이런 문제가 발생하지 않는다` 가 정확히 이 얘기다.

```java
@PostConstruct              -> 그 빈만 준비된 시점. 아직 위험하다
ApplicationReadyEvent       -> 전부 준비된 시점. 안전하다
```

<br/>

## @Profile 과 같이 쓴 이유

```java
@Bean
@Profile("local")
public TestDataInit testDataInit(ItemRepository itemRepository) {
    return new TestDataInit(itemRepository);
}
```

<br/>

`@Profile("local")` 이 없으면 운영 서버에서도 테스트 데이터가 들어간다.

빈 자체가 등록되지 않으면 리스너도 없으니 이벤트가 와도 아무 일도 안 일어난다.

<br/>

앞의 스프링 부트 글에서 본 자동 구성의 `@Conditional` 과 같은 원리다.

`조건이 맞을 때만 빈을 등록한다` 는 방식을 우리 코드에도 그대로 쓸 수 있는 것이다.
