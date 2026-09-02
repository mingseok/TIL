## API 방식(json) , @ResponseBody

@ResponseBody는 두가지 기능이 있다.

<br/>

## 첫번째. http 바디 부분에 내가 직접 넣는 방법.

view 페이지를 통해서 화면에 출력 시켜주는게 아닌, 

컨트롤러 클래스에서 내가 직접 `return` 값으로 화면에 출력 시키고 싶은 경우 

메서드에 `@ResponseBody`어너테이션을 붙여주면 된다.

<br/>

즉, return 값인 `‘helloworld'` 라는 문자열을 그대로 반환하고 싶다면, 

해당 메소드에 `@ResponseBody` 어너테이션을 추가하면 되는 것이다.

```java
@GetMapping("hello-string")
@ResponseBody
public String heeloString(@RequestParam("name") String name) {
    return "hello " + name; // 출력하면 "hello spring 이런 식으로 출력된다. 
}
```

<br/>

![이미지](/programming/img/입문5.PNG)


<br/><br/>

## 두번째. API (json)방식

일단 json은 ‘키’, ‘벨류’ 형태로 이루어진 구조이다.

<br/>

### 객체를 `@ResponseBody` 로 `return` 한다면 어떻게 될까?

코드에서의 `hello` 를 출력 시키면 어떻게 될까?

```java
@Controller
public class HelloController {

    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name);
        return hello;
    }

    // static을 사용하면 클래스 내부에서 클래스 생성가능
    static class Hello { 
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
```

<br/>

‘키’는 `Hello 클래스`의 `name` 이 되는 것이고, 벨류는 내가 입력한 `스프링!!!!`이 되는 것이다.

그리하여 json 방식이란 이런 것이다.

![이미지](/programming/img/입문6.PNG)

<br/><br/>

![이미지](/programming/img/입문7.PNG)


<br/><br/>


>**Reference** <br/>[스프링 핵심 원리 - 기본편](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8?utm_source=google&utm_medium=cpc&utm_campaign=04.general_backend&utm_content=spring&utm_term=%EC%8A%A4%ED%94%84%EB%A7%81%20%EC%9E%85%EB%AC%B8&gclid=CjwKCAiAjPyfBhBMEiwAB2CCImohok2YrQ2tRdhqfr3cZvKqkIJOHUJ36u6s1-7C9X1gzZIapTvOtxoCangQAvD_BwE)

<br/>

## 궁금증!

```java
객체가 JSON 으로 바뀌는 순간이 정확히 어디인가
```

앞의 요청 매핑 헨들러 어뎁터 글에서 본 `ReturnValueHandler` 다.

```java
컨트롤러가 List<Member> 를 반환
  -> RequestResponseBodyMethodProcessor 가 받는다 (@ResponseBody 를 보고)
  -> HttpMessageConverter 목록에서 맞는 것을 찾는다
  -> MappingJackson2HttpMessageConverter 가 JSON 으로 쓴다
  -> 응답 본문에 실린다
```

<br/>

## 컨버터를 고르는 기준이 두 가지다

```java
canWrite(반환 타입, 요청의 Accept 헤더)
```

<br/>

타입과 미디어 타입 둘 다 본다.

```java
String 반환 + Accept: text/plain        -> StringHttpMessageConverter
객체 반환   + Accept: application/json  -> MappingJackson2HttpMessageConverter
byte[] 반환                             -> ByteArrayHttpMessageConverter
```

<br/>

앞의 HTTP 헤더 - 표현 글에서 본 `콘텐츠 협상` 이 여기서 쓰인다.

클라이언트가 `무엇으로 받고 싶다` 고 말하면 서버가 맞춰주는 것이다.

<br/>

## Jackson 이 필드를 어떻게 읽나

`getter` 를 보고 읽는다. 필드 이름이 아니다.

```java
public class Member {
    private String name;

    public String getName() { return name; }
    public String getDisplayName() { return "회원 " + name; }   // 필드가 없어도
}
```

```java
{"name":"민석","displayName":"회원 민석"}
```

<br/>

`getDisplayName()` 에 대응하는 필드가 없는데도 JSON에 나온다.

`get` 으로 시작하는 메서드를 전부 속성으로 보기 때문이다.

<br/>

그래서 이런 실수가 나온다.

```java
public boolean isDeleted() { return deleted; }
```

<br/>

`boolean` 은 `is` 를 떼고 `deleted` 가 된다.

`Boolean` 이면 `isDeleted` 가 그대로 이름이 된다. 미묘하게 다르다.

<br/>

앞의 원시형과 참조형 글에서 본 그 차이가 여기까지 영향을 준다.

<br/>

## 엔티티를 그대로 반환하면 안 되는 이유

```java
@GetMapping("/api/members")
public List<Member> list() {
    return memberRepository.findAll();     // 엔티티를 그대로
}
```

<br/>

세 가지 문제가 한꺼번에 생긴다.

<br/>

```java
1. 비밀번호 같은 것까지 나간다
2. 엔티티 필드를 바꾸면 API 스펙이 조용히 바뀐다
3. 지연 로딩 프록시를 만나면 예외가 난다
```

<br/>

세 번째가 특히 자주 나온다.

앞의 프록시 - 즉시 로딩과 지연 로딩 글에서 본 그것이다.

<br/>

Jackson이 `getTeam()` 을 부르는 순간 프록시가 DB를 조회하려는데,

이미 트랜잭션이 끝나 있으면 예외가 난다.

```java
LazyInitializationException
```

<br/>

그래서 DTO로 바꿔서 내보낸다.

```java
@GetMapping("/api/members")
public List<MemberResponse> list() {
    return memberRepository.findAll().stream()
            .map(MemberResponse::from)
            .toList();
}
```

<br/>

앞의 API 개발 고급 - 지연 로딩과 조회 성능 글에서 본 그 방식이다.

<br/>

## 배열을 그냥 반환하면 확장이 막힌다

```java
[{"name":"민석"},{"name":"영한"}]
```

<br/>

여기에 총 개수를 추가하고 싶으면 응답 구조가 통째로 바뀐다.

<br/>

그래서 한 겹 감싸는 것이 보통이다.

```java
{"count":2,"data":[{"name":"민석"},{"name":"영한"}]}
```

<br/>

앞의 API 예외 처리 글에서 본 오류 응답도 마찬가지 이유로 감싼다.

성공이든 실패든 형태가 같으면 클라이언트가 처리하기 쉬워지는 것이다.

<br/>

## 응답 상태 코드는 어떻게 정하나

`@ResponseBody` 만 쓰면 항상 `200` 이다.

```java
@ResponseStatus(HttpStatus.CREATED)
@PostMapping("/api/members")
public MemberResponse create(@RequestBody MemberRequest request) { ... }
```

```java
public ResponseEntity<MemberResponse> create(...) {
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}
```

<br/>

`ResponseEntity` 를 쓰면 헤더까지 같이 정할 수 있다.

앞의 HTTP 상태 코드 글에서 본 `201` 응답의 `Location` 헤더가 그런 경우다.

```java
return ResponseEntity.created(URI.create("/api/members/" + id)).body(response);
```
