## MessageCodesResolver, Validation, @Validated

<br/>

## 핵심은 구체적인 것에서! 덜 구체적인 것으로!

- 검증 오류 코드로 메시지 코드들을 생성한다.

- `MessageCodesResolver` 인터페이스이고 `DefaultMessageCodesResolver` 는 기본 구현체이다.

### `DefaultMessageCodesResolver`의 기본 메시지 생성 규칙

<br/><br/>

## 객체 오류

code는 에러 코드를 말함.

```
객체 오류의 경우 다음 순서로 2가지 생성
1.: code + "." + object name 
2.: code

위의 예시) 오류 코드: required, object name: item
1.: required.item
2.: required
```

<br/><br/>

## 필드 오류

code는 에러 코드를 말함

자세한 순서에서 → 덜 자세한 순으로 내려가게 된다.

```
필드 오류의 경우 다음 순서로 4가지 메시지 코드 생성
1.: code + "." + object name + "." + field
2.: code + "." + field
3.: code + "." + field type
4.: code

위의 예시) 오류 코드: typeMismatch, object name "user", field "age", field type: int
1. "typeMismatch.user.age"
2. "typeMismatch.age"
3. "typeMismatch.int"
4. "typeMismatch"
```

<br/><br/>



## 중요한 것은?

애플리케이션 코드를 변경할 필요 없이 ‘설정’ 만으로도 

메시지를 변경할 수 있다는 것이 중요한 것이다.


<br/><br/>


## Validation, @Validated

<br/>

![이미지](/programming/img/입문113.PNG)

고객이 상품 등록 폼에서 상품명을 입력하지 않거나, 

가격, 수량 등이 너무 작거나 커서 검증 범위를 넘어서면, 서버 검증 로직이 실패해야 한다. 

<br/>

```
검증에 실패한 경우 고객에게 다시 고객이 입력하신 상품 등록 폼으로 돌아가서 
입력 하였던 정보를 보여주고, 어떤 값을 잘못 입력했는지 알려주어야 한다.
```

즉, 모델에 데이터들을 다시 다 담아야 되는 것이다. (잘못된 데이터까지)

<br/><br/>

## 기존 화면

![이미지](/programming/img/입문114.PNG)

<br/><br/>

## 변경 화면

![이미지](/programming/img/입문115.PNG)

<br/><br/>

## errors? 무엇인가?

errors가 null 이라면, `errors.containsKey()` 를 호출하는 순간 `NullPointerException` 이 발생한다.

`errors?.` 은 `errors`가 `null`일때 `NullPointerException` 이 발생하는 대신, `null` 을 반환한다.

![이미지](/programming/img/입문116.PNG)

<br/><br/>

## 틀렸을 경우 

입력칸에 빨간색줄 로직 설명 필요

![이미지](/programming/img/입문117.PNG)

`itemName` 에 오류가 들어 있으면, `.field-error` 스코프를 실행 시키고, 

`itemName` 에 오류가 없다면, 그냥 `form-control` 보여주는 것이다.

<br/>

![이미지](/programming/img/입문118.PNG)

<br/><br/>

## `BindingResult` 란?

`Item`으로 바인딩 된 결과가 담기게 되는 것이다. 

그리고 모델에 담을 필요 없이 자동으로 넘어간다.

![이미지](/programming/img/입문119.PNG)

<br/><br/>

## 스프링이 만들어준 검증 결과에 접근 방법

### 글로벌 에러는 여러가지 일수도 있기에 이렇게 사용하는 것이다.

![이미지](/programming/img/입문120.PNG)

<br/><br/>

## 필드 에러 같은 경우

`th:field`로 된 오류가 있다면 → `th:errorclass="field-error"` 것을 → `class="form-control"`에 추가해 주는 것이다.

![이미지](/programming/img/입문121.PNG)

<br/><br/>

## 타임리프는 스프링의 BindingResult 를 활용

타임리프는 스프링의 BindingResult 를 활용해서 편리하게 검증 오류를 표현하는 기능을 제공한다.

<br/>

`#fields :` `#fields` 로 `BindingResult` 가 제공하는 검증 오류에 접근할 수 있다.

`th:errors :` 해당 필드에 오류가 있는 경우, 태그를 출력한다. `th:if` 의 편의 버전이다.



`th:errorclass :` `th:field` 에서 지정한 필드에 오류가 있으면 `class` 정보를 추가한다.

<br/><br/>

## `errors.properties` 파일 생성해주기.

오류 메시지를 구분하기 쉽게 `errors.properties` 라는 별도의 파일로 관리해보자.

![이미지](/programming/img/입문122.PNG)

<br/>

### `errors.properties` 작성해주기.

순서대로 레벨 1부터 찾아서 → 아무것도 보이지 않는다면 레벨 4가 출력 되는 것이다.

```
#==ObjectError==
#Level1
totalPriceMin.item=상품의 가격 * 수량의 합은 {0}원 이상이어야 합니다. 현재 값 = {1}

#Level2 - 생략
totalPriceMin=전체 가격은 {0}원 이상이어야 합니다. 현재 값 = {1}

#==FieldError==
#Level1 -> 벨류를 봐도 완전 디테일
required.item.itemName=상품 이름은 필수입니다.
range.item.price=가격은 {0} ~ {1} 까지 허용합니다.
max.item.quantity=수량은 최대 {0} 까지 허용합니다.

#Level2 - 생략

#Level3
required.java.lang.String=필수 문자입니다.
required.java.lang.Integer=필수 숫자입니다.
min.java.lang.String={0} 이상의 문자를 입력해주세요.
min.java.lang.Integer={0} 이상의 숫자를 입력해주세요.
range.java.lang.String={0} ~ {1} 까지의 문자를 입력해주세요.
range.java.lang.Integer={0} ~ {1} 까지의 숫자를 입력해주세요.
max.java.lang.String={0} 까지의 문자를 허용합니다.
max.java.lang.Integer={0} 까지의 숫자를 허용합니다.

#Level4
required=필수 값 입니다.
min={0} 이상이어야 합니다.
range={0} ~ {1} 범위를 허용합니다.
max={0} 까지 허용합니다.
```

<br/>

### 스프링은 타입 오류가 발생하면 typeMismatch 라는 오류 코드를 사용한다

`errors.properties` 파일 작성.

```
#추가
typeMismatch.java.lang.Integer=숫자를 입력해주세요.
typeMismatch=타입 오류입니다.
```

<br/>

이렇게 하면 에러가 발생 했을때 어떤 조건으로 나와야 되는지 보여 줄 수 있다.

![이미지](/programming/img/입문123.PNG)

<br/><br/>

## 핵심은 여기서 부터.

### WebDataBinder 설명

`WebDataBinder`에 검증기를 추가하면, 해당 컨트롤러에서는 검증기를 자동으로 적용할 수 있다.

`@InitBinder` : 밑에 코드인 컨트롤러가 호출이 될때 마다 항상 불려진다.

<br/>

그리하여 계속 `dataBinder.addValidators(itemValidator);` 로직인 검증기가 실행 되는 것이다.

다시 말해, 해당 컨트롤러에서만 모든 메서드에 검증기를 도입 할 수 있는 것이다.

```java
@Slf4j
@Controller
public class ValidationItemController {

    @InitBinder
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(itemValidator);
    }

		// ... 생략
}
```

<br/><br/>

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

>**Reference** <br/>[스프링 MVC 2편 - 백엔드 웹 개발 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2/dashboard)