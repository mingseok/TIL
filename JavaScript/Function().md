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
