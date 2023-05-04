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

