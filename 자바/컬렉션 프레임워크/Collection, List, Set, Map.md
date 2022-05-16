## **Collection, List, Set, Map**

### List : 저장된 순서를 유지하고, 중복도 허용한다.

### Set : 저장된 순서 개념이 없고, 중복도 없다. (List랑 반대이다.)

### Map : 저장된 순서 개념이 없고, ‘키’는 중복이 안되고, ‘값’은 중복을 허용 한다.

<br/>

![이미지](/programming/img/컬렉션0.PNG)

<br/><br/>

## Collection 인터페이스의 메서드

**추가 - 지정된 객체(o) 또는 Collection (c) 의 객체들을 Collection에 추가 한다.**

```java
boolean add(Object o)
boolean addAll(Collection c)
```

<br/>

**삭제 - 지정된 객체를 삭제한다.**

```java
boolean remove(Object o)
```

<br/>

**삭제 - 지정된 Collection에 포함된 객체들을 삭제한다.**

```java
boolean removeAll(Collection c)
```

<br/>

**모두 삭제 - Collection의 모든 객체를 삭제한다.**

```java
void clear()
```

<br/>

Collection이 비어있는지 확인한다.

```java
boolean isEmpty()
```

<br/>

Collection에 저장된 객체의 개수를 반환한다.

```java
int size()
```

<br/>

**검색 - Collection 이 특정 객체를 가지고 있는지 확인 한다.**

```java
boolean contains(Object o)
boolean containsAll(Collection c)
```

<br/><br/>

## List 인터페이스의 메서드

Vector는 ArrayList랑 같은 것이다.

ArrayList의 옛날 버전이라고 생각하면 된다.

핵심은 ArrayList랑 LinekList이다.

![이미지](/programming/img/컬렉션1.PNG)

<br/>

추가 - 지정된 위치(index)에 객체 또는 컬렉션에 포함된 객체들을 추가한다.

```java
void add(int indexm Object element)
boolean addAll(int index, Collection c)
```

<br/>

읽기 - 지정된 위치(index)에 있는 객체를 반환한다.

```java
Object get(int index) // get : 가져 오는 것.
```

<br/>

검색 - 지정된 객체의 위치(index)를 반환한다.

(List의 첫 번째 요소부터 순방향으로 찾는다)

```java
int indexOf(Object o)

//지정된 객체의 위치(index)를 반환한다.
int lastIndexOf(Object o)
```

<br/>

삭제 - 지정된 위치(index)에 있는 객체를 삭제하고 삭제된 객체를 반환한다.

```java
Object remove(int index)
```

<br/>

변경 - 지정된 위치(index)에 객체(element)를 저장한다.

```java
Object set(int index, Object element) // set 변경 하는 것.
```

<br/>

정렬 - 지정된 비교자로 List를 정렬한다.

```java
void sort(Comparator c)
```

<br/>

지정된 범위에 있는 객체를 반환한다.

```java
List subList(int fromIndex, int toIndex)
```

<br/><br/>

## Set(집합) 인터페이스의 메서드

HashSet, TreeSet 이 핵심이다.

set인터페이스의 메서드 - Collection인터페이스와 동일

![이미지](/programming/img/컬렉션2.PNG)

<br/>

지정된 객체(o)를 Collection에 추가한다.

```java
boolean add(Object o)
```

<br/>

Collection의 모든 객체를 삭제한다.

```java
void clear()
```

<br/>

지정된 객체(o)가 Collection에 포함되어 있는지 확인한다.

```java
boolean contains(Object o)
```

<br/>

Collection이 비어있는지 확인한다.

```java
boolean isEmpty()
```

<br/>

지정된 객체를 삭제한다.

```java
boolean remove(Object o)
```

<br/>

Collection에 저장된 객체의 개수를 반환한다.

```java
int size()
```

<br/><br/>

## Map 인터페이스의 메서드

### 하나의 key와 value를 엔트리 라고 한다.


여기서 중요한건 HashMap, TreeMap 이다.

Hashtable은 HashMap이랑 같다.

Hashtable 옛날 버전, HashMap 신버전 

![이미지](/programming/img/컬렉션3.PNG)

<br/>

추가 - Map에 value객체를 key객체에 연결하여 저장한다.

```java
Object put(Object key, Object value)
```

<br/>

추가 - 지정된 Map의 모든 key-value쌍을 추가한다.

```java
void putAll(Map t)
```

<br/>

삭제 - 지정한 key객체 일치하는 key-value객체를 삭제한다.

```java
Object remove(Object key)
```

<br/>

검색 - 지정된 key 객체와 일치하는 Map의 key객체가 있는지 확인한다.

```java
boolean containsKey(Object key)
```

<br/>

검색 - 지정된 value객체와 일치하는 Map의 key객체가 있는지 확인한다.

```java
boolean containsValue(Object Value)
```

<br/>

지정한 key객체에 대응하는 value객체를 찾아서 반환한다.

```java
Object get(Object key)
```

<br/>

**Map에 ‘키’ 만 전부다 읽어 올때 사용 하는 것.**

**Map에 저장된 모든 key 객체를 반환한다.**

```java
Set keySet()
```

<br/>

**Map에 ‘벨류’ 만 전부다 읽어 올때 사용 하는 것.**

**Map에 저장된 모든 value객체를 반환한다.**

```java
Collection values()
```

<br/>

**그리고 ‘키’ 와 ‘값’ 의 한 쌍을 entry(엔트리) 라고 부른다.**

**Map에 저장되어 있는 모든 ‘키’ 와 ‘값’ 의 한 쌍(엔트리)을 가져 오면서 set을 반환**
```java
Set entrySet()
```


<br/><br/>

>**Reference**
><br/>남궁 성 지음, 『자바의 정석』, 도우출판.
