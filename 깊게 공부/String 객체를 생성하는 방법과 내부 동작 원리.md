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
