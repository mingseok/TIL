## 검증 - Form 전송 객체 분리


### Validation 메인 챕터 (물론 앞에 과정도 중요)

<br/>


## Form 전송 객체 분리 - 소개

```
HTML Form -> ItemSaveForm -> Controller -> Item 생성 -> Repository
```

- 장점: 전송하는 폼 데이터가 복잡해도 거기에 맞춘 별도의 폼 객체를 사용해서 데이터를 전달 받을 수 있다. 

    보통 등록과, 수정용으로 별도의 폼 객체를 만들기 때문에 검증이 중복되지 않는다.



- 단점: 폼 데이터를 기반으로 컨트롤러에서 Item 객체를 생성하고, 변환 하는 과정이 추가 된다.

```
수정의 경우 등록과 수정은 완전히 다른 데이터가 넘어온다
```

<br/>

회원 가입시 다루는 데이터와 수정시 다루는 데이터는 범위에 차이가 있다.

- 등록시에는 `로그인id`, `주민번호` 등등을 받을 수 있지만, 수정시에는 이런 부분이 빠진다.

    - (주민번호를 수정화면에서 변경하지 않는다..)

- 그리고 검증 로직도 많이 달라진다.

- 그리하여 `ItemUpdateForm` 이라는 별도의 객체로 데이터를 전달받는 것이 좋다.

<br/><br/>

## 이름 작성

`HTML`에서 넘어오는 것은 `ItemSaveForm` 이라고 작성하고, `API`에서 넘어올 때는 `ItemSaveRequest` 라고 짓는다.

중요한 것은 일관성 있게 작성하는 것이다.

<br/>

## 등록, 수정 뷰 템플릿은 합치는게 좋을까?

- 정답은 아니다.

- 어설프게 합치면 나중,, 유지보수에 고통스럽다.


<br/><br/>

## 기존 Item 클래스 이렇게만 남기기.

```java
@Data
public class Item {

    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;
}
```

<br/>

## `ItemSaveForm` 생성 (등록 시킬때 사용하는 클래스)

로직을 생각해보면 html 폼에서 저장할때 id가 없었다. → 그렇기에 뺐음

```java
@Data
public class ItemSaveForm {

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    @NotNull
    @Max(value = 9999)
    private Integer quantity;
}
```

<br/><br/>

## `ItemUpdateForm` 생성 (수정 할때 사용하는 클래스)

수정할때 조건이 수량은 무제한으로 가능하게 해달라고 하였다. 

그렇기에 quantity 필드의 애노테이션들을 뺐다.

```java
@Data
public class ItemUpdateForm {

    @NotNull
    private Long id;

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    // 이제부터 수정에서는 수량은 자유롭게 변경할 수 있다.
    private Integer quantity;
}
```

<br/><br/>

## 컨트롤러 - (등록 메서드)

매개변수에 `ItemSaveForm form` 으로 수정 되었다.

- `@ModelAttribute("item") ItemSaveForm form` -> 괄호의 “item”은 뭔가?
    
    뷰 탬플릿에서 `item`으로 사용할 것이라고 하는 것이다.
    

- `Item item = new Item();` → 생성 이유는?
    
    `itemRepository.save()`에 들어가 보면 `Item` 타입으로만 들어가야 된다.
    
    그렇기에 `Item` 객체를 생성해서, `form 게터`를 사용해서 저장된 데이터를 꺼내
    
    `Item.class 세터`로 저장시키는 것이다. 그 다음 `item`객체를 `save()` 시킨다
    

```java
@Slf4j
@Controller
public class ValidationItemControllerV {

    // ... 생략

    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {

        if (form.getPrice() != null && form.getQuantity() != null) {
            int resultPrice = form.getPrice() * form.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        // 코드 추가.
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v4/items/{itemId}";
    }

    // ... 생략
}
```

<br/><br/>

## 컨트롤러 - (수정 메서드)

```java
@Slf4j
@Controller
public class ValidationItemControllerV {

    // ... 생략

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, 
                       @Validated @ModelAttribute("item") ItemUpdateForm form, 
                       BindingResult bindingResult) {
        
        if (form.getPrice() != null && form.getQuantity() != null) {
            int resultPrice = form.getPrice() * form.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }
        
				// 추가 작성
        Item itemParam = new Item();
        itemParam.setItemName(form.getItemName());
        itemParam.setPrice(form.getPrice());
        itemParam.setQuantity(form.getQuantity());
        itemRepository.update(itemId, itemParam);
        return "redirect:/validation/v4/items/{itemId}";
    }
}
```

<br/><br/>

## 이렇게 작성하고 실행시켜 보면 잘 동작 한다.

등록도 잘 될것이고, 수정에서도 `“수량”` 을 `9999`까지 였던걸 `99999` 까지 해도 잘 저장 되는걸 알 수 있다.

### ‘등록’ 리펙토링 후 → 실행

![이미지](/programming/img/입문128.PNG)

<br/>

### ‘수정’ 리펙토링 후 → 실행

![이미지](/programming/img/입문129.PNG)

<br/><br/>

## 중요한 것은

`addForm.html` 는 이제 컨트롤러로 넘겨줄때 `ItemSaveForm form` 으로 변경 되었다는 점이다. 

그렇기에 `addForm.html`에서 넘겨주는 필드도 3개가 되는 것이다.

<br/>

`ItemSaveForm` 클래스에 있는 필드도 3개이다.

`ItemUpdateForm.class`와 `editForm.html` 도 필드가 갯수가 같다.

<br/>

즉, 폼에 딱 맞는 객체를 가져다 사용한 것이라고 생각하자.

```java
Form 전송 객체 분리해서 등록과 수정에 딱 맞는 기능을 구성하고, 검증도 명확히 분리했다.
```


<br/><br/>

>**Reference** <br/>[스프링 MVC 2편 - 백엔드 웹 개발 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2/dashboard)
<br/>

## 궁금증!

```java
클래스가 두 개로 늘어나는데, 그래도 나누는 게 나은 걸까?
```

나누기 전에 어떤 일이 벌어지는지 보면 답이 나온다.

<br/>

`Item` 하나로 등록과 수정을 다 하려면 이렇게 된다.

```java
public class Item {
    @NotNull(groups = UpdateCheck.class)          // 수정할 때만 필수
    private Long id;

    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class})
    private String itemName;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Range(min = 1000, max = 1000000, groups = {SaveCheck.class, UpdateCheck.class})
    private Integer price;

    @NotNull(groups = SaveCheck.class)             // 등록할 때만 필수
    @Max(value = 9999, groups = SaveCheck.class)
    private Integer quantity;
}
```

<br/>

어노테이션마다 `groups` 를 붙여야 하고, 필드가 늘어날 때마다 두 군데를 다 신경 써야 한다.

읽어서 `등록할 때 뭐가 필수인지` 파악하기가 어렵다.

<br/>

나누면 이렇게 된다.

```java
public class ItemSaveForm {
    @NotBlank
    private String itemName;

    @NotNull @Range(min = 1000, max = 1000000)
    private Integer price;

    @NotNull @Max(9999)
    private Integer quantity;
}

public class ItemUpdateForm {
    @NotNull
    private Long id;

    @NotBlank
    private String itemName;

    @NotNull @Range(min = 1000, max = 1000000)
    private Integer price;

    private Integer quantity;      // 수정할 때는 수량 제한이 없다
}
```

<br/>

`groups` 가 하나도 없다. 각 클래스만 보면 그 상황의 규칙이 전부 보인다.

<br/>

## 더 큰 이유는 도메인이 화면을 모르게 하는 것이다

원문의 `Item 클래스 이렇게만 남기기` 가 이 얘기다.

```java
// 나누기 전 - Item 이 화면 검증 규칙을 알고 있다
public class Item {
    @NotBlank(message = "공백 X")
    private String itemName;
}
```

<br/>

`Item` 은 원래 `상품이 무엇인가` 를 나타내는 도메인 객체다.

거기에 `이 화면에서는 공백을 못 넣는다` 는 규칙이 들어가 있으면 이상하다.

<br/>

관리자 화면에서는 공백을 허용해야 한다거나, 배치로 넣을 때는 검증이 필요 없다거나 하면

`Item` 을 고쳐야 한다. 화면 사정 때문에 도메인이 바뀌는 것이다.

<br/>

앞의 DTO 글에서 본 것과 정확히 같은 이유다.

```java
Item (도메인)         -> 상품이 무엇인지. DB 와 비즈니스 로직의 사정
ItemSaveForm (폼)     -> 등록 화면에서 뭘 받고 어떻게 검증할지. 화면의 사정
```

<br/>

## 그런데 필드가 중복된다

`itemName`, `price` 가 세 클래스에 다 있다. 중복 아닌가 싶다.

<br/>

이 중복은 감수하는 쪽이 맞다고 본다. 셋이 바뀌는 이유가 다르기 때문이다.

```java
Item           - DB 컬럼이 바뀌면 바뀐다
ItemSaveForm   - 등록 화면 요구사항이 바뀌면 바뀐다
ItemUpdateForm - 수정 화면 요구사항이 바뀌면 바뀐다
```

<br/>

지금은 모양이 같아도 앞으로 다른 방향으로 갈라진다.

등록에는 `약관 동의` 가 추가되고, 수정에는 `수정 사유` 가 추가되는 식이다.

<br/>

같아 보인다고 합쳐두면, 나중에 갈라질 때 다시 `groups` 같은 것으로 억지로 구분하게 된다.

```java
지금 같은 모양이라고 합치면 -> 나중에 갈라질 때 조건 분기가 생긴다
바뀌는 이유가 다르면 나눈다 -> 지금은 중복처럼 보여도 각자 자유롭게 바뀐다
```

<br/>

## 변환 코드는 어디에 둘까

컨트롤러에서 `Form` 을 `Item` 으로 바꾸는 코드가 생긴다.

```java
@PostMapping("/add")
public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form,
                      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
        return "items/addForm";
    }

    Item item = new Item();
    item.setItemName(form.getItemName());
    item.setPrice(form.getPrice());
    item.setQuantity(form.getQuantity());

    itemRepository.save(item);
    return "redirect:/items/{itemId}";
}
```

<br/>

이 세 줄이 컨트롤러에 있으면 필드가 늘어날 때마다 컨트롤러가 길어진다.

Form 쪽에 메서드로 두는 편이 낫다.

```java
public class ItemSaveForm {
    // ...

    public Item toItem() {
        return new Item(itemName, price, quantity);
    }
}
```

```java
itemRepository.save(form.toItem());
```

<br/>

`Form` 은 `Item` 을 알아도 되지만 `Item` 은 `Form` 을 몰라야 한다.

앞의 인터페이스 글에서 본 `의존 방향을 한쪽으로` 가 여기서도 적용된다.

<br/>

## @ModelAttribute("item") 으로 이름을 맞춘 이유

```java
@ModelAttribute("item") ItemSaveForm form
```

<br/>

이름을 안 주면 클래스 이름을 따라 `itemSaveForm` 이라는 이름으로 모델에 담긴다.

그러면 뷰 템플릿의 `th:object="${item}"` 을 전부 고쳐야 한다.

<br/>

이름을 `item` 으로 맞춰두면 뷰는 그대로 두고 자바 쪽만 바꿀 수 있다.

원문의 `등록, 수정 뷰 템플릿은 합치는게 좋을까` 도 이 이름 문제와 이어진다.
