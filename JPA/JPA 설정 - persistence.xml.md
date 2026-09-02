## JPA 설정(Maven 환경)

<br/>

## `pom.xml` 파일 설정하기.

```java
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>jpa-basic</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    
    <dependencies>
        <!-- 만약, 실행 했는데, 안될 경우 추가하기 -->
        <dependency>
            <groupId>javax.xml.bind</groupId>
            <artifactId>jaxb-api</artifactId>
            <version>2.3.0</version>
        </dependency>

        <!-- JPA 하이버네이트 -->
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-entitymanager</artifactId>
            <version>5.3.10.Final</version>
        </dependency>
        
        <!-- H2 데이터베이스 -->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>1.4.199</version>
        </dependency>
    </dependencies>
</project>
```

<br/><br/>

## 메이븐으로 할 경우 위치가 제일 중요하다!

/META-INF 파일 밑으로 만들어야 되는 것이다.

```
/META-INF/persistence.xml 위치
```

<br/>

![이미지](/programming/img/입문284.PNG)

<br/><br/>

## persistence.xml 코드

```java
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.2"
             xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_2.xsd">
    <persistence-unit name="hello">
        <properties>
            <!-- 필수 속성 -->
            <property name="javax.persistence.jdbc.driver" value="org.h2.Driver"/>
            <property name="javax.persistence.jdbc.user" value="sa"/>
            <property name="javax.persistence.jdbc.password" value=""/>
            <property name="javax.persistence.jdbc.url" value="jdbc:h2:tcp://localhost/~/test"/>
            <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>

            <!-- 옵션 -->
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
            <property name="hibernate.use_sql_comments" value="true"/>
            <!--<property name="hibernate.hbm2ddl.auto" value="create" />-->
        </properties>
    </persistence-unit>
</persistence>
```


<br/><br/>

>**Reference** <br/>[자바 ORM 표준 JPA 프로그래밍 - 기본편](https://www.inflearn.com/course/ORM-JPA-Basic)




<br/>

## 궁금증!

```java
persistence.xml 에 적는 것들이 각각 무엇을 결정하는가
```

크게 세 덩어리다.

```java
1. 연결 정보  - 어느 DB 에 어떻게 붙을지
2. 방언       - 어떤 SQL 문법을 쓸지
3. 옵션       - ddl-auto, show-sql 같은 동작 설정
```

<br/>

## 방언(dialect)이 왜 필요한가

앞의 ORM 글에서 본 `중간에서 매핑한다` 의 한 부분이다.

같은 JPQL이 DB마다 다른 SQL이 되어야 한다.

```java
-- 페이징
MySQL      : LIMIT 10 OFFSET 20
Oracle     : ROWNUM 을 쓰는 서브쿼리
SQL Server : OFFSET 20 ROWS FETCH NEXT 10 ROWS ONLY

-- 문자열 타입
Oracle : varchar2
MySQL  : varchar

-- 시퀀스
Oracle     : 지원함
MySQL      : 없음. AUTO_INCREMENT 를 쓴다
```

<br/>

앞의 기본 키 매핑 글에서 본 `IDENTITY 와 SEQUENCE` 선택도 이 차이 때문에 생긴 것이다.

<br/>

방언 클래스가 이 차이를 흡수한다.

`GenerationType.AUTO` 를 쓰면 방언을 보고 알아서 고르는 것도 이 덕분이다.

<br/>

앞의 PSA 글에서 본 구조와 같다. 인터페이스는 하나고 구현이 여러 개다.

<br/>

## 지금은 방언을 안 적어도 된다

```java
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect     # 안 적어도 된다
```

<br/>

스프링 부트가 `spring.datasource.url` 을 보고 알아낸다.

```java
jdbc:mysql://...        -> MySQL 방언
jdbc:postgresql://...   -> PostgreSQL 방언
```

<br/>

앞의 JDBC 표준 인터페이스 글에서 본 `SPI` 로 드라이버를 찾는 것과 같은 발상이다.

`URL 에 이미 정보가 있으니 굳이 두 번 적게 하지 말자` 는 것이다.

<br/>

## 위치가 중요하다는 것

원문의 `위치가 제일 중요하다` 는 지적이 맞다.

```java
src/main/resources/META-INF/persistence.xml
```

<br/>

이 경로에 없으면 못 찾는다.

<br/>

앞의 MyBatis 적용 글에서 본 XML 위치 규칙과 같은 성격의 문제다.

`정해진 자리에 두면 알아서 읽는다` 는 방식이라, 자리가 틀리면 조용히 못 찾는다.

<br/>

스프링 부트는 이 파일 자체가 필요 없다.

`@Entity` 를 스캔해서 찾고, 설정은 `application.yml` 에서 읽는다.

<br/>

## 그래도 한 번 써보는 것이 도움이 된다

앞의 JPA 어떻게 동작하는가 글에서 본 대로,

부트가 무엇을 대신 해주고 있는지가 이때 보인다.

```java
persistence.xml 이 하던 일          부트가 대신하는 것
------------------------------      ------------------------
어떤 DB 인가 (방언)                 URL 을 보고 알아낸다
커넥션 정보                          spring.datasource.*
어떤 엔티티가 있는가                  @Entity 를 스캔한다
EntityManagerFactory 생성           자동 구성이 빈으로 등록한다
```

<br/>

앞의 스프링이란 스프링 부트란 글에서 본 자동 구성이 하는 일이 이것이다.

`173` 개의 자동 구성 클래스 중 하나가 JPA 설정을 맡는다.

<br/>

## 트랜잭션 타입도 여기서 정했다

```xml
<persistence-unit name="hello" transaction-type="RESOURCE_LOCAL">
```

<br/>

```java
RESOURCE_LOCAL - 하나의 DB 만 쓴다. 직접 트랜잭션을 관리한다
JTA            - 여러 DB 를 묶는다. 분산 트랜잭션
```

<br/>

앞의 PSA 글에서 본 `JtaTransactionManager` 가 뒤쪽에 해당한다.

<br/>

`RESOURCE_LOCAL` 이면 `em.getTransaction().begin()` 을 직접 부른다.

스프링에서는 앞의 스프링으로 트랜잭션 해결 글에서 본 대로 `@Transactional` 이 대신한다.

```java
persistence.xml 방식 - em.getTransaction().begin() / commit() 을 손으로
스프링 방식          - @Transactional 하나
```

<br/>

이 차이가 앞의 스프링으로 트랜잭션 해결 글에서 본 `세 가지를 없앴다` 의 첫 번째다.
