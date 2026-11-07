
import java.util.Scanner;

public class UserInterface {
    private final RouteFinder routeFinder;
    private final Graph graph;
    private final Scanner scanner;

    public UserInterface(Graph graph, RouteFinder routeFinder) {
        this.graph = graph;
        this.routeFinder = routeFinder;
        // Standard input reader for CLI interaction
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("================================");
        System.out.println("     TAXI ROUTE OPTIMIZER");
        System.out.println("================================");

        boolean running = true;
        // Main application loop
        while (running) {
            System.out.println("\n1. View locations");
            System.out.println("2. Find shortest route");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    showLocations();
                    break;
                case "2":
                    findRoute();
                    break;
                case "3":
                    running = false;
                    System.out.println("Thank you for using the Taxi Route Optimizer.");
                    AppLogger.info("User exited the CLI.");
                    break;
                default:
                    // Catch any typos or invalid menu number
                    System.out.println("Invalid choice. Please enter 1, 2 or 3.");
            }
        }
    }
   // Prints out all available nodes so the user knows what they can type
    private void showLocations() {
        System.out.println("\nAvailable locations:");
        for (Location location : graph.getLocations()) {
            System.out.println("- " + location.getName());
        }
    }

    private void findRoute() {
        // Show locations first so the user doesn't have to guess or memorize names
        showLocations();

        System.out.print("\nEnter starting location: ");
        String startName = scanner.nextLine().trim();

        System.out.print("Enter destination: ");
        String endName = scanner.nextLine().trim();
        // Look up the actual Location objects from the user's strings
        Location start = graph.findLocation(startName);
        Location end = graph.findLocation(endName);
       
        // Validate that both locations actually exist in the graph
        if (start == null || end == null) {
            System.out.println("Location not found. Check the names and try again.");
            return;
        }

       
Route route = routeFinder.bestRoute(start, end);

if (route == null) {
    System.out.println("No route exists between those locations.");
    AppLogger.warning(
            "No route found from " + start.getName()
            + " to " + end.getName() + "."
    );
} else {
     // Success! Print the path and the total distance
    System.out.println("\nShortest route:");
    System.out.println(route);
    System.out.printf("Total distance: %.2f km%n",
            route.getTotalDistance());

    AppLogger.info(
            "Shortest route found from " + start.getName()
            + " to " + end.getName()
            + ". Distance: "
            + route.getTotalDistance() + " km."
    );
}
    }
}