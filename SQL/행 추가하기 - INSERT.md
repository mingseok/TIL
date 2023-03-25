## 행 추가하기 - INSERT

INSERT의 열 지정

```sql
INSERT INTO 테이블명(열1, 열2 ...) VALUE(값1, 값2, ...)
```

<br/>

no 열과 a 열만 지정해 행 추가하기

```sql
INSERT INTO sample41(a, no) VALUES('XYZ',2);
```

| no | a | b |
| --- | --- | --- |
| 1 | ABC | 2014-01-25 |
| 2 | XYZ | NULL |

특히 별도의 값을 지정하지 않았던 b 열에는 기본값인 NULL(b 열의 default 값)이 저장된 것을 알 수 있다.

디폴트값을 지정하는 것을 DEFAULT를 명시적으로 지정하는 방법이 있다.

<br/>

## DEFAULT 로 값을 지정해 행 추가하기

```sql
INSERT INTO sample411(no, d) VALUES(2, DEFAULT);
```

| no | d |
| --- | --- |
| 1 | 1 |
| 2 | 0 |

디폴트값으로 INSERT 되었다.

<br/>

암묵적으로 디폴트값을 가지는 행 추가하기

```sql
INSERT INTO sample411(no) VALUES(3);
```

| no | d |
| --- | --- |
| 1 | 1 |
| 2 | 0 |
| 3 | 0 |


열을 지정하지 않으면 디폴트값으로 행이 추가된다.


<br/><br/>

>**Reference** <br/> SQL 첫걸음 : 아사이 아츠시 지음, 박준용 옮김, 한빛미디어
