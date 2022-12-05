package bg.sofia.uni.fmi.mjt.airbnb;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Accommodation;
import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Apartment;
import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Bookable;
import bg.sofia.uni.fmi.mjt.airbnb.accommodation.BookableAccommodation;
import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;
import bg.sofia.uni.fmi.mjt.airbnb.filter.Criterion;
import bg.sofia.uni.fmi.mjt.airbnb.filter.LocationCriterion;
import bg.sofia.uni.fmi.mjt.airbnb.filter.PriceCriterion;

import java.awt.print.Book;
import java.time.LocalDateTime;

public class Airbnb implements AirbnbAPI {
    private Bookable[] accommodations;

    public Airbnb(Bookable[] accommodations) {
        this.accommodations = accommodations;
    }

    @Override
    public Bookable findAccommodationById(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }

        for (Bookable accommodation : accommodations) {
            if (accommodation.getId().equalsIgnoreCase(id)) {
                return accommodation;
            }
        }

        return null;
    }

    @Override
    public double estimateTotalRevenue() {
        double estimatedRevenue = 0.0d;

        for (Bookable accommodation : accommodations) {
            estimatedRevenue += accommodation.getTotalPriceOfStay();
        }

        return estimatedRevenue;
    }

    @Override
    public long countBookings() {
        long count = 0L;
        for (Bookable accommodation : accommodations) {
            if (accommodation.isBooked()) {
                ++count;
            }
        }
        return count;
    }

    @Override
    public Bookable[] filterAccommodations(Criterion... criteria) {
        int countMatching = 0;

        for (Bookable accommodation : accommodations) {
            if (matchCriteria(accommodation, criteria)) {
                countMatching++;
            }
        }

        Bookable[] included = new Bookable[countMatching];
        int resultIndex = 0;

        for (Bookable accommodation : accommodations) {
            if (matchCriteria(accommodation, criteria)) {
                included[resultIndex++] = accommodation;
            }
        }

        return included;
    }

    private boolean matchCriteria(Bookable accommodation, Criterion[] criteria) {
        for (Criterion criterion : criteria) {
            if (criterion != null && !criterion.check(accommodation)) {
                return false;
            }
        }


        return true;
    }

    public static void main(String... args) {
        BookableAccommodation apt1 = new Apartment(new Location(1, 1), 90);
        BookableAccommodation hot1 = new Apartment(new Location(2, 2), 60);
        BookableAccommodation vil1 = new Apartment(new Location(3, 3), 220);

        apt1.book(LocalDateTime.of(2022, 12, 22, 14, 0),
                LocalDateTime.of(2022, 12, 25, 14, 0));

        hot1.book(LocalDateTime.of(2022, 12, 25, 12, 0),
                LocalDateTime.of(2022, 12, 26, 11, 0));

        Bookable[] accommodations = {apt1, hot1, vil1};

        Airbnb airbnb = new Airbnb(accommodations);

        Criterion locationCriterion = new LocationCriterion(new Location(0, 0), 3.9);
        Criterion priceCriterion = new PriceCriterion(70, 300);

        System.out.println(airbnb.countBookings());
        System.out.println(airbnb.estimateTotalRevenue());

        Bookable[] filtered = airbnb.filterAccommodations(locationCriterion, priceCriterion);

        for (Bookable b : filtered) {
            System.out.println(b);
        }
    }
}
