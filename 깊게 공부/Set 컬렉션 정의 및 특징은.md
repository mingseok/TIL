## Set 컬렉션 정의 및 특징은?

<br/>

## Set이란?

`List`와 같이 여러 객체를 저장하는 자료구조이다.

Set은 순서가 없는 데이터의 집합이며
데이터의 중복을 허용하지 않는 특징을 가지고 있다.

<br/>

### Set에는 어떤 것들이 있나요?

- HashSet

- TreeSet
- LinkedHashSet

<br/><br/>

## Hash란 무엇인가?

여러가지 데이터가 있는 상황이라고 생각해보자.

각각의 데이터를 숫자로 변환하는 것을 `hash`라고 한다.

<br/><br/>

## HashSet 특징

`hashCode()`라는 메서드를 통해서 `hash`값을 가져오고, 

이 `hash`값을 비교하면서 중복값을 거른다.

```java
만약, hash값이 같다면?
그러면, equals()라는 함수를 호출해서 두 객체를 비교하는 것이고,
equals() 까지 데이터가 같게 나온다면, 그 값은 저장하지 않게 되는 것이다. (set이기에)
```

<br/><br/>

## TreeSet 특징

`compareTo()` 라는 메서드를 사용해서 데이터를 비교하며 중복을 거른다.

여기서, 이진탐색트리 구조로 저장하게 되는 것이다.

```java
트리구조의 특징
추가와 삭제를 할때 트리를 재배열해야하기 때문에 속도가 느리다.
하지만, 검색은 유리하다.

정리하면 -> 삽입, 삭제는 속도가 느린데, 검색에는 속도가 빠르다
```

<br/><br/>

## LinkedHashSet 특징

`HashSet`과 똑같이 `hashCode()`를 통해서 중복을 확인하는데, 

이 클래스 내부에 `LinkedList`가 포함되어 있으며, 내가 입력한 순서를 확인할 수 있다.

그렇기에, `LinkedHashSet`은 입력 순서까지 저장하고 있는 것이다.