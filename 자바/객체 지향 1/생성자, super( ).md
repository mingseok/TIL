## 생성자, super( )

### 생성자란? 

클래스가 생성될때 자동으로 클래스와 똑같은 이름을 
가진 생성자가 실행 되도록 약속 되어 있다. <br/>
(그 어떤 메서드보다 먼저 실행된다)

<br/>만약 똑같은 이름의 메서드가 없다면?
자바는 내부적으로 클래스와 똑같은 이름의 메서드를 
자동으로 만들어 준다 <br/>
(내가 직접 만들어 놨다면 상관 없음)

<br/>

생성자는 기본적으로 객체를 생성하는 역할을 하고
또 그 객체를 생성하는 과정에서 최초로 수행해야 될 일이 있다면 <br/>
그것을 할 수 있도록 메서드를 정의 할 수 있는 기회를 줄 수 있는 것이다. 

<br/>

생성자는 어디 부분인가?


```java
Calculator c1 = new Calculator(10, 20);
c1.sum();         
c1.avg();

// 만약
class ConstructorDemo {

		public ConstructorDemo(int param1) { 
	    // 있다면
		// 이름은 같지만 매개변수가 있다면 기본 생성자가 아니다 !!
		// 매개변수란? 메서드안 괄호 안에 ( ) 변수가 있는걸 매개 변수라고 한다.  
}

public static void main(String[] args) {
     ConstructorDemo c = new ConstructorDemo();
	 // 이 코드를 실행 한다면 에러가 발생한다. 왜??
	 // 기본 생성자에 매개변수가 들어가 있기 때문
     // 이 코드를 실행하기 위해선 내가 직접 public ConstructorDemo( ) { } 메서드를 
     // 만들어 줘야한다. 그러면 에러 해결.

     // new ConstructorDemo(); 라고 되어 있는 것은 클래스이름과 같은
     // 기본 생성자는 안에가 비어 있어야한다. 하지만 이건 메서드에서 매개변수가
     // 있기 때문에 에러가 발생한것이다 
	      
  }
}
```

<br/>Calculator(10, 20) —> 이 부분이 '생성자' 라고 부른다.

인스턴스를 생성하는 '자' 

<br/>(설명을 더 하자면 앞에 'new' 가 앞에 붙어 있기 때문에 생성자를( Calculator( ) ) 이용해서 <br/>
Calculator 라는 클래스를 구체화 한 인스턴스를(c1) 만들수 있게 된것이다.)

<br/><br/>예제 프로그램

```java
package ddd;

public class Calcu {

	int left, right;

	public Calcu(int left, int right) { // 클래스와 동일한 이름을 가지고 있으니깐 
		// 기본 생성자라고 생각 할수 있지만 매개변수가 있기 때문에 생성자가 아니다 (컨스트럭터)
		this.left = left;
		this.right = right;
	}

	public void sum() {
		System.out.println(this.left + this.right);
	}

	public void avg() {
		System.out.println((this.left + this.right) / 2);
	}

	public static void main(String[] args) {

		Calcu c1 = new Calcu(10, 20);
		c1.sum();
		c1.avg();

		Calcu c2 = new Calcu(20, 40);
		c2.sum();
		c2.avg();

	}

}
```

<br/>결론

```java
public Calculator( ) { 

} 

// 만드는 이유는 클래스안에 클래스 이름과 같은 기본 생성자가 
// 아닌 다른 메서드가 있기 때문이다.
// 즉, 클래스 이름과 같은 메서드는 무조건 기본 생성자 이여야 하는데 
// 클래스 이름인 기본 생성자 자리를 뺏고 기본 생성자가 아닌 다른
// 메서드로 만들었기 때문이다. 원래 같으면 클래스 이름의 메서드를
// 만들지 않았다고 가정한다면 자동으로 컴파일 하면서 자바가 기본 생성자를 만든다. 
// 하지만 클래스 이름으로 기본 생성자가 아닌 다른 메서드를 만들었다면 자바는
// 자동으로 다른 이름의 기본 생성자로 만들어 줄수 없다. 

// 결론은 클래스와 같은 이름의 메서드를 만들었을 경우 자바가 자동으로
// 클래스 이름과 같은 기본 생성자를 만들어 줄수 없으니 
// 내가 직접 기본 생성자를 만들어 줘야 에러가 없어진다

```

### super( ) 란?

super( ) 라는 것은 상위 클래스에도 생성자가 있고 하위 클래스에도 생성자가 있어서 <br/>코드 중복을 줄여 주기 위하여 사용하는것이다.
 
<br/>super( ) 에 —> ( ) 는 부모 클래스의 생성자라는 뜻이 된다