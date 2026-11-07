
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class RouteFinder {

    private final Graph graph;

    // Stores a location and its distance at the time it enters the queue.
    private static class QueueEntry {
        private final Location location;
        private final double distance;

        public QueueEntry(Location location, double distance) {
            this.location = location;
            this.distance = distance;
        }
    }

    public RouteFinder(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null.");
        }

        this.graph = graph;
    }

    public Route bestRoute(Location start, Location end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException(
                "Start and destination are required."
            );
        }

        List<Location> locations = graph.getLocations();

        if (!locations.contains(start) || !locations.contains(end)) {
            throw new IllegalArgumentException(
                "Both locations must exist in the graph."
            );
        }

        Map<Location, Double> distances = new HashMap<>();
        Map<Location, Location> previous = new HashMap<>();

        for (Location location : locations) {
            distances.put(location, Double.POSITIVE_INFINITY);
        }

        distances.put(start, 0.0);

        PriorityQueue<QueueEntry> queue = new PriorityQueue<>(
            Comparator.comparingDouble(entry -> entry.distance)
        );

        queue.add(new QueueEntry(start, 0.0));

        while (!queue.isEmpty()) {
            QueueEntry entry = queue.poll();
            Location current = entry.location;

            // Ignore an outdated queue entry.
            if (entry.distance > distances.get(current)) {
                continue;
            }

            // The shortest distance to the destination is now finalized.
            if (current.equals(end)) {
                break;
            }

            for (Road road : graph.getRoadsFrom(current)) {
                Location neighbour = road.getDestination();

                double newDistance =
                    distances.get(current) + road.getDistance();

                if (newDistance < distances.get(neighbour)) {
                    distances.put(neighbour, newDistance);
                    previous.put(neighbour, current);

                    queue.add(new QueueEntry(neighbour, newDistance));
                }
            }
        }

        double totalDistance = distances.get(end);

        if (!Double.isFinite(totalDistance)) {
             AppLogger.warning(
                     "No route found from " + start.getName()
                   + " to " + end.getName()
               );
         return null;
    }

        // Reconstruct the path from destination back to start.
        List<Location> path = new ArrayList<>();
        Location current = end;

        while (current != null) {
            path.add(current);
            current = previous.get(current);
        }

        Collections.reverse(path);

        Route route = new Route(path, totalDistance);

       AppLogger.info(
       "Route calculated: " + route
       );

       return route;
    }
}
