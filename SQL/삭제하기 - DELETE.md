## 삭제하기 - DELETE

```sql
DELETE FROM 테이블명 WHERE 조건식
```

여기서 DELETE FROM sample41; 으로 DELETE 명령을 실행하면 

sample41 테이블의 모든 데이터가 삭제된다.

<br/>

WHERE 구에 조건을 지정하여 no 열이 3인 행을 삭제 해보면

sample41 테이블에서 no 열이 3인 행 삭제하기.

```sql
DELETE FROM sample41 WHERE no = 3;
```

| no | a | b |
| --- | --- | --- |
| 1 | ABC | 2014-01-25 |
| 2 | XYZ | NULL |

ORDER BY 구는 사용할 수 없습니다.

어떤 행부터 삭제할 것인지는 중요하지 않으며, 의미가 없기 때문이다.

<br/><br/>

>**Reference** <br/> SQL 첫걸음 : 아사이 아츠시 지음, 박준용 옮김, 한빛미디어
