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
