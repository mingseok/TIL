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

