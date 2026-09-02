## API를 만들시 '회원 조회' 유의사항

API를 만들때는 엔티티를 절대로 외부에 반환하면 안된다 

(=파라미터를 받든, 나가든 )




<br/>

항상 중간에 `API`에 맞는 스펙에 맞는 `DTO`를 만들고, 

그걸 활용하는 것이 오류를 줄이는 방법이다

```
이 방법은, 추천이 아니라 강제이다.
```

<br/>

## 궁금증!

```java
"엔티티를 반환하면 안 된다" 는 게 강제인 이유
```

앞의 DTO를 만들어 두면 무슨 장점이 있나요 글에서 세 가지를 확인했다.

<br/>

### 첫번째) 숨겨야 할 것까지 나간다

```java
@Entity
class Member {
    private String email;
    private String password;      // 이것도 같이 나간다
}
```

<br/>

`@JsonIgnore` 로 막을 수는 있는데, 필드를 추가할 때마다 기억해야 한다.

```java
엔티티 반환 -> 막는 것을 잊으면 나간다 (기본이 공개)
DTO 반환   -> 넣는 것을 잊으면 안 나간다 (기본이 비공개)
```

<br/>

### 두번째) 양방향 연관관계면 무한 루프에 빠진다

앞의 DTO 글에서 실제로 돌려봤을 때 이렇게 됐다.

```java
결과 -> StackOverflowError - 서로를 계속 따라간다
```

<br/>

`Member` 가 `Team` 을 참조하고 `Team` 이 `Member` 목록을 참조하면 끝이 없다.

<br/>

### 세번째) API 스펙이 DB 스키마에 묶인다

컬럼 이름을 바꾸면 API 응답의 키도 바뀐다.

DB를 정리하려는 것뿐인데 앱을 쓰는 사용자까지 영향을 받는다.

<br/>

## JPA 라서 생기는 이유가 하나 더 있다

앞의 DB 접근(JPA) 글에서 본 지연 로딩 문제다.

```java
@Transactional
public Member find(Long id) {
    return memberRepository.findById(id).orElseThrow();
}

// 컨트롤러에서 JSON 으로 바꿀 때
member.getTeam().getName();      // 여기서 예외
```

```java
LazyInitializationException: could not initialize proxy - no Session
```

<br/>

트랜잭션이 끝나면 커넥션이 반납되어서, 지연 로딩이 DB에 물어볼 방법이 없다.

<br/>

앞의 JPA 설정, 적용, 핵심 글에서 본 `준영속 상태` 가 이것이다.

엔티티를 밖으로 내보내는 순간 그 상태가 되어버린다.

<br/>

## 파라미터로 받는 것도 안 되는 이유

원문에서 `파라미터를 받든` 이라고 한 부분이다.

```java
@PostMapping("/members")
public void save(@RequestBody Member member) { ... }
```

<br/>

클라이언트가 보낸 JSON이 그대로 엔티티가 된다.

```json
{ "name": "민석", "id": 999, "grade": "ADMIN" }
```

<br/>

`id` 와 `grade` 까지 클라이언트가 정할 수 있게 되는 것이다.

앞의 클라이언트 서버 구조 글에서 본 `클라이언트가 보낸 값은 조작될 수 있다` 가 그대로 적용된다.

<br/>

앞의 검증 - Form 전송 객체 분리 글에서 본 대로,

받을 것만 담은 별도 클래스를 쓰면 애초에 못 넣는다.

```java
class MemberSaveRequest {
    private String name;      // id 와 grade 가 없다
    private String email;
}
```

<br/>

## 그래서 이렇게 쓴다

```java
@Transactional(readOnly = true)
public MemberResponse find(Long id) {
    Member member = memberRepository.findById(id).orElseThrow();
    return MemberResponse.from(member);      // 트랜잭션 안에서 다 꺼낸다
}
```

<br/>

`from` 안에서 필요한 값을 전부 꺼내니, 밖에 나갈 때는 이미 평범한 객체다.

지연 로딩도 여기서 끝나 있어서 나중에 예외가 안 난다.

<br/>

앞의 트랜잭션 옵션 글에서 본 `readOnly = true` 를 붙인 것도 이유가 있다.

조회인데 실수로 값을 바꿔도 `UPDATE` 가 안 나가게 막아준다.
