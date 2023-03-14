## 자바스크립트 인라인

<br/>

## 자바스크립트 내추럴 템플릿

타임리프는 HTML 파일을 직접 열어도 동작하는 내추럴 템플릿 기능을 제공한다. 

자바스크립트 인라인 기능을 사용하면 주석을 활용해서 이 기능을 사용할 수 있다

```
인라인 사용 전 출력 `var username2 = /userA/ "test username";`

인라인 사용 후 출력 `var username2 = "userA";`
```

인라인 사용 전 결과를 보면 정말 순수하게 그대로 해석을 해버렸다. 

<br/>따라서 내추럴 템플릿 기능이 동작하지 않고, 심지어 렌더링 내용이 주석처리 되어 버린다

인라인 사용 후 결과를 보면 주석 부분이 제거되고, 기대한 "userA"가 정확하게 적용된다.

<br/><br/>

## 객체

타임리프의 자바스크립트 인라인 기능을 사용하면 객체를 JSON으로 자동으로 변환해준다.



```html
var user = [[${user}]];
```

인라인 사용 전 출력 `var user = BasicController.User(username=userA, age=10);`

인라인 사용 후 출력 `var user = {"username":"userA","age":10};`

<br/><br/>

## 컨트롤러

```java
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

<br/>
<br/> 

## html 파일.



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


<br/><br/>


>**Reference** <br/>[스프링 MVC 2편 - 백엔드 웹 개발 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2)