## Big-O 란?

<br/>

- 알고리즘의 성능을 수학적으로 표현해주는 표기법 이다
- Big-O 를 통해 알고리즘의 시간과 공간 복잡도를 표현할 수 있다.
- Big-O 표기법은 알고리즘의 실제 러닝타임을 표시하기 보다 데이터나 사용자의 증가율에 따른 <br/>알고리즘의 성능을 예측하는 게 목표이다.

<br/><br/>

### Big-O **표기법의 종류**

### **O(1)** : 입력 데이터 크기에 상관없이 언제나 일정한 시간이 걸리는 알고리즘 이다.

<br/>밑에 코드에 함수를 보면 배열 방에 값이 [0] 인지를 확인 한다.

인자로 받는 배열 방이 얼마나 큰지 에 상관없이 언제나 일정한 속도로 결과를 반환 한다. 

이런 경우에 우리는 이 알고리즘을 “O( 1 ) 의 시간 복잡도를 가진다.” 라고 말한다.

```java
A (int[] n) {
		return (n[0] == 0) ? true : false;
  }
```

<br/>

그래프로 표시 하면 이렇다.

![이미지](/programming/img/빅오1.PNG)

<br/><br/><br/>

### **O(n)** : 입력 데이터 크기에 비례해서 처리 시간이 걸리는 알고리즘을 표현할 때 사용한다. <br/>선형 시간 알고리즘이라 부른다.

<br/>밑에 코드를 보면 이 함수는 n개의 데이터를 받으면 n 번 루프를 도니깐 n이 하나 늘어 날때 마다 <br/>처리가 한 번씩 늘어나서 n의 크기 만큼 처리 시간이 걸리게 되는 것이다.

```java
A (int[] n) {
		for ( int i = 0; i <= n.length; i++) {
					System.out.println(i);
				}
	}
```

 

<br/>그래프로 표시 하면 이렇다.

![이미지](/programming/img/빅오2.PNG)

<br/><br/><br/>

### **O(n^2)**  : 버블 정렬 같은 비효율저긴 정렬 알고리즘이 이에 해당 한다.

<br/>함수를 보면 n을 돌리면서 그 안에서 n으로 루프를 또 돌릴때 n 스퀘어가 됩니다.

<br/>그래서 n개의 데이터를 받으면 첫 번째 루프에서 n번 돌면서 각각의 엘리먼트 에서 <br/>n번씩 또 도니깐 처리 횟수가 n을 가로 세로 길이로 가지는 면적 만큼 되는 것이다. 

<br/>데이터가 커지면 커질수록 데이터 처리 시간의 부담도 커지는 것이다.

<br/>(만약 **O(n^3) 이 된다면 넓이가 추가 되는 것이다. 큐브 모양이 되는것.)**

```java
A (int[] n) {
		for ( int i = 0; i <= n.length; i++) {
       for ( int j = 0; j <= n.length; j++) {
					System.out.println(i + j);
					 }
				}
	}
```

<br/>그래프로 표시 하면 이렇다.

처음에는 조금씩 상승하다가 나중에 가면 하나 추가할 때마다 그래프가 수직상승 하는 것이다.

![이미지](/programming/img/빅오3.PNG)

<br/><br/><br/>

### **O(2^n)** : 대표적인 피보나치의 수열이 이며, 재귀로 계산하는 알고리즘이 이에 해당 한다. <br/>n^2와 혼동되는 경우가 있는데 2^n이 훨씬 더 크다.

<br/>(피보나치의 수열)

![이미지](/programming/img/빅오4.PNG)

<br/>피보나치의 수열 코드 구현 하면 이렇다.

함수를 호출 할때마다 바로 전에 숫자랑 전전 숫자를 알아야 더 할 수 있기 때문에 해당 숫자를 알아내기 <br/>위해서 ‘1’ 를 뺀 숫자를 가지고 한번 재귀 호출을 하고, ‘2’ 를 뺀 숫자를 가지며, 매번 함수가 호출될 때마다 <br/>또 두번씩 호출 된다. 그것이 트리의 높이 만큼 반복 하는 것이다.

```java
public static int fibonacci (int num) {
		if (num == 1) {
				return 1;
		} 
		else if (num == 2) {
				return 1;
		} 
		else {
				return fibonacci(num -1) + fibonacci(num -2);
		}
}

public static void main(String[] args) {
				System.out.println("피보나치 수열의 10번째 원소는" + fibonacci(10) + "입니다."); 
		
}


https://blog.opid.kr/489
```

<br/>그래프로 표시 하면 이렇다.

![이미지](/programming/img/빅오5.PNG)

<br/><br/><br/>

### **O(log n)** : 이진 탐색의 경우가 이에 해당한다. <br/>( 정렬된 배열[10] 까지 있는 곳에 key = ‘6’ 를 찾는 것) 한번 처리가 진행될 때마다 <br/>검색해야 하는 데이터의 양이 절반씩 뚝뚝 떨어지는 알고리즘을 **O(log n) 알고리즘이라고 한다.**

<br/>순차 검색에 비교해서 속도가 현저하게 빠르다.

```java
package Search;
 
public class BinarySearch {
    public static void main(String[] args) {
        int[] arr = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
 
        binarySearch(2, arr);
    }
 
    public static void binarySearch(int iKey, int arr[]) {
        int mid;
        int left = 0;
        int right = arr.length - 1;
 
        while (right >= left) {
            mid = (right + left) / 2;
 
            if (iKey == arr[mid]) {
                System.out.println(iKey + " is in the array with index value: " + mid);
                break;
            }
 
            if (iKey < arr[mid]) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
 
        }
    }
}

출처: https://blog.opid.kr/489 [opid's document]
```

<br/>그래프로 표시 하면 이렇다.

**O(log n)이 O(n) 보다도 훨씬 적게 드는 걸 알 수 있다.**

데이터가 증가해도 성능이 크게 차이 나지 않는다.

![이미지](/programming/img/빅오6.PNG)

<br/><br/><br/>

### **O (n log n)** : 병합 정렬 등의 **대부분 효율이 좋은 알고리즘이 이에 해당 한다. <br/>아무리 좋은 알고리즘이라 할지라도 n log n 보다 빠를 수 없다.** <br/>입력값이 **최선**일 경우, 비교를 건너 뛰어 O(n)이 될 수 있다.

<br/><br/>

### **O(n!) : 가장 느린 알고리즘으로 입력값이 조금만 커져도 계산이 어렵다.**

<br/><br/>

### **상한 & 최악**

위의 빅오 표기법을 설명하며 상한, 최선, 최악이란 단어를 사용했다. 그렇다면 이 것들은 무슨 말일까?

**빅오(O)는 상한을 의미한다.**

여기서 중요한 포인트는 **상한 = 최악의 경우 와 혼동하는 것이다.**

빅오 표기법은 정확하게 쓰기에는 너무 길고 복잡한 함수를 "적당히 정확하게" 표현하는 방법일 뿐,

최악의 경우/ 평균적인 경우의 시간 복잡도와는 아무런 관계가 없는 개념이다.

가장 빨리 실행될때가 하한, 가장 늦게 실행될때가 상한을 뜻한다.

이 중 가장 늦게 실행될 때를 빅오(O), 가장 빨리 실행될 때를 빅오메가(Ω), 평균을 빅세타(θ) 로 지칭한다.

<br/><br/>

>**Reference** <br/>
https://codermun-log.tistory.com/235
<br/>이지영,**『**자바로 배우는 쉬운 자료구조**』**, 한빛아카데미.


