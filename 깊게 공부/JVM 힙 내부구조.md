## JVM 힙 내부구조



![이미지](/programming/img/입문395.PNG)

- `Eden` 영역 : 새롭게 생성된 객체들이 할당되는 영역

- `Survival` 영역(S0, S1) : `minor gc`로부터 살아남은 객체들이 존재하는 영역
    - `Survival` 영역의 특별한 규칙 : `Survival 0`, 혹은 `Survival 1` 둘 중 하나는
        
        꼭 비어 있어야 한다는 것이다
        

<br/><br/>

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