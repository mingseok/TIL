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
