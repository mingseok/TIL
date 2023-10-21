## toString() 내부 동작

toString() 메소드는 객체를 문자열로 리턴하는 메소드이다.

<br/>

## 코드로 설명

```java
public class Member {
    private String name;
    private int age;

    public Member(String name, int age) {
        this.name = name;
        this.age = age;
    }  
}

public class Main {
    public static void main(String[] args) {
        Member member = new Member("minseok", 27);

        System.out.println(member); // Member@123772c4
        System.out.println(member.toString()); // Member@123772c4
    }
}
```

<br/><br/>

## 객체 출력 시 컴파일러가 자동으로 toString() 메소드를 호출한다

위 예제 처럼, 객체만 출력한 것과 객체에 `toString()` 메소드를 

적용한 것이 동일하다는 것일까? → 정답은 동일하다.

<br/>

기본적으로 `PrintStream` 클래스의 출력 관련 메소드(print, println 등)를 통해 

객체를 출력하도록 명령하면 컴파일러가 내부적으로 `toString()` 메소드를 호출한다. 

```java
굳이, 객체.toString 형태를 가질 필요가 없다는 뜻이 된다.
```

<br/><br/>

## 그렇다면, 내가 원하는 ****결괏값 도출하고 싶다면?****

`toString` 메소드를 `Overriding(재정의)` 하면 된다.

```java
public class Member {
    private String name;
    private int age;

    public Member(String name, int age) {
        this.name = name;
        this.age = age;
    }  

    @Override
    public String toString() {
        return "Name: " + name + " Age: " + age; 
    }
}

public class main {
    public static void main(String[] args) {
        Member member = new Member("minseok", 27); // Name: minseok Age: 27
        System.out.println(member); // Name: minseok Age: 27
    }
}

```

포인트는 `리턴값`은 개발자가 원하는 값으로 마음대로 설정할 수 있다.