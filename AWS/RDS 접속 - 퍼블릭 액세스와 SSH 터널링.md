## RDS 접속 - 퍼블릭 액세스와 SSH 터널링

<br/>

## 내 컴퓨터에서 RDS 에 붙는 두 가지 길

```java
1. 퍼블릭 액세스   - RDS 에 공인 IP 를 주고 인터넷에서 직접
2. SSH 터널링     - EC2 를 거쳐서. RDS 는 프라이빗에 그대로
```

<br/>

편한 것은 1번이고 맞는 것은 2번이다.

<br/><br/>

## 퍼블릭 액세스

RDS를 만들 때 `퍼블릭 액세스: 예` 로 두고, 보안 그룹에 내 IP를 3306으로 열면 된다.

```java
IntelliJ DB 도구
  Host: mydb.xxxx.ap-northeast-2.rds.amazonaws.com
  Port: 3306
  User / Password
```

<br/>

이러면 DB가 인터넷에 있는 것이다.

보안 그룹이 내 IP만 허용하니 남은 못 붙지만, 그 규칙 하나가 전부다.

앞의 보안 그룹과 NACL 글에서 본 대로 `테스트하느라 잠깐 0.0.0.0/0` 이 여기서 제일 위험하다.

<br/>

집 IP가 바뀌면 또 막힌다. 그때마다 보안 그룹을 고친다.

<br/><br/>

## SSH 터널링

RDS는 프라이빗 서브넷에 두고, EC2를 통로로 쓴다.

```java
내 컴퓨터:13306 --SSH--> EC2 --> RDS:3306
```

<br/>

```java
ssh -i key.pem -N -L 13306:mydb.xxxx.rds.amazonaws.com:3306 ec2-user@<EC2 IP>
```

<br/>

```java
-N       명령을 실행하지 않고 터널만
-L       로컬 포트 포워딩. 내 13306 을 저쪽 3306 으로
```

<br/>

이 명령을 띄워두면 내 컴퓨터의 `localhost:13306` 이 RDS가 된다.

```java
IntelliJ
  Host: localhost
  Port: 13306
```

<br/>

IntelliJ는 이걸 내장하고 있다. 연결 설정의 `SSH/SSL` 탭에서 EC2 정보를 넣으면 터널을 알아서 연다.

<br/><br/>

## 무슨 일이 일어나는가

```java
1. 내 컴퓨터가 EC2 에 SSH 로 붙는다 (22 번. 암호화된 통로)
2. IntelliJ 가 localhost:13306 에 접속한다
3. SSH 클라이언트가 그 연결을 잡아서 통로 안으로 넣는다
4. EC2 쪽 SSH 서버가 통로에서 꺼내 RDS:3306 으로 보낸다
5. 응답은 반대로
```

<br/>

RDS 입장에서는 EC2가 접속한 것이다. EC2의 보안 그룹만 허용되어 있으면 된다.

내 컴퓨터의 IP는 RDS에 안 나타난다.

<br/>

앞의 EC2 접속 글에서 본 배스천 호스트가 이 EC2 역할이다.

<br/><br/>

## 이게 왜 나은가

```java
RDS 에 공인 IP 가 없다. 인터넷에서 존재 자체가 안 보인다
열린 포트가 22 번 하나. 그것도 EC2 에만
DB 비밀번호가 새도 통로가 없으면 못 쓴다
집 IP 가 바뀌어도 EC2 의 22 번만 고치면 된다 (또는 SSM 으로 그것도 없앤다)
```

<br/>

앞의 VPC 글에서 본 `DB 는 프라이빗 서브넷에` 가 이것으로 가능해진다.

<br/><br/>

## Session Manager 포트 포워딩

SSH 없이도 된다.

```java
CLI 의 ssm start-session 에
  --document-name AWS-StartPortForwardingSessionToRemoteHost
  --parameters host=<RDS 엔드포인트>,portNumber=3306,localPortNumber=13306
```

<br/>

앞의 EC2 접속 글에서 본 SSM이 터널링도 한다. 22번 포트도, 키 파일도 필요 없다.

권한은 IAM으로 준다. 누가 DB에 터널을 열었는지 기록에 남는다.

<br/><br/>

## 자주 겪는 문제

```java
터널은 열렸는데 DB 접속이 안 된다
  -> EC2 의 보안 그룹이 RDS 의 보안 그룹에 3306 으로 허용되어 있나. 앞의 EC2에서 RDS 연결 확인 글

터널이 자꾸 끊긴다
  -> SSH 유휴 타임아웃. ServerAliveInterval 60 을 SSH 설정에

localhost:13306 이 이미 사용 중
  -> 이전 터널 프로세스가 살아 있다. 죽이거나 다른 포트로
```

<br/><br/>

## 운영 DB 에 직접 붙는 것 자체를 줄인다

터널이 있어도 사람이 운영 DB에 붙어서 쿼리를 날리는 것은 최소화한다.

```java
UPDATE 를 WHERE 없이 실행
운영 중에 무거운 SELECT 로 락
```

<br/>

앞의 DB 락 글에서 본 그런 일이 사람 손에서 난다.

<br/>

읽기는 앞의 RDS 글에서 본 리드 레플리카에 붙고, 쓰기는 마이그레이션 스크립트로 검토를 거쳐 실행하는 것이 관례다.

터널은 `붙을 수 있는 길` 이고, `붙어야 하는가` 는 별개다.

<br/><br/>

## 정리

```java
퍼블릭 액세스  - 편하지만 DB 가 인터넷에. 보안 그룹 하나에 의존
SSH 터널링    - EC2 를 거쳐 프라이빗 RDS 로. localhost:13306 = RDS
SSM 터널링    - SSH 도 키도 없이. IAM 권한. 기록 남음
원칙         - 운영 DB 에 사람이 직접 붙는 일 자체를 줄인다
```
