## isEqualTo()



이 Assertions는 org.assertj.core.api.Assertions 클래스다 실수하지말자. 

assertThat메소드가 여기에 들어있다.


<br/>

### 테스트코드중 단골로 쓰는 테스트방식이다. 

assertThat()으로, 비교할 대상을 설정하고 isEqualTo()로 사용자가 생각하는 값을 비교한뒤 그게 맞는지 검사하는 테스트다.

<br/>

### 예를들어, 

내가 10만원 상품의 가격을 Discount메소드로  10% 할인을 적용한값 Value값을 테스트하려면,

 

Value값은 9만원이 나와야할걸 개발자 입장은 알고있으니, Assertions.assertThat(Value).isEqualTo(90000); 을 입력하면 

테스트가 통과하는걸 볼수있을것이다.