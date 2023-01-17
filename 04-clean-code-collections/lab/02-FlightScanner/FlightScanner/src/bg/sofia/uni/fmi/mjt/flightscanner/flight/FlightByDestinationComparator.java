package bg.sofia.uni.fmi.mjt.flightscanner.flight;

import java.util.Comparator;

public class FlightByDestinationComparator implements Comparator<Flight> {
    @Override
    public int compare(Flight left, Flight right) {
        return left.getTo().id().compareTo(right.getTo().id());
    }
}
