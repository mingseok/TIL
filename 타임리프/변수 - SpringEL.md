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
<br/>

## 궁금증!

```java
${member.name} 은 어떻게 값을 꺼내나
```

`getName()` 을 부른다. 필드에 직접 접근하는 게 아니다.

```java
${member.name}    ->  member.getName()
${member.isVip}   ->  member.isVip()  또는 member.getIsVip()
```

<br/>

앞의 HTTP 요청 파라미터 - @ModelAttribute 글에서 본 자바빈 규약과 같다.

<br/>

그래서 `getter` 가 없으면 안 나온다.

```java
public class Member {
    private String name;      // getter 가 없다
}
```

```java
EL1008E: Property or field 'name' cannot be found
```

<br/>

앞의 lombok 글에서 본 `@Getter` 를 안 붙였을 때 이 오류가 난다.

<br/>

## 대괄호 표기도 된다

```java
${member['name']}          -- 점 표기와 같다
${map['key']}              -- Map 에서 꺼낸다
${list[0]}                 -- 리스트의 첫 번째
```

<br/>

`Map` 은 점으로도 된다.

```java
${map.key}                 -- map.get("key")
```

<br/>

키에 점이나 하이픈이 들어 있으면 대괄호를 써야 한다.

```java
${map['user-name']}
```

<br/>

## 없는 값을 꺼내면 어떻게 되나

```java
${member.name}             -- member 가 null 이면 예외
${member?.name}            -- member 가 null 이면 null 을 돌려준다
```

<br/>

물음표가 안전 탐색 연산자다.

<br/>

앞의 Optional 글에서 본 것과 같은 발상인데, 문법으로 지원하는 것이다.

<br/>

체인으로 이어도 된다.

```java
${member?.team?.name}
```

<br/>

중간에 하나라도 `null` 이면 전체가 `null` 이 된다.

<br/>

`null` 일 때 다른 값을 보여주려면 붙여 쓴다.

```java
${member?.name} ?: '이름 없음'
```

<br/>

## 유틸 객체가 여럿 있다

```java
${#numbers.formatInteger(price, 3, 'COMMA')}     -- 1,000,000
${#dates.format(date, 'yyyy-MM-dd')}
${#temporals.format(localDate, 'yyyy-MM-dd')}    -- LocalDate 는 이쪽
${#strings.length(name)}
${#strings.isEmpty(name)}
${#lists.size(items)}
```

<br/>

`#dates` 와 `#temporals` 가 나뉘어 있다.

```java
#dates      - java.util.Date
#temporals  - LocalDate, LocalDateTime
```

<br/>

`#temporals` 는 별도 의존관계가 필요하다.

```java
thymeleaf-extras-java8time
```

<br/>

스프링 부트 스타터에 들어 있어서 대개 그냥 된다.

<br/>

## 메서드를 부를 수 있다는 게 양날이다

```java
${member.calculateDiscount(order)}
```

<br/>

화면에서 도메인 메서드를 부를 수 있다.

<br/>

편한데, 화면이 도메인에 직접 붙게 된다.

앞의 타임리프 소개 글에서 본 그 문제다.

<br/>

메서드 안에서 예외가 나면 화면을 그리다가 터진다.

이미 응답이 나가기 시작한 뒤라 오류 페이지로 넘기기도 어렵다.

<br/>

앞의 HttpServletRequest, HttpServletResponse 글에서 본

`본문을 쓰기 시작하면 되돌릴 수 없다` 는 그 문제다.

<br/>

## 정적 메서드도 부를 수 있다

```java
${T(java.lang.Math).random()}
${T(com.example.Grade).valueOf('GOLD')}
```

<br/>

`T()` 안에 클래스 이름을 적으면 정적 멤버에 접근한다.

<br/>

이건 안 쓰는 게 낫다.

화면에서 아무 클래스나 부를 수 있다는 뜻이라, 템플릿이 외부에서 오면 위험하다.

<br/>

## 세 가지 표현식을 구분해야 한다

```java
${...}    변수 표현식.        모델의 값
*{...}    선택 변수 표현식.   th:object 안에서 쓴다
#{...}    메시지 표현식.      properties 파일의 값
@{...}    링크 표현식.        URL
```

<br/>

앞의 스프링 메시지, 국제화 글에서 본 `#{...}` 가 여기 들어 있다.

<br/>

기호가 다 다르니 처음엔 헷갈리는데,

`무엇을 찾는가` 로 나뉜다고 보면 정리가 된다.

```java
$  - 모델에서
*  - 선택된 객체에서
#  - 메시지 파일에서
@  - URL 을 만든다
```
