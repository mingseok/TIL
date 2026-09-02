## JVM 동작원리와 내부구조

JVM, 자바 가상머신은 단순하게 말하면 컴파일 된 코드(바이트코드)를 실행시켜주는 가상의 컴퓨터라고 생각하면 편하다.

```java
어딜 가든간에 현금 뽑으려면 'ATM' 찾듯이 -> 어느 컴퓨터에서든 바이트코드를 돌리려면 'JVM'을 찾으면 된다
```

<br/>

## 자바는 JVM에 의해 실행 된다

즉, 운영체제에 상관없이 실행 될 수 있고, 가비지 컬렉터(GC)로 메모리 관리를 

자동으로 관리 해준다는 점에서 안정적인 프로그래밍을 할 수 있다는 것이다

<br/>

## JVM의 구성은 크게 4가지로 구분된다

4가지에 대해서는 밑에서 하나씩 설명하겠다.

- `Class Loader`

- `Runtime Data Area`
- `Execution Engine`
- `Garbage Collector`

<br/>

## JVM 원리

### 첫번째) 

자바 소스 코드는 → `.java` 형태로 저장이 된다.

```java
public A {
   public static void main(String[] args) {
      // ... 생략
   }
}
```

<br/>

### 두번째)

위, 자바 소스 파일을 자바 컴파일러가 `javac.exe` 명령어를 실행 시켜, 바이트코드로 변경해주는데, 그것이 바로 → `.class` 파일로 저장된다

![이미지](/programming/img/입문396.PNG)
```java
바이트코드로 변환하는 이유는?

-> 작성한 코드를 1차적으로 숨기는 이유도 있을 것이고, 바이트코드로 변경하였으니,
   문법 검사와 같은 작업을 이후에는 하지 않게 되면서, 시간을 단축시키는 의미도 있다고 한다
```

<br/>

### 세번째)

클래스 파일(바이트코드를 말함)들은 `Class Loader`가 `JVM` 메모리 영역인, → `Runtime Data Area`로 로딩 시키게 되는 것이다.

클래스 로더는 `바이트 코드`를 읽어서 `JVM`의 `실행 엔진`이 사용할 수 있도록  메모리 영역인 `Runtime Data Area`로 적재한다

```java
바이트코드를 읽고, 클래스 정보를 메모리의 Heap/Method Area에 저장하는 곳이다.
```

![이미지](/programming/img/입문397.PNG)

<br/>

## `Class Loader`의 내부는 어떠한가?

![이미지](/programming/img/입문398.PNG)


- `Loading`

    - 프로그램을 실행시키기 위해 필요한 `class`를 찾기 위한 동작

- `Linking`

    - `JVM`의 메모리에 로드 하기 위해 연결(link) 한다.

- `Initialization`

    - class variable을 초기화하는 역할도 수행

```java
클래스를 메모리에 올리는 로딩 기능은 메모리에 올리지 않고, 어플리케이션에서 필요한 경우 동적으로 메모리에 적재하게 되는 것이다
```

이런 클래스 파일의 로딩은 `Loading` → `Linking` → `Initialization`인 3단계들을 거쳐서 `Runtime Data Area` 로 넘어가게 되는 것이다.

<br/>

### 네번째)

## `Runtime Data Area` 영역은 5가지 영역이 있다

![이미지](/programming/img/입문399.PNG)

### 크게 설명하면 이렇다.

- `Metaspace`, `Heap` : 모든 스레드가 공유되고 있다

- `Stack`, `PC Register`, `Native Method Stack` : 스레드마다 하나씩 생성되는 공간이다

<br/>

## Metaspace 영역

`JVM`이 시작될 때, 생성되는 공간으로 바이트코드가 이 영역에 저장된다

- 상수 풀(Constant Pool)

    - 문자열, 정수, 실수, 클래스 및 인터페이스에 대한 참조 등의 ‘상수 값들’을 저장

- 메소드 정보

    - 클래스의 ‘메소드들에 대한 정보’를 보관  메소드의 이름, 시그니처, 접근 제어자 등을 포함한다.

- 클래스의 ‘필드들에 대한 정보’를 보관합니다.    

    - 필드의 이름, 타입, 접근 제어자 등을 포함

- `클래스 정보`, `변수 정보`, `static으로 선언한 변수`가 저장된다.

- 모든 스레드가 공유하는 영역이다.

- Java 8 이후 버전에서는 XX:MetaspaceSize와 XX:MaxMetaspaceSize 옵션을 사용하여 메타스페이스 영역의 크기를 설정할 수 있다.

<br/>

### Metaspace 에는 별도의 영역이 존재한다.

- `Runtime Constant Pool`이라는 별도의 영역이 존재

    - 상수 자료형을 저장하여 참조하는 역할을 한다

```java
저장되는 정보의 종류는?
- Field Info : 멤버 변수의 이름, 데이터 타입, 접근 제어자의 정보
- Method Info : 메소드 이름, Return 타입, 매개변수, 접근 제어자의 정보
- Type Info : Class인지 Interface인지 여부 저장, Type의 속성, 이름, Super Class의 이름
```

<br/>

## Heap 영역

![이미지](/programming/img/입문400.PNG)


- `Eden 영역` : 새롭게 생성된 객체들이 할당되는 영역

    - `Eden` 영역이 꽉 차면 `survival` 영역으로 넘어가게 된다.

- `Survival 영역(S0, S1)` : `minor gc`로부터 살아남은 객체들이 존재하는 영역

    - `Survival` 영역의 특별한 규칙 : `Survival 0`, 혹은 `Survival 1` 둘 중 하나는 꼭 비어 있어야 한다는 것이다

<br/>

### Heap 영역은

동적으로 생성된 객체가 저장되는 영역으로 GC의 대상이 되는 공간이다

- `new` 연산자로 생성된 모든 `Object`와 `인스턴스 변수`, 그리고 `배열`을 저장한다

### Heap 영역은 두 영역으로 구분할 수 있다

- `Young Generation`
    - 생명 주기가 짧은 객체를 `GC` 대상으로 하는 영역

    - 여기 객체들은 `Eden`이라는 곳에 할당이 된 후에, `Survivor 0`과 `1`을 거쳐,
        
        오래 사용되었다고 판단된 객체들은 `Old Generation`으로 이동하게 되는 것이다
        
- `Old Generation`
    - 생명 주기가 긴 객체를 GC 대상으로 하는 영역

<br/>

## Stack 영역

Stack 영역은 `지역변수`나 `메서드의 매개변수`, 임시적으로 사용되는 변수, 메서드의 정보가 저장되는 영역이다

- 지역변수와, 매개변수의 특성상 해당 메서드의 호출이 종료되면 사라진다

    - 그렇기에, 금방 사용되고, 사라지는 데이터들을 저장하는 공간이다

- 스택 영역의 후입선출(LIFO) 구조

   - 가장 최근에 호출된 메서드가 가장 먼저 실행을 완료하고 스택에서 제거된다.

    - 이러한 구조 때문에 메서드의 재귀 호출과 같은 작업을 처리할 수 있습니다. 

<br/>

## PC Register 영역

스레드가 시작될 때, 생성되며 현재 수행 중인 `JVM`의 명령어 `주소`를 저장하는 공간이다

- 즉, 스레드가 어떤 부분을 어떤 명령어로 수행할 지를 저장하는 공간이다

- JVM의 메모리 영역 중 하나로 각 스레드마다 현재 ‘실행 중인 명령어의 주소’를 저장하는 메모리 공간이다

<br/>

## Native Method Stack 영역

`Java`가 아닌 다른 언어인 c, c++ 같은 코드를 위한 공간이다

- 실제 실행할 수 있는 기계어로 작성된 프로그램을 실행되는 영역이다

```java
각 스레드 별로 생성된다는 특징이 있다
```

<br/>

### 다섯번째)

## `Execution Engine` 이 무엇인가?

바이트 코드를 `네이티브 코드`로 변환 시켜주고, `GC를 실행`하는 실행 엔진

```java
"클래스 로드로 부터 로드된 '.class' 파일의 바이트코드를 실행하는 엔진인 것이다"
```

![이미지](/programming/img/입문401.PNG)

<br/>

### 바이트코드를 실행 시키기 위해서는?

바이트코드를 컴퓨터가 이해할 수 있도록 `기계어`로 바꿔주는 작업이 필요한데, 

`Execution Engine`는 두가지 방법이 사용한다.

- `Interpreter` : 명령어를 한줄 한줄 해석하면서 실행한다

- `JIT Complier` : Interpreter 의 단점을 해결하기 위한 방법으로 런타임 시간에 한꺼번에 변경하여 실행한다
    
- `Garbage Collector` : 더 이상 참조되지 않는 메모리 객체를 모아 제거하는 역할을 수행

<br/>

### 여섯번째)

## Garbage Collector 설명

가비지 컬렉터(GC)로 메모리 관리를 자동으로 관리 해준다는 점에서 안정적인 프로그래밍을 할 수 있다는 것이다

<br/>

### 크게 두가지로 나눌 수 있다

- `Minor GC`

    - `Heap` 영역의 `Young Generation`에서 발생하는 `GC`를 말한다

- `Major GC`

    - `Heap` 영역의 `Old Generation`에서 발생하는 `GC`를 말한다

앞으로 사용되지 않는 객체의 메모리를 `'Garbage'`라고 부른다

- 이런 `'Garbage'`를 정해진 스케줄에 의해 정리해주는 것을 `'GC'`라 부른다

<br/><br/>

## GC가 왜 필요하지?

GC는 메모리 관리 기법 중 하나이다.

```java
프로그램이 "동적으로 할당했던 메모리 영역" 중 "필요 없게 된 영역"을 알아서 해제한다
```

위의 `“동적으로 할당했던 메모리 영역”` 은 → `Heap` 영역 메모리를 뜻하고 `“필요 없게 된 영역”` 은 → 어떤 변수도 가리키지 않게 된 영역을 의미한다

<br/>

### GC의 장점은?

- 개발자의 실수로 인한 메모리 누수를 막을 수 있다

- 해제된 메모리에 접근하는 오류를 막을 수 있다
- 해제된 메모리를 또 해제하는 이중 해제 또한 막을 수 있다

<br/>

### GC의 단점은?

- GC 작업은 순수 오버헤드

- 개발자는 언제 GC가 메모리를 해제하는지 모른다

<br/>

## 'GC' 를 제대로 이해하기 위해서는?

`‘Stop The World’` 를 이해 해야 한다.

- GC를 수행하기 위해 `JVM`이 멈추는 현상을 말한다

- GC가 작동하는 동안 GC관련 `Thread`를 제외한 모든 `Thread`는 멈춘다
- 일반적으로 `‘튜닝’`이라는 것은 이 시간을 최소화하는 것을 의미한다

```java
모든 쓰레드들의 작업을 중단 후 (Stop The World 과정) 
사용하지 않는 메모리를 제거(Mark and Sweep 과정)하고 작업이 재개됩니다.
```

<br/>

## GC의 종류로는?

- Serial GC

- Parallel GC → 자바 8버전에서 사용
- CMS GC
- G1 GC → 자바 9버전, 10버전 부터 사용 (디폴트)
- Z GC

```java
그럼, 이중에서 뭘 선택해야 하는데?
- 나의 어플리케이션에 서비스 특성에 맞는 GC를 선택하는것이 옳다
```

<br/>

## 궁금증!

```java
Class Loader가 "필요한 class를 찾는다" 고 했는데, 어디서 어떻게 찾는 것일까?
```

클래스 로더는 하나가 아니라 세 개가 계층으로 쌓여 있다.

직접 찍어보면 확인된다.

```java
public class Loader {
    public static void main(String[] args) {
        System.out.println("내 클래스      = " + Loader.class.getClassLoader());
        System.out.println("  그 부모      = " + Loader.class.getClassLoader().getParent());
        System.out.println("  그 위의 부모 = " + Loader.class.getClassLoader().getParent().getParent());
        System.out.println("String 클래스  = " + String.class.getClassLoader());
    }
}
```

<br/>

### 결과

```java
내 클래스      = jdk.internal.loader.ClassLoaders$AppClassLoader@1369b299
  그 부모      = jdk.internal.loader.ClassLoaders$PlatformClassLoader@1dbd16a6
  그 위의 부모 = null
String 클래스  = null
```

<br/>

세 계층이 이렇게 나뉘어 있다.

```java
Bootstrap ClassLoader  - java.lang.* 같은 자바 핵심 클래스. C로 짜여 있어서 자바에서는 null 로 보인다
Platform ClassLoader   - 그 외 표준 라이브러리
Application ClassLoader - 우리가 짠 클래스와 라이브러리 (클래스패스)
```

`String` 의 로더가 `null` 인 것은 로더가 없다는 뜻이 아니라, `Bootstrap` 이 자바 객체가 아니라서 그렇다.

<br/>

## 왜 굳이 나눠놨는가

찾을 때 `자기가 먼저 찾지 않고 부모에게 먼저 물어보기` 때문이다.

```java
클래스가 필요하다
-> Application 이 Platform 에게 물어본다
-> Platform 이 Bootstrap 에게 물어본다
-> Bootstrap 이 없다고 하면 Platform 이 찾아본다
-> 그것도 없으면 그때서야 Application 이 찾는다
```

<br/>

이걸 `위임(delegation)` 이라고 한다. 이렇게 하는 이유는 보안이다.

<br/>

누가 `java.lang.String` 이라는 이름으로 악의적인 클래스를 만들어 클래스패스에 넣었다고 해보자.

위임 구조가 없다면 `Application` 이 그것을 먼저 찾아서 로드해버린다.

세상의 모든 문자열이 가짜 `String` 이 되는 것이다.

<br/>

위임 구조에서는 `Bootstrap` 이 진짜 `java.lang.String` 을 먼저 돌려주니, 가짜는 로드될 기회가 없다.

```java
항상 위에서부터 찾는다 -> 핵심 클래스를 아무도 바꿔치기 할 수 없다
```

<br/>

## 클래스는 처음부터 다 로드되지 않는다

원문에 `필요한 경우 동적으로 메모리에 적재한다` 고 되어 있는 그 부분이다.

직접 확인해봤다.

```java
public class Loader {
    public static void main(String[] args) {
        System.out.println("아직 Lazy 를 안 건드림");
        System.out.println("이제 건드린다 -> " + Lazy.VALUE);
    }
}

class Lazy {
    static final String VALUE = make();

    static String make() {
        System.out.println("   (Lazy 클래스 초기화됨)");
        return "값";
    }
}
```

<br/>

### 결과

```java
아직 Lazy 를 안 건드림
   (Lazy 클래스 초기화됨)
이제 건드린다 -> 값
```

<br/>

`Lazy` 클래스는 `Lazy.VALUE` 를 읽으려는 순간에야 초기화됐다.

프로그램이 시작될 때 다 올려놓는 것이 아니라, 진짜 쓸 때 올린다.

<br/>

`main` 이 도는 동안 한 번도 안 쓰는 클래스는 끝까지 로드되지 않는다.

스프링 부트를 띄우면 수천 개 클래스가 로드되지만, 그것도 전부 `쓰이니까` 로드된 것이다.

<br/>

앞에서 본 holder 방식 싱글턴이 동작하는 근거도 이것이다.

```java
public class Singleton {
    private static class Holder {
        private static final Singleton INSTANCE = new Singleton();
    }

    public static Singleton getInstance() {
        return Holder.INSTANCE;
    }
}
```

`getInstance()` 를 처음 부르는 순간에야 `Holder` 가 로드되면서 `INSTANCE` 가 만들어진다.

아무도 안 부르면 객체가 아예 안 만들어지는 것이다.

<br/>

## 로드된 클래스를 눈으로 보려면

```java
java -verbose:class Loader
```

<br/>

```java
[0.008s][info][class,load] jdk.internal.module.ModuleLoaderMap  source: shared objects file
[0.015s][info][class,load] jdk.internal.loader.BootLoader       source: shared objects file
[0.017s][info][class,load] jdk.internal.loader.URLClassPath     source: jrt:/java.base
```

<br/>

`Hello World` 하나 찍는 데도 수백 개의 클래스가 로드된다.

오른쪽의 `source:` 가 어디서 읽어왔는지를 알려준다.

`shared objects file` 은 미리 만들어둔 캐시에서 읽은 것이고, `jrt:/java.base` 는 JDK 모듈에서 읽은 것이다.

<br/>

## Linking 단계에서 하는 검증

원문의 `Loading -> Linking -> Initialization` 중 `Linking` 이 하는 일을 좀 더 풀면 세 가지다.

```java
Verification  : 이 바이트코드가 안전한지 검사한다
Preparation   : static 필드 자리를 잡고 기본값(0, null)으로 채운다
Resolution    : 다른 클래스를 가리키는 이름들을 실제 주소로 바꾼다
```

<br/>

`Verification` 이 있어서 자바는 이상한 `.class` 파일을 실행하지 않는다.

스택이 넘치는 코드나 타입이 안 맞는 코드가 있으면 여기서 거부한다.

<br/>

바이트코드를 손으로 조작해서 넣어도, 검증을 통과하지 못하면 `VerifyError` 가 나면서 실행되지 않는다.

`C` 처럼 잘못된 포인터로 아무 메모리나 건드리는 일이 자바에서 안 생기는 이유 중 하나다.

<br/>

`Preparation` 이 앞에서 봤던 `기본값으로 먼저 채운다` 는 그 단계다.

우리가 적은 초기화식은 아직 실행되지 않고, 다음 단계인 `Initialization` 에서 실행된다.

```java
Preparation    : static int count 자리를 잡고 0 을 넣는다
Initialization : static int count = 10 이라고 적은 것을 실행해서 10 으로 바꾼다
```
