## (중요) 트랜잭션 AOP 주의 사항

키워드 : 프록시 내부 호출

<br/>

## 문제의 목표는

분명히 트랜잭션을 적용했는데, 적용되지 않는 것이다.

ex) 내 돈은 빠졌는데, 상대방한테는 돈이 증가하지 않은 것이다.

<br/>

## 면접 질문

```
“스프링 트랜잭션을 사용할때 어떤 점을 주의해야 되나요?”
```

이런식으로 물어보기 때문에 잘 알고 있어야 한다.

<br/><br/>

## @Transactional

`@Transactional`을 사용하면 스프링의 트랜잭션 AOP가 적용된다.

```
트랜잭션 AOP는 기본적으로 프록시 방식을 가지고 AOP를 사용한다.
@Transactional 애노테이션을 사용하면, 프록시가 만들어진다고 생각하자.
```

앞서 배운 것 처럼 `@Transactional` 을 적용하면 프록시 객체가 요청을 먼저 받아서 트랜잭션을 처리하고, 실제 객체를 호출해준다.

<br/>

따라서 트랜잭션을 적용하려면 항상 프록시를 통해서 대상 객체(Target)을 호출해야 한다.

이렇게 해야 프록시에서 먼저 트랜잭션을 적용하고, 이후에 대상 객체를 호출하게 된다.

```
만약 프록시를 거치지 않고 대상 객체를 직접 호출하게 되면 
AOP가 적용되지 않고, 트랜잭션도 적용되지 않는다.
```

<br/><br/>

![이미지](/programming/img/입문256.PNG)

AOP를 적용하면 스프링은 대상 객체 대신에 프록시를 스프링 빈으로 등록한다.

따라서 스프링은 의존관계 주입시에 항상 실제 객체 대신에 프록시 객체를 주입한다.

<br/>

프록시 객체가 주입되기 때문에 대상 객체를 직접 호출하는 문제는 일반적으로 발생하지 않는다. 

하지만 대상 객체의 내부에서 메서드 호출이 발생하면 프록시를 거치지 않고 대상 객체를 직접 호출하는 문제가 발생한다.

<br/>

### 이렇게 되면 @Transactional 이 있어도 트랜잭션이 적용되지 않는다.

```
실무에서 반드시 한번은 만나서 고생하는 문제이기 때문에 꼭 이해하고 넘어가자.
```

<br/><br/>

## 코드로 확인하기.

```java
@Slf4j
@SpringBootTest
public class InternalCallV1Test {

    // ... 생략

    @Slf4j
    static class CallService {
        public void external() {
            log.info("call external");
            printTxInfo();

            // 문제점 발생
            internal();
        }

        @Transactional // -- 핵심 -- 
        public void internal() {
            log.info("call internal");
            printTxInfo();
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}", txActive);
        }
    }
}
```

호출 되는 순서 : `external()` → `printTxInfo()` → `internal()` → `printTxInfo()`

<br/>

### 출력값을 보면 이렇다.

내부 `internal()` 메서드에는 `@Transactional` 이 붙어 있음에도, 트랜잭션이 적용되지 않아서 

`tx active=false` 라고 출력 되는걸 알 수 있다. → 이유가 뭘까?

![이미지](/programming/img/입문257.PNG)

<br/><br/>

## 실제 호출되는 흐름 분석.

![이미지](/programming/img/입문258.PNG)

1. 클라이언트인 테스트 코드는 `callService.external()` 을 호출한다. 
    
    여기서 `callService` 는 트랜잭션 프록시이다.
    
2. `callService` 의 트랜잭션 프록시가 호출된다.
3. `external()` 메서드에는 `@Transactional` 이 없다. 
    
    따라서 트랜잭션 프록시는 트랜잭션을 적용하지 않는다.
    
4. 트랜잭션 적용하지 않고, 실제 `callService` 객체 인스턴스의 `external()` 을 호출한다.
5. `external()` 은 내부에서 `internal()` 메서드를 호출한다. 그런데 여기서 문제가 발생한다.

<br/><br/>

## 문제 원인

```
자바 언어에서 메서드 앞에 별도의 참조가 없으면 this 라는 뜻으로 
자기 자신의 인스턴스를 가리킨다.
```

결과적으로 자기 자신의 내부 메서드를 호출하는 `this.internal()` 이 되는데, 

여기서 `this` 는 자기 자신을 가리키므로, 실제 대상 객체의 인스턴스를 뜻한다. 

<br/>

결과적으로 이러한 내부 호출은 프록시를 거치지 않는다. 

따라서 트랜잭션을 적용할 수 없다. 

결과적으로 target 에 있는 `internal()`을 직접 호출하게 된 것이다.

<br/><br/>

## 프록시 방식의 AOP 한계

`@Transactional` 를 사용하는 트랜잭션 AOP는 프록시를 사용한다. 

프록시를 사용하면 메서드 내부 호출에 프록시를 적용할 수 없다.

```
AOP를 사용하든, 트랜잭션을 사용하든 둘다 프록시가 생성이 된다.
그리하여 내부 호출에서는 AOP나 트랜잭션에서는 적용이 되지 않는다고 생각하기.
```

<br/><br/>

## 그리하여 해결 방법은

```java
@SpringBootTest
public class InternalCallV2Test {

    @Autowired
    CallService callService;

    @Test
    void externalCallV2() {
        callService.external();
    }

    @TestConfiguration
    static class InternalCallV2Config {
        @Bean
        CallService callService() {
            return new CallService(innerService());
        }

        @Bean
        InternalService innerService() {
            return new InternalService();
        }
    }

    @Slf4j
    @RequiredArgsConstructor
    static class CallService {

        private final InternalService internalService;

        public void external() {
            log.info("call external");
            printTxInfo();
            internalService.internal();
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}", txActive);
        }
    }

    @Slf4j
    static class InternalService {
        @Transactional
        public void internal() {
            log.info("call internal");
            printTxInfo();
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}", txActive);
        }
    }
}
```

`InternalService` 클래스를 만들고 `internal()` 메서드를 여기로 옮겼다.

이렇게 메서드 내부 호출을 외부 호출로 변경했다.

<br/>

`CallService` 에는 트랜잭션 관련 코드가 전혀 없으므로, 트랜잭션 프록시가 적용되지 않는다.

`InternalService` 에는 트랜잭션 관련 코드가 있으므로 트랜잭션 프록시가 적용된다.

```
실무에서는 이렇게 별도의 클래스로 분리하는 방법을 주로 사용한다.
```


<br/><br/>

>**Reference** <br/>[스프링 DB 2편 - 데이터 접근 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-db-2/dashboard)

