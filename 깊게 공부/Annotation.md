## Annotation

프로그램에게 추가적인 정보를 제공해주는 메타데이터 입니다.

- 어노테이션 = 메타데이터

- 메타데이터란?

    - 컴파일 과정에서 코드를 어떻게 컴파일하고 처리할 것인지 알려주는 정보입니다.

```
### 용도는?
- 런타임시 특정 기능을 실행하도록 정보를 제공한다.
- 객체가 애플리케이션 내부에서 해야 할 역할을 정의할 수 있다.
  e.g.) `@controller`, `@repository`, `@entity`
```


<br/>

## @Target은 무엇이고 왜 쓰는가?

`@Target`의 기능은 어노테이션을 붙일 수 있는 대상을 지정하는 것입니다.

- e.g.) `@controller`의 `ElementType.TYPE`으로 설정하였다면, 해당 `Annotation`은 `클래스 선언시` 사용 가능하다는 의미가 됩니다.
    

<br/>

`ElementType` 종류는 여러가지 종류가 있고 `field`, `method`, `class` 등등 정의된 곳에 어노테이션을 넣을 수 있습니다.

- `ElementType.TYPE` : 클래스, 인터페이스, 열거타입

- `ElementType.METHOD` : 메서드 선언
- `ElementType.CONSTRUCTOR` : 생성자 선언

```
### 사용 이유는?
`@Target` 어노테이션을 사용하여 `다른 어노테이션`을 정의할 때, 원하는 유형에 대한 `제한`을 명시적으로 설정할 수 있기에 사용합니다.
 (이를 컴파일러가 검사해주는 것)
```


<br/>

## @Retention은 무엇이고 왜 쓰는가?

`@Retention`은 어노테이션이 유지 될 기간을 지정하는데 사용됩니다.

- 즉, 어노테이션이 `소스 코드`, `컴파일 된 클래스 파일`, `런타임` 중
    언제까지 유지 할지를 결정 짓도록 해줍니다.

<br/>

어노테이션에는 세 가지의 지속 시간을 결정할 수 있습니다. (이를 `유지 정책`이라 한다)

```java
"유지 정책이란?"
@Retention 어노테이션에 '유지정책' 세가지 중 하나를 선택하여 지속 시간을 결정할 수 있다.
```

<br/>

### `@Retention(RetentionPolicy.SOURCE)`

- 자바 소스 파일에 존재하는 것으로, 컴파일 이후 클래스 파일이 되면 사라집니다.
    - 즉, 클래스 파일이 되기 이전까지는 지속됩니다.

- e.g.) `@Override` 어노테이션의 유지 정책은 `SOURCE`이다.

    - 오버라이딩이 제대로 되었는지 확인하는 용도로 클래스 파일에 남길 필요 없이 `컴파일 시에만` 확인하고 사라진다.
        
    - `롬복`의 `@Getter` , `@Setter`도 있습니다.

<br/>

### `@Retention(RetentionPolicy.CLASS)`

- 클래스 파일까지는 존재하지만, 실행할 때 사용하지는 않는다.

    - 즉, 런타임이 `실행되기 전까지만` 존재한다.

        - 런타임에서 `클래스로더`가 해당 클래스를 읽어오면 사라질 것으로 추정됩니다.

- e.g.) `롬복`의 `@NotNull` 어노테이션의 유지 정책은 `CLASS`입니다.

    - 클래스 파일까지는 존재 하지만, 실행할 때는 사용되지 않습니다.

<br/>

### `@Retention(RetentionPolicy.RUNTIME)`

- 클래스 파일까지 존재하며, 실행 시 사용 됩니다.

    - 즉, 지속 시간이 가장 깁니다.

    - 스프링 프레임워크에서 많이 어노테이션 입니다.

        - e.g.) `@Component`, `@Autowired`, `@Service`, `@Repository`

```
### 사용 이유는?

`@Retention` 어노테이션은 주로 런타임에 어노테이션 정보를 활용해야 하는 경우에 사용됩니다. 

- e.g.) 스프링의 `컴포넌트 스캔`은 런타임에 클래스에 붙은 어노테이션 정보를 활용하여 `빈 등록`을 수행하므로,
        해당 어노테이션은 `RetentionPolicy.RUNTIME`으로 유지되어야 합니다.
```

<br/>

## RetentionPolicy는 무엇인가요?

`RetentionPolicy`는 Enum 상수 중 하나입니다.

`RetentionPolicy` 열거형은 다음 세 가지 값으로 정의됩니다:

```java
- RetentionPolicy.SOURCE  : 소스 코드(.java)까지 남아있는다.
- RetentionPolicy.CLASS   : 클래스 파일(.class)까지 남아있는다.
- RetentionPolicy.RUNTIME : 런타임까지 남아있는다.(=사라지지 않는다.)
```

<br/>

## 궁금증!

```java
@Retention 을 CLASS 로 두면 정말 런타임에 못 읽는 걸까?
```

두 어노테이션을 같은 클래스에 붙여놓고 리플렉션으로 읽어봤다.

```java
@Retention(RetentionPolicy.RUNTIME)
@interface KeepRuntime { }

@Retention(RetentionPolicy.CLASS)
@interface KeepClass { }

@KeepRuntime
@KeepClass
class Target1 { }
```

<br/>

```java
System.out.println(Target1.class.getAnnotation(KeepRuntime.class) != null);
System.out.println(Target1.class.getAnnotation(KeepClass.class) != null);
```

<br/>

### 결과

```java
RUNTIME 어노테이션 = true
CLASS 어노테이션    = false
```

<br/>

`CLASS` 는 `.class` 파일에는 들어 있지만 JVM이 메모리로 올릴 때 버린다.

그래서 실행 중에는 찾을 수가 없다.

```java
SOURCE  - 컴파일하면 사라진다.        (@Override, @SuppressWarnings, 롬복)
CLASS   - .class 에는 있지만 안 읽힌다. 기본값. 바이트코드를 직접 읽는 도구용
RUNTIME - 실행 중에 읽을 수 있다.      (@Controller, @Transactional, @Entity)
```

<br/>

스프링 어노테이션이 전부 `RUNTIME` 인 이유가 여기 있다.

스프링이 실행 중에 클래스를 훑으면서 `@Component` 가 붙었는지 확인해야 하기 때문이다.

<br/>

`@Override` 가 `SOURCE` 인 것도 이유가 있다. 컴파일러가 검사하고 나면 더 쓸 일이 없기 때문이다.

<br/>

## 어노테이션 자체는 아무 일도 안 한다

이게 제일 중요한 지점이다. 어노테이션은 표시일 뿐이고, 읽어서 처리하는 코드가 따로 있어야 한다.

<br/>

`@MyTest` 가 붙은 메서드만 골라 실행하는 코드를 직접 짜봤다.

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface MyTest {
    String value() default "";
}

class Target1 {
    @MyTest("첫 번째 테스트")
    public void testA() { ... }

    public void notATest() { ... }
}
```

<br/>

```java
for (Method method : Target1.class.getDeclaredMethods()) {
    MyTest test = method.getAnnotation(MyTest.class);
    if (test != null) {
        System.out.println("실행 : " + method.getName() + " (설명 = " + test.value() + ")");
        method.invoke(instance);
    }
}
```

<br/>

### 결과

```java
실행 : testA (설명 = 첫 번째 테스트)
  testA 본문 실행됨
```

<br/>

`notATest` 는 안 불렸다. 어노테이션이 없어서 건너뛴 것이다.

<br/>

JUnit이 `@Test` 를 찾아 실행하는 방식이 정확히 이것이다.

어노테이션에 마법이 있는 게 아니라, `리플렉션으로 훑어서 붙어 있으면 실행` 하는 코드가 있는 것이다.

```java
어노테이션        - "여기에 표시를 해둔다"
리플렉션 + 처리기 - "표시를 찾아서 실제로 일을 한다"
```

<br/>

## 어노테이션 안에 어노테이션을 넣을 수 있다

`@Service` 안에 `@Component` 가 들어 있다는 얘기가 이 구조다.

```java
@Retention(RetentionPolicy.RUNTIME)
@MyComponent                       // 어노테이션에 어노테이션을 붙였다
@interface MyServiceAnno { }

@MyServiceAnno
class MyService { }
```

<br/>

```java
System.out.println(MyService.class.getAnnotation(MyComponent.class) != null);
```

<br/>

### 결과

```java
false  <- 직접 붙인 게 아니라 못 찾는다

붙어 있는 것 = MyServiceAnno
  그 안에 = MyComponent 가 들어 있다
```

<br/>

`getAnnotation()` 은 직접 붙은 것만 본다.

한 단계 들어가서 찾아야 `MyComponent` 가 나온다.

<br/>

그래서 스프링은 이걸 직접 뒤지지 않고 `AnnotatedElementUtils` 라는 도우미를 쓴다.

```java
AnnotatedElementUtils.hasAnnotation(MyService.class, MyComponent.class);   // true
```

<br/>

몇 단계를 타고 들어가든 찾아준다. 이것을 `메타 어노테이션` 이라고 한다.

`@RestController` 안에 `@Controller` 가, 그 안에 다시 `@Component` 가 들어 있는 구조도 같은 방식이다.

```java
@RestController -> @Controller -> @Component
```

<br/>

## @Target 을 안 쓰면 어떻게 되는가

`@Target` 을 생략하면 거의 모든 곳에 붙일 수 있게 된다.

```java
@interface Anything { }

@Anything
class C {
    @Anything
    private String field;

    @Anything
    void method(@Anything String param) { }     // 전부 통과한다
}
```

<br/>

컴파일러가 아무것도 안 막아주니, 엉뚱한 데 붙여놓고 왜 동작을 안 하나 고민하게 된다.

원문의 `컴파일러가 검사해주는 것` 이 이 실수를 막아주는 장치인 것이다.
