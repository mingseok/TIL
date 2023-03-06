## 롬복과 최신 트랜드

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

`@AllArgsConstructor` : 모든 필드 값을 매개변수로 받는 생성자 어너테이션.

<br/>

`@Data` : `@Getter`, `@Setter`, `@RequiredArgsConstructor`, `@ToString`, `@EqualsAndHashCode`을 한꺼번에 설정 해주는 어너테이션.

<br/><br/>

>**Reference** <br/>[스프링 핵심 원리 - 기본편](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8?utm_source=google&utm_medium=cpc&utm_campaign=04.general_backend&utm_content=spring&utm_term=%EC%8A%A4%ED%94%84%EB%A7%81%20%EC%9E%85%EB%AC%B8&gclid=CjwKCAiAjPyfBhBMEiwAB2CCImohok2YrQ2tRdhqfr3cZvKqkIJOHUJ36u6s1-7C9X1gzZIapTvOtxoCangQAvD_BwE)
