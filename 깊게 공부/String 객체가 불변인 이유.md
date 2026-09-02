## String 객체가 불변인 이유

선언할 때는 변수에 값을 저장하는 방식이 아닌 하나의 객체를 생성하고, 해당 객체인 주소값을 연결 시켜주는 방식입니다.

```java
String 변수 = new String("문자열");
String 변수 = "문자열";
```

`String` 인스턴스는 한 번 생성되면, 그 값을 `읽기`만 할 수 있고, 변경할 수는 없습니다. 

- 이러한 객체를 `”불변 객체"` 라고 한다.

<br/>

## `String` 불변 객체

1. 캐싱 기능에 의한 메모리 절약과 속도 향상

    - Java에서 String 객체들은 Heap의 String Pool 이라는 공간에 저장되는데, 참조하려는 문자열이
    
      String Pool에 존재하는 경우 새로 생성하지 않고 Pool에 있는 객체를 사용하기 때문에

      특정 문자열 값을 재사용하는 빈도가 높을 수록 상당한 성능 향상을 기대할 수 있다.
        
2. thread-safe
    - String 객체는 불변이기 때문에 여러 쓰레드에서 동시에 특정 String 객체를 참조하더라도 안전하다.
        

<br/>

즉, 덧셈 연산자를 사용해서 문자열과 문자열을 더한다면, 새로운 String `인스턴스`가 `생성`되는 것이다.

아래 예시를 보면 `str1`, `str2`를 더한 `str3`은 새로운 `인스턴스`인 것입니다.

```java
String str1 = "Hello";
String str2 = "JAVA";

String str3 = str1 + str2; // "HelloJAVA"
```

<br/>

## String Pool을 눈으로 확인해보자

`==` 는 주소를 비교하니까, 같은 객체인지 아닌지 바로 알 수 있다.

```java
String a = "hello";
String b = "hello";
String c = new String("hello");
String d = c.intern();

System.out.println("리터럴 == 리터럴  : " + (a == b));
System.out.println("리터럴 == new     : " + (a == c));
System.out.println("리터럴 == intern  : " + (a == d));
System.out.println("equals 비교       : " + a.equals(c));
```

<br/>

### 결과

```java
리터럴 == 리터럴  : true
리터럴 == new     : false
리터럴 == intern  : true
equals 비교       : true
```

`"hello"` 라고 두 번 적었는데 같은 객체다. 두 번째는 만들지 않고 Pool에 있던 것을 그대로 준 것이다.

<br/>

`new String("hello")` 는 Pool을 무시하고 무조건 새로 만든다. 그래서 `false` 가 나온다.

`intern()` 은 "Pool에 같은 게 있으면 그걸 달라"는 뜻이라서 다시 `true` 가 된다.

```java
"hello"           -> Pool에 있으면 그걸 준다
new String("...") -> Pool을 안 보고 무조건 새로 만든다
```

<br/>

## 궁금증!

```java
그런데 왜 하필 불변으로 만들었을까? 가변이면 안 되는 이유가 있나?
```

위에서 본 Pool 자체가 불변이라서 가능한 것이다.

`a` 와 `b` 가 같은 객체를 보고 있는데 만약 문자열을 고칠 수 있다면,

`a` 를 고치는 순간 아무 상관도 없는 `b` 까지 같이 바뀌어버린다.

```java
공유해서 아끼려면 -> 아무도 못 고쳐야 한다
```

Pool로 메모리를 아끼는 것과 불변인 것은 하나를 고르는 문제가 아니라, 하나가 다른 하나의 전제인 것이다.

<br/>

## 더 큰 이유는 따로 있다

문자열은 `HashMap` 의 키로 제일 많이 쓰인다.

키가 도중에 바뀌면 어떻게 되는지 직접 만들어봤다.

```java
Map<MutableKey, String> map = new HashMap<>();
MutableKey key = new MutableKey("설정파일경로");
map.put(key, "/etc/app.conf");

System.out.println("바꾸기 전 조회 = " + map.get(key));
key.value = "다른값";                                    // 키를 바꿔버린다
System.out.println("바꾼 뒤 조회   = " + map.get(key));
System.out.println("map 안에는 그대로 있는데 = " + map.size() + "개");
```

<br/>

### 결과

```java
바꾸기 전 조회 = /etc/app.conf
바꾼 뒤 조회   = null
map 안에는 그대로 있는데 = 1개
```

값은 분명히 안에 들어 있는데 못 찾는다.

`HashMap` 은 넣을 때 계산한 `hashCode` 로 자리를 정해두는데, 키가 바뀌면서 그 자리가 달라져버렸기 때문이다.

<br/>

문자열이 가변이었다면 이 일이 매일 일어났을 것이다.

`String` 이 불변이라서 `Map` 의 키로 마음 놓고 쓸 수 있는 것이다.

<br/>

## 보안 쪽도 같은 얘기다

```java
void connect(String url) {
    checkPermission(url);   // 허용된 주소인지 검사
    open(url);              // 접속
}
```

검사와 접속 사이에 다른 스레드가 `url` 을 바꿔치기할 수 있다면 검사가 아무 소용이 없다.

불변이기 때문에 검사를 통과한 값과 실제로 쓰는 값이 같다는 것이 보장되는 것이다.

<br/>

파일 경로, DB 접속 정보, 클래스 이름을 전부 `String` 으로 넘기는데 이게 도중에 바뀔 수 있다면 위험하다.

<br/>

## 대신 hashCode를 캐싱해둔다

`String` 내부를 열어보면 `hash` 라는 필드가 하나 있다.

```java
public final class String {
    private final byte[] value;
    private final byte coder;
    private int hash;          // 계산해둔 hashCode를 담아두는 곳
}
```

값이 안 바뀌니까 `hashCode()` 도 절대 안 바뀐다. 그래서 한 번 계산하고 저장해두고 계속 재사용한다.

`Map` 의 키로 아무리 많이 써도 해시 계산이 매번 일어나지 않는 것이다.

<br/>

```java
불변이라서 -> 공유해도 되고, 키로 써도 되고, 해시를 캐싱해도 되고, 검사한 값을 믿어도 된다
```
