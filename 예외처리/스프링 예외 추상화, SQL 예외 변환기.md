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


