## SOLID

- SRP : 단일 책임 원칙

- OCP : 개방-폐쇄 원칙
- LSP : 리스코프 치환 원칙
- ISP : 인터페이스 분리 원칙
- DIP : 의존관계 역전 원칙

<br/><br/>

## SRP 단일 책임 원칙

한 클래스는 하나의 책임만 가져야 한다.

- 중요한 기준은 변경이다.

    - 변경이 있을때 파급 효과가 적으면 단일 책임 원칙을 잘 따른 것이다.

<br/><br/>


## (중요) OCP : 개방-폐쇄 원칙

소프트웨어 요소는 확장에는 열려 있으나 변경에는 닫혀 있어야 한다.

<br/>

### 다형성을 이용하면 OCP를 할 수 있다?

그렇다고 하지만 문제점이 있다. → 분명히 밑에 코드를 보면 다형성을 잘 사용하고 있다.

<br/>

하지만, `Repository`를 변경하려면 밑에 처럼 코드를 직접 변경해야 되는 것이다.

```java
// MemberRepository m = new MemoryMemberRepository() // 기존 코드
MemberRepository m = new JdbcMemberRepository() // 변경 코드
```

정리 하면, 인터페이스 잘 만들고, 구현체 잘 만들고 하였는데, 적용을 하려고 하니깐

OCP가 깨지는 것이다. (클라이언트 코드를 변경을 해야 된다는 것이 문제!)

<br/><br/>

## OCP를 지킬 수 있는 방법

다형성을 사용하고 클라이언트가 DIP를 잘 지키면 OCP가 적용이 될 수 있는 가능성이 열리는 것이다. 

애플리케이션을 `‘사용 영역’`과 `‘구성 영역’`으로 나눴다.

![이미지](/programming/img/입문11.PNG)

<br/>

`AppConfig`가 의존관계를 `FixDiscountPolicy` → `RateDiscountPolicy` 로 변경 하더라도

`AppConfig`에서 변경된 구현체를 클라이언트 코드에 주입(변경된 것을)해 주는 것이다.

<br/>

때문에 클라이언트 코드에서는 어떠한 코드를 변경하지 않아도 된다.

결과적으로 `소프트웨어 요소를 새롭게 확장해도 사용 영역의 변경은 닫혀 있다.`

즉, 변경에 닫혀 있다는 말은 → 변경할 필요가 없다는 것이다.

<br/><br/>

## LSP 리스코프 치환 원칙

프로그램의 객체는 프로그램의 정확성을 깨뜨리지 않으면서 하위 타입의 인스턴스로 바꿀수 있어야 한다.

- 자동차 인터페이스에 엑셀이란 기능은 “앞으로 가라” 라는 기능이다.

    - 그런데 뒤로 가게 구현해 버리면 이건 LSP 를 위반한 것이다.

    - 느리더라도 ‘앞으로’ 가면 리스코프 치환 원칙을 지키는 것이다.

<br/><br/>

## ISP 인터페이스 분리 원칙

특정 클라이언트를 위한 인터페이스 여러 개가 범용 인터페이스 하나보다 낫다.

- 인터페이스도 기능에 맡게 적당한 크기로 잘 쪼개는 것이 중요하다는 것이다

    - 자동차 인터페이스 → 운전 인터페이스, 정비 인터페이스

    - 인터페이스가 명확해지고, 대처 가능성이 높아진다.

<br/><br/>

## (중요) DIP 의존관계 역전 원칙

프로그래머는 `“추상화에 의존해야지, 구체화에 의존하면 안된다.”`

- 클라이언트 코드가 구현 클래스를 보지 말고, 인터페이스만 바라보라는 뜻이다.

- `멤버Service`가 `멤버Repository`인 인터페이스만 바라봐야 된다는 것이다.

    따라서, `MemoryMemberRepository`나 `JdbcMemberRepository`에 대해서는 몰라야 된다는 것이다.

<br/>

```
운전자는 자동차의 ‘역할’에 대해서만 알아야지
아반떼에 대해서 디테일하게 알고 있다면 잘못된 것이다.

운전자는 자동차의 ‘역할’에 대해서 자세히 알고 있어야 되는 것이다.
```

역할과 구현을 철저하게 분리하도록 시스템을 설계해야 하는 것이다.
 - 다시 말해, 시스템도 언제든지 갈아 끼울 수 있도록 말이다.

<br/><br/>

### DIP를 위반한 코드1)

밑에 코드를 보면 `MemberService` 인 클라이언트가 구현 클래스를 직접 선택하고 있는 코드이다. 

```java
public class MemberService {

    MemberRepository m = new JdbcMemberRepository() // 변경 코드
```

<br/>

### DIP를 위반한 코드2)

`MemberService.class`는 `MemberRepository`에도 의존하고, `MemoryMemberRepository` 에도 의존하고 있다. 

<br/>

즉, 추상화에도 의존하고 구체화에도 의존하고 있는 것이다.

```java
public class MemberService {

    private final MemberRepository memberRepository = new MemoryMemberRepository();

    // ... 코드 생략
}
```

<br/><br/>

## DIP를 지킬 수 있는 해결 방법

공연으로 생각하자면, ‘공연 기획자’가 나와야 된다.

```
- AppConfig - 
애플리케이션의 전체 동작 방식을 설정하기 위한 것.
구현 객체를 생성하고, 연결하는 책임을 가지는 별도의 설정 클래스이다.
```

<br/>

`AppConfig.class` 코드로 보면 이렇다.


```java
public class AppConfig {

    public MemberService memberService() {
        return new MemberServiceImpl(new MemoryMemberRepository());
    }

    public OrderService orderService() {
        return new OrderServiceImpl(new MemoryMemberRepository(), new FixDiscountPolicy());
    }
}
```

<br/><br/>

### `MemberServiceImpl.class` 변경 전.

![이미지](/programming/img/입문12.PNG)

<br/>

### `MemberServiceImpl.class` 변경 후. (생성자 주입이라고 부른다 =인젝션)

![이미지](/programming/img/입문13.PNG)

<br/><br/>

## `OrderServiceImpl.class` 로 자세한 설명을 하겠다. 

```java
public class AppConfig {

    // ...

    public OrderService orderService() {
        return new OrderServiceImpl(new MemoryMemberRepository(), new FixDiscountPolicy());
    }
}
```

핵심은 `OrderServiceImpl.class` 는 구현체에 대해서 전혀 모른다는 것이다!

<br/>



### `OrderServiceImpl.class` 코드 수정 (생성자 주입이라고 부른다 , 영어로 인젝션)

![이미지](/programming/img/입문14.PNG)


<br/>


AppConfig 클래스에서 `OrderServiceImpl` 클래스로 뭐가 넘어가는가?

`new MemoryMemberRepository()`, `new FixDiscountPolicy()` 가 넘어가게 되어 각각의 인터페이스에 할당 되는 것이다.

<br/>

다시 말해, `OrderServiceImpl.class` 코드 어디를 봐도 구현체는 안보인다!

그리하여 DIP를 지킬 수 있게 된 것이다. (철저하게 인터페이스에만 의존했다.)



<br/>

이런 과정을 통해 `관심사`를 분리 했다.

- 객체를 생성하고 연결하는 `역할`과 실행하는 `역할`이 명확히 분리되었다.

- 객체 생성의 `역할`은 AppConfig 에서만, 로직이 돌아가는 `역할`은 클래스에서만 고민하면 된다.


<br/><br/>

## DI (=Dependency Injection =의존관계 주입)

스프링의 DI를 사용하면 ‘기존 코드를 전혀 손대지 않고, 설정만으로 구현 클래스를 변경’ 할 수 있다.

- 뭔가 밖에서 넣어주는 기분이 들것이다.  `“누가?”` 스프링이 넣어 주는 것.)


<br/><br/>

>**Reference** <br/>[스프링 핵심 원리 - 기본편](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8?utm_source=google&utm_medium=cpc&utm_campaign=04.general_backend&utm_content=spring&utm_term=%EC%8A%A4%ED%94%84%EB%A7%81%20%EC%9E%85%EB%AC%B8&gclid=CjwKCAiAjPyfBhBMEiwAB2CCImohok2YrQ2tRdhqfr3cZvKqkIJOHUJ36u6s1-7C9X1gzZIapTvOtxoCangQAvD_BwE)

<br/>

## 궁금증!

```java
다섯 개가 따로 있는 원칙일까
```

읽다 보면 결국 같은 말을 다르게 하는 것처럼 느껴진다.

<br/>

```java
SRP - 이유가 다르면 나눠라
OCP - 새 기능은 추가로 넣고 기존 것은 안 건드려라
LSP - 자식이 부모 자리에 들어가도 말이 되게 해라
ISP - 안 쓰는 메서드를 억지로 구현하게 하지 마라
DIP - 구체적인 것 말고 추상적인 것에 기대라
```

<br/>

`OCP` 를 지키려면 `DIP` 가 필요하고, `DIP` 를 지키려면 인터페이스를 잘 나눠야 하니 `ISP` 가 따라온다.

<br/>

전부 `바뀌는 것과 안 바뀌는 것을 갈라놓아라` 는 한 문장으로 모인다.

<br/>

## LSP 가 제일 헷갈린다

정사각형과 직사각형 예가 유명하다.

```java
class Rectangle {
    void setWidth(int w) { this.width = w; }
    void setHeight(int h) { this.height = h; }
}

class Square extends Rectangle {
    void setWidth(int w) { this.width = w; this.height = w; }    // 같이 바꿔야 정사각형
    void setHeight(int h) { this.width = h; this.height = h; }
}
```

<br/>

수학적으로는 정사각형이 직사각형의 일종이니 상속이 맞아 보인다.

<br/>

그런데 이런 코드가 깨진다.

```java
void test(Rectangle r) {
    r.setWidth(5);
    r.setHeight(4);
    assert r.area() == 20;      // Square 를 넣으면 16 이 나온다
}
```

<br/>

`Rectangle` 자리에 `Square` 를 넣으면 결과가 달라진다. `LSP` 위반이다.

<br/>

`Square` 가 잘못된 게 아니라, `가로와 세로를 따로 바꿀 수 있다` 는 `Rectangle` 의 약속을 못 지키는 것이다.

<br/>

앞의 상속, 오버라이딩 글에서 본 대로 상속은 `is-a` 관계라고 배우는데,

실제 기준은 `말이 되는가` 가 아니라 `약속을 지킬 수 있는가` 인 것이다.

<br/>

## 자바 표준 라이브러리에도 위반 사례가 있다

```java
List<String> list = List.of("a", "b");
list.add("c");
```

```java
UnsupportedOperationException
```

<br/>

`List` 인터페이스에는 `add` 가 있는데 이 구현은 못 한다.

`List` 자리에 넣었더니 터지는 것이니 `LSP` 위반이다.

<br/>

앞의 방어적 복사와 unmodifiableList 를 다룬 글에서 본 그 예외다.

```java
Collections.unmodifiableList(list).add("x")   ->  UnsupportedOperationException
```

<br/>

`Set` 도 마찬가지다.

<br/>

원칙을 지키려면 `ImmutableList` 를 따로 두고 `add` 를 아예 없애야 하는데,

자바는 하위 호환 때문에 그렇게 못 한 것이다.

<br/>

## 원칙을 다 지키려다 더 나빠지는 경우

인터페이스를 잘게 쪼개면 `ISP` 는 지켜지는데 파일이 폭발한다.

```java
Readable, Writable, Deletable, Searchable, Countable, ...
```

<br/>

구현체 하나가 인터페이스 여섯 개를 구현하게 되면 오히려 읽기 어려워진다.

<br/>

앞의 Java에서의 역할과 구현 글에서 본 판단 기준이 여기서도 통한다.

```java
정말 따로 쓰이는가
따로 바뀌는가
```

<br/>

아니라면 굳이 안 나눠도 된다.

원칙은 문제가 생겼을 때 꺼내는 도구지, 처음부터 다 적용하는 규칙이 아니다.

<br/>

## 스프링이 OCP 와 DIP 를 대신 해준다

원칙만으로는 이 문제가 안 풀린다.

```java
public class OrderService {
    private DiscountPolicy policy = new FixDiscountPolicy();   // 구체 클래스를 안다
}
```

<br/>

`DIP` 를 지키려고 인터페이스를 썼는데, `new` 하는 순간 구체 클래스를 알게 된다.

<br/>

누군가 밖에서 넣어줘야 풀린다.

```java
public OrderService(DiscountPolicy policy) {
    this.policy = policy;
}
```

<br/>

그 `누군가` 를 대신 해주는 것이 스프링 컨테이너다.

<br/>

앞의 IoC (=제어의 역전) 글에서 본 그 뒤집힘이

`SOLID 를 지키려다 보니 필요해진 것` 이라고 보면 순서가 맞다.

```java
원칙을 지키려면 -> 밖에서 넣어줘야 하고
밖에서 넣어주려면 -> 그걸 관리할 무언가가 필요하고
그것이 -> DI 컨테이너
```
