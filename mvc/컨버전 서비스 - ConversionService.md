## 컨버전 서비스 - ConversionService

이렇게 타입 컨버터를 하나하나 직접 찾아서 타입 변환에 사용하는 것은 매우 불편하다. 

그래서 스프링은 개별 컨버터를 모아두고 그것들을 묶어서 편리하게 사용할 수 있는 기능을 

제공하는데, 이것이 바로 컨버전 서비스( ConversionService )이다

<br/>

### ConversionService 인터페이스

버전 서비스 인터페이스는 단순히 컨버팅이 가능한가? 확인하는 기능과, 컨버팅 기능을 제공한다.

<br/>

### 사용 예를 확인해보자.

ConversionServiceTest - 컨버전 서비스 테스트 코드

```java
package hello.typeconverter.converter;

public class ConversionServiceTest {
    
    @Test
    void conversionService() {

        //등록
        DefaultConversionService conversionService = new DefaultConversionService();

        conversionService.addConverter(new StringToIntegerConverter());
        conversionService.addConverter(new IntegerToStringConverter());
        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());

        //사용
        assertThat(conversionService.convert("10", Integer.class)).isEqualTo(10);

        assertThat(conversionService.convert(10, String.class)).isEqualTo("10");

        IpPort ipPort = conversionService.convert("127.0.0.1:8080", IpPort.class);
        assertThat(ipPort).isEqualTo(new IpPort("127.0.0.1", 8080));

        String ipPortString = conversionService.convert(new IpPort("127.0.0.1", 8080), String.class);
        assertThat(ipPortString).isEqualTo("127.0.0.1:8080");
    }
}
```

`DefaultConversionService` 는 `ConversionService` 인터페이스를 구현했는데, 추가로 컨버터를

등록하는 기능도 제공한다.

<br/>

### 등록과 사용 분리

컨버터를 등록할 때는 `StringToIntegerConverter` 같은 타입 컨버터를 명확하게 알아야 한다. 

반면에 컨버터를 사용하는 입장에서는 타입 컨버터를 전혀 몰라도 된다. 

<br/>

타입 컨버터들은 모두 컨버전 서비스 내부에 숨어서 제공된다. 

따라서 타입을 변환을 원하는 사용자는 컨버전 서비스 인터페이스에만 의존하면 된다. 

물론 컨버전 서비스를 등록하는 부분과 사용하는 부분을 분리하고 의존관계 주입을 사용해야 한다.

<br/>

### 컨버전 서비스 사용

```java
Integer value = conversionService.convert("10", Integer.class)
```

<br/>

### 인터페이스 분리 원칙 - ISP(Interface Segregation Principal)

인터페이스 분리 원칙은 클라이언트가 자신이 이용하지 않는 메서드에 의존하지 않아야 한다.

`DefaultConversionService` 는 클래스 이며, 이 클래스의 위를 보면 두 인터페이스로 구현했다.

- `ConversionService` : 컨버터 사용에 초점
- `ConverterRegistry` : 컨버터 등록에 초점

<br/>

들어가보면 GenericConversionService 이런거 무시 하고 최상위 까지 올라가 보면 

![이미지](/programming/img/나67.PNG)

<br/>

이렇게 있는 걸 알 수 있다. `ConversionService` , `ConverterRegistry`  가 등록 되어 있다.

![이미지](/programming/img/나68.PNG)

<br/>

`ConversionService`  인터페이스

![이미지](/programming/img/나69.PNG)

<br/>

`ConverterRegistry` 인터페이스

![이미지](/programming/img/나70.PNG)

이렇게 인터페이스를 분리하면 컨버터를 사용하는 클라이언트와 컨버터를 등록하고 

관리하는 클라이언트의 관심사를 명확하게 분리할 수 있다. 

<br/>특히 컨버터를 사용하는 클라이언트는 ConversionService 만 의존하면 되므로, 

컨버터를 어떻게 등록하고 관리하는지는 전혀 몰라도 된다. 

<br/>결과적으로 컨버터를 사용하는 클라이언트는 꼭 필요한 메서드만 알게된다. 

이렇게 인터페이스를 분리하는 것을 ISP 라 한다

<br/>스프링은 내부에서 ConversionService 를 사용해서 타입을 변환한다. 

예를 들어서 앞서 살펴본 `@RequestParam` 같은 곳에서 이 기능을 사용해서 타입을 변환한다.

이제 컨버전 서비스를 스프링에 적용해보자

<br/>

>**Reference** <br/>스프링 MVC 2편 - 백엔드 웹 개발 활용 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2