## DB 스키마 자동 생성

- 애플리케이션 로딩 시점에 `create`문으로 `DB`를 생성을 하고 시작하게 할 수 있다.

- `DB`방언을 활용해서 `DB`에 맞는 적절한 `DDL`생성을 해준다.
    - 예를 들어, 문자 같은 경우 `‘오라클’`은 → `varchar2` `‘MYSQL’`은 `varchar`를 사용하는데,
        
        현재 내가 뭘로 설정했냐에 따라, 적절하게 생성해준다.
        
    - 또한, 밑에 코드 처럼 `id`, `name` 두개만 있었다가, 갑자기 3개가 되었다고 하더라도
        
        `private int age;`까지 추가된 쿼리를 만들어 주는 것이다. → 즉 새로 테이블을 만든다.
        
    
    ```java
    private Long id;
    private String name;
    ```


<br/><br/>

## 주의 사항

운영 장비에는 절대 `create`, `create-drop`, `update` 사용하면 안된다.

<br/><br/>

## DDL 생성 기능

제약조건을 추가 할 수 있다.

`회원 이름은 필수`, `10자 초과 X` → 이런식으로 엔티티에서도 가능하다.

![이미지](/programming/img/입문302.PNG)

`DDL`생성 기능은 `DDL`을 자동 생성할 때만 사용되고 `JPA`의 실행 로직에는 영향을 주지 않는다.



<br/><br/>

>**Reference** <br/>[자바 ORM 표준 JPA 프로그래밍 - 기본편](https://www.inflearn.com/course/ORM-JPA-Basic)


