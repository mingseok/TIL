## PRG (Post-Redirect-Get)

![이미지](/programming/img/서68.PNG)

0. 클라이언트가 /add 페이지를 GET 방식으로 '상품 등록 폼' 화면을 서버(=컨트롤러)에 요청한다.

1. 그러면 서버에서 클라이언트한테 '상품 등록 폼' 을 html 렌더링 하여 화면을 보여준다.

2. 그 다음 클라이언트가 id 와 pwd 를 입력 하고 '저장' 버튼을 누른다. 

3. 그러면 서버쪽으로 POST로 넘어가게 된다. 그리고 컨트롤러에 저장하게 된다.

4. 컨트롤러에 저장 되고, Redirect 로 다시 클라이언트에게 넘어가게 된다. 어디로??

5. '상품 상세' 화면으로 !! 클라이언트에서 서버쪽으로 GET 요청을 보내는 것이다.

6. 그리고 '상풍 상세' 화면을 클라이언트에게 보여 주는 것이다.

<br/>

웹 브라우저의 새로 고침은 마지막에 서버에 전송한 데이터를 다시 전송한다.

<br/>새로 고침 문제를 해결하려면 상품 저장 후에 뷰 템플릿으로 이동하는 것이 아니라, 

상품 상세 화면으로 리다이렉트를 호출해주면 된다.

<br/>웹 브라우저는 리다이렉트의 영향으로 상품 저장 후에 실제 상품 상세 화면으로 다시 이동한다. 

따라서 마지막에 호출한 내용이 상품 상세 화면인 GET /items/{id} 가 되는 것이다.


```java
// PRG 방법 
// @ModelAttribute("item") 생략 가능하다. 그럼 어디에 매핑 되는건가?
// Item 클래스 이름으로 앞 대문자를 소문자로만 바꿔서 저장된다.
 @PostMapping("/add")
 public String addItemV3(@ModelAttribute("item") Item item) {
    
    itemRepository.save(item);
    
    return "redirect:/basic/items/" + item.getId();
}
```

<br/>

이후 새로고침을 해도 상품 상세 화면으로 이동하게 되므로 새로 고침 문제를 해결할 수 있다.


![이미지](/programming/img/서69.PNG)

<br/>

![이미지](/programming/img/서70.PNG)


<br/>


마지막 get 부분이다.

![이미지](/programming/img/서71.PNG)

상품 등록 처리 이후에 뷰 템플릿이 아니라 상품 상세 화면으로 리다이렉트 하도록 

코드를 작성해보자.

이런 문제 해결 방식을 PRG Post/Redirect/Get 라 한다.

<br/>

## 주의

`"redirect:/basic/items/" + item.getId() redirect에서 +item.getId()` 처럼 URL에 변수를

더해서 사용하는 것은 URL 인코딩이 안되기 때문에 위험하다. 

### 그러므로 RedirectAttributes 를 사용하자.


<br/>

>**Reference** <br/>스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1
