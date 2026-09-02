## String 객체를 생성하는 방법과 내부 동작 원리




먼저 `String`은 단순한 타입이 아니고, 자바에서 구현된 클래스이다

<br/>

선언할 때는 변수에 값을 저장하는 방식이 아닌 하나의 객체를 생성하고, 해당 객체에 문자들을 연결시키는 방식입니다.

```java
String 변수 = new String("문자열");
String 변수 = "문자열";
```

<br/>

`String` 인스턴스는 한 번 생성되면, 그 값을 `읽기`만 할 수 있고, 변경할 수는 없습니다. 

```
이러한 객체를 `”불변 객체"` 라고 한다.
```

즉, 덧셈 연산자를 사용해서 문자열과 문자열을 더한다면, 새로운 String `인스턴스`가 `생성`되는 것이다.

<br/>

아래 예시를 보면 `str1`, `str2`를 더한 `str3`은 새로운 `인스턴스`인 것입니다.

```java
String str1 = "Hello";
String str2 = "JAVA";

String str3 = str1 + str2; // "HelloJAVA"
```

<br/>


## 두 가지 방법이 있습니다.

`String`은 다른 참조 자료형과 달리 `new`연산자 외에도, `리터럴` 생성 방식이 있습니다.

```java
String a = "hello"
String a = new String("hello");
```

<br/>

## 리터럴 생성 방식

만들어진 객체가 저장되는 공간이 바로 `String Pool` 이라는 곳입니다.

`String Pool`은 `힙 메모리`에 위치하고 있지만, `new`연산 역시 `힙 메모리`에 객체가 생성되는데, `String Pool`에 저장되지는 않습니다.

![이미지](/programming/img/입문392.PNG)


[사진 참고](https://madplay.github.io/post/java-string-literal-vs-string-object)



<br/>

## String의 리터럴 생성

`String`을 리터럴로 생성 시, 내부적으로 `intern()`메소드를 호출하게 됩니다.

<br/>

`intern()`메소드는 `String Pool`에서 찾아보고, 존재하면 해당 문자열의 주소값을 반환, 

존재하지 않으면 `String Pool`에 그 값을 생성하고, 새로운 주소값을 할당하여 반환해줍니다.

<br/>

즉, 리터럴 생성으로 동일한 값의 두 `String`을 생성한다면, 값만 같은 서로 다른 객체를 생성하는 것이 아니라, 

`String Pool` 내부에 존재하는 객체의 주소값을 할당 받게 되는 것입니다.

<br/>

## 코드 확인

```java
String a = "hello";
String b = "hello";
String c = new String("hello");
String d = new String("hello");

System.out.println(a == b); //true
System.out.println(b == c); //false
System.out.println(c == d); //false
```

<br/>

## 궁금증!

```java
"hel" + "lo" 도 결합이니까, 리터럴 "hello" 와는 다른 객체일까?
```

같은 객체다. 결합인데도 `true` 가 나온다.

```java
String a = "hello";
String b = "hel" + "lo";              // 둘 다 상수
System.out.println("상수끼리 결합 == 리터럴 : " + (a == b));

String part = "hel";
String c = part + "lo";               // 변수가 섞였다
System.out.println("변수가 섞이면        : " + (a == c));
System.out.println("intern() 하면        : " + (a == c.intern()));

final String fixed = "hel";           // final 이면?
String d = fixed + "lo";
System.out.println("final 변수면        : " + (a == d));

String e = new String("hello");
System.out.println("new 로 만들면        : " + (a == e));
```

<br/>

### 결과

```java
상수끼리 결합 == 리터럴 : true
변수가 섞이면        : false
intern() 하면        : true
final 변수면        : true
new 로 만들면        : false
```

<br/>

## 갈리는 기준은 "컴파일할 때 값이 정해지는가" 다

`"hel" + "lo"` 는 컴파일러가 미리 합쳐서 `"hello"` 라는 상수 하나로 만들어버린다.

바이트코드에는 `+` 연산이 아예 없고 `ldc "hello"` 만 남는다.

<br/>

`part + "lo"` 는 다르다. `part` 가 변수라서 실행해봐야 값을 안다.

그래서 실행 시점에 새 문자열을 만들고, 이건 Pool에 등록되지 않는다.

<br/>

`final String fixed = "hel"` 이 `true` 인 것이 재밌다.

`final` 이라 값이 절대 안 바뀌니까 컴파일러가 이것도 상수로 취급한다.

```java
컴파일할 때 값이 확정되면 -> Pool에 들어간다 -> == 가 true
실행해봐야 알면          -> 새 객체가 된다   -> == 가 false
```

<br/>

`intern()` 을 부르면 Pool에서 같은 값을 찾아 그 주소를 돌려주니 다시 `true` 가 된다.

원문에서 `리터럴은 내부적으로 intern()을 호출한다` 고 한 것이 이 얘기다.

<br/>

## new String()을 쓰지 말아야 하는 이유

```java
String e = new String("hello");
```

<br/>

이 한 줄이 만드는 객체가 몇 개인지 세어보면 답이 나온다.

- `"hello"` 리터럴 -> Pool에 하나 (이미 있으면 재사용)

- `new String(...)` -> Pool 밖 힙에 또 하나

<br/>

같은 값을 담은 객체가 두 개가 되는 셈이다.

게다가 `==` 비교가 어긋나면서 버그의 원인이 되기 쉽다.

<br/>

문자열은 그냥 리터럴로 쓰면 된다. `new String()` 을 써야 할 상황은 사실상 없다.

<br/>

## String Pool은 어디에 있는가

원문에 `힙 메모리에 위치하고 있다` 고 되어 있는데, 여기에 역사가 하나 있다.

```java
Java 6 이하 -> PermGen 안에 있었다. 크기가 고정이라 문자열을 많이 intern 하면 터졌다
Java 7 이상 -> Heap 으로 옮겼다. GC 대상이 되고, 크기 제한도 사실상 없어졌다
```

<br/>

`Java 7` 에서 옮긴 덕분에 Pool에 있는 문자열도 아무도 안 쓰면 GC가 치울 수 있게 됐다.

그 전에는 한 번 Pool에 들어간 문자열이 영영 안 죽어서 메모리를 계속 먹었다.

<br/>

Pool은 해시 테이블로 되어 있고 크기를 조절할 수도 있다.

```java
java -XX:StringTableSize=60013 MyApp
```

문자열을 대량으로 `intern()` 하는 프로그램이 아니면 건드릴 일은 없다.

<br/>

## 정리하면

```java
String a = "hello";              // Pool 에 있으면 그것을 쓴다
String b = "hel" + "lo";         // 컴파일할 때 "hello" 로 합쳐진다 -> a 와 같은 객체
String c = part + "lo";          // 실행할 때 만들어진다 -> 새 객체
String d = new String("hello");  // 무조건 새 객체 + Pool 에도 하나
```

값을 비교할 때는 언제나 `equals` 를 쓰면 이 복잡한 것을 신경 쓸 필요가 없다.

`==` 가 우연히 맞아떨어지는 경우가 있어서 헷갈릴 뿐이다.
