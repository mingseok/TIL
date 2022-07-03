## 제어자 - static, final


제어자는 클래스, 변수 또는 메서드의 선언부에 함께 사용되어 부가적인 의미를 부여한다.

| 접근 제어자 | public  | protected | default | private |
| --- | --- | --- | --- | --- |
| 그 외 | static  | final | native | ... |

<br/>제어자는 클래스나 멤버변수와 메서드에 주로 사용되며, <br/>하나의 대상에 대해서 여러 제어자를 조합하여 사용하는 것이 가능하다.

<br/>

## static - 클래스의 공통적인

static은 **‘클래스의’** 또는 **‘공통적인’** 의 의미를 가지고 있다.

<br/>인스턴스변수는 하나의 클래스로부터 생성되었더라도 각기 다른 값을 유지하지만,  <br/>클래스변수는 인스턴스에 관계없이 같은 값을 갖는다. 

<br/>static 이 붙은 멤버변수와 메서드, 그리고 초기화 블럭은 인스턴스가 아닌 <br/>클래스에 관계된 것이기 때문에 **인스턴스를 생성하지 않고도 사용할 수 있다.**

<br/>인스턴스메서드와 static메서드의 근복적인 **차이는 메서드 내에서 인스턴스 멤버를 사용하는가의 여부에 있다.**

<br/>

**static 이 사용될 수 있는 곳 - 멤버변수, 메서드, 초기화 블럭**

![이미지](/programming/img/static.PNG)


<br/>인스턴스 멤버를 사용하지 않는 메서드는 static 을 붙여서 static 메서드로 선언하는 것을 고려해보자. <br/>static 메서드로 하는 것이 인스턴스를 생성하지 않고도 호출이 가능해서 더 편리하고 속도도 더 빠르다.

```java
class StaticTest {
	static int width = 200;  // 클래스 변수(static변수)
	static int height = 120; // 클래스 변수(static변수)

	static {
	   // static 변수의 복작한 초기화 수행
	}

	static int max(int a, int b) { // 클래스 메서드(static메서드)
		return a > b ? a : b;
	}
}
```

<br/><br/>

## final - 마지막의, 변경될 수 없는

변수에 사용되면 값을 변경할 수 없는 상수가 되며, 메서드에 사용되면 오버라이딩을 할 수 없게 <br/>되고 클래스에 사용하면 자신을 확장하는 자손클래스를 정의 하지 못하게 된다.

<br/>

**final이 사용될 수 있는 곳 - 클래스, 메서드, 멤버변수, 지역변수**

| 제어자 | 대상 | 의미 |
| --- | --- | --- |
| final | 클래스 | 변경될 수 없는 클래스, 확장될 수 없는 클래스가 된다. <br/>그래서 final로 지정된 클래스는 다른 클래스의 조상이 될 수 없다. |
| final | 메서드 | 변경될 수 없는 메서드, final로 지정된 메서드는 오버라이딩을 통해 재정의 될 수 없다. |
| final | 멤버변수 | 변수 앞에 final이 붙으면, 값을 변경할 수 없는 상수가 된다. |
| final | 지역변수 | 변수 앞에 final이 붙으면, 값을 변경할 수 없는 상수가 된다. |

<br/>

```java
final class FinalTest { // 조상이 될 수 없는 클래스
			final int MAX_SIZE = 10; // 값을 변경할 수 없는 멤버변수 (상수)

			final void getMaxSize() { // 오버라이딩할 수 없는 메서드 (변경불가)
						final int LV = MAX_SIZE; // 값을 변경할 수 없는 지역변수 (상수)
						return MAX_SIZE;
			}
}
```

<br/>

### 생성자를 이용한 final멤버 변수의 초기화

일반적으로 선언과 초기화를 동시에 하지만, 인스스변수의 경우 생성자에서 초기화 되도록 할 수 있다. 

인스턴스 마다 final이 붙은 멤버변수가 다른 값을 갖도록 하는 것이 가능하다.

예제.
```java
class Card {
    final int NUMBER;
    final String KIND;
    static int width = 100;
    static int height = 250;

    Card(String kind, int num) {
        KIND = kind;
        NUMBER = num;
    }

    Card() {
        this("HEART", 1);
    }

    public String toString() {
        return KIND + " " + NUMBER;
    }

}

class test {
    public static void main(String[] args) {
        Card c = new Card("HEART", 10);
        // c.NUMBER = 5; 에러 !!
        System.out.println(c.KIND);
        System.out.println(c.NUMBER);
        System.out.println(c); // System.out.println(c.toString());

    }
}

출력값.
HEART
10
HEART 10
```


<br/><br/>

>**Reference**
><br/>남궁 성 지음, 『자바의 정석』, 도우출판.
