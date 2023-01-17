package bg.sofia.uni.fmi.mjt.flightscanner.flight;

import java.util.Comparator;

public class FlightsByFreeSeatsComparator implements Comparator<Flight> {
    @Override
    public int compare(Flight left, Flight right) {
        return Integer.compare(left.getFreeSeatsCount(), right.getFreeSeatsCount());
    }
}
