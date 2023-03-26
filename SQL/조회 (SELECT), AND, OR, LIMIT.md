## 조회 (SELECT), AND, OR, LIMIT

테이블에서 데이터를 조회

<br/>

## 문법

```sql
SELECT 칼럼명1, 칼럼명2
    [FROM 테이블명 ] 
    [GROUP BY 칼럼명] 
    [ORDER BY 칼럼명 [ASC | DESC]] 
    [LIMIT offset, 조회 할 행의 수]
```

1. `칼럼명1, 칼럼명2` : 부분적으로 조회 하고 싶을때 이렇게 사용.

2. `FROM 테이블명` : 어떤 테이블을 가져올지 정하는 것이다.
3. 문법에 나와 있는 것처럼 명령어가 내려가야 되는것이다.

<br/>

![이미지](/programming/img/입문175.PNG)

<br/><br/>

### 예제1)

```sql
SELECT * FROM student;
```

전체 읽어 오는것.

```sql
SELECT name, birthday FROM student;
```

name, birthday 컬럼만 가져 오는 것이다.

![이미지](/programming/img/입문176.PNG)


<br/><br/>

### 예제2)

```sql
SELECT * FROM student WHERE id=3;
```

3번 id인 사람의 정보만 가져 오는 것이다.

![이미지](/programming/img/입문177.PNG)

<br/><br/>

## AND 란?

```sql
SELECT * FROM student WHERE sex='남자' AND address='서울';
```

두개 다 만족하는 것들만 출력

![이미지](/programming/img/입문178.PNG)

<br/><br/>

## OR 이란?

```sql
SELECT * FROM student WHERE sex='여자' OR address='서울';
```

둘중 하나라도 만족하면 출력

![이미지](/programming/img/입문179.PNG)

<br/><br/>

## LIMIT 이란?

```sql
SELECT * FROM student LIMIT 1;
```

LIMIT 은 내가 조회한 결과를 몇개를 가져 올 것인지 하는 것이다.

![이미지](/programming/img/입문180.PNG)

id 3번 말고 그 다음부터 ~ 1개를 가져 오는 것이다.


<br/><br/>

```sql
SELECT * FROM student LIMIT 3,1;
```

![이미지](/programming/img/입문181.PNG)

<br/><br/>

```sql
SELECT * FROM student WHERE sex='남자' LIMIT 2;
```

student 테이블 에서 * 전체를 조회 할건데 '남자' 인 데이터 2개 까지 출력 한다는 것이다.

![이미지](/programming/img/입문182.PNG)