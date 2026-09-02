## DB 스키마 자동 생성

- 애플리케이션 로딩 시점에 `create`문으로 `DB`를 생성을 하고 시작하게 할 수 있다.

- `DB`방언을 활용해서 `DB`에 맞는 적절한 `DDL`생성을 해준다.
    - 예를 들어, 문자 같은 경우 `‘오라클’`은 → `varchar2` `‘MYSQL’`은 `varchar`를 사용하는데,
        
        현재 내가 뭘로 설정했냐에 따라, 적절하게 생성해준다.
        
    - 또한, 밑에 코드 처럼 `id`, `name` 두개만 있었다가, 갑자기 3개가 되었다고 하더라도
        
        `private int age;`까지 추가된 쿼리를 만들어 주는 것이다. → 즉 새로 테이블을 만든다.
        
    
    ```java
    private Long id;
    private String name;
    ```


<br/><br/>

## 주의 사항

운영 장비에는 절대 `create`, `create-drop`, `update` 사용하면 안된다.

<br/><br/>

## DDL 생성 기능

제약조건을 추가 할 수 있다.

`회원 이름은 필수`, `10자 초과 X` → 이런식으로 엔티티에서도 가능하다.

![이미지](/programming/img/입문302.PNG)

`DDL`생성 기능은 `DDL`을 자동 생성할 때만 사용되고 `JPA`의 실행 로직에는 영향을 주지 않는다.



<br/><br/>

>**Reference** <br/>[자바 ORM 표준 JPA 프로그래밍 - 기본편](https://www.inflearn.com/course/ORM-JPA-Basic)



<br/>

## 궁금증!

```java
"운영에 update 를 쓰면 안 된다" 는 게 무엇 때문일까?
```

`update` 는 추가만 하고 삭제를 안 한다. 그래서 스키마가 조금씩 어긋난다.

```java
create      - 뜰 때마다 테이블을 다 지우고 새로 만든다
create-drop - 위와 같고, 내려갈 때도 지운다
update      - 바뀐 것을 반영한다. 다만 추가만 하고 삭제는 안 한다
validate    - 엔티티와 테이블이 맞는지 검사만 한다
none        - 아무것도 안 한다
```

<br/>

`create` 는 데이터가 통째로 날아가니 운영에서 못 쓴다는 것이 명백하다.

문제는 `update` 다. 안전해 보이는데 그렇지 않다.

<br/>

## update 가 안 하는 일들

```java
컬럼 삭제        - 필드를 지워도 컬럼은 그대로 남는다
컬럼 타입 변경    - VARCHAR(10) 을 VARCHAR(20) 으로 바꿔도 반영 안 되는 경우가 있다
컬럼 이름 변경    - 이름을 바꾸면 새 컬럼이 추가되고 옛 컬럼이 남는다
제약 조건 변경    - NOT NULL 을 붙여도 기존 데이터 때문에 실패할 수 있다
인덱스 삭제      - 안 한다
```

<br/>

특히 `컬럼 이름 변경` 이 위험하다.

```java
private String name;   ->   private String memberName;
```

<br/>

`name` 컬럼이 남은 채로 `member_name` 이 추가된다.

옛 데이터는 `name` 에 있고 새 데이터는 `member_name` 에 들어간다.

<br/>

그리고 `name` 에 `NOT NULL` 이 걸려 있으면 새 데이터 삽입이 전부 실패한다.

<br/>

## 그래서 운영에는 validate 를 쓴다

```java
spring.jpa.hibernate.ddl-auto=validate
```

<br/>

엔티티와 테이블이 다르면 애플리케이션이 아예 안 뜬다.

```java
Schema-validation: missing column [member_name] in table [member]
```

<br/>

앞의 스프링 컨테이너 내부 동작 글에서 본 `문제가 있으면 뜨지도 않는다` 가 여기서도 이득이 된다.

<br/>

배포하자마자 알 수 있는 편이, 그 컬럼을 처음 쓰는 사용자가 에러를 만나는 것보다 낫다.

<br/>

## 그럼 스키마는 누가 바꾸는가

마이그레이션 도구를 쓴다.

```java
db/migration/V1__create_member.sql
db/migration/V2__add_member_phone.sql
db/migration/V3__rename_name_to_member_name.sql
```

<br/>

SQL 파일을 순서대로 적어두면 도구가 알아서 적용한다.

`Flyway` 나 `Liquibase` 가 그것이다.

<br/>

이 방식의 이득이 몇 가지 있다.

```java
git 에 남는다        - 누가 언제 무엇을 바꿨는지 보인다
순서가 보장된다      - 어느 서버든 같은 순서로 적용된다
되돌릴 수 있다       - 어떤 상태였는지 알 수 있다
운영과 로컬이 같아진다 - 같은 파일로 만든다
```

<br/>

앞의 프로시저(procedure)란 글에서 본 `버전 관리가 안 된다` 는 문제를 이렇게 푸는 것이다.

<br/>

## 로컬에서도 create 를 조심해야 한다

편해서 쓰는데 함정이 있다.

<br/>

`create` 로 개발하면 `엔티티만 고치면 테이블이 알아서 바뀐다` 는 감각이 생긴다.

그러다 운영에 배포할 때 마이그레이션을 빠뜨린다.

<br/>

앞의 BE Flyway 마이그레이션 얘기처럼, 엔티티를 고쳤으면 마이그레이션도 같이 써야 한다.

<br/>

`validate` 로 로컬을 맞춰두면 이 실수가 사라진다.

마이그레이션을 안 쓰면 로컬에서도 안 뜨기 때문이다.

```java
로컬을 create 로 두면 - 편하다. 대신 마이그레이션을 잊는다
로컬을 validate 로 두면 - 번거롭다. 대신 운영과 같은 방식으로 개발한다
```

<br/>

## DDL 생성 기능이 실행에 영향을 안 준다는 것

원문 마지막의 그 문장이 중요한 함정을 가리킨다.

```java
@Column(nullable = false, length = 10)
private String name;
```

<br/>

`ddl-auto` 가 `none` 이면 이 설정은 아무 일도 안 한다.

`10` 자를 넘겨도 JPA가 안 막고, DB 컬럼이 `VARCHAR(255)` 면 그대로 들어간다.

<br/>

그래서 앞의 Bean Validation 글에서 본 검증을 따로 걸어야 한다.

```java
@NotBlank
@Size(max = 10)
private String name;
```

<br/>

`@Column` 은 테이블을 만들 때 쓰는 정보이고, `@Size` 는 값을 검사하는 정보다.

둘은 이름만 비슷하고 하는 일이 다르다.
