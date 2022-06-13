## Bean Validation - 소개

검증 기능을 지금처럼 매번 코드로 작성하는 것은 상당히 번거롭다. 

특히 특정 필드에 대한 검증 로직은 대부분 빈 값인지 아닌지, 특정 크기를 넘는지 

아닌지와 같이 매우 일반적인 로직이다. 다음 코드를 보자.

<br/>

bean Validation 은 이렇다.

```java
public class Item {

		private Long id;

		@NotBlank // null 이면 안되고, 공백이면 안되고, 빈 문자열이면 안된다.
		private String itemName;

		@NotNull
		@Range(min = 1000, max = 1000000)
		private Integer price; @NotNull

		@Max(9999)
		private Integer quantity;
		//...
}
```

이런 검증 로직을 모든 프로젝트에 적용할 수 있게 공통화하고, 

표준화 한 것이 바로 Bean Validation 이다.

Bean Validation을 잘 활용하면, 애노테이션 하나로 검증 로직을 매우 편리하게 적용할 수 있다.

<br/>

### 의존관계 추가

Bean Validation을 사용하려면 다음 의존관계를 추가해야 한다.

build.gradle

```java
implementation 'org.springframework.boot:spring-boot-starter-validation'
```

<br/>

## bean Validation 시작

### 검증 애노테이션

`@NotBlank` : 빈값 + 공백만 있는 경우를 허용하지 않는다.

`@NotNull` : null 을 허용하지 않는다.

`@Range(min = 1000, max = 1000000)` : 범위 안의 값이어야 한다.

`@Max(9999)` : 최대 9999까지만 허용한다.

<br/>

### 검증기 생성

다음 코드와 같이 검증기를 생성한다. 이후 스프링과 통합하면 우리가 직접 이런 코드를 

작성하지는 않으므로, 이렇게 사용하는구나 정도만 참고하자.

스프링이랑 같이 쓰면 생성하고 이렇게 사용하지 않는다.

```java
ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
Validator validator = factory.getValidator();
```

<br/>

### 검증 실행

검증 대상( item )을 직접 검증기에 넣고 그 결과를 받는다. 

Set 에는 ConstraintViolation 이라는 검증 오류가 담긴다. 

따라서 결과가 비어있으면 검증 오류가 없는 것이다.

```java
Set<ConstraintViolation<Item>> violations = validator.validate(item);
```

<br/>

테스크코드
```java
package hello.itemservice.validation;

import hello.itemservice.domain.item.Item;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class BeanVaildationTest {

    @Test
    void beanVaildation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Item item = new Item();

        // 오류 3개가 쭉 발생 해야 되는 것이다.
        item.setItemName(" "); // 공백
        item.setPrice(0);
        item.setQuantity(10000);

        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        for (ConstraintViolation<Item> violation : violations) {
            System.out.println("violation = " + violation);
            System.out.println("violation = " + violation.getMessage());
        }

    }

}

출력
violation = ConstraintViolationImpl{interpolatedMessage='1000에서 1000000 사이여야 합니다', propertyPath=price, rootBeanClass=class hello.itemservice.domain.item.Item, messageTemplate='{org.hibernate.validator.constraints.Range.message}'}
violation = 1000에서 1000000 사이여야 합니다
violation = ConstraintViolationImpl{interpolatedMessage='공백일 수 없습니다', propertyPath=itemName, rootBeanClass=class hello.itemservice.domain.item.Item, messageTemplate='{javax.validation.constraints.NotBlank.message}'}
violation = 공백일 수 없습니다
violation = ConstraintViolationImpl{interpolatedMessage='9999 이하여야 합니다', propertyPath=quantity, rootBeanClass=class hello.itemservice.domain.item.Item, messageTemplate='{javax.validation.constraints.Max.message}'}
violation = 9999 이하여야 합니다
```