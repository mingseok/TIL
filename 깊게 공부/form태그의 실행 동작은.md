## form태그의 실행 내부 동작은?

![이미지](/programming/img/입문530.PNG)

1. 이름을 `‘spring’` 이라고 작성하고 ‘등록’ 버튼을 누른다.

2. 그러면 `action` 인 → `"/members/new"` 로 이동하게 되는 것이다.

3. 그리고 `method`는 → `post` 로 등록 되어 있기 때문에 

    컨트롤러 클래스에 `@PostMapping(/members/new)` 어너테이션이 있는 곳으로 간다.

4. 다시 말해, 경로가 일치한 곳으로 `<input>`태그의

5. 입력값인 `‘spring’`이 서버로 넘어가게 되는 것이다.

```java
- "url창"에다가 엔터 치는것은 "Get매핑"이다. 주로 "조회"할때 사용한다.

- Post매핑은 "데이터를 <form>에 넣어서" 전달하는 방식. 
  보통 데이터를 "등록"할때 post를 사용한다.
```

<br/><br/>

## input 태그

```java
<input type="text" id="name" name="name" placeholder="이름을 입력하세요">
```

`name="name"` 부분이 중요하다. `"name"` 부분이 `‘키’` 이다.

그리고 `‘벨류’` 는 입력창에 입력할 수 있는 `‘민석’` 이 `‘벨류'`이다.

![이미지](/programming/img/입문531.PNG)

<br/><br/>

## 주의사항

`name`을 설정해줘야 컨트롤러로 넘어가는 것인데, 포인트는 `name`을 `DTO`의 `필드변수`명이랑 동일하게 작성해야 된다는 것이다.
<br/>

## 궁금증!

```java
"서버로 넘어간다" 고 했는데, 실제로 어떤 모양으로 넘어가는 걸까?
```

요청을 그대로 되돌려주는 작은 서버를 하나 띄워놓고 직접 보내봤다.

<br/>

### GET (주소창에 엔터)

```java
$ curl --get --data-urlencode "name=민석" --data-urlencode "age=30" http://localhost:18080/members
```

```java
GET /members?name=%eb%af%bc%ec%84%9d&age=30 HTTP/1.1
Host: localhost:18080

[본문] (없음)
```

<br/>

### POST (form 전송)

```java
$ curl -X POST http://localhost:18080/members/new --data-urlencode "name=민석" --data-urlencode "age=30"
```

```java
POST /members/new HTTP/1.1
Host: localhost:18080
Content-type: application/x-www-form-urlencoded
Content-length: 30

[본문] name=%EB%AF%BC%EC%84%9D&age=30
```

<br/>

## 여기서 세 가지가 보인다

### 첫번째) 실려가는 자리가 다르다

```java
GET  -> URL 뒤에 ?name=...&age=... 로 붙는다. 본문은 비어 있다
POST -> URL 은 깨끗하고, 값은 본문에 들어간다
```

<br/>

`GET` 은 값이 주소에 그대로 보인다.

브라우저 기록에 남고, 링크로 복사해서 남에게 보낼 수 있다. 조회에 쓰기 좋은 성질이다.

<br/>

반대로 비밀번호를 `GET` 으로 보내면 주소창과 서버 로그에 그대로 남는다.

로그인 폼이 항상 `POST` 인 이유가 이것이다.

<br/>

### 두번째) POST에는 Content-Type이 붙는다

```java
Content-type: application/x-www-form-urlencoded
```

<br/>

`form` 태그의 기본값이 이것이다.

`키=값` 을 `&` 로 이어 붙인 형식이라는 뜻인데, 보면 알 수 있듯이 쿼리 스트링과 모양이 똑같다.

<br/>

서버는 이 헤더를 보고 본문을 어떻게 읽을지 정한다.

`enctype="multipart/form-data"` 로 바꾸면 파일도 같이 보낼 수 있는 다른 형식이 된다.

```java
application/x-www-form-urlencoded  -> 기본값. 글자만 보낼 때
multipart/form-data                -> 파일 업로드가 있을 때
application/json                   -> form 이 아니라 자바스크립트로 보낼 때
```

<br/>

### 세번째) 한글은 그대로 못 간다

```java
민석 -> %EB%AF%BC%EC%84%9D
```

<br/>

`HTTP` 는 원래 영문과 기호만 다룰 수 있게 설계됐다.

한글은 UTF-8 바이트로 쪼갠 뒤 `%` 를 붙인 16진수로 바꿔서 보낸다.

<br/>

실제로 한글을 인코딩하지 않고 주소에 그대로 넣어봤더니 이렇게 나왔다.

```java
$ curl "http://localhost:18080/members?name=민석"
<h1>400 Bad Request</h1>URISyntaxException thrown
```

<br/>

브라우저는 이걸 알아서 해주기 때문에 평소에는 신경 쓸 일이 없다.

한글이 깨져 보이는 문제는 대부분 보낼 때와 읽을 때의 문자 인코딩이 안 맞아서 생긴다.

<br/>

## name이 왜 키가 되는가

원문의 `name 을 설정해줘야 넘어간다` 는 부분이 위 결과로 설명된다.

```java
<input type="text" name="name" value="민석">
<input type="text" name="age"  value="30">
```

```java
name=민석&age=30
```

<br/>

브라우저는 `form` 안의 입력 요소를 훑으면서 `name 속성=입력값` 을 이어 붙인다.

`name` 이 없는 요소는 아예 건너뛴다. 키가 없으니 보낼 방법이 없기 때문이다.

<br/>

`id` 는 자바스크립트나 CSS가 쓰는 것이라 전송과는 상관이 없다.

`id` 만 있고 `name` 이 없으면 화면에서는 멀쩡한데 서버에는 아무것도 안 넘어간다.

<br/>

## DTO 필드명과 맞춰야 하는 이유

```java
@PostMapping("/members/new")
public String create(MemberForm form) {
    // ...
}

class MemberForm {
    private String name;
    private int age;
}
```

<br/>

스프링은 본문의 `name=민석&age=30` 을 파싱한 다음,

키 이름으로 `MemberForm` 의 `setName`, `setAge` 를 찾아서 호출한다.

<br/>

키가 `username` 인데 필드가 `name` 이면 맞는 세터를 못 찾으니 그냥 넘어간다.

에러도 안 나고 값만 `null` 로 남는다. 그래서 찾기가 어렵다.

<br/>

이름을 다르게 쓰고 싶으면 `@ModelAttribute` 대신 명시적으로 받으면 된다.

```java
public String create(@RequestParam("username") String name) { ... }
```

<br/>

`age` 가 `int` 인데 사용자가 글자를 넣으면 변환에 실패한다.

이때 나는 예외가 `TypeMismatchException` 이고, 검증 얘기가 여기서부터 시작된다.
