import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class Prime {
    void checkPrime(int... numbers) {
        for (int num : numbers) {
            if (isPrime(num)) {
                System.out.print(num + " ");
            }
        }
        System.out.println();
    }

    boolean isPrime(int n) {
        if (n < 2) {
            return false;
        } else if (n % 2 == 0) { // account for even numbers now, so that we can do i += 2 in loop below
            return false;
        }

        int sqrt = (int) Math.sqrt(n);

        for (int i = 3; i <= sqrt; i += 2) { // skips even numbers for faster results
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}

public class Solution {
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            List<Integer> input = Arrays.stream(br.readLine().split(" "))
                    .mapToInt(Integer::parseInt).boxed()
                    .collect(Collectors.toList());

            final int SIZE = input.size();

            int[] numbers = new int[SIZE];

            for (int i = 0; i < SIZE; i++) {
                numbers[i] = input.get(i);
            }

            Prime ob = new Prime();

            ob.checkPrime(numbers);

            Method[] methods = Prime.class.getDeclaredMethods();

            Set<String> set = new HashSet<>();

            boolean overload = false;

            for (Method method : methods) {
                if (set.contains(method.getName())) {
                    overload = true;
                    break;
                }

                set.add(method.getName());
            }

            if (overload) {
                throw new Exception("Overloading not allowed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
