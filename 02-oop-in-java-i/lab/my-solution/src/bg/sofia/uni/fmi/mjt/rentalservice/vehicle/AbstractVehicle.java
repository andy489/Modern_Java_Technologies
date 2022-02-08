package bg.sofia.uni.fmi.mjt.rentalservice.vehicle;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class AbstractVehicle implements Vehicle {
    private final String id;
    private final Location location;
    private LocalDateTime until;

    public AbstractVehicle(String id, Location location) {
        this.id = id;
        this.location = location;
        this.until = LocalDateTime.now();
    }

    @Override
    public abstract double getPricePerMinute();

    @Override
    public String getId() {
        if (this.id == null || this.id.equals("")) {
            return "No ID";
        }
        return this.id;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public LocalDateTime getEndOfReservationPeriod() {
        if (LocalDateTime.now().isBefore(this.until)) {
            return this.until;
        } else {
            return LocalDateTime.now();
        }
    }

    @Override
    public void setEndOfReservationPeriod(LocalDateTime until) {
        this.until = until;
    }

    @Override
    public abstract String getType();

    @Override
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Vehicle vehicle)) {
            return false;
        }

        return this.getId().equals(vehicle.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

}
