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