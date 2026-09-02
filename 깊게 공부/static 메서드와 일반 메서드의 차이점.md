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

<br/>

## 위 두 문제의 답

### ex1) 답은 증가한다. `count = 1` 이 된다.

`static` 변수를 `인스턴스 메서드` 에서 건드리는 것은 아무 문제가 없다.

막혀 있는 방향은 그 반대다. `static` 메서드에서 인스턴스 변수를 못 건드리는 것이다.

```java
static 메서드  ->  인스턴스 변수  : 불가능 (객체가 있을지 모른다)
인스턴스 메서드 ->  static 변수    : 가능 (클래스는 이미 로드되어 있다)
```

인스턴스 메서드가 실행되고 있다는 것은 이미 객체가 만들어졌다는 뜻이고,

객체가 만들어졌다는 것은 그 클래스가 이미 로드되어 `static` 도 준비됐다는 뜻이기 때문이다.

<br/>

### ex2) 답은 `count = 2` 다.

객체를 두 개 만들었지만 `count` 는 하나뿐이다.

`counter1` 이 올리고 `counter2` 가 또 올려서 `2` 가 된다.

<br/>

주석에 적어둔 것처럼 `increment()` 에 `static` 을 붙여도 결과는 똑같다.

그러면 객체를 만들 이유가 없어지므로 이렇게 쓰면 된다.

```java
Counter.increment();
Counter.increment();
System.out.println(Counter.count);   // 2
```

`static` 만 건드리는 메서드라면 굳이 인스턴스를 만들 필요가 없는 것이다.

<br/>

## 궁금증!

```java
호출하는 방식이 정말 다른 것일까? 아니면 문법만 다른 것일까?
```

바이트코드를 보면 아예 다른 명령어를 쓴다.

```java
void run() {
    Helper helper = new Helper();
    helper.instanceWork();
    Helper.staticWork();
}
```

```java
  invokevirtual  // Method Helper.instanceWork
  invokestatic   // Method Helper.staticWork
```

<br/>

- `invokevirtual` : 객체를 하나 받아서, 그 객체의 실제 타입을 보고 부를 메서드를 정한다

- `invokestatic` : 객체를 받지 않는다. 클래스만 보고 바로 부른다

`static` 메서드에 `this` 가 없는 이유가 여기에 있다. 애초에 객체가 안 넘어온다.

<br/>

## 그래서 static 메서드는 오버라이딩이 안 된다

```java
class Parent {
    String instanceMethod() { return "부모"; }
    static String staticMethod() { return "부모"; }
}

class Child extends Parent {
    @Override
    String instanceMethod() { return "자식"; }
    static String staticMethod() { return "자식"; }
}
```

<br/>

```java
Parent p = new Child();
System.out.println("p.instanceMethod() = " + p.instanceMethod());
System.out.println("p.staticMethod()   = " + p.staticMethod());
```

<br/>

### 결과

```java
p.instanceMethod() = 자식
p.staticMethod()   = 부모
```

<br/>

담긴 것은 분명히 `Child` 인데 `static` 쪽만 `부모` 가 나온다.

`invokevirtual` 은 실행할 때 객체의 실제 타입을 보고 정하지만,

`invokestatic` 은 컴파일할 때 변수의 타입만 보고 정해버리기 때문이다.

<br/>

`p` 의 타입이 `Parent` 니까 `Parent.staticMethod()` 로 확정되고, 실행할 때 바뀌지 않는다.

이것을 오버라이딩이 아니라 `숨김(hiding)` 이라고 부른다. 자식 것이 부모 것을 덮는 게 아니라 가릴 뿐이다.

<br/>

참고로 저 코드를 컴파일하면 경고가 뜬다.

```java
warning: [static] static method should be qualified by type name, Parent,
         instead of by an expression
```

`참조 변수로 static 메서드를 부르지 말고 클래스 이름으로 불러라` 는 뜻이다.

`p.staticMethod()` 처럼 쓰면 다형성이 동작할 것처럼 오해하게 만들기 때문이다.

```java
static 메서드는 클래스 이름으로 부르자 -> Parent.staticMethod()
```

<br/>

## 그럼 언제 static을 쓰는가

객체의 상태와 상관없이 결과가 정해지는 것이면 `static` 으로 두면 된다.

```java
Math.max(3, 5);              // 어떤 객체인지와 무관하다
Integer.parseInt("123");     // 입력만 있으면 결과가 나온다
Collections.sort(list);      // 받은 것을 처리할 뿐이다
```

<br/>

반대로 `이 객체의 상태를 봐야` 결과가 정해지는 것이면 인스턴스 메서드여야 한다.

```java
member.getName();            // 어느 회원인지에 따라 답이 다르다
order.calculateTotal();      // 그 주문에 담긴 것을 봐야 한다
```

<br/>

`static` 을 남발하면 테스트할 때 갈아끼울 수가 없어서 문제가 된다.

인스턴스 메서드는 인터페이스로 뽑아서 가짜 구현을 넣을 수 있지만, `static` 은 그게 안 된다.

컴파일 시점에 어느 클래스의 것인지 고정되어 버리기 때문이다.
