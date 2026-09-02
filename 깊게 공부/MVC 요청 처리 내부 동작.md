## MVC 요청 처리 내부 동작



![이미지](/programming/img/입문539.PNG)

[그림 참고](/https://velog.io/@solchan/Spring-Spring-MVC%EB%9E%80-%EB%AC%B4%EC%97%87%EC%9D%B8%EA%B0%80)


MVC 패턴은 하나의 `서블릿`이나, `JSP`로 처리하던 것을

`컨트롤러(Controller)`와 `뷰(View)`라는 영역으로 서로 역할을 나눈 것을 말한다.

<br/><br/>

## 웹 애플리케이션은 보통 `MVC 패턴`을 사용한다.

```java
(M)모델: 뷰에 출력할 데이터를 담아둔다. 
         뷰가 필요한 데이터를 모두 모델에 담아서 전달해주는 덕분에 
         뷰는 비즈니스 로직이나 데이터 접근을 몰라도 되고, 
         화면을 렌더링 하는 일에만 집중할 수 있다.

(V)뷰: 모델에 담겨있는 데이터를 사용해서 화면을 그리는 일에 집중한다. 
       여기서는 HTML을 생성하는 부분을 말한다.

(C)컨트롤러: HTTP 요청을 받아서 파라미터를 검증하고, 비즈니스 로직을 실행한다. 
             그리고 뷰에 전달할 결과 데이터를 조회해서 모델에 담는다.
```

- `뷰 템플릿`은 화면 담당을 하고,

- `컨트롤러`는 처리 과정을 담당하고,

- `모델`은 데이터를 관리하는 담당한다.

이렇게 `화면`, `처리`, `데이터 분야`를 각 담당자 별로 나누는 기법을 `MVC`라고 하는 것이다.

<br/><br/>


### 내부 기능들

- `DispatcherServlet`: 클라이언트에게 요청을 받아 응답까지의 MVC 처리과정을 통제한다.

- `HandlerMapping`: 클라이언트의 요청 URL을 어떤 Controller가 처리할지 결정한다.

- `HandlerAdapter`: HandlerMapping에서 결정된 핸들러 정보로 해당 메소드를 직접 호출해주는 역할

- `ViewResolver`: Controller의 처리 결과(데이터)를 생성할 view를 결정한다.

<br/><br/>

## 순서

1. 클라이언트는 URL을 통해 요청을 전송한다.

2. 디스패처 서블릿은 핸들러 매핑을 통해 해당 요청이 어느 컨트롤러에게 온 요청인지 찾는다.

       `("/front-controller/members/new-form", new MemberFormController());`

3. 디스패처 서블릿은 핸들러 어댑터에게 요청의 전달을 맡긴다.

       - `handlerAdapters.add(new ControllerHandlerAdapter());`

4. 핸들러 어댑터는 해당 컨트롤러에 요청을 전달한다.

5. 컨트롤러는 비즈니스 로직을 처리한 후에 반환할 뷰의 이름을 반환한다.

6. 디스패처 서블릿은 뷰 리졸버를 통해 반환할 뷰를 찾는다.

7. 디스패처 서블릿은 컨트롤러에서 뷰에 전달할 데이터를 추가한다.

8. 데이터가 추가된 뷰를 반환한다.


<br/><br/>

## 스프링 MVC의 강점

- DispatcherServlet 코드의 변경 없이, 원하는 기능을 변경하거나 확장할 수 있다는 점이다.

    - 지금까지 설명한 대부분을 확장 가능할 수 있게 인터페이스로 제공한다.

<br/>

이 인터페이스들만 구현해서 DispatcherServlet 에 등록하면 나만의 컨트롤러를 만들 수도 있다.

![이미지](/programming/img/입문79.PNG)

<br/><br/>

## 동작 순서

### 1. `핸들러 조회:` 핸들러 매핑을 통해 요청 URL에 매핑된 핸들러(컨트롤러)를 조회한다.

![이미지](/programming/img/입문80.PNG)

<br/><br/>

### 1-1 핸들러(컨트롤러) 찾는 과정.

![이미지](/programming/img/입문81.PNG)

<br/><br/>


### 2. `핸들러 어댑터 조회:` 핸들러를 실행할 수 있는 핸들러 어댑터를 조회한다.

![이미지](/programming/img/입문82.PNG)

<br/><br/>


### 2-1. 핸들러 어댑터 찾는 과정

![이미지](/programming/img/입문83.PNG)

<br/><br/>

### 3. `핸들러 어댑터 실행:` 핸들러 어댑터를 실행한다.

![이미지](/programming/img/입문84.PNG)


<br/><br/>

### 4. `핸들러 실행:` 핸들러 어댑터가 실제 핸들러를 실행한다.

![이미지](/programming/img/입문85.PNG)

<br/><br/>

### 4-1. 실제 핸들러를 실행하여 `논리 이름` 을 넣은 ModelView 객체를 생성하여 반환 한다.

![이미지](/programming/img/입문86.PNG)


<br/><br/>


### 참고) ModelView 클래스

![이미지](/programming/img/입문87.PNG)

<br/><br/>


### 참고) createParamMap() 메서드

![이미지](/programming/img/입문88.PNG)


<br/><br/>

### 5. `ModelView 반환:` 

핸들러 어댑터는 `논리 이름` 을 넣은 `ModelView` 객체를 프론트 컨트롤러에 리턴 한다. (4번 사진 동일)


<br/><br/>

### 6. `viewResolver 호출:` `ModelView` 객체에서 논리 이름을 가져 온다. 


<br/>

### 6-1. 그리고 뷰 리졸버 메서드를 실행한다.

![이미지](/programming/img/입문89.PNG)


<br/><br/>

### 7. `View 반환:` 

뷰 리졸버는 뷰(html)의 논리 이름을 물리 이름으로 바꾸고, 
MyView객체를 생성하여 MyView에 물리 이름을 저장시킨다.

![이미지](/programming/img/입문90.PNG)


<br/><br/>


### 8. `뷰 렌더링:` MyView객체를 통해서 render() 메서드를 실행시킨다.

![이미지](/programming/img/입문91.PNG)


<br/><br/>


### 9. 물리 이름인 HTML로 전송(이동) 하게 되는 것이다.

![이미지](/programming/img/입문92.PNG)





<br/><br/>


### mvc 코드 작성해보기.

https://github.com/mingseok/SpringMvc




<br/><br/>

>**Reference** <br/>[스프링 MVC 2편 - 백엔드 웹 개발 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2/dashboard)


<br/>

## 궁금증!

```java
MVC 로 나누기 전에는 어떻게 짰길래 나누게 된 걸까?
```

`JSP` 하나에 전부 들어 있었다.

```java
<%
    MemberRepository repository = MemberRepository.getInstance();
    List<Member> members = repository.findAll();          // 데이터 조회
    if (session.getAttribute("loginMember") == null) {    // 로그인 확인
        response.sendRedirect("/login");
        return;
    }
%>
<html>
<body>
    <table>
    <% for (Member member : members) { %>
        <tr><td><%= member.getName() %></td></tr>
    <% } %>
    </table>
</body>
</html>
```

<br/>

한 파일 안에 DB 조회, 로그인 검사, HTML 생성이 다 섞여 있다.

<br/>

여기서 세 가지 문제가 생긴다.

- 화면 디자인만 바꾸려는데 비즈니스 로직 코드를 헤집고 다녀야 한다

- 비즈니스 로직만 고치려는데 HTML 사이에서 찾아야 한다
- 로직을 테스트하려면 JSP를 실행해야 한다. 톰캣을 띄워야 한다

<br/>

특히 마지막이 크다. 로직이 화면에 묶여 있으면 단위 테스트를 쓸 수가 없다.

<br/>

## 그래서 변경 이유가 다른 것을 나눈 것이다

```java
컨트롤러 - 요청이 바뀌면 바뀐다 (새 파라미터, 새 검증)
모델     - 화면에 필요한 데이터가 바뀌면 바뀐다
뷰       - 디자인이 바뀌면 바뀐다
```

<br/>

세 가지가 각각 다른 이유로 바뀐다. 그래서 나눠두면 하나를 고칠 때 나머지를 안 건드린다.

<br/>

앞의 클래스와 메소드 글에서 본 `단일 책임` 이 파일 단위로 적용된 것이다.

<br/>

## Model 이 왜 따로 필요한가

컨트롤러가 뷰를 직접 부르면 되지 않나 싶은데, 그러면 뷰가 컨트롤러를 알게 된다.

```java
// Model 없이
return new MembersView(members, loginMember, pageInfo);   // 뷰가 파라미터를 정한다
```

<br/>

`Model` 은 그 사이에 낀 `이름표 붙은 바구니` 다.

```java
model.addAttribute("members", members);
model.addAttribute("loginMember", member);
return "members";
```

<br/>

컨트롤러는 담기만 하고, 뷰는 꺼내기만 한다. 서로의 존재를 모른다.

<br/>

앞의 뷰 리졸버 글에서 본 `논리 이름과 물리 경로를 나눈다` 와 같은 구조다.

`이름으로 주고받으면 양쪽이 서로를 몰라도 된다` 는 방식이 여기서도 쓰인 것이다.

<br/>

## 컨트롤러에 비즈니스 로직을 넣으면 안 되는 이유

MVC의 `C` 를 오해해서 컨트롤러에 로직을 다 넣는 경우가 많다.

```java
@PostMapping("/orders")
public String order(@ModelAttribute OrderForm form) {
    Item item = itemRepository.findById(form.getItemId());

    if (item.getStock() < form.getCount()) {              // 재고 확인
        throw new IllegalStateException("재고 부족");
    }
    item.setStock(item.getStock() - form.getCount());     // 재고 차감

    int price = item.getPrice() * form.getCount();
    if (member.getGrade() == VIP) price = price * 90 / 100;   // 할인 계산

    orderRepository.save(new Order(...));
    return "redirect:/orders";
}
```

<br/>

이러면 처음 문제로 돌아간다. 로직을 테스트하려면 `HttpServletRequest` 를 흉내내야 한다.

<br/>

그래서 실제로는 한 겹 더 나눈다.

```java
Controller - HTTP 를 자바로 번역한다. 파라미터를 받고, 결과를 모델에 담는다
Service    - 비즈니스 로직. HTTP 를 모른다
Repository - 데이터 접근. 비즈니스 로직을 모른다
```

<br/>

`Service` 가 `HttpServletRequest` 를 모르기 때문에, 앞의 DI 글에서처럼

컨테이너 없이 `new` 해서 단위 테스트를 쓸 수 있게 된다.

```java
MVC 는 화면과 로직을 나눈 것
계층 구조는 그 로직을 다시 "웹" 과 "업무" 로 나눈 것
```

<br/>

## 요즘은 뷰가 서버 밖으로 나갔다

`React` 나 `Vue` 를 쓰면 서버가 HTML을 안 만든다.

```java
서버 MVC  : 컨트롤러 -> 모델 -> 뷰(JSP/타임리프) -> 완성된 HTML 을 내려준다
API + SPA : 컨트롤러 -> JSON 을 내려준다 -> 브라우저의 자바스크립트가 화면을 만든다
```

<br/>

이 경우 서버에는 `V` 가 없다. `@RestController` 가 그 형태다.

앞의 `@ResponseBody` 글에서 본 것처럼 뷰 리졸버를 아예 안 탄다.

<br/>

그래도 `MVC` 라는 발상 자체는 브라우저 쪽으로 옮겨갔을 뿐 사라지지 않았다.

`React` 의 컴포넌트도 결국 `상태(모델)` 와 `화면(뷰)` 을 나누는 구조다.
