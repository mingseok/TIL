## 방어적 복사, Unmodifiable

핵심은 **객체 내부의 값을 외부로부터 보호하는 것** 이라는 것을 유념하자.

<br/>

## 방어적 복사

생성자의 인자로 객체를 받았을 때, 외부에서 넘겨줬던 객체를 변경해도 내부의 객체는 변하지 않아야 한다. 

따라서 `방어적 복사가 적절`하다.

```java
import java.util.ArrayList;
import java.util.List;

public class Names {
    private final List<Name> names;

    public Names(List<Name> names) {
        this.names = new ArrayList<>(names);
    }

    public List<Name> getNames() {
        return new ArrayList<>(names); // 방어적 복사를 이용하여 복사본을 반환한다.
    }
}
```

<br/><br/>

## Unmodifiable Collection

getter를 통해 객체를 리턴할 때, `Unmodifiable Collection`을 이용한 값을 반환하는 것도 좋다.

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Names {
    private final List<Name> names;

    public Names(List<Name> names) {
        this.names = new ArrayList<>(names);
    }

    public List<Name> getNames() {
        // Collections.unmodifiableList 메서드를 이용하여 반환한다.
        return Collections.unmodifiableList(names); 
    }
}
```
<br/>

## 궁금증!

```java
방어적 복사와 unmodifiable 중 무엇을 써야 할까? 둘 다 하면 되나?
```

넷을 다 만들어놓고 돌려봤다.

<br/>

### 1) 아무것도 안 하면

```java
class Naive {
    private final List<String> names;
    Naive(List<String> names) { this.names = names; }        // 그대로 받는다
    List<String> getNames() { return names; }                // 그대로 준다
}
```

```java
List<String> outside = new ArrayList<>(List.of("민석"));
Naive naive = new Naive(outside);
outside.add("영한");                    // 밖에서 건드린다
naive.getNames().add("몰래추가");        // 받아서 건드린다
```

```java
내부 상태 = [민석, 영한, 몰래추가]
```

<br/>

`final` 을 붙였는데도 두 번이나 바뀌었다.

앞의 불변 객체 글에서 본 그 문제다. `final` 은 주소만 지키지 안쪽은 못 지킨다.

<br/>

### 2) 방어적 복사

```java
Copy(List<String> names) { this.names = new ArrayList<>(names); }
List<String> getNames() { return new ArrayList<>(names); }
```

```java
내부 상태 = [민석]
```

<br/>

밖에서 뭘 하든 내부는 안 바뀐다.

<br/>

### 3) unmodifiableList

```java
List<String> getNames() { return Collections.unmodifiableList(names); }
```

```java
추가 시도 -> UnsupportedOperationException
내부 상태 = [민석]
```

<br/>

여기까지는 둘 다 목적을 달성한 것처럼 보인다.

<br/>

## 그런데 4번에서 차이가 드러난다

`unmodifiableList` 는 복사본이 아니라 `창` 이다.

```java
List<String> view = unmod.getNames();
System.out.println("받아둔 목록 = " + view);

unmod.addInternally("나중에추가");        // 클래스 자신이 내부 리스트를 바꾼다
System.out.println("다시 보면    = " + view);
```

```java
받아둔 목록 = [민석]
다시 보면    = [민석, 나중에추가]   <- 내가 받아둔 게 바뀌었다
```

<br/>

받는 쪽 입장에서는 아무것도 안 했는데 목록이 달라졌다.

<br/>

`unmodifiableList` 는 원본을 감싼 껍데기라, 원본이 바뀌면 그대로 비친다.

`읽기 전용` 이지 `변하지 않음` 이 아닌 것이다.

```java
방어적 복사 - 그 순간의 사진. 원본이 바뀌어도 내 것은 그대로
unmodifiable - 원본을 보는 창. 내가 못 바꿀 뿐 원본이 바뀌면 같이 바뀐다
```

<br/>

## 5번은 둘 다 못 막는 문제다

```java
List<StringBuilder> boxes = new ArrayList<>(List.of(new StringBuilder("원본")));
List<StringBuilder> copied = new ArrayList<>(boxes);

boxes.get(0).append("+변경");
System.out.println(copied.get(0));
```

```java
복사본이 가리키는 값 = 원본+변경   <- 안쪽까지는 복사가 안 됐다
```

<br/>

리스트는 새로 만들었지만 그 안에 든 객체는 원본과 같은 것을 가리킨다.

이것을 `얕은 복사` 라고 한다.

<br/>

그래서 담기는 원소 자체가 불변이어야 진짜로 안전하다.

앞의 String 객체가 불변인 이유 글에서 본 그 성질이다.

```java
List<String>   - String 이 불변이라 안전하다
List<Name>     - Name 이 불변이면 안전하다
List<StringBuilder> - 가변이라 안 안전하다
```

<br/>

## 그래서 정리하면

```java
생성자에서 - 방어적 복사가 맞다 (밖의 참조를 끊어야 한다)
getter 에서 - 상황에 따라 다르다
   방어적 복사   : 안전하다. 대신 부를 때마다 새 리스트를 만든다
   unmodifiable : 가볍다. 대신 내부가 바뀌면 같이 바뀐다
담기는 원소   - 불변으로 만들어두는 것이 근본 해결이다
```

<br/>

내부 리스트가 만들어진 뒤로 절대 안 바뀐다면 `unmodifiable` 이 낫다.

복사 비용이 없고, 밖에서 고치려 하면 예외로 알려주니 실수도 빨리 드러난다.

<br/>

`Java 10` 부터는 `List.copyOf()` 로 둘을 합칠 수 있다.

```java
List<String> getNames() { return List.copyOf(names); }
```

<br/>

복사도 하고 수정도 막는다. 앞의 불변 객체 글에서 쓴 것이 이것이다.

다만 `null` 원소를 허용하지 않으니 그 점만 확인하면 된다.
