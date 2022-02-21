import java.util.Arrays;
import java.util.Scanner;

public class Problem11Pairs { // O(N * log(N)) (linear) solution for counting (without the printing)
    public static void main(String... args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Target sum: ");
        int target = Integer.parseInt(sc.nextLine());

        System.out.print("Integers: ");
        String[] input = sc.nextLine().split(" ");
        final int N = input.length;

        int[] arr = new int[N];

        for (int i = 0; i < N; i++) {
            arr[i] = Integer.parseInt(input[i]);
        }

        Arrays.sort(arr);

        long pairs = 0;
        int[] uniques = new int[N];
        int[] repetitions = new int[N];

        int k = 0;
        int rep = 1;
        for (int i = 1; i < N; i++) {
            if (arr[i] == arr[i - 1]) {
                rep++;
                if (i == N - 1) {
                    uniques[k] = arr[i];
                    repetitions[k] = rep;
                    k++;
                }
            } else {
                uniques[k] = arr[i - 1];
                repetitions[k] = rep;
                k++;
                rep = 1;
                if (i == N - 1) {
                    uniques[k] = arr[i];
                    repetitions[k] = 1;
                    k++;
                }
            }
        }

        int l = 0;
        int r = k - 1;
        if (l == r) {
            if (uniques[l] * 2 == target) {
                int combinations = repetitions[l] * (repetitions[l] - 1) / 2;
                pairs += combinations;
//                for (int i = 0; i < combinations; i++) {
//                    System.out.printf("%d, %d%n", uniques[l], uniques[l]);
//                }
            }
        } else {
            while (l < r) {
                if (uniques[l] * 2 == target) {
                    int combinations = repetitions[l] * (repetitions[l] - 1) / 2;
                    pairs += combinations;
//                    for (int i = 0; i < combinations; i++) {
//                        System.out.printf("%d, %d%n", uniques[l], uniques[l]);
//                    }
                }

                if (uniques[r] * 2 == target) {
                    int combinations = repetitions[r] * (repetitions[r] - 1) / 2;
                    pairs += combinations;
//                    for (int i = 0; i < combinations; i++) {
//                        System.out.printf("%d, %d%n", uniques[r], uniques[r]);
//                    }
                }

                int currentCombination = uniques[l] + uniques[r];
                if (currentCombination == target) {
                    int combinations = repetitions[l] * repetitions[r];
                    pairs += combinations;
//                    for (int i = 0; i < combinations; i++) {
//                        System.out.printf("%d, %d%n", uniques[l], uniques[r]);
//                    }
                    l++;
                } else if (currentCombination < target) {
                    l++;
                } else {
                    r--;
                }
            }
        }

        System.out.printf("Total pairs: %d", pairs);
    }
}
