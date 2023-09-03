## Arrays 배열을 다루기 편리한 메서드

## 1. 배열의 출력 - toString()

<br/>

## 2. 배열의 복사 - copyOf(), copyOfRange()

```java
int[] arr = { 0, 1, 2, 3, 4};

int[] arr2 = Arrays.copyOf(arr, arr.length); // arr 배열 그대로 복사

int[] arr3 = Arrays.copyOf(arr, 3); // 3개만 복사한다는 뜻이다.

int[] arr4 = Arrays.copyOf(arr, 3); // 7자리까지 있는 배열을 만들고 나머지는 0으로 채운다.

int[] arr5 = Arrays.copyOfRange(arr, 2, 4); // arr5=[2, 3] <- 4는 포함하지 않는다.

int[] arr6 = Arrays.copyOfRange(arr, 0, 7); // arr6=[0, 1, 2, 3, 4, 0, 0]
```

<br/>

### 배열 채우기 - fill(), setAll()

```java
int[] arr = new int[5]; // 생성 했다면
Arrays.fill(arr, 9); // arr=[9, 9, 9, 9, 9] 가 되는 것이다.
Arrays.setAll(arr, (i) -> (int)(Math.random()*5)+1); // arr=[1, 5, 2, 1, 1]
```

<br/><br/>

## 3. 배열의 정렬과 검색 - sort(), binarySearch()

정렬은 sort, 검색은 binarySearch() 이다.

binarySearch() == 이진탐색 → 정렬된 배열에만 가능하다.

```java
int[] arr = { 3, 2, 0, 1, 4};
int idx = Arrays.binarySearch(arr,2); // idx=1 잘못된 결과.

// binarySearch 사용하기 위한 순서.
// 정렬을 한다음에 binarySearch를 해야 한다.
Arrays.sort(arr);
System.out.println(Arrays.toString(arr)); // [0, 1, 2, 3, 4]
int idx = Arrays.binarySearch(arr,2); // idx=2 참인 결과.
```

<br/>

### 탐색 == 검색 같은 말이다.


<br/><br/>

## 4. 다차원 배열의 출력 - deepToString()

```java
int[] arr = {0, 1, 2, 3, 4};
int[][] arr2D = {{11,12}, {21,22}};

System.out.println(Arrays.toString(arr)); // [0, 1, 2, 3, 4]
System.out.println(Arrays.deepToString(arr2D)); // [[11, 12], [21, 22]]
```

<br/><br/>

### 다차원 배열의 비교 - deepEquals()

```java
int[][] str2D = new String[][]{{"aaa","bbb"},{"AAA","BBB"}};
int[][] str2D2 = new String[][]{{"aaa","bbb"},{"AAA","BBB"}};

System.out.println(Arrays.equals(str2Dd, str2D2)); // false
System.out.println(Arrays.deepEquals(str2Dd, str2D2)); // true
```

<br/><br/>

## 5. 배열을 List로 변환 - asList(Object...a)

Arrays.asList()는 배열의 내용을 수정하려고 할 때 List로 바꿔서 편리하게 사용하기 위함.

(Object...a) 는 배열이다. 매개변수 여러개 넣어도 된다는 말이다. 

(가변: 갯수가 정해져 있지 않다.)

<br/>list는 읽기 전용이다.

그리하여 list타입으로 추가 하려면 `UnsupportedOperationException` 예외 발생한다. -> 뜻 지원하지 않는 기능

그리하여 추가 하고 싶으면 

```java
// 생성자에다가 넣어서 다시 만들어야 하는것이다.
List list = new ArrayList(Arrays.asList(1,2,3,4,5)); 
```

<br/>

## 전체 예제1)
```java

class aaa {
	public static void main(String[] args) {
		int[] arr = { 0, 1, 2, 3, 4 };
		int[][] arr2D = { { 11, 12, 13 }, { 21, 22, 23 } };

		// arr=[0, 1, 2, 3, 4]
		System.out.println("arr=" + Arrays.toString(arr));

		// arr2D=[[11, 12, 13], [21, 22, 23]]
		System.out.println("arr2D=" + Arrays.deepToString(arr2D));

		int[] arr2 = Arrays.copyOf(arr, arr.length);
		int[] arr3 = Arrays.copyOf(arr, 3);
		int[] arr4 = Arrays.copyOf(arr, 7);
		int[] arr5 = Arrays.copyOfRange(arr, 2, 4);
		int[] arr6 = Arrays.copyOfRange(arr, 0, 7);

		// arr2=[0, 1, 2, 3, 4]
		System.out.println("arr2=" + Arrays.toString(arr2));

		// arr3=[0, 1, 2]
		System.out.println("arr3=" + Arrays.toString(arr3));

		// arr4=[0, 1, 2, 3, 4, 0, 0]
		System.out.println("arr4=" + Arrays.toString(arr4));

		// arr5=[2, 3]
		System.out.println("arr5=" + Arrays.toString(arr5));

		// arr6=[0, 1, 2, 3, 4, 0, 0]
		System.out.println("arr6=" + Arrays.toString(arr6));

		int[] arr7 = new int[5];
		Arrays.fill(arr7, 9); // arr=[9,9,9,9,9]

		// arr7=[9, 9, 9, 9, 9]
		System.out.println("arr7=" + Arrays.toString(arr7));

		Arrays.setAll(arr7, i -> (int) (Math.random() * 6) + 1);

		// arr7=[6, 3, 2, 6, 4]
		System.out.println("arr7=" + Arrays.toString(arr7));

		for (int i : arr7) {
			char[] graph = new char[i];
			Arrays.fill(graph, '*');
			// ******6
			// ***3
			// **2
			// ******6
			// ****4
			System.out.println(new String(graph) + i);
		}

		String[][] str2D = new String[][] { { "aaa", "bbb" }, { "AAA", "BBB" } };
		String[][] str2D2 = new String[][] { { "aaa", "bbb" }, { "AAA", "BBB" } };

		// false
		System.out.println(Arrays.equals(str2D, str2D2)); 

		// true
		System.out.println(Arrays.deepEquals(str2D, str2D2)); 

		char[] chArr = { 'A', 'D', 'C', 'B', 'E' };

		int idx = Arrays.binarySearch(chArr, 'B');
		// chArr=[A, D, C, B, E]
		System.out.println("chArr=" + Arrays.toString(chArr));
		
		// index of B =-2 잘못된 값이다. 왜? 정렬을 안해서 그렇다.
		System.out.println("index of B =" + Arrays.binarySearch(chArr, 'B'));
		
		System.out.println("= After sorting =");
		Arrays.sort(chArr);
		
		// chArr=[A, B, C, D, E]
		System.out.println("chArr=" + Arrays.toString(chArr));

		// index of B =1
		System.out.println("index of B =" + Arrays.binarySearch(chArr, 'B'));
	}
}

출력값.
arr=[0, 1, 2, 3, 4]
arr2D=[[11, 12, 13], [21, 22, 23]]
arr2=[0, 1, 2, 3, 4]
arr3=[0, 1, 2]
arr4=[0, 1, 2, 3, 4, 0, 0]
arr5=[2, 3]
arr6=[0, 1, 2, 3, 4, 0, 0]
arr7=[9, 9, 9, 9, 9]
arr7=[6, 3, 2, 6, 4]
******6
***3
**2
******6
****4
false
true
chArr=[A, D, C, B, E]
index of B =-2
= After sorting =
chArr=[A, B, C, D, E]
index of B =1
```
