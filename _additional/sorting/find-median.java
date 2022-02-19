// https://www.hackerrank.com/challenges/find-the-median/problem

import java.util.Scanner;

public class Solution {

    private static int partition(int[] arr, int l, int r) {
        int randIndex = l + (int) (Math.random() * ((r - l) + 1));
        arr[randIndex] += arr[r] - (arr[r] = arr[randIndex]);

        int piv = arr[r];
        int pivIndex = l;

        for (int i = l; i <= r; i++) {
            if (arr[i] < piv) {
                arr[i] += arr[pivIndex] - (arr[pivIndex] = arr[i]);
                pivIndex++;
            }
        }
        arr[r] += arr[pivIndex] - (arr[pivIndex] = arr[r]);
        return pivIndex;
    }

    private static int quickSelect(int[] arr, int l, int r, int target) {
        int partition = partition(arr, l, r);
        if (partition == target) {
            return arr[partition];
        } else if (partition < target) {
            return quickSelect(arr, partition + 1, r, target);
        } else {
            return quickSelect(arr, l, partition - 1, target);
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();

        int[] arr = new int[n];

        for (int i = 0; i < n; i++) {
            arr[i] = in.nextInt();
        }

        int median = quickSelect(arr, 0, n - 1, n / 2);
        System.out.println(median);
    }
}
