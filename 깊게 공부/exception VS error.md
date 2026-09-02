## exception VS error

<br/>

## **Error와 Exception의 차이**

Error는 실행 중 일어날 수 있는 치명적 오류를 말합니다. 

<br/>

컴파일 시점에 체크할 수 없고, 오류가 발생하면 프로그램은 비정상 종료되며 예측 불가능한 UncheckedException에 속합니다.

반면, Exception은 Error보다 비교적 경미한 오류이며, try-catch를 이용해 프로그램의 비정상 종료를 막을 수 있습니다.

<br/>

## Error(=오류)

시스템이 종료되어야 할 수준의 상황이며 수습할 수 없는 심각한 문제를 의미한다. (=개발자가 미리 예측할 수 없는것)

```java
Error도 "언체크 예외"이다
```

<br/>

## Exception(=예외)

개발자가 구현한 로직에서 발생한 실수나 사용자의 영향에 의해 발생합니다. 

오류와 달리 개발자가 미리 예측하여 방지할 수 있기에 상황에 맞는 예외처리를 해야한다.

<br/>

## 계층 구조

- `Throwable:` 최상위 예외이다. → 하위에 `Exception` 과 `Error` 가 있다

![이미지](/programming/img/입문411.PNG)

<br/>

## Error(=오류)에 대해서

개발자가 미리 오류를 대처하기는 힘든 오류이다.

개발자는 이 예외를 잡으려고 해서는 안된다.

- `StackOverflowError`

    - 호출의 깊이가 깊어지거나 재귀가 지속되어 `stack overflow` 발생 시 던져지는 오류이다.

- `OutOfMemoryError`

    - `JVM`이 할당된 메모리의 부족으로 더 이상 객체를 할당할 수 없을 때 던져지는 오류이다.

```java
메모리 부족이나 심각한 시스템 오류와 같이 애플리케이션에서 복구 불가능한 시스템 예외이다.
```

<br/>

## Exception(=예외)에 대해서

`Exception` 과 그 하위 예외는 모두 컴파일러가 체크하는 `체크 예외`이다.

```java
단 "RuntimeException"은 예외로 한다.
```

- `NullPointerException`

    - 객체가 필요한 경우인데, 프로그램이 `null`을 사용하려고 시도할 경우 던져지는 예외이다.

- `IllegalArgumentException`

    - 부적절한 `argument`를 받았을 경우에 던져지는 예외이다.

<br/>

### 하지만 Exception은 `"던질 수 있는"` 예외이기도 하다.

프로그램의 로직이 진행되다가 개발자가 임의로 예외를 던질 수 있다는 의미가 된다.

웹 애플리케이션의 경우 여러 사용자의 요청을 처리하기 때문에 하나의 예외 때문에 시스템이 종료되면 안된다. 

`WAS`가 해당 예외를 받아서 처리하는데, 주로 사용자에게 개발자가 지정한, 오류 페이지를 보여준다. → (서버가 죽지 않는다)

<br/>

## **Checked Exception, Unchecked Exception 설명**



### **Checked Exception**

`Exception` 을 상속 받으면 체크 예외 이다.

```java
체크 예외는 예외를 잡아서 "처리하거나", "던지거나" 둘중 하나이다.
```

<br/>

체크 예외는 예외를 잡아서 처리할 수 없을 때, 예외를 밖으로 던지는 `throws` 예외를 필수로 선언해야 한다.

- 그렇지 않으면 컴파일 오류가 발생한다.

<br/>

### 체크 예외의 장점

개발자가 실수로 예외를 누락하지 않도록 컴파일러를 통해 문제를 잡아주는 안전 장치이다.

<br/>

### 체크 예외의 단점

실제로는 개발자가 모든 체크 예외를 반드시 잡거나 던지도록 처리해야 하기 때문에, 너무 번거로운 일이 된다.

- 크게 신경쓰고 싶지 않은 예외까지 모두 챙겨야 한다.

<br/>

### **Unchecked Exception**

`RuntimeException` 을 상속받으면 언체크 예외가 된다.

<br/>

### `RuntimeException`: 언체크 예외, 런타임 예외

- 컴파일러가 체크 하지 않는 `언체크 예외`이다.
- `RuntimeException` 의 이름을 따라서  그 `하위 언체크 예외`를 `런타임 예외`라고 많이 부른다.

<br/>

### 언체크 예외 설명

- 언체크 예외는 말 그대로 컴파일러가 예외를 체크하지 않는다는 뜻이다.
- 언체크 예외는 예외를 잡아서 처리할 수 없을 때, 예외를 밖으로 던지는 throws 예외를 생략할 수 있다.

<br/>
    

### 언체크 예외의 장점

- 신경쓰고 싶지 않은 언체크 예외를 무시할 수 있다.
- 신경쓰고 싶지 않은 예외의 의존관계를 참조하지 않아도 되는 장점이 있다.

<br/>

### 언체크 예외의 단점

- 언체크 예외는 개발자가 실수로 예외를 누락할 수 있다.
- 반면에 체크 예외는 컴파일러를 통해 예외 누락을 잡아준다.

<br/>

## 체크, 언체크 정리

체크 예외와 언체크 예외의 차이는 예외를 처리할 수 없을 때 예외를 밖으로 던지는 부분에 있다. 

이 부분을 필수로 선언해야 하는가? 생략할 수 있는가? 의 차이다.

<br/>

## 궁금증!

```java
체크 예외가 안전 장치라면서, 실무에서는 왜 대부분 언체크 예외를 쓸까?
```

두 가지 이유가 있다.

<br/>

## 첫번째) 체크 예외는 의존 관계를 위로 끌고 올라간다

```java
class MemberRepository {
    void save(Member member) throws SQLException {
        // JDBC 로 저장
    }
}

class MemberService {
    void join(Member member) throws SQLException {    // 어쩔 수 없이 같이 선언
        memberRepository.save(member);
    }
}

class MemberController {
    void create(Member member) throws SQLException {  // 여기까지 올라온다
        memberService.join(member);
    }
}
```

<br/>

`SQLException` 은 JDBC의 예외다. 데이터베이스를 어떻게 쓰는지에 대한 얘기다.

그런데 컨트롤러까지 그 이름을 알게 됐다.

<br/>

여기서 저장소를 JPA로 바꾸면 어떻게 될까.

`SQLException` 이 아니라 `PersistenceException` 이 나오니, 서비스와 컨트롤러의 `throws` 를 전부 고쳐야 한다.

<br/>

저장 방식을 바꿨을 뿐인데 컨트롤러가 바뀌는 것이다.

```java
체크 예외 -> 던지는 쪽의 사정을 받는 쪽이 알아야 한다 -> 결합도가 올라간다
```

<br/>

스프링이 `SQLException` 을 잡아서 `DataAccessException` 이라는 언체크 예외로 바꿔 던지는 이유가 이것이다.

서비스와 컨트롤러는 예외 이름을 몰라도 되게 만든 것이다.

<br/>

## 두번째) 스프링 트랜잭션은 체크 예외에 롤백하지 않는다

이건 모르면 크게 당한다.

```java
@Transactional
void order() throws IOException {
    save();
    throw new IOException();     // 롤백될까?
}
```

<br/>

롤백되지 않는다. 커밋된다.

`@Transactional` 의 기본 롤백 규칙이 `RuntimeException 과 Error 만` 이기 때문이다.

<br/>

체크 예외는 `비즈니스적으로 예상한 상황` 으로 보고 커밋해버린다.

주문이 실패했는데 데이터는 저장되어 있는 상황이 생기는 것이다.

<br/>

굳이 체크 예외를 쓰면서 롤백하고 싶으면 이렇게 적어야 한다.

```java
@Transactional(rollbackFor = Exception.class)
```

이걸 매번 신경 쓰느니 처음부터 언체크 예외를 쓰는 쪽이 안전하다.

<br/>

## 예외를 바꿔 던질 때는 원인을 담아야 한다

언체크 예외로 바꿔 던지는 것까지는 좋은데, 여기서 흔한 실수가 하나 있다.

```java
static void withoutCause() {
    try {
        deep();
    } catch (IllegalStateException e) {
        throw new RuntimeException("저장 실패");        // 원인을 버렸다
    }
}

static void withCause() {
    try {
        deep();
    } catch (IllegalStateException e) {
        throw new RuntimeException("저장 실패", e);     // 원인을 담았다
    }
}
```

<br/>

### 결과

```java
--- 원인을 안 담으면 ---
java.lang.RuntimeException: 저장 실패
  원인 = null

--- 원인을 담으면 ---
java.lang.RuntimeException: 저장 실패
  원인 = java.lang.IllegalStateException: 커넥션이 끊겼다
  터진 자리 = Cause.deep(Cause.java:36)
```

<br/>

원인을 안 담으면 `저장 실패` 라는 말만 남는다.

왜 실패했는지, 어느 줄에서 시작됐는지가 통째로 사라진다.

<br/>

원인을 담으면 로그에 `Caused by:` 로 원래 예외가 같이 찍힌다.

`커넥션이 끊겼다` 는 진짜 이유와 `Cause.java:36` 이라는 진짜 자리까지 따라온다.

```java
예외를 바꿔 던질 때는 반드시 원인을 함께 넘기자
throw new RuntimeException("메시지", e);
```

<br/>

## 예외를 삼키는 것이 제일 위험하다

```java
try {
    save();
} catch (Exception e) {
    // 일단 넘어가자
}
```

<br/>

이러면 실패했는데 아무 일도 없었던 것처럼 다음 줄로 넘어간다.

로그도 안 남고 예외도 안 올라가니, 나중에 데이터가 이상해도 여기를 의심할 수가 없다.

<br/>

정말 무시해도 되는 예외라면 왜 무시해도 되는지 주석으로 남기고, 최소한 로그는 찍어야 한다.

<br/>

## Error를 잡으면 안 되는 이유

원문에서 `개발자는 이 예외를 잡으려고 해서는 안된다` 고 한 것도 같은 맥락이다.

```java
try {
    // ...
} catch (OutOfMemoryError e) {
    // 어떻게든 이어가보자
}
```

<br/>

`OutOfMemoryError` 를 잡아도 메모리가 부족한 상황은 그대로다.

잡고 나서 하는 일마다 또 터진다. 앞에서 힙을 채우는 실험을 했을 때

`OutOfMemoryError` 를 잡고 출력을 하려다 출력 자체가 또 터졌던 것이 그 예다.

<br/>

`StackOverflowError` 도 마찬가지다. 스택이 이미 꽉 찬 상태라 뭘 해도 다시 넘친다.

이런 상황은 프로그램을 살리려 애쓰는 것보다, 빨리 죽고 다시 뜨는 쪽이 낫다.

```java
Exception -> 복구를 시도할 수 있다
Error     -> 복구할 방법이 없다. 잡지 말고 죽게 두자
```

<br/>

`catch (Throwable e)` 를 쓰면 `Error` 까지 같이 잡히니 쓰지 않는 것이 좋다.

`catch (Exception e)` 까지가 우리가 다룰 범위다.
