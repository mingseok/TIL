## Spring Data JPA 설명

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
}
```

- `<>` : 안에 들어갈 것은 -> `<엔티티, 엔티티의 PK 타입>`을 말한다

<br/>

## 인터페이스만 있고 구현체가 없는데, 동작이유는?


`인터페이스`를 보고 `Spring-Data-Jpa`가 구현클래스를 만들어서 준것이다

- `@Repository` 애노테이션 생략 가능

    - 컴포넌트 스캔을 스프링 데이터 JPA가 자동으로 처리

    - JPA 예외를 스프링 예외로 변환하는 과정도 자동으로 처리


<br/><br/>

## 주요 메서드

- `save(S)` : 새로운 엔티티는 저장하고 이미 있는 엔티티는 병합한다.


- `delete(T)` : 엔티티 하나를 삭제한다. 내부에서 `EntityManager.remove()` 호출


- `findById(ID)` : 엔티티 하나를 조회한다. 내부에서 `EntityManager.find()` 호출


- `getOne(ID)` : 엔티티를 프록시로 조회한다. 내부에서 `EntityManager.getReference()` 호출


- `findAll(…)` : 모든 엔티티를 조회한다. 정렬( Sort )이나 페이징( Pageable ) 조건을 파라미터로 제공할
수 있다.




<br/><br/>

>**Reference** <br/>[실전! 스프링 데이터 JPA](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%EB%8D%B0%EC%9D%B4%ED%84%B0-JPA-%EC%8B%A4%EC%A0%84?_gl=1*1x5vsec*_ga*OTY2ODU2MjYxLjE2NzkwNjYzNDU.*_ga_85V6SRKGJV*MTY5MjMyMTczNi40MC4xLjE2OTIzNDAwNDguNTIuMC4w)


<br/>

## 궁금증!

```java
JpaRepository 를 상속하면 어떤 메서드가 딸려오는가
```

계층이 세 겹이다.

```java
Repository            - 표시용 인터페이스. 메서드가 없다
  CrudRepository      - save, findById, delete, count ...
    ListCrudRepository / PagingAndSortingRepository
      JpaRepository   - flush, saveAndFlush, deleteAllInBatch, getReferenceById ...
```

<br/>

`JpaRepository` 를 상속하면 위의 것을 전부 물려받는다.

<br/>

앞의 스프링 데이터 JPA (사용 이유) 글에서 본 대로 구현체는 `SimpleJpaRepository` 다.

프록시가 그 인스턴스에게 넘긴다.

<br/>

## 왜 인터페이스가 여러 겹인가

필요한 만큼만 노출하기 위해서다.

```java
public interface MemberRepository extends CrudRepository<Member, Long> { }
```

<br/>

이렇게 하면 `flush` 나 `deleteAllInBatch` 같은 JPA 전용 메서드가 안 보인다.

<br/>

앞의 서비스 계층 글에서 본 `순수 자바만 남긴다` 와 이어진다.

`JpaRepository` 를 쓰면 그 리포지토리가 JPA 라는 것이 인터페이스에 드러난다.

<br/>

더 나아가면 아예 직접 인터페이스를 정의하기도 한다.

```java
public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
}

@Repository
class JpaMemberRepository implements MemberRepository {
    private final MemberJpaRepository jpaRepository;   // 스프링 데이터 JPA 를 안에서 쓴다
}
```

<br/>

서비스는 순수 인터페이스만 보게 되고, JPA는 구현 세부사항이 된다.

<br/>

## getReferenceById 를 알아두면 유용하다

```java
Member member = memberRepository.getReferenceById(1L);
```

<br/>

앞의 프록시 기초 글에서 본 `em.getReference()` 와 같다. 프록시만 받는다.

```java
findById         - SELECT 가 나간다. 없으면 Optional.empty()
getReferenceById - SELECT 가 안 나간다. 프록시만 준다
```

<br/>

연관관계를 세팅할 때만 필요하면 조회를 아낄 수 있다.

```java
// 팀 정보는 필요 없고 연결만 하면 될 때
Team team = teamRepository.getReferenceById(teamId);
member.changeTeam(team);        // team_id 만 있으면 되니 SELECT 가 필요 없다
```

<br/>

앞의 프록시 기초 글에서 본 대로 `getId()` 만 부르면 초기화가 안 되기 때문이다.

<br/>

다만 없는 id 를 넣으면 나중에 쓸 때 예외가 난다.

```java
EntityNotFoundException
```

<br/>

조회 시점이 아니라 쓰는 시점에 터지니, 원인을 찾기 어려울 수 있다.

<br/>

## deleteAllInBatch 와 deleteAll 의 차이

```java
deleteAll()        - 하나씩 조회하고 하나씩 DELETE
deleteAllInBatch() - DELETE FROM member 한 번
```

<br/>

앞의 쿼리 메소드 글에서 본 `deleteBy` 문제와 같은 얘기다.

<br/>

`deleteAllInBatch` 는 빠른데, 앞의 영속성 전이 CASCADE 글에서 본 대로

JPA의 `cascade` 나 `orphanRemoval` 이 동작하지 않는다.

DB로 바로 나가기 때문이다.

<br/>

그리고 1차 캐시에 옛 엔티티가 남는다.

앞의 쿼리 메소드 글에서 본 `clearAutomatically` 문제와 같다.

```java
테스트 정리   - deleteAllInBatch (빠르다)
운영 코드     - deleteAll 또는 개별 삭제 (생명주기가 지켜진다)
```

<br/>

## save 가 INSERT 인지 UPDATE 인지 어떻게 아는가

앞의 스프링 데이터 JPA (사용 이유) 글에서 본 `SimpleJpaRepository.save` 다.

```java
if (entityInformation.isNew(entity)) {
    em.persist(entity);
} else {
    return em.merge(entity);
}
```

<br/>

`isNew` 의 기본 판단은 `식별자가 null 인가` 다.

<br/>

그래서 앞의 기본 키 매핑 글에서 본 대로, 식별자를 직접 넣는 경우에 문제가 생긴다.

```java
@Id
private String code;        // UUID 나 코드값을 직접 넣는다
```

<br/>

식별자가 `null` 이 아니니 `merge` 로 간다.

`merge` 는 `SELECT` 를 먼저 날려서 있는지 확인하고 없으면 `INSERT` 한다.

저장할 때마다 쓸데없는 `SELECT` 가 하나씩 더 나가는 것이다.

<br/>

`Persistable` 을 구현해서 판단 기준을 바꿀 수 있다.

```java
@Override
public boolean isNew() {
    return createdAt == null;      // Auditing 이 아직 안 채웠으면 새 것
}
```

<br/>

앞의 Auditing 글에서 본 `@CreatedDate` 를 이 판단에 쓰는 것이다.
