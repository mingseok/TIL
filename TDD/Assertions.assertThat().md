## `Assertions.**assertThat**()`

### **Assertions는 org.assertj.core.api.Assertions 클래스이다. 다른거 사용하면 안됨!**

검증을 하기 위해서 이다.

<br/>내가 new Member(); 으로 객체 생성한거랑 DB에서 꺼낸거랑 똑같으면 확인 하는 것이다.

같으면 참이다.

<br/>Assertions.assertThat 사용하면 member랑 result 가 똑같은지 확인 시켜준다.

만약 isEqualTo(null)로 바꾼다면 안됨 !! 해보기.

<br/>

### isEqualTo()를 사용하여 해당 객체의 참조가 같을 경우 테스트가 성공합니다.

