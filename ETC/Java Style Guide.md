## Java Style Guide

## 패키지 이름은 소문자로 구성

나쁜 예시

```java
package com.navercorp.apiGateway

package com.navercorp.api_gateway
```

좋은 예시

```java
package com.navercorp.apigateway
```

<br/><br/>

## 소스파일당 1개의 탑레벨 클래스를 담기

나쁜 예시

```java
public class LogParser {
}

class LogType {
}
```

좋은 예시

```java
public class LogParser {
    // 굳이 한 파일안에 선언해야 한다면 내부 클래스로 선언
    class LogType {
    }
}
```

<br/><br/>

## static import에만 와일드 카드 허용

나쁜 예시

```java
import java.util.*;
```

좋은 예시

```java
import java.util.List;
import java.util.ArrayList;
```

<br/><br/>

## 상수는 대문자와 언더스코어로 구성

```java
public final int UNLIMITED = -1;
public final String POSTAL_CODE_EXPRESSION = “POST”;
```

<br/><br/>

## 변수에 소문자 카멜표기법 적용

나쁜 예시

```java
private boolean Authorized;
private int AccessToken;
```

좋은 예시

```java
private boolean authorized;
private int accessToken;
```

<br/><br/>

## 한 줄에 한 문장

나쁜 예시

```java
int base = 0; int weight = 2;
```

좋은 예시

```java
int base = 0;
int weight = 2;
```

<br/><br/>

## 중괄호

### 생략 가능하더라도 중괄호를 사용해야 한다.

<br/>

나쁜 예시

```java
if (sum > 0)
    return sum;
else
    return -1;
```

좋은 예시

```java
if (sum > 0) {
    return sum;
}
else {
    return -1;
}
```

<br/>

### 추가적인 코드 예시

```java
// 가능하다
if (condition) {
    body();
}

// 나름 허용한다.
if (condition) body();

// 이건 허용하지 않는다.
if (condition)
    body(); 
```

<br/><br/>

## K&R 스타일로 중괄호 선언

나쁜 예시

```java
public class SearchConditionParser
{
    public boolean isValidExpression(String exp)
    {

        if (exp == null)
        {
            return false;
        }

        for (char ch : exp.toCharArray())
        {
             ....
        }

        return true;
    }
}
```

좋은 예시

```java
public class SearchConditionParser {
    public boolean isValidExpression(String exp) {

        if (exp == null) {
            return false;
        }

        for (char ch : exp.toCharArray()) {
            ....
        }

        return true;
    }
}
```

<br/><br/>

## 닫는 중괄호와 같은 줄에 `else`, `catch`, `finally`, `while` 선언

ex1) 나쁜 예시

```java
if (line.startWith(WARNING_PREFIX)) {
    return LogPattern.WARN;
}
else if (line.startWith(DANGER_PREFIX)) {
    return LogPattern.DANGER;
}
else {
    return LogPattern.NORMAL;
}
```

ex1) 좋은 예시

```java
if (line.startWith(WARNING_PREFIX)) {
    return LogPattern.WARN;
} else if (line.startWith(DANGER_PREFIX)) {
    return LogPattern.NORMAL;
} else {
    return LogPattern.NORMAL;
}
```

<br/>

ex2) 나쁜 예시

```java
try {
    writeLog();
}
catch (IOException ioe) {
    reportFailure(ioe);
}
finally {
    writeFooter();
}
```

ex2) 좋은 예시

```java
try {
    writeLog();
} catch (IOException ioe) {
    reportFailure(ioe);
} finally {
    writeFooter();
}
```

<br/>

ex3) 나쁜 예시

```java
do {
    write(line);
    line = readLine();
}
while (line != null);
```

ex3) 좋은 예시

```java
do {
    write(line);
    line = readLine();
} while (line != null);
```

<br/><br/>

## 조건/반복문에 중괄호 필수 사용

나쁜 예시

```java
if (exp == null) return false;

for (char ch : exp.toCharArray()) if (ch == 0) return false;
```

좋은 예시

```java
if (exp == null) {
    return false;
}

for (char ch : exp.toCharArray()) {

    if (ch == 0) {
        return false;
    }

}
```

<br/><br/>

## 식별자와 여는 소괄호 사이에 공백 미삽입

나쁜 예시

```java
public StringProcessor () {} // 생성자

@Cached ("local")
public String removeEndingDot (String original) {
    assertNotNull (original);
    ...
}
```

좋은 예시

```java
public StringProcessor() {} // 생성자

@Cached("local")
public String removeEndingDot(String original) {
    assertNotNull(original);
    ...
}
```

<br/><br/>

## 타입 캐스팅에 쓰이는 소괄호 내부 공백 미삽입

나쁜 예시

```java
String message = ( String ) rawLine;
```

좋은 예시

```java
String message = (String)rawLine;
```

<br/><br/>

## 콤마/구분자 세미콜론의 뒤에만 공백 삽입

나쁜 예시

```java
for (int i = 0;i < length;i++) {
    display(level,message,i)
}
```

좋은 예시

```java
for (int i = 0; i < length; i++) {
    display(level, message, i)
}
```

<br/><br/>

## 단항 연산자와 연산 대상 사이에 공백을 미삽입

나쁜 예시

```java
int point = score[++ index] * rank -- * - 1;
```

좋은 예시

```java
int point = score[++index] * rank-- * -1;
```

<br/><br/>

## ASCII 코드 외의 문자

```java
String unitAbbrev = "μs";  - Best

String unitAbbrev = "\u03bcs"; // 최악, 이게 뭔지 알 도리가 없음
```

<br/><br/>

## 빈 블록인 경우

중괄호를 그냥 열고 닫아라.

```java
// 가능
void doNothing() {}

// 가능
void doNothingElse() {
}

// 안됨 (try에는 내용이 있기 때문에)
try {
    doSomething();
} catch (Exception e) {}
```

<br/><br/>

## 공백

```java
private int x; // 괜찮음
private Color color; // 괜찮음

private int   x;      // 해도 되지만, 권장하지 않음
private Color color;  // 괜찮음
```

<br/><br/>

## 그룹화 괄호 : 권장

```java
int n = 5 * 3 + 1 // 가능하지만 별로

int n = (5 * 3) + 1 // 이렇게 사용하자
```

<br/><br/>

## 배열

전부 가능하다.

```java
new int[] {           new int[] {
  0, 1, 2, 3            0,
}                       1,
                        2,
new int[] {             3,
  0, 1,               }
  2, 3
}                     new int[]
                          {0, 1, 2, 3}
```

<br/><br/>

## 숫자 리터럴

long형 숫자의 경우 `3000000000l`대신 `3000000000L` 으로 쓴다.

소문자 l은 숫자 1과 헷갈릴 수 있기 때문이다.