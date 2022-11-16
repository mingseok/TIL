## 추상 클래스 보다는 인터페이스를 우선하라

## 두줄 정리

정리하자면 일반적으로 다중 구현용 타입으로는 인터페이스가 가장 적절하며 

재사용성 측면이나 유연성 측면 그리고 다형성 측면에서 인터페이스를 우선하는 것이 옳다.


<br/><br/>

### 추상 클래스 보다는 인터페이스를 우선하라

자바에는 인터페이스와 추상 클래스를 제공한다.

자바 8 부터는 인터페이스에 default method를 제공하게 되어 

인퍼테이스와 추상 클래스 모두 인스턴스 메소드를 구현 형태로 제공할 수 있게되었다.

<br/><br/>

## 그렇다면 둘의 차이는 무엇일까?

추상 클래스를 상속받아 구현하는 클래스는 반드시 추상 클래스의 하위 타입이 되어야한다는 점이다.

즉, 자바에서는 단일 상속만 지원 하기 때문에 한 추상 클래스를 상속받은 클래스는 다른 클래스를 상속받을 수 없게되는 것이다.

<br/>

이와 반대로 인터페이스는 구현해야할 메소드만 올바르게 구현한다면 어떤 다른 클래스를 

상속했던 간에 같은 타입으로 취급된다.

<br/><br/>

## 인터페이스에는 다음과 같은 장점이 있다.

1. 기존 클래스에도 손쉽게 새로운 인터페이스를 구현해 넣을 수 있다.

2. 인터페이스는 믹스인 정의에 안성맞춤이다.

3. 인터페이스로는 계층구조가 없는 타입 프레임워크를 만들 수 있다.

4. 래퍼 클래스 관용구와 함께 사용한다면 인터페이스는 기능을 향상시키는 안전하고 강력한 수단이 된다.

<br/><br/>

## 1. 기존 클래스에도 손쉽게 새로운 인터페이스를 구현해 넣을 수 있다.

실제로 `Comparable`, `Iterable`, `AutoCloseabl` 등 새로운 인터페이스를 수 많은 기존 클래스가 

이 인터페이스를 구현한 채 릴리스됐다.

하지만 기존 클래스에 새로운 추상 클래스를 끼워 넣기는 어렵다. 

두 클래스가 같은 추상 클래스를 확장하려면, 그 추상 클래스는 계층 구조상 두 클래스의 공통 조상이어야 한다. 

즉 큰 혼란을 가져온다.

<br/><br/>

## 2. 인터페이스는 믹스인(mixin) 정의에 안성맞춤이다.

믹스인이란 어떤 클래스의 주 기능에 추가적인 기능을 혼합한다 하여서 믹스인이라고 한다. 

그러므로 믹스인 인터페이스는 어떤 클래스의 주 기능이외에 믹스인 인터페이스의 기능을 

추가적으로 제공하게 해주는 효과를 준다.

<br/>

추상 클래스가 믹스인 정의에 맞지않은 이유는 기존 클래스에 덧씌울 수 없기 때문이다. 

자바는 단일 상속을 지원하기 때문에 한 클래스가 두 부모를 가질 수 없고 부모와 자식이라는 

클래스 계층에서 믹스인이 들어갈 합리적인 위치가 없다.

<br/>

### 믹스인 인터페이스엔 대표적으로 Comparable, Cloneable, Serializable 이 존재한다.

```java
public class Mixin implements Comparable {

	@Override
	public int compareTo(Object o) {
    	return 0;
    }
}
```

<br/><br/>

## 3. 인터페이스로는 계층구조가 없는 타입 프레임워크를 만들 수 있다.

현실의 개념 중에는 동물과 포유류, 파충류, 조류 와 같이 타입을 계층적으로 정의하여 

구조적으로 잘 표현할 수 있는 개념이 있는가 하면 



가수와 작곡가, 그리고 가수겸 작곡가(SingerSongWriter) 같이 계층적으로 표현하기 어려운 개념이 존재한다.

<br/>

이런 계층구조가 없는 개념들은 인터페이스로 만들기 편하다.

가수, 작곡가 인터페이스가 존재한다. 

또한 가수겸 작곡가도 존재한다. 가수와 작곡가를 인터페이스로 정의하였으니 

사람이라는 클래스가 두 인터페이스를 구현해도 전혀 문제가 되지 않는다.

```java
public class People implements Singer, SongWriter {

    @Override
    public void Sing(String s) {

    }
    @Override
    public void Compose(int chartPosition) {

    }
}
```

<br/>

또한 두 인터페이스를 확장하고 새로운 메소드까지 추가한 인터페이스 또한 정의할 수 있다.

```java
public interface SingerSongWriter extends Singer, SongWriter {
    void strum();
    void actSensitive();
}
```

<br/>

## 만약 이러한 구조를 추상 클래스로 만들면 어떻게 될까?

```java
public abstract class Singer {
    abstract void sing(String s);
}

public abstract class SongWriter {
    abstract void compose(int chartPosition);
}

public abstract class SingerSongWriter {
    abstract void strum();
    abstract void actSensitive();
    abstract void Compose(int chartPosition);
    abstract void sing(String s);
}
```

추상 클래스로 만들었기 때문에 Singer 클래스와 SongWriter 클래스를 둘다 

상속할 수 없어 SIngerSongWriter라는 또 다른 추상 클래스를 만들어서 클래스 계층을 표현할 수 밖에 없다. 

<br/>

만약 이런 Singer 와 SongWriter와 같은 속성들이 많이 있다면, 

그러한 클래스 계층구조를 만들기 위해 많은 조합이 필요하고 결국엔 고도비만 계층구조가 만들어질 것이다. 

이러한 현상을 조합 폭발이라고 한다.

<br/><br/>

## 4. 인터페이스는 기능을 향상 시키는 안전하고 강력한 수단이 된다.

타입을 추상 클래스로 정의해두면 그 타입에 기능을 추가하는 방법은 상속뿐이다. 

상속해서 만든 클래스는 래퍼 클래스보다 활용도가 떨어지고 깨지기 쉽다.

<br/>

인터페이스의 메서드 중 구현 방법이 명백한 것이 있다면, 디폴트 메소드로 만들 수 있다.

그러나 디폴트 메서드는 제약이 있다.

- equals와 hashcode를 디폴트 메소드로 제공 안함

- 인터페이스는 인스턴스ㄹ 필드를 가질 수 없고, private 정적 메소드를 가질 수 없다.
- 본인이 만든 인터페이스가 아니면 디폴트 메소드 추가 불가능

<br/><br/>

## 추상 골격 구현 클래스

디폴트 메소드의 기능을 제공해주면서 개발자들이 중복되는 메소드의 구현을 하는 수고를 덜어주었다.

하지만 디폴트 메소드의 경우 여러 단점이 존재하기 때문에 추상 골격 구현 클래스를 

제공함으로써 인터페이스와 추상 클래스의 장점을 모두 가져갈 수 있다.

<br/><br/>

## 디폴트 메소드 단점

1. Object 메소드인 equals와 hashcode를 디폴트 메소드로 제공 안함.

2. 인터페이스는 인스턴스 필드를 가질 수 없고 public이 아닌 정적 메소드를 가질 수 없음.

3. 본인이 만든 인터페이스가 아니면 디폴트 메소드를 추가할 수 없음.

이에 대해 간략히 설명하면, 인터페이스로는 타입을 정의하고, 

메소드 구현이 필요한 부분은 추상 골격 구현 클래스에서 구현하는 것이다.

<br/>

이렇게 글로는 이해하기 어려우므로 간단한 예제를 통해 살펴보자.

코드 참고 - [https://it-mesung.tistory.com/192](https://it-mesung.tistory.com/192)

```java
//추상 골격 구현 클래스 사용 하지 않는 버전.
public interface Character {
  public void move();
  public void seat();
  public void attack();
}

public class Thief implements Character{
  @Override
  public void move() {
    System.out.println("걷다");
  }

  @Override
  public void seat() {
    System.out.println("앉다");
  }

  @Override
  public void attack() {
    System.out.println("표창을 던진다");
  }    
}

public class Wizard implements Character{
  @Override
  public void move() {
    System.out.println("걷다");
  }

  @Override
  public void seat() {
    System.out.println("앉다");
  }

  @Override
  public void attack() {
    System.out.println("마법봉을 휘두르다");
  }
}

public static void main(String[] args) {
  Thief thief = new Thief();
  Wizard wizard = new Wizard();
  thief.process();
  wizard.process();
}
```

위 소스에서 보는 바와 같이 attack() 메소드를 제외하고는 모두 중복 되는 것을 볼 수 있다. 

이런 중복된 부분을 추상 골격 구현 클래스를 이용하여 정의 하는 것이다.

<br/><br/>

```java
//추상 골격 구현 클래스 사용하는 버전
public abstract class AbstractCharacter implements Character{
  @Override
  public void move() {
    System.out.println("걷다");
  }

  @Override
  public void seat() {
    System.out.println("앉다");
  }

  @Override
  public void process() {
    move();
    seat();
    attack();
  }
}

public class Thief extends AbstractCharacter implements Character{
    @Override
    public void attack() {
        System.out.println("표창을 던진다");
    }
}

public class Wizard extends AbstractCharacter implements Character{
    @Override
    public void attack() {
        System.out.println("마법봉을 휘두르다");
    }
}
```

이 처럼 디폴트 메소드를 사용하지 않고 추상 골격 구현 클래스(AbstractCharacter)를 구현하여 중복을 없앨 수 있다.

<br/><br/>

## 정리

일반적으로 다중 구현용 타입으로는 인터페이스가 가장 적합하다. 

복잡한 인터페이스라면 구현하는 수고를 덜어주는 골격 구현을 함께 제공하는 방법을 꼭 고려해보자.

골격 구현은 가능한 한 인터페이스의 디폴트 메서드로 제공하여 그 인터페이스를 구현한 모든 곳에서 활용하도록 하는 것이 좋다. 

가능한 한이라고 말한 것은 인터페이스에 걸여 있는 구현상의 제약 때문에 추상 클래스로 골격 구현을 제공하는 경우가 흔해서이다.