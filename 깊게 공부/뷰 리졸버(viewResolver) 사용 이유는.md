## 뷰 리졸버(viewResolver) 사용 이유는?


ViewResolver는 DispatcherServlet에서 `“이런 이름을 가진 View를 줘”` 라고 

요청하면 `DispatcherServlet`에서 전달한 View 이름을 해석한 뒤 View 객체를 리턴 해주는 역할을 합니다.

<br>

스프링에서 데이터를 처리하거나 가지고 왔다면, 이 데이터를 `View의 영역으로 전달`을 해야 한다. 

View를 어떤 것을 사용할지 `자유롭게 설정을` 가능하다.

```
그때, `설정 역할`을 하는 것이 View Resolver라고 생각하면 된다.
```


<br><br>


## 왜 개념을 분리 했는가?

- 논리 뷰 이름 : ‘members’

- 물리 뷰 경로 : ‘/WEB-INF/views/members.jsp’

```java
private static MyView viewResolver(String viewName) {
    return new MyView("/WEB-INF/views/" + viewName + ".jsp");
}
```

만약, 나중에 폴더 이름이 `‘views’` 되어 있는 것이, `‘jsp’`로 바뀌는 것이다.

그러면 어떻게 해야 되는가? → `컨트롤러를 건들 필요가 없다!`

<br>

나는 위 코드인 `viewResolver()` 메서드만 변경하면 되는 것이다.

그리하여 논리적인 이름과 물리적인 이름은 나눠 놓으면, 

변경 사항이 발생 했을때, 컨트롤러 코드는 변경하지 않아도 되는 장점이 있는 것이다.



<br/><br/>


## 입력 화면, 출력 화면

<br/>

클라이언트는 컨트롤 클래스를 호출 시킨다는 것을 명심하자.

그리고 그것을 우리 알맞게 처리해주는 것이다.

![이미지](/programming/img/입문544.PNG)

<br/><br/>

## 흐름 

생성자에 들어가 있는 `‘키’` 값들은 `html`이나 `class` 주소들이 아니다.. (착각하지 말자.)

단지, `‘벨류’` 인 `객체`를 찾기 위해서 임의로 정해 놓은 문자열 일뿐이다.

1. 클라이언트의 모든 요청은 전체 컨트롤러가 요청을 받게 된다.

2. 전체 컨트롤러에서 해당 컨트롤러를 찾아 연결 시켜준다.
3. 해당 컨트롤러가 해당 뷰 페이지로 연결 시켜준다.
4. 클라이언트는 입력값을 입력한다. 그리고 서브밋 한다.
5. 다시 전체 컨트롤러로 와서 해당 URL의 컨트롤러를 찾는다.
6. 찾은 해당 컨트롤러로 가서 저장 시킬 데이터들은 저장 시키고,
7. 해당 뷰 페이지로 이동 된다.




<br/><br/>

>**Reference** <br/>[스프링 MVC 2편 - 백엔드 웹 개발 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2/dashboard)