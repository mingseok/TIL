## static 메서드와 일반 메서드의 차이점

`static` 메소드와 인스턴스 메소드의 차이는 인스턴스를 생성하는가, 하지 않는가의 차이로 나뉜다고 봅니다.

```
차이점은? `static` 메소드는 인스턴스 변수, 메소드를 호출할 수 없지만, 인스턴스 메소드는 `static` 변수, 메소드를 호출 가능하다.
```

<br/>

### 궁금증!

```java
왜 static 메소드는 인스턴스 멤버인 "인스턴스 변수", "인스턴스 메소드"를 사용할 수 없을까?
```

이유는, 인스턴스 메소드를 이용하려면 인스턴스 변수를 사용하므로 객체 생성이 필요합니다.

그러나 `static` 메소드는 항상 호출이 가능해야 하는데, 객체가 있을지 없을지 모르기 때문이다.

<br/>

### 생각해보기 ex1)

```java
public class Main {
    public static void main(String[] args) {
        Counter counter = new Counter();
        counter.increment();
    }
}

class Counter {
    static int count = 0; // 증가 할까?

    public void increment() {
        count++;
    }
}
```

<br/>

### 생각해보기 ex2)

```java
public class Main {
    public static void main(String[] args) {
        Counter counter1 = new Counter();
        counter1.increment();
        
        Counter counter2 = new Counter(); 
        counter2.increment();

        System.out.println(Counter.count);
    }
}

class Counter {
    static int count = 0;

    // 메소드에 static 붙여도 동작은 가능하다
    // 그렇게 되면, 위에 인스턴스들은 필요 없어지게 되는 것이다
    public void increment() { 
        count++;
    }
}
```
