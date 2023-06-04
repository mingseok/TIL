## 영속성 전이: CASCADE

- 특정 엔티티를 영속 상태로 만들 때 연관된 엔티티도 함께 영속상태로 만들도 싶을 때

    - ex) 부모 엔티티를 저장할 때 연관된 자식 엔티티도 함께 저장하고 싶을때 사용한다.

<br/>

### `Parent.class`

```java
@Entity
public class Parent {

    // 생략..

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL) // 핵심
    private List<Child> childList = new ArrayList<>();

    public void addChild(Child child) {
        childList.add(child);
        child.setParent(this);
    }

    // 게터, 세터 생략..
}
```

<br/>

`Child.class`

```java
@Entity
public class Child {

    // 생략..

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;

    // 게터, 세터 생략..
}
```

<br/>

## 결과값


![이미지](/programming/img/입문375.PNG)

<br/><br/>

## 영속성 전이: CASCADE - 주의!

- 영속성 전이는 연관관계를 매핑하는 것과 아무 관련이 없음

- 엔티티를 영속화할 때 연관된 엔티티도 함께 영속화하는 편리함을 제공할 뿐