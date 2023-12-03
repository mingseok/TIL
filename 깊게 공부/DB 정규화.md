## DB 정규화(Normalization)




데이터 중복과 `insertion`, `update`, `deletion` anomaly를 최소화하기 위해

일련의 normal forms(NF)에 따라 relational DB를 구성하는 과정

```java
"normal forms 이란?"
정규화 되기 위해 준수해야 하는 몇가지 rule들이 있는데
이 각각의 rule을 "normal form"이라고 부른다.
```

<br/><br/>

## DB 정규화 과정

![이미지](/programming/img/입문477.PNG)

- 처음부터 순차적으로 진행한다.

- normal form을 만족하지 못하면 만족하도록 테이블 구조를 조정한다.

- 앞 단계를 만족해야 다음 단계로 진행 할 수 있다.

<br/>

```java
"1NF ~ BCNF 까지는"
- FD와 Key만으로 정의되는 normal forms
- 3NF까지 도달하면 정규화 됐다고 말하기도 함
- 실무에서는 3NF 혹은 BCNF까지 진행한다. (많이 한다고 해도 4NF까지 정도)
```

<br/><br/>

## Normalization 예시 테이블

![이미지](/programming/img/입문478.PNG)

<br/><br/>

## 1NF :

`attribute의 value는 반드시 나눠질 수 없는 단일한 값이다.`

위 테이블을 보면, Kookmin은 `card_id가 두개로 되어 있다.` → 1NF 위반! (=`“c201, c202"`)

<br/>

저걸 처리해줘야지 다음 단계로 넘어갈 수 있게 되는 것이다. 

그리하여 아래 사진이 1NF가 해결 된 사진이다.

![이미지](/programming/img/입문479.PNG)

`1NF 해결!` → 하나의 튜플을 더 만들어서 해결하였다.

- `문제점:` 중복 데이터가 생기고 primary key도 변경을 해야 한다.

    - 원래는, `account_id` 만으로도 `primary key`가 가능했는데, 이제는 불가능하다.

<br/><br/>

## 왜 중복 데이터가 생긴 걸까?

일단, 키를 변경해주자.

- `(candidate) key`

    - {account_id, card_id}, {bank_name, account_num, card_id}

- `non-prime attribute`

    - class, ratio, empl_id, empl_name

<br/>

위 그림을 보면, `non-prime attribute` 들은 `{account_id, card_id}`키 중에서 

`account_id`만으로도 유니크하게 결정 지을 수 있을 것이다. 

```java
"이런 상황을"
모든 "non-prime attribute"들이 {account_id, card_id}에 대해 "partially dependent" 하는것.
- 즉, "부분적으로 디펜던트"하다고 말할 수 있다.

또 다른 e.g.)
{bank_name, account_num, card_id} 중에서도, {bank_name, account_num} 만으로도
"partially dependent" 하다고 말할 수 있다.
```

<br/>

### 참고

```java
- super key
    - table에서 tuple들을 unique하게 식별할 수 있는 attributes set

- candidate) key
    - 어느 한 attribute라도 제거하면 unique하게 tuples를 식별할 수 없는 super key

- primary key
    - table에서 tuple들을 unique하게 식별하려고 선택된 (candidate) key
```

### prime attribute

- 임의의 key에 속하는 attribute
- `account_id`, `bank_name`, `account_num`

### non-prime attribute

- prime attribute 설명의 반대라고 생각하기
- `class`, `ratio`, `empl_id`, `empl_name`, `card_id`

<br/><br/>

## 2NF :

위의 문제점인, 중복 데이터를 해결 하기 위해 나온 것이 2NF이다.

```java
"모든 non-prime attribute는 모든 key에 fully functionally dependent 해야 한다"
```

위의 문제점은 `card_id` 때문이다.

- `이유는?` 1NF 문제를 해결하기 위해서 card_id를 쪼개면서 튜블을 하나 더 추가하여 문제가 발생

<br/><br/>

### 2NF 해결 방법

![이미지](/programming/img/입문480.PNG)

새로운 테이블을 만들면서 card_id를 다른 테이블로 이동 시켜준 것이다.

이렇게 함으로써, EMPLOYEE 테이블도 중복 튜블을 제거 할 수 있게 되는 것이다.

<br/>

```java
"primary key가 account_id 하나로 다시 돌아왔다."
 이렇게 됨으로써, partially dependent가 아닌, 
 "fully functionally dependent"가 된것이다.

"non-prime attribute" 기준으로 보면, "account_id" 하나만으로 
 {class, ratio, empl_id, empl_name}들을 결정할 수 있다.
```

<br/><br/>

## 3NF :

```java
"모든 non-prime attribute는 어떤 key에도 transitively dependent 하면 안된다"
즉, 쉽게 말해서 "non-prime attribute"와 "non-prime attribute" 사이에는 FD가 있으면 안된다.
```

아래 사진 처럼 `empl_id` 키로만 `empl_name`을 결정할 수 있는 것이 아닌,

`account_id` 키로도, 다른 키로도 결정 할 수 있는 경우가 문제인 것이다.

<br/>

![이미지](/programming/img/입문481.PNG)

즉, 다른 키를 통해서 `transitively dependent`가 생기니깐 `empl_name`과 관련된 중복된 데이터가 같이 생성 되는 것이다. 

그렇기에 이런 중복을 없애주기 위해 3NF가 정의되는 것이다.

<br/>

### 3NF 해결 방법

![이미지](/programming/img/입문482.PNG)

EMPLOYEE 테이블에서는 empl_id가 primary key가 되기 때문에 중복된 데이터를 넣어줄 필요가 없게 된 것이다.

<br/>

### 여기까지 진행 하였다면, “정규화” 됐다

3NF까지 왔다면, 정규화 되었다고, 말할 수 있다.

<br/><br/>

## BCNF :

```java
"모든 유효한 non-trivial FD x → y는 x가 super key 여야 한다"
```

즉, `x → y` 같은 경우의 `“x"`는 `”레프트 핸드 사이드”` 라고 부르기에 x는 반드시 `슈퍼키`여야 한다

![이미지](/programming/img/입문483.PNG)


<br/>

하지만 위 사진처럼 class는 슈퍼키가 아니다.

- 슈퍼키는 개념 자체가 해당 테이블안에서 `tuple`을 유니크하게 식별할 수 있는
    
    `attribute의 집합을 슈퍼키`라고 하는 건데, `class는 non-prime attribute` 하기 때문이다.
    
- 그리하여, `functional dependency를 위반`하게 되는 것이다.

```java
"BCNF를 위반하기에 중복된 데이터가 계속해서 쌓이게 되는 것이다."
```

<br/>

### BCNF 해결 방법

![이미지](/programming/img/입문484.PNG)