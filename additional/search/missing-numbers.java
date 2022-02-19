// https://www.hackerrank.com/challenges/missing-numbers/problem

import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int MAX = 10001;
        int[] counting = new int[MAX];

        int n1 = in.nextInt();

        for (int i = 0; i < n1; i++) {
            ++counting[in.nextInt()];
        }

        int n2 = in.nextInt();

        for (int i = 0; i < n2; i++) {
            --counting[in.nextInt()];
        }

        for (int i = 0; i < MAX; i++) {
            if (counting[i] < 0) {
                System.out.print(i + " ");
            }
        }
    }
}
