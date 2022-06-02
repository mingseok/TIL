## Optional<T>

T 타입 객체의 래퍼 클래스 - Optional<T>

어떤 타입이든 저장 할 수 있다. 

중요한 것은 null 도 가능하다.

```java
public final class Optional<T> {
	private final T value; // T타입의 참조변수.
	...
}
```
	
<br/>
	
	
Optional.empty() : 빈 Optional 객체 생성한다
	
Optional.of(value) : value값이 null이 아닌 경우에 사용한다
	
Optional.ofNullable(value) : value값이 null인지 아닌지 확실하지 않은 경우에 사용한다	
	
	
<br/>	
	
	
## 1. Optional.empty()

```java
Optional<String> optStr = Optional.empty();
```	
비어있는 옵셔널 객체를 생성합니다. 조건에 따라 분기를 태워야하고 반환할 값이 없는 경우에도 사용됩니다.	 	
	
<br/>		
	
## 2. Optional.of(value)

```java
String str = "test";
Optional<String> optStr1 = Optional.of(str);
```
	
value가 null 인 경우 NPE 예외를 던집니다. 반드시 값이 있어야 하는 객체인 경우 해당 메서드를 사용하면 됩니다.	
	
Optional.of()는 null이 아닌 객체를 담고 있는 Optional 객체를 생성합니다. 

null이 아닌 객체를 생성하기 때문에 null을 넘겨서 생성하면 NPE이 발생하게 됩니다. 

```java	
String nullStr = null;
Optional<String> optStr2 = Optional.of(nullStr); //NPE 발생(null point exception)
```	
	
<br/>

## 3. Optional.ofNullable(value)

```java
String str = "test";
Optional<String> optStr1 = Optional.ofNullable(str);
```
	
value가 null 인 경우 비어있는 Optional 을 반환합니다. 값이 null 일수도 있는 것은 해당 메서드를 사용하면 됩니다.		
	

	
<br/>

### null을 직접 다루는 것은 위험하다. 

이유는? 예외처리 될 수도 있기 때문이다.

그리하여 간접적으로 객체 안에 담아서 사용 하는 것이다.

또 다른 이유는 null 체크를 해야 되는데 그러기 위해서는 if문을 사용해야 한다.

그러면 코드가 지저분 해진다.

<br/>정리하면, null 을 Optional<T> 이라는 객체에 넣는 것이다.

여기서 포인트는 Optional<T>은 객체 라는 것이다.

객체는 존재 하니깐 주소값이 있다. 

<br/>그리하여 그 안에 담긴 값인 value가 ‘null’ 이 되는 것이다.

즉, Optional<T> 객체 안에 집어 넣는 것이다.

이러한 문제점을 해결하기 위해 나온게 Optional<T> 인것이다.

<br/>String 같은 경우에도, 초기화 할때 null 로 하지 않고, “ “; 빈 문자열로 한다.

그럼 “ ” 빈 문자열은 char[] 배열이다. (길이가 0이다.)

<br/><br/>

## Optional<T> 객체 생성 방법

**ofNullable**() 메소드는 값이 null이 아니면 명시된 값을 가지는 Optional 객체를 반환하며, 

명시된 값이 null이면 비어있는 Optional 객체를 반환합니다.

```java
String str = "abc";

// 생성하면 optVal 주소 번지가 생성되고, 
// optVal 주소번지가 str 번지를 가르킨다
// 그리고 str 안에 value가 "abc" 인것이다.
// 정리해서 한단계 더 거치는 것이다.
// 밑에 사진을 보자.
Optional<String> optVal = Optional.of(str); // 생성

Optional<String> optVal = Optional.of(null); // 에러.

// null 을 저장할 수 있는 메서드는 이렇게 만들어야한다.
Optional<String> optVal = Optional.ofNullable(null); // 이렇게는 가능하다.
```

<br/>


![이미지](/programming/img/옵셔널.PNG)

### 이걸 사용하는 이유는, null 일수 있는 값은 그냥 쓰지 말고, Optional<T> 객체에 담아서 사용하자는 것이다.

<br/><br/>

## Optional<T> 를 초기화 할때

```java
Optional<String> optVal = null; // 이렇게는 바람직하지 않다.
Optional<String> optVal = Optional.<String>empty(); // 빈 객체로 초기화 하자.
```

<br/><br/>

## Optional<T> 객체의 값 가져오기

Optional객체의 값 가져오기 - get(), orElse(), orElseGet(), orElseThrow()

```java
Optional<String> optVal = Optional.of("abc"); // 저장

// optVal에 저장된 값을 반환. null이면 예외 발생
String str1 = optVal.get();

// optVal에 저장된 값이 null일 때는 ""를 반환.
String str2 = optVal.orElse("");

// 람다식 사용가능 () -> new String()
// (String::new)을 람다식을 바꾸면 () -> (new String()); 으로 바뀐다.
String str3 = optVal.orElseGet(String::new);

// 널이면 예외 발생.
String str4 = optVal.orElseThrow();
```

<br/><br/>

## isPresent() - Optional객체의 값이 null 이면 false, 아니면 true를 반환

“null 이 아니면 출력하라” 하는 것이다.

```java
if(Optional.ofNullable(str).isPresent() ) { // if(str!=null) {
		System.out.println(str);
}
```

<br/><br/>

## ifPresnt(Consumer) - 널이 아닐때만 작업 수행, 널이면 아무 일도 안함

Optional.ofNullable(str) 이 부분이 null 이 아닐때만 !! 뒷 부분인 System.out::println 이걸 실행 하는 것이다.

```java
// ifPresnt(Consumer) - 널이 아닐때만 작업 수행, 널이면 아무 일도 안함.

// System.out::println 람다식으로 바꾸면 () -> System.out.println() 값은 
// Optional에 벨류 == v 가 들어갈 것이다.
// 즉, () -> System.out.println(v) 가 될것이다.
Optional.ofNullable(str).ifPresent(System.out::println);
```
<br/>

OptionalInt, OptionalLong, OptionalDouble 이 있는데 이건 필요하면 그때 공부하기. 차이는 성능 문제.

<br/><br/>

>**Reference**
><br/>남궁 성 지음, 『자바의 정석』, 도우출판.
