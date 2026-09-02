## 요청 매핑 - API 예시

<br/>

## 회원 관리 API

`회원 목록 조회:` GET          /users

`회원 등록:`          POST       /users

`회원 조회:`          GET         /users/{userId}

`회원 수정:`          PATCH     /users/{userId}

`회원 삭제:`          DELETE    /users/{userId}

<br/><br/>

## 매핑 테스트 해보기.

REST API 매핑하기 좋다. `(PUT, DELETE 등등 사용)`

```java
@RestController
public class MappingClassController {

    // 회원 목록 조회
    @GetMapping("/mappring/users")
    public String user() {
        return "get users";
    }

    // 회원 등록
    @PostMapping("/mapping/users")
    public String addUser() {
        return "post user";
    }

    // 회원 상세 조회
    @GetMapping("/mapping/users/{userId}")
    public String findUser(@PathVariable String userId) {
        return "get userId=" + userId;
    }

    // 회원 수정
    @PatchMapping("/mapping/users/{userId}")
    public String updateUser(@PathVariable String userId) {
        return "update userId=" + userId;
    }

    // 회원 삭제
    @DeleteMapping("/mapping/users/{userId}")
    public String deleteUser(@PathVariable String userId) {
        return "delete userId=" + userId;
    }
}
```

<br/><br/>

## 회원 목록 조회 매핑

![이미지](/programming/img/입문95.PNG)

<br/><br/>

## 회원 등록 매핑

![이미지](/programming/img/입문96.PNG)

<br/><br/>

## 회원 수정 매핑

![이미지](/programming/img/입문97.PNG)

<br/><br/>

## 회원 수정 매핑

![이미지](/programming/img/입문98.PNG)

<br/><br/>

## 회원 삭제 매핑

![이미지](/programming/img/입문99.PNG)

<br/><br/>

## 위 코드를 개선 해보자면?

이렇게가 될 수 있다.

```java
@RestController
@RequestMapping("/mappring/users")
public class MappingClassController {

    // 회원 목록 조회
    @GetMapping
    public String user() {
        return "get users";
    }

    // 회원 등록
    @PostMapping
    public String addUser() {
        return "post user";
    }

    // 회원 상세 조회
    @GetMapping("/{userId}")
    public String findUser(@PathVariable String userId) {
        return "get userId=" + userId;
    }

    // 회원 수정
    @PatchMapping("/{userId}")
    public String updateUser(@PathVariable String userId) {
        return "update userId=" + userId;
    }

    // 회원 삭제
    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId) {
        return "delete userId=" + userId;
    }
}
```


<br/><br/>

>**Reference** <br/>[스프링 MVC 2편 - 백엔드 웹 개발 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2/dashboard)
<br/>

## 궁금증!

```java
같은 URL 에 메서드만 다르게 두는 게 왜 좋은가
```

URL이 `무엇` 인지만 말하게 되기 때문이다.

```java
GET    /users        회원 목록
POST   /users        회원 등록
GET    /users/{id}   회원 조회
PATCH  /users/{id}   회원 수정
DELETE /users/{id}   회원 삭제
```

<br/>

URL에 동사가 하나도 없다.

<br/>

동사를 URL에 넣으면 이렇게 된다.

```java
/getUserList
/createUser
/getUserById
/updateUser
/deleteUser
```

<br/>

주소가 다섯 개로 늘어난다.

그리고 `수정` 을 `modify` 라고 부를지 `update` 라고 부를지 매번 정해야 한다.

<br/>

메서드를 쓰면 그런 고민이 사라진다. HTTP가 이미 정해놨기 때문이다.

<br/>

## PUT 과 PATCH 를 구분해야 한다

앞의 HTTP 메서드 글에서 본 그 차이다.

```java
PUT   - 통째로 바꾼다. 안 보낸 필드는 사라진다
PATCH - 보낸 것만 바꾼다
```

<br/>

```java
// 원래 상태
{"name":"민석","age":30,"email":"a@b.c"}

// PUT /users/1  {"name":"영한"}
{"name":"영한"}                              <- age, email 이 사라진다

// PATCH /users/1  {"name":"영한"}
{"name":"영한","age":30,"email":"a@b.c"}     <- name 만 바뀐다
```

<br/>

실무에서 `PUT` 이라 적어놓고 `PATCH` 처럼 동작시키는 경우가 많다.

스펙과 다르게 동작하는 것이라 헷갈릴 여지가 생긴다.

<br/>

## 멱등성이 여기서 갈린다

```java
GET    멱등  - 몇 번 불러도 같다
PUT    멱등  - 같은 값으로 통째 교체라 결과가 같다
DELETE 멱등  - 이미 지운 것을 또 지워도 결과는 "없음"
POST   아님  - 부를 때마다 새로 생긴다
PATCH  아님  - 증가시키는 수정이면 부를 때마다 달라진다
```

<br/>

앞의 HTTP 메서드의 속성 글에서 본 대로,

`POST` 만 아닌 게 아니라 `PATCH` 도 멱등이 아니다.

```java
PATCH /users/1  {"point": "+100"}     // 부를 때마다 100 씩 늘어난다
```

<br/>

이게 왜 중요하냐면, 응답이 안 왔을 때 재시도해도 되는지가 여기서 갈리기 때문이다.

<br/>

```java
멱등이면    -> 그냥 다시 보내면 된다
멱등이 아니면 -> 멱등 키를 따로 보내서 서버가 중복을 걸러야 한다
```

<br/>

## 경로 변수를 여러 개 쓸 때

```java
@GetMapping("/users/{userId}/orders/{orderId}")
public Order find(@PathVariable Long userId, @PathVariable Long orderId) { ... }
```

<br/>

계층을 URL로 표현한 것이다.

`이 회원의 이 주문` 이라는 관계가 주소에 드러난다.

<br/>

다만 너무 깊어지면 읽기 힘들어진다.

```java
/users/1/orders/2/items/3/options/4     <- 여기까지 가면 과하다
```

<br/>

보통 두 단계까지만 쓰고 그 아래는 조회 조건으로 뺀다.

```java
/order-items?orderId=2
```

<br/>

## 미디어 타입으로 나누는 경우

```java
@PostMapping(value = "/users", consumes = "application/json")
public User createJson(@RequestBody UserRequest request) { ... }

@PostMapping(value = "/users", consumes = "application/x-www-form-urlencoded")
public String createForm(@ModelAttribute UserForm form) { ... }
```

<br/>

같은 `POST /users` 인데 요청 형식에 따라 다른 메서드로 간다.

<br/>

앞의 @RequestMapping(), HttpServletRequest 글에서 본 `consumes` 가 이걸 한다.

<br/>

앱과 웹 화면이 같은 API를 쓰는데 형식만 다를 때 이렇게 나누기도 한다.

다만 두 벌을 유지해야 하니, 요즘은 JSON 하나로 통일하는 편이다.
