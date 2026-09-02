## RDBMS와 DBMS 차이는?

<br/>

## DBMS란?

```java
사용자에게 "DB를 정의"하고 만들고 관리하는 기능을 제공하는 "소프트웨어 시스템"이다.
```

- 대표적인 `DBMS`는? → `PostgreSQL`, `MySQL`, `ORACLE database`, `SQL Server`

- DB를 정의하다 보면 부가적인 데이터가 발생한다.

    - 부가적인 데이터:  `“metadata”` 라고 부른다.

<br/>

DBMS는 데이터를 파일로 저장하고, 

RDBMS는 데이터를 테이블 형식으로 저장합니다.

<br/>

- DBMS에서 데이터는 일반적으로 계층적 형식 또는 탐색 형식으로 저장되며,

- RDBMS에서 테이블들은 primary key라는 식별자가 있으며 데이터의 값들은 테이블 형식으로 저장됩니다.
    

<br/><br/>

## RDBMS는 ACID를 위한 무결성 제약을 정의한다.

A - Atomocity 원자성

C - Consistency 일관성

I - Isolation 고립성

D - Durability 지속성

DBMS를 예로 들면 XML등이 있으며, 

RDBMS는 `mysql`, `oracle`, `sql server` 등이 있습니다.
<br/>

## 궁금증!

```java
"관계형" 이라는 말의 관계가 무엇을 뜻하는 걸까?
```

테이블끼리 연결되어 있다는 뜻으로 오해하기 쉬운데, 원래 뜻은 다르다.

<br/>

수학의 `관계(relation)` 에서 온 말이고, `테이블 하나가 곧 하나의 관계` 다.

```java
member 라는 관계 = (id, name, phone) 이라는 조합들의 집합
{ (1, 민석, 010-1111), (2, 영한, 010-2222) }
```

<br/>

즉 `관계형` 은 테이블 사이의 연결이 아니라 `테이블 자체` 를 가리키는 말이다.

외래 키로 테이블을 잇는 것은 그 위에 얹은 기능이지 이름의 유래가 아니다.

<br/>

## 그래서 집합 연산이 된다

테이블을 집합으로 보기 때문에 집합 연산이 그대로 쓰인다.

```sql
SELECT ... UNION     SELECT ...    -- 합집합
SELECT ... INTERSECT SELECT ...    -- 교집합
SELECT ... EXCEPT    SELECT ...    -- 차집합
```

<br/>

`JOIN` 도 두 집합의 곱집합에서 조건에 맞는 것만 고르는 연산이다.

```sql
SELECT * FROM a, b WHERE a.id = b.a_id;   -- 곱집합 후 걸러내기
SELECT * FROM a JOIN b ON a.id = b.a_id;  -- 같은 뜻
```

<br/>

SQL이 `어떻게 찾을지` 가 아니라 `무엇을 원하는지` 만 적으면 되는 이유가 이 수학적 배경이다.

집합 연산으로 표현되어 있으니, DB가 알아서 최적의 실행 계획을 짤 수 있다.

<br/>

## 그 계획을 실제로 볼 수 있다

앞의 DB 인덱스 글에서 본 것이 그 계획이다.

```sql
EXPLAIN QUERY PLAN SELECT * FROM member WHERE email = 'user99999@test.com';

-- 인덱스가 없으면
`--SCAN member

-- 인덱스가 있으면
`--SEARCH member USING INDEX idx_member_email (email=?)
```

<br/>

같은 SQL인데 실행 방법이 달라졌다.

우리는 `이 조건에 맞는 것을 달라` 고만 적었고, `어떻게 찾을지` 는 DB가 정한 것이다.

```java
절차적 언어 - 어떻게 할지를 내가 적는다 (for 문으로 하나씩 비교)
SQL         - 무엇을 원하는지만 적는다. 방법은 DB 가 정한다
```

<br/>

## DBMS 인데 관계형이 아닌 것들

원문의 구분을 실제 제품으로 보면 이렇다.

```java
관계형 (RDBMS)  - MySQL, PostgreSQL, Oracle, SQL Server, SQLite
문서형          - MongoDB     (JSON 같은 문서 단위로 저장)
키-값           - Redis       (키 하나에 값 하나)
컬럼형          - Cassandra   (컬럼 단위로 저장)
그래프          - Neo4j       (노드와 간선으로 저장)
```

<br/>

전부 `데이터를 관리하는 시스템` 이니 DBMS는 맞다.

다만 테이블과 SQL을 쓰지 않으니 관계형은 아니다.

<br/>

## 관계형이 아직도 기본인 이유

앞의 트랜잭션 글에서 본 `ACID` 때문이다.

```java
돈을 다루는 곳 - 잔액이 틀리면 안 된다 -> ACID 가 필요하다 -> 관계형
로그를 쌓는 곳 - 하나쯤 틀려도 된다. 대신 양이 많다 -> NoSQL 도 괜찮다
```

<br/>

NoSQL도 요즘은 트랜잭션을 지원하는 경우가 많아졌지만,

관계형만큼 오래 검증된 것은 아니다.

<br/>

그래서 실무에서는 대개 이렇게 나눈다.

```java
주 저장소     - 관계형 (정합성이 중요한 데이터)
캐시          - Redis
로그와 검색   - Elasticsearch
```

<br/>

하나로 다 하려 하지 않고, 성격에 맞는 것을 골라 쓰는 쪽으로 정리된 셈이다.
