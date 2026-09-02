## logical vs conceptual vs physical

<br/>

## `data models`설명

DB의 구조를 기술하는데 사용될 수 있는 `개념`들이 모인 `집합`이다

- DB 구조를 `추상화`해서 표현할 수 있는 수단을 제공한다.

`data model`은 여러 종류가 있으며 추상화 수준과 DB 구조화 방식이 조금씩 다르다

- DB 구조란? : 데이터 유형, 데이터 관계, 제약 사항 등등

- DB에서 읽고 쓰기 위한 기본적인 동작들도 포함된다.

<br/><br/>

## data models 분류하기

### `conceptual data models`이란?

- 일반 사용자. 즉, 비개발자분들도 쉽게 이해할 수 있는 개념들로 이뤄진 모델

- 추상화 수준이 가장 높다

- 비즈니스 요구 사항을 추상화하여 기술할 때 사용한다.

- `대표적인 모델은?` : `entity-relationship model`

![이미지](/programming/img/입문418.PNG)

[http://wiki.hash.kr/index.php/파일:ERD예시.PNG](http://wiki.hash.kr/index.php/%ED%8C%8C%EC%9D%BC:ERD%EC%98%88%EC%8B%9C.PNG)

위 사진은, `ER diagram` 이라고 한다.

<br/>

### `logical data models`이란?

백엔드 개발자라면 이걸 많이 사용한다

- 이해하기 어렵지 않으면서도 `디테일`하게 `DB를 구조화` 할 수 있는 개념들을 제공

- 데이터가 컴퓨터에 저장될 때의 구조와 크게 다르지 않게 DB 구조화를 가능하게 함

- 특정 DBMS나 storage에 종속되지 않는 수준에서 DB를 구조화할 수 있는 모델

- `대표적인 모델은?` : `relational data model`

    - 특징: 테이블 형태로 저장되게 되는 것이다.

![이미지](/programming/img/입문419.PNG)

https://www.youtube.com/watch?v=aL0XXc1yGPs&list=PLcXyemr8ZeoREWGhhZi5FZs6cvymjIBVe

<br/>

### `physical data models`이란?

- 컴퓨터에 데이터가 어떻게 파일 형태로 저장되는지를 기술할 수 있는 수단을 제공

- data format, data orderings, access path 등등

    - `access path` : 데이터 검색을 빠르게 하기 위한 구조체
    
        - `access path 대표적인 예시:` → `index`
<br/>

## 궁금증!

```java
세 단계 모델링이 실무에서는 어떤 순서로 진행될까?
```

이름은 어려운데 하는 일은 단순하다.

```java
개념 (conceptual) - 무엇을 저장할지 정한다. 기술과 무관하다
논리 (logical)    - 테이블과 컬럼으로 바꾼다. 정규화를 한다
물리 (physical)   - 실제 DB 문법으로 적는다. 타입과 인덱스를 정한다
```

<br/>

## 개념 모델링

여기서는 DB를 전혀 생각하지 않는다.

```java
회원 - 주문 - 상품

회원은 여러 주문을 한다 (1:N)
주문에는 여러 상품이 담긴다 (N:M)
```

<br/>

`엔티티` 와 `관계` 만 그린다. 컬럼 타입 같은 것은 아직 안 정한다.

<br/>

기획자와 이야기하는 단계라, 개발을 모르는 사람도 읽을 수 있어야 한다.

<br/>

## 논리 모델링

여기서 테이블로 바뀐다.

```java
N:M 관계는 테이블 하나로 못 만든다
-> 중간 테이블을 만든다

member (id, name, phone)
orders (id, member_id, order_date)
order_item (order_id, item_id, quantity)     <- 중간 테이블
item (id, name, price)
```

<br/>

앞의 정규화 글에서 한 작업이 여기에 속한다.

`member_name` 을 `orders` 에 두지 않고 `member` 로 빼는 판단이 논리 모델링이다.

<br/>

이 단계까지는 아직 MySQL인지 PostgreSQL인지 정하지 않아도 된다.

<br/>

## 물리 모델링

여기서 처음으로 특정 DB를 정한다.

```sql
CREATE TABLE member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,     -- MySQL 문법
    name VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_member_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

<br/>

여기서 정하는 것들이다.

```java
타입      - VARCHAR(50) 인가 TEXT 인가
제약      - NOT NULL, UNIQUE, FK 를 걸 것인가
인덱스    - 어느 컬럼에 걸 것인가
파티션    - 나눌 것인가
문자셋    - utf8mb4 (이모지까지 담으려면 utf8 로는 부족하다)
```

<br/>

앞의 DB 인덱스 글과 파티셔닝 글에서 다룬 결정들이 전부 이 단계에 속한다.

<br/>

## 논리와 물리를 나누는 이유

논리 모델은 DB를 바꿔도 그대로 쓸 수 있기 때문이다.

```java
논리 : member 테이블에 id, name, phone 이 있고 id 가 기본 키다
물리 : MySQL 이면 BIGINT AUTO_INCREMENT, PostgreSQL 이면 BIGSERIAL
```

<br/>

DB를 옮길 때 논리 모델은 그대로 두고 물리 모델만 다시 만들면 된다.

<br/>

## 실무에서는 자주 뒤섞인다

특히 물리 단계의 결정이 논리 단계로 거슬러 올라오는 경우가 많다.

```java
논리 모델대로 정규화했더니 조인이 다섯 번 붙는다
-> 조회가 너무 느리다
-> 컬럼 하나를 복사해두기로 한다 (역정규화)
-> 논리 모델이 바뀐다
```

<br/>

앞의 정규화 글에서 본 역정규화가 이 흐름이다.

성능이라는 물리적 사정이 논리 설계를 바꾸는 것이다.

<br/>

그래서 순서대로 한 번에 끝나는 일이 아니라, 오가면서 다듬는 작업에 가깝다.

<br/>

## JPA 를 쓰면 경계가 흐려진다

엔티티 클래스 하나에 논리와 물리가 같이 들어간다.

```java
@Entity
@Table(name = "member", indexes = @Index(name = "idx_phone", columnList = "phone"))
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                          // 물리 : AUTO_INCREMENT

    @Column(length = 50, nullable = false)
    private String name;                      // 물리 : VARCHAR(50) NOT NULL

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;                        // 논리 : N:1 관계
}
```

<br/>

편하기는 한데, 논리 구조가 물리 설정에 파묻혀서 잘 안 보인다.

<br/>

그래서 규모가 커지면 별도로 ERD를 그려서 논리 모델을 따로 관리한다.

코드만 봐서는 전체 관계가 안 보이기 때문이다.
