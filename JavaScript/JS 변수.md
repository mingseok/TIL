## JS 변수

“egoing” 이 출력 되는 것이다.

![이미지](/programming/img/js14.PNG)

<br/>

### 방법.

![이미지](/programming/img/js15.PNG)

<br/><br/>

>**Reference** <br/>생활코딩 자바스크립트 : https://www.youtube.com/watch?v=dPRtcRwKo-Y&list=PLuHgQVnccGMBB348PWRN0fREzYcYgFybf
<br/>

## 궁금증!

```java
var 와 let 이 반복문에서 다르게 동작한다
```

함수를 세 개 만들어서 나중에 불러봤다.

```javascript
var fns = [];
for (var i = 0; i < 3; i++) fns.push(() => i);
console.log(fns.map(f => f()));

let fns2 = [];
for (let j = 0; j < 3; j++) fns2.push(() => j);
console.log(fns2.map(f => f()));
```

<br/>

### 결과

```javascript
var 로 만든 함수 3개 = [ 3, 3, 3 ]
let 로 만든 함수 3개 = [ 0, 1, 2 ]
```

<br/>

`var` 는 셋 다 `3` 이다.

<br/>

## 왜 이렇게 되나

`var` 는 함수 단위 스코프다.

반복문이 끝나도 `i` 는 하나뿐이고, 세 함수가 그 하나를 같이 본다.

<br/>

반복이 끝난 뒤의 값이 `3` 이니 셋 다 `3` 이 나오는 것이다.

<br/>

`let` 은 블록 단위다.

반복마다 `j` 가 새로 만들어져서 각자 자기 것을 본다.

<br/>

앞의 익명 글에서 본 자바의 캡처와 대비된다.

```java
자바 - 값을 복사해서 들고 간다. 그래서 final 이어야 한다
JS var - 변수 자체를 공유한다
JS let - 반복마다 새 변수가 생긴다
```

<br/>

자바가 `final` 을 강제한 이유가 이 혼란을 막으려는 것이었던 셈이다.

<br/>

## var 시절의 우회법

```javascript
for (var i = 0; i < 3; i++) {
    (function(k) {
        fns.push(() => k);
    })(i);
}
```

<br/>

즉시 실행 함수로 감싸서 함수 스코프를 만드는 방식이다.

<br/>

`let` 이 나오면서 이 패턴이 사라졌다.

<br/>

## 호이스팅

```javascript
console.log(a);      // undefined
var a = 1;

console.log(b);      // ReferenceError
let b = 1;
```

<br/>

`var` 는 선언이 위로 끌어올려지고 `undefined` 로 초기화된다.

<br/>

`let` 도 끌어올려지긴 하는데 초기화가 안 된다.

선언 전까지는 접근이 막힌다.

<br/>

이 구간을 `일시적 사각지대` 라고 부른다.

<br/>

오류가 나는 게 나은 것이다.

`undefined` 로 조용히 넘어가면 나중에 엉뚱한 곳에서 터지기 때문이다.

<br/>

앞의 스프링 컨테이너 글에서 본 태도와 같다.

`애매한 것은 조용히 넘어가지 말고 일찍 터뜨린다.`

<br/>

## const 는 재할당만 막는다

```javascript
const arr = [1, 2];
arr.push(3);         // 된다
arr = [];            // TypeError
```

<br/>

앞의 final 키워드 글에서 본 자바의 `final` 과 같다.

참조를 못 바꾸는 것이지 안의 내용을 못 바꾸는 게 아니다.

<br/>

내용까지 막으려면 따로 해야 한다.

```javascript
Object.freeze(arr);
```

<br/>

이것도 한 겹만 얼린다. 중첩된 객체는 안 얼린다.

<br/>

앞의 방어적 복사 글에서 본 얕은 복사와 같은 문제다.

<br/>

## 지금은 const 를 기본으로 쓴다

```javascript
const 를 기본으로
바꿔야 하면 let
var 는 안 쓴다
```

<br/>

`const` 로 쓰면 `이 변수는 안 바뀐다` 는 게 코드에 드러난다.

<br/>

읽는 사람이 추적할 것이 줄어드는 것이다.

앞의 생성자 주입을 선택 글에서 본 `final` 의 이득과 같다.
