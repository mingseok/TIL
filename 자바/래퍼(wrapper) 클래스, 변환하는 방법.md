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
		...
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
