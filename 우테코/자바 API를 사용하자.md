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