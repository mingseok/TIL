## 예외처리하기 - try-catch문

프로그램의 실행도중에 발생하는 에러는 어쩔 수 없지만, 예외는 프로그래머가 이에 대한 처리를 미리 해주어야 한다.

<br/>

| 예외처리 |  |
| --- | --- |
| 정의 | 프로그램 실행 시 발생할 수 있는 예외에 대비한 코드를 작성하는 것 |
| 목적 | 프로그램의 비정상 종료를 막고, 정상적인 실행상태를 유지하는 것 |

<br/>

### 문법

```java
try {
	// 예외가 발생할 가능성이 있는 문장들을 넣는다.

} catch (Exception1 e1) {
	// Exception1 이 발생했을 경우, 이를 처리하기 위한 문장을 적는다.
} catch (Exception2 e2) {
	// Exception2 이 발생했을 경우, 이를 처리하기 위한 문장을 적는다.
}  catch (Exception3 e3) {
	// Exception3 이 발생했을 경우, 이를 처리하기 위한 문장을 적는다.
}
```

하나의 try블럭 다음에는 여러 종류의 예외를 처리할 수 있도록 하나 이상의 catch블럭이 올 수 있으며, <br/>이 중 발생한 예외의 종류와 일치하는 **단 한 개의 catch블럭만 수행된다.**

<br/>

### 예제.

```java
class aaa {
   public static void main(String args[]) {
      int number = 100;
      int result = 0;

      for (int i = 0; i < 10; i++) {
	 result = number / (int) (Math.random() * 10); // 9번째 라인
	 System.out.println(result);
      }
    } 
}

출력값.
12
16
Exception in thread "main" java.lang.ArithmeticException: / by zero
	at ddd.aaa.main(aaaa.java:9) // <- aaa.java의 9번째 라인
```

대부분의 경우 10번이 출력되기 이전에 예외가 발생하여 프로그램이 비정상적으로 종료될 것이다.

<br/>결과에 나타난 메세지를 보면. ArithmeticException 이 발생 했고, <br/>발생 위치는 aaa클래스의 main메서드 aaa.java의 9번째 라인 이라는 것을 알 수 있다.

<br/>ArithmeticException 은 산술연산과정에서 오류가 있을 때 발생하는 예외 이며, <br/>정수는 0으로 나누는 것이 금지되어 있기 때문에 발생하였다.


<br/>

### 예제

위 코드를 예외처리구문을  추가해서 실행도중 예외가 발생하더라도 <br/>프로그램이 실행 도중에 비정상적으로 종료되지 않도록 수정해보자.

<br/>작업을 모두 마치고 정상적으로 종료되었다.

<br/>만일 예외처리를 하지 않았다면, 세 번째 줄까지만 출력되고 예외가 발생해서 프로그램이 비정상적으로 종료되었을 것이다.

```java
class aaaa {
   public static void main(String args[]) {
      int number = 100;
      int result = 0;

      for (int i = 0; i < 10; i++) {
         try {
            result = number / (int) (Math.random() * 10);
            System.out.println(result);
         } catch (ArithmeticException e) {
            System.out.println("0");
         }
      }
    }
}

출력값.
12
11
50
20
0 <-- ArithmeticException 이 발생하여 0이 출력 되었다.
20
11
11
12
33
```

<br/>


## try-catch문에서의 흐름

**try블럭 내에서 예외가 발생한 경우,**

1. 발생한 예외와 일치하는 catch블럭이 있는지 확인한다.
2. 일치하는 catch블럭을 찾게 되면, 그 catch블럭 내의 문장들을 수행하고 전체 try-catch문을 빠져나가서 <br/>그 다음 문장을 계속해서 수행한다. 만일 일치하는 catch블럭을 찾지 못하면, 예외는 처리되지 못한다.


**try블럭 내에서 예외가 발생하지 않는 경우**

1. catch블럭을 거치지 않고 전체 try-catch문을 빠져나가서 수행을 계속한다.


<br/>

### 예제

예외가 발생하지 않았으므로 catch블럭의 문장이 실행되지 않았다.

```java
class aaaa {
   public static void main(String args[]) {
      System.out.println(1);
      System.out.println(2);
      
      try {
         System.out.println(3);
         System.out.println(4);
      } catch (Exception e) {
         System.out.println(5); // 실행되지 않는다.

      } // try-catch의 끝.
        System.out.println(6);
   }
}

출력값.
1
2
3
4
6
```

<br/>

### 예제

System.out.println(4); 는 실행되지 않는다.

try-catch문을 벗어나서 그 다음 문장을 실행하여 6을 화면에 출력한다.

try블럭에서 예외가 발생하면, 예외가 발생한 위치 이후에 있는 try블럭의 문장들은 수행 되지 않으므로, <br/>try블럭에 포함시킬 코드의 범위를 잘 선택해야 한다.

```java
class aaaa {
   public static void main(String args[]) {
      System.out.println(1);
      System.out.println(2);
      
      try {
         System.out.println(3);
         System.out.println(0 / 0); // 0으로 나눠서 고의로 ArithmeticException 에러 발생.
         System.out.println(4); // 실행되지 않는다.
			
      } catch (ArithmeticException ae) {
            System.out.println(5);
			
      } // try-catch의 끝
        System.out.println(6);
   } 
}

출력값
1
2
3
5
6
```

<br/>

## 예외의 발생과 catch블럭

괄호() 와 블럭 { } 두 부분으로 나눠져 있는데, 괄호 ( ) 내에는 처리하고자 하는 예외와 같은 타입의 참조변수 하나를 선언해야 한다.

<br/>예외에 해당하는 클래스의 인스턴스가 만들어 진다.

<br/>바로 위의 예제를 보면 ArithmeticException 이 발생했으므로 ArithmeticException 인스턴스가 생성된다.<br/> 예외가 발생한 문장이 try블럭에 포함되어 있다면, 이 예외를 처리할 수 있는 catch블럭이 있는지 찾게 된다.

<br/>모든 예외 클래스는 Exception클래스 자손이므로, catch블럭의 괄호( ) 에 Exception 클래스 타입의 참조변수를 <br/>선언해 놓으면 어떤 종류의 예외가 발생하더라도 이 catch블럭에 의해서 처리된다.


<br/><br/>


### 예제

결과는 같다.

대신에 Exception클래스의 참조변수를 선언하였다.

Exception클래스의 자손이므로 ArithmeticException 인스턴스와 Exception클래스와의 <br/>instanceof연산결과가 true가 되어 Exception클래스 타입의 참조변수를 선언한 catch블럭의 문장들이 수행되고 예외는 처리되는 것이다. 

```java
class ExceptionEx6 {
   public static void main(String args[]) {
      System.out.println(1);			
      System.out.println(2);
      
      try {
        System.out.println(3);
        System.out.println(0/0);
        System.out.println(4); 	// 실행되지 않는다.

        } catch (Exception e) {	// ArithmeticException대신 Exception을 사용.
             System.out.println(5);

        } // try-catch의 끝
          System.out.println(6);
	  
    } // main메서드의 끝
}

출력값.
1
2
3
5
6
```

<br/>

### 예제

Exception 클래스 타입의 참조변수를 선언한 것을 사용 하였다.

<br/>첫번째. 검사에서 일치하는 catch블럭을 찾았기 때문에 두번째 catch블럭은 검사하지 않게 된다.

<br/>만일 try 블럭 내에서 ArithmeticException 이 아닌 다른 종류의 예외가 발생한 경우에는 <br/>두번째 catch블럭인 Exception 클래스 타입의 참조변수를 선언한 곳에서 처리되었을 것이다.

<br/>어떤 종류의 예외가 발생하더라도 이 catch블럭에 의해 처리되도록 할 수 있다.
```java
class aaaa {
   public static void main(String args[]) {
     System.out.println(1);
     System.out.println(2);
     
     try {
       System.out.println(3);
       System.out.println(0 / 0); // 0으로 나눠서 에러 발생.
       System.out.println(4); // 실행되지 않는다.

      } catch (ArithmeticException ae) {
         if (ae instanceof ArithmeticException) {
            System.out.println("true");
      }
            System.out.println("ArithmeticException");

      } catch (Exception e) {
         System.out.println("Exception");
      } // try-catch의 끝
         System.out.println(6);
	 
    } // main메서드의 끝
}

출력값.
1
2
3
true
ArithmeticException
6
```

<br/><br/>

>**Reference**
><br/>남궁 성 지음, 『자바의 정석』, 도우출판.
