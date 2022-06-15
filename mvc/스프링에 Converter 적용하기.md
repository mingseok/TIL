## 스프링에 Converter 적용하기

웹 애플리케이션에 Converter 를 적용해보자.

```java
package hello.typeconverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToIntegerConverter());
        registry.addConverter(new IntegerToStringConverter());
        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConverter());
    }
}
```

스프링은 내부에서 `ConversionService` 를 제공한다. 

우리는 `WebMvcConfigurer` 가 제공하는 `addFormatters()` 를 사용해서 추가하고 

싶은 컨버터를 등록하면 된다. 

<br/>

이렇게 하면 스프링은 내부에서 사용하는 `ConversionService` 에 컨버터를 추가해준다.

HelloController - 기존 코드

```java
@GetMapping("/hello-v2")
public String helloV2(@RequestParam Integer data) {
		 System.out.println("data = " + data);
		 return "ok";
}
```

<br/>

### 실행

localhost:8080/hello-v2?data=10

실행 로그

```java
StringToIntegerConverter : convert source=10
data = 10
```

`?data=10` 의 쿼리 파라미터는 문자이고 이것을 `Integer data` 로 변환하는 과정이 필요하다.

실행해보면 직접 등록한 `StringToIntegerConverter` 가 작동하는 로그를 확인할 수 있다.

<br/>

`StringToIntegerConverter` 가 동작하게 된 것이다.

```java
package hello.typeconverter.converter;

@Slf4j
public class StringToIntegerConverter implements Converter<String, Integer> {

    // implements Converter<String, Integer> 받는 것들이
    // String 은 (String source) 매개변수로 들어가게 되는 것이다.
    // Integer 는 public Integer 반환 값으로 되는 것이다.

    @Override
    public Integer convert(String source) {

        // 이렇게 하면 문자를 숫자로 변경 해주는 컨버터 한게 만든 것이다.
        log.info("convert source={}", source);
        return Integer.valueOf(source); // 스트링을 숫자로 변경 시켜준다.

    }
}
```

그런데 생각해보면 StringToIntegerConverter 를 등록하기 전에도 이 코드는 잘 수행되었다. 

그것은 스프링이 내부에서 수 많은 기본 컨버터들을 제공하기 때문이다. 

<br/>

### 컨버터를 추가하면 추가한 컨버터가 기본 컨버터 보다 높은 우선순위를 가진다.

### 이번에는 직접 정의한 타입인 IpPort 를 사용해보자.

```java
@GetMapping("/ip-port")
public String ipPort(@RequestParam IpPort ipPort) {
     System.out.println("ipPort IP = " + ipPort.getIp());
     System.out.println("ipPort PORT = " + ipPort.getPort());
     return "ok";
}
```

<br/>

실행
localhost:8080/ip-port?ipPort=127.0.0.1:8080

실행 로그

```java
StringToIpPortConverter : convert source=127.0.0.1:8080
ipPort IP = 127.0.0.1
ipPort PORT = 8080
```

`?ipPort=127.0.0.1:8080` 쿼리 스트링이 `@RequestParam IpPort ipPort` 에서 

객체 타입으로 잘 변환 된 것을 확인할 수 있다

![이미지](/programming/img/나71.PNG)

<br/>

StringToIpPortConverter 가 동작하게 된 것이다.

```java
package hello.typeconverter.converter;

@Slf4j
public class StringToIpPortConverter implements Converter<String, IpPort> {

    @Override
    public IpPort convert(String source) {

        // "127.0.0.1:8080" 이라는 문자가 들어올 것이다.
        log.info("convert source={}", source);
        String[] split = source.split(":");// ":" 이걸 통해서 잘라 주는 것이다.

        // 즉, "127.0.0.1:8080" 이걸
        // ":" 기준으로 앞쪽 127.0.0.1 은 문자로 사용 할것이고,
        // ":" 기준으로 뒤쪽 8080 은 숫자로 사용 할 것이라고 말하는 것이다.
        String ip = split[0];
        int port = Integer.parseInt(split[1]);

        return new IpPort(ip, port); // IpPort 객체 생성
    }
}
```

<br/>

### 처리 과정

`@RequestParam` 은 `@RequestParam` 을 처리하는 `ArgumentResolver` 인

`RequestParamMethodArgumentResolver` 에서 `ConversionService` 를 사용해서 타입을 변환한다. 

<br/>부모 클래스와 다양한 외부 클래스를 호출하는 등 복잡한 내부 과정을 거치기 때문에 대략 

이렇게 처리되는 것으로 이해해도 충분하다.

<br/>

>**Reference** <br/>스프링 MVC 2편 - 백엔드 웹 개발 활용 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2