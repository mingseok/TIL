## HashSet(2)

HashSet은 객체를 저장하기전에 기존에 같은 객체가 있는지 확인 같은 객체가 
없으면 저장하고, 있으면 저장하지 않는다.


<br/>

### 예제1)

[David:10, abc, David:10] 출력값이 이렇게 나왔다.

David:10 가 두번이 나왔다. 왜??

**이유는 equals()와 hashCode()를 오버라이딩 해야 HashSet이 바르게 동작하는 것이다.**

<br/>정답은 예제2.

```java
import java.util.*;

class aaa {
	public static void main(String[] args) {
		HashSet set = new HashSet();

		set.add("abc");
		set.add("abc");
		set.add(new Person("David", 10));
		set.add(new Person("David", 10));

		System.out.println(set);
	}
}

class Person {
	String name;
	int age;

	Person(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public String toString() {
		return name + ":" + age;
	}
}
```

<br/><br/>

### 예제2)

이렇게 하여 각각 한번만 나오게 된것이다.

```java
import java.util.*;

class aaa {
	public static void main(String[] args) {
		HashSet set = new HashSet();

		set.add(new String("abc"));
		set.add(new String("abc"));
		set.add(new Person("David", 10));
		set.add(new Person("David", 10));

		System.out.println(set);
	}
}

class Person {
	String name;
	int age;

	Person(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Person)) {
			return false;
		}
		Person p = (Person) obj;
		return this.name.equals(p.name) && this.age == p.age;

	}

	public int hashCode() {
		return (name + age).hashCode();
	}

	public String toString() {
		return name + ":" + age;
	}
}

출력값.
[abc, David:10]
```

<br/><br/>

### 예제3)

합집합, 교집합, 차집합


![이미지](/programming/img/차교합.PNG)

<br/><br/>

```java
import java.util.*;

class aaa {
	public static void main(String args[]) {
		HashSet setA = new HashSet();
		HashSet setB = new HashSet();
		HashSet setHab = new HashSet();
		HashSet setKyo = new HashSet();
		HashSet setCha = new HashSet();

		setA.add("1");
		setA.add("2");
		setA.add("3");
		setA.add("4");
		setA.add("5");
		System.out.println("A = " + setA);

		setB.add("4");
		setB.add("5");
		setB.add("6");
		setB.add("7");
		setB.add("8");
		System.out.println("B = " + setB);

		// 교집합
		Iterator it = setB.iterator();
		while (it.hasNext()) {
			Object tmp = it.next();
			if (setA.contains(tmp))
				setKyo.add(tmp);
		}

		// 차집합
		it = setA.iterator();
		while (it.hasNext()) {
			Object tmp = it.next();
			if (!setB.contains(tmp))
				setCha.add(tmp);
		}

		// 합집합
		it = setA.iterator();
		while (it.hasNext())
			setHab.add(it.next());

		// 합집합
		it = setB.iterator();
		while (it.hasNext())
			setHab.add(it.next());

		System.out.println("A ∩ B = " + setKyo); // 한글 ㄷ을 누르고 한자키
		System.out.println("A ∪ B = " + setHab); // 한글 ㄷ을 누르고 한자키
		System.out.println("A - B = " + setCha);
	}
}

출력값.
A = [1, 2, 3, 4, 5]
B = [4, 5, 6, 7, 8]
A ∩ B = [4, 5]
A ∪ B = [1, 2, 3, 4, 5, 6, 7, 8]
A - B = [1, 2, 3]
```

<br/><br/>

>**Reference**
><br/>남궁 성 지음, 『자바의 정석』, 도우출판.
