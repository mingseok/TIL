## MessageCodesResolver

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






## 동작 방식

`rejectValue()` , `reject()` 는 내부에서 `MessageCodesResolver` 를 사용한다. 

여기에서 메시지 코드들을 생성한다.

`FieldError` , `ObjectError` 의 생성자를 보면, 오류 코드를 하나가 아니라 여러 오류 코드를 가질 수 있다. 

<br/>

`MessageCodesResolver` 를 통해서 생성된 순서대로 오류 코드를 보관한다.

이 부분을 `BindingResult` 의 로그를 통해서 확인 할 수 있다.

`codes [range.item.price, range.price, range.java.lang.Integer, range]`

<br/><br/>

## FieldError rejectValue("itemName", "required")

다음 4가지 오류 코드를 자동으로 생성

```
required.item.itemName
required.itemName
required.java.lang.String
required
```

<br/><br/>

## ObjectError reject("totalPriceMin")

다음 2가지 오류 코드를 자동으로 생성

```
totalPriceMin.item
totalPriceMin
```

<br/><br/>

## 중요한 것은?

애플리케이션 코드를 변경할 필요 없이 ‘설정’ 만으로도 

메시지를 변경할 수 있다는 것이 중요한 것이다.


<br/><br/>

>**Reference** <br/>[스프링 MVC 2편 - 백엔드 웹 개발 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2/dashboard)