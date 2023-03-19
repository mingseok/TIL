## 특별한 @ModelAttribute 사용법

<br/>

컨트롤러에서 `model.addAttribute(...)` 를 사용하여 담아야 되는 중복 코드가 있다면 이렇게 작성할 수 있다. 

즉, 컨트롤러 실행시 처음에 메서드에 붙은 @ModelAttribute("regions") 을 확인하고 모델에 담아둔다. 

<br/>그리하여 다른 메서드에서 모델을 사용하더라도 담아 놨던 데이터를 사용할 수 있다.

```java
@Controller
@RequestMapping("/basic/items")
public class BasicItemController {

    private final ItemRepository itemRepository;

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

@ModelAttribute 는 이렇게 컨트롤러에 있는 별도의 메서드에 적용할 수 있다.

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