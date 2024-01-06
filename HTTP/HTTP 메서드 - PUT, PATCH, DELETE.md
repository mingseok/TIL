## HTTP 메서드 - PUT, PATCH, DELETE

PUT 은 완전히 덮을 수 있을 자신이 있을 때 사용한다.


<br/>

## e.g.) 게시판에 게시글을 수정한다 할때 사용한다.

![이미지](/programming/img/입문599.PNG)

- 리소스가 있으면 대체

- 리소스가 없으면 생성

- 중요) 클라이언트가 리소스를 식벽한다는 것이다.

    - 즉, 리소스 위치를 알고 URL을 지정한다는 것이다.

        - POST와의 차이점이다

<br/><br/>

## e.g.) PUT 동작

![이미지](/programming/img/입문600.PNG)

<br/>

서버쪽에 있던 기본의 데이터는 없어지고, 새로운 데이터로 대체가 되어 버리는 것이다.

![이미지](/programming/img/입문601.PNG)

<br/><br/>

## 그렇다면 리소스가 없으면?

신규 리소스가 들어가게 되는 것이다.

![이미지](/programming/img/입문602.PNG)

<br/><br/>

## 중요한 것은

username 필드가 없을 경우다. → `그럼 어떻게 되는가?`

![이미지](/programming/img/입문603.PNG)

<br/><br/>

## username 필드는 삭제되는 것이다.

![이미지](/programming/img/입문604.PNG)


<br/><br/>


## 그리하여 부분 변경하고 싶을때는 `PATCH` 를 사용하자.

위와 같이, `username` 필드를 빼고 요청을 보낸다면?

![이미지](/programming/img/입문605.PNG)

<br/>

### 이렇게 `age만 50으로 변경하는 것이다.`

![이미지](/programming/img/입문606.PNG)

<br/><br/>

## DELETE

![이미지](/programming/img/입문607.PNG)

<br/>

이렇게 삭제 되는 것이다.

![이미지](/programming/img/입문608.PNG)

<br/><br/>

## 정리

`PUT` : 완전히 대체 한다.

`PATCH` : 부분 변경할때 사용한다.

`DELETE` : 삭제 할때 사용한다.