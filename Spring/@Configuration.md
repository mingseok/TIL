## @Configuration

## 내가 ‘직접 스프링 빈을 등록 할거다’ 라고 생각하면 된다.

<br/>

이렇게 하면 스프링이 실행 될때 `@Configuration` 어너테이션을 보고 

`@Bean` 달려 있는 것들을 모두 컨테이너에 등록하게 되는 것이다.

![이미지](/programming/img/입문15.PNG)

<br/>

등록된 스프링 빈들은 해당 클래스의 생성자 주입 되는 것이다.

추가로 : `@Controller`는 안된다.


<br/><br/>


>**Reference** <br/>[스프링 핵심 원리 - 기본편](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8?utm_source=google&utm_medium=cpc&utm_campaign=04.general_backend&utm_content=spring&utm_term=%EC%8A%A4%ED%94%84%EB%A7%81%20%EC%9E%85%EB%AC%B8&gclid=CjwKCAiAjPyfBhBMEiwAB2CCImohok2YrQ2tRdhqfr3cZvKqkIJOHUJ36u6s1-7C9X1gzZIapTvOtxoCangQAvD_BwE)
