## 자바 API를 사용하자

<br/>

## 배열 대신 Java Collection을 사용한다

Java Collection 자료구조(List, Set, Map 등)를 사용하면 데이터를 조작할 때 다양한 API를 사용할 수 있다.

예를 들어 List<String>에 "pobi"라는 값이 포함되어 있는지는 다음과 같이 확인할 수 있다.

```java
List<String> members = Arrays.asList("pobi", "jason");
boolean result = members.contains("pobi"); // true
```

<br/><br/>

## Java에서 제공하는 API를 적극 활용한다

함수(메서드)를 직접 구현하기 전에 Java API에서 제공하는 기능인지 검색을 먼저 해본다.

<br/>

Java API에서 제공하지 않을 경우 직접 구현한다.



예를 들어, 사용자를 출력할 때 사용자가 2명 이상이면 쉼표(,) 기준으로 출력을 위한 문자열은 다음과 같이 구현 가능하다.

```java
List<String> members = Arrays.asList("pobi", "jason");
String result = String.join(",", members); // "pobi,jason"
```
<br/>

## 궁금증!

```java
직접 구현하는 것과 API 를 쓰는 것이 결과만 같은 걸까?
```

성능과 안전성까지 다른 경우가 많다.

<br/>

## String.join 을 직접 만들면

```java
// 직접 구현
String result = "";
for (int i = 0; i < members.size(); i++) {
    result += members.get(i);
    if (i < members.size() - 1) {
        result += ",";
    }
}

// API
String result = String.join(",", members);
```

<br/>

앞의 String을 합칠때 "+" 연산자를 사용한다면 단점은 글에서 실제로 재봤다.

```java
String +      : 336ms
StringBuilder : 0ms
```

<br/>

`String.join` 은 내부에서 `StringJoiner` 를 쓴다.

직접 구현하면 반복문 안에서 `+` 를 쓰게 되기 쉽고, 그 순간 위 차이가 생긴다.

<br/>

그리고 마지막 원소 뒤에 쉼표를 안 붙이는 조건 처리를 직접 하다가 실수하기도 쉽다.

<br/>

## contains 도 자료구조에 따라 다르다

```java
List<String> members = Arrays.asList("pobi", "jason");
members.contains("pobi");     // 앞에서부터 하나씩 비교한다. O(n)

Set<String> members = Set.of("pobi", "jason");
members.contains("pobi");     // 해시로 바로 찾는다. O(1)
```

<br/>

원소가 열 개면 차이가 없는데, 수만 개면 확실히 갈린다.

<br/>

앞의 DB 인덱스 글에서 본 것과 같은 얘기다.

`전부 훑는 것` 과 `바로 찾아가는 것` 의 차이다.

<br/>

`중복이 없고 순서가 필요 없다면` `Set` 을 쓰는 것이 맞다.

<br/>

## Arrays.asList 는 크기를 못 바꾼다

원문 예제에 쓰인 그 메서드에 함정이 하나 있다.

```java
List<String> members = Arrays.asList("pobi", "jason");
members.add("brown");
```

```java
UnsupportedOperationException
```

<br/>

`Arrays.asList` 는 배열을 리스트처럼 보이게 감싼 것이다.

배열은 앞의 배열의 선언과 메모리 할당 글에서 본 대로 크기를 못 바꾸니, 추가도 안 된다.

<br/>

`set()` 은 된다. 배열의 값을 바꾸는 것은 되기 때문이다.

```java
members.set(0, "brown");     // 된다
members.add("brown");        // 안 된다
```

<br/>

크기를 바꿔야 한다면 감싸야 한다.

```java
List<String> members = new ArrayList<>(Arrays.asList("pobi", "jason"));
```

<br/>

`Java 9` 부터는 `List.of` 가 있는데 이건 아예 다 막혀 있다.

```java
Arrays.asList - 크기 고정, 값 변경 가능
List.of       - 전부 불변. null 도 못 담는다
```

<br/>

## 실무에서 자주 쓰는 것들

```java
String.join(",", list)                     // 이어 붙이기
String.valueOf(x) / Integer.parseInt(s)    // 변환
Objects.equals(a, b)                       // null 안전한 비교
Objects.requireNonNull(x, "메시지")         // null 검사
List.of / Set.of / Map.of                  // 불변 컬렉션
Collections.unmodifiableList(list)          // 읽기 전용 뷰
Comparator.comparing(Member::getAge)       // 정렬 기준
```

<br/>

`Objects.equals` 는 앞의 equals, hashCode 글에서 본 그 도우미다.

```java
a.equals(b)              // a 가 null 이면 터진다
Objects.equals(a, b)     // 둘 다 null 이어도 안전하다
```

<br/>

`Comparator.comparing` 은 앞의 Comparable과 Comparator 글에서 본 대로

뺄셈으로 직접 구현하다가 오버플로가 나는 문제를 피할 수 있다.

```java
(a, b) -> a.getAge() - b.getAge()          // 값이 크면 뒤집힌다
Comparator.comparingInt(Member::getAge)    // 안전하다
```

<br/>

## 그래도 직접 구현해봐야 할 때가 있다

`검색을 먼저 해본다` 는 원칙이 맞지만, 학습 목적일 때는 예외다.

<br/>

앞의 자료구조 & 알고리즘 글들에서 정렬이나 탐색을 직접 구현해본 것이 그런 경우다.

`Arrays.sort` 를 쓰면 한 줄이지만, 그 안에서 무슨 일이 일어나는지는 만들어봐야 안다.

```java
실무   - API 를 쓴다. 검증된 것을 다시 만들 이유가 없다
학습   - 만들어본다. 그래야 API 가 무엇을 해주는지 알게 된다
```
