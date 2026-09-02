## HTTP 메서드 - PUT, PATCH, DELETE

PUT 은 완전히 덮을 수 있을 자신이 있을 때 사용한다.


<br/>

## e.g.) 게시판에 게시글을 수정한다 할때 사용한다.

![이미지](/programming/img/입문599.PNG)

- 리소스가 있으면 대체

- 리소스가 없으면 생성

- 중요) 클라이언트가 리소스를 식벽한다는 것이다.

    - 즉, 리소스 위치를 알고 URL을 지정한다는 것이다.

        - POST와의 차이점이다

<br/><br/>

## e.g.) PUT 동작

![이미지](/programming/img/입문600.PNG)

<br/>

서버쪽에 있던 기본의 데이터는 없어지고, 새로운 데이터로 대체가 되어 버리는 것이다.

![이미지](/programming/img/입문601.PNG)

<br/><br/>

## 그렇다면 리소스가 없으면?

신규 리소스가 들어가게 되는 것이다.

![이미지](/programming/img/입문602.PNG)

<br/><br/>

## 중요한 것은

username 필드가 없을 경우다. → `그럼 어떻게 되는가?`

![이미지](/programming/img/입문603.PNG)

<br/><br/>

## username 필드는 삭제되는 것이다.

![이미지](/programming/img/입문604.PNG)


<br/><br/>


## 그리하여 부분 변경하고 싶을때는 `PATCH` 를 사용하자.

위와 같이, `username` 필드를 빼고 요청을 보낸다면?

![이미지](/programming/img/입문605.PNG)

<br/>

### 이렇게 `age만 50으로 변경하는 것이다.`

![이미지](/programming/img/입문606.PNG)

<br/><br/>

## DELETE

![이미지](/programming/img/입문607.PNG)

<br/>

이렇게 삭제 되는 것이다.

![이미지](/programming/img/입문608.PNG)

<br/><br/>

## 정리

`PUT` : 완전히 대체 한다.

`PATCH` : 부분 변경할때 사용한다.

`DELETE` : 삭제 할때 사용한다.
<br/>

## 궁금증!

```java
PUT 이 "덮어쓴다" 는 게 실무에서 어떤 사고를 부를까?
```

빠뜨린 필드가 `null` 이 되는 것이다.

```java
// 현재 상태
{ "id": 1, "name": "민석", "email": "a@test.com", "phone": "010-1111" }

// 이름만 바꾸려고 PUT 을 보낸다
PUT /members/1
{ "name": "바뀐이름" }

// 결과
{ "id": 1, "name": "바뀐이름", "email": null, "phone": null }
```

<br/>

`PUT` 은 `이 자원을 이것으로 대체하라` 는 뜻이라, 안 보낸 필드는 없어지는 것이 맞다.

<br/>

앞의 스프링 데이터 JPA 글에서 본 `merge` 문제와 정확히 같은 모양이다.

```java
Member member = new Member();
member.setId(1L);
member.setName("바뀐이름");
memberRepository.save(member);      // email 이 null 로 덮어써진다
```

<br/>

`PUT` 과 `merge` 가 같은 실수를 부르는 것이다.

<br/>

## 그래서 부분 수정에는 PATCH 를 쓴다

```java
PATCH /members/1
{ "name": "바뀐이름" }

// 결과
{ "id": 1, "name": "바뀐이름", "email": "a@test.com", "phone": "010-1111" }
```

<br/>

보낸 것만 바꾸고 나머지는 그대로 둔다.

<br/>

앞의 스프링 데이터 JPA 글에서 본 해법도 같은 발상이다.

```java
@Transactional
public void update(Long id, String name) {
    Member member = memberRepository.findById(id).orElseThrow();
    member.changeName(name);       // 이 필드만 바뀐다
}
```

<br/>

조회해서 필요한 것만 바꾸는 것이 `PATCH` 의 동작과 같다.

<br/>

## 그런데 PATCH 를 안 쓰는 곳도 많다

이유가 몇 가지 있다.

```java
1. 어떤 형식으로 보낼지 규격이 여러 개다 (JSON Merge Patch, JSON Patch)
2. null 을 "안 보냈다" 와 "null 로 바꿔달라" 로 구분하기 어렵다
3. 오래된 프록시나 방화벽이 PATCH 를 막는 경우가 있다
```

<br/>

`2번` 이 특히 골치다.

```java
PATCH /members/1
{ "phone": null }
```

<br/>

`전화번호를 지워달라` 는 뜻인지, `전화번호는 안 건드린다` 는 뜻인지 구분이 안 된다.

<br/>

자바에서 이걸 다루려면 `Optional` 을 한 겹 더 써야 한다.

```java
class MemberPatchRequest {
    private Optional<String> phone;    // null = 안 보냄, Optional.empty() = 지워달라
}
```

<br/>

번거로워서 실무에서는 대개 이렇게 우회한다.

```java
POST /members/1/change-phone     -- 동작을 명시한 별도 엔드포인트
```

<br/>

앞의 리소스 식별 글에서 `리소스와 행위를 분리하자` 고 했는데,

부분 수정이 복잡할 때는 오히려 행위를 드러내는 쪽이 명확한 경우가 있다.

<br/>

## PUT 으로 새로 만들 수도 있다

원문의 `리소스가 없으면` 부분이다.

```java
PUT /members/1
```

<br/>

`1번` 이 없으면 만들고, 있으면 덮어쓴다.

<br/>

`POST` 와의 차이가 여기서 나온다.

```java
POST /members       - 서버가 id 를 정한다. 두 번 보내면 두 개 생긴다
PUT /members/1      - 클라이언트가 id 를 정한다. 두 번 보내도 하나다
```

<br/>

앞의 멱등 글에서 본 그 차이다.

`PUT` 이 멱등할 수 있는 이유가 `주소를 클라이언트가 정하기 때문` 이다.

<br/>

## DELETE 응답에 무엇을 담을까

```java
204 No Content    - 지웠고 돌려줄 것이 없다 (가장 흔하다)
200 OK            - 지웠고 지운 내용을 돌려준다
202 Accepted      - 지우기로 접수했다 (실제 삭제는 나중에)
```

<br/>

앞의 상태 코드 글에서 본 대로 `204` 에는 본문을 넣으면 안 된다.

<br/>

없는 것을 지우라고 하면 어떻게 할지도 정해야 한다.

```java
404 를 준다  - 정직하다. 다만 앞의 4xx 글에서 본 대로 존재 여부가 새어 나간다
204 를 준다  - 멱등하게 보인다. 두 번째 요청도 성공한 것처럼 처리된다
```

<br/>

재시도를 고려하면 `204` 쪽이 다루기 편하다.

`결과적으로 없으면 된 것 아니냐` 는 관점이다.
