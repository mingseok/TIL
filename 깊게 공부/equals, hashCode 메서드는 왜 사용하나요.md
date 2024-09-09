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
