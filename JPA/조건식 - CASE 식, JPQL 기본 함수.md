
## 조건식 - CASE 식

`COALESCE`: 하나씩 조회해서 null이 아니면 반환

`NULLIF`: 두 값이 같으면 null 반환, 다르면 첫번째 값 반환

<br/>



사용자 이름이 없으면 이름 없는 회원을 반환

```sql
select coalesce(m.username,'이름 없는 회원') from Member m
```

<br/>

사용자 이름이 ‘관리자’면 null을 반환하고 나머지는 본인의 이름을 반환

```sql
select NULLIF(m.username, '관리자') from Member m
```

<br/><br/>

## JPQL 기본 함수

- CONCAT

- SUBSTRING
- TRIM
- LOWER, UPPER
- LENGTH
- LOCATE

    - 해당 위치를 반환해준다.

- ABS, SQRT, MOD

- SIZE, INDEX(=JPA 용도, index는 그렇게 추천하지 않는다.)


<br/><br/>

>**Reference** <br/>[자바 ORM 표준 JPA 프로그래밍 - 기본편](https://www.inflearn.com/course/ORM-JPA-Basic)

