## String, StringBuffer, StringBuilder의 차이는?

String은 불변의 속성을 가집니다.

<br/>

StringBuffer와 StringBuilder는 가변의 속성을 가집니다.

StringBuffer는 동기화를 지원하여 
멀티 쓰레드 환경에서 주로 사용됩니다.

<br/>

StringBuilder는 동기화를 지원하지 않아 
싱글 쓰레드 환경에서 주로 사용합니다.

```java
String        -> 못 바꾼다 (바꾸면 새 객체가 만들어진다)
StringBuffer  -> 바꿀 수 있다 + 동기화 O
StringBuilder -> 바꿀 수 있다 + 동기화 X
```

<br/>

## 불변이라는 말은 무슨 뜻인가?

`String` 안을 열어보면 글자를 담아둔 배열이 있는데, 이 배열이 `final` 로 잡혀 있고 밖으로 내주지도 않는다.

```java
public final class String {
    private final byte[] value;
    private final byte coder;
    ...
}
```

그래서 `String` 에는 애초에 "고친다"는 동작이 없다.

`concat`, `replace`, `toUpperCase` 전부 원본은 그대로 두고 새 `String` 을 만들어서 돌려주는 것이다.

```java
String a = "hello";
a.concat(" world");
System.out.println(a);   // hello  (a는 그대로다)

a = a.concat(" world");  // 새로 받은 것을 다시 담아야 바뀐 것처럼 보인다
```

<br/>

## StringBuffer와 StringBuilder는 형제다

이 둘은 남남이 아니라 같은 부모를 상속받는다.

```java
class StringBuilder extends AbstractStringBuilder
class StringBuffer  extends AbstractStringBuilder

abstract class AbstractStringBuilder {
    byte[] value;    // 실제 글자가 담기는 배열
    byte coder;
    int count;       // 지금까지 채운 길이
}
```

핵심은 `value` 와 `count` 두 개다.

`append` 를 하면 새 객체를 만드는 것이 아니라, `count` 가 가리키는 자리에 글자를 이어 붙이고 `count` 만 늘린다.

배열이 꽉 차면 그때 더 큰 배열을 만들어 옮겨 담는다.

```java
String        -> 붙일 때마다 새 배열, 새 객체
StringBuilder -> 배열 하나를 계속 재사용, count만 늘어난다
```

<br/>

## 그럼 두 형제의 차이는 딱 하나다

`javap` 로 `StringBuffer` 를 열어보면 답이 바로 나온다.

```java
public final class java.lang.StringBuffer extends java.lang.AbstractStringBuilder {
  public synchronized int length();
  public synchronized java.lang.StringBuffer append(java.lang.String);
  public synchronized java.lang.String toString();
}
```

`StringBuilder` 쪽에는 `synchronized` 가 하나도 없다.

기능은 완전히 같고, 메서드마다 락을 잡느냐 마느냐의 차이만 있는 것이다.

<br/>

## 생각해보기 ex1)

스레드 두 개가 같은 객체에 `10000` 번씩 붙이면 길이가 얼마가 될까?

```java
public class Race {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 3; i++) {
            System.out.println("StringBuilder 길이 = " + run(new StringBuilder()));
        }
        for (int i = 0; i < 3; i++) {
            System.out.println("StringBuffer  길이 = " + run(new StringBuffer()));
        }
    }

    static int run(CharSequence target) throws Exception {
        Runnable job = () -> {
            for (int i = 0; i < 10000; i++) {
                if (target instanceof StringBuilder sb) sb.append("a");
                else ((StringBuffer) target).append("a");
            }
        };
        Thread t1 = new Thread(job);
        Thread t2 = new Thread(job);
        t1.start(); t2.start();
        t1.join(); t2.join();
        return target.length();
    }
}
```

<br/>

### 결과

```java
StringBuilder 길이 = 12658
StringBuilder 길이 = 11046
Exception in thread "Thread-5" java.lang.ArrayIndexOutOfBoundsException:
        arraycopy: last destination index 147 out of bounds for byte[142]
	at java.base/java.lang.AbstractStringBuilder.append(AbstractStringBuilder.java:592)
StringBuilder 길이 = 10000

StringBuffer  길이 = 20000
StringBuffer  길이 = 20000
StringBuffer  길이 = 20000
```

`20000` 이 나와야 하는데 `StringBuilder` 는 매번 다른 숫자가 나온다.

`count` 를 읽고 -> 글자를 넣고 -> `count` 를 늘리는 사이에 다른 스레드가 끼어들기 때문이다.

심하면 배열 크기를 늘리는 도중에 끼어들어서 위처럼 예외까지 터진다.

<br/>

## 궁금증!

```java
그러면 StringBuffer를 쓰면 무조건 안전한 것일까?
```

아니다. 안전한 범위는 `메서드 하나` 까지다.

```java
public class Compound {
    public static void main(String[] args) throws Exception {
        StringBuffer sb = new StringBuffer();

        Runnable job = () -> {
            if (sb.length() == 0) {
                sleep();                 // 확인과 추가 사이의 틈을 눈에 보이게 벌린 것
                sb.append("첫 번째");
            }
        };

        Thread t1 = new Thread(job);
        Thread t2 = new Thread(job);
        t1.start(); t2.start();
        t1.join(); t2.join();

        System.out.println("결과 -> " + sb);
    }

    static void sleep() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

<br/>

### 결과

```java
결과 -> 첫 번째첫 번째
```

`length()` 도 `append()` 도 각각은 `synchronized` 라서 안전하다.

문제는 `비어 있는지 확인` 하고 나서 `추가` 하기까지의 사이다. 그 틈에서 락이 한 번 풀린다.

그 사이에 다른 스레드가 들어와서, 두 스레드가 모두 "아직 비어 있네" 라고 판단해버린 것이다.

```java
메서드 하나로 끝나는 동작   -> StringBuffer가 알아서 지켜준다
확인하고 나서 바꾸는 동작   -> 직접 synchronized로 묶어야 한다
```

<br/>

## 그런데 실무에서 StringBuffer를 볼 일이 거의 없다

`10만` 번씩 붙여서 시간을 재보면 이렇다.

```java
String +      : 336ms
StringBuilder : 0ms
StringBuffer  : 1ms
```

`String +` 만 압도적으로 느리고, 나머지 둘은 차이가 보이지도 않는다.

락을 두고 다투는 스레드가 없으면 `synchronized` 비용은 거의 들지 않기 때문이다.

<br/>

그러면 그냥 안전한 `StringBuffer` 를 쓰면 되지 않나 싶은데, 실제로는 그럴 일이 잘 없다.

`StringBuilder` 는 보통 메서드 안에서 만들고, 메서드 안에서 `toString()` 하고, 그대로 버린다.

지역 변수이기 때문에 애초에 다른 스레드가 볼 수가 없는 것이다.

그리고 진짜로 공유해야 하는 상황이라면, 위 궁금증에서 봤듯이 `StringBuffer` 만으로는 부족해서 어차피 직접 묶어야 한다.

<br/>

## 그럼 "+" 는 무조건 쓰면 안 되는가?

그건 또 아니다. 한 줄짜리 결합은 컴파일러가 알아서 처리해준다.

```java
String simple(String a, String b) {
    return a + b + "!";
}
```

이걸 `javap -c` 로 열어보면 이렇게 되어 있다.

```java
  0: aload_1
  1: aload_2
  2: invokedynamic #7,  0   // makeConcatWithConstants
  7: areturn
```

`+` 세 개가 호출 한 번으로 합쳐져 있다.

문제가 되는 것은 반복문 안에서 붙일 때다.

```java
String result = "";
for (String word : words) {
    result += word;      // 한 바퀴 돌 때마다 새 String이 만들어진다
}
```

이 경우는 반복문 안에 `makeConcatWithConstants` 가 그대로 들어가 있어서, 한 바퀴마다 새 문자열이 통째로 만들어진다.

앞에서 잰 `336ms` 가 바로 이 코드였다.

```java
한 줄 결합이면 "+" 도 괜찮다. 반복문 안에서 붙는다면 StringBuilder를 쓰자
```
