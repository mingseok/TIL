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


<br/>

## 궁금증!

```java
application.properties 에 적은 저 세 줄이 실제로 무엇을 만드는가?
```

앞의 커넥션 풀 글에서 본 `DataSource` 를 만든다.

```java
spring.datasource.url=jdbc:mysql://localhost:3306/facegram?autoReconnect=true
spring.datasource.username=root
spring.datasource.password=1234
```

<br/>

스프링 부트가 이 값을 보고 `HikariDataSource` 를 만들어 빈으로 등록한다.

앞의 스프링이란 스프링 부트란 글에서 본 자동 구성이 하는 일이다.

```java
@ConditionalOnClass(DataSource.class)          // JDBC 가 클래스패스에 있고
@ConditionalOnMissingBean(DataSource.class)    // 내가 직접 만든 빈이 없으면
-> DataSourceAutoConfiguration 이 만들어준다
```

<br/>

## URL 을 쪼개보면

```java
jdbc:mysql://localhost:3306/facegram?autoReconnect=true
^^^^ ^^^^^   ^^^^^^^^^ ^^^^ ^^^^^^^^ ^^^^^^^^^^^^^^^^^
 |    |         |       |      |            |
 |    |         |       |      |            +-- 옵션
 |    |         |       |      +-- 데이터베이스 이름
 |    |         |       +-- 포트 (앞의 PORT, DNS 글에서 본 3306)
 |    |         +-- 호스트
 |    +-- 어느 드라이버를 쓸지
 +-- JDBC 프로토콜
```

<br/>

`jdbc:mysql` 부분을 보고 어느 드라이버를 쓸지 정한다.

앞의 JDBC 표준 인터페이스 글에서 본 `SPI` 로 클래스패스에서 드라이버를 찾아낸다.

<br/>

## autoReconnect=true 는 권장되지 않는다

원문의 URL에 붙어 있는 이 옵션은 지금은 쓰지 말라고 안내된다.

<br/>

이유는 앞의 DBCP 글에서 본 문제와 이어진다.

```java
커넥션이 끊긴 줄 모르고 사용하려다 실패한다
-> autoReconnect 가 조용히 다시 연결한다
-> 그런데 트랜잭션 상태는 복구되지 않는다
```

<br/>

트랜잭션 도중에 재연결되면 앞에서 한 작업이 사라진 채로 이어진다.

에러도 안 나고 데이터만 어긋난다.

<br/>

지금은 커넥션 풀이 이 문제를 다르게 푼다.

```java
spring.datasource.hikari.max-lifetime=1800000     # 30분마다 커넥션을 새로 만든다
spring.datasource.hikari.connection-test-query=SELECT 1
```

<br/>

앞의 DBCP 글에서 본 대로, 빌려주기 전에 살아 있는지 확인하는 방식이다.

끊긴 커넥션은 조용히 버리고 새로 만든다.

<br/>

## 비밀번호를 파일에 적어두면 안 된다

원문의 `password=1234` 는 학습용이라 괜찮지만, 실제로는 위험하다.

<br/>

`application.properties` 는 git에 올라간다.

한 번 올라가면 나중에 지워도 커밋 기록에 남는다.

<br/>

그래서 실무에서는 밖에서 주입한다.

```java
spring.datasource.password=${DB_PASSWORD}
```

<br/>

그리고 환경 변수로 넣는다.

```bash
export DB_PASSWORD=진짜비밀번호
java -jar app.jar
```

<br/>

앞의 스프링 컨테이너 글에서 본 `EnvironmentCapable` 이 이 값을 읽어온다.

`${...}` 를 실제 값으로 바꾸는 것은 `BeanFactoryPostProcessor` 가 하는 일이다.

<br/>

## 로컬 개발과 운영을 나눈다

프로필로 파일을 나눠두면 편하다.

```java
application.properties            # 공통
application-local.properties      # 로컬 (여기에만 비밀번호를 둬도 된다. gitignore 로 뺀다)
application-prod.properties       # 운영 (환경 변수만 참조)
```

```java
spring.profiles.active=local
```

<br/>

앞의 `@EventListener` 글에서 본 `@Profile("local")` 과 같이 쓰면,

로컬에서만 테스트 데이터를 넣는 것도 가능해진다.

<br/>

## 문자셋을 utf8 로 만든 것

```sql
create database facegram character set utf8
```

<br/>

앞의 attribute data type 글에서 본 문제가 여기서 생긴다.

MySQL의 `utf8` 은 진짜 UTF-8이 아니라 한 글자를 최대 `3` 바이트로만 저장한다.

<br/>

이모지는 `4` 바이트가 필요해서 안 들어간다.

```sql
INSERT INTO post (content) VALUES ('안녕 😀');
-> Incorrect string value: '\xF0\x9F\x98\x80'
```

<br/>

게시글이나 댓글에 이모지가 들어갈 수 있으니 `utf8mb4` 로 만드는 편이 낫다.

```sql
create database facegram character set utf8mb4 collate utf8mb4_unicode_ci;
```
