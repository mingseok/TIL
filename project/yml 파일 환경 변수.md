## yml 파일 환경 변수로 관리하는 방법


![이미지](/programming/img/입문652.PNG)


IntelliJ IDEA의 "Run/Debug Configurations" 대화 상자를 열여 줍니다.

여기서는 "LinkcentralApplication"이라는 이름의 Spring Boot 애플리케이션 구성을 수정하려고 합니다. 

"Edit Configurations" 버튼이 클릭합니다.


<br/><br/>



![이미지](/programming/img/입문653.PNG)

여기서는 다양한 실행 옵션들을 설정할 수 있는 메뉴들이 있습니다.

"Spring Boot" 섹션에서 "Active profiles" 또는 "Environment variables" 같은 설정들을 할 수 있습니다. 

"EnvFile"이 체크되어 있어서 환경 변수 파일을 로드하는 옵션이 활성화되어 있음을 말합니다.


<br/><br/>

![이미지](/programming/img/입문654.PNG)

환경 변수 섹션에 "AWS_S3_ACCESS_KEY"와 "AWS_S3_SECRET_KEY"라는 두 개의 환경 변수가 설정되어 있습니다. 

이러한 키는 아마도 AWS S3 버킷에 접근하기 위한 인증 정보일 것입니다. 

사용자는 이 값을 시스템 환경 변수나 외부 파일에서 불러와서 사용할 수 있습니다.


<br/><br/>

![이미지](/programming/img/입문655.PNG)

"AWS_S3_ACCESS_KEY"와 "AWS_S3_SECRET_KEY"의 값을 입력하는 부분이며, 

이 값들은 실제 환경 변수의 값으로 대체될 것입니다. 

- 시스템 환경 변수를 포함할지 여부를 선택할 수도 있습니다.

<br/><br/>

## 전체 정리

프로젝트의 실행 환경을 설정하는 방법을 순서대로 보여 줍니다.

사용자는 이 설정들을 통해 애플리케이션을 실행할 때 필요한 환경 변수를 관리하고, 

AWS와 같은 서비스에 안전하게 연결하기 위한 인증 정보를 설정할 수 있습니다.