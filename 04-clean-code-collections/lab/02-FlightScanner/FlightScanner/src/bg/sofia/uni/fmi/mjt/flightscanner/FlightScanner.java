package bg.sofia.uni.fmi.mjt.flightscanner;

import bg.sofia.uni.fmi.mjt.flightscanner.airport.Airport;
import bg.sofia.uni.fmi.mjt.flightscanner.exception.FlightCapacityExceededException;
import bg.sofia.uni.fmi.mjt.flightscanner.flight.Flight;
import bg.sofia.uni.fmi.mjt.flightscanner.flight.FlightByDestinationComparator;
import bg.sofia.uni.fmi.mjt.flightscanner.flight.FlightsByFreeSeatsComparator;
import bg.sofia.uni.fmi.mjt.flightscanner.flight.RegularFlight;
import bg.sofia.uni.fmi.mjt.flightscanner.passenger.Gender;
import bg.sofia.uni.fmi.mjt.flightscanner.passenger.Passenger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class FlightScanner implements FlightScannerAPI {
    private final Map<Airport, Set<Flight>> flights;

    public FlightScanner() {
        this.flights = new HashMap<>();
    }

    public FlightScanner(Map<Airport, Set<Flight>> flights) {
        this.flights = flights;
    }

    @Override
    public void add(Flight flight) {
        if (flight == null) {
            throw new IllegalArgumentException("Flight cannot be null");
        }

        Airport from = flight.getFrom();

        flights.putIfAbsent(from, new HashSet<>());
        flights.get(from).add(flight);
    }

    @Override
    public void addAll(Collection<Flight> flights) {
        if (flights == null) {
            throw new IllegalArgumentException("Flights cannot be null");
        }

        for (Flight f : flights) {
            add(f);
        }
    }

    @Override
    public List<Flight> searchFlights(Airport from, Airport to) {
        if (from == null || to == null || from.equals(to)) {
            throw new IllegalArgumentException("Source and destination of a flight cannot be null or equal");
        }

        return bfs(from, to);
    }

    private List<Flight> bfs(Airport current, Airport target) {
        Set<Airport> visited = new HashSet<>();
        Queue<Airport> queue = new LinkedList<>();

        Map<Airport, Flight> parentOf = new HashMap<>();

        visited.add(current);
        queue.add(current);
        parentOf.put(current, null);

        while (!queue.isEmpty()) {
            Airport airport = queue.poll();

            if (airport.equals(target)) {
                List<Flight> flightPlan = new LinkedList<>();

                while (parentOf.get(target) != null) {
                    flightPlan.add(parentOf.get(target));
                    target = parentOf.get(target).getFrom();
                }

                Collections.reverse(flightPlan);

                return flightPlan;
            }

            Set<Flight> outgoingFlights = flights.get(airport);

            if (outgoingFlights == null) {
                continue;
            }

            for (Flight outgoingFlight : outgoingFlights) {
                if (!visited.contains(outgoingFlight.getTo())) {
                    visited.add(outgoingFlight.getTo());
                    queue.add(outgoingFlight.getTo());
                    parentOf.put(outgoingFlight.getTo(), outgoingFlight);
                }
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<Flight> getFlightsSortedByFreeSeats(Airport from) {
        return getFlightsSortedByComparator(from, new FlightsByFreeSeatsComparator());
    }

    @Override
    public List<Flight> getFlightsSortedByDestination(Airport from) {
        return getFlightsSortedByComparator(from, new FlightByDestinationComparator());
    }

    private List<Flight> getFlightsSortedByComparator(Airport from, Comparator<Flight> customComparator) {
        if (from == null) {
            throw new IllegalArgumentException("From airport cannot be null");
        }

        Set<Flight> res = flights.get(from);

        if (res == null) {
            return List.of();
        }

        List<Flight> sorted = new ArrayList<>(res);
        sorted.sort(customComparator.reversed());
        return List.copyOf(sorted);
    }

    public static void main(String... args) throws FlightCapacityExceededException {
        FlightScanner fs = new FlightScanner();

        Airport a1 = new Airport("SOF");
        Airport a2 = new Airport("CDG");
        Airport a3 = new Airport("MAN");
        Airport a4 = new Airport("FCO");
        Airport a5 = new Airport("TLS");
        Airport a6 = new Airport("AMS");

        Flight f1 = RegularFlight.of("001-AB", a1, a2, 100);
        Flight f2 = RegularFlight.of("002-CD", a2, a3, 90);
        Flight f3 = RegularFlight.of("003-KJ", a2, a4, 120);
        Flight f4 = RegularFlight.of("004-LO", a3, a1, 80);
        Flight f5 = RegularFlight.of("005-PU", a5, a6, 110);
        Flight f6 = RegularFlight.of("006-VE", a1, a6, 105);
        Flight f7 = RegularFlight.of("007-SE", a4, a6, 110);
        Flight f8 = RegularFlight.of("008-LA", a3, a5, 70);
        Flight f9 = RegularFlight.of("009-GZ", a6, a2, 105);

        f1.addPassenger(new Passenger("A1-F546-B13", "Pesho", Gender.MALE));
        f1.addPassenger(new Passenger("T5-OP12-98S", "Mia", Gender.FEMALE));

        fs.addAll(List.of(f1, f2, f3, f4, f5, f6, f7, f8, f9));

        System.out.println(fs.getFlightsSortedByDestination(a2)); // [002-CD, 003-KJ]
        System.out.println(fs.getFlightsSortedByFreeSeats(a2)); // [003-KJ, 002-CD]

        System.out.println(fs.searchFlights(a1, a5)); // [001-AB, 002-CD, 008-LA]
        fs.add(RegularFlight.of("010", a6, a5, 122));
        System.out.println(fs.searchFlights(a1, a5)); // [006-VE, 010]
    }
}
