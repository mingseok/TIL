## **DIP: 의존관계 역전 원칙**

- 프로그래머는 “추상화에 의존해야지, 구체화에 의존하면 안된다.”
    
    - 의존성 주입은 이 원칙을 따르는 방법 중 하나다. 
    
    - **즉, 클라이언트 코드가 구현클래스를 바라 보지 말고,** 
    
    - **인터페이스만 바라 보라는 뜻이 된다. == 멤버 서비스가 멤버리파지토리 인터페이스만** 
    
    - **바라 보고 메모리 리파지토리나, JDBC리파지토리 이런건 몰라야 된다는 뜻이다.**
    
- 쉽게 이야기해서 구현 클래스에 의존하지 말고, 인터페이스에 의존하라는 뜻
- 앞에서 이야기한 역할(Role)에 의존하게 해야 한다는 것과 같다.
- 객체 세상도 클라이언트가 인터페이스에 의존해야 유연하게 구현체를 변경 할 수 있다. <br/>구현체에 의존하게 되면 변경이 아주 어려워 진다.


<br/>

### **객체 세상도 클라이언트가 인터페이스에 의존해야 유연하게 구현체를 변경할 수 있는 것이다.**

<br/>

## 정리

- 객체 지향의 핵심은 다형성
- 다형성 만으로는 쉽게 부품을 갈아 끼우듯이 개발할 수 없다.

- 다형성 만으로는 구현 객체를 변경할 때 클라이언트 코드도 함께 변경된다.
- 다형성 만으로는 OCP, DIP를 지킬 수 없다.
- 뭔가 더 필요하다.


<br/>

>**Reference** <br/>스프링 핵심 원리 - 기본편 : https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8
