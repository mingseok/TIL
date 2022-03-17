## 매개변수의 다형성(Vector)

메서드의 매개변수에 다형성을 적용하면 아래와 같이 하나의 메서드로 간단히 처리 할 수 있다

```java
void buy(Product p) {
		money = money - p.price; 
		bonusPoint = bonusPoint + p.bonusPoint;
}
```

<br/>매개변수가 Product타입의 참조변수라는 것은, 메서드의 매개변수로 Product 클래스의 자손타입의 참조변수면 어느 것이나 <br/>매개변수로 받아들일 수 있다는 뜻이다.

<br/>p로 인스턴스의 price 와 bonusPoint를 사용할 수 있기에 이와 같이 할 수 있다.

<br/>상속받기만 하면, buy(Product p) 메서드의 매개변수로 받아들여질 수 있다.

<br/>Tv클래스와 Computer 클래스는 Product클래스의 자손이므로 위의 코드와 같이 buy(Product p)메서드에 <br/>매개변수로 Tv인스턴스와 Computer인스턴스를 제공하는 것이 가능하다.

```java
Buyer b = new Buyer();
Tv t = new Tv();
Computer c = new Computer();
b.buy(t);
b.buy(c);
```

<br/><br/>예제 1.

```java
package test;

class Product {
	int price; // 제품의 가격
	int bonusPoint; // 제품구매 시 제공하는 보너스점수

	Product(int price) {
		this.price = price;
		bonusPoint = (int) (price / 10.0); // 보너스점수는 제품가격의 10%
	}
}

class Tv extends Product {
	Tv() {
		// 조상클래스의 생성자 Product(int price)를 호출한다.
		super(100);
	}

	// Object 클래스의 toString()을 오버라이딩 한다.
	public String toString() {
		return "Tv";
	}
}

class Computer extends Product {
	Computer() {
		super(200);
	}

	public String toString() {
		return "Computer";
	}
}

class Buyer {
	int money = 1000;
	int bonusPoint = 0;

	void buy(Product p) {
		if (money < p.price) {
			System.out.println("잔액이 부족하여 물건을 살 수 없습니다.");
			return;
		}

		money -= p.price; // 가진 돈에서 구입한 제품의 가격을 뺀다.
		bonusPoint += p.bonusPoint; // 제품의 보너스 점수를 추가한다.
		System.out.println(p + "을/를 구입하셨습니다.");
	}
}

public class Test1 {

	public static void main(String[] args) {

		Buyer b = new Buyer();

		b.buy(new Tv());
		b.buy(new Computer());

		System.out.println("현재 남은 돈은 " + b.money + "만원입니다.");
		System.out.println("현재 보너스점수는 " + b.bonusPoint + "점입니다.");
	}

}

출력값
Tv을/를 구입하셨습니다.
Computer을/를 구입하셨습니다.
현재 남은 돈은 700만원입니다.
현재 보너스점수는 30점입니다.
```


<br/>

## 여러 종류의 객체를 배열로 다루기

이런 코드가 있다.

```java
Product p1 = new Tv();
Product p2 = new Computer();
Product p3 = new Audio();
```

<br/>Product타입의 참조변수 배열

공통의 조상을 가진 서로 다른 종류의 객체를 배열로 묶어서 다룰 수 있다.

```java
Product p[] = new Product[3];
p[0] = new Tv();
p[1] = new Computer();
p[2] = new Audio();
```

<br/>제품을 담기 위해 Buyer클래스에 Product 배열인 item을 추가해 주었다

item[i++] = p; 문장을 추가함으로써 물건을 구입하면, 배열 item에 저장 되도록 했다.

Product클래스 타입의 배열을 사용함으로써 구입한 제품을 하나의 배열로 간단하게 다룰 수 있게 됬다.

```java
class Buyer { // 고객, 물건을 사는 사람
	int money = 1000; // 소유금액
	int bonusPoint = 0; // 보너스점수
	Product[] item = new Product[10]; // 구입한 제품을 저장하기 위한 배열
	int i = 0;

	void buy(Product p) {
		if (money < p.price) {
			System.out.println("잔액이 부족하여 물건을 살 수 없습니다.");
			return;
		}

		money -= p.price; // 가진 돈에서 구입한 제품의 가격을 뺀다
		bonusPoint += p.bonusPoint; // 제품의 보너스 점수를 추가한다.
		item[i++] = p; // 제품을 Product[] item에 저장한다.
		System.out.println(p + "을/를 구입하셨습니다.");
	}
}
```

<br/><br/>예제.

이, 예제의 문제는

배열의 크기를 10으로 했기 때문에 11개 이상의 제품을 구입할 수가 없다는 것이다.

그렇다고, 해서 배열의 크기를 무조건 크게 설정 할 수도 없는 일이다.

```java
package test;

class Product {
	int price; // 제품의 가격
	int bonusPoint; // 제품구매 시 제공하는 보너스 점수

	Product(int price) {
		this.price = price;
		bonusPoint = (int) (price / 10.0);
	}

	Product() {
	} // 기본 생성자
}

class Tv extends Product {
	Tv() {
		super(100);
	}

	public String toString() {
		return "Tv";
	}
}

class Computer extends Product {
	Computer() {
		super(200);
	}

	public String toString() {
		return "Computer";
	}
}

class Audio extends Product {
	Audio() {
		super(50);
	}

	public String toString() {
		return "Audio";
	}
}

class Buyer { // 고객, 물건을 사는 사람
	int money = 1000; // 소유금액
	int bonusPoint = 0; // 보너스점수
	Product[] item = new Product[10]; // 구입한 제품을 저장하기 위한 배열
	int i = 0;

	void buy(Product p) {
		if (money < p.price) {
			System.out.println("잔액이 부족하여 물건을 살 수 없습니다.");
			return;
		}

		money -= p.price; // 가진 돈에서 구입한 제품의 가격을 뺀다
		bonusPoint += p.bonusPoint; // 제품의 보너스 점수를 추가한다.
		item[i++] = p; // 제품을 Product[] item에 저장한다.
		System.out.println(p + "을/를 구입하셨습니다.");
	}

	void summary() {
		int sum = 0;
		String itemList = "";

		// 반복문을 이용해서 구입한 물품의 총 가격과 목록을 만든다.
		for (int i = 0; i < item.length; i++) {
			if (item[i] == null) {
				break;
			}
			sum += item[i].price;
			itemList += item[i] + ", ";
		}
		System.out.println("구입하신 물품의 총금액은 : " + sum + "만원 입니다.");
		System.out.println("구입하신 제품은 " + itemList + "입니다.");
	}

}

public class Test1 {

	public static void main(String[] args) {
		Buyer b = new Buyer();

		b.buy(new Tv());
		b.buy(new Computer());
		b.buy(new Audio());
		b.summary();

	}

}

출력값.
Tv을/를 구입하셨습니다.
Computer을/를 구입하셨습니다.
Audio을/를 구입하셨습니다.
구입하신 물품의 총금액은 : 350만원 입니다.
구입하신 제품은 Tv, Computer, Audio, 입니다.
```

<br/>

## 이럴땐 Vector 클래스를 사용하면 된다.

그러면 배열의 크기를 알아서 관리해주기 때문에 저장할 인스턴스의 개수에 신경 쓰지 않아도 된다.

<br/>

### Vector 클래스는

| 메서드 / 생성자 | 설명 |
| --- | --- |
| Vector( ) | 10개의 객체를 저장할 수 있는 Vector 인스턴스를 생성한다. <br/>10개 이상의 인스턴스가 저장되면, 자동적으로 크기가 증가된다.  |
| boolean add(Object o) | Vector에 객체를 추가한다.<br/>추가에 성공하면 결과값으로 true, 실패하면 false를 반환한다. |
| boolean remove(Object o) | Vector에 저장되어 있는 객체를 제거한다. 제거에 성공하면 true, 실패하면 false를 반환한다. |
| boolean isEmpty( ) | Vector가 비어 있는지 검사한다. 비어 있으면  true, 실패하면 false를 반환한다. |
| Object get (int index) | 지정된 위치(index)의 객체를 반환한다. 반환타입이 Object 타입이므로 적절한 타입으로의 형변환이 필요하다. |
| int size( ) | Vector에 저장된 객체의 개수를 반환한다. |

<br/><br/>Vector  이용한 예제.
```java
package test;

import java.util.Vector;

class Product {
	int price; // 제품의 가격
	int bonusPoint; // 제품구매 시 제공하는 보너스 점수

	Product(int price) {
		this.price = price;
		bonusPoint = (int) (price / 10.0);
	}

	Product() {
	} // 기본 생성자
}

class Tv extends Product {
	Tv() {
		super(100);
	}

	public String toString() {
		return "Tv";
	}
}

class Computer extends Product {
	Computer() {
		super(200);
	}

	public String toString() {
		return "Computer";
	}
}

class Audio extends Product {
	Audio() {
		super(50);
	}

	public String toString() {
		return "Audio";
	}
}

class Buyer { // 고객, 물건을 사는 사람
	int money = 1000; // 소유금액
	int bonusPoint = 0; // 보너스점수
	Vector item = new Vector(); // 구입한 제품을 저장하기 위한 배열
	int i = 0;

	void buy(Product p) {
		if (money < p.price) {
			System.out.println("잔액이 부족하여 물건을 살 수 없습니다.");
			return;
		}

		money -= p.price; // 가진 돈에서 구입한 제품의 가격을 뺀다
		bonusPoint += p.bonusPoint; // 제품의 보너스 점수를 추가한다.
		item.add(p); // 제품을 Product[] item에 저장한다.
		System.out.println(p + "을/를 구입하셨습니다.");
	}

	void refund(Product p) { // 구입한 제품을 환불한다.
		if (item.remove(p)) { // 제품을 Vector에서 제거한다.
			money += p.price;
			bonusPoint -= p.bonusPoint;
			System.out.println(p + "을/를 반품하셨습니다.");
		} else { // 제거에 실패한 경우
			System.out.println("구입하신 제품 중 해당 제품이 없습니다.");

		}

	}

	void summary() {
		int sum = 0;
		String itemList = "";

		if (item.isEmpty()) {
			System.out.println("구입하신 제품이 없습니다.");
			return;
		}

		// 반복문을 이용해서 구입한 물품의 총 가격과 목록을 만든다.
		for (int i = 0; i < item.size(); i++) {
			Product p = (Product) item.get(i);
			sum += p.price;
			itemList += (i == 0) ? "" + p : ", " + p;
		}

		System.out.println("구입하신 물품의 총금액은 : " + sum + "만원 입니다.");
		System.out.println("구입하신 제품은 " + itemList + "입니다.");
	}

}

public class Test1 {

	public static void main(String[] args) {
		Buyer b = new Buyer();
		Tv tv = new Tv();
		Computer com = new Computer();
		Audio audio = new Audio();

		b.buy(tv);
		b.buy(com);
		b.buy(audio);
		b.summary();
		System.out.println();
		b.refund(com);
		b.summary();

	}

}
```

<br/><br/>

>**Reference**
><br/>남궁 성 지음, 『자바의 정석』, 도우출판.