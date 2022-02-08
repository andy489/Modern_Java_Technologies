package bg.sofia.uni.fmi.mjt.rentalservice.service;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;
import bg.sofia.uni.fmi.mjt.rentalservice.vehicle.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class RentalService implements RentalServiceAPI {

    static final double SECONDS_IN_ONE_MINUTE = 60.0;

    private final Vehicle[] vehicles;

    public RentalService(Vehicle[] vehicles) {
        this.vehicles = vehicles;
    }

    @Override
    public double rentUntil(Vehicle vehicle, LocalDateTime until) {

        if (vehicle == null) {
            return -1.0;
        }

        if (until.isBefore(LocalDateTime.now())) {
            return -1.0;
        }

        for (Vehicle currVehicle : this.vehicles) {
            boolean idChek = currVehicle.getId().equals(vehicle.getId());
            boolean availableCheck = currVehicle.getEndOfReservationPeriod().isBefore(LocalDateTime.now());

            if (idChek) {
                if (availableCheck) {
                    currVehicle.setEndOfReservationPeriod(until);

                    double interval = (double) ChronoUnit.SECONDS.between(LocalDateTime.now(), until);

                    int durationInMinutes = (int) Math.ceil(interval / SECONDS_IN_ONE_MINUTE);

                    return durationInMinutes * currVehicle.getPricePerMinute();
                } else {
                    return -1.0;
                }
            }
        }
        return -1.0;
    }

    @Override
    public Vehicle findNearestAvailableVehicleInRadius(String type, Location location, double maxDistance) {
        Vehicle foundVehicle = null;

        for (Vehicle currVehicle : this.vehicles) {

            double dist = currVehicle.getLocation().getDistance(location);

            boolean typeCheck = type.equalsIgnoreCase(currVehicle.getType());
            boolean distanceRangeCheck = dist <= maxDistance;
            boolean availableCheck = currVehicle.getEndOfReservationPeriod().isBefore(LocalDateTime.now());

            if (typeCheck && distanceRangeCheck && availableCheck) {
                maxDistance = dist;
                foundVehicle = currVehicle;
            }
        }

        return foundVehicle;
    }

    public void displayVehicles() {
        for (Vehicle v : this.vehicles) {
            System.out.println(v.getId() + " " + v.getLocation().toString());
        }
    }
}
