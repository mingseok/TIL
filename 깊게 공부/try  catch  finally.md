## try / catch / finally

<br/>

## 다음 코드에서 “finally run”이 출력 되는가?

```java
try {
    throw new Exception("hell world");
} catch (Exception e) {
    return;
} finally {
    System.out.println("finally run");
}
```

<br/>

### 정답은. → “finally run”은 출력 됩니다.

`try` 블럭에서 `Exception`이 발생하여, `catch`블럭으로 넘어가게 되어도

무조건 `finally`블럭은 실행 시킨 다음, 다시 `catch`블럭으로 돌아와 `return` 하게 됩니다.

```java
즉, try catch 블럭에서는 finally 블럭이 있다면 
무조건 finally 블럭을 실행 시키고 제어권이 넘어가게 됩니다.
```

<br/><br/>

## 각각 설명

`try` 블록은 그저 처리할 예외가 발생할지도 모를 코드 블록을 정의하는 역할을 합니다. 

`catch`블록은 try 블록 내부에서 예외가 발생할 경우 호출되는 문장 블록입니다.

`finally` 블록은 try 블록에서 일어난 일에 관계없이 항상 실행이 보장되는 곳입니다.

- finally 블록은 생략할 수 있습니다.

<br/>

## 궁금증!

```java
"제어권이 넘어간다"고 했는데, 그러면 return 값은 언제 정해지는 걸까?
```

`return` 할 값은 `finally` 로 넘어가기 `전에` 이미 정해진다.

`finally` 는 그 뒤에 실행되고, 정해진 값을 건드리지 못한다.

```java
static int changeAfter() {
    int value = 10;
    try {
        return value;
    } finally {
        value = 99;      // 반환값에 반영될까?
    }
}
```

<br/>

### 결과

```java
10
```

`99` 가 아니라 `10` 이다.

`return value` 를 만난 순간 `10` 이라는 값이 이미 챙겨진 상태로 `finally` 로 넘어가기 때문이다.

그 뒤에 `value` 를 아무리 바꿔도 이미 챙겨둔 값과는 상관이 없다.

```java
return 값을 챙긴다 -> finally 실행 -> 챙겨둔 값을 반환
```

<br/>

## 그런데 finally에서 return을 해버리면 얘기가 달라진다

```java
static String swallow() {
    try {
        throw new RuntimeException("터졌다");
    } finally {
        return "finally의 return";
    }
}
```

<br/>

### 결과

```java
finally의 return
```

예외를 던졌는데 아무 일도 없었다는 듯이 값이 돌아온다.

`finally` 의 `return` 이 던져지던 예외를 통째로 덮어써버린 것이다.

<br/>

이건 꽤 위험하다. 밖에서는 이 메서드가 실패했다는 사실을 알 방법이 없어진다.

로그도 안 남고 예외도 안 올라가니, 나중에 값이 이상해도 어디서 잘못됐는지 찾을 수가 없다.

```java
finally 안에서는 return 하지 말자. 예외를 삼켜버린다
```

<br/>

## 그럼 finally는 원래 어디에 쓰던 것인가

자원을 닫는 용도였다. 예외가 나든 안 나든 파일이나 커넥션은 닫아야 하니까.

```java
Connection connection = null;
try {
    connection = dataSource.getConnection();
    // ... 쿼리 실행
} catch (SQLException e) {
    // ... 처리
} finally {
    if (connection != null) {
        try {
            connection.close();
        } catch (SQLException e) {
            // close에서 또 예외가 날 수 있어서 여기도 감싸야 한다
        }
    }
}
```

<br/>

닫는 코드가 본 로직보다 길어지고, `null` 검사에 중첩 `try` 까지 붙는다.

여러 개를 열었으면 닫는 순서까지 신경 써야 한다.

<br/>

## 그래서 try-with-resources가 나왔다

```java
try (Connection connection = dataSource.getConnection()) {
    // ... 쿼리 실행
} catch (SQLException e) {
    // ... 처리
}
```

괄호 안에서 연 것은 블록을 벗어날 때 자동으로 닫힌다. `finally` 를 적을 일이 없어졌다.

여러 개를 열었으면 선언한 순서의 `반대로` 닫아준다.

<br/>

`AutoCloseable` 을 구현한 것만 괄호 안에 넣을 수 있다.

```java
public interface AutoCloseable {
    void close() throws Exception;
}
```

<br/>

## finally가 안 도는 경우도 있다

`무조건` 이라고 했지만 예외가 두 가지 있다.

- `System.exit()` 를 만나면 JVM 자체가 내려가므로 `finally` 도 못 돈다

- 그 스레드가 죽어버리거나 JVM이 강제 종료되어도 마찬가지다

일상적인 코드에서는 볼 일이 없지만, `무조건` 이라는 말이 완전히 무조건은 아니라는 것 정도는 알아두면 된다.
