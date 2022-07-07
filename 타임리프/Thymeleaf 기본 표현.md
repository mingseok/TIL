### 간단한 표현


## 변수 표현식:${...}

```html
<span th:text="${message}">메시지</span>
```

th: text태그 안의 텍스트를 서버에서 전달 받은 값에 따라 표현하고자 할 때 사용된다.

`<span th:text="${post.content}">message</span>` 이 경우 서버에서 post.content 라는 

변수가 있을 경우 message의 자리를 변수값으로 대체하게 된다.)


<br/><br/>


## 선택 변수 표현식:*{...}

`object=”{profileDto}` 그릇을 두고,

`th:field="*{bio}"` 를 사용하여 담는 것이다.

즉, `th:field="*{bio}"` 는 클라이언트가 작성한걸 담는 것이다.

![이미지](/programming/img/타임.PNG)

<br/><br/>

## 메시지 표현:#{...}


```html
<small class="form-text text-danger" th:if="${#fields.hasErrors('loginId')}" th:errors="*{loginId}">loginId Error</small>
```

'html 안에서 자바 코드를 실행하고 그 결과를 렌더 하겠다' 라고 생각하면 될 것이다.



<br/><br/>

## 링크 URL 표현식:@{...}

```html
<form class="needs-validation col-sm-6" action="#" th:action="@{/login}" method="post" novalidate>
```

타임리프에서 URL을 생성할 때는 @{...} 문법을 사용하면 된다.


<br/><br/>

## 조각 표현식:~{...}


