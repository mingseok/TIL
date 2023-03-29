## LIKE, BETWEEN

<br/>

### BETWEEN , NOT BETWEEN 연산자.

“어떤 값이 어떤 특정 값 사이에 있다” 이, 여부를 참과 거짓으로 나타낼 수 있다.

<br/>

여기서 포인트는 예를 들어 `BETWEEN 1 AND 4` 이렇게 되어 있으면 ‘4’는 포함되지 않는 것이다.



‘4’ 전까지를 말하는 것이다.

![이미지](/programming/img/입문201.PNG)

<br/><br/>

```sql
SELECT 5 BETWEEN 1 AND 10;
```





‘5’ 는 1과 10 사이에 있다. → 참인 것이다.

![이미지](/programming/img/입문202.PNG)

<br/><br/>

```sql
SELECT 'banana' NOT BETWEEN 'Apple' AND 'camera';
```



이건 단어의 앞 글자만 보면 되는 것이다. A와 C 사이에 B 가 있으니 참이지만, NOT 이 붙어 있으니 false 로 나온 것이다.

참고로 mysql 은 소, 대문자 구분을 하지 않는다.

![이미지](/programming/img/입문203.PNG)


<br/><br/>

```sql
SELECT * FROM OrderDetails
WHERE ProductID BETWEEN 1 AND 4;
```



![이미지](/programming/img/입문204.PNG)


<br/><br/>

```sql
SELECT * FROM Customers
WHERE CustomerName BETWEEN 'a' AND 'b';
```



여기서 포인트는 마지막꺼는 포함하지 않는 다는 것이다.

a AND b 를 했지만 a 관한 것만 출력 되는 것이다.

b 가 없어서 출력 안되는 것이 아니다.

![이미지](/programming/img/입문205.PNG)


<br/><br/>

## **LIKE** '... **%** ...' 연산자

![이미지](/programming/img/입문206.PNG)

‘_’ 는 딱 한개의 글자만을 뜻하는 것이다.

<br/><br/>

## ‘%’ 설명

```sql
SELECT
  'HELLO' LIKE 'hel%', -- hel 도 참이고, hellllll 도 참 인것이다.
  'HELLO' LIKE 'H%', -- h 도 참이고, hellllll 도 참 인것이다.
  'HELLO' LIKE 'H%O', -- HO 도 참이고, hellllllO 도 참 인것이다.
  'HELLO' LIKE '%O', -- O 도 참이고, hellllllO 도 참 인것이다.
  'HELLO' LIKE '%HELLO%', -- %HELLO% 앞뒤로 있으니, 'asdgaweggHELLOasasd' 이것도 참이다.
  'HELLO' LIKE '%H', -- H로 끝난다는 의미이다.
  'HELLO' LIKE 'L%' -- L로 시작한다는 의미이다.
```

<br/>

```sql
SELECT * FROM Customers
WHERE CustomerName LIKE 'b%';
```

CustomerName 컬럼에서 첫글자가 ‘b’ 인 애들 모두 출력



![이미지](/programming/img/입문207.PNG)

<br/><br/>


```sql
SELECT * FROM Employees
WHERE Notes LIKE '%economics%'
```


활용도는 이걸 포함한 것을 찾고 싶을때 사용 하는 것이다.

![이미지](/programming/img/입문208.PNG)

<br/><br/>


## ‘_’ 설명

딱 맞게 빈자리를 채울 수 있다면 참인 것이다.

```sql
SELECT
  'HELLO' LIKE 'HEL__', -- HEL__ 은 2글자 더 올수 있는 것이다. 그리하여 'HELLO' 가 참이다.
  'HELLO' LIKE 'h___O', -- h___O 는 3글자 더 올수 있으니, 'HELLO' 가 참인 것이다. 
  'HELLO' LIKE 'HE_LO', -- HE_LO 는 1글자 더 올수 있으니, 'HELLO' 가 참인 것이다.
  'HELLO' LIKE '_____',  -- 여기까지가 참이다.
  'HELLO' LIKE '_HELLO', -- 거짓이다. false 
  'HELLO' LIKE 'HEL_',   -- 거짓이다. false
  'HELLO' LIKE 'H_O'     -- 거짓이다. false
```

<br/>


```sql
SELECT * FROM OrderDetails
WHERE OrderID LIKE '1025_'
```

![이미지](/programming/img/입문209.PNG)