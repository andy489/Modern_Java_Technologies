public class ArrayAnalyzer {
    private static final int MINIMUM_ELEMENTS_REQUIREMENT = 3;

    public static boolean isMountainArray(int[] array) {
        final int SIZE = array.length;

        if (SIZE < MINIMUM_ELEMENTS_REQUIREMENT) {
            return false;
        }

        int iterator = 0;

        // Climbing upwards while we can.
        while (iterator + 1 < SIZE && array[iterator] < array[iterator + 1]) {
            ++iterator;
        }

        // Sloping downwards while we can
        while (iterator + 1 < SIZE && array[iterator] > array[iterator + 1]) {
            ++iterator;
        }

        return iterator == SIZE - 1;
    }
}
