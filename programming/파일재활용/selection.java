public class Test1 {

	private static void selectionSort(int[] arr) {
		selectionSort(arr, 0);
	}

	private static void selectionSort(int[] arr, int start) {
		if (start < arr.length - 1) { // 어차피 마지막에는 정렬할게 하나 이기 때문에 할 필요가 없는 것이다.
					      //5 4 2 3 1 --> 0 인덱스
					      //  4 2 3 5 --> 1 인덱스
					      //    4 3 5 --> 2 인덱스
					      //      4 5 --> 3 인덱스
					      //        5 --> 4 남은게 5 하나 이기 때문할 필요가 없는것이다.
	
			
			int min_index = start;

			for (int i = start; i < arr.length; i++) {
				if (arr[i] < arr[min_index]) {
					min_index = i;
				}
			}
			swap(arr, start, min_index);
			selectionSort(arr, start + 1);
		}
	}

	private static void swap(int[] arr, int index1, int index2) {
		int tmp = arr[index1];
		arr[index1] = arr[index2];
		arr[index2] = tmp;
	}

	private static void printArray(int[] arr) {
		for (int data : arr) {
			System.out.print(data + ",");
		}
		System.out.println();
	}

	public static void main(String[] args) {
		int[] arr = { 3, 6, 1, 8, 2, 4 };
		printArray(arr);
		selectionSort(arr);
		printArray(arr);
	}

}
