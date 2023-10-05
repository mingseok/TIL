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

