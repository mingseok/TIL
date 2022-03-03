## toString() 설명

toString( ) : 객체를 문자열으로 변환하기 위한 메서드

<br/>

### toString( ) 잘못 했을때의 경우.

```java
package test;

class Card {
	String kind;
	int number;

	Card() {
		this("SPADE", 1);
	}

	Card(String kind, int number) {
		this.kind = kind;
		this.number = number;
	}

}

class aaa {
	public static void main(String[] args) {
		Card c1 = new Card();
		Card c2 = new Card("HEART", 10);
		System.out.println(c1);
		System.out.println(c2);
		System.out.println(c1.toString());
		System.out.println(c2.toString());

	}
}

출력값.
test.Card@1877ab81
test.Card@305fd85d
test.Card@1877ab81
test.Card@305fd85d
```

<br/><br/>제데로 하고 싶을 경우 toString을 오버라이딩 해야 한다.

<br/><br/>

### toString( ) 알맞게 했을때의 경우.

```java
class Card {
	String kind;
	int number;

	Card() {
		this("SPADE", 1);  // Card(String kind, int number)를 호출
	}

	Card(String kind, int number) {
		this.kind = kind;
		this.number = number;
	}

	public String toString() {
		// Card인스턴스의 kind와 number를 문자열로 반환한다.
		return "kind : " + kind + ", number : " + number;
	}
}

class CardToString2 {
	public static void main(String[] args) {
		Card c1 = new Card();
		Card c2 = new Card("HEART", 10);
		System.out.println(c1.toString());
		System.out.println(c2.toString());
	}
}

출력값.
kind : SPADE, number : 1
kind : HEART, number : 10
```




<br/><br/>

>**Reference**
><br/>남궁 성 지음, 『자바의 정석』, 도우출판.