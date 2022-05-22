## 속성 변경 - th:action

`th:action` 이렇게만 있을 경우는, 그 앞에 action의 경로도 같이 비어 버리는 것이다. 

<br/>그러면, 아무것도 값이 없게 되면 그냥 현재 url 에다가 데이터를 넘기게 되는 것이다.

즉, `th:action="@{/basic/items/add}` 이렇다는 것이다.


```html
th:action
```

- HTML form에서 action 에 값이 없으면 현재 URL에 데이터를 전송한다.

- 상품 등록 폼의 URL과 실제 상품 등록을 처리하는 URL을 똑같이 맞추고 HTTP 메서드로 
두 기능을 구분한다.
    - 상품 등록 폼: `GET /basic/items/add`

    - 상품 등록 처리: `POST /basic/items/add`
- 이렇게 하면 하나의 URL로 등록 폼과, 등록 처리를 깔끔하게 처리할 수 있다.




### 취소

취소시 상품 목록으로 이동한다.

```html
th:onclick="|location.href='@{/basic/items}'|"
```

<br/>

>**Reference** <br/>스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1