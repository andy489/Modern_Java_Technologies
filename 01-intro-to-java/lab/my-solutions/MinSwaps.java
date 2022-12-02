/*
Create a function that takes as parameters two binary numbers as strings
and returns the minimum number of swaps to convert the first binary string into the second
For example,
    minSwaps("1100", "1001") -> 1
    minSwaps("110011", "010111") -> 1
    minSwaps("10011001", "01100110") -> 4
Both binary strings will be of equal length and will have an equal number of zeroes and ones.
A swap is switching two arbitrary bits in the binary string.
 */

public class MinSwaps {
    public static int minSwaps(String s1, String s2) {
        int differences = 0;
        int LEN1 = s1.length();

        for (int i = 0; i < LEN1; i++) {
            differences += s1.charAt(i) == s2.charAt(i) ? 0 : 1;
        }

        return differences / 2;
    }

    public static void main(String... args) {
        System.out.println(MinSwaps.minSwaps("1110101", "1101000"));
    }
}