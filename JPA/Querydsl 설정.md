## Querydsl 설정과 검증

<br/>

## 첫번째. build.gradle 설정

주석 되어 있는 부분들 추가해주기

```java
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	compileOnly 'org.projectlombok:lombok'


	// queryDSL 설정
	implementation "com.querydsl:querydsl-jpa"
	implementation "com.querydsl:querydsl-core"
	implementation "com.querydsl:querydsl-collections"
	annotationProcessor "com.querydsl:querydsl-apt:${dependencyManagement.importedProperties['querydsl.version']}:jpa" // querydsl JPAAnnotationProcessor 사용 지정
	annotationProcessor "jakarta.annotation:jakarta.annotation-api" // java.lang.NoClassDefFoundError (javax.annotation.Generated) 대응 코드
	annotationProcessor "jakarta.persistence:jakarta.persistence-api" // java.lang.NoClassDefFoundError (javax.annotation.Entity) 대응 코드



	// 쿼리 (?) 확인하기 위한 설정
	// 스프링 부트 3.0 이상이라면? -> implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.0'
	implementation 'com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.5.8'



	runtimeOnly 'com.h2database:h2'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

tasks.named('test') {
	useJUnitPlatform()
}

// Querydsl 설정부
def generated = 'src/main/generated'

// querydsl QClass 파일 생성 위치를 지정
tasks.withType(JavaCompile) {
	options.getGeneratedSourceOutputDirectory().set(file(generated))
}

// java source set 에 querydsl QClass 위치 추가
sourceSets {
	main.java.srcDirs += [ generated ]
}

// gradle clean 시에 QClass 디렉토리 삭제
clean {
	delete file(generated)
}
```


<br/><br/>

## 두번째. 엔티티 작성

패키지 위치 잘보기

```java
package study.querydsl.entity;

@Entity
@Getter @Setter
public class Hello {

    @Id @GeneratedValue
    private Long id;

}
```

<br/><br/>

## 세번째. 애플리케이션 실행

build 혹은 애플리케이션으로 빌드하면 `Q파일`이 생성된다.


<br/><br/>

## 네번째. build에서 Q파일 확인하기

파일 색갈이랑 위치는 좀 다를수있다 (중요한건 코드)

QHello 클래스를 클릭하였을때, 

밑에 있는 사진처럼 코드가 나온다면 제대로 된것이다.


![이미지](/programming/img/입문381.PNG)


<br/><br/>

## 다섯번째. gitignore 추가 설정

깃허브 커밋할때는 Q파일은 필요 없으니 제외시키는 것이다

```
### Querydsl
/src/main/generated
```

<br/><br/>


## 여섯번째. 메인 애플리케이션 테스트에서 확인해보기

테스트까지 통과 되었다면, 세팅에 있어서 더 이상 문제가 없는 것이다


```java
@SpringBootTest
@Transactional
class QuerydslApplicationTests {

	@Autowired
	EntityManager em;

	@Test
	void contextLoads() {
		Hello hello = new Hello();
		em.persist(hello);

		JPAQueryFactory query = new JPAQueryFactory(em);
		QHello qHello = new QHello("h");

		Hello result = query
				.selectFrom(qHello)
				.fetchOne();

		assertThat(result).isEqualTo(hello);
		assertThat(result.getId()).isEqualTo(hello.getId());
	}

}
```


<br/><br/>

>**Reference** <br/>[실전! Querydsl](https://www.inflearn.com/course/querydsl-%EC%8B%A4%EC%A0%84?_gl=1*lhve3a*_ga*OTY2ODU2MjYxLjE2NzkwNjYzNDU.*_ga_85V6SRKGJV*MTY5MjcwODMyNi40Mi4xLjE2OTI3MDgzMzMuNTMuMC4w)


<br/>

## 궁금증!

```java
Q 클래스가 언제 어떻게 만들어지는가
```

앞의 롬복이 만드는 메소드들이 생성되는 시점은 글에서 본 어노테이션 프로세싱이다.

다만 롬복과 방식이 다르다.

```java
롬복      - 컴파일러 내부 구조에 끼어들어 기존 클래스를 고친다 (규격 밖)
QueryDSL - 새 클래스 파일을 만들어낸다 (정석적인 사용법)
```

<br/>

`@Entity` 가 붙은 클래스를 찾아서 `Q` 로 시작하는 클래스를 생성한다.

```java
@Entity
public class Member {
    @Id private Long id;
    private String name;
    private int age;
}
```

```java
public class QMember extends EntityPathBase<Member> {
    public static final QMember member = new QMember("member");

    public final NumberPath<Long> id = createNumber("id", Long.class);
    public final StringPath name = createString("name");
    public final NumberPath<Integer> age = createNumber("age", int.class);
}
```

<br/>

필드마다 타입에 맞는 객체가 만들어지는 것이 핵심이다.

앞의 JPQL vs Querydsl 글에서 본 컴파일 검사가 이 타입 구분에서 나온다.

<br/>

## 빌드 산출물이라 생기는 문제들

```java
build/generated/sources/annotationProcessor/java/main/
```

<br/>

소스가 아니라 빌드 결과물이다. 그래서 몇 가지를 신경 써야 한다.

```java
clean 하면 사라진다        -> 컴파일을 다시 해야 IDE 가 인식한다
git 에 넣으면 안 된다      -> build/ 를 .gitignore 에 넣는다
엔티티를 고치면 다시 생성해야 한다
```

<br/>

세 번째가 헷갈리는 상황을 만든다.

엔티티에서 필드를 지웠는데 컴파일이 통과하는 경우가 있다.

옛 `Q` 클래스가 남아 있어서다. `clean build` 를 하면 드러난다.

<br/>

## JPAQueryFactory 를 빈으로 등록하는 이유

```java
@Configuration
public class QuerydslConfig {

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }
}
```

<br/>

앞의 스프링에서 빈을 등록하는 방법 글에서 본 `@Bean` 방식이다.

`JPAQueryFactory` 는 남이 만든 클래스라 `@Component` 를 붙일 수 없다.

<br/>

`EntityManager` 를 넘기는 이유는 앞의 JPQL vs Querydsl 글에서 본 대로다.

QueryDSL은 SQL을 직접 만들지 않고 JPQL을 만들어서 JPA에게 넘긴다.

```java
QueryDSL 코드 -> JPQL -> (하이버네이트가) SQL
```

<br/>

## 싱글톤인데 EntityManager 를 필드로 들고 있어도 되는가

앞의 싱글톤 레지스트리 글에서 본 `빈에 상태를 두면 안 된다` 를 어긴 것처럼 보인다.

<br/>

앞의 JPA 어떻게 동작하는가 글에서 본 대로, 주입되는 것은 프록시다.

```java
System.out.println(em.getClass());
// -> class jdk.proxy.$Proxy123
```

<br/>

이 프록시가 메서드 호출을 받을 때마다 `ThreadLocal` 에서 진짜 `EntityManager` 를 꺼낸다.

앞의 커넥션 풀, DataSource 글에서 본 그 방식이다.

<br/>

그래서 `JPAQueryFactory` 를 싱글톤으로 둬도 스레드마다 다른 영속성 컨텍스트를 쓴다.

<br/>

## 정적 임포트로 짧게 쓴다

```java
import static com.example.entity.QMember.member;

queryFactory.selectFrom(member).where(member.name.eq("민석")).fetch();
```

<br/>

`QMember.member` 를 매번 적으면 길어진다.

<br/>

다만 같은 엔티티를 두 번 조인해야 하면 별칭을 따로 만든다.

```java
QMember m1 = new QMember("m1");
QMember m2 = new QMember("m2");

queryFactory.select(m1, m2)
        .from(m1, m2)
        .where(m1.age.gt(m2.age))
        .fetch();
```

<br/>

생성자에 넘기는 문자열이 JPQL의 별칭이 된다.

앞의 페이징 API, 조인 글에서 본 세타 조인이 이런 형태가 된다.

<br/>

## 의존성 두 줄이 하는 일이 다르다

```java
implementation 'com.querydsl:querydsl-jpa:5.x:jakarta'
annotationProcessor 'com.querydsl:querydsl-apt:5.x:jakarta'
```

<br/>

앞의 롬복이 만드는 메소드들이 생성되는 시점은 글에서 본 것과 같은 구분이다.

```java
implementation      - 실행할 때 필요하다 (JPAQueryFactory, BooleanExpression 등)
annotationProcessor - 컴파일할 때만 필요하다 (Q 클래스를 만든다)
```

<br/>

롬복은 `compileOnly` 인데 QueryDSL은 `implementation` 인 이유가 여기 있다.

롬복이 만든 코드는 `.class` 에 박히지만, QueryDSL은 실행 중에도 라이브러리를 쓴다.
