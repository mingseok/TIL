## 영속성 전이: CASCADE

- 특정 엔티티를 영속 상태로 만들 때 연관된 엔티티도 함께 영속상태로 만들도 싶을 때

    - ex) 부모 엔티티를 저장할 때 연관된 자식 엔티티도 함께 저장하고 싶을때 사용한다.

<br/>

### `Parent.class`

```java
@Entity
public class Parent {

    // 생략..

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL) // 핵심
    private List<Child> childList = new ArrayList<>();

    public void addChild(Child child) {
        childList.add(child);
        child.setParent(this);
    }

    // 게터, 세터 생략..
}
```

<br/>

`Child.class`

```java
@Entity
public class Child {

    // 생략..

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;

    // 게터, 세터 생략..
}
```

<br/>

## 결과값


![이미지](/programming/img/입문375.PNG)

<br/><br/>

## 영속성 전이: CASCADE - 주의!

- 영속성 전이는 연관관계를 매핑하는 것과 아무 관련이 없음

- 엔티티를 영속화할 때 연관된 엔티티도 함께 영속화하는 편리함을 제공할 뿐
<br/>

## 궁금증!

```java
CASCADE 는 DB 의 ON DELETE CASCADE 와 같은 것일까?
```

다르다. 동작하는 층이 아예 다르다.

```java
DB 의 ON DELETE CASCADE - DB 가 처리한다. 애플리케이션은 모른다
JPA 의 CascadeType      - JPA 가 처리한다. DELETE 문을 여러 개 만들어 보낸다
```

<br/>

앞의 테이블 작성 글에서 DB 쪽을 확인해봤다.

```sql
DELETE FROM members WHERE id = 1;
```

```sql
삭제 전 profile 수 = 2
1번 회원 삭제 후 profile 수 = 1
```

<br/>

`profile` 에 대한 `DELETE` 를 보낸 적이 없는데 사라졌다. DB가 알아서 한 것이다.

<br/>

JPA의 `CascadeType.REMOVE` 는 이렇게 동작한다.

```sql
SELECT * FROM child WHERE parent_id = 1;     -- 자식을 먼저 조회하고
DELETE FROM child WHERE id = 1;              -- 하나씩 지운다
DELETE FROM child WHERE id = 2;
DELETE FROM parent WHERE id = 1;
```

<br/>

자식이 `1000` 개면 `DELETE` 가 `1000` 번 나간다.

앞의 DBCP 글에서 본 대로 쿼리 하나마다 왕복이 있으니 그만큼 느리다.

<br/>

## 그래서 대량이면 DB 쪽이 훨씬 빠르다

```java
JPA CASCADE - 자식 수만큼 DELETE. 대신 엔티티 생명주기가 관리된다
DB CASCADE  - DELETE 한 번. 대신 JPA 는 아무것도 모른다
```

<br/>

DB 쪽으로 처리하면 영속성 컨텍스트에는 지워진 자식이 그대로 남아 있다.

앞의 영속성 컨텍스트 글에서 본 1차 캐시가 실제 DB와 어긋나는 것이다.

<br/>

그래서 섞어 쓸 때는 `clear()` 로 비워줘야 한다.

<br/>

## CASCADE 를 쓰면 안 되는 경우

원문의 `주의` 가 가리키는 지점이다.

```java
@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
private List<Child> children;
```

<br/>

`Child` 를 `Parent` 만 참조하고 있으면 괜찮다.

<br/>

그런데 다른 곳에서도 `Child` 를 참조하고 있으면 위험하다.

```java
Parent 를 지운다
  -> Child 도 지워진다
  -> 그런데 Order 가 그 Child 를 참조하고 있었다
  -> 외래 키 제약 위반이거나, 참조가 깨진 데이터가 남는다
```

<br/>

그래서 판단 기준이 이렇게 된다.

```java
CASCADE 를 쓸 만한 경우
  - 자식의 생명주기가 부모에게 완전히 종속된다
  - 그 부모만 자식을 참조한다
  예) 게시글과 첨부파일, 주문과 주문항목

CASCADE 를 쓰면 안 되는 경우
  - 여러 곳에서 참조한다
  예) 회원과 게시글 (게시글에 남의 댓글이 달려 있다)
```

<br/>

앞의 테이블 작성 글에서 본 판단과 같다.

<br/>

## orphanRemoval 은 다른 것이다

이름이 비슷해서 헷갈린다.

```java
CascadeType.REMOVE - 부모를 지우면 자식도 지운다
orphanRemoval      - 부모의 컬렉션에서 빼면 자식이 지워진다
```

<br/>

```java
parent.getChildren().remove(0);      // orphanRemoval = true 면 DELETE 가 나간다
em.remove(parent);                   // CascadeType.REMOVE 면 자식도 지워진다
```

<br/>

앞의 JPA API 를 확인해보면 `@OneToMany` 에만 `orphanRemoval` 이 있다.

```java
public interface jakarta.persistence.OneToMany {
    CascadeType[] cascade();
    String mappedBy();
    boolean orphanRemoval();
}
```

```java
public interface jakarta.persistence.ManyToOne {
    CascadeType[] cascade();
    // orphanRemoval 이 없다
}
```

<br/>

`ManyToOne` 쪽에서는 컬렉션이 없으니 `고아` 라는 개념 자체가 없기 때문이다.

<br/>

## CascadeType 값들

```java
ALL     - 아래 전부
PERSIST - 저장할 때 같이 저장
MERGE   - 병합할 때 같이
REMOVE  - 삭제할 때 같이
REFRESH - 새로고침할 때 같이
DETACH  - 준영속으로 만들 때 같이
```

<br/>

실무에서 자주 쓰는 것은 `PERSIST` 와 `ALL` 두 개다.

```java
cascade = CascadeType.PERSIST   // 부모를 저장하면 자식도 저장. 삭제는 따로
cascade = CascadeType.ALL       // 생명주기가 완전히 붙어 있을 때
```

<br/>

`ALL` 은 편한데 `REMOVE` 까지 포함하니, 위에서 본 위험을 확인하고 써야 한다.

`저장만 같이 하고 싶다` 면 `PERSIST` 만 주는 편이 안전하다.
