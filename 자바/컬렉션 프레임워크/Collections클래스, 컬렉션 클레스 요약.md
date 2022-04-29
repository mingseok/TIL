## Collections클래스, 컬렉션 클레스 요약

Objects : 객체 다룰때 사용

Arrays : 배열 다룰때 사용

Collections : 컬렉션


<br/>

## 1. 컬렉션 채우기, 복사, 정렬, 검색 - fill(), copy(), sort(), binarySearch() 등

<br/>

## 2. 컬렉션의 동기화 - synchronizedXXX() 메서드

```java
static Collection synchronizedCollection(Collection c)
static List synchronizedList(List list)
static Set synchronizedSet(Set s)
static Map synchronizedMap(Map m)
... 등등 
```

<br/>

## 3. 변경불가 컬렉션 만들기 즉, 읽기 전용이고 컬렉션이 변경되지 않고 보호해야 될때 사용  - unmodifiableXXX()

```java
static Collection unmodifiableCollection(Collection c)
static List unmodifiableList(List list)
static Set unmodifiableSet(Set s)
static Map unmodifiableMap(Map m)
... 등등 
```
<br/>

## 4. 싱글톤 컬렉션 만들기 - singletonXXX() 메서드
    
객체 1개만 저장 한다.
    

```java
static List singletonList(Object o)
static Set singleton(Object s) // singletonSet이 아님에 주의
static Map singletonMap(Object key, Object value)
```

<br/>

## 5. 한 종류의 객체만 저장하는 컬렉션 만들기 - checkedXXX()

```java
static Collection checkedCollection(Collection c)
static List checkedList(List list)
static Set checkedSet(Set s)
static Map checkedMap(Map m)
... 등등 
```

즉, 5번이 무슨 말이냐면,

```java
List list = new ArrayList();

// 즉 list를 생성해서 그 list안에는 Stirng 클래스만 들어올수 있게 하는 것이다.
List checkedList = checkedList(list, String.class); 

checkedList.add("abc"); // 성공
checkedList.add(new Integer(3)); // 에러. Integer 타입은 오지 못한다.
```

<br/><br/>

## 컬렉션 클래스 정리 & 요약

1. ArrayList 와 Vector 특징 : Object 배열을 가지고 있고, 여기에 저장을 한다. 배열기반의 컬렉션 클래스들이다.

2. 1번을 기반으로 만든 것이 Stack 이다. - 마지막에 들어 간것이 제일 일찍 나오는 구조. Vector 를 가지고 Stack을 구현한다.
3. 배열의 단점은?? → 추가, 삭제가 불리하다. 그걸 개선 한것이 ListedList 이다. ListedList 는 연결 기반이다. 
4. 3번인 ListedList를 가지고 만든 것이 Queue 이다. 제일 먼저 들어간 것이 제일 빨리 나오는 것이다. 
5. 검색기능을 항샹 시킨것이 HashMap 이다. 어떤 것이냐, 배열의 장점과 링크드리스트의 장점을 합친 것이다. <br/>HashMap은 key와 value를 쌍으로 저장하는 것이다. 그리고 HashMap은 신버전이고 Hashtable은 옛날 버전이다. <br/>신버전을 사용하도록 하자.
6. TreeMap 같은 경우는 연결 기반을 변경한 것으로 최대 2개 까지 연결 할 수 있는 것이다.<br/> TreeMap 의 장점은 검색, 범위검색, 정렬기능 향상 시킨것이다. (중위 순회 하면 정렬 된다)
7. HashMap 에서의 key 가지고만 만든 것이 HashSet 이고, TreeMap에서의 key만 가지고 만든 것이 TreeSet 이다.
8. HashMap 의 변경으로 Properties 가 있다. 다른점은 HashMap은 키, 벨류가 (Object, Object) 이지만,<br/> Properties 는 키, 벨류가 (String, String) 이다. 장점은 파일에 읽기와 쓰기가 쉽다.
9. HashMap은 순서를 유지 하지 않지만, 순서를 유지 하고 싶다면, LinkedHashMap과 LinkedHashSet을 사용하면 되는 것이다.

![이미지](/programming/img/컬렉션정리.PNG)

<br/><br/>

>**Reference**
><br/>남궁 성 지음, 『자바의 정석』, 도우출판.
