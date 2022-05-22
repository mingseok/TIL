## Model

`ControllerV3 인터페이스` 만들기 생성하기.

```java
package hello.servlet.web.frontcontroller.v3;

public interface ControllerV3 {

	// 여기서 paramMap 은 매개변수이다.
    ModelView process(Map<String, String> paramMap);
    
}
```

<br/>

`MemberFormControllerV3` 클래스 생성

```java
package hello.servlet.web.frontcontroller.v3.controller;

import java.util.Map;

public class MemberFormControllerV3 implements ControllerV3 {

    @Override
    public ModelView process(Map<String, String> paramMap) {
        return new ModelView("new-form");
    }
}
```

<br/>

`MemberSaveControllerV3` 클래스 생성

<br/>

`ModelView` 클래스 안에 있는 맵에 저장 할때는 `‘members/save'` 가 왔을때 저장 하는 것이다.

`mv.getModel().put("member", member);` 이 부분에서 저장 하는 것이다.

<br/>getModel() 로 가져와서 put() 메서드를 사용하여 저장 하는 것이다.

```java
package hello.servlet.web.frontcontroller.v3.controller;

import hello.servlet.domain.MemberRepository;
import hello.servlet.domain.member.Member;
import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;

import java.util.Map;

public class MemberSaveControllerV3 implements ControllerV3 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(Map<String, String> paramMap) {
        String username = paramMap.get("username");
        int age = Integer.parseInt(paramMap.get("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        ModelView mv = new ModelView("save-result");
        mv.getModel().put("member", member);
        return mv;
    }

}
```


<br/>

>**Reference** <br/>스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술 - https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1