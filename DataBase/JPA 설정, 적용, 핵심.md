## JPA 설정, 적용, 핵심

참고 : 하이버네이트 라고 말한다면 JPA라고 생각하면 된다.

<br/>

### `build.gradle` 의존 관계를 추가한다.

```java
//JPA, 스프링 데이터 JPA 추가
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
```

<br/>

### main디렉토리에서의 - `application.properties`설정 추가.

```java
#JPA log
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

<br/>

### test디렉토리에서의 - `application.properties` 설정 추가.

```java
#JPA log
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

<br/>

### 설명

`org.hibernate.SQL=DEBUG` : 하이버네이트가 생성하고 실행하는 SQL을 확인할 수 있다.

`org.hibernate.type.descriptor.sql.BasicBinder=TRACE` : SQL에 바인딩 되는 파라미터를 확인할 수 있다.

<br/><br/>

## JPA의 핵심

- `JPA`에서 가장 중요한 부분은 객체와 테이블을 매핑하는 것이다.

- `JPA`의 모든 데이터 변경은 `‘트랜잭션’` 안에서 이루어 진다.
- `JPA`에서는 `EntityManager` 를 의존 관계 주입을 받아야 한다. → 이것이 JPA이다. 즉, 여기에 `‘저장’`, `‘조회’` 를 하는 것이다.

```java
private final EntityManager em;

public JpaItemRepository(EntityManager em) {
    this.em = em;
}
```

<br/><br/>

## `Item.class`

```java
@Data
@Entity
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_name", length = 10)
    private String itemName;
    private Integer price;
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
```

- `@Entity` : `JPA`에서 관리하는 것이라고 선언하는 것이다.

    - 즉, 이 에노테이션이 있어야 JPA가 인식할 수 있다.

    - 또한, 테이블이랑 매핑이 되어 관리 된다고 생각해도 된다.

- `@Id` : `PK` 라고 알려주는 애노테이션이다.
    - 이걸 사용 시 해당 변수는 `PK`가 된다.
- `@GeneratedValue(strategy = GenerationType.IDENTITY)` : `@Id`와 세트로 사용한다.

    - `IDENTITY` 전략이란? -> DB에서 값을 넣어주는 전략이라고 생각하기.

    - 즉 `Auto increment`라고 생각하면 된다.
- `@Column(name = "item_name", length = 10)` :
    - `itemName` 변수명은 → 데이터 베이스에서 `item_name` 랑 매핑이 된다고 선언하는 것이다.

    - `length` 길이 제한도 가능하다.
    - 하지만, `@Column(name = "item_name", length = 10)` 이것도 생략이 가능하다.
        - 카멜 케이스를 언더스코어로 자동으로 변환 해준다. `itemName` → `item_name`

<br/><br/>

## JPA는 public 또는 protected 의 기본 생성자가 필수이다.

기본 생성자를 꼭 넣어주자.

```java
public Item() {}
```

<br/><br/>

## `JpaItemRepository.class`

```java
@Slf4j
@Repository
@Transactional
public class JpaItemRepository implements ItemRepository {

    private final EntityManager em;

    public JpaItemRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Item save(Item item) {
        em.persist(item); // persist : "영구적으로 보존한다" 라는 뜻이다.
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item findItem = em.find(Item.class, itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        Item item = em.find(Item.class, id);
        return Optional.ofNullable(item);
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String jpql = "select i from Item i";
        
        // .. 생략

        return query.getResultList();
    }
}
```

- `private final EntityManager em` : JPA의 모든 동작은 엔티티 매니저를 통해서 이루어진다.

    - 엔티티 매니저는 내부에 데이터소스를 가지고 있고, 데이터베이스에 접근할 수 있다.

- `@Transactional` : JPA의 모든 데이터 변경(등록, 수정, 삭제)은 트랜잭션 안에서 이루어져야 한다.

    - 조회는 `트랜잭션`이 없어도 가능하다.

    - 변경의 경우 일반적으로 서비스 계층에서 트랜잭션을 시작하기 때문에 문제가 없다.
    - 일반적으로는 비즈니스 로직을 시작하는 `서비스 계층`에 `트랜잭션`을 걸어주는 것이 맞다.

<br/><br/>

## save() - 저장

- `em.persist(item)` : JPA에서 객체를 테이블에 저장할 때는 엔티티 매니저가 제공하는 `persist()` 메서드를 사용하면 된다.

<br/>

### JPA가 만들어서 실행한 SQL

```sql
insert into item (id, item_name, price, quantity) values (null, ?, ?, ?)

또는

insert into item (id, item_name, price, quantity) values (default, ?, ?, ?)

또는

insert into item (item_name, price, quantity) values (?, ?, ?)
```

- JPA가 만들어서 실행한 SQL을 보면 id 에 값이 빠져있는 것을 확인할 수 있다.
- PK 키 생성 전략을 IDENTITY 로 사용했기 때문에 JPA가 이런 쿼리를 만들어서 실행한 것이다.
    
    물론 쿼리 실행 이후에 Item 객체의 id 필드에 데이터베이스가 생성한 PK값이 들어가게 된다. 
    
    (JPA가 INSERT SQL 실행 이후에 생성된 ID 결과를 받아서 넣어준다)


<br/><br/>

## update() - 수정

`em.update()` : 같은 메서드를 호출 해야 될 것 같은데, 수정에서는 그러지 않는다.

```
그런데 어떻게 UPDATE SQL이 실행되는 것일까?
```

- `JPA`는 트랜잭션이 커밋되는 시점에, 변경된 엔티티 객체가 있는지 확인한다.
    
    그리고 특정 엔티티 객체가 변경된 경우에는 `update SQL`을 실행한다.
    
<br/>

### 내가 내것을 변경하였는데? , 어떻게?

JPA가 처음 조회하는 시점에, 원본 객체를 복사해서 내부에 ‘스냅샷’ 이라는 것을 가지고 있다.

(우리 눈에는 보이지 않는다) 

<br/><br/>

## findById() - 단건 조회

```java
public Optional<Item> findById(Long id) {
    Item item = em.find(Item.class, id);
    return Optional.ofNullable(item);
}
```

- JPA에서 엔티티 객체를 PK를 기준으로 조회할 때는 `find()` 를 사용한다.
    
    - 조회 타입과, `PK` 값을 주면 된다. 
    

그러면 `JPA`가 다음과 같은 조회 `SQL`을 만들어서 실행하고, 결과를 객체로 바로 변환해준다.

<br/><br/>

## findAll - 목록 조회 (JPQL)

```
JPA에서 단순히 PK를 기준으로 조회하는 것이 아닌, 
여러 데이터를 복잡한 조건으로 데이터를 조회하려면 어떻게 하면 될까?
```

`JPA`는 `JPQL`(Java Persistence Query Language)이라는 객체지향 쿼리 언어를 제공한다.



주로 여러 데이터를 `복잡한 조건`으로 `조회`할 때 사용한다.

<br/>

SQL이 테이블을 대상으로 한다면, JPQL은 엔티티 객체를 대상으로 SQL을 실행한다 생각하면 된다.

엔티티 객체를 대상으로 하기 때문에 `from` 다음에 `Item` 엔티티 객체 이름이 들어간다.

<br/>

엔티티 객체와 속성의 대소문자는 구분해야 한다.

```java
String jpql = "select i from Item i";
```

<br/><br/>

>**Reference** <br/>[스프링 DB 2편 - 데이터 접근 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-db-2/dashboard)


<br/>

## 궁금증!

```java
영속성 컨텍스트라는 게 왜 필요한 걸까? 바로 DB 에 쓰면 안 되나?
```

세 가지를 얻기 위해서다. 하나씩 보면 각각 이득이 다르다.

<br/>

## 첫번째) 1차 캐시

```java
@Transactional
public void test() {
    Member a = memberRepository.findById(1L).orElseThrow();   // SELECT 나간다
    Member b = memberRepository.findById(1L).orElseThrow();   // 쿼리가 안 나간다

    System.out.println(a == b);      // true
}
```

<br/>

두 번째 조회는 DB에 안 간다. 영속성 컨텍스트에 이미 있기 때문이다.

<br/>

그리고 `a == b` 가 `true` 다. 같은 객체다.

같은 트랜잭션 안에서는 같은 행이 항상 같은 객체로 보장된다.

<br/>

앞의 equals hashCode 글에서 본 동일성 문제가 여기서는 안 생긴다.

`==` 로 비교해도 되는 이유가 이 보장 덕분이다.

<br/>

## 두번째) 쓰기 지연

```java
@Transactional
public void test() {
    memberRepository.save(new Member("a"));   // INSERT 가 안 나간다
    memberRepository.save(new Member("b"));   // 아직 안 나간다
    memberRepository.save(new Member("c"));   // 아직도 안 나간다
}   // 여기서 세 개가 한꺼번에 나간다
```

<br/>

모아뒀다가 커밋 직전에 한 번에 보낸다.

앞의 DBCP 글에서 본 대로 네트워크 왕복이 비싸니, 세 번을 한 번으로 줄이는 것이다.

<br/>

`hibernate.jdbc.batch_size` 를 설정하면 진짜로 하나의 `INSERT` 로 묶어서 보낸다.

```java
spring.jpa.properties.hibernate.jdbc.batch_size=50
```

<br/>

다만 앞의 JdbcTemplate 글에서 본 문제가 여기서 나온다.

`JdbcTemplate` 과 섞어 쓰면 아직 안 나간 것을 못 본다.

<br/>

## 세번째) 변경 감지

```java
@Transactional
public void updateName(Long id, String name) {
    Member member = memberRepository.findById(id).orElseThrow();
    member.changeName(name);
    // save() 를 안 불렀는데 UPDATE 가 나간다
}
```

<br/>

조회할 때 복사본(스냅샷)을 만들어두고, 커밋할 때 원본과 비교한다.

다른 곳이 있으면 그 부분만 `UPDATE` 를 만든다.

<br/>

앞의 트랜잭션 옵션 글에서 본 `readOnly = true` 가 이 스냅샷을 안 만드는 것이다.

<br/>

## 그래서 준영속 상태가 문제가 된다

영속성 컨텍스트가 관리하는 동안만 위 세 가지가 동작한다.

```java
영속 (managed)   - 영속성 컨텍스트가 관리 중. 변경 감지가 동작한다
준영속 (detached) - 관리에서 벗어남. 변경해도 아무 일 없다
비영속 (transient) - 아직 저장된 적 없음
```

<br/>

트랜잭션이 끝나면 영속성 컨텍스트도 닫히고, 안에 있던 엔티티는 전부 준영속이 된다.

```java
Member member = memberService.find(1L);   // 트랜잭션이 여기서 끝났다
member.changeName("바뀐이름");             // 아무 일도 안 일어난다
```

<br/>

앞의 DB 접근(JPA) 글에서 본 `LazyInitializationException` 도 준영속 상태 때문이다.

<br/>

## 그래서 엔티티를 밖으로 내보내지 않는다

앞의 DTO 글에서 본 이유에 이것이 하나 더 붙는다.

```java
엔티티를 컨트롤러까지 들고 나가면
  - 준영속이라 지연 로딩이 안 된다
  - 변경 감지도 안 된다
  - 그런데 코드에서는 그게 안 보인다
```

<br/>

트랜잭션 안에서 DTO로 바꿔서 나가면 이 혼란이 사라진다.

```java
@Transactional(readOnly = true)
public MemberResponse find(Long id) {
    Member member = memberRepository.findById(id).orElseThrow();
    return MemberResponse.from(member);      // 필요한 것을 여기서 다 꺼낸다
}
```

<br/>

## ddl-auto 설정을 조심해야 한다

```java
spring.jpa.hibernate.ddl-auto=create        # 뜰 때 테이블을 다 지우고 새로 만든다
spring.jpa.hibernate.ddl-auto=update        # 바뀐 것만 반영한다
spring.jpa.hibernate.ddl-auto=validate      # 엔티티와 테이블이 맞는지 검사만 한다
spring.jpa.hibernate.ddl-auto=none          # 아무것도 안 한다
```

<br/>

운영에서 `create` 를 쓰면 서버가 뜰 때마다 데이터가 전부 날아간다.

`update` 도 위험하다. 컬럼을 지우는 것은 안 해주고 추가만 하니, 스키마가 조금씩 어긋난다.

<br/>

운영은 `validate` 나 `none` 으로 두고, 스키마 변경은 마이그레이션 도구로 한다.

```java
로컬 개발  - create 또는 update
운영       - validate (엔티티와 테이블이 다르면 아예 안 뜬다)
```

<br/>

`validate` 로 두면 앞의 스프링 컨테이너 글에서 본 대로 문제가 있을 때 시작 자체가 실패한다.

배포하자마자 알 수 있으니, 조용히 어긋나는 것보다 낫다.
