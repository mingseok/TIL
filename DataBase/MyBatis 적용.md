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

