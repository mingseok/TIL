## lombok

막상 개발을 해보면, 대부분이 다 불변이고, 

그래서 다음과 같이 생성자에 final 키워드를 사용하게 된다.

그런데 생성자도 만들어야 하고, 주입 받은 값을 대입하는 코드도 만들어야 하고…

<br/>

### 필드 주입처럼 좀 편리하게 사용하는 방법은 없을까?

역시 개발자는 귀찮은 것은 못 참는다!

<br/>

### 다음 기본 코드를 최적화해보자.

위치

![이미지](/programming/img/스프링26.PNG)

<br/>


마지막에 코끼리 꼭 리플레쉬 해주기.

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

<br/>

설치 해주기.

![이미지](/programming/img/스프링27.PNG)

<br/>

설정에서 꼭 해주기.

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

        helloLombok.setName("하하하");

        String name = helloLombok.getName();
        System.out.println("name = " + name);

		// @toString 이용
        System.out.println("helloLombok = " + helloLombok);
    }

}

출력값.
name = 하하하
helloLombok = HelloLombok(name=하하하, age=0)

// 이런게 필요가 없는 것이다.
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

<br/>

![이미지](/programming/img/스프링29.PNG)

<br/>

## `@RequiredArgsConstructor` 롬복에 있는 것이며,

현재 `OrderServiceImpl` 클래스에 `final`이 붙은 객체들을 가지고 생성자를 만들어 주는 것이다.

![이미지](/programming/img/스프링30.PNG)

<br/>


>**Reference** <br/>스프링 핵심 원리 - 기본편 : https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8
