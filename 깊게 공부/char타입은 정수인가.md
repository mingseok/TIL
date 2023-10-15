## char타입은 정수인가?

char는 기본형 타입에서 문자 타입으로 분리되었지만, 

사실상 2 바이트의 정수입니다.

<br/>

### 그 이유는

실제로 컴퓨터는 문자를 구별할 수 없습니다. 

즉, 컴퓨터 안에는 절대 문자가 저장될 수 없을뿐더러, 

모든 것이 숫자로 저장되는 것입니다.

<br/><br/>

## 그렇다면 char는 대체 어떻게 저장되나요?

사람과 컴퓨터가 언어를 교환하기 위해서 만들어진 코드가 있다. 

대표적으로는 아스키코드가 있습니다. 

<br/>

따라서 우리가 `char`형 변수의 문자를 선언하게 되면, 

해당 문자는 아스키코드의 규칙에 맞게 숫자로 치환이 되어 컴퓨터에 저장되게 됩니다.

<br/>

### 코드 확인

```java
char c = 'A';
int c1 = 'A';

System.out.println(c);   // A
System.out.println('A'); // A

System.out.println((int)c);   // 65
System.out.println((int)'A'); // 65
System.out.println(c1);       // 65
```

<br/>

### 연산도 가능하다

```java
char c1 = 'A';
char c2 = 'A';

System.out.println(c1 + c2);   // 130
System.out.println('A' + 'A'); // 130
```

정수 65와 65가 더해진 것이다.