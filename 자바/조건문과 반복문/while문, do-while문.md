## while문, do-while문 설명

<br/>

## while문 

```java
  // while문은 먼저 조건식을 평가해서 조건식이 거짓이면 문장 전체를 벗어나고, 
  // 참이면 블럭 { } 내의 문장을 수행하고 다시 조건식으로 돌아간다. 
  // 조건식이 거짓이 될 때까지 이 과정이 계속 반복 된다.
	
			while(조건식) {
			    // 조건식의 연산결과가 참인 동안, 반복될 문장들을 적는다.
			}

```

<br/>

## while문 예제


```java
package test;

public class Test5 {

	public static void main(String[] args) {

		int i = 1;
		while (i <= 10) {
			System.out.println(i++);
		}
	}

}
```

<br/><br/>


## do-while문


```java

		do {

			//조건식의 연산결과가 참일 때 수행될 문장들을 적는다.
			//최소 한번은 수행될 것을 보장한다.

		}	while(조건문) ; <- 끝에 ';'을 잊지 않도록 하기
```

<br/>

## do-while문 예제

```java
package test;

import java.util.*;

public class Test5 {

	public static void main(String[] args) {

		int input = 0, answer = 0;

		answer = (int) (Math.random() * 10) + 1;
		Scanner sc = new Scanner(System.in);

		do {
			System.out.println("1과 10사이의 정수 입력");
			input = sc.nextInt();

		} while (input != answer);
		
		System.out.println("정답입니다.");
	}

}
```