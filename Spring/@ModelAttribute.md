## @ModelAttribute

흔히 생각 하는 그 ModelAttribute 가 아니다.

<br/>

컨트롤러에 메서드를 만들어 준다.

```java

public class FormItemController {

    @ModelAttribute("regions")
    public Map<String, String> regions() {

        // 링크드 해쉬맵을 쓰는이유? 순서 보장하기 위해
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");
        
        return regions;
    }

```

<br/><br/>

이렇게 한다면,

### 다른 곳에서 Model model 에 "regions" 가 담겨져 가는 것이다. 


이렇게 함으로써 반복 되는 코드를 줄일 수가 있다.

```java

    @GetMapping
    public String items(Model model) {
        
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        
        return "form/items";
    }


    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
       
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "form/item";
    }
```

<br/>

## 정리

@ModelAttribute 어너테이션을 사용한다면

항상 어떤 컨트롤러의 메서드든 모델에 담겨져 있는 것이다.

<br/>

### 코드로 보면 

model 이 있는 모든 메서드에 이렇게 들어가게 되는 것이다.

```java
model.addAttribute("regions", regions);
```