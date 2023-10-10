## System.out.println() 내부 동작원리

평소엔 그냥 콘솔에 출력만 했었지, 정확히 어떤 의미인지 모르고 사용해 왔다.

그렇기에, 이번에 `내부 동작이 어떤지?`정리를 하려고 한다.

```java
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello");
    }
}
```

<br/><br/>

## System은 java.lang 패키지에 속하는 클래스다.

`java.lang` 패키지는 컴파일 단계에서 암묵적으로 `import java.lang.*` 를 추가 해준다.

- 따라서 우리는 `import`를 하지 않고 바로 쓸 수 있는 것이다

<br/>

`println()` 메서드는 `PrintStream` 클래스의 메서드이지만 

코드 어디에도 `PrintStream` 이라는 이름은 찾을 수 없다.

```java
"System.out.println()" 대신 "PrintStream.println()"을 사용하면 컴파일 에러가 발생한다.
```

<br/><br/>

## `println()` 메서드는 인스턴스 메서드이다

클래스를 통해 외부로 호출할 수 없다. 

```java
public void println(String x) {
    synchronized (this) {
        print(x);
        newLine();
    }
}
```

여기서 만약 `println()` 메서드를 사용하려면 `java.io` 패키지를 `import`하고 

<br/>

`PrintStream` 클래스에 대한 객체를 생성해야 한다. 

그러나 `System.out`은 `import` 조차 없이 `println()` 메서드를 호출한다.

<br/><br/>

## **out**

자바에서는 점 표기법`(”.”)`을 통해 클래스의 속성에 접근한다. 

즉 `System.out`에서 `out`은 `”System"` 클래스의 `필드` 또는 `메서드`이다.

```java
public final class System {
    ...
    
    public static final PrintStream out = null;
}
```

`System` 클래스는 `PrintStream` 타입의 `out`이라는 이름의 필드 변수를 가집니다.

<br/>

### 포인트는

`PrintStream` 클래스는 `println()` 메서드를 포함하기 때문에 

우리는 `System.out` 객체를 통해 `println()` 메서드를 호출할 수 있게 되는 것입니다.