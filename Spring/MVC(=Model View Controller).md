## MVC(=Model View Controller)

<br/>

MVC 패턴은 하나의 `서블릿`이나, `JSP`로 처리하던 것을 

`컨트롤러(Controller)`와 `뷰(View)`라는 영역으로 서로 역할을 나눈 것을 말한다. 



<br/>

웹 애플리케이션은 보통 `MVC 패턴`을 사용한다.

```
컨트롤러: HTTP 요청을 받아서 파라미터를 검증하고, 비즈니스 로직을 실행한다. 
          그리고 뷰에 전달할 결과 데이터를 조회해서 모델에 담는다.

모델: 뷰에 출력할 데이터를 담아둔다. 
      뷰가 필요한 데이터를 모두 모델에 담아서 전달해주는 덕분에 
      뷰는 비즈니스 로직이나 데이터 접근을 몰라도 되고, 
      화면을 렌더링 하는 일에만 집중할 수 있다.

뷰: 모델에 담겨있는 데이터를 사용해서 화면을 그리는 일에 집중한다. 
    여기서는 HTML을 생성하는 부분을 말한다.
```

`뷰 템플릿`은 화면 담당을 하고, `컨트롤러`는 처리 과정을 담당, `모델`은 데이터를 관리하는 담당한다. 

이렇게 `화면`, `처리`, `데이터 분야`를 각 담당자 별로 나누는 기법을 `MVC`라고 하는 것이다.

<br/><br/>

## 이렇게 나눌 수 있다.

- 컨트롤러에 비즈니스 로직을 둘 수도 있지만, 이렇게 되면 컨트롤러가 너무 많은 역할을 담당한다
    
- 그리하여 일반적으로 비즈니스 로직은 서비스라는 계층을 별도로 만들어서 처리한다.

![이미지](/programming/img/입문74.PNG)


<br/><br/>

>**Reference** <br/>[스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1)
<br/>

## 궁금증!

```java
왜 굳이 셋으로 나눴을까
```

바뀌는 속도가 다르기 때문이다.

```java
View       - 자주 바뀐다. 디자인 개편, 문구 수정
Controller - 가끔 바뀐다. 화면이 늘어날 때
Model      - 잘 안 바뀐다. 도메인 규칙
```

<br/>

앞의 JSP, 서블릿과 JSP의 한계 글에서 본 문제가 이거다.

하나의 파일에 다 있으면, 디자인만 고치는데 비즈니스 로직 파일을 열어야 한다.

<br/>

```java
<%
    MemberRepository repository = MemberRepository.getInstance();
    List<Member> members = repository.findAll();
%>
<html>
<body>
    <% for (Member member : members) { %>
        <tr><td><%=member.getName()%></td></tr>
    <% } %>
</body>
</html>
```

<br/>

디자이너가 `<td>` 하나 옮기려다 `repository.findAll()` 을 지울 수도 있는 구조인 것이다.

<br/>

## Model 이 두 가지 뜻으로 쓰인다

여기서 자주 헷갈린다.

```java
MVC 의 M  - 도메인 모델. 회원, 주문, 상품 같은 업무 개념
스프링 Model - 컨트롤러가 뷰에 값을 넘기는 통

@GetMapping("/members")
public String list(Model model) {         // 이 Model 은 값 전달용 통
    model.addAttribute("members", ...);
    return "members";
}
```

<br/>

앞의 @Get매핑, Model 글에서 본 그 `Model` 은 후자다.

MVC 이론에서 말하는 Model 과 이름만 같고 다른 것이다.

<br/>

## 컨트롤러가 뚱뚱해지는 문제

```java
@PostMapping("/orders")
public String order(OrderForm form) {
    Member member = memberRepository.findById(form.getMemberId());
    if (member.getGrade() == Grade.VIP) {
        discount = price * 0.1;              // 여기에 규칙이 들어와 있다
    }
    ...
}
```

<br/>

MVC로 나눠도 컨트롤러에 로직이 쌓이면 원래 문제로 돌아간다.

<br/>

그래서 보통 한 층을 더 나눈다.

```java
Controller - 요청 받고 응답 만들기만
Service    - 업무 흐름
Domain     - 규칙 자체
Repository - 저장소 접근
```

<br/>

앞의 SOLID 글에서 본 단일 책임 원칙을 층에 적용한 것이다.

<br/>

## Model 2 라는 말

옛날에 부르던 이름이다.

```java
Model 1 - JSP 가 전부 다 한다. 요청 처리 + 화면
Model 2 - 서블릿이 처리하고 JSP 는 화면만. = MVC
```

<br/>

지금 `MVC` 라고 부르는 것이 그때의 Model 2다.

<br/>

앞의 Front Controller 글에서 본 흐름이 여기서 이어진다.

```java
Model 1  -> Model 2 (MVC)  -> Front Controller  -> 스프링 MVC
```

<br/>

한 번에 지금 모습이 된 게 아니라, 불편한 것을 하나씩 걷어내며 온 것이다.

<br/>

## 웹이 아닌 곳에도 같은 구조가 있다

```java
안드로이드   - MVVM
아이폰       - MVC (초기) 
리액트       - 컴포넌트 + 상태
```

<br/>

이름은 달라도 `화면` 과 `데이터` 와 `그 사이를 잇는 것` 으로 나누는 것은 같다.

<br/>

바뀌는 속도가 다른 것을 같은 파일에 두면 안 된다는 것이

여러 곳에서 각자 발견한 결론인 셈이다.
