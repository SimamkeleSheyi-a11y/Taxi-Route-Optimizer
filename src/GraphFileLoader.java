import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GraphFileLoader {

    private static class RoadData {
        private final String from;
        private final String destination;
        private final double distance;

        private RoadData(String from, String destination,
                         double distance) {
            this.from = from;
            this.destination = destination;
            this.distance = distance;
        }
    }

    public static Graph loadGraph(String filePath)
            throws IOException {

        List<String> locationNames = new ArrayList<>();
        List<RoadData> roadData = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(
                Paths.get(filePath), StandardCharsets.UTF_8)) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] fields = line.split(",", -1);

                for (int i = 0; i < fields.length; i++) {
                    fields[i] = fields[i].trim();
                }

                try {
                    if ("LOCATION".equalsIgnoreCase(fields[0])) {
                        if (fields.length != 2
                                || fields[1].isEmpty()) {
                            throw new IllegalArgumentException(
                                "Use LOCATION,name"
                            );
                        }

                        if (locationNames.contains(fields[1])) {
                            throw new IllegalArgumentException(
                                "Duplicate location: " + fields[1]
                            );
                        }

                        locationNames.add(fields[1]);

                    } else if ("ROAD".equalsIgnoreCase(fields[0])) {
                        if (fields.length != 4
                                || fields[1].isEmpty()
                                || fields[2].isEmpty()) {
                            throw new IllegalArgumentException(
                                "Use ROAD,from,destination,distance"
                            );
                        }

                        double distance =
                            Double.parseDouble(fields[3]);

                        if (!Double.isFinite(distance)
                                || distance < 0) {
                            throw new IllegalArgumentException(
                                "Distance must be finite and non-negative"
                            );
                        }

                        roadData.add(new RoadData(
                            fields[1], fields[2], distance
                        ));

                    } else {
                        throw new IllegalArgumentException(
                            "Unknown record type: " + fields[0]
                        );
                    }

                } catch (IllegalArgumentException ex) {
                    throw new IOException(
                        "Invalid dataset at line " + lineNumber
                        + ": " + ex.getMessage(), ex
                    );
                }
            }
        }

        if (locationNames.isEmpty()) {
            throw new IOException(
                "Dataset contains no locations."
            );
        }

        Graph graph = new Graph();

        for (String name : locationNames) {
            graph.addLocation(new Location(name));
        }

        for (RoadData road : roadData) {
            Location from = graph.findLocation(road.from);
            Location destination =
                graph.findLocation(road.destination);

            if (from == null || destination == null) {
                throw new IOException(
                    "Road references an undeclared location: "
                    + road.from + " -> " + road.destination
                );
            }

            graph.addRoad(new Road(
                from, destination, road.distance
            ));
        }

        return graph;
    }
}
