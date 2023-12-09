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