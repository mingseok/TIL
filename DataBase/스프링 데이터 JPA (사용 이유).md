## 스프링 데이터 JPA (사용 이유)

스프링 데이터 JPA는 JPA를 편리하게 사용할 수 있도록 도와주는 `라이브러리`이다.

수많은 편리한 기능을 제공하지만 가장 대표적인 기능은 다음과 같다.

- 공통 인터페이스 기능

- 쿼리 메서드 기능

```
- JpaRepository 인터페이스를 통해서 기본적인 CRUD 기능 제공한다.
- 공통화 가능한 기능이 거의 모두 포함되어 있다.
- CrudRepository 에서 fineOne() findById() 로 변경되었다.
```

<br/><br/>

## JpaRepository 사용법

```java
public interface ItemRepository extends JpaRepository<Item, Long> {
}
```

- 인터페이스를 인터페이스 받을땐 ‘상속’ 이라고 말한다.

    - 제네릭에 관리할 <엔티티, 엔티티ID> 를 주면 된다.

        - `엔티티란?` → 관리할 것을 말한다.

        - `엔티티ID란?` → Item에 PK라고 설정 되어 있는 타입을 말하는 것이다.

- 그러면 `JpaRepository` 가 제공하는 기본 `CRUD` 기능을 모두 사용할 수 있다.

<br/><br/>

## 스프링 데이터 JPA가 구현 클래스를 대신 생성

![이미지](/programming/img/입문261.PNG)

- JpaRepository 인터페이스만 상속받으면,

  스프링 데이터 JPA가 프록시 기술을 사용해서 구현 클래스를 직접 만들어준다는 것이다.
    
    - 그리고 만든 구현 클래스의 인스턴스를 만들어서 스프링 빈으로 등록한다.

```
따라서 개발자는 구현 클래스 없이 인터페이스만 만들면 기본 CRUD 기능을 사용할 수 있다.
```

<br/><br/>

## 쿼리 메서드 기능

스프링 데이터 JPA는 인터페이스에 메서드만 적어두면, 

메서드 이름을 분석해서 쿼리를 자동으로 만들고 실행해주는 기능을 제공한다.

### 순수 JPA 리포지토리 (데이터 JPA 사용 전)

```java
public List<Member> findByUsernameAndAgeGreaterThan(String username, int age) {
    return em.createQuery("select m from Member m where m.username = :username
           and m.age > :age ")
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
}
```

- 순수 JPA를 사용하면 직접 JPQL을 작성하고, 파라미터도 직접 바인딩 해야 한다.

<br/>

### 스프링 데이터 JPA

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
}
```

- 스프링 데이터 JPA는 메서드 이름을 분석해서 필요한 JPQL을 만들고 실행해준다.
    
    물론 JPQL은 JPA가 SQL로 번역해서 실행한다
    
- 물론 그냥 아무 이름이나 사용하는 것은 아니고 다음과 같은 규칙을 따라야 한다.

<br/><br/>

## 스프링 데이터 JPA 메소드명 작성 규칙.

스프링 데이터 JPA가 제공하는 쿼리 메소드 기능들

- 조회: `find…By` , `read…By` , `query…By` , `get…By`

    - 예:) `findHelloBy` 처럼 ...에 식별하기 위한 내용(설명)이 들어가도 된다

    - 즉, 메소드명을 시작할때 `find`, `read`, `query`, `get` 로 시작하면 된다는 것이다.

- COUNT: `count…By` 반환타입 `long`

- EXISTS: `exists…By` 반환타입 `boolean`
- 삭제: `delete…By` , `remove…By` 반환타입 `long`
- DISTINCT: `findDistinct`, `findMemberDistinctBy`
- LIMIT: `findFirst3`, `findFirst`, `findTop`, `findTop3`


<br/><br/>

>**Reference** <br/>[스프링 DB 2편 - 데이터 접근 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-db-2/dashboard)


<br/>

## 궁금증!

```java
인터페이스만 만들었는데 어떻게 동작할까? 구현체는 누가 만드는가?
```

앞의 AOP 글에서 본 프록시와 같은 방식이다. 실행 중에 구현체를 만들어서 빈으로 등록한다.

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}
```

<br/>

이 인터페이스를 주입받아서 클래스 이름을 찍어보면 이렇게 나온다.

```java
class com.sun.proxy.$Proxy123
```

<br/>

앞의 AOP 글에서 본 `$Proxy0` 과 같은 형태다.

JDK 동적 프록시라 인터페이스만 있으면 만들 수 있다.

<br/>

## 메서드 이름을 어떻게 SQL 로 바꾸는가

`findByEmail` 이라는 이름을 문법에 따라 쪼갠다.

```java
findBy | Email
  ^      ^
  |      +-- Member 엔티티의 email 필드
  +-- 조회한다
```

<br/>

그리고 이 정보로 JPQL을 만든다.

```java
SELECT m FROM Member m WHERE m.email = :email
```

<br/>

이 작업은 애플리케이션이 뜰 때 한 번만 한다.

요청마다 이름을 파싱하는 것이 아니라, 시작할 때 만들어두고 재사용한다.

<br/>

## 그래서 오타를 시작할 때 잡아준다

```java
Optional<Member> findByEmial(String email);      // 오타
```

<br/>

애플리케이션이 아예 안 뜬다.

```java
PropertyReferenceException: No property 'emial' found for type 'Member'
```

<br/>

앞의 스프링 컨테이너 글에서 본 `미리 만들어두면 문제가 있을 때 뜨지도 않는다` 가 여기서도 작동한다.

<br/>

필드 이름을 바꿨는데 메서드 이름을 안 고쳤을 때도 시작 시점에 걸린다.

`JdbcTemplate` 이었다면 그 쿼리를 실행할 때까지 몰랐을 것이다.

<br/>

## 이름이 길어지면 쓰지 말아야 한다

```java
List<Member> findByNameAndAgeGreaterThanAndCityAndDeletedFalseOrderByCreatedAtDesc(...);
```

<br/>

읽을 수가 없다. 조건이 세 개를 넘어가면 `@Query` 로 직접 쓰는 편이 낫다.

```java
@Query("SELECT m FROM Member m WHERE m.name = :name AND m.age > :age AND m.deleted = false")
List<Member> search(@Param("name") String name, @Param("age") int age);
```

<br/>

`@Query` 도 시작할 때 문법을 검사해준다. 오타가 있으면 안 뜬다.

<br/>

조건이 상황에 따라 붙었다 빠졌다 하면 이것도 한계다.

그때가 앞의 JdbcTemplate 글에서 본 대로 QueryDSL이 필요해지는 지점이다.

```java
조건이 고정이고 단순하다  -> 메서드 이름
조건이 고정이고 복잡하다  -> @Query
조건이 동적으로 바뀐다    -> QueryDSL
```

<br/>

## 기본 제공 메서드도 그냥 생기는 게 아니다

`findById`, `save`, `delete` 같은 것들은 `SimpleJpaRepository` 라는 구현체가 갖고 있다.

```java
public class SimpleJpaRepository<T, ID> implements JpaRepositoryImplementation<T, ID> {

    @Transactional
    public <S extends T> S save(S entity) {
        if (entityInformation.isNew(entity)) {
            entityManager.persist(entity);      // 새 것이면 INSERT
            return entity;
        }
        return entityManager.merge(entity);      // 있는 것이면 UPDATE
    }
}
```

<br/>

`save()` 가 `INSERT` 인지 `UPDATE` 인지 알아서 정해주는 것이 이 코드다.

<br/>

그리고 클래스에 `@Transactional` 이 붙어 있다.

그래서 서비스에 `@Transactional` 을 안 붙여도 `save()` 하나는 트랜잭션 안에서 돈다.

<br/>

다만 여러 개를 묶으려면 서비스에 붙여야 한다.

```java
public void order() {
    itemRepository.save(item);      // 트랜잭션 1
    orderRepository.save(order);    // 트랜잭션 2  (따로 논다)
}
```

<br/>

앞의 서비스 계층 글에서 본 `트랜잭션 경계는 서비스` 라는 원칙이 그래서 필요하다.

<br/>

## merge 를 조심해야 한다

`save()` 에 이미 있는 엔티티를 넘기면 `merge` 가 불린다.

```java
Member member = new Member();
member.setId(1L);                  // 기존 id
member.setName("바뀐이름");
memberRepository.save(member);      // email 은 안 넣었다
```

<br/>

`merge` 는 넘긴 객체의 모든 필드로 덮어쓴다.

`email` 을 안 넣었으니 `null` 로 덮어써진다.

<br/>

그래서 수정할 때는 `save()` 를 쓰지 않고, 조회해서 바꾸는 방식을 쓴다.

```java
@Transactional
public void update(Long id, String name) {
    Member member = memberRepository.findById(id).orElseThrow();
    member.changeName(name);       // 변경 감지가 알아서 UPDATE 를 만든다
}
```

<br/>

앞의 DB 접근(JPA) 글에서 본 변경 감지를 쓰는 것이 더 안전하다.

바꾸려는 필드만 정확히 바뀐다.
