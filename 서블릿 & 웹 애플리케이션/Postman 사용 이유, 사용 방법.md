## Postman 사용 이유, 사용 방법

포스트맨 사용 방법은 항상 스프링 실행중 일때 사용해야 한다!

<br/>

## form태그 테스트를 포스트맨으로 쉽게 할 수 있다.

![이미지](/programming/img/입문49.PNG)

<br/><br/>

콘솔창에 바로 실행 되는 걸 알 수 있다.

![이미지](/programming/img/입문50.PNG)

<br/><br/>

## 그래서 이걸 왜 쓰는거야?

Post 방법을 사용해서 데이터가 서버단으로 제대로 가는지 테스트 해보고 싶은 것이다.

<br/>

그런데 테스트 하려면 HTML 파일에 form태그를 만들어야 한다는 것이다.



- 이 작업이 너무 귀찮은 것이다.

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<form action="/request-param" method="post">
    username: <input type="text" name="username"/>
    age: <input type="text" name="age"/>
    <button type="submit">전송</button>
</form>
</body>
</html>
```

<br/><br/>

이렇게 포스트맨을 사용하면 HTML 파일을 만들지 않고, 테스트를 쉽게 할 수 있는 것이다.

![이미지](/programming/img/입문51.PNG)


<br/><br/>



## 예제)

```java
@Slf4j
public class MappingController {

    @GetMapping("/hello-basic")
    public String helloBasic() {
        log.info("helloBasic");
        return "ok";
    }
}
```

<br/><br/>

위 코드를 `포스트맨`에서 `GET`으로 실행 시키면 “ok” 가 출력 되며, 잘 실행되는 것을 알 수 있다.

![이미지](/programming/img/입문70.PNG)


<br/><br/>

## 만약 Post로 한다면?

이렇게 `405 에러`가 발생하는 것을 알 수 있다.

추가로 url 주소를 잘못 입력해서 실행하더라도 에러가 발생한다. `(url 에러는 404에러)`

![이미지](/programming/img/입문71.PNG)