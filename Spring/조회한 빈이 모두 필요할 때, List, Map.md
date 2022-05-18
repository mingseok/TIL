## 조회한 빈이 모두 필요할 때, List, Map

```java
package hello.core.autowired;

public class AllBeanTest {

    @Test
    void findAllBean() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(DiscountService.class);
    }

    static class DiscountService {
        private final Map<String, DiscountService> policyMap;
        private final List<DiscountService> policies;

        public DiscountService(Map<String, DiscountService> policyMap, 
																									List<DiscountService> policies) {
            this.policyMap = policyMap;
            this.policies = policies;
            System.out.println("policyMap = " + policyMap);
            System.out.println("policies = " + policies);
        }
    }

}
```



출력값이

policyMap = { }
policies = [ ]

이렇게 나온다. 왜냐???? 

DiscountService 클래스만 그냥 땡겨 온것이기 때문이다.

<br/><br/>

## 위에 코드를 수정 해보자.

```java
package hello.core.autowired;

public class AllBeanTest {

    @Test
    void findAllBean() {
        ApplicationContext ac = 
			new AnnotationConfigApplicationContext(AutoAppConfig.class, DiscountService.class);
    }

    static class DiscountService {
        private final Map<String, DiscountService> policyMap;
        private final List<DiscountService> policies;

        @Autowired
        public DiscountService(Map<String, DiscountService> policyMap, 
																										List<DiscountService> policies) {
            this.policyMap = policyMap;
            this.policies = policies;
            System.out.println("policyMap = " + policyMap);
            System.out.println("policies = " + policies);
        }
    }

}
```

<br/>

`new AnnotationConfigApplicationContext(AutoAppConfig.class,DiscountService.class);`



이 코드는 2가지로 나누어 이해할 수 있다.

1. `new AnnotationConfigApplicationContext()` 를 통해 스프링 컨테이너를 생성한다.
2. `AutoAppConfig.class, DiscountService.class` 를 파라미터로 넘기면서 해당 클래스들을 자동으로 스프링 빈에 모두 등록한다.
    
<br/>

### 정리하면 스프링 컨테이너를 생성하면서, 해당 컨테이너에 동시 AutoAppConfig , DiscountService 를 <br/>스프링 빈으로 자동 등록한다.



뭐 때문에???? 



`AutoAppConfig.class` 클래스를 스프링 빈에 등록 하였기 때문에.


<br/>

그리하여

### - Map 은 fixDiscountPolicy 가 ‘키’ 가 되고, hello.core.discount.FixDiscountPolicy@dfddc9a 객체가 값이 되는 것이다.

### - List 는 스프링 빈 인스턴스가 바로 오는 것이다.


<br/>

이렇게 출력 되는걸 알 수 있다.

```java
policyMap = {fixDiscountPolicy=hello.core.discount.FixDiscountPolicy@dfddc9a, rateDiscountPolicy=hello.core.discount.RateDiscountPolicy@4b9df8a}
policies = [hello.core.discount.FixDiscountPolicy@dfddc9a, hello.core.discount.RateDiscountPolicy@4b9df8a]
```

<br/><br/>

### 위에 코드를 추가 한다.

```java
package hello.core.autowired;

public class AllBeanTest {

    @Test
    void findAllBean() {
        ApplicationContext ac = 
			new AnnotationConfigApplicationContext(AutoAppConfig.class, DiscountService.class);
        
		DiscountService discountService = ac.getBean(DiscountService.class);

        Member member = new Member(1L, "userA", Grade.VIP);
        
        
        // 현재 이런식으로 저장 되어 있다.
        // policyMap = {fixDiscountPolicy=hello.core.discount.FixDiscountPolicy@6b7906b3, 
        //              rateDiscountPolicy=hello.core.discount.RateDiscountPolicy@3a1dd365}
        // policies = [hello.core.discount.FixDiscountPolicy@6b7906b3, 
        //              hello.core.discount.RateDiscountPolicy@3a1dd365]
        
        // discount(member, 10000, "fixDiscountPolicy") "fixDiscountPolicy" 설정 했기 때문이다.
        // 'discountPrice' 변수에 대입 되는 것은 fixDiscountPolicy로 할것이냐, rateDiscountPolicy를 정해서
        // 돌아오게 되는 것이다.

        // 어떻게???? discount() 메서드에서 'fixDiscountPolicy' 라는 '키'로 스프링 빈에 등록 되어 
        // 있는(=현재 스프링 빈은 policyMap)
        // fixDiscountPolicy 클래스 객체를 찾아서 반환 해준다. (자세한 설명은 맨밑에 있다.)
        // 그리하여 무조건 1천 할인 하는 곳으로 가게 되어 fixDiscountPolicy 클래스로 된 것이다.
        int discountPrice = discountService.discount(member, 10000, "fixDiscountPolicy");

        assertThat(discountService).isInstanceOf(DiscountService.class);
        assertThat(discountPrice).isEqualTo(1000);

        // 위에 설명과 동일하다.
        int rateDiscountPrice = discountService.discount(member, 20000, "rateDiscountPolicy");
        assertThat(rateDiscountPrice).isEqualTo(2000);
    }

    static class DiscountService {
        private final Map<String, DiscountPolicy> policyMap;
        private final List<DiscountPolicy> policies;

        @Autowired
        public DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policies) {
            this.policyMap = policyMap;
            this.policies = policies;

            System.out.println("policyMap = " + policyMap);
            System.out.println("policies = " + policies);
        }

        public int discount(Member member, int price, String discountCode) {

            DiscountPolicy discountPolicy = policyMap.get(discountCode);

            // 매개변수로 넘어온 해당 "fixDiscountPolicy" 는 이름이 '키' 이기 때문에 get을 이용하여
            // 해당 객체를 꺼내 주는 것이다. 즉, 스프링이 매핑 시켜주는 것이다.
            
            // 그리고 discountPolicy 변수는 아무 객체든 받을수 있으므로, 다형성을 이용한 것이다.
            
            // 그리하여 discount() 메서드로 가서 해당 원하는 할인 정책을 찾아 갈수 있게 되는 것이다.
            // 여기서 return값인 discount() 메서드는 DiscountPolicy 인터페이스에 있는 메서드이다.
            return discountPolicy.discount(member, price);
        }
    }

}
```

<br/><br/>

## 설명

현재 이런식으로 저장 되어 있다.

```java
policyMap = {fixDiscountPolicy=hello.core.discount.FixDiscountPolicy@6b7906b3, 
		         rateDiscountPolicy=hello.core.discount.RateDiscountPolicy@3a1dd365}
policies = [hello.core.discount.FixDiscountPolicy@6b7906b3, 
			       hello.core.discount.RateDiscountPolicy@3a1dd365]
```

![이미지](/programming/img/스프링33.PNG)

`'discountPrice'` 변수에 대입 되는 것은 `fixDiscountPolicy`로 할것이냐, 


`rateDiscountPolicy`를 정해서 돌아오게 되는 것이다.

<br/>

### 어떻게????

`discount()` 메서드를 호출해서 `'fixDiscountPolicy'` 라는 '키' 로 스프링 빈에 등록 되어 

있는 `fixDiscountPolicy` 클래스의 값인 객체를 반환 해준다. 

<br/>

### `discount()` 메서드 내용은 여기서부터 설명한다.

![이미지](/programming/img/스프링34.PNG)

매개변수로 넘어온 해당 "`fixDiscountPolicy`" 는 이름이 '키' 이기 때문에 get을 이용하여

해당 객체를 꺼내 주는 것이다. 즉, 스프링이 매핑 시켜주는 것이다.


<br/>
그리고 `discountPolicy`변수는 아무 객체든 받을 수 있으므로, 다형성을 이용한 것이다.

그리하여 `discount()` 메서드로 가서 해당 원하는 할인 정책을 찾아 갈 수 있게 되는 것이다.

<br/>

여기서 `discount()` 메서드는 `DiscountPolicy` 인터페이스에 있는 메서드이다.

그리하여 무조건 1000천원 할인 하는 곳으로 가게 되어 `fixDiscountPolicy` 클래스로 된 것이다.




<br/>

참고.

`fixDiscountPolicy` 는 무조건 1000천원 할인.

`rateDiscountPolicy` 는 10%로 할인

<br/>

## 로직 분석

1. `DiscountService`는 `Map`으로 모든 `DiscountPolicy` 를 주입 받는다. 

2. 이때 `fixDiscountPolicy`, `rateDiscountPolicy` 가 주입된다.
3. `discount()` 메서드로 discountCode를 통해 "`fixDiscountPolicy`"가 넘어오면 <br/>`map`에서
`fixDiscountPolicy` 스프링 빈을 찾아서 실행한다. 
4. 물론 “`rateDiscountPolicy`”가 넘어오면 `rateDiscountPolicy` 스프링 빈을 찾아서 실행한다.

<br/>

### 주입 분석

`Map<String, DiscountPolicy>` : map의 키에 스프링 빈의 이름을 넣어주고, <br/>그 값으로 `DiscountPolicy` 타입으로 조회한 모든 스프링 빈을 담아준다.

`List<DiscountPolicy>` : DiscountPolicy 타입으로 조회한 모든 스프링 빈을 담아준다.

<br/>만약 해당하는 타입의 스프링 빈이 없으면, 빈 컬렉션이나 Map을 주입한다.

<br/>참고 - 스프링 컨테이너를 생성하면서 스프링 빈 등록하기 

스프링 컨테이너는 생성자에 클래스 정보를 받는다. 

여기에 클래스 정보를 넘기면 해당 클래스가 스프링 빈으로 자동 등록된다.

<br/>


>**Reference** <br/>스프링 핵심 원리 - 기본편 : https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8
