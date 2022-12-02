/*
Create two versions of a function that, for two given integers n and m,
calculates a real number: the sum
    1/m + 2/m^2 + 3/m^3 + ... + n/m^n
One of the versions should use recursion, the other - iteration.
 */

public class CalcSum {
    public static double sumIter(int n, int m) {
        double result = 0.0;
        int denominator = m;

        for (int i = 1; i <= n; i++) {
            result += i * 1.0 / denominator;
            denominator *= m;
        }

        return result;
    }

    public static double sumRec(int n, int m) {
        if (n == 1) {
            return 1.0 / m;
        }
        return sumRec(n - 1, m) + n * 1.0 / Math.pow(m, n);
    }

    public static void main(String... args) {
        System.out.println(CalcSum.sumIter(10, 2));
        System.out.println(CalcSum.sumRec(10, 2));
    }
}
