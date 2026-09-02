## 롬복

필드 주입처럼 좀 편리하게 사용하는 방법


<br/>

### 기존 설정을 추가 해보자.


![이미지](/programming/img/스프링26.PNG)

<br/><br/>


### 마지막에 코끼리 꼭 리플레쉬 해주기.

```java
dependencies {
	//lombok 라이브러리 추가 시작
	compileOnly 'org.projectlombok:lombok'
	annotationProcessor 'org.projectlombok:lombok'
	testCompileOnly 'org.projectlombok:lombok'
	testAnnotationProcessor 'org.projectlombok:lombok'
	//lombok 라이브러리 추가 끝
}
```

<br/><br/>

### 설치 해주기.

![이미지](/programming/img/스프링27.PNG)

<br/><br/>

### 체크 박스 따라 해주기.

![이미지](/programming/img/스프링28.PNG)

<br/><br/>

## 롬복 예제)

```java
package hello.core;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HelloLombok {

    private String name;
    private int age;

    public static void main(String[] args) {
        HelloLombok helloLombok = new HelloLombok();
        helloLombok.setName("김민석");

        String name = helloLombok.getName();
        System.out.println("name = " + name); // 출력: name = 김민석

        // @ToString 어너테이션  출력값이 밑에 처럼 나오는 것이다.
        System.out.println(helloLombok); // 출력: HelloLombok(name=김민석, age=0)
    }
}


// 밑에 코드들을 생략할 수 있게 되었다.
/* 
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
*/
```

<br/><br/>

### 주입 해줬던 생성자를 생략할 수 있다.

![이미지](/programming/img/스프링29.PNG)

<br/><br/>


`OrderServiceImpl` 클래스에서 `final`이 붙은 객체들을 `@RequiredArgsConstructor` 어너테이션이 생성자를 만들어 주는 것이다.

![이미지](/programming/img/스프링30.PNG)

<br/><br/>


`@NoArgsConstructor` : 매개변수가 없는 기본 생성자 어너테이션.

<br/>

`@AllArgsConstructor` : 모든 필드 값을 파라미터로 받는 생성자를 만들어줍니다.

```java
@AllArgsConstructor
public class Customer {
    private final Long id;
    private String name;
    private int age;
}

Customer customer = new Customer(2L, "김민석", 23);
```



<br/>

`@Data` : `@Getter`, `@Setter`, `@RequiredArgsConstructor`, `@ToString`, `@EqualsAndHashCode`을 한꺼번에 설정 해주는 어너테이션.

<br/><br/>


## 만약, 테스트 코드에서도 lombok을 사용하고 싶다면

build.gradle 파일에 추가해야 한다.

```java
//테스트에서 lombok 사용
testCompileOnly 'org.projectlombok:lombok'
testAnnotationProcessor 'org.projectlombok:lombok'
```






<br/><br/>

>**Reference** <br/>[스프링 핵심 원리 - 기본편](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8?utm_source=google&utm_medium=cpc&utm_campaign=04.general_backend&utm_content=spring&utm_term=%EC%8A%A4%ED%94%84%EB%A7%81%20%EC%9E%85%EB%AC%B8&gclid=CjwKCAiAjPyfBhBMEiwAB2CCImohok2YrQ2tRdhqfr3cZvKqkIJOHUJ36u6s1-7C9X1gzZIapTvOtxoCangQAvD_BwE)

<br/>

## 궁금증!

```java
롬복이 만들어낸 코드를 실제로 볼 수 있나
```

`delombok` 이라는 기능이 있다. 어노테이션을 실제 코드로 풀어준다.

```java
@Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode
public class Member {
    private final Long id;
    private String name;
}
```

<br/>

이걸 풀어보면 이렇게 나온다.

```java
public class Member {
    private final Long id;
    private String name;

    public Member(final Long id) { this.id = id; }

    public Long getId() { return this.id; }
    public String getName() { return this.name; }
    public void setName(final String name) { this.name = name; }

    public String toString() { return "Member(id=" + this.getId() + ", name=" + this.getName() + ")"; }

    public boolean equals(final Object o) { ... }
    public int hashCode() { ... }
}
```

<br/>

`@RequiredArgsConstructor` 가 `final` 필드만 골라서 생성자를 만든 것이 보인다.

`name` 은 `final` 이 아니라 생성자에 안 들어갔다.

<br/>

앞의 생성자 주입을 선택 글에서 본 그 동작이 이래서 되는 것이다.

<br/>

## 어떻게 컴파일 중에 코드를 넣나

애노테이션 프로세서를 쓴다.

```java
javac 가 소스를 읽고 AST(구문 트리)를 만든다
  -> 애노테이션 프로세서가 호출된다
  -> 롬복이 그 AST 를 직접 고친다
  -> 고쳐진 트리로 바이트코드를 만든다
```

<br/>

원래 애노테이션 프로세서는 `새 파일을 만드는 것` 까지만 표준으로 허용한다.

기존 트리를 고치는 것은 표준이 아니다.

<br/>

롬복은 컴파일러 내부 API를 써서 그걸 한다.

그래서 자바 버전이 크게 바뀔 때마다 롬복이 깨지는 일이 생기는 것이다.

<br/>

앞의 어노테이션(Annotation) 동작 원리 글에서 본 세 가지 유지 정책 중,

롬복은 `SOURCE` 다. 클래스 파일에 어노테이션 자체는 안 남는다.

```java
SOURCE  - 컴파일하면 사라진다. 롬복
CLASS   - 클래스 파일엔 있지만 런타임에 못 읽는다
RUNTIME - 런타임에 읽을 수 있다. 스프링 어노테이션 대부분
```

<br/>

## @Data 를 쓰지 말라는 이유

`@Data` 는 이걸 다 붙인 것이다.

```java
@Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode
```

<br/>

문제는 `@Setter` 다.

<br/>

엔티티에 `setter` 가 열려 있으면 아무 데서나 상태를 바꿀 수 있다.

```java
member.setName("변경");        // 어디서 바꿨는지 추적이 안 된다
```

<br/>

앞의 캡슐화 글에서 본 그 문제다.

의미 있는 메서드로 바꾸는 게 낫다.

```java
member.changeName("변경");     // 이름으로 의도가 드러난다
```

<br/>

## @ToString 이 위험한 경우

연관관계가 있는 엔티티에서 무한 루프가 난다.

```java
@ToString
class Member {
    private Team team;
}

@ToString
class Team {
    private List<Member> members;
}
```

<br/>

`Member.toString()` 이 `Team.toString()` 을 부르고,

그게 다시 `Member.toString()` 을 부른다.

```java
StackOverflowError
```

<br/>

앞의 양방향 연관관계 글에서 본 그 구조에서 바로 터진다.

<br/>

지연 로딩이면 더 골치 아프다.

`toString()` 이 프록시를 건드려서 쿼리가 나가버린다.

로그 한 줄 찍으려다 쿼리가 수십 개 나가는 것이다.

<br/>

그래서 연관 필드는 빼야 한다.

```java
@ToString(exclude = "team")
```

<br/>

## @EqualsAndHashCode 도 조심해야 한다

기본값이 `모든 필드로 비교` 다.

<br/>

앞의 equals(), hashCode() 글에서 본 대로,

엔티티는 식별자로만 비교하는 게 맞다.

```java
@EqualsAndHashCode(of = "id")
```

<br/>

모든 필드로 비교하면 `Set` 에 넣어둔 객체의 필드를 바꿨을 때

`contains` 가 `false` 를 돌려주는 문제가 생긴다.

해시가 바뀌어서 엉뚱한 버킷을 찾기 때문이다.

<br/>

## 그래서 실무에서는 이 정도만 쓴다

```java
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member { ... }
```

<br/>

```java
@Getter                    - 조회는 열어둔다
@RequiredArgsConstructor   - 스프링 빈의 생성자 주입에
@Builder                   - 필드가 많은 객체를 만들 때
@Slf4j                     - 로거
```

<br/>

`@Setter`, `@Data`, `@ToString`, `@EqualsAndHashCode` 는 안 쓰거나 옵션을 붙여서 쓴다.

<br/>

`@NoArgsConstructor(access = PROTECTED)` 는 JPA 때문이다.

앞의 엔티티 매핑 글에서 본 대로 JPA는 기본 생성자가 필요한데,

`public` 으로 열어두면 아무나 빈 객체를 만들 수 있어서 `protected` 로 막는 것이다.

<br/>

## canEqual 이라는 메서드가 같이 생긴다

`delombok` 으로 풀어보면 안 시킨 메서드가 하나 더 나온다.

```java
protected boolean canEqual(final java.lang.Object other) {
    return other instanceof Member;
}
```

<br/>

`equals` 안에서 이걸 부른다.

```java
if (!(o instanceof Member)) return false;
final Member other = (Member) o;
if (!other.canEqual((java.lang.Object) this)) return false;
```

<br/>

`나도 너를 같다고 볼 수 있냐` 를 상대에게 되묻는 것이다.

<br/>

상속이 있을 때 필요하다.

```java
class Member { }
class VipMember extends Member { }
```

<br/>

`member.equals(vip)` 는 `instanceof` 만으로는 `true` 가 되어버린다.

`VipMember` 도 `Member` 이기 때문이다.

<br/>

그런데 반대로 `vip.equals(member)` 는 `false` 다.

`Member` 는 `VipMember` 가 아니기 때문이다.

<br/>

앞의 equals(), hashCode() 글에서 본 대칭성이 깨지는 것이다.

```java
a.equals(b) 가 true 면 b.equals(a) 도 true 여야 한다
```

<br/>

`canEqual` 은 자식 쪽에서 오버라이딩되어 `false` 를 돌려주므로,

양쪽 다 `false` 가 되어 대칭성이 지켜진다.

<br/>

`hashCode` 도 특이하다.

```java
final int PRIME = 59;
int result = 1;
result = result * PRIME + ($id == null ? 43 : $id.hashCode());
```

<br/>

`59` 와 `43` 이라는 소수를 쓴다.

앞의 해시(Hash) 글에서 본 대로 소수를 곱하면 값이 골고루 퍼지기 때문이다.

<br/>

자바 표준의 `Objects.hash()` 는 `31` 을 쓴다.

숫자는 다르지만 소수를 쓴다는 발상은 같다.
