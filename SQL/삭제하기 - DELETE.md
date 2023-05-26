## 삭제하기 - DELETE

행단위로 데이터를 삭제한다.

<br/>

## DELETE로 행 삭제하기 문법

```sql
DELETE FROM 테이블명 WHERE 조건식
```

<br/><br/>

### 이런 표가 있다고 생각하자.

![이미지](/programming/img/입문172.PNG)

<br/>

## 조건을 달아서 삭제하기.

```sql
DELETE FROM student WHERE id = 2;
```

위에 있는 테이블 사진과 비교하면, ‘박재숙’ 이 없어진 걸 알 수 있다.

![이미지](/programming/img/입문173.PNG)


<br/><br/>

## 테이블 내용 전체 삭제.

```sql
DELETE FROM student;
```

<br/><br/>

## TRUNCATE (자르다)

- 테이블의 전체 데이터를 삭제

- 테이블에 외부키(foreign key)가 없다면 DELETE보다 훨씬 빠르게 삭제됨

<br/>

### 문법

```sql
TRUNCATE 테이블명
```

<br/>

### 예제

```sql
TRUNCATE student;
```

<br/>

다 지워 지는 걸 알 수 있다.

![이미지](/programming/img/입문174.PNG)


<br/><br/>

## DROP TABLE - 테이블을 삭제한다.

<br/>

### 문법

```sql
DROP TABLE 테이블명;
```

<br/>

### 예제

컬럼이 아닌 테이블을 삭제 하는 것이다.

```sql
DROP TABLE student;
```