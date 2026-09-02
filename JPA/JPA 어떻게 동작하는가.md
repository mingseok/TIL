## JPA 어떻게 동작하는가?

<br/>

## JPA 구동 방식

![이미지](/programming/img/입문285.PNG)

1. `JPA`는 `Persistence` 라는 클래스가 있다.

    - 시작은 여기서부터 시작하는 것이다.
2. `META-INF` → `persistence.xml` 파일에서 설정 정보를 읽는다.
3. `EntityManagerFactory` 라는 클래스를 만든다.
    - 그리하여 JPA 작업이 필요할때 마다 `EntityManagerFactory` 클래스에서 공장처럼 
    
       필요할때 마다 `EntityManager`를 만들어 생성한다.
        
4. 그리하여 `EntityManager`로 사용하게 되는 것이다.

```
- 추가로 JPA의 모든 작업은 트랜잭션 안에서 작업을 해야 된다.
- 엔티티 매니저는 쓰레드간에 공유X (사용하고 버려야 한다).
```

<br/><br/>

## 코드 작성해보기.

```java
public class JpaMain {
    public static void main(String[] args) {
        Persistence.createEntityManagerFactory("hello");
    }
}
```

<br/><br/>

## 여기서 `“hello”` 는 뭐야?

`name`의 이름을 말하는 것이다.

![이미지](/programming/img/입문286.PNG)

<br/><br/>

## 테이블 생성

```sql
create table Member ( 
   id bigint not null, 
   name varchar(255), 
   primary key (id) 
);
```

<br/>

![이미지](/programming/img/입문287.PNG)

<br/><br/>

## `Member.class` 코드

객체와 테이블을 생성하고 매핑하기

```java
@Entity
public class Member {

    @Id
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

- `@Entity:` JPA가 관리할 객체

- `@Id:` 데이터베이스 PK와 매핑

<br/><br/>

## `JpaMain.class` 코드

`EntityManagerFactory` 는 로딩 시점에 딱 하나만 만들어 놔야 한다.

실제로, DB에 저장하거나 하는 트랜잭션 단위들은 전부 `EntityManager` 만들어 줘야 하는 것이다.

```java
public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction(); // 트랜잭션 생성
        tx.begin(); // 트랜잭션 시작

        /**
         * 만드는 로직
         */
        Member member = new Member();
        member.setId(1L);
        member.setName("HelloA");

        /**
         * JPA 저장
         * persist -> 저장 명령어
         */
        em.persist(member);

        /**
         * 커밋
         */
        tx.commit();

        em.close();
        emf.close();
    }
}
```

<br/><br/>

## 위 코드를 정석으로 작성하려고 한다면?

이렇게 작성해야 한다.

```java
public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction(); // 트랜잭션 생성
        tx.begin(); // 트랜잭션 시작

        try {
            Member member = new Member();
            member.setId(2L);
            member.setName("HelloB");

            em.persist(member);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
```

<br/><br/>

## 실행 시켜보면 이렇다.

![이미지](/programming/img/입문288.PNG)

이렇게 SQL 쿼리를 만들어 주는 것이다.

<br/><br/>

## DB에서 확인 해보면 저장되어 있는 것을 알 수 있다.

![이미지](/programming/img/입문289.PNG)

<br/><br/>

## 만약 테이블 이름이 다르다면?

`@Table(name = "USER")` 라고 설정해주면 되는 것이다.

그렇다면, 쿼리가 나갈때 `“USER”` 라는 테이블에 `insert` 하라고 나가게 되는 것이다.

```java
@Entity
@Table(name = "USER")
public class Member {

    @Id
    private Long id;
    private String name;

    // ... 생략
```

<br/><br/>

## 조회 할때는?

```java
try {
    Member findMember = em.find(Member.class, 1L);
    em.persist(findMember);

    tx.commit();
} catch (Exception e) {
    tx.rollback();
} finally {
    em.close();
}
emf.close();
```

<br/><br/>

## 수정 할때는?

```java
try {
    Member findMember = em.find(Member.class, 1L);
    findMember.setName("HelloJPA");

    tx.commit();
} catch (Exception e) {
    tx.rollback();
} finally {
    em.close();
}
emf.close();
```

<br/><br/>

## 정리

결국 `‘em’` -> 엔티티매니저가 결국 자바 컬렉션이라고 이해하면 된다.

```
em.persist() -> 저장
em.find() -> 수정
```


<br/><br/>

>**Reference** <br/>[자바 ORM 표준 JPA 프로그래밍 - 기본편](https://www.inflearn.com/course/ORM-JPA-Basic)




<br/>

## 궁금증!

```java
EntityManager 와 EntityManagerFactory 를 왜 나눠 놓았을까?
```

만드는 비용이 완전히 다르기 때문이다.

```java
EntityManagerFactory - 애플리케이션에 하나. 만들 때 아주 비싸다
EntityManager        - 트랜잭션마다 하나. 가볍다. 쓰고 버린다
```

<br/>

`EntityManagerFactory` 를 만들 때 하는 일이 많다.

```java
설정 파일을 읽는다
엔티티 클래스를 전부 스캔해서 매핑 정보를 만든다
DB 방언에 맞는 SQL 생성기를 준비한다
커넥션 풀을 만든다
```

<br/>

앞의 스프링 컨테이너 내부 동작 글에서 본 `미리 다 만들어둔다` 와 같은 방식이다.

시작할 때 한 번 비용을 치르고, 그 뒤로는 꺼내 쓰기만 한다.

<br/>

## EntityManager 는 왜 매번 새로 만드는가

앞의 영속성 컨텍스트 글에서 본 대로, 그 안에 1차 캐시와 스냅샷이 들어 있다.

```java
Map<식별자, 엔티티> 1차 캐시
Map<식별자, 값 복사본> 스냅샷
```

<br/>

이것을 여러 요청이 공유하면 큰일 난다.

```java
A 요청이 조회한 회원을 B 요청이 보게 된다
A 요청이 수정 중인 값을 B 요청이 커밋해버린다
```

<br/>

그래서 요청마다 새로 만들어서 쓰고 버린다.

<br/>

앞의 커넥션 풀, DataSource 글에서 본 `ThreadLocal` 이 이것을 처리한다.

```java
스레드마다 다른 EntityManager 를 매달아둔다
같은 요청 안에서는 그것을 꺼내 쓴다
요청이 끝나면 닫는다
```

<br/>

## 그런데 스프링에서는 필드로 주입받는다

```java
@PersistenceContext
private EntityManager em;
```

<br/>

싱글톤 빈에 요청마다 달라져야 하는 것을 필드로 두는 것처럼 보인다.

앞의 싱글톤 레지스트리 글에서 본 `빈에 상태를 두면 안 된다` 를 어긴 것 같다.

<br/>

실제로 주입되는 것은 프록시다.

```java
System.out.println(em.getClass());
// -> class jdk.proxy.$Proxy123
```

<br/>

이 프록시가 메서드 호출을 받을 때마다 `ThreadLocal` 에서 진짜 `EntityManager` 를 꺼내 넘긴다.

<br/>

앞의 프로토타입 사용 이유 글에서 본 `proxyMode` 와 같은 방식이다.

`주입 시점` 과 `사용 시점` 을 떼어놓는 것이다.

```java
em.persist(member)
  -> 프록시가 가로챈다
  -> 현재 스레드의 진짜 EntityManager 를 찾는다
  -> 거기에 넘긴다
```

<br/>

## 트랜잭션이 반드시 필요한 이유

```java
em.persist(member);      // 트랜잭션 없이 부르면?
```

```java
TransactionRequiredException: No EntityManager with actual transaction available
```

<br/>

앞의 플러시 글에서 본 대로, JPA는 커밋 시점에 SQL을 보낸다.

트랜잭션이 없으면 커밋할 시점 자체가 없으니 보낼 방법이 없는 것이다.

<br/>

조회는 트랜잭션 없이도 된다.

```java
em.find(Member.class, 1L);      // 트랜잭션 없어도 동작한다
```

<br/>

보낼 것이 없고 읽기만 하니 상관없기 때문이다.

<br/>

## persistence.xml 대신 스프링 부트가 하는 일

앞의 스프링이란 스프링 부트란 글에서 본 자동 구성이 이 파일을 대신한다.

```java
persistence.xml 이 하던 일
  - 어떤 DB 인가 (방언)
  - 커넥션 정보
  - 어떤 엔티티가 있는가
  - ddl-auto, show-sql 같은 옵션

스프링 부트
  - application.yml 에 적으면 된다
  - 엔티티는 @Entity 를 스캔해서 찾는다
  - 방언은 URL 을 보고 알아낸다
```

<br/>

앞의 DB 연결 글에서 본 세 줄이 그 대부분을 대체하는 셈이다.

```java
spring.datasource.url=jdbc:mysql://localhost:3306/facegram
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.format_sql=true
```

<br/>

그래도 `persistence.xml` 을 한 번 써보는 것이 도움이 된다.

부트가 무엇을 대신 해주고 있는지가 그때 보이기 때문이다.
