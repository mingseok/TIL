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