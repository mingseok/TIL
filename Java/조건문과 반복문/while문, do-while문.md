## while문, do-while문 설명
무조건 실행 해야 된다 하면 do while

조건에 따라 실행하고 싶다 하면 그냥 while
<br/>

## while문 
while문은 먼저 조건식을 평가해서 **조건식이 거짓이면 문장 전체를 벗어나고**, 
<br/>**참이면 블럭 { } 내의 문장을 수행**하고 **다시 조건식으로 돌아간다.** 
<br/>**조건식이 거짓이 될 때까지** 이 과정이 계속 반복 된다.

```java
while(조건식) {
// 조건식의 연산결과가 참인 동안, 반복될 문장들을 적는다.
}

```

<br/>

### while문 예제

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
     //어렵게 생각하지마라. 
     //결국 while문 처럼 ture면 다시 반복하고, false면 반복문을 벗어나는 것이다.
     
     //조건식의 연산결과가 참일 때 수행될 문장들을 적는다. 
     //최소 한번은 수행될 것을 보장한다.
  } while(조건문); <- 끝에 ';'을 잊지 않도록 하기
```

<br/>

## do-while문 예제

```java
import java.util.*;

public class aaa {

    public static void main(String[] args) {

        int input = 0, answer = 5;

        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("1과 10사이의 정수 입력");
            input = sc.nextInt();

        } while (input != answer); // 1 != 5 될 경우 조건식이 true 이므로, 조건식이 맞다.
                                   // 그러면 다시 do while문을 반복한다. 내려가는게 아님

                                   // 그러다 5 != 5 될 경우 false 이므로, 조건식이 틀리다.
                                   // 그러면 do while문을 종료 하는 것이다. 
        
        // while문은 밑쪽 즉, 여기는 해당 사항이 없다.
        // do { } 괄호 안을 위한 while문이다. (while문 뒤에 { } 가 없음.)

        System.out.println("정답입니다.");
    }

}
```


<br/><br/>

>**Reference**
><br/>남궁 성 지음, 『자바의 정석』, 도우출판.
