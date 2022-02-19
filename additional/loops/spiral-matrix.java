import java.util.Scanner;

public class SpiralMatrix {
    public static void main(String... args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();

        int belowMain, aboveMain, x;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                belowMain = Math.min(i, j);
                aboveMain = Math.min(n - i - 1, n - j - 1);
                x = Math.min(belowMain, aboveMain);

                int maxNum = n * n;
                int LEN = Integer.toString(maxNum).length();

                if (i <= j) {
                    int k = n * n + 1 - ((n - 2 * x) * (n - 2 * x) - (i - x) - (j - x));
                    int len = Integer.toString(k).length();
                    System.out.printf("%d" + " ".repeat(LEN - len + 1), k);
                } else {
                    int k = n * n + 1 - ((n - 2 * x - 2) * (n - 2 * x - 2) + (i - x) + (j - x));
                    int len = Integer.toString(k).length();
                    System.out.printf("%d" + " ".repeat(LEN - len + 1), k);
                }
            }
            System.out.println();
        }
    }
}
