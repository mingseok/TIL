## RedirectAttributes

상품을 저장하고 상품 상세 화면으로 리다이렉트 한 것 까지는 좋았다. 

그런데 고객 입장에서 저장이 잘 된것인지 안 된 것인지 확신이 들지 않는다. 

<br/>

그래서 저장이 잘 되었으면 상품 상세 화면에 "저장되었습니다" 라는 메시지를 보여달라는 
요구사항이 왔다.


```java
    // RedirectAttributes 방법
    @PostMapping("/add")
    public String addItemV4(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);

        // redirectAttributes 에다가 항상 하는 addAttribute를 사용하여 넣을 수가 있다.
        redirectAttributes.addAttribute("itemId", savedItem.getId());

        // 그냥 status 안에 'true' 를 저장하는 것이다.
        redirectAttributes.addAttribute("status", true);

        // 이렇게 사용하면 return 마지막 {itemId} 부분이 매개변수로 받은 "itemId" 로 치환 된다.
        // 즉, savedItem.getId()) 가 된다는 말이다.
        
        // 그리고 못들어간 "status" 는 쿼리 파라미터식으로 들어가게 되는 것이다.
        // "redirect:/basic/items/savedItem.getId())?status=true" 가 되는 것이다.
        
        // 브라우저에서 확인 해보면 
        // localhost:8080/basic/items/3?status=true 가 되는 것이다.
        return "redirect:/basic/items/{itemId}";
    }
```


남은 `redirectAttributes.addAttribute("status", true);` 코드는

쿼리 파라미터로 넘어가게 되는것이다. 

밑에 사진 처럼.

![이미지](/programming/img/서72.PNG)

<br/>

실행 시켜보면 

![이미지](/programming/img/서73.PNG)

<br/>실행해보면 다음과 같은 리다이렉트 결과가 나온다.

localhost:8080/basic/items/3?status=true

<br/>

## 이렇게 되는 이유는??

`redirectAttributes.addAttribute("status", true);` status에 true를 넣었기 때문에,

`<h2 th:if="${param.status}" th:text="'저장 완료!'"></h2>` if 문을 수행할 수 있게 된 것이다.

<br/>

## RedirectAttributes

`RedirectAttributes` 를 사용하면 URL 인코딩도 해주고, 

`pathVarible` , 쿼리 파라미터까지 처리해준다.

`redirect:/basic/items/{itemId}`

- 경로 상(pathVariable)에 탬플릿 URL이 있으면 pathVariable 바인딩: `{itemId}`

- 경로 상에 탬플릿 URL이 없으면 나머지는 쿼리 파라미터로 처리: `?status=true`

<br/>

### th:if : 해당 조건이 참이면 실행

`${param.status}` : 타임리프에서 쿼리 파라미터를 편리하게 조회하는 기능

원래는 컨트롤러에서 모델에 직접 담고 값을 꺼내야 한다.

그런데 쿼리 파라미터는 자주 사용해서 타임리프에서 직접 지원한다.

뷰 템플릿에 메시지를 추가하고 실행해보면 "저장 완료!" 라는 메시지가 나오는 것을 확인할 수 있다. 

물론, 상품 목록에서 상품 상세로 이동한 경우에는 해당 메시지가 출력 되지 않는다.


<br/>

>**Reference** <br/>스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1