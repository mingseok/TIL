## 래퍼(wrapper) 클래스 / 변환하는 방법

### 래퍼 클래스는 기본형 값을 감싸는 클래스를 말한다.

8개의 기본형을 객체로 다뤄야할 때 사용하는 클래스.

8개의 기본형은 객체가 아니다. 즉, 참조형이 아닌것이다.

<br/>

하지만 기본형을 객체처럼 다뤄야 할 때가 있다. 

그럴때 사용하는 것이 래퍼 클래스이다.


<br/>Integer 라는 래퍼 클래스라고 한다.

```java
public final class Integer extends Number implements Comparable {
      
      private int value; // 기본형(int)을 감싸고 있는 것이다.

      ...
}
```

<br/>char 랑 int 만 다르고 나머지는 맨 앞 소문자를 대문자로 바꾼게 끝이다.

| 기본형 | 래퍼클래스 | 생성자 | 활용예 |
| --- | --- | --- | --- |
| boolean | Boolean | Boolean(boolean value) | Boolean b = new Boolean(true); |
| char | Character | Charcter( char value) | Character c =new Character('a); |
| byte | Byte | Byte (byte value) | Byte b = new Byte(10); |
| short | Short |  Short (short value) | Short s =new Short(10); |
| int | Integer | Integer (int value ) | Integer i =new Integer(100); |
| long | Long | Long(long value ) | Long l = new Long(100); |
| float | Float | Float (double value) | Float f =new Float(1.0); |
| double | Double | Double (double value) | Double d =new Double(1.0); |


<br/><br/>

## Number 클래스란?

모든 숫자 래퍼 클래스의 조상이다.

그리고 Long 보다 큰것은 BigInteger 이고, Double 보다 큰것은 BigDecimal 이다.

그리고 안에 메서드를 보면 래퍼 객체를 → 기본형으로 변경하는 것이다.

예를 들면, new Integer(100) 래퍼 클래스 객체에서 int 100으로 변경하고 싶다면 

밑에 처럼 intValue() 메서드를 사용하면 되는 것이다.

![이미지](/programming/img/래퍼클래스.PNG)


<br/><br/>

## 문자열을 숫자로 변환하는 다양한 방법

```java
// 래퍼 클래스를 기본형으로 바꾸는 것
// 문자열을 이용해서 래퍼 클래스 객체를 만들고 
// 그걸 intValue() 메서드를 이용해 바꾸는 것이다.

// 만약, Long 타입 래퍼 클래스라면 longValue() 메서드를 사용하면 되는 것이다.
int i = new Integer("100").intValue(); 

int i2 = Integer.parseInt("100") // 문자열을 숫자로 변환하는 것.

Integer i3 = Integer.valueOf("100"); // 래퍼클래스로 변환 가능하다. 그리고 int도 가능하다.
```

<br/>

## 문자열 → 기본형으로 바꾸는 방법

그리고 Integer.valueOf(”100”); 이렇게 사용해도 가능하다.

```java
byte   b = Byte.parseByte("100");
short  s = Short.parseShort("100"); 
int    i = Integer.parseInt("100");
long   l = Long.parseLong("100");
float  f = Float.parseFloat("100"); 
double d = Double.parseDouble("100");
```

<br/>

## 문자열 → 래퍼 클래스로 바꾸는 방법

그리고 Byte b = new Byte(”100”); 도 사용한다.

```java
Byte     b = Byte.valueOf("100");
Short    s = Short.valueOf("100"); 
Integer  i = Integer.valueOf("100");
Long     l = Long.valueOf("100");
Float    f = Float.valueOf("100"); 
Double   d = Double.valueOf("100");
```

<br/>

## 숫자 → 문자열로 변환하는 방법
```java
int num = 100;
String str;
str = Integer.toString(num);
```

<br/><br/>

>**Reference**
><br/>남궁 성 지음, 『자바의 정석』, 도우출판.

<br/>

## 궁금증!

```java
parseInt 와 valueOf 가 무엇이 다른가
```

반환 타입이 다르다.

```java
Integer.parseInt("10")   ->  int      (원시형)
Integer.valueOf("10")    ->  Integer  (객체)
```

<br/>

`valueOf` 안을 보면 `parseInt` 를 부른다.

```java
public static Integer valueOf(String s) {
    return Integer.valueOf(parseInt(s, 10));
}
```

<br/>

그리고 캐시를 확인한다.

```java
public static Integer valueOf(int i) {
    if (i >= -128 && i <= IntegerCache.high) {
        return IntegerCache.cache[i + 128];      // 미리 만들어둔 것
    }
    return new Integer(i);
}
```

<br/>

앞의 오토박싱 & 언박싱 글에서 본 그 캐시다.

<br/>

원시형이 필요하면 `parseInt` 를 쓰는 게 낫다.

`valueOf` 를 쓰면 객체를 만들었다가 다시 언박싱하는 셈이 된다.

<br/>

## 변환 실패는 예외로 나온다

```java
Integer.parseInt("abc")     ->  NumberFormatException
Integer.parseInt("")        ->  NumberFormatException
Integer.parseInt(null)      ->  NumberFormatException  (NPE 가 아니다)
Integer.parseInt(" 10 ")    ->  NumberFormatException  (공백도 안 된다)
Integer.parseInt("10.5")    ->  NumberFormatException
```

<br/>

앞뒤 공백도 안 받는다는 게 의외다.

`trim()` 을 먼저 해야 한다.

<br/>

`NumberFormatException` 은 언체크 예외다.

앞의 체크 예외, 언체크 예외 글에서 본 그 분류인데,

`try-catch` 를 강제하지 않으니 놓치기 쉽다.

<br/>

사용자 입력을 그대로 넣으면 `500` 이 나가는 것이다.

<br/>

## 그래서 스프링에서는 안 쓰게 된다

```java
@RequestParam int age
```

<br/>

앞의 스프링 타입 컨버터 글에서 본 대로 `ConversionService` 가 대신 해준다.

<br/>

실패하면 `400` 이 나가고, `@ModelAttribute` 면 `BindingResult` 에 담긴다.

내가 `parseInt` 를 부르면 그 처리가 다 없어진다.

<br/>

## 오버플로는 안 잡아준다

```java
Integer.parseInt("2147483648")     ->  NumberFormatException
```

<br/>

이건 잡아준다. `int` 범위를 넘으니 형식 오류로 본다.

<br/>

그런데 계산 중의 오버플로는 못 잡는다.

```java
int a = Integer.MAX_VALUE;
a + 1     ->  -2147483648      조용히 넘어간다
```

<br/>

앞의 Comparable과 Comparator 글에서 본 그 문제다.

<br/>

터지게 하려면 따로 있다.

```java
Math.addExact(a, 1)      ->  ArithmeticException: integer overflow
Math.multiplyExact(a, 2)
```

<br/>

금액 계산처럼 틀리면 안 되는 곳에는 이걸 쓰는 게 낫다.

<br/>

## 문자열로 바꾸는 방법도 여러 개다

```java
String.valueOf(10)       ->  "10"
Integer.toString(10)     ->  "10"
10 + ""                  ->  "10"
```

<br/>

`String.valueOf` 는 `null` 을 받아도 안 터진다.

```java
String.valueOf(null객체)     ->  "null"
객체.toString()              ->  NullPointerException
```

<br/>

앞의 String 클래스 글에서 본 대로 `+ ""` 는 내부에서 `StringBuilder` 를 만든다.

반복문 안에서 하면 객체가 계속 생기는 것이다.

<br/>

## 진법 변환도 된다

```java
Integer.toBinaryString(10)     ->  "1010"
Integer.toHexString(255)       ->  "ff"
Integer.parseInt("ff", 16)     ->  255
Integer.parseInt("1010", 2)    ->  10
```

<br/>

앞의 비트 연산 글에서 본 것들을 확인할 때 유용하다.

<br/>

`Integer.toBinaryString(-1)` 을 찍어보면 `1` 이 32개 나온다.

2의 보수 표현이 눈에 보이는 것이다.

<br/>

## 래퍼 클래스에 유용한 상수가 있다

```java
Integer.MAX_VALUE      2147483647
Integer.MIN_VALUE      -2147483648
Integer.SIZE           32       (비트 수)
Integer.BYTES          4
Double.NaN
Double.POSITIVE_INFINITY
```

<br/>

`Double.NaN` 은 자기 자신과도 같지 않다.

```java
Double.NaN == Double.NaN      ->  false
Double.valueOf(Double.NaN).equals(Double.NaN)  ->  true
```

<br/>

`==` 와 `equals` 의 결과가 반대다.

IEEE 754 스펙에서 `NaN` 은 무엇과도 같지 않다고 정했는데,

`equals` 는 `HashMap` 에서 쓸 수 있어야 해서 같다고 처리한 것이다.

<br/>

그래서 확인은 `Double.isNaN()` 으로 한다.

<br/>

앞의 실수 표현 글에서 본 부동소수점의 특성이

래퍼 클래스에서 이렇게 어긋나게 드러나는 것이다.
