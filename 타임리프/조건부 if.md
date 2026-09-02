## 조건부 if, switch 문

<br/>

## if, unless (=if 의 반대)





```html
<span th:text="'미성년자'" th:if="${user.age lt 20}"></span>
<span th:text="'성인'" th:unless="${user.age ge 20}"></span>
```

첫번째 코드: ‘20’ 보다 작으면(=lt) ‘미성년자’ 가 출력 되는 것이다. → `if(10 < 20)` -> 참

<br/>

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

<br/>

## 궁금증!

```java
th:if 는 태그를 숨기는 건가 안 만드는 건가
```

아예 안 만든다.

```html
<span th:if="${false}">안 보임</span>
```

<br/>

응답 HTML에 이 `span` 이 없다.

CSS로 `display: none` 을 준 것과 완전히 다르다.

```html
<span style="display:none">안 보임</span>      <!-- HTML 에는 있다 -->
```

<br/>

숨긴 것은 개발자 도구로 보면 내용이 보인다.

<br/>

그래서 권한에 따라 감춰야 하는 것은 `th:if` 를 써야 한다.

```html
<div th:if="${isAdmin}">
    <button>회원 삭제</button>
</div>
```

<br/>

CSS로 숨기면 버튼은 안 보여도 그 안의 정보는 소스에 남는다.

<br/>

## 그래도 서버 쪽 검사가 따로 필요하다

화면에서 버튼을 안 보여준 것과, 그 기능을 못 쓰게 막은 것은 다른 얘기다.

```java
@DeleteMapping("/members/{id}")
@PreAuthorize("hasRole('ADMIN')")           // 이게 있어야 한다
public void delete(@PathVariable Long id) { ... }
```

<br/>

버튼이 안 보여도 요청은 직접 보낼 수 있기 때문이다.

앞의 Postman 사용 이유 글에서 본 도구로 그냥 보내면 된다.

<br/>

`화면에서 안 보이게 하는 것` 은 편의고, `막는 것` 은 서버의 일이다.

<br/>

## unless 가 있는 이유

```html
<span th:if="${member.age >= 20}">성인</span>
<span th:unless="${member.age >= 20}">미성년자</span>
```

<br/>

`else` 가 없어서 조건을 뒤집어 쓰는 방식이다.

<br/>

`th:if="${not (...)}"` 로 쓸 수도 있는데 `unless` 가 읽기 낫다.

<br/>

문제는 조건이 두 곳에 적힌다는 점이다.

한쪽만 고치면 둘 다 나오거나 둘 다 안 나온다.

<br/>

그래서 조건이 복잡하면 `switch` 를 쓰거나 값을 미리 계산해서 넘긴다.

<br/>

## th:switch 가 있다

```html
<div th:switch="${member.grade}">
    <span th:case="'GOLD'">골드 회원</span>
    <span th:case="'SILVER'">실버 회원</span>
    <span th:case="*">일반 회원</span>
</div>
```

<br/>

`*` 이 `default` 다.

<br/>

앞의 enum 글에서 본 대로 `enum` 을 쓰면 이렇게도 된다.

```html
<span th:case="${T(com.example.Grade).GOLD}">
```

<br/>

길어서 문자열로 비교하는 경우가 많은데,

그러면 `enum` 이름을 바꿨을 때 화면이 조용히 안 맞게 된다.

<br/>

## 조건에 쓰이는 값의 판정 규칙

`boolean` 이 아니어도 조건이 된다.

```java
null        ->  false
0           ->  false
"false"     ->  false  (문자열인데도)
"off", "no" ->  false
그 외        ->  true
빈 문자열 "" ->  true   (이게 함정이다)
```

<br/>

빈 문자열이 `true` 다.

```html
<span th:if="${name}">이름이 있습니다</span>
```

<br/>

`name` 이 `""` 면 이 문구가 나온다.

<br/>

앞의 검색 조건, NULL 값 글에서 본 것과 같은 종류의 함정이다.

`값이 없다` 를 무엇으로 볼지가 층마다 다른 것이다.

<br/>

명확하게 쓰는 게 낫다.

```html
<span th:if="${not #strings.isEmpty(name)}">
```

<br/>

## th:if 와 th:each 를 같이 쓰면

```html
<tr th:each="m : ${members}" th:if="${m.age >= 20}">
```

<br/>

우선순위가 정해져 있다.

```java
1. th:each
2. th:if / th:unless / th:switch
3. th:object / th:with
4. th:attr / th:value 등
5. th:text / th:utext
```

<br/>

`th:each` 가 먼저라, 반복하면서 조건에 맞는 것만 그린다.

의도한 대로 동작하는 것이다.

<br/>

그래도 화면에서 거르는 것보다 컨트롤러에서 걸러 넘기는 게 낫다.

`몇 건이 나왔는지` 를 화면에서는 세기 어렵기 때문이다.
