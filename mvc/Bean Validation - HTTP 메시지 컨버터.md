## Bean Validation - HTTP 메시지 컨버터

`@Valid` , `@Validated` 는 `HttpMessageConverter(@RequestBody)` 에도 적용할 수 있다.

<br/>

### 참고

`@ModelAttribute` 는 HTTP 요청 파라미터(URL 쿼리 스트링, POST Form)를 다룰 때 사용한다.


`@RequestBody` 는 `HTTP Body`의 데이터를 객체로 변환할 때 사용한다. 주로 `API JSON` 요청을 다룰 때 사용한다.

```java
package hello.itemservice.web.validation;

@Slf4j
@RestController
@RequestMapping("/validation/api/items")
public class ValidationItemApiController {

    @PostMapping
    public Object addItem(@RequestBody @Validated ItemSaveForm form, BindingResult bindingResult) {

// @RequestBody @Validated ItemSaveForm form
// API로 받고 싶은거다. 즉, JSON 형태로 받고 싶은것이다.


        log.info("API 컨트롤러 호출");

        if (bindingResult.hasErrors()) {
            log.info("검증 오류 발생 errors={}", bindingResult);
            return bindingResult.getAllErrors();
        }

        log.info("성공 로직 실행");
        return form;
    }
}
```



결국 `json` 객체로 변하여 json으로 생성된 결과 화면을 볼 수 있는 것이다.

`@RestController` 란? 자동적으로 `@ResponseBody` 어너테이션이 붙는다.

제이슨으로 보여주기 위해서이다.

<br/>

`getAllErrors()` 는? 오브젝트 에러와 필드 에러를 모두 반환 해준다.

어떻게? 오브젝트의 자식은 필드 에러이기 때문이다.

실행 시키면 이렇게 출력 된다.

![이미지](/programming/img/나1.PNG)



<br/>

그리고 만약 “qqqq” 를 넣는다면? 

실패한다.

![이미지](/programming/img/나2.PNG)

<br/>

## API의 경우 3가지 경우를 나누어 생각해야 한다.

### 1. 성공 요청: 성공

### 2. 실패 요청: JSON을 객체로 생성하는 것 자체가 실패함

<br/>

실패 이유는? 컨트롤러까지 못간것이다.

1. 일단 API json 밑에 있는 저 문잗 객체로 변경 되어야 되는 것이다.

```
{"itemName":"hello", "price":1000, "quantity": 10}
```

<br/>

2. 즉, ItemSaveForm 클래스로 값이 들어가고, ItemSaveForm 의 객체로 변경 되어야 

3. Validation 할수 있는데 그 단계까지도 못간 것이다.
4. 즉, HTTP 메시지 컨버터가 ItemSaveForm 객체를 만들어야 컨트롤러를 호출 할 수 있는 것이다
    
    ```java
    @PostMapping
        public Object addItem( ItemSaveForm form ```
    ```

<br/>
    
4. 왜냐? {"itemName":"hello", "price":1000, "quantity": 10} 이 한 덩어리 json을 객체로 만드는 걸 

5. 실패 했기 때문이다. 그리하여 컨트롤러 자체가 호출이 안되고, 예외가 터지는 것이다.

<br/>

즉, ItemSaveForm 은 컨트롤러 자체가 호출이 안된다는 것이다. 

JSON을 가지고 객체를 못만든 것이다. 그냥 끝난 것이다.

<br/>

### 3. 검증 오류 요청: JSON을 객체로 생성하는 것은 성공했고, 검증에서 실패함

postman 에서 작성해보기

```
POST http://localhost:8080/validation/api/items/add
{"itemName":"hello", "price":1000, "quantity": 10000}
```

수량( quantity )이 10000 이면 `BeanValidation` `@Max(9999)` 에서 걸린다.

이렇게 출력된다.

<br/>

`return bindingResult.getAllErrors();` 는 ObjectError 와 FieldError 를 반환한다. 

스프링이 이 객체를 JSON으로 변환해서 클라이언트에 전달했다. 

<br/>

여기서는 예시로 보여주기 위해서 검증 오류 객체들을 그대로 반환했다. 

실제 개발할 때는 이 객체들을 그대로 사용하지 말고, 필요한 데이터만 뽑아서 별도의 API 스펙을 정의하고 <br/>

그에 맞는 객체를 만들어서 반환해야 한다

```
[
    {
        "codes": [
            "Max.itemSaveForm.quantity",
            "Max.quantity",
            "Max.java.lang.Integer",
            "Max"
        ],
        "arguments": [
            {
                "codes": [
                    "itemSaveForm.quantity",
                    "quantity"
                ],
                "arguments": null,
                "defaultMessage": "quantity",
                "code": "quantity"
            },
            9999
        ],
        "defaultMessage": "9999 이하여야 합니다",
        "objectName": "itemSaveForm",
        "field": "quantity",
        "rejectedValue": 10000,
        "bindingFailure": false,
        "code": "Max"
    }
]
```

<br/>

### 검증 오류 요청 로그

```
API 컨트롤러 호출
검증 오류 발생, errors=org.springframework.validation.BeanPropertyBindingResult: 1 errors
Field error in object 'itemSaveForm' on field 'quantity': rejected value 
[99999]; codes 
[Max.itemSaveForm.quantity,Max.quantity,Max.java.lang.Integer,Max]; arguments 
[org.springframework.context.support.DefaultMessageSourceResolvable: codes 
[itemSaveForm.quantity,quantity]; arguments []; default message 
[quantity],9999]; default message [9999 이하여야 합니다]
```

<br/>

## @ModelAttribute vs @RequestBody

HTTP 요청 파리미터를 처리하는 `@ModelAttribute` 는 각각의 필드 단위로 세밀하게 적용된다. 

그래서 특정 필드에 타입이 맞지 않는 오류가 발생해도 나머지 필드는 정상 처리할 수 있었다.

<br/>

`HttpMessageConverter` 는 `@ModelAttribute` 와 다르게 각각의 필드 단위로 적용되는 것이 아니라,

전체 객체 단위로 적용된다.

<br/>

따라서 메시지 컨버터의 작동이 성공해서 `ItemSaveForm` 객체를 만들어야 `@Valid` , `@Validated` 가 적용된다.