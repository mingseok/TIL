## 싱글톤 패턴 (Singleton Pattern)

싱글톤 패턴은 인스턴스를 오직 1개만 생성하는 패턴입니다.

예를 들면, 디비 커넥션이나 스레드 풀 객체를 생성할 때 사용합니다.

<br/><br/>

## 코드로 확인

```java
public class Singleton {

    private static Singleton instance = new Singleton();
    
    private Singleton() {
        // 생성자는 외부에서 호출하지 못하게 private 으로 지정해야 한다.
    }

    public static Singleton getInstance() {
        return instance;
    }
}
```

<br/><br/>

## 싱글톤 패턴을 사용하는 이유는?

```java
인스턴스를 오직 한개로만 가져가면 어떤 좋은점이 있다는 거지?
```

<br/>

### 메모리 측면

최초 한번의 `new` 연산자를 통해서 고정된 메모리 영역을 사용하기 때문에 

추후 해당 객체에 접근할 때, 메모리 낭비를 방지할 수 있다

<br/>

### 데이터 공유가 쉽다

싱글톤 인스턴스가 전역으로 사용되는 인스턴스이기 때문에, 

다른 클래스의 인스턴스들이 접근하여 사용할 수 있다

```java
데이터 공유의 문제점!
- 여러 클래스의 인스턴스에서 싱글톤 인스턴스의 데이터를 
  동시에 접근하게 되면 동시성 문제가 발생할 수 있으니 이점을 유의해야 한다
```

<br/><br/>

## 이렇게

도메인 관점에서 인스턴스가 한개만 존재하는 것을 

보증하고 싶은 경우 싱글톤 패턴을 사용한다

<br/><br/>


## 싱글톤 패턴 문제점

- 의존관계상 클라이언트가 구체 클래스에 의존한다. `DIP`를 위반한다.

- `테스트`하기 어렵다.

- 내부 속성을 변경하거나 `초기화` 하기 어렵다.

- `private` 생성자로 자식 클래스를 만들기 어렵다.

- 결론적으로 `유연성`이 떨어진다.

- 안티패턴으로 불리기도 한다.

<br/><br/>

## 문제점 정리

싱글톤 패턴은 안티패턴으로 불릴 만큼 단독으로 사용한다면 

객체 지향에 위반되는 사례가 많다는 것이다 

- 단일 책임 원칙을 위반합니다.

- 테스트하기가 어렵다는 것이다

- `DIP`를 위반, `OCP` 원칙 또한 위반할 가능성이 있다.

```java
스프링 컨테이너 같은 프레임워크의 도움을 받으면 
싱글톤 패턴의 문제점들을 보완하면서 장점의 혜택을 누릴 수 있다
```

<br/><br/>

## 싱글톤 패턴은 언제 사용 되는 건가요?

싱글톤은 스프링에서 사용 됩니다

- 스프링에서 싱글톤을 저장하고, 관리해주는 친구가 바로 `applicationContext` 이다.
    - 명칭은 : IOC 컨테이너, 스프링 컨테이너, 빈 팩토리 등으로 불린다.

- DB 커넥션

    - 자바는 데이터 베이스를 연결할때, 커넥션 객체를 하나 만들어서
        
        데이터 베이스와 통신을 한다. → 여기서 DB와 계속해서 연결을 유지하지 않는다.
        
        - 끊고 → 연결 → 끊고 → 연결 → (반복)
            - 그렇기에, 매번 커넥션 객체 생성하는 것이 아니라,
                
                만들어 놓고 필요할 때 마다 가져다 쓰는 것이다.
                
- 스레드 풀
    - 스레드는 생성하는데 시간이 오래 걸린다.
        - 그렇기에, 미리 스레드를 여러개 만들어 놓고
            
            사용 → 반납 → 사용 → 반납 하는 것이다.
            

<br/><br/>

## 스프링은 왜 Bean 을 Singleton으로 생성할까?

스프링에서 하나의 요청을 처리하기 위해서는 다양한 기능을 

담당하는 객체들이 계층형을 이루고 있다고 한다. 

```java
클라이언트 요청 마다 각 로직을 담당하는 객체를 만들어 사용한다면,
GC가 있더라도 메모리 부하가 올 수 있다는 점이다
```

<br/>

서블릿은 대부분의 멀티 스레딩 환경에서 싱글톤을 동작하며 

서블릿 클래스 하나당 하나의 객체를 생성하여, 

클라이언트 요청 처리를 담당하는 스레드들이 해당 객체를 공유해서 사용한다

<br/><br/>

## 그렇다면, 스프링은 어떻게 빈을 싱글톤으로 생성할까?

Bean을 어떻게 만드는지 생각하면 된다

- 스프링은 어노테이션 설정만으로 IoC 컨테이너에 제어권을 넘겨줌으로써
    
    손쉽게 Bean을 싱글톤으로 생성하여 사용할 수 있다
    

```java
Component-scan 대상이 되는 어노테이션들 
@Repository, @Service, @Controller, @Component 등을 사용하면 된다
```
<br/>

## 궁금증!

```java
private 생성자면 정말 못 만드나
```

리플렉션으로 뚫어봤다.

```java
Eager a = Eager.getInstance();

Constructor<Eager> c = Eager.class.getDeclaredConstructor();
c.setAccessible(true);
Eager b = c.newInstance();
```

<br/>

### 결과

```java
getInstance() == 리플렉션으로 만든 것 ? false
```

<br/>

서로 다른 객체다. `private` 생성자가 뚫린 것이다.

<br/>

앞의 리플렉션(Reflection) 글에서 본 `setAccessible(true)` 가 접근 제어를 무시한다.

`private` 은 컴파일러가 지키는 약속이지, 런타임에 잠긴 문이 아닌 것이다.

<br/>

## enum 으로 만들면 막힌다

```java
enum EnumSingleton { INSTANCE }
```

```java
Constructor<?> ec = EnumSingleton.class.getDeclaredConstructors()[0];
ec.setAccessible(true);
ec.newInstance();
```

<br/>

### 결과

```java
IllegalArgumentException : Cannot reflectively create enum objects
```

<br/>

`newInstance()` 안에 `enum` 이면 거부하는 코드가 박혀 있다.

JVM 차원에서 막아둔 것이라 우회할 방법이 없다.

<br/>

앞의 enum 은 어떻게 동작할까 글에서 본 대로,

`enum` 상수는 클래스가 로딩될 때 `static` 블록에서 한 번만 만들어진다.

<br/>

## 직렬화도 막아준다

일반 싱글톤은 역직렬화하면 새 객체가 생긴다.

```java
객체를 파일로 저장 -> 다시 읽으면 -> 새로운 객체
```

<br/>

막으려면 이 메서드를 직접 넣어야 한다.

```java
private Object readResolve() {
    return INSTANCE;         // 새로 만든 것 대신 기존 것을 돌려준다
}
```

<br/>

`enum` 은 이걸 안 써도 된다.

역직렬화할 때 이름으로 기존 상수를 찾아오도록 자바가 정해놨기 때문이다.

<br/>

## 지연 초기화의 함정

`getInstance()` 에서 처음 부를 때 만드는 방식이 있다.

```java
public static Singleton getInstance() {
    if (instance == null) {
        instance = new Singleton();
    }
    return instance;
}
```

<br/>

멀티 스레드에서 깨진다.

앞의 조회수 증가 문제를 다룬 글에서 본 것과 똑같은 구조다.

```java
스레드 A: null 인지 확인 -> 맞다
스레드 B: null 인지 확인 -> 맞다 (A 가 아직 안 만들었다)
둘 다 new 한다 -> 객체가 두 개
```

<br/>

`synchronized` 를 붙이면 막히는데, 부를 때마다 락을 잡아서 느려진다.

<br/>

## 그래서 홀더 방식을 쓴다

```java
public class Singleton {
    private Singleton() {}

    private static class Holder {
        private static final Singleton INSTANCE = new Singleton();
    }

    public static Singleton getInstance() {
        return Holder.INSTANCE;
    }
}
```

<br/>

`Holder` 는 `getInstance()` 를 처음 부를 때 로딩된다.

<br/>

앞의 클래스 로딩 순서 글에서 본 대로,

클래스 초기화는 JVM이 락을 걸고 한 번만 실행한다.

```java
내가 synchronized 를 안 써도
클래스 초기화 자체가 이미 thread-safe 하다
```

<br/>

락 비용도 없고 코드도 짧아서, `enum` 을 안 쓸 거면 이 방식이 표준이다.

<br/>

## 실무에서는 직접 안 만든다

앞의 싱글톤 패턴, 싱글톤 방식의 주의점 글에서 본 그 이유다.

```java
MemberRepository repo = MemoryMemberRepository.getInstance();
```

<br/>

`getInstance()` 를 부르는 쪽이 구체 클래스를 알게 된다.

테스트에서 다른 것으로 못 바꾼다.

<br/>

스프링 컨테이너가 관리하면 이게 다 풀린다.

```java
public OrderService(MemberRepository repo) { ... }      // 인터페이스만 안다
```

<br/>

싱글톤이라는 결과는 같은데, 만드는 책임을 컨테이너가 가져간 것이다.

<br/>

## 싱글톤을 쓰면 안 되는 경우

상태를 가지는 객체다.

```java
@Component
public class Counter {
    private int count;        // 모든 요청이 이 값을 공유한다
}
```

<br/>

앞의 멀티 쓰레드 이해 글에서 본 그 문제가 생긴다.

<br/>

그리고 테스트 사이에 상태가 남는다.

```java
테스트 A 에서 count 를 5 로 만들었다
테스트 B 가 그 상태에서 시작한다
```

<br/>

테스트 순서에 따라 결과가 달라지는 것이라, 원인을 찾기가 아주 어렵다.

싱글톤은 무상태여야 한다는 규칙이 여기서 나온 것이다.
