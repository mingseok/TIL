## JDBC 반복 문제 해결 (JdbcTemplate)

<br/>

## 지금까지 이런 코드를 계속 반복 작성 했다.

```java
@Slf4j
public class MemberRepositoryV4_2 implements MemberRepository {

		// .. 생략

    @Override
    public Member save(Member member) {
        String sql = "insert into member(member_id, money) values(?, ?)";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            throw exTranslator.translate("save", sql, e);
        } finally {
            close(con, pstmt, null);
        }
    }

    @Override
    public Member findById(String memberId) {
        String sql = "select * from member where member_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found memberId=" + memberId);
            }
        } catch (SQLException e) {
            throw exTranslator.translate("findById", sql, e);
        } finally {
            close(con, pstmt, rs);
        }
    }

    @Override
    public void update(String memberId, int money) {
        String sql = "update member set money=? where member_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw exTranslator.translate("update", sql, e);
        } finally {
            close(con, pstmt, null);
        }
    }

    @Override
    public void delete(String memberId) {
        String sql = "delete from member where member_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw exTranslator.translate("delete", sql, e);
        } finally {
            close(con, pstmt, null);
        }
    }

		// .. 생략
}
```

이렇게 작성하기 너무 힘들다..

<br/><br/>

## 이런 반복을 처리하는 방법 → 템플릿 콜백 패턴

스프링은 JDBC의 반복 문제를 해결하기 위해 JdbcTemplate 이라는 템플릿을 제공한다.

### 결론 부터 보자면, JdbcTemplate 사용함으로써 다 해결해준다.

```java
@Slf4j
public class MemberRepositoryV5 implements MemberRepository {
    private final JdbcTemplate template;

    public MemberRepositoryV5(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

	  // .. 생략
```

<br/><br/>

## 전체 코드

```java
@Slf4j
public class MemberRepositoryV5 implements MemberRepository {
    private final JdbcTemplate template;

    public MemberRepositoryV5(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into member(member_id, money) values(?, ?)";
        template.update(sql, member.getMemberId(), member.getMoney());
        // 첫번째 : sql을 넘긴다.
        // 두번째 : 파라미터 값을 넘긴다.
        // 세번째 : 파라미터 값을 넘긴다.
        return member;
    }

    @Override
    public Member findById(String memberId) {
        String sql = "select * from member where member_id = ?";
        return template.queryForObject(sql, memberRowMapper(), memberId);
        // queryForObject : 한개 출력하기
    }

    @Override
    public void update(String memberId, int money) {
        String sql = "update member set money=? where member_id=?";
        template.update(sql, money, memberId);
    }

    @Override
    public void delete(String memberId) {
        String sql = "delete from member where member_id=?";
        template.update(sql, memberId);
    }

    // 조회의 경우
    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setMemberId(rs.getString("member_id"));
            member.setMoney(rs.getInt("money"));
            return member;
        };
    }
}
```

JdbcTemplate 은 JDBC로 개발할 때 발생하는 반복을 대부분 해결해준다. 

그 뿐만 아니라 지금까지 학습했던, 트랜잭션을 위한 커넥션 동기화는 물론이고, 

예외 발생시 스프링 예외 변환기도 자동으로 실행해준다.

<br/><br/>

## 단점.

동적 SQL을 해결하기 어렵다.
<br/>

## 궁금증!

```java
JdbcTemplate 이 커넥션을 어디서 가져오길래 트랜잭션에 참여할 수 있을까?
```

`dataSource.getConnection()` 을 직접 부르지 않는다.

<br/>

직접 불렀다면 트랜잭션과 다른 커넥션을 받아서, 같은 트랜잭션에 못 묶였을 것이다.

```java
@Transactional
public void order() {
    jdbcTemplate.update("INSERT INTO orders ...");     // 커넥션 A 라면
    jdbcTemplate.update("UPDATE item SET ...");        // 커넥션 B 를 받으면 따로 논다
}
```

<br/>

그래서 앞의 커넥션 풀 글에서 본 `DataSourceUtils` 를 거친다.

```java
Connection connection = DataSourceUtils.getConnection(dataSource);
```

<br/>

```java
현재 스레드에 트랜잭션이 있나?
  있으면 -> 그 트랜잭션이 쓰던 커넥션을 준다
  없으면 -> dataSource.getConnection() 을 부른다
```

<br/>

반납할 때도 마찬가지다.

```java
DataSourceUtils.releaseConnection(connection, dataSource);
```

<br/>

```java
트랜잭션 커넥션이면 -> 닫지 않는다 (트랜잭션이 끝날 때 닫아야 한다)
아니면            -> 닫는다 (풀에 반납)
```

<br/>

이 두 줄 덕분에 `JdbcTemplate` 이 `@Transactional` 과 자연스럽게 맞물린다.

<br/>

## 그래서 세 가지가 같이 동작한다

앞의 스프링으로 트랜잭션 해결 글에서 본 그 세 가지가 여기서 만난다.

```java
@Transactional (AOP)     - 트랜잭션을 시작하고 커넥션을 ThreadLocal 에 넣는다
DataSourceUtils          - JdbcTemplate 이 그 커넥션을 꺼내 쓴다
JdbcTemplate             - 반복 코드를 없앤다
```

<br/>

세 개가 따로 만들어졌는데 `ThreadLocal 에 커넥션을 둔다` 는 약속 하나로 이어져 있다.

<br/>

## 그래서 JPA 와 섞어 쓸 때 문제가 생긴다

```java
@Transactional
public void order() {
    orderRepository.save(order);                      // JPA
    jdbcTemplate.update("UPDATE item SET ...");       // JdbcTemplate
}
```

<br/>

둘 다 같은 커넥션을 쓰기 때문에 트랜잭션 자체는 하나로 묶인다.

<br/>

문제는 JPA가 쓰기를 미룬다는 것이다.

앞의 DB 접근(JPA) 글에서 본 대로, `save()` 가 곧바로 `INSERT` 를 날리지 않는다.

```java
orderRepository.save(order);                  // 아직 DB 에 안 갔다
jdbcTemplate.update("SELECT ... FROM orders") // 방금 저장한 것이 안 보인다
```

<br/>

`JdbcTemplate` 은 JPA의 대기열을 모르기 때문에 옛 상태를 본다.

<br/>

섞어 써야 한다면 그 사이에 `flush()` 를 불러야 한다.

```java
orderRepository.save(order);
entityManager.flush();                        // 여기서 DB 로 보낸다
jdbcTemplate.update(...);
```

<br/>

## 결과를 객체로 바꾸는 부분

`RowMapper` 를 직접 만드는 대신 쓸 수 있는 것들이 있다.

```java
// 직접 만들기
private RowMapper<Item> itemRowMapper() {
    return (rs, rowNum) -> new Item(rs.getLong("id"), rs.getString("item_name"));
}

// 이름이 맞으면 자동 매핑
BeanPropertyRowMapper.newInstance(Item.class);
```

<br/>

`BeanPropertyRowMapper` 는 컬럼 이름과 세터 이름을 맞춰본다.

```java
item_name 컬럼 -> setItemName()      (스네이크를 카멜로 바꿔서 찾는다)
```

<br/>

편한데 앞의 form 태그 글에서 본 문제와 같은 함정이 있다.

이름이 안 맞으면 에러도 안 나고 그 필드만 `null` 로 남는다.

<br/>

컬럼 이름이 다르면 SQL에서 별칭을 붙여 맞춘다.

```sql
SELECT item_nm AS item_name FROM item
```

<br/>

## NamedParameterJdbcTemplate 이 더 안전하다

`?` 는 순서로 맞춰야 해서 실수하기 쉽다.

```java
jdbcTemplate.update("UPDATE item SET item_name=?, price=?, quantity=? WHERE id=?",
        itemName, price, quantity, id);
```

<br/>

컬럼 순서를 바꾸면 값도 같이 바꿔야 하는데, 타입이 같으면 컴파일도 통과하고 실행도 된다.

값만 엉뚱하게 들어간다.

<br/>

이름으로 넘기면 이 실수가 없어진다.

```java
template.update("UPDATE item SET item_name=:itemName, price=:price WHERE id=:id",
        new BeanPropertySqlParameterSource(updateParam));
```

<br/>

객체의 필드 이름과 `:이름` 을 맞춰서 넣어준다.

순서가 바뀌어도 상관없고, 이름이 안 맞으면 실행할 때 바로 에러가 난다.
