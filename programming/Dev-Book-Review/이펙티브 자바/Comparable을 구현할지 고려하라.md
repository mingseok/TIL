## Comparable을 구현할지 고려하라

## 두줄 정리

- 순서가 있는 클래스를 작성한다면, `Comparable` 인터페이스를 구현하는 것이 좋다.

- `compareTo` 메서드를 구현할 때는 박싱 클래스에서 제공하는 `compare()`를 적극 활용하자.

<br/>

## `Comparable` 이란?

- `Comparable` 인터페이스는 객체를 정렬하는데 사용되는 메서드인 `compareTo`를 정의하고 있다.

- `Comparable` 인터페이스를 구현한 클래스는 반드시 `compareTo`를 정의해야 한다.

<br/>

## `Comparable` 인터페이스 특징?

- 자바에서 같은 타입의 인스턴스를 비교해야만 하는 클래스들은 모두 `Comparable` 인터페이스를 구현하고 있다.

- `Boolean` 타입을 제외한 래퍼 클래스와 알파벳, 연대같이 순서가 명확한 클래스들은 모두 정렬이 가능하다.
- 기본 정렬 순서는 작은 값에서 큰 값으로 정렬되는 **오름차순**이다.

<br/>

## `Comparable` 구현의 장점은?

- `Comparable`을 구현한 객체의 배열은 쉽게 정렬 가능하다.

    - ex) `Arrays.sort(a)`

- `Collection` 객체들에서도 정렬을 활용할 수 있다.

    - `TreeSet` 자료구조 같은 경우, `Comparable`을 구현한 타입만 제너릭으로 받을 수 있다.

        - `String` 타입을 넣는 경우, 들어간 모든 문자열을 알파벳순으로 출력 가능하다.

<br/>

natrual order만 정의해줄 뿐인데, 부가 효과가 많다.

반대로 구현하지 않았을 경우, 부가 효과를 못 누린다는 뜻이다.

<br/>

## `Comparable` 인터페이스의 `compareTo` 메서드

`compareTo`는 해당 객체와 전달된 객체의 순서를 비교한다.

- `compareTo`는 `Object`의 `euqals`와 두가지 차이점이 있다.

    - `compareTo`는 `equals`와 달리 단순 동치성에 더해 순서까지 비교할 수 있으며, 제네릭하다.

- `Comparable`을 구현했다는 것은 그 클래스의 인스턴스에 자연적인 순서가 있음을 뜻한다.

    - 예를 들어, `Comparable`을 구현한 객체들의 배열은 `Arrays.sort(a)`로 쉽게 정렬이 가능하다.

<br/>

## `compareTo` 메서드 일반규약

먼저 짚고 넘어갈 `compareTo`의 반환값에 대해 살펴보면

기준이 되는 객체를 x, 비교를 하는 객체를 y라 할 때

x 가 y보다 작은 경우 -1을, 같을 경우 0을, 클 경우 1을 반환한다.

결국 두 가지 차이를 빼고 `compareTo`는 기본적으로 equals와 비슷하다.

<br/>

- `compareTo`는 동치성 비교 뿐만 아니라 순서까지 비교할 수 있다
- 또한, 제네릭하기 때문에 형변환에 대한 부담이 없다

따라서 `compareTo` 메서드의 일반 규약은 `equals`의 규약과 비슷하다  (아이템10)

<br/>

즉, 반사성/대칭성/추이성을 지켜야 한다

- 이 항목은 필수는 아니지만 꼭 지키는 게 좋다
- 정렬된 컬렉션에서 동치성을 비교할 때 `equals` 대신 `compareTo`를 사용하기 때문에 안 지키면 이상하게 동작할 수 있음
    

<br/>

## 구현은?

- `Comparable`은 타입을 인수로 받는 제네릭 인터페이스이므로 `compareTo`의 인수타입은 컴파일 시에 정해지기 때문에 

    입력 인수 확인이나 형변환을 할 필요가 없다.
- 단순히 다른 타입이 온다면 `ClassCastException`을 발생시키면 된다. (대부분 그렇게 되어있다)
- 다른 타입간의 비교도 허용하지만 공통 인터페이스를 기반으로 했다는 전제하에 가능하다.

<br/>

## `equals`와 `compareTo`의 차이는?

[`compareTo`와 `equals`가 일관되지 않는 `BigDecimal` 클래스]

```java
final BigDecimal bigDecimal1 = new BigDecimal("1.0");
final BigDecimal bigDecimal2 = new BigDecimal("1.00");

final HashSet<BigDecimal> hashSet = new HashSet<>();
hashSet.add(bigDecimal1);
hashSet.add(bigDecimal2);

System.out.println(hashSet.size());

final TreeSet<BigDecimal> treeSet = new TreeSet<>();
treeSet.add(bigDecimal1);
treeSet.add(bigDecimal2);

System.out.println(treeSet.size());

// 실행결과 
hashSet: 2
treeSet: 1
```

- `HashSet`과 `TreeSet`은 서로 다른 메서드로 객체의 동치성을 비교한다.

- `HashSet`은 `equals`를 기반으로 비교하기 때문에 추가된 두 `BigDeciaml`이 다른값으로 인식되어 크기가 `2`가 된다.

- 반면에, `TreeSet`은 `compareTo`를 기반으로 객체에 대한 동치성을 비교하기 때문에 같은값으로 인식되어 

    `compareTo`가 `0`을 반환하기 때문에 크기가 `1`이 된다.

<br/>

## compareTo 메서드에서 관계연산자 (<, >)를 사용하지 않는것을 추천한다.

- 박싱된 기본 타입 클래스들에 새로 추가된 정적 메서드 `compare`를 대신 이용한다.

- 관계연산자(`<`,`>`)는 오류를 유발할 가능성이 있기때문에 추천하지 않는다.


<br/>

### 클래스에 핵심 필드 여러개일때 비교

클래스에 핵심필드가 여러개라면 가장 핵심적인 필드부터 비교하자.

교 결과가 `0`이 아니라면, 즉 순서가 결정되면 바로 결과를 반환하면 된다. 

똑같지 않은 필드를 찾을 때 까지 비교해나가도록 구현하면 된다.

<br/>

## Comparator 인터페이스

자바 8에서 Comparator 인터페이스가 일련의 비교자 생성 메서드와 메서드 연쇄방식으로 비교자를 생성할 수 있게 되었다.

- 비교자들은 `compareTo` 메서드를 구현하는데 활용될 수 있다.
    
    이 방식은 간결하지만 약간의 성능 저하가 뒤따른다. 
    
    자바의 정적 임포트 기능을 활용하면 정적 비교자 생성 메서드들을 그 이름만으로 
    
    사용할 수 있어 코드가 훨씬 깔끔해진다.
    
- `Comparator`는 `comparingInt`와 `thenComparingInt`등의 숫자용 기본 타입을 커버하는 보조 생성 메서드들을 가지고 있다.
- `comparing`와 `thenComparing`이란 객체 참조용 비교자 생성 메서드 또한 가지고 있다.

<br/>

## 정리

순서를 고려해야하는 값 클래스를 작성한다면 꼭 `Comparable` 인터페이스를 구현하여 

그 인스턴스들을 쉽게 정렬하고, 검색하고, 비교 기능을 제공하는 컬렉션과 어우러지도록 
해야한다.

<br/>

`compareTo` 메서드에서 필드의 값을 비교할 때 `<`와`>` 연산자는 지양해야 한다. 

그 대신 박싱된 기본 타입 클래스가 제공하는 정적 `compare` 메서드나 `Comparator`가 제공하는 비교자 생성 메서드를 이용하자.