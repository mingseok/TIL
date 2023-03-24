## 검증 - Form 전송 객체 분리

```
Validation 앞 내용들보다 여기서 부터가 중요하다.
```

<br/><br/>


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