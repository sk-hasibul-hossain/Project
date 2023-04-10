
//import java.lang.Math;

public class test {
    public static void main(String args[]) {
        int[][] arr = { { 10, 1 }, { 12, 3 }, { 101, 2 }, { 3, 4 }, { 5, 5 }, { 8, 0 }, { 71, 15 }, { 1, 10 },
                { 6, 2 } };

        // Arrays.sort(arr);
        int temp = 0;
        for (int i = 0; i < 9 - 1; i++) {
            for (int j = i; j < 9 - 1; j++) {
                if (arr[i][0] > arr[j + 1][0]) {
                    temp = arr[i][0];
                    arr[i][0] = arr[j + 1][0];
                    arr[j + 1][0] = temp;

                    temp = arr[i][1];
                    arr[i][1] = arr[j + 1][1];
                    arr[j + 1][1] = temp;
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            System.out.println(arr[i][0] + " " + arr[i][1]);
        }

    }

}
