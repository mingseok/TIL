## 변수EL

타임리프에서 변수를 사용할 때는 변수 표현식을 사용한다.

즉, 스프링이 내부에서 객체를 접근하는 문법이다.

```
변수 표현식 : ${...}
```

<br/>


## 컨트롤러

```java
package hello.thymeleaf.basic;

@Controller
@RequestMapping("/basic")
public class BasicController {

    @GetMapping("/variable")
    public String variable(Model model) {
        User userA = new User("userA", 10);
        User userB = new User("userB", 20);

        List<User> list = new ArrayList<>();
        list.add(userA);
        list.add(userB);

        Map<String, User> map = new HashMap<>();
        map.put("userA", userA);
        map.put("userB", userB);

        model.addAttribute("user", userA);
        model.addAttribute("users", list);
        model.addAttribute("userMap", map);

        return "basic/variable";
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


<br/><br/>



## HTML 파일

각각 3가지씩 묶은 표현 방식들이 같은 기능들을 수행한다.


```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<h1>SpringEL 표현식</h1>
<ul>Object
    <li><span th:text="${user.username}"></span></li>
    <li><span th:text="${user['username']}"></span></li>
    <li><span th:text="${user.getUsername()}"></span></li>
</ul>
<ul>List
    <li><span th:text="${users[0].username}"></span></li>
    <li><span th:text="${users[0]['username']}"></span></li>
    <li><span th:text="${users[0].getUsername()}"></span></li>
</ul>
<ul>Map
    <li><span th:text="${userMap['userA'].username}"></span></li>
    <li><span th:text="${userMap['userA']['username']}"></span></li>
    <li><span th:text="${userMap['userA'].getUsername()}"></span></li>
</ul>
</body>
</html>
```


<br/><br/>


## 실행 시켜 보면

![이미지](/programming/img/겨2.PNG)




<br/>

>**Reference** <br/>스프링 MVC 2편 - 백엔드 웹 개발 활용 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2