## 엔티티 조회, DTO 조회

<br/>

## 주의

엔티티를 직접 노출할 때는 양방향 연관관계가 걸린 곳은 

꼭! 한곳을 `@JsonIgnore` 처리 해야 한다.

```
안그러면 양쪽을 서로 호출하면서 무한 루프가 걸린다
```

결과가 오래걸리거나, 늦다 싶으면 이유는 이것이다.

<br/>

## 즉시 로딩(EARGR)은 사용하지마라!

지연 로딩(LAZY)을 피하기 위해 즉시 로딩(EARGR)으로 설정하면 안된다.

<br/>

즉시 로딩(EARGR) 때문에 연관관계가 필요 없는 경우에도 

데이터를 항상 조회해서 성능 문제가 발생할 수 있다.


<br/><br/>

## 쿼리 방식 선택 권장 순서 

### toOne 관계일때 말하는 것이다 (@ManyToOne, @OneToOne)

1. 우선 엔티티를 DTO로 변환하는 방법을 선택

2. 필요하면 패치 조인으로 성능을 최적화 한다. 

    - 대부분의 성능 이슈는 여기서 해결될 것이다

3. 그래도 안되면 DTO로 직접 조회하는 방법을 사용한다.

4. 최후의 방법은 JPA가 제공하는 네이티브 SQL이나 

    스프링 JDBC Template을 사용해서 SQL을 직접 사용한다



<br/><br/>

## ToOne (ManyToOne, OneToOne) 관계는 모두 폐치조인 한다

`ToOne` 관계는 `row`수를 증가시키지 않으므로 페이징 쿼리에 영향을 주지 않는다


<br/><br/>

## 컬렉션 같은 경우

- 지연 로딩으로 조회 한다

    - hibernate.default_batch_fetch_size : 글로벌 설정

    - @BatchSize : 개발 최적화

<br/>

### application.yml

```yml
  jpa:
    hibernate:
      ddl-auto: create
    properties:
      hibernate:
        default_batch_fetch_size: 1000
```

개별로 설정하려면 `@BatchSize`를 적용하면 된다

(컬렉션은 컬렉션 필드에, 엔티티는 엔티티 클래스에 적용)


<br/>

### 장점

- 쿼리 호출 수가 `1 + N` -> `1 + 1` 로 최적화 된다

- 조인보다 DB 데이터 전송량이 최적화 된다

- 컬렉션 패치 조인은 페이징이 불가능 하지만 이 방법은 페이징이 가능하다




<br/><br/>

## 정리

ToOne 관계는 폐치 조인해도 페이징에 영향을 주지 않는다.

<br/>

따라서 ToOne 관계는 폐치조인으로 쿼리 수를 줄이고 해결하며, 

나머지는 default_batch_fetch_size: 1000 설정으로 최적화 하자

(Max를 1000 이라고 생각하면 될것이다)


<br/><br/>


## 권장 순서

1. `엔티티` 조회 방식으로 우선 접근
    
    1. 페치조인으로 쿼리 수를 최적화

    2. 컬렉션 최적화

        1. 페이징 필요 : `default_batch_fetch_size`, `@BatchSize`로 최적화

        2. 페이징 필요X -> 페치 조인 사용

2. `엔티티` 조회 방식으로 해결이 안되면 `DTO` 조회 방식 사용

3. `DTO` 조회 방식으로 해결이 안되면 `NativeSQL` 아니면, 스프링 `JdbcTemplate` 사용하기


<br/><br/>

## `엔티티` 조회 방식부터 추천하는 이유는?

### 엔티티 조회 방식은 

페치 조인이나, `default_batch_fetch_size`, `@BatchSize`와 같이 

코드를 거의 수정하지 않고, 옵션만 약간 변경해서 다양한 성능 최적화를 시도할 수 있다.

<br/>

### 반면에 DTO를 직접 조회하는 방식은

성능을 최적화 하거나 성능 최적화 방식을 변경할 때 많은 코드를 변경해야 한다.
<br/>

## 궁금증!

```java
"권장 순서" 를 왜 그 순서로 두었을까
```

`고칠 수 있는 여지를 얼마나 남기는가` 순서다.

```java
1. 엔티티 조회 + fetch join
2. 엔티티 조회 + batch size
3. DTO 로 직접 조회
4. 네이티브 SQL
```

<br/>

위로 갈수록 유지보수가 쉽고, 아래로 갈수록 성능을 세밀하게 다룰 수 있다.

<br/>

## 1번을 먼저 시도하는 이유

`fetch join` 은 조회 방식만 바꾼다. 반환 타입이 엔티티 그대로다.

```java
// 바꾸기 전
SELECT o FROM Order o

// 바꾼 뒤
SELECT o FROM Order o JOIN FETCH o.member
```

<br/>

쓰는 쪽 코드가 안 바뀐다. 성능 문제가 생겼을 때 이 한 줄만 고치면 되는 것이다.

<br/>

앞의 DB 접근(JPA) 글에서 본 `N+1` 이 이걸로 대부분 해결된다.

<br/>

## 그런데 컬렉션은 fetch join 을 하나만 할 수 있다

```java
SELECT o FROM Order o
JOIN FETCH o.member
JOIN FETCH o.orderItems       // 여기까지는 된다
JOIN FETCH o.deliveries       // 두 번째 컬렉션은 안 된다
```

```java
MultipleBagFetchException: cannot simultaneously fetch multiple bags
```

<br/>

이유는 곱집합 때문이다.

```java
주문 1건에 주문항목 3개, 배송 2개
-> 조인하면 3 × 2 = 6 행이 나온다
-> 어느 것이 진짜 데이터인지 구분할 수 없다
```

<br/>

그래서 컬렉션은 하나만 `fetch join` 하고, 나머지는 `2번` 으로 푼다.

<br/>

## 2번 batch size 가 하는 일

```java
spring.jpa.properties.hibernate.default_batch_fetch_size=100
```

<br/>

`N+1` 을 `1+1` 로 바꿔준다.

```sql
-- batch size 없이
SELECT * FROM orders;                       -- 1번
SELECT * FROM order_item WHERE order_id=1;  -- N번
SELECT * FROM order_item WHERE order_id=2;
...

-- batch size 있으면
SELECT * FROM orders;
SELECT * FROM order_item WHERE order_id IN (1,2,3, ... ,100);   -- 한 번에
```

<br/>

`IN` 절로 묶어서 한 번에 가져온다.

앞의 DBCP 글에서 본 대로, 왕복 횟수가 줄어드는 것이 핵심이다.

<br/>

이 설정은 전역으로 걸어두는 것이 좋다. 코드를 안 고쳐도 되기 때문이다.

<br/>

## 3번 DTO 직접 조회로 넘어가는 시점

```java
SELECT new com.example.OrderDto(o.id, m.name, o.orderDate)
FROM Order o JOIN o.member m
```

<br/>

필요한 컬럼만 가져오니 네트워크로 오가는 양이 줄어든다.

앞의 DB 인덱스 글에서 본 대로, 안 쓸 데이터를 읽는 것은 그냥 낭비다.

<br/>

대신 잃는 것이 있다.

```java
영속성 컨텍스트에 안 들어간다  -> 변경 감지가 안 된다
재사용이 어렵다              -> DTO 마다 쿼리를 따로 짜야 한다
```

<br/>

그래서 `조회 전용 화면` 에만 쓴다.

수정이 필요하면 엔티티로 조회해야 앞의 JPA 설정, 적용, 핵심 글에서 본 변경 감지를 쓸 수 있다.

<br/>

## 4번 네이티브 SQL 은 마지막이다

```java
@Query(value = "SELECT ... FROM ...", nativeQuery = true)
```

<br/>

DB 방언이 그대로 코드에 들어온다.

앞의 PSA 글에서 본 `기술에 묶이지 않는다` 는 이득을 포기하는 것이다.

<br/>

그래도 필요한 경우가 있다.

```java
윈도우 함수 (ROW_NUMBER, RANK)
DB 전용 함수 (MySQL 의 GROUP_CONCAT)
복잡한 통계 쿼리
```

<br/>

앞의 MyBatis 설명, 설정 방법 글에서 본 것처럼,

이런 것은 SQL로 직접 쓰는 편이 훨씬 짧고 읽기 쉽다.

<br/>

## 즉시 로딩을 쓰지 말라는 것

원문에서 강조한 그 부분은 앞의 즉시 로딩과 지연 로딩 글에서 다뤘다.

```java
EAGER 는 fetch join 으로도 끌 수가 없다
JPQL 에서 N+1 이 그대로 터진다
```

<br/>

`LAZY` 로 두면 위의 `1번`, `2번`, `3번` 을 상황에 맞게 고를 수 있다.

`EAGER` 로 두면 그 선택지 자체가 사라진다.

<br/>

`권장 순서` 라는 것이 성립하려면 먼저 `LAZY` 여야 하는 것이다.
