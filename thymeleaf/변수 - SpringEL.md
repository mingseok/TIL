## 변수 - SpringEL

타임리프에서 변수를 사용할 때는 변수 표현식을 사용한다.

변수 표현식 : ${...}

<br/>

그리고 이 변수 표현식에는 스프링 EL이라는 스프링이 제공하는 표현식을 사용할 수 있다.

java 클래스

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

<br/>

/resources/templates/basic/variable.html

<br/>

${user.username} 이렇게 사용할 수 있고, <br/>

또는 ${user['username']} 도 사용 할 수 있고,

또는 ${user.getUsername()} 도 사용 할 수 있다.

<br/>

### List 로 사용 할때는 

    ${users[0].username} 도 사용 가능 하며, 
    ${users[0]['username']} 도 사용가능 하며,
    ${users[0].getUsername()} 가능 하다.

<br/>

### Map 으로 사용 할때는 이렇게도 가능하다.

    ${userMap['userA'].username}
    ${userMap['userA']['username']}
    ${userMap['userA'].getUsername()}


<br/>

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
    <li>${user.username} = <span th:text="${user.username}"></span></li>
    <li>${user['username']} = <span th:text="${user['username']}"></span></li>
    <li>${user.getUsername()} = <span th:text="${user.getUsername()}"></span></li>
</ul>
<ul>List
    <li>${users[0].username} = <span th:text="${users[0].username}"></span></li>
    <li>${users[0]['username']} = <span th:text="${users[0]['username']}"></span></li>
    <li>${users[0].getUsername()} = <span th:text="${users[0].getUsername()}"></span></li>
</ul>
<ul>Map
    <li>${userMap['userA'].username} = <span th:text="${userMap['userA'].username}"></span></li>
    <li>${userMap['userA']['username']} = <span th:text="${userMap['userA']['username']}"></span></li>
    <li>${userMap['userA'].getUsername()} = <span th:text="${userMap['userA'].getUsername()}"></span></li>
</ul>
</body>
</html>
```
<br/>

실행 시켜 보면

![이미지](/programming/img/겨2.PNG)


<br/>

### 추가로 th:with= 로 지역 변수도 선언 할수 있다.


<br/>

>**Reference** <br/>스프링 MVC 2편 - 백엔드 웹 개발 활용 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2