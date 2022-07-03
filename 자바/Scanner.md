## 스캐너 사용

<br/>

```java
import java.util.Scanner;
 
public class test {

    public static void main(String[] args) {
    
        Scanner sc = new Scanner(System.in);
 
        int i = sc.nextInt();
        long l = sc.nextLong();
        double d = sc.nextDouble();
        char c = sc.next().charAt(0);
        String s = sc.nextLine();
    
        System.out.println("int: " + i);
        System.out.println("long: " + l);
        System.out.println("double: " + d);
        System.out.println("char: " + c);
        System.out.println("string: " + s);
    }
}


```