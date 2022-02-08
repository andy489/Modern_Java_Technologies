package bg.sofia.uni.fmi.mjt.gifts.comparators;

import bg.sofia.uni.fmi.mjt.gifts.gift.Priceable;

import java.util.Comparator;

public class ItemComparator implements Comparator<Priceable> {

    @Override
    public int compare(Priceable p1, Priceable p2) {
        return Double.compare(p1.getPrice(), p2.getPrice());
    }
}
