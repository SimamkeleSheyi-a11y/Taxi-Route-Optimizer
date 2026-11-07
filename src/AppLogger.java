
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class AppLogger {

    private static final Logger LOGGER =
    // Centralized logger instance for the entire application
        Logger.getLogger("TaxiRouteOptimizer");

    private static boolean initialized = false;

    private AppLogger() {
        // Prevent creating instances of this utility class.
    }
    public static synchronized void initialize() {
        if (initialized) {
            return; // Already configured, exit early
        }

        try {
            Path logDirectory = Paths.get("logs");
            Files.createDirectories(logDirectory);
            // 'true' enables append mode, so existing logs are not overwritten
            FileHandler handler = new FileHandler(
                logDirectory.resolve("taxi-route.log").toString(),
                true
            );

            handler.setFormatter(new SimpleFormatter());
            LOGGER.setUseParentHandlers(false);
            LOGGER.addHandler(handler);
            LOGGER.setLevel(Level.INFO);

            initialized = true;
            LOGGER.info("Logging system initialized.");

        } catch (IOException | SecurityException exception) {// Fail gracefully, if we can't write to a file, warn the user but 
            // allow the application to continue running without file logging.
            System.err.println(
                "Warning: Could not initialize file logging: "
                + exception.getMessage()
            );
        }
    }

    public static void info(String message) {
        LOGGER.info(message);
    }

    public static void warning(String message) {
        LOGGER.warning(message);
    }

    public static void error(String message, Throwable exception) {
        LOGGER.log(Level.SEVERE, message, exception);
    }
}
