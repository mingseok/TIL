## Bean Validation - 한계와 groups

수정시 요구사항

등록시에는 quantity 수량을 최대 9999까지 등록할 수 있지만 수정시에는 

수량을 무제한으로 변경할 수 있다.

<br/>등록시에는 id 에 값이 없어도 되지만, 수정시에는 id 값이 필수이다.

수정 요구사항 적용 수정시에는 `Item` 에서 `id` 값이 필수이고, 

`quantity` 도 무제한으로 적용할 수 있다.

```java
package hello.itemservice.domain.item;

@Data
public class Item {
 
	@NotNull //수정 요구사항 추가
	private Long id;
 
	@NotBlank
	private String itemName;
 
	@NotNull
	@Range(min = 1000, max = 1000000)
  private Integer price;
	
	@NotNull
  //@Max(9999) //수정 요구사항 추가
  private Integer quantity;
      //...
	}
```



그런데 수정은 잘 동작하지만 등록에서 문제가 발생한다.

<br/>등록시에는 id 에 값도 없고, quantity 수량 제한 최대 값인 9999도 적용되지 않는 문제가 발생한다.

등록시 화면이 넘어가지 않으면서 다음과 같은 오류를 볼 수 있다.

```java
'id': rejected value [null];
```

왜냐하면 등록시에는 id 에 값이 없다. 

<br/>따라서 `@NotNull` id 를 적용한 것 때문에 검증에 실패하고 다시

폼 화면으로 넘어온다. 결국 등록 자체도 불가능하고, 수량 제한도 걸지 못한다.

<br/>결과적으로 item 은 등록과 수정에서 검증 조건의 충돌이 발생하고, 

등록과 수정은 같은 BeanValidation 을 적용할 수 없다.

<br/>

>**Reference** <br/>스프링 MVC 2편 - 백엔드 웹 개발 활용 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2