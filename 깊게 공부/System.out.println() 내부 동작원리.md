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

<br/>

## System은 java.lang 패키지에 속하는 클래스다.

`java.lang` 패키지는 컴파일 단계에서 암묵적으로 `import java.lang.*` 를 추가 해준다.

- 따라서, `java.lang` 패키지의 클래스들은 `import`를 하지 않아도 된다.

<br/>

`println()` 메서드는 `PrintStream` 클래스의 메서드이지만 코드 어디에도 `PrintStream` 이라는 이름은 찾을 수 없다.

```java
"System.out.println()" 대신 "PrintStream.println()"을 사용하면 컴파일 에러가 발생한다.
```

<br/>

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

여기서 만약 `println()` 메서드를 사용하려면 `java.io` 패키지를 `import`하고 `PrintStream` 클래스에 대한 객체를 생성해야 한다. 

그러나 `System.out`은 `import` 조차 없이 `println()` 메서드를 호출한다.

<br/>

## out

```java
public final class System {
    public static final PrintStream out = null;
    
    ...
}
```

`out`은 → `System` 클래스의 `“static PrintStream”` 타입으로 되어 있는 변수 이름이다. → 즉, `out`은 `PrintStream` 타입이다.

그러므로, `println()`이라는 메서드는 `PrintStream` 클래스에 선언되어 있으며 `static` 메서드이다.

<br/>

`out` 이라는 클래스 변수와 `println()`이라는 메서드 모두 `static`으로 선언되어 있기 때문에, 

우리는 별도의 클래스 `객체 생성 필요 없이` 사용할 수 있었던 것이다.
