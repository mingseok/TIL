# GET, POST

## GET메서드 POST메서드 란?

둘다 웹 서비스 개발에 주로 사용하는 메서드 이다.

클라이언트가 서버에게 웹페이지를 보여달라고 말하는 것을 우리는 요청 이라 부르고, 

서버가 클라이언트에게 요청받은 것에 대한 대답으로 웹페이지 내용을 표현하기 위해

html문서로 주는것을 응답 이라 부른다.
<br/><br/>
## **GET 데이터를 보내기**

클라이언트의 데이터를 URL뒤에 붙여서 보낸다. 위에서 쓴 예시처럼 아이디 패스워드를 보낸다고 하면, **www.naver.com?id=mingseok&pass=0000 이런식으로 간다.**

URL 뒤에 "?" 마크를 통해 URL의 끝을 알리면서, 데이터 표현의 시작점을 알린다.
<br/><br/>
## POST**데이터를 보내기**

POST로 데이터를 전송할때에는 Body영역 데이터 타입을 Header Content-Type에 작성 해줘야 한다. POST방식은 GET방식과는 다르게 보내는 데이터를 url를 통해 볼 수 없어 보안적으로 안전하다. 
하지만 다른 툴을 사용하여 POST영역의 데이터를 확인이 가능하기 때문에 안심해서는 안된다.

ex)

HEADER 영역

Content-Type:application/json; charset=UTF-8

.....

BODY 영역

{
	"param1":"value1",
	"param2":"value2"
}