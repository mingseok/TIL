## DTO를 만들어 두면 무슨 장점이 있나요?

<br/>

## 궁금증. DTO와 entity 차이는 뭘까?

entity가 디비와 연동하는데 쓰는데 클래스라면 dto는 프론트와 데이터를 주고받을 때 쓰는 클래스이다 

db에 있는 데이터를 바로 프론트에 뿌려주지 않고 ui관련 데이터도 담아줄 dto가 필요하다고 하다고 생각한다.


<br/>


## DTO란?

계층간의 데이터 교환을 위한 객체를 말합니다. 

데이터를 전달하기 위해 사용하는 객체이다.

```java
데이터 담아서 전달하는 바구니
```

- 오직, 게터, 세터 메서드만 갖는다.

- 다른 로직은 갖지 않는다.




<br/>

DTO는 일반적으로 데이터 전송 및 객체 간 데이터 교환을 간단하게 

만들어주는 패턴으로, 복잡한 애플리케이션에서 많은 이점을 제공합니다.

<br/>

자바의 메소드를 선언할 때 리턴 타입은 한 가지만 선언할 수 있다.

```java
public class MemberDTO {
  public String name;
  public String phone;
  public String email;
}
```

그렇기에, 이처럼 DTO를 만들어 놓으면 메소드의 리턴 타입에 MemberDTO로 선언하고 그 객체를 리턴해 주면 된다.

<br/>

### 예를 들어, 이렇게 말이다

```java
public MemberDTO getMemberDTO() {
  MemberDTO dto = new MemberDTO();
  
  // ... 생략
  
  return dto;
}
```

<br/>

## 이렇게, 설명하는 이유는 생성자 때문이다

객체를 생성할 때 그 사람의 아무 정보도 모를 때도 있고, 이름만 알 때도 있고, 

이름과 전화번호만 알 때도 있고, 모든 정보를 알고 있을 때도 있다.

<br/>

이러한 각각의 상황에 따른 생성자를 MemberDTO에 추가하면 다음과 같이 구현 할 수 있는 것이다

```java
// 아무 정보도 모를 때
public MemberDTO() {
}

// 이름만 알 때
public MemberDTO(String name) {
  this.name = name;
}

// 이름과 전화번호만 알 때
public MemberDTO(String name, String phone) {
  this.name = name;
  this.phone = phone;
}

// 모든 정보를 알고 있을 때
public MemberDTO(String name, String phone, String email) {
  this.name = name;
  this.phone = phone;
  this.email = email;
}
```

너무 많이 만드는 것은 좋지 않다.

꼭 필요에 맞는 생성자만 만드는 습관을 들여야만 한다.

<br/>

## 궁금증!

```java
엔티티를 그대로 컨트롤러에서 반환하면 정말 안 될까? 한 번 더 만드는 게 번거로운데
```

세 가지가 걸린다. 하나씩 보면 왜 굳이 만드는지 납득이 된다.

<br/>

## 첫번째) 숨겨야 할 것까지 나간다

```java
@Entity
class Member {
    private Long id;
    private String email;
    private String password;      // 이것도 같이 나간다
    private String phone;
}
```

<br/>

이걸 그대로 반환하면 응답 JSON에 비밀번호와 전화번호가 들어간다.

`@JsonIgnore` 를 붙여서 막을 수는 있지만, 필드를 새로 추가할 때마다 매번 기억해야 한다.

<br/>

DTO는 방향이 반대다. 넣기로 한 것만 들어간다.

빠뜨려서 안 나가는 것은 안전한 실수지만, 빠뜨려서 나가는 것은 사고다.

```java
엔티티 반환 -> 막는 것을 잊으면 나간다 (기본이 공개)
DTO 반환   -> 넣는 것을 잊으면 안 나간다 (기본이 비공개)
```

<br/>

## 두번째) 양방향 연관관계면 무한 루프에 빠진다

`Member` 가 `Team` 을 참조하고 `Team` 이 다시 `Member` 목록을 참조하는 구조를 만들어봤다.

```java
class Member {
    String name;
    Team team;

    @Override
    public String toString() {
        return "{name=" + name + ", team=" + team + "}";
    }
}

class Team {
    String name;
    List<Member> members = new ArrayList<>();

    @Override
    public String toString() {
        return "{name=" + name + ", members=" + members + "}";
    }
}
```

<br/>

```java
Team team = new Team("백엔드");
Member member = new Member("민석", team);
team.members.add(member);

System.out.println(member);
```

<br/>

### 결과

```java
StackOverflowError - 서로를 계속 따라간다
```

<br/>

`Member` 를 찍으려면 `Team` 을 찍어야 하고, `Team` 을 찍으려면 그 안의 `Member` 를 찍어야 한다.

끝이 없다.

<br/>

JSON으로 바꿀 때도 똑같은 일이 일어난다.

`@JsonIgnore` 나 `@JsonManagedReference` 로 한쪽을 끊어야 하는데,

그러면 엔티티가 화면 사정을 알게 되는 셈이라 이상해진다.

<br/>

DTO를 쓰면 필요한 것만 평평하게 담으니 이 문제가 아예 안 생긴다.

```java
class MemberResponse {
    private String name;
    private String teamName;    // 팀 객체가 아니라 이름만
}
```

<br/>

## 세번째) API 스펙이 DB 스키마에 묶인다

컬럼 이름을 하나 바꾸면 API 응답의 키 이름도 같이 바뀐다.

DB를 정리하려는 것뿐인데 앱을 쓰는 사용자까지 영향을 받는 것이다.

<br/>

반대 방향도 마찬가지다. 화면에서 필드 이름 하나 바꿔달라고 하면 컬럼을 바꿔야 한다.

<br/>

DTO가 사이에 끼면 그 둘이 분리된다.

컬럼이 `user_nm` 이든 `member_name` 이든, DTO에서 `name` 으로 바꿔 담으면 밖에서는 안 바뀐다.

```java
엔티티 -> DB 사정을 담당한다
DTO   -> 화면과 API 사정을 담당한다
```

<br/>

## 요즘은 setter 없이 만든다

원문의 `오직 게터, 세터 메서드만 갖는다` 는 전통적인 설명이고, 지금은 좀 다르게 쓴다.

<br/>

응답 DTO는 만들고 나면 값이 바뀔 일이 없으니 `setter` 를 안 둔다.

```java
public class MemberResponse {

    private final String name;
    private final String teamName;

    public MemberResponse(Member member) {
        this.name = member.getName();
        this.teamName = member.getTeam().getName();
    }

    public String getName() {
        return name;
    }

    public String getTeamName() {
        return teamName;
    }
}
```

<br/>

생성자에서 엔티티를 받아 변환까지 해두면, 컨트롤러가 한 줄로 끝난다.

```java
return new MemberResponse(member);
```

<br/>

`Java 16` 부터는 `record` 로 더 짧게 쓸 수 있다.

```java
public record MemberResponse(String name, String teamName) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getName(), member.getTeam().getName());
    }
}
```

`record` 는 필드가 전부 `final` 이고 게터와 `equals`, `hashCode`, `toString` 까지 자동으로 만들어준다.

원문에서 말한 `데이터를 담아 전달하는 바구니` 에 딱 맞는 문법이다.

<br/>

## 생성자를 여러 개 만드는 것보다는

원문 마지막의 `상황별 생성자` 는 필드가 늘어나면 감당이 안 된다.

`이름만`, `이름과 전화번호`, `이름과 이메일` 까지 가면 타입이 같아서 오버로딩도 안 된다.

```java
MemberDTO(String name)
MemberDTO(String name, String phone)
MemberDTO(String name, String email)   // 컴파일 에러. 위와 시그니처가 같다
```

<br/>

그래서 요즘은 `용도별로 DTO를 따로 만든다`.

```java
MemberCreateRequest    // 가입할 때 받는 것
MemberUpdateRequest    // 수정할 때 받는 것
MemberResponse         // 내려줄 때 쓰는 것
MemberSummaryResponse  // 목록에서 쓰는 것
```

<br/>

클래스 개수는 늘어나지만, 각각이 `언제 쓰이는 것인지` 이름에 드러난다.

생성자가 네 개 달린 DTO 하나보다 읽기가 훨씬 낫다.
