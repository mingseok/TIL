## 속성 변경 - th:value

```html
th:value="${[item.id](http://item.id/)}"
```

<br/>모델에 있는 item 정보를 획득하고 프로퍼티 접근법으로 출력한다. ( item.getId() )

value 속성을 th:value 속성으로 변경한다.

![이미지](/programming/img/서67.PNG)

<br/>

### 상품 수정 링크

```html
th:onclick="|location.href='@{/basic/items/{itemId}/edit(itemId=${[item.id](http://item.id/)})}'|"
```

<br/>

### 목록으로 링크

```html
th:onclick="|location.href='@{/basic/items}'|"
```

<br/>

>**Reference** <br/>스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1