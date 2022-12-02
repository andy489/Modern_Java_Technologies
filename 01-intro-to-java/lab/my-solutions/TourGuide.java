public class TourGuide {
    public static int getBestSightseeingPairScore(int[] places) {
        if (places == null) {
            return 0;
        }

        int LEN = places.length;

        if (LEN < 2) {
            return 0;
        }

        int i = 0, j = 1;
        int best = Integer.MIN_VALUE;
        while (j < LEN) {
            int curr = places[j] + places[i] + i - j;
            if (i == j) {
                j++;
            } else if (curr > best) {
                best = curr;
                j++;
            } else {
                i++;
            }
        }

        j--;
        for (; i < j; i++) {
            int curr = places[j] + places[i] + i - j;
            if (curr > best) {
                best = curr;
            }
        }

        return best;
    }

    public static void main(String... args) {
        System.out.println(getBestSightseeingPairScore(new int[]{8, 1, 5, 2, 6})); // 11
        System.out.println(getBestSightseeingPairScore(new int[]{1, 2})); // 2
        System.out.println(getBestSightseeingPairScore(new int[]{10, 7, 14})); // 22
        System.out.println(getBestSightseeingPairScore(new int[]{0, 1, 2, 3, 1})); // 4
        System.out.println(getBestSightseeingPairScore(new int[]{4, 4, 4, 4})); // 7
        System.out.println(getBestSightseeingPairScore(new int[]{1, 6, 7, 6, 7, 21, 30})); // 50
    }
}
