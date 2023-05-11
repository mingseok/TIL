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
package org.example.hellojpa;

import javax.persistence.Persistence;

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
package org.example.hellojpa;

import javax.persistence.Entity;
import javax.persistence.Id;

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
package org.example.hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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
package org.example.hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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



