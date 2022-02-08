package bg.sofia.uni.fmi.mjt.gifts.comparators;

import bg.sofia.uni.fmi.mjt.gifts.gift.Gift;

import java.util.Comparator;

public class GiftComparator implements Comparator<Gift<?>> {

    @Override
    public int compare(Gift g1, Gift g2) {
        return Double.compare(g1.getPrice(), g2.getPrice());
    }
}
