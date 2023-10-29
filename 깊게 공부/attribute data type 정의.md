## attribute data type 정의

<br/>

## attribute data type : 숫자

- 정수

    - 정수를 저장할 때 사용

        - `ex)` `TINYINT`, `MEDIUMINT`, `BIGINT` …등
- 부동 소수점 방식

    - 실수를 저장할 때 사용

    - 고정 소수점 방식에 비해 정확하지 않다.
        - `ex)` `FLOAT`, `DOUBLE`
- 고정 소수점 방식

    - 실수를 정확하게 저장할 때 사용 → 돈 계산할 경우

        - `ex)` `DECIMAL`, `NUMERIC`
        - `DECIMAL` 같은 경우는 `DECIMAL(5, 2)` 두개의 인자를 받는다.
            
            첫번째는 숫자의 총 자리수, 두번째는 소수점`(“.”)` 기준, 뒤쪽의 자리 수
            

<br/><br/>

## attribute data type : 문자열

- 고정 크기 문자열

    - 최대 몇 개의 ‘문자’를 가지는 문자열을 저장할지를 지정

    - 저장될 문자열의 길이가 최대 길이보다 작으면 나머지를 `space`로 채워서 저장한다.

    - `name char(4)` 일 경우 → `‘a   ‘` 으로 저장한다는 것이다. or `‘고고고고’` → 꽉 차있기 때문

        - 이렇게, `스페이스`를 채워서 저장을 하고, 다시 읽어 올때는 스페이스는 제거가 된다.

    - `ex)` `char(n)` (0 ≤ n ≤ 255)

        - 휴대폰 번호 같은 컬럼들은 `char(n)`를 사용하자.
- 가변 크기 문자열

    - 최대 몇개의 ‘문자’를 가지는 문자열을 저장할지를 지정

    - 차이로는 → “저장될 문자열의 길이 만큼만 저장”

    - `name varchar(4)` 일 경우 → `‘a’`, `‘한국’`, `‘고고고고’` 스페이스 없이 저장된다는 뜻

    - `ex)` `varchar(n)` (0 ≤ n ≤ 65,535)

- 사이즈가 큰 문자열

    - 사이즈가 큰 문자열을 저장할 때 사용

        - `ex)` `MEDIUMTEXT`, `LONGTEXT`

<br/><br/>

## attribute data type : 날짜와 시간

- 날짜

    - 년, 월, 일을 저장

    - YYYY-MM-DD

        - `ex)` `DATA`
- 시간

    - 시, 분, 초를 저장

    - hh:mm:ss

        - `ex)` `TIME`

- 날짜와 시간

    - 날짜와 시간을 같이 표현

    - YYYY-MM-DD hh:mm:ss
    
        - `ex)` `DATETIME`, `TIMESTAMP`