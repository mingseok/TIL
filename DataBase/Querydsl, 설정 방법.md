## Querydsl, 설정 방법

## DSL?

- 도메인 + 특화 + 언어

- 특정한 도메인에 초점을 맞춘 제한적인 표현력을 가진 컴퓨터 프로그래밍 언어
- 특징 : 단순, 간결, 유창

<br/><br/>

## Querydsl

- 쿼리 + 도메인 + 특화 + 언어

- 쿼리에 특화된 프로그래밍 언어
- 단순, 간결, 유창
- 다양한 저장소 쿼리 기능 통합

<br/><br/>

## 문제 : 사람을 찾아보기

```
- 20 ~ 40살
- 성 = 김씨
- 나이 많은 순서
- 3명을 출력하라.
```

<br/><br/>

### 회원 Table

```sql
create table Member (
	id bigint auto primary key,
  age integer not null,
  name varchar(255)
)
```

<br/>

### 회원 엔티티

```java
@Entity
public class Member {

    @Id @GeneratedValue
    private Long id;
    private String name;
    private int age;

    ...
}
```

<br/><br/>

## 위에 있는, 테이블과 회원 엔티티를 작성한 뒤 Querydsl을 사용하면?

이렇게 쿼리를 만들어 주는 것이다.

```sql
select id, age, name
from Member
where age between 20 and 40
and name like '김%'
order by age desc
limit 3
```

<br/><br/>

## 만들어지는 과정.

```
Querydsl → JPQL → SQL 
```

그리하여 `Querydsl`은 이렇게 정의 할 수 있다.

Querydsl을 가지고, `JPQL`을 만들어 주는 `‘빌더’` 이다. 라고 생각하면 되는 것이다.

<br/><br/>

## SpringDataJPA + Querydsl

- SpringData의 약점은 조회이다.

### 하지만 Querydsl로 복잡한 조회 기능을 보완 할 수 있다.

- 복잡한 쿼리

- 동적 쿼리

```
단순한 경우 : SpringDataJPA
복잡한 경우 : Querydsl 직접 사용
```

<br/><br/>

## Querydsl 장점

`Querydsl` 덕분에 동적 쿼리를 매우 깔끔하게 사용할 수 있다.

```java
List<Item> result = query
 .select(item)
 .from(item)
 .where(likeItemName(itemName), maxPrice(maxPrice))
 .fetch();
```

- 쿼리 문장에 오타가 있어도 컴파일 시점에 오류를 막을 수 있다.

- 메서드 추출을 통해서 코드를 재사용할 수 있다.
    - 예를 들어서 여기서 만든 likeItemName(itemName) ,maxPrice(maxPrice) 메서드를 다른 쿼리에서도 함께 사용할 수 있다
        

<br/><br/>

## 설정 방법

`build.gradle` 추가 해주기. 

```java
dependencies {
	// Querydsl 추가
	implementation 'com.querydsl:querydsl-jpa'
	annotationProcessor "com.querydsl:querydsl-apt:${dependencyManagement.importedProperties['querydsl.version']}:jpa"
	annotationProcessor "jakarta.annotation:jakarta.annotation-api"
	annotationProcessor "jakarta.persistence:jakarta.persistence-api"
}

// Querydsl 추가, 자동 생성된 Q클래스 gradle clean으로 제거
clean {
	delete file('src/main/generated')
}
```

<br/><br/>

## Querydsl 은 환경에 따라 동작이 달라진다

1. `Gradle` 을 사용 했을때와,

2. `Intellij IDEA` 를 사용 했을때의 경우이다.

![이미지](/programming/img/입문262.PNG)

<br/><br/>

## 첫번째. `Gradle` 로 했을 경우

`clean` 더블 클릭 해주기.

![이미지](/programming/img/입문263.PNG)

<br/><br/>

### compileJava 더블 클릭 해주기.

![이미지](/programming/img/입문264.PNG)

<br/><br/>

### 위 과정을 진행하게 된다면,

`build` 파일에 → `QItem.class`가 생성되게 된다.

![이미지](/programming/img/입문265.PNG)

<br/><br/>

## 두번째. `Intellij IDEA` 사용 방법

시작전에, 클리어 한번 해주기.

![이미지](/programming/img/입문266.PNG)

<br/><br/>

### `Intellij IDEA`로 설정한다.

![이미지](/programming/img/입문267.PNG)

<br/><br/>

그리고 메인 메서드를 한번 실행 시키면, 이렇게 `generated`파일이 생성되고, 

그 안에 `QItem.class` 가 생성 되는 것을 알 수 있다.

![이미지](/programming/img/입문268.PNG)

<br/><br/>

### 삭제 방법은?

build.gradle 에서 추가 하였기 때문에, clean 을 더블 클릭 하면 되는 것이다.

```java
//Querydsl 추가, 자동 생성된 Q클래스 gradle clean으로 제거
clean {
	delete file('src/main/generated')
}
```

<br/>

![이미지](/programming/img/입문269.PNG)


<br/><br/>

>**Reference** <br/>[스프링 DB 2편 - 데이터 접근 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-db-2/dashboard)


<br/>

## 궁금증!

```java
Q 클래스는 언제 어떻게 만들어지는 걸까?
```

앞의 롬복 글에서 본 어노테이션 프로세싱으로 만들어진다.

다만 롬복과는 방식이 다르다.

```java
롬복      - 기존 클래스에 메서드를 끼워 넣는다 (규격 외 방식)
QueryDSL - 새 클래스 파일을 만들어낸다 (정석적인 방식)
```

<br/>

`@Entity` 가 붙은 클래스를 찾아서, 그 필드 정보를 담은 `Q` 클래스를 생성한다.

```java
@Entity
public class Member {
    @Id private Long id;
    private String name;
    private int age;
}
```

<br/>

컴파일하면 `build/generated/` 아래에 이런 파일이 생긴다.

```java
public class QMember extends EntityPathBase<Member> {
    public static final QMember member = new QMember("member");

    public final NumberPath<Long> id = createNumber("id", Long.class);
    public final StringPath name = createString("name");
    public final NumberPath<Integer> age = createNumber("age", int.class);
}
```

<br/>

필드마다 타입에 맞는 객체가 만들어진다.

`name` 은 `StringPath`, `age` 는 `NumberPath` 다.

<br/>

## 이 타입 구분이 핵심이다

```java
member.name.like("%민석%")        // StringPath 에는 like 가 있다
member.age.like("%30%")           // 컴파일 에러. NumberPath 에는 like 가 없다

member.age.goe(20)                // 숫자에는 goe (>=) 가 있다
member.name.goe(20)               // 컴파일 에러
```

<br/>

문자열에만 되는 연산과 숫자에만 되는 연산이 타입으로 갈린다.

<br/>

앞의 MyBatis 글에서 본 XML이었다면 이런 실수를 실행할 때까지 몰랐을 것이다.

```xml
<if test="age != null">
    AND age LIKE CONCAT('%', #{age}, '%')     <!-- 실행해봐야 안다 -->
</if>
```

<br/>

## 동적 쿼리가 자바 코드로 표현된다

```java
public List<Item> search(String name, Integer maxPrice) {
    return queryFactory
            .selectFrom(item)
            .where(
                    nameLike(name),          // null 이면 조건에서 빠진다
                    priceLoe(maxPrice)
            )
            .fetch();
}

private BooleanExpression nameLike(String name) {
    return StringUtils.hasText(name) ? item.itemName.like("%" + name + "%") : null;
}

private BooleanExpression priceLoe(Integer maxPrice) {
    return maxPrice != null ? item.price.loe(maxPrice) : null;
}
```

<br/>

`where()` 에 `null` 을 넘기면 그 조건은 무시된다.

앞의 MyBatis 글에서 본 `<where>` 태그와 같은 일을 자바 문법으로 하는 것이다.

<br/>

그리고 조건을 메서드로 뽑아뒀으니 다른 쿼리에서 재사용할 수 있다.

XML 조각은 재사용하려면 `<sql>` 과 `<include>` 를 써야 하는데 자바 메서드가 훨씬 자연스럽다.

<br/>

## 빌드 설정이 까다로운 이유

`Q` 클래스는 소스가 아니라 빌드 산출물이다.

```java
build/generated/sources/annotationProcessor/java/main/
```

<br/>

그래서 몇 가지 문제가 생긴다.

```java
clean 하면 사라진다        -> 컴파일을 다시 해야 IDE 가 인식한다
git 에 넣으면 안 된다      -> .gitignore 에 build/ 를 넣어둔다
엔티티를 고치면 다시 생성해야 한다 -> 안 하면 옛 Q 클래스로 컴파일된다
```

<br/>

`엔티티 필드를 지웠는데 컴파일이 통과하는` 경우가 그래서 생긴다.

옛날 `Q` 클래스가 남아 있어서다. `clean build` 를 하면 드러난다.

<br/>

## JPAQueryFactory 를 빈으로 등록해야 한다

```java
@Configuration
public class QuerydslConfig {

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }
}
```

<br/>

`EntityManager` 를 넘겨야 하는 이유가 있다.

QueryDSL은 SQL을 직접 만들지 않고 JPQL을 만들어서 JPA에게 넘기기 때문이다.

```java
QueryDSL 코드 -> JPQL -> (하이버네이트가) SQL
```

<br/>

그래서 앞의 DB 접근(JPA) 글에서 본 것들이 그대로 적용된다.

`N+1` 도 똑같이 생기고, 해결도 `fetchJoin()` 으로 한다.

```java
queryFactory
        .selectFrom(order)
        .join(order.member, member).fetchJoin()      // JOIN FETCH 와 같다
        .fetch();
```

<br/>

`EntityManager` 는 스레드마다 다른 것이 주입되어야 하는데,

스프링이 프록시를 넣어주기 때문에 위처럼 필드로 들고 있어도 안전하다.

앞의 커넥션 풀 글에서 본 `ThreadLocal` 방식과 같은 원리다.
