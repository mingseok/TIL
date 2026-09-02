## CloudWatch - 지표, 로그, 경보

<br/>

## 세 가지를 한다

```java
지표(Metrics)   - 숫자의 시계열. CPU 40%, 커넥션 12개, 힙 800MB
로그(Logs)      - 텍스트. 앱 로그를 모아서 검색
경보(Alarms)    - 지표가 조건을 넘으면 알림
```

<br/>

앞의 ALB 글에서 본 헬스체크는 `받을 수 있나` 만 본다. 왜 느린지, 무엇이 차오르는지는 여기서 본다.

<br/><br/>

## 기본으로 오는 지표

```java
EC2   - CPUUtilization, NetworkIn/Out, DiskReadOps, StatusCheckFailed
RDS   - CPUUtilization, DatabaseConnections, FreeableMemory, FreeStorageSpace, ReadLatency
ALB   - RequestCount, TargetResponseTime, HTTPCode_Target_5XX_Count, UnHealthyHostCount
```

<br/>

EC2 기본 지표에 메모리가 없다.

AWS는 하이퍼바이저 밖에서 보기 때문에 CPU와 네트워크는 알지만, 운영체제 안의 메모리 사용량은 모른다.

디스크 사용률(용량)도 없다. IOPS만 있다.

<br/>

메모리와 디스크 용량은 인스턴스 안에 CloudWatch 에이전트를 깔아서 보내야 한다.

앞의 페이지 캐시 글에서 본 대로 `used` 와 `available` 을 구분해서 보내는 설정을 확인한다. 페이지 캐시를 사용 중으로 세면 항상 90%로 보인다.

<br/><br/>

## 앱 지표를 보낸다

운영체제 지표만으로는 부족하다. JVM 안이 안 보인다.

```java
힙 사용량                 - GC 뒤에도 안 내려가면 누수. 앞의 ThreadLocal 내부 구조 글
HikariCP 활성/대기 커넥션  - 대기가 생기면 풀이 작거나 쿼리가 느린 것. 앞의 커넥션 풀 글
톰캣 스레드 사용량         - 앞의 스레드 풀 글
GC 시간                   - 앞의 가비지 컬렉션 글
```

<br/>

스프링 부트의 Micrometer가 이 지표를 이미 다 갖고 있다. CloudWatch로 보내는 레지스트리를 붙이면 된다.

<br/>

실제로 붙이면서 걸린 것이 있다.

```java
스프링 부트 4 에는 CloudWatch 레지스트리 자동 설정이 없다   -> 빈을 직접 등록
서버가 두 대면 지표가 섞인다                              -> host 태그를 붙여서 구분
커넥션 풀이 여러 개면 역시 섞인다                          -> 풀 이름 태그
```

<br/>

태그 없이 보내면 `힙 800MB` 가 어느 서버인지 모른다. 지표를 만들 때 `무엇으로 나눠 볼 것인가` 를 먼저 정한다.

<br/><br/>

## 경보

지표가 조건을 넘으면 SNS로 알린다. SNS가 이메일이나 슬랙 웹훅으로 보낸다.

```java
CPU > 80%            5 분 연속
디스크 사용률 > 85%
메모리 사용률 > 90%
JVM 힙 > 85%         GC 뒤에도
HikariCP 대기 > 0     1 분 연속        <- 커넥션을 못 받아 기다리는 스레드가 있다
RDS 커넥션 > 80% of max_connections
ALB 5XX > 10/분
```

<br/>

실제로 서버 두 대에 이런 식으로 열 몇 개를 걸어두고 있다.

<br/>

## 조건의 두 축

```java
임계값    - 얼마를 넘으면
기간      - 얼마 동안 연속으로
```

<br/>

`CPU > 80%` 를 1분으로 걸면 배포할 때마다 울린다. 5분으로 걸면 진짜 부하만 잡힌다.

`디스크 > 85%` 는 1분이어도 된다. 디스크는 튀지 않는다.

지표의 성질에 따라 기간이 다르다.

<br/>

## 데이터 부족

인스턴스가 꺼지거나 에이전트가 죽으면 지표가 안 온다.

```java
missing data 처리
  notBreaching  - 정상으로 본다. 서버가 죽었는데 조용하다 (위험)
  breaching     - 경보로 본다. 서버가 죽으면 울린다
```

<br/>

`서버가 죽어서 지표가 안 오는 것` 이야말로 알아야 할 상황인데, 기본값에 따라 조용히 넘어간다.

지표를 안 보내는 것 자체가 신호라고 보면 `breaching` 이 맞다.

<br/><br/>

## 로그

앱 로그를 CloudWatch Logs로 보내면 서버에 들어가지 않고 검색한다.

```java
로그 그룹    - 앱 단위. /app/api
로그 스트림  - 서버 단위. 인스턴스 ID
```

<br/>

서버가 두 대면 어느 쪽 로그인지 SSH로 두 번 들어가 봐야 하는데, 여기 모으면 한 번에 검색된다.

<br/>

보내려면 로그가 어디에 쓰이는지 알아야 한다.

```java
systemd 유닛의 표준 출력으로     -> journalctl 에 있다
파일로 (logback 의 FileAppender) -> journalctl 에 없다. 파일 경로를 에이전트에 알려줘야 한다
```

<br/>

앱이 파일로 쓰고 있는데 `journalctl` 만 보고 `로그가 없다` 고 헤맨 적이 있다.

<br/>

## Logs Insights

모인 로그를 쿼리로 검색한다.

```java
fields @timestamp, @message
| filter @message like /ERROR/
| stats count() by bin(5m)
```

<br/>

`5분 단위 에러 수` 가 한 줄이다. 앞의 로깅 글에서 본 대로 로그를 JSON으로 남기면 필드 단위로 필터가 된다.

<br/><br/>

## 비용

```java
기본 지표          - 무료
커스텀 지표        - 개당 월 $0.30. 태그 조합마다 별개로 센다
경보              - 개당 월 $0.10
로그 수집          - GB 당 $0.50. 저장은 별도
상세 모니터링      - 1 분 단위. 인스턴스당 월 약 $2. 기본은 5 분
```

<br/>

커스텀 지표가 함정이다. `힙 사용량 × 서버 2대 × 메모리 영역 4개` 면 지표 8개다. Micrometer가 내보내는 것을 전부 보내면 수백 개가 되어 월 $100이 넘는다.

필요한 것만 필터해서 보낸다.

<br/>

로그 보존 기간을 안 정하면 영구 보존이다. 30일이나 90일로 정한다.

앞의 AWS 비용 글에서 본 `안 쓰는 것에 나가는 돈` 의 로그 버전이다.

<br/><br/>

## 정리

```java
지표     - EC2 기본에 메모리가 없다. 에이전트. 앱 지표는 Micrometer 로. 태그로 서버 구분
경보     - 임계값 + 기간. 지표 성질에 따라 기간이 다르다. 데이터 부족은 breaching
로그     - 파일로 쓰는지 stdout 인지 먼저. Insights 로 쿼리
비용     - 커스텀 지표 개수와 로그 보존 기간을 정한다
```
