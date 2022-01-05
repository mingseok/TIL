**Reference** [https://jjingho.tistory.com/10](https://jjingho.tistory.com/10)

### 자바빈이란?

JSP 기반 웹 어플리케이션에서 정보를 표현할 때 사용하는 것 중 하나가 자바빈 이다.
<br/>
예를 들어, ‘회원 정보’, ‘게시판 글’ 등의 정보를 출력할 때, 정보를 저장하고 있는 자바빈 객체를 사용 하게 된다. (DTO 혹은 VO의 형태가 Java Bean이라고 생각하면 쉽다.)
<br/><br/>
JSP 프로그래밍에서 사용하는 자바빈 클래스는 위 코드처럼 데이터를 저장하는 필드, 데이터를 읽어올 때 사용되는 메서드 그리고 값을 저장할 때 사용되는 메서드로 구성.
<br/><br/>
### 자바빈 프로퍼티

프로퍼티는 자바빈에 저장되는 값을 나타낸다. 메서드 이름을 사용해서 프로퍼티의 이름을 결정하게 된다. 프로퍼티 값을 변경하는 메서드는 프로퍼티의 이름 중 첫 글자를 대문자로 변환한 문자열 앞에 set을 붙인다. 프로퍼티 값을 읽어오는 메서드는 프로퍼티의 이름 중 첫 글자를 대문자로 변환한 문자열 앞에 get을 붙인다.

```java
package chap08.member;

import java.util.Date;

public class MemberInfo {

	private String id;
	private String pwd;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}
```