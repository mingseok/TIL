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