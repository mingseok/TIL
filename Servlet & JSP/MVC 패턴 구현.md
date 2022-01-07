# MVC 패턴 구현

### **MVC → (Model - View - Controller) 각각의 요소는 역할을 담당한다.**

모델 : 비즈니스 영역의 로직을 처리한다.

뷰 : 비즈니스 영역에 대한 프레젠테이션 뷰(즉, 사용자가 보게 될 결과 화면)를 담당한다.

컨트롤러 : 사용자의 입력 처리와 흐름 제어를 담당한다.
<br/><br/>

![이미지](/programming/img/서블릿7.PNG)

**MVC 패턴의 핵심 두가지다.**

1. 비즈니스 로직을 처리하는 모델과 결과 화면을 보여주는 뷰를 분리한다.
2. 어플리케이션의 흐름 제어나 사용자의 처리 요청은 컨트롤러에 집중 된다.

모델은 비즈니스와 관련된 로직을 처리하면 될 뿐 

사용자에게 보일 화면이나 흐름제어에 대해서는 처리 하지 않는다.
<br/><br/>
반대로 뷰는 사용자에게 알맞은 화면을 보여주는 역할만 수행할 뿐, 비즈니스 로직이나 흐름 제어 등을 처리하지 않는다. 이렇게 모데로가 뷰가 분리되어 있기 때문에 모델의 내부 로직이 변경되더라도 뷰는 영향을 받지 않고, 반대로 뷰와 모델이 직접 연결되어 있지 않기 때문에 내부 구현 로직에 상관없이 뷰를 변경할 수 있다. 

즉, MVC 패턴을 사용함으로써 유지보수 작업이 쉬워지고 어플리케이션을 쉽게 확장할 수 있게 된다.
<br/><br/>

### **MVC 패턴과 모델2 구조의 매핑**
<br/>

컨트롤러 = 서블릿

모델 = 로직 처리 클래스, 자바빈

뷰 = JSP

사용자 = 웹 브라우저 내지 휴대폰과 같은 다양한 기기
<br/><br/>

### **MVC의 컨트롤러 : 서블릿**

컨트롤러 역할을 하는 서블릿

![이미지](/programming/img/서블릿8.PNG)
<br/><br/>

과정 1 : 웹 브라우저가 전송한 HTTP 요청을 받는다. 서블릿의 doGet( ) 메서드나 doPost( ) 메서드가호출 된다.

```java
public void doGet(HttpServletRequest request, HttpServletResponse response)
											throws ServletException, IOException {

					processRequest(request, response);
}

public void doPost(HttpServletRequest request, HttpServletResponse response)
											throws ServletException, IOException {

					processRequest(request, response);
}
			
```
<br/><br/>
과정 2 : 웹 브라우저가 어떤 기능을 요청했는지 분석한다. 예를 들어, 게시판 목록을 요청 했는지, 글쓰기를 요청했는지 알아낸다.

```java
private void processRequest(HttpServletRequest request, HttpServletResponse response)
											throws ServletException, IOException {

					String type = request.getParameter("type");
}
```
<br/><br/>
과정 3 : 모델을 사용하여 요청한 기능을 수행한다. ( 모델에서 되는 과정은 이렇다. → 1. 컨트롤러로 부터 요청 받음 → 2. 비즈니스 로직 수행 → 3. 수행 결과 컨트롤러에 리턴 )

```java
Object result = null;

if(type == null || type.equals("greeting")) {
		
				result = "안녕하세요";
		 
  } else if (type.equals("date")) {
	
				... 
	
	}
```

 
<br/><br/>
과정 4 : 모델로부터 전달받은 결과물을 알맞게 가공한 후, request, session의 setAttribute( ) 메서드를 사용하여 결과값을 속성에 저장한다. 이렇게 저장한 결과값은 뷰인 jsp에서 사용한다.

```java
request.setAttribute("result", resultObject);
```
<br/><br/>
과정 5 : 웹 브라우저에 결과를 전송할 jsp를 선택한 후, 해당 jsp로 포워딩한다. 경우에 따라 리다이렉트를 하기도 한다.

```java
RequestDispatcher dispatcher = request.getRequestDispatcher("/simpleView.jsp");

dispatcher.forward(request, response);
```