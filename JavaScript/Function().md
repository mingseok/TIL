## Function()

함수라고 이며, 메소드라고도 부른다.



`two()` 함수를 사용함으로써 `document.write('<li>2-1</li>');` ,

`document.write('<li>2-2</li>');` 인 중복 코드를 없애고, 

함수를 부름으로써 해결할 수 있게 된 것이다.

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>

<h1>Function</h1>
<h2>Basic</h2>
<ul>
    <script>
        function two(){
            document.write('<li>2-1</li>');
            document.write('<li>2-2</li>');
        }
        
        document.write('<li>1</li>');
        two();
        
        document.write('<li>3</li>');
        two();
    </script>
</ul>
<h2>Parameter & Argument</h2>
<h2>Return</h2>

</body>
</html>
```

<br/><br/>

## `nightDayHandler();` 만 사용 하였을 경우, 

함수는 이 이벤트가 소속된 이 태그를 가리키도록 약속 되어 있는데,

내가 따로 만든 독립된 함수를 사용하고 싶다면 

### nightDayHandler(`this`); 넣어 주는 것이다.

![이미지](/programming/img/js12.PNG)

<br/><br/>

## 여기로 받는 것이다.

![이미지](/programming/img/js13.PNG)

<br/><br/>

>**Reference** <br/>생활코딩 자바스크립트 : https://www.youtube.com/watch?v=dPRtcRwKo-Y&list=PLuHgQVnccGMBB348PWRN0fREzYcYgFybf

<br/>

## 궁금증!

```java
화살표 함수와 일반 함수의 this 가 다르다
```

찍어봤다.

```javascript
const obj = {
  name: "객체",
  normal() { return this?.name; },
  arrow: () => this?.name,
};
```

<br/>

### 결과

```javascript
일반 함수의 this.name = 객체
화살표 함수의 this    = 바깥 스코프의 this
```

<br/>

일반 함수는 `부른 주체` 가 `this` 다.

`obj.normal()` 이니 `obj` 인 것이다.

<br/>

화살표 함수는 `this` 를 자기가 안 만든다.

정의된 자리의 바깥 `this` 를 그대로 쓴다.

<br/>

앞의 익명 글에서 본 자바의 익명 클래스와 람다의 차이와 똑같다.

```java
익명 클래스 - this 가 자기 자신
람다        - this 가 바깥 객체
```

<br/>

## 떼어내면 this 가 사라진다

```javascript
const detached = obj.normal;
detached();          // this.name = undefined
```

<br/>

`obj.` 를 안 붙이고 불렀으니 `this` 가 `obj` 가 아니다.

<br/>

엄격 모드에서는 `undefined` 가 되고, 아니면 전역 객체가 된다.

<br/>

콜백으로 넘길 때 이 문제가 터진다.

```javascript
button.addEventListener('click', obj.handle);      // this 가 버튼이 된다
```

<br/>

해결 방법이 셋이다.

```javascript
button.addEventListener('click', () => obj.handle());
button.addEventListener('click', obj.handle.bind(obj));
// 또는 handle 을 화살표 함수로 정의한다
```

<br/>

## 그래서 메서드에는 화살표 함수를 안 쓴다

```javascript
const obj = {
  name: "객체",
  greet: () => `안녕 ${this.name}`,      // this 가 obj 가 아니다
};
```

<br/>

객체 메서드로는 일반 함수를 쓰고,

콜백으로는 화살표 함수를 쓰는 게 규칙에 가깝다.

<br/>

## 함수도 객체다

```javascript
function f() {}
f.myProp = 1;
console.log(f.myProp);      // 1
```

<br/>

속성을 붙일 수 있다.

<br/>

```javascript
f.length      // 파라미터 개수
f.name        // 함수 이름
```

<br/>

앞의 데이터타입, 변수 글에서 본 대로

`typeof function(){}` 이 `"function"` 인데, 실제로는 객체의 한 종류다.

<br/>

그래서 함수를 변수에 담고 인자로 넘기고 반환할 수 있다.

<br/>

앞의 람다식 글에서 본 자바의 `함수를 값처럼 넘긴다` 가

자바스크립트에는 원래부터 있었던 것이다.

<br/>

## 인자 개수를 안 맞춰도 된다

```javascript
function f(a, b) { return [a, b]; }
f(1);           // [1, undefined]
f(1, 2, 3);     // [1, 2]. 3 은 무시된다
```

<br/>

오류가 안 난다.

<br/>

남는 인자는 `arguments` 로 볼 수 있다.

```javascript
function f() { return arguments.length; }
f(1, 2, 3);     // 3
```

<br/>

화살표 함수에는 `arguments` 가 없다.

`this` 와 마찬가지로 자기 것을 안 만들기 때문이다.

<br/>

요즘은 나머지 파라미터를 쓴다.

```javascript
const f = (...args) => args.length;
```

<br/>

이건 진짜 배열이라 `map` 같은 것을 바로 쓸 수 있다.

`arguments` 는 유사 배열이라 안 된다.

<br/>

## 클로저

```javascript
function counter() {
  let count = 0;
  return () => ++count;
}

const c = counter();
c();   // 1
c();   // 2
```

<br/>

`counter` 가 끝났는데도 `count` 가 살아 있다.

<br/>

반환된 함수가 그 변수를 참조하고 있으니 GC가 못 지우기 때문이다.

<br/>

앞의 GC 글에서 본 도달 가능성 판정이다.

`참조가 남아 있으면 안 지운다.`

<br/>

이걸 이용해서 비공개 상태를 만든다.

```javascript
count 를 밖에서 직접 못 바꾼다
c() 를 통해서만 늘어난다
```

<br/>

앞의 캡슐화 글에서 본 것을 클래스 없이 하는 방법인 셈이다.

<br/>

다만 참조를 계속 들고 있으니 메모리 누수의 원인이 되기도 한다.

이벤트 리스너를 안 떼면 그 안의 클로저가 통째로 안 지워지는 식이다.
