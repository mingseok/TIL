## View 분리 

### 단순 반복 되는 뷰 로직 분리

모든 컨트롤러에서 뷰로 이동하는 부분에 중복이 있고, 깔끔하지 않다.

```java
String viewPath = "/WEB-INF/views/new-form.jsp";
RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
dispatcher.forward(request, response);
```

<br/>

이 부분을 깔끔하게 분리하기 위해 별도로 ‘뷰’ 를 처리하는 객체를 만들자.

![이미지](/programming/img/서35.PNG)

<br/>

`MyView` 클래스 생성

```java
package hello.servlet.web.frontcontroller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyView {

    private String viewPath;

    public MyView(String viewPath) {
        this.viewPath = viewPath;
    }

    public void render(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
        
    }
}
```

<br/>

## 코드 설명 및 정리

```java
MyView view = controller.process(request, response);
```

( `controller.process` 뜻은 `controller` 은 인터페이스이고, `process` 는 인터페이스 안에 추상메서드로 지정한 것이다 )

<br/>

`process()` 메서드가 MyView 를 생성해서 넘겨 준다. 어떤걸? 

이 코드를 → `new MyView("/WEB-INF/views/new-form.jsp");` 

MyView의 객체를 생성하여 반환 해준다.

<br/>그리하여 `MyView view = controller.process(request, response);` 의 

view는 MyView의 객체가 들어가게 되면서

<br/>

`view.render(request, response);` 의 render() 메서드는 MyView 클래스의 메서드이며,

`dispatcher` 부분이 들어가 있는 것이다. 

<br/>그리하여 render() 메서드에 의해 jsp로 이동 하게 되는 것이다.

그리고 viewPath변수에 저장된 문자는 이동해야 될 jsp 경로가 저장되어 있다.

![이미지](/programming/img/서36.PNG)


<br/>

## 뷰 리졸버

```java
`MyView view = viewResolver(viewName)`
```

컨트롤러가 반환한 논리 뷰 이름을 실제 물리 뷰 경로로 변경한다. 

그리고 실제 물리 경로가 있는 MyView 객체를 반환한다.

<br/>

논리 뷰 이름: members

물리 뷰 경로: /WEB-INF/views/members.jsp

<br/>

>**Reference** <br/>스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1