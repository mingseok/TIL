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