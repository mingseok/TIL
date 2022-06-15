## BindingResult



### 필드 오류 - FieldError

```java
if (!StringUtils.hasText(item.getItemName())) {
		bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
}
```

<br/>

FieldError 생성자 요약

```java
public FieldError(String objectName, String field, String defaultMessage) {}
```

필드에 오류가 있으면 `FieldError` 객체를 생성해서 `bindingResult` 에 담아두면 된다.

`objectName` : `@ModelAttribute` 이름

`field` : 오류가 발생한 필드 이름

`defaultMessage` : 오류 기본 메시지


<br/>

### 글로벌 오류 - ObjectError

```java
bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야
합니다. 현재 값 = " + resultPrice));
```

<br/>

### ObjectError 생성자 요약

```java
public ObjectError(String objectName, String defaultMessage) { }
```

특정 필드를 넘어서는 오류가 있으면 `ObjectError` 객체를 생성해서 `bindingResult` 에 담아두면 된다.

`objectName` : `@ModelAttribute` 의 이름

`defaultMessage` : 오류 기본 메시지

<br/>

## 주의

`BindingResult bindingResult` 파라미터의 위치는 `@ModelAttribute Item item` 다음에 와야 한다.

컨트롤러 주석 잘보기.

```java
@PostMapping("/add")
public String addItemV1(@ModelAttribute Item item,
                BindingResult bindingResult, // item 매개변수에 바인딩 된 결과를 여기에 담기는 것이다.
                                            // bindingResult 가 v1에 사용했던 errors 변수 역할을 해주는 것이다.
                RedirectAttributes redirectAttributes,
                Model model) {

     if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수 입니다."));
     }

     if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
     }

     if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지 허용합니다."));
     }


     //특정 필드가 아닌 복합 룰 검증
     if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();

            if (resultPrice < 10000) {
                  bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 
                                            이상이여야 합니다. 현재 값 = " + resultPrice));
            }
     }


     // 검증에 실패하면 다시 입력 폼으로
     // hasErrors() 가 에러가 있으면 조건 수행 해주게 도와 주는 것이다.
     if (bindingResult.hasErrors()) {

            log.info("errors={}", bindingResult);

            // bindingResult 는 model.addAttribute에 담지 않아도 된다.
            // 이유는? bindingResult 는 자동으로 view 에 같이 넘어가기 때문이다.
            return "validation/v2/addForm";
     }


     // 성공 로직
     Item savedItem = itemRepository.save(item);
     redirectAttributes.addAttribute("itemId", savedItem.getId());
     redirectAttributes.addAttribute("status", true);
     return "redirect:validation/v2/items/{itemId}";
}
```

<br/>

### 타임리프

글로벌 오류 처리

```html
<div th:if="${#fields.hasGlobalErrors()}">
	 <p class="field-error" th:each="err : ${#fields.globalErrors()}" 
			th:text="${err}">전체 오류 메시지</p>
</div>
```

<br/>

필드 오류 처리

```html
<input type="text" id="itemName" th:field="*{itemName}"
			th:errorclass="field-error" class="form-control" placeholder="이름을 입력하세요">

<div class="field-error" th:errors="*{itemName}">상품명 오류</div>
```

<br/>

html

```html
<form action="item.html" th:action th:object="${item}" method="post">

    <div th:if="${#fields.hasGlobalErrors()}">
       <p class="field-error" th:each="err : ${#fields.globalErrors()}" th:text="${err}">전체 오류 메시지</p>
    </div>

    <div>
       <label for="itemName" th:text="#{label.item.itemName}">상품명</label>
       <input type="text" id="itemName" th:field="*{itemName}"
               th:errorclass="field-error" class="form-control" placeholder="이름을 입력하세요">

       <!--이렇게 하면 에러가 있으면 출력하고 아니면 출력하지 않는다.-->
       <div class="field-error" th:errors="*{itemName}">상품명 오류</div>
     </div>

     <div>
         <label for="price" th:text="#{label.item.price}">가격</label>
         <input type="text" id="price" th:field="*{price}"
                th:errorclass="field-error" class="form-control" placeholder="가격을 입력하세요">
         <div class="field-error" th:errors="*{price}">가격 오류</div>
     </div>

     <div>
         <label for="quantity" th:text="#{label.item.quantity}">수량</label>
         <input type="text" id="quantity" th:field="*{quantity}"
                th:errorclass="field-error" class="form-control" placeholder="수량을 입력하세요">
         <div class="field-error" th:errors="*{quantity}">수량 오류</div>
     </div>
```

<br/>

### 타임리프 스프링 검증 오류 통합 기능

타임리프는 스프링의 `BindingResult` 를 활용해서 편리하게 검증 오류를 표현하는 기능을 제공한다.

<br/>

`#fields` : `#fields` 로 `BindingResult` 가 제공하는 검증 오류에 접근할 수 있다. (문법이다)

`th:errors` : 해당 필드에 오류가 있는 경우에 태그를 출력한다. `th:if` 의 편의 버전이다.

`th:errorclass` : 예를 들어, `th:field="*{price}"` 에서 지정한 필드에 오류가 있으면 class 

    정보를 추가한다. 그러면 `th:errorclass="field-error"` 까지 포함 되어 렌더링 

    되는 것이다. `"field-error"` 는 뭔가? css 말한다.

<br/><br/>

### 정리

스프링이 제공하는 검증 오류를 보관하는 객체이다. 

검증 오류가 발생하면 여기에 보관하면 된다.

`BindingResult` 가 있으면 `@ModelAttribute` 에 데이터 바인딩 시 오류가 발생해도 

컨트롤러가 호출된다!

<br/>

### 예) `@ModelAttribute`에 바인딩 시 타입 오류가 발생하면?

- `BindingResult` 가 없으면 `400 오류`가 발생하면서 컨트롤러가 호출되지 않고, 
오류 페이지로 이동한다.

- `BindingResult` 가 있으면 오류 정보( FieldError )를 BindingResult 에 담아서 
컨트롤러를 정상 호출한다.

<br/>

>**Reference** <br/>스프링 MVC 2편 - 백엔드 웹 개발 활용 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2