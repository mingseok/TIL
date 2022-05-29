## Exception 처리 test

assertThatThrownBy()라는 예외처리를 가독성 있게 테스트할 수 있는 함수가 제공된다.


```java

    @Test
    void notFoundMessageCode() {

        // assertThatThrownBy() 는 예외 발생할때 사용함
        Assertions.assertThatThrownBy(() -> ms.getMessage("no_code", null, null))
                .isInstanceOf(NoSuchMessageException.class);
    }

```    

람다식 안에서 그냥 throw new Exception()을 하여 단순하게 바로 예외를 던졌는데 실제로는 <br/>저 식안에 예외가 발생하는 코드를 넣으면 되겠다.


<br/><br/>


이렇게만 작성한다면 NoSuchMessageException 예외가 터지게 되는 것이다.

```java

    @Test
    void notFoundMessageCode() {
        ms.getMessage("no_code", null, null)
    }

```