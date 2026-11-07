
//Represents a road connecting two locations.
public class Road {

    private final Location from;
    private final Location destination;
    private final double distance;

    public Road(Location from, Location destination, double distance) {
        // Fail early if we got bad data
        if (from == null || destination == null) {
            throw new IllegalArgumentException(
                "Road locations cannot be null."
            );
        }
        // Distances can't be negative or infinite
        if (!Double.isFinite(distance) || distance < 0) {
            throw new IllegalArgumentException(
                "Road distance must be finite and non-negative."
            );
        }

        this.from = from;
        this.destination = destination;
        this.distance = distance;
    }

    public Location getFrom() {
        return from;
    }
    public Location getDestination() {
        return destination;
    }
    public double getDistance() {
        return distance;
    }
    @Override
    public String toString() {
        return from + " -> " + destination + " (" + distance + " km)";
    }
}
