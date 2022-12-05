package bg.sofia.uni.fmi.mjt.airbnb.accommodation;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

public class Hotel extends BookableAccommodation {
    private static final String HOTEL = "Hotel";

    private static long instances;

    public Hotel(Location location, double pricePerNight){
        super(HOTEL, location, pricePerNight);
        ++instances;
    }

    @Override
    protected long getIdSuffix(){
        return instances;
    }
}
