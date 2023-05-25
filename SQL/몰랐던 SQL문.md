## 별칭 사용

```sql
"select m from Member m where m.username = :username"
```

`"FROM Member m"` 구문에서 `"m"`은 `"Member"` 엔티티에 대한 별칭(alias)입니다. 

따라서 이후의 쿼리에서 `"m"`을 사용하여 `"Member"` 엔티티의 컬럼에 접근할 수 있습니다.

<br/>

```sql
from Member m 이렇게 했기 때문에 가능한 것이다.
```

즉, `"FROM Member m"` 구문은 `"Member"` 엔티티를 `"m"`이라는 별칭으로 지정하는 것을 의미합니다. 

이후의 쿼리에서는 `"m"`을 사용하여 `"Member"` 엔티티에 접근할 수 있습니다.


<br/><br/>

## :username 설명

```java
public Optional<Member> find(String username) {
    return em.createQuery("select m from Member m where m.username = :username", Member.class)
            .setParameter("username", username)
            .getResultList().stream().findAny();
}
```

`":username"`은 SQL 쿼리에서 사용되는 바인딩 변수(binding variable)의 이름입니다.

`바인딩 변수`란, 쿼리를 실행할 때 값을 대입해야 하는 매개변수`(String username)`를 의미합니다. 

예를 들어, 아래와 같은 쿼리에서 `":username"`은 바인딩 변수입니다.