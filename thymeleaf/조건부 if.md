## 조건부 if, switch 문

타임리프의 조건식

if , unless ( if 의 반대)

<br/>

### if, unless

타임리프는 해당 조건이 맞지 않으면 태그 자체를 렌더링하지 않는다.

만약 다음 조건이 `false` 인 경우 `<span>...<span>` 부분 자체가 렌더링 되지 않고 사라진다.

`<span th:text="'미성년자'" th:if="${user.age lt 20}"></span>`

<br/>만약, ‘20’ 보다 작으면(=lt) ‘미성년자’ 가 출력 되는 것이다. → `if(10 > 20)` 참

그리고 `th:unless` 이건 `if문`에 만족하지 않으면 참 인것이다. 

즉, false면 반대로 `true`가 되는 것이다. → `if(10 >= 20)` 

<br/>그리하여 userB = 20 일때부터는 아무것도 출력되지 않는것이다.

```html
<span th:text="'미성년자'" th:if="${user.age lt 20}"></span>
<span th:text="'성인'" th:unless="${user.age ge 20}"></span>
```

<br/>

실행 시켜보면

![이미지](/programming/img/겨6.PNG)

여기서 재밌는 것은 해당 ‘줄’ 에 조건을 충족해야 출력이 되는 것이다.

아니면 해당 줄이 없는 것과 똑같다.

<br/>

## switch문

```html
<td th:switch="${user.age}">
		 <span th:case="10">10살</span>
		 <span th:case="20">20살</span>
		 <span th:case="*">기타</span>
 </td>
```

<br/>

>**Reference** <br/>스프링 MVC 2편 - 백엔드 웹 개발 활용 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2