## CSS 선택자

### css 선택자는 크게 3가지가 있다.

- 태그 선택자
- id 선택자
- 클래스 선택자

<br/><br/>

CSS를 어디다 줄것인가. 명확히 하기 위해서 쓰는 것.

![이미지](/programming/img/js7.PNG)

<br/>

## 아무것도 붙지 않는건 태그를 뜻한다

태그 뿐만 아니라 아이디, 클래스도 가능하다.

만약 그룹명 == 클래스명 으로 한다고 과정 하면

<br/>

## class명을 사용할때는 .을 써준다.

```html
<style>
 .g1 {  <!-- '.'의 뜻은 *.g1 이다. 전체를 말한 것 이였다. 
	      그렇다면 li.g1 이렇게도 가능하단 얘기이다. 
	      풀이 하자면 *.g1은 g1안에 들어가 있는 모든 것들 이였는데
	      li.g1 이렇게 한다면 g1안에 들은 li만 찾아라 가 되는것이다. -->
	
  color: red;
  font-weight: bold;
  }
</style>

<h1 class="g1">서두</h1>
```

<br/>

## 아이디에 CSS 주는 경우는 #을 사용한다.

```html
<style>
  #name1{
  color: red;
  font-weight: bold;
  }
</style>

<h1 id="name1">서두</h1>
```

클래스, 아이디 이름을 정해 주는데 이름이 길때는 '-' 사용 한다.

<br/>

### 되도록 쓸거면 클래스를 사용하자.

이유는 중복 아이디가 나올 경우 충돌이 일어 날 수 있기에

되도록 클래스를 사용하자.

```html
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>HTML</title>
</head>

<body>

<h1 style="color: red; font-weight: bold;">서두</h1>
<!-- (:) 와 (;) 잘 구분 하자 그리고 해당 부분의 마지막은 ; 로 끝낸다. -->

</body>
</html>
```

<br/>

![이미지](/programming/img/js8.PNG)

<br/><br/>

>**Reference** <br/>생활코딩 자바스크립트 : https://www.youtube.com/watch?v=dPRtcRwKo-Y&list=PLuHgQVnccGMBB348PWRN0fREzYcYgFybf

<br/>

## 궁금증!

```java
선택자 우선순위가 어떻게 정해지나
```

점수를 매겨서 비교한다.

```java
인라인 스타일   1000 점
id             100 점
클래스, 속성, 가상 클래스   10 점
태그, 가상 요소   1 점
```

<br/>

```css
#header .nav li a       -> 100 + 10 + 1 + 1 = 112
div.container p         -> 1 + 10 + 1 = 12
```

<br/>

점수가 높은 쪽이 이긴다.

같으면 나중에 쓴 것이 이긴다.

<br/>

## 그래서 id 로 스타일을 주면 안 된다

```css
#submit { color: blue; }
.btn-danger { color: red; }
```

<br/>

`#submit` 이 100점이고 `.btn-danger` 가 10점이라

클래스를 아무리 붙여도 안 바뀐다.

<br/>

덮어쓰려면 더 구체적으로 써야 하는데,

그러다 보면 선택자가 계속 길어지는 악순환이 생긴다.

<br/>

그래서 CSS에서는 클래스만 쓰는 게 관례가 됐다.

`id` 는 자바스크립트에서 요소를 찾는 용도로만 쓴다.

<br/>

## !important 는 마지막 수단이다

```css
color: red !important;
```

<br/>

점수를 무시하고 이긴다.

<br/>

문제는 이걸 이기려면 또 `!important` 를 써야 한다는 것이다.

<br/>

앞의 예외 처리 얘기와 비슷하다.

`한 번 쓰기 시작하면 계속 쓰게 되는` 종류의 도구다.

<br/>

## 자바스크립트에서 쓰는 선택자

```javascript
document.querySelector('.item')
element.matches('.item')
element.closest('.item')
```

<br/>

셋 다 같은 문법을 쓴다.

<br/>

앞의 이벤트 글에서 본 이벤트 위임이 이걸 쓴다.

```javascript
if (e.target.matches('.delete-btn')) { ... }
```

<br/>

## 자주 쓰는 조합

```css
.a .b        후손. 몇 단계든
.a > .b      직계 자식만
.a + .b      바로 다음 형제
.a ~ .b      뒤의 모든 형제
.a.b         둘 다 가진 것
.a, .b       둘 중 하나
```

<br/>

공백 하나와 `>` 하나로 뜻이 달라진다.

<br/>

`>` 를 쓰면 범위가 좁아져서 예상치 못한 곳에 적용되는 일이 준다.

<br/>

## 속성 선택자

```css
[type="checkbox"]
[href^="https"]      앞이 일치
[href$=".pdf"]       뒤가 일치
[class*="btn"]       포함
```

<br/>

앞의 LIKE, BETWEEN 글에서 본 SQL의 `LIKE` 와 같은 발상이다.

```java
^= 는 LIKE '값%'
$= 는 LIKE '%값'
*= 는 LIKE '%값%'
```

<br/>

기호는 정규식에서 가져왔다.

<br/>

## 가상 클래스와 가상 요소

```css
:hover, :focus, :first-child, :nth-child(2n)     가상 클래스
::before, ::after, ::placeholder                가상 요소
```

<br/>

콜론 개수가 다르다.

```java
가상 클래스 - 상태. 콜론 하나
가상 요소   - 실제로 없는 요소를 만든다. 콜론 둘
```

<br/>

옛날에는 둘 다 하나였다가 구분하려고 바뀐 것이라,

`:before` 로 써도 대개 동작한다.

<br/>

## nth-child 와 nth-of-type

```css
p:nth-child(2)      두 번째 자식인데 그게 p 여야 한다
p:nth-of-type(2)    p 들 중 두 번째
```

<br/>

```html
<div>
    <h1>제목</h1>
    <p>첫 문단</p>
</div>
```

<br/>

`p:nth-child(2)` 는 `첫 문단` 을 잡는다. 두 번째 자식이 `p` 이기 때문이다.

`p:nth-child(1)` 은 아무것도 안 잡는다. 첫 번째 자식이 `h1` 이라서다.

<br/>

이게 헷갈려서 안 되는 줄 알고 헤매는 경우가 많다.

<br/>

앞의 반복 글에서 본 타임리프의 줄무늬 처리를

CSS로 하면 이걸 쓴다.

```css
tr:nth-child(odd) { background: #f5f5f5; }
```

<br/>

서버에서 계산해서 클래스를 붙이는 것보다 이쪽이 낫다.

화면 꾸미기는 화면에서 하는 것이다.
