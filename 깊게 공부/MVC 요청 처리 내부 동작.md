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

