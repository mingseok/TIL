## Bean Validation - 스프링 적용

스프링 MVC는 어떻게 Bean Validator를 사용하는가?

스프링 부트가 `spring-boot-starter-validation` 라이브러리를 넣으면 자동으로 

`Bean Validator`를 인지하고 스프링에 통합한다.

<br/>

### 스프링 부트는 자동으로 글로벌 Validator로 등록한다.



### `LocalValidatorFactoryBean` 을 글로벌 `Validator`로 등록한다. 

그렇기 때문에 여러곳에서 사용 할 수 있는 것이다.

<br/>

즉, 컨트롤러에 `@Validated` 어너테이션이 있으면 애가 글로벌`Validated` 가 실행이 되면서

 item쪽을 보면서 어너테이션이 붙은 애들을 보고 바로 검증을 해버리는 것이다. 
 
 그리고 오류가 있으면 BindingResult 에 딱 넣어주는 것이다. (= 밑에 코드들 보기)

```java
	@PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId,
                       @Validated @ModelAttribute Item item,
                       BindingResult bindingResult) {

						```
```

<br/>

```java
@Data
public class Item {

    private Long id;

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    @NotNull
    @Max(9999)
    private Integer quantity;

    ```

}
```

이 Validator는 `@NotNull` 같은 애노테이션을 보고 검증을 수행한다. 

이렇게 글로벌 Validator가 적용되어 있기 때문에, `@Valid` , `@Validated` 만 적용하면 된다.

검증 오류가 발생하면, FieldError , ObjectError 를 생성해서 BindingResult 에 담아준다.

<br/>

### 코드 제거

기존에 등록한 ItemValidator를 제거해두자! 오류 검증기가 중복 적용된다.

```java
private final ItemValidator itemValidator;

@InitBinder
public void init(WebDataBinder dataBinder) {
		log.info("init binder {}", dataBinder);
		dataBinder.addValidators(itemValidator);
}
```

@ModelAttribute → 각각의 필드 타입 변환시도 → 변환에 성공한 필드만 BeanValidation 적용

<br/>

## 검증 순서

1. @ModelAttribute 각각의 필드에 타입 변환 시도
    
    request.getParameter 인 → itemName, price, quantity 을 딱 스프링 프레임워크가 
    스프링 mvc에 넣어 주는 것이다.
    
2. 성공하면 다음으로
3. 실패하면 typeMismatch 로 FieldError 추가
    가격에 다가 qqqq 넣은 경우 등등
    
<br/>

## 바인딩에 성공한 필드만 Bean Validation 적용

당연한 것이다, qqqq 로 들어 온것은 어차피 오류 이기 때문에 할 필요가 없는 것이다.

BeanValidator는 바인딩에 실패한 필드는 BeanValidation을 적용하지 않는다.

<br/>생각해보면 타입 변환에 성공해서 바인딩에 성공한 필드여야 BeanValidation 적용이 의미 있다.

(일단 모델 객체에 바인딩 받는 값이 정상으로 들어와야 검증도 의미가 있다.)

<br/>

즉,

@ModelAttribute → 각각의 필드 타입 변환시도 → 변환에 성공한 필드만 BeanValidation 적용


<br/>

### 예를 들어)

item 클래스에서 itemName 에 문자 "A" 입력 타입 변환 성공 itemName 필드에 BeanValidation 적용

하지만, 이런 경우는 typeMismatch 에러가 발생하는 것이다.

<br/>

price 에 문자 "A" 입력 "A"를 숫자 타입 변환 시도 실패 typeMismatch FieldError 추가

price 필드는 BeanValidation 적용 X

<br/>

>**Reference** <br/>스프링 MVC 2편 - 백엔드 웹 개발 활용 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2