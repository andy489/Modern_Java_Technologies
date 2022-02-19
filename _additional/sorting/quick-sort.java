import java.util.Scanner;

public class QuickSort {
    private static int partition(int[] arr, int l, int r) {
        int pivotIndex = l + (r - l + 1) / 2;
        arr[pivotIndex] += arr[r] - (arr[r] = arr[pivotIndex]);

        int pivot = arr[r];
        pivotIndex = l;

        for (int i = l; i < r; i++) {
            if (arr[i] < pivot) {
                arr[pivotIndex] += arr[i] - (arr[i] = arr[pivotIndex]);
                ++pivotIndex;
            }
        }

        arr[pivotIndex] += arr[r] - (arr[r] = arr[pivotIndex]);
        return pivotIndex;
    }

    private static void quickSortRecursive(int[] arr, int start, int end) {
        if (start < end) {
            int pivotIndex = partition(arr, start, end);
            quickSortRecursive(arr, start, pivotIndex - 1);
            quickSortRecursive(arr, pivotIndex + 1, end);
        }
    }

    private static void quickSortIterative(int[] arr, int l, int r) {
        int[] stack = new int[r - l + 1];

        int top = -1;

        stack[++top] = l;
        stack[++top] = r;

        while (top >= 0) {
            r = stack[top--];
            l = stack[top--];

            int pivotIndex = partition(arr, l, r);
            if (pivotIndex - 1 > l) {
                stack[++top] = l;
                stack[++top] = pivotIndex - 1;
            }
            if (pivotIndex + 1 < r) {
                stack[++top] = pivotIndex + 1;
                stack[++top] = r;
            }
        }
    }

    private static void print(int[] arr) {
        for (int element : arr) {
            System.out.print(element + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();

        int[] arr = new int[n];

        for (int i = 0; i < n; i++) {
            arr[i] = in.nextInt();
        }

        quickSortIterative(arr, 0, n - 1);
        // quickSortRecursive(arr, 0, n - 1);

        System.out.println("Sorted array: ");
        print(arr);
    }
}
