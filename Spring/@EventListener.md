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