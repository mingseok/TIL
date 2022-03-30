class aaa {

    /*
     * 테스트 설명.
     * int[] arr = { 4, 2, 3 };
     * 4 2 3 -> 2 < 4 비교
     * 4 4 3 -> 2는 카피 해뒀으니 2는 신경쓰지말고 4를 2자리에 대입한다. 그리고,
     * j 가 -1 이므로 while문 종료 후 카피 해둔 2를 원래 있던 4 자리에 넣는다.
     * 
     * 2 4 3 -> (현재 i는 2 이고, j는 1이다.) 3 < 4 비교
     * 2 4 4 -> 3은 카피 해뒀으니 3는 신경쓰지말고 4를 3자리에 대입한다. 
     * 그리고 핵심은 그 다음이다.
     *
     * 2 4 4 -> j가 0이므로 다시 while문 조건으로 올라가는데, target(3) < arr[[0] => 2] 비교가 되는 것이다.
     * while()문 실행하지 않고 바로 밑 37번째줄로 내려온다.
     * 타켓은 3이였고, j는 0 이였다. 그러면 0 + 1 이 되므로 인덱스 1에 타켓 3을 넣는것이다.
     * 그리하여 2 3 4 가 되는 것이다.
     * 타겟보다 작으므로 그 밑으론 손볼 필요가 없는 것이다. !!
     */
    
    private static void insert(int[] arr) {
        insert(arr, arr.length);
    }

    private static void insert(int[] arr, int length) {

        for (int i = 1; i < length; i++) {

            int target = arr[i];
            int j = i - 1;

            while (j >= 0 && target < arr[j]) {
                arr[j + 1] = arr[j];
                j--;
            }

            arr[j + 1] = target;

        }

    }

    private static void print(int[] arr) {
        for (int data : arr) {
            System.out.print(data);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[] arr = { 4, 1, 5, 2, 3 };
        print(arr);
        insert(arr);
        print(arr);
    }
}
