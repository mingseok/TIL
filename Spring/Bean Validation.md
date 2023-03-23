## Bean Validation

<br/>

## Bean Validation 의존관계 추가

`build.gradle` 파일

```
implementation 'org.springframework.boot:spring-boot-starter-validation'
```

<br/><br/>

## 검증 애노테이션과 여러 인터페이스의 모음이다.

`@NotBlank` : null이면 안되고, 빈 문자이면 안되고, 공백 있으면 안되는 애노테이션이다.

`@NotNull` : null이면 안된다.

`@Range(min = 1000, max = 1000000)` : 범위가 최소 1000부터 ~ 1000000 까지이다.

`@Max(9999)` : 수량은 9999까지 이다.

<br/><br/>

## 사용 예제

```java
@Data
public class Item {

    private Long id;

    @NotBlank // @NotBlank(message = "공백X") 이렇게 메시지 변경할 수 있다.
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    @NotNull
    @Max(9999)
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

<br/><br/>

### 컨트롤러 로직 (그냥 참고)

```java
@Slf4j
@Controller
public class ValidationItemController {

    // ... 생략

    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute Item item, 
                                     BindingResult bindingResult, ...) {

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "validation/v3/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    // ... 생략
}
```

<br/><br/>

## 검증 순서

1. `@ModelAttribute Item item` 가 리퀘스트 파라미터를 `Item`클래스에 맡게 데이터를 넣어준다.

    - itemName, price, quantity 변수 들을 말한다.

2. 다음으로 성공하면, Validator로 적용한다. 

    - (참고 : 바인딩에 성공한 필드만 Bean Validation을 적용한다.)

2-1. 만약, `@ModelAttribute` 데이터 바인딩에 실패한다면 `typeMismatch`로 `FieldError`에 추가 한다.

- (바인딩에 실패 예시는 → 가격에는 int만 넣어야 되는데, String 넣는 경우들)


<br/><br/>


## 수정 되는지 확인해보기.

현재는  `“비어 있을 수 없습니다”` 출력 된다.

![이미지](/programming/img/입문125.PNG)

<br/>

### 코드 수정

```java
@Data
public class Member {

    private Long id;

    @NotEmpty(message = "틀렸다 1번")
    private String loginId;

    @NotEmpty(message = "틀렸다 2번")
    private String name;

    @NotEmpty(message = "틀렸다 3번")
    private String password;
}
```

<br/><br/>

## 수정 후 확인

![이미지](/programming/img/입문126.PNG)

<br/><br/>

## 에러 코드

Bean Validation을 적용하고 bindingResult 에 등록된 검증 오류 코드를 보면,

오류 코드가 애노테이션 이름으로 등록 된다는 것을 알 수 있다.

### @NotBlank

- `NotBlank.item.itemName`
- `NotBlank.itemName`
- `NotBlank.java.lang.String`
- `NotBlank`

### @Range

- `Range.item.price`
- `Range.price`
- `Range.java.lang.Integer`
- `Range`

<br/><br/>

## errors.properties 추가 등록

{0} 부분은 필드명이고, {1} , {2} ...은 각 애노테이션 마다 다르다.

```
#--Bean Validation 추가--

NotBlank={0} 공백X
Range={0}, {2} ~ {1} 허용
Max={0}, 최대 {1}
```

이렇게 설정하고 실행 시킨다면 틀렸을때, 추가 한 문자들이 출력 되는 것이다.

### 결국, 중요한 건 애노테이션에 이름에 맞게 설정하면 되는 것이다.

<br/><br/>

## 사용 예시)

만약 `NotBlank`에 대해 더 자세히 알고 싶다고 요청이 들어온다면?

`“NotBlank={0} 공백X”` 보다 저 자세히 레벨(우선 순위)을 정해줘서 작성하면 되는 것이다.

```
NotBlank.item.itemName=상품 이름을 적어주세요. // 추가 해주기.

NotBlank={0} 공백X
Range={0}, {2} ~ {1} 허용
Max={0}, 최대 {1}
```

<br/><br/>

## 그런데 만약,

`“NotBlank.item.itemName=상품 이름을 적어주세요.”` 도 없고,

`“NotBlank={0} 공백X”` 이것도 없다면 어떻게 될까?

`Item` 클래스에 있는 `“@NotBlank(message = "공백X")”` 메시지로 출력 되는 것이다.

```java
@Data
public class Item {

    @NotBlank(message = "공백X")
    private String itemName;

    // ... 생략
```

<br/><br/>

## Bean Validation - 오브젝트 오류

Bean Validation에서 특정 필드`(FieldError)`가 아닌 해당 오브젝트 관련 오류`(ObjectError)`는

어떻게 처리해야 될까? → 오브젝트 오류 같은 경우는 자바코드를 사용하여 해결하자.

```java
@Slf4j
@Controller
public class ValidationItemController {

    // ... 생략

    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute Item item, 
                                   BindingResult bindingResult, ...) {

        // 이렇게 오브젝트 오류를 해결하자.
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        // ... 생략
    }

    // ... 생략
}
```

<br/>

### `errors.properties`

```
#==ObjectError==
#Level1
totalPriceMin.item=상품의 가격 * 수량의 합은 {0}원 이상이어야 합니다. 현재 값 = {1}

#Level2
totalPriceMin=전체 가격은 {0}원 이상이어야 합니다. 현재 값 = {1}

#==FieldError==
required.item.itemName=상품 이름은 필수입니다.
range.item.price=가격은 {0} ~ {1} 까지 허용합니다.
max.item.quantity=수량은 최대 {0} 까지 허용합니다.
```

<br/><br/>

## 위 2개 코드가 치환 되는 과정을 보면 이렇다.

![이미지](/programming/img/입문127.PNG)

- `errorCode :` errors.properties 파일에 있는 문자를 지정해둔 변수명을 적는 곳이다.

    - 설명 : `"required.item.itemName"` 같은 경우는 통채로 "" 묶어서 사용하는 것이다.

- `errorArgs :` 오류 메시지에서 `'{0}'` 을 치환하기 위한 값

    - 설명 : `new Object[]{…}` 생성하는 이유는 errors.properties에 설정해둔 `{0}, {1}, {2}` 등을
        
        인덱스 배열 순서대로 치환 된다고 보면 된다.
        
- `defaultMessage :` 오류 메시지를 찾을 수 없을때 사용하는 기본 메시지

```
이렇게 오브젝트 오류는 자바 코드로 처리하자.
```

<br/><br/>

## 결국 정리해보자면,

@NotBlank, @Range 등등 애노테이션을 사용하면, 스프링이 애노테이션 이름으로 

만들어 버리기 때문에 우리는 그걸 이용해서 사용해야 되는 것이다. 

이런식으로 이용하면 되는 것이다.

```java
-- 이건 필드 관련 스프링이 만들어 주는것--
NotBlank={0} 공백X
Range={0}, {2} ~ {1} 허용
Max={0}, 최대 {1}

--이건 object 관련 스프링이 만들어 주는것--
totalPriceMin.item=상품의 가격 * 수량의 합은 {0}원 이상이어야 합니다. 현재 값 = {1}
```

<br/><br/>

## 참고

앞에서 등등 있었는데, 이건 스프링이 만들어준것이 아니다. 오해하지 말기.

내가 임의로 설정해 놓은 것이다.

```java
required.item.itemName=상품 이름은 필수입니다.
required = 필수 값 입니다. 
```


<br/><br/>

>**Reference** <br/>[스프링 MVC 2편 - 백엔드 웹 개발 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2/dashboard)