## 자바스크립트 (=JS)

### 자바 스크립트는 `<body>` 태그 안에서 작성해준다.

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <script>
       document.write('hello world); 
    </script>
</body>
</html>

출력값: hello world
```



### 어떤 페이지에 글씨를 출력할 때는 `document.write()` 라는 걸 사용해주는 것이다.

<br/>

## 자바스크립트를 쓰는 이유는?

그냥 html을 할 경우는 동적으로 되지 않는다. 

그리하여 우리는 마법 같은 변할 수 있는 자스를 사용하는 것이다.

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>

    <h1>JavaScript</h1>
		
    <script>
       document.write(1+1);
    </script>

    <h1>html</h1>
    1+1

</body>
</html>
```

<br/>

### 결과 화면

![이미지](/programming/img/js1.PNG)

<br/><br/>

>**Reference** <br/>생활코딩 자바스크립트 : https://www.youtube.com/watch?v=dPRtcRwKo-Y&list=PLuHgQVnccGMBB348PWRN0fREzYcYgFybf

<br/>

## 궁금증!

```java
== 를 쓰면 안 된다는 이유
```

직접 찍어보면 왜 그런지가 보인다.

```javascript
0 == ""      ->  true
0 == "0"     ->  true
"" == "0"    ->  false
```

<br/>

`A = B` 이고 `A = C` 인데 `B ≠ C` 다.

<br/>

수학의 등호라면 성립할 수 없는 관계다.

<br/>

`""` 와 `"0"` 은 숫자로 바꿀 때 둘 다 `0` 이 되는데,

둘 다 문자열이면 숫자로 안 바꾸고 그대로 비교하기 때문이다.

<br/>

## 다른 것들도 이상하다

```javascript
[] == false          ->  true
NaN == NaN           ->  false
null == undefined    ->  true
null === undefined   ->  false
```

<br/>

`[] == false` 가 `true` 인 과정이 이렇다.

```java
[] -> "" (배열을 문자열로) -> 0 (숫자로)
false -> 0
0 == 0 -> true
```

<br/>

두 단계를 거쳐서 같아진다.

<br/>

`NaN == NaN` 이 `false` 인 것은 앞의 래퍼 클래스 글에서 본 자바와 같다.

IEEE 754가 그렇게 정했기 때문이다.

<br/>

## 그래서 === 를 쓴다

타입이 다르면 그냥 `false` 다. 변환을 안 한다.

<br/>

예외는 앞의 데이터타입, 변수 글에서 본 그 경우다.

```javascript
if (value == null)      // null 과 undefined 를 한 번에
```

<br/>

이건 의도가 분명하니 괜찮다.

<br/>

## 숫자 정렬이 이상하다

```javascript
[1, 10, 9, 2].sort()   ->  [1, 10, 2, 9]
```

<br/>

문자열로 바꿔서 비교하기 때문이다.

```javascript
"1" < "10" < "2" < "9"
```

<br/>

숫자로 정렬하려면 비교 함수를 줘야 한다.

```javascript
[1, 10, 9, 2].sort((a, b) => a - b)   ->  [1, 2, 9, 10]
```

<br/>

앞의 Comparable과 Comparator 글에서 본 뺄셈 오버플로가

자바스크립트에는 없다.

숫자가 64비트 부동소수점이라 `int` 범위 개념이 없기 때문이다.

<br/>

대신 `2^53` 을 넘으면 정밀도가 깨진다.

앞의 데이터타입, 변수 글에서 본 그 한계다.

<br/>

## 0.1 + 0.2 가 0.3 이 아니다

```javascript
0.1 + 0.2 = 0.30000000000000004
0.1 + 0.2 === 0.3 -> false
```

<br/>

앞의 실수 표현 글에서 본 그 문제다. 자바도 같다.

<br/>

자바에는 `BigDecimal` 이 있는데 자바스크립트에는 표준이 없다.

<br/>

그래서 금액을 다룰 때 정수로 계산하는 방식을 쓴다.

```javascript
원 단위로 정수로만 다룬다
표시할 때만 나눈다
```

<br/>

앞의 프로젝트에서 금액을 `long` 으로 다루는 것과 같은 이유다.

<br/>

## 언어를 왜 이렇게 만들었나

10일 만에 만들었기 때문이다.

<br/>

1995년에 넷스케이프가 브라우저에 넣을 스크립트 언어를 급하게 만들었다.

`간단한 폼 검증` 정도가 목적이었다.

<br/>

이렇게 큰 언어가 될 줄 몰랐던 것이다.

<br/>

그리고 웹의 하위 호환 원칙 때문에 고칠 수가 없다.

```java
20 년 전에 만든 페이지가 지금도 동작해야 한다
```

<br/>

앞의 인터페이스의 Default Method 글에서 본 것과 같은 제약인데,

자바스크립트는 그 강도가 훨씬 세다.

<br/>

그래서 고치는 대신 새 문법을 추가하는 방식으로 발전했다.

```java
== 는 두고 === 를 추가
var 는 두고 let, const 를 추가
function 은 두고 화살표 함수를 추가
```

<br/>

`쓰지 말아야 할 것` 목록이 계속 늘어나는 언어가 된 이유다.

<br/>

린터를 쓰는 게 사실상 필수인 것도 이 때문이다.

```javascript
eslint: eqeqeq, no-var
```

<br/>

언어가 막아주지 않으니 도구로 막는 것이다.
