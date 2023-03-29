## JOIN

<br/>

## JOIN이란? → 2개의 테이블을 엮는 것이다.

2개의 테이블을 엮기 위해서는 `‘일대다’` 의 관계가 맺여져 있어야 한다.

<br/>

## JOIN 예시

예를 들어, `배송 정보`를 만들려고 한다. → 그렇다면 필요한 정보는 무엇일까?

1. 어떤 휴대폰을 샀는지에 대한 `휴대폰 정보`가 필요하다.

2. 그리고 그 휴대폰을 누가 샀는지에 대한 `유저 정보`가 필요한 것이다.

![이미지](/programming/img/입문186.PNG)

그리하여 두개의 테이블이 필요한데, 이런 상황에서 JOIN을 사용하는 것이다.

<br/><br/>

## ‘일대다’ 관계의 이해

`PK:` 기본키 → 중복X, 빈공간X 

`FK:` 외래키 → 중복O

![이미지](/programming/img/입문187.PNG)

흐름을 보자면, `BLK` 라는 친구가 구매한 물품을 `구매 테이블`에서 관리한다.

구매 테이블을 보니 → `BLK`는 `‘지갑’` 도 사고, `‘맥북프로’` 도 사고 1개 이상을 샀다.

<br/>

그렇기에, 구매 테이블에서의 아이디는 `PK`를 사용하면 안되는 것이다.

즉, 회원 테이블의 `BLK`과 구매 테이블의 `BLK`는 같은 것이다. 

<br/>

그럼에도 불구하고, 서로 확실히 다르다는 것을 알게 되었다.

```html
그리하여 이것이 '일대다' 의 관계라는 것이다.
또한 "기본키 외래키 관계" 라고도 부른다.
```

- `사원번호` : `지금껏 받았던 월급 테이블` → ‘일대다’ 의 관계로 설명할 수 있다.

- `학생 학번` : `수강 신청` → ‘일대다’

<br/><br/>

## 조인의 문법

WHERE 조건은 생략이 가능하다.

![이미지](/programming/img/입문188.PNG)

<br/>

### 설명을 위해 다시 가져왔다. (보면서 이해하기)

![이미지](/programming/img/입문189.PNG)

<br/><br/>

## 예제1)

```sql
SELECT *
	FROM buy
		INNER JOIN member
		ON buy.mem_id = member.mem_id
	WHERE buy.mem_id = 'GRL';
```

1. `SELECT *` : 모든 열을 다 본다는 것.

2. `FROM buy` : `FROM` 바로 뒤에 온것을 기준 테이블로 정한다.
3. `INNER JOIN member` : `INNER JOIN`을 회원 테이블과 한다는 것이다.
4. `ON` : 조건이 붙는다는 것이다.
    - `buy.mem_id` : 구매 테이블`.`아이디 와
        
        `member.mem_id` : 회원 테이블 아이디가 같은 것을 조인 한다는 말이다.
        
        `왜??` → 그래야 의미가 있기 때문이다. 구매 테이블 아이디가 `BLK`라면 
        
        회원 테이블에서도 `BLK`라는 아이디를 찾는게 당연하다. 
        
        (샀던 사람은 `BLK`인데 다른 사람을찾으면 의미가 없음;;)
        
    - 즉, 정리하면 구매 테이블과 회원 테이블이 같은 것을 조인해야 된다.
5. `WHERE buy.mem_id = 'GRL'` : 조건이 붙는 것인데,
    - `‘GRL’` 이라는 아이디인 사람만 정보를 알고 싶은 것이다.

<br/><br/>

## 출력 시켜보면

이렇게 하는 것이 내부 조인이다.

![이미지](/programming/img/입문190.PNG)

<br/><br/>

## `WHERE`절을 뺀다면?

구매 테이블에 있는 순서대로가 전부 출력 되는 것이다.

- 기준은 구매 테이블에 있는 것이다.

- 이유는? → `FROM buy` : `FROM` 바로 뒤에 온것을 기준 테이블로 정했기 때문이다.

![이미지](/programming/img/입문191.PNG)

## 필요한 열만 출력하기

```sql
SELECT mem_id, prod_name, addr, CONCAT(phone1, phone2) AS '연락처'
	FROM buy
	INNER JOIN member
    ON buy.mem_id = member.mem_id;
```

실행했더니 아래와 같은 에러가 뜬다.

이유는? → mem_id가 member 테이블에도 buy 테이블에도 있기에 어떤 mem_id를 가져와야 할지 모르기에 에러가 난다.

<br/><br/>

## 제대로 작성하기

`CONCAT` : 문자열을 합치는 것이다.

```sql
SELECT buy.mem_id, prod_name, addr, CONCAT(phone1, phone2) AS '연락처'
	FROM buy
	INNER JOIN member
    ON buy.mem_id = member.mem_id;
```

<br/>

### 실행 결과

기준은 buy.mem_id인 것이다.

![이미지](/programming/img/입문192.PNG)

<br/><br/>

## 이렇게 사용하자.

SQL을 명확히 하기 위해서 SELECT 다음의 열 이름(컬럼 이름)에도 모두 (테이블_이름.열_이름) 형식으로 작성해야 한다.

```sql
-- 이렇게 간단한게 좋다.
SELECT B.mem_id, M.mem_name, B.prod_name, M.addr, CONCAT(M.phone1, M.phone2) AS '연락처'
	FROM buy B
		INNER JOIN member M
    ON B.mem_id = M.mem_id;
```

<br/><br/>

## 문제) - 중복된 결과 1개만 출력

구매 기록이 한 번이라도 있는 사람들의 리스트를 뽑아내고자 한다.

이때 중복된 이름은 필요 없으므로 DISTINCT를 이용해 뽑아낼 수 있다.

```sql
SELECT DISTINCT M.mem_id, M.mem_name, M.addr
	FROM buy B
        INNER JOIN member M
        ON B.mem_id = M.mem_id
        ORDER BY M.mem_id;
```

<br/>

![이미지](/programming/img/입문193.PNG)

### 지금까지가 INNER JOIN 이다.

- 한쪽 테이블에 데이터가 없다면 그 행 자체를 아예, 결과에서 빼버리는 것이다.

### OUTTER JOIN 설명

- 테이블 결합을 했을때, 한쪽 테이블에 데이터가 없다고 하더라도 가져와서
    
    null 표시 한다는 것이 특징이다.
    

<br/>

하나만 알면 된다. → LEFT JOIN만 알자.

왜?? → 이유는 코드 위치만 변경해주면 되기 때문에 다 똑같은 것이다.

![이미지](/programming/img/입문194.PNG)

<br/><br/>

## 코드 확인

```sql
SELECT M.mem_id, M.mem_name, B.prod_name, M.addr
	FROM member M
		LEFT OUTER JOIN buy B
		ON M.mem_id = B.mem_id
	ORDER BY M.mem_id;
```

- `M.mem_id, M.mem_name, B.prod_name, M.addr` : 4개만 출력한다는 뜻
    - 중요하다. → 나는 지금까지 모든 조건에서 하는건 줄 알았다. 하지만 틀렸다. 여기서 컬럼을 지정하는 것이다.
        
- `FROM member M` : member 를 M으로 지정
- `LEFT OUTER JOIN buy B` :
    - `LEFT` 라서 `왼쪽 기준`이 되는 것이다. member 테이블이 기준이 되므로
        
         “member 테이블에 있는 내용은 다 나와라” 가 되는 것이다.
        
    - `buy B` : buy를 B로 지정
    
<br/>
<br/>

## 출력

이렇게 출력 된 이유는, `ORDER BY` 정렬을 했기 때문이다.

밑에 보면, `잇지`는 구매한 것이 없지만 그래도 출력 되는걸 알 수 있다.

이유는? → `LEFT JOIN` 이니깐 왼쪽에 있는 애들은 다 나오게 되는 것이기에 출력 된 것이다.

![이미지](/programming/img/입문195.PNG)