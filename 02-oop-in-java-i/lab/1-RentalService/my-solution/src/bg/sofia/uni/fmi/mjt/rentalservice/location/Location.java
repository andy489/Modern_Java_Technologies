package bg.sofia.uni.fmi.mjt.rentalservice.location;

public class Location {

    private final double x;
    private final double y;

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDistance(Location other) {
        return Math.sqrt(Math.pow(other.getX() - this.x, 2) + Math.pow(other.getY() - this.y, 2));
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

}