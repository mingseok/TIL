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
