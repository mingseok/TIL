## trigger란 무엇인가요?



`trigger`는 테이블에서 변화가 일어날 때 마다 통계를 계산해주고 싶을 때, 계산하는 용도로도 사용될 수 있습니다.

- `update`, `insert`, `delete` 등을 한번에 감지하도록 설정이 가능하다. (mySQL은 불가능)

<br/><br/>

## trigger 사용 시 주의 사항

`Logic tier`에서 소스 코드로는 발견할 수 없는 로직이기 때문에

어떤 동작이 일어나는지 파악하기 어렵고 문제가 생겼을 때 대응하기 어렵습니다.

<br/>

즉, 가시적이지 않아서 `개발도`, `관리도`, `문제 파악도` 힘들어진다는 것입니다.

또한, 여러개의 `trigger`가 있을때는 파악하기 힘들다는 것입니다.

<br/>

e.g.) 기능 추가를 하였다

```java
소스코드를 수정하고 테스트를 하는데, 데이터가 이상하게 변경되어 있는 것이다.

즉, 내가 수정하지 않은 부분까지도 수정되어 있는 것이다.

"처음부터 `trigger`라고 인식하지 못하면, `내 잘못으로 생각하는게 정상이다`"
```

<br/><br/>

## 주의 사항 정리

- 과도한 트리거 사용은 `DB`에 부담을 주고 응답을 느리게 만든다
- 디버깅이 어렵다
    - 수면 아래에서 동작하는 트리거는 `Logic tier` 에서 소스코드만 봐서는
        
        트리거가 존재하는지에 대한 여부를 판단할 수 없기 때문이다
        
- 문서 정리가 특히나 중요하다

```java
즉, 트리거는 최후의 카드로 남겨 놓는것이 좋은 선택일 것이다.
왜? → 유지보수 관리가 너무 힘들기 때문이다.
```
<br/>

## 궁금증!

```java
"소스코드만 봐서는 알 수 없다" 는 게 실제로 어떤 모습일까?
```

SQLite로 직접 만들어봤다. 게시글이 등록되거나 삭제될 때 로그를 남기는 트리거다.

```sql
CREATE TABLE post (id INTEGER PRIMARY KEY, title TEXT, view_count INTEGER DEFAULT 0);
CREATE TABLE post_log (id INTEGER PRIMARY KEY, post_id INTEGER, action TEXT);

CREATE TRIGGER post_insert_log AFTER INSERT ON post
BEGIN
  INSERT INTO post_log (post_id, action) VALUES (NEW.id, '등록됨');
END;

CREATE TRIGGER post_delete_log AFTER DELETE ON post
BEGIN
  INSERT INTO post_log (post_id, action) VALUES (OLD.id, '삭제됨');
END;
```

<br/>

이제 `post` 에만 손을 대본다.

```sql
INSERT INTO post (title) VALUES ('첫 글');
INSERT INTO post (title) VALUES ('두 번째 글');
DELETE FROM post WHERE id = 1;
```

<br/>

### 결과

```sql
--- post 테이블 ---
2|두 번째 글|0

--- post_log (아무도 INSERT 안 했는데 쌓여 있다) ---
1|1|등록됨
2|2|등록됨
3|1|삭제됨
```

<br/>

`post_log` 에 `INSERT` 를 한 적이 한 번도 없는데 세 줄이 쌓여 있다.

애플리케이션 코드를 아무리 뒤져도 `post_log` 에 넣는 코드는 안 나온다.

<br/>

원문의 `내 잘못으로 생각하는게 정상이다` 가 정확히 이 상황이다.

디버거를 걸어도 안 보인다. 자바 코드 밖에서 벌어지는 일이기 때문이다.

<br/>

## NEW 와 OLD 라는 특별한 이름

트리거 안에서만 쓸 수 있는 두 가지가 있다.

```sql
INSERT -> NEW 만 있다 (들어온 행)
DELETE -> OLD 만 있다 (지워진 행)
UPDATE -> 둘 다 있다 (OLD = 바뀌기 전, NEW = 바뀐 후)
```

<br/>

`UPDATE` 트리거에서 둘을 비교하면 `무엇이 바뀌었는지` 를 알 수 있다.

```sql
CREATE TRIGGER price_change AFTER UPDATE ON item
WHEN OLD.price != NEW.price
BEGIN
  INSERT INTO price_history (item_id, before, after)
  VALUES (OLD.id, OLD.price, NEW.price);
END;
```

<br/>

가격이 바뀔 때만 이력을 남기는 것이다.

`WHEN` 절 덕분에 다른 컬럼만 바뀐 경우에는 안 돈다.

<br/>

## AFTER 와 BEFORE

```sql
BEFORE - 실제 변경 전에 실행된다. NEW 값을 고칠 수 있다
AFTER  - 변경이 끝난 뒤 실행된다. 로그 남기기에 쓴다
```

<br/>

`BEFORE` 는 더 위험하다. 내가 넣은 값이 저장되기 전에 바뀔 수 있기 때문이다.

```sql
CREATE TRIGGER normalize BEFORE INSERT ON member
BEGIN
  SELECT ... ;  -- NEW.email 을 소문자로 바꾸는 식
END;
```

<br/>

애플리케이션에서 `Kim@Test.com` 을 넣었는데 조회하면 `kim@test.com` 이 나온다.

값이 왜 다른지 코드로는 절대 알 수 없다.

<br/>

## 그런데 트리거가 유용한 자리도 있다

원문의 `최후의 카드` 라는 결론에 조건을 붙이면 이렇다.

```java
애플리케이션이 하나뿐이다        -> 코드로 하는 편이 낫다
DB 를 여러 경로에서 건드린다     -> 트리거를 고려할 만하다
```

<br/>

여러 애플리케이션이 같은 테이블을 쓰거나, 배치 스크립트나 사람이 직접 SQL을 날리는 환경이라면

애플리케이션 코드에 규칙을 넣어봐야 우회된다.

<br/>

`감사 로그(audit log)` 가 그런 예다.

누가 어떤 경로로 데이터를 바꿨든 반드시 기록이 남아야 한다면, DB 쪽에 두는 것이 확실하다.

<br/>

그래도 문서화는 필수다. 원문의 `문서 정리가 특히나 중요하다` 가 이 때문이다.

<br/>

## 트리거를 찾는 방법은 알아두자

트리거가 있는지 확인하는 SQL 정도는 기억해두면 좋다.

```sql
-- MySQL
SHOW TRIGGERS;
SELECT * FROM information_schema.TRIGGERS WHERE EVENT_OBJECT_TABLE = 'post';

-- PostgreSQL
SELECT * FROM information_schema.triggers;

-- SQLite
SELECT name, sql FROM sqlite_master WHERE type = 'trigger';
```

<br/>

데이터가 이상한데 코드에서 원인을 못 찾겠으면, 이걸 한 번 확인해보는 것이 빠르다.
