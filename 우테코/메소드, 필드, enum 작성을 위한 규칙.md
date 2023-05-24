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