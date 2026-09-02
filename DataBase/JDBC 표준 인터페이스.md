## JDBC 표준 인터페이스

![이미지](/programming/img/입문210.PNG)

대표적으로 다음 3가지 기능을 표준 인터페이스로 정의해서 제공한다.

`java.sql.Connection` - 연결

`java.sql.Statement` - SQL을 담은 내용

`java.sql.ResultSet` - SQL 요청 응답

<br/><br/>

자바는 이렇게 표준 인터페이스를 정의해두었다. 

이제부터 개발자는 이 `표준 인터페이스`만 사용해서 개발하면 된다.

```
하지만, 인터페이스만 있다고 해서 기능이 동작하지는 않는다. 
```


<br/>

`JDBC 인터페이스`를 각각의 DB 벤더(회사)에서 자신의 DB에 맞도록 구현해서 

라이브러리로 제공하는데, 이것을 `JDBC 드라이버`라 한다.

<br/>

예를 들어, `MySQL DB`에 접근할 수 있는 것은 `MySQL JDBC 드라이버`라 하고, 

`Oracle DB`에 접근할 수 있는 것은 `Oracle JDBC 드라이버`라 한다.


<br/>

![이미지](/programming/img/입문211.PNG)

<br/>

![이미지](/programming/img/입문212.PNG)

<br/><br/>

## 정리

`JDBC`의 등장으로 다음 2가지 문제가 해결되었다

- 데이터베이스를 `다른 종류`의 데이터베이스로 변경하면 애플리케이션 서버의
    
    데이터베이스 사용 코드도 `함께 변경`해야 하는 문제.
    
    - 애플리케이션 로직은 이제 `JDBC 표준 인터페이스`에만 의존한다.
        
        따라서 데이터베이스를 다른 종류의 데이터베이스로 변경하고 싶으면 
        
        `JDBC 구현 라이브러리`만 변경하면 된다.
        
- 개발자가 각각의 데이터베이스마다 `커넥션 연결`, `SQL 전달`, `결과를 응답 받는 방법`을 새로 학습해야 하는 문제 해결.
    
    - 개발자는 JDBC 표준 인터페이스 사용법만 학습하면 된다.

<br/><br/>

## SQL Mapper

![이미지](/programming/img/입문213.PNG)

- `장점:` JDBC를 편리하게 사용하도록 도와준다.

    - SQL 응답 결과를 객체로 편리하게 변환해준다.

    - JDBC의 반복 코드를 제거해준다.

- `단점:` 개발자가 SQL을 직접 작성해야한다.

- 대표 기술: 스프링 JdbcTemplate, MyBatis

<br/><br/>

## ORM 기술

![이미지](/programming/img/입문214.PNG)

- ORM은 객체를 관계형 데이터베이스 테이블과 매핑해주는 기술이다.

    - 이 기술 덕분에 개발자는 반복적인 SQL을 직접 작성하지 않고, ORM 기술이 개발자 대신에 SQL을 동적으로 만들어 실행해준다. 
        
- 대표 기술: JPA, 하이버네이트, 이클립스링크
    - JPA는 자바 진영의 ORM 표준 인터페이스이다.

<br/>

## 핵심.

이런 기술들도 내부에서는 모두 JDBC를 사용한다






<br/><br/>

>**Reference** <br/>[스프링 DB 2편 - 데이터 접근 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-db-2/dashboard)





<br/>

## 궁금증!

```java
JDBC 가 "표준 인터페이스" 라는 게 코드에서 어떻게 드러날까?
```

`java.sql` 패키지 안에 있는 것들이 전부 인터페이스라는 점에서 드러난다.

```java
public interface Connection { ... }
public interface Statement { ... }
public interface PreparedStatement extends Statement { ... }
public interface ResultSet { ... }
```

<br/>

구현이 하나도 없다. `무엇을 할 수 있는지` 만 정해놓은 것이다.

<br/>

실제 구현은 각 DB 회사가 만들어서 배포한다. 그것이 `드라이버` 다.

```java
mysql-connector-j-9.7.0.jar        -- MySQL 이 만든 구현체
postgresql-42.x.jar                -- PostgreSQL 이 만든 구현체
ojdbc11.jar                        -- Oracle 이 만든 구현체
```

<br/>

그래서 우리 코드는 인터페이스만 쓰고, jar 만 바꾸면 DB가 바뀐다.

```java
Connection connection = dataSource.getConnection();   // 타입은 인터페이스
System.out.println(connection.getClass().getName());
// -> com.mysql.cj.jdbc.ConnectionImpl  (드라이버가 만든 구현체)
```

<br/>

앞의 PSA 글에서 본 구조와 완전히 같다.

인터페이스는 표준이 정하고, 구현은 각자 만들고, 쓰는 쪽은 인터페이스만 안다.

<br/>

## 드라이버는 어떻게 찾아지는가

예전에는 직접 등록해야 했다.

```java
Class.forName("com.mysql.cj.jdbc.Driver");    // 옛날 코드
```

<br/>

지금은 이 줄이 필요 없다. `JDBC 4.0` 부터 자동으로 찾는다.

<br/>

드라이버 jar 안을 열어보면 이런 파일이 들어 있다.

```java
META-INF/services/java.sql.Driver
```

<br/>

그 안에 구현 클래스 이름이 적혀 있고, `DriverManager` 가 클래스패스를 훑어서 읽는다.

이 방식을 `SPI(Service Provider Interface)` 라고 한다.

<br/>

앞의 스프링 부트 글에서 본 자동 구성의 `AutoConfiguration.imports` 파일과 같은 발상이다.

`jar 안에 목록을 넣어두면 프레임워크가 읽어간다` 는 방식이다.

<br/>

## PreparedStatement 를 써야 하는 이유

`Statement` 와 `PreparedStatement` 중 후자를 쓰라고 하는 이유가 두 가지다.

<br/>

### 첫번째) SQL 인젝션을 막는다

```java
// 위험
String sql = "SELECT * FROM member WHERE id = '" + input + "'";
```

<br/>

`input` 에 `1' OR '1'='1` 을 넣으면 이렇게 된다.

```sql
SELECT * FROM member WHERE id = '1' OR '1'='1'
```

<br/>

조건이 항상 참이 되어 전체가 조회된다.

`1'; DROP TABLE member; --` 를 넣으면 테이블이 날아간다.

<br/>

`PreparedStatement` 는 값을 따로 보낸다.

```java
PreparedStatement ps = connection.prepareStatement("SELECT * FROM member WHERE id = ?");
ps.setString(1, input);
```

<br/>

SQL 구조를 먼저 서버에 보내고 값은 나중에 보내기 때문에,

값에 무엇이 들어 있든 SQL 문법으로 해석되지 않는다.

```java
문자열을 이어 붙인다 -> 값이 SQL 의 일부가 된다
? 로 넘긴다         -> 값은 끝까지 값으로만 남는다
```

<br/>

### 두번째) 실행 계획을 재사용한다

DB는 SQL을 받으면 파싱하고 실행 계획을 짠다.

앞의 DB 인덱스 글에서 본 `EXPLAIN QUERY PLAN` 의 그 계획이다.

<br/>

`?` 를 쓰면 SQL 문자열이 항상 같으니, 한 번 짠 계획을 재사용할 수 있다.

값을 이어 붙이면 매번 다른 SQL이 되어서 매번 새로 짜야 한다.

```java
SELECT * FROM member WHERE id = 1     -- 새 SQL
SELECT * FROM member WHERE id = 2     -- 또 새 SQL
SELECT * FROM member WHERE id = ?     -- 항상 같은 SQL
```

<br/>

## 그런데 JDBC 를 직접 쓸 일은 거의 없다

앞의 JdbcTemplate 글에서 본 반복 코드 때문이다.

```java
Connection connection = null;
PreparedStatement ps = null;
ResultSet rs = null;
try {
    // ... 진짜 하고 싶은 일은 두세 줄
} catch (SQLException e) {
    throw new RuntimeException(e);
} finally {
    if (rs != null) try { rs.close(); } catch (SQLException e) {}
    if (ps != null) try { ps.close(); } catch (SQLException e) {}
    if (connection != null) try { connection.close(); } catch (SQLException e) {}
}
```

<br/>

앞의 try-catch-finally 글에서 본 그 중첩된 정리 코드다.

<br/>

그래도 JDBC를 알아야 하는 이유는, 위에 얹힌 것들이 전부 이걸 쓰기 때문이다.

```java
JDBC (표준 인터페이스)
  -> JdbcTemplate     (반복 코드를 없앰)
  -> MyBatis          (SQL 을 XML 로 분리)
  -> JPA / Hibernate  (객체와 테이블을 매핑)
```

<br/>

문제가 생겼을 때 결국 이 층까지 내려가서 봐야 하는 경우가 있다.
