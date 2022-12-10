package primes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MathUtilsTest {

    @Test
    void testIsPrime0IsNotPrime() {
        assertFalse(MathUtils.isPrime(0), "0 is NOT prime");
    }

    @Test
    void testIsPrime1IsNotPrime() {
        assertFalse(MathUtils.isPrime(1), "1 is NOT prime");
    }

    @Test
    void testIsPrime2IsPrime() {
        assertTrue(MathUtils.isPrime(2), "2 is prime");
    }

    @Test
    void testIsPrime3IsPrime() {
        assertTrue(MathUtils.isPrime(3), "3 is prime");
    }

    @Test
    void testIsPrime10isNotPrime() {
        assertFalse(MathUtils.isPrime(10), "10 is NOT prime");
    }

    @Test
    void testIsPrime16IsNotPrime() {
        assertFalse(MathUtils.isPrime(16), "16 is NOT prime");
    }

    @Test
    void testIsPrime17IsPrime() {
        assertTrue(MathUtils.isPrime(17), "17 is prime");
    }

    @Test
    void testIsPrime21isNotPrime() {
        assertFalse(MathUtils.isPrime(21), "21 is NOT prime");
    }

    @Test
    void testIsPrime49isNotPrime() {
        assertFalse(MathUtils.isPrime(49), "49 is NOT prime");
    }

    @Test
    void testIsPrime100003IsPrime() {
        assertTrue(MathUtils.isPrime((int) 1e6 + 3), "100003 is prime");
    }

    @Test
    void testIsPrimeNegativeNumber() {
        assertThrows(
                IllegalArgumentException.class, () -> MathUtils.isPrime(-1),
                "Prime is not defined for negative numbers and should throw exception"
        );
    }
}
