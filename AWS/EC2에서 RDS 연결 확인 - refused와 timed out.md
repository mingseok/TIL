## EC2에서 RDS 연결 확인 - refused와 timed out

<br/>

## 앱을 띄우기 전에 연결부터 확인한다

앱을 올렸는데 안 되면 원인이 앱인지 네트워크인지 DB인지 모른다.

층을 하나씩 확인하면 어디서 막혔는지 바로 나온다.

```java
1. 이름이 풀리나        - DNS
2. 포트가 열렸나        - 네트워크 (보안 그룹, 라우팅)
3. 로그인이 되나        - 인증
4. 앱 설정이 맞나       - 앱
```

<br/><br/>

## 1. 이름이 풀리나

```java
$ nslookup mydb.xxxx.ap-northeast-2.rds.amazonaws.com
Address: 10.0.20.15
```

<br/>

EC2 안에서 조회하면 사설 IP가 나와야 한다.

앞의 VPC 글에서 본 대로 VPC 안의 DNS가 사설 IP를 돌려준다.

<br/>

안 나오면 엔드포인트를 잘못 적었거나, RDS가 다른 VPC에 있다.

<br/><br/>

## 2. 포트가 열렸나

## 궁금증!

```java
같은 명령이 상황마다 어떻게 다르게 답하나
```

로컬에서 세 가지 상황을 만들어봤다.

```java
1) 3306 에 무언가가 듣고 있는 상태     nc -zv 127.0.0.1 3306
   Connection to 127.0.0.1 port 3306 [tcp/mysql] succeeded!

2) 아무도 안 듣는 포트                nc -zv 127.0.0.1 3307
   nc: connectx to 127.0.0.1 port 3307 (tcp) failed: Connection refused

3) 응답이 없는 주소                   nc -zv -G 2 10.255.255.1 3306
   nc: connectx to 10.255.255.1 port 3306 (tcp) failed: Operation timed out
```

<br/>

세 답이 다르고, 각각 뜻이 다르다.

```java
succeeded  - 도달했고 서비스가 있다. 네트워크 층은 끝
refused    - 도달했는데 그 포트에 아무도 없다. RST 가 돌아온 것
timed out  - 도달 자체가 안 됐다. 아무 답이 없다
```

<br/>

앞의 TCP 3 way handshake 글에서 본 대로 SYN을 보냈을 때 세 가지가 가능하다.

```java
SYN-ACK 가 온다   - succeeded
RST 가 온다       - refused. 서버는 있는데 그 포트가 안 열렸다
아무것도 안 온다   - timed out. 중간에서 버려졌다
```

<br/>

## refused 면

RDS까지 갔다. 네트워크는 정상이다.

```java
RDS 가 아직 생성 중이거나 멈춰 있다
포트가 3306 이 아니다 (PostgreSQL 은 5432)
```

<br/>

RDS는 보안 그룹이 막으면 RST를 안 보내고 버린다. 그래서 RDS에서 `refused` 는 드물다.

<br/>

## timed out 이면

중간 어딘가에서 패킷이 버려졌다. 거의 항상 보안 그룹이다.

```java
1. RDS 의 보안 그룹 인바운드에 3306 이 있나
2. 그 규칙의 출발지가 EC2 의 보안 그룹(또는 EC2 의 IP) 인가
3. EC2 와 RDS 가 같은 VPC 인가. 다르면 피어링과 라우트가 있나
4. NACL 이 기본값인가
```

<br/>

앞의 보안 그룹과 NACL 글에서 본 대로 `출발지를 보안 그룹으로` 지정했다면 EC2에 그 보안 그룹이 실제로 붙어 있는지 본다.

규칙은 맞는데 EC2에 다른 보안 그룹이 붙어 있는 경우가 흔하다.

<br/>

`-G 2` 는 2초 기다리고 포기하라는 것이다. 없으면 기본 75초를 기다린다.

앞의 타임아웃은 왜 반드시 걸어야 하나 글에서 본 대로 확인 명령에도 타임아웃을 건다.

<br/><br/>

## 3. 로그인이 되나

포트가 열렸으면 클라이언트로 붙어본다.

```java
$ mysql -h mydb.xxxx.ap-northeast-2.rds.amazonaws.com -u admin -p
```

<br/>

```java
Access denied for user 'admin'@'10.0.10.5'    - 비밀번호나 사용자 틀림. 네트워크는 정상
Unknown database 'mydb'                        - DB 이름 틀림. 로그인은 됐다
mysql>                                         - 성공
```

<br/>

`Access denied` 가 나면 오히려 다행이다. 네트워크와 DB 프로세스는 다 정상이라는 뜻이다.

<br/>

클라이언트가 없으면 설치한다. Amazon Linux 2023은 `mariadb105` 패키지로 `mysql` 명령을 쓸 수 있다.

<br/><br/>

## 4. 앱의 오류 메시지 읽기

앱 로그의 예외가 위 층 중 어디인지 알려준다.

```java
Communications link failure                          - 네트워크. 2 번 층. timed out 과 같은 상황
Access denied for user                               - 인증. 3 번 층
Unknown database                                     - DB 이름. 4 번 층
Connection is not available, request timed out       - 커넥션 풀이 30 초 기다렸다 포기. 원인은 위 중 하나
```

<br/>

마지막 것이 헷갈린다. HikariCP의 메시지라 앱 문제 같은데, 실제 원인은 그 앞에 찍힌 `Communications link failure` 다.

앞의 커넥션 풀 글에서 본 대로 풀이 커넥션을 못 만들어서 기다리다 포기한 것이다.

<br/>

앱을 띄우기 전에 `nc` 한 번이면 이 30초씩 기다리는 시행착오를 안 한다.

<br/><br/>

## 내 컴퓨터에서는 안 되는데 EC2 에서는 되는 이유

```java
RDS 가 퍼블릭 액세스 아님       - 공인 IP 가 없다. 내 컴퓨터에서는 이름조차 안 풀리거나 사설 IP 가 나온다
보안 그룹이 EC2 만 허용         - 내 IP 는 없다
```

<br/>

정상이다. 그렇게 되어 있어야 한다.

내 컴퓨터에서 붙어야 하면 앞의 RDS 접속 글에서 본 터널링을 쓴다.

<br/><br/>

## 정리

```java
층 순서      - DNS -> 포트 -> 인증 -> 앱 설정. 위에서부터
nc -zv      - succeeded / refused / timed out 이 각각 다른 층을 가리킨다
timed out   - 보안 그룹부터. 규칙과 실제 붙은 보안 그룹이 다른 경우가 흔하다
앱 로그      - Communications link failure 는 네트워크, Access denied 는 인증
```
