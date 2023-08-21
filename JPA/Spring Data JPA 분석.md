## Spring Data JPA 분석

<br/>

### `@Repository` 적용: JPA 예외를 스프링이 추상화한 예외로 변환

<br/>

## @Transactional 트랜잭션 적용

- JPA의 모든 변경은 트랜잭션 안에서 동작

- 스프링 데이터 JPA는 변경`(등록, 수정, 삭제)` 메서드를 트랜잭션 처리

- 서비스 계층에서 트랜잭션을 시작하지 않으면 리파지토리에서 트랜잭션 시작

- 서비스 계층에서 트랜잭션을 시작하면 리파지토리는 해당 트랜잭션을 전파 받아서 사용

- 그래서 스프링 데이터 JPA를 사용할 때 트랜잭션이 없어도 데이터 등록, 변경이 가능했음

    (사실은 트랜
잭션이 리포지토리 계층에 걸려있는 것임)

<br/><br/>

## @Transactional(readOnly = true)

- 데이터를 단순히 조회만 하고 변경하지 않는
 트랜잭션에서 `readOnly = true` 옵션을
 
  사용하면 `플러시`를 생략해서 약간의 성능 향상을 얻을 수 있음


<br/><br/>


## 매우 중요!! -> save() 메서드

- 새로운 엔티티면 저장( `persist` )

- 새로운 엔티티가 아니면 병합( `merge` )


<br/><br/>

>**Reference** <br/>[실전! 스프링 데이터 JPA](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%EB%8D%B0%EC%9D%B4%ED%84%B0-JPA-%EC%8B%A4%EC%A0%84?_gl=1*1x5vsec*_ga*OTY2ODU2MjYxLjE2NzkwNjYzNDU.*_ga_85V6SRKGJV*MTY5MjMyMTczNi40MC4xLjE2OTIzNDAwNDguNTIuMC4w)

