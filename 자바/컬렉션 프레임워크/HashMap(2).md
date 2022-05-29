## HashMap(2)

<br/>

### HashMap - 주요 메서드

```java
Object put(Object key, Object value) // 추가

void putAll(Map m) // 추가

Object remove(Object key) // 삭제

//기존에 지정된 'key' 로 새로운 value를 지정하는 것이다.
Object replace(Object key, Object value) // 변경.

boolean replace(Object key, Object oldValue, Object new Value) // 변경
```

<br/>

### ‘키’ 와 ‘값’ 을 한쌍으로 엔트리 라고 부른다.


<br/>


## HashMap에서 저장된 데이터들을 읽어 올때 사용하는 메서드

entrySet()을 이용해서 getKey() 메서드와 getValue() 를 사용하면 읽어와 출력 시킬 수 있다.

```java
Set entrySet() // 호출하면, '키' 와 '값' 의 쌍으로 받는다.

Set keySet() // '키' 들을! 가지고 올때는 이렇게 작성하는 것이다.

Collection values() // '값' 들을! 가지고 올때는 이렇게 작성하는 것이다.
```

<br/>

## HashMap 저장된 데이터를 불러 올때 사용하는 메서드

즉, “myId” 키 를 넣으면 , 값인 value ‘1234’ 가 반환 되는 것이다.

```java
Object get(Obejct key) // “myId” 키 를 넣으면 , 값인 value ‘1234’ 가 반환 되는 것이다.

entrySet()을 이용해서 getKey() 메서드와 getValue() 를 사용하면 읽어와 출력 시킬수 있다.


// 만약, 현재 등록되어 있지 않은 key값을 넣었다고 생각해보자. 
// 그러면 매개변수인 defaultValue 에 지정된 값이 반환하게 하는 것이다.
Object getOrDefault(Object key, Object defaultValue) 

boolean containsKey(Object Key) // 현재 키가 맵에 있는지 확인하는 것이다. 있으면 ture

boolean containsValue(Object value) // 현재 벨류가 맵에 있는지 확인하는 것이다. 있으면 true
```

<br/>

## 부가 메서드

```java
int size() // 크기 확인

boolean isEmpty() // 비었는지 확인 

- 공백의 length를 가지고 0이면 true / 아니면 false를 반환

void clear() // 모두 삭제

Object clone() // 복제.
```

<br/><br/>


### 예제 1)

trim() : 공백 제거 메서드.

```java
import java.util.*;

class aaa {
	public static void main(String[] args) {
		HashMap map = new HashMap();
		map.put("myId", "1234");
		map.put("asdf", "1111"); // 이게 지워지고, 뒤에 오는 1234가 덮어진다.
		map.put("asdf", "1234");

		Scanner s = new Scanner(System.in); // 화면으로부터 라인단위로 입력받는다.

		while (true) {
			System.out.println("id와 password를 입력해주세요.");
			System.out.print("id :");
			String id = s.nextLine().trim();

			System.out.print("password :");
			String password = s.nextLine().trim();
			System.out.println();

			if (!map.containsKey(id)) {
				System.out.println("입력하신 id는 존재하지 않습니다. 다시 입력해주세요.");
				continue;
			} else {
						// (map.get(id)) 가 반환 하는 것은 1234이다.
				if (!(map.get(id)).equals(password)) { 
					System.out.println("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
				} else {
					System.out.println("id와 비밀번호가 일치합니다.");
					break;
				}
			}
		} // while
	} // main의 끝
}

출력값.
id와 password를 입력해주세요.
id :n
password :n

입력하신 id는 존재하지 않습니다. 다시 입력해주세요.
id와 password를 입력해주세요.
id :myId
password :12  

비밀번호가 일치하지 않습니다. 다시 입력해주세요.
id와 password를 입력해주세요.
id :myId
password :1234

id와 비밀번호가 일치합니다.
```

<br/><br/>


### 예제 2)


```java
import java.util.*;

class aaa {
	public static void main(String[] args) {
		HashMap map = new HashMap();
		map.put("김자바", new Integer(90)); // 밑에 걸로 덮어진다.
		map.put("김자바", new Integer(100));
		map.put("이자바", new Integer(100));
		map.put("강자바", new Integer(80));
		map.put("안자바", new Integer(90));

		// 핵심은 여기다.
		// entrySet() 이용하여 맵에 저장된 것들을 읽어 올수 있었다.
		Set set = map.entrySet();
		Iterator it = set.iterator();

		while (it.hasNext()) {
		  // (Map.Entry) 부분은 map 인터페이스 안에 Entry 인터페이스가 들어 있는 것이다.
			Map.Entry e = (Map.Entry) it.next(); 
			System.out.println("이름 : " + e.getKey() + ", 점수 : " + e.getValue()); // 각각 메서드를 이용하여 가져왔다.
		}

		set = map.keySet();
		System.out.println("참가자 명단 : " + set);

		Collection values = map.values();
		it = values.iterator();

		int total = 0;

		while (it.hasNext()) {
			Integer i = (Integer) it.next();
			total += i.intValue();
		}

		System.out.println("총점 : " + total);
		System.out.println("평균 : " + (float) total / set.size());
		System.out.println("최고점수 : " + Collections.max(values));
		System.out.println("최저점수 : " + Collections.min(values));
	}
}

출력값.
이름 : 안자바, 점수 : 90
이름 : 김자바, 점수 : 100
이름 : 강자바, 점수 : 80
이름 : 이자바, 점수 : 100
참가자 명단 : [안자바, 김자바, 강자바, 이자바]
총점 : 370
평균 : 92.5
최고점수 : 100
최저점수 : 80
```


<br/><br/>


### 예제 3)


```java
import java.util.*;

class aaa {
	public static void main(String[] args) {
		String[] data = { "A", "K", "A", "K", "D", "K", "A", "K", "K", "K", "Z", "D" };

		HashMap map = new HashMap();

		for (int i = 0; i < data.length; i++) {
      // containsKey 메서드는 맵이 "A" 를 포함하고 있냐? 물어보는것이다.
			// 맨처음에는 아무것도 없으니, else 로 간다.

			// 만약, 맵에 있는데 또 다시 똑같은 값이 들어왔다면 
			// 기존 값 '1' 에서 +1 하는 것이다.
			if (map.containsKey(data[i])) {
				Integer value = (Integer) map.get(data[i]);
				map.put(data[i], new Integer(value.intValue() + 1));
			} else {
				map.put(data[i], new Integer(1));
			}
		}

		Iterator it = map.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			int value = ((Integer) entry.getValue()).intValue();
			System.out.println(entry.getKey() + " : " + printBar('#', value) + " " + value);
		}
	} // main

	public static String printBar(char ch, int value) {
		char[] bar = new char[value];

		for (int i = 0; i < bar.length; i++) {
			bar[i] = ch;
		}

		return new String(bar); // String(char[] chArr)
	}
}

출력값.
A : ### 3
D : ## 2
Z : # 1
K : ###### 6
```


<br/><br/>

>**Reference**
><br/>남궁 성 지음, 『자바의 정석』, 도우출판.
