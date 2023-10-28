## DTO를 만들어 두면 무슨 장점이 있나요?


### DTO란?

계층간의 데이터 교환을 위한 객체를 말합니다. 

데이터를 전달하기 위해 사용하는 객체이다.

<br/>


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

<br/><br/>

## 이렇게, 설명하는 이유는 생성자 때문이다

객체를 생성할 때 그 사람의 아무 정보도 모를 때도 있고,

이름만 알 때도 있고, 이름과 전화번호만 알 때도 있고, 모든 정보를 알고 있을 때도 있다.

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