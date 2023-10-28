## static primitive, static reference 저장

<br/>

## metaspace영역

java8부터 생긴 영역이다.

`Metaspace`는 클래스 메타데이터 및 클래스 정보를 저장하는 영역으로, 

클래스로더가 클래스를 로드하고 메타데이터를 저장하는 곳이다.

<br/><br/>

## java8부터는 static을 heap영역에서 관리한다

여기서, 오해 하면 안되는 것은 → `static object`를 말한다.

- `static`객체는 `heap`영역에 저장

- 하지만`reference`는 여전히 `metaspace`에서 관리된다.
- 그렇기 때문에 참조를 잃은 `static object`는 `GC`의 대상이 될 수 있다.

```java
static int i = 1;

static void a() { 
}

static method static A a = new A(); // reference 형식의 static field
```