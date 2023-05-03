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

