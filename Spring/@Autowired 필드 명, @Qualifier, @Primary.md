### 조회 대상 빈이 2개 이상일 때 해결 방법

- @Autowired 필드 명 매칭

- @Qualifier @Qualifier끼리 매칭 빈 이름 매칭
- @Primary 사용

<br/>

## 1. @Autowired 필드 명 매칭

`@Autowired` 는 타입 매칭을 시도하고, 이때 여러 빈이 있으면 필드 이름, 

파라미터 이름으로 빈 이름을 추가 매칭한다.

<br/>

### 기존 코드

```java
@Autowired private DiscountPolicy discountPolicy
```

### 필드 명을 빈 이름으로 변경

```java
@Autowired private DiscountPolicy rateDiscountPolicy
```

<br/>

필드 명이 rateDiscountPolicy 이므로 정상 주입된다.

필드 명 매칭은 먼저 타입 매칭을 시도 하고 그 결과에 여러 빈이 있을 때 추가로 동작하는 기능이다.

### @Autowired 매칭 정리

1. 타입 매칭

2. 타입 매칭의 결과가 2개 이상일 때 필드 명, 파라미터 명으로 빈 이름 매칭


<br/>

## 2. @Qualifier 사용

@Qualifier 는 추가 구분자를 붙여주는 방법이다. 

주입 시 추가적인 방법을 제공하는 것이지 빈 이름을 변경하는 것은 아니다

빈 등록시 @Qualifier를 붙여 준다.

<br/>

`RateDiscountPolicy`  클래스

```java
@Component
@Qualifier("mainDiscountPolicy")
public class RateDiscountPolicy implements DiscountPolicy {

}
```

`FixDiscountPolicy`  클래스

```java
@Component
@Qualifier("fixDiscountPolicy")
public class FixDiscountPolicy implements DiscountPolicy {

}
```

이렇게 사용하는 것이다.

![이미지](/programming/img/스프링32.PNG)

<br/>

## @Primary 사용 → 자주 사용

만약, 나는 `RateDiscountPolicy` 클래스가 먼저 선택되게 할거야 한다면,

<br/>이렇게 붙여 주면 되는 것이다.

![이미지](/programming/img/스프링31.PNG)

@Primary 는 우선순위를 정하는 방법이다.

@Autowired 시에 여러 빈이 매칭되면 

@Primary 가 우선권을 가진다.


<br/>

>**Reference** <br/>스프링 핵심 원리 - 기본편 : https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8
