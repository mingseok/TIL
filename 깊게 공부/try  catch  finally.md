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

`try` 블록은 그저 처리할 예외가 발생할지도 모를 코드 블록을 정의하는 역할을 합니다. 

`catch`블록은 try 블록 내부에서 예외가 발생할 경우 호출되는 문장 블록입니다.

`finally` 블록은 try 블록에서 일어난 일에 관계없이 항상 실행이 보장되는 곳입니다.

- finally 블록은 생략할 수 있습니다.