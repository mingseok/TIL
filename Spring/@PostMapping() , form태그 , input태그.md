## @PostMapping() ,form태그 ,input태그

<br/>

## input태그

```html
<input type="text" id="name" name="name" placeholder="이름을 입력하세요">
```

`name="name"` 부분이 중요하다. `"name"` 부분이 `‘키’` 이다.

<br/>

그리고 `‘벨류’` 는 입력창에 입력할 수 있는 `‘민석’` 이 `‘벨류'`이다.

![이미지](/programming/img/입문16.PNG)


<br/>

### 주의사항

`name`을 설정해줘야 컨트롤러로 넘어가는 것인데,

포인트는 `name`을 `DTO`의 `필드변수`명이랑 동일하게 작성해야 된다는 것이다.


<br/><br/>

## form태그

폼 태그에서 서버로 뿌려 지는 것이다. `‘action’` 이랑 `‘method’` 부분을 잘 보자.

```html
<form action="/members/new" method="post">
    <div class="form-group">
        <label for="name">이름</label>
        <input type="text" id="name" name="name" placeholder="이름을 입력하세요">
    </div>
    <button type="submit">등록</button>
</form>
```

<br/>

### 위 form태그를 실행 시키면 이렇게 출력 될 것이다.

1. 이름을 `‘spring’` 이라고 작성하고 ‘등록’ 버튼을 누르면 
    
    ![이미지](/programming/img/입문17.PNG)
    
2. `action` 인 → `"/members/new"` 로 이동하게 되는 것이다. 



3. 그리고 `method`는 → `post` 로 등록 되어 있기 때문에 
4. 컨트롤러 클래스에 `@PostMapping(/members/new)` 어너테이션이 있는 곳으로 간다.
5. 다시 말해, 경로가 일치한 곳으로 `<input>`태그의 
6. 입력값인 `‘spring’`이 서버로 넘어가게 되는 것이다.

<br/>

![이미지](/programming/img/입문18.PNG)


<br/>

```
- url창에다가 엔터 치는것은 Get매핑이다. 주로 조회할때 사용한다.

- Post매핑은 데이터를 <form>에 넣어서 전달하는 방식. 
  보통 데이터를 등록할때 post를 사용한다.
```

<br/><br/>

## 서버로 넘어온 ‘spring’ 입력값은 어디에 저장되나?

위 코드 사진에 매개변수를 보면, `(MemberForm form)` 되어 있다.

즉, `MemberForm.class` 에 `‘spring’` 이 저장 되는 것이다.

(MemberForm.class 코드는 밑에 있다.)

<br/>

알아둘 것은, `MemberForm.class`에 저장 하려면 

`MemberForm` 클래스의 `setName()` 에 접근 해야 하는데, 그렇게 하지 않았다.

<br/>

이유는, 스프링이 알아서 해줬기 때문이다. 그리하여 우리는 `getName()`으로 꺼내기만 하면 된다.

```java
@Controller
public class MemberController {

    @PostMapping("/members/new")
    public String create(MemberForm form) {
        Member member = new Member();
        member.setName(form.getName());

        memberService.join(member);

        return "redirect:/";
    }
}
```

<br/>

### MemberForm 클래스

```java
public class MemberForm {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

<br/><br/>

## sout 으로 확인해보기.

```java
@PostMapping("/members/new")
public String create(MemberForm form) {
    Member member = new Member();
    member.setName(form.getName());

    memberService.join(member);

    System.out.println("member = " + member.getName());

    return "redirect:/";
}
```

<br/>

### ‘민석’이라고 입력하면 콘솔창에 출력 되는 걸 알 수 있다.

![이미지](/programming/img/입문19.PNG)

<br/><br/>

![이미지](/programming/img/입문20.PNG)


<br/><br/>


>**Reference** <br/>[스프링 핵심 원리 - 기본편](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8?utm_source=google&utm_medium=cpc&utm_campaign=04.general_backend&utm_content=spring&utm_term=%EC%8A%A4%ED%94%84%EB%A7%81%20%EC%9E%85%EB%AC%B8&gclid=CjwKCAiAjPyfBhBMEiwAB2CCImohok2YrQ2tRdhqfr3cZvKqkIJOHUJ36u6s1-7C9X1gzZIapTvOtxoCangQAvD_BwE)

<br/>

## 궁금증!

```java
form 태그가 실제로 무엇을 보내는지 찍어봤다
```

앞의 HTTP 요청 데이터 글에서 만들었던 방식으로 서버를 띄우고 확인했다.

<br/>

```java
$ curl -s -X POST http://localhost:18081/echo \
       -H "Content-Type: application/x-www-form-urlencoded" \
       --data-urlencode "name=민석" -d "age=30"
```

```java
[메서드] POST
[Content-Type] application/x-www-form-urlencoded
[본문] name=%EB%AF%BC%EC%84%9D&age=30
```

<br/>

한글이 `%EB%AF%BC%EC%84%9D` 로 바뀌어 나간다.

앞의 URI 와 웹 브라우저 요청 흐름 글에서 본 퍼센트 인코딩이다.

<br/>

브라우저가 폼을 보낼 때 이 변환을 알아서 해준다.

`curl` 로는 `--data-urlencode` 를 써야 같은 모양이 된다.

```java
$ curl -s -X POST ... -d "name=민석&age=30"

[본문] name=민석&age=30       <- 이건 변환을 안 한 것
```

<br/>

`민석` 을 UTF-8 로 인코딩하면 `EB AF BC EC 84 9D` 여섯 바이트인데,

그 바이트를 하나씩 `%XX` 로 적은 것이 앞의 결과다.

앞의 String 은 어떻게 저장될까 글에서 본 그 바이트가 여기서 다시 나온다.

<br/>

## GET 과 POST 에서 form 이 하는 일이 다르다

```java
<form method="get" action="/search">
   -> /search?keyword=값       URL 에 붙는다

<form method="post" action="/members">
   -> 본문에 keyword=값        본문에 실린다
```

<br/>

붙는 자리만 다르고 형식은 똑같다.

그래서 스프링에서는 둘 다 `@RequestParam` 으로 받는다.

<br/>

앞의 HTTP 요청 파라미터 - @RequestParam 글에서 본 대로,

`request.getParameter()` 가 두 곳을 다 뒤지기 때문이다.

<br/>

## input 의 name 이 키가 된다

```java
<input type="text" name="username" value="민석">
```

<br/>

`id` 가 아니라 `name` 이다. 여기서 자주 실수한다.

```java
<input type="text" id="username">      <- 서버로 안 간다
```

<br/>

`id` 는 자바스크립트와 `<label>` 이 쓰는 것이고,

`name` 이 있어야 폼 전송에 포함된다.

<br/>

## 값이 안 오는 경우들

체크박스가 대표적이다.

```java
<input type="checkbox" name="open" value="true">
```

<br/>

체크하면 `open=true` 가 가고, 체크를 풀면 **아예 안 간다.**

`open=false` 가 아니라 키 자체가 없는 것이다.

<br/>

그래서 `수정` 화면에서 체크를 풀어도 값이 안 바뀌는 문제가 생긴다.

```java
서버는 open 이 안 왔으니 "값이 없다" 로 보고 기존 값을 유지한다
```

<br/>

타임리프의 `th:field` 가 이걸 해결한다.

숨은 필드를 같이 만들어주기 때문이다.

```java
<input type="checkbox" name="open" value="true">
<input type="hidden" name="_open" value="on">      <- 이게 자동으로 생긴다
```

<br/>

스프링이 `_open` 은 왔는데 `open` 이 안 왔으면 `false` 로 판단한다.

<br/>

## 파일을 보낼 때는 형식이 바뀐다

```java
<form method="post" enctype="multipart/form-data">
```

<br/>

앞의 클라이언트에서 서버로 데이터 전송 글에서 본 그 형식이다.

```java
Content-Type: multipart/form-data; boundary=----WebKitFormBoundary

------WebKitFormBoundary
Content-Disposition: form-data; name="username"

민석
------WebKitFormBoundary
Content-Disposition: form-data; name="file"; filename="a.png"
Content-Type: image/png

(바이너리)
------WebKitFormBoundary--
```

<br/>

경계 문자열로 여러 조각을 나눠서 보낸다.

파일과 일반 값을 한 요청에 같이 담을 수 있는 이유다.

<br/>

받을 때는 `MultipartFile` 을 쓴다.

```java
@PostMapping("/upload")
public String upload(@RequestParam String username,
                     @RequestParam MultipartFile file) { ... }
```

<br/>

## form 은 GET 과 POST 만 된다

```java
<form method="delete">     <- 안 된다. GET 으로 처리된다
```

<br/>

HTML 스펙이 그렇다.

<br/>

그래서 숨은 필드로 흉내내는 방법이 있다.

```java
<form method="post">
    <input type="hidden" name="_method" value="delete">
</form>
```

<br/>

`HiddenHttpMethodFilter` 가 이 값을 보고 `DELETE` 로 바꿔준다.

앞의 필터, 스프링 인터셉터 글에서 본 필터의 활용 예다.

<br/>

요즘은 화면에서 자바스크립트로 요청을 보내는 경우가 많아 이 방식은 잘 안 쓴다.

`fetch` 로는 어떤 메서드든 보낼 수 있기 때문이다.
