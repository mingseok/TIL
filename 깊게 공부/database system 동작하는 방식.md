## database system 동작하는 방식

<br/>

## 데이터베이스란?

```java
"전자적으로 저장되고, 사용"되는 "관련있는 데이터"들의 "조직화된 집합"을 말한다.
```

### `database system`이란?

- database + DBMS + 연관된 applications

    - 줄여서 database라고도 부르기에, 문맥에 따라서 잘 파악해야 한다.

<br/><br/>

## `database system` 동작하는 방식

![이미지](/programming/img/입문417.PNG)

https://www.youtube.com/watch?v=aL0XXc1yGPs&list=PLcXyemr8ZeoREWGhhZi5FZs6cvymjIBVe

1. 처음에, `사용자`나 `개발자`가 있을 것이다.

2. 우린 개발자이기에, `애플리케이션 프로그램`을 만들었다.

3. 데이터 베이스에 접근하기 위한, 쿼리들을 `DBMS Software`에서 `쿼리`들을 받는다.

4. 그리고, 그 `쿼리`가 무엇을 의미하는지 `분석`한다.

5. `쿼리`가 `요청`한 게 무엇인지 `파악`이 되면, 그 요청을 처리하기 전에

6. 데이터가 어떤 형태로 되어 있는지에 대한 추가적인 정보를 알아야, 
    
    데이터를 `읽어` 올 수 있기에, 추가적인 정보를 확인한다.
    
7. 그, 추가적인 정보를 바탕으로 `실제 요청` 받은 데이터를 찾는다.
8. 찾았다면, `애플리케이션`으로 돌려주게 되는 것이다.

```java
이렇게 1 ~ 8번까지 동작하는 방식이 "database system"이 동작하는 방식이다.
```