## MVCC 개념

<br/>

### lock - based

| 트랜잭션1 \ 트랜잭션2 | read-lock | write-lock |
| --- | --- | --- |
| read-lock | O | X |
| write-lock | X | X |

<br/>

### MVCC를 사용한다면?

| 트랜잭션1 \ 트랜잭션2 | read-lock | write-lock |
| --- | --- | --- |
| read-lock | O | O |
| write-lock | O | X |

<br/><br/>

## MVCC 동작 설명

이제부터는 `write(x=50)` 되어 있는 것이 있다면 `write_lock(x)` 과정을 거쳐서 온다고 생각하자. 

즉, lock을 획득한 다음 동작하는 것이다.  (앞으로는 생략한다.)

![이미지](/programming/img/입문471.PNG)

1. `write_lock(x)`을 획득한 한다.

2. x의 값을 10에서 50으로 업데이트 해준다면 → 50이라는 값이 바로 `DB로 적용되는 것이 아니라,` 
    
    `다른 공간의 개념으로 x = 50이라는 공간에 써주게 된다.`
    
    (실제로, 어떻게 구현되는지는, 각각의 RDBMS마다 차이가 있다)
    
3. 그리고 트랜잭션1이 x를 read 한다면? → `DB에 저장되어 있는 X = 10의 값을 읽게 되는 것이다.`

```java
"mvcc의 중요한 특징은 commit된 데이터만 읽는다는 것이다"
```

<br/><br/>

### 추가 사항

![이미지](/programming/img/입문472.PNG)

`commit`을 진행하게 된다면 → `이제서야 DB에 x = 50으로 업데이트 된다.`

그리고 `commit`을 진행하면 `write_lock(x) 또한 반납하게 된다.`

<br/>

```java
"commit할때 lock을 반납하는 이유는?" -> recoverabillity를 위해서이다.
```

즉, 롤백 되는 경우를 생각했을때, 롤백이 잘 되도록 하기 위해서이다.



### 이어서 진행

![이미지](/programming/img/입문473.PNG)

<br/>

`read(x) → ?` 의 값은 어떤걸 가져올까?

- 이것은 트랜잭션1번의 아이솔레이션 레벨에 따라서 다르게 값을 가져오게 되는 것이다.

    - `만약, read committed라면?`

        - read하는 가장 최근에 commit한 데이터를 읽는다

        - read committed이라면? → `read(x) ⇒ 50 을 읽는다.`

    - `만약, repeatable read라면?`

        - 트랜잭션1번 시작 시간 기준으로 그전에 commit된 데이터를 읽는다.

        - 즉, 위 그림에서 보자면 → 처음 시작하는 read(x) ⇒ 10 윗부분을 읽는다는 것이다.

            - 사진속에는 50이라고 되어 있지만, 저때는 락공간에 따로 보관하고 있을때이다.

            - 그리하여 repeatable read이라면? → `read(x) ⇒ 10 을 읽는다.`

<br/><br/>

## MVCC 정리

데이터를 읽을 때 특정 시점 기준으로 가장 최근에 `commit`된 데이터를 읽는다

- 특정 시점이란? → 아이솔레이션 레벨에 따라 다르다.

    - 데이터 변화했다는 히스토리를 RDBMS가 내부적으로 관리해줘야 한다.

        - 그렇기에, MVCC는 추가적으로 저장 공간을 많이 쓰게 된다.

            - 즉, 이런 부분이 단점 될 수 있다.

- `장점이라면`, read와 write는 서로를 block하지 않는다.

    - 성능면에서 동시에 처리할 수 있는 트랜잭션이 더 늘어날 수 있다는 측면에서 장점이 되는 것이다.
        
    - 그렇기에, 현업에서는 mvcc를 사용하여 구현을 한다.

<br/><br/>

## locking read란?

![이미지](/programming/img/입문474.PNG)

트랜잭션1번의 아이솔레이션 레벨이 repeatable read라고 할지라도, 가장 최근의 commit된 데이터를 읽어 온다.

<br/>

### locking read 문법

```sql
select ... FOR UPDATE; // write/read 전용 락을 말한다.
select ... FOR SHARE;  // read 전용 락을 말한다.

e.g.)
select balance
from account
where id = 'x';
for update; // 이 부분이다!
```

만약, serializable 레벨이라면 → 일반적인 쿼리문에서 `for share;` 구문을 작성하지 않아도 

mysql이 알아서 for share 처럼 동작시키는 것이다.