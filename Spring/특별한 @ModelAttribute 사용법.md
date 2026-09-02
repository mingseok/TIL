## 특별한 @ModelAttribute 사용법

<br/>

컨트롤러에서 `model.addAttribute(...)` 를 사용하여 model 담아야 되는 중복 코드가 있다면 이렇게 작성할 수 있다. 

즉, 스프링 실행시 컨트롤러로 오면 첫번째로 하는 일이 `@ModelAttribute("regions")` 애노테이션이 붙은 메서드를 실행한다. 

따라서 미리 모델에 담아둔 것이다.

<br/>그리하여 다른 메서드에서 모델을 사용하더라도 담아 놨던 데이터를 사용할 수 있다.

```java
@Controller
@RequestMapping("/basic/items")
public class BasicItemController {

	@ModelAttribute("regions")
	public Map<String, String> regions() {
		Map<String, String> regions = new LinkedHashMap<>();
		regions.put("SEOUL", "서울");
		regions.put("BUSAN", "부산");
		regions.put("JEJU", "제주");
		return regions;
	}

    // ... 생략
}
```

`@ModelAttribute` 는 이렇게 컨트롤러에 있는 별도의 메서드에 적용할 수 있다.

<br/><br/>

## 모델에 저장 될때는 자동으로 담기게 된다.

"regions" : @ModelAttribute("regions") 이름으로 저장

regions : return regions; 이름을 저장.

```java
model.addAttribute("regions", regions); 되는 것이다.
```

<br/><br/>

## 생각할 점.

이 컨트롤러가 호출될때 마다 new LinkedHashMap<>(); 을 생성한다.

이건 메모리측에서 비효율 적이다.

그리하여 static으로 하여 공유해서 사용하는 것이 좋을 것 같다.


<br/><br/>

>**Reference** <br/>[스프링 MVC 2편 - 백엔드 웹 개발 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2/dashboard)
<br/>

## 궁금증!

```java
메서드에 붙는 @ModelAttribute 는 언제 실행되나
```

컨트롤러 안의 모든 요청보다 먼저 실행된다.

```java
@Controller
public class ItemController {

    @ModelAttribute("regions")
    public Map<String, String> regions() {
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        return regions;
    }

    @GetMapping("/items/new")
    public String form() { ... }       // 여기 들어오기 전에 regions() 가 먼저 돈다

    @PostMapping("/items/new")
    public String save() { ... }       // 여기도 마찬가지
}
```

<br/>

메서드가 돌려준 값이 `regions` 라는 이름으로 모델에 담긴다.

<br/>

## 왜 이런 게 필요한가

모든 화면에서 필요한 값이 있기 때문이다.

```java
@GetMapping("/items/new")
public String form(Model model) {
    model.addAttribute("regions", 지역목록());     // 이 줄이
    return "items/form";
}

@PostMapping("/items/new")
public String save(Model model) {
    model.addAttribute("regions", 지역목록());     // 여기도 필요하다
    return "items/form";
}
```

<br/>

검증에 실패해서 폼으로 다시 갈 때도 지역 목록이 있어야 화면이 그려진다.

빼먹으면 그때만 화면이 깨진다.

<br/>

앞의 Validation 글에서 본 그 상황이다.

<br/>

## 단점도 있다

해당 컨트롤러의 **모든** 요청에서 실행된다.

```java
@GetMapping("/items/{id}/delete")
public String delete(@PathVariable Long id) { ... }
```

<br/>

삭제할 때도 지역 목록을 만든다. 필요 없는데도 만든다.

<br/>

그 안에서 DB를 조회하면 요청마다 쿼리가 하나씩 더 나가는 셈이다.

```java
@ModelAttribute("categories")
public List<Category> categories() {
    return categoryRepository.findAll();      // 모든 요청마다 조회
}
```

<br/>

값이 잘 안 바뀌는 것이면 캐시를 얹거나 `enum` 으로 두는 편이 낫다.

<br/>

## @ControllerAdvice 에 붙일 수도 있다

```java
@ControllerAdvice
public class GlobalModelAdvice {

    @ModelAttribute("loginMember")
    public Member loginMember(HttpSession session) {
        return (Member) session.getAttribute("loginMember");
    }
}
```

<br/>

이러면 모든 컨트롤러에 적용된다.

앞의 API 예외 처리 - @ControllerAdvice 글에서 본 그 어노테이션이다.

<br/>

예외 처리 말고 이런 용도로도 쓸 수 있는 것이다.

```java
@ExceptionHandler   - 예외를 공통 처리
@ModelAttribute     - 모델 값을 공통 주입
@InitBinder         - 바인딩 설정을 공통 적용
```

<br/>

## 파라미터에 붙는 것과 헷갈리지 않기

이름은 같은데 완전히 다르다.

```java
// 파라미터에 붙으면 - 요청 값을 객체로 묶어서 받는다
public String save(@ModelAttribute ItemForm form) { ... }

// 메서드에 붙으면 - 반환값을 모델에 미리 담아둔다
@ModelAttribute("regions")
public Map<String, String> regions() { ... }
```

<br/>

앞의 HTTP 요청 파라미터 - @ModelAttribute 글에서 다룬 것이 앞의 경우다.

<br/>

같은 어노테이션이 붙는 자리에 따라 다르게 동작하는 것은 스프링에 종종 있다.

```java
@Transactional  - 클래스에 붙으면 모든 메서드, 메서드에 붙으면 그것만
@RequestMapping - 클래스에 붙으면 공통 경로, 메서드에 붙으면 개별 경로
```

<br/>

어노테이션 자체가 하는 일은 없고, 그것을 읽는 쪽이 자리를 보고 판단하는 것이라

이런 설계가 가능한 것이다.

앞의 어노테이션(Annotation) 동작 원리 글에서 본 `표시일 뿐` 이라는 성질이 여기서 드러난다.
