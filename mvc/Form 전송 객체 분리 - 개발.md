## Form 전송 객체 분리 - 개발

Item 클래스의 검증은 이제 사용하지 않는다.

```java
package hello.itemservice.domain.item;

@Data
public class Item {
    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
```

<br/>

web 디렉토리 → form → `ItemSaveForm` 클래스 생성



### 저장용 폼

```java
package hello.itemservice.web.form;

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

<br/>

web 디렉토리 → form → `ItemUpdateForm` 클래스 생성

### 수정용 폼

```java
package hello.itemservice.web.form;

@Data
public class ItemUpdateForm {

    @NotNull
    private Long id;

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    // 수정에서는 수량은 자유롭게 변경할 수 있다.
    private Integer quantity;

}
```

<br/>

## 폼 객체 바인딩

`Item` 대신에 `ItemSaveform` 을 전달 받는다. 

그리고 `@Validated` 로 검증도 수행하고, `BindingResult`로 검증 결과도 받는다.

```java
@PostMapping("/add")
public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form,
							BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		 //...
}
```

<br/>

## 주의

`@ModelAttribute("item")` 에 `item` 이름을 넣어준 부분을 주의하자. 이것을 넣지 않으면

`ItemSaveForm` 의 경우 규칙에 의해 `itemSaveForm` 이라는 이름으로 MVC Model에 담기게 된다.

<br/>이렇게 되면 뷰 템플릿에서 접근하는 `th:object` 이름도 함께 변경해주어야 한다

### 폼 객체를 Item으로 변환

```java
//성공 로직
Item item = new Item();
item.setItemName(form.getItemName());
item.setPrice(form.getPrice());
item.setQuantity(form.getQuantity());

Item savedItem = itemRepository.save(item);
```

폼 객체의 데이터를 기반으로 `Item` 객체를 생성한다. 

이렇게 폼 객체 처럼 중간에 다른 객체가 추가되면 변환하는 과정이 추가된다.

<br/>

### 수정

```java
@PostMapping("/{itemId}/edit")
public String edit(@PathVariable Long itemId, @Validated
					@ModelAttribute("item") ItemUpdateForm form, BindingResult bindingResult) {
		//...
}
```

<br/>

수정의 경우도 등록과 같다. 그리고 폼 객체를 `Item` 객체로 변환하는 과정을 거친다.

```java
//성공 로직
Item itemParam = new Item();
itemParam.setItemName(form.getItemName());
itemParam.setPrice(form.getPrice());
itemParam.setQuantity(form.getQuantity());

itemRepository.update(itemId, itemParam);
```

<br/>

### 정리

Form 전송 객체 분리해서 등록과 수정에 딱 맞는 기능을 구성하고, 검증도 명확히 분리했다.

<br/>

>**Reference** <br/>스프링 MVC 2편 - 백엔드 웹 개발 활용 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2