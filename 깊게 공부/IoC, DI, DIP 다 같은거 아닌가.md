## IoC, DI, DIP 다 같은거 아닌가?



이번 기회로 알게 되었다.

IoC(Inversion of Control), DIP, DI 는 모두 다른 개념이다.

<br/><br/>

## IoC (=제어의 역전, =DI 컨테이너)

`제어권`이 사용자에게 있지 않고, `프레임워크`에 있습니다.

스프링에서는 인스턴스의 `생성`부터 `소멸`까지
개발자가 아닌 컨테이너에서 대신 `관리`하게 된다.

<br/>

`IoC`란 코드의 흐름을 제어하는 주체가 바뀌는 것이다.

- 객체를 생성하는 것

- 객체의 생명주기를 관리하는 것 등.

<br/>

```java
"내가 뭔가를 호출하는 것이 아닌, 프레임워크 같은것이 대신 호출해 주는 것이다"
```

`IoC`를 적용한다는 것은 이러한 흐름 제어를 또 다른 제 3자가 수행한다는 것을 의미한다.

<br/>

### 기준은 뭘까?

라이브러리는 내 코드가 `라이브러리`를 이용한다. → 즉, 제어권이 내 코드에 있다. 

반면, 프레임워크는 프레임워크가 나의 코드를 실행한다. 

- 즉, 제어권은 프레임워크에 있다.

<br/><br/>

## DIP (=의존관계 역전 법칙)

`SOLID` 원칙 중 하나이다. 

`DIP`가 주장하는 바의 핵심은 `추상화`에 의존하라는 것이다

```java
프로그래머는 "추상화에 의존해야지, 구체화에 의존하면 안된다."
```

클라이언트 코드가 `구현 클래스`를 보지 말고, `인터페이스`만 바라보라는 뜻이다.

<br/>

### 예시)

운전자는 자동차의 `‘역할’`에 대해서만 알아야지,

아반떼에 대해서만, 디테일하게 알고 있다면 잘못된 것이다.

`역할`과 `구현`을 철저하게 분리하도록 시스템을 설계해야 하는 것이다.

- 다시 말해, 시스템도 언제든지 갈아 끼울 수 있도록 말이다.

<br/><br/>

## DI (=의존성 주입)

`DI`는 필요로 하는 객체를 스스로 생성하는 것이 아닌, 

외부로 부터 주입받는 기법을 의미한다. 

```java
마틴 파울러의 글에 따르면 3가지 타입으로 정의할 수 있다.
-> 그 중 "Constructor Injection" 방식을 스프링에서도 권장한다.
```

<br/>

### 장점

객체간의 의존관계를 미리 설정해두면,
스프링 컨테이너가 의존관계를 자동으로 연결해준다.

이렇게 되면, 직접 의존하는 객체를 `생성`하거나 `검색`해서 
가져올 필요 없기에, `결합도`가 낮아지는 장점이 있다.

<br/>

### `DI`는 `IoC`개념이 적용된 결과물 중 하나이다.

의존성을 주입한다는 것을 `IoC` 적인 행위로 바라볼 수 는 있지만, 

`IoC`가 곧 의존성 주입이라고 보기는 어렵기 때문이다.

<br/>

### 스프링 DI

‘기존 코드를 전혀 손대지 않고, 설정만으로 구현 클래스를 변경’ 할 수 있다.

- 뭔가 밖에서 넣어주는 기분이 들것이다. → `“누가?”` 스프링이 넣어 주는 것.
<br/>

## 궁금증!

```java
세 개가 다르다는 건 알겠는데, 코드에서 어느 줄이 무엇에 해당할까?
```

한 코드를 단계별로 고쳐가면서 어느 지점에서 무엇이 생기는지 보면 확실해진다.

<br/>

### 0단계) 아무것도 없는 코드

```java
class OrderService {
    private final TossPayClient payClient = new TossPayClient();   // 직접 만든다

    void order(int amount) {
        payClient.pay(amount);
    }
}
```

<br/>

여기에는 셋 다 없다.

- `OrderService` 가 `TossPayClient` 라는 구체 클래스를 직접 안다 -> DIP 위반

- `OrderService` 가 직접 `new` 한다 -> 제어권이 자기에게 있다
- 밖에서 넣어주는 것이 없다 -> 주입이 없다

<br/>

### 1단계) 인터페이스를 끼운다 = DIP

```java
interface PayClient {
    void pay(int amount);
}

class OrderService {
    private final PayClient payClient = new TossPayClient();   // 타입만 인터페이스로
}
```

<br/>

이제 `payClient` 라는 필드는 `PayClient` 라는 추상에 의존한다.

`추상화에 의존하라` 는 DIP를 절반 지킨 것이다.

<br/>

절반이라고 한 이유는, `new TossPayClient()` 라고 적힌 순간 여전히 구체 클래스를 알기 때문이다.

이걸 마저 없애려면 만드는 일을 밖으로 내보내야 한다.

<br/>

### 2단계) 밖에서 받는다 = DI

```java
class OrderService {
    private final PayClient payClient;

    OrderService(PayClient payClient) {     // 밖에서 받는다
        this.payClient = payClient;
    }
}
```

<br/>

`new` 가 사라졌다. 이제 `OrderService` 는 `TossPayClient` 라는 이름을 아예 모른다.

`PayClient` 를 구현한 무언가가 들어온다는 것만 안다.

<br/>

이 순간 DIP도 완전해진다.

```java
DIP -> "무엇에 의존할 것인가" 의 문제. 인터페이스에 의존하라
DI  -> "그 인터페이스의 구현체를 누가 넣어줄 것인가" 의 문제. 밖에서 넣어라
```

<br/>

DIP는 원칙이고 DI는 그 원칙을 지키는 방법 중 하나다.

DI 없이도 DIP를 지킬 수는 있지만(팩토리 메서드 등), DI가 제일 자연스럽다.

<br/>

### 3단계) 그 밖이 누구인가 = IoC

```java
OrderService service = new OrderService(new TossPayClient());   // main 에서 내가 조립
```

<br/>

여기까지는 아직 IoC가 아니다. 조립하는 코드가 여전히 내 손에 있기 때문이다.

<br/>

```java
@Service
class OrderService {
    private final PayClient payClient;

    OrderService(PayClient payClient) { ... }   // 스프링이 알아서 넣어준다
}
```

<br/>

이제 조립하는 코드가 어디에도 없다. 스프링이 대신 한다.

`언제 만들지, 무엇을 넣을지, 언제 없앨지` 를 전부 컨테이너가 정한다.

```java
IoC -> "흐름을 누가 쥐고 있는가" 의 문제. 내가 아니라 프레임워크가 쥔다
```

<br/>

## 정리하면

```java
DIP -> 원칙 : 구체가 아니라 추상에 의존해라
DI  -> 방법 : 의존 대상을 밖에서 넣어줘라
IoC -> 결과 : 그 "밖" 을 프레임워크가 맡으면서 흐름의 주도권이 넘어간다
```

<br/>

세 개가 자주 같이 나오는 이유는 순서대로 이어져 있기 때문이다.

DIP를 지키려다 보니 DI가 나오고, DI를 자동화하려다 보니 IoC 컨테이너가 나온다.

<br/>

## 라이브러리와 프레임워크의 차이도 여기서 나온다

원문의 기준을 코드로 보면 이렇다.

```java
// 라이브러리 : 내가 부른다
String json = objectMapper.writeValueAsString(member);

// 프레임워크 : 프레임워크가 나를 부른다
@GetMapping("/members")
public List<Member> list() { ... }        // 내가 부르는 코드가 어디에도 없다
```

<br/>

`list()` 를 부르는 코드를 우리가 쓴 적이 없다.

앞의 프론트 컨트롤러 글에서 본 `DispatcherServlet` 이 URL을 보고 대신 불러준다.

<br/>

이걸 `헐리우드 원칙` 이라고 부르기도 한다.

`먼저 연락하지 마세요, 저희가 연락드리겠습니다` 라는 뜻이다.

```java
main 메서드부터 아래로 흐르던 제어가
-> 컨테이너가 위에서 내 코드를 부르는 방향으로 뒤집힌다
```

이 뒤집힘이 `역전(Inversion)` 이라는 말의 정확한 의미다.
