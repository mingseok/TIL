## equals, hashCode 메서드는 왜 사용하나요?

<br/>

먼저 equals는 `equals`는 같은 타입의 두 참조 변수가 같은지 비교할 때 사용합니다

-  equals는 동등성의 개념입니다

```
동등성: 변수가 참조하고 있는 객체의 주소가 서로 다르더라도 내용만 같으면 두 변수는 동등하다고 말 할 수 있다.
```

<br/>

## 동일성이란?

말 그대로 객체 A와 객체 B가 완전히 같은 하나의 객체라는 것입니다.

즉, 메모리에 저장된 주소 공간이 완전히 같을 경우, `"객체 A와 B는 동일하다"` 라고 표현 합니다

![이미지](/programming/img/입문393.PNG)

```java
참고: Primitive 타입은 객체가 아니라 주소가 없으므로 '==' 연산자를 사용하였을 때 내용이 같으면 동일하다고 말한다.
```

<br/>

## 코드로 확인해보기

```java
class Main {
  public static void main(String[] args) {
    String person1 = new String("seok");
    String person2 = new String("seok");

    System.out.println(person1 == person2); //false
    System.out.println(person1.equals(person2)); //true
  }
}
```

위 코드 처럼 `new String`을 통해 참조 타입으로 변수를 생성한 경우, 참조 타입은 주소 공간을 저장하므로, 

주소 공간이 다른, `person1`과 `person2`는 동일성이 보장되지 않고 동등성만 보장 된다.

<br/>

### equals를 구현하지 않을 경우

```java
public static void main(String[] args) {
    Car car1 = new Car(1,"avante");
    Car car2 = new Car(1,"avante");
    System.out.println(car1.equals(car2)); //false
}
```

<br/>

## hashcode() & 필요한 이유는?

`해시코드`는 해시 테이블과 같은 자료 구조에서 객체를 빠르게 검색하기 위해 사용되며, 동등성 보장 실패에 대한 이유도 있습니다

`equals`만 잘 재정의 하면 모든 객체의 동등성이 보장될 것 같지만, 모든 객체가 그렇지는 않다는 것입니다

<br/>

이, 예외는 `Hash` 값을 사용하는 `Hash Collection`들인 자료구조`(HashMap, HashSet, HashTable)` 때문에 일어납니다.

```java
public static void main(String[] args) {
    Set<Car> cars = new HashSet<>();
    cars.add(new Car(1, "sonata"));
    cars.add(new Car(1, "sonata"));

    System.out.println(cars.size()); // 2 -> HashSet 사이즈가 1이 아닌 2임 
}
```



중복을 불허 하는 `Set`을 사용하여 완전히 상태가 같은 객체를 두 번 `set`에 집어넣었다. 

<br/>

이론 상으로는 중복이기에 `Hashset`의 길이가 1이어야 하지만 프로그램을 돌리면 길이가 2가 나온다. 

```java
동등성 보장에 실패한 것이다.
```

<br/>

## equals, hashCode 동작 원리

![이미지](/programming/img/입문394.PNG)

출처 : https://tecoble.techcourse.co.kr/post/2020-07-29-equals-and-hashCode/

<br/>

즉, `hashcode`값이 서로 같아야만 `equals` 메소드로 객체 비교를 수행하는 것이다. 

`Hashcode`가 다르면 동등성 비교는 쳐다 보지도 않는다는 말입니다.

<br/>

## 정리

`equals` 메서드를 재정의한 경우, `hashCode` 메서드도 재정의하여 일관성 있게 동작하도록 해야 합니다. 

이렇게 하지 않으면 같은 내용을 가진 객체라도 서로 다른 해시 코드를 반환할 수 있어, 해시 테이블 등에서 문제가 발생할 수 있습니다.

<br/>

## 궁금증!

```java
그러면 hashCode 만 재정의하면 어떻게 될까? 셋 중에 어느 조합이 통할까?
```

세 가지를 다 만들어서 같은 값을 두 번 넣어봤다.

```java
// equals 만 재정의
Set<OnlyEquals> set1 = new HashSet<>();
set1.add(new OnlyEquals(1, "sonata"));
set1.add(new OnlyEquals(1, "sonata"));

// 둘 다 재정의
Set<Both> set2 = new HashSet<>();
set2.add(new Both(1, "sonata"));
set2.add(new Both(1, "sonata"));

// hashCode 만 재정의
Set<OnlyHash> set3 = new HashSet<>();
set3.add(new OnlyHash(1, "sonata"));
set3.add(new OnlyHash(1, "sonata"));
```

<br/>

### 결과

```java
--- equals 만 재정의 ---
HashSet 크기 = 2
equals 로는  = true

--- 둘 다 재정의 ---
HashSet 크기 = 1

--- hashCode 만 재정의하면 ---
HashSet 크기 = 2
```

<br/>

둘 다 재정의했을 때만 `1` 이 나온다. 하나만 해서는 안 되는 것이다.

<br/>

이유는 `HashSet` 이 중복을 찾는 순서에 있다.

```java
1. hashCode() 로 어느 칸(버킷)에 넣을지 정한다
2. 그 칸에 이미 뭔가 있으면, 그때 equals() 로 같은 것인지 확인한다
```

<br/>

- `equals` 만 재정의 : `hashCode` 가 서로 달라서 다른 칸으로 간다. `equals` 는 불릴 기회조차 없다

- `hashCode` 만 재정의 : 같은 칸으로는 갔는데, `equals` 가 주소를 비교하니 다르다고 나온다

원문의 `hashcode 값이 서로 같아야만 equals 로 비교한다` 는 말이 이 순서를 가리킨다.

<br/>

## 넣고 나서 필드를 바꾸면

```java
Map<Both, String> map = new HashMap<>();
Both key = new Both(1, "sonata");
map.put(key, "내 차");

System.out.println("바꾸기 전 = " + map.get(key));
key.id = 99;                                      // 키를 바꿔버린다
System.out.println("바꾼 뒤   = " + map.get(key));
System.out.println("map 크기  = " + map.size());
```

<br/>

### 결과

```java
바꾸기 전 = 내 차
바꾼 뒤   = null
map 크기  = 1
```

<br/>

들어 있는데 못 찾는다. 심지어 `key` 라는 같은 변수로 찾았는데도 그렇다.

`id` 를 바꾸면서 `hashCode` 가 달라졌고, 엉뚱한 칸을 뒤지게 된 것이다.

<br/>

이 값은 이제 영영 못 꺼낸다. 지울 수도 없어서 메모리에 그대로 남는다.

```java
Map 이나 Set 의 키로 쓸 객체는 넣은 뒤에 바뀌면 안 된다
-> hashCode 에 쓰는 필드는 final 이어야 안전하다
```

`String` 이 불변이라서 키로 쓰기 좋다는 얘기가 여기서 다시 나온다.

<br/>

## equals가 지켜야 하는 규약

`Object` 문서에 다섯 가지가 적혀 있다.

```java
반사성 : x.equals(x) 는 항상 true
대칭성 : x.equals(y) 가 true 면 y.equals(x) 도 true
추이성 : x.equals(y) 이고 y.equals(z) 면 x.equals(z)
일관성 : 값이 안 바뀌었으면 몇 번을 불러도 결과가 같아야 한다
null   : x.equals(null) 은 항상 false
```

<br/>

당연한 말 같지만, 상속이 끼면 `대칭성` 이 쉽게 깨진다.

```java
class Point {
    int x, y;
    public boolean equals(Object o) {
        return o instanceof Point p && x == p.x && y == p.y;
    }
}

class ColorPoint extends Point {
    Color color;
    public boolean equals(Object o) {
        return o instanceof ColorPoint cp && super.equals(cp) && color == cp.color;
    }
}
```

<br/>

```java
Point p = new Point(1, 2);
ColorPoint cp = new ColorPoint(1, 2, RED);

p.equals(cp);    // true  (Point 입장에서는 좌표가 같다)
cp.equals(p);    // false (ColorPoint 입장에서는 색이 없다)
```

<br/>

방향에 따라 답이 달라진다. 이러면 `List.contains()` 같은 것이 순서에 따라 다르게 동작한다.

이 문제 때문에 `equals` 를 재정의할 클래스는 상속하지 말라는 얘기가 나온다.

상속이 필요하면 상속 대신 필드로 품는 쪽이 안전하다.

<br/>

## 실무에서는 이렇게 만든다

직접 짜기보다 `Objects` 의 도우미를 쓰는 것이 낫다.

```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;                    // 같은 객체면 바로 true
    if (!(o instanceof Car other)) return false;   // 타입 확인 + null 확인 동시에
    return id == other.id && Objects.equals(name, other.name);
}

@Override
public int hashCode() {
    return Objects.hash(id, name);
}
```

<br/>

`Objects.equals` 는 `null` 을 알아서 처리해준다. `name` 이 `null` 이어도 안 터진다.

`instanceof` 패턴 매칭을 쓰면 `null` 검사와 형변환이 한 줄로 끝난다.

<br/>

## JPA 엔티티는 좀 다르다

엔티티에 `@EqualsAndHashCode` 를 그냥 붙이면 문제가 생긴다.

<br/>

모든 필드를 다 쓰게 되는데, 지연 로딩 필드가 섞여 있으면 프록시를 건드려서 쿼리가 나간다.

그리고 저장 전에는 `id` 가 `null` 이라가 저장 후에 값이 생기니, `hashCode` 가 도중에 바뀌어버린다.

<br/>

그래서 엔티티는 `id` 만 쓰거나, 비즈니스적으로 유일한 값만 쓴다.

```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Member other)) return false;
    return id != null && id.equals(other.id);   // id 가 있을 때만 같다고 본다
}

@Override
public int hashCode() {
    return getClass().hashCode();               // 저장 전후로 안 바뀌게 고정
}
```

`hashCode` 를 고정값으로 두면 해시 성능은 떨어지지만, 바뀌어서 못 찾는 것보다는 낫다는 판단이다.
