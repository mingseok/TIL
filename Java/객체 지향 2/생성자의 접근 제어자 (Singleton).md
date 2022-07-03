## 생성자의 접근 제어자

생성자에 접근 제어자를 사용함으로써 인스턴스의 생성을 제한할 수 있다.

생성자의 접근 제어자를 private으로 지정하면, 외부에서 생성자에 접근할 수 없으므로 인스턴스를 생성할 수 없게 된다.<br/> 그래도 클래스 내부에서는 인스턴스를 생성 할 수 있다.

```java
class Singleton {
	private Singleton() {
	...
	}
}
```

<br/>생성자를 통해 직접 인스턴스를 생성하지 못하게 하고, public 메서드를 통해 인스턴스에 접근하게 함으로써 <br/>사용할 수 있는 인스턴스의 개수를 제한 할 수 있다.

<br/>또 한가지는 생성자가 private인 클래스는 다른 클래스의 조상이 될 수 없다.

<br/>왜냐하면, 자손클래스의 인스턴스를 생성할때 조상클래스의 생성자를 호출해야 하는데, <br/>생성자 접근 제어자가 private이므로 자손클래스에서 호출하는 것이 불가능하기 때문이다.

<br/>

### 예제.

**예제.1-1 코드. → 예제 1-1 이랑 1-2 코드 비교하고 생각해보기.**

결국, 다른 클래스에서 생성하는 것을 막는다 어떻게? 생성자 private로 막는다. <br/>즉, private은 같은 클래스 내에서만 접근이 가능하기 때문에 겟 인스턴스로 들어온건 같은 클래스니깐 되는것이고,<br/> 그 밖에 다른 것들은 예를 들면, Singleton s = new Singleton(); 이런것들은 생성자 Singleton() 으로 들어가서 생성되기 <br/>때문에 안되는 것이다. (생성자가 private이기 때문) 

<br/>만약, private Singleton() { } 지우면 

<br/>그럼 main 메서드에서 싱글톤 객체 인스턴스를 생성할 수 있게 된다. 더이상 싱글톤이 아니게 되는거다.<br/> 이유는 디폴트 생성자가 자동으로 생성돼서 사용자가 Singleton이라는 객체 인스턴스를 생성할 수 있게 된다.

<br/>

1-1 일 같은 경우는 

첫번째.  private static Singleton s = new Singleton(); 에서 객체가 생성된다.

<br/>두번째.  Singleton s = Singleton.getInstance(); 에서 메서드로 들어간다.

<br/>세번째.  if (s == null) { 에서 s는 위에서 객체 생성 했기 때문에 null 이 아니므로 다시 돌아와서 끝난다. <br/>private Singleton() { 생성자로 가지 않는 이유는 처음에 객체 생성 했을때 다녀왔기 때문. <br/>그래서 바로 리턴 되는 것이다. (만약 private static Singleton s = new Singleton(); 가 s1, s2 더 있다면 계속 만들어 질것이다. 

```java
package test;

final class Singleton { // 상속 받을수 없다.
    private static Singleton s = new Singleton(); // 클래스 내부에만 가능한 클래스 변수

    private Singleton() {
        // ...
    }

    public static Singleton getInstance() { // 외부에도 가능한 클래스변수
        if (s == null) {
            s = new Singleton();
        }
        return s;
    }
}

public class aaa {
    public static void main(String[] args) {
        // Singleton s = new Singleton(); 에러 발생.
        Singleton s = Singleton.getInstance();
       
    }
}
```

**예제 1-2 코드**

첫번째. 처음부터 s에 만들어 지지 않는다. 여긴 선언만 했음.

<br/>두번째. 겟인스턴스 메서드로 들어가서 if문에 (null == null) 이 된다. 여기서 객체를 생성하면서 

private Singleton( ) {  를 들린다. 그리고 객체가 생성된다.

<br/>세번째. 그리고 다시 돌아와 return s; 로 리턴한다.

결국 여기서는 Singleton.getInstance( ); 로 들어가니깐 계속 생성이 되는것이다. <br/>겟인스턴스메서드가 있다면 무조건 객체를 생성할수 잇다고 생각하면된다.<br/> Singleton s = new Singleton(); 이렇게 할 경우 외부에서 받는거니깐 에러 발생.
```java
package test;

final class Singleton { // 상속 받을수 없다.
    private static Singleton s; // 클래스 내부에만 가능한 클래스 변수

    private Singleton() {
        // ...
    }

    public static Singleton getInstance() { // 외부에도 가능한 클래스변수
        if (s == null) {
            s = new Singleton();
        }
        return s;
    }
}

public class aaa {
    public static void main(String[] args) {
        // Singleton s = new Singleton(); 에러 발생.
        Singleton s = Singleton.getInstance();
       
      		
    }
}
```

<br/><br/>

>**Reference**
><br/>남궁 성 지음, 『자바의 정석』, 도우출판.
