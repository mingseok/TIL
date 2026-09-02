## JVM 힙 내부구조



![이미지](/programming/img/입문395.PNG)

- `Eden` 영역 : 새롭게 생성된 객체들이 할당되는 영역

- `Survival` 영역(S0, S1) : `minor gc`로부터 살아남은 객체들이 존재하는 영역
    - `Survival` 영역의 특별한 규칙 : `Survival 0`, 혹은 `Survival 1` 둘 중 하나는 꼭 비어 있어야 한다는 것이다
        

<br/>

## Heap 영역은

동적으로 생성된 객체가 저장되는 영역으로 GC의 대상이 되는 공간이다

- `new` 연산자로 생성된 모든 `Object`와 `인스턴스 변수`, 그리고 `배열`을 저장한다

### Heap 영역은 두 영역으로 구분할 수 있다

- `Young Generation`
    - 생명 주기가 짧은 객체를 GC 대상으로 하는 영역
    
    - 여기 객체들은 `Eden`이라는 곳에 할당이 된 후에, `Survivor 0`과 `1`을 거쳐,
        
         오래 사용되었다고 판단된 객체들은 `Old Generation`으로 이동하게 되는 것이다
        
        
        
- `Old Generation`
    - 생명 주기가 긴 객체를 GC 대상으로 하는 영역

```java
이렇게 "Method Area"와 "Heap Area"는 여러 스레드들 간에 공유되는 메모리이다
```

<br/>

## 궁금증!

```java
힙을 왜 굳이 둘로 나눠놨을까? 그냥 한 덩어리로 두면 안 되나?
```

`대부분의 객체는 만들어지자마자 금방 죽는다` 는 관찰에서 나온 구조다.

메서드 안에서 잠깐 쓰고 버리는 객체, `for` 문 한 바퀴 돌면서 만든 객체가 압도적으로 많다.

<br/>

객체를 만들고 바로 버리는 코드를 돌려놓고 GC 로그를 켜보면 바로 보인다.

```java
public class Gc {
    public static void main(String[] args) {
        for (int i = 0; i < 300000; i++) {
            byte[] garbage = new byte[1024];   // 만들고 바로 버린다
            if (garbage.length == 0) System.out.println("");
        }
    }
}
```

```java
java -Xmx64m -Xlog:gc Gc
```

<br/>

### 결과

```java
[0.021s][info][gc] GC(0) Pause Young (Normal) 35M->1M(64M) 0.370ms
[0.024s][info][gc] GC(1) Pause Young (Normal) 30M->1M(64M) 0.189ms
[0.028s][info][gc] GC(2) Pause Young (Normal) 37M->1M(64M) 0.253ms
```

`35M` 이던 것이 `1M` 이 되었다. 만든 것의 대부분이 죽었다는 뜻이다.

<br/>

이럴 때 힙 전체를 뒤지는 것은 낭비다. `거의 다 죽었을 게 뻔한 구역` 만 따로 모아서 훑으면

한 번에 `0.3ms` 밖에 안 걸린다. 세대를 나눈 이유가 바로 이것이다.

```java
대부분 금방 죽는다 -> 금방 죽을 애들만 모아서 자주 훑자 -> Young
살아남은 애들은 오래 살더라 -> 얘들은 가끔만 훑자      -> Old
```

<br/>

## Survivor 둘 중 하나가 왜 꼭 비어 있어야 하는가?

`Young` 영역의 GC는 지우는 방식이 아니라 `살아남은 것만 옮기는` 방식이기 때문이다.

- `Eden` 이 꽉 차면 GC가 돈다

- 살아있는 객체만 골라서 비어 있는 `Survivor` 쪽으로 통째로 옮긴다
- 옮기고 나면 `Eden` 과 원래 있던 `Survivor` 는 통째로 비워버린다

죽은 객체를 하나하나 찾아 지우는 것이 아니라, 산 것만 데리고 나오고 나머지는 쓸어버리는 것이다.

<br/>

옮겨 담을 빈 공간이 반드시 하나는 필요하기 때문에, `S0` 과 `S1` 중 하나는 항상 비어 있어야 한다.

이렇게 하면 빈 자리가 군데군데 생기는 `단편화` 도 없다. 옮길 때마다 앞에서부터 차곡차곡 쌓기 때문이다.

```java
Eden 꽉 참 -> 산 것만 빈 Survivor로 이사 -> Eden과 쓰던 Survivor는 통째로 비움
```

<br/>

## 그럼 언제 Old로 넘어가는가?

이사를 갈 때마다 객체에 `나이` 가 하나씩 붙는다.

이 나이가 기준값을 넘으면 "얘는 오래 살 놈이구나" 하고 `Old` 로 보낸다.

<br/>

기본값을 확인해보면 이렇다.

```java
java -XX:+PrintFlagsFinal -version | grep MaxTenuringThreshold

uint MaxTenuringThreshold = 15
```

<br/>

나이가 붙는 것도 로그로 볼 수 있다.

```java
java -Xmx64m -Xlog:gc+age=trace Age
```

```java
GC(1) Age table:
GC(1) - age   1:     102432 bytes,     102432 total
GC(1) - age   2:     185704 bytes,     288136 total

GC(3) Age table:
GC(3) - age   1:     102496 bytes,     102496 total
GC(3) - age   2:      51216 bytes,     153712 total
GC(3) - age   3:     102432 bytes,     256144 total
GC(3) - age   4:     185704 bytes,     441848 total
```

GC가 돌 때마다 `age 1` 짜리가 새로 생기고, 살아남은 것들은 나이가 하나씩 올라가고 있다.

이 나이가 `15` 가 되면 `Old` 로 승격되는 것이다.

<br/>

## Minor GC와 Major GC

- `Minor GC` : `Young` 영역만 훑는다. 위에서 본 것처럼 밀리초 단위로 끝난다.

- `Major GC` : `Old` 영역까지 훑는다. 뒤질 것이 많아서 훨씬 오래 걸린다.

둘 다 도는 동안은 애플리케이션 스레드가 멈춘다. 이것을 `stop-the-world` 라고 한다.

<br/>

`Minor GC` 는 자주 돌지만 짧아서 티가 잘 안 난다.

문제가 되는 것은 `Major GC` 다. 오래 사는 객체가 계속 쌓이면 `Old` 가 차고, 그러면 긴 멈춤이 생긴다.

```java
캐시나 static 컬렉션에 객체를 계속 넣기만 하면 Old가 차오른다
-> Major GC가 자주 돌고 -> 멈추는 시간이 길어진다
```

<br/>

## 참고로, 요즘 기본 GC는 G1이다

```java
java -XX:+PrintFlagsFinal -version | grep UseG1GC

bool UseG1GC = true
```

`G1` 에서는 위 그림처럼 `Eden` 과 `Survivor` 가 큰 덩어리로 붙어 있지 않다.

힙을 작은 구역으로 잘게 쪼개놓고, 각 구역에 그때그때 `Eden` 이나 `Survivor` 역할을 맡긴다.

<br/>

그래도 `Eden에서 시작해서 Survivor를 거쳐 Old로 간다` 는 흐름 자체는 그대로다.

위 그림은 자리 배치가 아니라 그 흐름을 이해하는 그림으로 보면 된다.
