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

