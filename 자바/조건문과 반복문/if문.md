## if문 모르는 것에 대해서 풀이 설명

```java
package test;

public class gghh {

	public static void main(String[] args) {

		int ptr = 1;

		if (ptr <= 0)

			System.out.println("1");
		System.out.println("2");
		System.out.println("3");
	}

}
```

<br/>

결과는 2, 3이 나온다.

이유는 else 도 적지 않고 중괄호 없을 시 이렇게만 했다면 자바의 if문은 맨 처음의 **'1' 만
true로 인정한다.** <br/>그 뒤 '2', '3' 은 else 라고 생각한다.

<br/>
그리고 int ptr = 1; 에서 —> 0 으로 하였을 경우 if문에 조건에 맞아서 출력 값은 1, 2, 3 이 출력된다.
