## 리플렉션(Reflection)

<br/>

## 실행 중에 클래스를 들여다본다

```java
Class<?> c = obj.getClass();
c.getDeclaredFields();       // 필드 목록
c.getDeclaredMethods();      // 메서드 목록
c.getAnnotations();          // 어노테이션
```

<br/>

컴파일할 때는 모르는 클래스를 실행 중에 조사하고 부를 수 있다.

앞의 자바는 어떻게 실행되나요 글에서 본 대로 `.class` 파일에 이 정보가 전부 들어 있어서 가능하다.

<br/><br/>

## 궁금증!

```java
private 도 열리나
```

```java
Field f = Refl.class.getDeclaredField("secret");
f.setAccessible(true);
f.get(r);
```

<br/>

### 결과

```java
private 필드 읽기 = 42, 99 로 바꾼 뒤 = 99
private 메서드 호출 = 100
```

<br/>

읽고 쓰고 부를 수 있다.

<br/>

`private` 은 컴파일러가 지키는 규칙이다. 런타임에 `setAccessible(true)` 로 풀린다.

앞의 싱글톤 패턴 (Singleton Pattern) 글에서 본 대로 `private` 생성자로 싱글톤을 지킬 수 없는 이유가 이것이다.

<br/>

자바 9 모듈부터는 제한이 생겼다.

```java
InaccessibleObjectException: module java.base does not "opens java.lang" to unnamed module
```

<br/>

JDK 내부 클래스의 `private` 은 기본으로 안 열린다. 앞의 ThreadLocal 내부 구조 글에서 `--add-opens` 를 줘야 했던 이유다.

내가 만든 클래스는 여전히 열린다.

<br/><br/>

## 비용

직접 호출과 비교했다. 1억 번이다.

```java
직접 호출        =  60 ms
Method.invoke   = 627 ms    <- 10 배
MethodHandle    = 241 ms    <- 4 배
```

<br/>

`Method.invoke` 가 느린 이유가 있다.

```java
인자를 Object[] 배열로 감싼다        - 할당
int 를 Integer 로 박싱한다          - 앞의 오토박싱 & 언박싱 글
접근 권한을 매번 검사한다
반환값을 언박싱한다
```

<br/>

`MethodHandle` 은 자바 7에 들어온 더 가벼운 방식이다. 타입을 미리 확정해서 박싱과 검사를 줄인다.

앞의 람다식 글에서 본 `invokedynamic` 이 이걸 쓴다.

<br/><br/>

## 그래서 스프링은 어떻게 쓰나

스프링은 리플렉션 없이 돌아가지 않는다.

```java
@Autowired 필드 주입     - Field.setAccessible(true) + set(). private 필드에 넣는 방법
@RequestMapping 찾기    - getDeclaredMethods() 로 전부 훑고 getAnnotation()
@Transactional 프록시   - 인터페이스나 클래스 구조를 읽어서 프록시 생성
Jackson 역직렬화        - 기본 생성자 newInstance() + setter 나 필드 set
JPA 엔티티             - 필드에 직접 값을 넣는다. setter 가 없어도 되는 이유
```

<br/>

그런데 위에서 본 대로 10배 느린데 요청마다 하면 어떻게 되나.

<br/>

안 한다. 기동할 때 한 번 훑고 결과를 캐시한다.

```java
기동: 모든 컨트롤러의 메서드를 리플렉션으로 읽어서 -> 매핑 테이블에 저장
요청: 테이블에서 찾아서 -> Method.invoke (이건 매번)
```

<br/>

`invoke` 는 요청마다 하지만 `찾는 것` 은 안 한다. 그리고 `invoke` 한 번은 수백 ns라 HTTP 처리 전체에 비하면 무시할 수준이다.

<br/>

앞의 스프링 컨테이너, 스프링 빈, BeanDefinition 글에서 본 `BeanDefinition` 이 그 캐시다.

기동이 느린 이유 중 하나가 이 훑는 작업이다.

<br/><br/>

## 리플렉션이 깨는 것들

```java
컴파일 검사   - 메서드 이름을 문자열로 부르니 오타가 실행 시점에 터진다
리팩토링     - IDE 가 이름 변경을 추적 못 한다
인라인 최적화 - JIT 가 invoke 너머를 못 본다
네이티브 컴파일 - GraalVM 은 실행 전에 무엇을 부를지 알아야 한다
```

<br/>

마지막이 요즘 문제다.

앞의 자바 가상머신(JVM) 글에서 본 GraalVM 네이티브 이미지는 `실행 중에 무엇을 부를지` 를 빌드할 때 알아야 한다.

리플렉션은 그걸 실행 중에 정하니 충돌한다.

<br/>

스프링 부트 3의 AOT가 그 답이다. 빌드할 때 리플렉션 대상을 미리 찾아 목록으로 만든다.

<br/><br/>

## 어노테이션과의 관계

```java
@Retention(RetentionPolicy.RUNTIME)
```

<br/>

앞의 lombok 글에서 본 세 가지 유지 정책 중 `RUNTIME` 이어야 리플렉션으로 읽을 수 있다.

`SOURCE` 나 `CLASS` 면 `getAnnotation()` 이 `null` 을 돌려준다.

<br/>

스프링 어노테이션이 전부 `RUNTIME` 인 이유다. 실행 중에 읽어야 하니까.

롬복은 `SOURCE` 다. 컴파일할 때 코드를 만들고 사라진다. 실행 중에는 롬복 어노테이션이 없다.

<br/><br/>

## 정리

```java
할 수 있는 것  - 클래스 구조 조사, private 접근, 문자열로 메서드 호출
비용         - 직접 호출의 10 배. MethodHandle 은 4 배
스프링       - 기동 때 한 번 훑고 캐시. 요청마다 훑지 않는다
대가         - 컴파일 검사와 리팩토링 안전성을 잃는다. 네이티브 컴파일과 충돌
```

<br/>

프레임워크를 만드는 게 아니면 직접 쓸 일이 드물다.

테스트에서 `private` 필드를 확인하려고 쓰는 정도인데, 그것도 설계를 다시 보라는 신호인 경우가 많다.
