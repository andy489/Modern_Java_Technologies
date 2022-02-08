package bg.sofia.uni.fmi.mjt.rentalservice.vehicle;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;

public class Scooter extends AbstractVehicle {

    private static final double SCOOTER_PRICE = 0.3;

    private final String type;
    private final double price;

    {
        this.type = "SCOOTER";
        this.price = SCOOTER_PRICE;
    }

    public Scooter(String id, Location location) {
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
