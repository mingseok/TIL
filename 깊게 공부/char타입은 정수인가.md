## char타입은 정수인가?

char는 기본형 타입에서 문자 타입으로 분리되었지만, 사실상 2 바이트의 정수입니다.

```
그 이유는 실제로 컴퓨터는 문자를 구별할 수 없습니다. 
```

즉, 컴퓨터 안에는 절대 문자가 저장될 수 없을뿐더러, 모든 것이 숫자로 저장되는 것입니다.

<br/>

## 그렇다면 char는 대체 어떻게 저장되나요?

사람과 컴퓨터가 언어를 교환하기 위해서 만들어진 코드가 있다. 

대표적으로는 아스키코드가 있습니다. 

<br/>

따라서 우리가 `char`형 변수의 문자를 선언하게 되면, 

해당 문자는 아스키코드의 규칙에 맞게 숫자로 치환이 되어 컴퓨터에 저장되게 됩니다.

<br/>

## 코드로 확인

```java
char c = 'A';
int c1 = 'A';

System.out.println(c);   // A
System.out.println('A'); // A

System.out.println((int)c);   // 65
System.out.println((int)'A'); // 65
System.out.println(c1);       // 65
```

### 연산도 가능하다

```java
char c1 = 'A';
char c2 = 'A';

System.out.println(c1 + c2);   // 130
System.out.println('A' + 'A'); // 130
```

정수 65와 65가 더해진 것이다.

<br/>

## 궁금증!

```java
"아스키코드로 저장된다"고 했는데, 그러면 한글은 어떻게 되는 걸까?
```

아스키코드는 `1` 바이트라서 `0 ~ 127` 밖에 표현하지 못한다. 영어 알파벳과 기호가 전부다.

그런데 자바의 `char` 는 `2` 바이트다. 범위를 찍어보면 이렇다.

```java
System.out.println((int) Character.MIN_VALUE + " ~ " + (int) Character.MAX_VALUE);
```

```java
0 ~ 65535
```

<br/>

`6만` 개가 넘는다. 아스키코드보다 훨씬 넓다.

자바의 `char` 가 쓰는 것은 아스키코드가 아니라 `유니코드` 다. 정확히는 `UTF-16` 방식이다.

<br/>

아스키코드는 유니코드 앞부분에 그대로 포함되어 있다. `'A'` 가 `65` 인 것은 그래서 똑같다.

그리고 그 뒤로 한글, 한자, 그리스 문자까지 자리가 이어진다.

```java
System.out.println((int) 'A');    // 65
System.out.println((int) '가');   // 44032

char han = '가';
System.out.println(han);          // 가
```

한글도 `char` 한 칸에 그대로 들어가는 것이다.

<br/>

## 그런데 이모지는 안 들어간다

```java
String emoji = "😀";
System.out.println("글자 하나인데 length() = " + emoji.length());
System.out.println("charAt(0) = " + (int) emoji.charAt(0));
System.out.println("charAt(1) = " + (int) emoji.charAt(1));
System.out.println("실제 코드포인트 개수 = " + emoji.codePointCount(0, emoji.length()));
```

<br/>

### 결과

```java
글자 하나인데 length() = 2
charAt(0) = 55357
charAt(1) = 56832
실제 코드포인트 개수 = 1
```

눈에는 글자 하나인데 `length()` 가 `2` 다.

<br/>

유니코드에 문자가 계속 늘어나면서 `65535` 개로도 모자라게 되었다.

넘치는 문자들은 `char` 두 개를 짝지어서 표현하기로 했는데, 그 조각들이 `55357` 과 `56832` 다.

각각은 아무 의미가 없고, 둘이 붙어 있어야 웃는 얼굴이 된다.

```java
글자 수를 세야 한다면 length() 가 아니라 codePointCount() 를 써야 한다
```

<br/>

## char가 정수라서 생기는 일

`char` 끼리 더하면 `char` 가 아니라 `int` 가 나온다.

```java
System.out.println('A' + 'A');   // 130
```

<br/>

자바는 `int` 보다 작은 타입끼리 계산할 때 일단 `int` 로 올려놓고 계산한다.

`byte`, `short`, `char` 가 전부 그렇다. CPU가 `int` 단위로 계산하는 것이 편하기 때문이다.

<br/>

그래서 이런 코드가 컴파일 에러가 난다.

```java
char c = 'A';
c = c + 1;
```

```java
error: incompatible types: possible lossy conversion from int to char
        c = c + 1;
              ^
```

`c + 1` 의 결과가 `int` 인데 `char` 칸에 넣으려니 넘칠 수 있다고 막는 것이다.

<br/>

그런데 이건 된다.

```java
char c = 'A';
c += 1;
System.out.println(c);   // B
```

`+=` 같은 복합 대입 연산자는 형변환을 자동으로 넣어준다.

`c += 1` 이 사실은 `c = (char)(c + 1)` 로 처리되는 것이다.

```java
c = c + 1   -> int 를 char 에 넣으려 해서 에러
c += 1      -> 컴파일러가 (char) 캐스팅을 넣어줘서 통과
```

<br/>

## char는 자바에서 유일하게 음수가 없는 타입이다

```java
byte  : -128 ~ 127
short : -32768 ~ 32767
int   : -21억 ~ 21억
char  : 0 ~ 65535
```

문자에 붙는 번호는 음수일 이유가 없어서 부호 비트를 안 쓰고 전부 숫자에 쓴 것이다.

같은 `2` 바이트인 `short` 보다 표현할 수 있는 최댓값이 두 배 큰 이유가 여기에 있다.
