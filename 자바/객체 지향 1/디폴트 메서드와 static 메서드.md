## 디폴트 메서드와 static메서드

아래 왼쪽과 같이 newMethod( ) 라는 추상 메서드를 추가하는 대신, <br/>오른쪽과 같이 디폴트 메서드를 추가하면, 기존의 MyInterface를 구현한 클래스를 변경하지 않아도 된다.

<br/>즉, 조상 클래스에 새로운 메서드를 추가한 것과 동일해 지는 것이다.

![이미지](/programming/img/디폴트.PNG)


<br/>


![이미지](/programming/img/디폴트2.PNG)

<br/><br/>위의 규칙이 외우기 귀찮으면, 그냥 필요한 쪽의 메서드와 같은 내용으로 오버라이딩 해버리면 그만이다.


<br/>

### 예제.
```java
class DefaultMethodTest {
	public static void main(String[] args) {
		Child c = new Child();
		c.method1();
		c.method2();
		MyInterface.staticMethod(); 
		MyInterface2.staticMethod();
	}
}

class Child extends Parent implements MyInterface, MyInterface2 {
	public void method1() {	
		System.out.println("method1() in Child"); // 오버라이딩
	}			
}

class Parent {
	public void method2() {	
		System.out.println("method2() in Parent");
	}
}

interface MyInterface {
	default void method1() { 
		System.out.println("method1() in MyInterface");
	}
	
	default void method2() { 
		System.out.println("method2() in MyInterface");
	}

	static  void staticMethod() { 
		System.out.println("staticMethod() in MyInterface");
	}
}

interface MyInterface2 {
	default void method1() { 
		System.out.println("method1() in MyInterface2");
	}

	static  void staticMethod() { 
		System.out.println("staticMethod() in MyInterface2");
	}
}

출력값.
method1() in Child
method2() in Parent
staticMethod() in MyInterface
staticMethod() in MyInterface2
```



<br/><br/>

>**Reference**
><br/>남궁 성 지음, 『자바의 정석』, 도우출판.