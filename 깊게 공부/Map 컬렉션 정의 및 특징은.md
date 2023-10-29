## Map 컬렉션 정의 및 특징은?

<br/>

## Map

별도의 구조를 가지고 있는 것이 `map`이다.



Map은 키와 값이 한 쌍으로 이뤄져 있고, 
키는 중복으로 저장이 될 수  없고, 순서가 없습니다. 

<br/>

그렇기에, `set`과 비슷한 특징을 가지고 있다.

`key`를 통해서 `value`를 저장하는 구조가 `map`이라는 자료구조이다.

<br/>

### Map에는 어떤 것들이 있나요?

- HashMap
- HashTable

- LinkedHashMap
- TreeMap

<br/><br/>

## HashMap 특징

`hashCode()` 사용하여 `key`를 저장을 하고, 거기에 따라서 `value`를 저장하는게 `HashMap`이다.

그래서 `Hash`를 사용하기 때문에 → 조회에서 유리하다

<br/><br/>

## HashTable 특징

`HashMap()`과 비슷한데, 여기에 `thread-safe` 한 것이라고 생각하면 된다.

```java
HashMap와 HashTable 차이는 ArrayList와 Vector의 차이처럼 
thread-safe 한지, 안한지 차이로 볼수 있다.
```

<br/><br/>

## LinkedHashMap 특징

`LinkedHashSet`과 똑같이 내부에 `LinkedList`를 가지고 있어서, 입력순서를 저장하게 된다.

<br/><br/>

## TreeMap 특징

`TreeSet`과 같이 이진트리를 구조로 `key`를 정렬해서

추가와 삭제가 성능이 좀 떨어지지만 조회가 유리하는 특징이 있습니다.