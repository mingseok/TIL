## 삽입 (INSERT)

## 문법

```sql
INSERT INTO table_name VALUES (value1, value2, value3,...)
```

또 다른 문법으로는 컬럼들이 들어가서 values에 들어가는 것이다. 

즉, c언어에서 `printf()` 라고 생각하면 될 것이다.


</br>

```sql
INSERT INTO table_name (column1, column2, column3,...) VALUES (value1, value2, value3,...)
```

</br>

### 이렇게 앞부분을 생략하고 많이 사용한다.

```sql
INSERT INTO `student` VALUES ('2', '김스', '여자', '서울', '2000-10-26');
```

</br>

insert 해보면 이렇게 저장 되는 걸 알 수 있다.

![이미지](/programming/img/입문163.PNG)