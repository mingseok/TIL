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
