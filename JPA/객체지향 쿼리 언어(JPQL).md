## JPQL(Java Persistence Query Language)

- JPQL은 객체지향 쿼리 언어다.

    - 따라서 테이블을 대상으로 쿼리하는 것이 아니라, 
    
        엔티티 객체를 대상으로 쿼리한다.

- JPQL은 SQL을 추상화해서 특정 데이터베이스 SQL에 의존하지 않는다.
- JPQL은 결국 SQL로 변환된다. (중요)

<br/><br/>

## JPQL

- `JPA`를 사용하면 엔티티 객체를 중심으로 개발

- 문제는 `검색 쿼리`
- 검색을 할 때도 `테이블이 아닌 엔티티 객체를 대상으로 검색`
- 모든 `DB` 데이터를 객체로 변환해서 검색하는 것은 불가능
- 애플리케이션이 필요한 데이터만 `DB`에서 불러오려면 결국 `검색 조건`이 포함된 `SQL`이 필요
- `JPA`는 `SQL`을 추상화한 `JPQL`이라는 객체 지향 쿼리 언어 제공
- `SQL`과 문법 유사, `SELECT`, `FROM`, `WHERE`, `GROUP BY`, `HAVING`, `JOIN` 지원
- `JPQL`은 엔티티 객체를 대상으로 쿼리
    - `SQL`은 데이터베이스 테이블을 대상으로 쿼리





<br/><br/>

## JPQL 문법

```
select_문 :: =
   select_절
   from_절
   [where_절]
   [groupby_절]
   [having_절]
   [orderby_절]
```


<br/>

### 예시
```
select m from `Member` as m where m.`age` > 18
```

- 위의 `Member`가 엔티티 라는 것이다.

- 엔티티는 대소문자 구분한다.

- JPQL 키워드는 대소문자 구분하지 않는다. `(=SELECT, FROM, where)`

- 엔티티 이름 사용, 테이블 이름이 아니다. (@Entity의 이름을 말함)

- 별칭은 필수`(=m)` , `(=as는 생략가능하다)`

<br/><br/>

## 집합과 정렬

```sql
select
    COUNT(m),   // 회원수
    SUM(m.age), // 나이 합
    AVG(m.age), // 평균 나이
    MAX(m.age), // 최대 나이
    MIN(m.age)  // 최소 나이
from Member m
```

- `GROUP BY`, `HAVING` : 똑같이 사용가능하다.

- `ORDER BY` : 똑같이 사용가능하다.



<br/><br/>

## 코드 확인

```java
//검색
String jpql = "select m From Member m where m.name like ‘%hello%'";

List<Member> result = em.createQuery(jpql, Member.class).getResultList();
```


- 테이블이 아닌 객체를 대상으로 검색하는 객체 지향 쿼리

- SQL을 추상화해서 특정 데이터베이스 SQL에 의존X
- JPQL을 한마디로 정의하면 객체 지향 SQL이다.

<br/><br/>

## JPQL과 실행된 SQL 확인

```
나이가 18살 이상인 회원을 모두 검색하고 싶다면?
```

![이미지](/programming/img/입문379.PNG)




<br/><br/>

>**Reference** <br/>[자바 ORM 표준 JPA 프로그래밍 - 기본편](https://www.inflearn.com/course/ORM-JPA-Basic)

