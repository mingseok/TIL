## printStackTrace()와 getMessage() 메서드

| printStackTrace | 예외발생 당시에 호출스택에 있었던 메서드의 정보와 예외 메세지를 화면에 출력한다. |
| --- | --- |
| getMessage | 발생한 예외클래스의 인스턴스에 저장된 메세지를 얻을 수 있다. |

<br/>

### 예제

비정상적으로 종료되었을 때의 결과와 비슷하지만 예외는 try-catch문에 의해 처리 되었으며, <br/>프로그램은 정상적으로 종료되었다.

<br/>정보와 예외 메세지를 출력하였다.

<br/>예외처리를 하여 예외가 발생해도 비정상적으로 종료하지 않도록 해주는 동시에, <br/>printStackTrace() 또는 getMessage() 와 같은 메서드를 통해서 예외의 발생원인을 알 수 있다.

```java
class aaaa {
	public static void main(String args[]) {
		System.out.println(1);
		System.out.println(2);
		try {
			System.out.println(3);
			System.out.println(0 / 0); // 예외발생!!!
			System.out.println(4); // 실행되지 않는다.
			
		} catch (ArithmeticException ae) {
			ae.printStackTrace(); // 참조변수 ae를 통해, 생성된 ArithmeticException 인스턴스에 접근할 수 있다.
			System.out.println("예외메시지 : " + ae.getMessage());
			
		} // try-catch의 끝
		System.out.println(6);
	} // main메서드의 끝
}

출력값.
1
2
3
java.lang.ArithmeticException: / by zero
	at ddd.aaaa.main(aaaa.java:9)
예외메시지 : / by zero
6
```

<br/><br/>

>**Reference**
><br/>남궁 성 지음, 『자바의 정석』, 도우출판.
