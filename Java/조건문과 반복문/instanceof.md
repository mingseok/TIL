## instanceof

실제 타입을 알아보기 위해 instanceof 연산자를 사용한다.

<br/>조건문에 사용되며, instanceof의 **왼쪽에는 참조변수를 오른쪽에는 타입(클래스명)이 피연산자**로 위치한다. 

<br/>연산의 결과로 boolean값인 true와 false 중의 하나를 반환 한다.

<br/>true를 얻었다는 것은 **참조변수가 검사한 타입으로 형변환이 가능**하다는 것을 뜻한다.

<br/>instanceof 연산자를 이용해서 참조 변수 c가 가리키고 있는 인스턴스의 타입을 체크하고,
<br/>조상타입의 참조변수로 자손타입의 인스턴스를 참조할 수 있기 때문.

<br/>

### instanceof 예제.

생성된 인스턴스는 FireEngine 타입인도, Object타입과 Car타입의 instanceof 에서도 true를 결과로 얻었다.

<br/>이유는, FireEngine 클래스는 Object클래스와 Car클래스의 **자손 클래스이므로** 조상의 멤버들을 상속 받았기 때문에,<br/> **FireEngine  인스턴스는 Object와 Car인스턴스를 포함하고 있는 셈이기 때문이다.**

<br/>instanceof 연산의 **결과가 true라는 것은 검사한 타입으로 형변환을 해도 아무런 문제가 없다는 뜻이다.**

```java
package test;

class Car {
}

class FireEngine extends Car {
}

public class Test1 {

	public static void main(String[] args) {

		FireEngine fe = new FireEngine();

		if (fe instanceof FireEngine) {
			System.out.println("FireEngine");
		}

		if (fe instanceof Car) {
			System.out.println("Car");
		}

		if (fe instanceof Object) {
			System.out.println("Object");
		}

		System.out.println(fe.getClass().getName()); // 클래스의 이름을 출력.

	}

}

출력값.
FireEngine
Car
Object
test.FireEngine
```

### 어떤 타입에 대한 instanceof 연산의 결과가 true라는 것은 <br/>검사한 타입으로 형변환이 가능하다는 것을 뜻한다.


<br/><br/>

>**Reference**
><br/>남궁 성 지음, 『자바의 정석』, 도우출판.
