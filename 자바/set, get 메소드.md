## set, get 메소드

GET 란?
priavte 필드(변수)의 값을 리턴 하는 역할을 한다. <br/>( 필드에서 가져온다는 뜻)
한가지 알것 (is)!! 필드 타입이 boolean 일 경우는 **is**FieldName( ) 이다

<br/>

Getter 작성 방법

```java
class Car {
   private double speed;

   public double getSpeed( ) {
      return speed;
   }
}
// 여기서 약속이 있다. 
// 시작은 get으로 적고 필드(변수)의 첫문자를 대문자로 적는다



그리고 또 다른 경우
필요한 경우 필드의 값을 가공해서 리턴한다.
예제 )
 double getSpeed( ) {
 double km = speed*1.6;
 return km; // 마일에서 km로 
   }
```

<br/><br/>SET 란?
외부에서 주어진 값을 필드값으로 수정한다.
필요한 경우 외부의 값을 유효성 검사를 한다.

<br/>Setter 작성 방법

```java
class Car {
  private double speed;
  public void setSpeed(double speed) { // 유효성 검사
    this.speed = speed;
  }
}
// 여기서 약속이 있다. 
// 시작은 set으로 적고 필드(변수)의 첫문자를 대문자로 적는다

-- Setter 사용한 예제 코드 --
void setSpeed(double speed) {
  if(speed < 0) { // 여기서 음수인지 검사
									// ( - 값이면 밑으로 내려가 모두 0으로 처리 )
     this.speed = 0;
     return;
   } else {
     this.speed = speed;
   }
}
```

<br/><br/>
GET, SET 사용해보기. 1-2 까지 있음

```java
1-1
public class Car {
  private int speed; // private는 변하지 않는것 
  private boolean stop;

	public int getSpeed() {
     return speed; 
  }

  public boolean isStop() {
     return stop;
  }
}
```

```java
1-2
public static void main (String[] args()) {
   Car myCar = new Car();
   System.out.println(myCar.getSpeed()); //getSteed한 이유는 private로 되어 있기때문에 접근 불가능하기 때문이다.
   System.out.println(myCar.isSpeed());
   }
}
```

<br/><br/>결과 값은 —> 0, false 가 나온다

![이미지](/programming/img/set.PNG)