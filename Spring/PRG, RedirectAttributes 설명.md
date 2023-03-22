## PRG, RedirectAttributes 설명

<br/>

## PRG Post/Redirect/Get

상품 등록을 완료하고 웹 브라우저의 새로고침 버튼을 클릭해보면,

상품이 계속해서 중복 등록되는 것을 확인할 수 있다.

<br/>

이런 상황을 방지 하기 위해 PRG가 있는 것이다.

![이미지](/programming/img/입문104.PNG)

<br/>

```java
@PostMapping("/add")
public String addItemV3(@ModelAttribute Item item) {

    itemRepository.save(item);
    return "redirect:/basic/items/" + item.getId();
}
```

<br/><br/>

## 1번째 리다이렉트.

![이미지](/programming/img/입문105.PNG)

<br/><br/>

## 2번째. GET 방식으로 다시 호출

![이미지](/programming/img/입문106.PNG)

## RedirectAttributes 설명

고객 입장에서 저장이 잘 된 것인지 안 된 것인지 확신이 들지 않는다. 

그래서 저장이 잘 되었으면 상품 상세 화면에 "저장되었습니다"라는 메시지를 

보여 달라는 요구사항이 왔다. 어떻게 해야 될까?

<br/><br/>

## 동작 흐름 순서대로 설명

1. 첫번째 리다이렉트로 보낸다.

2. “itemId” 에 번호가 담긴다.
3. 반환 될때는 "redirect:/basic/items/3?status=true" 이렇게 URL이 만들어 지는 것이다.

```java
@PostMapping("/add")
public String addItemV4(@ModelAttribute Item item, 
                        RedirectAttributes redirectAttributes) {

    Item saveItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", saveItem.getId());
    redirectAttributes.addAttribute("status", true);

    // "redirect:/basic/items/3?status=true"로 반환 된다.
    return "redirect:/basic/items/{itemId}";
}
```

<br/><br/>

4. 이쪽으로 요청이 오게 된다.
5. 그리하여 HTML로 모델을 보낸다.

```java
@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

		// ...		
}
```

<br/><br/>

6. 이렇게 코드를 추가 해주는 것이다.

```html
<body>
<div class="container">
    <div class="py-5 text-center">
        <h2>상품 상세</h2>
    </div>

    <!-- 추가 -->
    <h2 th:if="${param.status}" th:text="'저장 완료'"></h2>

    // ... 코드 생략
```

<br/><br/>

## 그리하여 이렇게 출력 되는 걸 알 수 있다.

![이미지](/programming/img/입문107.PNG)



<br/><br/>

>**Reference** <br/>[스프링 MVC 2편 - 백엔드 웹 개발 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2/dashboard)