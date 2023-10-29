## logical vs conceptual vs physical

<br/>

## `data models`설명

DB의 구조를 기술하는데 사용될 수 있는 `개념`들이 모인 `집합`이다

- DB 구조를 `추상화`해서 표현할 수 있는 수단을 제공한다.

`data model`은 여러 종류가 있으며 추상화 수준과 DB 구조화 방식이 조금씩 다르다

- DB 구조란? : 데이터 유형, 데이터 관계, 제약 사항 등등

- DB에서 읽고 쓰기 위한 기본적인 동작들도 포함된다.

<br/><br/>

## data models 분류하기

### `conceptual data models`이란?

- 일반 사용자. 즉, 비개발자분들도 쉽게 이해할 수 있는 개념들로 이뤄진 모델

- 추상화 수준이 가장 높다

- 비즈니스 요구 사항을 추상화하여 기술할 때 사용한다.

- `대표적인 모델은?` : `entity-relationship model`

![이미지](/programming/img/입문418.PNG)

[http://wiki.hash.kr/index.php/파일:ERD예시.PNG](http://wiki.hash.kr/index.php/%ED%8C%8C%EC%9D%BC:ERD%EC%98%88%EC%8B%9C.PNG)

위 사진은, `ER diagram` 이라고 한다.

<br/>

### `logical data models`이란?

백엔드 개발자라면 이걸 많이 사용한다

- 이해하기 어렵지 않으면서도 `디테일`하게 `DB를 구조화` 할 수 있는 개념들을 제공

- 데이터가 컴퓨터에 저장될 때의 구조와 크게 다르지 않게 DB 구조화를 가능하게 함

- 특정 DBMS나 storage에 종속되지 않는 수준에서 DB를 구조화할 수 있는 모델

- `대표적인 모델은?` : `relational data model`

    - 특징: 테이블 형태로 저장되게 되는 것이다.

![이미지](/programming/img/입문419.PNG)

https://www.youtube.com/watch?v=aL0XXc1yGPs&list=PLcXyemr8ZeoREWGhhZi5FZs6cvymjIBVe

<br/>

### `physical data models`이란?

- 컴퓨터에 데이터가 어떻게 파일 형태로 저장되는지를 기술할 수 있는 수단을 제공

- data format, data orderings, access path 등등

    - `access path` : 데이터 검색을 빠르게 하기 위한 구조체
    
        - `access path 대표적인 예시:` → `index`