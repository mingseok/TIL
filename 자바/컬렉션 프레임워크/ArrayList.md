## ArrayList

### List : 저장된 순서를 유지하고, 중복도 허용한다.

ArrayList : 데이터의 공간으로 배열을 사용한다. 

<br/>

## ArrayList의 메서드

<br/>

### 생성자.

```java
// 기본 생성자
ArrayList() 

// 컬렉션들끼리 변환할때 많이 사용한다.
ArrayList(Collection c)

// 배열의 길이를 적절히 지정해주는 것이 중요
// 배열은 한번 만들면 길이를 바꾸지 못한다.
ArrayList(int initialCapacity)
```

<br/>

### 추가.

```java
// 성공하면 ture, 실패하면 false
boolean add(Object o) 

// int index는 저장 위치다.
// 원래 그냥 저장하면 맨뒤에 저장하지만 int index를 사용함으로써 
// 앞쪽으로도 저장 할 수 있다.
void add(int index, Object element) 

// 컬렉션이 가지고 있는 요소를 그대로 저장 하는 것이다.
boolean addAll(Collection c)

// 어디에 추가할지 위치를 정할수도 있다.
boolean addAll(int index, Collection c)
```

<br/>

### 삭제.

```java
// 삭제에 성공하면 ture, 실패하면 false
boolean remove(Object o) 

// 특정 위치에 있는 객체를 삭제 할때
boolean remove(int index) 

// 컬렉션에 있는 객체들을 삭제
boolean removeAll(Collection c)

// 어레이 리스트에 있는 객체들을 모두 삭제
void clear()
```

<br/>

### 검색.

```java
// 못찾으면 -1 반환 한다.
int indexOf(Object o)

// 오른쪽 끝에서 부터 왼쪽으로 찾는다.
int lastIndexOf(Object o)

// 지정된 객체가 존재하는지 물어보는 것. 
// 있으면 ture, 없으면 false
boolean contains(Object o)

// 객체 읽기
Object get(int index)

// 변경
Object set(int index, Object element)
```

<br/>

### subList().

```java
// 배열있다면 일부만 뽑아내서 새로운 배열로 만드는 것이다.
List subList(int fromIndex, int toIndex)

// 어레이 리스트가 가지고 있는 객체 배열을 반환 하는 것이다.
Object[] toArray()

// 어레이 리스트에 비어 있는지 확인
boolean isEmpty()

// 빈공간 제거.
void trimToSize()

// 어레이 리스트에 저장된 객체의 갯수를 반환
int size()
```

<br/><br/>

### 예제 1)

```java
import java.util.*;

class bbb {
    
	public static void main(String[] args) {
		// 어레이 리스트 배열 10개 자리 생성
		ArrayList list1 = new ArrayList(10);

		// 배열 0자리부터 순서대로 값들이 저장 된다.
		// add 메서드로 추가하면 저장되어 있는 값 뒤에 하나씩 추가 된다.
		list1.add(new Integer(5)); // list1.add(5); 이렇게도 가능하다
		list1.add(new Integer(4));
		list1.add(new Integer(2));
		list1.add(new Integer(0));
		list1.add(new Integer(1));
		list1.add(new Integer(3));

		// 어레이 리스트의 일부를 뽑아서 새로운 리스트를 만든다는 것이다.
		// 1부터 4이다. 즉, 4, 2, 0 배열이 생성 되는 것이다.
		ArrayList list2 = new ArrayList(list1.subList(1,4)); 
		print(list1, list2);
		System.out.println("---------------------------");

		// 정렬하는 것이다.
		// Collection은 인터페이스이고, Collections는 클래스 이다.
		// Collections 유틸 클래스란? math 클래스 처럼 컬렉션을 다루는 데 있어
        // 필요한 메서드들을 제공하는 클래스 이다. 안에 sort 정렬 등등 메서드들이 있다.
		Collections.sort(list1);	
		Collections.sort(list2);	
		print(list1, list2);
		System.out.println("---------------------------");

		// 결과가 true 가 나온다.
		// list1이 list2의 모든 요소를 포함하고 있느냐 물어보는 것이다. 맞으면 true
		System.out.println("list1.containsAll(list2):" + list1.containsAll(list2));

		list2.add("B"); // 값 뒤에 추가 하였다.
		list2.add("C"); // 값 뒤에 추가 하였다.
		list2.add(3, "A"); // 위치를 지정해줘서 'A' 가 중간에 추가 되었다.
		print(list1, list2);
		System.out.println("---------------------------");

		// 변경이다.
		list2.set(3, "AA"); // 기존에 있는 'A'는 없어지고, 'AA'로 변경 되었다.
		print(list1, list2);
		System.out.println("---------------------------");

		// indexOf()는 지정된 객체의 위치(인덱스)를 알려준다.
		// 만약, 문자열 "1" 이 있고, 숫자열 1 이 있다.
		// list : [ 1, 0, 1, 2, 3, 4, 5 ]
		// 여기서 0자리에 1은 문자열이고, 2자리에 있는 1은 숫자이다.
		list1.add(0, "1");
		// list1.indexOf("1") 하면 0 반환.
		// list1.indexOf(1) 하면 2 반환.	
		System.out.println("index="+ list1.indexOf(1)); 
		
		
		// 만약, 원하는걸 지우고 싶을때.
		// list : [ 1, 0, 1, 2, 3, 4, 5 ]
		list1.remove(1); // 한다면 인덱스 1인 자리인 '0'인 객체가 삭제되는 것이다.
		list1.remove(new Integer(1)); // 이렇게 해야 1을 삭제

		// list1에서 list2와 겹치는 부분만 남기고 나머지는 삭제한다.
		System.out.println("list1.retainAll(list2):" + list1.retainAll(list2));	
		print(list1, list2);
		System.out.println("---------------------------");

		//  list2에서 list1에 포함된 객체들을 삭제한다.
		for(int i= list2.size()-1; i >= 0; i--) {
			if(list1.contains(list2.get(i)))
				list2.remove(i);
		}
		print(list1, list2);
		System.out.println("---------------------------");
	} // main의 끝

	
	public static void print(ArrayList list1, ArrayList list2) {
		System.out.println("list1:"+list1);
		System.out.println("list2:"+list2);
		System.out.println();		
	}

} // class

출력값.
list1:[5, 4, 2, 0, 1, 3]
list2:[4, 2, 0]

---------------------------
list1:[0, 1, 2, 3, 4, 5]
list2:[0, 2, 4]

---------------------------
list1.containsAll(list2):true
list1:[0, 1, 2, 3, 4, 5]
list2:[0, 2, 4, A, B, C]

---------------------------
list1:[0, 1, 2, 3, 4, 5]
list2:[0, 2, 4, AA, B, C]

---------------------------
index=2
list1.retainAll(list2):true
list1:[2, 4]
list2:[0, 2, 4, AA, B, C]

---------------------------
list1:[2, 4]
list2:[0, AA, B, C]

---------------------------
```

<br/><br/>

![이미지](/programming/img/어레이.PNG)



<br/>전부 삭제 하고 싶을때, 1번 과정 처럼 하면 이렇게 남아 있다.

그러므로 배열 마지막 부터 지워야 다 지워 지는 것이다.
![이미지](/programming/img/어레이1.PNG)

<br/><br/>

>**Reference**
><br/>남궁 성 지음, 『자바의 정석』, 도우출판.
