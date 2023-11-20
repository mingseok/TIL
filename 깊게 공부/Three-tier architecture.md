## Three-tier architecture / stored procedure


![이미지](/programming/img/입문449.PNG)


<br/>

## Presentation tier

- 사용자에게 보여지는 부분을 담당하는 tier

- html, javascript, css, native app 등등

<br/><br/>

## Logic tier

- 서비스와 관련된 기능과 정책 등등 비즈니스 로직을 담당하는 tier

    - e.g. 당근마켓의 비즈니스 로직

        - 회원 가입 / 탈퇴

        - 상품 리스트업 알고리즘

        - 상품 정보 업로드 기능

        - 상품 검색 기능

        - 메시지 기능

- application tier, middle tier 라고도 불린다

- java + spring, python + django 등등


<br/><br/>

## Data tier

- 데이터를 저장하고 관리하고 제공하는 역할을 하는 tier

- mysql, oracle, sql server, postgreSQL, mongoDB

    - e.g. 당근마켓의 데이터

        - 회원 정보

        - 상품 정보

        - 판매 / 구매 내역

        - 지역 정보

<br/><br/>

## Stored procedure를 사용한다는 것은?

- RDBMS에 저장되고 사용되는 절차이다.

- 주된 사용 목적은 비즈니스 로직 구현이다.

```java
"즉, Logic tier에서만이 아닌, 데이터 tier에서 비즈니스 로직이 존재할 수 있다는 것이다."
```

<br/><br/>

## stored procedure 장점

- `network traffic`을 줄여서 응답 속도를 향상시킬 수 있다.

    - 즉, network traffic이 한번만 발생 한다.

    - 이것이 가장 큰 장점이다.

    - 비즈니스 로직이 RDBMS에 있기에 가능.


<br/>

밑에 그림처럼, network traffic을 줄여서 응답 속도를 향상 시킬 수 있다는 것이다.

![이미지](/programming/img/입문450.PNG)


<br/>

### 여러 서비스에서 재사용이 가능하다.

![이미지](/programming/img/입문451.PNG)

즉, 카카오회사의 DB가 있다고 생각해보자.

- 카카오 회사의 `사용자 정보`를 가져오기 위해서

    - 카카오톡에서도 필요할 것이고,

    - 카카오페이에서도 필요할 것이고,

    - 카카오뱅크에서도 필요할 것이다.

그리하여, 같은 DB를 여러 서비스가 사용할 수 있다는 것이다.

<br/>

하지만, 비즈니스 로직이 procedure 를 통해서 RDBMS에 로직을 저장해 놓고, 

사용한다면? → 각각의 서비스들은 그 로직만 호출해주면 되는 것이다.

- 매우 편리!

<br/><br/>

## stored procedure 단점 & 실무에서 쓰기에 조심스러운 이유

- `유지 보수 비용이 커진다`

    - 개발자들이 자바의 문법만 안다고 되는게 아니라, procedure 관련 문법도 알아야 된다.

- `DB 서버를 추가하는 것은 간단한 작업이 아니다`

    - 트래픽이 늘어나면 메모리가 초과 될 수도 있다.

        - 즉, procedure 는 서버들의 비즈니스 로직을 다 가지고 있으니
            
            할 일이 너무 많아져 메모리를 초과 할 수도 있다.
            
- `예상치 못한 문제의 영향을 최소화할 수 있다`
    - transparent 부분에서 예상치 못한 버그가 있었다면, 이전 버전으로 롤백 한다.

        - Logic tier에서 소스 코드를 통해서 비즈니스 로직을 관리 했다면? →
            
            `Logic tier`에서의 서버는 한대가 아니기 때문에 
            
            4대가 있다고 가정 한다면, 4분의 1만 피해를 본다는 뜻이 된다.
            

<br/><br/>

## 캐시를 써서 응답 속도를 향상 시킬 수 있다.

비즈니스 로직을 소스 코드에 두고도 응답 속도를 향상 시킬 수 있다

![이미지](/programming/img/입문452.PNG)

1. 만약, 같은 `id`의 호출에 대해서는 1차적으로 캐시에서 확인을 한다.

2. 확인하여 캐시에 있다면 → 그 정보를 바로 return 해주는 것이다.

```java
"응답 속도를 향상 시키면 DB 부하까지도 줄일 수 있는 장점이 있다."
```

그렇기에 어플리케이션과 디비 사이에 캐시를 두는 것은 좋은 선택인 것이다!

<br/><br/>

## Procedure 정리

- Procedure로는 복잡하고 유연한 코드를 작성하기는 어렵다

- 오늘날의 프로그래밍 언어는 훨씬 다양하고 강력한 기능들을 제공한다.

- procedure는 가독성이 떨어진다

- procedure는 디버깅이 어렵다