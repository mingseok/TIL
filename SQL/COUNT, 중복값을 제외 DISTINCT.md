## COUNT, 중복값을 제외 DISTINCT



![이미지](/programming/img/입문165.PNG)

```java
SELECT COUNT(집합) FROM 테이블명 WHERE 조건식
```

<br/><br/>

테이블 전체의 행 숫자를 알고 싶다면 `COUNT(*)`를 사용하고

특정 컬럼의 숫자를 확인하려면 `COUNT(컬럼명)`을 사용한다.



![이미지](/programming/img/입문166.PNG)

위에서 보듯, 총 5행이 있는 테이블이지만

1. NULL값이 한 행 포함된 A 컬럼은 4로 count한 것을 알 수 있고

2. 중복값은 포함해서 집계한다.

<br/><br/>

## DISTINCT 키워드는 중복을 제거해서 반환한다.

### 아래 두 개의 SELECT문을 비교해보자.

![이미지](/programming/img/입문167.PNG)