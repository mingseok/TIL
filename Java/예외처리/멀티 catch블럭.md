## 멀티 catch블럭

JDK1.7부터 여러 catch 블럭을 ‘|’ 기호를 이용해서 하나의 catch블럭으로 합칠 수 있게 되었다.

```java
try {
   ...
} catch (ExceptionA | ExceptionB e) {
	e.printStackTrace();
}
```

만일 멀티 catch블럭의 ‘|’ 기호로 연결된 예외 클래스가 조상과 자손의 관계에 있다면 컴파일 에러가 발생한다.

<br/>설명 코드를 작성해보겠다. ( 주석 잘보기 )

<br/>왜냐면 두 예외 클래스가 조상과 자손의 관계에 있다면, <br/>그냥 조상 클래스만 써주는 것과 똑같기 때문이다. 불필요한 코드는 제거하라는 의미에서 에러가 발생하는 것이다.

```java
try {
   ...
} catch (ParentException | ChildException e) { // 에러 !!
		e.printStackTrace();
}
```

<br/>불필요한 코드 제거 한 코드 (위에 코드 수정)

```java
try {
   ...
} catch (ParentException e) { // 통과 !!
		e.printStackTrace();
}
```

<br/>멀티 catch 블럭 내에서는 실제로 어떤 에외가 발생한 것인지 알 수 없다.

<br/>그래서 참조변수 e 로 멀티 catch블럭에 ‘|’ 기호로 연결된 예외 클래스들의 <br/>공통 분모인 조상 예외 클래스에 선언된 멤버만 사용할 수 있다.

<br/>즉, 코드로 다시 설명하겠다.

```java
try {
  ...
} catch (ExceptionA | ExceptionB e) {
		e.methodA(); // 에러!! ExceptionA 에 선언된 methodA()는 호출 불가.
					 // 이유는. 하나의 참조변수에 어떤 객체가 들어올지 모르기 때문.
}
```

위 코드를 설명하면 ExA 클래스랑 ExB 클래스가 있는데 각각 서로 공통적이지 않은 <br/>해당 methodA() 와 methodB() 가 있다. 즉, 둘다 공통적인게 없을 수도 있다고 컴퓨터는 생각하기 때문이다.

<br/>공통적인 부분만 사용은 가능하다, ‘e’ 의 객체가 ExA 인지, ExB 인지 알 수 없으니 에러가 발생하는 것이다<br/> 참조변수 ‘e’로 사용 할 수 있는건 ExA 와 ExB 의 공통된 것만 사용 가능한 것이다.

<br/><br/>

>**Reference**
><br/>남궁 성 지음, 『자바의 정석』, 도우출판.
