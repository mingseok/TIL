## 정렬 -  ORDER BY




지정된 칼럼을 기준으로 행을 정렬

<br/>

## 문법

```sql
SELECT * FROM 테이블명 ORDER BY 정렬의 기준으로 사용할 열 [DESC | ASC]
```

![이미지](/programming/img/입문183.PNG)

<br/><br/>

## 예제

```sql
select * from student order by distance desc;
```



![이미지](/programming/img/입문184.PNG)


<br/><br/>


```sql
select * from student order by distance desc, address asc;
```



![이미지](/programming/img/입문185.PNG)




<br/><br/>




<br/><br/>

## ORDER BY DESC로 내림차순으로 정렬하기.

내림차순은 열명 뒤에 DESC를 붙여 지정한다.

오름차순은 내림차순과 달리 생략 가능하며 ASC로도 지정할 수 있다.

```sql
SELECT 열명 FROM 테이블명 ORDER BY 열명 DESC
```

<br/>

그리고 정렬에 기준이 있는데 문자열형 같은 경우는 ‘사전식 순서’ 에 의해 결정된다.

<br/><br/>

## ORDER BY로 복수 열 지정하기.

SELECT 구에서 열을 지정한 것처럼 콤마(,) 로 열명을 구분해 지정하면 된다.

```sql
SELECT 열명 FROM 테이블명 ORDER BY 열명1, 열명2
```

 복수열로 지정했다면

| a | b |
| --- | --- |
| 1 | 1 |
| 2 | 1 |
| 2 | 2 |
| 1 | 3 |
| 1 | 2 |

<br/>

### 이렇게 변한다.

| a | b |
| --- | --- |
| 1 | 1 |
| 1 | 2 |
| 1 | 3 |
| 2 | 1 |
| 2 | 2 |

중요한건 a를 먼저 바꿔놓고, 그 다음 a에 맞게 b를 수정한다는 것이다.

뒤에 온 b의 순서는 1, 1, 2, 2, 3 이런게 아니라 a 에 맞춰 있는걸 기준으로 하는 것이다.

<br/><br/>

## NULL 값을 정렬순서

NULL 값을 가지는 행은 가장 먼저 표시되거나 가장 나중에 표시된다.

데이터베이스 제품에 따라 기준이 다른다.

<br/>

MySQL의 경우는 NULL 값을 가장 작은 값으로 취급해 ASC(오름차순) 에서는 가장 먼저, 

DESC(내림차순) 에서는 가장 나중에 표시한다.


<br/><br/>

>**Reference** <br/> SQL 첫걸음 : 아사이 아츠시 지음, 박준용 옮김, 한빛미디어
