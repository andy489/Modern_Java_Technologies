package bg.sofia.uni.fmi.mjt.math;

public class NumberUtils {

    public static boolean isPrime(int n) {

        if (n < 2) {
            throw new IllegalArgumentException("Number not in the domain of function");
        }

        if (n == 2) {
            return true;
        }

        if (n % 2 == 0) {
            return false;
        }

        int squareRoot = (int) Math.sqrt(n);

        for (int i = 3; i <= squareRoot; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }

        return true;

    }

}
