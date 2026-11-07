
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Graph {
    private final List<Location> locations;
    private final List<Road> roads;

    // Constructor
    public Graph() {
        locations = new ArrayList<>();
        roads = new ArrayList<>();
    }

    // Add a location to the graph
    public void addLocation(Location location) {
        if (location == null) {
            throw new IllegalArgumentException(
                "Location cannot be null."
            );
        }
        if (!locations.contains(location)) {
            locations.add(location);
        }
    }
    // Add a road connecting two locations
    public void addRoad(Road road) {
        if (road == null) {
            throw new IllegalArgumentException(
                "Road cannot be null."
            );
        }
        if (!locations.contains(road.getFrom())
                || !locations.contains(road.getDestination())) {
            throw new IllegalArgumentException(
                "Add both locations to the graph before adding the road."
            );
        }
        roads.add(road);
    }

    // Return all locations
    public List<Location> getLocations() {
        return Collections.unmodifiableList(
            new ArrayList<>(locations)
        );
    }

    // Return all roads
    public List<Road> getRoads() {
        return Collections.unmodifiableList(
            new ArrayList<>(roads)
        );
    }

    // Return roads leaving a particular location
    public List<Road> getRoadsFrom(Location location) {
        if (location == null) {
            throw new IllegalArgumentException(
                "Location cannot be null."
            );
        }

        List<Road> result = new ArrayList<>();
        for (Road road : roads) {
            if (road.getFrom().equals(location)) {
                result.add(road);
            }
        }
        return result;
    }

    // Find a location by name
    public Location findLocation(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        for (Location location : locations) {
            if (location.getName().equalsIgnoreCase(name.trim())) {
                return location;
            }
        }
        return null;
    }
}
