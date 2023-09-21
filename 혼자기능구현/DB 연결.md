## lntellij 내장 DB

### 1. mySQL 들어가기 -> root 으로 일단 들어가자. 비번 1234




![이미지](/programming/img/개인1.PNG)

<br/><br/>




```
꼭 참고하기
https://www.notion.so/mySQL-a5c0418f122b403a9fd0099832c2c5a7
```

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

### 5. Database 에서 + 모양 클릭 해주기.

![이미지](/programming/img/개인4.PNG)

<br/><br/>

### 6. root 과 1234 는 꼭 mySQL 에서 계정 들어 갈때 ID와 PWD 이다.
    
꼭 테스트 해보자.
    

![이미지](/programming/img/개인5.PNG)

<br/><br/>

### 7. 그리고 다시 실행 시켜 보기.

![이미지](/programming/img/개인6.PNG)

<br/><br/>

### 8. 테이블 생성 방법.

(빨간색 표시 되어 있는 걸 선택하기)

![이미지](/programming/img/개인7.PNG)

<br/><br/>

### 9. 작성 해주기.

![이미지](/programming/img/개인8.PNG)


<br/><br/>

### 10. 런 하면 생성 된걸 알 수 있다.

![이미지](/programming/img/개인9.PNG)

