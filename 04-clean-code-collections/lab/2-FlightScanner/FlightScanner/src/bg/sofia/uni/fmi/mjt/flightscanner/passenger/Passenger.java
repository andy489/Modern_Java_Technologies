package bg.sofia.uni.fmi.mjt.flightscanner.passenger;

public record Passenger(String id, String name, Gender gender) {
    public Passenger{
        if (id == null || id.isEmpty() || id.isBlank()) {
            throw new IllegalArgumentException("Passenger id cannot be null, empty or blank");
        }

        if (name == null || name.isEmpty() || name.isBlank()) {
            throw new IllegalArgumentException("Passenger name cannot be null, empty or blank");
        }
    }
}
