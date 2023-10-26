## NULL 값, 비교 연산자

<br/>

## null의 의미

- 값이 존재하지 않는다

- 값이 존재하나 아직 그 값이 무엇인지 알지 못한다
- 해당 사항과 관련이 없다


<br/>

## NULL 값 검색

= 연산자로 NULL 을 검색할 수 없다.


<br/>


### NULL 관련 잘못된 코드

```sql
select name, address
from Address
where phone_nbr = NULL;
```

실제로 제대로 작동하는 select구문이 아니다. (물론 오류가 발생하지도 않는다)


`null을 검색하고 싶을때는 'IS NULL' 사용하기`

<br/><br/>

## IS NULL

NULL 값을 검색할 때는 = 연산자가 아닌 `‘IS NULL’` 을 사용 해야 한다.

`IS NULL` 을 사용해 birthday가 NULL인 행만 추출

```sql
SELECT * FROM sample21 WHERE birthday IS NULL;
```

<br/>

### 출력값

| no | name | birthday | address |
| --- | --- | --- | --- |
| 2 | 김재진 | NULL | 대구광역시 동구 |
| 3 | 홍길동 | NULL | 서울특별시 마포구 |

반대로 NULL 값이 아닌 행을 검색하고 싶다면 `‘IS NOT NULL’` 을 사용하면 된다.

<br/><br/>

## 비교 연산자

대표적인 비교 연산자들은 이렇게 있다.

```
`=` , `<>` , `>` , `>=` , `<` , `<=`
```

<br/><br/>

>**Reference** <br/> SQL 첫걸음 : 아사이 아츠시 지음, 박준용 옮김, 한빛미디어
