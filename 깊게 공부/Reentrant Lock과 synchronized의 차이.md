## Reentrant Lock과 synchronized의 차이



둘다 `JVM`환경에서 쓰레드에 `Lock`걸어야 할때의 방법이다.

<br/><br/>

## Synchronized

```java
public synchronized void method(){
}
```

현재 데이터를 사용하고 있는 해당 스레드를 제외하고, 

나머지 스레드들은 메서드에 접근 시 블록 전체에 `lock`을 걸어 막는 개념이다.

- 자바에서 스레드 동기화 시 사용하는 대표적인 기법(?) → “이거 맞나?”

- `Synchronized` 키워드를 너무 남발하면 오히려 프로그램 성능저하를 일으킬 수 있다.

<br/>

### `Syncronized`를 사용할 경우

나의 스레드가 

- 현재 멈추어 있는지?

- 기아 상태(=무한히 진행하지 못하는 상태)에 빠진 건지?
- `Locking`을 들고 있는 건지?

등등의 정보를 얻을 수 없다.

<br/><br/>

## 하지만, **ReentrantLock 가능하다.**

```java
private final ReentrantLock lock = new ReentrantLock();
```

`Syncronized`는 `Lock`이 걸리면 `Release`까지 무한하게 기다릴 수 밖에 없으나, 

`ReentrantLock`을 사용하면 현재 상태를 확인할 수 있다.

<br/><br/>


## 임계영역 범위는?

```java
"임계영역" 뜻은?
-> 동시에 여러 스레드가 공유 데이터에 접근하려는 상황을 관리하고 제어하는 영역이다.
```

### **synchronized**

- 메서드 안에 임계 영역의 시작과 끝이 존재해야 한다.

### **ReentrantLock**

- `lock()`, `unlock()`으로 시작과 끝을 명시하기 때문에 여러 메서드에 나눠서 작성할 수 있다.

<br/><br/>

## 경쟁 상태는?

```java
"경쟁상태" 뜻은?
-> '경쟁 상태' 또는 '경쟁 조건'은 다중 스레드나 다중 프로세스 환경에서 
    여러 실행 단위가 데이터에 동시 접근하고 수정하려고
    시도할 때 발생하는 문제를 가리킵니다.
```

### **synchronized**

- 스레드 진입권 획득 순서 보장 하지 않는다.

### **ReentrantLock**

- 메서드를 호출함으로써 어떤 스레드가 먼저 락을 획득하게 될지 순서를 지정할 수 있다.


<br/>
<br/>


## Synchronized와 ReentrantLock의 차이점

- `Synchronized`는 어느 스레드가 먼저 `lock`을 획득할지
    
    보장하지 못하나, `ReentrantLock`락은 가능하다
    
- `Synchronized`는 임계 영역의 시작과 끝이 있어야 하나,
    
    `ReentrantLock`는 `lock`, `unlock`을 사용해 여러 메서드에 나눠 작성이 가능하다
    
    - 현실적으로 `rock`, `unlock`을 여러 메서드에서 쓰는 경우는 거의 없다고 한다.
    
- 공정성
    - `lock()` 사용 시 경쟁이 발생하면 가장 오래 기다린 스레드에게 `lock`을 제공하는 것

    - `Synchronized`는 공정성 지원하지 않지만 `ReentrantLock`는 지원함