## 메소드, 필드, enum 작성을 위한 규칙

<br/>

## 한 함수가 한 가지 기능만 담당하게 한다

함수 길이가 길어진다면 한 함수에서 여러 일을 하려고 하는 경우일 가능성이 높다. 

아래와 같이 한 함수에서 안내 문구 출력, 사용자 입력, 유효값 검증 등 여러 일을 하고 있다면 이를 적절하게 분리한다.

```java
private List<Integer> userInput() {
    System.out.println("숫자를 입력해 주세요: ");
    String userInput = Console.readLine().trim();
    List<Integer> user = new ArrayList<>();
    for (char c : userInput.toCharArray()) {
      user.add(Character.getNumericValue(c));
    }
    if (user.size() != 3) {
      throw new IllegalArgumentException("[ERROR] 숫자가 잘못된 형식입니다.");
    }
    return user;
}
```

<br/><br/>

## 필드(인스턴스 변수)의 수를 줄이기 위해 노력한다

필드(인스턴스 변수)의 수가 많은 것은 객체의 복잡도를 높이고, 버그 발생 가능성을 높일 수 있다. 

필드에 중복이 있거나, 불필요한 필드가 없는지 확인해 필드의 수를 최소화 한다.

<br/>

예를 들어 총 상금 및 수익률을 구하는 다음 객체를 보자.

```java
public class LottoResult {
    private Map<Rank, Integer> result = new HashMap<>();
    private double profitRate;
    private int totalPrize;
}
```

<br/><br/>

## final 키워드를 사용해 값의 변경을 막는다

최근에 등장하는 프로그래밍 언어들은 기본이 불변 값이다. 

자바는 **final** 키워드를 활용해 값의 변경을 막을 수 있다.

```java
public class Money {
    private final int amount;

    public Money(int amount) {
        ...
    }
}
```

<br/><br/>

## 연관성이 있는 상수는 static final 대신 enum을 활용한다

```java
public enum Rank {
    FIRST(6, 2_000_000_000),
    SECOND(5, 30_000_000),
    THIRD(5, 1_500_000),
    FOURTH(4, 50_000),
    FIFTH(3, 5_000),
    MISS(0, 0);

    private int countOfMatch;
    private int winningMoney;

    private Rank(int countOfMatch, int winningMoney) {
        this.countOfMatch = countOfMatch;
        this.winningMoney = winningMoney;
    }
}
```

<br/><br/>

## 비즈니스 로직과 UI 로직을 분리한다

비즈니스 로직과 UI 로직을 한 클래스가 담당하지 않도록 한다. 

단일 책임의 원칙에도 위배된다.

```java
public class Lotto {
    private List<Integer> numbers;

    // 로또 숫자가 포함되어 있는지 확인하는 비즈니스 로직
    public boolean contains(int number) {
        ...
    }

    // UI 로직
    private void print() {
        ...
    }
}
```
<br/>

## 궁금증!

```java
"연관성 있는 상수는 static final 대신 enum" 이 왜 나은지 코드로 보면
```

앞의 enum 사용하는 이유는 글에서 실제로 돌려본 결과가 그 답이다.

```java
public static final int PLUS = 1;
public static final int MINUS = 2;

calculate(6, 3, 1);     // 이게 PLUS 인지 읽는 사람이 알 수 있나
calculate(6, 3, 99);    // 99 는 없는 값인데 컴파일이 된다
```

<br/>

`enum` 으로 바꾸면 둘 다 해결된다.

```java
calculate(6, 3, Operator.PLUS);   // 이름으로 보인다
calculate(6, 3, 99);              // 컴파일 에러
```

<br/>

## 그리고 분기를 없앨 수 있다

이쪽이 실무에서 더 큰 이득이다.

```java
// 상수를 쓰면 분기가 밖에 생긴다
int calculate(int a, int b, int operator) {
    if (operator == PLUS) return a + b;
    if (operator == MINUS) return a - b;
    throw new IllegalArgumentException();
}
```

<br/>

연산이 하나 늘 때마다 이 `if` 를 찾아가서 고쳐야 한다.

그리고 이런 분기가 코드 여기저기 흩어져 있으면 하나를 빠뜨려도 컴파일러가 안 알려준다.

<br/>

```java
enum Operator {
    PLUS("+") {
        @Override int apply(int a, int b) { return a + b; }
    },
    MINUS("-") {
        @Override int apply(int a, int b) { return a - b; }
    };

    abstract int apply(int a, int b);
}
```

```java
+ -> 9
- -> 3
```

<br/>

새 상수를 추가하는 순간 `apply` 를 구현하라고 컴파일러가 막는다.

빠뜨릴 수가 없는 구조가 된다.

<br/>

앞의 객체는 객체스럽게 사용한다 글에서 본 `계산을 안으로 옮긴다` 와 같은 발상이다.

<br/>

## 필드 수를 줄이라는 규칙

필드가 많으면 그 조합의 수만큼 상태가 생긴다.

```java
class Order {
    private boolean paid;
    private boolean shipped;
    private boolean canceled;
    private boolean refunded;
}
```

<br/>

`boolean` 네 개면 조합이 `16` 가지다.

그중 실제로 말이 되는 것은 몇 개 안 된다.

```java
paid=false, shipped=true    -> 결제 안 했는데 배송됐다?
canceled=true, shipped=true -> 취소됐는데 배송됐다?
```

<br/>

말이 안 되는 조합을 코드가 막지 않으니, 언젠가 그 상태가 실제로 생긴다.

<br/>

`enum` 하나로 바꾸면 있을 수 없는 상태가 아예 표현되지 않는다.

```java
enum OrderStatus { CREATED, PAID, SHIPPED, CANCELED, REFUNDED }

class Order {
    private OrderStatus status;     // 다섯 가지만 존재한다
}
```

<br/>

앞의 우아한 테크코스 글의 `인스턴스 변수를 줄이자` 가 이런 이득을 말한 것이다.

<br/>

## final 을 붙이라는 규칙

앞의 불변 객체 글과 DI 생성자 주입 글에서 본 그 이득이다.

```java
private final String name;
```

<br/>

컴파일러가 두 가지를 보장해준다.

```java
생성자에서 반드시 채워진다 - 안 채우면 컴파일 에러
만들어진 뒤에는 안 바뀐다
```

<br/>

그리고 스프링 빈처럼 여러 스레드가 같이 쓰는 객체에서는 이게 동시성 안전으로 이어진다.

<br/>

다만 앞의 불변 객체 글에서 본 대로 `final` 은 절반만 지킨다.

```java
private final List<Name> names;     // 리스트를 바꿔치기는 못 하지만
names.add(...);                     // 안에 넣는 것은 된다
```

<br/>

그래서 컬렉션 필드는 `final` 만으로 부족하고, 앞의 일급 컬렉션 글에서 본 방식이 필요하다.

<br/>

## 비즈니스 로직과 UI 로직을 분리하라는 규칙

앞의 서비스 계층 글과 MVC 요청 처리 내부 동작 글에서 본 그 구분이다.

```java
class LottoGame {
    public void run() {
        System.out.println("구입 금액을 입력해 주세요.");     // UI
        int money = Integer.parseInt(scanner.nextLine());   // UI
        int count = money / 1000;                            // 로직
        System.out.println(count + "개를 구매했습니다.");      // UI
    }
}
```

<br/>

이러면 `구입 금액으로 몇 장을 살 수 있는가` 라는 규칙을 테스트하려고 콘솔 입력을 흉내내야 한다.

<br/>

나누면 순수 자바로 테스트할 수 있다.

```java
class LottoMachine {
    public int countOf(int money) {
        return money / 1000;
    }
}
```

```java
assertThat(machine.countOf(5000)).isEqualTo(5);
```

<br/>

앞의 DB 접근 (+테스트 방법) 글에서 본 대로,

`테스트를 쓰기 어렵다` 는 것은 설계에 문제가 있다는 신호다.
