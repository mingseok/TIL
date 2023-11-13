## lntellij 내장 DB

### 1. mySQL 들어가기 -> root 으로 일단 들어가자. 비번 1234




![이미지](/programming/img/개인1.PNG)

<br/><br/>




### 2. 테이블 생성

```sql
create database facegram character set utf8
```

<br/>

### 3. 테이블 조회

```sql
show databases;
```

![이미지](/programming/img/개인2.PNG)

<br/><br/>

### 4. `application.properties` 에서 작성 해주기.

파란색 부분들은 해당 프로젝트에 맞게 바꿔주기.

```java
spring.datasource.url=jdbc:mysql://localhost:3306/facegram?autoReconnect=true
spring.datasource.username=root
spring.datasource.password=1234
```

![이미지](/programming/img/개인3.PNG)


<br/><br/>

### 여기서 부턴 여기 참고하기.

링크 들어가서 따라하고 다시 돌아와 밑에 있는 5번부터 진행하기.

[정리 노션](https://www.notion.so/mySQL-a5c0418f122b403a9fd0099832c2c5a7](https://regal-receipt-228.notion.site/mySQL-a5c0418f122b403a9fd0099832c2c5a7?pvs=4))



<br/><br/>

### 5. 그리고 다시 실행 시켜 보기.

![이미지](/programming/img/개인6.PNG)

<br/><br/>

### 6. 테이블 생성 방법.

(빨간색 표시 되어 있는 걸 선택하기)

![이미지](/programming/img/개인7.PNG)

<br/><br/>

### 7. 작성 해주기.

![이미지](/programming/img/개인8.PNG)


<br/><br/>

### 8. 런 하면 생성 된걸 알 수 있다.

![이미지](/programming/img/개인9.PNG)

