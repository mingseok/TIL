## DB 정규화(Normalization)




데이터 중복과 `insertion`, `update`, `deletion` anomaly를 최소화하기 위해

일련의 normal forms(NF)에 따라 relational DB를 구성하는 과정

```java
"normal forms 이란?"
정규화 되기 위해 준수해야 하는 몇가지 rule들이 있는데
이 각각의 rule을 "normal form"이라고 부른다.
```

<br/><br/>

## DB 정규화 과정

![이미지](/programming/img/입문477.PNG)

- 처음부터 순차적으로 진행한다.

- normal form을 만족하지 못하면 만족하도록 테이블 구조를 조정한다.

- 앞 단계를 만족해야 다음 단계로 진행 할 수 있다.

<br/>

```java
"1NF ~ BCNF 까지는"
- FD와 Key만으로 정의되는 normal forms
- 3NF까지 도달하면 정규화 됐다고 말하기도 함
- 실무에서는 3NF 혹은 BCNF까지 진행한다. (많이 한다고 해도 4NF까지 정도)
```

<br/><br/>

## Normalization 예시 테이블

![이미지](/programming/img/입문478.PNG)

<br/><br/>

## 1NF :

`attribute의 value는 반드시 나눠질 수 없는 단일한 값이다.`

위 테이블을 보면, Kookmin은 `card_id가 두개로 되어 있다.` → 1NF 위반! (=`“c201, c202"`)

<br/>

저걸 처리해줘야지 다음 단계로 넘어갈 수 있게 되는 것이다. 

그리하여 아래 사진이 1NF가 해결 된 사진이다.

![이미지](/programming/img/입문479.PNG)

`1NF 해결!` → 하나의 튜플을 더 만들어서 해결하였다.

- `문제점:` 중복 데이터가 생기고 primary key도 변경을 해야 한다.

    - 원래는, `account_id` 만으로도 `primary key`가 가능했는데, 이제는 불가능하다.

<br/><br/>

## 왜 중복 데이터가 생긴 걸까?

일단, 키를 변경해주자.

- `(candidate) key`

    - {account_id, card_id}, {bank_name, account_num, card_id}

- `non-prime attribute`

    - class, ratio, empl_id, empl_name

<br/>

위 그림을 보면, `non-prime attribute` 들은 `{account_id, card_id}`키 중에서 

`account_id`만으로도 유니크하게 결정 지을 수 있을 것이다. 

```java
"이런 상황을"
모든 "non-prime attribute"들이 {account_id, card_id}에 대해 "partially dependent" 하는것.
- 즉, "부분적으로 디펜던트"하다고 말할 수 있다.

또 다른 e.g.)
{bank_name, account_num, card_id} 중에서도, {bank_name, account_num} 만으로도
"partially dependent" 하다고 말할 수 있다.
```

<br/>

### 참고

```java
- super key
    - table에서 tuple들을 unique하게 식별할 수 있는 attributes set

- candidate) key
    - 어느 한 attribute라도 제거하면 unique하게 tuples를 식별할 수 없는 super key

- primary key
    - table에서 tuple들을 unique하게 식별하려고 선택된 (candidate) key
```

### prime attribute

- 임의의 key에 속하는 attribute
- `account_id`, `bank_name`, `account_num`

### non-prime attribute

- prime attribute 설명의 반대라고 생각하기
- `class`, `ratio`, `empl_id`, `empl_name`, `card_id`

<br/><br/>

## 2NF :

위의 문제점인, 중복 데이터를 해결 하기 위해 나온 것이 2NF이다.

```java
"모든 non-prime attribute는 모든 key에 fully functionally dependent 해야 한다"
```

위의 문제점은 `card_id` 때문이다.

- `이유는?` 1NF 문제를 해결하기 위해서 card_id를 쪼개면서 튜블을 하나 더 추가하여 문제가 발생

<br/><br/>

### 2NF 해결 방법

![이미지](/programming/img/입문480.PNG)

새로운 테이블을 만들면서 card_id를 다른 테이블로 이동 시켜준 것이다.

이렇게 함으로써, EMPLOYEE 테이블도 중복 튜블을 제거 할 수 있게 되는 것이다.

<br/>

```java
"primary key가 account_id 하나로 다시 돌아왔다."
 이렇게 됨으로써, partially dependent가 아닌, 
 "fully functionally dependent"가 된것이다.

"non-prime attribute" 기준으로 보면, "account_id" 하나만으로 
 {class, ratio, empl_id, empl_name}들을 결정할 수 있다.
```

<br/><br/>

## 3NF :

```java
"모든 non-prime attribute는 어떤 key에도 transitively dependent 하면 안된다"
즉, 쉽게 말해서 "non-prime attribute"와 "non-prime attribute" 사이에는 FD가 있으면 안된다.
```

아래 사진 처럼 `empl_id` 키로만 `empl_name`을 결정할 수 있는 것이 아닌,

`account_id` 키로도, 다른 키로도 결정 할 수 있는 경우가 문제인 것이다.

<br/>

![이미지](/programming/img/입문481.PNG)

즉, 다른 키를 통해서 `transitively dependent`가 생기니깐 `empl_name`과 관련된 중복된 데이터가 같이 생성 되는 것이다. 

그렇기에 이런 중복을 없애주기 위해 3NF가 정의되는 것이다.

<br/>

### 3NF 해결 방법

![이미지](/programming/img/입문482.PNG)

EMPLOYEE 테이블에서는 empl_id가 primary key가 되기 때문에 중복된 데이터를 넣어줄 필요가 없게 된 것이다.

<br/>

### 여기까지 진행 하였다면, “정규화” 됐다

3NF까지 왔다면, 정규화 되었다고, 말할 수 있다.

<br/><br/>

## BCNF :

```java
"모든 유효한 non-trivial FD x → y는 x가 super key 여야 한다"
```

즉, `x → y` 같은 경우의 `“x"`는 `”레프트 핸드 사이드”` 라고 부르기에 x는 반드시 `슈퍼키`여야 한다

![이미지](/programming/img/입문483.PNG)


<br/>

하지만 위 사진처럼 class는 슈퍼키가 아니다.

- 슈퍼키는 개념 자체가 해당 테이블안에서 `tuple`을 유니크하게 식별할 수 있는
    
    `attribute의 집합을 슈퍼키`라고 하는 건데, `class는 non-prime attribute` 하기 때문이다.
    
- 그리하여, `functional dependency를 위반`하게 되는 것이다.

```java
"BCNF를 위반하기에 중복된 데이터가 계속해서 쌓이게 되는 것이다."
```

<br/>

### BCNF 해결 방법

![이미지](/programming/img/입문484.PNG)
<br/>

## 궁금증!

```java
정규화를 안 하면 실제로 어떤 사고가 나는지 데이터로 보면
```

주문과 회원 정보를 한 테이블에 넣어놓고 무엇이 문제인지 확인해봤다.

```sql
CREATE TABLE orders (
    order_id INTEGER PRIMARY KEY,
    member_name TEXT,
    member_phone TEXT,
    item_name TEXT,
    price INTEGER
);

INSERT INTO orders VALUES
 (1, '민석', '010-1111-1111', '책', 15000),
 (2, '민석', '010-1111-1111', '노트', 3000),
 (3, '영한', '010-2222-2222', '펜', 1000);
```

<br/>

### 첫번째) 수정 이상

민석의 전화번호가 바뀌면 두 행을 다 고쳐야 한다.

```sql
UPDATE orders SET member_phone = '010-9999-9999' WHERE member_name = '민석';
```

<br/>

한 행만 고치고 놓치면 같은 사람의 전화번호가 두 개가 된다.

주문이 `1000` 건이면 `1000` 행을 다 고쳐야 하고, 하나라도 실패하면 데이터가 어긋난다.

<br/>

### 두번째) 삭제 이상

영한이 주문을 취소하면 어떻게 될까.

```sql
DELETE FROM orders WHERE order_id = 3;
```

<br/>

주문만 지웠는데 영한이라는 회원 정보까지 같이 사라진다.

주문 기록과 회원 정보는 별개인데, 한 행에 있어서 같이 없어지는 것이다.

<br/>

### 세번째) 삽입 이상

가입만 하고 아직 주문을 안 한 회원은 넣을 수가 없다.

```sql
INSERT INTO orders VALUES (?, '새회원', '010-3333-3333', NULL, NULL);
```

<br/>

주문 테이블인데 주문 없는 행을 억지로 만들어야 한다.

`item_name` 과 `price` 를 `NULL` 로 채워두면 나중에 집계할 때마다 이 행을 걸러내야 한다.

<br/>

## 나누면 셋 다 사라진다

```sql
CREATE TABLE member (id INTEGER PRIMARY KEY, name TEXT, phone TEXT);
CREATE TABLE orders (id INTEGER PRIMARY KEY, member_id INTEGER, item_name TEXT, price INTEGER);
```

<br/>

```java
수정 - 전화번호는 member 에 한 곳에만 있다. 한 줄만 고치면 된다
삭제 - 주문을 지워도 member 는 그대로다
삽입 - 주문 없는 회원도 member 에 넣을 수 있다
```

<br/>

정규화의 목적이 `공간 절약` 이라고 오해하기 쉬운데, 진짜 목적은 이것이다.

`같은 사실을 한 곳에만 적어두는 것` 이다.

```java
같은 사실이 여러 곳에 있으면 -> 언젠가 서로 달라진다
한 곳에만 있으면            -> 달라질 수가 없다
```

<br/>

## 그런데 실무에서는 일부러 어기기도 한다

정규화하면 조회할 때 조인이 늘어난다.

```sql
-- 정규화 전 : 테이블 하나
SELECT member_name, item_name FROM orders;

-- 정규화 후 : 조인이 필요하다
SELECT m.name, o.item_name
FROM orders o JOIN member m ON o.member_id = m.id;
```

<br/>

테이블이 다섯 개로 쪼개져 있으면 조인이 네 번 붙는다.

조회가 아주 많은 화면에서는 이 비용이 문제가 된다.

<br/>

그래서 일부러 중복을 두기도 한다. 이것을 `역정규화` 라고 한다.

```sql
CREATE TABLE orders (
    id INTEGER PRIMARY KEY,
    member_id INTEGER,
    member_name TEXT,        -- 일부러 복사해둔다
    item_name TEXT
);
```

<br/>

다만 이러면 위에서 본 `수정 이상` 이 그대로 돌아온다.

회원 이름이 바뀌면 주문 테이블도 같이 고쳐야 한다.

<br/>

## 그래서 판단 기준이 필요하다

```java
바뀌는 값을 복사하면 -> 위험하다. 동기화를 계속 신경 써야 한다
안 바뀌는 값을 복사하면 -> 괜찮다
```

<br/>

주문 시점의 상품 가격이 좋은 예다.

```sql
CREATE TABLE order_item (
    order_id INTEGER,
    item_id INTEGER,
    price INTEGER          -- 주문 당시 가격을 복사해둔다
);
```

<br/>

이건 중복이 아니라 `그때의 사실` 이다.

나중에 상품 가격이 오르더라도 과거 주문의 결제 금액은 그대로여야 한다.

<br/>

`item` 테이블을 조인해서 현재 가격을 가져오면 오히려 틀린 값이 된다.

```java
정규화가 맞는 것 - 지금의 사실 (회원 전화번호, 상품 이름)
복사해둬야 하는 것 - 그때의 사실 (주문 당시 가격, 배송지)
```

<br/>

## 3정규형까지가 실무의 기준이다

`BCNF` 나 `4정규형` 까지 가는 경우는 드물다.

<br/>

`3정규형` 만 지켜도 위에서 본 세 가지 이상은 대부분 사라진다.

그 이상은 이론적으로는 더 나은데, 테이블이 너무 잘게 쪼개져서 다루기가 힘들어진다.

```java
1정규형 - 한 칸에 값 하나만 (전화번호 여러 개를 콤마로 넣지 않는다)
2정규형 - 복합 키의 일부에만 딸린 컬럼을 분리한다
3정규형 - 키가 아닌 컬럼에 딸린 컬럼을 분리한다
```

<br/>

`3정규형` 을 한 줄로 줄이면 `모든 컬럼은 기본 키에만 의존해야 한다` 가 된다.
