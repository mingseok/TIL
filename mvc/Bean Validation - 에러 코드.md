## Bean Validation - 에러 코드

Bean Validation이 기본으로 제공하는 오류 메시지를 좀 더 자세히 변경하고 싶으면 어떻게 하면 될까?

`Bean Validation`을 적용하고 `bindingResult` 에 등록된 검증 오류 코드를 보자.

<br/>오류 코드가 애노테이션 이름으로 등록된다. 마치 `typeMismatch` 와 유사하다.

`NotBlank` 라는 오류 코드를 기반으로 `MessageCodesResolver` 를 통해 

<br/>다양한 메시지 코드가 순서대로 생성 된다.

### `@NotBlank`

```
NotBlank.item.itemName
NotBlank.itemName
NotBlank.java.lang.String
NotBlank
```

<br/>

### `@Range`

```
Range.item.price
Range.price
Range.java.lang.Integer
Range
```

<br/>

## 메시지 등록

`errors.properties` 에 메시지를 등록해보자.

```
#Bean Validation 추가
NotBlank={0} 공백X
Range={0}, {2} ~ {1} 허용
Max={0}, 최대 {1}
```

### {0} 은 필드명이고, {1} , {2} ...은 각 애노테이션 마다 다르다.

{0} 은 필드명이 출력 되는 자리 인 것이다.

<br/>

왜 {0} 으로 하냐면, 밑에 사진처럼 이상한 값을 받기 위해서 이다.

![이미지](/programming/img/나.PNG)


BeanValidation 메시지 찾는 순서

1. 생성된 메시지 코드 순서대로 messageSource 에서 메시지 찾기

2. 애노테이션의 message 속성 사용 @NotBlank(message = "공백! {0}")
3. 라이브러리가 제공하는 기본 값 사용 공백일 수 없습니다

<br/>

애노테이션의 message 사용 예

```java

@NotBlank(message = "공백은 입력할 수 없습니다.")
private String itemName;
 
    ```
```

<br/>

## Bean Validation - 오브젝트 오류

오브젝트 오류 관련 부분만 직접 자바 코드로 작성하는 것을 권장한다.

```java
//특정 필드 예외가 아닌 전체 예외
if (item.getPrice() != null && item.getQuantity() != null) {
    int resultPrice = item.getPrice() * item.getQuantity();
            
    if (resultPrice < 10000) {
         bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
    }
}
```

<br/>

## Bean Validation - 수정에 적용

Item 모델 객체에 @Validated 를 추가하자.
```java
@PostMapping("/{itemId}/edit")
public String edit(@PathVariable Long itemId, @Validated @ModelAttribute Item item, BindingResult bindingResult) {

 //특정 필드 예외가 아닌 전체 예외
 if (item.getPrice() != null && item.getQuantity() != null) {
		 int resultPrice = item.getPrice() * item.getQuantity(); 
		 if (resultPrice < 10000) {
				 bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
		 }
	}
 
 if (bindingResult.hasErrors()) {
		 log.info("errors={}", bindingResult);
		 return "validation/v3/editForm";
 }

 itemRepository.update(itemId, item);
 return "redirect:/validation/v3/items/{itemId}";
}
```