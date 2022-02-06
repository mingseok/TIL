## 스위치 조건문

default란 if문에서 else 같은 역할을 한다.


```java
public class test {
			
		public static void main(String[] args) {
			Scanner sc = new Scanner (System.in);

			int month = sc.nextInt();
			int day = 0;
	
					switch (month) {
	
								case 1:
										day = 31;
										break;

								case 2:
										day = 28;
										break;

							 	case 3:
										day = 31;
										break;

								case 4:
										day = 30;
										break;

								default:

					}
     }
}
```
<br/><br/>

## 응용문제 1단계

```java
public class test {
			
		public static void main(String[] args) {
			Scanner sc = new Scanner (System.in);

			int month = sc.nextInt();
			int day = 0;
	
					switch (month) {
	
								case 1:
										day = 31;
										break;

								case 2:
										day = 28;
										break;

							 	case 3:

								case 4:
										day = 30;
										break;

								default:

					}
     }
}

출력값 : 30이 출력.
	      이유는 case3이 없기 때문에 그 밑에 case4로 내려가 실행한다. 그리고 break를 만나
        종료 한다.
```

<br/><br/>


## 응용문제 2단계


```java
public class test {
			
		public static void main(String[] args) {
			Scanner sc = new Scanner (System.in);

			int month = sc.nextInt();
			int day = 0;
	
					switch (month) {
								
								// 이렇게 묶어서도 사용 할수 있다.	
								case 1:
								case 5:
								case 8:
								case 10:
								case 6:
										day = 31;
										break;

								case 2:
										day = 28;
										break;

							 	case 3:

								case 4:
										day = 30;
										break;

								default:

					}
     }
}

출력값 : 30이 출력.
	      이유는 case3이 없기 때문에 그 밑에 case4로 내려가 실행한다. 그리고 break를 만나 종료 한다.
```

<br/>

무조건 실행 해야 된다 하면 do while

조건에 따라 실행하고 싶다 하면 그냥 while