## 자바 가상머신(JVM)

<br/>

## 자바 가상머신(JVM) 운영체제 위에서 동작한다.

- JVM은 운영체제로부터 할당받은 메모리 공간을 기반으로 자바 프로그램을 실행해야 한다.
- JVM은 운영체제로부터 할당받은 메모리 공간을 이용해서 자기 자신도 실행을 하고, 자바 프로그램도 실행을 한다.
    
<br/>


### 총 메모리 공간은 세개의 영역으로 구분한다

- 메서드 영역

    - 메서드의 바이트 코드, static 변수
- 스택 영역
    - 지역변수, 매개변수
- 힙 영역
    - 인스턴스(객체)

<br/>

## 바이트코드란?

- 바이트코드는 고급 언어로 작성된 소스 코드를
    
    가상머신이 이해할 수 있는 중간 코드로 컴파일한 것을 말한다.


<br/>

## 가상머신은

바이트코드를 각각의 하드웨어 아키텍처에 맞는 기계어로 다시 컴파일한다.

정리하자면, 자바 바이트코드는 자바 가상머신에 의해서 실행되는 코드이다.

<br/>

## 메서드 영역

- 메서드의 자바 바이트코드는 JVM이 구분하는 메모리 공간 중에서 `메서드 영역`에 저장된다.

- static으로 선언된 클래스 변수도 `메서드 영역`에 저장된다.

```java
public class JavaTest {

    // 메인 메서드도 static 이므로 메서드 영역에 저장
    public static void main(String[] args) { 
		
    }
}

class JVMTest {
    static int average = 0; // 메서드 영역 저장
    public void Run() { }
}
```

이 영역에 저장된 내용은 프로그램 시작 전에 로드되고, 프로그램 종료시 소멸된다.

<br/><br/>

## 스택 영역

- 매개변수, 지역변수가 할당되는 메모리 공간

- 프로그램이 실행되는 도중에 임시로 할당되었다가 바로 소멸되는 특징이 있는 변수가 할당된다.
- 이 영역에 저장된 변수는 해당 변수가 선언된 메서드 종료시 소멸된다.

![이미지](/programming/img/입문386.PNG)

<br/><br/>

## 힙 영역

- 인스턴스(객체)가 생성되는 메모리 공간

- JVM에 의한 메모리 공간의 정리(가비지 컬렉션)이 이뤄지는 공간
- 할당은 프로그래머가 소멸은 JVM이 처리한다
- 참조변수에 의한 참조가 전혀 이뤄지지 않는 인스턴스가 소멸의 대상이 된다
    - 따라서 JVM은 인스턴스의 참조관계를 확인하고 소멸할 대상을 선정한다.

![이미지](/programming/img/입문387.PNG)

<br/><br/>

## Garbage Collection에 대한 추가 설명

- GC는 한번도 발생하지 않을 수 있다.

- GC가 발생하면, 소멸의 대상이 되는 인스턴스는 결정되지만 이것이 실제 소멸로 바로 이어지지는 않는다.
- 인스턴스의 실제 소멸로 이어지지 않은 상태에서 프로그램이 종료될 수 있다.
- 종료가 되면 어차피 인스턴스는 소멸 된다.