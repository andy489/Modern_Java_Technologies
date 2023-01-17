package bg.sofia.uni.fmi.mjt.flightscanner.flight;

import bg.sofia.uni.fmi.mjt.flightscanner.airport.Airport;
import bg.sofia.uni.fmi.mjt.flightscanner.exception.FlightCapacityExceededException;
import bg.sofia.uni.fmi.mjt.flightscanner.exception.InvalidFlightException;
import bg.sofia.uni.fmi.mjt.flightscanner.passenger.Passenger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RegularFlight implements Flight {
    private final String flightId;
    private final Airport from;
    private final Airport to;
    private final int totalCapacity;
    private final Set<Passenger> passengerSet;

    private RegularFlight(String flightId, Airport from, Airport to, int totalCapacity) {
        this.flightId = flightId;
        this.from = from;
        this.to = to;
        this.totalCapacity = totalCapacity;
        this.passengerSet = new HashSet<>();
//         this.passengerSet = HashSet.newHashSet(totalCapacity);
    }

    public static RegularFlight of(String flightId, Airport from, Airport to, int totalCapacity) {
        if (flightId == null || flightId.isEmpty() || flightId.isBlank()) {
            throw new IllegalArgumentException("Flight id cannot be null, empty or blank");
        }

        if (from == null || to == null) {
            throw new IllegalArgumentException("Flight cannot have null as start or end destination");
        }

        if (from.equals(to)) {
            throw new InvalidFlightException("Source and destination of a flight cannot be the same");
        }

        if (totalCapacity < 0) {
            throw new IllegalArgumentException("Total capacity of flight cannot be negative number");
        }

        return new RegularFlight(flightId, from, to, totalCapacity);
    }

    @Override
    public Airport getFrom() {
        return from;
    }

    @Override
    public Airport getTo() {
        return to;
    }

    @Override
    public void addPassenger(Passenger passenger) throws FlightCapacityExceededException {
        if (passenger == null) {
            throw new IllegalArgumentException("Passenger cannot be null");
        }

        if (passengerSet.size() >= totalCapacity) {
            throw new FlightCapacityExceededException(String.valueOf(totalCapacity));
        }

        passengerSet.add(passenger);
    }

    @Override
    public void addPassengers(Collection<Passenger> passengers) throws FlightCapacityExceededException {
        if (passengers == null) {
            throw new IllegalArgumentException("Passenger cannot be null");
        }

        if (this.passengerSet.size() + passengers.size() > totalCapacity) {
            throw new FlightCapacityExceededException(String.valueOf(totalCapacity));
        }

        this.passengerSet.addAll(passengers);
    }

    @Override
    public Collection<Passenger> getAllPassengers() {
        return Set.copyOf(passengerSet);
    }

    @Override
    public int getFreeSeatsCount() {
        return totalCapacity - passengerSet.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegularFlight flight = (RegularFlight) o;
        return Objects.equals(this.flightId, flight.flightId);
    }

    @Override
    public String toString() {
        return flightId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightId);
    }
}
