
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Holds the final path found by the algorithm along with its calculated distance.
public class Route {
    private final List<Location> path;
    private final double totalDistance;

    public Route(List<Location> path, double totalDistance) {
        //Checks to ensure we don't build an invalid route object
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Route path cannot be empty.");
        }
        if (!Double.isFinite(totalDistance) || totalDistance < 0) {
            throw new IllegalArgumentException("Invalid route distance.");
        }
    
        this.path = new ArrayList<>(path);
        this.totalDistance = totalDistance;
    }

    public List<Location> getPath() {
        // Hand out an unmodifiable view so external code can't mess with the path directly
        return Collections.unmodifiableList(path);
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    @Override
    public String toString() {
        return String.join(" -> ",
            path.stream().map(Location::getName).toArray(String[]::new))
            + String.format(" (%.2f km)", totalDistance);
    }
}
