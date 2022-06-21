## 요청 매핑 - API 예시 (postman 사용)

postman 잘 사용하기.

postman을 사용하기 전에는 인텔리제이 실행 시키기.

![이미지](/programming/img/서49.PNG)

<br/>

## 요청 매핑 - API 예시

회원 관리를 HTTP API로 만든다 생각하고 매핑을 어떻게 하는지 알아보자.

(실제 데이터가 넘어가는 부분은 생략하고 URL 매핑만)

<br/>

## 회원 관리 API

회원 목록 조회: GET /users

회원 등록: POST /users

회원 조회: GET /users/{userId}

회원 수정: PATCH /users/{userId}

회원 삭제: DELETE /users/{userId}


```java
package hello.springmvc.basic.requestmapping;

import org.springframework.web.bind.annotation.*;

@RestController
public class MappingClassController {

    /**
     * 회원 목록 조회:  GET     /users
     * 회원 등록:      POST    /users
     * 회원 상세 조회:  GET     /users/{userId}
     * 회원 수정:      PATCH   /users/{userId}
     * 회원 삭제:      DELETE  /users/{userId}
     */

    // 회원 목록 조회 GET
    @GetMapping("/mapping/users")
    public String user() {
        return "get users";
    }

    // 회원 등록 POST
    @PostMapping("/mapping/users")
    public String addUser() {
        return "post user";
    }

    // 회원 상세 조회 GET
    @GetMapping("/mapping/users/{userId}") // url 적을땐 /mapping/users/userA 이런식으로 적어야 한다.
    public String findUser(@PathVariable String userId) {
        return "get UserId=" + userId;
    }

    // 회원 수정 PATCH
    @PatchMapping("/mapping/users/{userId}")
    public String updateUser(@PathVariable String userId) {
        return "update UserId=" + userId;
    }

    // 회원 삭제 DELETE
    @DeleteMapping("/mapping/users/{userId}")
    public String deleteUser(@PathVariable String userId) {
        return "delete UserId=" + userId;
    }

}
```

<br/>하지만 위에 코드를 보면,

### `/mapping/users` 는 다 똑같다 그러면 어떻게 하면 되냐?

더 코드가 깔끔해 진 것이다.

```java
package hello.springmvc.basic.requestmapping;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mapping/users") // 하면 시작 url은 고정인 것이다.
public class MappingClassController {

    // 회원 목록 조회 GET
    @GetMapping
    public String user() {
        return "get users";
    }

    // 회원 등록 POST
    @PostMapping
    public String addUser() {
        return "post user";
    }

    // 회원 상세 조회 GET
    @GetMapping("/{userId}")
    public String findUser(@PathVariable String userId) {
        return "get UserId=" + userId;
    }

    // 회원 수정 PATCH
    @PatchMapping("/{userId}")
    public String updateUser(@PathVariable String userId) {
        return "update UserId=" + userId;
    }

    // 회원 삭제 DELETE
    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId) {
        return "delete UserId=" + userId;
    }

}
```

<br/>

>**Reference** <br/>스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1
