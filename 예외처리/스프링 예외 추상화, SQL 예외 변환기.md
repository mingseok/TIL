## 스프링 예외 추상화, SQL 예외 변환기

<br/>

## 예외 추상화가 나오게 된 계기.

데이터베이스에서 SQL 예외들을 잡아서 복구하고 싶을때가 있다.

![이미지](/programming/img/입문281.PNG)

그리하여 DB에서 “오류 코드” 라는 것을 반환 해준다.

그걸 읽어서 우리가 판단하는 것이다.

<br/>

### 하지만, 문제는

```java
오류 코드라는 것이 DB마다 다 다르다는 것이다.
```

<br/><br/>

## 그리하여 스프링이 예외 추상화를 제공해주는 것이다.

![이미지](/programming/img/입문282.PNG)

서비스 계층에서도 스프링이 제공하는 예외를 사용하면 된다. 

- 예를 들어서 JDBC 기술을 사용하든, JPA 기술을 사용하든 스프링이 제공하는 예외를 사용하면 된다.
    
- JDBC나 JPA를 사용할 때 발생하는 예외를 스프링이 제공하는 예외로 변환해주는 역할도 스프링이 제공한다.
    

<br/><br/>

## 스프링의 최고 상위 예외는 런타임

그림에서 보는 것 처럼 런타임 예외를 상속 받았기 때문에 스프링이 제공하는 데이터 접근 계층의 모든 예외는 런타임 예외이다.

```
스프링이 제공하는 데이터 접근 계층의 모든 예외는 런타임 예외이다.
```

<br/><br/>

## 크게 두가지로 구분 짓는다.

- `NonTransient 예외` : 일시적이라는 뜻

    - Transient 하위 예외는 동일한 SQL을 다시 시도했을 때 성공할 가능성이 있다.

    - 예를 들어서 쿼리 타임아웃, 락과 관련된 오류들이다.

        - 이런 오류들은 데이터베이스 상태가 좋아지거나, 락이 풀렸을 때 다시 시도하면 성공

- `Transient 예외` : 일시적이지 않다는 뜻
    - 예를 들어서 SQL 문법 오류, 데이터베이스 제약조건 위배 등이 있다.

<br/><br/>

## 스프링이 제공하는 예외 변환기

```java
@Test
void exceptionTranslator() {
    String sql = "select bad grammar";

    try {
        Connection con = dataSource.getConnection();
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.executeQuery();
    } catch (SQLException e) {
        assertThat(e.getErrorCode()).isEqualTo(42122);

        //org.springframework.jdbc.support.sql-error-codes.xml
        SQLExceptionTranslator exTranslator = new
                SQLErrorCodeSQLExceptionTranslator(dataSource);

        //org.springframework.jdbc.BadSqlGrammarException
        DataAccessException resultEx = exTranslator.translate("select", sql, e);

        assertThat(resultEx.getClass()).isEqualTo(BadSqlGrammarException.class);
    }
}
```

<br/><br/>

## 스프링이 제공하는 SQL 예외 변환기

```java
SQLExceptionTranslator exTranslator = new
SQLErrorCodeSQLExceptionTranslator(dataSource);
DataAccessException resultEx = exTranslator.translate("select", sql, e);
```

- translate() 메서드의 첫번째 파라미터는 읽을 수 있는 설명이고,
    
    두번째는 실행한 sql, 마지막은 발생된 SQLException 을 전달하면 된다. 
    
    이렇게 하면 적절한 스프링 데이터 접근 계층의 예외로 변환해서 반환해준다.
    
- SQL 문법이 잘못되었으므로 `BadSqlGrammarException` 을 반환하는 것을 확인할 수 있다.
    
    눈에 보이는 반환 타입은 최상위 타입인 `DataAccessException` 이지만 실제로는 `BadSqlGrammarException` 예외가 반환된다.
    

`BadSqlGrammarException` 은 최상위 타입인 `DataAccessException` 를 상속 받아서 만들어진다.

<br/><br/>

## 각각의 DB마다 SQL ErrorCode는 다르다.

스프링은 어떻게 각각의 DB가 제공하는 SQL ErrorCode까지 고려해서 예외를 변환할 수 있을까?

바로 `sql-error-codes.xml` 파일에 있다. 

<br/>

스프링 SQL 예외 변환기는 SQL ErrorCode를 이 파일에 대입해서 

어떤 스프링 데이터 접근 예외로 전환해야 할지 찾아낸다.

<br/><br/>

## 정리

- 스프링은 데이터 접근 계층에 대한 일관된 예외 추상화를 제공한다.
- 스프링은 예외 변환기를 통해서 `SQLException` 의 `ErrorCode` 에 맞는 적절한 스프링 데이터 접근 예외로 변환해준다.
    
- 만약 서비스, 컨트롤러 계층에서 예외 처리가 필요하면 특정 기술에 종속적인
    
    SQLException 같은 예외를 직접 사용하는 것이 아니라, 스프링이 제공하는 데이터 접근 예외를 사용하면 된다.
    
- 스프링 예외 추상화 덕분에 특정 기술에 종속적이지 않게 되었다.
    
    이제 JDBC에서 JPA같은 기술로 변경되어도 예외로 인한 변경을 최소화 할 수 있다.


<br/><br/>

## SQL 예외 변환기

```java
public class MemberRepositoryV4_2 implements MemberRepository {
      
    private final DataSource dataSource; // 커넥션을 획득하는 방법을 추상화하는 인터페이스
    private final SQLExceptionTranslator exTranslator; // 인터페이스
    
    public MemberRepositoryV4_2(DataSource dataSource) {
        this.dataSource = dataSource;
        this.exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource); // 구현체
    }

	//.. 생략
}
```

- `private final SQLExceptionTranslator exTranslator;` 추가 (인터페이스)

- `this.exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);` 추가 (구현체)

    - 에러 코드를 기반으로 SQL 인셉션을 스프링 정한 예외 계층으로 변환 해주는 것이다.

    - dataSource 넣어주는 이유는? → 여기서 어떤 DB를 쓰는지 정보를 넣는 것이다.



<br/>

## 궁금증!

```java
어떻게 DB 마다 다른 오류 코드를 하나로 맞추나
```

DB별 오류 코드가 파일에 정리되어 있다.

```java
org/springframework/jdbc/support/sql-error-codes.xml
```

<br/>

이 파일을 열어보면 이런 식이다.

```xml
<bean id="MySQL" class="...SQLErrorCodes">
    <property name="badSqlGrammarCodes">
        <value>1054,1064,1146</value>
    </property>
    <property name="duplicateKeyCodes">
        <value>1062</value>
    </property>
</bean>

<bean id="H2" class="...SQLErrorCodes">
    <property name="duplicateKeyCodes">
        <value>23001,23505</value>
    </property>
</bean>
```

<br/>

`중복 키` 라는 같은 상황을 MySQL은 `1062`, H2는 `23505` 로 알려준다.

<br/>

이 표를 보고 같은 예외로 바꾸는 것이다.

```java
MySQL 1062  ->  DuplicateKeyException
H2    23505 ->  DuplicateKeyException
```

<br/>

## 어느 DB 인지는 어떻게 아나

`DataSource` 에서 메타데이터를 읽는다.

```java
connection.getMetaData().getDatabaseProductName()     ->  "MySQL"
```

<br/>

기동할 때 한 번 읽어두고, 그 이름으로 표를 찾는다.

<br/>

## 요즘은 SQLState 기반도 쓴다

오류 코드는 DB 벤더가 정한 것이고,

`SQLState` 는 표준에 가깝다.

```java
23  - 무결성 제약 위반
42  - 문법 오류
08  - 연결 문제
```

<br/>

앞 두 자리가 분류다.

<br/>

`SQLExceptionSubclassTranslator` 가 이걸 쓴다.

JDBC 4부터 예외 자체가 세분화됐기 때문이다.

```java
SQLIntegrityConstraintViolationException
SQLSyntaxErrorException
SQLTimeoutException
```

<br/>

`sql-error-codes.xml` 방식은 스프링 6에서 기본이 아니게 됐다.

표준 쪽이 충분히 자리 잡았기 때문이다.

<br/>

## @Repository 가 이걸 켠다

```java
@Repository
public class MemberRepository { ... }
```

<br/>

앞의 @Controller, @RestController 글에서 본 그 부가 기능이다.

<br/>

`PersistenceExceptionTranslationPostProcessor` 가

`@Repository` 가 붙은 빈에 프록시를 씌운다.

<br/>

앞의 프록시 패턴 글에서 본 그 방식이다.

```java
메서드를 부른다 -> 예외가 난다 -> 프록시가 잡는다 -> 변환해서 다시 던진다
```

<br/>

`@Service` 에 붙이면 이게 안 된다.

<br/>

## Spring Data JPA 는 자동으로 된다

```java
public interface MemberRepository extends JpaRepository<Member, Long> { }
```

<br/>

`@Repository` 를 안 붙여도 된다.

<br/>

앞의 스프링 데이터 JPA 분석 글에서 본 대로,

`SimpleJpaRepository` 에 이미 `@Repository` 가 붙어 있기 때문이다.

<br/>

## 무엇이 좋아지는가

서비스 층이 기술을 모르게 된다.

```java
public void join(Member member) {
    try {
        memberRepository.save(member);
    } catch (DuplicateKeyException e) {           // JDBC 든 JPA 든 같은 예외
        throw new ServiceException(ErrorCode.DUPLICATE_EMAIL);
    }
}
```

<br/>

JDBC에서 JPA로 바꿔도 이 코드가 안 바뀐다.

<br/>

앞의 PSA(Portable Service Abstraction) 글에서 본 그 이득이다.

```java
기술은 아래에서 바뀌고, 위층은 그대로 있는다
```

<br/>

## 그래도 완전하지는 않다

변환 표에 없는 코드는 이렇게 나온다.

```java
UncategorizedSQLException
```

<br/>

`분류 못 하겠다` 는 뜻이다. 원본 `SQLException` 은 안에 들어 있다.

<br/>

그리고 예외의 이름이 같아도 상황이 미묘하게 다를 수 있다.

`DataIntegrityViolationException` 이 유니크 제약 위반인지 외래 키 위반인지는

이 예외만으로는 구분이 안 된다.

<br/>

정확히 나누려면 결국 원본을 봐야 한다.

```java
Throwable cause = NestedExceptionUtils.getMostSpecificCause(e);
```

<br/>

앞의 예외 포함과 스택 트레이스 글에서 본 그 메서드다.

<br/>

추상화가 대부분을 덮어주지만, 마지막 몇 퍼센트는 뚫고 내려가야 하는 것이다.

추상화의 성질이 원래 그렇다.
