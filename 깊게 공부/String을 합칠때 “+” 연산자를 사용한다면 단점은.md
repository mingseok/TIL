## String을 합칠때 “+” 연산자를 사용한다면 단점은?

<br/>

## String '+' 연산 단점

결론은 메모리 및 시간 모두에서 낭비가 발생한다는 것입니다. -> 이유는, 자바에서 String은 불변입니다. 

- 즉, 처음 생성된 문자열에 대해 수정을 할 수 없습니다.

그렇기에, `“a” + “b”` 더하게 된다면 새로운 String 인스턴스를 생성하게 되고, 

그 공간에 데이터를 하나하나 복사하는 작업을 치러야 합니다.

<br/>

따라서 문자열을 많이 결합하면 결합 할수록 공간의 낭비뿐만 아니라 속도 또한 매우 느려지게 된다는 것입니다.

<br/>

## StringBuilder를 사용하자.

StringBuilder는 문자열 연산에 대해 빠르고 효율적인 클래스 입니다. 

StringBuilder는 불변이 아니고, 미리 일정한 크기의 배열을 잡아두고 거기에 붙여나가는 방식입니다.

<br/>

### 만약 공간이 가득 찼다면?

배열이 가득 찼을 경우엔 기존보다 2배 더 크게 새로운 배열을 만드는 형태 생성 됩니다.

크기를 유연하게 늘려주어 가변적이라는 차이점이 있습니다.

<br/>

StringBuffer / StringBuilder는 데이터를 임시로 저장하는 메모리에 

문자열을 저장해두고 그 안에서 추가, 수정, 삭제 작업을 할 수 있습니다.

<br/>

## 궁금증!

```java
그러면 "+" 는 무조건 쓰면 안 되는 것일까?
```

그건 아니다. `javap` 로 컴파일된 결과를 열어보면 세 가지 경우가 다 다르게 나온다.

<br/>

## 첫번째) 양쪽이 다 상수인 경우

```java
String constant() {
    return "a" + "b" + "c";
}
```

```java
  0: ldc  #7   // String abc
  2: areturn
```

`+` 연산이 아예 사라졌다. 컴파일할 때 이미 `"abc"` 한 덩어리로 합쳐진 것이다.

실행 시점에는 붙이는 일 자체가 일어나지 않는다.

<br/>

## 두번째) 변수가 섞여 있는 한 줄

```java
String simple(String a, String b) {
    return a + b + "!";
}
```

```java
  0: aload_1
  1: aload_2
  2: invokedynamic #7,  0   // makeConcatWithConstants
  7: areturn
```

`+` 두 개가 호출 한 번으로 합쳐져 있다.

중간 문자열을 만들었다 버렸다 하지 않고 한 번에 처리해주는 것이다.

<br/>

## 세번째) 반복문 안에서 붙이는 경우

```java
String loop(String[] words) {
    String result = "";
    for (String word : words) {
        result += word;
    }
    return result;
}
```

```java
  12: iload         5
  14: iload         4
  16: if_icmpge     40           // 반복문 시작
  ...
  28: invokedynamic #13,  0      // makeConcatWithConstants  <- 반복문 안에 있다
  33: astore_2
  37: goto          12
```

여기가 문제다. 합치는 호출이 반복문 안에 들어가 있다.

한 바퀴 돌 때마다 지금까지 만든 문자열을 통째로 새로 만드는 것이다.

<br/>

`10` 글자짜리에 한 글자를 붙이면 `11` 글자를 새로 만들고, 그 다음엔 `12` 글자를 새로 만든다.

앞의 내용을 매번 처음부터 다시 복사하기 때문에 길어질수록 급격히 느려진다.

<br/>

## 얼마나 느린지 직접 재보면

`10만` 번 붙여서 시간을 재봤다.

```java
String +      : 336ms
StringBuilder : 0ms
StringBuffer  : 1ms
```

`336ms` 대 `0ms` 다. 원문에서 말한 낭비가 이 숫자로 나오는 것이다.

```java
한 줄짜리 결합 -> "+" 써도 된다. 오히려 읽기 좋다
반복문 안에서  -> StringBuilder
```

<br/>

## 배열이 정말 2배로 늘어나는가?

`StringBuilder` 내부 배열을 반사로 꺼내서 언제 커지는지 찍어봤다.

```java
static int capacity(StringBuilder sb) throws Exception {
    Field field = Class.forName("java.lang.AbstractStringBuilder").getDeclaredField("value");
    field.setAccessible(true);
    return ((byte[]) field.get(sb)).length;
}
```

<br/>

### 결과

```java
처음 = 16
17번째 글자에서 16 -> 34
35번째 글자에서 34 -> 70
```

기본 크기가 `16` 이고, 꽉 찰 때마다 커진다.

정확히는 `2배` 가 아니라 `2배 + 2` 다. `16 -> 34 -> 70` 이 그 규칙이다.

<br/>

## 그래서 크기를 미리 주면 더 좋다

늘어날 때마다 새 배열을 만들고 기존 내용을 복사하는 비용이 든다.

대충이라도 크기를 알고 있다면 처음부터 잡아두면 이 복사가 아예 안 일어난다.

```java
StringBuilder sb = new StringBuilder(1000);   // 처음부터 1000칸 잡고 시작
```

<br/>

```java
"+" 가 느린 게 아니라, 반복문 안에서 매번 새로 만드는 게 느린 것이다
```
