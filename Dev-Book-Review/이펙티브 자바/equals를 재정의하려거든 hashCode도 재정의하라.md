## equals를 재정의하려거든 hashCode도 재정의하라

<br/>

## 전체적인 나의 한줄평(?) 정리

equals를 재정의한 클래스 모두에서 hashCode도 재정의해야 한다. 

그렇지 않으면 프로그램이 제대로 동작하지 않을 것이다. 

재정의한 hashCode는 Object의 API 문서에 기술된 일반 규약을 따라야 하며, 

서로 다른 인스턴스라면 되도록 해시코드로 서로 다르게 구현해야 한다. 

<br/>

## 다시 처음부터 시작하자면,,

`equals`를 재정의한 클래스는 `hashCode`도 재정의 해야 한다.

그렇지 않으면 인스턴스를 `HashMap`이나 `HashSet` 같은 컬렉션의 원소로 사용할 때 문제가 발생한다고 합니다.

다시 말해, `equals`와 함께 `hashCode`도 재정의 하지 않으면 `HashMap`이나 `HashSet`의 원소로 사용할 때 일관성이 무너지는 문제점 입니다.

<br/><br/>

## 설명

자바 최고 조상 Object 에는 equals 메서드 뿐만 아니라, hashCode 메서드도 기본적으로 정의되어 있습니다. 

HashMap 이나 HashSet 같은 Collection 에서 Object 의 hashCode 메서드를 활용하기 때문입니다.

그렇기에 아이템 10에서 Object 를 재정의 하였다면 hashCode 도 함께 재정의해야 한다 라고 



먼저 설명하는 이유를 알 것 같았습니다.


<br/>

```java
그리고 Object 명세서에서 정의되어 있는 HashCode 가 지켜야 하는 규약들이 있습니다. 
```


<br/>

### 이건 책에 있는 내용이지만 중요하다고 생각하여 가져왔습니다.

- "equals 비교에 사용되는 정보가 변경되지 않았다면, hashCode 는 몇 번을 호출해도 일관되게
    
    항상 같은 값을 반환해야 한다."
    
- "equals 가 두 객체를 같다고 판단했다면, 두 객체의 hashCode 는 동일한 값을 반환해야 한다."
- "equals 가 두 객체를 다르다고 판단했더라도, 두 객체의 hashCode 는 서로 다른 값을 반환 할
    
    필요는 없다. 다만, 다른 값을 반환해야 해시테이블의 성능이 좋아진다."
    

<br/>

### 이 코드는 HashMap의 조회 함수 입니다. 

```java
public V get(Object key) {
        Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
 }

static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}

final Node<K,V> getNode(int hash, Object key) {
        Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
        if ((tab = table) != null && (n = tab.length) > 0 &&
            (first = tab[(n - 1) & hash]) != null) { 
            if (first.hash == hash && // 찾은 hash값이 맞는지 비교하고 맞다면 equals를 통해 값을 준다.
                ((k = first.key) == key || (key != null && key.equals(k))))
                return first;
            if ((e = first.next) != null) { // 같은 해쉬값이고 equals의 결과가 다른경우 다음노드를 조회
                if (first instanceof TreeNode)
                    return ((TreeNode<K,V>)first).getTreeNode(hash, key);
                do {
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
    }
```



HashMap은 들어온 키 객체를 hash값으로 바꾸고 이걸 **주소값으로 쓰는 값**을 찾아 hash값과

키값을 equals로 비교하여 다 통과하면 해당 노드를 반환 합니다.

<br/><br/>

## 상황을 만들어 봅시다!

PhoneNumber라는 객체가 equals만 재정의 되어있고 hashCode가 재정의 안 되어있다고 생각합니다 

그리고 이 맵에서 우린 전화번호가 같으면 같은 사람을 뽑고 싶은 상황인 것입니다.

```java
Map<PhoneNumber,Person> map = new HashMap();
map.put(new  PhoneNumber(010-3333), new Person("y"));
map.put(new  PhoneNumber(010-3333), new Person("z"));
```

하지만 실제 조회해 보면 같은 전화번호인데 서로 **다른 두명이 조회** 될 것입니다.

<br/>

## 이유가 뭐야?

이유는. 해쉬코드를 **재정의 안해서 입니다.**

위 **16번째 줄**을 보면 table에서 객체를 찾을 때 **hash값이 다르기** 때문에 애초에 같은 



PhoneNumber의 다른 해쉬값을 주소로 해서 Person객체를 각각 저장했을 것입니다

<br/>

```java
그리하여 해시코드가 다르면 동치성을 비교조차 하지 않는다는 것입니다.
```

<br/><br/>

## 두번째 조건을 만족시키려면 항상 같은 HashCode를 반환하면 되지 않은가?

```java
@Override
public int hashCode() {
     return 42;
}
```

정답은: 안된다 입니다.

항상 같은 hashCode는 성능에 좋지 않다고 알려주고 있습니다.

<br/>

그러면 다시 한번 조회함수를 보러 가보면,

20번째 줄은 hashCode로 찾은 첫번째 노드에 값이 실제로 equals를 만족하지 못해서 다음 노드를 찾는 조건입니다.

<br/>

즉 **해쉬값이 같은데** 실제 값은 다르면 **해당 노드에 다음 노드의 주소**를 저장해놓는 것입니다.

이렇게 될 경우 **항상 같은 해쉬코드를 반환**하도록 하면 그냥 연결리스트를 쓰는 것과 

똑같은 시간복잡도를 갖게 되는 것입니다.

<br/><br/>

## 올바른 해쉬코드를 만드는 법 (69p) 참고.

```java
@Override
public int hashCode() {
    int result = Integer.hashCode(areaCode);
    result = 31 * result + Integer.hashCode(areaCode);
    result = 31 * result + Integer.hashCode(areaCode);
	  return result;
}
```

- 2비트 정수 범위에 균일하게 배치하려고 약간의 연산을 추가한 것 말고는 딱히 어려울 건 없습니다.

    - `31 * result`는 필드를 곱하는 순서에 따라 `result` 값이 달라지게 만들어줍니다.

    - 비슷한 필드가 여러개일 때 효과가 크다는 것입니다.

- 필드의 타입에 따라 구분되는 해시 값을 잘 사용하면 되고, 대부분은 클래스에 이미 구현되어 있습니다.

<br/>

```java
equals()에 사용되지 않은 필드는 '반드시' 제거해야 한다는 것입니다. 안그러면 규약을 어기게 된다
```

<br/><br/>

## 그런데, 왜 하필 31이야?

- 짝수이고 오버플로가 발생한다면 정보를 잃게 된다고 합니다.

    - 여기서 나의 질문) “홀수를 곱해도 똑같은 상황이 발생하는 거 아닌가?”

- 소수여서 31이다.
    - (책에도 소수를 곱해야하는 정확한 이유는 모른다고 하는데 ,,, 그냥 넘어 가야 되나..?)

- 연산을 빠르게 처리할 수 있다. 31 * i는 jvm이 최적화해 (i << 5) - i로 바꿔 쉬프트 연산을 하도록 설계되어 있다고 합니다.

<br/><br/>

## 여기서 주의할 점은

- **equals에 논리적 동치를 확인**하는데 **사용하지 않는 필드**는 빼고 해야한다.
- 만약 hash로 바꾸려는 필드가 기본 타입이 아니면 해당 필드의 hashCode를 불러 구현한다.
- 참조 타입 필드가 null일 경우 0을 사용.
- 31을 곱하는 이유는 비슷한 필드가 여러개일 때 해시효과를 크게 높여주기 위해서다.

- Objects클래스에 있는 hash함수도 있지만 성능이 느리다.
- 인텔리제이의 자동완성 해쉬는 Objects의 해쉬를 사용한다.

```java
@Override
public int hashCode() {
    return Objects.hash(lineNum,prefix,areaCode);
}
```

<br/><br/>

## **해쉬코드를 지연 초기화하자.**

- 자주 해쉬의 키로 사용할 것 같으면 인스턴스가 만들어질 대 해시코드를 계산해 줘야 한다.

- 해쉬의 키로 사용되지 않는 경우이고 해쉬계산에 많은 시간이 걸린다면 지연초기화하면 좋다.

```java
private int hashCode;

@Override
public int hashCode() {
      	int result = hashCode;
        if(result == 0) {
        int result = Integer.hashCode(areaCode);
        result = 31 * result + Integer.hashCode(areaCode);
        result = 31 * result + Integer.hashCode(areaCode);
        hashCode = result;
        }
        return result;
}
```

지연초기화 상황은 스레드 안정성을 고려해야 한다. 

동시에 여러 쓰레드가 hashCode를 호출하면 여러 쓰레드가 동시에 계산하여 

<br/>

우리의 처음 의도와는 다르게 여러번 계산하는 상황이 발생할 수 있다. 

그래서 지연 초기화를 하려면 동기화를 신경써주는 것이 좋다.

<br/><br/>

## 성능을 높인답시고 해시코드를 계산할 때 핵심 필드 생략을 하지말자.

- 핵심필드를 생략하고 적은 수의 필드만 쓰면 당연히 성능은 빨라지겠지만, 해시 품질이 나빠질것이다.
- 당장 `hashCode()` 메서드의 성능은 좋아질 수 있으나, 많은 필드를 사용하는 것은
    
    해시 값을 32 비트 정수로 고루 퍼지게 만드는데 도움을 주기 때문에 무작정 필드를 빼지 말자. 
    
    해시 테이블에서 심각한 비효율이 날 수 있다.


<br/><br/>
    

## 결론

`equals`를 재정의할 때는 `hashCode`도 반드시 재정의해야 한다. 

그렇지 않으면 프로그램이 제대로 동작하지 않을 것이다.

<br/>

재정의한 `hashCode`는 `Object`의 API 문서에 기술된 일반 규약을 따라야 하며, 

서로 다른 인스턴스라면 되도록 해시코드도 서로 다르게 구현해야 한다.

<br/>

AutoValue 프레임워크를 사용하면 `equals`와 `hashCode`를 자동으로 만들어준다.



- `equals()`를 재정의할 때는 반드시 `hashCode()`도 재정의하자.

- `hashCode()`를 정의할 때도 반드시 일반 규약을 따르자.
- 다른 인스턴스의 `hashCode()`는 최대한 32비트 정수 안에서 고르게 나오게 하도록 노력하자.

<br/>

### 사실 요즘은 직접 만들기보다 IDE의 도움을 받아 만드는 것이 좋다.