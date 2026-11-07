# Taxi Route Optimizer

## 1. Project Overview

The Taxi Route Optimizer is a Java application that finds the shortest distance route between locations in a sample road network. It uses Dijkstra's shortest path algorithm to help identify efficient routes based on road distances. The system represents locations as vertices and roads as weighted, directed edges in a graph. It calculates the shortest available route and its total distance in kilometres.

The project includes both a command line interface (CLI) and a graphical user interface (GUI).
NB: The road distances are illustrative values for demonstration and testing. They are not verified real-world measurements.

## 2. Features

- Display all available locations.
- Calculate the shortest route between a starting location and a destination.
- Display the route and total distance in kilometres.
- Match location names without regard to letter case in the CLI.
- Validate menu selections and location names.
- Handle identical starting and destination locations with a distance of 0 km.
- Identify when no route exists between two locations.
- Provide an interactive graphical interface with route information.
- Load locations and road data from a CSV dataset in the command-line interface.
- Validate dataset records and road distances.
- Record application events, route calculations, warnings, and exit activity in a log file.

## 3. Technologies and Tools

- **Programming language:** Java
- **Algorithm:** Dijkstra's shortest-path algorithm
- **Data structures:** Java Collections Framework, including lists, maps, and a priority queue
- **Development environment:** Visual Studio Code
- **Data format:** Comma-Separated Values
- **Logging:** Java Util Logging
- **GUI:** Java Swing
- **Compatibility target:** Java 8

## 4. Project Structure

The `src` directory contains ten Java source files, three CSV datasets, and log files generated at runtime:

| File                       | Description                                                             |
|----------------------------|-------------------------------------------------------------------------|
| `Main.java`                | Loads and validates the CSV dataset, initializes the routing components,|
|                            | and starts the CLI.                                                     |
| `Location.java`            | Represents a location in the network.                                   |
| `Road.java`                | Represents a directed road and its distance.                            |
| `Graph.java`               | Stores locations and roads and provides graph operations.               |
| `Route.java`               | Represents a calculated path and its total distance.                    |
| `RouteFinder.java`         | Implements Dijkstra's algorithm to find the shortest route.             |
| `GraphFileLoader.java`     | Loads and validates locations and roads from a CSV file.                |
| `UserInterface.java`       | Provides the command line menu and handles user input.                  |
| `TaxiRouteGUI.java`        | Provides the graphical interface for route selection and results.       |
| `AppLogger.java`           | Configures file logging for application events.                         |
| `src/data/roads.csv`       | Contains the sample locations and directed road connections.            |
| `malformed-roads.csv`      | Used to test malformed CSV records.                                     |
| `invalid-roads.csv`        | used to test invalid road data.                                         |
| `src/logs/taxi-route.log`  | Stores application log entries when logging is enabled and initialized. |

The log file is created automatically when logging is initialized. Its location depends on the application's working directory.

## 5. System Requirements

- A Java Development Kit (JDK) that supports the `--release 8` compiler option.
- Visual Studio Code or another Java compatible development environment.
- Windows PowerShell or a compatible terminal.

Check your Java installation with:
java -version
javac -version

## 6. How to Compile and Run

Open a PowerShell terminal in the project directory.

### Run the command-line interface

Compile the source files:
cd src
javac --release 8 *.java

Start the CLI:
java Main

To specify a different dataset, provide its path relative to the current working directory:
java Main data/roads.csv

### Run the graphical interface

From the project root directory, compile the files if you have not already done so:
javac --release 8 src\*.java

Launch the GUI:
java -cp src TaxiRouteGUI

The GUI constructs its own sample road network in TaxiRouteGUI.java; it does not load src/data/roads.csv.

## 7. How to Use the Application

### Command-line interface

When the CLI starts, choose an option from the menu:

1. **View locations:** Display all available locations.
2. **Find shortest route:** Enter a starting location and destination to calculate the shortest available route.
3. **Exit:** Exit.

Enter location names from the displayed list. The CLI supports case insensitive location matching.

### Graphical User Interface

1. Launch the GUI using the command above.
2. Select a starting location from the dropdown or type it.
3. Select a destination from the dropdown or type it.
4. Calculate the route and review the route details and total distance.
5. Use the exit control to close the application.

The GUI provides default location selections when it opens.

## 8. Sample Network

The prototype contains five locations:

- Bellville
- Parow
- Goodwood
- Cape Town
- Atlantis

The sample road network uses directed roads with illustrative distances. Atlantis is disconnected from the other locations to support testing of unreachable routes.

## 9. Example Output

For a CLI search from Bellville to Cape Town:

Shortest route:
Bellville -> Parow -> Goodwood -> Cape Town (19.00 km)
Total distance: 19.00 km

For an unreachable destination:

No route exists between those locations.

For an unknown location:

Location not found. Check the names and try again.

For the same starting location and destination:

Bellville (0.00 km)
Total distance: 0.00 km

## 10. Logging

The application uses `AppLogger.java` to record significant events for troubleshooting and monitoring.

The log file is created at:
logs/taxi-route.log

The path is relative to the working directory from which the application is launched. For example, when running the CLI from src, the log is stored at src/logs/taxi-route.log.

Log entries include:

- Logging initialization and application startup.
- Successful route calculations.
- Warnings when no route exists.
- Application exit events.
- Errors encountered during application startup or dataset loading.

To inspect the log from the project root:
Get-Content .\src\logs\taxi-route.log -Tail 30

## 11. Testing and Evaluation

Manual tests have been performed on the prototype.

| Test scenario | Expected behaviour | Reported result |
|---|---|---|
| Bellville to Cape Town | Shortest route of 19.00 km | Passed |
| Bellville to Goodwood | Select the 7.00 km route via Parow rather than the direct 8.00 km road | Passed |
| Lowercase location names in CLI & GUI | Recognize valid names regardless of capitalization | Passed |
| Unknown location | Display an error without crashing | Passed |
| Invalid menu option | Display an error and return to the menu | Passed |
| Missing dataset | Display a meaningful error message | Passed |
| Invalid or malformed dataset | Reject invalid road data and report the problem | Passed |
| Same start and destination | Return a distance of 0.00 km | Passed |
| Atlantis to Cape Town | Report that no route exists | Passed |
| Application exit | Display the exit message and close as expected | Passed |
| Logging | Record startup, route, warning, and exit events | Passed |

These results are based on manual testing. Automated unit tests and more extensive performance measurements remain future improvements.

## 12. Current Limitations

- The CLI loads locations and road distances from `data/roads.csv`, while the GUI currently uses a hardcoded sample network.
- Road distances are illustrative and are not verified real world measurements.
- The prototype does not use live traffic information.
- The road network is directed, so a road in one direction does not automatically imply a road in the reverse direction.
- The current graph implementation scans stored roads when retrieving outgoing roads, which can limit performance on larger networks.
- The application has not yet been evaluated against live road network data.
- The GUI's "Available Locations" list only displays four of the five locations, Atlantis is cut off, though all locations remain usable by typing. Its "Location not found" dialog also has a minor grammar error "location were not found".
## 13. Future Improvements

- Integrate verified road-network data.
- Add live traffic and travel time estimates.
- Improve graph representation and route search performance.
- Add automated unit and integration tests.
- Extend the network and evaluate algorithm performance on larger datasets.
- Improve route reporting and accessibility in the GUI.

## 14. Conclusion

The Taxi Route Optimizer demonstrates object-oriented design, graph modelling, Java Collections Framework usage, shortest-path computation, input validation, graphical interaction, CSV data loading, and application logging. It provides a working prototype for finding shortest-distance routes in a small sample road network.