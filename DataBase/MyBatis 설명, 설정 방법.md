## MyBatis 설명, 설정 방법

`MyBatis`는 `JdbcTemplate`보다 더 많은 기능을 제공하는 `SQL Mapper` 이다.

기본적으로 `JdbcTemplate`이 제공하는 대부분의 기능을 제공한다.

<br/>

`MyBatis` 의 가장 매력적인 점은 `SQL`을 `XML`에 편리하게 작성할 수 있고,

동적 쿼리를 매우 편리하게 작성할 수 있다는 점이다.

```
MyBatis가 제공하는 최고의 기능이자 마이바티스를 사용하는 이유는 바로 동적 SQL 기능 때문이다.
```

<br/><br/>

## JdbcTemplate - SQL 여러줄

```sql
String sql = "update item " +
 "set item_name=:itemName, price=:price, quantity=:quantity " +
 "where id=:id";
```

<br/><br/>

## MyBatis - SQL 여러줄

```sql
<update id="update">
	 update item
	 set item_name=#{itemName},
			 price=#{price},
			 quantity=#{quantity}
	 where id = #{id}
</update>
```

`MyBatis`는 `XML`에 작성하기 때문에 라인이 길어져도 문자 더하기에 대한 불편함이 없다.

<br/><br/>

## JdbcTemplate - 동적 쿼리

```sql
String sql = "select id, item_name, price, quantity from item";

//동적 쿼리
if (StringUtils.hasText(itemName) || maxPrice != null) {
        sql += " where";
    }
    boolean andFlag = false;
if (StringUtils.hasText(itemName)) {
        sql += " item_name like concat('%',:itemName,'%')";
        andFlag = true;
    }
if (maxPrice != null) {
        if (andFlag) {
            sql += " and";
        }
        sql += " price <= :maxPrice";
    }
log.info("sql={}", sql);
return template.query(sql, param, itemRowMapper());
```

<br/><br/>

## MyBatis - 동적 쿼리

```sql
<select id="findAll" resultType="Item">
     select id, item_name, price, quantity
     from item
     <where>
         <if test="itemName != null and itemName != ''">
             and item_name like concat('%',#{itemName},'%')
         </if>
         <if test="maxPrice != null">
             and price &lt;= #{maxPrice}
         </if>
     </where>
</select>
```

`JdbcTemplate`은 자바 코드로 직접 동적 쿼리를 작성해야 한다. 

반면에 `MyBatis`는 동적 쿼리를 매우 편리하게 작성할 수 있는 다양한 기능들을 제공해준다.

<br/><br/>

## 설정의 장단점

`JdbcTemplate`은 스프링에 내장된 기능이고, 

별도의 설정 없이 사용할 수 있다는 장점이 있다. 

반면에 `MyBatis`는 약간의 설정이 필요하다.

<br/><br/>

## 정리

프로젝트에서 동적 쿼리와 복잡한 쿼리가 많다면 `MyBatis`를 사용하고, 

단순한 쿼리들이 많으면 `JdbcTemplate`을 선택해서 사용하면 된다. 

<br/>

물론 둘을 함께 사용해도 된다. 

```
하지만 MyBatis만으로도 충분할 것이다.
```


<br/><br/>


## 설정 방법

### `build.gradle` 에 다음 의존 관계를 추가한다.

```
//MyBatis 추가
implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:2.2.0'
```

참고 - 스프링 부트 3.0 이상일 경우는 2.2.0 대신에 3.0.1을 사용해야 한다.

<br/>

### main → application.properties

```java
#MyBatis
mybatis.type-aliases-package=hello.itemservice.domain
mybatis.configuration.map-underscore-to-camel-case=true
logging.level.hello.itemservice.repository.mybatis=trace
```

<br/>

### test → application.properties

```java
#MyBatis
mybatis.type-aliases-package=hello.itemservice.domain
mybatis.configuration.map-underscore-to-camel-case=true
logging.level.hello.itemservice.repository.mybatis=trace
```

main 이랑 test 파일 둘 다 적용 해줘야 한다.

<br/><br/>

>**Reference** <br/>[스프링 DB 2편 - 데이터 접근 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-db-2/dashboard)


<br/>

## 궁금증!

```java
JdbcTemplate 도 SQL 을 쓰는데, MyBatis 는 무엇이 더 나은 걸까?
```

`동적 쿼리` 하나 때문이라고 봐도 될 정도다.

<br/>

검색 조건이 상황마다 달라지는 경우를 `JdbcTemplate` 으로 짜면 이렇게 된다.

```java
String sql = "SELECT * FROM item WHERE 1=1 ";
List<Object> params = new ArrayList<>();

if (StringUtils.hasText(name)) {
    sql += " AND name LIKE ? ";
    params.add("%" + name + "%");
}
if (maxPrice != null) {
    sql += " AND price <= ? ";
    params.add(maxPrice);
}
```

<br/>

`WHERE 1=1` 이라는 이상한 조건이 붙는다.

첫 조건이 무엇이 될지 모르니 `AND` 를 붙이기 위해 넣는 꼼수다.

<br/>

그리고 문자열을 이어 붙이니 앞의 JdbcTemplate 글에서 본 공백 문제가 그대로 생긴다.

조건이 다섯 개만 넘어가도 읽기가 어려워진다.

<br/>

## MyBatis 로 쓰면

```xml
<select id="findAll" resultType="Item">
    SELECT id, item_name, price, quantity
    FROM item
    <where>
        <if test="itemName != null and itemName != ''">
            AND item_name LIKE CONCAT('%', #{itemName}, '%')
        </if>
        <if test="maxPrice != null">
            AND price &lt;= #{maxPrice}
        </if>
    </where>
</select>
```

<br/>

`<where>` 태그가 똑똑하다.

- 안쪽 조건이 하나도 없으면 `WHERE` 자체를 안 붙인다

- 첫 조건 앞의 `AND` 를 알아서 떼어준다

`WHERE 1=1` 같은 꼼수가 필요 없어진 것이다.

<br/>

## 그런데 XML 이라 불편한 점이 있다

```xml
AND price &lt;= #{maxPrice}
```

<br/>

`<` 를 그냥 못 쓴다. XML 태그 시작으로 해석되기 때문이다.

`&lt;` 로 쓰거나 `<![CDATA[ ... ]]>` 로 감싸야 한다.

<br/>

그리고 자바 코드와 XML이 이름으로만 연결되어 있다.

```java
public interface ItemMapper {
    List<Item> findAll(ItemSearchCond cond);     // 여기의 findAll 과
}
```

```xml
<select id="findAll" ...>                        <!-- 여기의 findAll 이 맞아야 한다 -->
```

<br/>

이름을 잘못 쓰면 애플리케이션이 뜰 때 에러가 나기는 한다.

다만 IDE에서 자동 완성이나 이름 변경이 잘 안 먹는다.

<br/>

## #{} 와 ${} 를 구분해야 한다

이게 실무에서 제일 중요하다.

```xml
#{itemName}     <!-- PreparedStatement 의 ? 로 바뀐다 -->
${itemName}     <!-- 문자열이 그대로 들어간다 -->
```

<br/>

앞의 JDBC 글에서 본 SQL 인젝션 문제가 `${}` 에서 그대로 생긴다.

```xml
WHERE name = '${name}'
```

<br/>

`name` 에 `1' OR '1'='1` 을 넣으면 전체가 조회된다.

<br/>

`${}` 는 값이 아니라 `SQL 구조` 를 동적으로 만들어야 할 때만 쓴다.

```xml
ORDER BY ${sortColumn}      <!-- 컬럼 이름은 ? 로 넘길 수 없다 -->
```

<br/>

이때도 허용된 값 목록과 비교해서 걸러야 한다.

```java
if (!List.of("name", "price", "created_at").contains(sortColumn)) {
    throw new IllegalArgumentException();
}
```

<br/>

## MyBatis 를 쓰는 자리

앞의 JdbcTemplate 글에서 정리한 흐름에 놓으면 이렇다.

```java
JdbcTemplate - SQL 이 자바 문자열. 동적 쿼리가 지저분하다
MyBatis      - SQL 이 XML. 동적 쿼리가 깔끔하다. 대신 컴파일 검사가 없다
QueryDSL     - SQL 이 자바 코드. 컴파일러가 검사한다
```

<br/>

지금 새로 시작하는 프로젝트에서는 JPA와 QueryDSL을 쓰는 경우가 많다.

<br/>

그래도 MyBatis가 나은 경우가 있다.

```java
통계나 리포트처럼 복잡한 SQL 을 손으로 짜야 할 때
레거시 DB 라 테이블이 객체로 매핑하기 어려울 때
DBA 가 SQL 을 직접 관리해야 할 때
```

<br/>

SQL이 XML에 따로 있으니 DBA가 자바를 몰라도 튜닝할 수 있다는 것이 장점이 되기도 한다.
