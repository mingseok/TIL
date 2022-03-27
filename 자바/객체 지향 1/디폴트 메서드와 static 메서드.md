## Default Method

인터페이스는 기능에 대한 선언만 가능하기 때문에, 실제 코드를 구현한 로직은 포함될 수 없다. <br/>하지만 자바8에서 이러한 룰을 깨트리는 기능이 나오게 되었는데 <br/>그것이 Default Method(디폴트 메소드)이다. <br/>메소드 선언 시에 default를 명시하게 되면 인터페이스 내부에서도 로직이 포함된 메소드를 선언할 수 있다.

<br/>

- interface에서도 메소드 구현이 가능하다.
- 참조 변수로 함수를 호출할 수 있다.
- implements한 클래스에서 재정의가 가능하다.

<br/>

이런식이다.

```java
interface MyInterface { 
    default void printHello() { 
    	System.out.println("Hello World"); 
    } 
}
```
default라는 키워드를 메소드에 명시하게 되면 인터페이스 내부라도 코드를 작성할 수 있다.

<br/>

## static Method

- interface에서도 메소드 구현이 가능하다.
- 반드시 클래스 명으로 메소드를 호출해야 한다.
- 재정의 불가능!


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
><br/>siyoon210 - https://siyoon210.tistory.com/95
><br/>dahye_e - https://dahyeee.tistory.com/entry/JAVA-interface-default-static%EB%A9%94%EC%86%8C%EB%93%9C
