package primes;

public class MathUtils {

    public static boolean isPrime(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Prime is not defined for negative numbers");
        }

        if (n < 2) {
            return false;
        }

        if (n < 4) {
            return true;
        }

        if ((n & 1) == 0 || n % 3 == 0) {
            return false;
        }

        int squareRoot = (int) Math.sqrt(n);

        for (int i = 5; i <= squareRoot; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }

        return true;
    }
}
