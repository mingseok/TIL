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
	
	
`Optional.empty()` : 빈 Optional 객체 생성한다
	
`Optional.of(value)` : value값이 null이 아닌 경우에 사용한다
	
`Optional.ofNullable(value)` : value값이 null인지 아닌지 확실하지 않은 경우에 사용한다	
	
	
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

<br/>

## 궁금증!

```java
orElse 와 orElseGet 이 무엇이 다른가
```

이름이 비슷해서 아무거나 써도 될 것 같은데 아니다.

<br/>

값이 있는 `Optional` 에 둘 다 써봤다.

```java
Optional<String> present = Optional.of("있음");

present.orElse(expensive("orElse"));
present.orElseGet(() -> expensive("orElseGet"));
```

<br/>

### 결과

```java
(비싼 계산 실행: orElse(값이 있는데도))
결과 = 있음, 있음
```

<br/>

`orElse` 쪽만 `비싼 계산` 이 실행됐다.

값이 있어서 쓰지도 않을 건데 실행된 것이다.

<br/>

## 자바 문법 때문이다

```java
present.orElse(expensive())
```

<br/>

메서드를 부르려면 인자를 먼저 평가해야 한다.

`orElse` 안으로 들어가기 전에 `expensive()` 가 실행되는 것이다.

<br/>

```java
present.orElseGet(() -> expensive())
```

<br/>

이건 람다를 넘긴다. 실행이 아니라 `실행 방법` 을 넘기는 것이다.

앞의 람다식 글에서 본 그 지연 실행이다.

<br/>

## 실무에서 이게 문제가 되는 경우

```java
Member member = memberRepository.findById(id)
        .orElse(memberRepository.findGuest());     // 항상 조회한다
```

<br/>

회원을 찾았는데도 게스트 조회 쿼리가 나간다.

<br/>

```java
.orElseGet(() -> memberRepository.findGuest())     // 없을 때만 조회
```

<br/>

예외를 던지는 것도 마찬가지다.

```java
.orElse(throwException())          // 이건 아예 컴파일이 안 된다
.orElseThrow(() -> new MemberNotFoundException())
```

<br/>

`orElseThrow` 가 따로 있는 이유다.

<br/>

## 상수면 orElse 가 낫다

```java
.orElse("기본값")           // 계산할 게 없다
.orElseGet(() -> "기본값")  // 람다 객체를 만드는 비용만 더 든다
```

<br/>

`값을 만드는 데 비용이 드느냐` 로 고르면 된다.

<br/>

## Optional 을 어디에 쓰면 안 되나

```java
필드         -> 안 된다. 직렬화가 안 되고 메모리만 늘어난다
파라미터     -> 안 된다. null 을 넘길지 Optional.empty 를 넘길지 애매해진다
컬렉션 안    -> 안 된다. 빈 리스트를 쓰면 된다
```

<br/>

`Optional` 은 반환 타입으로 쓰라고 만든 것이다.

```java
Optional<Member> findById(Long id);
```

<br/>

`이 메서드는 값이 없을 수도 있다` 를 타입으로 알려주는 게 목적이다.

<br/>

앞의 @Autowired, DI 글에서 본 선택적 주입도 같은 맥락이다.

```java
public MemberService(Optional<StatCollector> stat) { ... }
```

<br/>

여기서는 파라미터에 쓰지만, 스프링이 채워주는 자리라 예외에 가깝다.

<br/>

## isPresent + get 은 안 쓰는 게 낫다

```java
if (optional.isPresent()) {
    Member member = optional.get();
    ...
}
```

<br/>

이러면 `null` 체크와 다를 게 없다.

```java
if (member != null) { ... }
```

<br/>

`Optional` 을 쓴 의미가 없어진 것이다.

<br/>

```java
optional.ifPresent(member -> ...);
optional.map(Member::getName).orElse("이름 없음");
optional.filter(m -> m.getAge() > 20).orElseThrow();
```

<br/>

체인으로 이어 쓰는 게 원래 쓰임새다.

<br/>

`get()` 은 자바 10에서 `orElseThrow()` 라는 이름이 따로 생겼다.

`get` 이라는 이름이 안전해 보여서 그냥 부르는 사람이 많았기 때문이다.

<br/>

## null 을 완전히 없애주지는 않는다

```java
Optional<Member> optional = null;      // 이게 가능하다
optional.isPresent();                  // NullPointerException
```

<br/>

`Optional` 자체가 `null` 일 수 있다.

<br/>

그래서 `Optional` 을 반환하는 메서드는 절대 `null` 을 반환하면 안 된다.

```java
return Optional.ofNullable(member);    // member 가 null 이면 empty
```

<br/>

`of` 와 `ofNullable` 을 헷갈리면 여기서 터진다.

```java
Optional.of(null)          -> NullPointerException
Optional.ofNullable(null)  -> Optional.empty
```

<br/>

앞의 NullPointerException 글에서 본 대로,

`null` 이 없어진 게 아니라 다룰 자리가 정해진 것이라고 보면 된다.
