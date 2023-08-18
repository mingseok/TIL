## Spring Data JPA 설명

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
}
```

- `<>` : 안에 들어갈 것은 -> `<엔티티, 엔티티의 PK 타입>`을 말한다

<br/>

## 인터페이스만 있고 구현체가 없는데, 동작이유는?


`인터페이스`를 보고 `Spring-Data-Jpa`가 구현클래스를 만들어서 준것이다

- `@Repository` 애노테이션 생략 가능

    - 컴포넌트 스캔을 스프링 데이터 JPA가 자동으로 처리

    - JPA 예외를 스프링 예외로 변환하는 과정도 자동으로 처리


<br/><br/>

## 주요 메서드

- `save(S)` : 새로운 엔티티는 저장하고 이미 있는 엔티티는 병합한다.


- `delete(T)` : 엔티티 하나를 삭제한다. 내부에서 `EntityManager.remove()` 호출


- `findById(ID)` : 엔티티 하나를 조회한다. 내부에서 `EntityManager.find()` 호출


- `getOne(ID)` : 엔티티를 프록시로 조회한다. 내부에서 `EntityManager.getReference()` 호출


- `findAll(…)` : 모든 엔티티를 조회한다. 정렬( Sort )이나 페이징( Pageable ) 조건을 파라미터로 제공할
수 있다.




<br/><br/>

>**Reference** <br/>[실전! 스프링 데이터 JPA](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%EB%8D%B0%EC%9D%B4%ED%84%B0-JPA-%EC%8B%A4%EC%A0%84?_gl=1*1x5vsec*_ga*OTY2ODU2MjYxLjE2NzkwNjYzNDU.*_ga_85V6SRKGJV*MTY5MjMyMTczNi40MC4xLjE2OTIzNDAwNDguNTIuMC4w)

