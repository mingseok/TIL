```java
class A{}

class B extends A{}

class instanceofEx01 {

 

public static void main(String[] args) {

  A a = new A();

  B b = new B();

 

  System.out.println("a instanceof A : " + (a instanceof A));

  System.out.println("b instanceof A : " + (b instanceof A));

  System.out.println("a instanceof B : " + (a instanceof B));

  System.out.println("b instanceof B : " + (b instanceof B));

  }

}

 

<결과>

a instanceof A : true

b instanceof A : true

a instanceof B : false

b instanceof B : true
```


<br/>

>**Reference**
<br/>참고 자료 : https://improver.tistory.com/140


