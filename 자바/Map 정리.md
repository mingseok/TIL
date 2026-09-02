## Map 활용 설명

### 맵을 쓰는 이유는?

만약 → 과정, 레벨, 미션을 선택한다.

```java
백엔드, 레벨1, 자동차경주
```

<br/>

이렇게 맵에 키로 “백엔드, 레벨1, 자동차경주” 저장 되어 있다면

이미 있는 것이므로, 이미 있다고 판단한다.


<br/>

### 만약 없을 경우에는? 




`hashMap` 이기 때문에 없다면 null 을 반환 한다.

조건 검사하여 null 이 나왔다면, 이제 그 값을 저장하면 되는 것이다.

이런 것이 맵을 사용하는 이유.

<br/>

### 조회 할때도 맵을 사용한다.

어떻게? 키를 비교해서 해당 값이 있다면 벨류에 저장 되어 있는 리스트들을 꺼내 주는 것이다.

<br/>

### 또다른 맵들은?

- `LinkedHashMap` - 순서를 유지 해주는 맵이다.
- `hashMap` - null값 허용

<br/><br/>

## `keySet()`은 → 키 전체 가져올 수 있다.

```java
class a {
    public static void main(String[] args) {
        HashMap<Integer, String> hashMap = new HashMap<>();
        hashMap.put(1, "가가");
        hashMap.put(2, "나나");
        hashMap.put(3, "다다");
        hashMap.put(4, "라라");

        System.out.println(hashMap.keySet());
    }
}

출력: [1, 2, 3, 4]
```

<br/><br/>

## `get()` 는 → 키를 이용해 벨류를 출력한다

```java
class a {
    public static void main(String[] args) {
        HashMap<Integer, String> hashMap = new HashMap<>();
        hashMap.put(1, "가가");
        hashMap.put(2, "나나");
        hashMap.put(3, "다다");
        hashMap.put(4, "라라");

        System.out.println(hashMap.get(2)); // 가가
		}
}

출력 : 나나
```

<br/><br/>

## `containsKey(키)` 사용

### `containsValue(벨류)` → 가능하다.

map 안에 ‘키’ 가 있는지 확인 하는 것이다.

```java
class a {
    public static void main(String[] args) {
        HashMap<Integer, String> hashMap = new HashMap<>();
        hashMap.put(1, "가가");
        hashMap.put(2, "나나");
        hashMap.put(3, "다다");
        hashMap.put(4, "라라");

        if (hashMap.containsKey(2)) {
            System.out.println("성공");
        }
    }
}

출력값 : 성공
```

<br/><br/>

## `entrySet()` -> 키, 벨류 다 가져오는 법 (=만능 방법이다)

```java
class a{
    public static void main(String[] args) {
        HashMap<Integer, String> hashMap = new HashMap<>();
        hashMap.put(123, "고고");
        hashMap.put(456, "노노");
        hashMap.put(789, "도도");

        for (Map.Entry<Integer, String> entry : hashMap.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
    }
}

출력: 
789
도도
456
노노
123
고고
```

<br/><br/>

## `replace(key, value)`

```java
class a{
    public static void main(String[] args) {
        HashMap<Integer, String> hashMap = new HashMap<>();
        hashMap.put(123, "고고");
        hashMap.put(456, "노노");
        hashMap.put(789, "도도");

        hashMap.replace(123, "민석");

        for (Map.Entry<Integer, String> entry : hashMap.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
    }
}

출력 :
789
도도
456
노노
123
민석
```

<br/><br/>

## `remove()` → 해당 내용을 삭제 시킨다.

```java
class a{
    public static void main(String[] args) {
        HashMap<Integer, String> hashMap = new HashMap<>();
        hashMap.put(123, "고고");
        hashMap.put(456, "노노");
        hashMap.put(789, "도도");

        hashMap.remove(123);

        for (Map.Entry<Integer, String> entry : hashMap.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
    }
}

출력 :
789
도도
456
노노
```

<br/>

### `size()`

<br/><br/>



## Map에 객체를 넣어서 사용해야 될 경우.

```java
public class run {
    public static void main(String[] args) {
        HashMap<TestClassKey, TestClassValue> map = new HashMap<>();

        map.put(new TestClassKey("1번 키"), new TestClassValue("1번 값"));
        map.put(new TestClassKey("2번 키"), new TestClassValue("2번 값"));
        map.put(new TestClassKey("3번 키"), new TestClassValue("3번 값"));
        map.put(new TestClassKey("4번 키"), new TestClassValue("4번 값"));

        for (Map.Entry<TestClassKey, TestClassValue> entry : map.entrySet()) {
            entry.getKey().printName();
            entry.getValue().printName();
        }
    }
}

출력:
1번 키
1번 값
2번 키
2번 값
3번 키
3번 값
4번 키
4번 값
```

<br/>

### `TestClassKey` 클래스

```java
public class TestClassKey {

    private String name;

    public TestClassKey(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestClassKey that = (TestClassKey) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public void printName() {
        System.out.println(this.name);
    }
}
```

<br/>

### `TestClassValue` 클래스

```java
public class TestClassValue {
    private final String name;

    public TestClassValue(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestClassValue that = (TestClassValue) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public void printName() {
        System.out.println(this.name);
    }
}
```

<br/>

## 궁금증!

```java
getOrDefault 로 꺼내서 담으면 왜 안 들어가나
```

`Map<String, List<String>>` 에 값을 추가하려고 이렇게 쓴 적이 있다.

```java
Map<String, List<String>> m = new HashMap<>();
m.getOrDefault("k", new ArrayList<>()).add("잃어버림");
```

<br/>

### 결과

```java
getOrDefault 후 = {}
```

<br/>

맵이 비어 있다. 아무것도 안 들어갔다.

<br/>

`getOrDefault` 는 기본값을 **돌려주기만** 한다. 맵에 넣지는 않는다.

새로 만든 `ArrayList` 에 값을 넣고, 그 리스트는 그대로 버려진 것이다.

<br/>

## computeIfAbsent 를 써야 한다

```java
m.computeIfAbsent("k", k -> new ArrayList<>()).add("저장됨");
```

<br/>

### 결과

```java
computeIfAbsent 후 = {k=[저장됨]}
```

<br/>

없으면 만들어서 **맵에 넣고** 그것을 돌려준다.

<br/>

이름에 답이 있다.

```java
getOrDefault      - 없으면 이 값을 줘 (맵은 안 건드림)
computeIfAbsent   - 없으면 이걸로 계산해서 넣어 (맵을 바꿈)
putIfAbsent       - 없으면 이 값을 넣어
```

<br/>

## compute 계열이 여러 개다

```java
computeIfAbsent(k, f)   - 없을 때만 만든다
computeIfPresent(k, f)  - 있을 때만 고친다
compute(k, f)           - 있든 없든 부른다
merge(k, v, f)          - 없으면 v, 있으면 f 로 합친다
```

<br/>

개수를 세는 데는 `merge` 가 짧다.

```java
counts.merge(word, 1, Integer::sum);
```

<br/>

```java
if (counts.containsKey(word)) {
    counts.put(word, counts.get(word) + 1);
} else {
    counts.put(word, 1);
}
```

<br/>

이 네 줄이 한 줄이 되는 것이다.

<br/>

## 값이 null 이면 헷갈린다

```java
map.get("k")           // null 이 나왔다
```

<br/>

키가 없는 건지, 값이 `null` 인 건지 구분이 안 된다.

<br/>

```java
map.containsKey("k")   // 이걸로 구분한다
```

<br/>

그래서 `HashMap` 에 `null` 값을 안 넣는 게 낫다.

<br/>

`ConcurrentHashMap` 은 아예 `null` 을 못 넣는다.

```java
NullPointerException
```

<br/>

멀티 스레드에서 `get` 이 `null` 을 돌려줬을 때

`키가 없다` 와 `값이 null 이다` 를 구분하려고 `containsKey` 를 부르면,

그 사이에 다른 스레드가 바꿔버릴 수 있기 때문이다.

<br/>

앞의 멀티 쓰레드 글에서 본 `읽고 -> 확인하고 -> 쓴다` 사이의 틈이다.

애초에 `null` 을 금지해서 그 애매함을 없앤 것이다.

<br/>

## 키로 쓰는 객체는 불변이어야 한다

```java
class Key { int value; }

Map<Key, String> map = new HashMap<>();
Key k = new Key(1);
map.put(k, "값");

k.value = 2;              // 키를 바꿨다
map.get(k);               // null 이 나온다
```

<br/>

앞의 해시(Hash) 글에서 본 대로,

`put` 할 때의 해시값으로 자리를 정해뒀는데 해시가 바뀌어버린 것이다.

<br/>

엉뚱한 자리를 찾으니 못 찾는다.

그렇다고 원래 자리에서 사라진 것도 아니라, 꺼낼 방법이 없어진다.

<br/>

그래서 키로는 `String`, `Integer`, `enum` 처럼 불변인 것을 쓴다.

앞의 lombok 글에서 본 `@EqualsAndHashCode` 를 엔티티에 함부로 쓰면 안 되는 이유도 같다.

<br/>

## 순서가 필요하면 종류를 바꾼다

```java
HashMap        - 순서 없음. 제일 빠르다
LinkedHashMap  - 넣은 순서를 유지한다
TreeMap        - 키 순서로 정렬된다
```

<br/>

`HashMap` 의 순서는 정해져 있지 않다.

지금 실행에서 어떤 순서로 나왔다고 해서 다음에도 그럴 거라는 보장이 없다.

<br/>

`LinkedHashMap` 은 LRU 캐시로도 쓸 수 있다.

```java
new LinkedHashMap<>(16, 0.75f, true) {
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > 100;      // 100개가 넘으면 가장 오래된 것을 버린다
    }
};
```

<br/>

세 번째 인자가 `true` 면 접근 순서로 정렬된다.

꺼내 쓴 것이 뒤로 가니, 앞에 있는 것이 제일 안 쓴 것이 되는 구조다.
