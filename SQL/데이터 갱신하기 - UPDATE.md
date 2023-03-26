## 데이터를 수정 (UPDATE)

### 문법

```sql
UPDATE 테이블명 SET (컬럼1)=(컬럼1의값), (컬럼2)=(컬럼2의값) WHERE (대상이될컬럼명)=(컬럼의값)
```

<br/><br/>

### 이렇게 출력 된다.

![이미지](/programming/img/입문168.PNG)

<br/><br/>

## 예제

```sql
UPDATE `student` SET address='서울';
```

즉, `address` 라는 컬럼에 `‘서울’` 을 저장하겠다. 하는 것이다.

<br/>

하면 모두 서울로 변하는 것이다.

![이미지](/programming/img/입문169.PNG)

<br/><br/>

```sql
UPDATE `student` SET name='이진경' WHERE id=1;
```

### where 문은 “어디에 적용 될 것인가?” , ”어떤 행에 적용 될 것인가?” 

지정하는 키워드이다.

그렇다면 WHERE id=1; 이라고 했다면, `id` 컬럼의 값이 1인 행에 대해서 실행 한다는 것이다.

![이미지](/programming/img/입문170.PNG)

즉, where 부터 본다는 것이다.

<br/><br/>

```sql
UPDATE `student` SET name='이고잉', birthday='2001-4-1' WHERE id=3;
```

이렇게 변경 되는 것이다.

![이미지](/programming/img/입문171.PNG)