## 자바스크립트 인라인

텍스트 렌더링

```html
var username = [[${user.username}]];
```

인라인 사용 전 출력 `var username = userA;`

인라인 사용 후 출력 `var username = "userA";`

인라인 사용 전 렌더링 결과를 보면 userA 라는 변수 이름이 그대로 남아있다. 

<br/>타임리프 입장에서는 정확하게 렌더링 한 것이지만 아마 개발자가 기대한 것은 다음과 같은 

"userA"라는 문자일 것이다.

결과적으로 userA가 변수명으로 사용되어서 자바스크립트 오류가 발생한다. 

<br/>다음으로 나오는 숫자 age의 경우에는 " 가 필요 없기 때문에 정상 렌더링 된다.

인라인 사용 후 렌더링 결과를 보면 문자 타입인 경우 " 를 포함해준다. 

추가로 자바스크립트에서 문제가 될 수 있는 문자가 포함되어 있으면 이스케이프 처리도 해준다.

<br/>

## 자바스크립트 내추럴 템플릿

타임리프는 HTML 파일을 직접 열어도 동작하는 내추럴 템플릿 기능을 제공한다. 

자바스크립트 인라인 기능을 사용하면 주석을 활용해서 이 기능을 사용할 수 있다

```html
var username2 = /[[${user.username}]]/ "test username";
```

인라인 사용 전 출력 `var username2 = /userA/ "test username";`

인라인 사용 후 출력 `var username2 = "userA";`

인라인 사용 전 결과를 보면 정말 순수하게 그대로 해석을 해버렸다. 

<br/>따라서 내추럴 템플릿 기능이 동작하지 않고, 심지어 렌더링 내용이 주석처리 되어 버린다

인라인 사용 후 결과를 보면 주석 부분이 제거되고, 기대한 "userA"가 정확하게 적용된다.

<br/>

## 객체

타임리프의 자바스크립트 인라인 기능을 사용하면 객체를 JSON으로 자동으로 변환해준다.



```html
var user = [[${user}]];
```

인라인 사용 전 출력 `var user = BasicController.User(username=userA, age=10);`

인라인 사용 후 출력 `var user = {"username":"userA","age":10};`

<br/>

자바 클래스.
```java
package hello.thymeleaf.basic;

import lombok.Data;
import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/basic")
public class BasicController {
    
    @GetMapping("/javascript")
    public String javascript(Model model) {

        model.addAttribute("user", new User("UserA", 10));
        addUsers(model);

        return "basic/javascript";
    }

    
    private void addUsers(Model model) {
        List<User> list = new ArrayList<>();
        list.add(new User("userA", 10));
        list.add(new User("userB", 20));
        list.add(new User("userC", 30));

        model.addAttribute("users", list);
    }
    
    
    @Data
    static class User {
        private String username;
        private int age;

        public User(String username, int age) {
            this.username = username;
            this.age = age;
        }
    }
}
```


<br/> html 파일.



```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>

<!-- 자바스크립트 인라인 사용 전 -->
<script>
    var username = [[${user.username}]];
    var age = [[${user.age}]];
    //자바스크립트 내추럴 템플릿
    var username2 = /*[[${user.username}]]*/ "test username";
    //객체
    var user = [[${user}]];
</script>

<!-- 자바스크립트 인라인 사용 후 -->
<script th:inline="javascript">

    //"UserA" 가 변수에 잘 들어간다
    var username = [[${user.username}]];

    //'10' 이 변수에 잘 들어간다.
    var age = [[${user.age}]];

    //자바스크립트 내추럴 템플릿
    var username2 = /*[[${user.username}]]*/ "test username";
    
    //객체를 제이슨으로 넣어준다.
    var user = [[${user}]];
</script>

</body>
</html>
```


<br/>

>**Reference** <br/>스프링 MVC 2편 - 백엔드 웹 개발 활용 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2