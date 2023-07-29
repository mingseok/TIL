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
