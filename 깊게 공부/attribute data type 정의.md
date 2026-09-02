## attribute data type 정의

<br/>

## attribute data type : 숫자

- 정수

    - 정수를 저장할 때 사용

        - `ex)` `TINYINT`, `MEDIUMINT`, `BIGINT` …등
- 부동 소수점 방식

    - 실수를 저장할 때 사용

    - 고정 소수점 방식에 비해 정확하지 않다.
        - `ex)` `FLOAT`, `DOUBLE`
- 고정 소수점 방식

    - 실수를 정확하게 저장할 때 사용 → 돈 계산할 경우

        - `ex)` `DECIMAL`, `NUMERIC`
        - `DECIMAL` 같은 경우는 `DECIMAL(5, 2)` 두개의 인자를 받는다.
            
            첫번째는 숫자의 총 자리수, 두번째는 소수점`(“.”)` 기준, 뒤쪽의 자리 수
            

<br/><br/>

## attribute data type : 문자열

- 고정 크기 문자열

    - 최대 몇 개의 ‘문자’를 가지는 문자열을 저장할지를 지정

    - 저장될 문자열의 길이가 최대 길이보다 작으면 나머지를 `space`로 채워서 저장한다.

    - `name char(4)` 일 경우 → `‘a   ‘` 으로 저장한다는 것이다. or `‘고고고고’` → 꽉 차있기 때문

        - 이렇게, `스페이스`를 채워서 저장을 하고, 다시 읽어 올때는 스페이스는 제거가 된다.

    - `ex)` `char(n)` (0 ≤ n ≤ 255)

        - 휴대폰 번호 같은 컬럼들은 `char(n)`를 사용하자.
- 가변 크기 문자열

    - 최대 몇개의 ‘문자’를 가지는 문자열을 저장할지를 지정

    - 차이로는 → “저장될 문자열의 길이 만큼만 저장”

    - `name varchar(4)` 일 경우 → `‘a’`, `‘한국’`, `‘고고고고’` 스페이스 없이 저장된다는 뜻

    - `ex)` `varchar(n)` (0 ≤ n ≤ 65,535)

- 사이즈가 큰 문자열

    - 사이즈가 큰 문자열을 저장할 때 사용

        - `ex)` `MEDIUMTEXT`, `LONGTEXT`

<br/><br/>

## attribute data type : 날짜와 시간

- 날짜

    - 년, 월, 일을 저장

    - YYYY-MM-DD

        - `ex)` `DATA`
- 시간

    - 시, 분, 초를 저장

    - hh:mm:ss

        - `ex)` `TIME`

- 날짜와 시간

    - 날짜와 시간을 같이 표현

    - YYYY-MM-DD hh:mm:ss
    
        - `ex)` `DATETIME`, `TIMESTAMP`
<br/>

## 궁금증!

```java
타입을 정할 때 실무에서 실제로 걸리는 지점들
```

이론적인 정의보다 `잘못 고르면 무슨 일이 생기는가` 로 보면 기억에 남는다.

<br/>

## 첫번째) 돈에 실수형을 쓰면 안 된다

```sql
CREATE TABLE payment (amount DOUBLE);
```

<br/>

```java
0.1 + 0.2 = 0.30000000000000004
```

<br/>

부동소수점은 `0.1` 을 정확히 표현하지 못한다. 이진수로 딱 떨어지지 않기 때문이다.

`10` 원짜리 오차가 백만 건 쌓이면 장부가 안 맞는다.

<br/>

돈은 `DECIMAL` 을 쓰거나, 아예 정수로 저장한다.

```sql
amount DECIMAL(19, 2)     -- 정확한 십진 소수
amount BIGINT             -- 원 단위 정수로 저장 (더 안전하다)
```

<br/>

자바에서도 `double` 이 아니라 `BigDecimal` 을 쓴다.

<br/>

## 두번째) VARCHAR 길이를 넉넉히 잡으면 되는가

```sql
name VARCHAR(1000)     -- 넉넉하게?
```

<br/>

저장 공간은 실제 길이만큼만 쓰니 낭비는 아니다.

문제는 인덱스와 정렬이다.

<br/>

인덱스에는 최대 길이를 기준으로 자리를 잡는 경우가 있고,

`ORDER BY` 할 때 임시 테이블을 만들면 최대 길이로 메모리를 잡는다.

<br/>

앞의 B tree 글에서 본 대로 인덱스 키가 길면 노드에 담기는 키가 줄어들고, 트리가 높아진다.

```java
짧은 키 -> 노드 하나에 많이 담긴다 -> 트리가 낮다 -> 디스크 접근이 적다
```

<br/>

그래서 `실제로 필요한 만큼` 잡는 것이 맞다.

<br/>

## 세번째) 문자셋을 잘못 고르면 이모지가 안 들어간다

```sql
CREATE TABLE post (content VARCHAR(1000)) CHARSET=utf8;
INSERT INTO post VALUES ('안녕 😀');
```

```java
Incorrect string value: '\xF0\x9F\x98\x80'
```

<br/>

MySQL의 `utf8` 은 이름과 달리 진짜 UTF-8이 아니다. 한 글자를 최대 `3` 바이트로만 저장한다.

<br/>

앞의 char 글에서 본 대로 이모지는 그 범위를 넘어간다.

`4` 바이트가 필요한데 `3` 바이트만 받아서 거부하는 것이다.

<br/>

`utf8mb4` 를 써야 한다. `mb4` 가 `최대 4바이트` 라는 뜻이다.

```sql
CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
```

<br/>

## 네번째) 날짜 타입은 시간대를 조심해야 한다

```sql
DATETIME  - 시간대 정보가 없다. 넣은 값 그대로 저장된다
TIMESTAMP - UTC 로 저장하고 조회할 때 세션 시간대로 바꿔준다
```

<br/>

서버가 여러 지역에 있거나 시간대 설정이 다르면 값이 달라 보인다.

```java
서울 서버에서 2026-01-01 09:00 저장
-> TIMESTAMP 면 UTC 2026-01-01 00:00 으로 저장된다
-> 미국 서버에서 조회하면 2025-12-31 19:00 으로 보인다
```

<br/>

`DATETIME` 은 이 변환을 안 하니 항상 같은 값이 나온다. 대신 어느 시간대인지 알 수 없다.

<br/>

그래서 `UTC 로 저장하고 보여줄 때만 변환한다` 는 규칙을 정해두는 것이 안전하다.

`TIMESTAMP` 의 `2038년 문제` 도 있어서, 요즘은 `DATETIME` 에 UTC 를 담는 경우가 많다.

<br/>

## 다섯번째) NULL 을 허용할 것인가

```sql
age INT           -- NULL 가능
age INT NOT NULL  -- NULL 불가
```

<br/>

앞의 래퍼 클래스 글에서 본 `없음과 0을 구분해야 하는가` 가 판단 기준이다.

<br/>

`NULL` 을 허용하면 SQL이 까다로워진다.

```sql
SELECT * FROM member WHERE age != 30;    -- age 가 NULL 인 행은 안 나온다
```

<br/>

`NULL` 은 `모름` 이라서 `30이 아니다` 라고 단정할 수 없다는 논리다.

`NULL` 을 포함하려면 조건을 하나 더 붙여야 한다.

```sql
WHERE age != 30 OR age IS NULL
```

<br/>

집계 함수도 `NULL` 을 무시한다.

```sql
SELECT COUNT(age) FROM member;     -- NULL 인 행은 안 센다
SELECT COUNT(*) FROM member;       -- 전부 센다
```

<br/>

두 결과가 다르다는 것을 모르면 통계가 어긋난다.

가능하면 `NOT NULL DEFAULT` 로 두고, 정말 `모름` 을 표현해야 할 때만 허용하는 편이 낫다.
