## 동작하기 전에 실행하는것


```java
    @BeforeEach // 
    public void beforeEach() {
        // 이렇게 사용하는 이유는. 같은 메모리를 사용하기 위해서 이다. 'DI'
        // 1. new MemoryMemberRepository(); 생성하고
        // 2. 윗 부분 MemoryMemberRepository memberRepository; 저장하고
        // 3. new MemberService(memberRepository); 넣어서 MemberService클래스가 가지고 간다.
        // 4. MemberService 클래스에 있는
        // 5. this.memberRepository = memberRepository; 에 의해
        // 6. MemberService 클래스에 있는
        // 7. private final MemberRepository memberRepository; 에 저장 되는 것이다.

        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

```    