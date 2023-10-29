## static에 대해 설명 / static을 사용하는 이유는?

<br/>

## static에 대해 설명해주세요.

static 키워드를 사용한 변수나 메소드는 클래스가 메모리에 올라갈 때 

자동으로 생성되며 클래스 로딩이 끝나면 바로 사용할 수 있습니다. 

<br/>

즉, 인스턴스(객체) 생성 없이 바로 사용 가능합니다.

모든 객체가 메모리를 공유한다는 특징이 있고, GC 관리 영역 밖에 있기 때문에 프로그램이 

종료될 때까지 메모리에 값이 유지된 채로 존재하게 됩니다.

<br/><br/>

## static을 사용하는 이유는?

static은 자주 변하지 않는 값이나 공통으로 사용되는 값 같은 공용자원에 대한 
접근에 있어서 

매번 메모리에 로딩하거나 값을 읽어들이는 것보다 일종의 '전역변수'와 같은 개념을 통해 

접근하는 것이 비용도 줄이고 효율을 높일 수 있습니다.

<br/>

인스턴스 생성 없이 바로 사용 가능하기 때문에 프로그램 내에서 

공통으로 사용되는 데이터들을 관리할 때 이용합니다.

<br/><br/>

## static 키워드 사용한다는 의미는?

메모리에 한번 할당되어 프로그램이 종료될 때 해제되는 것을 의미한다.

```java
일반적으로 우리가 만든 "Class"는 "Static"영역에 생성되고, 
"new"연산을 통해 생성한 객체는 "Heap"영역에 생성 된다
```

<br/>

객체의 생성 시에 할당된 `Heap`영역의 메모리는 

`Garbage Collector`를 통해 수시로 관리를 받습니다.

<br/>

### 하지만

`Static` 키워드를 통해 `Static` 영역에 할당된 메모리는 모든 객체가 
공유하는 

메모리라는 장점을 
지니지만, `Garbage Collector`의 관리 영역 밖에 존재하므로 

`Static`을 자주 사용하면 프로그램의 종료시까지 메모리가 할당된 채로 
존재하므로 악영향을 줄 수도 있게 된다

<br/><br/>

## static 변수의 특징은?

`Static` 변수는 클래스 변수입니다. 

객체를 생성하지 않고도 `Static` 자원에 접근이 가능합니다.

```java
public class MyCalculator {
    public static appName = "MyCalculator";

    public static int add(int x, int y) {
        return x + y;
    }

    public int min(int x, int y) {
        return x - y;
    }
}

// static 메소드 이므로 객체 생성 없이 사용 가능
MyCalculator.add(1, 2); 
MyCalculator.min(1, 2);
```

<br/><br/>

## **Static 변수(=정적 변수)**

메모리에 고정적으로 할당되어, 프로그램이 종료될 때 해제되는 변수이다

<br/>

`Java`에서 `Static` 변수는 메모리에 한번 할당되어 프로그램이 종료될 때 해제되는 

변수로, 메모리에 한번 할당되므로 
여러 객체가 해당 메모리를 공유하게 됩니다.

<br/><br/>

## 언제 static 키워드를 사용해야 되나요?

```java
public class Person {
    private String name = "MinSeok";

    public void printName() {
        System.out.println(this.name);
    }
}
```

위와 같은 클래스를 통해 100명의 `Person` 객체를 생성하면, 

`"MinSeok"`라는 같은 값을 갖는 메모리가 100개나 중복해서 생성하게 되는 것이다

<br/>

### 이러한 경우!

`static`을 사용하여 여러 객체가 하나의 메모리를 참조하도록 하면,메모리 효율이 더욱 높아질 것이다

<br/>

또한 `"MinSeok"`라는 이름은 결코 변하지 않는 값이므로 `final` 키워드를 붙여주며,

일반적으로 `Static`은 상수의 값을 갖는 경우가 많으므로 `public`으로 선언을 하여 사용합니다.

<br/>

### 그리하여

일반적으로 `static` 변수는 `public` 및 `final`과 함께 사용되어 `public static final`로 활용 됩니다.

```java
public class Person {
     public static final String name = "MinSeok";

     public static void printName() {
         System.out.println(this.name);
     }
}
```

<br/>

`printName`이라는 함수 역시 `static` 키워드가 붙어서 `static`메소드가 되었는데,

`static` 변수는 `static` 메소드를 통해 접근하도록 권장되기 때문이다

<br/><br/>

## static 변수는 static 메소드를 통해 접근하는 이유는?

`static` 메소드는 객체의 생성 없이 접근하는 함수이므로, 

할당되지 않은 메모리 영역에 접근 하므로 문제가 발생하게 됩니다. 

<br/>

### 그러므로

`static` 변수에 접근하기 위한 메소드는 반드시 `static` 메소드가 되어야 합니다.

<br/><br/>

## static의 장점

1. `Static`이 메모리 측면에서 효율적일 수 있다는 점이다.

    - `Static` 메모리 영역에 저장되어 고정된 메모리 영역을 사용하기 때문에
        
        매번 인스턴스를 생성하며 낭비되는 메모리를 줄일 수 있다.
        
2. 객체를 생성하지 않고, 사용 가능 하기 때문에 속도가 빠르다는 것이다.

    - 클래스가 메모리에 올라가는 시점에 생성되어
        
        바로 사용이 가능하기에 속도면에서 이점을 가진다.
        

<br/><br/>

## Static의 단점

1. 프로그램 종료시까지 메모리에 할당된 채로 존재한다는 것이다.

    - 우리가 만든 Class는 프로그램 실행 시 `Static` 영역에 생성된다.
        
        그런데, `Garbage Collector`를 통해 수시로 관리를 받는 `Heap` 영역과 다르게 
        
        `Static` 영역은 `Garbage Collector`의 관리를 받지 않는다. 
        
2. `Static`이 객체 지향적이지 못하다는 점이다.
    - `Static`은 따로 객체를 생성하지 않고, 메모리의 `Static`영역에 할당된 곳에서
        
        여러 클래스들이 데이터를 불러온다. 
        
        이러한 `Static`의 특징은 객체의 데이터들이 `캡슐화`되어야 한다는 
        
        객체지향 프로그래밍의 원칙을 위반한다.
        
3. `Static` 메서드는 `Interface`를 구현하는데 사용될 수 없다는 점이다.
    - `Static` 메서드는 코드의 재사용성을 높여주는
        
        자바의 유용한 객체 지향적 기능(Interface 등)을 사용하는 것을 방해한다.
        
<br/><br/>

## 그렇다면, static은 사용하면 안되나요?

`Static`은 큰 단점을 가지지만 장점 또한 가지고 있다. 

<br/>

프로그램을 만들때, 무작정 `Static`을 사용하지 않기보다는 위에 제시된 단점들을 고려하여 

적절하게 `Static` 키워드를 사용해야 한다면 가독성 높고 효율적인 코딩을 할 수 있을 것이다.

<br/><br/>

## 메인 메서드가 static인 이유는?

프로그램 시작 전에 메모리에 로딩 된다.

인스턴스 생성과 관계없이 제일 먼저 말이다. 

<br/>

이유는 → 호출되는 메소드로 중복된 메서드가 있으면 안되기 때문이다.

<br/>

그리고 스태틱으로 지정하게 되면 `‘오버라이딩이 일어나지 않는다’` → 

이 말은 즉, 메서드가 딱 한개만 존재한다고 말할 수 있다.