## this( ), this

먼저 에러 코드를 보겠다

생성자 내에서 다른 생성자를 호출할 때는 클래스이름인 ‘Car’ 대신 ‘this’를 사용한다는데, <br/>그러지 않아서 에러고, 또 다른 에러는 생성자 호출이 첫 번째 줄이 아닌 두번째 줄이기 때문이다.

```java
Car (String color) {
			door = 5;
			Car (color, "auto", 4); // 에러1. 생성자의 두 번째 줄에서 다른 생성자 호출
									// 에러2. this(color, "auto", 4); 로 해야한다.
}
```

<br/>예제 코드.

이름 대신 this를 사용해야만 하므로 ‘Car’ 대신 ‘this’를 사용 했다.

그리고 생성자 Car( )의 첫째 줄에서 호출하였다는 점을 눈여겨 보기 바란다.

```java
class Car {
    String color;
    String gearType;
    int door;

    Car() {
        this("while", "auto", 4);
    }

    Car(String color) {
        this(color, "auto", 4);
    }

    Car(String color, String gearType, int door) {
        this.color = color;
        this.gearType = gearType;
        this.door = door;
    }

}

class aaa {

    public static void main(String[] args) {

        Car c1 = new Car();
        Car c2 = new Car("blue");

        System.out.println("c1의 color=" + c1.color + ", gearType=" 
														+ c1.gearType + ", door=" + c1.door);
        System.out.println("c2의 color=" + c2.color + ", gearType=" 
														+ c2.gearType + ", door=" + c2.door);

    }
}

실행 결과.
c1의 color=while, gearType=auto, door=4
c2의 color=blue, gearType=auto, door=4
```

<br/>생성자들은 일반적으로 서로 관계가 깊은 경우가 많아서 이처럼 서로 호출하도록 하여 <br/>유기적으로 연결해주면 더 좋은 코드를 얻을 수 있다.

<br/>this.color는 인스턴스변수이고, color는 생성자의 매개변수로 정의된 지역변수로 서로 구별이 가능하다. <br/>만일 오른쪽코드에서 ‘this.color = color’ 대신 ‘color = color’ 와 같이하면 둘다 지역변수로 간주된다. 

<br/>this 는 참조변수로 인스턴스 자신을 가르킨다.

<br/>하지만, this를 사용할 수 있는 것은 인스턴스멤버 뿐이다. static 메서드에서는 인스턴스 멤버들을 사용할 수 없는 것처럼, <br/>this 역시 사용할 수 없다. 왜냐하면 static 메서드는 인스턴스를 생성하지 않고도 호출될 수 있으므로,<br/> static 메서드가 호출된 시점에 인스턴스가 존재하지 않을 수도 있기 때문이다.

<br/>이렇게 사용하지 말고

```java
Car(String c, String g, int d) {
    color = c;
    gearType = g;
    door = d;
}
```

<br/>이렇게 사용하자.

```java
Car(String color, String gearType, int door) {
    this.color = color;
    this.gearType = gearType;
    this.door = door;
}
```

<br/>

### **this → 인스턴스 자신을 가리키는 참조변수, 인스턴스의 주소가 저장되어 있다.**

### **this( ), this(매개변수) → 생성자, 같은 클래스의 다른 생성자르 호출할 때 사용한다.**

**this와 this( ) 는 비슷하게 생겼을 뿐 완전히 다른 것이다. this는 ‘참조 변수’ 이고, this( ) 는 ‘생성자’ 이다.**



<br/><br/>

>**Reference**
><br/>남궁 성 지음, 『자바의 정석』, 도우출판.