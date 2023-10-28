## Optional API 사용하는 이유

개발할때 가장 많이 발생하는 예외 중 하나가 NPE(NullPointerException)입니다.

<br/>

NPE를 피하려면 null 여부 검사를 필연적으로 하게 되는데 

만약 null 검사를 해야하는 변수가 많은 경우 코드가 복잡해지고 번거롭습니다. 

<br/>

하지만 Java8 부터 Optional<T>을 제공하여 null로 인한 예외가 발생하지 

않도록 도와주고, Optional 클래스의 메소드를 통해 null을 컨트롤 할 수 있습니다.

<br/><br/>

## 자바 프로그래밍에서 `NullPointerException`을 종종 보게 되는 이유

```
null을 리턴하니까! 
null 체크를 깜빡했으니까!
```

<br/><br/>

## 생각해보기

```java
메소드에서 작업 중 특별한 상황에서 값을 
제대로 리턴할 수 없는 경우 선택할 수 있는 방법은? -> "3번"
```

1. 예외를 던진다. (`비싸다`, 스택트레이스를 찍어두니까.)

2. `null`을 리턴한다. 

    - 비용 문제가 없지만 그 코드를 사용하는 클리어인트 코드가 주의해야 됨.

3. 자바 8부터 `Optional`을 리턴한다. 

    - 클라이언트에 코드에게 명시적으로 빈 값일 수도 있다는 걸 알려주고,
        
        빈 값인 경우에 대한 처리를 강제한다.

<br/><br/>


## 과거에는

`Optional`이 나오기 전까지는 이렇게 처리하여 왔다!

```java
OptionalExample springBoot = new OptionalExample(1, "spring boot", true);

Progress progress = springBoot.getProgress();
if (progress != null) {
    System.out.println(progress.getStudyDuration());
}
```

<br/>

### 하지만, 자바 8부터 Optional이 생긴 후는 이렇다.

`“리턴 타입에만 쓰는거다”` 라고 생각하는게 좋다. 

<br/>

(다른곳 변수, 매개변수 등등 사용하지 말자)

```java
public Optional<Progress> getProgress() {
    return Optional.ofNullable(progress);
}
```