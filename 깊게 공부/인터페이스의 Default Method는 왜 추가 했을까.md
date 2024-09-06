## 인터페이스의 Default Method는 왜 추가 했을까?

인터페이스에서 몸통이 추가 된 메서드를 `Default Method`라고 한다.

즉, 메서드를 구현할 수 있다.

<br/>

또한 이를 구현한 클래스에서는 `Default Method`를 오버라이딩 할 수 있다

```java
pulbic interface MyInterface {
   default void Hello() {
       System.out.println("Hello World");
   }
}
```

<br/>

## 디폴트 메서드를 왜 추가 했을까?

인터페이스가 변경이 되면, 그 인터페이스를 구현하고 있는 모든 클래스들이 해당 메서드를 구현해야 되는 문제가 있다.

### 상황을 생각해보자.

```java
A본사(인터페이스)가 있고, A본사를 구현하고 있는 B지점(클래스), C지점(클래스)이 있다고 생각해보자.

-- 문제점 --
A본사 내부에서만 추가해야 될게 생겨 추가 하려고 하는데, A본사와 모든 지점인 B지점, C지점까지 추가되는 것이다.
```

이런 문제를 해결하기 위해, 인터페이스에서 메서드를 구현해 놓을 수 있도록 자바8에서 추가해 놓은 것이다.

<br/>

이런 문제를 해결 하기 위해 몸통이 { } 있는 메서드인 디폴트 메서드가 생긴 것이다. 

```java
필수!
메서드 앞에 무조건 default 키워드를 붙여 줘야 된다 → 생략 불가능
```

<br/>

## 디폴트 메서드를 사용하면 충돌이라는 문제점 발생.

```java
public class DefaultFoo implements Foo, Bar {

	  
}
```

만약, 인터페이스 2개가 있고, 두개의 인터페이스에서 똑같은게 있다면 어느 기준으로 

사용되게 하는 것이냐? 라고 질문 할 수 있다. → 이럴때는 다시 재정의해서 사용하면 그만인 것이다.

<br/>

충돌이 발생한다면 한가지만 생각하자.

```
직접, 오버라이딩 해주면 되는 것이다.
```

<br/>

## 예제) 주석 잘보기

```java
class MyCalTest {
    public static void main(String[] args) {
        MyCal myCal = new MyCal(); // 내가 만든 계산기

        int plus = myCal.plus(1, 3); // 추상
        int cal = myCal.exec(2, 5); // 디폴트 메서드

        System.out.println(cal); // 출력 : 7
    }
}

class MyCal implements Calculator {
    @Override
    public int plus(int a, int b) {
        return a + b;
    }

    @Override
    public int minus(int a, int b) {
        return a - b;
    }

		// 디폴트 메서드는 없어도 된다!
}

public interface Calculator {
    public int plus(int a, int b);

    public int minus(int a, int b);

    default int exec(int a, int b) {
        return a + b;
    }
}
```
