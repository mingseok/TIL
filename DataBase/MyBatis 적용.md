## MyBatis 적용

### main → repository → mybatis → ItemMapper 인터페이스 생성

```java
@Mapper
public interface ItemMapper {
    void save(Item item);

    void update(@Param("id") Long id, @Param("updateParam") ItemUpdateDto updateParam);

    Optional<Item> findById(Long id);

    List<Item> findAll(ItemSearchCond itemSearch);
}
```

- 이 인터페이스에는 `@Mapper` 애노테이션을 붙여주어야 한다.

    - 그래야 `MyBatis`에서 인식할 수 있다.

- 파라미터가 두개일 경우 `@Param()` 작성 해주기.

<br/><br/>

## 파일 생성 위치

resources → 위 ItemMapper 인터페이스랑 위치 똑같이 하기.

![이미지](/programming/img/입문259.PNG)

<br/>

### ItemMapper.xml 파일

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="hello.itemservice.repository.mybatis.ItemMapper">
    
    <insert id="save" useGeneratedKeys="true" keyProperty="id">
        insert into item (item_name, price, quantity)
        values (#{itemName}, #{price}, #{quantity})
    </insert>
    
    <update id="update">
        update item
        set item_name=#{updateParam.itemName},
            price=#{updateParam.price},
            quantity=#{updateParam.quantity}
        where id = #{id}
    </update>
    
    <select id="findById" resultType="Item">
        select id, item_name, price, quantity
        from item
        where id = #{id}
    </select>
    
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
    
</mapper>
```

<br/><br/>

## insert - save 설명

```xml
void save(Item item); -> 해당 인터페이스

<insert id="save" useGeneratedKeys="true" keyProperty="id">
    insert into item (item_name, price, quantity)
    values (#{itemName}, #{price}, #{quantity})
</insert>
```

- `id` 에는 매퍼 인터페이스에 설정한 메서드 이름을 지정하면 된다.

    - 여기서는 메서드 이름이 `save()` 이므로 `save` 로 지정하면 된다

- 파라미터는 `#{}` 문법을 사용하면 된다.

    - 그리고 매퍼에서 넘긴 객체의 프로퍼티 이름을 적어주면 된다.

- `#{}` 문법을 사용하면 `PreparedStatement` 를 사용한다.
    - `JDBC`의 `?` 를 치환한다 생각하면 된다.
- `useGeneratedKeys`는 데이터베이스가 키를 생성해 주는 `IDENTITY`전략일 때 사용한다.

    - `IDENTITY` 전략이란? -> DB에서 값을 넣어주는 전략이라고 생각하기.
        - 즉 `Auto increment`라고 생각하면 된다.
    - `keyProperty`는 생성되는 키의 속성 이름을 지정한다.

    - `Insert`가 끝나면 `item`객체의 `id`속성에 생성된 값이 입력된다.

<br/><br/>

## select - findById

```xml
Optional<Item> findById(Long id); -> 해당 인터페이스

<select id="findById" resultType="Item">
    select id, item_name, price, quantity
    from item
    where id = #{id}
</select>
```

- `resultType`은 반환 타입을 명시하면 된다. 여기서는 결과를 `Item` 객체에 매핑한다

    - 앞서 `application.properties` 에 `mybatis.type-aliasespackage=hello.itemservice.domain`
        
        속성을 지정한 덕분에 모든 패키지 명을 다 적지는 않아도 된다. 
        
        그렇지 않으면 모든 패키지 명을 다 적어야 한다.
        
<br/><br/>

## select - findAll

```xml
List<Item> findAll(ItemSearchCond itemSearch); -> 해당 인터페이스

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

- `Mybatis`는 `<where>` , `<if>` 같은 동적 쿼리 문법을 통해 편리한 동적 쿼리를 지원한다.

- `<if>` 는 해당 조건이 만족하면 구문을 추가한다.

- `<where>` 은 적절하게 `where`문장을 만들어준다.
    - 예제에서 `<if>`가 모두 실패하게 되면 `SQL where` 를 만들지 않는다.

    - 예제에서 `<if>`가 하나라도 성공하면 처음 나타나는 `and` 를 `where`로 변환해준다.

<br/><br/>

## findAll() 메서드에서 and 를 where 로 변환 해준다?

만약, 하나라도 성공한다면? 밑에 where 문이 없어지고 결론적으론 이렇게 치환 된다는 것이다.

```xml
<select id="findAll" resultType="Item">
        select id, item_name, price, quantity
        from item where item_name like concat('%', #{itemName}, '%')
</select>
```

만약, 모두 실패하면 where를 만들지 않는 것이다.

<br/><br/>

## XML 특수문자

```xml
< : &lt;
> : &gt;
& : &amp;
```


<br/><br/>

>**Reference** <br/>[스프링 DB 2편 - 데이터 접근 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-db-2/dashboard)


<br/>

## 궁금증!

```java
Mapper 인터페이스만 만들었는데 구현체는 누가 만드는가?
```

앞의 스프링 데이터 JPA 글에서 본 것과 같은 방식이다. 실행 중에 프록시를 만든다.

```java
@Mapper
public interface ItemMapper {
    void save(Item item);
    List<Item> findAll(ItemSearchCond cond);
}
```

<br/>

주입받아서 클래스 이름을 찍어보면 프록시다.

```java
class com.sun.proxy.$Proxy68
```

<br/>

## 프록시가 하는 일

메서드 호출을 가로채서 XML을 찾아 실행한다.

```java
itemMapper.findAll(cond)
  -> 프록시가 가로챈다
  -> "ItemMapper 인터페이스의 findAll 이구나"
  -> ItemMapper.xml 의 namespace 와 id 로 SQL 을 찾는다
  -> 파라미터를 바인딩해서 실행한다
  -> 결과를 resultType 에 맞춰 객체로 만든다
  -> 돌려준다
```

<br/>

연결 고리는 이름 두 개다.

```xml
<mapper namespace="hello.itemservice.repository.mybatis.ItemMapper">
    <select id="findAll" resultType="Item">
```

<br/>

`namespace` 가 인터페이스의 전체 이름과 같아야 하고,

`id` 가 메서드 이름과 같아야 한다.

<br/>

## 이름으로 연결되어서 생기는 문제

앞의 Querydsl 글과 비교하면 차이가 확실하다.

```java
MyBatis  - 이름 문자열로 연결된다. IDE 가 잘 못 따라간다
QueryDSL - 자바 코드라 컴파일러가 검사한다
```

<br/>

메서드 이름을 바꾸면 XML도 같이 고쳐야 하는데, IDE의 이름 변경 기능이 XML까지 못 간다.

<br/>

애플리케이션이 뜰 때 검사해주기는 한다.

```java
Invalid bound statement (not found): ItemMapper.findAll
```

<br/>

이 에러가 나면 셋 중 하나다.

```java
namespace 가 인터페이스 이름과 다르다
id 가 메서드 이름과 다르다
XML 파일 위치를 못 찾고 있다
```

<br/>

## XML 위치 규칙

기본은 `자바 인터페이스와 같은 패키지 경로` 다.

```java
src/main/java/hello/itemservice/repository/mybatis/ItemMapper.java
src/main/resources/hello/itemservice/repository/mybatis/ItemMapper.xml
```

<br/>

`resources` 아래에 패키지 경로를 그대로 만들어야 한다.

폴더 하나만 틀려도 못 찾는다.

<br/>

한군데 모아두고 싶으면 설정으로 바꿀 수 있다.

```java
mybatis.mapper-locations=classpath:mapper/**/*.xml
```

<br/>

## resultType 에 별칭을 주는 이유

```xml
<select id="findAll" resultType="Item">
```

<br/>

원래는 전체 이름을 적어야 한다.

```xml
resultType="hello.itemservice.domain.Item"
```

<br/>

매번 적기 번거로우니 별칭을 등록해둔다.

```java
mybatis.type-aliases-package=hello.itemservice.domain
```

<br/>

이러면 그 패키지 안의 클래스는 짧은 이름으로 쓸 수 있다.

<br/>

## 카멜 케이스 변환

```java
mybatis.configuration.map-underscore-to-camel-case=true
```

<br/>

앞의 JdbcTemplate 글에서 본 `BeanPropertyRowMapper` 와 같은 일을 한다.

```java
item_name 컬럼 -> setItemName()
```

<br/>

이 설정을 안 켜면 `item_name` 을 못 찾아서 그 필드만 `null` 이 된다.

에러가 안 나고 값만 비는 형태라 알아채기 어렵다.

<br/>

규칙에 안 맞는 이름은 SQL에서 별칭으로 맞춰야 한다.

```sql
SELECT item_nm AS item_name FROM item
```

<br/>

## 로그를 켜두면 훨씬 편하다

```java
logging.level.hello.itemservice.repository.mybatis=trace
```

<br/>

실행된 SQL과 넘어간 파라미터가 그대로 찍힌다.

```java
==>  Preparing: SELECT id, item_name, price FROM item WHERE price <= ?
==> Parameters: 10000(Integer)
<==      Total: 3
```

<br/>

앞의 MyBatis 설명 글에서 본 `<if>` 조건이 실제로 붙었는지 안 붙었는지가 여기서 보인다.

동적 쿼리를 쓸 때는 이 로그가 사실상 필수다.
