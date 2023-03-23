## Validator 분리

목표
복잡한 검증 로직을 별도로 분리하자.

컨트롤러에서 검증 로직이 차지하는 부분은 매우 크다. 이런 경우 별도의 클래스로 

역할을 분리하는 것이 좋다. 그리고 이렇게 분리한 검증 로직을 재사용 할 수도 있다.

<br/>

ItemValidator 클래스.

```java
package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {

        // isAssignableFrom() 이란?
        // item == clazz 서로 클래스가 맞나 검증
        // item == subItem // 자식 클래스 이지만 서로 맞나 검증
        // 자식 클래스까지 커버가 된다.
        return Item.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Item item = (Item) target;

        // 다형성 - 부모는 자식을 담을 수 있다. 하지만 자식은 부모를 담을 수 없다.
        if (!StringUtils.hasText(item.getItemName())) {
            errors.rejectValue("itemName", "required");
        }

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            errors.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        }

        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            errors.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        //특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();

            if (resultPrice < 10000) {
                errors.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

    }
}
```

<br/>

컨트롤러

```java
public class ValidationItemControllerV2 {

    private final ItemValidator itemValidator; // 주입   

    @PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes,
                            Model model) {

        itemValidator.validate(item, bindingResult);

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
}
```

<br/>

스프링은 검증을 체계적으로 제공하기 위해 다음 인터페이스를 제공한다.

```java
public interface Validator {
		boolean supports(Class<?> clazz);
		void validate(Object target, Errors errors);
}
```

`supports() {}` : 해당 검증기를 지원하는 여부 확인(뒤에서 설명)


`validate(Object target, Errors errors)` : 검증 대상 객체와 BindingResult

실행해보면 기존과 완전히 동일하게 동작하는 것을 확인할 수 있다. 

검증과 관련된 부분이 깔끔하게 분리되었다.

<br/>

스프링이 Validator 인터페이스를 별도로 제공하는 이유는 체계적으로 검증 기능을 도입하기 

위해서다. 그런데 앞에서는 검증기를 직접 불러서 사용했고, 이렇게 사용해도 된다. 

그런데 Validator 인터페이스를 사용해서 검증기를 만들면 스프링의 추가적인 도움을 받을 수 있다

컨트롤러에서만 적용이 가능하다

<br/>

이건 해당 컨트롤러가 호출 될때 마다, 항상 호출 된다.

`@GetMapping` 이든 `@PostMapping("/add")` 매핑 이든 모든 메서드는 항상 먼저 호출 되어

검증기를 넣어 놓는 것이다.

<br/>

(`WebDataBinder` 이란 항상 새로 만들어 지는 것이다.)

```java
private final ItemValidator itemValidator; // 검증 클래스 DI

// WebDataBinder 란? 호출 될때 마다 새로 만들어 지는 것이다
// 즉 컨트롤러에 있는 모든 메서드가 호출 되기 전에 호출 되는 것이다.
// 그리하여 검증기 클래스를 만들어 놓는것이라고 생각하면 된다.
@InitBinder
public void init(WebDataBinder dataBinder) { 
      dataBinder.addValidators(itemValidator); // 등록한 검증기가 있는지 확인
}
```

<br/>

컨트롤러

### `validator`를 직접 호출하는 부분이 사라지고, 대신에 검증 대상 앞에 `@Validated` 가 붙었다.

```java
		@PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes,
                            Model model) {

       
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

@Validated 를 추가 함으로써 이 코드를 뺄 수 있게 되었다.

즉, @Validated 어너테이션이 대신 수행 하는 것이다.

```java
itemValidator.validate(item, bindingResult);
```

<br/>

### 동작 방식

`@Validated` 는 “검증기를 실행하라” 는 애노테이션이다.

이 애노테이션이 붙으면 앞서 WebDataBinder 에 등록한 검증기를 찾아서 실행한다. 

<br/>

### 그런데 여러 검증기를 등록한다면 그 중에 어떤 검증기가 실행되어야 할지 구분이 필요하다. 

이때 `supports()` 가 사용된다.

<br/>

### 여기서는 `supports(Item.class)` 호출되고, 결과가 `true` 이므로 ItemValidator 의 validate() 가 호출된다.

<br/>

>**Reference** <br/>스프링 MVC 2편 - 백엔드 웹 개발 활용 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2