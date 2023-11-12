## transaction isolation level

<br/>

### 트랜잭션의 이상한 현상들은 이렇다.

- `Dirty read` : commit 되지 않은 변화를 읽음

- `Non-repeatable read` : 같은 데이터를 두번 읽었는데, 값이 달라짐
    - `Fuzzy read` 라고 불리기도 한다.
- `Phantom read` : 없던 데이터가 생겼다.

<br/>

이런 현상들은 발생하지 않는 것이 좋은 것이다.

즉, 이상한 결과, 데이터 불일치를 발생시키는 현상이기 때문이다.

```java
하지만, 위 현상들이 없게 할수는 있지만,
동시 처리 가능한 트랜잭션 수가 줄어들어, 결국 DB의 성능이 떨어지게 되는 것이다.
```

<br/><br/>

## 그리하여 나온것이 isolation level 인것이다.

일부 이상한 현상은 허용하는 `level`을 만들어, 사용자가 필요에 따라 적절하게 선택하도록 하는 것이다.

단계가 높아질수록 느려진다고 생각하면 되는 것이다.

| Isolation level | Dirty read | Non-repeatable read | Phantom read |
| --- | --- | --- | --- |
| Read uncommitted | 허용 | 허용 | 허용 |
| Read committed | 불가 | 허용 | 허용 |
| Repeatable read | 불가 | 불가 | 허용 |
| Serializable | 불가 | 불가 | 불가 |

<br/>

- `Read uncommitted` : 모두 허용한다.

    - 좋게 말하면 가장 자유로운 것이고,
    - 나쁘게 말하면 이상한 현상이 가장 취약한 곳

    - 동시성은 높아서 처리량은 가장 높다.

- `Read committed` : 커밋된 데이터만 읽는다.

- `Repeatable read` : `Read committed` 의 레벨에서 허용하지 않는 것은 같이 허용하지 않는다
    - 추가로 `Non-repeatable read`도 허용하지 않는다.

- `Serializable` : 이상한 현상들을 모두 허용하지 않는다.
    
    - 위 세가지 현상 뿐만 아니라, `이상한 현상 자체가 발생하지 않는 level`을 의미한다.

<br/><br/>

## 정리

3가지 이상 현상에 따라, 각각의 isolation level이 구분 지었다.

그렇기에, 개발자는 isolation level을 통해 전체 처리량과 

데이터 일관성 사이에서 어느 정도 트레이드 오프 해야 하는 것이다.

<br/><br/>

## SNAPSHOT ISOLATION

기존의 level들 하고 약간 다르다.

이상한 현상들을 정의한 뒤, 얼마만큼 허용하는지에 대해 level을 부여 했다.

<br/>

### SNAPSHOT ISOLATION은 

isolation 구현 방식에 기반하여 level이 정의 된 것이다.

- 트랜잭션 시작전에 commit된 데이터만 보입니다.

- first-committer win → 즉, 처음 commit한 트랜잭션이 승리자가 된다.

    - 그 뒤에, 다른 트랜잭션이 commit하려고 하면 실패한다.