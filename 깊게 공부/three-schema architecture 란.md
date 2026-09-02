## three-schema architecture 란?

<br/>

## `three-schema architecture` 란?

- `database system`을 구축하는 아키텍처 중의 하나

- `user application`으로 부터 물리적인 `database`를 분리시키는 목적

안정적으로 데이터베이스 시스템을 운영하기 위해서 사용되는 아키텍처이다.



- 각 레벨을 독립시켜서 어느 레벨에서의 변화가 상위 레벨에 영향을 주지 않기 위함이다.

    - 즉, 매핑만 바꿔주면 되기에, 안정적으로 운영할 수 있다.

```java
하지만, 요즘은 대부분의 "DBMS"가 "three level"을 완벽하게 혹은 명시적으로 나누지는 않는다.
```

<br/>

각각의 스키마들은 데이터 베이스 구조를 표현하는 것이지, 

실제로 이 데이터가 존재하는 곳은 `internal level`에만 있다는 것이다.

<br/><br/>

## `three-schema architecture` 구조는?

- 세 가지 `level`이 존재하며, 각각의 `level`마다 `schema`가 정의되어 있다.

![이미지](/programming/img/입문420.PNG)

### `internal schemas` (internal level)

가장 데이터 베이스랑 가까운 곳이다

- 물리적으로 데이터가 어떻게 저장되는지 표현 하는데,
    
    `physical data model`을 통해 표현한다.
    
    - ex) `data storage`, `data structure`, index로 대표되는 `access path` 등등
        
        물리적으로 실체가 있는 내용을 기술하는 스키마가 → `internal schemas`이다
        

<br/>

### `external schemas` (external level)

실제 사용자가 바라보는 스키마라고 이해하기.

- `external views`, `user views` 라고도 불린다.

- 특정 유저들이 필요로 하는 데이터만 표현한다.

    - 기 외 알려줄 필요가 없는 데이터는 숨긴다.
- `external schemas`는 `logical data model`을 통해 표현이 된다.

<br/>

### `conceptual schemas` (conceptual level)

`internal schemas` 와 `external schemas`만으로 잘 사용하고 있었는데,
계속해서 

사용자에게 조건을 맞추다 보니, 
`중복된 데이터`, `데이터 불일치 발생`,전체적으로 관리가 힘들어지게 된다.

```java
"이, 문제를 해결하기 위해 등장한 것이 conceptual schemas 이다."
```

- `conceptual schemas`는 `internal schemas`를 추상화하여 표현한 스키마이다.

- 전체 `database`에 대한 구조를 기술한다.
- `물리적`으로 저장 구조에 관한 내용은 숨긴다.
- `entities`, `data types`, `relationships`, `user operations`, `constraints`에 집중할 수 있다.
- `logical data model`을 통해 기술된다.
<br/>

## 궁금증!

```java
세 단계로 나누면 실제로 무엇이 편해질까?
```

`한쪽이 바뀌어도 다른 쪽이 안 바뀐다` 는 것이다. 이것을 `데이터 독립성` 이라고 한다.

```java
외부 스키마 (external)   - 사용자마다 보는 모습. VIEW
개념 스키마 (conceptual) - 전체 구조. 테이블 정의
내부 스키마 (internal)   - 실제 저장 방식. 파일, 인덱스, 페이지
```

<br/>

## 물리적 데이터 독립성

내부 스키마가 바뀌어도 개념 스키마는 그대로다.

```sql
CREATE INDEX idx_member_email ON member(email);
```

<br/>

인덱스를 만드는 것은 `어떻게 저장하고 찾을지` 를 바꾸는 일이다. 내부 스키마 변경이다.

<br/>

그런데 앞의 DB 인덱스 글에서 본 대로 SQL은 한 글자도 안 바뀐다.

```sql
SELECT * FROM member WHERE email = 'user99999@test.com';
```

<br/>

인덱스가 있든 없든 같은 SQL이고, 결과도 같다. 속도만 `92` 배 달라진다.

```java
`--SCAN member                               -- 인덱스 없을 때
`--SEARCH member USING INDEX idx_member_email -- 인덱스 있을 때
```

<br/>

앞의 파티셔닝 글에서 본 것도 같다.

테이블을 열두 조각으로 나눠도 애플리케이션은 그냥 `orders` 를 조회한다.

<br/>

이게 안 됐다면, 인덱스를 하나 만들 때마다 모든 쿼리를 고쳐야 했을 것이다.

<br/>

## 논리적 데이터 독립성

개념 스키마가 바뀌어도 외부 스키마는 그대로다. 이쪽이 더 어렵다.

```sql
-- 원래
CREATE TABLE member (id, name, phone, address);

-- 주소를 분리했다
CREATE TABLE member (id, name, phone);
CREATE TABLE address (member_id, city, street);
```

<br/>

앞의 정규화 글에서 한 그 작업이다. 테이블 구조가 바뀌었다.

<br/>

이때 뷰를 만들어두면 쓰는 쪽은 안 바뀐다.

```sql
CREATE VIEW member_full AS
SELECT m.id, m.name, m.phone, a.city || ' ' || a.street AS address
FROM member m LEFT JOIN address a ON m.id = a.member_id;
```

<br/>

원래 `SELECT * FROM member` 를 쓰던 코드는 `member_full` 을 보게 하면 그대로 돈다.

<br/>

## 그런데 논리적 독립성은 잘 안 지켜진다

뷰로 완벽하게 가릴 수 있는 변경이 많지 않기 때문이다.

<br/>

컬럼이 없어지면 뷰에서도 만들어낼 수 없다.

테이블이 여러 개로 쪼개지면 뷰로 합칠 수는 있는데, 그 뷰로는 `INSERT` 가 안 되는 경우가 많다.

<br/>

그래서 실무에서는 뷰 대신 애플리케이션 계층이 그 역할을 한다.

```java
DB 테이블 구조가 바뀌어도
  -> Repository 만 고치고
  -> Service 와 Controller 는 안 바뀐다
```

<br/>

앞의 DTO 글에서 본 `엔티티와 DTO 를 나누는` 이유가 정확히 이것이다.

```java
three-schema 의 외부 스키마 = 애플리케이션의 DTO
개념 스키마              = 엔티티와 테이블
내부 스키마              = 인덱스, 파티션, 저장 방식
```

<br/>

이름만 다를 뿐 `층을 나눠서 변경을 가둔다` 는 발상은 똑같다.

<br/>

## 외부 스키마가 보안에도 쓰인다

앞의 DML/DDL 글에서 본 그 방식이다.

```sql
CREATE VIEW member_public AS
SELECT id, name, SUBSTR(phone, 1, 3) || '-****' AS phone FROM member;

GRANT SELECT ON member_public TO analyst;
REVOKE ALL ON member FROM analyst;
```

<br/>

분석가는 `member_public` 만 볼 수 있고 원본 테이블은 못 본다.

같은 데이터인데 보는 사람에 따라 다른 모습을 보여주는 것이다.

<br/>

원문의 `external schemas 는 사용자마다 다르다` 가 이런 모습이다.
