## LIKE, BETWEEN

<br/>


## LIKE 문제 1)

이름이 N으로 시작하거나 N으로 끝나는 직원들의 이름을 알고 싶다.

```sql
select name
from employee
where name like 'N%' or name like '%N';
```

- N% : n으로 시작하는 경우

- %N : n으로 끝나는 경우


<br/>


### 출력값 확인

| name |
| --- |
| BROWN |
| JOHN |
| NICOLE |


<br/><br/>


## LIKE 문제 2)

이름에 NG가 들어가는 직원들의 이름을 알고 싶다

```sql
select name
from employee
where name like '%NG%';
```

<br/>

### 출력값 확인

| name |
| --- |
| DINGYO |
| JISUNG |




<br/><br/>


## LIKE 문제 3)

이름이 J로 시작하는, 총 네글자의 이름을 가지는 직원들의 이름을 알고 싶다

```sql
select name
from employee
where name 'J___';
```

<br/>

### 출력값 확인

| name |
| --- |
| JANE |
| AOHN |




<br/><br/>


## 만약, 찾고 싶은 대상이 '%' 이거나 '_' 일 경우는?

%로 시작하거나 _로 끝나는 프로젝트 이름을 찾고 싶다면?

```sql
select name
from project
where name LIKE '\%%' or name name LIKE '%\_';
```






<br/><br/>

## BETWEEN , NOT BETWEEN 연산자.

```
“어떤 값이 어떤 특정 값 사이에 있다” 이, 여부를 참과 거짓으로 나타낼 수 있다.
```


<br/>

(중요) 예를 들어 `BETWEEN 1 AND 4` 이렇게 되어 있으면 ‘4’는 포함되지 않는 것이다.



‘4’ 전까지를 말하는 것이다.

![이미지](/programming/img/입문201.PNG)

<br/><br/>

```sql
SELECT 5 BETWEEN 1 AND 10;
```





‘5’ 는 1과 10 사이에 있다. → 참인 것이다.

![이미지](/programming/img/입문202.PNG)

<br/><br/>

```sql
SELECT 'banana' NOT BETWEEN 'Apple' AND 'camera';
```



이건 단어의 앞 글자만 보면 되는 것이다. A와 C 사이에 B 가 있으니 참이지만, NOT 이 붙어 있으니 false 로 나온 것이다.

참고로 mysql 은 소, 대문자 구분을 하지 않는다.

![이미지](/programming/img/입문203.PNG)


<br/><br/>

```sql
SELECT * FROM OrderDetails
WHERE ProductID BETWEEN 1 AND 4;
```



![이미지](/programming/img/입문204.PNG)


<br/><br/>

```sql
SELECT * FROM Customers
WHERE CustomerName BETWEEN 'a' AND 'b';
```



여기서 포인트는 마지막꺼는 포함하지 않는 다는 것이다.

a AND b 를 했지만 a 관한 것만 출력 되는 것이다.

b 가 없어서 출력 안되는 것이 아니다.

![이미지](/programming/img/입문205.PNG)


