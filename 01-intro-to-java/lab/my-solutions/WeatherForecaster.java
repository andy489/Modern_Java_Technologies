public class WeatherForecaster {
    public static int[] getsWarmerIn(int[] temperatures) {

        final int SIZE = temperatures.length;

        int[] nextWarmerDay = new int[SIZE];

        // Create an IMPLICIT empty stack
        int[] stack = new int[SIZE];
        int stackIterator = 0;

        // Iterate elements from right to left
        for (int t = SIZE - 1; t >= 0; --t) {
            /*
             Search stack until:
             - we find greater element on top of stack
             - stack is empty
            */

            while (stackIterator != 0) {
                if (temperatures[stack[stackIterator - 1]] > temperatures[t]) {
                    nextWarmerDay[t] = stack[stackIterator - 1] - t;
                    break;
                } else {
                    --stackIterator;
                }
            }
            stack[stackIterator++] = t;
        }

        return nextWarmerDay;
    }
    /*
     All element enter stack only once
     After processed (which is equal to pop) they never come back
     => Time complexity is O(n), where n = SIZE (of input), which is linear
    */
}
