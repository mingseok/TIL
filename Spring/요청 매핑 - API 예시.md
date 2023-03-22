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