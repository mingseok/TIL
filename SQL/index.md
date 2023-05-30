## 제약, 인덱스

이처럼 열에 대해 정의하는 제약을 '`열 제약`'이라고 부른다.

```sql
create table sample631 (
    a integer NOT NULL,
    b integer NOT NULL UNIQUE,
    c varchar(30)
);
```

<br/><br/>

## 테이블 제약 정의하기

한개의 제약으로 복수의 열에 제약을 설명하는 경우를 '`테이블 제약`'이라고 부른다.

```sql
create table sample632 (
    no integer NOT NULL,
    sub_no integer NOT NULL,
    name varchar(30),
    PRIMARY KEY (no, sub_no)
);
```


<br/><br/>

## 제약 이름 붙이기

제약에 이름을 붙이면 관리하기 쉬워진다.

```sql
create table sample632 (
    no integer NOT NULL,
    sub_no integer NOT NULL,
    name varchar(30),
    CONSTRAINT pkey_sample PRIMARY KEY (no, sub_no)
);
```

이렇게 이름을 지정할 수가 있다.

<br/><br/>

## 인덱스

인덱스의 역할은 검색속도의 향상이다.

<br/>

### 검색이란?

`select` 명령에 `where` 구로 조건을 지정하고 그에 일치하는 행을 찾는 일련의 과정을 말한다.


<br/>

### 예시

책안에 있는 특정한 부분을 찾고 싶은 경우, 

본문을 처음부터 읽어나가기보다 목차나 색인을 참고해서 찾는 편이 효율적이다.

```
인덱스가 바로 이런 역할이다
```


<br/><br/>

## 인덱스의 종류 (=키)

- `primary :` 중복되지 않는 유일한 키

- `normal :` 중복을 허용하는 인덱스
- `unique :` 중복을 허용하지 않는 유일한 키
- `foreign :` 다른 테이블과의 관계성을 부여하는 키
- `full text :` 자연어 검색, myisam에서만 지원

<br/><br/>

## 예제를 위한 준비 단계.

```sql
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` tinyint(4) NOT NULL AUTO_INCREMENT,
  `name` char(4) NOT NULL,
  `address` varchar(50) NOT NULL,
  `department` enum('국문과','영문과','컴퓨터공학과','전자공학과','물리학과') NOT NULL,
  `introduction` text NOT NULL,
  `number` char(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_number` (`number`) USING BTREE,
  KEY `idx_department` (`department`),
  KEY `idx_department_name` (`department`,`address`),
  FULLTEXT KEY `idx_introduction` (`introduction`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
```

<br/>


## INSERT

```sql
INSERT INTO `student` VALUES (1, '이숙경', '청주', '컴퓨터공학과', '저는 컴퓨터 공학과에 다닙니다. computer', '0212031');
INSERT INTO `student` VALUES (2, '박재숙', '서울', '영문과', '저는 영문과에 다닙니다.', '0512321');
INSERT INTO `student` VALUES (3, '백태호', '경주', '컴퓨터공학과', '저는 컴퓨터 공학과에 다니고 경주에서 왔습니다.', '0913134');
INSERT INTO `student` VALUES (4, '김경훈', '제천', '국문과', '제천이 고향이고 국문과에 다닙니다.', '9813413');
INSERT INTO `student` VALUES (6, '김경진', '제주', '국문과', '이번에 국문과에 입학한 김경진이라고 합니다. 제주에서 왔어요.', '0534543');
INSERT INTO `student` VALUES (7, '박경호', '제주', '국문과', '박경호입니다. 잘 부탁드립니다.', '0134511');
INSERT INTO `student` VALUES (8, '김정인', '대전', '영문과', '김정인입니다. 대전에서 왔고, 영문과에 다닙니다.', '0034543');
```

<br/>

![이미지](/programming/img/입문196.PNG)

<br/><br/>

## primary key (프라이머리 키)

```
PRIMARY KEY (`id`)
```


- 테이블 전체를 통틀어서 중복되지 않는 값을 지정해야 한다.
    - ‘id’ 인 컬럼을 ‘프라이머리 키' 로 지정한다. 라는 뜻은 `INSERT` 될때 중복이 되어선 안된다 라는 뜻이다.

        
- where 문을 이용해서 데이터를 조회할 때 가장 고속으로 데이터를 가져올 수 있다.

- 테이블마다 딱 하나의 primary key를 가질 수 있다.
- null 값도 절대로 허용이 안된다.

<br/>

### 예제

```sql
select * from student where id=3;
```

장점은. 아주 고속으로 데이터를 가져 올 수 있는 것이다.

![이미지](/programming/img/입문197.PNG)

<br/><br/>

## unique key (유니크 키)

- `UNIQUE KEY` idx_number 는 유니크 키의 이름이다. 

    만약, 이걸 생략하면 자동으로 데이터 베이스가 이름을 만들어 준다.
    
    그리고 `(`number`)` 지정하면 number 컬럼이 `‘유니크 키’` 로 지정이 된다. 

<br/>
    
- 즉, 이 `“테이블 전체에서 다른 값이랑 충돌하지 않는다”` 라는 것이 보장이 되는 것이다.
    
    여기서 `number는 == 학생의 학번`이라고 생각하면 된다. (유일한 식별자)

<br/>


### `프라이머리 키`와의 차이는 null을 허용한다 vs 안한다 의 차이다.

- `유니크 키` 는 `null을 허용 한다.`

- 여러개의 `unique key`를 지정할 수 있다.

<br/>

### 예제

```sql
select * from student where number=0534543;
```

![이미지](/programming/img/입문198.PNG)

<br/><br/>

## normal key

- 'KEY' `idx_department` (`department`)` - KEY 라고 되어 있는 것들은 모두 normal key 이다.
- 인덱스가 중복되는 “국문과 사람들을 중복으로 걸고 싶다.” 이럴때 사용한다.
    
    즉, 인덱스를 걸어 놨기 때문에 인덱스를 걸어 놓지 않은것 보다 더 빠르게 조회 가능하다.
    
- `중복을 허용한다.`
- 여러개의 키를 지정할 수 있다.


<br/>

### 예제

```sql
select * from student where department='국문과'
```

![이미지](/programming/img/입문199.PNG)

<br/><br/>

## 중복키

- `KEY `idx_department_name` (`department`,`address`)` 즉, (`department`,`address`) 를 묶어서 

    하나의 노멀 키로 구성을 한것이다. 즉, 하나의 정의 안에 두개의 키가 들어간 것이다.
- 하나의 키에 여러개의 칼럼을 포함


<br/>

### 예제

```sql
select * from student where department='국문과' AND address='제주';
```

![이미지](/programming/img/입문200.PNG)