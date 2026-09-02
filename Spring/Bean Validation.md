## @Validated 설명, Bean Validation


<br/>

## @Validated 설명

`@ModelAttribute Item` 에 대해서 자동으로 검증기가 수행이 되는 것이다.

그리고 검증을 끝내고 문제가 있다면 → `BindingResult bindingResult` 에 담기는 것이다.

<br/>

### 이렇게 할 수 있었던 이유는,

`@InitBinder` 메서드 내부로직에 검증기가 있기 때문에 가능한 것이다.

- `@Validated`는 검증 대상 앞에 붙어야 된다.

- `@Validated`는 "검증기를 실행하라" 라는 애노테이션이다.
- `@Validated`와 `@Valid` 는 동일하다. (둘다 사용해도 된다.)

```java
@Slf4j
@Controller
public class ValidationItemControllerV2 {

		// ... 생략

    @PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item, 
                                       BindingResult bindingResult, 
                                       RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "validation/v2/addForm";
        }
    }

    // ... 생략
}
```

추가로 `@Validated` 와 `BindingResult bindingResult`는 세트라고 생각하면 된다.


<br/><br/>

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

<br/>

## 궁금증!

```java
@NotNull, @NotEmpty, @NotBlank 는 뭐가 다를까?
```

셋 다 비슷해 보이는데 통과 기준이 다르다.

```java
값             @NotNull   @NotEmpty   @NotBlank
null           실패        실패         실패
""             통과        실패         실패
"   " (공백)    통과        통과         실패
"abc"          통과        통과         통과
```

<br/>

`@NotNull` 은 `null` 만 막는다. 빈 문자열은 통과한다.

사용자가 입력창을 비우고 보내면 대부분 `""` 로 오기 때문에, `@NotNull` 만으로는 못 막는다.

<br/>

`@NotBlank` 는 공백만 있는 것도 막는다. 문자열에는 이게 맞다.

```java
문자열       -> @NotBlank
컬렉션, 배열 -> @NotEmpty (@NotBlank 는 문자열 전용이라 못 쓴다)
숫자, 객체    -> @NotNull
```

<br/>

`@NotEmpty` 는 문자열과 컬렉션 둘 다에 쓸 수 있는데, 문자열이면 `@NotBlank` 가 낫다.

<br/>

## 숫자에는 @NotBlank 를 못 쓴다

```java
@NotBlank
private Integer price;     // 컴파일은 되는데 실행하면 터진다
```

```java
UnexpectedTypeException: No validator could be found for constraint
'jakarta.validation.constraints.NotBlank' validating type 'java.lang.Integer'
```

<br/>

`@NotBlank` 는 `CharSequence` 에만 붙일 수 있다.

숫자는 `@NotNull` 로 `null` 을 막고, 범위는 `@Min`, `@Max`, `@Positive` 로 따로 잡는다.

```java
@NotNull
@Range(min = 1000, max = 1000000)
private Integer price;
```

<br/>

## 검증 순서가 중요하다

원문의 `검증 순서` 를 더 파보면, 실패했을 때 무엇이 남는지가 다르다.

```java
1. @ModelAttribute 로 타입 변환을 시도한다
     - 실패하면 typeMismatch 오류를 BindingResult 에 담고, 그 필드는 검증을 건너뛴다
2. 변환에 성공한 필드만 Bean Validation 을 돌린다
```

<br/>

`price` 에 `"abc"` 를 넣으면 `Integer` 로 못 바꾼다.

이때 `@Range` 검증은 아예 안 돈다. 검증할 값 자체가 없기 때문이다.

<br/>

그래서 사용자에게는 `숫자를 입력해주세요` 라는 타입 오류만 보인다.

`1000원 이상 입력해주세요` 라는 메시지는 안 나온다.

```java
값이 아예 안 들어옴 -> 타입 오류만
값은 들어왔는데 범위가 틀림 -> Bean Validation 오류
```

<br/>

## @Valid 와 @Validated 의 차이

```java
@Valid      - 자바 표준 (jakarta.validation)
@Validated  - 스프링이 만든 것 (org.springframework.validation.annotation)
```

<br/>

컨트롤러 파라미터에 붙일 때는 기능이 거의 같다.

차이는 두 가지다.

<br/>

### 첫번째) @Validated 만 그룹을 지정할 수 있다

```java
@Validated(SaveCheck.class)
```

<br/>

같은 클래스를 등록할 때와 수정할 때 다르게 검증하고 싶을 때 쓴다.

다만 원문의 `Form 전송 객체 분리` 처럼 클래스를 나누는 편이 실무에서는 더 많이 쓰인다.

<br/>

### 두번째) @Validated 는 컨트롤러 밖에서도 쓸 수 있다

```java
@Service
@Validated                              // 클래스에 붙인다
public class OrderService {

    public void order(@Valid OrderCommand command) { ... }
}
```

<br/>

이건 AOP로 동작한다. 앞의 AOP 글에서 본 프록시가 메서드 호출을 가로채서 검증한다.

<br/>

그래서 AOP의 제약이 그대로 따라온다.

```java
같은 클래스 안에서 내부 호출하면 -> 검증이 안 된다
private 메서드에 붙이면          -> 검증이 안 된다
```

<br/>

그리고 검증에 실패하면 `BindingResult` 가 아니라 예외가 던져진다.

```java
ConstraintViolationException
```

컨트롤러에서는 오류를 화면에 보여줄 수 있었지만, 서비스에서는 예외로 처리해야 한다.

<br/>

## 메시지를 찾는 순서

원문의 `errors.properties 추가 등록` 이 왜 동작하는지는 이 순서 때문이다.

```java
1. 어노테이션의 message 속성       @NotBlank(message = "이름은 필수입니다")
2. MessageSource 에서 코드로 찾기   NotBlank.item.itemName -> NotBlank.itemName
                                    -> NotBlank.java.lang.String -> NotBlank
3. 라이브러리 기본 메시지           "must not be blank"
```

<br/>

`2번` 이 앞의 `MessageCodesResolver` 가 만들어주는 코드들이다.

구체적인 것부터 찾다가 없으면 점점 덜 구체적인 것으로 내려간다.

<br/>

그래서 `errors.properties` 에 `NotBlank` 한 줄만 적어두면 모든 `@NotBlank` 에 적용되고,

특정 필드만 다르게 하고 싶으면 `NotBlank.item.itemName` 을 추가하면 된다.

```java
NotBlank=공백은 입력할 수 없습니다
NotBlank.item.itemName=상품 이름은 필수입니다
```

<br/>

메시지를 코드에서 빼두면 문구를 바꿀 때 자바 파일을 안 건드려도 된다.

앞의 뷰 리졸버 글에서 본 `이름과 실제를 분리한다` 가 여기에도 적용된 것이다.
