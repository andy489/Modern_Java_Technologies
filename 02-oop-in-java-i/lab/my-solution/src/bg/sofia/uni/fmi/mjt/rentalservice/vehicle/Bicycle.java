package bg.sofia.uni.fmi.mjt.rentalservice.vehicle;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;

public class Bicycle extends AbstractVehicle {

    private static final double BICYCLE_PRICE = 0.2;

    private final String type;
    private final double price;

    {
        this.type = "BICYCLE";
        this.price = BICYCLE_PRICE;
    }

    public Bicycle(String id, Location location) {
        super(id, location);
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public double getPricePerMinute() {
        return this.price;
    }
}
