## Auditing

엔티티를 생성, 변경할 때 변경한 사람과 시간을 추적하고 싶으면?


- 등록일

- 수정일

- 등록자

- 수정자


<br/>

## 설정 방법

### `@EnableJpaAuditing` : 메인 클래스에 적용하기

```java
@EnableJpaAuditing // 추가해주기
@SpringBootApplication
public class DataJapApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJapApplication.class, args);
	}
}
```

<br/><br/>

### @EntityListeners(AuditingEntityListener.class) 엔티티에 적용


```java
@EntityListeners(AuditingEntityListener.class) // 무조건 사용하는 엔티티 최상위
@MappedSuperclass
@Getter
public class BaseTimeEntity {
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}


@EntityListeners(AuditingEntityListener.class) // 그나마 덜 쓰는 엔티티
@MappedSuperclass
@Getter
public class BaseEntity extends BaseTimeEntity {
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;
}
```

- `@MappedSuperclass` : 진짜 상속관계가 아니고, 

    현재 클래스의 속성들을 테이블에 내려서 같이 쓸수 있게 해주는 애노테이션이다

<br/><br/>

## 추가 설명

저장시점에 등록일, 등록자는 물론이고, 수정일, 수정자도 같은 데이터가 저장된다.

데이터가 중복 저장되는 것 같지만, 이렇게 해두면 변경 컬럼만 확인해도 

마지막에 업데이트한 유저를 확인 할 수 있으므로 유
지보수 관점에서 편리하다.


<br/><br/>

>**Reference** <br/>[실전! 스프링 데이터 JPA](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%EB%8D%B0%EC%9D%B4%ED%84%B0-JPA-%EC%8B%A4%EC%A0%84?_gl=1*1x5vsec*_ga*OTY2ODU2MjYxLjE2NzkwNjYzNDU.*_ga_85V6SRKGJV*MTY5MjMyMTczNi40MC4xLjE2OTIzNDAwNDguNTIuMC4w)


<br/>

## 궁금증!

```java
Auditing 이 값을 언제 채우는가
```

앞의 영속성 컨텍스트 글에서 본 생명주기 콜백 시점이다.

```java
@PrePersist  - persist 하기 직전    -> @CreatedDate, @CreatedBy 를 채운다
@PreUpdate   - UPDATE 나가기 직전   -> @LastModifiedDate, @LastModifiedBy 를 채운다
```

<br/>

`AuditingEntityListener` 가 이 콜백을 듣고 값을 넣어준다.

```java
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

<br/>

앞의 상속관계 매핑 글에서 본 `@MappedSuperclass` 가 여기 쓰인다.

테이블은 안 만들고 컬럼만 물려주는 용도다.

<br/>

## updatable = false 를 붙이는 이유

이걸 안 붙이면 `UPDATE` 할 때 `created_at` 도 같이 나간다.

```sql
UPDATE member SET name=?, created_at=?, updated_at=? WHERE id=?
```

<br/>

값은 안 바뀌니 결과는 같지만, `생성 시각은 절대 안 바뀐다` 는 규칙이 코드에 안 드러난다.

<br/>

그리고 누가 실수로 `setCreatedAt()` 을 부르면 실제로 바뀌어버린다.

`updatable = false` 면 그래도 DB에는 안 나간다.

<br/>

## @EnableJpaAuditing 을 어디에 붙일까

```java
@EnableJpaAuditing
@SpringBootApplication
public class Application { }
```

<br/>

메인 클래스에 붙이는 경우가 많은데, 테스트할 때 문제가 된다.

<br/>

앞의 DB 접근 (+테스트 방법) 글에서 본 `@WebMvcTest` 같은 슬라이스 테스트는

메인 클래스를 읽으면서 `@EnableJpaAuditing` 까지 켠다.

그런데 JPA 관련 빈은 안 띄우니 오류가 난다.

```java
JPA metamodel must not be empty!
```

<br/>

그래서 설정 클래스를 따로 두는 편이 낫다.

```java
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig { }
```

<br/>

앞의 스프링에서 빈을 등록하는 방법 글에서 본 대로,

설정을 용도별로 나눠두면 필요한 것만 켤 수 있다.

<br/>

## 누가 했는지도 기록하려면

```java
@CreatedBy
private String createdBy;

@LastModifiedBy
private String lastModifiedBy;
```

<br/>

이 값을 어디서 가져올지 알려줘야 한다.

```java
@Bean
public AuditorAware<String> auditorProvider() {
    return () -> Optional.ofNullable(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .filter(Authentication::isAuthenticated)
            .map(Authentication::getName);
}
```

<br/>

앞의 로그인 쿠키, 세션 글에서 본 시큐리티의 인증 정보를 꺼내는 것이다.

<br/>

여기서도 앞의 클라이언트 서버 구조 글에서 본 원칙이 적용된다.

`누가 했는지` 는 클라이언트가 보낸 값이 아니라 서버가 세션에서 꺼내야 한다.

<br/>

## 배치나 스케줄러에서는 인증 정보가 없다

```java
Optional.empty()      -> created_by 가 null 이 된다
```

<br/>

`NOT NULL` 을 걸어뒀으면 저장이 실패한다.

<br/>

그래서 기본값을 정해두는 편이 안전하다.

```java
return () -> Optional.of(현재사용자().orElse("SYSTEM"));
```

<br/>

앞의 attribute data type 글에서 본 `NULL 을 허용할 것인가` 판단이 여기서도 나온다.

`SYSTEM` 이라는 값을 넣으면 `배치가 한 것` 이라는 정보가 남고,

`null` 로 두면 `모른다` 가 되어 나중에 추적이 안 된다.

<br/>

## Auditing 이 채우는 시각의 기준

`@CreatedDate` 는 애플리케이션 서버의 시각을 쓴다.

<br/>

앞의 attribute data type 글에서 본 그 문제가 여기서 생긴다.

서버가 여러 대이고 시간대나 시각이 조금씩 다르면 값이 어긋난다.

<br/>

그래서 DB 시각을 쓰고 싶으면 이렇게 한다.

```java
@Column(insertable = false, updatable = false,
        columnDefinition = "datetime default CURRENT_TIMESTAMP")
private LocalDateTime createdAt;
```

<br/>

다만 이러면 저장 직후에 그 값을 읽을 수 없다.

DB가 채운 값이라 영속성 컨텍스트는 모르기 때문이다.

<br/>

앞의 영속성 컨텍스트 글에서 본 1차 캐시가 DB와 어긋나는 상황이라,

`refresh` 로 다시 읽어야 한다. 그래서 대개는 애플리케이션 시각을 쓰고 서버 시각을 맞춰둔다.
