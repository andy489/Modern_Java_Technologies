package bg.sofia.uni.fmi.mjt.rentalservice.vehicle;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;

public class Car extends AbstractVehicle {

    private static final double CAR_PRICE = 0.5;

    private final String type;
    private final double price;

    {
        type = "CAR";
        price = CAR_PRICE;
    }

    public Car(String id, Location location) {
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
