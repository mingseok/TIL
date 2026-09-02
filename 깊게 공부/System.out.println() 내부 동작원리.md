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

`java.lang` 패키지는 컴파일 단계에서 암묵적으로 `import java.lang.*` 를 추가 해준다.

- 따라서, `java.lang` 패키지의 클래스들은 `import`를 하지 않아도 된다.

<br/>

`println()` 메서드는 `PrintStream` 클래스의 메서드이지만 코드 어디에도 `PrintStream` 이라는 이름은 찾을 수 없다.

```java
"System.out.println()" 대신 "PrintStream.println()"을 사용하면 컴파일 에러가 발생한다.
```

<br/>

## `println()` 메서드는 인스턴스 메서드이다

클래스를 통해 외부로 호출할 수 없다. 

```java
public void println(String x) {
    synchronized (this) {
        print(x);
        newLine();
    }
}
```

여기서 만약 `println()` 메서드를 사용하려면 `java.io` 패키지를 `import`하고 `PrintStream` 클래스에 대한 객체를 생성해야 한다. 

그러나 `System.out`은 `import` 조차 없이 `println()` 메서드를 호출한다.

<br/>

## out

```java
public final class System {
    public static final PrintStream out;
    
    ...
}
```

`out`은 → `System` 클래스의 `“static PrintStream”` 타입으로 되어 있는 변수 이름이다. → 즉, `out`은 `PrintStream` 타입이다.

<br/>

여기서 `static` 인 것은 `out` 하나뿐이다. `println()` 은 위에서 본 대로 인스턴스 메서드다.

정리하면 `System.out.println()` 은 이렇게 나뉜다.

```java
System.out   -> System 클래스의 static 필드에서 PrintStream 인스턴스를 꺼낸다
.println()   -> 꺼낸 그 인스턴스의 메서드를 호출한다
```

우리가 객체를 만들지 않아도 되는 이유는 `println` 이 `static` 이라서가 아니라,

`System` 이 이미 만들어둔 인스턴스를 `out` 에 담아두고 나눠주기 때문이다.

<br/>

## 궁금증!

```java
정말 그런지 바이트코드로 확인할 수 있을까?
```

앞의 `Hello` 를 컴파일해서 열어보면 명령어 두 개로 갈라져 있다.

```java
$ javap -c Hello.class

  public static void main(java.lang.String[]);
    Code:
       0: getstatic      // Field java/lang/System.out:Ljava/io/PrintStream;
       3: ldc            // String Hello
       5: invokevirtual  // Method java/io/PrintStream.println
       8: return
```

<br/>

- `getstatic` : `System` 의 `static` 필드인 `out` 을 꺼낸다

- `invokevirtual` : 꺼낸 인스턴스에 대고 `println` 을 호출한다

`invokevirtual` 은 인스턴스 메서드를 부를 때 쓰는 명령어다.

`static` 메서드였다면 `invokestatic` 이 찍혔을 것이다.

<br/>

실제 JDK 선언을 봐도 마찬가지다.

```java
$ javap java.lang.System | grep out
  public static final java.io.PrintStream out;

$ javap java.io.PrintStream | grep println
  public void println();          // static 이 없다
  public void println(int);
  public void println(java.lang.String);
```

<br/>

## final인데 값은 언제 채워지는가?

`out` 은 `static final` 인데 선언부에는 값을 대입하는 코드가 없다.

`final` 은 반드시 초기화해야 하는데 어떻게 통과하는 걸까.

<br/>

JVM이 시작될 때 네이티브 코드로 직접 채워 넣는다.

```java
$ javap -p java.lang.System | grep setOut
  public static void setOut(java.io.PrintStream);
  private static native void setOut0(java.io.PrintStream);
```

<br/>

`setOut0` 이 `native` 로 되어 있다. 자바 문법의 `final` 규칙 밖에서 값을 넣는 것이다.

그래서 `System.setOut()` 으로 출력 대상을 바꿔치기 할 수도 있다.

```java
System.setOut(new PrintStream(new FileOutputStream("log.txt")));
System.out.println("이제 파일로 나간다");
```

테스트에서 콘솔 출력을 가로채서 검증할 때 쓰는 방법이 이것이다.

<br/>

## println이 느린 이유

`println` 안에 `synchronized` 가 있는 것을 위에서 봤다.

여러 스레드가 동시에 찍어도 줄이 섞이지 않는 것은 이 덕분인데, 대신 느리다.

<br/>

`2만` 줄을 찍어서 `BufferedWriter` 와 비교해봤다.

```java
for (int i = 0; i < 20000; i++) {
    System.out.println("줄 " + i);
}
```

```java
BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
for (int i = 0; i < 20000; i++) {
    writer.write("줄 " + i);
    writer.newLine();
}
writer.flush();
```

<br/>

### 결과

```java
System.out.println : 37ms
BufferedWriter     : 4ms
```

<br/>

이유는 두 가지다.

- 줄마다 락을 잡았다 푼다

- `println` 은 줄이 끝날 때마다 바로 내보낸다. 모아뒀다 한 번에 보내지 않는다

`BufferedWriter` 는 버퍼에 모아뒀다가 `flush()` 할 때 한 번에 내보낸다.

알고리즘 문제에서 출력이 많을 때 `BufferedWriter` 를 쓰라는 이유가 이것이다.

<br/>

실무에서 `System.out.println` 대신 로거를 쓰는 이유도 겹친다.

레벨별로 끄고 켤 수 있고, 시간과 스레드 이름이 같이 찍히고, 비동기로 내보낼 수 있기 때문이다.
