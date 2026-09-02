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

<br/><br/>

## RedirectAttributes 설명

고객 입장에서 저장이 잘 된 것인지 안 된 것인지 확신이 들지 않는다. 

그래서 저장이 잘 되었으면 상품 상세 화면에 "저장되었습니다"라는 메시지를 

보여 달라는 요구사항이 왔다. 어떻게 해야 될까?

<br/><br/>

## 동작 흐름 순서대로 설명

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

1. 첫번째 리다이렉트로 보낸다.

2. `.../{itemId}` 에 번호가 담긴다.

    - `redirectAttributes.addAttribute("itemId", saveItem.getId())` 이부분을 사용함으로써 
    
        `return` 값에 치환이 되도록 `{itemId}`인 문법으로 사용할 수 있게 된 것이다. 
        
        기존 같으면 이렇게 사용하지 못한다.

3. 반환 될때는 "redirect:/basic/items/3?status=true" 이렇게 URL이 만들어 지는 것이다.

    - 나머지 `?status=true"` 부분은 쿼리 파라미터로 전달 되는 것이다.


<br/><br/>



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
}
```

4. 이쪽으로 요청이 오게 된다.

5. 그리하여 HTML로 모델을 보낸다.

<br/><br/>



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

6. 이렇게 `param.status` 을 꺼내서 true를 사용할 수 있게 되는 것이다.



<br/><br/>

## 그리하여 이렇게 출력 되는 걸 알 수 있다.

![이미지](/programming/img/입문107.PNG)



<br/><br/>

>**Reference** <br/>[스프링 MVC 2편 - 백엔드 웹 개발 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2/dashboard)
<br/>

## 궁금증!

```java
"새로고침하면 중복 등록된다" 는 게 브라우저 입장에서 왜 그런 걸까?
```

브라우저는 새로고침할 때 `직전에 보낸 요청` 을 그대로 다시 보낸다.

그러니 직전 요청이 무엇이었느냐가 전부다.

<br/>

리다이렉트하는 서버를 띄워놓고 실제 응답을 받아봤다.

```java
$ curl -i -X POST http://localhost:18081/order -d "item=book"

HTTP/1.1 302 Temporary Redirect
Content-length: 0
Location: /order/done
```

<br/>

본문이 없고 `Location` 헤더만 있다.

브라우저는 이걸 받으면 화면을 안 그리고, `/order/done` 으로 다시 요청한다.

<br/>

```java
$ curl -L -X POST http://localhost:18081/order -d "item=book"

주문 완료 화면
[최종 URL] http://localhost:18081/order/done
```

<br/>

두 번째 요청은 `POST` 가 아니라 `GET` 이다.

`302` 를 받으면 브라우저가 메서드를 `GET` 으로 바꿔서 다시 보내기 때문이다.

<br/>

## 그래서 새로고침이 안전해진다

```java
PRG 없이
  브라우저의 마지막 요청 = POST /add
  새로고침 -> POST /add 가 다시 간다 -> 상품이 또 저장된다

PRG 적용
  브라우저의 마지막 요청 = GET /items/1
  새로고침 -> GET /items/1 이 다시 간다 -> 조회만 다시 될 뿐
```

<br/>

브라우저가 `POST 를 다시 보내시겠습니까?` 같은 경고창을 띄우는 것도 이 때문이다.

`GET` 은 그냥 다시 보내도 되지만 `POST` 는 위험하다는 것을 브라우저도 알고 있는 것이다.

<br/>

## RedirectAttributes 가 필요한 이유

리다이렉트는 요청이 두 번 일어나는 것이라, 첫 번째 요청의 `Model` 이 두 번째로 넘어가지 않는다.

```java
@PostMapping("/add")
public String addItem(@ModelAttribute Item item, Model model) {
    itemRepository.save(item);
    model.addAttribute("message", "저장되었습니다");   // 이건 사라진다
    return "redirect:/basic/items/" + item.getId();
}
```

<br/>

`Model` 은 한 요청 안에서만 사는 데이터다.

리다이렉트 응답에는 본문이 없으니 담아 보낼 자리도 없다.

<br/>

`RedirectAttributes` 는 이걸 두 가지 방법으로 넘긴다.

```java
@PostMapping("/add")
public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
    Item saved = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", saved.getId());   // URL 에 넣는다
    redirectAttributes.addFlashAttribute("status", true);       // 세션에 잠깐 넣는다
    return "redirect:/basic/items/{itemId}";
}
```

<br/>

```java
addAttribute      -> URL 에 붙는다. /basic/items/3?status=true
                     주소창에 보이고, 복사해서 다시 열어도 그대로다

addFlashAttribute -> 세션에 잠깐 저장했다가, 다음 요청에서 꺼내주고 바로 지운다
                     URL 에 안 보이고, 새로고침하면 사라진다
```

<br/>

`{itemId}` 처럼 경로에 쓴 것은 URL로 치환되고,

남은 것들은 쿼리 스트링으로 붙는다.

<br/>

## Flash 라는 이름이 붙은 이유

한 번 읽으면 사라지기 때문이다.

```java
1. POST 처리 중 -> flash 에 담아 세션에 저장
2. 302 응답
3. GET 요청 도착 -> 세션에서 꺼내 Model 에 넣고, 세션에서 지운다
4. 화면에서 "저장되었습니다" 표시
5. 사용자가 새로고침 -> 세션에 없으니 메시지가 안 뜬다
```

<br/>

`저장되었습니다` 가 새로고침할 때마다 계속 뜨면 이상하다.

한 번만 보여주고 없어져야 하는 메시지라서 이런 방식이 나온 것이다.

<br/>

`addAttribute` 로 넘기면 URL에 남기 때문에 새로고침해도 계속 뜬다.

성공 메시지 같은 것은 `addFlashAttribute` 가 맞다.

```java
계속 유지되어야 하는 값 (조회 조건, 페이지 번호) -> addAttribute
한 번만 보여줄 값 (저장 완료 메시지)            -> addFlashAttribute
```

<br/>

## PRG 를 쓸 수 없는 경우

API 서버에는 PRG가 필요 없다.

```java
@PostMapping("/api/items")
public ItemResponse add(@RequestBody ItemRequest request) {
    return ItemResponse.from(itemRepository.save(request.toEntity()));
}
```

<br/>

이건 브라우저가 화면을 그리는 것이 아니라 자바스크립트가 결과를 받아 처리하는 구조다.

새로고침으로 요청이 다시 가는 일 자체가 없다.

<br/>

대신 API에는 다른 방식의 중복 방지가 필요하다.

같은 요청이 두 번 들어와도 한 번만 처리되게 하는 것인데, 이것을 `멱등성` 이라고 한다.

요청마다 고유한 키를 받아서, 이미 처리한 키면 저장된 결과를 그대로 돌려주는 식이다.
