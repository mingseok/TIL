## 실행을 위해서 왜 main 메서드가 필요한 것인가요?


이유는 프로그램이 실행되는데에 있어서 자바 프로그램의 진입점은 

`main()` 메소드이기 때문에 반드시 있어야 한다는 것이다


<br/>

즉, 자바는 main 메서드를 프로그램 실행의 시작 기점으로 삼기 때문에 main 메서드가 필요한 것이다.

```java
public static void main(String[] args) {
}
```

<br/>

## main 메서드는 문자열 배열을 매개 변수로 선언한 이유는 뭘까?

문자열 배열을 매개변수로 주지 않으면 아래와 같은 에러가 발생한다.

- 매개변수를 사용하지도 않을 것인데, 왜 문자열 배열을 매개변수로 할당 해줘야 하는 것일까?

```java
/*
 오류: chapter1.Hello 클래스에서 기본 메소드를 찾을 수 없습니다.
 다음 형식으로 기본 메소드를 정의하십시오.
 public static void main(String[] args) 또는 JavaFX 애플리케이션 클래스는 javafx.application.Application을 확장해야 합니다.
*/
class Main {
	public static void main(){ // 매개변수 제거
    	System.out.println("Hello")
  }
}
```

<br/>

## 이유는

프로그램을 실행하면 `JVM`은 길이가 `0`인 `String` 배열을 먼저 생성하고 

`main()` 메서드를 호출할 때 매개값으로 전달한다고 한다.

`main()`메서드를 호출할 때 문자열 배열이 할당되어야 하는 것인데 

클래스의 메서드 선언시 매개변수를 주지 않아 버리니 에러가 발생하는 것이다.

<br/>

## 자바는 설계시에 문자열 배열을 생성하고 매개값으로 전달되도록 설계했을까?


이유는 프로그램을 실행할 때 외부에서 프로그램으로 데이터를 전달 해줘야 하는 경우가 있기 때문이다.

<br/>

이 때는 보통 커맨드라인을 통하여 데이터를 전달하면서 명령어로 실행한다. 

이 명령어는 문자열이기 때문에 문자열로 인식을 받아야 하는 것이다.



또 배열인 이유는 여러 가지 데이터가 전달될 수 있기 때문이다.

<br/>

또한 할당되는 매개변수가 정수열, 실수열 등 배열이 아닌 문자열 배열인 이유는 

프로그램이 실행되기 위해서는 커맨드라인을 통해 명령어로 프로그램을 실행하는데 

커맨드라인 명령행은 전부 문자이기 때문에 문자열 배열이 할당되어야 하는 것이다.

<br/>

## 궁금증!

```java
"길이가 0인 String 배열을 만들어서 넘긴다"는 게 정말일까? null이 아니라?
```

찍어보면 바로 확인된다.

```java
public class Args {
    public static void main(String[] args) {
        System.out.println("args == null ? " + (args == null));
        System.out.println("길이 = " + args.length);
        for (int i = 0; i < args.length; i++) {
            System.out.println("args[" + i + "] = " + args[i]
                    + " (타입 " + args[i].getClass().getSimpleName() + ")");
        }
    }
}
```

<br/>

### 인자 없이 실행

```java
$ java Args

args == null ? false
길이 = 0
```

`null` 이 아니라 빈 배열이다. 그래서 `args.length` 를 그냥 불러도 `NullPointerException` 이 안 난다.

<br/>

### 인자를 주고 실행

```java
$ java Args hello 123 true

args == null ? false
길이 = 3
args[0] = hello (타입 String)
args[1] = 123 (타입 String)
args[2] = true (타입 String)
```

<br/>

`123` 과 `true` 를 넘겼는데도 전부 `String` 이다.

커맨드라인에서 넘어오는 것은 어차피 다 글자라서, 숫자로 쓰려면 직접 바꿔야 한다.

```java
int number = Integer.parseInt(args[0]);
```

<br/>

## String... 으로 적어도 된다

```java
public static void main(String... args) {
    System.out.println("String... 으로도 실행됨");
}
```

```java
String... 으로도 실행됨
```

<br/>

가변 인자는 컴파일하면 결국 배열이 되기 때문이다.

`String[]` 이든 `String...` 이든 바이트코드 상으로는 같은 시그니처라서 JVM이 못 알아본다.

<br/>

## static을 빼면 어떻게 되는가

```java
public class NoStatic {
    public void main(String[] args) {      // static 제거
        System.out.println("실행될까");
    }
}
```

<br/>

컴파일은 통과한다. 문법적으로 아무 문제가 없기 때문이다.

그런데 실행하면 이렇게 나온다.

```java
Error: Main method not found in class NoStatic, please define the main method as:
   public static void main(String[] args)
```

<br/>

이유는 간단하다. 인스턴스 메서드를 부르려면 객체가 있어야 하는데,

객체를 만들려면 누군가 `new` 를 해야 하고, 그 `new` 를 적을 자리가 아직 없다.

<br/>

프로그램이 시작되기도 전이라 객체를 만들어줄 코드가 어디에도 없는 것이다.

`static` 이어야만 JVM이 객체 없이 클래스만 보고 바로 부를 수 있다.

```java
public  -> JVM이 클래스 밖에서 부르니까 열려 있어야 한다
static  -> 객체를 만들 방법이 없으니 객체 없이 부를 수 있어야 한다
void    -> 돌려줘봐야 받을 자바 코드가 없다 (종료 코드는 System.exit 로 준다)
String[] -> 커맨드라인에서 넘어오는 것은 전부 글자이고, 여러 개일 수 있다
```

네 가지가 전부 `프로그램이 시작되는 지점` 이라는 특수한 상황에서 나온 결과다.

<br/>

## 이름이 왜 하필 main인가

문법적인 이유는 없다. JVM에 그렇게 박혀 있을 뿐이다.

<br/>

`java 클래스이름` 을 실행하면 JVM은 그 클래스를 로드한 뒤

`main` 이라는 이름에 `([Ljava/lang/String;)V` 라는 시그니처를 가진 `public static` 메서드를 찾는다.

찾으면 부르고, 없으면 위에서 본 에러를 낸다.

<br/>

`C` 에서 `main` 을 진입점으로 쓰던 관습을 그대로 가져온 것이다.

`main` 대신 `start` 로 하자고 정했다면 지금 우리는 `start` 를 적고 있었을 것이다.

<br/>

## 진입점이 항상 main인 것도 아니다

우리가 직접 `main` 을 안 적는 경우도 많다.

- 스프링 부트 : `main` 은 있지만 `SpringApplication.run()` 한 줄이고, 진짜 시작은 그 안이다

- 서블릿 컨테이너 : 톰캣의 `main` 이 돌고, 우리 코드는 톰캣이 불러준다
- JUnit 테스트 : 테스트 실행기가 리플렉션으로 `@Test` 메서드를 찾아서 부른다

<br/>

공통점은 `누군가의 main` 이 반드시 하나는 있다는 것이다.

JVM 입장에서는 시작할 지점이 하나 필요하고, 그 뒤로는 그 코드가 알아서 흘러가는 것뿐이다.

```java
java 명령 -> 클래스 로드 -> main 호출 -> 그 다음은 우리 코드의 몫
```
