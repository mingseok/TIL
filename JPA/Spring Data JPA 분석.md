## Spring Data JPA 분석

<br/>

### `@Repository` 적용: JPA 예외를 스프링이 추상화한 예외로 변환

<br/>

## @Transactional 트랜잭션 적용

- JPA의 모든 변경은 트랜잭션 안에서 동작

- 스프링 데이터 JPA는 변경`(등록, 수정, 삭제)` 메서드를 트랜잭션 처리

- 서비스 계층에서 트랜잭션을 시작하지 않으면 리파지토리에서 트랜잭션 시작

- 서비스 계층에서 트랜잭션을 시작하면 리파지토리는 해당 트랜잭션을 전파 받아서 사용

- 그래서 스프링 데이터 JPA를 사용할 때 트랜잭션이 없어도 데이터 등록, 변경이 가능했음

    (사실은 트랜
잭션이 리포지토리 계층에 걸려있는 것임)

<br/><br/>

## @Transactional(readOnly = true)

- 데이터를 단순히 조회만 하고 변경하지 않는
 트랜잭션에서 `readOnly = true` 옵션을
 
  사용하면 `플러시`를 생략해서 약간의 성능 향상을 얻을 수 있음


<br/><br/>


## 매우 중요!! -> save() 메서드

- 새로운 엔티티면 저장( `persist` )

- 새로운 엔티티가 아니면 병합( `merge` )


<br/><br/>

>**Reference** <br/>[실전! 스프링 데이터 JPA](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%EB%8D%B0%EC%9D%B4%ED%84%B0-JPA-%EC%8B%A4%EC%A0%84?_gl=1*1x5vsec*_ga*OTY2ODU2MjYxLjE2NzkwNjYzNDU.*_ga_85V6SRKGJV*MTY5MjMyMTczNi40MC4xLjE2OTIzNDAwNDguNTIuMC4w)


<br/>

## 궁금증!

```java
프록시가 만들어지는 과정을 순서대로 보면
```

앞의 스프링 데이터 JPA (사용 이유) 글에서 본 그 프록시가 이렇게 만들어진다.

```java
1. @EnableJpaRepositories 가 스캔 범위를 정한다
     (스프링 부트는 @SpringBootApplication 위치를 기준으로 자동 설정한다)
2. Repository 를 상속한 인터페이스를 전부 찾는다
3. 인터페이스마다 RepositoryFactoryBean 을 빈으로 등록한다
4. 컨테이너가 그 팩토리에게 실제 객체를 만들게 한다
5. 팩토리가 JDK 동적 프록시를 만들어서 돌려준다
```

<br/>

앞의 스프링 빈(Bean)이란 BeanDefinition이란 글에서 본 순서 안에서 일어난다.

`3번` 이 `BeanDefinition` 등록 단계고, `4번` 이 객체 생성 단계다.

<br/>

## 팩토리 빈이라는 것

```java
public interface FactoryBean<T> {
    T getObject();
    Class<?> getObjectType();
}
```

<br/>

`이 빈은 스스로가 목적이 아니라 다른 것을 만들어내는 역할` 이라는 표시다.

<br/>

컨테이너는 `FactoryBean` 을 만나면 그 객체 대신 `getObject()` 의 결과를 등록한다.

<br/>

앞의 스프링에서 빈을 등록하는 방법 글에서 본 두 방식에 더해 세 번째 방식인 셈이다.

```java
@Component  - 그 클래스를 new 한다
@Bean       - 그 메서드를 부른다
FactoryBean - getObject() 를 불러서 나온 것을 등록한다
```

<br/>

## 프록시가 호출을 어디로 넘기는가

```java
memberRepository.findById(1L)
  -> 프록시가 받는다
  -> "이건 기본 제공 메서드인가?"
       예   -> SimpleJpaRepository 로 넘긴다
       아니오 -> 미리 만들어둔 JPQL 을 실행한다

memberRepository.findByEmail("a@test.com")
  -> 프록시가 받는다
  -> 시작할 때 만들어둔 "SELECT m FROM Member m WHERE m.email = :email" 을 실행한다
```

<br/>

두 갈래가 있는 셈이다.

<br/>

## 직접 구현을 끼워 넣는 방법

프록시가 만들어주는 것으로 부족하면 직접 구현할 수 있다.

```java
public interface MemberRepositoryCustom {
    List<Member> search(SearchCond cond);
}

public class MemberRepositoryImpl implements MemberRepositoryCustom {
    // QueryDSL 같은 것으로 직접 구현
}

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
}
```

<br/>

이름 규칙이 있다. `인터페이스 이름 + Impl` 이어야 찾는다.

```java
MemberRepositoryCustom  ->  MemberRepositoryCustomImpl
MemberRepository        ->  MemberRepositoryImpl        (둘 다 찾는다)
```

<br/>

앞의 MyBatis 적용 글에서 본 `이름으로 연결되는 방식` 이 여기서도 쓰인다.

이름이 틀리면 프록시가 그 구현을 못 찾고, 호출하면 예외가 난다.

<br/>

## 그런데 Custom 이 많아지면 문제가 된다

`MemberRepositoryImpl` 하나에 온갖 조회 메서드가 쌓인다.

<br/>

그래서 요즘은 조회 전용 클래스를 따로 두는 방식을 더 많이 쓴다.

```java
@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<MemberDto> search(SearchCond cond) { ... }
}
```

<br/>

`Custom` 인터페이스도 `Impl` 규칙도 필요 없다.

그냥 평범한 스프링 빈이라 앞의 DI 생성자 주입 글에서 본 대로 주입받아 쓰면 된다.

<br/>

그리고 화면별로 클래스를 나눌 수 있다.

```java
MemberQueryRepository       - 회원 조회 화면용
MemberStatQueryRepository   - 통계 화면용
```

<br/>

앞의 검증 - Form 전송 객체 분리 글에서 본 `바뀌는 이유가 다르면 나눈다` 와 같은 기준이다.

리포지토리 하나에 전부 몰아넣으면 화면 하나가 바뀔 때마다 그 파일이 바뀐다.
