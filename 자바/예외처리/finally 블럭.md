## finally블럭

예외의 발생여부에 상관없이 실행되어야할 코드를 포함시킬 목적으로 사용된다.

<br/>try-catch문의 끝에 선택적으로 덧붙여 사용할 수 있으며, try-catch-finally의 순서로 구성된다.

```java
try {
	// 예외가 발생할 가능성이 있는 문장들을 넣는다.
} catch (Exception1 e1) {
	// 예외처리를 위한 문장을 적는다.
} finally {
	// 예외의 발생여부에 관계없이 항상 수행되어야하는 문장들을 넣는다.
	// finally블럭은 try-catch문의 맨 마지막에 위치 해야 한다.
}
```

예외가 발생한 경우에는 try → catch → finally 의 순으로 실행되고, 예외가 발생하지 

않은 경우에는 try → finally 의 순으로 실행된다.

<br/>이 예제가 하는 일은 프로그램 설치를 위한 준비를 하고 파일들을 복사하고 설치가 완료되면,<br/> 프로그램을 설치하는데 사용된 임시파일들을 삭제하는 순서로 진행된다.

<br/>프로그램의 설치과정 중에 예외가 발생하더라도, 설치에 사용된 임시파일들이 삭제되도록 catch블럭에 deleteTempFiles() 메서드를 넣엇다.

<br/>예외의 발생여부에 관계없이 deleteTempFiles()메서드는 실행되어야 하는 것이다.

```java
class FinallyTest {
	public static void main(String args[]) {
		try {
			startInstall();		// 프로그램 설치에 필요한 준비를 한다.
			copyFiles();		// 파일들을 복사한다. 
			deleteTempFiles();	// 프로그램 설치에 사용된 임시파일들을 삭제한다.
		} catch (Exception e) {
			e.printStackTrace();
		    	deleteTempFiles();   // 프로그램 설치에 사용된 임시파일들을 삭제한다.
		} // try-catch의 끝
	} // main의 끝

   static void startInstall() { 
        /* 프로그램 설치에 필요한 준비를 하는 코드를 적는다.*/ 
   }
   static void copyFiles() { /* 파일들을 복사하는 코드를 적는다. */ }
   static void deleteTempFiles() { /* 임시파일들을 삭제하는 코드를 적는다.*/ }
}
```

아래 코드는 위의 예제를 finally블럭을 사용해서 변경한 것이다.

<br/>try블럭에서 return문이 실행되는 경우에도 finally블럭의 문장들이 먼저 실행된 후에, 현재 실행 중인 메서드를 종료한다.

```java
class FinallyTest3 {
	public static void main(String args[]) {
		// method1()은 static메서드이므로 인스턴스 생성없이 직접 호출이 가능하다.
		FinallyTest3.method1();		
        System.out.println("method1()의 수행을 마치고 main메서드로 돌아왔습니다.");
	}	// main메서드의 끝

	static void method1() {
		try {
			System.out.println("method1()이 호출되었습니다.");
			return;		// 현재 실행 중인 메서드를 종료한다.
		}	catch (Exception e)	{
			e.printStackTrace();
		} finally {
			System.out.println("method1()의 finally블럭이 실행되었습니다.");
		}
	}	// method1메서드의 끝
}

출력값.
method1()이 호출되었습니다.
method1()의 finally블럭이 실행되었습니다.
method1()의 수행을 마치고 main메서드로 돌아왔습니다.
```


<br/><br/>

>**Reference**
><br/>남궁 성 지음, 『자바의 정석』, 도우출판.
