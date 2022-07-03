## getter, setter

### 간단하게 말하자면 이렇다.
get이름() : 데이터를 가져올때 사용.
<br/>set이름() : 데이터를 저장할때 사용.

<br/>

**get**으로 시작하는 메서드는 단순히 멤버변수의 값을 반환하는 일을 하고,<br/>  **set**으로 시작하는메서드는 매개변수에 지정된 값을 검사하여 조건에 맞는 값일 때만 <br/> 멤버변수의 값을 변경하도록 작성 되어 있다.

<br/>보통 멤버변수의 값을 읽는 메서드의 이름을 ‘get멤버변수이름’ 으로 하고, <br/> 멤버변수의 값을 변경하는 메서드의 이름을 ‘set멤버변수이름’ 으로 한다. <br/> 그렇게 해야 하는 것은 아니지만 암묵적인 규칙으므로 특별한 이유가 없는 한 따르도록 하자.

<br/>get 으로 시작하는 메서드를 겟터, set 으로 시작하는 메서드를 셋터 라고 부른다.

### <br/>예제.

Time클래스의 모든 멤버변수의 접근 제어자를 private으로 하고, <br/>이들을 다루기 위한 public메서드를 추가했다.<br/> 그래서 ‘t.hour=13;’ 과 같이 멤버변수로의 직접적인 접근은 허가되지 않는다.<br/> 오로지 메서드를 통한 접근만이 허용될 뿐이다.

```java
class Time {
    private int hour;
    private int minute;
    private int second;

    Time(int hour, int minute, int second) {
        setHour(hour);
        setMinute(minute);
        setSecond(second);
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        if (hour < 0 || hour > 23) {
            return;
        } else {
            this.hour = hour;
        }
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        if (minute < 0 || minute > 59) {
            return;
        } else {
            this.minute = minute;
        }
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        if (second < 0 || second > 59) {
            return;
        } else {
            this.second = second;
        }
    }

    public String toString() {
        return hour + ":" + minute + ":" + second;
    }
}

public class test {
    public static void main(String[] args) {

        Time t = new Time(12, 35, 30);
        System.out.println(t);
        // t.hour = 13; 에러 !!
        t.setHour(t.getHour()+1); // 현재시간보다 1시간 후로 변경한다.
        System.out.println(t); // System.out.println(t.toString()); 과 같다

    }
}
```

<br/><br/>

>**Reference**
><br/>남궁 성 지음, 『자바의 정석』, 도우출판.
