
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        AppLogger.initialize();
        // Grab path from CLI args, otherwise fallback to the default dataset
        String filePath = args.length > 0
                ? args[0]
                : "data/roads.csv";

        try {
            // Wire up the core components
            Graph graph = GraphFileLoader.loadGraph(filePath);
            RouteFinder routeFinder = new RouteFinder(graph);
            UserInterface userInterface =
                    new UserInterface(graph, routeFinder);

            AppLogger.info("CLI application started. Dataset: " + filePath);
            // Hand control over to the UI loop
            userInterface.start();

        } catch (InvalidPathException ex) {
            AppLogger.error("Invalid dataset path: " + filePath, ex);
            System.err.println("Invalid dataset path: " + filePath);

        } catch (IOException ex) {
            //Check if it's just missing to give a better error message
            AppLogger.error("Unable to load dataset: " + filePath, ex);

            if (!Files.exists(Paths.get(filePath))) {
                System.err.println(
                        "Dataset file not found: " + filePath
                );
            } else {
                System.err.println(
                        "Unable to load dataset: " + ex.getMessage()
                );
            }

        } catch (IllegalArgumentException ex) {
            AppLogger.error("Unable to start Taxi Route Optimizer.", ex);
            System.err.println(
                    "Unable to start Taxi Route Optimizer: "
                    + ex.getMessage()
            );
        }
    }
}
