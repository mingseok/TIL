public class Test {

	private static void bubbleSort(int[] arr) {
		bubbleSort(arr, arr.length - 1);
	}

	private static void bubbleSort(int[] arr, int last) {
		if (last > 0) {
			for (int i = 1; i <= last; i++) {
				if (arr[i - 1] > arr[i]) {
					swap(arr, i - 1, i);
				}
			}
			bubbleSort(arr, last - 1); // 중요!!!! 
						   // 재귀 호출 설명.
						   // arr[]는 참조변수이다. 
						   // void로 재귀호출의 return문을 대신 할수 있기 때문에 return문을 사용하지 않는 것이다. 
						   // 왜? void 여도 값을 변하게 할수 있고, 참조변수로 하면 더 간단하게 할 수 있으므로.
						   // ex) 한 스택에 값이 변경 되었다면 그 주소를 arr에 담아서 변경된 주소가 밖으로 나가는 것이다.
		}
	}

	private static void swap(int[] arr, int source, int target) { // void 지만 int[] arr로 참조변수가 있으니깐
								      // 서로 변경된 값이 저장 되는 것이다.
								      // 여기가 끝나고 다시 여기를 호출 했던 부분으로 돌아간다.
							              // 그리고 내려가 나머지 작업을 동작하는 것이다.
		
								      // 만약! swap() 메서드로 넘어와서 안에 printArray() 메서드로 
								      // 이동하는게 있다면 printArray()으로 가서 진행하면 되는거다. 
								      // 그리고 진행이 다 끝나면 원래 내가 호출 당했던 곳으로 돌아가면 된다.
								
								      // 중요!!!! 돌아가는건 맞다. 
								      // 그런데 또 다른 메서드가 있다면 그걸 타고 들어가 
								      // 해당 메서드를 수행하고 돌아가는 것이다.
									
									
		int tmp = arr[source];
		arr[source] = arr[target];
		arr[target] = tmp;
	}

	private static void printArray(int[] arr) {
		for (int data : arr) {
			System.out.print(data + ", ");
		}
		System.out.println();
	}

	public static void main(String[] args) {
		int[] arr = { 3, 5, 4, 2, 1 };
		printArray(arr);
		bubbleSort(arr);
		printArray(arr);

	}

}
