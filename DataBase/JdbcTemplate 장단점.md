## JdbcTemplate 장/단점

<br/>

## 장점.

- 반복 문제를 해결해 준다.
- 설정이 간단하다. (라이브러리 하나만 추가 하면 끝)

<br/>

## 단점.

- 동적 쿼리 해결하는 것이 힘들다.
- `‘이름 지정 파라미터’`: 장점이자.. 단점인 `‘이름 지정 파라미터’`는 순서대로 하게 된다면,
    
    개발자가 실수로 순서를 바꾸는 순간 큰일 나는 것이다.
    

<br/>

실무에서 가장 간단하고 실용적인 방법으로 SQL을 사용하려면 JdbcTemplate을 사용하면 된다.

하지만, JdbcTemplate의 최대 단점은 → 동적 쿼리 문제를 해결하지 못한다는 점이다

<br/>

그리고 SQL을 자바 코드로 작성하기 때문에 SQL 라인이 코드를 넘어갈 때 마다 

문자 더하기를 해주어야 하는 단점도 있다.

<br/><br/>

## 그리하여

동적 쿼리 문제를 해결하면서 동시에 SQL도 편리하게 작성할 수 있게 

도와주는 기술이 바로 MyBatis 이다.
<br/>

## 궁금증!

```java
JdbcTemplate 이 없애준다는 반복 코드가 정확히 무엇일까?
```

앞의 JDBC 글에서 본 그 형태다. 하나씩 세어보면 이렇다.

```java
public Member find(Long id) {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
        connection = dataSource.getConnection();                    // 1. 커넥션 얻기
        ps = connection.prepareStatement("SELECT * FROM member WHERE id = ?");   // 2. SQL 준비
        ps.setLong(1, id);                                          // 3. 파라미터 바인딩
        rs = ps.executeQuery();                                     // 4. 실행
        if (rs.next()) {                                            // 5. 결과 꺼내기
            return new Member(rs.getLong("id"), rs.getString("name"));
        }
        throw new NoSuchElementException();
    } catch (SQLException e) {
        throw new RuntimeException(e);                              // 6. 예외 변환
    } finally {
        if (rs != null) try { rs.close(); } catch (SQLException e) {}       // 7. 자원 정리
        if (ps != null) try { ps.close(); } catch (SQLException e) {}
        if (connection != null) try { connection.close(); } catch (SQLException e) {}
    }
}
```

<br/>

여기서 개발자가 실제로 정해야 하는 것은 세 가지뿐이다.

```java
2. 어떤 SQL 을 실행할지
3. 어떤 값을 넣을지
5. 결과를 어떤 객체로 만들지
```

<br/>

나머지 `1, 4, 6, 7` 은 어떤 쿼리를 짜든 똑같다.

<br/>

`JdbcTemplate` 은 그 똑같은 부분을 자기가 하고, 다른 부분만 받는다.

```java
public Member find(Long id) {
    return jdbcTemplate.queryForObject(
            "SELECT * FROM member WHERE id = ?",          // 2
            memberRowMapper(),                            // 5
            id);                                          // 3
}
```

<br/>

`20` 줄이 `4` 줄이 됐다. 그리고 `finally` 를 빠뜨릴 걱정이 없어졌다.

<br/>

## 이 구조를 템플릿 콜백 패턴이라고 한다

앞의 추상클래스 글에서 본 템플릿 메서드 패턴과 같은 발상이다.

```java
변하지 않는 순서 - 템플릿이 갖고 있는다
변하는 부분     - 밖에서 받는다
```

<br/>

다른 점은 `상속` 이 아니라 `파라미터로 받는다` 는 것이다.

```java
템플릿 메서드 패턴 - 추상 메서드를 자식이 구현한다 (상속)
템플릿 콜백 패턴   - 함수를 파라미터로 넘긴다 (조합)
```

<br/>

앞의 상속 글에서 본 대로 상속은 결합이 강하다.

`JdbcTemplate` 을 상속받아 쓰라고 했다면 훨씬 불편했을 것이다.

<br/>

`RowMapper` 가 그 콜백이다.

```java
public interface RowMapper<T> {
    T mapRow(ResultSet rs, int rowNum) throws SQLException;
}
```

<br/>

메서드가 하나뿐이라 람다로 쓸 수 있다.

```java
jdbcTemplate.query("SELECT * FROM member",
        (rs, rowNum) -> new Member(rs.getLong("id"), rs.getString("name")));
```

<br/>

## 예외 변환이 조용히 해주는 일

`6번` 이 그냥 감싸는 것이 아니다.

`JdbcTemplate` 은 `SQLException` 을 스프링의 예외 계층으로 바꿔서 던진다.

```java
SQLException (에러 코드 1062)  ->  DuplicateKeyException
SQLException (에러 코드 1213)  ->  DeadlockLoserDataAccessException
```

<br/>

앞의 PSA 글에서 본 `DataAccessException` 계층이다.

<br/>

이게 왜 필요한지는 에러 코드를 보면 안다.

```java
중복 키 오류 - MySQL 은 1062, PostgreSQL 은 23505, H2 는 23505
```

<br/>

직접 처리하려면 DB마다 다른 숫자를 코드에 적어야 한다.

DB를 바꾸면 그 숫자를 전부 찾아 고쳐야 한다.

<br/>

스프링은 `sql-error-codes.xml` 이라는 파일에 DB별 매핑표를 들고 있다가,

우리에게는 항상 같은 예외로 바꿔서 던져준다.

```java
try {
    memberRepository.save(member);
} catch (DuplicateKeyException e) {          // DB 가 무엇이든 이 예외다
    throw new AlreadyJoinedException();
}
```

<br/>

## 단점은 SQL 이 자바 문자열이라는 것

원문의 단점에 해당하는 부분이다.

```java
String sql = "SELECT m.id, m.name, t.name AS team_name " +
             "FROM member m " +
             "LEFT JOIN team t ON m.team_id = t.id " +
             "WHERE m.deleted = false ";
```

<br/>

줄 끝에 공백을 빠뜨리면 `member mLEFT JOIN` 이 된다.

컴파일은 되고 실행할 때 문법 오류가 난다.

<br/>

`Java 15` 부터는 텍스트 블록으로 좀 나아졌다.

```java
String sql = """
        SELECT m.id, m.name, t.name AS team_name
        FROM member m
        LEFT JOIN team t ON m.team_id = t.id
        WHERE m.deleted = false
        """;
```

<br/>

그래도 컴파일러가 SQL 문법을 검사해주지는 않는다.

이 문제를 푸는 것이 다음 단계인 MyBatis와 QueryDSL이다.

```java
JdbcTemplate - SQL 이 자바 문자열. 오타를 실행해야 안다
MyBatis      - SQL 이 XML 로 분리된다. 동적 쿼리가 편해진다
QueryDSL     - SQL 을 자바 코드로 짠다. 컴파일러가 검사해준다
```
