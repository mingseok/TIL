## 스프링 MVC 구조

<br/>

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

>**Reference** <br/>[스프링 MVC 2편 - 백엔드 웹 개발 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2/dashboard)