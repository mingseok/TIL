## enum 사용하는 이유는?

Enum은 열거형이라고 불리며, 서로 연관된 상수들의 집합을 의미합니다.

```java
enum도 하나의 객체고, 하나의 클래스이다 / enum도 인스턴스이다. 
```

그리고 `JVM`에서 유일하게 하나만 존재하는 것이다.

<br/>

## 간단하게 정의하면 이렇다.

- 클래스처럼 보이게 하는 상수

- 서로 관련있는 상수들끼리 모아 상수들을 정의하는 것
- enum 클래스 형을 기반으로 한 클래스형 선언

<br/>

## enum 사용 이유는?

- `enum`은 컴파일 타임에 타입 안정성을 보장합니다.

    - 특정 범위의 값만 사용 가능하므로 컴파일 오류나 런타임 예외를 줄입니다.

- 가독성이 좋다

    - `enum`은 값들이 명시적으로 정의되어 있기 때문에 코드를 읽을 때 쉽게 이해할 수 있습니다.

- enum은 관리가 용이하다

    - 값이 추가되거나 변경되는 경우, 한 곳에서만 변경하면 되기 때문에 코드의 유지 보수가 용이합니다.
        
- 성능
    - `enum`은 컴파일 타임에 정적인 값으로 변환되기 때문에, 실행 시간에서 상수 검색의 `“오버헤드”`를 줄입니다.
        
        - `오버헤드` : 어떤 처리를 하기 위해 들어가는 간접적인 처리 시간 및 메모리

```java
인스턴스가 JVM 내에 하나만 존재한다는 것이 보장 되므로, Java에서 싱글톤을 만드는 가장 좋은 방법으로 권장됩니다.
```

<br/>

## 궁금증!

```java
"enum도 하나의 클래스"라는 말은 어디까지 사실일까?
```

`javap` 로 컴파일된 결과를 열어보면 그냥 클래스가 맞다.

```java
abstract class Operator extends java.lang.Enum<Operator> {
  public static final Operator PLUS;
  public static final Operator MINUS;
  public static final Operator DIVIDE;
  private final java.lang.String symbol;
  private static final Operator[] $VALUES;
  public static Operator[] values();
  public static Operator valueOf(java.lang.String);
  private Operator(java.lang.String);
  static {};
}
```

<br/>

세 가지가 눈에 띈다.

- `java.lang.Enum` 을 상속받는 평범한 클래스다

- 상수 하나하나가 `public static final` 필드다
- 생성자가 `private` 이라서 밖에서 `new` 를 할 수 없다

`static {}` 안에서 상수들을 하나씩 만들고, 그 뒤로는 아무도 더 만들 수 없다.

`JVM에 하나만 존재한다` 는 말이 여기서 나오는 것이다.

```java
클래스 초기화는 JVM이 딱 한 번만 실행한다 -> enum 상수도 딱 한 번만 만들어진다
```

<br/>

그래서 `equals` 가 아니라 `==` 로 비교해도 된다. 어차피 그 값은 세상에 하나뿐이다.

```java
Operator.PLUS == Operator.valueOf("PLUS")   // true
```

<br/>

## int 상수를 쓰면 뭐가 문제인가

```java
public static final int PLUS = 1;
public static final int MINUS = 2;

calculate(6, 3, 1);     // 이게 PLUS인지 읽는 사람이 알 수 있나
calculate(6, 3, 99);    // 99는 없는 값인데 컴파일이 된다
```

숫자는 아무 의미가 없어서 읽을 수가 없고, 범위를 벗어난 값을 넣어도 컴파일러가 안 막는다.

<br/>

`enum` 이면 이 두 가지가 한 번에 해결된다.

```java
calculate(6, 3, Operator.PLUS);   // 무슨 연산인지 이름으로 보인다
calculate(6, 3, 99);              // 컴파일 에러
```

원문에서 말한 `타입 안정성` 이 이 얘기다.

<br/>

## 진짜로 유용한 것은 여기서부터다

`enum` 은 클래스라서 필드와 메서드를 가질 수 있다. 상수마다 다르게 동작하게 만들 수도 있다.

```java
enum Operator {
    PLUS("+") {
        @Override
        int apply(int a, int b) {
            return a + b;
        }
    },
    MINUS("-") {
        @Override
        int apply(int a, int b) {
            return a - b;
        }
    },
    DIVIDE("/") {
        @Override
        int apply(int a, int b) {
            return a / b;
        }
    };

    private final String symbol;

    Operator(String symbol) {
        this.symbol = symbol;
    }

    abstract int apply(int a, int b);

    String getSymbol() {
        return symbol;
    }
}
```

<br/>

```java
for (Operator operator : Operator.values()) {
    System.out.println(operator.getSymbol() + " -> " + operator.apply(6, 3));
}
```

<br/>

### 결과

```java
+ -> 9
- -> 3
/ -> 2
```

<br/>

## 이게 왜 좋은가

같은 것을 `if` 나 `switch` 로 짜면 이렇게 된다.

```java
int calculate(int a, int b, Operator operator) {
    if (operator == Operator.PLUS) return a + b;
    if (operator == Operator.MINUS) return a - b;
    if (operator == Operator.DIVIDE) return a / b;
    throw new IllegalArgumentException();
}
```

<br/>

연산이 하나 늘어날 때마다 이 `if` 를 찾아가서 고쳐야 한다.

그리고 이런 분기가 코드 여기저기에 흩어져 있으면, 하나를 빠뜨려도 컴파일러가 안 알려준다.

<br/>

`enum` 안에 넣어두면 새 상수를 추가하는 순간 `apply` 를 구현하라고 컴파일러가 막는다.

빠뜨릴 수가 없는 구조가 되는 것이다.

```java
분기를 밖에 두면 -> 추가할 때마다 찾아다녀야 한다
분기를 enum 안에 두면 -> 추가하는 자리에서 같이 적게 된다
```

<br/>

## ordinal()은 쓰지 말자

`enum` 에는 선언 순서를 돌려주는 `ordinal()` 이 있다.

```java
Operator.PLUS.ordinal()    // 0
Operator.MINUS.ordinal()   // 1
```

<br/>

이 값을 DB에 저장하거나 계산에 쓰면 나중에 크게 당한다.

중간에 상수를 하나 끼워넣는 순간 뒤에 있던 것들의 숫자가 전부 밀리기 때문이다.

<br/>

순서에 의미가 필요하면 필드로 직접 들고 있는 것이 안전하다.

```java
enum Grade {
    BRONZE(1), SILVER(2), GOLD(3);

    private final int level;    // ordinal 대신 직접 들고 있는다
}
```
