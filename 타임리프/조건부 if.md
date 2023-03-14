## 조건부 if, switch 문

<br/>

## if, unless (=if 의 반대)





```html
<span th:text="'미성년자'" th:if="${user.age lt 20}"></span>
<span th:text="'성인'" th:unless="${user.age ge 20}"></span>
```

첫번째 코드: ‘20’ 보다 작으면(=lt) ‘미성년자’ 가 출력 되는 것이다. → `if(10 < 20)` -> 참

두번째 코드: `th:unless` 이건 `if문`에 만족하지 않으면 참 인것이다. 

- 즉, false면 반대로 `true`가 되는 것이다. → `if(20, 30 >= 20)` -> 참


<br/>그리하여 조건에 만족하지 않는다면 '미성년자' , '성인' 중에서 아무것도 출력되지 않는다.


<br/><br/>

## 실행 시켜보면

![이미지](/programming/img/겨6.PNG)

여기서 재밌는 것은 해당 ‘줄’ 에 조건을 충족해야 출력이 되는 것이다.

즉, 해당 라인이 false라면, 해당 라인은 없어지는 것이다.


<br/><br/>

## switch문

```html
<td th:switch="${user.age}">
		 <span th:case="10">10살</span>
		 <span th:case="20">20살</span>
		 <span th:case="*">기타</span>
 </td>
```

<br/><br/>

>**Reference** <br/>스프링 MVC 2편 - 백엔드 웹 개발 활용 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2