## three-schema architecture 란?

<br/>

## `three-schema architecture` 란?

- `database system`을 구축하는 아키텍처 중의 하나

- `user application`으로 부터 물리적인 `database`를 분리시키는 목적

안정적으로 데이터베이스 시스템을 운영하기 위해서 사용되는 아키텍처이다.



- 각 레벨을 독립시켜서 어느 레벨에서의 변화가 상위 레벨에 영향을 주지 않기 위함이다.

    - 즉, 매핑만 바꿔주면 되기에, 안정적으로 운영할 수 있다.

```java
하지만, 요즘은 대부분의 "DBMS"가 "three level"을 완벽하게 혹은 명시적으로 나누지는 않는다.
```

<br/>

각각의 스키마들은 데이터 베이스 구조를 표현하는 것이지, 

실제로 이 데이터가 존재하는 곳은 `internal level`에만 있다는 것이다.

<br/><br/>

## `three-schema architecture` 구조는?

- 세 가지 `level`이 존재하며, 각각의 `level`마다 `schema`가 정의되어 있다.

![이미지](/programming/img/입문420.PNG)

### `internal schemas` (internal level)

가장 데이터 베이스랑 가까운 곳이다

- 물리적으로 데이터가 어떻게 저장되는지 표현 하는데,
    
    `physical data model`을 통해 표현한다.
    
    - ex) `data storage`, `data structure`, index로 대표되는 `access path` 등등
        
        물리적으로 실체가 있는 내용을 기술하는 스키마가 → `internal schemas`이다
        

<br/>

### `external schemas` (external level)

실제 사용자가 바라보는 스키마라고 이해하기.

- `external views`, `user views` 라고도 불린다.

- 특정 유저들이 필요로 하는 데이터만 표현한다.

    - 기 외 알려줄 필요가 없는 데이터는 숨긴다.
- `external schemas`는 `logical data model`을 통해 표현이 된다.

<br/>

### `conceptual schemas` (conceptual level)

`internal schemas` 와 `external schemas`만으로 잘 사용하고 있었는데,
계속해서 

사용자에게 조건을 맞추다 보니, 
`중복된 데이터`, `데이터 불일치 발생`,전체적으로 관리가 힘들어지게 된다.

```java
"이, 문제를 해결하기 위해 등장한 것이 conceptual schemas 이다."
```

- `conceptual schemas`는 `internal schemas`를 추상화하여 표현한 스키마이다.

- 전체 `database`에 대한 구조를 기술한다.
- `물리적`으로 저장 구조에 관한 내용은 숨긴다.
- `entities`, `data types`, `relationships`, `user operations`, `constraints`에 집중할 수 있다.
- `logical data model`을 통해 기술된다.