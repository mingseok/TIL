## @RequiredArgsConstructor


@NotNull 어노테이션이나 final 키워드를 사용하고 있는 

속성들만으로 이루어진 생성자를 자동으로 만들어 준다.

<br/>

### 코드 주석 잘보기.

```java

@RequiredArgsConstructor // 사용시 final 이 붙은 클래스를 생성자 주입 과정을 자동 생성 해주는 것이다.
public class BasicItemController {

    private final ItemRepository itemRepository;

/*
위에 있는 @RequiredArgsConstructor 가 대신 이 과정을 해준다.

    @Autowired
    public BasicItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
*/

```
