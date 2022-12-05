package bg.sofia.uni.fmi.mjt.airbnb.filter;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Bookable;

public class PriceCriterion implements Criterion {
    private final double minPrice;
    private final double maxPrice;

    public PriceCriterion(double minPrice, double maxPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    @Override
    public boolean check(Bookable bookable) {
        if (bookable == null) {
            return false;
        }

        double pricePerNight = bookable.getPricePerNight();

        return minPrice <= pricePerNight && pricePerNight <= maxPrice;
    }
}
