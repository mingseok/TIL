## DML, VDL, DDL 설명

<br/>

## database language 설명

오늘날의 DBMS는 `DML`, `VDL`, `DDL`이 따로 존재하기 보다는 통합된 언어로 존재한다.

- 대표적인 예가 relational database language : `SQL` 이다.

<br/>

### `DDL` (=data definition language)

- `conceptual schema`를 정의하기 위해 사용되는 언어

- `VDL` (=view defivition language) 라는 것도 있는데, `DDL`이 `VDL`역할까지 수행한다.

    - `VDL`은 `external schemas`를 정의하기 위해 사용되는 언어이다.

<br/>

### `DML` (=data manipulation language)

- `database`에 있는 `data`를 활용하기 위한 언어

- data `추가`, `삭제`, `수정`, `검색` 등등의 기능을 제공하는 언어
<br/>

## 궁금증!

```java
DDL 과 DML 이 실제로 어떻게 다르게 동작할까?
```

트랜잭션에서 다르게 다뤄진다는 것이 제일 큰 차이다.

```sql
BEGIN;
INSERT INTO member VALUES (1, '민석');    -- DML
CREATE TABLE temp (id INTEGER);           -- DDL
ROLLBACK;
```

<br/>

MySQL에서는 `CREATE TABLE` 순간 앞의 `INSERT` 까지 커밋되어 버린다.

DDL이 자동으로 커밋을 일으키기 때문이다. 이것을 `암묵적 커밋` 이라고 한다.

<br/>

그래서 마이그레이션 스크립트에서 사고가 난다.

```sql
BEGIN;
ALTER TABLE member ADD COLUMN age INT;    -- 여기서 이미 커밋된다
UPDATE member SET age = 0;                -- 이게 실패해도
ROLLBACK;                                 -- 컬럼 추가는 되돌릴 수 없다
```

<br/>

컬럼은 추가됐는데 값 채우기는 실패한 상태로 남는다.

<br/>

PostgreSQL은 DDL도 트랜잭션 안에서 되돌릴 수 있다.

DB마다 다르니, 마이그레이션 도구를 쓸 때 어느 DB인지 알고 써야 한다.

```java
MySQL      - DDL 이 암묵적 커밋을 일으킨다. 롤백 안 된다
PostgreSQL - DDL 도 롤백된다
```

<br/>

## 세 번째 갈래가 하나 더 있다

원문의 둘에 더해 `DCL` 이 있다.

```java
DDL (Data Definition Language)   - CREATE, ALTER, DROP, TRUNCATE
DML (Data Manipulation Language) - SELECT, INSERT, UPDATE, DELETE
DCL (Data Control Language)      - GRANT, REVOKE
TCL (Transaction Control)        - COMMIT, ROLLBACK, SAVEPOINT
```

<br/>

`DCL` 은 권한을 다룬다.

```sql
GRANT SELECT ON member TO readonly_user;
REVOKE INSERT ON member FROM app_user;
```

<br/>

실무에서 조회 전용 계정을 따로 만들 때 쓴다.

앞의 트랜잭션 옵션 글에서 본 `readOnly` 는 애플리케이션 쪽 힌트라 강제력이 없지만,

`GRANT SELECT` 만 준 계정은 DB가 아예 쓰기를 막는다.

<br/>

## DELETE 와 TRUNCATE 가 갈리는 이유

둘 다 전체 삭제인데 하나는 DML, 하나는 DDL이다.

```sql
DELETE FROM member;      -- DML
TRUNCATE TABLE member;   -- DDL
```

<br/>

```java
DELETE   - 행을 하나씩 지운다. 로그를 남겨서 롤백할 수 있다. WHERE 를 쓸 수 있다
TRUNCATE - 테이블을 통째로 비운다. 로그를 거의 안 남긴다. 훨씬 빠르지만 되돌릴 수 없다
```

<br/>

`100만` 행을 지울 때 `DELETE` 는 `100만` 개의 언두 로그를 남긴다.

앞의 MVCC 글에서 본 그 언두 로그다. 시간도 오래 걸리고 디스크도 많이 쓴다.

<br/>

`TRUNCATE` 는 그냥 데이터 파일을 새로 만드는 것에 가깝다. 거의 즉시 끝난다.

<br/>

그래서 테스트 데이터를 비울 때는 `TRUNCATE` 가 훨씬 낫다.

대신 되돌릴 수 없으니 운영 DB에서는 조심해야 한다.

<br/>

## VDL 이 지금은 왜 안 보이는가

원문에 `DDL 이 VDL 역할까지 수행한다` 고 되어 있는 그 이유다.

```sql
CREATE VIEW active_member AS
SELECT id, name FROM member WHERE deleted = false;
```

<br/>

`CREATE VIEW` 도 결국 `CREATE` 다. 문법이 통합되어 있어서 굳이 이름을 나눌 이유가 없어졌다.

<br/>

`VIEW` 는 앞의 three-schema 글에서 본 `외부 스키마` 를 만드는 도구다.

같은 데이터를 사용자마다 다르게 보여줄 수 있게 한다.

```sql
-- 개인정보를 가린 뷰
CREATE VIEW member_public AS
SELECT id, name, SUBSTR(phone, 1, 3) || '-****' AS phone FROM member;
```

<br/>

이 뷰만 조회할 수 있는 계정을 만들면, 그 계정은 전화번호를 볼 수 없다.

`DCL` 과 같이 쓰면 권한을 컬럼 단위로 나누는 셈이 된다.
